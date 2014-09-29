package oecp.platform.bpm.service;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Map;

import oecp.framework.exception.BizException;
import oecp.platform.bpm.vo.Node;

import org.jbpm.api.ProcessDefinition;
import org.jbpm.api.ProcessInstance;
import org.jbpm.api.cmd.Command;
import org.jbpm.api.history.HistoryActivityInstance;
import org.jbpm.api.history.HistoryTask;
import org.jbpm.api.model.Transition;
import org.jbpm.api.task.Task;

public interface JbpmService {

	/**begin**********流程定义的方法********************************************************************/
	/**
	 * 通过zip文件发布流程
	 * 
	 * @author yongtree
	 * @date 2011-6-29 下午04:11:17
	 * @param zip
	 * @return
	 * @throws BizException 
	 */
	public String deploy(File zip) throws BizException;
	/**
	 * 
	 * 通过deployId得到流程定义
	 * @author yangtao
	 * @date 2011-7-26下午03:55:54
	 * @param deployId
	 * @return
	 */
	public ProcessDefinition getProcessDefinitionByDeployId(String deployId);
	/**
	 * 
	 * 通过配置文件获得任务节点
	 * @author yangtao
	 * @date 2011-7-26下午03:56:22
	 * @param xml
	 * @param includeStart
	 * @return
	 */
	public List<Node> getTaskNodesFromXml(String xml, boolean includeStart);
	
	/**
	 * 
	 * 删除流程定义
	 * @author yangtao
	 * @date 2011-7-26下午03:56:48
	 * @param deployId
	 * @param cascade
	 */
	public void deleteDeployment(String deployId,boolean cascade);
	


	/**
	 * 按发布id取得流程定义
	 * 
	 * @return
	 * @throws IOException
	 */
	public String getDefinitionXmlByDpId(String deployId) throws IOException;
	
	/**
	 * 按发布id取得流程定义的PNG图片
	 * 
	 * @throws IOException
	 */
	public byte[] getDefinitionPngByDpId(String deployId) throws IOException;

	/**
	 * 按流程实例ID取得流程定义
	 * 
	 * @param piId
	 * @return
	 * @throws IOException
	 */
	public String getDefinitionXmlByPiId(String piId) throws IOException;
	
	/**
	 * 
	 * 根据流程实例ID得到流程定义
	 * @author yangtao
	 * @date 2011-7-27上午10:30:10
	 * @param instanceId
	 * @return
	 */
	public ProcessDefinition getProcessDefinitionByInstanceId(String instanceId);
	
	/**
	 * 
	 * 根据任务ID得到封装的流程定义
	 * @author yangtao
	 * @date 2011-7-27上午11:18:28
	 * @param taskId
	 * @return
	 */
	public ProcessDefinition getProDefinitionByTaskId(String taskId);
	
	/**
	 * 根据某个流程定义，取出该流程中所有的节点
	 * 
	 * @param deployId
	 *            是否包括启动节点
	 * @return
	 */
	public List<Node> FindAllActivities(String deployId);
	
	/**
	 * 
	 * 初始化流程定义信息：给每个任务增加一个默认的指派人员接口
	 * @author yangtao
	 * @date 2011-7-28下午03:06:54
	 * @param deployId
	 */
	public void initializeProcessDefinition(String deployId);
	
	
	/**
	 * 
	 * 获得流程图中每个节点的位置的值 x y x1 y1
	 * @author yangtao
	 * @date 2011-8-11上午10:46:05
	 * @param deployId
	 */
	public Map<String,Map> getAllActivityPlaceValue(String deployId);
	/**end**********流程定义的方法********************************************************************/
	
	/**begin**********流程实例的方法********************************************************************/
	/**
	 * 根据流程实例ID取得流程实例
	 * 
	 * @param piId
	 * @return
	 */
	public ProcessInstance getProcessInstanceByPid(String piId);
	
	/**
	 * 
	 * 根据任务ID获得流程实例
	 * @author yangtao
	 * @date 2011-7-29上午11:05:14
	 * @param taskId
	 * @return
	 */
	public ProcessInstance getProcessInstanceByTaskId(String taskId);

	/**
	 * 启动工作流，并返回流程实例
	 * 
	 * @param pdid
	 * @param billKey 业务单据KEY
	 * @param variables
	 * @return
	 */
	public ProcessInstance startProcess(String pdid, String billKey,Map variables);

	/**
	 * 显示某个流程当前任务对应的所有出口
	 * 
	 * @param piId
	 * @return
	 */
	public List<Transition> getTransitionsForSignalProcess(String piId);

	/**
	 * 取到节点类型
	 * 
	 * @param xml
	 * @param nodeName
	 * @return
	 */
	public String getNodeType(String xml, String nodeName);

	/**
	 * 通过流程定义实例ID取得流程对应的XML
	 * 
	 * @param piId
	 * @return
	 * @throws IOException
	 */
	public String getProcessDefintionXMLByPiId(String piId) throws IOException;
	
	/**
	 * 执行普通任务节点下一步
	 * 
	 * @param executionId
	 * @param transitionName
	 * @param variables
	 */
	public void signalProcess(String executionId, String transitionName,
			Map<String, Object> variables);

	/**
	 * 结束流程实例
	 * 
	 * @param piId
	 */
	public void endProcessInstance(String piId);
	
	/**
	 * 删除流程实例
	 * 
	 * @param piId
	 */
	public void deleteProcessInstance(String piId);
	
	/**
	 * 
	 * 得到某个定义的所有的流程实例
	 * @author yangtao
	 * @date 2011-7-26上午08:31:31
	 * @return
	 */
	public List<ProcessInstance> getAllProcessInstance(String processDefinitionId,int start,int limit);
	
	/**
	 * 
	 * 得到某个定义的所有的流程实例的个数
	 * @author yangtao
	 * @date 2011-7-26上午08:31:31
	 * @return
	 */
	public int getAllProcessInstanceCount(String processDefinitionId);
	
	/**
	 * 
	 * 根据实例ID得到当前正在等待的节点
	 * @author yangtao
	 * @date 2011-7-26上午08:31:31
	 * @return String
	 */
	public List<String> getCurrentActivityNameByPid(String processInstanceId,String deployId);
	/**end**********流程实例的方法********************************************************************/
	
	
	
	/**begin*******流程任务的方法*****************************************************************************/
	/**
	 * 取得某流程实例正在执行的任务
	 * 
	 * @param piId
	 * @return
	 */
	public List<Task> getTasksByPiId(String piId);
	
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
	 * 通过流程实例ID和节点的名字得到相关任务
	 * @author yongtree
	 * @date 2011-7-4下午08:58:15
	 * @param piId
	 * @param actName
	 * @return
	 */
	public Task getTaskByPiIdAndActName(String piId,String actName);

	/**
	 * 完成任务，
	 * 
	 * @param taskId
	 *            任务ID
	 * @param transitionName
	 *            　下一步的转换名称
	 * @param variables
	 *            　流程的附加数据
	 */
	public void completeTask(String taskId, String transitionName, Map variables);
	
	/**
	 * 
	 * 根据任务ID执行任务
	 * @author yangtao
	 * @date 2011-7-28上午09:26:51
	 * @return
	 */
	public void completeTask(String taskId,String transitionName,String nextperson,boolean isTake,String userName);
	
	/**
	 * 
	 * 完成任务
	 * @author yangtao
	 * @date 2011-9-5下午04:41:11
	 * @param task
	 */
	public void completeTask(Task task);
	
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
	 * 获取候选人的任务 带分页
	 * @author yangtao
	 * @date 2011-8-16下午04:10:54
	 * @param userName
	 * @return
	 */
	public List<Task> findCandiateTasks(String userName,int start,int limit);
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
	 * 给某个任务增加候选人
	 * @author yangtao
	 * @date 2011-8-16下午03:17:22
	 * @param taskId
	 * @param userName
	 */
	public void addCandidateUser(String taskId,String userName);
	
	/**
	 * 
	 * 某个候选人接受任务
	 * @author yangtao
	 * @date 2011-8-3上午09:27:46
	 * @param taskId
	 * @param userName
	 */
	public void takeTask(String taskId,String userName);
	
	/**
	 * 
	 * 完成任务，根据目的节点名称建立transition并且按照这个执行路径去执行任务
	 * @author yangtao
	 * @date 2011-8-10下午04:07:58
	 * @param task
	 * @param destActivityName
	 * @param createTransitionName
	 */
	public void completeTaskByCreateTransiton(Task task, String destActivityName,String createTransitionName)throws Exception;
	
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
	public List<HistoryTask> getPersonDoneTasks(String userName,int start,int limit);
	
	/**
	 * 根据当前任务返回流程图中这个任务上面的所有任务节点
	 * 
	 * @author YangTao
	 * @date 2012-1-29下午03:00:03
	 * @param currentActivityName
	 * @param deployId
	 * @param resultList
	 * @return
	 */
	public List getPreTaskByCurrentTask(String currentActivityName,String deployId,String processIntanceId,List resultList);
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
	 * 根据当前任务得到流程图中的下一个任务名称(有弊端)
	 * @author yangtao
	 * @date 2011-9-16上午08:49:54
	 * @param currentActivityName
	 * @param deployId
	 * @return
	 */
	public String getNextTaskByCurrentTask(String currentActivityName,String deployId);
	
	/**
	 * 
	 * 根据当前任务得到流程图中的下一个任务名称(多个)
	 * @author yangtao
	 * @date 2011-9-16上午08:49:54
	 * @param currentActivityName
	 * @param deployId
	 * @return
	 */
	public List<String[]> getNextTasksByCurrentTask(String currentActivityName,String deployId);
	
	/**
	 * 
	 * 获得某个任务的所有侯选人
	 * @author yangtao
	 * @date 2011-8-26下午04:40:58
	 * @param tip
	 * @return
	 */
	public String getTaskCandidateUser(Task tip);
	/**
	 * 
	 * 根据登录账号从一个任务上删除这个候选人
	 * @author yangtao
	 * @date 2011-8-30下午03:20:15
	 * @param task
	 * @param userLoginId
	 */
	public void removeTaskCandidate(Task task,String userLoginId);
	/**
	 * 
	 * 给某个任务创建子任务，并分配人
	 * @author yangtao
	 * @date 2011-8-29下午04:46:37
	 * @param task
	 * @param list
	 */
	public void createSubTasks(Task task,List<oecp.platform.user.eo.User> list);
	
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
	/**end*******流程任务的方法*****************************************************************************/


	
	/**begin*******流程历史的方法*****************************************************************************/
	/**
	 * 
	 * 根据流程ID查询出历史实例
	 * @author yangtao
	 * @date 2011-7-28上午09:44:10
	 * @param processInstanceId
	 */
	public List <HistoryActivityInstance> queryHistoryActivityInstanceByPid(String processInstanceId);

	/**end*******流程历史的方法*****************************************************************************/
	/**
	 * 执行command命令
	 */
	public Object executeCommand(Command command);
}
