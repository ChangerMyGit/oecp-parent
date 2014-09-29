/*
 * Copyright (c) 2011 OECP All Rights Reserved.                	
 * <a href="http://www.oecp.cn">http://www.oecp.cn</a>                                                                 
 */

package oecp.framework.vo.base;


/**
 * 普通VO父类
 * 
 * @author slx
 * @date 2011-8-23下午04:03:23
 * @version 1.0
 */
public abstract class BaseNormalVO implements DataVO {

	private BaseNormalVOUtility util;
	public BaseNormalVO() {
		util = new BaseNormalVOUtility(getClass());
	}

	public String[] getFieldNames() {
		return util.getFieldNames();
	}

	public Object getValue(String fieldname) {
		return util.getValue(this, fieldname);
	}

	public void setValue(String fieldname, Object value) {
		util.setValue(this, fieldname, value);
	}

	@Override
	public Class<?> getFieldType(String fieldname) {
		return util.getFieldType(fieldname);
	}

}
