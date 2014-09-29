/*
 * Copyright (c) 2011 OECP All Rights Reserved.                	
 * <a href="http://www.oecp.cn">http://www.oecp.cn</a>                                                                 
 */    

package oecp.platform.bpm.enums;

/**
 * 异常含义
 * @author yangtao
 * @date 2011-8-19上午11:58:03
 * @version 1.0
 */
public class ExceptionMsgType {
	//默认异常返回信息
	public static final String EXECUTE_FAILURE = "执行失败";
	//给某个功能发布工作流时
	public static final String NOT_CREATE_PROCESS_TO_FUNCTION = "无法给该功能创建审批流！";
	//删除某个功能上的流程定义时
	public static final String DELETE_PROCESS_DEFINITION_FAILURE = "删除失败，该流程已经有实例、任务在运行";
	//删除某个功能上的流程定义时
	public static final String DELETE_PROCESS_DEFINITION_YOURSELF_CREATED = "删除流程定义时，只能删除你们组织发布的流程定义";
	//删除某个功能上的流程定义时
	public static final String DELETE_PROCESS_ASSIGNED = "流程定义已经分配给某个组织";
	//取回任务时
	public static final String NEXT_TASK_COMPLETE = "下个任务已经完成，无法取回任务";
	//会签任务，已经执行到下个任务时，无法取回的
	public static final String COUNTERSIGN_NO_BACK = "会签任务无法取回";
	//退回任务时
	public static final String NO_PRETASK = "没有上一个任务，无法驳回";
	//没找到当前任务
	public static final String NO_CURRENT_TASK = "没找到当前任务，无法执行";
	//给组织分配流程定义时
	public static final String ONE_FUNCTION_ONE_ORG = "某个功能上的一个流程定义只能分配给一个组织";
	//在这个任务上你不是候选人
	public static final String YOU_ISNOT_CANDIDATE = "在这个任务上你不是候选人，请联系管理员！";
	//给任务指派人员时
	public static final String NEXT_TASK_NO_CANDIDATE = "下一个任务没有找到候选人，请联系管理员！";
	//流程实例结束
	public static final String PROCESS_INTANCE_IS_END = "该流程已经结束";
	//流程结束时回写单据状态时
	public static final String FUNCTION_NO_REGISTER_SERVICE = "该功能上没有注册业务服务名称";
	//启动流程时
	public static final String NO_RIGHT_START_PROCESS = "你无权启动此流程";
	//启动流程时
	public static final String HAVE_NO_FUNCTION = "该功能不存在";
	//启动流程时
	public static final String NO_PROCESS_ON_FUNCTION = "该功能没支持审批流";
	//启动流程时
	public static final String NO_ASSIGN_PROCESS_ON_FUNCTION = "该功能没有分配流程";
	//流程实例被强制删除时
	public static final String NO_PROCESS_INSTANCE = "流程实例不存在";
}
