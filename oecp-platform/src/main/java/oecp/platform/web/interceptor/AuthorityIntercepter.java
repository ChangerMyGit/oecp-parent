/*
 * Copyright (c) 2011 OECP All Rights Reserved.                	
 * <a href="http://www.oecp.cn">http://www.oecp.cn</a>                                                                 
 */

package oecp.platform.web.interceptor;

import java.net.URLEncoder;
import javax.servlet.http.HttpServletResponse;
import oecp.platform.web.WebContext;
import org.apache.struts2.ServletActionContext;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.opensymphony.xwork2.ActionContext;
import com.opensymphony.xwork2.ActionInvocation;
import com.opensymphony.xwork2.interceptor.AbstractInterceptor;

/**
 * session超时拦截器
 * 
 * @author yongtree
 * @date 2011-6-10 下午01:39:45
 * @version 1.0
 */
@Component(value = "authorityIntercepter")
@Scope("prototype")
public class AuthorityIntercepter extends AbstractInterceptor {

	private static final long serialVersionUID = 1L;

	private final static String STATUS_CODE = "__timeout";

	private String result = "exception";

	@Override
	public String intercept(ActionInvocation invocation) throws Exception {
		if (WebContext.getCurrentUser() == null) {
			ActionContext ctx = invocation.getInvocationContext();
			HttpServletResponse response = (HttpServletResponse) ctx
					.get(ServletActionContext.HTTP_RESPONSE);
			response.addHeader(STATUS_CODE, URLEncoder.encode("登录超时，请重新登录！", "UTF-8"));
//			HttpServletRequest request = (HttpServletRequest) ctx
//					.get(ServletActionContext.HTTP_REQUEST);
//			request.setAttribute("exception.message", "登录超时，请重新登录！");
			return result;
		}
		return invocation.invoke();
	}

	public void setResult(String result) {
		this.result = result;
	}

}
