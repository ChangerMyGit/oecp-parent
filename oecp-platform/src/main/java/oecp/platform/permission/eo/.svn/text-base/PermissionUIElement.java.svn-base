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

import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import oecp.framework.entity.base.StringPKEO;
import oecp.platform.bcfunction.eo.UIElement;

/**
 * 权限分配的UI控件
 * @author slx
 * @date 2011 5 5 14:36:39
 * @version 1.0
 */
@Entity
@Table(name="OECP_SYS_PERMISSION_UIELEMENT")
public class PermissionUIElement extends StringPKEO {

	private static final long serialVersionUID = 1L;
	/** 已经分配权限的UI **/
	private PermissionFuncUI permissionFuncUI;
	/** 未授权的界面元素 **/
	private UIElement uiElement;
	@ManyToOne
	public PermissionFuncUI getPermissionFuncUI() {
		return permissionFuncUI;
	}
	public void setPermissionFuncUI(PermissionFuncUI permissionFuncUI) {
		this.permissionFuncUI = permissionFuncUI;
	}
	@ManyToOne
	public UIElement getUiElement() {
		return uiElement;
	}
	public void setUiElement(UIElement uiElement) {
		this.uiElement = uiElement;
	}
}
