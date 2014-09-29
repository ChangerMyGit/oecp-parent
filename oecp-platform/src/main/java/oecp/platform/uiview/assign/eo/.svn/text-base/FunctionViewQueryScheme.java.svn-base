/**
 * oecp-platform - FunctionViewQueryScheme.java
 * copyright 2009-2012 OECP www.oecp.cn
 * 创建人:songlixiao	创建时间:下午5:10:20		版本:v1
 * ============================================
 * 修改人：			修改时间:					版本:
 * 修改原因:
 *
 * ============================================
 */

package oecp.platform.uiview.assign.eo;

import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import oecp.framework.entity.base.StringPKEO;
import oecp.platform.query.setting.eo.QueryScheme;

/** 
 * 功能视图与查询方案关联关系
 * @author songlixiao  
 * @date 2014年2月10日 下午5:10:20 
 * @version 1.0
 *  
 */
@Entity
@Table(name="OECP_SYS_UI_FUNCVIEW_QUERY")
public class FunctionViewQueryScheme extends StringPKEO{
	
	private static final long	serialVersionUID	= 1L;
	/** 功能UI视图 **/
	private FunctionView funcView;
	/** 查询方案 **/
	private QueryScheme queryScheme;
	@ManyToOne
	public FunctionView getFuncView() {
		return funcView;
	}
	public void setFuncView(FunctionView funcView) {
		this.funcView = funcView;
	}
	@ManyToOne
	public QueryScheme getQueryScheme() {
		return queryScheme;
	}
	public void setQueryScheme(QueryScheme queryScheme) {
		this.queryScheme = queryScheme;
	}
}
