package oecp.platform.permission.web;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;

import oecp.framework.exception.BizException;
import oecp.framework.web.ext.JsonTreeVO;
import oecp.framework.web.ext.JsonCheckboxTreeVO;
import oecp.platform.bcfunction.eo.Function;
import oecp.platform.bcfunction.eo.FunctionUI;
import oecp.platform.bcfunction.eo.UIElement;
import oecp.platform.bcfunction.service.BcFunctionService;
import oecp.platform.bcfunction.service.FunctionUIService;
import oecp.platform.bcfunction.service.UIElementService;
import oecp.platform.bcinfo.eo.BizComponent;
import oecp.platform.org.eo.OrgUseBC;
import oecp.platform.org.service.OrgService;
import oecp.platform.permission.eo.Permission;
import oecp.platform.permission.eo.PermissionFuncUI;
import oecp.platform.permission.eo.PermissionUIElement;
import oecp.platform.permission.service.PermissionService;
import oecp.platform.role.eo.OrgRole;
import oecp.platform.role.eo.Role;
import oecp.platform.role.service.OrgRoleService;
import oecp.platform.role.service.RoleService;
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
 * 角色功能Action
 * 
 * @author liujingtao
 * 
 */
@Controller("PermissionManagerAction")
@Scope("prototype")
@ParentPackage("default-package")
@Namespace("/permission")
public class PermissionManagerAction extends BasePlatformAction {
	private static final long	serialVersionUID	= -7457053056764832876L;

	@Resource
	private RoleService			roleService;

	@Resource
	private OrgRoleService		orgRoleService;

	@Resource
	private PermissionService	permissionService;

	@Resource
	private OrgService			orgService;

	@Resource
	private BcFunctionService	bcFunctionService;

	@Resource
	private FunctionUIService	functionUIService;

	@Resource
	private UIElementService	uiElementService;

	// 登录用户id
	private String				userId;
	// 组织角色id
	private String				orgRoleId;
	// 更新后的permissionJson
	private List<Permission>	permissions;

	/**
	 * 获取角色树
	 * 
	 * @return Json树形结构
	 */
	@Transactional
	@Action(value = "roleTreeCode")
	public String roleTreeCode() throws IOException, BizException {
		String orgId = this.getOnlineUser().getLoginedOrg().getId();
		if (!StringUtils.isEmpty(orgId)) {
			// 获取角色列表
			List<Role> roleList = roleService.getRolesByOrgId(orgId);
			List<JsonTreeVO> treevos = new ArrayList<JsonTreeVO>();
			for (Role role : roleList) {
				JsonTreeVO vo = new JsonTreeVO();
				vo.setId("role" + String.valueOf(role.getId()));
				vo.setText(role.getName());
				vo.setLeaf(false);
				List<OrgRole> orgRoleList = orgRoleService.getUserOrgRoleByRoleId(role.getId());
				List<JsonTreeVO> treevosChild = new ArrayList<JsonTreeVO>();
				for (OrgRole orgRole : orgRoleList) {
					JsonTreeVO voChild = new JsonTreeVO();
					voChild.setId(orgRole.getId());
					voChild.setText(orgRole.getOrg().getName());
					voChild.setLeaf(true);
					treevosChild.add(voChild);
				}
				vo.setChildren(treevosChild);
				treevos.add(vo);
			}
			// 转换为Extjs树形结构Json
			setJsonString(JSON.toJSONString(treevos));
		}
		return SUCCESS;
	}

	/**
	 * 查询组织角色功能权限树
	 * 
	 * @return Json树形结构
	 */
	@Transactional
	@Action(value = "permissionTreeCode")
	public String permissionTreeCode() throws IOException, BizException {
		String orgId = null;
		if (StringUtils.isEmpty(orgRoleId)) {
			orgId = this.getOnlineUser().getLoginedOrg().getId();
		} else {
			OrgRole orgRole = orgRoleService.find_full(orgRoleId);
			orgId = orgRole.getOrg().getId();
		}
		List<OrgUseBC> orgbcList = orgService.getUsedBCs(orgId);
		List<JsonCheckboxTreeVO> treevos = new ArrayList<JsonCheckboxTreeVO>();
		// 遍历组织包含的组件
		for (OrgUseBC orgbc : orgbcList) {
			JsonCheckboxTreeVO vo_bc = new JsonCheckboxTreeVO();
			BizComponent bc = orgbc.getBc();
			vo_bc.setId("bc_" + bc.getId());
			vo_bc.setText(bc.getName());
			vo_bc.setType("bc");
			vo_bc.setLeaf(false);
			vo_bc.setChecked(false);
			List<Function> funList = bcFunctionService.getRootFunctions(bc.getId());
			List<Permission> permissionList = permissionService.getRolePermission(orgRoleId);
			List<JsonCheckboxTreeVO> treevos_fun = new ArrayList<JsonCheckboxTreeVO>();
			// 遍历组件包含的功能
			// 由于功能可能包含子功能，所以使用递归方法遍历funCheck()
			for (Function fun : funList) {
				treevos_fun.add(funCheck(fun, permissionList, null));
			}
			vo_bc.setChildren(treevos_fun);
			treevos.add(vo_bc);
		}

		// 转换为Extjs树形结构Json
		setJsonString(JSON.toJSONString(treevos));

		return SUCCESS;
	}

	/**
	 * 修改组织角色功能权限
	 * 
	 * @return Json树形结构
	 */
	@Transactional
	@Action(value = "updateRolePermission")
	public String updateRolePermission() throws IOException, BizException, CloneNotSupportedException {
		trimPermissionList();
		permissionService.updateRolePermission(orgRoleId, permissions);
		setJsonString("{success : true , msg : '保存成功'}");
		return SUCCESS;
	}

	private void trimPermissionList() {
		if (this.permissions != null) {
			for (int i = 0; i < permissions.size(); i++) {
				Permission p = permissions.get(i);
				List<PermissionFuncUI> uis = p.getPermissionFuncUIs();
				if (uis != null) {
					for (int j = 0; uis != null && j < uis.size(); j++) {
						PermissionFuncUI ui = uis.get(j);
						ui.setPermission(p);
						List<PermissionUIElement> eles = ui.getPermissionUIElements();
						if (eles != null) {
							for (int k = 0; k < eles.size(); k++) {
								eles.get(k).setPermissionFuncUI(ui);
							}
						}
					}
				}
			}
		}
	}

	/**
	 * 对fun递归循环
	 * 
	 * @param fun
	 *            当前功能
	 * @param permissionList
	 *            功能权限列表
	 * @param parentFun
	 *            当前功能的父功能
	 * 
	 *            当ui界面checked=false时，默认ele元素checked=false
	 *            当ui界面checked=true时，ele元素默认checked=true，
	 *            当禁用元素列表里存在时，checked=false
	 */
	private JsonCheckboxTreeVO funCheck(Function fun, List<Permission> permissionList, Function parentFun) {
		JsonCheckboxTreeVO vo_fun = new JsonCheckboxTreeVO();
		vo_fun.setId("fun_" + fun.getId());
		vo_fun.setText(fun.getName());
		vo_fun.setType("fun");
		vo_fun.setLeaf(false);
		vo_fun.setChecked(false);
		Permission permissionTmp = null;
		for (Permission permission : permissionList) {
			if (permission.getFunction().getId().equals(fun.getId())) {
				vo_fun.setChecked(true);
				permissionTmp = permission;
				break;
			}
		}
		if (fun.getParent() == parentFun && (fun.getRunable() == false)) {
			List<Function> funChildList = fun.getChildren();
			List<JsonCheckboxTreeVO> treevos_funChild = new ArrayList<JsonCheckboxTreeVO>();
			for (Function funChild : funChildList) {
				treevos_funChild.add(funCheck(funChild, permissionList, fun));
			}
			vo_fun.setChildren(treevos_funChild);
		} else if (fun.getParent() == parentFun && (fun.getRunable() == true)) {
			List<FunctionUI> funUIList = functionUIService.getFunctionUIs(fun.getId());
			List<PermissionFuncUI> pfUIList = null;
			if (permissionTmp != null) {
				pfUIList = permissionTmp.getPermissionFuncUIs();
			}
			List<JsonCheckboxTreeVO> treevos_funUI = new ArrayList<JsonCheckboxTreeVO>();
			for (FunctionUI funUI : funUIList) {
				JsonCheckboxTreeVO vo_funUI = new JsonCheckboxTreeVO();
				vo_funUI.setId("fui_" + funUI.getId());
				vo_funUI.setText(funUI.getName());
				vo_funUI.setType("ui");
				vo_funUI.setLeaf(false);
				vo_funUI.setChecked(false);
				PermissionFuncUI pfUITmp = null;
				if (permissionTmp != null) {
					for (PermissionFuncUI pfUI : pfUIList) {
						if (pfUI.getFunctionUI().getId().equals(funUI.getId())) {
							vo_funUI.setChecked(true);
							pfUITmp = pfUI;
							break;
						}
					}
				}
				List<UIElement> uiEleList = uiElementService.getElements(funUI.getId());
				List<PermissionUIElement> puiElList = null;
				if (pfUITmp != null) {
					puiElList = pfUITmp.getPermissionUIElements();
				}
				List<JsonCheckboxTreeVO> treevos_uiEle = new ArrayList<JsonCheckboxTreeVO>();
				for (UIElement uiEle : uiEleList) {
					JsonCheckboxTreeVO vo_uiEle = new JsonCheckboxTreeVO();
					vo_uiEle.setId("ele_" + uiEle.getId());
					vo_uiEle.setText(uiEle.getDescription());
					vo_uiEle.setType("ele");
					vo_uiEle.setLeaf(true);
					vo_uiEle.setChecked(vo_funUI.getChecked());
					if (pfUITmp != null) {
						for (PermissionUIElement puiEl : puiElList) {
							if (puiEl.getUiElement().getId().equals(uiEle.getId())) {
								vo_uiEle.setChecked(false);
								break;
							}
						}
					}

					treevos_uiEle.add(vo_uiEle);
				}
				vo_funUI.setChildren(treevos_uiEle);
				treevos_funUI.add(vo_funUI);
				pfUITmp = null;
			}
			vo_fun.setChildren(treevos_funUI);
			permissionTmp = null;
		}
		return vo_fun;
	}

	/**
	 * @return the roleService
	 */
	public RoleService getRoleService() {
		return roleService;
	}

	/**
	 * @param roleService
	 *            the roleService to set
	 */
	public void setRoleService(RoleService roleService) {
		this.roleService = roleService;
	}

	/**
	 * @return the orgRoleService
	 */
	public OrgRoleService getOrgRoleService() {
		return orgRoleService;
	}

	/**
	 * @param orgRoleService
	 *            the orgRoleService to set
	 */
	public void setOrgRoleService(OrgRoleService orgRoleService) {
		this.orgRoleService = orgRoleService;
	}

	/**
	 * @return the userId
	 */
	public String getUserId() {
		return userId;
	}

	/**
	 * @param userId
	 *            the userId to set
	 */
	public void setUserId(String userId) {
		this.userId = userId;
	}

	/**
	 * @return the orgRoleId
	 */
	public String getOrgRoleId() {
		return orgRoleId;
	}

	/**
	 * @param orgRoleId
	 *            the orgRoleId to set
	 */
	public void setOrgRoleId(String orgRoleId) {
		this.orgRoleId = orgRoleId;
	}

	/**
	 * @return the permissionService
	 */
	public PermissionService getPermissionService() {
		return permissionService;
	}

	/**
	 * @param permissionService
	 *            the permissionService to set
	 */
	public void setPermissionService(PermissionService permissionService) {
		this.permissionService = permissionService;
	}

	/**
	 * @return the orgService
	 */
	public OrgService getOrgService() {
		return orgService;
	}

	/**
	 * @param orgService
	 *            the orgService to set
	 */
	public void setOrgService(OrgService orgService) {
		this.orgService = orgService;
	}

	/**
	 * @return the bcFunctionService
	 */
	public BcFunctionService getBcFunctionService() {
		return bcFunctionService;
	}

	/**
	 * @param bcFunctionService
	 *            the bcFunctionService to set
	 */
	public void setBcFunctionService(BcFunctionService bcFunctionService) {
		this.bcFunctionService = bcFunctionService;
	}

	/**
	 * @return the functionUIService
	 */
	public FunctionUIService getFunctionUIService() {
		return functionUIService;
	}

	/**
	 * @param functionUIService
	 *            the functionUIService to set
	 */
	public void setFunctionUIService(FunctionUIService functionUIService) {
		this.functionUIService = functionUIService;
	}

	/**
	 * @return the uiElementService
	 */
	public UIElementService getUiElementService() {
		return uiElementService;
	}

	/**
	 * @param uiElementService
	 *            the uiElementService to set
	 */
	public void setUiElementService(UIElementService uiElementService) {
		this.uiElementService = uiElementService;
	}

	public List<Permission> getPermissions() {
		return permissions;
	}

	public void setPermissions(List<Permission> permissions) {
		this.permissions = permissions;
	}
}
