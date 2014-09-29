/*
 * Copyright (c) 2011 OECP All Rights Reserved.                	
 * <a href="http://www.oecp.cn">http://www.oecp.cn</a>                                                                 
 */    

package oecp.platform.bcevent.listener;

/**
 * 业务事件监听接口
 * @author slx
 * @date 2011-8-15下午05:06:19
 * @version 1.0
 */
public interface BCEventListenerItf {
	
	public abstract void onAction(String bcCode,String eventCode,String data,String dataXSD,String[] args);
}
