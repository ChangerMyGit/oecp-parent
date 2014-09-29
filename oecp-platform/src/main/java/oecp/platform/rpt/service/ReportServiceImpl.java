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

package oecp.platform.rpt.service;

import java.io.StringReader;
import java.io.StringWriter;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import oecp.framework.dao.DAO;
import oecp.framework.dao.QLBuilder;
import oecp.framework.dao.QLType;
import oecp.framework.dao.QueryObject;
import oecp.framework.dao.QueryResult;
import oecp.framework.util.DateUtil;
import oecp.framework.util.SpringContextUtil;
import oecp.framework.vo.base.SimpleDataVO;
import oecp.platform.api.datapermission.DataPermissionSQLService;
import oecp.platform.base.service.PlatformBaseServiceImpl;
import oecp.platform.org.eo.Organization;
import oecp.platform.query.setting.annotation.OperatorDescription;
import oecp.platform.query.setting.enums.Operator;
import oecp.platform.query.setting.eo.QueryConditionSetting;
import oecp.platform.query.setting.eo.QueryScheme;
import oecp.platform.rpt.ReportResultSet;
import oecp.platform.rpt.setting.eo.Report;
import oecp.platform.rpt.setting.eo.ReportUIView;
import oecp.platform.uiview.eo.UIComponent;
import oecp.platform.uiview.utils.UIComponentObjUtils;
import oecp.platform.user.eo.User;

import org.apache.commons.lang.StringUtils;
import org.hibernate.Query;
import org.hibernate.Session;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import freemarker.template.Configuration;
import freemarker.template.Template;
import groovy.lang.Binding;
import groovy.lang.GroovyShell;


/**
 * 报表服务实现类
 * 
 * @author slx
 * @date 2012-4-27 上午10:11:24
 * @version 1.0
 * 
 */
@Service("reportService")
public class ReportServiceImpl extends PlatformBaseServiceImpl<Report> implements ReportService {

	@Resource(name = "dataPermissionSQLService")
	protected DataPermissionSQLService dataPerService;

	@Override
	public Report findByCode(String rptCode) {
		Report report = this.getDao().findByWhere(Report.class, " o.code=? ", new Object[] { rptCode });
		report.loadLazyAttributes();
		// EO 转 VO 后返回
		if(report.getView()!=null){
			UIComponent uieo = report.getView().getMainui();
			report.getView().setMainuivo(UIComponentObjUtils.transfer2UIVO(uieo));
		}
		if(report.getQueryscheme()!=null){
			report.getQueryscheme().getFixedconditions().size();
			report.getQueryscheme().getCommonconditions().size();
			report.getQueryscheme().getOtherconditions().size();
		}
		return report;
	}

	@Override
	@Transactional
	public ReportUIView getViewByRptCode(String rptCode) {
		ReportUIView view = getDao().findByWhere(ReportUIView.class, " o.id = (SELECT rpt.view.id FROM Report rpt WHERE rpt.code=? )", new Object[] { rptCode });
		if(view !=null){
			view.loadLazyAttributes();
			// EO 转 VO 后返回
			UIComponent uieo = view.getMainui();
			view.setMainuivo(UIComponentObjUtils.transfer2UIVO(uieo));
		}
		return view;
	}

	@Override
	public QueryScheme getQuerySchemeByRptCode(String rptCode) {
		QueryScheme qs = getDao().findByWhere(QueryScheme.class, " o.id = (SELECT rpt.queryscheme.id FROM Report rpt WHERE rpt.code=? )", new Object[] { rptCode });
		qs.loadLazyAttributes();
		return qs;
	}

	@Override
	public ReportResultSet execQuery(Report rpt, String funcCode, User user, Organization org, QueryObject qo, int start, int limit) {
		if (rpt == null) {
			return null;
		}
		List<QueryConditionSetting> fixedconditions = rpt.getQueryscheme().getFixedconditions();
		// 添加固定条件
		if(fixedconditions!=null && fixedconditions.size()>0){
			for (QueryConditionSetting con : fixedconditions) {
				String operator = null;
				try {
					operator = Operator.class.getField(con.getOperators().get(0).name()).getAnnotation(OperatorDescription.class).operator();
				} catch (Exception e) {
					throw new RuntimeException("操作符配置错误！！");
				} 
				qo.addCondition(con.getField(),operator , processDefaultValue(con.getDefaultvalue(), user, org, funcCode));
			}
		}
		
		DAO rptdao = getRptDao(rpt);
		String ql = rpt.getQlstr();
		if (rpt.getDoqlscript()) {
			// 执行查询前加工脚本
			String ql_t = execQLScript(rpt.getQlscript(),rptdao, ql, qo, funcCode, user, org);
			if(StringUtils.isNotEmpty(ql_t)){
				ql = ql_t;
			}
		} 
		String where = qo.getWhereQL();
		Object[] params = qo.getQueryParams();
		if(!StringUtils.isEmpty(where)){
			ql = QLBuilder.appendWhere(ql,where);
//			ql = ql.concat(" AND ").concat(where);
		}

		// 执行查询
		QueryResult<SimpleDataVO> qr = rptdao.queryScrollVOs(SimpleDataVO.class, ql,rpt.getQltype(), params, start, limit);
		ReportResultSet rrs = new ReportResultSet();
		rrs.getGrids().put("grid1", qr);
		rrs.getCharts().put("chart1", qr.getResultlist());
		if (rpt.getDorsscript()) {
			// 执行查询后结果处理脚本
			ReportResultSet rrs_t = execRSScript(rpt.getRsscript(), rptdao, rrs, ql, params, funcCode, user, org);
			if(rrs_t != null){
				rrs = rrs_t;
			}
		}

		return rrs;
	}
	
	private Configuration cfg = new Configuration();
	protected String processDefaultValue(String str_defvalue,User user,Organization org,String funccode){
		try {
			StringReader sr = new StringReader(str_defvalue);
			Template template = new Template("extproesser", sr ,cfg );
			StringWriter sw = new StringWriter();
			
			Map<String ,Object> param = new HashMap<String, Object>();
			param.put("user", user);
			param.put("org", org);
			param.put("now", new Date());
			param.put("dateUtil", new DateUtil());
			param.put("funccode", funccode);
			template.process(param, sw);
			return sw.toString();
		} catch (Exception e) {
			throw new RuntimeException("默认值设置错误："+e.getMessage());
		}
	}

	/**
	 * 执行SQL加工脚本
	 * @author slx
	 * @date 2012-4-27下午5:10:06
	 * @param script
	 * @param ql
	 * @param qo
	 * @param funcCode
	 * @param user
	 * @param org
	 * @return
	 */
	protected String execQLScript(String script,DAO dao, String ql, QueryObject qo, String funcCode, User user, Organization org) {
		HashMap<String, Object> propertys = new HashMap<String, Object>();
		propertys.put("ql", ql);
		propertys.put("dao", dao);
		propertys.put("qo", qo);
		propertys.put("funcCode", funcCode);
		propertys.put("user", user);
		propertys.put("org", org);
		
		return (String)execGroovy(script, propertys);
	}

	/**
	 * 执行报表结果处理脚本
	 * @author slx
	 * @date 2012-4-27下午5:10:29
	 * @param script
	 * @param dao
	 * @param rrs
	 * @param ql
	 * @param params
	 * @param funcCode
	 * @param user
	 * @param org
	 * @return
	 */
	protected ReportResultSet execRSScript(String script, DAO dao, ReportResultSet rrs, String ql, Object[] params, String funcCode, User user, Organization org) {
		HashMap<String, Object> propertys = new HashMap<String, Object>();
		propertys.put("dao", dao);
		propertys.put("rrs", rrs);
		propertys.put("ql", ql);
		propertys.put("params", params);
		propertys.put("funcCode", funcCode);
		propertys.put("user", user);
		propertys.put("org", org);
		
		return (ReportResultSet)execGroovy(script, propertys);
	}

	/**
	 * 执行Groovy语言的脚本
	 * @author slx
	 * @date 2012-4-27下午5:10:41
	 * @param script
	 * @param propertys
	 * @return
	 */
	protected Object execGroovy(String script,HashMap<String, Object> propertys){
		Binding bd = new Binding();
		Iterator<String> it_keys = propertys.keySet().iterator();
		while (it_keys.hasNext()) {
			String key = (String) it_keys.next();
			bd.setProperty(key, propertys.get(key));
		}
		GroovyShell shell = new GroovyShell(bd);
		return shell.evaluate(script);
	}

	/**
	 * 获得报表查询使用的dao
	 * @author slx
	 * @date 2012-4-27下午5:11:36
	 * @param rpt
	 * @return
	 */
	protected DAO getRptDao(Report rpt) {
		String daoname = rpt.getDaobeanname();
		return (DAO) SpringContextUtil.getBean(daoname);
	}

	public void setDataPerService(DataPermissionSQLService dataPerService) {
		this.dataPerService = dataPerService;
	}

}
