/*
 * Copyright (c) 2011 OECP All Rights Reserved.                	
 * http://www.oecp.cn                                                                 
 */

package oecp.platform.user.enums;

import oecp.framework.util.enums.EnumDescription;

/**
 * 用户的状态
 * 
 * @author yongtree
 * @date 2011-4-27 下午04:41:04
 * @version 1.0
 */
public enum UserState {

	@EnumDescription("禁用")
	disabled, @EnumDescription("启用")
	enabled;
}
