/************************* 版权声明 *********************************
 *                                                                  *
 *                     版权所有：百洋软件                          				 	*
 *          Copyright (c) 2011 by www.oecp.cn                	*
 *                                                                  *
 ************************* 变更记录 *********************************
 *
 * 创建者：宋黎晓   创建日期： 2011-6-16
 * 备注：
 * 
 * 修改者：       修改日期：
 * 备注：
 * 
 */    

package oecp.framework.entity.enums;

import oecp.framework.util.enums.EnumDescription;


/**
 * 数据类型
 * @author slx
 * @date 2011-6-16 下午02:08:02
 * @version 1.0
 */
public enum DataType {
	@EnumDescription("String")
	STRING,
	@EnumDescription("Integer")
	INTEGER,
	@EnumDescription("Long")
	LONG,
	@EnumDescription("Double")
	DOUBLE,
	@EnumDescription("逻辑")
	BOOLEAN,
	@EnumDescription("枚举")
	ENUM,
	@EnumDescription("日期")
	DATE,
	@EnumDescription("时间")
	DATETIME
}
