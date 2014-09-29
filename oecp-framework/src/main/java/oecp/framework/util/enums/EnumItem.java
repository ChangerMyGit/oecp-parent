/************************* 版权声明 *********************************
 * 
 * 版权所有：百洋软件
 * Copyright (c) 2009 by Pearl Ocean.
 * 
 ************************* 变更记录 *********************************
 *
 * 创建者：slx   创建日期： 2009-12-7
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

package oecp.framework.util.enums;

/**
 * 枚举项内容
 * @author slx
 * @date 2009-12-7 下午01:56:56
 * @version 1.0
 */
public class EnumItem{
	private Enum value;
	
	private String name;

	public EnumItem(Enum value , String name) {
		this.value = value;
		this.name = name;
	}
	
	public Enum getValue() {
		return value;
	}

	public void setValue(Enum value) {
		this.value = value;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
	
	
}
