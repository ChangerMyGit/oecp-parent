/*
 * Copyright (c) 2011 OECP All Rights Reserved.                	
 * <a href="http://www.oecp.cn">http://www.oecp.cn</a>                                                                 
 */    

package oecp.platform.bcevent.service;

import java.util.List;

import org.springframework.stereotype.Component;

import oecp.framework.exception.BizException;
import oecp.framework.util.soap.ComplexType;
import oecp.framework.util.soap.WSDLUtils;
import oecp.platform.base.service.PlatformBaseServiceImpl;
import oecp.platform.bcevent.eo.EventInfo;

/**
 * 事件工具类
 * @author slx
 * @date 2011-8-19下午02:08:48
 * @version 1.0
 */
@Component("eventUtils")
public class EventUtilsServiceImpl extends PlatformBaseServiceImpl<EventInfo> implements EventUtilsService {

	public ComplexType getEventSourceType(String eventID) throws BizException {
		EventInfo event = find(eventID);
		String dataxsd = event.getStructinfo();
		// TODO 根据事件ID返回事件源结构
		ComplexType type = null;
		try {
			List<ComplexType> types = WSDLUtils.getDataStruct(dataxsd);
			type = types.get(0);
		} catch (Exception e) {
			throw new BizException("事件源数据格式描述不正确，无法解析！");
		}
		return type;
	}


}
