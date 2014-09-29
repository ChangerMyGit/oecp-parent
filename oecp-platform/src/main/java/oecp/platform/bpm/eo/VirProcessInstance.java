/*
 * Copyright (c) 2011 OECP All Rights Reserved.                	
 * <a href="http://www.oecp.cn">http://www.oecp.cn</a>                                                                 
 */

package oecp.platform.bpm.eo;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import oecp.framework.entity.base.StringPKEO;
import oecp.platform.bpm.enums.VirProcessInstanceState;
import oecp.platform.org.eo.Post;
import oecp.platform.role.eo.OrgRole;
import oecp.platform.user.eo.User;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * 
 * 虚拟流程启动的实例
 * @author yangtao
 * @date 2011-8-2下午03:53:00
 * @version 1.0
 */
@Entity
@Table(name = "OECP_BPM_VIR_PROCESS_INSTANCE")
public class VirProcessInstance extends StringPKEO {

	/**
	 * 
	 */
	private static final long serialVersionUID = -2478562068955750729L;
	//虚拟流程定义
	private VirProDefinition virProDefinition;
	//jbpm4中的流程实例ID
	private String processInstanceId;
	//该流程实例的启动人
	private String createUserLoginId;
	//启动时间
	private String createTime;
	//业务主键值
	private String billKey;
	//单据信息
	private String billInfo;
	//流程实例的状态
	private VirProcessInstanceState virProcessInstanceState;

	/**
	 * @return the virProDefinition
	 */
	@ManyToOne
	public VirProDefinition getVirProDefinition() {
		return virProDefinition;
	}

	/**
	 * @param virProDefinition the virProDefinition to set
	 */
	public void setVirProDefinition(VirProDefinition virProDefinition) {
		this.virProDefinition = virProDefinition;
	}

	/**
	 * @return the processInstanceId
	 */
	public String getProcessInstanceId() {
		return processInstanceId;
	}

	/**
	 * @param processInstanceId the processInstanceId to set
	 */
	public void setProcessInstanceId(String processInstanceId) {
		this.processInstanceId = processInstanceId;
	}

	/**
	 * @return the createUserLoginId
	 */
	public String getCreateUserLoginId() {
		return createUserLoginId;
	}

	/**
	 * @param createUserLoginId the createUserLoginId to set
	 */
	public void setCreateUserLoginId(String createUserLoginId) {
		this.createUserLoginId = createUserLoginId;
	}

	/**
	 * @return the createTime
	 */
	public String getCreateTime() {
		return createTime;
	}

	/**
	 * @param createTime the createTime to set
	 */
	public void setCreateTime(String createTime) {
		this.createTime = createTime;
	}

	/**
	 * @return the virProcessInstanceState
	 */
	public VirProcessInstanceState getVirProcessInstanceState() {
		return virProcessInstanceState;
	}

	/**
	 * @param virProcessInstanceState the virProcessInstanceState to set
	 */
	public void setVirProcessInstanceState(
			VirProcessInstanceState virProcessInstanceState) {
		this.virProcessInstanceState = virProcessInstanceState;
	}

	/**
	 * @return the billKey
	 */
	public String getBillKey() {
		return billKey;
	}

	/**
	 * @param billKey the billKey to set
	 */
	public void setBillKey(String billKey) {
		this.billKey = billKey;
	}

	/**
	 * @return the billInfo
	 */
	public String getBillInfo() {
		return billInfo;
	}

	/**
	 * @param billInfo the billInfo to set
	 */
	public void setBillInfo(String billInfo) {
		this.billInfo = billInfo;
	}
	
	
}
