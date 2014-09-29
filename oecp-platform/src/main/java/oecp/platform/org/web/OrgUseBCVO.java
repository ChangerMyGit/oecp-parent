/************************* 版权声明 *********************************
 *                                                                  *
 *                     版权所有：百洋软件                          				 	*
 *          Copyright (c) 2011 by www.oecp.cn                	*
 *                                                                  *
 ************************* 变更记录 *********************************
 *
 * 创建者：宋黎晓   创建日期： 2011 5 23
 * 备注：
 * 
 * 修改者：       修改日期：
 * 备注：
 * 
 */    

package oecp.platform.org.web;

import java.io.Serializable;
import java.util.Date;

/**
 * 公司启用业务组件VO
 * @author slx
 * @date 2011 5 23 10:05:45
 * @version 1.0
 */
public class OrgUseBCVO implements Serializable{
	private static final long serialVersionUID = 1L;
	/** 组织id **/
	private String orgid;
	/** 组件id **/
	private String bcid;
	/** 组件编号 **/
	private String bcCode;
	/** 组件名 **/
	private String bcName;
	/** 业务组件描述 **/
	private String bcDiscription;
	/** 启用日期 **/
	private Date startUseDate;
	/** 是否启用 **/
	private boolean used;
	
	public String getOrgid() {
		return orgid;
	}
	public void setOrgid(String orgid) {
		this.orgid = orgid;
	}
	public String getBcid() {
		return bcid;
	}
	public void setBcid(String bcid) {
		this.bcid = bcid;
	}
	public String getBcCode() {
		return bcCode;
	}
	public void setBcCode(String bcCode) {
		this.bcCode = bcCode;
	}
	public String getBcName() {
		return bcName;
	}
	public void setBcName(String bcName) {
		this.bcName = bcName;
	}
	public Date getStartUseDate() {
		return startUseDate;
	}
	public void setStartUseDate(Date startUseDate) {
		this.startUseDate = startUseDate;
	}
	public boolean isUsed() {
		return used;
	}
	public void setUsed(boolean used) {
		this.used = used;
	}
	public String getBcDiscription() {
		return bcDiscription;
	}
	public void setBcDiscription(String bcDiscription) {
		this.bcDiscription = bcDiscription;
	}
	
}
