/*
 * Copyright (c) 2011 OECP All Rights Reserved.                	
 * http://www.oecp.cn                                                                 
 */

package oecp.platform.api.auth.webservice;

/**
 * 认证服务
 * 
 * @author yongtree
 * @date 2011-4-27 下午05:34:16
 * @version 1.0
 */
public interface AuthService {

	/**
	 * 通过平台分配给的_key来获得当前在线的用户<br>
	 * 以解决平台下各组件的单点登录的问题
	 * 
	 * @author yongtree
	 * @date 2011-4-28 上午10:00:58
	 * @param _key 平台登录后产生的用户KEY
	 * @return
	 */
	public OnlineUser getOnlineUser(String _key);


}
