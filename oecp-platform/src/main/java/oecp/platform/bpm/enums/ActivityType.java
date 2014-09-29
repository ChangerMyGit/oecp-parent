/*
 * Copyright (c) 2011 OECP All Rights Reserved.                	
 * <a href="http://www.oecp.cn">http://www.oecp.cn</a>                                                                 
 */    

package oecp.platform.bpm.enums;

import oecp.framework.util.enums.EnumDescription;

/**
 * 流程节点类型
 * @author yangtao
 * @date 2011-8-18上午10:59:15
 * @version 1.0
 */
public enum ActivityType {
	@EnumDescription("开始节点")
	START,
	@EnumDescription("任务节点")
	TASK,
	@EnumDescription("条件节点")
	DECISION,
	@EnumDescription("结束节点")
	END
}
