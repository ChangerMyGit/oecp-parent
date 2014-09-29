/*
 * Copyright (c) 2011 OECP All Rights Reserved.                	
 * <a href="http://www.oecp.cn">OECP</a> 
 */

package oecp.platform.bpm.service;

import oecp.framework.util.enums.EnumDescription;

import org.jbpm.api.ProcessInstance;

/**
 * 流程执行的结果
 * 
 * @author yongtree
 * @date 2011-7-4 上午10:51:03
 * @version 1.0
 * 
 */
public class ProExecutionResult {

	private ExecutionState state;

	private ProcessInstance processInstance;
	
	public ProExecutionResult(ExecutionState state){
		this.state=state;
	}
	public ProExecutionResult(ExecutionState state,ProcessInstance processInstance){
		this.state=state;
		this.processInstance=processInstance;
	}

	public ExecutionState getState() {
		return state;
	}

	public ProcessInstance getProcessInstance() {
		return processInstance;
	}


	public enum ExecutionState {
		@EnumDescription("成功")
		SUCCESS, @EnumDescription("未开启")
		UNOPENED, @EnumDescription("未分配")
		UNALLOCATED
	}
	
	

}
