package oecp.platform.ref.web;

import oecp.framework.vo.base.BaseNormalVO;

/**
 * Extjs表格过滤器vo
 * 
 * @author wl
 * 
 */
public class QueryFilter extends BaseNormalVO {
	// 过滤字段名
	private String field;
	// 过滤值
	private QueryFilterData data;

	public String getField() {
		return field;
	}

	public void setField(String field) {
		this.field = field;
	}

	public QueryFilterData getData() {
		return data;
	}

	public void setData(QueryFilterData data) {
		this.data = data;
	}

	public String buildWhereHql() {
		String operator = "";
		String value = "";
		if (data != null) {
			String type = data.getType();
			if ("string".equals(type.toLowerCase())) {
				operator = " LIKE ";
				value = "('%" + data.getValue() + "%')";
			} else if ("numeric".equals(type.toLowerCase())) {
				operator = transformOperator(type.toLowerCase());
				value = data.getValue();
			} else if ("bool".equals(type.toLowerCase())
					|| "boolean".equals(type.toLowerCase())) {
				operator = " = ";
				value = data.getValue();
			} else if ("date".equals(type.toLowerCase())) {
				// TODO 日期类型格式如何处理 目前传过来的默认格式是 MM/dd/yyyy
				operator = transformOperator(type.toLowerCase());
				value = "'" + data.getValue() + "'";
			} else {
				operator = " = ";
				value = "'" + data.getValue() + "'";
			}
			return this.field + operator + value;
		}
		return "";
	}

	/**
	 * 将filter传入的字符符号转换为数据库条件符
	 * 
	 * @param operator
	 * @return
	 */
	public static final String transformOperator(String operator) {
		String sign = " = ";
		if ("lt".equals(operator)) {
			sign = " < ";
		} else if ("gt".equals(operator)) {
			sign = " > ";
		} else if ("eq".equals(operator)) {
			sign = " = ";
		}
		return sign;
	}
}
