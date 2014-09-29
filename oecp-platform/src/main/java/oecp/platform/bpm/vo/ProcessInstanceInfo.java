package oecp.platform.bpm.vo;
/**
 * 
 * jbpm中流程实例的封装
 * @author yangtao
 * @date 2011-7-27上午10:28:39
 * @version 1.0
 */
public class ProcessInstanceInfo {
	//虚拟流程实例ID
	private String virProcessInstanceId;
	//流程实例ID
	private String processInstanceId;
	//流程定义ID
	private String processDefinitionId;
	//流程部署ID
	private String deployId;
	//流程名称
	private String processName;
	//流程版本
	private int processVersion;
	//该流程实例的启动人
	private String createUserLoginId;
	//启动时间
	private String createTime;
	//状态
	private String status;
	//业务主键
	private String billKey;
	//业务单据
	private String billInfo;

	
	public ProcessInstanceInfo() {
		
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
	 * @return the processName
	 */
	public String getProcessName() {
		return processName;
	}


	/**
	 * @param processName the processName to set
	 */
	public void setProcessName(String processName) {
		this.processName = processName;
	}


	/**
	 * @return the processVersion
	 */
	public int getProcessVersion() {
		return processVersion;
	}


	/**
	 * @param processVersion the processVersion to set
	 */
	public void setProcessVersion(int processVersion) {
		this.processVersion = processVersion;
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
	 * @return the status
	 */
	public String getStatus() {
		return status;
	}


	/**
	 * @param status the status to set
	 */
	public void setStatus(String status) {
		this.status = status;
	}



	/**
	 * @return the virProcessInstanceId
	 */
	public String getVirProcessInstanceId() {
		return virProcessInstanceId;
	}



	/**
	 * @param virProcessInstanceId the virProcessInstanceId to set
	 */
	public void setVirProcessInstanceId(String virProcessInstanceId) {
		this.virProcessInstanceId = virProcessInstanceId;
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



	public String getBillInfo() {
		return billInfo;
	}



	public void setBillInfo(String billInfo) {
		this.billInfo = billInfo;
	}
	
	
}
