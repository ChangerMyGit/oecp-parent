package oecp.platform.ref.web;

import oecp.framework.vo.base.BaseNormalVO;

/**
 * Extjs表格过滤器值vo
 * 
 * @author wl
 * 
 */
public class QueryFilterData extends BaseNormalVO {
	// 数据类型
	private String type;
	// 值
	private String value;
	// 条件符号 gt lt
	private String comparison;
	
	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}

	public String getComparison() {
		return comparison;
	}

	public void setComparison(String comparison) {
		this.comparison = comparison;
	}

}
