package oecp.platform.api.permission.webservice;


/**
 * 权限服务API
 * 
 * @author slx
 * @date 2011 4 15 14:27:38
 * @version 1.0
 */
public interface PermissionAPI {
	
	/**
	 * 检查用户在一个组织内是否拥有某功能的使用权限
	 * @author slx
	 * @date 2011 4 15 14:48:22
	 * @modifyNote
	 * @param userID
	 * 		用户账号
	 * @param orgID
	 * 		组织主键
	 * @param uisign
	 * 		ui标识（可以是界面url）
	 * @return
	 * 		是否有权
	 */
	public boolean checkUserPower(String userID, String orgID, String uisign);
	
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
	 * 		功能列表对象数组
	 */
	public Function[] getUserFunctions(String userID,String orgID);

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
	 * 		功能列表对象数组
	 */
	Function[] getUserBCFunctions(String userID, String bcCode, String orgID);
	
}
