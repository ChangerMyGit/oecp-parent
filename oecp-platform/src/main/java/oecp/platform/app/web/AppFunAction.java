/*
 * Copyright (c) 2011 OECP All Rights Reserved.                	
 * <a href="http://www.oecp.cn">OECP</a> 
*/                                                                
  

package oecp.platform.app.web;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;

import oecp.framework.web.ext.JsonTreeVO;
import oecp.platform.bcfunction.eo.Function;
import oecp.platform.bcfunction.service.BcFunctionService;
import oecp.platform.bcinfo.eo.BizComponent;
import oecp.platform.bcinfo.service.BizComponentService;
import oecp.platform.web.BasePlatformAction;

import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.Namespace;
import org.apache.struts2.convention.annotation.ParentPackage;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;

import com.alibaba.fastjson.JSON;

/** 
 * 系统应用的功能action，主要统一对外提供对Function的查询操作
 *
 * @author liujt  
 * @date 2011-8-22 上午10:16:49 
 * @version 1.0
 */
@Controller
@Scope("prototype")
@ParentPackage("default-package")
@Namespace("/app/fun")
public class AppFunAction extends BasePlatformAction {
	
	private static final long serialVersionUID = -4491229013242943291L;
	
	@Resource
	private BizComponentService bizComponentService;
	@Resource
	private BcFunctionService bcFunctionService;
	/**
	 * 得到所有组件的功能树 <br>
	 * @return
	 */
	@Transactional
	@Action(value = "allFunsTree")
	public String allFunsTree() {
		List<JsonTreeVO> vos = new ArrayList<JsonTreeVO>();
		
		List<BizComponent> bcs = bizComponentService.findAll();
		for(BizComponent bc :bcs){
			JsonTreeVO bc_vo = new JsonTreeVO();
			bc_vo.setId("bc_" + bc.getId());
			bc_vo.setLeaf(false);
			bc_vo.setText(bc.getName());
			List<Function> funcList = bcFunctionService.getRootFunctions(bc.getId());
			List<JsonTreeVO> funs_vos = eo2vo(funcList);
			bc_vo.setChildren(funs_vos);
			vos.add(bc_vo);
		}
		setJsonString(JSON.toJSONString(vos));
		
		return SUCCESS;
	}
	
	/**
	 * EO转换为vo
	 * 
	 * @param funcs
	 * @return
	 */
	private List<JsonTreeVO> eo2vo(List<Function> funcs) {
		List<JsonTreeVO> treeList = new ArrayList<JsonTreeVO>();
		for (Function func : funcs) {
			JsonTreeVO vo = new JsonTreeVO();
			vo.setId("fun_" + func.getId());
			vo.setText(func.getName());
			vo.setLeaf(func.getRunable());
			vo.setChildren(eo2vo(func.getChildren()));
			treeList.add(vo);
		}
		return treeList;
	}

	public BizComponentService getBizComponentService() {
		return bizComponentService;
	}

	public void setBizComponentService(BizComponentService bizComponentService) {
		this.bizComponentService = bizComponentService;
	}

	public BcFunctionService getBcFunctionService() {
		return bcFunctionService;
	}

	public void setBcFunctionService(BcFunctionService bcFunctionService) {
		this.bcFunctionService = bcFunctionService;
	}
	
}
