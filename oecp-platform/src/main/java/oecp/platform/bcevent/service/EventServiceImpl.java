/*
 * Copyright (c) 2011 OECP All Rights Reserved.                	
 * <a href="http://www.oecp.cn">http://www.oecp.cn</a>                                                                 
 */    

package oecp.platform.bcevent.service;

import java.io.Serializable;
import java.util.List;
import javax.annotation.Resource;
import org.springframework.stereotype.Service;
import oecp.framework.exception.BizException;
import oecp.framework.util.OECPBeanUtils;
import oecp.framework.util.SpringContextUtil;
import oecp.framework.util.soap.SoapUtils;
import oecp.platform.base.service.OECPValidator;
import oecp.platform.base.service.PlatformBaseServiceImpl;
import oecp.platform.bcevent.eo.EventInfo;
import oecp.platform.bcevent.eo.ListenerInfo;
import oecp.platform.bcevent.listener.BCEventListenerItf;
import oecp.platform.bcinfo.eo.BizComponent;

/**
 * 事件管理服务类
 * @author slx
 * @date 2011-8-8下午02:42:12
 * @version 1.0
 */
@Service("eventService")
public class EventServiceImpl extends PlatformBaseServiceImpl<EventInfo> implements EventService {

	@Resource(name="eventValidator")
	private OECPValidator validator;
	
	@Override
	public void delete(Serializable id) throws BizException {
		List<ListenerInfo> lss = getEventListeners((String)id);
		if(lss != null && lss.size()>0 ){
			throw new BizException("请先移除所有的监听器，然后再删除事件！");
		}
		super.delete(id);
	}
	
	@Override
	public void create(EventInfo t) throws BizException {
		t.setId(null);
		validator.validator("create",t,getDao());
		getDao().create(t);
	}
	
	@Override
	public void update(EventInfo t) throws BizException {
		validator.validator("update",t,getDao());
		super.update(t);
	}
	
	@Override
	public List<ListenerInfo> getEventListeners(String eventID) {
		List<ListenerInfo> lss = getDao().queryByWhere(ListenerInfo.class, " o.event.id=? ORDER BY o.idx", new Object[]{eventID});
		return lss;
	}
	@Override
	public void addListener(ListenerInfo listener) {
		listener.setId(null);
		getDao().create(listener);
	}

	@Override
	public void removeListener(String listenerID) {
		getDao().delete(ListenerInfo.class, listenerID);
	}

	@Override
	public ListenerInfo findListener(String listenerID) {
		return getDao().find(ListenerInfo.class, listenerID);
	}
	
	@Override
	public void updateListener(ListenerInfo listener) {
		getDao().update(listener);
	}

	@Override
	public void updateEventInfoFromBC(String bcInfoID) throws Exception {
		BizComponent bc = getDao().find(BizComponent.class, bcInfoID);
		String url = bc.getWebServiceDomainUrl().concat("/BCRegister?wsdl");
		Object[] res = SoapUtils.callService(url, "getBizEvents");
		StringBuffer formule = new StringBuffer("code->code")
		.append(",name->name")
		.append(",description->description")
		.append(",eventSourceXSD->structinfo");
		
		List<EventInfo> events = OECPBeanUtils.createObjectList((List)res[0], EventInfo.class, formule.toString());
		
		for (int i = 0; i < events.size(); i++) {
			EventInfo event = events.get(i);
			EventInfo eventindb = getDao().findByWhere(EventInfo.class, " o.code=? " , new Object[]{event.getCode()});
			event.setBc(bc);
			if(eventindb != null){
				event.setId(eventindb.getId());
				update(event);
			}else{
				create(event);
			}
		}
	}
	
	@Override
	public void fireEvnet(String bcCode,String eventCode, String dataXML) throws BizException {
		// 根据事件编号查找事件信息，找不到则抛出异常。
		EventInfo evtInfo = getDao().findByWhere(EventInfo.class, " o.bc.code=? AND o.code=? ", new Object[]{bcCode,eventCode});
		if(evtInfo==null){
			throw new BizException("事件未注册!");
		}
		// 找到事件信息后，查找监听器。没有监听器，return；
		List<ListenerInfo> listeners = evtInfo.getListeners();
		if(listeners==null || listeners.size()==0){
			return;
		}
		
		// 有监听器，创建监听器对象，运行之。传入参数，事件编号，事件源数据，事件源数据描述。
		for (ListenerInfo listenerInfo : listeners) {
			BCEventListenerItf listener = (BCEventListenerItf)SpringContextUtil.getBean(listenerInfo.getBeanname());
			
			listener.onAction(evtInfo.getBc().getCode(), evtInfo.getCode(), dataXML,evtInfo.getStructinfo(), new String[]{listenerInfo.getArg0()});
		}
		
	}

	public void setValidator(OECPValidator validator) {
		this.validator = validator;
	}
	
}
