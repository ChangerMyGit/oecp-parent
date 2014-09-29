/*
 * Copyright (c) 2013 上海医师在线信息技术有限公司  All Rights Reserved.                	
*/                                                                
  

package oecp.platform.web.spring.context;

import java.io.File;
import java.net.URLClassLoader;

import javax.servlet.ServletContextEvent;

import org.springframework.web.context.ContextLoaderListener;

/** 
 * spring上下文加载器，加入OECP平台特殊的加载方式。
 * @author slx  
 * @date 2013-11-12 上午11:05:36 
 * @version 1.0
 *  
 */
public class OECPSpringContextLoaderListener extends ContextLoaderListener {

	public void contextInitialized(ServletContextEvent event) {
		BCDeployConfig.platformDocBase = event.getServletContext().getRealPath("/");
		loadBCDeployConfig(); 
		OECPClassLoaderHelper oecp_clhelper = new OECPClassLoaderHelper((URLClassLoader)this.getClass().getClassLoader());
		oecp_clhelper.loadClass();
		oecp_clhelper.copyWebResouces();
		super.contextInitialized(event);
	}
	
	private void loadBCDeployConfig(){
		File f_cfg = new File(this.getClass().getClassLoader().getResource("/BCDeploy.xml").getFile());
		if(f_cfg.exists()){
			BCDeployConfig.loadConfig(f_cfg);
		}
	}
}
