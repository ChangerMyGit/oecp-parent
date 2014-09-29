/*
 * Copyright (c) 2011 OECP All Rights Reserved.                	
 * <a href="http://www.oecp.cn">http://www.oecp.cn</a>                                                                 
 */    

package oecp.platform.bcevent.service;

import java.util.List;

import oecp.framework.exception.BizException;
import oecp.framework.service.BaseService;
import oecp.platform.bcevent.eo.EventInfo;
import oecp.platform.bcevent.eo.ListenerInfo;

/**
 * 事件管理服务接口
 * @author slx
 * @date 2011-8-8下午02:41:04
 * @version 1.0
 */
public interface EventService extends BaseService<EventInfo> {

	/**
	 * 获得事件所有的监听器
	 * 
	 * @author slx
	 * @date 2011-8-8下午04:12:32
	 * @param eventID
	 * @return
	 */
	public List<ListenerInfo> getEventListeners(String eventID);
	/**
	 * 添加监听器
	 * 
	 * @author slx
	 * @date 2011-8-8下午02:46:52
	 * @param listener
	 */
	public void addListener(ListenerInfo listener);
	
	/**
	 * 更新监听器
	 * 
	 * @author slx
	 * @date 2011-8-8下午02:47:06
	 * @param listener
	 */
	public void updateListener(ListenerInfo listener);
	
	/**
	 * 根据id查找监听器信息
	 * 
	 * @author slx
	 * @date 2011-8-9下午04:23:09
	 * @param listenerID
	 * @return
	 */
	public ListenerInfo findListener(String listenerID);
	
	/**
	 * 移除监听器
	 * 
	 * @author slx
	 * @date 2011-8-8下午02:47:15
	 * @param listenerID
	 */
	public void removeListener(String listenerID);
	
	/**
	 * 获取某个组件下的所哟事件信息
	 * 
	 * @author slx
	 * @date 2011-8-11上午10:28:37
	 * @param bcInfoID
	 */
	public void updateEventInfoFromBC(String bcInfoID) throws Exception  ;
	
	/**
	 * 发起事件
	 * 
	 * @author slx
	 * @date 2011-8-8下午02:47:31
	 * @param bcCode
	 * @param eventCode
	 * @param dataXML
	 */
	public void fireEvnet(String bcCode,String eventCode,String dataXML)  throws BizException;
}
