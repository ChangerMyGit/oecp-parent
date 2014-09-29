/*
 * Copyright (c) 2011 OECP All Rights Reserved.                	
 * <a href="http://www.oecp.cn">http://www.oecp.cn</a>                                                                 
 */

package oecp.platform.bpm.service;

import java.io.File;
import java.util.List;
import java.util.Map;

import oecp.framework.dao.QueryCondition;
import oecp.framework.dao.QueryResult;
import oecp.framework.exception.BizException;
import oecp.framework.service.BaseService;
import oecp.platform.bpm.eo.ProDefinition;
import oecp.platform.bpm.eo.VirProActivity;
import oecp.platform.bpm.eo.VirProDefinition;
import oecp.platform.bpm.vo.Node;
import oecp.platform.bpm.vo.ProDefinitionInfo;
import oecp.platform.bpm.vo.VirProcessDefinitionInfo;
import oecp.platform.org.eo.Organization;
import oecp.platform.user.eo.User;

import org.jbpm.api.ProcessDefinition;

/**
 * 业务流程定义
 * 
 * @author yongtree
 * @date 2011-6-16 下午02:29:00
 * @version 1.0
 */
public interface ProDefinitionService extends BaseService<ProDefinition> {

	/**
	 * 获取流程定义列表
	 * @author luanyoub
	 * @date 2014年1月9日上午9:21:02
	 * @param org
	 * @param funcId
	 * @return
	 */
	public List<ProDefinitionInfo> getProDefinitions(Organization org,String funcId);
	
	/**
	 * 
	 * 根据流程定义名称获取流程
	 * @author yangtao
	 * @date 2011-9-14上午10:44:03
	 * @param processName
	 * @return
	 */
	public QueryResult<ProDefinitionInfo> getProDefinitions(List<QueryCondition> conditions,Organization org,int start,int limit); 
	/**
	 * 得到分配的流程
	 * @author yongtree
	 * @date 2011-7-4上午11:36:51
	 * @param org
	 * @param funcId
	 * @return
	 */
	public ProDefinition getDistriProDefinition(Organization org, Long funcId);
	
	/**
	 * 
	 * 根据jbpm4中的流程定义得到封装的流程定义
	 * @author yangtao
	 * @date 2011-7-27上午10:44:53
	 * @param pd
	 * @return
	 */
	public ProDefinition getProDefinition(ProcessDefinition pd);
	
	/**
	 * 发布新流程
	 * @author yongtree
	 * @date 2011-6-29 下午04:08:06
	 * @param orgIds
	 * @param proDefinition
	 * @param zip
	 * @throws BizException 
	 */
	public void deploy(String orgIds,ProDefinition proDefinition, File zip) throws BizException;
	
	/**
	 * 删除流程
	 * @author yangtao
	 * @date 2011-7-25 下午04:08:06
	 * @param deployId
	 * @param proDefinitionId
	 * @throws BizException 
	 */
	public void delete(String deployId,String proDefinitionId,Organization org)throws Exception;
	
	/**
	 * 根据某个流程定义，取出该流程中所有的任务节点和开始节点
	 * 
	 * @param deployId
	 *            是否包括启动节点
	 * @return
	 */
	public List<Node> FindAllActivities(String deployId);
	
	/**
	 * 
	 * 根据虚拟流程ID获得所有虚拟任务节点
	 * @author yangtao
	 * @date 2011-8-2上午08:49:47
	 * @param virProcessDefId
	 * @return
	 */
	public List<VirProActivity> FindAllVirTaskActivities(String virProcessDefId);
	
	/**
	 * 
	 * 增加虚拟流程
	 * @author yangtao
	 * @date 2011-8-1下午03:37:27
	 * @param processDefinitionId
	 * @param orgIds
	 */
	public void addVirProcessDefinition(String processDefinitionId,String orgIds)throws Exception;
	
	/**
	 * 
	 * 虚拟流程列表
	 * @author yangtao
	 * @date 2011-8-1下午03:47:11
	 * @param condition
	 * @param start
	 * @param limit
	 * @return
	 */
	public QueryResult<VirProcessDefinitionInfo> queryVirProcessDefinition(String[] params,int start,int limit,Organization og);
	
	/**
	 * 
	 * 删除虚拟流程
	 * @author yangtao
	 * @date 2011-8-2上午08:56:31
	 */
	public void deleteVirProcessDefinition(String virProcessDefIds)throws Exception ;
	
	/**
	 * 
	 * 给虚拟节点进行分配
	 * @author yangtao
	 * @date 2011-8-2下午02:54:04
	 * @param virProActivityId
	 * @param virProDefinitionId
	 * @param assignFlag
	 * @param ids
	 * @return
	 */
	public String assignVirProActivity(String virProActivityId,String virProDefinitionId,String counterSignRule,String passRate,String assignFlag,String ids,Organization org,String[] config);

	/**
	 * 
	 * 获得流程图中每个节点的位置的值 x y x1 y1
	 * @author yangtao
	 * @date 2011-8-11上午10:46:05
	 * @param deployId
	 */
	public Map<String,Map> getAllActivityPlaceValue(String deployId);
	
		
	/**
	 * 
	 * 根据条件获取VirProDefinition
	 * @author yangtao
	 * @date 2011-8-16下午12:01:41
	 * @param conditions
	 * @return
	 */
	public List<VirProDefinition> getVirProDefinitionByConditions(List<QueryCondition> conditions);
	
	/**
	 * 
	 * 获取虚拟节点分配人员 角色 岗位的情况
	 * @author yangtao
	 * @date 2011-8-16上午10:08:53
	 * @param virProActivityId
	 * @param virProDefinitionId
	 * @return
	 */
	public String[] getVirProActivityAssign(String virProActivityId,String virProDefinitionId);
	
	/**
	 * 
	 * 得到某个条件节点配置的信息
	 * @author yangtao
	 * @date 2011-8-26下午02:52:37
	 * @param virProActivityId
	 * @param virProDefinitionId
	 * @return
	 */
	public String[] getDecisionConditionAssign(String virProActivityId,String virProDefinitionId);
	
	/**
	 * 
	 * 给decision节点配置条件信息
	 * @author yangtao
	 * @date 2011-8-26下午02:19:44
	 * @param virProActivityId
	 * @param virProDefinitionId
	 * @param paramName
	 * @param borderValue
	 */
	public void assignDecisionCondition(String virProActivityId,String virProDefinitionId,String paramName,String borderValue);

	/**
	 * 
	 * 启用流程
	 * @author YangTao
	 * @date 2011-10-19上午11:39:55
	 * @param user
	 * @param virProDefinitionId
	 */
	public void useVirProcessDefinition(User user,String virProDefinitionId,String isUseId)throws Exception;

	/**
	 * 
	 * 获取条件节点上配置的GROOVY脚本
	 * @author YangTao
	 * @date 2012-1-30上午09:20:55
	 * @param virProActivityId
	 * @param virProDefinitionId
	 * @return
	 */
	public String getDecisionConditionScript(String virProActivityId,String virProDefinitionId);
	/**
	 * 根据功能编号和组织获取已经启用的流程定义
	 * 
	 * @author YangTao
	 * @date 2012-2-9上午10:55:29
	 * @param functionCode
	 * @param orgId
	 * @return
	 * @throws Exception
	 */
	public VirProDefinition getVirProDefinition(String functionCode,String orgId)throws Exception;

	
	/**
	 * 获取某个虚拟流程定义上面的所分配的所有人员
	 * 
	 * @author YangTao
	 * @date 2012-2-24下午04:39:49
	 * @param virdef
	 * @return
	 */
	public List<User> getUsersOnVirProDefinition(VirProDefinition virdef);
}
