/*
 * Copyright (c) 2011 OECP All Rights Reserved.                	
 * <a href="http://www.oecp.cn">http://www.oecp.cn</a>                                                                 
 */    

package oecp.platform.bpm.service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.annotation.Resource;

import oecp.framework.dao.QueryCondition;
import oecp.framework.dao.QueryObject;
import oecp.framework.dao.QueryResult;
import oecp.framework.exception.BizException;
import oecp.platform.base.service.PlatformBaseServiceImpl;
import oecp.platform.bcfunction.eo.FunctionUI;
import oecp.platform.bpm.enums.CounterSignRule;
import oecp.platform.bpm.enums.ExceptionMsgType;
import oecp.platform.bpm.enums.TaskActivityAuditType;
import oecp.platform.bpm.enums.TaskAuditDecision;
import oecp.platform.bpm.enums.TaskHistoryState;
import oecp.platform.bpm.enums.VirProcessInstanceState;
import oecp.platform.bpm.eo.CandidateUser;
import oecp.platform.bpm.eo.HistoryTaskEo;
import oecp.platform.bpm.eo.ProActivity;
import oecp.platform.bpm.eo.ProDefinition;
import oecp.platform.bpm.eo.TaskEo;
import oecp.platform.bpm.eo.VirProActivity;
import oecp.platform.bpm.eo.VirProcessInstance;
import oecp.platform.bpm.vo.HistoryTaskInfo;
import oecp.platform.bpm.vo.TaskInfo;
import oecp.platform.org.eo.Person;
import oecp.platform.org.eo.Post;
import oecp.platform.org.service.PersonService;
import oecp.platform.role.eo.OrgRole;
import oecp.platform.user.eo.User;
import oecp.platform.user.service.UserService;

import org.apache.commons.lang.StringUtils;
import org.jbpm.api.ProcessDefinition;
import org.jbpm.api.task.Task;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


/**
 * 活动节点服务实现类
 * @author yongtree
 * @date 2011-6-28 下午09:27:03
 * @version 1.0
 */
@Service("proActivityService")
@Transactional
public class ProActivityServiceImpl extends PlatformBaseServiceImpl<ProActivity> implements
		ProActivityService {
	
	@Resource
	private PersonService personService;

	@Resource
	private UserService userService;
	
	@Resource
	private JbpmService jbpmService;
	
	
	@Resource
	private ProDefinitionService proDefinitionService;

	@Autowired
	private ProExecutionService proExecutionService;
	
	private SimpleDateFormat dateformat=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	
	
	@Override
	public ProActivity getActivityByDefAndActivityName(String proDefId,
			String activityName) {
		return this.getDao().findByWhere(ProActivity.class, "proDef.proDefId=? and activityName=?", new Object[]{proDefId,activityName});
	}
	

	/**
	 * 
	 * 根据任务ID得到封装的流程定义
	 * @author yangtao
	 * @date 2011-7-27上午11:18:28
	 * @param taskId
	 * @return
	 */
	public ProDefinition getProDefinitionByTaskId(String taskId){
		ProcessDefinition pd = this.jbpmService.getProDefinitionByTaskId(taskId);
		return this.proDefinitionService.getProDefinition(pd);
	}

	/**
	 * 
	 * 查出某个人的所有任务
	 * @author yangtao
	 * @date 2011-7-28上午09:23:11
	 * @param userName
	 * @return
	 */
	public List<Task> findPersonalTasks(String userName){
		return this.jbpmService.findPersonalTasks(userName);
	}
	
	/**
	 * 
	 * 获取某个人的候选任务
	 * @author yangtao
	 * @date 2011-8-3上午09:15:31
	 * @param userName
	 * @return
	 */
	public List<Task> findGroupTasks(String userName){
		return this.jbpmService.findGroupTasks(userName);
	}
	/**
	 * 
	 * 获取候选人的任务 带分页
	 * @author yangtao
	 * @date 2011-8-16下午04:10:54
	 * @param userName
	 * @return
	 */
	public List<Task> findCandiateTasks(String userName,int start,int limit){
		return this.jbpmService.findCandiateTasks(userName, start, limit);
	}
	
	/**
	 * 根据当前任务返回流程图中这个任务上面的所有任务节点(和历史任务的交集)
	 * 
	 * @author YangTao
	 * @date 2012-1-29下午03:00:03
	 * @param billKey
	 * @return
	 */
	public QueryResult<TaskInfo> getPreTaskByCurrentTask(String billKey){
		QueryResult<TaskInfo> qr = new QueryResult<TaskInfo>();
		try {
			VirProcessInstance vpi = this.getVirProcessInstanceByBizKey(billKey);
			String deployId = vpi.getVirProDefinition().getProDefinition().getDeployId();
			List<String> taskNames = this.jbpmService.getCurrentActivityNameByPid(vpi.getProcessInstanceId(),deployId );
			if(taskNames.size()!=0){
				String currentTaskName = taskNames.get(0);
				List<String> resultList = new LinkedList();
				resultList = this.jbpmService.getPreTaskByCurrentTask(currentTaskName,deployId,vpi.getProcessInstanceId(),resultList);
				//去重
				resultList = this.removeDuplicateWithOrder(resultList);
				List<TaskInfo> tis = new ArrayList();
				for(String taskName : resultList){
					TaskInfo tii = new TaskInfo();
					tii.setTaskName(taskName);
					tis.add(tii);
				}
				qr.setResultlist(tis);
				qr.setTotalrecord(new Long(resultList.size()));
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return qr;
	}
	
	/**
	 * 根据当前任务返回和当前结点连接的下一个任务（多个）
	 * @author yangtao
	 * @date 2013-5-9下午4:58:10
	 * @return
	 */
	public QueryResult<TaskInfo> getNextTasksByCurrentTask(String billKey){
		QueryResult<TaskInfo> qr = new QueryResult<TaskInfo>();
		try {
			VirProcessInstance vpi = this.getVirProcessInstanceByBizKey(billKey);
			String deployId = vpi.getVirProDefinition().getProDefinition().getDeployId();
			List<String> taskNames = this.jbpmService.getCurrentActivityNameByPid(vpi.getProcessInstanceId(),deployId );
			if(taskNames.size()!=0){
				String currentTaskName = taskNames.get(0);
				List<String[]> resultList = new LinkedList();
				resultList = this.jbpmService.getNextTasksByCurrentTask(currentTaskName,deployId);
				List<TaskInfo> tis = new ArrayList();
				for(String[] task : resultList){
					TaskInfo tii = new TaskInfo();
					tii.setTaskName(task[0]);
					tii.setIncomeTransitionName(task[1]);
					tis.add(tii);
				}
				qr.setResultlist(tis);
				qr.setTotalrecord(new Long(resultList.size()));
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return qr;
	}
	
	//list去重，保持顺序
	private List<String> removeDuplicateWithOrder(List<String> list) {
	    Set<String> set = new HashSet<String>();
	    List<String> newList = new ArrayList<String>();
	    for (Iterator<String> iter = list.iterator(); iter.hasNext();) {
	          String element = iter.next();
	          if (set.add(element))
	             newList.add(element);
	    } 
	    list.clear();
	    list.addAll(newList);
	    return list;
	}


	
	
	/**
	 * 
	 * 根据任务ID执行任务：
	 * 有会签的，先判断当前人是不是这个任务的候选人，是就执行，并且去看这个人是不是候选人的代理人，是就执行
	 * 无会签的，先判断进来的这个人是不是这个任务的候选人，是就执行；不是就去看这个人是不是候选人的代理人，是就执行
	 * @author yangtao
	 * @date 2011-7-28上午09:26:51
	 * @return
	 */
	public void completeTask(String taskId,String nextperson,String auditOpinion,String auditDecision,boolean isTake,String userName,String preTaskName,String transitionName)throws Exception{
		List<TaskEo> taskEos = this.getDao().queryByWhere(TaskEo.class, "o.taskId=?", new Object[]{taskId});
		Task task = this.jbpmService.getTaskByTaskId(taskId);
			
		String agentUserName = null;
		if(taskEos.size()!=0){
			TaskEo taskEo = taskEos.get(0);
			VirProcessInstance vpi = taskEo.getVirProcessInstance();
			//防止服务器重启的情况
			this.jbpmService.initializeProcessDefinition(vpi.getVirProDefinition().getProDefinition().getDeployId());
			//说明：任务的候选人和候选人的代理人都可以来办理
			CandidateUser candidateUser = null;
			List<CandidateUser> cdus = taskEo.getCandidateUsers();
			//有会签的情况
			if(taskEo.getCounterSignRule()!=CounterSignRule.NO_COUNTERSIGN_RULE){
				//标志当前这个任务是否执行完毕
				boolean executeFlag = false;
				//一、先执行和保存本应该是当前人执行的任务
				for(int i=0;i<cdus.size();i++){
					CandidateUser cdu = cdus.get(i);
					if(userName.equals(cdu.getUser().getLoginId())){
						if(executeFlag){
							break;
						}else{
							agentUserName = userName;
							if(candidateUser==null)
								candidateUser = cdu;
							cdu.setIsExecutedTask(true);
							this.getDao().update(cdu);
							List li = this.getDao().queryByWhere(HistoryTaskEo.class, "taskEo.taskId=? and auditUserName=? and agentAuditUserName=?", new Object[]{taskId,userName,userName});
							if(cdu.getAgent()!=null){
								List li2 = this.getDao().queryByWhere(HistoryTaskEo.class, "taskEo.taskId=? and auditUserName=? and agentAuditUserName=?", new Object[]{taskId,userName,cdu.getAgent().getLoginId()});
								if(li.size()==0&&li2.size()==0)
									executeFlag = this.completeTaskAndSaveHistory(taskEo, task, vpi, nextperson, auditOpinion, auditDecision, isTake, userName, agentUserName, preTaskName,transitionName);
							}else{
								if(li.size()==0)
									executeFlag = this.completeTaskAndSaveHistory(taskEo, task, vpi, nextperson, auditOpinion, auditDecision, isTake, userName, agentUserName, preTaskName,transitionName);
							}
						}
					} 
				}
				//二、再执行和保存当前执行人所代理的任务
				if(!executeFlag){
					for(int i=0;i<cdus.size();i++){
						CandidateUser cdu = cdus.get(i);
						if(cdu.getAgent()!=null&&userName.equals(cdu.getAgent().getLoginId())){
							if(executeFlag){
								break;
							}else{
								agentUserName = userName;
								String userName2 = cdu.getUser().getLoginId();
								if(candidateUser==null)
									candidateUser = cdu;
								cdu.setIsExecutedTask(true);
								this.getDao().update(cdu);
								List li = this.getDao().queryByWhere(HistoryTaskEo.class, "taskEo.taskId=? and auditUserName=? and agentAuditUserName=?", new Object[]{taskId,userName2,agentUserName});
								List li2 = this.getDao().queryByWhere(HistoryTaskEo.class, "taskEo.taskId=? and auditUserName=? and agentAuditUserName=?", new Object[]{taskId,userName2,userName2});
								if(li.size()==0&&li2.size()==0)
									executeFlag = this.completeTaskAndSaveHistory(taskEo, task, vpi, nextperson, auditOpinion, auditDecision, isTake, userName2, agentUserName, preTaskName,transitionName);
							}
						}
					}
				}
				
				if(candidateUser==null)
					throw new BizException(ExceptionMsgType.YOU_ISNOT_CANDIDATE);
			}else{//没有会签的情况
				//一、先找当前进来用户是否是这个任务的候选人
				for(int i=0;i<cdus.size();i++){
					CandidateUser cdu = cdus.get(i);
					if(userName.equals(cdu.getUser().getLoginId())){
						agentUserName = userName;
						cdu.setIsExecutedTask(true);
						this.getDao().update(cdu);
						candidateUser = cdu;
						break;
					}
				}
				//二、如果当前人不是任务的候选人，再去找当前人是不是某个候选人的代理人
				if(candidateUser==null){
					for(int i=0;i<cdus.size();i++){
						CandidateUser cdu = cdus.get(i);
						if(cdu.getAgent()!=null&&userName.equals(cdu.getAgent().getLoginId())){
							agentUserName = userName;
							userName = cdu.getUser().getLoginId();
							cdu.setIsExecutedTask(true);
							this.getDao().update(cdu);
							candidateUser = cdu;
							break;
						}
					}
				}
				if(candidateUser==null)
					throw new BizException(ExceptionMsgType.YOU_ISNOT_CANDIDATE);
				this.completeTaskAndSaveHistory(taskEo, task, vpi, nextperson, auditOpinion, auditDecision, isTake, userName, agentUserName, preTaskName,transitionName);
			}
		}else{
			throw new BizException(ExceptionMsgType.NO_CURRENT_TASK);
		}
	}
	
	/**
	 * 执行任务的逻辑
	 * 
	 * @author YangTao
	 * @date 2012-2-15上午10:27:54
	 * @param taskEo
	 * @param task
	 * @param vpi
	 * @param nextperson
	 * @param auditOpinion
	 * @param auditDecision
	 * @param isTake
	 * @param userName
	 * @param agentUserName
	 * @param preTaskName
	 * @throws Exception
	 */
	private boolean completeTaskAndSaveHistory(TaskEo taskEo,Task task ,VirProcessInstance vpi,String nextperson,String auditOpinion,String auditDecision,
			boolean isTake,String userName,String agentUserName,String preTaskName,String transitionName)throws Exception{
		boolean result = false;
		String taskId = taskEo.getTaskId();
		if(CounterSignRule.NO_COUNTERSIGN_RULE.toString().equals(taskEo.getCounterSignRule().toString())){//没有会签
			//保存历史信息
			this.saveActivityHistory(taskEo, task, auditOpinion, auditDecision, userName,agentUserName,TaskHistoryState.IS_DONE);
			taskEo.setIsEnd(true);
			taskEo.setEndTime(dateformat.format(new Date()));
			if(TaskAuditDecision.AGREE.toString().equals(auditDecision)){//同意
				taskEo.setAgreenCount(1);
				//先接受任务再执行
				this.jbpmService.completeTask(taskId,transitionName,nextperson,isTake,userName);
			}else if(TaskAuditDecision.NO_AGREE.toString().equals(auditDecision)){//不同意
				taskEo.setNoAgreenCount(1);
				//结束流程实例
				this.proExecutionService.endProcessInstance(vpi.getProcessInstanceId(),true);
			}else if(TaskAuditDecision.BACK.toString().equals(auditDecision)){//驳回
				taskEo.setNoAgreenCount(1);
				//驳回
				this.jbpmService.completeTaskByCreateTransiton(task, preTaskName, System.currentTimeMillis()+"");
			}
			result = true;
			this.getDao().update(taskEo);
		}else if(CounterSignRule.ONE_TICKET_PASS.toString().equals(taskEo.getCounterSignRule().toString())){//一票通过
			//把当前执行人从该任务候选人中去掉
			this.jbpmService.removeTaskCandidate(task, userName);
			if(TaskAuditDecision.AGREE.toString().equals(auditDecision)){//同意
				//保存历史信息
				this.saveActivityHistory(taskEo, task, auditOpinion, auditDecision, userName,agentUserName,TaskHistoryState.IS_DONE);
				taskEo.setIsEnd(true);
				taskEo.setEndTime(dateformat.format(new Date()));
				taskEo.setAgreenCount(taskEo.getAgreenCount()+1);
				//先接受任务再执行
				this.jbpmService.completeTask(taskId,transitionName,nextperson,isTake,userName);
				result = true;
			}else if(TaskAuditDecision.NO_AGREE.toString().equals(auditDecision)){//不同意
				taskEo.setNoAgreenCount(taskEo.getNoAgreenCount()+1);
				//所有人都不同意
				if(taskEo.getNoAgreenCount()==taskEo.getCandidateUserCount()){
					//保存历史信息
					this.saveActivityHistory(taskEo, task, auditOpinion, auditDecision, userName,agentUserName,TaskHistoryState.IS_DONE);
					taskEo.setIsEnd(true);
					taskEo.setEndTime(dateformat.format(new Date()));
					//结束流程实例
					this.proExecutionService.endProcessInstance(vpi.getProcessInstanceId(),true);
					result = true;
				}else{
					//保存历史信息
					this.saveActivityHistory(taskEo, task, auditOpinion, auditDecision, userName,agentUserName,TaskHistoryState.IS_DOING);
				}
			}
			this.getDao().update(taskEo);
		}else if(CounterSignRule.ONE_TICKET_NO_PASS.toString().equals(taskEo.getCounterSignRule().toString())){//一票否决
			if(TaskAuditDecision.AGREE.toString().equals(auditDecision)){//同意
				//把当前执行人从该任务候选人中去掉
				this.jbpmService.removeTaskCandidate(task, userName);
				taskEo.setAgreenCount(taskEo.getAgreenCount()+1);
				//所有人都同意
				if(taskEo.getAgreenCount()==taskEo.getCandidateUserCount()){
					//保存历史信息
					this.saveActivityHistory(taskEo, task, auditOpinion, auditDecision, userName,agentUserName,TaskHistoryState.IS_DONE);
					taskEo.setIsEnd(true);
					taskEo.setEndTime(dateformat.format(new Date()));
					//先接受任务再执行
					this.jbpmService.completeTask(taskId,transitionName,nextperson,isTake,userName);
					result = true;
				}else{
					//保存历史信息
					this.saveActivityHistory(taskEo, task, auditOpinion, auditDecision, userName,agentUserName,TaskHistoryState.IS_DOING);
				}
			}else if(TaskAuditDecision.NO_AGREE.toString().equals(auditDecision)){//不同意
				//保存历史信息
				this.saveActivityHistory(taskEo, task, auditOpinion, auditDecision, userName,agentUserName,TaskHistoryState.IS_DONE);
				taskEo.setIsEnd(true);
				taskEo.setEndTime(dateformat.format(new Date()));
				taskEo.setNoAgreenCount(1);
				//结束流程实例
				this.proExecutionService.endProcessInstance(vpi.getProcessInstanceId(),true);
				result = true;
			}
			this.getDao().update(taskEo);
		}else if(CounterSignRule.PROPORTION.toString().equals(taskEo.getCounterSignRule().toString())){//比例通过
			double passRate = new BigDecimal(taskEo.getPassRate()).divide(new BigDecimal(100),2, RoundingMode.HALF_UP).doubleValue();
			//把当前执行人从该任务候选人中去掉
			this.jbpmService.removeTaskCandidate(task, userName);
			if(TaskAuditDecision.AGREE.toString().equals(auditDecision)){//同意
				//保存审批过的人和同意数量
				taskEo.setAgreenCount(taskEo.getAgreenCount()+1);
				double nowPassRate = new BigDecimal(taskEo.getAgreenCount()).divide(new BigDecimal(taskEo.getCandidateUserCount()),2, RoundingMode.HALF_UP).doubleValue();
				//当前通过率大于设定的值就执行该任务
				if(nowPassRate>=passRate){
					//保存历史信息
					this.saveActivityHistory(taskEo, task, auditOpinion, auditDecision, userName,agentUserName,TaskHistoryState.IS_DONE);
					taskEo.setIsEnd(true);
					taskEo.setEndTime(dateformat.format(new Date()));
					//先接受任务再执行
					this.jbpmService.completeTask(taskId,transitionName,nextperson,isTake,userName);
					result = true;
				}else{
					//保存历史信息
					this.saveActivityHistory(taskEo, task, auditOpinion, auditDecision, userName,agentUserName,TaskHistoryState.IS_DOING);
				}
			}else if(TaskAuditDecision.NO_AGREE.toString().equals(auditDecision)){//不同意
				//保存审批过的人和不同意数量
				taskEo.setNoAgreenCount(taskEo.getNoAgreenCount()+1);
				double nowNoPassRate = new BigDecimal(taskEo.getNoAgreenCount()).divide(new BigDecimal(taskEo.getCandidateUserCount()),2, RoundingMode.HALF_UP).doubleValue();
				//当前通过率大于设定的值就执行该任务
				if(nowNoPassRate>=(1-passRate)){
					//保存历史信息
					this.saveActivityHistory(taskEo, task, auditOpinion, auditDecision, userName,agentUserName,TaskHistoryState.IS_DONE);
					taskEo.setIsEnd(true);
					taskEo.setEndTime(dateformat.format(new Date()));
					//结束流程实例
					this.proExecutionService.endProcessInstance(vpi.getProcessInstanceId(),true);
					result = true;
				}else{
					//保存历史信息
					this.saveActivityHistory(taskEo, task, auditOpinion, auditDecision, userName,agentUserName,TaskHistoryState.IS_DOING);
				}
			}
			this.getDao().update(taskEo);
		}
		return result;
	}
	
	/**
	 * 
	 * 业务单据处审批执行任务
	 * @author yangtao
	 * @date 2011-9-19上午10:50:36
	 * @param funcKey
	 * @param bizKey
	 * @param billInfo
	 * @param userKey
	 * @param auditOpinion
	 * @param auditDecision
	 * @return
	 */
	public void completeTask(String funcKey, String bizKey,String billInfo,
			String userKey, String auditOpinion,String auditDecision,String preTaskName,String transitionName)throws Exception{
		try {
			TaskEo taskeo = this.getCrrentTaskByBizKey(bizKey);
			this.completeTask(taskeo.getTaskId(), "", auditOpinion, auditDecision, true, userKey,preTaskName,transitionName);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			throw e;
		}
	}
	
	/**
	 * 根据主键获得当前等待执行的任务
	 * 
	 * @author YangTao
	 * @date 2012-2-9上午10:16:40
	 * @param bizKey
	 * @return
	 * @throws Exception
	 */
	public TaskEo getCrrentTaskByBizKey(String bizKey)throws Exception{
		TaskEo taskEo = null;
		VirProcessInstance virProcessInstance = null;
		try {
			//根据业务主键查询出所对应的虚拟流程实例
			virProcessInstance = this.getVirProcessInstanceByBizKey(bizKey);
			//流程实例已经结束时，抛异常
			if(virProcessInstance.getVirProcessInstanceState()!=VirProcessInstanceState.RUNNING){
				throw new BizException(ExceptionMsgType.PROCESS_INTANCE_IS_END);
			}
			//根据流程实例取出当前任务
			List<Task> taskList = this.getTasksByPiId(virProcessInstance.getProcessInstanceId());
			Task task = null;
			if(taskList.size()==0){
				throw new Exception(ExceptionMsgType.NO_CURRENT_TASK);
			}else{
				task = taskList.get(0);
			}
			//简单封装
			taskEo = this.getDao().queryByWhere(TaskEo.class, "o.taskId=?", new Object[]{task.getId()}).get(0);
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}
		return taskEo;
	}
	
	/**
	 * 根据主键得到当前的流程实例
	 * 
	 * @author YangTao
	 * @date 2012-2-9上午10:16:58
	 * @param bizKey
	 * @return
	 * @throws Exception
	 */
	public VirProcessInstance getVirProcessInstanceByBizKey(String bizKey)throws BizException{
		VirProcessInstance virProcessInstance = null;
		//根据业务主键查询出所对应的虚拟流程实例
		List<QueryCondition> conditions = new ArrayList<QueryCondition> ();
		QueryCondition qc = new QueryCondition("o.billKey","=",bizKey);
		conditions.add(qc);
		List<VirProcessInstance> list = this.proExecutionService.getVirProcessInstanceByConditions(conditions);
		if (list.size() != 0){
			virProcessInstance = list.get(0);
		}else{
			throw new BizException(ExceptionMsgType.NO_PROCESS_INSTANCE);
		}
		return virProcessInstance;
	} 
	/**
	 * 
	 * 把某个任务分配给某个人
	 * @author yangtao
	 * @date 2011-7-28上午09:44:10
	 * @param taskId
	 * @param userName
	 */
	public void assignTask(String taskId,String userName){
		this.jbpmService.assignTask(taskId, userName);
	}
	
	/**
	 * 
	 * 给某个任务增加候选人
	 * @author yangtao
	 * @date 2011-8-16下午03:17:22
	 * @param taskId
	 * @param userName
	 */
	public void addCandidateUser(String taskId,String userName){
		this.jbpmService.addCandidateUser(taskId, userName);
	}
	
	/**
	 * 
	 * 待办任务中给某个任务委派人员
	 * @author yangtao
	 * @date 2011-8-17上午09:48:08
	 * @param taskId
	 * @param assignFlag
	 * @param ids
	 */
	public void reassignVirProActivity(String taskId,String assignFlag,String ids,String userName){
		List<TaskEo> taskEos = this.getDao().queryByWhere(TaskEo.class, "o.taskId=?", new Object[]{taskId});
		if(taskEos.size()!=0){
			TaskEo taskEo = taskEos.get(0);
			List<CandidateUser> list = new ArrayList<CandidateUser>();
			String userLoginIds = "";
			//根据角色、岗位、人员拼出用户登录账号
			String[] idss = ids.split(",");
			if(TaskActivityAuditType.USER.toString().equals(assignFlag)){
				for(String id : idss){
					if(!StringUtils.isEmpty(id)){
						User user = this.getDao().find(User.class, id);
						CandidateUser cd = new CandidateUser(user,null);
						list.add(cd);
						userLoginIds+=user.getLoginId();
						userLoginIds+=",";
					}
				}
			}else if(TaskActivityAuditType.POST.toString().equals(assignFlag)){
				for(String id : idss){
					List<Person> ps = personService.getPersonsByPost(id);
					// 根据岗位得到相应的用户
					List<oecp.platform.user.eo.User> pus = new ArrayList<oecp.platform.user.eo.User>();
					for (Person person : ps) {
						oecp.platform.user.eo.User u = userService.getUserByPersonId(person.getId());
						if (u != null) {
							pus.add(u);
						}
					}
					for(oecp.platform.user.eo.User user : pus){
						CandidateUser cd = new CandidateUser(user,null);
						list.add(cd);
						userLoginIds+=user.getLoginId();
						userLoginIds+=",";
					}
				}
			}else if(TaskActivityAuditType.ROLE.toString().equals(assignFlag)){
				for(String id : idss){
					if(!StringUtils.isEmpty(id)){
						List<oecp.platform.user.eo.User> pus = userService.getUsersByRoleId(id);
						for(oecp.platform.user.eo.User user : pus){
							CandidateUser cd = new CandidateUser(user,null);
							list.add(cd);
							userLoginIds+=user.getLoginId();
							userLoginIds+=",";
						}
					}
				}
			}
			if(userLoginIds.endsWith(","))
				userLoginIds = userLoginIds.substring(0, userLoginIds.length()-1);
			String auditOpinion = "用户"+userName+"将任务委派给:"+userLoginIds;
			//保存历史
			this.saveActivityHistory(taskEo, this.getTaskByTaskId(taskId), auditOpinion, TaskAuditDecision.REASSIGN.toString(), userName,null,TaskHistoryState.IS_DOING);
			//去重
			for(int i=0;i<list.size();i++){
				CandidateUser cdu = list.get(i);
				boolean flag = false;
				for(CandidateUser cd : taskEo.getCandidateUsers()){
					if(cdu.getUser().getLoginId().equals(cd.getUser().getLoginId())){
						flag = true;
					}
				}
				if(flag)
					list.remove(i);
				else
					this.getDao().create(cdu);
			}
			//重新委派的人员和以前配置的委派人员合并在一起
			if(taskEo.getCandidateUsers()!=null)
				taskEo.getCandidateUsers().addAll(list);
			taskEo.setCandidateUserCount(taskEo.getCandidateUsers().size());
			this.getDao().update(taskEo);
				
			//给这个任务增加候选人
			this.addCandidateUser(taskId, userLoginIds);
		}
	}
	/**
	 * 
	 * 根据条件查询出VirProActivity
	 * @author yangtao
	 * @date 2011-8-16上午11:52:21
	 * @param conditions
	 * @return
	 */
	public List<VirProActivity> getVirProActivityByConditons(List<QueryCondition> conditions){
		String sqlFiled = "1=1";
		Object[] sqlParams = new Object[conditions.size()];
		int i = 0;
		for(QueryCondition qc : conditions){
			sqlFiled+=" and "+qc.getField()+qc.getOperator()+"? ";
			sqlParams[i] = qc.getValue();
			i++;
		}
		return this.getDao().queryByWhere(VirProActivity.class, sqlFiled, sqlParams);
	} 

	
	/**
	 * 
	 * 根据taskId获得Task
	 * @author yangtao
	 * @date 2011-7-29上午11:16:42
	 * @param taskId
	 * @return
	 */
	public Task getTaskByTaskId(String taskId){
		return this.jbpmService.getTaskByTaskId(taskId);
	}
	
	/**
	 * 根据业务主键，获取功能流程查看页面路径
	 * 
	 * @author YangTao
	 * @date 2012-2-1上午10:03:25
	 * @param billKey
	 * @return
	 */
	public String getFormResourceName(String billKey){
		String result = "";
		//调用服务
		VirProcessInstance vpi = null;
		try {
			vpi = this.getVirProcessInstanceByBizKey(billKey);
		} catch (Exception e) {
			e.printStackTrace();
		}
		List<FunctionUI> li = this.getDao().queryByWhere(FunctionUI.class, "o.function.code=? and o.isDefaultForProcess=?", new Object[]{vpi.getVirProDefinition().getProDefinition().getBelongFunction().getCode(),true});
		if(li.size()!=0)
			result = li.get(0).getSign();
		return result;
	}
	/**
	 * 
	 * 得到某个人的已办任务
	 * @author yangtao
	 * @date 2011-8-8上午09:40:27
	 * @param userName
	 * @param start
	 * @param limit
	 * @return
	 */
	public QueryResult<HistoryTaskInfo> getPersonDoneTasks(List<QueryCondition> conditions,int start,int limit){
		//按照任务结束时间降序
		LinkedHashMap<String,String> map = new LinkedHashMap<String,String>();
		map.put("endTime", "DESC");
		map.put("id", "ASC");
		QueryObject qo = new QueryObject();
		qo.setQueryConditions(conditions);
		String sql = qo.getWhereQL();
		Object[] ob = qo.getQueryParams();
		if(sql.indexOf("o.auditUserName")!=-1){
			QueryCondition qc = qo.findQueryCondition("o.auditUserName", "=");
			sql = sql.replaceAll("o.auditUserName = \\?", "(o.auditUserName = ? or o.agentAuditUserName = ?)");
			List list = new ArrayList();
			list.add(0, qc.getValue());
			for(Object object : ob){
				list.add(object);
			}
			ob = list.toArray();
		}
		QueryResult<HistoryTaskEo> qr = this.getDao().getScrollData(HistoryTaskEo.class, start, limit, sql, ob, map);
//		QueryResult<HistoryTaskEo> qr = this.getDao().getScrollData(HistoryTaskEo.class, start, limit, conditions, map);
		QueryResult<HistoryTaskInfo> qr2 = new QueryResult<HistoryTaskInfo>();
		List<HistoryTaskInfo> resultlist = new ArrayList<HistoryTaskInfo>();
		for(HistoryTaskEo ht : qr.getResultlist()){
			HistoryTaskInfo hi = new HistoryTaskInfo();
			hi.setProcessName(ht.getTaskEo().getVirProcessInstance().getVirProDefinition().getName());
			hi.setActivityName(ht.getTaskEo().getTaskName());
			hi.setStatus(ht.getEnumDescription("taskHistoryState"));
			hi.setStartTime(ht.getStartTime());
			hi.setEndTime(ht.getEndTime());
			hi.setAuditDecision(ht.getEnumDescription("taskAuditDecision"));
			hi.setAuditOpinion(ht.getAuditOpinion());
			hi.setAuditUserName(ht.getAuditUserName());
			hi.setProcessInstanceId(ht.getTaskEo().getVirProcessInstance().getProcessInstanceId());
			hi.setDeployId(ht.getTaskEo().getVirProcessInstance().getVirProDefinition().getProDefinition().getDeployId());
			hi.setBillKey(ht.getTaskEo().getVirProcessInstance().getBillKey());
			hi.setBillInfo(ht.getTaskEo().getVirProcessInstance().getBillInfo());
			hi.setTaskId(ht.getTaskEo().getTaskId());
			hi.setCounterSignRuleId(ht.getTaskEo().getCounterSignRule().toString());
			hi.setCounterSignRuleName(ht.getTaskEo().getEnumDescription("counterSignRule"));
			if(ht.getAgentAuditUserName()==null)
				hi.setAgentUserName(ht.getAuditUserName());
			else
				hi.setAgentUserName(ht.getAgentAuditUserName());
			hi.setId(ht.getId());
			resultlist.add(hi);
		}
		qr2.setResultlist(resultlist);
		qr2.setTotalrecord(qr.getTotalrecord());
		return qr2;
	}
	
	
	/**
	 * 
	 * 得到某个的待办任务
	 * @author yangtao
	 * @date 2011-8-8下午04:29:51
	 * @param userName
	 * @param start
	 * @param limit
	 * @return
	 */
	public QueryResult<TaskInfo> getPersonUnDoneTasks(Map map,int start,int limit){
		String countSql = "SELECT COUNT(DISTINCT o.id) FROM TaskEo o JOIN o.candidateUsers cu LEFT JOIN cu.agent ag";
		String sql = "SELECT DISTINCT o FROM TaskEo o JOIN o.candidateUsers cu LEFT JOIN cu.agent ag";
		//组装查询条件
		String sqlParam = " WHERE o.isEnd=0 AND cu.isExecutedTask=0 ";
		Set set = map.keySet();
		Iterator it = set.iterator();
		while(it.hasNext()){
			String name = (String)it.next();
			if("userName".equals(name)){
				sqlParam += " and  (cu.user.loginId='"+(String)map.get(name)+"' or ag.loginId='"+(String)map.get(name)+"') ";
			}
			if("beginTime".equals(name)){
				sqlParam += " and o.startTime>='"+(String)map.get(name)+"'";
			}
			if("endTime".equals(name)){
				sqlParam += " and o.startTime<='"+(String)map.get(name)+"'";
			}
			if("billInfo".equals(name)){
				sqlParam += " and o.virProcessInstance.billInfo like '"+"%"+(String)map.get(name)+"%"+"'";
			}
			if("orgId".equals(name)){
				sqlParam += " and o.virProcessInstance.virProDefinition.assignedOrg.id='"+(String)map.get(name)+"'";
			}
		}
		long total = (Long)(this.getDao().getHibernateSession().createQuery(countSql+sqlParam).list().get(0));
		sqlParam += " ORDER BY o.startTime DESC,o.id ASC ";
		List<TaskEo> list = this.getDao().getHibernateSession().createQuery(sql+sqlParam).setFirstResult(start).setMaxResults(limit).list();
		QueryResult<TaskInfo> qr = new QueryResult<TaskInfo>();
		List<TaskInfo> resultList2 = new ArrayList<TaskInfo>();
		for(TaskEo taskEo : list){
			TaskInfo ti = new TaskInfo();
			VirProcessInstance vpi = taskEo.getVirProcessInstance();
			ti.setId(taskEo.getId());
			ti.setTaskId(taskEo.getTaskId());
			ti.setTaskName(taskEo.getTaskName());
			ti.setExecutionId(vpi.getProcessInstanceId());
			ti.setDeployId(vpi.getVirProDefinition().getProDefinition().getDeployId());
			ti.setProcessDefinitionId(vpi.getVirProDefinition().getProDefinition().getProDefId());
			ti.setProcessName(vpi.getVirProDefinition().getName());
			ti.setProcessVersion(vpi.getVirProDefinition().getVersion());
			ti.setAssignOrgId(vpi.getVirProDefinition().getAssignedOrg().getId());
			ti.setBillKey(vpi.getBillKey());
			ti.setBillInfo(vpi.getBillInfo());
			ti.setCreateTime(taskEo.getStartTime());
			ti.setTaskCandiateUser(getCandiateUserFromTaskEo(taskEo));
			ti.setCounterSignRuleName(taskEo.getEnumDescription("counterSignRule"));
			ti.setCounterSignRuleId(taskEo.getCounterSignRule().toString());
			ti.setNextTask(taskEo.getNextTask());
			ti.setNextTaskUser(taskEo.getNextTaskUser());
			ti.setEditBill(taskEo.getEditBill());
			resultList2.add(ti);
		}
		
		qr.setTotalrecord(total);
		qr.setResultlist(resultList2);
		return qr;
	}
	
	//获取某个任务的候选人(已经办理的人标志为绿色)
	private String getCandiateUserFromTaskEo(TaskEo taskEo){
		String result = "";
		List<CandidateUser> list = taskEo.getCandidateUsers();
		result = "<span style=\"color:green;\">";
		for(CandidateUser cadu : list){
			if(cadu.getIsExecutedTask()){
				result+= cadu.getUser().getLoginId();
				result +=";";
			}
		}
		if(result.endsWith(";"))
			result = result.substring(0,result.length()-1);
		result+="</span>";
		for(CandidateUser cadu : list){
			if(!cadu.getIsExecutedTask()){
				result+= cadu.getUser().getLoginId();
				result +=";";
			}
		}
		if(result.endsWith(";"))
			result = result.substring(0,result.length()-1);
		return result;
	}
	
	/**
	 * 
	 * 取回某个任务1、有会签，正在办理此会签，办理过的人可以取回
	 * 			  2、有会签，该任务节点已经办理完毕，下个任务没办理之前，可以取回
	 * 			  3、无会签，在下个任务没办理之前，可以取回
	 * @author yangtao
	 * @date 2011-8-10下午02:57:23
	 * @param executionId
	 * @param deployId
	 * @param userName
	 */
	public void withdrawTask(String histaskEoId,String userName)throws Exception{
		//要取回的历史任务
		HistoryTaskEo ht = this.getDao().find(HistoryTaskEo.class, histaskEoId);
		//防止服务器重启的情况
		this.jbpmService.initializeProcessDefinition(ht.getTaskEo().getVirProcessInstance().getVirProDefinition().getProDefinition().getDeployId());
		
		//根据流程实例获取当前任务
		TaskEo taskEo = this.getCrrentTaskByBizKey(ht.getTaskEo().getVirProcessInstance().getBillKey());
		Task task = this.getTaskByTaskId(taskEo.getTaskId());
		//当前要取回的任务
		TaskEo te = ht.getTaskEo();
		List<CandidateUser> cadus = te.getCandidateUsers();
		//如果取回的任务就是当前正在执行的任务
		if(taskEo.getTaskId().equals(te.getTaskId())){
			if(ht.getTaskAuditDecision().toString().equals(TaskAuditDecision.REASSIGN.toString())){//取回委派的任务
				String reassignUserLoginId = ht.getAuditOpinion().substring(ht.getAuditOpinion().indexOf(":")+1);
				List candidateUsers = te.getCandidateUsers();
				if(reassignUserLoginId.contains(",")){
					for(String id : reassignUserLoginId.split(",")){
						for(int i=0;i<cadus.size();i++){
							CandidateUser cadu = cadus.get(i);
							if(cadu.getUser().getLoginId().equals(id))
								candidateUsers.remove(i);
						}
						this.jbpmService.removeTaskCandidate(task, id);
					}
					te.setCandidateUsers(candidateUsers);
					te.setCandidateUserCount(candidateUsers.size());
				}else{
					for(int i=0;i<cadus.size();i++){
						CandidateUser cadu = cadus.get(i);
						if(cadu.getUser().getLoginId().equals(reassignUserLoginId))
							candidateUsers.remove(i);
					}
					te.setCandidateUsers(candidateUsers);
					te.setCandidateUserCount(candidateUsers.size());
					this.jbpmService.removeTaskCandidate(task, reassignUserLoginId);
				}
				
				this.getDao().update(te);
				//删除他取回的这个任务历史
				this.getDao().delete(HistoryTaskEo.class, histaskEoId);
			}else{//取回同意、不同意的任务
				if(ht.getTaskAuditDecision().toString().equals(TaskAuditDecision.AGREE.toString())){
					te.setAgreenCount(te.getAgreenCount()-1);
				}else if(ht.getTaskAuditDecision().toString().equals(TaskAuditDecision.NO_AGREE.toString())){
					te.setNoAgreenCount(te.getNoAgreenCount()-1);
				}
				//从执行过的人里面去掉这个人
				for(CandidateUser cadu : cadus){
					if((cadu.getUser().getLoginId().equals(ht.getAuditUserName())&&
							cadu.getUser().getLoginId().equals(ht.getAgentAuditUserName()))||
							(cadu.getAgent()!=null&&cadu.getUser().getLoginId().equals(ht.getAuditUserName())&&cadu.getAgent().getLoginId().equals(ht.getAgentAuditUserName()))){
						cadu.setIsExecutedTask(false);
					}
				}
				te.setCandidateUsers(cadus);
				this.getDao().update(te);
				//再把这个任务分配给这个人
				this.jbpmService.addCandidateUser(task.getId(), ht.getAuditUserName());
				//删除他取回的这个任务历史
				this.getDao().delete(HistoryTaskEo.class, histaskEoId);
			}
		}else{//如果取回的任务就不是当前正在执行的任务
			List li = this.getDao().queryByWhere(HistoryTaskEo.class, "taskEo.taskId=?", new Object[]{taskEo.getTaskId()});
			if(li.size()>0){//下个任务已经执行了
				throw new BizException(ExceptionMsgType.NEXT_TASK_COMPLETE);
			}else{//下个任务没有执行的情况
				if(te.getTaskId().equals(taskEo.getPreTaskId())){
					if(ht.getTaskAuditDecision().toString().equals(TaskAuditDecision.REASSIGN.toString())){//取回委派的任务
						throw new BizException(ExceptionMsgType.COUNTERSIGN_NO_BACK);
					}else{
						String destActivityName = te.getTaskName();
						//删除当前任务的eo
						taskEo.getCandidateUsers().clear();
						this.getDao().delete(TaskEo.class, taskEo.getId());
						
						//执行任务
						this.jbpmService.completeTaskByCreateTransiton(task, destActivityName, System.currentTimeMillis()+"");
						//把旧任务的信息，拷贝到新任务中
						TaskEo currentTaskEo = this.getCrrentTaskByBizKey(te.getVirProcessInstance().getBillKey());
						currentTaskEo.setPreTaskId(te.getPreTaskId());
						if(ht.getTaskAuditDecision().toString().equals(TaskAuditDecision.AGREE.toString())){
							currentTaskEo.setAgreenCount(te.getAgreenCount()-1);
						}else if(ht.getTaskAuditDecision().toString().equals(TaskAuditDecision.NO_AGREE.toString())){
							currentTaskEo.setNoAgreenCount(te.getNoAgreenCount()-1);
						}
						List<CandidateUser> lists = currentTaskEo.getCandidateUsers();
						for(CandidateUser cadu : lists){
							for(CandidateUser c : cadus){
								if(cadu.getUser()==c.getUser()&&cadu.getAgent()==c.getAgent()){
									cadu.setIsExecutedTask(c.getIsExecutedTask());
								}
							}
							if((cadu.getUser().getLoginId().equals(ht.getAuditUserName())&&
									cadu.getUser().getLoginId().equals(ht.getAgentAuditUserName()))||
									(cadu.getAgent()!=null&&cadu.getUser().getLoginId().equals(ht.getAuditUserName())&&cadu.getAgent().getLoginId().equals(ht.getAgentAuditUserName()))){
								cadu.setIsExecutedTask(false);
							}
						}
						this.getDao().update(currentTaskEo);
						
						//历史任务对应的eo改为新的eo
						List<HistoryTaskEo> tk = this.getDao().queryByWhere(HistoryTaskEo.class, "taskEo.id=?", new Object[]{te.getId()});
						for(HistoryTaskEo hi : tk){
							hi.setTaskEo(currentTaskEo);
							hi.setTaskHistoryState(TaskHistoryState.IS_DOING);
							this.getDao().update(hi);
						}
						
						//删除他取回的这个任务历史
						this.getDao().delete(HistoryTaskEo.class, histaskEoId);
						te.getCandidateUsers().clear();
						//删除历史任务对应的旧的eo
						this.getDao().delete(TaskEo.class, te.getId());
					}
				}else{
					throw new BizException(ExceptionMsgType.NEXT_TASK_COMPLETE);
				}
			}
		}
	}
	
	/**
	 * 
	 * 保存节点执行历史 如任务的人工执行
	 * @author yangtao
	 * @date 2011-8-18下午02:09:26
	 * @param vpi
	 * @param task
	 * @param createTransitionName
	 * @param auditOpinion
	 * @param auditDecision
	 * @param userName
	 */
	public void saveActivityHistory(TaskEo taskEo,Task task,String auditOpinion,String auditDecision,String userName,String agentUserName,TaskHistoryState th){
		HistoryTaskEo historyTask = new HistoryTaskEo();
		historyTask.setTaskEo(taskEo);
		historyTask.setAuditUserName(userName);
		if(agentUserName!=null)
			historyTask.setAgentAuditUserName(agentUserName);
		if(TaskAuditDecision.AGREE.toString().equals(auditDecision)){
			historyTask.setTaskAuditDecision(TaskAuditDecision.AGREE);
		}else if(TaskAuditDecision.NO_AGREE.toString().equals(auditDecision)){
			historyTask.setTaskAuditDecision(TaskAuditDecision.NO_AGREE);
		}else if(TaskAuditDecision.BACK.toString().equals(auditDecision)){
			historyTask.setTaskAuditDecision(TaskAuditDecision.BACK);
		}else if(TaskAuditDecision.REASSIGN.toString().equals(auditDecision)){
			historyTask.setTaskAuditDecision(TaskAuditDecision.REASSIGN);
		}
		historyTask.setAuditOpinion(auditOpinion);
		historyTask.setStartTime(dateformat.format(task.getCreateTime()));
		historyTask.setEndTime(dateformat.format(new Date()));
		//如果该任务节点办理完毕，把所有的这些任务的状态都改成办理完毕
		if(th.toString().equals(TaskHistoryState.IS_DONE.toString())){
			List<HistoryTaskEo> list = this.getDao().queryByWhere(HistoryTaskEo.class, "taskEo.taskId=?", new Object[]{task.getId()});
			for(HistoryTaskEo ht : list){
				ht.setTaskHistoryState(TaskHistoryState.IS_DONE);
				this.getDao().update(ht);
			}
		}
		historyTask.setTaskHistoryState(th);
		this.getDao().create(historyTask);
	}
	/**
	 * 取得某流程实例正在执行的任务
	 * 
	 * @param piId
	 * @return
	 */
	public List<Task> getTasksByPiId(String piId){
		return this.jbpmService.getTasksByPiId(piId);
	}
	
	/**
	 * 
	 * 根据当前任务得到流程图中的上一个任务名称
	 * @author yangtao
	 * @date 2011-8-22下午03:53:24
	 * @param task
	 * @return
	 */
	public String getPreTaskByCurrentTask(String currentActivityName,String deployId){
		return this.jbpmService.getPreTaskByCurrentTask(currentActivityName, deployId);
	}
	
	/**
	 * 
	 * 保存平台任务
	 * @author yangtao
	 * @date 2011-8-30上午11:27:57
	 * @param taskEo
	 */
	public void saveTaskEo(TaskEo taskEo){
		this.getDao().create(taskEo);
	}
	
	/**
	 * 
	 * 是否是任务的候选人
	 * @author yangtao
	 * @date 2011-9-14下午02:50:09
	 * @param task
	 * @param userLoginId
	 * @return
	 */
	public boolean isTaskCandidate(Task task,String userLoginId){
		return this.jbpmService.isTaskCandidate(task, userLoginId);
	}
	/**
	 * 
	 * 是否是任务的候选人
	 * @author yangtao
	 * @date 2011-9-14下午02:50:09
	 * @param taskEo
	 * @param userLoginId
	 * @return
	 */
	public boolean isTaskCandidate(TaskEo taskEo,String userLoginId){
		boolean result = false;
		List<CandidateUser> list = taskEo.getCandidateUsers();
		for(CandidateUser cdu : list){
			if(cdu.getUser().getLoginId().equals(userLoginId)||(cdu.getAgent()!=null&&cdu.getAgent().getLoginId().equals(userLoginId))){
				result = true;
				break;
			}
		}
		return result;
	}
	
	/**
	 * 获取某个虚拟任务节点上面配置的人员（岗位、角色转成人员）
	 * 
	 * @author YangTao
	 * @date 2012-2-1上午11:03:53
	 * @param vpa
	 * @return
	 */
	public List<User> getVirProActivityAssignedUsers(VirProActivity vpa){
		List<User> list = new ArrayList<User>();
		List<oecp.platform.user.eo.User> assignUsers = vpa.getAssignUsers();//参与的用户
		List<Post> assignPosts = vpa.getAssignPosts();//参与的岗位
		List<OrgRole> assignRoles = vpa.getAssignRoles();//参与的角色
		//第一  后台给当前任务节点配置的人员
		if(assignUsers!=null && assignUsers.size()!=0){
			list.addAll(assignUsers);
		}
		//第二  后台给当前任务节点配置的岗位
		else if(assignPosts!=null && assignPosts.size()!=0){
			for (Post p : assignPosts) {
				List<Person> ps = personService.getAllPersonsByPost(p
						.getId());
				// 根据岗位得到相应的用户
				for (Person person : ps) {
					oecp.platform.user.eo.User u = userService.getUserByPersonId(person.getId());
					if (u != null) {
						list.add(u);
					}
				}
			}
		}
		//第三  后台给当前任务节点配置的角色
		else if(assignRoles!=null && assignRoles.size()!=0){
			for (OrgRole or : assignRoles) {
				List<oecp.platform.user.eo.User> rus = or.getUsers();
				list.addAll(rus);
			}
		}
		//去重,不保持顺序
		HashSet<User> set = new HashSet<User>();
		set.addAll(list);
		list = Arrays.asList(set.toArray(new User[0]));
		for(User user : list){//人员必须是流程分配组织里面的人员;
			if(user.getCreatedByOrg()!=vpa.getVirProDefinition().getAssignedOrg())
				list.remove(user);
		}
		return list;
	}

	/**
	 * 根据业务主健和结点名称获取结点配置人员
	 * @author yangtao
	 * @date 2013-5-13下午4:52:21
	 * @param billKey
	 * @param activityName
	 * @return
	 */
	public List<User> queryUserFromVpa(String billKey,String activityName)throws Exception{
		List<QueryCondition> conditionss = new ArrayList<QueryCondition>();
		QueryCondition q = new QueryCondition("billKey","=",billKey);
		conditionss.add(q);
		List<VirProcessInstance> li = this.proExecutionService.getVirProcessInstanceByConditions(conditionss);
		List<oecp.platform.user.eo.User> list = new ArrayList<oecp.platform.user.eo.User>();
		if(li.size()>0){
			List<QueryCondition> conditions = new ArrayList<QueryCondition>();
			QueryCondition qc = new QueryCondition("virProDefinition.id","=",li.get(0).getVirProDefinition().getId());
			QueryCondition qc2 = new QueryCondition("activityName","=",activityName);
			conditions.add(qc);
			conditions.add(qc2);
			List<VirProActivity> vpas = getVirProActivityByConditons(conditions);
			if(vpas.size()!=0){
				VirProActivity vpa = vpas.get(0);
				list = getVirProActivityAssignedUsers(vpa);
			}
		}
		return list;
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


	public JbpmService getJbpmService() {
		return jbpmService;
	}


	public void setJbpmService(JbpmService jbpmService) {
		this.jbpmService = jbpmService;
	}


	public ProDefinitionService getProDefinitionService() {
		return proDefinitionService;
	}


	public void setProDefinitionService(ProDefinitionService proDefinitionService) {
		this.proDefinitionService = proDefinitionService;
	}


	public ProExecutionService getProExecutionService() {
		return proExecutionService;
	}


	public void setProExecutionService(ProExecutionService proExecutionService) {
		this.proExecutionService = proExecutionService;
	}
	
	
	
}
