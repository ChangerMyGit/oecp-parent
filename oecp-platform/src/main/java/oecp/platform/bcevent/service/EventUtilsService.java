/*
 * Copyright (c) 2011 OECP All Rights Reserved.                	
 * <a href="http://www.oecp.cn">http://www.oecp.cn</a>                                                                 
 */    

package oecp.platform.bcevent.service;

import oecp.framework.exception.BizException;
import oecp.framework.util.soap.ComplexType;

/**
 * 事件工具服务
 * @author slx
 * @date 2011-8-19下午02:01:08
 * @version 1.0
 */
public interface EventUtilsService {

	/**
	 * 根据事件编号得到
	 * 
	 * @author slx
	 * @date 2011-8-19下午02:02:32
	 * @param eventCode
	 * @return
	 */
	public ComplexType getEventSourceType(String eventID) throws BizException ;
}
