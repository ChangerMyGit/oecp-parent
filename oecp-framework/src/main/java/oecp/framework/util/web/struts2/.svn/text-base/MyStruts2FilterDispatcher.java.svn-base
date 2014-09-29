/************************* 版权声明 *********************************
 * 
 * 版权所有：百洋软件
 * Copyright (c) 2009 by Pearl Ocean.
 * 
 ************************* 变更记录 *********************************
 *
 * 创建者：slx   创建日期： 2009-11-25
 * 创建记录：创建类结构。
 * 
 * 修改者：       修改日期：
 * 修改记录：
 ************************* 随   笔 *********************************
 *
 * 这里可以写写感想，感慨，疑问什么的。
 * 
 ******************************************************************
 */

package oecp.framework.util.web.struts2;

import java.io.IOException;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts2.dispatcher.mapper.ActionMapping;
import org.apache.struts2.dispatcher.ng.filter.StrutsPrepareAndExecuteFilter;

import com.opensymphony.xwork2.ActionContext;

/**
 * 重写struts的过滤器
 * 
 * @author yongtree
 * @date 2009-11-25 上午11:15:14
 * @version 2.0
 */
public class MyStruts2FilterDispatcher extends StrutsPrepareAndExecuteFilter {


	public MyStruts2FilterDispatcher() {

	}

	@Override
	public void doFilter(ServletRequest req, ServletResponse res,
			FilterChain chain) throws IOException, ServletException {
		HttpServletRequest request = (HttpServletRequest) req;
		HttpServletResponse response = (HttpServletResponse) res;

//			if(request.getParameterMap().toString().indexOf("\\u0023")>-1){
//				StringBuffer sf_msg = new StringBuffer("受到struts2漏洞攻击,来源ip:").append(request.getRemoteAddr());
//				RuntimeException e = new RuntimeException(sf_msg.toString());
//				request.setAttribute("exception",e);
//				throw e;
//			}
			try {
				prepare.setEncodingAndLocale(request, response);
				prepare.createActionContext(request, response);
				prepare.assignDispatcherToThread();
				if (excludedPatterns != null
						&& prepare.isUrlExcluded(request, excludedPatterns)) {
					chain.doFilter(request, response);
				} else {
					request = prepare.wrapRequest(request);
					ActionMapping mapping = prepare.findActionMapping(request,
							response, true);
					if (mapping == null) {
						boolean handled = execute.executeStaticResourceRequest(
								request, response);
						if (!handled) {
							chain.doFilter(request, response);
						}
					} else {
						/**
						 * 更新flash作用域
						 */
						FlashScope fs = (FlashScope) ActionContext.getContext()
								.getSession().get(FlashScope.FLASH_SCOPE);
						if (fs != null) {
							fs.next();
						}
						execute.executeAction(request, response, mapping);
					}
				}
			} finally {
				prepare.cleanupRequest(request);
			}
		}
}
