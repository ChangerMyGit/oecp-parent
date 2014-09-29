/*
 * Copyright (c) 2011 OECP All Rights Reserved.                	
 * <a href="http://www.oecp.cn">http://www.oecp.cn</a>                                                                 
 */    

package oecp.platform.api.bpm;

import oecp.framework.exception.BizException;
import oecp.platform.bpm.enums.TaskAuditDecision;


/**
 *
 * @author yangtao
 * @date 2011-8-16上午11:36:26
 * @version 1.0
 */
public interface ExecutionAPIService {
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
			String userKey, String variables)throws Exception;
	
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
	 * @return
	 */
	public ExecutionResult completeTask(String funcCode, String bizKey,String billInfo,
			String userLoginId, String auditOpinion, TaskAuditDecision decision,String preTaskName,String transitionName)throws Exception;

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
	public Object[] isAudit(String funcCode,String bizKey,String billInfo,String userLoginId)throws Exception;


	/**
	 * 
	 * 是否是第一个节点(驳回到第一个节点时可以编辑单据)
	 * @author yangtao
	 * @date 2011-8-26下午03:33:07
	 * @param billKey
	 * @return
	 */
	public boolean isTheFirstNode(String billKey)throws BizException;
	
	/**
	 * 
	 * 根据单据主键判断该张单据是否走了流程
	 * @author YangTao
	 * @date 2011-12-22上午11:58:50
	 * @param billKey
	 * @return
	 */
	public boolean billIsInProcess(String billKey);
}
