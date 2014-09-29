package oecp.platform.permission.service;

import java.util.List;

import oecp.framework.service.BaseService;
import oecp.platform.permission.eo.Permission;
import oecp.platform.permission.vo.BizComponentVO;

public interface PermissionService extends BaseService<Permission> {

	/**
	 * 检查用户在一个组织内是否拥有某功能的使用权限
	 * @author slx
	 * @date 2011 4 15 14:48:22
	 * @modifyNote
	 * @param userID
	 * 		用户账号
	 * @param orgID
	 * 		组织主键
	 * @param sign
	 * 		页面标示
	 * @return
	 * 		是否有权
	 */
	public boolean checkUserPermission(String userID, String orgID, String sign);
	
	/**
	 * 获得用户在本组织内的可用功能列表.
	 * 功能对象下包含界面,界面中包含界面元素.
	 * <br>
	 * <b>注:界面中包含的界面元素,不是用户有权使用的元素,而是用户无权使用的元素.</b>
	 * @author slx
	 * @date 2011 4 15 14:51:00
	 * @modifyNote
	 * @param userID
	 * 		用户账号
	 * @param orgID
	 * 		组织id
	 * @return
	 * 		已分配功能列表
	 */
	public List<Permission> getUserPermission(String userID,String orgID);
	

	/**
	 * 获得用户在本组织内的可用的某一个组件的功能列表.
	 * 功能对象下包含界面,界面中包含界面元素.
	 * <br>
	 * <b>注:界面中包含的界面元素,不是用户有权使用的元素,而是用户无权使用的元素.</b>
	 * @author slx
	 * @date 2011 4 15 14:51:00
	 * @modifyNote
	 * @param userID
	 * 		用户账号
	 * @param bcCode
	 * 		业务组件编号
	 * @param orgID
	 * 		组织id
	 * @return
	 * 		本组件内已分配功能列表
	 */
	public List<Permission> getUserBCPermissions(String bcCode, String userID, String orgID);
	
	/**
	 * 获得用户在本组织内的可用业务组件列表.
	 * 
	 * @author slx
	 * @date 2011 4 15 14:51:00
	 * @modifyNote
	 * @param userID
	 * 		用户账号
	 * @param orgID
	 * 		组织id
	 * @return
	 * 		已分配业务组件列表
	 */
	public List<BizComponentVO> getPermissionBCs(String userID,String orgID);

	
	/**
	 * 获得组织角色的可用功能列表.
	 * 功能对象下包含界面,界面中包含界面元素.
	 * <br>
	 * <b>注:界面中包含的界面元素,不是用户有权使用的元素,而是用户无权使用的元素.</b>
	 * @author liujingtao
	 * @date 2011 5 25 17:30:06
	 * @modifyNote
	 * @param orgRoleId
	 * 		组织id
	 * @return
	 * 		已分配功能列表
	 */
	public List<Permission> getRolePermission(String orgRoleId);
	
	/**
	 * 修改组织角色的可用功能列表.
	 * 以及功能对象下包含界面,界面中包含界面元素.
	 * @author liujingtao
	 * @date 2011 5 30 9:50:33
	 * @param orgRoleId
	 * 		组织角色id
	 * @param permissionList
	 * 		功能权限列表
	 */
	public List<Permission> updateRolePermission(String orgRoleId,List<Permission> permissionList);
	
}
