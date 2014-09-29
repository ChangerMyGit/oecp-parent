/**
 * oecp-platform - FunctionQSService.java
 * copyright 2009-2012 OECP www.oecp.cn
 * 创建人:songlixiao	创建时间:上午10:10:37		版本:v1
 * ============================================
 * 修改人：			修改时间:					版本:
 * 修改原因:
 *
 * ============================================
 */

package oecp.platform.bcfunction.service;

import java.util.List;

import oecp.framework.exception.BizException;
import oecp.framework.service.BaseService;
import oecp.platform.bcfunction.eo.FunctionQueryScheme;
import oecp.platform.query.setting.eo.QueryScheme;

/** 
 * 业务功能与查询方案关系服务
 * @author songlixiao  
 * @date 2014年1月28日 上午10:10:37 
 * @version 1.0
 *  
 */
public interface FunctionQSService extends BaseService<FunctionQueryScheme> {

	/**
	 * 根据功能id获得此功能下所有的查询方案
	 * @author songlixiao
	 * @date 2014年1月28日上午10:15:35
	 * @param funcId
	 * @return
	 */
	public List<QueryScheme> getQuerySchemesByFuncId(String funcId);
	
	/**
	 * 保存查询方案
	 * 如果qs的id为空，说明是新增。否则为更新
	 * @author songlixiao
	 * @date 2014年1月28日上午10:17:28
	 * @param funcId
	 * @param qs
	 */
	public void saveQueryScheme(String funcId,QueryScheme qs) throws BizException;
	
	/**
	 * 删除查询方案
	 * @author songlixiao
	 * @date 2014年1月28日上午10:19:08
	 * @param funcId
	 * @param qsId
	 */
	public void deleteQueryScheme(String funcId,String qsId) throws BizException;
}
