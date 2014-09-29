package oecp.platform.role.web;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.annotation.Resource;

import oecp.framework.dao.QueryResult;
import oecp.framework.exception.BizException;
import oecp.framework.util.FastJsonUtils;
import oecp.framework.web.JsonResult;
import oecp.framework.web.ext.JsonTreeVO;
import oecp.framework.web.ext.JsonTreeVOBuilder;
import oecp.platform.org.eo.Organization;
import oecp.platform.org.service.OrgService;
import oecp.platform.permission.service.PermissionService;
import oecp.platform.role.eo.OrgRole;
import oecp.platform.role.eo.Role;
import oecp.platform.role.service.OrgRoleService;
import oecp.platform.role.service.RoleService;
import oecp.platform.role.vo.OrgRoleJsonTreeVO;
import oecp.platform.role.vo.RoleVO;
import oecp.platform.user.eo.User;
import oecp.platform.web.BasePlatformAction;

import org.apache.commons.lang.StringUtils;
import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.Namespace;
import org.apache.struts2.convention.annotation.ParentPackage;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;

import com.alibaba.fastjson.JSON;

/**
 * 角色管理Action
 * 
 * @author wangliang
 * @date 2011-5-20
 */
@Controller("roleAction")
@Scope("prototype")
@ParentPackage("default-package")
@Namespace("/role")
public class RoleAction extends BasePlatformAction {

	private static final long serialVersionUID = 1L;
	// 公司服务
	@Resource
	private OrgService orgService;
	// 公司角色服务
	@Resource
	private OrgRoleService orgRoleService;
	// 角色服务
	@Resource
	private RoleService roleService;
	// 权限服务
	@Resource
	private PermissionService permissionService;
	// 参数Id
	private String id;
	// 主键
	private Long pk;
	// 角色新增编辑参数
	private Role role;
	// 组织角色主键参数
	private String orgRoleId;
	// 删除角色主键参数
	private String[] ids;
	// 公司树中当前选择的公司
	private String orgId;
	// 组织角色参数
	private String orgRoles;
	// 角色委派保存参数
	private String[] usersJson;

	/**
	 * 获取当前公司和下级公司树
	 * 
	 * @author wangliang
	 * @return
	 * @throws BizException
	 */
	@Action(value = "orgsTree")
	@Transactional
	public String orgsTree() throws BizException {
		Organization org = orgService.find(this.getOnlineUser().getLoginedOrg()
				.getId());
		// EO转换为TreeVo
		List<JsonTreeVO> tree = JsonTreeVOBuilder.getTreeVOsFromEOs(Arrays
				.asList(org), "id", "name", "childOrgs",
				JsonTreeVOBuilder.CheckShow.NONE);
		String json = JSON.toJSONString(tree, true);
		setJsonString(json);
		return SUCCESS;
	}

	/**
	 * 获取下级公司树
	 * 
	 * @return
	 * @throws BizException
	 */
	@Action(value = "lowerOrgsTree")
	@Transactional
	public String lowerOrgsTree() throws BizException {
		if (!StringUtils.isEmpty(id)) {
			Organization org = orgService.find(id);
			// EO转换为TreeVo
			List<JsonTreeVO> tree = JsonTreeVOBuilder.getTreeVOsFromEOs(Arrays
					.asList(org), "id", "name", "childOrgs",
					JsonTreeVOBuilder.CheckShow.NOCHECK);
			String json = JSON.toJSONString(tree, true);
			// 添加复选框属性,追加选择属性和组织角色Id属性
			// TODO 使用替换容易出现问题。
			json = json.replaceAll("\"id\":",
					"\"orgRoleId\":\"\",\"id\":");
			this.setJsonString(json);
		}
		return SUCCESS;
	}

	/**
	 * 获取拥有角色的组织
	 * 
	 * @return
	 * @throws BizException
	 */
	@Action(value = "getHasRoleOrgs")
	@Transactional
	public String getHasRoleOrgs() throws BizException {
		JsonResult jr = new JsonResult(false, "");
		if (!StringUtils.isEmpty(id) && !StringUtils.isEmpty(orgId)) {
			// 获取当前选择公司和所有下级公司
			Organization org = orgService.find(orgId);
			List<String> orgIds = getChildOrg(org);
			String[] orgWhere = new String[orgIds.size()];
			orgWhere = orgIds.toArray(orgWhere);
			// 查询已经存在的公司角色
			List<OrgRole> orgRoles = orgRoleService
					.getUserOrgRoleByRoleIdOrgId(id, orgWhere);
			List<OrgRoleJsonTreeVO> checkedTree = new ArrayList<OrgRoleJsonTreeVO>();
			// 拼装已有角色权限的的组织属性
			for (OrgRole orgRole : orgRoles) {
				OrgRoleJsonTreeVO v = new OrgRoleJsonTreeVO();
				v.setOrgRoleId(orgRole.getId());
				v.setChecked(true);
				v.setId(orgRole.getOrg().getId());
				checkedTree.add(v);
			}
			if (checkedTree != null && checkedTree.size() > 0) {
				jr.setSuccess(true);
				jr.setResult(checkedTree);
				jr.setContainFields(new String[] { "id", "checked",
								"orgRoleId" });
			}
		}
		setJsonString(jr.toJSONString());
		return SUCCESS;
	}

	/**
	 * 递归所有公司Id
	 * 
	 * @param org
	 * @return
	 */
	private List<String> getChildOrg(Organization org) {
		List<String> list = new ArrayList<String>();
		list.add(org.getId());
		if (org.getChildOrgs() != null && org.getChildOrgs().size() > 0) {
			for (Organization o : org.getChildOrgs()) {
				list.addAll(getChildOrg(o));
			}
		}
		return list;
	}

	/**
	 * 根据公司Id获取公司创建的和上级由公司分配的角色列表
	 * 
	 * @author wangliang
	 * @return
	 * @throws BizException
	 */
	@Action(value = "queryRoleByOrgId")
	@Transactional
	public String queryRoleByOrgId() throws BizException {
		if (!StringUtils.isEmpty(id)) {
			// 获取当前公司创建和上级公司分配的角色
			QueryResult<Role> rs = roleService.getAllOrgRole(this
					.getOnlineUser().getLoginedOrg().getId(), id, start, limit);
			setRoleSuccessJson(rs, id);
		}
		return SUCCESS;
	}

	/**
	 * 保存角色
	 * 
	 * @author wangliang
	 * @return
	 * @throws BizException
	 */
	@Action(value = "saveRole")
	public String saveRole() throws BizException {
		try {
			doSaveRole();
			setJsonString("{success:true,msg:'保存成功！'}");
		} catch (Exception ex) {
			ex.printStackTrace();
			setJsonString("{success:false,msg:'保存失败，请联系管理员！'}");
		}
		return SUCCESS;
	}

	/**
	 * 保存组织角色
	 * 
	 * @author wangliang
	 * @return
	 * @throws BizException
	 */
	@Action(value = "saveOrgRole")
	public String saveOrgRole() throws BizException {
		JsonResult jr = new JsonResult(true, "更新成功！");
		if (!StringUtils.isEmpty(orgRoles) && !StringUtils.isEmpty(id)) {
			List<OrgRoleJsonTreeVO> trees = JSON.parseArray(orgRoles,
					OrgRoleJsonTreeVO.class);
			updateOrgRole(trees);
		} else {
			jr.setSuccess(false);
			jr.setMsg("更新失败，请联系管理员！");
		}
		setJsonString(jr.toJSONString());
		return SUCCESS;
	}

	/**
	 * 删除角色
	 * 
	 * @author wangliang
	 * @return
	 * @throws BizException
	 */
	@Action(value = "delRole")
	public String delRole() throws BizException {
		if (ids != null && ids.length > 0) {
			roleService.delete(ids);
			setJsonString("{success:true,msg:'删除成功！'}");
		}else{
			setJsonString("{success:false,msg:'请选择一条记录'}");
		}
		return SUCCESS;
	}

	/**
	 * 获取当前公司及子公司json
	 * 
	 * @return
	 * @throws BizException
	 */
	@Action(value = "getOrgTree")
	@Transactional
	public String getOrgTree() throws BizException {
		if (!StringUtils.isEmpty(id)) {
			// 获取本公司以及下属公司tree
			Organization org = orgService.getChildOrgs(this.getOnlineUser()
					.getLoginedOrg().getId());
			List<JsonTreeVO> treevos = JsonTreeVOBuilder.getTreeVOsFromEOs(
					Arrays.asList(org), "id", "name", "childOrgs",
					JsonTreeVOBuilder.CheckShow.NONE);
			String json = JSON.toJSONString(treevos, true);
			setJsonString(json);
		}
		return SUCCESS;
	}

	/**
	 * 通过角色Id获取已经分配的公司
	 * 
	 * @return
	 * @throws BizException
	 */
	@Action(value = "getAssignedOrgByRoleId")
	public String getAssignedOrgByRoleId() throws BizException {
		if (!StringUtils.isEmpty(id)) {
			// 获取当前角色
			List<OrgRole> orgRoles = orgRoleService.getUserOrgRoleByRoleId(id);
			if (orgRoles != null && orgRoles.size() > 0) {
				List<Organization> orgs = new ArrayList<Organization>();
				for (OrgRole orgRole : orgRoles) {
					orgs.add(orgRole.getOrg());
				}
				String json = FastJsonUtils.toJson(orgs, new String[] { "id" });
				setJsonString(json);
			}
		}
		return SUCCESS;
	}

	/**
	 * 获取当前公司里拥有某角色的所用用户
	 * 
	 * @return
	 */
	@Action(value = "getHasRoleUsers")
	public String getHasRoleUsers() {
		List<User> users = orgRoleService.getHasRoleUsers(this.getOnlineUser()
				.getLoginedOrg().getId(), id);
		JsonResult jr = new JsonResult(users);
		jr.setContainFields(new String[] { "id", "name" });
		String json = jr.toJSONString();
		setJsonString(json);
		return SUCCESS;
	}

	/**
	 * 获取当前公司里没有某角色的所用用户
	 * 
	 * @return
	 */
	@Action(value = "getNotHasRoleUsers")
	public String getNotHasRoleUsers() {
		List<User> users = orgRoleService.getNotHasRoleUsers(this
				.getOnlineUser().getLoginedOrg().getId(), id);
		JsonResult jr = new JsonResult(users);
		jr.setContainFields(new String[] { "id", "name" });
		String json = jr.toJSONString();
		setJsonString(json);
		return SUCCESS;
	}

	/**
	 * 委派角色 保存
	 * 
	 * @return
	 * @throws BizException
	 */
	@Action(value = "saveUsersRole")
	public String saveUsersRole() throws BizException {
		orgRoleService.saveOrgRoleByUserIds(getOnlineUser().getLoginedOrg()
				.getId(), orgRoleId, usersJson);
		JsonResult jr = new JsonResult(true, "保存成功!");
		setJsonString(jr.toJSONString());
		return SUCCESS;
	}

	/**
	 * 更新组织角色
	 * 
	 * @param orgRoleTreeVo
	 * @throws BizException
	 */
	private void updateOrgRole(List<OrgRoleJsonTreeVO> orgRoleTreeList)
			throws BizException {
		boolean checked = false;
		String orgRoleId = "";
		String orgPK = "";
		for (OrgRoleJsonTreeVO treevo : orgRoleTreeList) {
			checked = treevo.getChecked();
			orgRoleId = treevo.getOrgRoleId();
			orgPK = treevo.getId();
			if (StringUtils.isEmpty(orgRoleId) && checked
					&& !StringUtils.isEmpty(orgPK)) {
				// 新增
				OrgRole orgRole = new OrgRole();
				Role role = new Role();// 角色
				role.setId(id);
				orgRole.setRole(role);
				Organization org = new Organization();// 公司
				org.setId(orgPK);
				orgRole.setOrg(org);
				orgRoleService.save(orgRole);
			} else if (!StringUtils.isEmpty(orgRoleId) && !checked
					&& !StringUtils.isEmpty(orgPK)) {
				// 删除
				orgRoleService.delete(orgRoleId);
			}
		}
	}

	/**
	 * 保存角色
	 * 
	 * @author wangliang
	 * @throws BizException
	 */
	private void doSaveRole() throws BizException {
		Organization org = new Organization();// 获取当前登录公司
		org.setId(this.getOnlineUser().getLoginedOrg().getId());
		role.setOrg(org);
		roleService.save(role);// 保存角色
		OrgRole orgRole = null;// 关联组织角色
		if (!StringUtils.isEmpty(orgRoleId)) {
			orgRole=this.orgRoleService.find(orgRoleId);
		}else{
			orgRole=new OrgRole();
		}
		orgRole.setRole(role);
		orgRole.setLocked(false);
		orgRole.setOrg(org);
		orgRoleService.save(orgRole);
	}

	/**
	 * 装配Json返回值
	 * 
	 * @param rs
	 * @return
	 */
	private void setRoleSuccessJson(QueryResult<Role> rs, String orgId) {
		if (rs.getResultlist() != null && rs.getResultlist().size() > 0) {
			List<RoleVO> vos = orgRoleEO2RoleEO(rs.getResultlist(), orgId);
			// JsonTree 转换
			String resultJson = JSON.toJSONString(vos, true);
			StringBuffer json = new StringBuffer();
			json.append("{success:true");
			json.append(",totalCounts:");
			json.append(rs.getTotalrecord());
			json.append(",result:");
			json.append(resultJson);
			json.append("}");
			setJsonString(json.toString());
		} else {
			setJsonString("{success:true,totalCounts:0,result:[]}");
		}
	}

	/**
	 * 组织角色列表获取角色
	 * 
	 * @param from
	 * @param orgId
	 * @return
	 */
	private List<RoleVO> orgRoleEO2RoleEO(List<Role> from, String orgId) {
		ArrayList<RoleVO> list = new ArrayList<RoleVO>();
		for (int i = 0; i < from.size(); i++) {
			Role eo = from.get(i);
			RoleVO vo = new RoleVO();
			vo.setId(eo.getId());
			vo.setCode(eo.getCode());
			vo.setName(eo.getName());
			vo.setLocked(eo.getLocked());
			vo.setCreateOrgName(eo.getOrg().getName());
			vo.setIsEdit(orgId.equals(eo.getOrg().getId()) ? true : false);
			for (int j = 0; j < eo.getOrgRoles().size(); j++) {
				if (orgId.equals(eo.getOrgRoles().get(j).getOrg().getId())) {
					vo.setOrgRoleId(eo.getOrgRoles().get(j).getId());
					break;
				}
			}
			list.add(vo);
		}
		return list;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public Long getPk() {
		return pk;
	}

	public void setPk(Long pk) {
		this.pk = pk;
	}

	public Role getRole() {
		return role;
	}

	public void setRole(Role role) {
		this.role = role;
	}

	public String getOrgRoleId() {
		return orgRoleId;
	}

	public void setOrgRoleId(String orgRoleId) {
		this.orgRoleId = orgRoleId;
	}

	public String[] getIds() {
		return ids;
	}

	public void setIds(String[] ids) {
		this.ids = ids;
	}

	public String getOrgId() {
		return orgId;
	}

	public void setOrgId(String orgId) {
		this.orgId = orgId;
	}

	public String getOrgRoles() {
		return orgRoles;
	}

	public void setOrgRoles(String orgRoles) {
		this.orgRoles = orgRoles;
	}

	public String[] getUsersJson() {
		return usersJson;
	}

	public void setUsersJson(String[] usersJson) {
		this.usersJson = usersJson;
	}

	public void setOrgService(OrgService orgService) {
		this.orgService = orgService;
	}

	public void setOrgRoleService(OrgRoleService orgRoleService) {
		this.orgRoleService = orgRoleService;
	}

	public void setRoleService(RoleService roleService) {
		this.roleService = roleService;
	}

	public void setPermissionService(PermissionService permissionService) {
		this.permissionService = permissionService;
	}

}
