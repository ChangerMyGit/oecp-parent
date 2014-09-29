/*
 * Copyright (c) 2012 OECP All Rights Reserved.                	
 * <a href="http://www.oecp.cn">OECP</a> 
*/                                                                
  

package oecp.platform.org.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Component;

import oecp.platform.event.annotation.Listener;
import oecp.platform.event.service.EventEngineImpl;
import oecp.framework.util.SpringContextUtil;
import oecp.platform.org.eo.OrgUseBC;
import oecp.platform.org.eo.Organization;

/** 
 * 建账事件监听器
 * @author wangliang  
 * @date 2012-2-14 下午2:32:36 
 * @version 1.0
 *  
 */
@Component
@Listener(source="oecp.platform.org.eo.OrgUseBC")
public class OrgUseBCListener {
	
/**
 * 启用模块监听<br>
 * 将新启用的模块追加到事件引擎中的权限列表中
 * @author wangliang
 * @date 2012-2-14下午4:54:53
 * @param orgUseBC		事件源
 * @param org			公司
 * @param sources		其他参数
 */
	public void onStartUseBCs(OrgUseBC orgUseBC,Organization org,Object...sources){
		//获取事件引擎
		EventEngineImpl eventEngine = (EventEngineImpl)SpringContextUtil.getBean("eventEngine");
		Map<String, List<String>> permission = eventEngine.getPermission();
		List<String> bccodes = permission.get(orgUseBC.getOrg().getId());
		if(bccodes == null){
			bccodes = new ArrayList<String>();
		}
		if(bccodes.contains(orgUseBC.getBc().getCode())){
				bccodes.remove(orgUseBC.getBc().getCode());
		}
		permission.put(orgUseBC.getOrg().getId(),bccodes);
	}

}
