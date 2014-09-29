/*
 * Copyright (c) 2011 OECP All Rights Reserved.                	
 * <a href="http://www.oecp.cn">http://www.oecp.cn</a>                                                                 
 */    

package oecp.platform.api.bpm;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import oecp.framework.dao.QueryCondition;
import oecp.framework.exception.BizException;
import oecp.framework.util.SpringContextUtil;
import oecp.platform.base.service.PlatformBaseServiceImpl;
import oecp.platform.bcfunction.eo.Function;
import oecp.platform.bcfunction.service.BcFunctionService;
import oecp.platform.bpm.bizservice.BizServiceForBpm;
import oecp.platform.bpm.enums.ExceptionMsgType;
import oecp.platform.bpm.enums.TaskAuditDecision;
import oecp.platform.bpm.enums.VirProcessInstanceState;
import oecp.platform.bpm.eo.TaskEo;
import oecp.platform.bpm.eo.VirProDefinition;
import oecp.platform.bpm.eo.VirProcessInstance;
import oecp.platform.bpm.service.ProActivityService;
import oecp.platform.bpm.service.ProDefinitionService;
import oecp.platform.bpm.service.ProExecutionService;
import oecp.platform.user.eo.User;

import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * 工作流对外服务接口
 * @author yangtao
 * @date 2011-8-16上午11:37:01
 * @version 1.0
 */
@Service(value = "executionAPIService")
@Transactional
public class ExecutionAPIServiceImpl extends PlatformBaseServiceImpl implements ExecutionAPIService {
	
	@Resource
	private ProDefinitionService proDefinitionService;

	@Resource
	private BcFunctionService bcFunctionService;

	@Resource
	private ProActivityService proActivityService;

	@Resource
	private ProExecutionService proExecutionService;
	/**
	 * 启动工作流
	 * 
	 * @author YangTao
	 * @date 2011-10-11下午04:18:40
	 * @param funcCode 功能编号
	 * @param bizKey 业务主键
	 * @param billInfo 业务信息如业务单据号
	 * @param orgId 组织标识
	 * @param userKey 当前用户主键
	 * @param variables 参数
	 * @return
	 */
	public ExecutionResult startProcessInstance(String funcCode, String bizKey,String billInfo,String orgId,
			String userKey, String variables)throws Exception{
		ExecutionResult executionResult = new ExecutionResult();
		try {
			Map<String,Object> map = new HashMap<String,Object>();
			map.put("FormData", variables);
			Function function = this.bcFunctionService.getFunctionByCode(funcCode);
			
			VirProDefinition virdef = this.proDefinitionService.getVirProDefinition(funcCode, orgId);
			//获取单据服务接口
			String bizServiceForBpmName = function.getBizServiceForBpm();
			BizServiceForBpm bizServiceForBpm = null;
			if(StringUtils.isEmpty(bizServiceForBpmName)){
				throw new BizException(ExceptionMsgType.FUNCTION_NO_REGISTER_SERVICE);
			}else{
				//从spring中取出服务方法
				bizServiceForBpm = (BizServiceForBpm)SpringContextUtil.getBean(bizServiceForBpmName);
			}
			//如果该组织的功能上没分配流程，就直接修改单据的状态
			if (virdef == null){
				bizServiceForBpm.changeBillState(bizKey,funcCode, VirProcessInstanceState.END);
			}else{
				User creator = this.getDao().find(User.class, userKey);
				//启动虚拟流程定义
				this.proExecutionService.startVirProcess(creator, virdef.getId(), bizKey, billInfo, map, false);
				//修改单据状态
				bizServiceForBpm.changeBillState(bizKey,funcCode, VirProcessInstanceState.RUNNING);
				//返回启动成功的信息
				executionResult.setProcessInstanceState(ProcessInstanceState.CREATED);
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			throw e;
		}
		
 		return executionResult;
	}
	
	/**
	 * 提交一个任务
	 * 
	 * @author YangTao
	 * @date 2011-10-11下午04:16:39
	 * @param funcCode 功能编号
	 * @param bizKey 业务主键
	 * @param billInfo 业务信息如业务单据号
	 * @param userLoginId 当前用户的登录账号
	 * @param auditOpinion 审批意见
	 * @param decision 审批决定：同意、不同意、驳回、委派
	 * @param transitionName 当前任务向下走的路线
	 * @return
	 */
	public ExecutionResult completeTask(String funcCode, String bizKey,String billInfo,
			String userLoginId, String auditOpinion, TaskAuditDecision decision,String preTaskName,String transitionName)throws Exception{
		ExecutionResult executionResult = new ExecutionResult();
		try {
			//根据流程实例获得当前正在等待执行的任务
			TaskEo taskEo = this.proActivityService.getCrrentTaskByBizKey(bizKey);
			//执行当前任务
			this.proActivityService.completeTask(taskEo.getTaskId(), "", auditOpinion, decision.toString(), true, userLoginId,preTaskName,transitionName);
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}
		//返回执行成功的信息
		executionResult.setTaskState(TaskState.COMPLETED);
 		return executionResult;
	}
	
	
	/**
	 * 判断某个人是否有审核该单据的权限
	 * 
	 * @author YangTao
	 * @date 2011-10-11下午04:21:19
	 * @param funcCode 功能编号
	 * @param bizKey 业务主键
	 * @param billInfo 业务信息如业务单据号
	 * @param userLoginId 当前用户的登录账号
	 * @return
	 */
	public Object[] isAudit(String funcCode,String bizKey,String billInfo,String userLoginId)throws Exception{
		Object[] object= new Object[2];
		try {
			//根据流程实例获得当前正在等待执行的任务
			TaskEo taskEo = this.proActivityService.getCrrentTaskByBizKey(bizKey);
			boolean b = this.proActivityService.isTaskCandidate(taskEo, userLoginId);
			object[0] = b;
			object[1] = taskEo.getCounterSignRule().toString();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			throw e;
		}
		
		return object;
	}
	
	/**
	 * 
	 * 是否是第一个节点
	 * @author yangtao
	 * @date 2011-8-26下午03:33:07
	 * @param billKey
	 * @return
	 */
	public boolean isTheFirstNode(String billKey)throws BizException{
		boolean result = false;
		//获取流程实例
		VirProcessInstance vpi = this.proActivityService.getVirProcessInstanceByBizKey(billKey);
		//流程实例已经结束时，抛异常
		if(vpi.getVirProcessInstanceState()!=VirProcessInstanceState.RUNNING){
			throw new BizException(ExceptionMsgType.PROCESS_INTANCE_IS_END);
		}
		String currentActivityName = (String)this.proExecutionService.getCurrentActivityNameByPid(vpi.getProcessInstanceId(),vpi.getVirProDefinition().getProDefinition().getDeployId()).get(0);
		String preActivityName = this.proActivityService.getPreTaskByCurrentTask(currentActivityName, vpi.getVirProDefinition().getProDefinition().getDeployId());
		if(!StringUtils.isEmpty(preActivityName))
			result = false;
		else
			result = true;
		
		return result;
	}
	
	/**
	 * 
	 * 根据单据主键判断该张单据是否走了流程
	 * @author YangTao
	 * @date 2011-12-22上午11:58:50
	 * @param billKey
	 * @return
	 */
	public boolean billIsInProcess(String billKey){
		boolean result = false;
		List<QueryCondition> conditions = new ArrayList<QueryCondition>();
		QueryCondition qc = new QueryCondition("o.billKey","=",billKey);
		conditions.add(qc);
		List<VirProcessInstance> list = this.proExecutionService.getVirProcessInstanceByConditions(conditions);
		if(list.size()!=0){
			result = true;
		}
		return result;
	}
	
}
