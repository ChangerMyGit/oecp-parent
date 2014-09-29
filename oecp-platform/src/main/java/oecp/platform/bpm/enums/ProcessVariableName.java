/*
 * Copyright (c) 2011 OECP All Rights Reserved.                	
 * <a href="http://www.oecp.cn">http://www.oecp.cn</a>                                                                 
 */    

package oecp.platform.bpm.enums;

/**
 * 放置在流程实例变量Map里面的变量的名称
 * @author yangtao
 * @date 2011-8-23上午10:30:49
 * @version 1.0
 */
public class ProcessVariableName {
	//流程的启动人
	public static final String CREATOR = "creator";
	//虚拟流程定义ID
	public static final String VIR_PRO_DEFINITION_ID = "virProDefinitionId";
	//虚拟流程实例
	public static final String VIR_PRO_INSTANCE = "virProcessInstance";
	//业务主键
	public static final String BUSINESS_KEY = "billKey";
	//条件名称
	public static final String DECISION_NAME = "decisonName";
	//前一个任务的ID
	public static final String PRE_TASKID = "preTaskId";
	//下一个任务的人员
	public static final String NEXT_TASK_USER = "nextTaskUserIds";
	
	/***业务流程**********************************/
	//业务流程定义
	public static final String BIZ_PRO_DEFINITION = "bizProDefinition";
}
