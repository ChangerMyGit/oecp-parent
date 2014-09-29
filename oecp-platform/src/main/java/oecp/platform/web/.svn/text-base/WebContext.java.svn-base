/*
 * Copyright (c) 2011 OECP All Rights Reserved.                	
 * http://www.oecp.cn                                                                 
 */

package oecp.platform.web;

import java.util.ArrayList;
import java.util.List;

import oecp.framework.cache.Cache;
import oecp.framework.cache.CacheManager;
import oecp.framework.util.web.struts2.Struts2Utils;
import oecp.framework.web.SessionMap;
import oecp.framework.web.WebConstant;
import oecp.framework.web.filter.SnaFilter;

/**
 * OECP平台web上下文，从中可以取到某些上下文的对象
 * 
 * @author yongtree
 * @date 2011-4-28 下午05:15:17
 * @version 1.0
 */
public class WebContext {

	@SuppressWarnings("unchecked")
	public static List<OnlineUser> getOnlineUsers() {
		List<OnlineUser> users = new ArrayList<OnlineUser>();
		if (!SnaFilter.isCluster()) {// 如果不是集群环境下
			Cache cache = CacheManager
					.getCache(WebConstant.OECP_CACHE_ONLINEUSER);
			List<String> sessionIds = cache.keys();
			for (String sessionId : sessionIds) {
				OnlineUser user = (OnlineUser) cache.get(sessionId);
				if (user != null && user.getUser() != null
						&& user.getLoginedOrg() != null) {
					users.add(user);
				}
			}
		} else {
			Cache cache = CacheManager.getCache(WebConstant.OECP_CACHE_SESSION);
			List<String> sessionIds = cache.keys();
			for (String sessionId : sessionIds) {
				SessionMap sessionMap = (SessionMap) cache
						.get(sessionId);
				if (sessionMap != null
						&& sessionMap.get(WebConstant.OECP_SESSION_ONLINEUSER) != null) {
					OnlineUser user = (OnlineUser) sessionMap
							.get(WebConstant.OECP_SESSION_ONLINEUSER);
					if (user != null && user.getUser() != null
							&& user.getLoginedOrg() != null) {
						users.add(user);
					}
				}
			}
		}
		return users;
	}

	public static OnlineUser getCurrentUser() {
		OnlineUser onlineUser = (OnlineUser) Struts2Utils.getSession()
				.getAttribute(WebConstant.OECP_SESSION_ONLINEUSER);
		if(onlineUser!=null){			
			onlineUser.activating();
		}
		return onlineUser;
	}

	public static OnlineUser getOnlineUser(String sessionId) {
		OnlineUser user;
		if (!SnaFilter.isCluster()) {
			user = (OnlineUser) CacheManager.get(
					WebConstant.OECP_CACHE_ONLINEUSER, sessionId);
		} else {
			SessionMap sessionMap = (SessionMap) CacheManager
					.get(WebConstant.OECP_CACHE_SESSION, sessionId);
			user = (OnlineUser) sessionMap
					.get(WebConstant.OECP_SESSION_ONLINEUSER);
		}
		if (user != null && user.getUser() != null && user.getLoginedOrg() != null)
			return user;
		return null;
	}

}
