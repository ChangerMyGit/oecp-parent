/*
 * Copyright (c) 2011 OECP All Rights Reserved.                	
 * <a href="http://www.oecp.cn">OECP</a> 
*/                                                                
  

package oecp.demo.uiviewtest.web;

import javax.annotation.Resource;

import oecp.bcbase.web.BizBaseAction;
import oecp.demo.uiviewtest.eo.Uiviewtest;
import oecp.demo.uiviewtest.service.UiviewtestServices;

import org.apache.struts2.convention.annotation.Namespace;
import org.apache.struts2.convention.annotation.ParentPackage;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
/**
 * UiviewtestAction
 * @author
 * @date 
 * @version 1.0
 */
@Controller("uiviewtestAction")
@Scope("prototype")
@ParentPackage("default-package")
@Namespace("/demo/Uiviewtest")
public class UiviewtestAction extends BizBaseAction<Uiviewtest>{
	private static final long serialVersionUID = 1L;
	
	/** 单据服务类 **/
	@Resource
	private UiviewtestServices uiviewtestServices;
	public void setUiviewtestServices(UiviewtestServices uiviewtestServices) {
		this.uiviewtestServices = uiviewtestServices;
	}
	protected UiviewtestServices getBillService(){
		return uiviewtestServices;
	}
	
}
