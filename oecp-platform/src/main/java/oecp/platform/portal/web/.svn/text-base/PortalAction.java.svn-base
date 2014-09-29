/*
 * Copyright (c) 2011 OECP All Rights Reserved.                	
 * http://www.oecp.cn                                                                 
 */

package oecp.platform.portal.web;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpSession;

import oecp.framework.util.DateUtil;
import oecp.framework.web.JsonResult;
import oecp.framework.web.WebConstant;
import oecp.platform.bcfunction.eo.Function;
import oecp.platform.bcfunction.eo.UIElement;
import oecp.platform.bcfunction.service.BcFunctionService;
import oecp.platform.org.eo.Organization;
import oecp.platform.org.eo.OrganizationConfig;
import oecp.platform.permission.eo.Permission;
import oecp.platform.permission.eo.PermissionFuncUI;
import oecp.platform.permission.eo.PermissionUIElement;
import oecp.platform.permission.service.PermissionService;
import oecp.platform.permission.vo.BizComponentVO;
import oecp.platform.web.BasePlatformAction;
import oecp.platform.web.OnlineUser;

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
 * 门户action，主要用在系统首页的全局功能
 * 
 * @author yongtree
 * @date 2011-5-11 上午10:50:05
 * @version 1.0
 */
@Controller
@Scope("prototype")
@ParentPackage("default-package")
@Namespace("/portal")
public class PortalAction extends BasePlatformAction {

	private static final long serialVersionUID = 1L;

	@Autowired
	private BcFunctionService bcFunctionService;

	@Autowired
	private PermissionService permissionService;

	private String orgid;

	private String userid;

	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Action(value = "getOnlineUser")
	public String loadOnlineUser() {
		Map user = new HashMap();
		user.put("id", getOnlineUser().getUser().getId());
		user.put("loginId", getOnlineUser().getUser().getLoginId());
		user.put("name", getOnlineUser().getUser().getName());
		user.put("loginTime", DateUtil.getDateStr(getOnlineUser().getUser()
				.getLastLoginTime(), "hh:MM:ss"));
		user.put("orgId", getOnlineUser().getLoginedOrg().getId());
		user.put("orgName", getOnlineUser().getLoginedOrg().getName());
		JsonResult jr = new JsonResult(user);
		this.setJsonString(jr.toJSONString());
		return SUCCESS;
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Transactional
	@Action(value = "menu")
	public String loadFunctionMenu() {
		String menu_json = null;// (String)
								// getOnlineUser().getData().get("_menu_json");
		// if (menu_json == null) {
		List<BizComponentVO> bcs = permissionService.getPermissionBCs(
				getOnlineUser().getUser().getId(), getOnlineUser().getLoginedOrg().getId());
		List<MenuTree> menus = new ArrayList<MenuTree>();
		for (BizComponentVO bc : bcs) {
			MenuTree menu = new MenuTree();
			menu.setId(bc.getCode());
			menu.setText(bc.getName());
			// 得到用户在某个组件的权限列表
			List<Permission> ps = permissionService.getUserBCPermissions(
					bc.getCode(), getOnlineUser().getUser().getId(),getOnlineUser().getLoginedOrg().getId());
			// 得到该组件下的所有的功能列表
			List<Function> functions = this.bcFunctionService.getRootFunctions(bc.getId());
			menu.setChildren(buildFunctionTree(functions, ps));
			menus.add(menu);
		}
		/** begin****把页面元素的权限放入session*****add by yangtao *********/
		HttpSession session = this.getRequest().getSession();
		List<Permission> ps = permissionService.getUserPermission(
				getOnlineUser().getUser().getId(), getOnlineUser().getLoginedOrg().getId());
		Map<String, List> map = new LinkedHashMap();
		for (Permission p : ps) {
			List resultList = new ArrayList();
			Function func = p.getFunction();
			List<PermissionFuncUI> list = p.getPermissionFuncUIs();
			for (PermissionFuncUI pfi : list) {
				List<PermissionUIElement> li = pfi.getPermissionUIElements();
				for (PermissionUIElement pui : li) {
					UIElement ui = pui.getUiElement();
					resultList.add(ui.getElementId());
				}
			}
			if (map.get(func.getCode()) != null) {
				resultList.addAll(map.get(func.getCode()));
			}
			map.put(func.getCode(), resultList);
		}
		session.setAttribute(WebConstant.OECP_UIELEMENT_PERMISSION, map);
		/** end******把页面元素的权限放入session******add by yangtao ******/
		menu_json = JSON.toJSONString(menus);
		getOnlineUser().getData().put("_menu_json", menu_json);
		// }
		this.setJsonString(menu_json);
		return SUCCESS;

	}

	@Transactional
	@Action(value = "menu4userid")
	@Deprecated
	public String loadFunctionMenu4userid() {
		List<BizComponentVO> bcs = permissionService.getPermissionBCs(
				this.userid, this.orgid);
		List<MenuTree> menus = new ArrayList<MenuTree>();
		for (BizComponentVO bc : bcs) {
			MenuTree menu = new MenuTree();
			menu.setId(bc.getCode());
			menu.setText(bc.getName());
			// 得到用户在某个组件的权限列表
			List<Permission> ps = permissionService.getUserBCPermissions(
					bc.getCode(), this.userid, this.orgid);
			// 得到该组件下的所有的功能列表
			List<Function> functions = this.bcFunctionService
					.getRootFunctions(bc.getId());
			menu.setChildren(buildFunctionTree(functions, ps));
			menus.add(menu);
		}
		this.setJsonString(JSON.toJSONString(menus));
		return SUCCESS;
	}

	public List<MenuTree> buildFunctionTree(List<Function> functions,
			List<Permission> ps) {
		List<MenuTree> treeList = new ArrayList<MenuTree>();
		for (Function func : functions) {
			if (checkPermission(func, ps)) {
				MenuTree vo = new MenuTree();
				vo.setId(String.valueOf(func.getCode()));
				vo.setText(func.getName());
				vo.setLeaf(func.getRunable());
				vo.setFuncid(func.getId()); // slx 2012年6月13日添加此行，为实现桌面快捷方式
				String url = func.findDefaultUI() == null ? "#" : func
						.findDefaultUI().getSign();
				// 判断url
				if (url.startsWith("_JS.")) {
					vo.setIframe(false);
					url = url.substring(4);
				} else {
					vo.setIframe(true);
					url = func.getBc().getHttpDomainUrl() + url;
				}
				vo.setIframeUrl(url);
				vo.setChildren(buildFunctionTree(func.getChildren(), ps));
				treeList.add(vo);
			}
		}
		return treeList;
	}

	private boolean checkPermission(Function f, List<Permission> ps) {
		for (Permission p : ps) {
			if (f.equalsPK(p.getFunction()))
				return true;
		}
		return false;
	}

	@SuppressWarnings("unchecked")
	@Action(value = "select")
	public String select() {
		OnlineUser ou = getOnlineUser();
		if (ou == null) {
			return LOGIN;
		}
		List<Organization> orgs = (List<Organization>) getOnlineUser()
				.getData().get(WebConstant.OECP_SESSION_ORGS);
		if (StringUtils.isEmpty(orgid)) {
			return LOGIN;
		} else {
			boolean is = false;
			for (Organization org : orgs) {
				if (orgid.equals(org.getId())) {
					getOnlineUser().setLoginedOrg(org);
					getOnlineUser().login();
					/** begin 增加组织logo图片 **/
					OrganizationConfig ogc = this.getOnlineUser()
							.getLoginedOrg().getOrganizationConfig();
					String logoUrl = WebConstant.OECP_DEFAULT_ORG_LOGOURL;
					if (ogc != null) {
						logoUrl = this.getOnlineUser().getLoginedOrg()
								.getOrganizationConfig().getLogoUrl();
					}
					this.getSession().setAttribute(
							WebConstant.OECP_ORG_LOGOURL, logoUrl);
					/** end 增加组织logo图片 **/
					is = true;
					break;
				}
			}
			if (is) {
				return "index";
			} else {
				return LOGIN;
			}
		}
	}

	public void setBcFunctionService(BcFunctionService bcFunctionService) {
		this.bcFunctionService = bcFunctionService;
	}

	public void setPermissionService(PermissionService permissionService) {
		this.permissionService = permissionService;
	}

	public String getOrgid() {
		return orgid;
	}

	public void setOrgid(String orgid) {
		this.orgid = orgid;
	}

	public String getUserid() {
		return userid;
	}

	public void setUserid(String userid) {
		this.userid = userid;
	}

}
