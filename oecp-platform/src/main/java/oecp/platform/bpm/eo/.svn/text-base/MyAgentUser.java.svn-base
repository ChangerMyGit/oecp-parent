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
import oecp.platform.user.eo.User;

/**
 * 我的代理人
 *
 * @author YangTao
 * @date 2012-2-2下午01:52:58
 * @version 1.0
 */
@Entity
@Table(name="OECP_BPM_MY_AGENT_CONFIG")
public class MyAgentUser extends StringPKEO{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1332019685834808563L;

	//流程定义
	private List<VirProDefinition> virProDefinitions = new ArrayList<VirProDefinition>();
	
	//用户
	private User user;
	
	//用户的代理人
	private User agent;
	
	//开始时间
	private String beginTime;
	
	//结束时间
	private String endTime;
	
	
	@ManyToMany(cascade={CascadeType.REMOVE})
	@JoinTable(name="OECP_BPM_MY_AGENT_VIRPRODEF",joinColumns={@JoinColumn(name="myagentuser_pk")},inverseJoinColumns={@JoinColumn(name="virprodef_pk")})
	public List<VirProDefinition> getVirProDefinitions() {
		return virProDefinitions;
	}

	public void setVirProDefinitions(List<VirProDefinition> virProDefinitions) {
		this.virProDefinitions = virProDefinitions;
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

	public String getBeginTime() {
		return beginTime;
	}

	public void setBeginTime(String beginTime) {
		this.beginTime = beginTime;
	}

	public String getEndTime() {
		return endTime;
	}

	public void setEndTime(String endTime) {
		this.endTime = endTime;
	}
	
	
}
