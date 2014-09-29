/**
 * 
 */
package oecp.platform.uiview.assign.eo;

import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import oecp.framework.entity.base.StringPKEO;
import oecp.platform.bcfunction.eo.Function;
import oecp.platform.org.eo.Organization;
import oecp.platform.print.eo.PrintTemplate;
import oecp.platform.uiview.eo.UIComponent;

/**
 * 功能的视图
 * @author slx
 * 
 */
@Entity
@Table(name="OECP_SYS_UI_FUNCVIEW")
public class FunctionView extends StringPKEO {

	private static final long serialVersionUID = 1L;
	/** 功能视图编号 **/
	private String viewcode;
	/** 名称 **/
	private String viewname;
	/** 表单视图 **/
	private UIComponent viewcomp;
	/** 创建公司 **/
	private Organization org;
	/** 对应的功能 **/
	private Function func;
	/** 是否共享 **/
	private Boolean shared;
	/** 是否系统默认 **/
	private Boolean sysdefault;
	
	private List<PrintTemplate> printTemplates;
	
	@Column(length=20)
	public String getViewcode() {
		return viewcode;
	}
	public void setViewcode(String viewcode) {
		this.viewcode = viewcode;
	}
	@Column(length=50)
	public String getViewname() {
		return viewname;
	}
	public void setViewname(String viewname) {
		this.viewname = viewname;
	}
	
	@ManyToOne()
	public Organization getOrg() {
		return org;
	}
	@ManyToOne()
	public Function getFunc() {
		return func;
	}
	public void setFunc(Function func) {
		this.func = func;
	}
	@ManyToOne(cascade={CascadeType.ALL})
	public UIComponent getViewcomp() {
		return viewcomp;
	}
	public void setViewcomp(UIComponent viewcomp) {
		this.viewcomp = viewcomp;
	}
	public void setOrg(Organization org) {
		this.org = org;
	}
	public Boolean getShared() {
		return shared==null?false:shared;
	}
	public void setShared(Boolean shared) {
		this.shared = shared;
	}
	public Boolean getSysdefault() {
		return sysdefault==null?false:sysdefault;
	}
	public void setSysdefault(Boolean sysdefault) {
		this.sysdefault = sysdefault;
	}

	@ManyToMany
	@JoinTable(name = "OECP_SYS_VIEW_PRINT", joinColumns = { @JoinColumn(name = "print_pk") }, inverseJoinColumns = { @JoinColumn(name = "uiview_pk") })
	public List<PrintTemplate> getPrintTemplates() {
		return printTemplates;
	}

	public void setPrintTemplates(List<PrintTemplate> printTemplates) {
		this.printTemplates = printTemplates;
	}
	
}
