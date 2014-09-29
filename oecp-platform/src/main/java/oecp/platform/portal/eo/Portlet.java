/**
 * oecp-platform - Portlet.java
 * copyright 2009-2012 OECP www.oecp.cn
 * 创建人:songlixiao	创建时间:下午2:02:04		版本:v1
 * ============================================
 * 修改人：			修改时间:					版本:
 * 修改原因:
 *
 * ============================================
 */

package oecp.platform.portal.eo;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

import oecp.framework.entity.base.StringPKEO;

/**
 * portlet注册信息eo
 * 
 * @author songlixiao
 * @date 2014年3月19日 下午2:02:04
 * @version 1.0
 * 
 */
@Entity
@Table(name = "OECP_SYS_PORTAL_PORTLET")
public class Portlet extends StringPKEO {

	private static final long	serialVersionUID	= 1L;
	/** 显示名 **/
	private String				displayName;
	/** xtype名(Portlet的类必须继承自Ext.ux.Portlet) **/
	private String				xtype;
	/** js类初始化参数 **/
	private String				initParams;
	/** js文件名，如果是多个文件，可以用js数组的形式保存 **/
	private String				jsFiles;

	public String getDisplayName() {
		return displayName;
	}

	public void setDisplayName(String displayName) {
		this.displayName = displayName;
	}

	public String getXtype() {
		return xtype;
	}

	public void setXtype(String xtype) {
		this.xtype = xtype;
	}

	@Column(name = "initparams", length = 1000)
	public String getInitParams() {
		return initParams;
	}

	public void setInitParams(String initParams) {
		this.initParams = initParams;
	}

	@Column(name = "jsfiles", length = 1000)
	public String getJsFiles() {
		return jsFiles;
	}

	public void setJsFiles(String jsFiles) {
		this.jsFiles = jsFiles;
	}

}
