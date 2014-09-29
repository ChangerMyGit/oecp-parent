/*
 * Copyright (c) 2011 OECP All Rights Reserved.                	
 * <a href="http://www.oecp.cn">http://www.oecp.cn</a>                                                                 
 */

package oecp.platform.bpm.web;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;

import oecp.framework.dao.QueryCondition;
import oecp.framework.dao.QueryResult;
import oecp.framework.web.JsonResult;
import oecp.platform.bpm.enums.ExceptionMsgType;
import oecp.platform.bpm.service.ProActivityService;
import oecp.platform.bpm.service.ProExecutionService;
import oecp.platform.bpm.vo.HistoryTaskInfo;
import oecp.platform.bpm.vo.TaskInfo;
import oecp.platform.user.eo.User;
import oecp.platform.web.BasePlatformAction;

import org.apache.commons.lang.StringUtils;
import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.Namespace;
import org.apache.struts2.convention.annotation.ParentPackage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

/**
 * 流程任务action
 * 
 * @author yangtao
 * @date 2011-7-26 下午02:36:42
 * @version 1.0
 */
@Controller
@Scope("prototype")
@ParentPackage("default-package")
@Namespace("/bpm/task")
public class BPMTaskAction extends BasePlatformAction {

	private static final long serialVersionUID = 1L;


	@Autowired
	private ProActivityService proActivityServcie;
	@Autowired
	private ProExecutionService proExecutionService;
	
	private String billKey;
	private String activityName;

	/**
	 * 
	 * 待办任务列表
	 * @author yangtao
	 * @date 2011-7-26上午09:13:06
	 * @return
	 */
	@Action("list")
	public String list() {
		//从前台获取参数
		String userName = this.getRequest().getParameter("userName");
		String beginTime = this.getRequest().getParameter("beginTime");
		String endTime = this.getRequest().getParameter("endTime");
		String billInfo = this.getRequest().getParameter("billInfo");
		String orgId = this.getOnlineUser().getLoginedOrg().getId();
		
		//查询平台中封装的任务
		Map<String,Object> map = new HashMap<String,Object>();
//		map.put("orgId", orgId);
		if(StringUtils.isNotEmpty(userName))
			map.put("userName", userName);
		if(StringUtils.isNotEmpty(beginTime))
			map.put("beginTime", beginTime);
		if(StringUtils.isNotEmpty(endTime))
			map.put("endTime", endTime);
		if(StringUtils.isNotEmpty(billInfo))
			map.put("billInfo", billInfo);
		QueryResult<TaskInfo> qr = this.proActivityServcie.getPersonUnDoneTasks(map, start, limit);
		JsonResult jr = new JsonResult(qr.getTotalrecord().intValue(), qr
				.getResultlist());
		jr.setContainFields(new String[] {"id","billInfo","billKey","taskId","taskName","assignee","executionId","formResourceName","deployId","processName","assignOrgId","createTime","taskCandiateUser","counterSignRuleId","counterSignRuleName","nextTask","nextTaskUser","editBill"});
		this.setJsonString(jr.toJSONString());
		return SUCCESS;
	}
	
	/**
	 * 
	 * 执行某个任务
	 * @author yangtao
	 * @date 2011-7-26上午11:32:57
	 * @return
	 */
	@Action("completeTask")
	public String completeTask(){
		try{
			//从前台获取参数
			String taskId = this.getRequest().getParameter("taskId");//任务ID
			String nextperson = this.getRequest().getParameter("nextTaskUser");//下一个节点执行人
			String auditOpinion = this.getRequest().getParameter("auditOpinion");//审批意见
			String auditDecision = this.getRequest().getParameter("auditDecision");//审批决定
			String preTaskName = this.getRequest().getParameter("preTaskName");//驳回用到
			String nextTransitionName = this.getRequest().getParameter("nextTransitionName");//同意
			String userName = this.getOnlineUser().getUser().getLoginId();
			//执行服务方法
			this.proActivityServcie.completeTask(taskId,nextperson,auditOpinion,auditDecision,true,userName,preTaskName,nextTransitionName);
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			handExcetionMsg(this.getResponse(),e.getMessage());
		}
		return SUCCESS;
	}

	/**
	 * 
	 * 把任务重新指派给某一个人
	 * @author yangtao
	 * @date 2011-7-26上午11:34:59
	 * @return
	 */
	@Action("reassignPerson")
	public String reassignPerson(){
		try{
			//从前台获取参数
			String taskId = this.getRequest().getParameter("taskId");
			String userName = this.getRequest().getParameter("userName");
//			this.proActivityServcie.assignTask(taskId, userName);
			//执行服务方法
			this.proActivityServcie.addCandidateUser(taskId, userName);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			handExcetionMsg(this.getResponse(),e.getMessage());
		}
		return SUCCESS;
	}
	
	/**
	 * 
	 * 重新给某个待办任务委派候选人
	 * @author yangtao
	 * @date 2011-8-17上午09:43:19
	 * @return
	 */
	@Action("reassignVirProActivity")
	public String reassignVirProActivity(){
		try{
			//从前台获取参数
			String taskId = this.getRequest().getParameter("taskId");
			String assignFlag = getRequest().getParameter("assignFlag");
			String ids = getRequest().getParameter("ids");
			String userName = this.getOnlineUser().getUser().getLoginId();
			//执行服务方法
			this.proActivityServcie.reassignVirProActivity(taskId,assignFlag,ids,userName);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			handExcetionMsg(this.getResponse(),e.getMessage());
		}
		return SUCCESS;
	}
	
	/**
	 * 
	 * 退回到上一个任务
	 * @author yangtao
	 * @date 2011-8-5下午01:57:33
	 * @return
	 */
	@Action(value="reject")
	public String reject(){
		try{
			//从前台获取参数
			String taskId = this.getRequest().getParameter("taskId");//任务ID
			String nextperson = this.getRequest().getParameter("nextTaskUser");//下一个节点执行人
			String auditOpinion = this.getRequest().getParameter("auditOpinion");//审批意见
			String auditDecision = this.getRequest().getParameter("auditDecision");//审批决定
			String userName = this.getOnlineUser().getUser().getLoginId();
			//执行服务方法
			this.proActivityServcie.completeTask(taskId,nextperson,auditOpinion,auditDecision,true,userName,null,null);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			handExcetionMsg(this.getResponse(),e.getMessage());
		}
		return SUCCESS;
	}

	/**
	 * 
	 * 查询某个人的已办任务
	 * @author yangtao
	 * @date 2011-8-8上午09:38:25
	 * @return
	 */
	@Action("doneTask")
	public String doneTask(){
		//从前台获取参数
		String userName = this.getRequest().getParameter("userName");
		String beginTime = this.getRequest().getParameter("beginTime");
		String endTime = this.getRequest().getParameter("endTime");
		String billInfo = this.getRequest().getParameter("billInfo");
		String orgId = this.getOnlineUser().getLoginedOrg().getId();
		//封装前台获取的参数
		List<QueryCondition> conditions = new ArrayList<QueryCondition>();
		if(StringUtils.isNotEmpty(userName)){
			QueryCondition qc = new QueryCondition("o.auditUserName","=",userName);
			conditions.add(qc);
		}
		if(StringUtils.isNotEmpty(beginTime)){
			QueryCondition qc = new QueryCondition("o.startTime",">=",beginTime.replace("T", " "));
			conditions.add(qc);
		}
		if(StringUtils.isNotEmpty(endTime)){
			QueryCondition qc = new QueryCondition("o.endTime","<=",endTime.replace("T", " "));
			conditions.add(qc);
		}
		if(StringUtils.isNotEmpty(billInfo)){
			QueryCondition qc = new QueryCondition("o.taskEo.virProcessInstance.billInfo","like","%"+billInfo+"%");
			conditions.add(qc);
		}
//		QueryCondition qc2 = new QueryCondition("o.taskEo.virProcessInstance.virProDefinition.assignedOrg.id","=",orgId);
//		conditions.add(qc2);
		//调用服务
		QueryResult<HistoryTaskInfo> qr = this.proActivityServcie.getPersonDoneTasks(conditions, start, limit);
		//封装向前台展示的数据
		JsonResult jr = new JsonResult(qr.getTotalrecord().intValue(), qr
				.getResultlist());
		jr.setContainFields(new String[] {"id","billInfo", "taskId","billKey","processInstanceId","deployId","processName","activityName","status","startTime","endTime","auditUserName","auditOpinion","auditDecision","counterSignRuleId","counterSignRuleName","formResourceName","agentUserName"});
		this.setJsonString(jr.toJSONString());
		return SUCCESS;
	}
	
	/**
	 * 
	 * 取回任务
	 * @author yangtao
	 * @date 2011-8-10下午02:54:56
	 * @return
	 */
	@Action("withdraw")
	public String withdraw(){
		try {
			//从前台获取参数
			String histaskEoId = this.getRequest().getParameter("histaskEoId");
			String userName = this.getOnlineUser().getUser().getLoginId();
			//调用服务
			this.proActivityServcie.withdrawTask(histaskEoId, userName);
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			handExcetionMsg(this.getResponse(),e.getMessage());
		}
		return SUCCESS;
	}
	
	/**
	 * 根据当前任务返回流程图中这个任务上面的所有任务节点
	 * 
	 * @author YangTao
	 * @date 2012-1-29下午03:00:03
	 * @param taskId
	 * @return
	 */
	@Action("getPreTaskByCurrentTask")
	public String getPreTaskByCurrentTask(){
		String billKey = this.getRequest().getParameter("billKey");
		QueryResult<TaskInfo> qr = this.proActivityServcie.getPreTaskByCurrentTask(billKey);
		JsonResult jr = new JsonResult(qr.getTotalrecord().intValue(), qr
				.getResultlist());
		jr.setContainFields(new String[] {"taskName"});
		this.setJsonString(jr.toJSONString());
		return SUCCESS;
	}
	
	/**
	 * 根据当前任务返回和当前结点连接的下一个任务（多个）
	 * @author yangtao
	 * @date 2013-5-9下午4:58:10
	 * @return
	 */
	@Action("getNextTasksByCurrentTask")
	public String getNextTasksByCurrentTask(){
		String billKey = this.getRequest().getParameter("billKey");
		QueryResult<TaskInfo> qr = this.proActivityServcie.getNextTasksByCurrentTask(billKey);
		JsonResult jr = new JsonResult(qr.getTotalrecord().intValue(), qr
				.getResultlist());
		jr.setContainFields(new String[] {"taskName","incomeTransitionName"});
		this.setJsonString(jr.toJSONString());
		return SUCCESS;
	}
	
	/**
	 * 根据业务主键，获取功能流程查看页面路径
	 * 
	 * @author YangTao
	 * @date 2012-2-1上午10:03:25
	 * @param billKey
	 * @return
	 */
	@Action("getFormResourceName")
	public String getFormResourceName(){
		String billKey = this.getRequest().getParameter("billKey");
		String result = this.proActivityServcie.getFormResourceName(billKey);
		this.setJsonString(result);
		return SUCCESS;
	}
	/**
	 * 
	 * 处理业务异常信息,返回到页面
	 * @author yangtao
	 * @date 2011-8-19上午11:10:55
	 * @param response
	 * @param message
	 */
	private void handExcetionMsg(HttpServletResponse response,String message){
		if(StringUtils.isEmpty(message))
			message = ExceptionMsgType.EXECUTE_FAILURE;
		PrintWriter out = null;
		try {
			response.setCharacterEncoding("utf-8");
			response.setContentType("text/xml;charset=utf-8");
			out = response.getWriter();
			out.println(message);
			response.setStatus(HttpServletResponse.SC_EXPECTATION_FAILED);
			out.flush();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}finally{
			if(out!=null)
				out.close();
		}
	}
	
	/**
	 * 根据结点名称获取该结点配置的人员
	 * @author yangtao
	 * @date 2013-5-13下午2:37:55
	 * @return
	 */
	@Action(value = "queryUserList")
	public String queryUserList() {
		try {
			List<User> list = this.proActivityServcie.queryUserFromVpa(billKey,activityName);
			JsonResult jr = new JsonResult(list.size(), list);
			jr.setContainFields(new String[] { "id", "loginId", "name",
					"createTime", "lastLoginTime", "email", "personId", "state" });
			this.setJsonString(jr.toJSONString());
		} catch (Exception e) {
			e.printStackTrace();
		}
		return SUCCESS;
	}
	
	/**
	 * @return the proActivityServcie
	 */
	public ProActivityService getProActivityServcie() {
		return proActivityServcie;
	}

	/**
	 * @param proActivityServcie the proActivityServcie to set
	 */
	public void setProActivityServcie(ProActivityService proActivityServcie) {
		this.proActivityServcie = proActivityServcie;
	}

	public String getBillKey() {
		return billKey;
	}

	public void setBillKey(String billKey) {
		this.billKey = billKey;
	}

	public String getActivityName() {
		return activityName;
	}

	public void setActivityName(String activityName) {
		this.activityName = activityName;
	}
	
	
}
