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
import javax.persistence.Lob;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import org.hibernate.annotations.Cascade;

import oecp.framework.entity.base.StringPKEO;
import oecp.platform.bpm.enums.CounterSignRule;
import oecp.platform.org.eo.Post;
import oecp.platform.role.eo.OrgRole;
import oecp.platform.user.eo.User;

/**
 * 
 * 虚拟流程的节点
 * @author yangtao
 * @date 2011-8-2上午08:27:17
 * @version 1.0
 */
@Entity
@Table(name = "OECP_BPM_VIR_ACTIVITY")
public class VirProActivity extends StringPKEO {


	/**
	 * 
	 */
	private static final long serialVersionUID = -400221894468507256L;

	private VirProDefinition virProDefinition;

	private String activityName;
	//任务会签规则
	private CounterSignRule counterSignRule;
	//比例值
	private String passRate;
	//条件
	private List<DecisionCondition> decisionConditions = new ArrayList<DecisionCondition>();
	// 参与的用户，优先级高
	private List<User> assignUsers = new ArrayList<User>();
	// 参与的岗位，优先级中
	private List<Post> assignPosts = new ArrayList<Post>();
	// 参与的角色，优先级低
	private List<OrgRole> assignRoles = new ArrayList<OrgRole>();
	// 流程发起人
	private Boolean isCommitUser;
	//条件节点，后期改为用GROOVY脚本来实现
	private String decisionScript;
	//是否指定下个任务结点：否 0 是 1
	private String nextTask;
	//是否指定下个任务结点的人员：否 0 是 1
	private String nextTaskUser;
	//是否可编辑单据
	private String editBill;
	
	@ManyToMany()
	@JoinTable(name = "OECP_BPM_VIR_ACTIVITY_USER", joinColumns = { @JoinColumn(name = "viractivity_pk") }, inverseJoinColumns = { @JoinColumn(name = "user_pk") })
	public List<User> getAssignUsers() {
		return assignUsers;
	}

	public void setAssignUsers(List<User> assignUsers) {
		this.assignUsers = assignUsers;
	}

	@ManyToMany()
	@JoinTable(name = "OECP_BPM_VIR_ACTIVITY_ORGROLE", joinColumns = { @JoinColumn(name = "viractivity_pk") }, inverseJoinColumns = { @JoinColumn(name = "orgrole_pk") })
	public List<OrgRole> getAssignRoles() {
		return assignRoles;
	}

	public void setAssignRoles(List<OrgRole> assignRoles) {
		this.assignRoles = assignRoles;
	}

	@ManyToMany()
	@JoinTable(name = "OECP_BPM_VIR_ACTIVITY_POST", joinColumns = { @JoinColumn(name = "viractivity_pk") }, inverseJoinColumns = { @JoinColumn(name = "post_pk") })
	public List<Post> getAssignPosts() {
		return assignPosts;
	}

	public void setAssignPosts(List<Post> assignPosts) {
		this.assignPosts = assignPosts;
	}

	
	
	@ManyToMany()
	@JoinTable(name = "OECP_BPM_VIR_ACTIVITY_DECISION", joinColumns = { @JoinColumn(name = "viractivity_pk") }, inverseJoinColumns = { @JoinColumn(name = "decondition_pk") })
	public List<DecisionCondition> getDecisionConditions() {
		return decisionConditions;
	}

	/**
	 * @param decisionConditions the decisionConditions to set
	 */
	public void setDecisionConditions(List<DecisionCondition> decisionConditions) {
		this.decisionConditions = decisionConditions;
	}

	public String getActivityName() {
		return activityName;
	}

	public void setActivityName(String activityName) {
		this.activityName = activityName;
	}

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

	@Lob
	public String getDecisionScript() {
		return decisionScript;
	}

	public void setDecisionScript(String decisionScript) {
		this.decisionScript = decisionScript;
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

	public Boolean getIsCommitUser() {
		return isCommitUser;
	}

	public void setIsCommitUser(Boolean isCommitUser) {
		this.isCommitUser = isCommitUser;
	}

	
	
	
}
