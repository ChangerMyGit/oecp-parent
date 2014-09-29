/*
 * Copyright (c) 2011 OECP All Rights Reserved.                	
 * <a href="http://www.oecp.cn">http://www.oecp.cn</a>                                                                 
 */

package oecp.platform.user.web;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;

import oecp.framework.dao.QueryCondition;
import oecp.framework.dao.QueryResult;
import oecp.framework.exception.BizException;
import oecp.framework.web.JsonResult;
import oecp.platform.bpm.enums.ExceptionMsgType;
import oecp.platform.org.eo.Organization;
import oecp.platform.org.eo.Person;
import oecp.platform.org.service.PersonService;
import oecp.platform.role.eo.OrgRole;
import oecp.platform.role.service.OrgRoleService;
import oecp.platform.user.eo.User;
import oecp.platform.user.service.UserService;
import oecp.platform.web.BasePlatformAction;

import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.Namespace;
import org.apache.struts2.convention.annotation.ParentPackage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;

import com.alibaba.fastjson.JSON;

/**
 * 用户管理action
 * 
 * @author yongtree
 * @date 2011-5-25 下午03:13:00
 * @version 1.0
 */
@Controller
@Scope("prototype")
@ParentPackage("default-package")
@Namespace("/user")
public class UserAction extends BasePlatformAction {

	private static final long serialVersionUID = 1L;

	private String orgId;

	private User user;

	private String id;

	List<QueryCondition> conditions = new ArrayList<QueryCondition>();

	@Autowired
	private UserService userService;
	@Autowired
	private PersonService personService;
	
	private OrgRoleService orgRoleService;

	private String selectedRoles;
	
	private String resetPWD;

	@Action(value = "list")
	public String list() {
		//用户管理里面是否选择所属机构
		boolean isSelectedOrg = false;
		if (StringUtils.isEmpty(orgId)) {
			orgId = getOnlineUser().getLoginedOrg().getId();
		}else{
			isSelectedOrg = true;
		}

		// 判断要查询的公司是否拥有权限，如果没有权限，则默认还是自己所在的公司

		QueryResult<User> qr = this.userService.getUsersByOrgID(orgId,isSelectedOrg,
				conditions, start, limit, getOrderBy());
		JsonResult jr = new JsonResult(qr.getTotalrecord().intValue(), qr
				.getResultlist());
		jr.setContainFields(new String[] { "id", "loginId", "name",
				"createTime", "lastLoginTime", "email", "personId", "state","createdByOrg.name" });
		this.setJsonString(jr.toJSONString());
		return SUCCESS;
	}

	@Action(value = "find")
	public String getUserById() {
		try {
			this.user = this.userService.find(id);
			JsonResult jr = new JsonResult(this.user);
			String personStr="";
			if (StringUtils.isNotEmpty(this.user.getPersonId())) {
				Person person = personService.find(this.user.getPersonId());
				if (person != null)
					personStr = "\"personName\":\"" + person.getName() + "\",";
			}
			jr.setContainFields(new String[] { "id", "loginId", "name","createTime", "lastLoginTime", "email", "personId",	"state", "password"});
			String json = jr.toJSONString();
			if(StringUtils.isNotEmpty(personStr)){
				json = json.replace("\"personId\"", personStr+"\"personId\"");
			}
			this.setJsonString(json);
		} catch (BizException e) {
			returnErrorMsg(e.getMessage());
		}
		return SUCCESS;
	}

	@Action(value = "selectableRoles")
	public String getSelectableRoles() {
		List<OrgRole> roles = orgRoleService.getAllOrgRole(getOrgId());
		if (StringUtils.isNotEmpty(id)) {
			List<OrgRole> userRoles = this.orgRoleService.getUserOrgRole(id,
					getOrgId());
			roles.removeAll(userRoles);
		}
		List<String[]> list = new ArrayList<String[]>();
		for (OrgRole or : roles) {
			String[] arr = new String[2];
			arr[0] = or.getId();
			arr[1] = or.getRole().getName() + "[属于：" + or.getOrg().getName()
					+ "]";
			list.add(arr);
		}
		this.setJsonString(JSON.toJSONString(list));
		return SUCCESS;
	}

	@Action(value = "selectedRoles")
	public String getSelectedRoles() {
		List<String[]> list = new ArrayList<String[]>();
		if (!StringUtils.isEmpty(id)) {
			List<OrgRole> roles = this.orgRoleService.getUserOrgRole(id,
					getOrgId());
			for (OrgRole or : roles) {
				String[] arr = new String[2];
				arr[0] = or.getId();
				arr[1] = or.getRole().getName() + "[属于："
						+ or.getOrg().getName() + "]";
				list.add(arr);
			}
		}
		this.setJsonString(JSON.toJSONString(list));
		return SUCCESS;
	}

	@Action(value = "create")
	public String create() {
		try {
			// 检查loginId是否存在
			user.setPassword(DigestUtils.md5Hex(user.getPassword()));
			user.setCreatedByOrg(getOnlineUser().getLoginedOrg());
			this.userService.createUserAndAssignRoles(user, selectedRoles.split(","));
			JsonResult jr = new JsonResult(null);
			setJsonString(jr.toJSONString());
		} catch (Exception e) {
			returnErrorMsg(e.getMessage());
		}
		return SUCCESS;
	}

	@Action(value = "save")
	@Transactional
	public String save() {
		try {
			User oldUser = userService.find(user.getId());
			oldUser.copyAttributeValue(this.user, new String[] { "name","email", "loginId", "personId", "state" });
			this.userService.updateUserAndAssignRoles(oldUser, getOnlineUser().getLoginedOrg(), selectedRoles.split(","));
			setJsonString("{success:true,msg:'保存成功！'}");
		} catch (Exception e) {
			returnErrorMsg(e.getMessage());
		}
		return SUCCESS;
	}

	@Action(value = "assignRoles")
	@Transactional
	public String assignRoles() {
		try {
			User user = userService.find(this.user.getId());
			this.userService.assignRoles(user, getOnlineUser().getLoginedOrg(),
					selectedRoles.split(","));
			setJsonString("{success:true,msg:'保存成功！'}");
		} catch (BizException e) {
			returnErrorMsg(e.getMessage());
		}
		return SUCCESS;
	}

	@Action(value = "resetPWD")
	public String resetPwd() {
		try {
			this.setJsonString(new JsonResult(userService.resetPWD(id,resetPWD))
					.toJSONString());
		} catch (Exception e) {
			returnErrorMsg(e.getMessage());
		}
		return SUCCESS;
	}
	
	@Action(value = "deleteUser")
	public String deleteUser() {
		try {
			userService.delete(id);
			setJsonString("{success:true,msg:'删除成功！'}");
		} catch (Exception e) {
			returnErrorMsg("用户已经启用，无法删除!");
		}
		return SUCCESS;
	}

	@Action(value = "updatePWD")
	public String updatePWD() {
		String oldPassWord = this.getRequest().getParameter("oldPassWord");
		String newPassWord1 = this.getRequest().getParameter("newPassWord1");
		System.out.println(DigestUtils.md5Hex(oldPassWord));
		System.out.println(this.getOnlineUser().getUser().getPassword());
		try {
			this.userService.updatePassWord(this.getOnlineUser().getUser(), oldPassWord, newPassWord1);
				
		} catch (Exception e) {
			e.printStackTrace();
			handExcetionMsg(this.getResponse(),e.getMessage());
		}
		return SUCCESS;
	}
	/**
	 * 获取用户所属公司
	 * @author wangliang
	 * @date 2012-5-31下午4:36:42
	 * @return
	 */
	@Action(value="getOrgs4User")
	public String getOrgs4User(){
		JsonResult jr = new JsonResult("");
		List<Organization> orgs=this.userService.getOrganizationsToUser(id);
		if(orgs != null && orgs.size()>0){
			List<Map<String, String>> data = new ArrayList<Map<String, String>>();
			for (Organization o : orgs) {
				Map<String, String> org = new HashMap<String, String>();
				org.put("id", o.getId());
				org.put("code", o.getCode());
				org.put("name", o.getName());
				data.add(org);
			}
			jr.setResult(data);
		}
		this.setJsonString(jr.toJSONString());
		return SUCCESS;
	}
	/**
	 * 
	 * 处理业务异常信息,返回到页面
	 * @author yangtao
	 * @date 2011-8-19上午11:10:55
	 * @param response
	 * @param message
	 */
	private void handExcetionMsg(HttpServletResponse response,String message){
		if(StringUtils.isEmpty(message))
			message = ExceptionMsgType.EXECUTE_FAILURE;
		try {
			response.setCharacterEncoding("utf-8");
			response.setContentType("text/xml;charset=utf-8");
			PrintWriter out = response.getWriter();
			out.println(message);
			response.setStatus(HttpServletResponse.SC_EXPECTATION_FAILED);
			out.flush();
			out.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public void setSelectedRoles(String selectedRoles) {
		this.selectedRoles = selectedRoles;
	}

	public void setOrgRoleService(OrgRoleService orgRoleService) {
		this.orgRoleService = orgRoleService;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public void setUserService(UserService userService) {
		this.userService = userService;
	}

	public List<QueryCondition> getConditions() {
		return conditions;
	}

	public void setConditions(List<QueryCondition> conditions) {
		this.conditions = conditions;
	}

	public String getOrgId() {
		if (StringUtils.isEmpty(orgId))
			orgId = getOnlineUser().getLoginedOrg().getId();
		return orgId;
	}

	public void setOrgId(String orgId) {
		this.orgId = orgId;
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	public String getResetPWD() {
		return resetPWD;
	}

	public void setResetPWD(String resetPWD) {
		this.resetPWD = resetPWD;
	}

	public void setPersonService(PersonService personService) {
		this.personService = personService;
	}
}
