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

import oecp.framework.entity.base.StringPKEO;
import oecp.platform.bcfunction.eo.Function;
import oecp.platform.org.eo.Organization;

/**
 * 业务流程的封装
 * @author yangtao
 * @date 2011-9-5上午10:14:22
 * @version 1.0
 */
@Entity
@Table(name="OECP_BPM_BIZ_DEF")
public class BizProDefinition  extends StringPKEO {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -8040061345644617119L;

	//名称
	private String name;
	
	//描述
	private String description;
	
	//部署ID
	private String deployId;
	
	//创建时间
	private Date createTime;
	
	//流程定义ID
	private String proDefId;
	
	//创建组织
	private Organization createdByOrg;

	//流程节点
	private List<BizProActivity> bizProActivities= new ArrayList<BizProActivity>();
	
	//版本
	private int version;

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
	 * @return the deployId
	 */
	public String getDeployId() {
		return deployId;
	}

	/**
	 * @param deployId the deployId to set
	 */
	public void setDeployId(String deployId) {
		this.deployId = deployId;
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
	 * @return the proDefId
	 */
	public String getProDefId() {
		return proDefId;
	}

	/**
	 * @param proDefId the proDefId to set
	 */
	public void setProDefId(String proDefId) {
		this.proDefId = proDefId;
	}

	/**
	 * @return the createdByOrg
	 */
	@ManyToOne
	public Organization getCreatedByOrg() {
		return createdByOrg;
	}

	/**
	 * @param createdByOrg the createdByOrg to set
	 */
	public void setCreatedByOrg(Organization createdByOrg) {
		this.createdByOrg = createdByOrg;
	}

	

	/**
	 * @return the bizProActivities
	 */
	@OneToMany(mappedBy = "bizProDefinition", cascade = { CascadeType.ALL })
	public List<BizProActivity> getBizProActivities() {
		return bizProActivities;
	}

	/**
	 * @param bizProActivities the bizProActivities to set
	 */
	public void setBizProActivities(List<BizProActivity> bizProActivities) {
		this.bizProActivities = bizProActivities;
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
	
	
}
