/*
 * Copyright (c) 2011 OECP All Rights Reserved.                	
 * <a href="http://www.oecp.cn">http://www.oecp.cn</a>                                                                 
 */    

package oecp.platform.bcevent.web;

import java.util.List;

import javax.annotation.Resource;

import oecp.framework.exception.BizException;
import oecp.framework.util.FastJsonUtils;
import oecp.framework.web.JsonResult;
import oecp.platform.bcevent.eo.EventInfo;
import oecp.platform.bcevent.eo.ListenerInfo;
import oecp.platform.bcevent.service.EventService;
import oecp.platform.web.BasePlatformAction;

import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.Namespace;
import org.apache.struts2.convention.annotation.ParentPackage;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

/**
 * 事件管理aciton
 * @author slx
 * @date 2011-8-8下午03:23:38
 * @version 1.0
 */
@Controller("eventAction")
@Scope("prototype")
@ParentPackage("default-package")
@Namespace("/event")
public class EventAction extends BasePlatformAction {
	
	private static final long serialVersionUID = 1L;

	@Resource(name="eventService")
	private EventService evtService;
	private EventInfo eventInfo;
	private String eventID;
	private ListenerInfo lsinfo;
	private String lsID;
	private String bcID;
	
	@Action("list")
	public String listAll() throws BizException{
		List<EventInfo> events = evtService.query(null);
		JsonResult jr = new JsonResult(events);
		jr.setContainFields(new String[]{"id","code","name","bc.name","bc.id"});
		setJsonString(jr.toJSONString());
		return SUCCESS;
	}
	
	@Action("regist")
	public String regist() throws BizException{
		evtService.create(eventInfo);
		setJsonString("{success:true,msg:'保存成功!'}");
		return SUCCESS;
	}
	
	@Action("eventInfo")
	public String eventInfo() throws BizException{
		eventInfo = evtService.find(eventID);
		setJsonString(FastJsonUtils.toJson(eventInfo, new String[]{"id","bc","code","name","description","structinfo"}));
		return SUCCESS;
	}
	
	@Action("update")
	public String update() throws BizException{
		evtService.save(eventInfo);
		setJsonString("{success:true,msg:'保存成功!'}");
		return SUCCESS;
	}
	
	@Action("remove")
	public String remove() throws BizException{
		evtService.delete(eventID);
		setJsonString("{success:true,msg:'删除成功!'}");
		return SUCCESS;
	}
	
	@Action("updateFromBC")
	public String updateFromBC() throws Exception{
		evtService.updateEventInfoFromBC(bcID);
		setJsonString("{success:true,msg:'事件信息获取并更新完成!'}");
		return SUCCESS;
	}
	
	@Action("listeners")
	public String loadListeners(){
		List<ListenerInfo> lss = evtService.getEventListeners(eventID);
		JsonResult jr = new JsonResult(lss);
		jr.setContainFields(new String[]{"id","name","beanname","classname","idx","event.id","event.name"});
		setJsonString(jr.toJSONString());
		return SUCCESS;
	}
	
	@Action("registListener")
	public String registListener(){
		evtService.addListener(lsinfo);
		setJsonString("{success:true,msg:'保存成功!'}");
		return SUCCESS;
	}
	
	@Action("listenerInfo")
	public String listenerInfo(){
		lsinfo = evtService.findListener(lsID);
		setJsonString(FastJsonUtils.toJson(lsinfo,new String[]{"id","name","beanname","classname","event","idx","code"}));
		return SUCCESS;
	}
	
	@Action("updateListener")
	public String updateListener(){
		evtService.updateListener(lsinfo);
		setJsonString("{success:true,msg:'保存成功!'}");
		return SUCCESS;
	}
	
	@Action("romoveListener")
	public String romoveListener(){
		evtService.removeListener(lsID);
		setJsonString("{success:true,msg:'删除成功!'}");
		return SUCCESS;
	}

	public EventInfo getEventInfo() {
		return eventInfo;
	}
	public void setEventInfo(EventInfo eventInfo) {
		this.eventInfo = eventInfo;
	}
	public void setEvtService(EventService evtService) {
		this.evtService = evtService;
	}
	public String getEventID() {
		return eventID;
	}
	public void setEventID(String eventID) {
		this.eventID = eventID;
	}
	public ListenerInfo getLsinfo() {
		return lsinfo;
	}
	public void setLsinfo(ListenerInfo lsinfo) {
		this.lsinfo = lsinfo;
	}
	public String getLsID() {
		return lsID;
	}
	public void setLsID(String lsID) {
		this.lsID = lsID;
	}
	public String getBcID() {
		return bcID;
	}
	public void setBcID(String bcID) {
		this.bcID = bcID;
	}
}
