/*
 * Copyright (c) 2011 OECP All Rights Reserved.                	
 * http://www.oecp.cn                                                                 
 */    

package oecp.platform.user.eo;

import java.util.Date;
import java.util.List;

import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import oecp.framework.entity.base.StringPKEO;
import oecp.platform.org.eo.Organization;
import oecp.platform.role.eo.OrgRole;
import oecp.platform.user.enums.UserState;

/**
 * 用户/账号
 * @author yongtree
 * @date 2011-4-27 下午04:25:15
 * @version 1.0
 */
@Entity
@Table(name="OECP_SYS_USER")
public class User extends StringPKEO{

	private static final long serialVersionUID = 1L;

	private String loginId;
	
	private String name;
	
	private String password;
	
	private Date createTime;
	
	private Date lastLoginTime;
	
	private String email;//绑定员工后，可以将员工的邮箱覆盖到此，如果不绑定员工，则自定义一个email

	private String personId;//绑定员工ID
	
	private UserState state;
	
	private List<OrgRole> orgRoles;
	
	private Organization createdByOrg;
	
	@ManyToOne
	public Organization getCreatedByOrg() {
		return createdByOrg;
	}

	public void setCreatedByOrg(Organization createdByOrg) {
		this.createdByOrg = createdByOrg;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public UserState getState() {
		return state;
	}

	public void setState(UserState state) {
		this.state = state;
	}

	public String getPersonId() {
		return personId;
	}

	public void setPersonId(String personId) {
		this.personId = personId;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getLoginId() {
		return loginId;
	}

	public void setLoginId(String loginId) {
		this.loginId = loginId;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public Date getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}

	public Date getLastLoginTime() {
		return lastLoginTime;
	}

	public void setLastLoginTime(Date lastLoginTime) {
		this.lastLoginTime = lastLoginTime;
	}

	@ManyToMany()
	@JoinTable(name="OECP_SYS_USER_ORGROLE",
			joinColumns={@JoinColumn(name="user_pk")},
			inverseJoinColumns={@JoinColumn(name="orgrole_pk")})
	public List<OrgRole> getOrgRoles() {
		return orgRoles;
	}

	public void setOrgRoles(List<OrgRole> orgRoles) {
		this.orgRoles = orgRoles;
	}
	
}
