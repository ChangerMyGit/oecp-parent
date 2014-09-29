/**
 * oecp-platform - UIViewParser.java
 * copyright 2009-2012 OECP www.oecp.cn
 * 创建人:slx	创建时间:2011-11-7上午10:41:16		版本:v1
 * ============================================
 * 修改人：			修改时间:					版本:
 * 修改原因:
 *
 * ============================================
 */

package oecp.platform.web.spring.context;

import java.util.List;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlRootElement;

/** 
 * 业务组件部署配置
 * @author slx  
 * @date 2013-11-15 下午4:12:54 
 * @version 1.0
 * 
 */
@XmlRootElement(name="bc") 
public class BCDeploy {
	private String name;
	private String webResouceRoot;
	private boolean reloadWebResouces;
	private List<String> classDirs;
	private List<String> jarDirs;
	
	@XmlElement(name="name")
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	@XmlElement(name="webResouceRoot")
	public String getWebResouceRoot() {
		return webResouceRoot;
	}
	public void setWebResouceRoot(String webResouceRoot) {
		this.webResouceRoot = webResouceRoot;
	}
	@XmlElement(name="reloadWebResouces")
	public boolean isReloadWebResouces() {
		return reloadWebResouces;
	}
	public void setReloadWebResouces(boolean reloadWebResouces) {
		this.reloadWebResouces = reloadWebResouces;
	}
	@XmlElementWrapper(name="classDirs")
	@XmlElement(name="dir")
	public List<String> getClassDirs() {
		return classDirs;
	}
	public void setClassDirs(List<String> classDirs) {
		this.classDirs = classDirs;
	}
	
	@XmlElementWrapper(name="jarDirs")
	@XmlElement(name="dir")
	public List<String> getJarDirs() {
		return jarDirs;
	}
	public void setJarDirs(List<String> jarDirs) {
		this.jarDirs = jarDirs;
	}
}
