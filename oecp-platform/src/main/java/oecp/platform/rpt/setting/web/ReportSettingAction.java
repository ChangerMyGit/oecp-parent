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

package oecp.platform.rpt.setting.web;

import java.beans.IntrospectionException;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.util.List;

import javax.annotation.Resource;
import javax.persistence.Transient;

import oecp.framework.dao.QueryCondition;
import oecp.framework.dao.QueryResult;
import oecp.framework.entity.base.BaseEO;
import oecp.framework.exception.BizException;
import oecp.framework.util.FastJsonUtils;
import oecp.framework.vo.base.DataVO;
import oecp.framework.web.JsonResult;
import oecp.platform.rpt.setting.eo.Report;
import oecp.platform.rpt.setting.service.ReportSettingService;
import oecp.platform.uiview.assign.service.FunctionViewServiceImpl;
import oecp.platform.uiview.vo.UIComponentVO;
import oecp.platform.uiview.web.parser.UIViewParser;
import oecp.platform.web.BasePlatformAction;

import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.Namespace;
import org.apache.struts2.convention.annotation.ParentPackage;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import com.alibaba.fastjson.serializer.PropertyFilter;

/**
 * 报表设置action
 * 
 * @author slx
 * @date 2012-4-28 上午9:38:10
 * @version 1.0
 * 
 */
@Controller
@Scope("prototype")
@ParentPackage("default-package")
@Namespace("/report/setting")
public class ReportSettingAction extends BasePlatformAction {
	private static final long serialVersionUID = 1L;
	@Resource(name = "reportSettingService")
	private ReportSettingService rptSettingService;
	@Resource(name = "extUIViewParser")
	private UIViewParser parser;
	
	private Report report;

	private List<QueryCondition> conditions;

	@Action(value = "list")
	public String list() {
		try {
			QueryResult<Report> qr_rpt = rptSettingService.queryReports(conditions, start, limit);
			JsonResult jr = new JsonResult(qr_rpt.getTotalrecord().intValue(),qr_rpt.getResultlist());
			jr.setMsg("查询成功");
			jr.setContainFields(new String[] { "id", "code", "daobeanname", "name", "view.title" });
			setJsonString(jr.toJSONString());
		} catch (Exception e) {
			e.printStackTrace();
			setJsonString("{success:false,msg:'"+ e.getMessage() + "'}");
		}
		return SUCCESS;
	}
	
	@Action(value = "daonames")
	public String getDaos(){
		String[] daonames = rptSettingService.getDaoNames();
		StringBuffer sf_string = new StringBuffer("");
		for (int i = 0; i < daonames.length; i++) {
			sf_string.append(i==0?"":",").append("{name:'").append(daonames[i]).append("'}");
		}
		setJsonString("{success:true,msg:'可用dao获取成功',result:["+ sf_string.toString() +"]}");
		return SUCCESS;
	}
	
	@Action(value = "save")
	public String save() throws BizException {
		rptSettingService.save(report);
		setJsonString("{success:true,msg:'保存成功！'}");
		return SUCCESS;
	}
	
	@Action(value = "preview")
	public String preview() throws BizException {
		setJsonString("{success:true,msg:'预览加载成功！',result:".concat(parser.parserToSourceCode(report.getView().getMainuivo()).concat("}")));
		return SUCCESS;
	}

	@Action(value = "load")
	public String load() throws BizException {
		report = rptSettingService.find_full(report.getId());
		setJsonString("{success:true,result:"+FastJsonUtils.toJson(report,new PropertyFilter(){
			@Override
			public boolean apply(Object source, String name, Object value) {
				if("mainuivo".equals(name) || "id".equals(name)){ // 允许输出view中的
					return true;
				}
				if("parent".equals(name) || "items".equals(name)){ // 对于ui控件中的部分属性不输出
					return false;
				}
				
				if(source instanceof BaseEO){
					if(source instanceof DataVO){
						return true;
					}
					PropertyDescriptor[] propertyDescriptors;
					try {
						propertyDescriptors = Introspector.getBeanInfo(source.getClass())
								.getPropertyDescriptors();
						for (int i = 0; i < propertyDescriptors.length; i++) {
							if(name.equals(propertyDescriptors[i].getName())){
								boolean istransient = propertyDescriptors[i].getReadMethod().isAnnotationPresent(Transient.class);
								return !istransient;
							}
						}
					} catch (IntrospectionException e) {
						e.printStackTrace();
					}
					return false;
				}else{
					return true;
				}
			}
		})+"}");
		return SUCCESS;
	}

	@Action(value = "del")
	public String delete() throws BizException {
		rptSettingService.delete(report.getId());
		setJsonString("{success:true,msg:'删除成功！'}");
		return SUCCESS;
	}

	public void setRptSettingService(ReportSettingService rptSettingService) {
		this.rptSettingService = rptSettingService;
	}

	public Report getReport() {
		return report;
	}

	public void setReport(Report report) {
		this.report = report;
	}

	public List<QueryCondition> getConditions() {
		return conditions;
	}

	public void setConditions(List<QueryCondition> conditions) {
		this.conditions = conditions;
	}

	public void setParser(UIViewParser parser) {
		this.parser = parser;
	}
}
