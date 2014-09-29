/************************* 版权声明 *********************************
 *                                                                  *
 *                     版权所有：百洋软件                          				 	*
 *          Copyright (c) 2011 by www.oecp.cn                	*
 *                                                                  *
 ************************* 变更记录 *********************************
 *
 * 创建者：宋黎晓   创建日期： 2011 5 5
 * 备注：
 * 
 * 修改者：       修改日期：
 * 备注：
 * 
 */    

package oecp.platform.permission.eo;

import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import oecp.framework.entity.base.StringPKEO;
import oecp.platform.bcfunction.eo.Function;
import oecp.platform.role.eo.OrgRole;

/**
 * 权限分配EO
 * @author slx
 * @date 2011 5 5 12:13:32
 * @version 1.0
 */
@Entity
@Table(name="OECP_SYS_PERMISSION")
public class Permission extends StringPKEO {

	private static final long serialVersionUID = 1L;
	/** 公司角色 **/
	private OrgRole orgRole;
	/** 功能操作 **/
	private Function function;
	/** 分配的UI **/
	private List<PermissionFuncUI> permissionFuncUIs;
	
	@ManyToOne
	public OrgRole getOrgRole() {
		return orgRole;
	}
	public void setOrgRole(OrgRole orgRole) {
		this.orgRole = orgRole;
	}
	
	@ManyToOne
	public Function getFunction() {
		return function;
	}
	public void setFunction(Function function) {
		this.function = function;
	}
	
	@OneToMany(mappedBy="permission",cascade={CascadeType.MERGE,CascadeType.PERSIST,CascadeType.REMOVE})
	public List<PermissionFuncUI> getPermissionFuncUIs() {
		return permissionFuncUIs;
	}
	public void setPermissionFuncUIs(List<PermissionFuncUI> permissionFuncUIs) {
		this.permissionFuncUIs = permissionFuncUIs;
	}
	
}
