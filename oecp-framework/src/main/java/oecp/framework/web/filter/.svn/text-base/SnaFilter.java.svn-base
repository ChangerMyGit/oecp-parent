/*
 * Copyright (c) 2011 OECP All Rights Reserved.                	
 * http://www.oecp.cn                                                                 
 */

package oecp.framework.web.filter;

import java.io.IOException;
import java.util.Iterator;
import java.util.Set;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import oecp.framework.cache.CacheManager;
import oecp.framework.web.SessionMap;
import oecp.framework.web.WebConstant;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * <p>
 * SNA:Shared Nothing Architecture
 * </p>
 * 集群环境下，session 不共享架构下的Servlet过滤器
 * 
 * @author yongtree
 * @date 2011-5-9 下午01:23:54
 * @version 1.0
 */
public class SnaFilter implements Filter {

	private Log logger = LogFactory.getLog(SnaFilter.class);

	private static boolean cluster = false;

	@Override
	public void destroy() {
		// TODO 记录系统日志
	}

	@Override
	public void doFilter(ServletRequest req, ServletResponse res,
			FilterChain chain) throws IOException, ServletException {
		final HttpServletRequest hrequest = (HttpServletRequest) req;
		final HttpServletResponse hresponse = (HttpServletResponse) res;
		String uri = hrequest.getRequestURI();
		logger.debug("开始SNA拦截-----------------" + uri);
		HttpSession httpSession = hrequest.getSession();
		String sessionId = httpSession.getId();
		long sessionTime = httpSession.getCreationTime();
		SessionMap sessionMap = (SessionMap) CacheManager.get(
				WebConstant.OECP_CACHE_SESSION, sessionId);
		if (sessionMap != null
				&& (Long.valueOf(sessionMap.get("creationTime").toString())) != sessionTime) {// session的创建时间和集群缓存里的session的创建时间不一致，则需要复制session
			// 将cache的数据复制到session中
			initHttpSession(httpSession, sessionMap);
		}
		chain.doFilter(hrequest, hresponse);

	}

	private void initHttpSession(HttpSession session,
			SessionMap sessionMap) {
		Set<String> keySet = sessionMap.keySet();
		Iterator<String> it = keySet.iterator();
		while (it.hasNext()) {
			String key = it.next();
			session.setAttribute(key, sessionMap.get(key));
		}
		sessionMap.put("creationTime", session.getCreationTime());
		CacheManager.set(WebConstant.OECP_CACHE_SESSION, session.getId(),
				sessionMap);
	}

	@Override
	public void init(FilterConfig chain) throws ServletException {
		cluster = true;
	}

	public static boolean isCluster() {
		return cluster;
	}

}
