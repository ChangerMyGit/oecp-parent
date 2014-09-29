/*
 * Copyright (c) 2011 OECP All Rights Reserved.                	
 * http://www.oecp.cn                                                                 
 */

package oecp.platform.api.auth.webservice;

import javax.jws.WebParam;
import javax.jws.WebResult;
import javax.jws.WebService;

import oecp.platform.web.WebContext;

import org.apache.cxf.annotations.WSDLDocumentation;
import org.springframework.stereotype.Service;

/**
 * 
 * @author yongtree
 * @date 2011-4-28 上午10:15:35
 * @version 1.0
 */
@Service("authAPI")
@WebService(targetNamespace = "http://www.oecp.cn", serviceName = "authAPI")
//@WSDLDocumentationCollection(value = {
//		@WSDLDocumentation(placement = Placement.SERVICE, value = "[API描述：用户认证服务接口]"),
//		@WSDLDocumentation(placement = Placement.TOP, value = "提供对外的用户认证服务，认证不通过的用户将无权使用本系统.版权归<a href='http://www.oecp.cn'>OECP社区</a>所有."), })
public class AuthServiceImpl implements AuthService {

	@WSDLDocumentation("<p>功能描述：通过平台提供的Key得到当前的在线用户。</p>"
			+ "<p>参数描述：</br>1 _key 平台为登录用户动态创建的key</p>"
			+ "<p>返回值描述：OnlineUser 在线用户的相关信息。如果用户未登录，则认证不通过将返回空值NULL，组件接收到NULL后，可提示重新登录!<p>")
	@WebResult(name = "onlineUser")
	public OnlineUser getOnlineUser(@WebParam(name = "key") String _key) {
		oecp.platform.web.OnlineUser ou = WebContext.getOnlineUser(_key);
		OnlineUser onlineUser = null;
		if (ou != null && ou.getLoginedOrg() != null) {
			onlineUser = new OnlineUser();
			onlineUser.setUserId(ou.getUser().getId());
			onlineUser.setLoginId(ou.getUser().getLoginId());
			onlineUser.setOrgId(ou.getLoginedOrg().getId());
			onlineUser.setKey(_key);
		}
		return onlineUser;
	}

}
