/**
 * oecp-platform - EventConfig.java
 * copyright 2009-2012 OECP www.oecp.cn
 * 创建人:luanyoubo	创建时间:下午1:38:44		版本:v1
 * ============================================
 * 修改人：			修改时间:					版本:
 * 修改原因:
 *
 * ============================================
 */

package oecp.platform.event.eo;

import javax.persistence.Entity;
import javax.persistence.Table;

import oecp.framework.entity.base.StringPKEO;

/** 
 * 事件配置
 * @author luanyoubo  
 * @date 2013年12月16日 下午1:38:44 
 * @version 1.0
 *  
 */
@Entity
@Table(name = "OECP_SYS_EVENTCONFIGE")
public class EventConfige extends StringPKEO {
	private static final long serialVersionUID = 6448487986214210861L;
	// 事件源
	private String eventSource;
	// 监听器
	private String listenerClass;
	// 动作
	private String action;
	// 启动
	private String startFlag;
	// 同步/异步
	private String synFlag;
	
	public String getEventSource() {
		return eventSource;
	}
	public void setEventSource(String eventSource) {
		this.eventSource = eventSource;
	}
	public String getListenerClass() {
		return listenerClass;
	}
	public void setListenerClass(String listenerClass) {
		this.listenerClass = listenerClass;
	}
	public String getAction() {
		return action;
	}
	public void setAction(String action) {
		this.action = action;
	}
	public String getStartFlag() {
		return startFlag;
	}
	public void setStartFlag(String startFlag) {
		this.startFlag = startFlag;
	}
	public String getSynFlag() {
		return synFlag;
	}
	public void setSynFlag(String synFlag) {
		this.synFlag = synFlag;
	}
}
