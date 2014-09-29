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

import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import oecp.framework.entity.base.StringPKEO;
import oecp.platform.org.eo.Organization;

/**
 * 角色
 * 
 * @author slx
 * @date 2011 5 4 09:46:11
 * @version 1.0
 */
@Entity
@Table(name = "OECP_SYS_ROLE")
public class Role extends StringPKEO {

	private static final long serialVersionUID = 1L;
	/** 编码 **/
	private String code;
	/** 名称 **/
	private String name;
	/** 是否锁定 **/
	private Boolean locked;
	/** 创建组织 **/
	private Organization org;

	private List<OrgRole> orgRoles;

	@OneToMany(mappedBy="role")
	public List<OrgRole> getOrgRoles() {
		return orgRoles;
	}

	public void setOrgRoles(List<OrgRole> orgRoles) {
		this.orgRoles = orgRoles;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Boolean getLocked() {
		return locked;
	}

	public void setLocked(Boolean locked) {
		this.locked = locked;
	}

	@ManyToOne
	public Organization getOrg() {
		return org;
	}

	public void setOrg(Organization org) {
		this.org = org;
	}
}
