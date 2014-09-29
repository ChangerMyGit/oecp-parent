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

package oecp.platform.query.setting.eo;

import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinTable;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import oecp.framework.entity.base.StringPKEO;

/**
 * 查询方案EO。
 * 
 * @author slx
 * @date 2012-4-24 上午11:22:28
 * @version 1.0
 */
@Entity
@Table(name = "OECP_SYS_QUERY_SCHEME")
public class QueryScheme extends StringPKEO {
	private static final long serialVersionUID = 1L;
	/** 方案编号 **/
	private String code;
	/** 方案名称 **/
	private String name;
	/** 固定查询条件 **/
	private List<QueryConditionSetting> fixedconditions;
	/** 常用查询条件 **/
	private List<QueryConditionSetting> commonconditions;
	/** 其他（可选）查询条件 **/
	private List<QueryConditionSetting> otherconditions;

	@Column(length = 20, nullable = false)
	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	@Column(length = 40, nullable = false)
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	@OneToMany(cascade = {CascadeType.PERSIST,CascadeType.MERGE,CascadeType.REMOVE})
	@JoinTable(name = "OECP_SYS_QUERY_CON_FIXED")
	public List<QueryConditionSetting> getFixedconditions() {
		return fixedconditions;
	}

	public void setFixedconditions(List<QueryConditionSetting> fixedconditions) {
		this.fixedconditions = fixedconditions;
	}

	@OneToMany(cascade = {CascadeType.PERSIST,CascadeType.MERGE,CascadeType.REMOVE})
	@JoinTable(name = "OECP_SYS_QUERY_CON_COMMON")
	public List<QueryConditionSetting> getCommonconditions() {
		return commonconditions;
	}

	public void setCommonconditions(List<QueryConditionSetting> commonconditions) {
		this.commonconditions = commonconditions;
	}

	@OneToMany(cascade = {CascadeType.PERSIST,CascadeType.MERGE,CascadeType.REMOVE})
	@JoinTable(name = "OECP_SYS_QUERY_CON_OTHER")
	public List<QueryConditionSetting> getOtherconditions() {
		return otherconditions;
	}

	public void setOtherconditions(List<QueryConditionSetting> otherconditions) {
		this.otherconditions = otherconditions;
	}
}
