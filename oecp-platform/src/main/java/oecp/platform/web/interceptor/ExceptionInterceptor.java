/*
 * Copyright (c) 2011 OECP All Rights Reserved.                	
 * <a href="http://www.oecp.cn">http://www.oecp.cn</a>                                                                 
 */

package oecp.platform.web.interceptor;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import oecp.framework.exception.BizException;

import org.apache.struts2.ServletActionContext;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.opensymphony.xwork2.ActionContext;
import com.opensymphony.xwork2.ActionInvocation;
import com.opensymphony.xwork2.interceptor.AbstractInterceptor;

/**
 * 异常拦截器
 * 
 * @author yongtree
 * @date 2011-6-10 上午10:40:49
 * @version 1.0
 */
@Component(value = "exceptionInterceptor")
@Scope("prototype")
public class ExceptionInterceptor extends AbstractInterceptor {

	private final static String STATUS_CODE = "__500_error";

	public String intercept(ActionInvocation invocation) throws Exception {
		String result = "exception";
		try {
 			result = invocation.invoke();
		} catch (BizException e) {
			handlingException(invocation, e);
			
		} catch (Exception ex) {
			handlingException(invocation, ex);
		}
		return result;

	}

	public void handlingException(ActionInvocation invocation, Exception e) throws UnsupportedEncodingException {
		e.printStackTrace();
		ActionContext ctx = invocation.getInvocationContext();
		HttpServletRequest request = (HttpServletRequest) ctx
				.get(ServletActionContext.HTTP_REQUEST);
		HttpServletResponse response = (HttpServletResponse) ctx
				.get(ServletActionContext.HTTP_RESPONSE);
			
		response.addHeader(STATUS_CODE, URLEncoder.encode(e.getMessage()==null? "无错误消息":e.getMessage(),
				"UTF-8"));
		request.setAttribute("exception", e);
	}

}
