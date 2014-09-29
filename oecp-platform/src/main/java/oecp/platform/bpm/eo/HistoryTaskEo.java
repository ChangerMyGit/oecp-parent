/*
 * Copyright (c) 2011 OECP All Rights Reserved.                	
 * <a href="http://www.oecp.cn">http://www.oecp.cn</a>                                                                 
 */    

package oecp.platform.bpm.eo;

import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import oecp.framework.entity.base.StringPKEO;
import oecp.platform.bpm.enums.TaskAuditDecision;
import oecp.platform.bpm.enums.TaskHistoryState;

/**
 * 对任务节点历史的封装
 * @author yangtao
 * @date 2011-7-29上午10:56:38
 * @version 1.0
 */
@Entity
@Table(name = "OECP_BPM_TASK_HIS")
public class HistoryTaskEo  extends StringPKEO {
	/**
	 * 
	 */
	private static final long serialVersionUID = 5369427019019386577L;

	//流程任务
	private TaskEo taskEo;

	//该任务审批人(用户账号)
	private String auditUserName;
	
	//该任务的代理审批人(用户账号)
	private String agentAuditUserName;
	
	//审批意见
	private String auditOpinion;
	
	//审批决定
	private TaskAuditDecision taskAuditDecision;
	
	//开始时间
	private String startTime;
	
	//结束时间
	private String endTime;
	
	//状态
	private TaskHistoryState taskHistoryState;
	
	
	

	/**
	 * @return the taskEo
	 */
	@ManyToOne
	public TaskEo getTaskEo() {
		return taskEo;
	}

	/**
	 * @param taskEo the taskEo to set
	 */
	public void setTaskEo(TaskEo taskEo) {
		this.taskEo = taskEo;
	}


	/**
	 * @return the auditUserName
	 */
	public String getAuditUserName() {
		return auditUserName;
	}

	/**
	 * @param auditUserName the auditUserName to set
	 */
	public void setAuditUserName(String auditUserName) {
		this.auditUserName = auditUserName;
	}

	/**
	 * @return the auditOpinion
	 */
	public String getAuditOpinion() {
		return auditOpinion;
	}

	/**
	 * @param auditOpinion the auditOpinion to set
	 */
	public void setAuditOpinion(String auditOpinion) {
		this.auditOpinion = auditOpinion;
	}


	/**
	 * @return the startTime
	 */
	public String getStartTime() {
		return startTime;
	}

	/**
	 * @param startTime the startTime to set
	 */
	public void setStartTime(String startTime) {
		this.startTime = startTime;
	}

	/**
	 * @return the endTime
	 */
	public String getEndTime() {
		return endTime;
	}

	/**
	 * @param endTime the endTime to set
	 */
	public void setEndTime(String endTime) {
		this.endTime = endTime;
	}

	/**
	 * @return the taskAuditDecision
	 */
	public TaskAuditDecision getTaskAuditDecision() {
		return taskAuditDecision;
	}

	/**
	 * @param taskAuditDecision the taskAuditDecision to set
	 */
	public void setTaskAuditDecision(TaskAuditDecision taskAuditDecision) {
		this.taskAuditDecision = taskAuditDecision;
	}

	/**
	 * @return the taskHistoryState
	 */
	public TaskHistoryState getTaskHistoryState() {
		return taskHistoryState;
	}

	/**
	 * @param taskHistoryState the taskHistoryState to set
	 */
	public void setTaskHistoryState(TaskHistoryState taskHistoryState) {
		this.taskHistoryState = taskHistoryState;
	}

	public String getAgentAuditUserName() {
		return agentAuditUserName;
	}

	public void setAgentAuditUserName(String agentAuditUserName) {
		this.agentAuditUserName = agentAuditUserName;
	}

	
	
	
}
