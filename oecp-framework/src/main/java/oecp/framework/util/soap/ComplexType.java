/*
 * Copyright (c) 2011 OECP All Rights Reserved.                	
 * <a href="http://www.oecp.cn">http://www.oecp.cn</a>                                                                 
 */    

package oecp.framework.util.soap;

import java.util.List;

/**
 * 复杂类型
 * @author slx
 * @date 2011-8-19上午11:02:17
 * @version 1.0
 */
public class ComplexType {
	/** 类型的名称 **/
	private String name;
	/** 类型内的属性 **/
	private List<Attribute> attrs;
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public List<Attribute> getAttrs() {
		return attrs;
	}
	public void setAttrs(List<Attribute> attrs) {
		this.attrs = attrs;
	}
}
