/**
 * oecp-platform - BaseInterceptor.java
 * copyright 2009-2012 OECP www.oecp.cn
 * 创建人:Administrator	创建时间:下午1:58:41		版本:v1
 * ============================================
 * 修改人：			修改时间:					版本:
 * 修改原因:
 *
 * ============================================
 */

package oecp.platform.base.interceptor;

import java.lang.reflect.Method;

import org.apache.struts2.ServletActionContext;
import org.springframework.context.ApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;

import com.opensymphony.xwork2.ActionInvocation;
import com.opensymphony.xwork2.interceptor.AbstractInterceptor;

/** 
 * 拦截器父类
 * @author luanyoubo  
 * @date 2014年3月11日 下午1:58:41 
 * @version 1.0
 *  
 */
public abstract class BaseInterceptor extends AbstractInterceptor{
	private static final long serialVersionUID = -4749708597724775557L;
	
	// 获取Spring上下文
	public ApplicationContext getSpringApplicationContext() {
		return WebApplicationContextUtils.getWebApplicationContext(ServletActionContext.getServletContext());
	}
	
	// 获取拦截拦截方法
	public Method getInvacotionMethod(ActionInvocation invocation) {
		Method method = null;
		try {
			String methodName = invocation.getProxy().getMethod();
			method = invocation.getAction().getClass().getMethod(methodName);
		} catch (Exception e) {
		}
		return method;
	}
}
