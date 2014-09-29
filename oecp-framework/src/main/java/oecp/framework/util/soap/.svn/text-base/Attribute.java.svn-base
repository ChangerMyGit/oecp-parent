/*
 * Copyright (c) 2011 OECP All Rights Reserved.                	
 * <a href="http://www.oecp.cn">http://www.oecp.cn</a>                                                                 
 */    

package oecp.framework.util.soap;

/**
 * 属性
 * @author slx
 * @date 2011-8-19上午11:07:04
 * @version 1.0
 */
public class Attribute {
	/** 属性名 **/
	private String name;
	/** 属性类型名 **/
	private String typename;
	/** 是否复杂类型 **/
	private boolean complextype;
	/** 内部复杂类型 **/
	private ComplexType type; // 非复杂类型时，此属性为空。
	/** 是否可空 **/
	private boolean nullable;
	/** 最少包含个数 **/
	private int minOccurs;
	/** 最多包含个数 **/
	private int maxOccurs; // 为1，表示不是集合。其他都是集合，-1表示无上限，其他表示集合上限。
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getTypename() {
		return typename;
	}
	public void setTypename(String typename) {
		this.typename = typename;
	}
	public boolean isComplextype() {
		return complextype;
	}
	public void setComplextype(boolean complextype) {
		this.complextype = complextype;
	}
	public ComplexType getType() {
		return type;
	}
	public void setType(ComplexType type) {
		this.type = type;
	}
	public boolean isNullable() {
		return nullable;
	}
	public void setNullable(boolean nullable) {
		this.nullable = nullable;
	}
	public int getMinOccurs() {
		return minOccurs;
	}
	public void setMinOccurs(int minOccurs) {
		this.minOccurs = minOccurs;
	}
	public int getMaxOccurs() {
		return maxOccurs;
	}
	public void setMaxOccurs(int maxOccurs) {
		this.maxOccurs = maxOccurs;
	}
}
