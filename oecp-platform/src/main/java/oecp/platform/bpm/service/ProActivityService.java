/*
 * Copyright (c) 2011 OECP All Rights Reserved.                	
 * <a href="http://www.oecp.cn">http://www.oecp.cn</a>                                                                 
 */

package oecp.platform.bpm.service;

import java.util.List;
import java.util.Map;

import oecp.framework.dao.QueryCondition;
import oecp.framework.dao.QueryResult;
import oecp.framework.exception.BizException;
import oecp.framework.service.BaseService;
import oecp.platform.bpm.enums.TaskHistoryState;
import oecp.platform.bpm.eo.ProActivity;
import oecp.platform.bpm.eo.ProDefinition;
import oecp.platform.bpm.eo.TaskEo;
import oecp.platform.bpm.eo.VirProActivity;
import oecp.platform.bpm.eo.VirProcessInstance;
import oecp.platform.bpm.vo.HistoryTaskInfo;
import oecp.platform.bpm.vo.TaskInfo;
import oecp.platform.user.eo.User;

import org.jbpm.api.task.Task;

/**
 * 流程节点服务,包括任务节点
 * 
 * @author yongtree
 * @date 2011-6-28 下午02:19:07
 * @version 1.0
 */
public interface ProActivityService extends BaseService<ProActivity> {

	/**
	 * 根据ProcessDefinition id和activityName得到相应的节点
	 * 
	 * @author yongtree
	 * @date 2011-6-28 下午03:12:28
	 * @param proDefId ProcessDefinition id
	 * @param activityName
	 * @return
	 */
	public ProActivity getActivityByDefAndActivityName(String proDefId,
			String activityName);

	/**
	 * 
	 * 根据任务ID得到封装的流程定义
	 * @author yangtao
	 * @date 2011-7-27上午11:18:28
	 * @param taskId
	 * @return
	 */
	public ProDefinition getProDefinitionByTaskId(String taskId);
	
	/**
	 * 
	 * 查出某个人的所有任务
	 * @author yangtao
	 * @date 2011-7-28上午09:23:11
	 * @param userName
	 * @return
	 */
	public List<Task> findPersonalTasks(String userName);
	
	/**
	 * 
	 * 获取某个人的候选任务
	 * @author yangtao
	 * @date 2011-8-3上午09:15:31
	 * @param userName
	 * @return
	 */
	public List<Task> findGroupTasks(String userName);
	/**
	 * 
	 * 根据任务ID执行任务,待办任务时调用
	 * @author yangtao
	 * @date 2011-7-28上午09:26:51
	 * @return
	 */
	public void completeTask(String taskId,String nextperson,String auditOpinion,String auditDecision,boolean isTake,String userName,String preTaskName,String transitionName)throws Exception;
	

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
			String userKey, String auditOpinion,String auditDecision,String preTaskName,String transitionName)throws Exception; 
	/**
	 * 
	 * 把某个任务分配给某个人
	 * @author yangtao
	 * @date 2011-7-28上午09:44:10
	 * @param taskId
	 * @param userName
	 */
	public void assignTask(String taskId,String userName);
	
	/**
	 * 
	 * 待办任务中给某个任务重新指派人员
	 * @author yangtao
	 * @date 2011-8-17上午09:48:08
	 * @param taskId
	 * @param assignFlag
	 * @param ids
	 */
	public void reassignVirProActivity(String taskId,String assignFlag,String ids,String userName);
	
	/**
	 * 
	 * 给某个任务增加候选人
	 * @author yangtao
	 * @date 2011-8-16下午03:17:22
	 * @param taskId
	 * @param userName
	 */
	public void addCandidateUser(String taskId,String userName);
	
	/**
	 * 
	 * 根据条件查询出VirProActivity
	 * @author yangtao
	 * @date 2011-8-16上午11:52:21
	 * @param conditions
	 * @return
	 */
	public List<VirProActivity> getVirProActivityByConditons(List<QueryCondition> conditions);


	/**
	 * 
	 * 根据taskId获得Task
	 * @author yangtao
	 * @date 2011-7-29上午11:16:42
	 * @param taskId
	 * @return
	 */
	public Task getTaskByTaskId(String taskId);
	
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
	public QueryResult<HistoryTaskInfo> getPersonDoneTasks(List<QueryCondition> conditions,int start,int limit);
	
	

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
	public QueryResult<TaskInfo> getPersonUnDoneTasks(Map map,int start,int limit);
	/**
	 * 
	 * 取回某个任务
	 * @author yangtao
	 * @date 2011-8-10下午02:57:23
	 * @param executionId
	 * @param deployId
	 * @param userName
	 */
	public void withdrawTask(String histaskEoId,String userName)throws Exception;

	/**
	 * 取得某流程实例正在执行的任务
	 * 
	 * @param piId
	 * @return
	 */
	public List<Task> getTasksByPiId(String piId);
	
	/**
	 * 
	 * 根据当前任务得到流程图中的上一个任务名称
	 * @author yangtao
	 * @date 2011-8-22下午03:53:24
	 * @param task
	 * @return
	 */
	public String getPreTaskByCurrentTask(String currentActivityName,String deployId);
	
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
	public void saveActivityHistory(TaskEo taskEo,Task task,String auditOpinion,String auditDecision,String userName,String agentUserName,TaskHistoryState th);

	/**
	 * 
	 * 保存平台任务
	 * @author yangtao
	 * @date 2011-8-30上午11:27:57
	 * @param taskEo
	 */
	public void saveTaskEo(TaskEo taskEo);
	
	/**
	 * 
	 * 是否是任务的候选人
	 * @author yangtao
	 * @date 2011-9-14下午02:50:09
	 * @param task
	 * @param userLoginId
	 * @return
	 */
	public boolean isTaskCandidate(Task task,String userLoginId);
	
	/**
	 * 
	 * 是否是任务的候选人
	 * @author yangtao
	 * @date 2011-9-14下午02:50:09
	 * @param task
	 * @param userLoginId
	 * @return
	 */
	public boolean isTaskCandidate(TaskEo taskEo,String userLoginId);
	
	/**
	 * 根据当前任务返回流程图中这个任务上面的所有任务节点
	 * 
	 * @author YangTao
	 * @date 2012-1-29下午03:00:03
	 * @param billKey
	 * @return
	 */
	public QueryResult<TaskInfo> getPreTaskByCurrentTask(String billKey);
	
	/**
	 * 根据当前任务返回和当前结点连接的下一个任务（多个）
	 * @author yangtao
	 * @date 2013-5-9下午4:58:10
	 * @return
	 */
	public QueryResult<TaskInfo> getNextTasksByCurrentTask(String billKey);
	
	/**
	 * 根据业务主键，获取功能流程查看页面路径
	 * 
	 * @author YangTao
	 * @date 2012-2-1上午10:03:25
	 * @param billKey
	 * @return
	 */
	public String getFormResourceName(String billKey);
	
	/**
	 * 获取某个虚拟任务节点上面配置的人员（岗位、角色转成人员）
	 * 
	 * @author YangTao
	 * @date 2012-2-1上午11:03:53
	 * @param vpa
	 * @return
	 */
	public List<User> getVirProActivityAssignedUsers(VirProActivity vpa);
	
	/**
	 * 根据主键获得当前等待执行的任务
	 * 
	 * @author YangTao
	 * @date 2012-2-9上午10:16:40
	 * @param bizKey
	 * @return
	 * @throws Exception
	 */
	public TaskEo getCrrentTaskByBizKey(String bizKey)throws Exception;
	
	/**
	 * 根据主键得到当前的流程实例
	 * 
	 * @author YangTao
	 * @date 2012-2-9上午10:16:58
	 * @param bizKey
	 * @return
	 * @throws Exception
	 */
	public VirProcessInstance getVirProcessInstanceByBizKey(String bizKey)throws BizException;
	
	/**
	 * 根据业务主健和结点名称获取结点配置人员
	 * @author yangtao
	 * @date 2013-5-13下午4:52:21
	 * @param billKey
	 * @param activityName
	 * @return
	 */
	public List<User> queryUserFromVpa(String billKey,String activityName)throws Exception;
}
