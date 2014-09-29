/*
 * Copyright (c) 2011 OECP All Rights Reserved.                	
 * http://www.oecp.cn                                                                 
 */

package oecp.platform.web;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Map;

import oecp.framework.util.PropertiesUtil;
import oecp.framework.web.struts2.BaseExtJsAction;

import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.Actions;
import org.apache.struts2.convention.annotation.InterceptorRef;
import org.apache.struts2.convention.annotation.ParentPackage;
import org.apache.struts2.convention.annotation.Result;
import org.apache.struts2.convention.annotation.Results;
import org.springframework.stereotype.Controller;

/**
 * 平台action基类
 * 
 * @author yongtree
 * @date 2011-5-10 下午04:01:34
 * @version 1.0
 */
@Controller
@ParentPackage(value = "default-package")
@Actions( {
		@Action(value = "index", interceptorRefs = { @InterceptorRef(value = "indexStack") }, results = { @Result(location = "/WEB-INF/content/index.jsp") }),
		@Action(value = "login", interceptorRefs = { @InterceptorRef(value = "defaultStack") }, results = { @Result(location = "/WEB-INF/content/login.jsp") }) })
@Results(value = {
		@Result(name = "input", type = "redirect", location = "/WEB-INF/content/error.jsp"),
		@Result(name = "index", type = "redirect", location = "/index.jspx"),
		@Result(name = "login", type = "redirect", location = "/login.jspx"),
		@Result(name = "success", location = "/WEB-INF/content/json.jsp"),
		@Result(name = "exception", location = "/WEB-INF/content/error.jsp") })
public class BasePlatformAction extends BaseExtJsAction {

	private static final long serialVersionUID = 1L;

	public OnlineUser getOnlineUser() {
		return WebContext.getCurrentUser();
	}

	public Map<String, String> getAppConfig() {
		try {
			return new PropertiesUtil("app-config.properties")
					.readAllProperties();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}

}
