package oecp.platform.org.web;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;

import oecp.framework.exception.BizException;
import oecp.framework.util.FastJsonUtils;
import oecp.framework.web.ext.JsonTreeVO;
import oecp.framework.web.ext.JsonTreeVOBuilder;
import oecp.platform.bcfunction.eo.Function;
import oecp.platform.org.eo.Department;
import oecp.platform.org.eo.Organization;
import oecp.platform.org.service.DeptService;
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
 * 组织部门管理Action
 * 
 * @author liujingtao
 * @date 2011 6 13 15:10:23
 * @version 1.0
 */
@Controller("DeptManageAction")
@Scope("prototype")
@ParentPackage("default-package")
@Namespace("/dept")
public class DeptManageAction extends BasePlatformAction {
	private static final long serialVersionUID = 1L;
	@Resource
	private DeptService deptService;

	private Department dept;

	private String orgid;
	/**
	 * 部门树
	 * 
	 * @author liujingtao
	 * @date 2011 6 13 15:43:24
	 * @return
	 */
	@Action("depttree")
	@Transactional
	public String depttree() {
		if(StringUtils.isEmpty(orgid)){
			orgid = this.getOnlineUser().getLoginedOrg().getId();
		}
		List<Department> depts = deptService.getTopDepts(orgid);
		List<JsonTreeVO> orgvos = JsonTreeVOBuilder.getTreeVOsFromEOs(depts, "id", "name", "childDepts",JsonTreeVOBuilder.CheckShow.NONE);
		String json = JSON.toJSONString(orgvos, true);
		super.setJsonString(json);
		return SUCCESS;
	}

	/**
	 * 部门详细信息
	 * 
	 * @author liujingtao
	 * @date 2011 6 15 14:33:34
	 * @modifyNote
	 * @return
	 * @throws BizException
	 */
	@Action("deptInfo")
	public String deptInfo() throws BizException {
		String deptid = getRequest().getParameter("deptid");
		if (!StringUtils.isEmpty(deptid)) {
			Department dept = deptService.find(deptid);
			if(dept!=null){
				if (dept.getParent() == null) {
					dept.setParent(new Department());
				}
				String json = FastJsonUtils.toJson(dept, new String[] { "id",
						"name", "code","parent" });
				setJsonString(json);
			}
		}
		return SUCCESS;
	}

	/**
	 * 部门添加或修改
	 * 
	 * @author liujingtao
	 * @date 2011 6 15 10:09:55
	 * @return
	 * @throws BizException
	 */
	@Action("saveDept")
	@Transactional
	public String saveDept() throws BizException {
		try {
			if (dept != null) {
				Organization org = new Organization();
				org.setId(this.getOnlineUser().getLoginedOrg().getId());
				dept.setOrg(org);
				if (StringUtils.isEmpty(dept.getId())) {
					dept.setId(null);
				}
				if (dept.getParent()!=null && StringUtils.isEmpty(dept.getParent().getId())) {
					dept.setParent(null);
				}
				deptService.save(dept);
			}

			setJsonString("{success:true,msg:'保存成功！'}");
		} catch (Exception ex) {
			ex.printStackTrace();
			setJsonString("{success:false,msg:'保存失败，请联系管理员！'}");
		}
		return SUCCESS;
	}

	/**
	 * 删除部门信息
	 * 
	 * @author liujingtao
	 * @date 2011 6 16 11:29:26
	 * @return
	 */
	@Action("deleteDept")
	public String deleteDept() {
		try {
			String deptid = getRequest().getParameter("deptid");
			deptService.delete(deptid);
			setJsonString("{success : true , msg : '删除成功'}");
		} catch (Exception e) {
			setJsonString("{success : false , msg : '部门在使用，无法删除！'}");
		}
		return SUCCESS;
	}

	private List<JsonTreeVO> eo2vo(List<Department> depts, Department parent) {
		List<JsonTreeVO> treeList = new ArrayList<JsonTreeVO>();
		for (Department dept : depts) {
			if (dept.getParent() == parent) {
				JsonTreeVO vo = new JsonTreeVO();
				vo.setId(String.valueOf(dept.getId()));
				vo.setText(dept.getName());
				vo.setLeaf(false);
				vo.setChildren(eo2vo(depts, dept));
				treeList.add(vo);
			}
		}
		return treeList;
	}

	public DeptService getDeptService() {
		return deptService;
	}

	public void setDeptService(DeptService deptService) {
		this.deptService = deptService;
	}

	public Department getDept() {
		return dept;
	}

	public void setDept(Department dept) {
		this.dept = dept;
	}

	public String getOrgid() {
		return orgid;
	}

	public void setOrgid(String orgid) {
		this.orgid = orgid;
	}

}
