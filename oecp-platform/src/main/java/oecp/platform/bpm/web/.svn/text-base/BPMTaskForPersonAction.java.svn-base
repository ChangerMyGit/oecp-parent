/*
 * Copyright (c) 2011 OECP All Rights Reserved.                	
 * <a href="http://www.oecp.cn">http://www.oecp.cn</a>                                                                 
 */    

package oecp.platform.bpm.web;

import java.io.IOException;
import java.io.PrintWriter;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;

import oecp.framework.dao.QueryCondition;
import oecp.framework.dao.QueryResult;
import oecp.framework.exception.BizException;
import oecp.framework.web.JsonResult;
import oecp.platform.bpm.enums.ExceptionMsgType;
import oecp.platform.bpm.eo.MyAgentUser;
import oecp.platform.bpm.service.MyAgentUserService;
import oecp.platform.bpm.service.ProActivityService;
import oecp.platform.bpm.vo.HistoryTaskInfo;
import oecp.platform.bpm.vo.TaskInfo;
import oecp.platform.bpm.vo.VirProcessDefinitionInfo;
import oecp.platform.org.eo.Organization;
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
 * 个人的待办任务和已办任务
 * @author yangtao
 * @date 2011-8-23下午02:39:34
 * @version 1.0
 */
@Controller
@Scope("prototype")
@ParentPackage("default-package")
@Namespace("/bpm/person/task")
public class BPMTaskForPersonAction extends BasePlatformAction {
	
	@Autowired
	private ProActivityService proActivityServcie;
	
	@Autowired
	private MyAgentUserService myAgentUserService;
	
	private MyAgentUser myAgenter;
	
	private User selectedUser;
	
	private SimpleDateFormat dateformat=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	
	private List<QueryCondition> conditions = new ArrayList<QueryCondition>();
	/**
	 * 
	 * 待办任务列表
	 * @author yangtao
	 * @date 2011-7-26上午09:13:06
	 * @return
	 */
	@Action("undoTask")
	public String undoTask() {
		//查询条件
		String userName = this.getOnlineUser().getUser().getLoginId();
		String beginTime = this.getRequest().getParameter("beginTime");
		String endTime = this.getRequest().getParameter("endTime");
		String billInfo = this.getRequest().getParameter("billInfo");
		String orgId = this.getOnlineUser().getLoginedOrg().getId();
		
		Map<String,Object> map = new HashMap<String,Object>();
		map.put("orgId", orgId);
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
		jr.setContainFields(new String[] {"id","billInfo","billKey","taskId","taskName","assignee","executionId","formResourceName","deployId","processName","assignOrgId","createTime","counterSignRuleId","counterSignRuleName","nextTask","nextTaskUser","editBill"});
		this.setJsonString(jr.toJSONString());
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
		//查询条件
		String userName = this.getOnlineUser().getUser().getLoginId();
		String beginTime = this.getRequest().getParameter("beginTime");
		String endTime = this.getRequest().getParameter("endTime");
		String billInfo = this.getRequest().getParameter("billInfo");
		String orgId = this.getOnlineUser().getLoginedOrg().getId();
		
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
		QueryCondition qc2 = new QueryCondition("o.taskEo.virProcessInstance.virProDefinition.assignedOrg.id","=",orgId);
		conditions.add(qc2);

		QueryResult<HistoryTaskInfo> qr = this.proActivityServcie.getPersonDoneTasks(conditions, start, limit);
		
		JsonResult jr = new JsonResult(qr.getTotalrecord().intValue(), qr
				.getResultlist());
		jr.setContainFields(new String[] { "id","billInfo","taskId","billKey","processInstanceId","deployId","processName","activityName","status","startTime","endTime","auditUserName","auditOpinion","auditDecision","counterSignRuleId","counterSignRuleName","formResourceName","agentUserName"});
		this.setJsonString(jr.toJSONString());
		return SUCCESS;
	}
	
	/**
	 * 
	 * 执行某个任务 (待办任务中执行)
	 * @author yangtao
	 * @date 2011-7-26上午11:32:57
	 * @return
	 */
	@Action("completeTask")
	public String completeTask(){
		try{
			String taskId = this.getRequest().getParameter("taskId");//任务ID
			String nextperson = this.getRequest().getParameter("nextTaskUser");//下一个节点执行人
			String auditOpinion = this.getRequest().getParameter("auditOpinion");//审批意见
			String auditDecision = this.getRequest().getParameter("auditDecision");//审批决定
			String preTaskName = this.getRequest().getParameter("preTaskName");//驳回用到
			String nextTransitionName = this.getRequest().getParameter("nextTransitionName");//同意
			String userName = this.getOnlineUser().getUser().getLoginId();
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
	 * 执行某个任务 (表单中执行)
	 * @author yangtao
	 * @date 2011-7-26上午11:32:57
	 * @return
	 */
	@Action("completeTaskForBiz")
	public String completeTaskForBiz(){
		try{
			String funcKey = this.getRequest().getParameter("funcKey");
			String bizKey = this.getRequest().getParameter("bizKey");
			String billInfo = this.getRequest().getParameter("billInfo");
			String auditOpinion = this.getRequest().getParameter("auditOpinion");//审批意见
			String auditDecision = this.getRequest().getParameter("auditDecision");//审批决定
			String preTaskName = this.getRequest().getParameter("preTaskName");//驳回用到
			String nextTransitionName = this.getRequest().getParameter("nextTransitionName");//同意
			String userName = this.getOnlineUser().getUser().getLoginId();
			this.proActivityServcie.completeTask(funcKey, bizKey, billInfo, userName, auditOpinion, auditDecision,preTaskName,nextTransitionName);
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			handExcetionMsg(this.getResponse(),e.getMessage());
		}
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
			String histaskEoId = this.getRequest().getParameter("histaskEoId");
			String userName = this.getOnlineUser().getUser().getLoginId();
			this.proActivityServcie.withdrawTask(histaskEoId, userName);
			
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
			String taskId = this.getRequest().getParameter("taskId");
			String assignFlag = getRequest().getParameter("assignFlag");
			String ids = getRequest().getParameter("ids");
			String userName = this.getOnlineUser().getUser().getLoginId();
			this.proActivityServcie.reassignVirProActivity(taskId,assignFlag,ids,userName);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			handExcetionMsg(this.getResponse(),e.getMessage());
		}
		return SUCCESS;
	}
	/**
	 * 获取我的代理用户
	 * 
	 * @author YangTao
	 * @date 2012-2-6下午02:30:15
	 * @return
	 */
	@Action("getMyAgentUser")
	public String getMyAgentUser(){
		User user = this.selectedUser;
		if(user==null || StringUtils.isEmpty(user.getId()))
			user = this.getOnlineUser().getUser();
		QueryResult<MyAgentUser> qr = myAgentUserService.getMyAgentUser(user);
		JsonResult jr = new JsonResult(qr.getTotalrecord().intValue(), qr
				.getResultlist());
		jr.setContainFields(new String[] {  "id", "agent.id","agent.loginId", "agent.name",
				"agent.createTime", "agent.lastLoginTime", "agent.email", "agent.personId", "agent.state" });
		this.setJsonString(jr.toJSONString());
		return SUCCESS;
	}
	/**
	 * 获取代理用户代理的流程列表范围
	 * 
	 * @author YangTao
	 * @date 2012-2-6下午02:50:43
	 * @return
	 */
	@Action("getMyAgentUserProcessDef")
	public String getMyAgentUserProcessDef(){
		try {
			QueryResult<VirProcessDefinitionInfo> qr = this.myAgentUserService.getMyAgentProcessDef(myAgenter);
			JsonResult jr = new JsonResult(qr.getTotalrecord().intValue(), qr
					.getResultlist());
			jr.setContainFields(new String[] { "id","name","proDefinitionId","proDefinitionName","deployId","processDefinitionId","assignedOrgId","assignedOrgName","isUseId","isUseName","belongFunctionId","belongFunctionName"});
			this.setJsonString(jr.toJSONString());
		} catch (BizException e) {
			e.printStackTrace();
			this.handExcetionMsg(this.getResponse(), e.getMessage());
		}
		return SUCCESS;
	}
	/**
	 * 
	 * 根据组织查询出该组织下面的所有用户 自己创建的和上面分配的角色的用户（自己、和自己的代理人都不在里面）
	 * @author yangtao
	 * @date 2011-8-15下午02:55:53
	 * @return
	 */
	@Action(value = "queryUserList")
	public String queryUserList() {
		try {
			User user = this.selectedUser;
			if(user==null || StringUtils.isEmpty(user.getId()))
				user = this.getOnlineUser().getUser();

			// TODO 判断要查询的公司是否拥有权限，如果没有权限，则默认还是自己所在的公司
			QueryResult<User> qr = this.myAgentUserService.getAllUsers(myAgenter,user,
					conditions, -1, -1, getOrderBy());
			JsonResult jr = new JsonResult(qr.getTotalrecord().intValue(), qr
					.getResultlist());
			jr.setContainFields(new String[] { "id", "loginId", "name",
					"createTime", "lastLoginTime", "email", "personId", "state" });
			this.setJsonString(jr.toJSONString());
		} catch (BizException e) {
			e.printStackTrace();
			this.handExcetionMsg(this.getResponse(), e.getMessage());
		}
		return SUCCESS;
	}
	/**
	 * 获取有我参与审批的流程定义列表
	 * 
	 * @author YangTao
	 * @date 2012-2-7上午10:59:04
	 */
	@Action("getMyProcessDef")
	public String  getMyProcessDef(){
		User user = this.selectedUser;
		if(user==null || StringUtils.isEmpty(user.getId()))
			user = this.getOnlineUser().getUser();
		QueryResult<VirProcessDefinitionInfo> qr = new QueryResult<VirProcessDefinitionInfo>();
		try {
			qr = this.myAgentUserService.getMyProcessDef(myAgenter,user);
		} catch (BizException e) {
			e.printStackTrace();
			this.handExcetionMsg(this.getResponse(), e.getMessage());
		}
		JsonResult jr = new JsonResult(qr.getTotalrecord().intValue(), qr
				.getResultlist());
		jr.setContainFields(new String[] { "id","name","proDefinitionId","proDefinitionName","deployId","processDefinitionId","assignedOrgId","assignedOrgName","isUseId","isUseName","belongFunctionId","belongFunctionName"});
		this.setJsonString(jr.toJSONString());
		return SUCCESS;
	}
	/**
	 * 保存
	 * 
	 * @author YangTao
	 * @date 2012-2-7下午03:16:17
	 * @return
	 */
	@Action("saveMyAgentUser")
	public String saveMyAgentUser(){
		User user = this.selectedUser;
		if(user==null || StringUtils.isEmpty(user.getId()))
			user = this.getOnlineUser().getUser();
		myAgenter.setUser(user);
		myAgenter.setBeginTime(dateformat.format(new Date()));
		try {
			if(StringUtils.isNotEmpty(myAgenter.getId()))
				myAgentUserService.save(myAgenter);
			else
				myAgentUserService.create(myAgenter);
		} catch (BizException e) {
			e.printStackTrace();
			this.handExcetionMsg(this.getResponse(), e.getMessage());
		}
		setJsonString("{success:true,msg:'保存成功！'}");
		return SUCCESS;
	}
	/**
	 * 收回代理用户
	 * 
	 * @author YangTao
	 * @date 2012-2-8上午09:17:38
	 * @return
	 */
	@Action("takeBackAgent")
	public String takeBackAgent(){
		try {
			myAgenter = myAgentUserService.find(myAgenter.getId());
			myAgenter.setEndTime(dateformat.format(new Date()));
			myAgentUserService.save(myAgenter);
		} catch (BizException e) {
			e.printStackTrace();
		}
		setJsonString("{success:true,msg:'收回成功！'}");
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
			this.handExcetionMsg(this.getResponse(), e.getMessage());
		}finally{
			if(out!=null)
				out.close();
		}
	}

	public ProActivityService getProActivityServcie() {
		return proActivityServcie;
	}

	public void setProActivityServcie(ProActivityService proActivityServcie) {
		this.proActivityServcie = proActivityServcie;
	}

	public MyAgentUserService getMyAgentUserService() {
		return myAgentUserService;
	}

	public void setMyAgentUserService(MyAgentUserService myAgentUserService) {
		this.myAgentUserService = myAgentUserService;
	}

	public MyAgentUser getMyAgenter() {
		return myAgenter;
	}

	public void setMyAgenter(MyAgentUser myAgenter) {
		this.myAgenter = myAgenter;
	}

	public User getSelectedUser() {
		return selectedUser;
	}

	public void setSelectedUser(User selectedUser) {
		this.selectedUser = selectedUser;
	}

	public List<QueryCondition> getConditions() {
		return conditions;
	}

	public void setConditions(List<QueryCondition> conditions) {
		this.conditions = conditions;
	}
	
	

}
