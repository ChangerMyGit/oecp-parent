/*
 * Copyright (c) 2011 OECP All Rights Reserved.                	
 * <a href="http://www.oecp.cn">http://www.oecp.cn</a>                                                                 
 */    

package oecp.platform.bpm.enums;

import oecp.framework.util.enums.EnumDescription;

/**
 * 历史任务节点的状态
 * @author yangtao
 * @date 2011-8-12上午09:45:50
 * @version 1.0
 */
public enum TaskHistoryState {
	@EnumDescription("正在办理中")
	IS_DOING,
	@EnumDescription("办理完毕")
	IS_DONE
	
}
