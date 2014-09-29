/*
 * Copyright (c) 2011 OECP All Rights Reserved.                	
 * <a href="http://www.oecp.cn">OECP</a> 
*/                                                                
  

package oecp.platform.bcdepend.web;

import java.util.List;

import javax.annotation.Resource;

import org.apache.commons.lang.xwork.StringUtils;
import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.Namespace;
import org.apache.struts2.convention.annotation.ParentPackage;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import oecp.framework.exception.BizException;
import oecp.framework.web.JsonResult;
import oecp.platform.bcdepend.eo.BizDepend;
import oecp.platform.bcdepend.service.BizDependService;
import oecp.platform.web.BasePlatformAction;

/** 
 *  组件依赖action
 * @author lintao  
 * @date 2011-8-8 下午02:28:51 
 * @version 1.0
 *  
 */
@Controller
@Scope("prototype")
@ParentPackage("default-package")
@Namespace("/bcDepend")
public class BcDependAction extends BasePlatformAction {

	private static final long serialVersionUID = 1L;
	//组件依赖服务
	@Resource
	private BizDependService bizDependService;
	//组件依赖
	private BizDepend bizDepend;
	//依赖对象的Id
	private String id;
	private String[] ids;
	
	/**
	 * 
	* 根据组件ID,获取组件的依赖描述
	* @author lintao
	* @date 2011-8-15上午08:57:29
	* @param 
	* @return
	 * @throws BizException 
	 */
	@Action("list")
	public String listBizDepends() throws BizException{
			if(!StringUtils.isEmpty(id)){
				List<BizDepend> bizs = bizDependService.queryByBizComponentId(id);
				if(bizs == null || bizs.size() <=0){
					throw new BizException("查询失败");
				}else{
					JsonResult jr = new JsonResult(bizs);
					jr.setContainFields(new String[]{"id","url","dependName_EN","dependName_CN","dependDesc","bc.id"});
					setJsonString(jr.toJSONString());
				}
			}else{
				throw new BizException("查询失败");
			}
		return SUCCESS;
	}
	
	/**
	 * 
	* 添加依赖描述
	* @author lintao
	* @date 2011-8-15上午08:58:36
	* @param 
	* @return
	 */
	@Action("add")
	public String addBizDepend(){
		try {
			bizDependService.create(bizDepend);
			setJsonString("{success : true,msg : '添加成功'}");
		} catch (BizException e) {
			e.printStackTrace();
			setJsonString("{success : false,msg : '添加失败'}");
		}
		return SUCCESS;
	}
	
	/**
	 * 
	* 编辑组件依赖描述
	* @author lintao
	* @date 2011-8-15上午08:59:03
	* @param 
	* @return
	 */
	@Action("update")
	public String updateBizDepend(){
		try {
			bizDependService.save(bizDepend);
			setJsonString("{success : true,msg : '更新成功'}");
		} catch (BizException e) {
			setJsonString("{success : false,msg : '更新失败'}");
			e.printStackTrace();
		}
		return SUCCESS;
	}
	/**
	 * 
	* 根据依赖ID查找
	* @author lintao
	* @date 2011-8-15上午09:00:24
	* @param 
	* @return
	 */
	@Action("find")
	public String findBizDepend() throws BizException{
		bizDepend = bizDependService.find(id);
		JsonResult jr = new JsonResult(bizDepend);
		jr.setContainFields(new String[]{"id","url","dependName_EN","dependName_CN","dependDesc","bc.id"});
		setJsonString(jr.toJSONString());
		return SUCCESS;
	}
	/**
	 * 
	* 删除依赖
	* @author lintao
	* @date 2011-8-15上午09:00:51
	* @param 
	* @return
	 */
	@Action("delete")
	@Transactional
	public String deleteBizDepend(){
		try {
			bizDependService.delete(ids);
			setJsonString("{success : true,msg : '删除成功'}");
		} catch (BizException e) {
			setJsonString("{success : false,msg : '删除失败'}");
			e.printStackTrace();
		}
		return SUCCESS;
	}
	
	public BizDependService getBizDependService() {
		return bizDependService;
	}
	public void setBizDependService(BizDependService bizDependService) {
		this.bizDependService = bizDependService;
	}
	public BizDepend getBizDepend() {
		return bizDepend;
	}
	public void setBizDepend(BizDepend bizDepend) {
		this.bizDepend = bizDepend;
	}
	public String[] getIds() {
		return ids;
	}
	public void setIds(String[] ids) {
		this.ids = ids;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}
	
}
