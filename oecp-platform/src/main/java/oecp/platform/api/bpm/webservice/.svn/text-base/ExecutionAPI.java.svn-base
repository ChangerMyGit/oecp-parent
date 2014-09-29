/*
 * Copyright (c) 2011 OECP All Rights Reserved.                	
 * http://www.oecp.cn                                                                 
 */

package oecp.platform.api.bpm.webservice;

import oecp.platform.api.bpm.ExecutionResult;

/**
 * 工作流服务
 * 
 * @author yongtree
 * @date 2011-4-12 下午04:37:03
 * @version 1.0
 */
//@TODO 工作流服务需要根据现有的架构做出相应的改变
public interface ExecutionAPI {

	/**
	 * 开启一个流程
	 * 
	 * @author yongtree
	 * @date 2011-4-13 上午09:38:04
	 * @param funcKey
	 *            功能编号，流程定义的key将和功能key保持一致
	 * @param BizKey
	 *            业务主键
	 * @param billInfo
	 *            单据号，比如订单编号：ORDER0000001         
	 * @param userKey
	 *            能够唯一标示用户的Key，通常为用户表的主键，或者用户的登陆名
	 * @param variables
	 *            变量数据，一般为相应的单据数据。格式为一个XML文档，结构与相应的XSD对应。
	 * @param orgId
	 *   			组织ID
	 * @return
	 */
	public ExecutionResult startProcessInstance(String funcKey, String bizKey,String billInfo,String orgId,
			String userKey, String variables);

	/**
	 * 提交一个任务
	 * 
	 * @author yongtree
	 * @date 2011-4-13 下午04:04:52
	 * @param funcKey
	 *            功能编号，流程定义的key将和功能key保持一致
	 * @param BizKey
	 *            业务主键
	 * @param billInfo
	 *            单据号，比如订单编号：ORDER0000001   
	 * @param userKey
	 *            能够唯一标示用户的Key，通常为用户表的主键，或者用户的登陆名
	 * @param variables
	 *            变量数据，一般为相应的单据数据。格式为一个XML文档，结构与相应的XSD对应。
	 * @param message
	 *            任务注释或者审核意见
	 * @return
	 */
	public ExecutionResult completeTask(String funcKey, String bizKey,String billInfo,
			String userKey, String variables, String message);

	/**
	 * 退回任务，向上一个任务提交者退回。
	 * 
	 * @author yongtree
	 * @date 2011-4-14 上午11:46:17
	 * @param funcKey
	 *            功能编号，流程定义的key将和功能key保持一致
	 * @param BizKey
	 *            业务主键
	 * @param billInfo
	 *            单据号，比如订单编号：ORDER0000001   
	 * @param userKey
	 *            能够唯一标示用户的Key，通常为用户表的主键，或者用户的登陆名
	 * @param clearHist
	 *            是否清除历史轨迹，默认为false
	 * @param message
	 * @return
	 */
	public ExecutionResult rollbackTask(String funcKey, String bizKey,String billInfo,
			String userKey, boolean clearHist, String message);

	/**
	 * 取回任务 <br>
	 * 当下一执行人办理后，则任务无法取回
	 * 
	 * @author yongtree
	 * @date 2011-4-14 下午12:02:47
	 * @param funcKey
	 *            功能编号，流程定义的key将和功能key保持一致
	 * @param BizKey
	 *            业务主键
	 * @param billInfo
	 *            单据号，比如订单编号：ORDER0000001   
	 * @param userKey
	 *            能够唯一标示用户的Key，通常为用户表的主键，或者用户的登陆名
	 * @return
	 */
	public ExecutionResult withdrawTask(String funcKey, String bizKey,String billInfo,
			String userKey);

}
