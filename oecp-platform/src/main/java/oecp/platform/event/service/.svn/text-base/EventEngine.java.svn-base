package oecp.platform.event.service;
/*
 * Copyright (c) 2012 OECP All Rights Reserved.                	
 * <a href="http://www.oecp.cn">OECP</a> 
*/                                                                

import oecp.platform.org.eo.Organization;

/** 
 *	事件引擎类接口类
 * @author wangliang  
 * @date 2012-2-8 上午9:10:38 
 * @version 1.0
 *  
 */
public interface EventEngine {
	/**
	 * 事件触发接口
	 * @author wangliang
	 * @date 2012-2-9上午10:32:33
	 * @param source	事件源
	 * @param eventName	事件名称
	 * @param org		事件发生组织
	 * @param objects	其他参数
	 */
	public void fireEvent(Object source,String eventName,Organization org,Object...objects);

}
