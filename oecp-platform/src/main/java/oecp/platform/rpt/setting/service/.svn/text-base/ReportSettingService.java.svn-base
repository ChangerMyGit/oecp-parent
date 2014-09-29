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

package oecp.platform.rpt.setting.service;

import java.util.List;

import oecp.framework.dao.QueryCondition;
import oecp.framework.dao.QueryResult;
import oecp.framework.service.BaseService;
import oecp.platform.rpt.setting.eo.Report;

/**
 * 报表设置服务类 <br/>
 * 报表包含：基本设置（主体）、查询条件设置、视图设置。使用级联保存
 * 
 * @author slx
 * @date 2012-4-26 上午10:21:41
 * @version 1.0
 * 
 */
public interface ReportSettingService extends BaseService<Report> {

	/**
	 * 获得所有dao的beanname
	 * 
	 * @author slx
	 * @date 2012-4-26上午10:26:40
	 * @return
	 */
	public String[] getDaoNames();

	/**
	 * 利用查询条件列表查询报表
	 * 
	 * @author slx
	 * @date 2012-4-28上午9:46:03
	 * @param queryConditions
	 * @param start
	 * @param limit
	 * @return
	 */
	public QueryResult<Report> queryReports(List<QueryCondition> queryConditions,int start, int limit);
}
