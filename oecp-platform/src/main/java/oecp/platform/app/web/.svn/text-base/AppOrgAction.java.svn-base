/*
 * Copyright (c) 2011 OECP All Rights Reserved.                	
 * <a href="http://www.oecp.cn">http://www.oecp.cn</a>                                                                 
 */

package oecp.platform.app.web;

import java.util.List;

import javax.annotation.Resource;

import oecp.framework.exception.BizException;
import oecp.framework.web.ext.JsonTreeVOBuilder;
import oecp.platform.bcfunction.service.BcFunctionService;
import oecp.platform.org.eo.Organization;
import oecp.platform.org.service.OrgService;
import oecp.platform.web.BasePlatformAction;

import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.Namespace;
import org.apache.struts2.convention.annotation.ParentPackage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;

import com.alibaba.fastjson.JSON;

/**
 * 系统应用的组织action，主要统一对外提供对Organization的查询操作
 * 
 * @author yongtree
 * @date 2011-5-26 下午02:41:59
 * @version 1.0
 */
@Controller
@Scope("prototype")
@ParentPackage("default-package")
@Namespace("/app/org")
public class AppOrgAction extends BasePlatformAction {

	private static final long serialVersionUID = -4491229013242968291L;
	@Autowired
	private OrgService orgService;

	@Resource
	private BcFunctionService bcFunctionService;
	
	
	/**
	 * 得到当前用户的组织树 <br>
	 *如果需要返回带有checkbox的树<br>
	 * ?cb=0 带有没有选择的checkbox tree<br>
	 * ?cb=1 带有选择的checkbox tree<br> 
	 * @return
	 * @throws BizException 
	 */
	@Transactional
	@Action(value = "ownerTree")
	public String ownerTree() throws BizException {
		Organization root = this.orgService.getChildOrgs(this.getOnlineUser().getLoginedOrg().getId());
		String cb = getRequest().getParameter("cb");
		// TODO 添加缓存支持
		this.setJsonString("["
				+ JSON.toJSONString(JsonTreeVOBuilder.getTreeVOFromEO(root,
						"id", "name", "childOrgs",
						"0".equals(cb)?JsonTreeVOBuilder.CheckShow.NOCHECK:("1".equals(cb)?JsonTreeVOBuilder.CheckShow.CHECK:JsonTreeVOBuilder.CheckShow.NONE))) + "]");
		return SUCCESS;
	}
	
	/**
	 * 得到可以分配的组织树 <br>
	 * @author luanyoubo
	 * @date 2014年1月13日下午2:40:49
	 * @return
	 */
	@Transactional
	@Action(value = "getDefOrgTree")
	public String getDefOrgTree() throws BizException{
		Organization root = this.orgService.getChildOrgs(this.getOnlineUser().getLoginedOrg().getId());
		String cb = getRequest().getParameter("cb");
		// 流程定义配置组织数需要按照功能菜单过滤
		String functionid = getRequest().getParameter("functionid");//功能ID
		String bcid = bcFunctionService.find(functionid).getBc().getId();
		orgFilter(root, bcid);
		this.setJsonString("["
				+ JSON.toJSONString(JsonTreeVOBuilder.getTreeVOFromEO(root,
						"id", "name", "childOrgs",
						"0".equals(cb)?JsonTreeVOBuilder.CheckShow.NOCHECK:("1".equals(cb)?JsonTreeVOBuilder.CheckShow.CHECK:JsonTreeVOBuilder.CheckShow.NONE))) + "]");

		return SUCCESS;
	}

	/**
	 * 过滤组织
	 * @author luanyoubo
	 * @date 2014年1月13日下午2:48:19
	 * @param root
	 * @param bcid
	 */
	private void orgFilter(Organization root, String bcid) {
		List<Organization> childOrgs = root.getChildOrgs();
		for (int i = 0; i < childOrgs.size(); i++) {
			Organization org = childOrgs.get(i);
			if (!orgService.getUserBCs(org.getId(), bcid) || org.getLock())
				childOrgs.remove(i);
			if (org.getChildOrgs().size() > 0)
				orgFilter(org, bcid);
		}
	}

	public void setOrgService(OrgService orgService) {
		this.orgService = orgService;
	}

	public void setBcFunctionService(BcFunctionService bcFunctionService) {
		this.bcFunctionService = bcFunctionService;
	}

}
