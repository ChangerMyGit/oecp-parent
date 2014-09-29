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
import javax.persistence.Lob;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import oecp.framework.entity.base.StringPKEO;
import oecp.platform.bcfunction.eo.Function;
import oecp.platform.org.eo.Organization;

/**
 * 流程定义
 * 
 * @author yongtree
 * @date 2011-6-24 上午11:01:33
 * @version 1.0
 */
@Entity
@Table(name = "OECP_BPM_DEF")
public class ProDefinition extends StringPKEO {

	private static final long serialVersionUID = 1L;

	private String name;

	private String description;

	private String deployId;// 对应的jbpm的JBPM4_DEPLOYMENT的DBID_

	private Date createTime;

	private String proDefId;// 对应的jbpm的processDefinition的ID

	private Organization createdByOrg;

	private Function belongFunction;

	private List<ProActivity> activities = new ArrayList<ProActivity>();
	
	private int version;//流程定义的版本
	
	//流程图片对应的json字符串  复制流程用到
	private String webPictureString;
	

	@OneToMany(mappedBy = "proDef", cascade = { CascadeType.ALL })
	public List<ProActivity> getActivities() {
		return activities;
	}

	public void setActivities(List<ProActivity> activities) {
		this.activities = activities;
	}

	@ManyToOne
	public Function getBelongFunction() {
		return belongFunction;
	}

	public void setBelongFunction(Function belongFunction) {
		this.belongFunction = belongFunction;
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

	public String getDeployId() {
		return deployId;
	}

	public void setDeployId(String deployId) {
		this.deployId = deployId;
	}

	public Date getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}

	public String getProDefId() {
		return proDefId;
	}

	public void setProDefId(String proDefId) {
		this.proDefId = proDefId;
	}

	@ManyToOne
	public Organization getCreatedByOrg() {
		return createdByOrg;
	}

	public void setCreatedByOrg(Organization createdByOrg) {
		this.createdByOrg = createdByOrg;
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
	@Lob
	public String getWebPictureString() {
		return webPictureString;
	}

	public void setWebPictureString(String webPictureString) {
		this.webPictureString = webPictureString;
	}

	
}
