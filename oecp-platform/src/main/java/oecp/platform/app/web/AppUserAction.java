/*
 * Copyright (c) 2011 OECP All Rights Reserved.                	
 * <a href="http://www.oecp.cn">http://www.oecp.cn</a>                                                                 
 */

package oecp.platform.app.web;

import org.apache.struts2.convention.annotation.Namespace;
import org.apache.struts2.convention.annotation.ParentPackage;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import oecp.platform.web.BasePlatformAction;

/**
 * 用户相关的通用查询和操作
 * 
 * @author yongtree
 * @date 2011-5-31 下午02:00:07
 * @version 1.0
 */
@Controller
@Scope("prototype")
@ParentPackage("default-package")
@Namespace("/app/user")
public class AppUserAction extends BasePlatformAction {

}