/*
 * Copyright (c) 2011 OECP All Rights Reserved.                	
 * <a href="http://www.oecp.cn">OECP</a> 
*/                                                                
  

package oecp.platform.bcdepend.service;

import java.util.List;

import oecp.framework.exception.BizException;
import oecp.framework.service.BaseService;
import oecp.platform.bcdepend.eo.BizDepend;
import oecp.platform.bcinfo.eo.BizComponent;

/** 
 * 组件依赖服务接口类
 * @author lintao  
 * @date 2011-8-8 下午01:35:22 
 * @version 1.0
 *  
 */
public interface BizDependService extends BaseService<BizDepend> {
	/**
	 * 
	* 
	* @author lintao
	* @date 2011-8-8下午03:59:01
	* @param bc_pk 
	* 				业务组件ID 
	* @return list
	*               业务组件下的依赖
	 */
	List<BizDepend> queryByBizComponentId(String bc_pk);
	/**
	 * 
	* 业务依赖注册服务
	* @author lintao
	* @date 2011-8-15上午09:14:46
	* @param  bc                           url
	*             关联组件            组件依赖描述
	* @return
	 */
	void registerBizDepend(BizComponent bc)throws BizException;
	
	/**
	 * 根据组件ID,删除所有依赖
	* 
	* @author lintao
	* @date 2011-8-15下午03:36:04
	* @param 
	* @return
	 */
	void deleteByBizComponentId(String bc_pk);
	
}
