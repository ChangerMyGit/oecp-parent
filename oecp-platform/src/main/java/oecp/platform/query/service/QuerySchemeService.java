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

package oecp.platform.query.service;

import java.util.Map;

import oecp.framework.service.BaseService;
import oecp.platform.query.setting.eo.QueryScheme;

/** 
 *
 * @author wangliang  
 * @date 2012-5-11 下午3:21:41 
 * @version 1.0
 *  
 */
public interface QuerySchemeService extends
		BaseService<QueryScheme> {
	/**
	 * 根据编号 获取查询方案
	 * @author wangliang
	 * @date 2012-5-11下午3:43:26
	 * @param code
	 * @return
	 */
	public QueryScheme getQuerySchemeByCode(String code);
	public QueryScheme getQuerySchemeByCode_full(String code);
	
	/**
	 * 获取条件符
	 * @author wangliang
	 * @date 2012-5-14上午10:20:33
	 * @return
	 */
	@SuppressWarnings("rawtypes")
	public Map getOperator() throws NoSuchFieldException, SecurityException;
}
