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
import javax.persistence.FetchType;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import oecp.framework.entity.base.StringPKEO;
import oecp.platform.bcfunction.eo.FunctionUI;

/**
 * 分配给用户的ui权限
 * @author slx
 * @date 2011 5 5 14:29:35
 * @version 1.0
 */
@Entity
@Table(name="OECP_SYS_PERMISSON_FUNUI")
public class PermissionFuncUI extends StringPKEO {

	private static final long serialVersionUID = 1L;
	/** 所属功能权限对象 **/
	private Permission permission;
	/** 功能UI **/
	private FunctionUI functionUI;
	/** 无权使用的UI元素列表 **/
	private List<PermissionUIElement> permissionUIElements;
	
	@ManyToOne
	public Permission getPermission() {
		return permission;
	}
	public void setPermission(Permission permission) {
		this.permission = permission;
	}
	@ManyToOne
	public FunctionUI getFunctionUI() {
		return functionUI;
	}
	public void setFunctionUI(FunctionUI functionUI) {
		this.functionUI = functionUI;
	}
	@OneToMany(mappedBy="permissionFuncUI",cascade={CascadeType.ALL},fetch=FetchType.EAGER)
	public List<PermissionUIElement> getPermissionUIElements() {
		return permissionUIElements;
	}
	public void setPermissionUIElements(List<PermissionUIElement> permissionUIElements) {
		this.permissionUIElements = permissionUIElements;
	}
	
}
