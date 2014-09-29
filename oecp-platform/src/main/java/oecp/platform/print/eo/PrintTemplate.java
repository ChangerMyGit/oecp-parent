/*
 * Copyright (c) 2012 OECP All Rights Reserved.                	
 * <a href="http://www.oecp.cn">OECP</a> 
 */
package oecp.platform.print.eo;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Transient;

import oecp.framework.entity.base.StringPKEO;
import oecp.platform.bcfunction.eo.Function;
import oecp.platform.org.eo.Organization;

/**
 * 打印模板
 * 
 * @author wangliang
 * @date 2012-3-16 上午11:51:12
 * @version 1.0
 * 
 */
@Entity
@Table(name = "OECP_SYS_P_TEMPLATE")
public class PrintTemplate extends StringPKEO {
	private static final long serialVersionUID = 1L;
	private String name;//模板名称
	private String vtemplate;// 打印内容
	
	private String vmode;// 打印参数

	private Function function;//功能
	
	private Organization organ;//创建组织
	
	private Boolean checked;//模板分配时使用
	
	@Column(length=8000)
	public String getVtemplate() {
		return vtemplate;
	}

	public void setVtemplate(String vtemplate) {
		this.vtemplate = vtemplate;
	}
	@Transient
	//暂时不考虑
	public String getVmode() {
		return vmode;
	}

	public void setVmode(String vmode) {
		this.vmode = vmode;
	}

	@ManyToOne
	public Function getFunction() {
		return function;
	}

	public void setFunction(Function function) {
		this.function = function;
	}
	
	@ManyToOne
	public Organization getOrgan() {
		return organ;
	}

	public void setOrgan(Organization organ) {
		this.organ = organ;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	@Transient
	public Boolean getChecked() {
		if(checked == null){
			return Boolean.FALSE;
		}
		return checked;
	}

	public void setChecked(Boolean checked) {
		this.checked = checked;
	}

}
