/**
 * oecp-platform - OECPFilter.java
 * copyright 2009-2012 OECP www.oecp.cn
 * 创建人:songlixiao	创建时间:上午11:10:15		版本:v1
 * ============================================
 * 修改人：			修改时间:					版本:
 * 修改原因:
 *
 * ============================================
 */

package oecp.platform.web.filter;
import java.io.IOException;
import java.util.List;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import oecp.framework.util.ReflectionUtils;
import oecp.platform.web.spring.context.BCDeploy;
import oecp.platform.web.spring.context.BCDeployConfig;
import oecp.platform.web.spring.context.OECPClassLoaderHelper;
/** 
 * OECP业务组件测试过滤器
 * <b><br/>注意：
 * <br/>每次打开组件的RUL时都重新加载一次组件的资源。效率比较低。因此正式环境尽量不要使用此过滤器。</b>
 * @author songlixiao  
 * @date 2013-12-6 上午11:10:15 
 * @version 1.0
 *  
 */
public class OECPBCDebugFilter implements Filter {

		@Override
		public void init(FilterConfig filterConfig) throws ServletException {
		}
		
		@Override
		public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
			HttpServletRequest req = (HttpServletRequest)request ;
			HttpServletResponse rps = (HttpServletResponse)response;
			reloadBCResouces(req, rps);
			chain.doFilter(req, rps);
		}

		/**
		 * 重新加载此次访问的业务组件的资源
		 * @author songlixiao
		 * @date 2013-12-6上午11:18:44
		 * @param request
		 * @param response
		 */
		private void reloadBCResouces(HttpServletRequest req, HttpServletResponse response) {
			List<BCDeploy> bcs = BCDeployConfig.getAllBCDeployInfos();
			int len = bcs.size();
			for (int i=0 ; i < len ; i++ ) {// 用RUL匹配业务组件的名称，匹配上如果设置为Reload为true的则重新拷贝。
				if(req.getRequestURI().indexOf("/"+bcs.get(i).getName()+"/")>-1){
					if(bcs.get(i).isReloadWebResouces()){
						OECPClassLoaderHelper.copyWebResouces(bcs.get(i).getName());
					}
					return;
				}
			}
		}
		
		@Override
		public void destroy() {
		}
}
