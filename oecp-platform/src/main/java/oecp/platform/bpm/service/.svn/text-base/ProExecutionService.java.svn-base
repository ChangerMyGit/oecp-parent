/*
 * Copyright (c) 2011 OECP All Rights Reserved.                	
 * <a href="http://www.oecp.cn">OECP</a> 
 */

package oecp.platform.bpm.service;


import java.io.IOException;
import java.util.List;
import java.util.Map;

import oecp.framework.dao.QueryCondition;
import oecp.framework.dao.QueryResult;
import oecp.framework.service.BaseService;
import oecp.platform.bpm.eo.ProDefinition;
import oecp.platform.bpm.eo.VirProDefinition;
import oecp.platform.bpm.eo.VirProcessInstance;
import oecp.platform.bpm.vo.HistoryTaskInfo;
import oecp.platform.bpm.vo.ProcessInstanceInfo;
import oecp.platform.org.eo.Organization;
import oecp.platform.user.eo.User;

import org.jbpm.api.ProcessDefinition;
import org.jbpm.api.ProcessInstance;

/**
 * 流程的执行服务，处理流程的发起
 * 
 * @author yongtree
 * @date 2011-7-4 上午09:52:30
 * @version 1.0
 * 
 */
public interface ProExecutionService extends BaseService {

	
	public ProExecutionResult startVirProcess(String functionCode,Organization org, User creator, String billKey,String billInfo,Map<String, Object> variables) throws Exception;
	/**
	 * 启动工作流，并返回流程实例
	 * 
	 * @param pdid
	 * @param billKey 业务单据KEY
	 * @param variables
	 * @return
	 */
	public ProcessInstance startProcess(String pdid, String billKey,Map<String, Object> variables);
	
	/**
	 * 
	 * 启动虚拟流程
	 * @author yangtao
	 * @date 2011-8-2下午03:47:25
	 * @param userLoginId
	 * @param virProDefinitionId
	 * @param billKey
	 * @param variables
	 * @return
	 */
	public ProcessInstance startVirProcess(User creator,String virProDefinitionId, String billKey,String billInfo,Map<String, Object> variables,boolean isAdminStart)throws Exception;
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
	 * 根据流程实例ID得到封装的流程定义
	 * @author yangtao
	 * @date 2011-7-27上午10:42:52
	 * @param instanceId
	 * @return
	 */
	public ProDefinition getProDefinitionByInstanceId(String instanceId);
	
	/**
	 * 结束流程实例
	 * 
	 * @param piId
	 */
	public void endProcessInstance(String piId,boolean isPersonDo)throws Exception;
	
	/**
	 * 删除流程实例
	 * 
	 * @param piId
	 */
	public void deleteProcessInstance(String piId);
	
	/**
	 * 按发布id取得流程定义的PNG图片
	 * 
	 * @throws IOException
	 */
	public byte[] getDefinitionPngByDpId(String deployId) throws IOException;
	
	/**
	 * 按发布id取得流程定义的XML
	 * 
	 * @throws IOException
	 */
	public String getDefinitionXmlByDpId(String deployId) throws IOException;
	
	/**
	 * 取得流程实例
	 * 
	 * @param piId
	 * @return
	 */
	public ProcessInstance getProcessInstanceByPid(String piId);
	
	/**
	 * 
	 * 根据流程实例ID查询出历史
	 * @author yangtao
	 * @date 2011-7-28上午09:49:02
	 * @param billKey
	 * @return
	 */
	public List<HistoryTaskInfo> queryHistoryByBillKey(String billKey);
	
	/**
	 * 
	 * 查询出所有的流程实例
	 * @author yangtao
	 * @date 2011-7-28上午10:05:10
	 * @return
	 */
	public QueryResult<ProcessInstanceInfo> queryProcessInstance(List<QueryCondition> conditions,int start,int limit);
	
	/**
	 * 
	 * 根据实例ID得到当前正在等待的节点
	 * @author yangtao
	 * @date 2011-7-26上午08:31:31
	 * @return String
	 */
	public List<String> getCurrentActivityNameByPid(String processInstanceId,String deployId);
	
	
	/**
	 * 
	 * 根据查询条件获得VirProcessInstance
	 * @author yangtao
	 * @date 2011-8-16上午11:59:14
	 * @param conditions
	 * @return
	 */
	public List<VirProcessInstance> getVirProcessInstanceByConditions(List<QueryCondition> conditions);

	public final String OWNER="_owner";
	public final String HOLDER="_holder";
	
}
