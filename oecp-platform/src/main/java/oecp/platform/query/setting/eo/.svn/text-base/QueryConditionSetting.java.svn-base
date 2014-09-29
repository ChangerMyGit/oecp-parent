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

import javax.persistence.Column;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.Table;

import oecp.framework.entity.base.StringPKEO;
import oecp.platform.query.setting.enums.Operator;

/**
 * 查询条件设置
 * 
 * @author slx
 * @date 2012-4-24 上午11:51:27
 * @version 1.0
 */
@Entity
@Table(name = "OECP_SYS_QUERY_CONDITION")
public class QueryConditionSetting extends StringPKEO {
	private static final long serialVersionUID = 1L;
	/** 字段名 **/
	private String field;
	/** 显示名称 **/
	private String dispname;
	/** 可用操作符 **/
	private List<Operator> operators;
	/** 默认值 **/
	private String defaultvalue;
	/** 字段类型（全类名） **/
	private String fieldType;
	/** 是否必填条件 **/
	private Boolean required;
	/** 编辑控件设置 **/
	private String editorcfg;

	@Column(length=80 , nullable = false)
	public String getField() {
		return field;
	}

	public void setField(String field) {
		this.field = field;
	}
	@Column(length=20)
	public String getDispname() {
		return dispname;
	}

	public void setDispname(String dispname) {
		this.dispname = dispname;
	}

	@ElementCollection(targetClass = Operator.class, fetch = FetchType.EAGER)
	@JoinTable(name="OECP_SYS_QUERY_CON_OPERATORS")
	@Enumerated(EnumType.STRING)
	@Column(length = 20, nullable = false)
	public List<Operator> getOperators() {
		return operators;
	}

	public void setOperators(List<Operator> operators) {
		this.operators = operators;
	}

	@Column(length=1000)
	public String getDefaultvalue() {
		return defaultvalue;
	}

	public void setDefaultvalue(String defaultvalue) {
		this.defaultvalue = defaultvalue;
	}

	@Column(length=100)
	public String getFieldType() {
		return fieldType;
	}

	public void setFieldType(String fieldType) {
		this.fieldType = fieldType;
	}

	public Boolean getRequired() {
		return required;
	}

	public void setRequired(Boolean required) {
		this.required = required;
	}
	@Column(length=1000)
	public String getEditorcfg() {
		return editorcfg;
	}

	public void setEditorcfg(String editorcfg) {
		this.editorcfg = editorcfg;
	}
}
