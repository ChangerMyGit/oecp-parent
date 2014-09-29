/**
 * oecp-platform - FunctionQueryScheme.java
 * copyright 2009-2012 OECP www.oecp.cn
 * 创建人:songlixiao	创建时间:上午9:59:00		版本:v1
 * ============================================
 * 修改人：			修改时间:					版本:
 * 修改原因:
 *
 * ============================================
 */

package oecp.platform.bcfunction.eo;

import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import oecp.framework.entity.base.StringPKEO;
import oecp.platform.query.setting.eo.QueryScheme;

/** 
 * 业务功能与查询方案关系表
 * @author songlixiao  
 * @date 2014年1月28日 上午9:59:00 
 * @version 1.0
 *  
 */
@Entity
@Table(name = "OECP_SYS_QUERY_SCHEME_FUNC")
public class FunctionQueryScheme extends StringPKEO {
	private static final long	serialVersionUID	= 1L;
	/** 功能 **/
	private Function func;
	
	/** 查询方案 **/
	private QueryScheme queryScheme;
	
	@ManyToOne
	public Function getFunc() {
		return func;
	}

	public void setFunc(Function func) {
		this.func = func;
	}
	
	@ManyToOne
	public QueryScheme getQueryScheme() {
		return queryScheme;
	}

	public void setQueryScheme(QueryScheme queryScheme) {
		this.queryScheme = queryScheme;
	}
}
