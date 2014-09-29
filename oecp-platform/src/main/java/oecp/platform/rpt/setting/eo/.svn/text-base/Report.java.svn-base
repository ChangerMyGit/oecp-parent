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

package oecp.platform.rpt.setting.eo;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import oecp.framework.dao.QLType;
import oecp.framework.entity.base.StringPKEO;
import oecp.platform.query.setting.eo.QueryScheme;

/**
 * 报表配置实体
 * 
 * @author slx
 * @date 2012-4-20 下午3:38:25
 * @version 1.0
 */
@Entity
@Table(name = "OECP_SYS_RPT_REPORT")
public class Report extends StringPKEO {
	private static final long serialVersionUID = 1L;
	/** 报表编号 **/
	private String code;
	/** 报表名称 **/
	private String name;
	/** 报表数据源 **/
	private String daobeanname;
	/** 查询语句类型 **/
	private QLType qltype;
	/** 查询语句 **/
	private String qlstr;
	/** 是否使用查询条件加工脚本 **/
	private Boolean doqlscript;
	/** 查询条件加工脚本 **/
	private String qlscript;
	/** 是否使用结果加工脚本 **/
	private Boolean dorsscript;
	/** 结果加工脚本 **/
	private String rsscript;
	/** 查询条件设置 **/
	private QueryScheme queryscheme;
	/** ui视图 **/
	private ReportUIView view;

	/**  **/
	@Column(length = 10, nullable = false)
	public String getCode() {
		return code;
	}
	
	public void setCode(String code) {
		this.code = code;
	}

	@Column(length = 20, nullable = false)
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	@Column(length = 40, nullable = false)
	public String getDaobeanname() {
		return daobeanname;
	}

	public void setDaobeanname(String daobeanname) {
		this.daobeanname = daobeanname;
	}

	@Enumerated(EnumType.STRING)
	@Column(length = 20, nullable = false)
	public QLType getQltype() {
		return qltype;
	}

	public void setQltype(QLType qltype) {
		this.qltype = qltype;
	}

	@Column(length = 4000, nullable = false)
	public String getQlstr() {
		return qlstr;
	}

	public void setQlstr(String qlstr) {
		this.qlstr = qlstr;
	}

	public Boolean getDoqlscript() {
		if (doqlscript == null) {
			return false;
		}
		return doqlscript;
	}

	public void setDoqlscript(Boolean doqlscript) {
		this.doqlscript = doqlscript;
	}

	@Column(length = 4000)
	public String getQlscript() {
		return qlscript;
	}

	public void setQlscript(String qlscript) {
		this.qlscript = qlscript;
	}

	public Boolean getDorsscript() {
		if (dorsscript == null) {
			return false;
		}
		return dorsscript;
	}

	public void setDorsscript(Boolean dorsscript) {
		this.dorsscript = dorsscript;
	}

	@Column(length = 4000)
	public String getRsscript() {
		return rsscript;
	}

	public void setRsscript(String rsscript) {
		this.rsscript = rsscript;
	}

	@ManyToOne(cascade = {CascadeType.PERSIST,CascadeType.MERGE,CascadeType.REMOVE})
	public QueryScheme getQueryscheme() {
		return queryscheme;
	}

	public void setQueryscheme(QueryScheme queryscheme) {
		this.queryscheme = queryscheme;
	}

	@ManyToOne(cascade = {CascadeType.PERSIST,CascadeType.MERGE,CascadeType.REMOVE})
	public ReportUIView getView() {
		return view;
	}

	public void setView(ReportUIView view) {
		this.view = view;
	}
}
