/*
 * Copyright (c) 2011 OECP All Rights Reserved.                	
 * http://www.oecp.cn                                                                 
 */

package oecp.framework.auth;

import java.io.Serializable;
import java.util.Date;

/**
 * 在线用户
 * 
 * @author yongtree
 * @date 2011-4-27 下午04:17:04
 * @version 1.0
 */
public class OnlineUser implements Serializable {

	private static final long serialVersionUID = 1L;

	private String sessionId;//将request.getSession().getId()赋值该属性

	private String id;

	private String loginId;

	private String name;

	private String orgId;

	private String orgName;

	private Date loginTime;

	public void login() {
		// TODO 登录后调用该方法,将该用户存放在全局缓存中
		
	}

	public void logout() {
		// TODO 登出时调用该方法，将该用户从全局缓存中清除

	}

	public Date getLoginTime() {
		return loginTime;
	}

	public void setLoginTime(Date loginTime) {
		this.loginTime = loginTime;
	}

	public String getSessionId() {
		return sessionId;
	}

	public void setSessionId(String sessionId) {
		this.sessionId = sessionId;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getLoginId() {
		return loginId;
	}

	public void setLoginId(String loginId) {
		this.loginId = loginId;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getOrgId() {
		return orgId;
	}

	public void setOrgId(String orgId) {
		this.orgId = orgId;
	}

	public String getOrgName() {
		return orgName;
	}

	public void setOrgName(String orgName) {
		this.orgName = orgName;
	}

}
