/************************* 版权声明 *********************************
 * 
 * 版权所有：百洋软件
 * Copyright (c) 2009 by Pearl Ocean.
 * 
 ************************* 变更记录 *********************************
 *
 * 创建者：slx   创建日期： 2009-5-14
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

package oecp.platform.billsn.eo;

import javax.persistence.Entity;
import javax.persistence.Table;

import oecp.framework.entity.base.StringPKEO;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * 单据号规则
 * @author slx
 * @date 2009-5-14 下午03:01:22
 * @version 1.0
 */
@Entity
@Table(name = "OECP_SYS_BILLSNRULE")
@Cache(usage=CacheConcurrencyStrategy.READ_WRITE)
public class BillSNRuleEO extends StringPKEO{

	private static final long serialVersionUID = 7331023338033740253L;

	
	/** 单据类型主键 **/
	private String billtype_id;
	
	/** 单据类型前缀 **/
	private String billtype_prefix;
	
	/** 是否需要单据类型前缀 **/
	private Boolean billtype;
	
	/** 是否需要年前缀（即使设置为否依然添加年前缀） **/
	private Boolean year;
	
	/** 是否需要月前缀 **/
	private Boolean month;
	
	/** 是否需要日前缀 **/
	private Boolean day;
	
	/** 公司编码 **/
	private String orgcode ;
	
	/** 流水号长度 **/
	private Integer snLenth;
	
	/** 是否公司规则（公司唯一） **/
	private Boolean orgRule;
	
	/** 是否需要公司前缀 **/
	private Boolean org;
	
	/** 单据上的一个属性名称 **/
	private String billAttributeName;
	
	/** 是否需要单据上的某个属性作为单据号的一部分 **/
	private Boolean billAttribute;

	/**
	 * 公司编码
	 */
	private String orgid ;
	
	
	
	public String getOrgid() {
		return orgid;
	}

	public void setOrgid(String orgid) {
		this.orgid = orgid;
	}

	public Boolean getBillAttribute() {
		return billAttribute;
	}

	public void setBillAttribute(Boolean billAttribute) {
		this.billAttribute = billAttribute;
	}

	public String getBillAttributeName() {
		return billAttributeName;
	}

	public void setBillAttributeName(String billAttributeName) {
		this.billAttributeName = billAttributeName;
	}

	public String getBilltype_id() {
		return billtype_id;
	}

	public void setBilltype_id(String billtype_id) {
		this.billtype_id = billtype_id;
	}

	public String getBilltype_prefix() {
		return billtype_prefix;
	}

	public void setBilltype_prefix(String billtype_prefix) {
		this.billtype_prefix = billtype_prefix;
	}

	public Boolean getYear() {
		return year;
	}

	public void setYear(Boolean year) {
		this.year = year;
	}

	public Boolean getMonth() {
		return month;
	}

	public void setMonth(Boolean month) {
		this.month = month;
	}

	public Boolean getDay() {
		return day;
	}

	public void setDay(Boolean day) {
		this.day = day;
	}

	public String getOrgcode() {
		return orgcode;
	}

	public void setOrgcode(String orgcode) {
		this.orgcode = orgcode;
	}

	public Integer getSnLenth() {
		return snLenth;
	}

	public void setSnLenth(Integer snLenth) {
		this.snLenth = snLenth;
	}

	public Boolean getOrgRule() {
		return orgRule;
	}

	public void setOrgRule(Boolean orgRule) {
		this.orgRule = orgRule;
	}

	public Boolean getBilltype() {
		return billtype;
	}

	public void setBilltype(Boolean billtype) {
		this.billtype = billtype;
	}

	public Boolean getOrg() {
		return org;
	}

	public void setOrg(Boolean org) {
		this.org = org;
	}
}
