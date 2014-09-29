/*
 * Copyright (c) 2012 OECP All Rights Reserved.                	
 * <a href="http://www.oecp.cn">OECP</a> 
*/                                                                
  

package oecp.platform.print.service;

import java.util.List;

import javax.annotation.Resource;

import oecp.framework.exception.BizException;
import oecp.platform.base.service.PlatformBaseServiceImpl;
import oecp.platform.print.eo.PrintTemplate;
import oecp.platform.uiview.assign.eo.FunctionView;
import oecp.platform.uiview.assign.service.FunctionViewService;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/** 
 * 单据打印模板接口实现类
 * @author wangliang  
 * @date 2012-3-19 上午10:10:49 
 * @version 1.0
 *  
 */
@Service("printTemplateService")
public class PrintTemplateServiceImpl extends PlatformBaseServiceImpl<PrintTemplate>
		implements PrintTemplateService {
	
	@Resource(name="functionViewServiceImpl")
	private FunctionViewService fvservice;
	
	@Override
	public List<PrintTemplate> getTemplatesByFunCode(String functionCode) {
		return getDao().queryByWhere(PrintTemplate.class, "o.function.code=?", new Object[]{functionCode});
	}

	@Override
	public List<PrintTemplate> getTemplatesByFunId(String functionId) {
		return getDao().queryByWhere(PrintTemplate.class, "o.function.id=?", new Object[]{functionId});
	}
	
	@Transactional
	public List<PrintTemplate> getHasCheckedTemplates(String viewId) throws BizException{
		FunctionView fv = fvservice.find(viewId);
		fv.loadLazyAttributes();
		List<PrintTemplate> view_print_list = fv.getPrintTemplates();
		return view_print_list;
	}

	public void setFvservice(FunctionViewService fvservice) {
		this.fvservice = fvservice;
	}
	
}
