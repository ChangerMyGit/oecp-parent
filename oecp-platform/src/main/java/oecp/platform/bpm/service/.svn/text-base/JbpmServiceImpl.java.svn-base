package oecp.platform.bpm.service;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.zip.ZipInputStream;

import javax.annotation.Resource;

import oecp.framework.exception.BizException;
import oecp.platform.bpm.enums.ActivityType;
import oecp.platform.bpm.enums.ProcessVariableName;
import oecp.platform.bpm.eo.ProActivity;
import oecp.platform.bpm.eventlistener.EndEventListener;
import oecp.platform.bpm.vo.Node;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.jbpm.api.Execution;
import org.jbpm.api.ExecutionService;
import org.jbpm.api.HistoryService;
import org.jbpm.api.ProcessDefinition;
import org.jbpm.api.ProcessEngine;
import org.jbpm.api.ProcessInstance;
import org.jbpm.api.ProcessInstanceQuery;
import org.jbpm.api.RepositoryService;
import org.jbpm.api.TaskService;
import org.jbpm.api.cmd.Command;
import org.jbpm.api.cmd.Environment;
import org.jbpm.api.history.HistoryActivityInstance;
import org.jbpm.api.history.HistoryTask;
import org.jbpm.api.model.Activity;
import org.jbpm.api.model.Transition;
import org.jbpm.api.task.Participation;
import org.jbpm.api.task.Task;
import org.jbpm.jpdl.internal.activity.DecisionHandlerActivity;
import org.jbpm.jpdl.internal.activity.StartActivity;
import org.jbpm.jpdl.internal.activity.TaskActivity;
import org.jbpm.pvm.internal.env.EnvironmentFactory;
import org.jbpm.pvm.internal.env.EnvironmentImpl;
import org.jbpm.pvm.internal.model.ActivityCoordinatesImpl;
import org.jbpm.pvm.internal.model.ActivityImpl;
import org.jbpm.pvm.internal.model.EventImpl;
import org.jbpm.pvm.internal.model.ExecutionImpl;
import org.jbpm.pvm.internal.model.ProcessDefinitionImpl;
import org.jbpm.pvm.internal.model.TransitionImpl;
import org.jbpm.pvm.internal.task.OpenTask;
import org.jbpm.pvm.internal.task.ParticipationImpl;
import org.jbpm.pvm.internal.task.TaskImpl;
import org.jbpm.pvm.internal.wire.descriptor.ObjectDescriptor;
import org.jbpm.pvm.internal.wire.usercode.UserCodeReference;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service(value = "jbpmService")
@Transactional
public class JbpmServiceImpl implements JbpmService {

	private static final Log logger = LogFactory.getLog(JbpmServiceImpl.class);

	@Resource(name = "processEngine")
	private ProcessEngine processEngine;

	@Resource(name = "repositoryService")
	private RepositoryService repositoryService;

	@Resource(name = "executionService")
	private ExecutionService executionService;

	@Resource(name = "taskService")
	private TaskService taskService;

	@Resource(name = "historyService")
	private HistoryService historyService;

	@Resource
	private ProActivityService proActivityServcie;

	
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
	@Override
	public String deploy(File zip) throws BizException {
		try {
			FileInputStream fin = new FileInputStream(zip);
			ZipInputStream zi = new ZipInputStream(fin);
			// 添加流程定义及发布流程至Jbpm流程表中
			String deployId = processEngine.getRepositoryService()
					.createDeployment().addResourcesFromZipInputStream(zi)
					.deploy();
			//初始化流程定义
//			this.initializeProcessDefinition(deployId);
			// zi.close();
			// fin.close();
			return deployId;
		} catch (Exception e) {
			e.printStackTrace();
			throw new BizException(e);
		}
	}

	/**
	 * 
	 * 通过deployId得到流程定义
	 * @author yangtao
	 * @date 2011-7-26下午03:55:54
	 * @param deployId
	 * @return
	 */
	public ProcessDefinition getProcessDefinitionByDeployId(String deployId) {
		return repositoryService.createProcessDefinitionQuery().deploymentId(
				deployId).uniqueResult();
	}

	/**
	 * 
	 * 删除流程定义
	 * @author yangtao
	 * @date 2011-7-26下午03:56:48
	 * @param deployId
	 * @param cascade
	 */
	public void deleteDeployment(String deployId, boolean cascade) {
		if (cascade) {
			repositoryService.deleteDeploymentCascade(deployId);
		} else {
			repositoryService.deleteDeployment(deployId);
		}
	}

	/**
	 * 按发布id取得流程定义的XML
	 * 
	 * @throws IOException
	 */
	public String getDefinitionXmlByDpId(String deployId) throws IOException {
		Set<String> names = repositoryService.getResourceNames(deployId);
		for (String n : names) {
			if (n.indexOf(".jpdl.xml") != -1) {
				InputStream in = repositoryService.getResourceAsStream(
						deployId, n);
				BufferedReader br = new BufferedReader(new InputStreamReader(
						in, "UTF-8"));
				StringBuffer out = new StringBuffer();
				String line = "";
				while ((line = br.readLine()) != null) {
					out.append(line);
				}

				return out.toString();
			}
		}
		return null;
	}
	
	/**
	 * 按发布id取得流程定义的PNG图片
	 * 
	 * @throws IOException
	 */
	public byte[] getDefinitionPngByDpId(String deployId) throws IOException {
		Set<String> names = repositoryService.getResourceNames(deployId);
		byte[] bytes = new byte[9999999];
		for (String n : names) {
			if (n.indexOf(".png") != -1) {
				InputStream in = repositoryService.getResourceAsStream(
						deployId, n);
				in.read(bytes);
			}
		}
		return bytes;
	}

	/**
	 * 按流程例id取得流程定义的XML
	 * 
	 * @throws IOException
	 */
	public String getDefinitionXmlByPiId(String piId) throws IOException {
		ProcessInstance pi = executionService.createProcessInstanceQuery()
				.processInstanceId(piId).uniqueResult();
		ProcessDefinition pd = repositoryService.createProcessDefinitionQuery()
				.processDefinitionId(pi.getProcessDefinitionId())
				.uniqueResult();
		return getDefinitionXmlByDpId(pd.getDeploymentId());
	}
	
	/**
	 * 
	 * 根据任务ID得到封装的流程定义
	 * @author yangtao
	 * @date 2011-7-27上午11:18:28
	 * @param taskId
	 * @return
	 */
	public ProcessDefinition getProDefinitionByTaskId(String taskId){
		Task task = taskService.getTask(taskId);
		ProcessInstance pi = executionService.createProcessInstanceQuery()
			.processInstanceId(task.getExecutionId()).uniqueResult();
		return this.getProcessDefinitionByInstanceId(pi.getId());
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
		ProcessInstance pi = executionService.createProcessInstanceQuery()
		.processInstanceId(instanceId).uniqueResult();
		ProcessDefinition pd = repositoryService.createProcessDefinitionQuery()
				.processDefinitionId(pi.getProcessDefinitionId())
				.uniqueResult();
		return pd;
	}



	/**
	 * 取到节点类型
	 * 
	 * @param xml
	 * @param nodeName
	 * @return
	 */
	public String getNodeType(String xml, String nodeName) {
		String type = "";
		try {
			Element root = DocumentHelper.parseText(xml).getRootElement();
			for (Element elem : (List<Element>) root.elements()) {
				if (elem.attribute("name") != null) {
					String value = elem.attributeValue("name");
					if (value.equals(nodeName)) {
						type = elem.getQName().getName();
						return type;
					}
				}
			}
		} catch (Exception ex) {
			logger.info(ex.getMessage());
		}
		return type;
	}
	
	/**
	 * 从XML文件中取得任务节点名称列表
	 * 
	 * @param xml
	 * @param includeStart
	 *            是否包括启动节点
	 * @return
	 */
	public List<Node> getTaskNodesFromXml(String xml, boolean includeStart) {
		List<Node> nodes = new ArrayList<Node>();
		try {
			Element root = DocumentHelper.parseText(xml).getRootElement();
			for (Element elem : (List<Element>) root.elements()) {
				String type = elem.getQName().getName();
				if (ActivityType.TASK.toString().equalsIgnoreCase(type)) {
					if (elem.attribute("name") != null) {
						Node node = new Node(elem.attribute("name").getValue(),
								"任务节点");
						nodes.add(node);
					}
				} else if (includeStart && ActivityType.START.toString().equalsIgnoreCase(type)) {
					if (elem.attribute("name") != null) {
						Node node = new Node(elem.attribute("name").getValue(),
								"开始节点");
						nodes.add(node);
					}
				}
			}
		} catch (Exception ex) {
			logger.error(ex.getMessage());
		}
		return nodes;
	}
	
	/**
	 * 
	 * 初始化流程定义信息：给每个任务增加一个默认的指派人员接口;在结束节点增加一个事件监听器
	 * @author yangtao
	 * @date 2011-7-28下午03:06:54
	 * @param deployId
	 */
	public void initializeProcessDefinition(String deployId){
		try{   
	        ProcessDefinitionImpl processDefinition =(ProcessDefinitionImpl)repositoryService   
	            .createProcessDefinitionQuery().deploymentId(deployId)   
	            .uniqueResult();   
	        List list = processDefinition.getActivities();   
	        for(int i=0;i<list.size();i++){   
	            ActivityImpl activityImpl = (ActivityImpl)list.get(i);   
	            String type = activityImpl.getType();  
	            if(type.equalsIgnoreCase(ActivityType.TASK.toString())){   
	                TaskActivity taskActivity = (TaskActivity)activityImpl.getActivityBehaviour(); 
	                long dbid = taskActivity.getTaskDefinition().getDbid();
	                
	                //动态增加表单
//	                taskActivity.getTaskDefinition().setFormResourceName("js/component/bpm/test.jsp");
	                
	                /**begin*****动态增加任务的指派人员接口  ***********************************/
	                UserCodeReference userCodeReference = new UserCodeReference();   
	                ObjectDescriptor descriptor = new ObjectDescriptor();   
	                //加上任务的人员动态指派   
	                descriptor.setClassName("oecp.platform.bpm.handler.DefaultTaskAssignHandler");   
	                userCodeReference.setCached(false);   
	                userCodeReference.setDescriptor(descriptor);   
	                taskActivity.getTaskDefinition().setAssignmentHandlerReference(userCodeReference);
	                /**end*****动态增加任务的指派人员接口  ***********************************/
	                
	            }  
	            if(type.equalsIgnoreCase(ActivityType.END.toString())){//给结束节点加上一个事件监听器   
	                activityImpl.createEvent(EventImpl.START).createEventListenerReference(new EndEventListener());
	            }
	            if(type.equalsIgnoreCase(ActivityType.DECISION.toString())){
	            	DecisionHandlerActivity d = (DecisionHandlerActivity)activityImpl.getActivityBehaviour();
	            	
	            	UserCodeReference decisionHandlerReference = new UserCodeReference();   
	                ObjectDescriptor descriptor = new ObjectDescriptor();   
	                //加上动态决定
	                descriptor.setClassName("oecp.platform.bpm.handler.DefaultDecisionHandler");   
	                decisionHandlerReference.setCached(false);   
	                decisionHandlerReference.setDescriptor(descriptor);
	                
	            	d.setDecisionHandlerReference(decisionHandlerReference);
	            }
	        }   
	           
	    }catch(Exception e){   
	        e.printStackTrace();  
	    }    
	}
	

	/**
	 * 根据某个流程定义，取出该流程中所有的节点
	 * 
	 * @param deployId
	 *            是否包括启动节点
	 * @return
	 */
	public List<Node> FindAllActivities(String deployId) {
		List<Node> activityList = new ArrayList();
		try{   
	        ProcessDefinitionImpl processDefinition =(ProcessDefinitionImpl)repositoryService   
	            .createProcessDefinitionQuery().deploymentId(deployId)   
	            .uniqueResult();   
	        activityList = new ArrayList<Node>();   
	        List list = processDefinition.getActivities();   
	        for(int i=0;i<list.size();i++){   
	            ActivityImpl activityImpl = (ActivityImpl)list.get(i);   
	            String type = activityImpl.getType();   
                Node node = new Node(activityImpl.getName(),type);
                activityList.add(node);   
	        }   
	    }catch(Exception e){   
	        e.printStackTrace();  
	    }    
	    return activityList;
	}
	
	public void getAllTaskActivities(String deployId) {
		try{   
	        ProcessDefinitionImpl processDefinition =(ProcessDefinitionImpl)repositoryService   
	            .createProcessDefinitionQuery().deploymentId(deployId)   
	            .uniqueResult();   
	        List<ActivityImpl> activityList = new ArrayList<ActivityImpl>();   
	        List list = processDefinition.getActivities();   
	        for(int i=0;i<list.size();i++){   
	            ActivityImpl activityImpl = (ActivityImpl)list.get(i);   
	            String type = activityImpl.getType();   
	            if(type.equalsIgnoreCase(ActivityType.TASK.toString())){   
	                TaskActivity taskActivity = (TaskActivity)activityImpl.getActivityBehaviour(); 
	                
	                //动态增加表单
	                taskActivity.getTaskDefinition().setFormResourceName("test.jsp");
	                
	                
	                /**begin*****动态增加任务的指派人员接口  ***********************************/
	                UserCodeReference userCodeReference = new UserCodeReference();   
	                ObjectDescriptor descriptor = new ObjectDescriptor();   
	                //加上任务的人员动态指派   
	                descriptor.setClassName("oecp.platform.bpm.handler.TestTaskAssignHandler");   

	                userCodeReference.setCached(false);   
	                userCodeReference.setDescriptor(descriptor);   
	                taskActivity.getTaskDefinition().setAssignmentHandlerReference(userCodeReference);
	                /**end*****动态增加任务的指派人员接口  ***********************************/
	                
	                String formResourceName = taskActivity.getTaskDefinition().getFormResourceName();
	                System.out.println(formResourceName);   
	                activityList.add(activityImpl);   
	            }   
	            if(type.equalsIgnoreCase(ActivityType.START.toString())){   
	                StartActivity activityBehaviour = (StartActivity)activityImpl.getActivityBehaviour();   
	                System.out.println("==="+activityBehaviour.getFormResourceName());               
	                activityList.add(activityImpl);   
	                
	            }   
	        }   
	        for (ActivityImpl activityImpl : activityList) {   
	            System.out.println(activityImpl.getProcessDefinition().getKey()+"----"+activityImpl.getName());        
	        }   
	        System.out.println(activityList.size());   
	           
	    }catch(Exception e){   
	        e.printStackTrace();  
	    }    

	}
	
	/**
	 * 
	 * 获得流程图中每个节点的位置的值 x y x1 y1
	 * @author yangtao
	 * @date 2011-8-11上午10:46:05
	 * @param deployId
	 */
	public Map<String,Map> getAllActivityPlaceValue(String deployId){
		Map<String,Map> map = new HashMap<String,Map>();
		try{   
	        ProcessDefinitionImpl processDefinition =(ProcessDefinitionImpl)repositoryService   
	            .createProcessDefinitionQuery().deploymentId(deployId)   
	            .uniqueResult();   
	        List list = processDefinition.getActivities();  
	        Map<String,int[]> taskMap = new HashMap<String,int[]>();
	        for(int i=0;i<list.size();i++){   
	            ActivityImpl activityImpl = (ActivityImpl)list.get(i);   
	            String type = activityImpl.getType();   
	            if(type.equalsIgnoreCase(ActivityType.TASK.toString())){   
	            	int[] value = new int[4];
	            	ActivityCoordinatesImpl aci = activityImpl.getCoordinates();
	            	value[0] = aci.getX();
	            	value[1] = aci.getY();
	            	value[2] = aci.getX()+aci.getWidth();
	            	value[3] = aci.getY()+aci.getHeight();
	            	taskMap.put(activityImpl.getName(),value); 
	            }   
	        } 
	        map.put(ActivityType.TASK.toString(), taskMap);
	        Map<String,int[]> decisionMap = new HashMap<String,int[]>();
	        for(int i=0;i<list.size();i++){   
	            ActivityImpl activityImpl = (ActivityImpl)list.get(i);   
	            String type = activityImpl.getType();   
	            if(type.equalsIgnoreCase(ActivityType.DECISION.toString())){   
	            	int[] value = new int[4];
	            	ActivityCoordinatesImpl aci = activityImpl.getCoordinates();
	            	value[0] = aci.getX();
	            	value[1] = aci.getY();
	            	value[2] = aci.getX()+aci.getWidth();
	            	value[3] = aci.getY()+aci.getHeight();
	            	decisionMap.put(activityImpl.getName(),value); 
	            }   
	        }
	        map.put(ActivityType.DECISION.toString(), decisionMap);  
	    }catch(Exception e){   
	        e.printStackTrace();  
	    }
	    return map;
	}
	
	/**end**********流程定义的方法********************************************************************/
	
	
	
	
	/**begin**********流程实例的方法********************************************************************/
	/**
	 * 取得流程实例
	 * 
	 * @param piId
	 * @return
	 */
	public ProcessInstance getProcessInstanceByPid(String piId) {
		ProcessInstance pi = executionService.createProcessInstanceQuery()
				.processInstanceId(piId).uniqueResult();
		return pi;
	}
	
	/**
	 * 
	 * 根据任务ID获得流程实例
	 * @author yangtao
	 * @date 2011-7-29上午11:05:14
	 * @param taskId
	 * @return
	 */
	public ProcessInstance getProcessInstanceByTaskId(String taskId){
		Task task = taskService.getTask(taskId);
		TaskImpl taskImpl = (TaskImpl)task;
		ProcessInstance pi = taskImpl.getProcessInstance();
//		ProcessInstance pi = executionService.createProcessInstanceQuery()
//				.processInstanceId(task.getExecutionId()).uniqueResult();
		return pi;
	}


	
	/**
	 * 启动工作流，并返回流程实例
	 * 
	 * @param pdid
	 * @param billKey 业务单据KEY
	 * @param variables
	 * @return
	 */
	public ProcessInstance startProcess(String pdid, String billKey,
			Map variables) {
		ProcessDefinition pd = repositoryService.createProcessDefinitionQuery()
				.processDefinitionId(pdid).uniqueResult();
		// 启动工作流
		ProcessInstance pi = executionService.startProcessInstanceById(pd
				.getId(), variables, billKey);


		return pi;
	}

	

	/**
	 * 显示某个流程当前任务对应的所有出口
	 * 
	 * @param piId
	 * @return
	 */
	public List<Transition> getTransitionsForSignalProcess(String piId) {
		ProcessInstance pi = executionService.findProcessInstanceById(piId);
		EnvironmentFactory environmentFactory = (EnvironmentFactory) processEngine;
		Environment env = environmentFactory.openEnvironment();
		ExecutionImpl executionImpl = (ExecutionImpl) pi;
		Activity activity = executionImpl.getActivity();
		List<Transition> list = (List<Transition>) activity.getOutgoingTransitions();
		
		return list;
	}

	/**
	 * 执行下一步的流程，对于非任务节点
	 * 
	 * @param id
	 *            processInstanceId
	 * @param transitionName
	 * @param variables
	 */
	public void signalProcess(String executionId, String transitionName,
			Map<String, Object> variables) {

		executionService.setVariables(executionId, variables);
		// executionService.signalExecutionById(id);
		executionService.signalExecutionById(executionId, transitionName);
	}

	/**
	 * 按流程实例id取得流程定义的XML
	 * 
	 * @throws IOException
	 */
	public String getProcessDefintionXMLByPiId(String piId) throws IOException {
		ProcessInstance pi = executionService.createProcessInstanceQuery()
				.processInstanceId(piId).uniqueResult();
		ProcessDefinition pd = repositoryService.createProcessDefinitionQuery()
				.processDefinitionId(pi.getProcessDefinitionId())
				.uniqueResult();
		return getDefinitionXmlByDpId(pd.getDeploymentId());
	}
	

	/**
	 * 
	 * 得到某个定义的所有的流程实例
	 * @author yangtao
	 * @date 2011-7-26上午08:31:31
	 * @return
	 */
	public List<ProcessInstance> getAllProcessInstance(String processDefinitionId,int start,int limit){
		ProcessInstanceQuery processInstanceQuery = executionService.createProcessInstanceQuery();
		List<ProcessInstance> pdList = new ArrayList();
		//流程定义集合
		if(!"".equals(processDefinitionId)){
			pdList = processInstanceQuery.processDefinitionId(processDefinitionId).page(start, start+limit).list();
		}else{
			pdList = processInstanceQuery.page(start, start+limit).list();
		}
		
		return pdList;
	}
	
	/**
	 * 
	 * 得到某个定义的所有的流程实例的个数
	 * @author yangtao
	 * @date 2011-7-26上午08:31:31
	 * @return
	 */
	public int getAllProcessInstanceCount(String processDefinitionId){
		ProcessInstanceQuery processInstanceQuery = executionService.createProcessInstanceQuery();
		List<ProcessInstance> pdList = new ArrayList();
		//流程定义集合
		if(!"".equals(processDefinitionId)){
			pdList = processInstanceQuery.processDefinitionId(processDefinitionId).list();
		}else{
			pdList = processInstanceQuery.list();
		}
		
		return pdList.size();
	}
	
	/**
	 * 结束流程实例
	 * 
	 * @param piId
	 */
	public void endProcessInstance(String piId) {
		executionService.endProcessInstance(piId, Execution.STATE_ENDED);
	}

	/**
	 * 删除流程实例
	 * 
	 * @param piId
	 */
	public void deleteProcessInstance(String piId){
		executionService.deleteProcessInstance(piId);
	}
	
	/**
	 * 
	 * 根据实例ID得到当前正在等待的节点
	 * @author yangtao
	 * @date 2011-7-26上午08:31:31
	 * @return String
	 */
	public List<String> getCurrentActivityNameByPid(String processInstanceId,String deployId){
		List resultList = new ArrayList();
		ProcessInstance pi = this.getProcessInstanceByPid(processInstanceId);
		String actName = "";//当前运行的节点名称
		if(pi!=null){//流程实例存在
			Iterator<String> activityNames = pi.findActiveActivityNames().iterator();
			while (activityNames.hasNext()) {
				actName = activityNames.next();
				resultList.add(actName);
			}
		}else{//流程实例不存在，结束了
			ProcessDefinitionImpl processDefinition =(ProcessDefinitionImpl)repositoryService   
	            .createProcessDefinitionQuery().deploymentId(deployId)
	            .uniqueResult();   
	        List list = processDefinition.getActivities();   
	        for(int i=0;i<list.size();i++){   
	            ActivityImpl activityImpl = (ActivityImpl)list.get(i);   
	            String type = activityImpl.getType();  
	            if(type.endsWith("end")){
	            	actName = activityImpl.getName();
	            	resultList.add(actName);
	            }
	        }   
		}
		
		return resultList;
	}
	/**end**********流程实例的方法********************************************************************/
	
	
	
	
	
	/**begin*******流程任务的方法*****************************************************************************/
	/**
	 * 取得某流程实例对应的任务列表
	 * 
	 * @param piId
	 * @return
	 */
	public List<Task> getTasksByPiId(String piId) {
		List<Task> taskList = taskService.createTaskQuery().processInstanceId(
				piId).list();
		return taskList;
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
		return this.taskService.getTask(taskId);
	}


	/**
	 * 通过流程实例ID和节点的名字得到相关任务
	 * @author yongtree
	 * @date 2011-7-4下午08:58:15
	 * @param piId
	 * @param actName
	 * @return
	 */
	@Override
	public Task getTaskByPiIdAndActName(String piId, String actName) {
		return taskService.createTaskQuery().processInstanceId(piId)
				.activityName(actName).uniqueResult();
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
		return this.taskService.findPersonalTasks(userName);
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
		List<Task> list = this.taskService.findGroupTasks(userName);
		return list;
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
		List<Task> list = this.taskService.createTaskQuery().candidate(userName).page(start, start+limit).list();
		return list;
	}
	/**
	 * 完成任务
	 * 
	 * @param taskId
	 * @param transitionName
	 * @param variables
	 */
	public void completeTask(String taskId, String transitionName, Map variables) {
		Task task = taskService.getTask(taskId);
		ProcessInstance pi = executionService.createProcessInstanceQuery()
				.processInstanceId(task.getExecutionId()).uniqueResult();
		// 完成任务
		taskService.setVariables(taskId, variables);
		taskService.completeTask(taskId, transitionName);

		// 为下一任务授权
		assignTask(pi, null);
	}
	
	/**
	 * 
	 * 查出某个人的所有任务
	 * @author yangtao
	 * @date 2011-7-28上午09:23:11
	 * @param userName
	 * @return
	 */
	public void completeTask(String taskId,String transitionName,String nextperson,boolean isTake,String userName){
		//下个节点的执行人，通过DefaultTaskAssignHandler任务分配接口进行赋给
		Map<String,Object> map = new HashMap<String,Object>();
		map.put(ProcessVariableName.NEXT_TASK_USER,nextperson);
		map.put(ProcessVariableName.PRE_TASKID,taskId);
		this.executionService.setVariables(this.taskService.getTask(taskId).getExecutionId(),map);
		//是否先接受任务，然后再执行
		if(isTake)
			this.takeTask(taskId, userName);
		if(StringUtils.isNotBlank(transitionName))
			this.taskService.completeTask(taskId,transitionName);
		else
			this.taskService.completeTask(taskId);
	}
	
	/**
	 * 
	 * 完成任务
	 * @author yangtao
	 * @date 2011-9-5下午04:41:11
	 * @param task
	 */
	public void completeTask(Task task){
		this.taskService.completeTask(task.getId());
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
		for(String username : userName.split(",")){
			this.taskService.assignTask(taskId, username);
		}
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
//		List<Participation> list = this.taskService.getTaskParticipations(taskId);;
		for(String username : userName.split(",")){
			this.taskService.addTaskParticipatingUser(taskId, username, Participation.CANDIDATE);
		}
	}
	/**
	 * 
	 * 某个候选人接受任务
	 * @author yangtao
	 * @date 2011-8-3上午09:27:46
	 * @param taskId
	 * @param userName
	 */
	public void takeTask(String taskId,String userName){
		this.taskService.takeTask(taskId, userName);
	}
	
	public void assignTask(ProcessInstance pi, ProcessDefinition pd) {

		if (pd == null) {
			pd = repositoryService.createProcessDefinitionQuery()
					.processDefinitionId(pi.getProcessDefinitionId())
					.uniqueResult();
		}
		// 取得当前任务的名称，然后根据该任务名称以及流程定义，查看该任务将由哪一个用户或用户组来处理比较合适
		Iterator<String> activityNames = pi.findActiveActivityNames()
				.iterator();

		while (activityNames.hasNext()) {
			String actName = activityNames.next();
			Task task = taskService.createTaskQuery().processInstanceId(
					pi.getId()).activityName(actName).uniqueResult();
			if (task != null) {// 进行任务的授权用户的操作
				// 取得对应的用户

				ProActivity actity = proActivityServcie
						.getActivityByDefAndActivityName(pd.getId(), actName);
				// TODO 将任务按照人员、岗位、角色进行分配

				// if (assign != null) {
				// if (StringUtils.isNotEmpty(assign.getUserId())) {// 用户优先处理该任务
				// taskService
				// .assignTask(task.getId(), assign.getUserId());
				// }
				// if (StringUtils.isNotEmpty(assign.getRoleId())) {//
				// 角色中的人员成为该任务的候选人员
				// taskService.addTaskParticipatingGroup(task.getId(),
				// assign.getRoleId(), Participation.CANDIDATE);
				// }
				// }
			}
		}
	}
	
	
	/**
	 * 
	 * 完成任务，根据目的节点名称建立transition并且按照这个执行路径去执行任务
	 * @author yangtao
	 * @date 2011-8-10下午04:07:58
	 * @param task
	 * @param destActivityName
	 * @param createTransitionName
	 */
	public void completeTaskByCreateTransiton(Task task, String destActivityName,String createTransitionName)throws Exception{
		//这里不会影响事物   
	    EnvironmentImpl envImpl = ((EnvironmentFactory)processEngine).openEnvironment();   
	    try {   
	    	Map<String,Object> map = new HashMap<String,Object>();
			map.put(ProcessVariableName.PRE_TASKID,task.getId());
			this.executionService.setVariables(task.getExecutionId(),map);
	        //动态回退 
	        ExecutionImpl e = (ExecutionImpl)executionService.findExecutionById(task.getExecutionId());   
	        ActivityImpl currentActivityImpl = e.getActivity();   
	           
	        ProcessDefinitionImpl processDefinitionImpl = currentActivityImpl.getProcessDefinition();   
	           
	        //生成一个transition   
	        ActivityImpl destActivityImpl = processDefinitionImpl.findActivity(destActivityName);   
	        TransitionImpl toApply = currentActivityImpl.createOutgoingTransition();   
	        toApply.setSource(currentActivityImpl);   
	        toApply.setDestination(destActivityImpl);   
	        toApply.setName(createTransitionName);   
	        this.taskService.completeTask(task.getId(),createTransitionName); 
	        //创建的transition用完之后必须删除
	        removeOutTransition(processDefinitionImpl, currentActivityImpl.getName(), destActivityName);
	    } catch (Exception e) {   
	       throw e;
	    }finally{   
	        envImpl.close();   
	    }   
	}
	/**
	 * 
	 * 驳回的时候动态创建的transition用完之后必须删除
	 * @author YangTao
	 * @date 2011-11-28上午11:42:50
	 * @param pd
	 * @param sourceName
	 * @param destName
	 * @throws Exception
	 */
	private void removeOutTransition(ProcessDefinitionImpl pd,String sourceName,String destName) throws Exception{
		try {
			ActivityImpl sourceActivity = pd.findActivity(sourceName);
			List tranList = sourceActivity.getOutgoingTransitions();
			for (int i=0;i<tranList.size();i++) {
				Transition transition = (Transition)tranList.get(i);
				if (destName.equals(transition.getDestination().getName())) {
					tranList.remove(transition);
					break;
				}
			}
		} catch (Exception e) {
			// TODO: handle exception
			throw e;
		} 
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
	public List<HistoryTask> getPersonDoneTasks(String userName,int start,int limit){
		return this.historyService.createHistoryTaskQuery().assignee(userName).page(start, start+limit).list();
	}
	
	/**
	 * 根据当前任务返回流程图中这个任务上面的所有任务节点(和历史任务走过的任务节点的交集)
	 * 
	 * @author YangTao
	 * @date 2012-1-29下午03:00:03
	 * @param currentActivityName
	 * @param deployId
	 * @param resultList
	 * @return
	 */
	public List getPreTaskByCurrentTask(String currentActivityName,String deployId,String processIntanceId,List resultList){
		String result = null;
		boolean flag = false;
		try{  
			ProcessDefinitionImpl processDefinition =(ProcessDefinitionImpl)repositoryService   
	            .createProcessDefinitionQuery().deploymentId(deployId)   
	            .uniqueResult();   
	        List<ActivityImpl> activityList = new ArrayList<ActivityImpl>();   
	        List list = processDefinition.getActivities(); 
	        List <HistoryActivityInstance> historyList = this.queryHistoryActivityInstanceByPid(processIntanceId);
	        Set historyNames = new HashSet();
	        for(HistoryActivityInstance hai : historyList){
	        	historyNames.add(hai.getActivityName());
	        }
	        for(int i=0;i<list.size();i++){   
	            ActivityImpl activityImpl = (ActivityImpl)list.get(i);   
	            String type = activityImpl.getType();   
	            if(activityImpl.getName().equals(currentActivityName)){ 
	            	List li = activityImpl.getIncomingTransitions();
	    			for(Object t : li){
	    				TransitionImpl ti = (TransitionImpl)t;
	    				ActivityImpl preActivityImpl =  ti.getSource();
	    				if(historyNames.contains(preActivityImpl.getName())){
    						if(ActivityType.TASK.toString().equalsIgnoreCase(preActivityImpl.getType())){
		    		        	result = preActivityImpl.getName();
		    		        	resultList.add(result);
		    		        	resultList = this.getPreTaskByCurrentTask(preActivityImpl.getName(), deployId,processIntanceId,resultList);
		    		        }else{
		    		        	resultList = this.getPreTaskByCurrentTask(preActivityImpl.getName(), deployId,processIntanceId,resultList);
		    		        }
    					}
	    			}
	            }
	        }
			
	    }catch(Exception e){   
	        e.printStackTrace();  
	    }   
	    return resultList;
	}

	/**
	 * 
	 * 根据当前任务得到流程图中的上一个任务名称(有弊端)
	 * @author yangtao
	 * @date 2011-8-22下午03:53:24
	 * @param task
	 * @return
	 */
	public String getPreTaskByCurrentTask(String currentActivityName,String deployId){
		String result = null;
		boolean flag = false;
		try{  
			ProcessDefinitionImpl processDefinition =(ProcessDefinitionImpl)repositoryService   
	            .createProcessDefinitionQuery().deploymentId(deployId)   
	            .uniqueResult();   
	        List<ActivityImpl> activityList = new ArrayList<ActivityImpl>();   
	        List list = processDefinition.getActivities();   
	        for(int i=0;i<list.size();i++){   
	            ActivityImpl activityImpl = (ActivityImpl)list.get(i);   
	            String type = activityImpl.getType();   
	            if(activityImpl.getName().equals(currentActivityName)){ 
	            	List li = activityImpl.getIncomingTransitions();
	    			for(Object t : li){
	    				TransitionImpl ti = (TransitionImpl)t;
	    				ActivityImpl preActivityImpl =  ti.getSource();
	    		        if(ActivityType.TASK.toString().equalsIgnoreCase(preActivityImpl.getType())){
	    		        	result = preActivityImpl.getName();
	    		        	flag = true;
	    		        	break;
	    		        }else{
	    		        	result = this.getPreTaskByCurrentTask(preActivityImpl.getName(), deployId);
	    		        	flag = true;
	    		        	break;
	    		        }
	    			}
	            }
	            if(flag)
	            	break;
	        }
			
	    }catch(Exception e){   
	        e.printStackTrace();  
	    }   
	    return result;
	}
	
	/**
	 * 
	 * 根据当前任务得到流程图中的下一个任务名称(有弊端)
	 * @author yangtao
	 * @date 2011-9-16上午08:49:54
	 * @param currentActivityName
	 * @param deployId
	 * @return
	 */
	public String getNextTaskByCurrentTask(String currentActivityName,String deployId){
		String result = null;
		boolean flag = false;
		try{  
			ProcessDefinitionImpl processDefinition =(ProcessDefinitionImpl)repositoryService   
	            .createProcessDefinitionQuery().deploymentId(deployId)   
	            .uniqueResult();   
	        List<ActivityImpl> activityList = new ArrayList<ActivityImpl>();   
	        List list = processDefinition.getActivities();   
	        for(int i=0;i<list.size();i++){   
	            ActivityImpl activityImpl = (ActivityImpl)list.get(i);   
	            String type = activityImpl.getType();   
	            if(activityImpl.getName().equals(currentActivityName)){ 
	            	List li = activityImpl.getOutgoingTransitions();
	    			for(Object t : li){
	    				TransitionImpl ti = (TransitionImpl)t;
	    				ActivityImpl nextActivityImpl =  ti.getDestination();
	    		        if(ActivityType.TASK.toString().equalsIgnoreCase(nextActivityImpl.getType())){
	    		        	result = nextActivityImpl.getName();
	    		        	flag = true;
	    		        	break;
	    		        }else{
	    		        	result = this.getNextTaskByCurrentTask(nextActivityImpl.getName(), deployId);
	    		        	flag = true;
	    		        	break;
	    		        }
	    			}
	            }
	            if(flag)
	            	break;
	        }
			
	    }catch(Exception e){   
	        e.printStackTrace();  
	    }   
	    return result;
	}
	
	
	/**
	 * 
	 * 根据当前任务得到流程图中的下一个任务名称(多个)
	 * @author yangtao
	 * @date 2011-9-16上午08:49:54
	 * @param currentActivityName
	 * @param deployId
	 * @return
	 */
	public List<String[]> getNextTasksByCurrentTask(String currentActivityName,String deployId){
		String result = null;
		boolean flag = false;
		List<String[]> resultList = new ArrayList<String[]>();
		try{  
			ProcessDefinitionImpl processDefinition =(ProcessDefinitionImpl)repositoryService   
	            .createProcessDefinitionQuery().deploymentId(deployId)   
	            .uniqueResult();   
	        List<ActivityImpl> activityList = new ArrayList<ActivityImpl>();   
	        List list = processDefinition.getActivities();   
	        for(int i=0;i<list.size();i++){   
	            ActivityImpl activityImpl = (ActivityImpl)list.get(i);   
	            String type = activityImpl.getType();   
	            if(activityImpl.getName().equals(currentActivityName)){ 
	            	List li = activityImpl.getOutgoingTransitions();
	    			for(Object t : li){
	    				TransitionImpl ti = (TransitionImpl)t;
	    				ActivityImpl nextActivityImpl =  ti.getDestination();
	    		        if(ActivityType.TASK.toString().equalsIgnoreCase(nextActivityImpl.getType())||ActivityType.DECISION.toString().equalsIgnoreCase(nextActivityImpl.getType())){
	    		        	result = nextActivityImpl.getName();
	    		        	String[] s = new String[2];
	    		        	s[0] = result;
	    		        	s[1] = ti.getName();
	    		        	resultList.add(s);
	    		        }
	    			}
	    			break;
	            }
	        }
			
	    }catch(Exception e){   
	        e.printStackTrace();  
	    }   
	    return resultList;
	}
	/**
	 * 
	 * 获得某个任务的所有侯选人
	 * @author yangtao
	 * @date 2011-8-26下午04:40:58
	 * @param tip
	 * @return
	 */
	public String getTaskCandidateUser(Task task){
		String result = "";
		TaskImpl tip = (TaskImpl)task;
		Set<ParticipationImpl> set = tip.getParticipations();
		Iterator it = set.iterator();
		while(it.hasNext()){
			result+=((ParticipationImpl)it.next()).getUserId();
			result+=",";
		}
		if(result.endsWith(","))
			result = result.substring(0,result.length()-1);
		return result;
	}
	
	/**
	 * 
	 * 根据登录账号从一个任务上删除这个候选人
	 * @author yangtao
	 * @date 2011-8-30下午03:20:15
	 * @param task
	 * @param userLoginId
	 */
	public void removeTaskCandidate(Task task,String userLoginId){
		TaskImpl tip = (TaskImpl)task;
		ParticipationImpl ppi = null;
		Set<ParticipationImpl> set = tip.getParticipations();
		Iterator it = set.iterator();
		while(it.hasNext()){
			ParticipationImpl p = (ParticipationImpl)it.next();
			if(userLoginId.equals(p.getUserId()))
				ppi = p;
		}
		tip.removeParticipant(ppi);
	}
	/**
	 * 
	 * 给某个任务创建子任务，并分配人
	 * @author yangtao
	 * @date 2011-8-29下午04:46:37
	 * @param task
	 * @param list
	 */
	public void createSubTasks(Task task,List<oecp.platform.user.eo.User> list){
		for(oecp.platform.user.eo.User user : list){
			Task subTask = ((OpenTask)task).createSubTask();
			subTask.setAssignee(user.getLoginId());
			this.taskService.addTaskParticipatingUser(task.getId(), user.getLoginId(), Participation.CLIENT);
			
		}
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
		boolean result = false;
		TaskImpl tip = (TaskImpl)task;
		ParticipationImpl ppi = null;
		Set<ParticipationImpl> set = tip.getParticipations();
		Iterator it = set.iterator();
		while(it.hasNext()){
			ParticipationImpl p = (ParticipationImpl)it.next();
			if(userLoginId.equals(p.getUserId()))
				result = true;
		}
		
		return result;
	}
	/**end*******流程任务的方法*****************************************************************************/
	
	
	/**begin*******流程历史的方法*****************************************************************************/
	/**
	 * 
	 * 根据流程ID查询出历史实例，并且按照开始时间排序
	 * @author yangtao
	 * @date 2011-7-28上午09:44:10
	 * @param processInstanceId
	 */
	public List <HistoryActivityInstance> queryHistoryActivityInstanceByPid(String processInstanceId){
		return this.historyService.createHistoryActivityInstanceQuery().processInstanceId(processInstanceId).orderAsc("startTime").list();
	}
	/**end*******流程历史的方法*****************************************************************************/

	/**
	 * 执行command命令
	 */
	public Object executeCommand(Command command){
		return this.processEngine.execute(command);
	}

	public ProcessEngine getProcessEngine() {
		return processEngine;
	}

	public void setProcessEngine(ProcessEngine processEngine) {
		this.processEngine = processEngine;
	}

	public RepositoryService getRepositoryService() {
		return repositoryService;
	}

	public void setRepositoryService(RepositoryService repositoryService) {
		this.repositoryService = repositoryService;
	}

	public ExecutionService getExecutionService() {
		return executionService;
	}

	public void setExecutionService(ExecutionService executionService) {
		this.executionService = executionService;
	}

	public TaskService getTaskService() {
		return taskService;
	}

	public void setTaskService(TaskService taskService) {
		this.taskService = taskService;
	}

	public HistoryService getHistoryService() {
		return historyService;
	}

	public void setHistoryService(HistoryService historyService) {
		this.historyService = historyService;
	}

	public ProActivityService getProActivityServcie() {
		return proActivityServcie;
	}

	public void setProActivityServcie(ProActivityService proActivityServcie) {
		this.proActivityServcie = proActivityServcie;
	}
	
	
}
