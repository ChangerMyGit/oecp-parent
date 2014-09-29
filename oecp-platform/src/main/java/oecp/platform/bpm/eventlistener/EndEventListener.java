/*
 * Copyright (c) 2011 OECP All Rights Reserved.                	
 * <a href="http://www.oecp.cn">http://www.oecp.cn</a>                                                                 
 */    

package oecp.platform.bpm.eventlistener;

import oecp.framework.util.SpringContextUtil;
import oecp.platform.bpm.service.ProExecutionService;

import org.jbpm.api.listener.EventListener;
import org.jbpm.api.listener.EventListenerExecution;

/**
 * 进入结束节点时触发 更新虚拟流程实例的状态
 * @author yangtao
 * @date 2011-8-11下午03:42:15
 * @version 1.0
 */
public class EndEventListener implements EventListener {

	/* (non-Javadoc)
	 * @see org.jbpm.api.listener.EventListener#notify(org.jbpm.api.listener.EventListenerExecution)
	 */
	@Override
	public void notify(EventListenerExecution execution) throws Exception {
		// TODO Auto-generated method stub
		System.out.println("enter into EndEventListener");
		ProExecutionService proExecutionService = (ProExecutionService)SpringContextUtil.getBean("proExecutionService");
		proExecutionService.endProcessInstance(execution.getId(),false);
	}

}
