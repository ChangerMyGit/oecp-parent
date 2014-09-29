/*
 * Copyright (c) 2011 OECP All Rights Reserved.                	
 * <a href="http://www.oecp.cn">http://www.oecp.cn</a>                                                                 
 */    

package oecp.platform.bpm.service;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import oecp.framework.dao.QueryCondition;
import oecp.framework.dao.QueryResult;
import oecp.framework.exception.BizException;
import oecp.platform.base.service.PlatformBaseServiceImpl;
import oecp.platform.bpm.enums.ProcessVariableName;
import oecp.platform.bpm.eo.BizConfigEo;
import oecp.platform.bpm.eo.BizProActivity;
import oecp.platform.bpm.eo.BizProDefinition;
import oecp.platform.bpm.vo.Node;

import org.jbpm.api.ProcessDefinition;
import org.jbpm.api.ProcessInstance;
import org.jbpm.api.model.OpenExecution;
import org.jbpm.api.task.Task;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 *
 * @author yangtao
 * @date 2011-9-5上午10:36:07
 * @version 1.0
 */
@Service(value = "bizProDefinitionService")
@Transactional
public class BizProDefinitionServiceImpl  extends
	PlatformBaseServiceImpl<BizProDefinition>  implements BizProDefinitionService {

	@Resource
	private JbpmService jbpmService;
	
	@Resource
	private ProActivityService proActivityServcie;
	
	/**
	 * 
	 * 部署业务流程
	 * @author yangtao
	 * @date 2011-9-5上午10:35:36
	 * @param bizProDefinition
	 * @param zip
	 * @throws BizException
	 */
	@Override
	public void deploy(BizProDefinition bizProDefinition, File zip)
			throws BizException {
		// TODO Auto-generated method stub
		//jbpm4中保存流程定义
		String deployId = jbpmService.deploy(zip);

		ProcessDefinition pd = jbpmService
				.getProcessDefinitionByDeployId(deployId);
		
		List<Node> nodes = this.jbpmService.FindAllActivities(deployId);
		List<BizProActivity> list = new ArrayList<BizProActivity>();
		for(Node node : nodes){
			BizProActivity bp = new BizProActivity();
			bp.setActivityName(node.getName());
			bp.setBizProDefinition(bizProDefinition);
			list.add(bp);
		}
		
		bizProDefinition.setBizProActivities(list);
		bizProDefinition.setDeployId(deployId);
		bizProDefinition.setProDefId(pd.getId());
		bizProDefinition.setVersion(pd.getVersion());
		this.save(bizProDefinition);
	}
	
	/**
	 * 
	 * 业务流程列表
	 * @author yangtao
	 * @date 2011-9-5上午11:04:49
	 * @param list
	 * @param start
	 * @param limit
	 * @return
	 */
	public QueryResult<BizProDefinition> list(List<QueryCondition> conditions,int start,int limit){
		return this.getDao().getScrollData(BizProDefinition.class, start, limit, conditions, null);
	}
	
	/**
	 * 
	 * 获取某个业务流程下面的所有的节点
	 * @author yangtao
	 * @date 2011-9-5下午02:14:00
	 * @param bizProDefinitionId
	 * @return
	 */
	public List<BizProActivity> getBizProActivities(String bizProDefinitionId){
		return this.getDao().queryByWhere(BizProActivity.class, "bizProDefinition.id=?", new Object[]{bizProDefinitionId});
	}
	
	/**
	 * 
	 * 获得某个业务流程节点的配置信息
	 * @author yangtao
	 * @date 2011-9-5下午02:22:02
	 * @param bizProActivityId
	 * @return
	 */
	public String[] getBizConfigEoByBizProActivityId(String bizProActivityId){
		String[] result = new String[2];
		List<BizConfigEo> list = this.getDao().queryByWhere(BizConfigEo.class, "bizProActivity.id=?", new Object[]{bizProActivityId});
		if(list.size()!=0)
			result[0] = list.get(0).getWebServiceURL();
		return result;
	}
	
	/**
	 * 
	 * 保存配置信息
	 * @author yangtao
	 * @date 2011-9-5下午03:00:42
	 * @param bizProActivityId
	 * @param webServiceUrl
	 */
	public void saveConfigInfo(String bizProActivityId,String webServiceUrl){
		BizProActivity bizProActivity = this.getDao().find(BizProActivity.class, bizProActivityId);
		BizConfigEo bizConfigEo =  new BizConfigEo();
		bizConfigEo.setBizProActivity(bizProActivity);
		bizConfigEo.setWebServiceURL(webServiceUrl);
		this.getDao().create(bizConfigEo);
	}
	
	/**
	 * 
	 * 启动流程
	 * @author yangtao
	 * @date 2011-9-5下午03:49:00
	 * @param proDefId
	 */
	public void start(String proDefId){
		String billKey = System.currentTimeMillis()+"";
		Map variables = new HashMap();
		BizProDefinition bpd = this.getDao().queryByWhere(BizProDefinition.class, "proDefId=?", new Object[]{proDefId}).get(0);
		variables.put(ProcessVariableName.BIZ_PRO_DEFINITION, bpd);
		ProcessInstance pi = this.jbpmService.startProcess(proDefId, billKey, variables);
		this.completeTask(pi);
	}
	
	//循环把任务都执行完毕
	private void completeTask(ProcessInstance pi){
		Task task = null;
		List<Task> list = this.proActivityServcie.getTasksByPiId(pi.getId());
		
		if(list.size()!=0){
			task = list.get(0);
		}
		if(task==null)
			return;
		//begin执行后台配置的信息
		this.executeInterface(pi, task);	
		//end执行后台配置的信息
		this.jbpmService.completeTask(task);
		completeTask(pi);
	}
	
	//执行每个任务节点配置的接口
	private void executeInterface(ProcessInstance pi,Task task){
		String result = "";
		OpenExecution execution = (OpenExecution)pi;
		BizProDefinition bpd = (BizProDefinition)execution.getVariables().get(ProcessVariableName.BIZ_PRO_DEFINITION);
		List<BizConfigEo> list = this.getDao().queryByWhere(BizConfigEo.class, "bizProActivity.bizProDefinition.id=? and bizProActivity.activityName=?", new Object[]{bpd.getId(),task.getActivityName()});
		if(list.size()!=0)
			result = list.get(0).getWebServiceURL();
		
	}
}
