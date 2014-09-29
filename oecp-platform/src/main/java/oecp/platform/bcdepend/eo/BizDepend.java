package oecp.platform.bcdepend.eo;

import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import oecp.framework.entity.base.StringPKEO;
import oecp.platform.bcinfo.eo.BizComponent;

/** 
 *
 * @author lintao  
 * @date 2011-8-8 上午09:59:39 
 * @version 1.0
 *  
 */
@Entity
@Table(name = "OECP_SYS_BIZDEPEND")
public class BizDepend extends StringPKEO{

	private static final long serialVersionUID = 1L;
	/** 获取依赖服务wsdl的url **/
	private String url;
	/** 依赖的英文名称 **/
	private String dependName_EN;
	/** 依赖的中文名称 **/
	private String dependName_CN;
	/** 依赖的描述 **/
	private String dependDesc;
	/** 关联业务组件 **/
	private BizComponent bc;
	
	
	public String getUrl() {
		return url;
	}
	public void setUrl(String url) {
		this.url = url;
	}
	public String getDependName_EN() {
		return dependName_EN;
	}
	public void setDependName_EN(String dependNameEN) {
		dependName_EN = dependNameEN;
	}
	public String getDependName_CN() {
		return dependName_CN;
	}
	public void setDependName_CN(String dependNameCN) {
		dependName_CN = dependNameCN;
	}
	public String getDependDesc() {
		return dependDesc;
	}
	public void setDependDesc(String dependDesc) {
		this.dependDesc = dependDesc;
	}
	@ManyToOne
	public BizComponent getBc() {
		return bc;
	}
	public void setBc(BizComponent bc) {
		this.bc = bc;
	}

}
