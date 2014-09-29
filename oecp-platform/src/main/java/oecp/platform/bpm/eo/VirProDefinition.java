/*
 * Copyright (c) 2011 OECP All Rights Reserved.                	
 * <a href="http://www.oecp.cn">http://www.oecp.cn</a>                                                                 
 */

package oecp.platform.bpm.eo;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import org.hibernate.annotations.Cascade;

import oecp.framework.entity.base.StringPKEO;
import oecp.platform.bcfunction.eo.Function;
import oecp.platform.org.eo.Organization;

/**
 * 
 * 虚拟流程就是由ProDefinition经过分配给不同的组织形成的流程
 * @author yangtao
 * @date 2011-8-1下午03:14:35
 * @version 1.0
 */
@Entity
@Table(name = "OECP_BPM_VIR_DEF")
public class VirProDefinition extends StringPKEO {

	/**
	 * 
	 */
	private static final long serialVersionUID = -9060259786258907542L;

	private String name;

	private String description;

	private Date createTime;

	private ProDefinition proDefinition;//流程定义

	private Organization assignedOrg;//分配组织

	private int version;//流程定义的版本
	
	private Boolean isUse;//是否启用
	
	private String useDateTime;//启用/停用时间
	
	private String useLoginId;//停用人/启用人

	/**
	 * @return the name
	 */
	public String getName() {
		return name;
	}

	/**
	 * @param name the name to set
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * @return the description
	 */
	public String getDescription() {
		return description;
	}

	/**
	 * @param description the description to set
	 */
	public void setDescription(String description) {
		this.description = description;
	}

	/**
	 * @return the createTime
	 */
	public Date getCreateTime() {
		return createTime;
	}

	/**
	 * @param createTime the createTime to set
	 */
	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}

	/**
	 * @return the proDefinition
	 */
	@ManyToOne
	public ProDefinition getProDefinition() {
		return proDefinition;
	}

	/**
	 * @param proDefinition the proDefinition to set
	 */
	public void setProDefinition(ProDefinition proDefinition) {
		this.proDefinition = proDefinition;
	}

	/**
	 * @return the assignedOrg
	 */
	@ManyToOne
	public Organization getAssignedOrg() {
		return assignedOrg;
	}

	/**
	 * @param assignedOrg the assignedOrg to set
	 */
	public void setAssignedOrg(Organization assignedOrg) {
		this.assignedOrg = assignedOrg;
	}

	/**
	 * @return the version
	 */
	public int getVersion() {
		return version;
	}

	/**
	 * @param version the version to set
	 */
	public void setVersion(int version) {
		this.version = version;
	}

	public Boolean getIsUse() {
		return isUse;
	}

	public void setIsUse(Boolean isUse) {
		this.isUse = isUse;
	}

	public String getUseDateTime() {
		return useDateTime;
	}

	public void setUseDateTime(String useDateTime) {
		this.useDateTime = useDateTime;
	}

	public String getUseLoginId() {
		return useLoginId;
	}

	public void setUseLoginId(String useLoginId) {
		this.useLoginId = useLoginId;
	}
	
	
	
}
