/*
 * Copyright (c) 2011 OECP All Rights Reserved.                	
 * <a href="http://www.oecp.cn">http://www.oecp.cn</a>                                                                 
 */    

package oecp.platform.bpm.vo;

import java.util.Date;

/**
 *
 * @author yangtao
 * @date 2011-8-1下午03:45:58
 * @version 1.0
 */
public class VirProcessDefinitionInfo {
	private String id;
	
	private String name;//虚拟流程名称
	
	private String proDefinitionId;//ProDefinition的id
	
	private String proDefinitionName;//ProDefinition的名称
	
	private String deployId;// 对应的jbpm的JBPM4_DEPLOYMENT的DBID_

	private String processDefinitionId;// 对应的jbpm的processDefinition的ID
	
	private Date createTime;
	
	private String assignedOrgId;//分配组织ID
	
	private String assignedOrgName;//分配组织名称
	
	private String belongFunctionId;//所属功能的标识
	
	private String belongFunctionName;//所属功能的名称
	
	private String isUseId;//是否启用标识
	
	private String isUseName;//是否启用名称

	
	
	/**
	 * @return the id
	 */
	public String getId() {
		return id;
	}

	/**
	 * @param id the id to set
	 */
	public void setId(String id) {
		this.id = id;
	}

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
	 * @return the proDefinitionId
	 */
	public String getProDefinitionId() {
		return proDefinitionId;
	}

	/**
	 * @param proDefinitionId the proDefinitionId to set
	 */
	public void setProDefinitionId(String proDefinitionId) {
		this.proDefinitionId = proDefinitionId;
	}

	/**
	 * @return the proDefinitionName
	 */
	public String getProDefinitionName() {
		return proDefinitionName;
	}

	/**
	 * @param proDefinitionName the proDefinitionName to set
	 */
	public void setProDefinitionName(String proDefinitionName) {
		this.proDefinitionName = proDefinitionName;
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
	 * @return the processDefinitionId
	 */
	public String getProcessDefinitionId() {
		return processDefinitionId;
	}

	/**
	 * @param processDefinitionId the processDefinitionId to set
	 */
	public void setProcessDefinitionId(String processDefinitionId) {
		this.processDefinitionId = processDefinitionId;
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
	 * @return the assignedOrgId
	 */
	public String getAssignedOrgId() {
		return assignedOrgId;
	}

	/**
	 * @param assignedOrgId the assignedOrgId to set
	 */
	public void setAssignedOrgId(String assignedOrgId) {
		this.assignedOrgId = assignedOrgId;
	}

	/**
	 * @return the assignedOrgName
	 */
	public String getAssignedOrgName() {
		return assignedOrgName;
	}

	/**
	 * @param assignedOrgName the assignedOrgName to set
	 */
	public void setAssignedOrgName(String assignedOrgName) {
		this.assignedOrgName = assignedOrgName;
	}

	public String getIsUseId() {
		return isUseId;
	}

	public void setIsUseId(String isUseId) {
		this.isUseId = isUseId;
	}

	public String getIsUseName() {
		return isUseName;
	}

	public void setIsUseName(String isUseName) {
		this.isUseName = isUseName;
	}

	public String getBelongFunctionId() {
		return belongFunctionId;
	}

	public void setBelongFunctionId(String belongFunctionId) {
		this.belongFunctionId = belongFunctionId;
	}

	public String getBelongFunctionName() {
		return belongFunctionName;
	}

	public void setBelongFunctionName(String belongFunctionName) {
		this.belongFunctionName = belongFunctionName;
	}
	
	
}
