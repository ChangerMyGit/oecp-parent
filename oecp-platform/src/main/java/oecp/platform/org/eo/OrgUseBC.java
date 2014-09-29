/************************* 版权声明 *********************************
 *                                                                  *
 *                     版权所有：百洋软件                          				 	*
 *          Copyright (c) 2011 by www.oecp.cn                	*
 *                                                                  *
 ************************* 变更记录 *********************************
 *
 * 创建者：宋黎晓   创建日期： 2011 5 20
 * 备注：
 * 
 * 修改者：       修改日期：
 * 备注：
 * 
 */    

package oecp.platform.org.eo;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import oecp.framework.entity.base.StringPKEO;
import oecp.platform.bcinfo.eo.BizComponent;

/**
 * 公司启用业务组件记录EO
 * @author slx
 * @date 2011 5 20 17:00:55
 * @version 1.0
 */
@Entity
@Table(name="OECP_SYS_ORG_BC")
public class OrgUseBC extends StringPKEO {
	
	private static final long serialVersionUID = 1L;
	/** 公司 **/
	private Organization org;
	/** 业务组件 **/
	private BizComponent bc;
	/** 启用日期 **/
	private Date useDate;
	@ManyToOne
	public Organization getOrg() {
		return org;
	}
	public void setOrg(Organization org) {
		this.org = org;
	}
	@ManyToOne
	public BizComponent getBc() {
		return bc;
	}
	public void setBc(BizComponent bc) {
		this.bc = bc;
	}
	public Date getUseDate() {
		return useDate;
	}
	public void setUseDate(Date useDate) {
		this.useDate = useDate;
	}
}
