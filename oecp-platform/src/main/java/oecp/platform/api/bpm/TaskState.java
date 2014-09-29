/*
 * Copyright (c) 2011 OECP All Rights Reserved.                	
 * http://www.oecp.cn                                                                 
 */

package oecp.platform.api.bpm;

import oecp.framework.util.enums.EnumDescription;

/**
 * 任务状态
 * 
 * @author yongtree
 * @date 2011-4-14 上午11:04:21
 * @version 1.0
 */
public enum TaskState {
	@EnumDescription("未发现任务")
	NOT_FOUND, 
	@EnumDescription("任务被打开")
	OPEN, 
	@EnumDescription("任务完成")
	COMPLETED, 
	@EnumDescription("任务挂起")
	SUSPENDED, 
	@EnumDescription("后继接收者已办理")
	NEXT_DONE
	// 下一个任务已经办理，主要用在收回操作上
}
