/************************* 版权声明 *********************************
 * 
 * 版权所有：百洋软件
 * Copyright (c) 2009 by Pearl Ocean.
 * 
 ************************* 变更记录 *********************************
 *
 * 创建者：slx   创建日期： 2009-6-24
 * 创建记录：创建类结构。
 * 
 * 修改者：       修改日期：
 * 修改记录：
 ************************* 随   笔 *********************************
 *
 * 这里可以写写感想，感慨，疑问什么的。
 * 
 ******************************************************************
 */

package oecp.framework.dao;

import java.io.Serializable;
import java.lang.reflect.Constructor;
import java.text.ParseException;

import oecp.framework.util.DateUtil;

import org.apache.commons.lang.StringUtils;

/**
 * 条件对象
 * 
 * @author slx
 * @date 2009-6-24 上午10:53:46
 * @version 1.0
 */
public class QueryCondition implements Serializable, Cloneable {

	private static final long serialVersionUID = 1L;

	/** 字段名（表名.列名） **/
	private String field;

	/** 操作符 **/
	private String operator;

	/** 值 **/
	private Object value;

	/** 类型 **/
	private String fieldType;

	public QueryCondition() {
	}

	@Override
	public Object clone() throws CloneNotSupportedException {
		return super.clone();
	}

	public QueryCondition(String field, String operator, Object value) {
		this.field = field;
		this.operator = operator;
		this.value = value;
	}

	public String getField() {
		return field;
	}

	public void setField(String field) {
		this.field = field;
	}

	public String getOperator() {
		return operator;
	}

	public void setOperator(String operator) {
		this.operator = operator;
	}

	@SuppressWarnings("unchecked")
	public Object getValue() {
		if (this.value instanceof String[]) {
			this.value = ((String[]) this.value)[0];
		}
		if (this.value == null || StringUtils.isEmpty(this.value.toString())) {
			return null;
		}
		if (StringUtils.isNotEmpty(this.getFieldType()) && !this.value.getClass().getName().equals(this.getFieldType())
				&& !String.class.getName().equals(this.getFieldType())) {
			Class<?> clazz = null;
			try {
				clazz = Class.forName(this.getFieldType());
			} catch (ClassNotFoundException e) {
				throw new RuntimeException("Formula Error ,ClassNotFound : ".concat(this.getFieldType()));
			}
			if (fieldType.startsWith("java.lang.")) {// 处理基本数据类型 数值 布尔
				try {
					Constructor<?> constructor = clazz.getConstructor(String.class);
					this.value = constructor.newInstance((String) this.value);
				} catch (NoSuchMethodException nse) {
					throw new RuntimeException("Formula Error , no Constructor(String) : ".concat(this.getFieldType()));
				} catch (Exception e) {
					throw new RuntimeException(e);
				}
			} else if (java.sql.Date.class.equals(clazz)) {// 处理日期类
				this.value = java.sql.Date.valueOf((String) this.value);
			} else if (java.util.Date.class.equals(clazz)) {// 处理日期类
				String format = "yyyy-MM-dd";
				if (this.value.toString().length() == 19) {
					format = "yyyy-MM-dd HH:mm:ss";
				}else if((this.value.toString().length() == 23)){
					format = "yyyy-MM-dd HH:mm:ss.SSS";
				}
				try {
					this.value = DateUtil.parseDate((String) this.value, format);
				} catch (ParseException e) {
					throw new RuntimeException(e);
				}
			} else if (clazz.isEnum()) {// 处理枚举
				this.value = Enum.valueOf((Class<? extends Enum>) clazz, (String) this.value);
			}
		}
		return value;
	}

	public void setValue(Object value) {
		this.value = value;
	}

	public String getFieldType() {
		if (StringUtils.isEmpty(fieldType)) {
			fieldType = String.class.getName();
		}
		return fieldType;
	}

	public void setFieldType(String fieldType) {
		this.fieldType = fieldType;
	}
}
