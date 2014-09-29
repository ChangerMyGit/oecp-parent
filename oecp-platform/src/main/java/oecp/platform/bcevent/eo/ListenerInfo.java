/*
 * Copyright (c) 2011 OECP All Rights Reserved.                	
 * <a href="http://www.oecp.cn">http://www.oecp.cn</a>                                                                 
 */    

package oecp.platform.bcevent.eo;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import oecp.framework.entity.base.StringPKEO;

/**
 * 监听器信息
 * @author slx
 * @date 2011-8-8下午01:50:01
 * @version 1.0
 */
@Entity
@Table(name = "OECP_SYS_EVENTLISTENER")
public class ListenerInfo extends StringPKEO {

	private static final long serialVersionUID = 1L;
	/** 监听器名称 **/
	private String name;
	/** spring中的beanname **/
	private String beanname;
	/** 监听器类名 **/
	private String classname;
	/** 监听的事件 **/
	private EventInfo event;
	/** 参数0 **/
	private String arg0;
	/** 排序号 **/
	private Integer idx;
	
	@Column(nullable=false)
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	@Column(nullable=false)
	public String getClassname() {
		return classname;
	}
	public void setClassname(String classname) {
		this.classname = classname;
	}
	@ManyToOne
	@JoinColumn(nullable=false)
	public EventInfo getEvent() {
		return event;
	}
	public void setEvent(EventInfo event) {
		this.event = event;
	}
	public String getBeanname() {
		return beanname;
	}
	public void setBeanname(String beanname) {
		this.beanname = beanname;
	}
	public Integer getIdx() {
		return idx;
	}
	public void setIdx(Integer idx) {
		this.idx = idx;
	}
	public String getArg0() {
		return arg0;
	}
	public void setArg0(String arg0) {
		this.arg0 = arg0;
	}
}
