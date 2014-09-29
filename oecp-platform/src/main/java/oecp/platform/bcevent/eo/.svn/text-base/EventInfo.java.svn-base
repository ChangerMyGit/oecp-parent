/*
 * Copyright (c) 2011 OECP All Rights Reserved.                	
 * <a href="http://www.oecp.cn">http://www.oecp.cn</a>                                                                 
 */    

package oecp.platform.bcevent.eo;

import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.Lob;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OrderBy;
import javax.persistence.Table;

import oecp.framework.entity.base.StringPKEO;
import oecp.platform.bcinfo.eo.BizComponent;

/**
 * 业务事件信息
 * @author slx
 * @date 2011-8-8上午09:18:05
 * @version 1.0
 */
@Entity
@Table(name = "OECP_SYS_EVENT")
public class EventInfo extends StringPKEO {

	private static final long serialVersionUID = 1L;
	/** 所属组件 **/
	private BizComponent bc;
	/** 事件代号 **/
	private String code;
	/** 事件名称 **/
	private String name;
	/** 描述 **/
	private String description;
	/** 事件源结构描述 **/
	private String structinfo;
	/** 监听器列表 **/
	private List<ListenerInfo> listeners;
	@ManyToOne
	@JoinColumn(nullable=false)
	public BizComponent getBc() {
		return bc;
	}
	public void setBc(BizComponent bc) {
		this.bc = bc;
	}
	@Column(nullable=false)
	public String getCode() {
		return code;
	}
	public void setCode(String code) {
		this.code = code;
	}
	@Column(nullable=false)
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	@Lob
	@Column(nullable=false)
	public String getStructinfo() {
		return structinfo;
	}
	public void setStructinfo(String structinfo) {
		this.structinfo = structinfo;
	}
	@OneToMany(mappedBy="event")
	@OrderBy("idx")
	public List<ListenerInfo> getListeners() {
		return listeners;
	}
	public void setListeners(List<ListenerInfo> listeners) {
		this.listeners = listeners;
	}
}
