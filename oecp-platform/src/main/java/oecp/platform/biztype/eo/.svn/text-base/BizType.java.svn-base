/************************* 版权声明 *********************************
 *                                                                  *
 *                     版权所有：百洋软件                          				 	*
 *          Copyright (c) 2011 by www.oecp.cn                	*
 *                                                                  *
 ************************* 变更记录 *********************************
 *
 * 创建者：宋黎晓   创建日期： 2011-6-16
 * 备注：
 * 
 * 修改者：       修改日期：
 * 备注：
 * 
 */    

package oecp.platform.biztype.eo;

import java.util.List;

import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import oecp.framework.entity.base.StringPKEO;
import oecp.platform.org.eo.Organization;

/**
 * 业务类型
 * @author slx
 * @date 2011-6-16 上午10:51:09
 * @version 1.0
 */
@Entity
@Table(name="OECP_SYS_BIZTYPE")
public class BizType extends StringPKEO {
	private static final long serialVersionUID = 1L;
	/** 公司 **/
	private Organization org;
	/** 业务类型编号 **/
	private String code;
	/** 名称 **/
	private String name;
	/** 描述 **/
	private String description;
	/** 是否共享 **/
	private Boolean shared;
	@ManyToOne
	public Organization getOrg() {
		return org;
	}
	public void setOrg(Organization org) {
		this.org = org;
	}
	public String getCode() {
		return code;
	}
	public void setCode(String code) {
		this.code = code;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public Boolean getShared() {
		return shared;
	}
	public void setShared(Boolean shared) {
		this.shared = shared;
	}
}
