/*
 * Copyright (c) 2011 OECP All Rights Reserved.                	
 * <a href="http://www.oecp.cn">OECP</a> 
*/                                                                
  

package oecp.platform.bcinfo.web;

import java.util.List;

import javax.annotation.Resource;

import oecp.framework.exception.BizException;
import oecp.framework.web.JsonResult;
import oecp.platform.bcinfo.eo.BizComponent;
import oecp.platform.bcinfo.service.BizComponentService;
import oecp.platform.web.BasePlatformAction;

import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.Namespace;
import org.apache.struts2.convention.annotation.ParentPackage;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

/** 
 * 组件action
 * @author lintao  
 * @date 2011-8-9 上午10:24:23 
 * @version 1.0
 *  
 */
@Controller
@Scope("prototype")
@ParentPackage("default-package")
@Namespace("/bcComponent")
public class BcComponentAction extends BasePlatformAction{

	private static final long serialVersionUID = 1L;
	@Resource
	private BizComponentService bizComponentService;
	private BizComponent bizComponent;
	//对象的Id
	private String id;
	private String[] ids;
	
	/**
	 * 
	* 获取所有组件
	* @author lintao
	* @date 2011-8-15上午08:54:45
	* @param 
	* @return
	 * @throws BizException 
	 */
	@Action("list")
	public String listbizComponents() throws BizException{
		List<BizComponent> bizs = bizComponentService.findAll();
		if(bizs == null || bizs.size() <=0){
			throw new BizException("查询失败");
		}else{
			JsonResult jr = new JsonResult(bizs);
//			jr.setContainFields(new String[]{"id", "code", "name", "host", "servicePort","webPort","contextPath","dbType","dbIp","dbPort","dbUser","dbPwd","discription","initNum"});
			setJsonString(jr.toJSONString());
		}
		return SUCCESS;
	}
	@Action("add")
	public String addbizComponent() throws BizException{
			bizComponentService.create(bizComponent);
			setJsonString("{success : true,msg : '添加成功'}");

		return SUCCESS;
	}
	
	/**
	 * 
	* 编辑组件
	* @author lintao
	* @date 2011-8-15上午08:55:13
	* @param 
	* @return
	 * @throws BizException 
	 */
	@Action("update")
	public String updatebizComponent() throws BizException{
			bizComponentService.save(bizComponent);
			setJsonString("{success : true,msg : '更新成功'}");
		return SUCCESS;
	}
	/**
	 * 
	* 根据ID查找组件
	* @author lintao
	* @date 2011-8-15上午08:55:29
	* @param 
	* @return
	 */
	@Action("find")
	public String findbizComponent() throws BizException{
		bizComponent = bizComponentService.find(id);
		JsonResult jr = new JsonResult(bizComponent);
//		jr.setContainFields(new String[]{"id", "code", "name", "host", "servicePort","webPort","contextPath","dbType","dbIp","dbPort","dbUser","dbPwd","discription","initNum"});
		setJsonString(jr.toJSONString());
		return SUCCESS;
	}
	/**
	 * 
	* 根据ID[] 删除组件
	* @author lintao
	* @date 2011-8-15上午08:56:07
	* @param 
	* @return
	 * @throws BizException 
	 */
	@Action("delete")
	public String deletebizComponent() throws BizException{
		for(String id : ids){
			bizComponentService.deleteComponent(id);
		}
		setJsonString("{success : true,msg : '删除成功'}");
		return SUCCESS;
	}
	
	/**
	 * 
	* 获取组件详细信息
	* @author lintao
	* @date 2011-8-15上午08:56:35
	* @param 
	* @return
	 * @throws BizException 
	 */
	@Action("getBcInfo")
	public String getBcInfo() throws BizException{
			bizComponentService.saveComponentInfo(id);
			setJsonString("{success : true,msg : '获取信息成功'}");
		return SUCCESS;
	}
	/**
	 * 初始化组件
	* 
	* @author lintao
	* @date 2011-8-15上午09:02:42
	* @param 
	* @return
	 */
	@Action("initBcInfo")
	public String initBcInfo() throws BizException{
		bizComponentService.initComponentInfo(id);
		setJsonString("{success : true,msg : '初始化成功'}");
		return SUCCESS;
	}
	/**
	 * 
	* 组件对接
	* @author lintao
	* @date 2011-8-15上午08:56:55
	* @param 
	* @return
	 */
	@Action("connection")
	public String connection() throws BizException{
		BizComponent bc = bizComponentService.find(id);
		if(bc.getIsConnection()){
			throw new BizException("组件已经对接");
		}else{
			bizComponentService.connection(id);
			setJsonString("{success : true,msg : '组件对接成功'}");
		}
		return SUCCESS;
	}
	
	
	public BizComponentService getBizComponentService() {
		return bizComponentService;
	}
	public void setBizComponentService(BizComponentService bizComponentService) {
		this.bizComponentService = bizComponentService;
	}
	public BizComponent getBizComponent() {
		return bizComponent;
	}
	public void setBizComponent(BizComponent bizComponent) {
		this.bizComponent = bizComponent;
	}
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String[] getIds() {
		return ids;
	}
	public void setIds(String[] ids) {
		this.ids = ids;
	}

}
