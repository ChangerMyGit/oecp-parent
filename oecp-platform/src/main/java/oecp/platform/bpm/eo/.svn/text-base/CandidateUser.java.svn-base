package oecp.platform.bpm.eo;

import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import oecp.framework.entity.base.StringPKEO;
import oecp.platform.user.eo.User;

/**
 * 任务候选人，包括用户和代理人信息
 *
 * @author YangTao
 * @date 2012-2-3上午09:37:04
 * @version 1.0
 */
@Entity
@Table(name="OECP_BPM_CANDIDATE_USER")
public class CandidateUser extends StringPKEO{

	/**
	 * 
	 */
	private static final long serialVersionUID = -8096158402095266045L;

	//用户
	private User user;
	
	//用户的代理人
	private User agent;
	
	//是否执行任务
	private Boolean isExecutedTask;
	
	public CandidateUser(){
		
	}
	
	public CandidateUser(User user,User agent){
		this.user = user;
		this.agent = agent;
		this.isExecutedTask = false;
	}

	@ManyToOne
	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	@ManyToOne
	public User getAgent() {
		return agent;
	}

	public void setAgent(User agent) {
		this.agent = agent;
	}

	public Boolean getIsExecutedTask() {
		return isExecutedTask;
	}

	public void setIsExecutedTask(Boolean isExecutedTask) {
		this.isExecutedTask = isExecutedTask;
	}
	
	
	
}
