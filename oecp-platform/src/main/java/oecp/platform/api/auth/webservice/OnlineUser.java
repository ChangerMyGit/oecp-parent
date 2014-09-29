/*
 * Copyright (c) 2011 OECP All Rights Reserved.                	
 * http://www.oecp.cn                                                                 
 */    

package oecp.platform.api.auth.webservice;

import java.io.Serializable;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlType;

/**
 * 在线用户
 * @author yongtree
 * @date 2011-4-28 上午09:55:37
 * @version 1.0
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType
public class OnlineUser implements Serializable{
	
	private static final long serialVersionUID = 1L;

	private String key;
	
	private String userId;//用户主键
	
	private String loginId;//登录的ID
	
	private String orgId;//组织ID

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public String getOrgId() {
		return orgId;
	}

	public void setOrgId(String orgId) {
		this.orgId = orgId;
	}

	public String getKey() {
		return key;
	}

	public void setKey(String key) {
		this.key = key;
	}

	

	public String getLoginId() {
		return loginId;
	}

	public void setLoginId(String loginId) {
		this.loginId = loginId;
	}

	
}
