/*
 * Copyright (c) 2011 OECP All Rights Reserved.                	
 * http://www.oecp.cn                                                                 
 */

package oecp.framework.web.listener;

import javax.servlet.http.HttpSession;
import javax.servlet.http.HttpSessionEvent;
import javax.servlet.http.HttpSessionListener;

import oecp.framework.cache.CacheManager;
import oecp.framework.web.SessionMap;
import oecp.framework.web.WebConstant;

/**
 * 集群环境下的session监听并处理
 * 
 * @author yongtree
 * @date 2011-5-9 上午10:25:48
 * @version 1.0
 */
public class ClusterHttpSessionListener implements HttpSessionListener {

	@Override
	public void sessionCreated(HttpSessionEvent event) {
		HttpSession httpSession = event.getSession();
		String sessionId = httpSession.getId();
		SessionMap sessionMap = new SessionMap();
		sessionMap.put("creationTime", httpSession.getCreationTime());// 复制session的创建时间到缓存中，以用来在session转移时使用
		CacheManager.set(WebConstant.OECP_CACHE_SESSION, sessionId, sessionMap);// 初始化缓存
	}

	@Override
	public void sessionDestroyed(HttpSessionEvent event) {
		HttpSession httpSession = event.getSession();
		String sessionId = httpSession.getId();
		CacheManager.evict(WebConstant.OECP_CACHE_SESSION, sessionId);// 直接清除掉
		httpSession = null;//将session清空
	}

}
