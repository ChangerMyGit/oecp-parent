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
import javax.persistence.OneToMany;
import javax.persistence.Table;

import oecp.framework.entity.base.StringPKEO;
import oecp.platform.bpm.enums.CounterSignRule;
import oecp.platform.user.eo.User;

/**
 * 流程任务的封装
 * @author yangtao
 * @date 2011-8-30上午10:16:32
 * @version 1.0
 */
@Entity
@Table(name = "OECP_BPM_TASK")
public class TaskEo  extends StringPKEO {
	//流程实例
	private VirProcessInstance virProcessInstance;

	//任务ID
	private String taskId;
	
	//前一个任务的ID
	private String preTaskId;
	
	//任务名称
	private String taskName;
	
	//任务会签规则
	private CounterSignRule counterSignRule;
	
	//通过比例值
	private String passRate;
	
	//开始时间
	private String startTime;
	
	//结束时间
	private String endTime;
	
	//是否结束
	private Boolean isEnd;
	
	//任务的候选人
	private List<CandidateUser> candidateUsers  = new ArrayList<CandidateUser>();
	
	//审批通过数
	private int agreenCount;
	
	//审批不通过数
	private int noAgreenCount;
	
	//任务候选人的个数，在任务刚建成时初始化
	private int candidateUserCount;
	
	//是否指定下个任务结点：否 0 是 1
	private String nextTask;
	//是否指定下个任务结点的人员：否 0 是 1
	private String nextTaskUser;
	//是否可编辑单据
	private String editBill;

	/**
	 * @return the virProcessInstance
	 */
	@ManyToOne
	public VirProcessInstance getVirProcessInstance() {
		return virProcessInstance;
	}

	/**
	 * @param virProcessInstance the virProcessInstance to set
	 */
	public void setVirProcessInstance(VirProcessInstance virProcessInstance) {
		this.virProcessInstance = virProcessInstance;
	}

	/**
	 * @return the taskId
	 */
	public String getTaskId() {
		return taskId;
	}

	/**
	 * @param taskId the taskId to set
	 */
	public void setTaskId(String taskId) {
		this.taskId = taskId;
	}

	/**
	 * @return the taskName
	 */
	public String getTaskName() {
		return taskName;
	}

	/**
	 * @param taskName the taskName to set
	 */
	public void setTaskName(String taskName) {
		this.taskName = taskName;
	}

	/**
	 * @return the counterSignRule
	 */
	public CounterSignRule getCounterSignRule() {
		return counterSignRule;
	}

	/**
	 * @param counterSignRule the counterSignRule to set
	 */
	public void setCounterSignRule(CounterSignRule counterSignRule) {
		this.counterSignRule = counterSignRule;
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
	 * @return the isEnd
	 */
	public Boolean getIsEnd() {
		return isEnd;
	}

	/**
	 * @param isEnd the isEnd to set
	 */
	public void setIsEnd(Boolean isEnd) {
		this.isEnd = isEnd;
	}

	/**
	 * @return the candidateUsers
	 */
	@OneToMany(cascade = { CascadeType.REMOVE,CascadeType.PERSIST },orphanRemoval=true)
	@JoinTable(name = "OECP_BPM_TASK_CANDIDATE_USER", joinColumns = { @JoinColumn(name = "task_pk") }, inverseJoinColumns = { @JoinColumn(name = "candidateuser_pk") })
	public List<CandidateUser> getCandidateUsers() {
		return candidateUsers;
	}

	/**
	 * @param candidateUsers the candidateUsers to set
	 */
	public void setCandidateUsers(List<CandidateUser> candidateUsers) {
		this.candidateUsers = candidateUsers;
	}


	/**
	 * @return the agreenCount
	 */
	public int getAgreenCount() {
		return agreenCount;
	}

	/**
	 * @param agreenCount the agreenCount to set
	 */
	public void setAgreenCount(int agreenCount) {
		this.agreenCount = agreenCount;
	}

	/**
	 * @return the noAgreenCount
	 */
	public int getNoAgreenCount() {
		return noAgreenCount;
	}

	/**
	 * @param noAgreenCount the noAgreenCount to set
	 */
	public void setNoAgreenCount(int noAgreenCount) {
		this.noAgreenCount = noAgreenCount;
	}

	/**
	 * @return the passRate
	 */
	public String getPassRate() {
		return passRate;
	}

	/**
	 * @param passRate the passRate to set
	 */
	public void setPassRate(String passRate) {
		this.passRate = passRate;
	}

	public int getCandidateUserCount() {
		return candidateUserCount;
	}

	public void setCandidateUserCount(int candidateUserCount) {
		this.candidateUserCount = candidateUserCount;
	}

	public String getPreTaskId() {
		return preTaskId;
	}

	public void setPreTaskId(String preTaskId) {
		this.preTaskId = preTaskId;
	}

	public String getNextTask() {
		return nextTask;
	}

	public void setNextTask(String nextTask) {
		this.nextTask = nextTask;
	}

	public String getNextTaskUser() {
		return nextTaskUser;
	}

	public void setNextTaskUser(String nextTaskUser) {
		this.nextTaskUser = nextTaskUser;
	}

	public String getEditBill() {
		return editBill;
	}

	public void setEditBill(String editBill) {
		this.editBill = editBill;
	}
	
}
