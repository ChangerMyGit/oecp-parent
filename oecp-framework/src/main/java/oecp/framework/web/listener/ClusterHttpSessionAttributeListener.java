/*
 * Copyright (c) 2011 OECP All Rights Reserved.                	
 * http://www.oecp.cn                                                                 
 */

package oecp.framework.web.listener;

import java.io.Serializable;

import javax.servlet.http.HttpSession;
import javax.servlet.http.HttpSessionAttributeListener;
import javax.servlet.http.HttpSessionBindingEvent;

import oecp.framework.cache.CacheManager;
import oecp.framework.web.SessionMap;
import oecp.framework.web.WebConstant;

/**
 * 用于集群环境下对session处理的HttpSessionAttributeListener
 * 
 * @author yongtree
 * @date 2011-5-9 上午10:54:52
 * @version 1.0
 */
public class ClusterHttpSessionAttributeListener implements
		HttpSessionAttributeListener {

	@Override
	public void attributeAdded(HttpSessionBindingEvent event) {
		HttpSession httpSession = event.getSession();
		String attrName = event.getName();
		Object attrValue = event.getValue();
		String sessionId = httpSession.getId();
		SessionMap sessionMap = (SessionMap) CacheManager.get(
				WebConstant.OECP_CACHE_SESSION, sessionId);
		if (sessionMap == null) {
			sessionMap = new SessionMap();
		}
		if (attrValue instanceof Serializable) {
			sessionMap.put(attrName, (Serializable) attrValue);
		}
		CacheManager.set(WebConstant.OECP_CACHE_SESSION, sessionId, sessionMap);

	}

	@Override
	public void attributeRemoved(HttpSessionBindingEvent event) {
		HttpSession httpSession = event.getSession();

		String attrName = event.getName();
		String sessionId = httpSession.getId();
		SessionMap sessionMap = (SessionMap) CacheManager.get(
				WebConstant.OECP_CACHE_SESSION, sessionId);
		if (sessionMap != null) {
			sessionMap.remove(attrName);
			CacheManager.set(WebConstant.OECP_CACHE_SESSION, sessionId,
					sessionMap);
		}

	}

	@Override
	public void attributeReplaced(HttpSessionBindingEvent event) {
		attributeAdded(event);

	}

}
