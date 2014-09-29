/*
 * Copyright (c) 2011 OECP All Rights Reserved.                	
 * http://www.oecp.cn                                                                 
 */

package oecp.platform.api.bpm.webservice;

import javax.annotation.Resource;
import javax.jws.WebParam;
import javax.jws.WebResult;
import javax.jws.WebService;

import oecp.platform.api.bpm.ExecutionAPIService;
import oecp.platform.api.bpm.ExecutionResult;

import org.apache.cxf.annotations.WSDLDocumentation;
import org.springframework.stereotype.Service;

/**
 * 工作流对外API服务实现
 * 
 * @author yongtree
 * @date 2011-4-12 下午04:55:53
 * @version 1.0
 */
@Service("bpmAPI")
@WebService(targetNamespace="http://www.oecp.cn",serviceName="bpmAPI")
//@WSDLDocumentationCollection(value={
//		@WSDLDocumentation(placement=Placement.SERVICE,value="[API描述：工作流对外的服务接口][返回值描述：1、ExecutionResut {processInstanceState,taskState,processInstanceId,message}]"),
//		@WSDLDocumentation(placement=Placement.TOP,value="业务流程驱动服务,提供对业务流程的操作接口.版权归<a href='http://www.oecp.cn'>OECP社区</a>所有."),
//	})
public class ExecutionAPIImpl implements ExecutionAPI {

	
	@Resource
	private ExecutionAPIService executionAPIService;


	@WSDLDocumentation("<p>功能描述：开启一个流程。</p>"
			+ "<p>参数描述:</br>1 funcCode 业务功能编号（功能注册的编号）</br>2 billKey单据编号或主键</br>3 orgId 组织ID</br>4 userID用户ID（主键）</br> 5 variables流程变量（XML格式）。</p>"
			+ "<p>返回值描述：ExecutionResult 流程执行结果</p>")
	@WebResult(name="bpmResult")
	public ExecutionResult startProcessInstance(
			@WebParam(name="funcCode")String funcCode, 
			@WebParam(name="billKey")String billKey,
			@WebParam(name="billInfo")String billInfo,
			@WebParam(name="orgId")String orgId,
			@WebParam(name="userID")String userID, 
			@WebParam(name="variables")String variables) {
//		ExecutionResult executionResult = this.executionAPIService.startProcessInstance(funcCode, billKey,billInfo, orgId, userID, variables);
 		return null;
	}

	@WSDLDocumentation("<p>功能描述：提交一个任务。</p>"
			+ "<p>参数描述：</br>1 funcCode 业务功能编号（功能注册的编号）</br>2 billKey单据编号或主键</br>3 userID用户ID（主键）</br> 4 variables流程变量（XML格式）</br>5 message任务备注（审批意见）。</p>"
			+ "<p>返回值描述：ExecutionResult 流程执行结果</p>")
	@WebResult(name="bpmResult")
	public ExecutionResult completeTask(
			@WebParam(name="funcCode")String funcCode, 
			@WebParam(name="billKey")String billKey,
			@WebParam(name="billInfo")String billInfo,
			@WebParam(name="userID")String userID, 
			@WebParam(name="variables")String variables, 
			@WebParam(name="message")String message) {
		// TODO Auto-generated method stub
//		ExecutionResult executionResult = this.executionAPIService.completeTask(funcCode, billKey,billInfo, userID, variables, message);
		return null;
	}

	@WSDLDocumentation("<p>功能描述：退回任务，向上一个任务提交者退回。</p>"
			+ "<p>参数描述：</br>1 funcCode 业务功能编号（功能注册的编号）</br>2 billKey单据编号或主键</br>3 userID用户ID（主键）</br> 4 clearHist是否清除历史</br>5 message任务备注（审批意见）。</p>"
			+ "<p>返回值描述：ExecutionResult 流程执行结果<p>")
	@WebResult(name="bpmResult")
	public ExecutionResult rollbackTask(
			@WebParam(name="funcCode")String funcCode, 
			@WebParam(name="billKey")String billKey,
			@WebParam(name="billInfo")String billInfo,
			@WebParam(name="userID")String userID, 
			@WebParam(name="clearHist")boolean clearHist, 
			@WebParam(name="message")String message) {
		// TODO Auto-generated method stub
//		ExecutionResult executionResult = this.executionAPIService.rollbackTask(funcCode, billKey,billInfo, userID,  clearHist, message);
		return null;
	}

	@WSDLDocumentation("<p>功能描述：取回任务 ，当下一执行人办理后，则任务无法取回</p>"
			+ "<p>参数描述：</br>1 funcCode 业务功能编号（功能注册的编号）</br>2 billKey单据编号或主键</br>3、userID用户ID（主键）</br> 4 clearHist是否清除历史</br>5 message任务备注（审批意见）。</p>"
			+ "<p>返回值描述：ExecutionResult 流程执行结果<p>")
	@WebResult(name="bpmResult")
	public ExecutionResult withdrawTask(
			@WebParam(name="funcCode")String funcCode, 
			@WebParam(name="billKey")String billKey,
			@WebParam(name="billInfo")String billInfo,
			@WebParam(name="userID")String userID) {
		// TODO Auto-generated method stub
//		ExecutionResult executionResult = this.executionAPIService.withdrawTask(funcCode, billKey,billInfo, userID);
		return null;
	}

}
