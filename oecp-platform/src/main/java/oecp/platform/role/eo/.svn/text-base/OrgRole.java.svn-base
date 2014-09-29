/************************* 版权声明 *********************************
 *                                                                  *
 *                     版权所有：百洋软件                          				 	*
 *          Copyright (c) 2011 by www.oecp.cn                	*
 *                                                                  *
 ************************* 变更记录 *********************************
 *
 * 创建者：宋黎晓   创建日期： 2011 5 4
 * 备注：
 * 
 * 修改者：       修改日期：
 * 备注：
 * 
 */

package oecp.platform.role.eo;

import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import oecp.framework.entity.base.StringPKEO;
import oecp.platform.org.eo.Organization;
import oecp.platform.user.eo.User;

/**
 * 组织角色
 * 
 * @author slx
 * @date 2011 5 4 09:54:24
 * @version 1.0
 */
@Entity
@Table(name = "OECP_SYS_ORGROLE")
public class OrgRole extends StringPKEO {

	private static final long serialVersionUID = 1L;
	/** 角色 **/
	private Role role;
	/** 分配拥有的公司 **/
	private Organization org;
	/** 锁定 **/
	private Boolean locked;
	/** 用户列表 **/
	private List<User> users;

	@ManyToOne
	public Role getRole() {
		return role;
	}

	public void setRole(Role role) {
		this.role = role;
	}

	@ManyToOne
	public Organization getOrg() {
		return org;
	}

	public void setOrg(Organization org) {
		this.org = org;
	}

	public Boolean getLocked() {
		return locked;
	}

	public void setLocked(Boolean locked) {
		this.locked = locked;
	}

	@ManyToMany(cascade = { CascadeType.MERGE })
	@JoinTable(name = "OECP_SYS_USER_ORGROLE", joinColumns = { @JoinColumn(name = "orgrole_pk") }, inverseJoinColumns = { @JoinColumn(name = "user_pk") })
	public List<User> getUsers() {
		return users;
	}

	public void setUsers(List<User> users) {
		this.users = users;
	}

}
