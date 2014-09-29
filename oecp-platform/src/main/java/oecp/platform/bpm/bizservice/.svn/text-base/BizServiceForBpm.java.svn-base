/*
 * Copyright (c) 2011 OECP All Rights Reserved.                	
 * <a href="http://www.oecp.cn">http://www.oecp.cn</a>                                                                 
 */    

package oecp.platform.bpm.bizservice;

import java.io.Serializable;

import oecp.platform.bpm.enums.VirProcessInstanceState;

/**
 * bpm中调用业务单据的接口
 * @author yangtao
 * @date 2011-9-27上午09:11:27
 * @version 1.0
 */
public interface BizServiceForBpm {
	/**
	 * 
	 * 流程中，修改单据的状态
	 * @author yangtao
	 * @date 2011-9-27上午09:11:34
	 * @param bizPK
	 * @param bpmstate
	 */
	public void changeBillState(Serializable bizPK,String functionCode,VirProcessInstanceState bpmstate )throws Exception;
}
