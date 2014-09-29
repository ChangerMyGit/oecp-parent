/*
 * Copyright (c) 2011 OECP All Rights Reserved.                	
 * http://www.oecp.cn                                                                 
 */

package oecp.platform.web;

import java.io.Serializable;
import java.util.Date;

import javax.servlet.http.HttpSession;

import oecp.framework.cache.CacheManager;
import oecp.framework.util.web.struts2.Struts2Utils;
import oecp.framework.web.SessionMap;
import oecp.framework.web.WebConstant;
import oecp.framework.web.filter.SnaFilter;
import oecp.platform.org.eo.Organization;
import oecp.platform.user.eo.User;

/**
 * 在线用户
 * 
 * @author yongtree
 * @date 2011-4-28 下午05:55:02
 * @version 1.0
 */
public class OnlineUser implements Serializable {

	private static final long serialVersionUID = 2027381246939314102L;

	private User user;

	private Organization loginedOrg;

	private Date loginTime;

	private Date lastActiveTime;

	private SessionMap data = new SessionMap();
	
	private String sessionId;
	
	private String accessToken;
	
	
	
	public String getAccessToken() {
		return accessToken;
	}

	public void setAccessToken(String accessToken) {
		this.accessToken = accessToken;
	}

	public String getSessionId() {
		return sessionId;
	}

	/**
	 * 可将和该用户相关的session数据放到其中，不建议将大量的数据放入，以免在占用过多内存和同步缓存影响性能
	 * @author yongtree
	 * @date 2011-5-17 上午10:51:31
	 * @return
	 */
	public SessionMap getData() {
		return data;
	}


	/**
	 * 登录
	 * 
	 * @author yongtree
	 * @date 2011-5-10 下午03:47:01
	 */
	public void login() {
		this.loginTime = new Date();
		activating();
	}

	/**
	 * 退出登录
	 * 
	 * @author yongtree
	 * @date 2011-5-10 下午03:47:31
	 */
	public void logout() {
		HttpSession session = Struts2Utils.getSession();
		session.removeAttribute(WebConstant.OECP_SESSION_ONLINEUSER);// 将在线用户清除出session中
		if (!SnaFilter.isCluster()) {// 不是集群环境中，则需要清除掉Cache
			CacheManager.evict(WebConstant.OECP_CACHE_ONLINEUSER, session
					.getId());// 将在线用户放到Cache中
		}
	}

	/**
	 * 活动
	 * 
	 * @author yongtree
	 * @date 2011-5-10 下午03:50:12
	 */
	public void activating() {
		HttpSession session = Struts2Utils.getSession();
		this.sessionId=session.getId();
		session.setAttribute(WebConstant.OECP_SESSION_ONLINEUSER, this);// 将在线用户放在session中
		if (!SnaFilter.isCluster()) {// 不是集群环境中，则需要把在线用户单独放在cache中，集群环境下，由于session和cache同步，则不需要再次放在session下
			CacheManager.set(WebConstant.OECP_CACHE_ONLINEUSER,
					session.getId(), this);// 将在线用户放到Cache中
		}
		this.lastActiveTime = new Date();
	}

	public Date getLastActiveTime() {
		return lastActiveTime;
	}

	public Date getLoginTime() {
		return loginTime;
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	public Organization getLoginedOrg() {
		return loginedOrg;
	}

	public void setLoginedOrg(Organization loginedOrg) {
		this.loginedOrg = loginedOrg;
	}

}
