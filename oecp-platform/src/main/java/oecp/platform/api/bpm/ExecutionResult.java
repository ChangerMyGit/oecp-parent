package oecp.platform.api.bpm;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlType;

/**
 * 工作流办理后的返回结果
 * 
 * @author yongtree
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType
public class ExecutionResult {
	

	private ProcessInstanceState processInstanceState;// 流程执行的状态

	private TaskState taskState;// 流程任务的状态

	private String processInstanceId;// 流程实例ID

	private String message;// 流程办理完后产生的消息
	
	public TaskState getTaskState() {
		return taskState;
	}

	public void setTaskState(TaskState taskState) {
		this.taskState = taskState;
	}

	public ProcessInstanceState getProcessInstanceState() {
		return processInstanceState;
	}

	public void setProcessInstanceState(ProcessInstanceState processInstanceState) {
		this.processInstanceState = processInstanceState;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public String getProcessInstanceId() {
		return processInstanceId;
	}

	public void setProcessInstanceId(String processInstanceId) {
		this.processInstanceId = processInstanceId;
	}

	
}
