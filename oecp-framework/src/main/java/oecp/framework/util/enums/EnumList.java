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

import java.util.ArrayList;

import oecp.framework.util.entity.EOUtility;


/**
 * 枚举内容列表
 * @author slx
 * @date 2009-12-7 下午01:22:14
 * @version 1.0
 */
public class EnumList extends ArrayList<EnumItem> {

	public EnumList(Class<? extends Enum> c) {
		Enum[] es = c.getEnumConstants();
		for (Enum e : es) {
			EnumItem item = new EnumItem(e,EOUtility.getEnumDescription(e));
			add(item);
		}
	}
}
