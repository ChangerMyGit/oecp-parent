/*
 * Copyright (c) 2012 OECP All Rights Reserved.                	
 * <a href="http://www.oecp.cn">OECP</a> 
*/                                                                
  

package oecp.platform.print.service;

import java.util.List;

import oecp.framework.exception.BizException;
import oecp.framework.service.BaseService;
import oecp.platform.print.eo.PrintTemplate;

/** 
 * 单据打印模板接口类
 * @author wangliang  
 * @date 2012-3-19 上午10:04:14 
 * @version 1.0
 *  
 */
public interface PrintTemplateService extends BaseService<PrintTemplate>{
	/**
	 * 根据功能编号获取所有打印模板
	 * @author wangliang
	 * @date 2012-3-19上午10:44:42
	 * @param functionCode
	 * @return
	 */
	public List<PrintTemplate> getTemplatesByFunCode(String functionCode);
	/**
	 * 获取功能对应的所有打印模板
	 * @author wangliang
	 * @date 2012-3-29下午3:25:25
	 * @param functionId
	 * @return
	 */
	public List<PrintTemplate> getTemplatesByFunId(String functionId);
	/**
	 * 获取功能视图对应的所有打印模板
	 * @author wangliang
	 * @date 2012-3-29下午3:24:32
	 * @param functionId
	 * @param viewId
	 * @return
	 * @throws BizException
	 */
	public List<PrintTemplate> getHasCheckedTemplates(String functionId) throws BizException;
}
