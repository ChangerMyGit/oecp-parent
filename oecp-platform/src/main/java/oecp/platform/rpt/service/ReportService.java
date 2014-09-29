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

import oecp.framework.dao.QueryObject;
import oecp.framework.service.BaseService;
import oecp.platform.org.eo.Organization;
import oecp.platform.query.setting.eo.QueryScheme;
import oecp.platform.rpt.ReportResultSet;
import oecp.platform.rpt.setting.eo.Report;
import oecp.platform.rpt.setting.eo.ReportUIView;
import oecp.platform.user.eo.User;

/** 
 * 报表服务接口
 * @author slx  
 * @date 2012-4-27 上午9:07:58 
 * @version 1.0
 *  
 */
public interface ReportService extends BaseService<Report> {

	/**
	 * 根据报表编号找到报表的设置
	 * @author slx
	 * @date 2012-4-27上午9:08:56
	 * @param rptCode
	 * @return
	 */
	public Report findByCode(String rptCode);
	
	/**
	 * 根据报表编号获得报表视图配置
	 * @author slx
	 * @date 2012-4-27上午10:03:49
	 * @param rptCode
	 * @return
	 */
	public ReportUIView getViewByRptCode(String rptCode);
	
	/**
	 * 根据报表编号获得报表的查询方案
	 * @author slx
	 * @date 2012-4-27上午10:04:08
	 * @param rptCode
	 * @return
	 */
	public QueryScheme getQuerySchemeByRptCode(String rptCode);
	
	/**
	 * 执行查询。除了需要报表编号外，还需要传入所挂接的功能编号和用户，以便实现数据权限的控制(数据权限控制使用脚本控制)。
	 * （如需分页，需传入分页参数）
	 * @author slx
	 * @date 2012-4-27上午10:04:30
	 * @param Report
	 * 		报表配置
	 * @param funcCode
	 * 		所挂接的功能编号
	 * @param user
	 * 		正在执行查询的用户
	 * @param org
	 * 		用户所登录的公司
	 * @param qo
	 * 		查询对象(尚未进行过脚本加工的）
	 * @param start
	 * 		开始行
	 * @param limit
	 * 		每页行数
	 * @return
	 */
	public ReportResultSet execQuery(Report rpt,String funcCode,User user,Organization org,QueryObject qo,int start,int limit);
}
