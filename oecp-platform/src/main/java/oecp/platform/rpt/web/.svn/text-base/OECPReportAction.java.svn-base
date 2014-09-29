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

package oecp.platform.rpt.web;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import oecp.framework.dao.QueryCondition;
import oecp.framework.dao.QueryObject;
import oecp.framework.dao.QueryResult;
import oecp.framework.exception.BizException;
import oecp.framework.util.FastJsonUtils;
import oecp.framework.vo.base.DataVO;
import oecp.framework.web.JsonResult;
import oecp.platform.api.chart.ChartAPI;
import oecp.platform.api.chart.ChartAPI.ChartTypeEnum;
import oecp.platform.api.chart.ChartAPI.DataDescription;
import oecp.platform.api.chart.SimpleChartTypeEnum;
import oecp.platform.query.service.QuerySchemeService;
import oecp.platform.query.setting.eo.QueryScheme;
import oecp.platform.rpt.ReportResultSet;
import oecp.platform.rpt.service.ReportService;
import oecp.platform.rpt.setting.eo.Report;
import oecp.platform.rpt.setting.eo.ReportUIView;
import oecp.platform.uiview.enums.ComponentType;
import oecp.platform.uiview.vo.UIComponentVO;
import oecp.platform.uiview.web.parser.UIViewParser;
import oecp.platform.web.BasePlatformAction;

import org.apache.commons.lang.StringUtils;
import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.Namespace;
import org.apache.struts2.convention.annotation.ParentPackage;
import org.apache.struts2.convention.annotation.Result;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;

/** 
 * 报表action
 * @author slx  
 * @date 2012-5-14 上午11:27:21 
 * @version 1.0
 */
@Controller()
@Scope("prototype")
@ParentPackage("default-package")
@Namespace("/report")
public class OECPReportAction extends BasePlatformAction {
	private static final long serialVersionUID = 1L;
	/** 报表服务 **/
	@Resource(name="reportService")
	private ReportService reportService;
	/** 视图代码生成器 **/
	@Resource(name = "extUIViewParser")
	private UIViewParser parser;
	@Resource(name = "SimpleChartAPI")
	private ChartAPI chartAPI;
	@Resource
	private QuerySchemeService querySchemeService;
	
	private String rptcode ;
	private String funcCode;
	private List<QueryCondition> conditions;

	/**
	 * 打开报表jsp界面。这里只是跳转用。
	 * @author slx
	 * @date 2012-5-14下午3:03:14
	 * @return
	 */
	@Action(value="open",results={
			@Result(name = "OPEN",location = "/page/report/report.jsp")})
	public String open(){
		return "OPEN";
	}
	@Action(value="loadui")
	public String loadui(){
		ReportUIView view = reportService.getViewByRptCode(rptcode);
		setJsonString("{success:true,msg:'预览加载成功！',result:".concat(parser.parserToSourceCode(view.getMainuivo()).concat("}")));
		return SUCCESS;
	}
	@Action(value="query")
	public String doquery(){
		QueryObject qo = new QueryObject();
		qo.setQueryConditions(conditions);
		Report rpt = reportService.findByCode(rptcode);
		ReportResultSet rrs = reportService.execQuery(rpt, funcCode, getOnlineUser().getUser(), getOnlineUser().getLoginedOrg(), qo, 0, 0);
		// 转换图形的数据
		setJsonString("{success:true,msg:'预览加载成功！',result:".concat(report2Json(rrs,rpt.getView().getMainuivo()).concat("}")));
		return SUCCESS;
	}
	
	/**
	 * 将报表查询结果组织成json的形式
	 * @author slx
	 * @date 2012-5-14下午3:02:33
	 * @param rrs
	 * @param rptui
	 * @return
	 */
	private String report2Json(ReportResultSet rrs,UIComponentVO rptui){
		StringBuffer sf_json = new StringBuffer("{grids:");
		HashMap<String, QueryResult<? extends DataVO>> grids = rrs.getGrids();
		HashMap<String, List<?>> charts = rrs.getCharts();
		if(grids != null){
			boolean frist = true;
			sf_json.append("{");
			for (String key : grids.keySet()) {
				if(!frist){
					sf_json.append(",");
				}
				sf_json.append(key).append(":");
				sf_json.append("{totalrecord:").append(grids.get(key).getTotalrecord()).append(",datas:")
				.append(FastJsonUtils.toJson(grids.get(key).getResultlist())).append("}");
				frist = false;
			}
			sf_json.append("}");
		}else{
			sf_json.append("{}");
		}
		
		sf_json.append(",charts:");
		if(charts !=null){
			boolean frist = true;
			sf_json.append("{");
			for (String key : charts.keySet()) {
				if(!frist){
					sf_json.append(",");
				}
				sf_json.append(key).append(":");
				UIComponentVO chartui = getChartUIVO(rptui,key);
				if(chartui == null){
					sf_json.append("{}");
				}else{
					ChartTypeEnum type = Enum.valueOf(SimpleChartTypeEnum.class, (String)chartui.getValue("ctype"));
					DataDescription datadesc = new DataDescription((String)chartui.getValue("vField"), (String)chartui.getValue("vTitle"), (String)chartui.getValue("nField"), (String)chartui.getValue("nTitle"), (String)chartui.getValue("sField"), (String)chartui.getValue("sTitle"));
					sf_json.append(chartAPI.generateSimpleChartString(type, (String)chartui.getValue("title"), charts.get(key), datadesc));
				}
				frist = false;
			}
			sf_json.append("}");
		}else{
			sf_json.append("{}");
		}
		sf_json.append("}");
		
		return sf_json.toString();
	}
	
	/**
	 * 从ui视图设置中递归查找dataroot相匹配的图形ui控件。
	 * @author slx
	 * @date 2012-5-14下午2:55:02
	 * @param comp
	 * @param dataroot
	 * @return
	 */
	private UIComponentVO getChartUIVO(UIComponentVO comp,String dataroot){
		if(ComponentType.Chart == comp.getType() && dataroot.equals(comp.getValue("dataRoot"))){
			return comp;// 找到匹配的返回，找不到查找下级。
		}else{
			if(ComponentType.Grid == comp.getType() || ComponentType.Field == comp.getType()|| ComponentType.Chart == comp.getType()){
				return null;// 表格 、字段、图表内是不会有图表的。
			}
			for (UIComponentVO child : comp.getChildren()) {// 遍历下级子控件，进入递归。
				UIComponentVO chartui = getChartUIVO(child,dataroot);
				if(chartui!=null){
					return chartui;
				}
			}
		}
		return null; // 全都找不到返回空
	}
	
	/**
	 * 获取查询条件
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
		if(StringUtils.isEmpty(rptcode)){
			throw new BizException("方案编号为空！");
		}
		QueryScheme qs = reportService.getQuerySchemeByRptCode(rptcode);
		Map operator = querySchemeService.getOperator();
		Map val = new HashMap();
		val.put("conditions", qs.getOtherconditions());
		val.put("operator",operator);
		JsonResult jr = new JsonResult(val);
		setJsonString(jr.toJSONString());
		return SUCCESS;
	}
	
	/**
	 * 获取常用条件
	 * @author wangliang
	 * @date 2012-5-16上午11:02:01
	 * @return
	 * @throws SecurityException 
	 * @throws NoSuchFieldException 
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	@Action(value="getQueryCommon")
	public String getQueryCommonConditions() throws NoSuchFieldException, SecurityException{
		JsonResult jr = new JsonResult("");
		QueryScheme qs = reportService.getQuerySchemeByRptCode(rptcode);
		if(qs == null){
			jr.setSuccess(false);
		}else{
			Map operator = querySchemeService.getOperator();
			Map val = new HashMap();
			val.put("conditions", qs.getCommonconditions());
			val.put("operator",operator);
			jr.setSuccess(true);
			jr.setResult(val);
		}
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

	public void setReportService(ReportService reportService) {
		this.reportService = reportService;
	}
	public void setParser(UIViewParser parser) {
		this.parser = parser;
	}
	public void setChartAPI(ChartAPI chartAPI) {
		this.chartAPI = chartAPI;
	}
	public String getRptcode() {
		return rptcode;
	}
	public void setRptcode(String rptcode) {
		this.rptcode = rptcode;
	}
	public List<QueryCondition> getConditions() {
		return conditions;
	}
	public void setConditions(List<QueryCondition> conditions) {
		this.conditions = conditions;
	}
	public void setFuncCode(String funcCode) {
		this.funcCode = funcCode;
	}
	public void setQuerySchemeService(QuerySchemeService querySchemeService) {
		this.querySchemeService = querySchemeService;
	}
}
