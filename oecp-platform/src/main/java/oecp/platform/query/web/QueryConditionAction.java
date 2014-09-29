/**
 * oecp-platform - UIViewParser.java
 * copyright 2009-2012 OECP www.oecp.cn
 * 创建人:slx	创建时间:2011-11-7上午10:41:16		版本:v1
 * ============================================
 * 修改人：			修改时间:					版本:
 * 修改原因:
 *
 * ============================================
 */

package oecp.platform.query.web;

import java.util.HashMap;
import java.util.Map;

import javax.annotation.Resource;

import oecp.framework.exception.BizException;
import oecp.framework.web.JsonResult;
import oecp.platform.query.service.QuerySchemeService;
import oecp.platform.query.setting.eo.QueryScheme;
import oecp.platform.rpt.service.ReportService;
import oecp.platform.web.BasePlatformAction;

import org.apache.commons.lang.StringUtils;
import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.Namespace;
import org.apache.struts2.convention.annotation.ParentPackage;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

/** 
 * 查询方案使用Action
 * @author wangliang  
 * @date 2012-5-11 下午3:04:06 
 * @version 1.0
 *  
 */
@Controller
@Scope("prototype")
@ParentPackage("default-package")
@Namespace("/app/query")
public class QueryConditionAction extends BasePlatformAction{

	private static final long serialVersionUID = 1L;
	@Resource
	private QuerySchemeService querySchemeService;
	@Resource(name="reportService")
	private ReportService reportService;

	private String code;//查询方案编号
	
	/**
	 * 获取配置信息
	 * @author wangliang
	 * @date 2012-5-11下午3:20:36
	 * @return
	 * @throws BizException 
	 * @throws SecurityException 
	 * @throws NoSuchFieldException 
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	@Action(value="getQueryScheme")
	public String getQueryScheme() throws BizException, NoSuchFieldException, SecurityException{
		if(StringUtils.isEmpty(code)){
			throw new BizException("方案编号为空！");
		}
		QueryScheme qs = reportService.getQuerySchemeByRptCode(code);
		Map operator = querySchemeService.getOperator();
		Map val = new HashMap();
		val.put("conditions", qs.getOtherconditions());
		val.put("operator",operator);
		JsonResult jr = new JsonResult(val);
		setJsonString(jr.toJSONString());
		return SUCCESS;
	}
	
	@Action(value="loadQueryScheme")
	public String loadQueryScheme() throws BizException, NoSuchFieldException, SecurityException{
		if(StringUtils.isEmpty(code)){
			throw new BizException("方案编号为空！");
		}
		QueryScheme qs = querySchemeService.getQuerySchemeByCode_full(code);
		Map operator = querySchemeService.getOperator();
		Map val = new HashMap();
		val.put("conditions", qs.getOtherconditions());
		val.put("operator",operator);
		JsonResult jr = new JsonResult(val);
		setJsonString(jr.toJSONString());
		return SUCCESS;
	}
	/**
	 * 获取条件符号
	 * @author wangliang
	 * @date 2012-5-11下午3:20:46
	 * @return
	 * @throws ClassNotFoundException 
	 * @throws SecurityException 
	 * @throws NoSuchFieldException 
	 */
	@Action(value="getOperator")
	public String getQueryOperator() throws ClassNotFoundException, NoSuchFieldException, SecurityException{
		JsonResult jr = new JsonResult(querySchemeService.getOperator());
		setJsonString( jr.toJSONString());
		return SUCCESS;
	}
	/** =========== get set 方法 ===========*/

	public String getCode() {
		return code;
	}
	public void setCode(String code) {
		this.code = code;
	}

	public void setReportService(ReportService reportService) {
		this.reportService = reportService;
	}
}
