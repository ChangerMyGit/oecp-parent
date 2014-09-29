/*
 * Copyright (c) 2011 OECP All Rights Reserved.                	
 * <a href="http://www.oecp.cn">OECP</a> 
 */

package oecp.platform.bpm.service;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import oecp.framework.dao.QueryCondition;
import oecp.framework.dao.QueryResult;
import oecp.framework.exception.BizException;
import oecp.framework.util.SpringContextUtil;
import oecp.platform.base.service.PlatformBaseServiceImpl;
import oecp.platform.bcfunction.eo.Function;
import oecp.platform.bcfunction.service.BcFunctionService;
import oecp.platform.bpm.bizservice.BizServiceForBpm;
import oecp.platform.bpm.enums.ActivityType;
import oecp.platform.bpm.enums.ExceptionMsgType;
import oecp.platform.bpm.enums.ProcessVariableName;
import oecp.platform.bpm.enums.TaskAuditDecision;
import oecp.platform.bpm.enums.VirProcessInstanceState;
import oecp.platform.bpm.eo.HistoryTaskEo;
import oecp.platform.bpm.eo.ProDefinition;
import oecp.platform.bpm.eo.TaskEo;
import oecp.platform.bpm.eo.VirProActivity;
import oecp.platform.bpm.eo.VirProDefinition;
import oecp.platform.bpm.eo.VirProcessInstance;
import oecp.platform.bpm.vo.HistoryTaskInfo;
import oecp.platform.bpm.vo.Node;
import oecp.platform.bpm.vo.ProcessInstanceInfo;
import oecp.platform.org.eo.Organization;
import oecp.platform.org.service.PersonService;
import oecp.platform.user.eo.User;
import oecp.platform.user.service.UserService;

import org.apache.commons.lang.StringUtils;
import org.jbpm.api.ProcessDefinition;
import org.jbpm.api.ProcessInstance;
import org.jbpm.api.task.Task;
import org.springframework.stereotype.Service;

/**
 * 
 * @author yongtree
 * @date 2011-7-4 上午09:57:09
 * @version 1.0
 * 
 */
@Service("proExecutionService")
public class ProExecutionServiceImpl extends PlatformBaseServiceImpl implements
		ProExecutionService {

	@Resource
	private ProDefinitionService proDefinitionService;

	@Resource
	private BcFunctionService bcFunctionService;

	@Resource
	private ProActivityService proActivityServcie;

	@Resource
	private JbpmService jbpmService;

	@Resource
	private PersonService personService;
	
	@Resource
	private UserService userService;
	
	private SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	
	/**
	 * 
	 * 启动虚拟流程
	 * @author yangtao
	 * @date 2011-8-18下午03:47:25
	 * @param functionCode 功能编号
	 * @param org 组织
	 * @param creator 启动人
	 * @param billKey 业务主键
	 * @param billInfo 业务信息如单据号
	 * @param variables 启动流程初始化的流程变量集合
	 * @return
	 */
	@Override
	public ProExecutionResult startVirProcess(String functionCode,
			Organization org, User creator, String billKey,String billInfo,
			Map<String, Object> variables) throws Exception {
		ProcessInstance pi = null;
		try {
			Function function = this.bcFunctionService
				.getFunctionByCode(functionCode);
			VirProDefinition virdef = this.proDefinitionService.getVirProDefinition(functionCode, org.getId());
			if (virdef == null){
				//改变单据状态
				String bizServiceForBpmName = function.getBizServiceForBpm();
				if(StringUtils.isEmpty(bizServiceForBpmName)){
					throw new BizException(ExceptionMsgType.FUNCTION_NO_REGISTER_SERVICE);
				}else{
					//从spring中取出服务方法
					BizServiceForBpm bizServiceForBpm = (BizServiceForBpm)SpringContextUtil.getBean(bizServiceForBpmName);
					bizServiceForBpm.changeBillState(billKey,function.getCode(), VirProcessInstanceState.END);
				}
			}
			
			//流程实例的变量0
			if (variables == null) {
				variables = new HashMap<String, Object>();
			}
			
			//启动虚拟流程定义
			pi = this.startVirProcess(creator, virdef.getId(), billKey,billInfo, variables,false);
		} catch (Exception e) {
			throw e;
		}
		return new ProExecutionResult(ProExecutionResult.ExecutionState.SUCCESS, pi);
	}
	
	
	/**
	 * 
	 * 启动虚拟流程
	 * @author yangtao
	 * @date 2011-8-2下午03:47:25
	 * @param virProDefinitionId
	 * @param billKey
	 * @param variables
	 * @return
	 */
	public ProcessInstance startVirProcess(User creator,String virProDefinitionId, String billKey,String billInfo,Map<String, Object> variables,boolean isAdminStart)throws Exception{
		ProcessInstance pi = null;
		try {
			//启动之前，初始化流程定义信息：给每个任务增加一个默认的指派人员接口
			VirProDefinition vpd = this.getDao().find(VirProDefinition.class, virProDefinitionId);
			//判断当前creator是否有权利启动此流程
			this.judgeRightToStart(vpd,creator);
			this.jbpmService.initializeProcessDefinition(vpd.getProDefinition().getDeployId());
			
			//封装虚拟流程实例
			VirProcessInstance vpi = new VirProcessInstance();
			vpi.setProcessInstanceId(null);
			vpi.setVirProDefinition(vpd);
			vpi.setCreateTime(format.format(new Date()));
			vpi.setCreateUserLoginId(creator.getLoginId());
			vpi.setVirProcessInstanceState(VirProcessInstanceState.RUNNING);
			vpi.setBillKey(billKey);
			vpi.setBillInfo(billInfo);
			this.getDao().create(vpi);
			
	
			//在启动流程的时候，把整个个流程用到的参数放进去
			variables.put(ProcessVariableName.CREATOR, creator);
			variables.put(ProcessVariableName.BUSINESS_KEY, billKey);
			variables.put(ProcessVariableName.VIR_PRO_DEFINITION_ID, virProDefinitionId);
			variables.put(ProcessVariableName.VIR_PRO_INSTANCE, vpi);
			
			//启动流程
			pi = this.jbpmService.startProcess(vpd.getProDefinition().getProDefId(), billKey, variables);
			vpi.setProcessInstanceId(pi.getId());
			this.getDao().update(vpi);
			
			//执行第一个默认的任务
			this.executeFirstTask(pi, creator.getLoginId());
		} catch (Exception e) {
			throw e;
		}
		return pi;
	}
	//判读当前creator是否有权利启动此流程
	private void judgeRightToStart(VirProDefinition vpd,User creator)throws Exception{
		List<Node> list = this.jbpmService.FindAllActivities(vpd.getProDefinition().getDeployId());
		boolean isYouStart = false;
		for(Node node : list){
			String preActivityName = this.proActivityServcie.getPreTaskByCurrentTask(node.getName(), vpd.getProDefinition().getDeployId());
			if(StringUtils.isEmpty(preActivityName)&&node.getType().equalsIgnoreCase((ActivityType.TASK.toString()))){//第一个任务节点
				List<QueryCondition> conditions = new ArrayList<QueryCondition>();
				QueryCondition qc = new QueryCondition("virProDefinition.id","=",vpd.getId());
				QueryCondition qc2 = new QueryCondition("activityName","=",node.getName());
				conditions.add(qc);
				conditions.add(qc2);
				List<VirProActivity> vpas = this.proActivityServcie.getVirProActivityByConditons(conditions);
				if(vpas.size()!=0){
					VirProActivity vpa = vpas.get(0);
					List<User> users = this.proActivityServcie.getVirProActivityAssignedUsers(vpa);
					for(oecp.platform.user.eo.User user : users){
						if(user.getLoginId().equals(creator.getLoginId())){
							isYouStart = true;
						}
					}
					break;
				}
			}
		}
		
		if(!isYouStart)
			throw new BizException(ExceptionMsgType.NO_RIGHT_START_PROCESS);
	}
	//启动流程之后，执行第一个默认的任务
	private void executeFirstTask(ProcessInstance pi,String userLoginId)throws Exception{
		try {
			Task task = this.proActivityServcie.getTasksByPiId(pi.getId()).get(0);
			this.proActivityServcie.completeTask(task.getId(), "", "制作单据并发起流程", TaskAuditDecision.AGREE.toString(), true, userLoginId,"","");
		} catch (Exception e) {
			throw e;
		}
	}

	/**
	 * 启动工作流，并返回流程实例
	 * 
	 * @param pdid
	 * @param billKey 业务单据KEY
	 * @param variables
	 * @return
	 */
	public ProcessInstance startProcess(String pdid, String billKey,Map variables){
		return this.jbpmService.startProcess(pdid, billKey, variables);
	}

	
	
	/**
	 * 
	 * 根据流程实例ID得到流程定义
	 * @author yangtao
	 * @date 2011-7-27上午10:30:10
	 * @param instanceId
	 * @return
	 */
	public ProcessDefinition getProcessDefinitionByInstanceId(String instanceId){
		return this.jbpmService.getProcessDefinitionByInstanceId(instanceId);
	}

	/**
	 * 
	 * 根据流程实例ID得到封装的流程定义
	 * @author yangtao
	 * @date 2011-7-27上午10:42:52
	 * @param instanceId
	 * @return
	 */
	public ProDefinition getProDefinitionByInstanceId(String instanceId){
		ProcessDefinition pd = this.getProcessDefinitionByInstanceId(instanceId);
		return this.proDefinitionService.getProDefinition(pd);
	}
	
	/**
	 * 结束流程实例
	 * 
	 * @param piId
	 */
	public void endProcessInstance(String piId,boolean isPersonDo)throws Exception{
		 if(isPersonDo)
			this.jbpmService.endProcessInstance(piId);
		 VirProcessInstance vpi = this.getDao().queryByWhere(VirProcessInstance.class, "processInstanceId=?", new Object[] { piId}).get(0);
		 List<TaskEo> list = this.getDao().queryByWhere(TaskEo.class, "virProcessInstance.id=?", new Object[] { vpi.getId()});
		 for(TaskEo taskEo : list){
			 if(!taskEo.getIsEnd()){
				 taskEo.setIsEnd(true);
				 taskEo.setEndTime(format.format(new Date()));
				 this.getDao().update(taskEo); 
			 }
		 }
		 
		 vpi.setVirProcessInstanceState(isPersonDo?(VirProcessInstanceState.PERSON_END):(VirProcessInstanceState.END));
		 this.getDao().update(vpi); 
		//改变单据状态
		List<Function> li = this.getDao().queryByWhere(Function.class, "o.code=? and o.wsuserd=?", new Object[]{vpi.getVirProDefinition().getProDefinition().getBelongFunction().getCode(),true});
		if(li.size()!=0){
			Function function = li.get(0);
			String bizServiceForBpmName = function.getBizServiceForBpm();
			//从spring中取出服务方法
			BizServiceForBpm bizServiceForBpm = (BizServiceForBpm)SpringContextUtil.getBean(bizServiceForBpmName);
			bizServiceForBpm.changeBillState(vpi.getBillKey(),function.getCode(), isPersonDo?(VirProcessInstanceState.PERSON_END):(VirProcessInstanceState.END));
		}else{
			throw new BizException(ExceptionMsgType.FUNCTION_NO_REGISTER_SERVICE);
		}
	}
	
	/**
	 * 删除流程实例
	 * 
	 * @param piId
	 */
	public void deleteProcessInstance(String piId){
		this.jbpmService.deleteProcessInstance(piId);
		VirProcessInstance vpi = this.getDao().queryByWhere(VirProcessInstance.class, "processInstanceId=?", new Object[] { piId}).get(0);
		vpi.setVirProcessInstanceState(VirProcessInstanceState.END);
		this.getDao().update(vpi); 
//		List<VirProcessInstance> vpis = this.getDao().queryByWhere(VirProcessInstance.class, "processInstanceId=?", new Object[] { piId});
//		for(VirProcessInstance vpi : vpis){
//			//先删除正在执行的流程实例
//			this.jbpmService.deleteProcessInstance(piId);
//			//删除该流程涉及的历史
//			this.getDao().deleteByWhere(HistoryTaskEo.class, "virProcessInstance.id=?", new Object[] { vpi.getId()});
//		}
//		//删除该流程涉及的实例
//		this.getDao().deleteByWhere(VirProcessInstance.class, "processInstanceId=?", new Object[]{piId});
	}
	
	
	/**
	 * 按发布id取得流程定义的PNG图片
	 * 
	 * @throws IOException
	 */
	public byte[] getDefinitionPngByDpId(String deployId) throws IOException{
		return this.jbpmService.getDefinitionPngByDpId(deployId);
	}
	
	/**
	 * 按发布id取得流程定义的XML
	 * 
	 * @throws IOException
	 */
	public String getDefinitionXmlByDpId(String deployId) throws IOException{
		return this.jbpmService.getDefinitionXmlByDpId(deployId);
	}
	
	/**
	 * 取得流程实例
	 * 
	 * @param piId
	 * @return
	 */
	public ProcessInstance getProcessInstanceByPid(String piId){
		return this.jbpmService.getProcessInstanceByPid(piId);
	}
	
	/**
	 * 
	 * 根据流程实例ID查询出历史
	 * @author yangtao
	 * @date 2011-7-28上午09:49:02
	 * @param pid
	 * @return
	 */
	public List <HistoryTaskInfo> queryHistoryByBillKey(String billKey){
		//调用服务
		VirProcessInstance vpi = null;
		try {
			vpi = this.proActivityServcie.getVirProcessInstanceByBizKey(billKey);
		} catch (Exception e) {
			e.printStackTrace();
		}
		String pid = vpi.getProcessInstanceId();
		//按照任务结束时间升序
		LinkedHashMap<String,String> map = new LinkedHashMap<String,String>();
		map.put("endTime", "ASC");
		map.put("id", "ASC");
		List<HistoryTaskEo> hlist = this.getDao().queryByWhere(HistoryTaskEo.class, "taskEo.virProcessInstance.processInstanceId=?",new Object[] { pid},map);
		List<HistoryTaskInfo> resultList = new ArrayList<HistoryTaskInfo>();

		//已经执行过任务的历史信息
		for(HistoryTaskEo ht : hlist){
			HistoryTaskInfo hi = new HistoryTaskInfo();
			hi.setId(ht.getId());
			hi.setAuditDecision(ht.getEnumDescription("taskAuditDecision"));
			hi.setAuditOpinion(ht.getAuditOpinion());
			hi.setAuditUserName(ht.getAuditUserName());
			if(ht.getAgentAuditUserName()==null)
				hi.setAgentUserName(ht.getAuditUserName());
			else
				hi.setAgentUserName(ht.getAgentAuditUserName());
			hi.setProcessInstanceId(ht.getTaskEo().getVirProcessInstance().getProcessInstanceId());
			hi.setActivityName(ht.getTaskEo().getTaskName());
			hi.setStartTime(ht.getStartTime());
			hi.setEndTime(ht.getEndTime());
			hi.setStatus(ht.getEnumDescription("taskHistoryState"));
			hi.setTaskId(ht.getTaskEo().getTaskId());
			hi.setCounterSignRuleName(ht.getTaskEo().getEnumDescription("counterSignRule"));
			resultList.add(hi);
		}
		if(this.jbpmService.getProcessInstanceByPid(pid)==null){//流程实例已经结束的情况
			HistoryTaskInfo hi = new HistoryTaskInfo();
			hi.setStatus("<span style=\"color:red;\">该流程已经执行完毕！</span>");
			resultList.add(hi);
		}else{//流程正在执行当中
			//当前正在等待执行的任务
			List<String> list = this.getCurrentActivityNameByPid(pid, vpi.getVirProDefinition().getProDefinition().getDeployId());
			for(String actName : list){
				HistoryTaskInfo hi = new HistoryTaskInfo();
				hi.setProcessInstanceId(pid);
				hi.setActivityName(actName);
//				hi.setStartTime(hai.getStartTime().toString());
				hi.setStatus("<span style=\"color:green;\">等待执行</span>");
				resultList.add(hi);
			}
		}
		return resultList;
	}
	
	/**
	 * 
	 * 查询出所有的流程实例 (虚拟流程实例)
	 * @author yangtao
	 * @date 2011-7-28上午10:05:10
	 * @return
	 */
	public QueryResult<ProcessInstanceInfo> queryProcessInstance(List<QueryCondition> conditions,int start,int limit){
		//按照任务结束时间降序
		LinkedHashMap<String,String> map = new LinkedHashMap<String,String>();
		map.put("createTime", "DESC");
		map.put("id", "ASC");
		QueryResult<ProcessInstanceInfo> qr = new QueryResult<ProcessInstanceInfo>();
		QueryResult<VirProcessInstance> qr2 = this.getDao().getScrollData(VirProcessInstance.class, start, limit, conditions, map);
		List<ProcessInstanceInfo> resultList = new ArrayList<ProcessInstanceInfo>();
		for(VirProcessInstance vpi : qr2.getResultlist()){
			ProcessInstanceInfo pii = new ProcessInstanceInfo();
			pii.setVirProcessInstanceId(vpi.getId());
			pii.setProcessInstanceId(vpi.getProcessInstanceId());
			pii.setDeployId(vpi.getVirProDefinition().getProDefinition().getDeployId());
			pii.setProcessDefinitionId(vpi.getVirProDefinition().getProDefinition().getProDefId());
			pii.setProcessName(vpi.getVirProDefinition().getName());
			pii.setProcessVersion(vpi.getVirProDefinition().getProDefinition().getVersion());
			pii.setCreateUserLoginId(vpi.getCreateUserLoginId());
			pii.setCreateTime(vpi.getCreateTime());
			pii.setStatus(vpi.getEnumDescription("virProcessInstanceState"));
			pii.setBillKey(vpi.getBillKey());
			pii.setBillInfo(vpi.getBillInfo());
			resultList.add(pii);
		}
		
		qr.setResultlist(resultList);
		qr.setTotalrecord(qr2.getTotalrecord());
		return qr;
	}
	
	/**
	 * 
	 * 根据实例ID得到当前正在等待的节点
	 * @author yangtao
	 * @date 2011-7-26上午08:31:31
	 * @return String
	 */
	public List<String> getCurrentActivityNameByPid(String processInstanceId,String deployId){
		return this.jbpmService.getCurrentActivityNameByPid(processInstanceId,deployId);
	}
	
	
	/**
	 * 
	 * 根据查询条件获得VirProcessInstance
	 * @author yangtao
	 * @date 2011-8-16上午11:59:14
	 * @param conditions
	 * @return
	 */
	public List<VirProcessInstance> getVirProcessInstanceByConditions(List<QueryCondition> conditions){
		String sqlFiled = "1=1";
		Object[] sqlParams = new Object[conditions.size()];
		int i = 0;
		for(QueryCondition qc : conditions){
			sqlFiled+=" and "+qc.getField()+qc.getOperator()+"? ";
			sqlParams[i] = qc.getValue();
			i++;
		}
		return this.getDao().queryByWhere(VirProcessInstance.class, sqlFiled, sqlParams);
	}


	public ProDefinitionService getProDefinitionService() {
		return proDefinitionService;
	}


	public void setProDefinitionService(ProDefinitionService proDefinitionService) {
		this.proDefinitionService = proDefinitionService;
	}


	public BcFunctionService getBcFunctionService() {
		return bcFunctionService;
	}


	public void setBcFunctionService(BcFunctionService bcFunctionService) {
		this.bcFunctionService = bcFunctionService;
	}


	public ProActivityService getProActivityServcie() {
		return proActivityServcie;
	}


	public void setProActivityServcie(ProActivityService proActivityServcie) {
		this.proActivityServcie = proActivityServcie;
	}


	public JbpmService getJbpmService() {
		return jbpmService;
	}


	public void setJbpmService(JbpmService jbpmService) {
		this.jbpmService = jbpmService;
	}


	public PersonService getPersonService() {
		return personService;
	}


	public void setPersonService(PersonService personService) {
		this.personService = personService;
	}


	public UserService getUserService() {
		return userService;
	}


	public void setUserService(UserService userService) {
		this.userService = userService;
	}
	
	
}
