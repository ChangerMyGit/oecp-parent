/**
 * oecp-platform - ActionEventLog.java
 * copyright 2009-2012 OECP www.oecp.cn
 * 创建人:luanyoubo	创建时间:下午3:00:59		版本:v1
 * ============================================
 * 修改人：			修改时间:					版本:
 * 修改原因:
 *
 * ============================================
 */

package oecp.platform.event.eo;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Lob;
import javax.persistence.Table;

import oecp.framework.entity.base.StringPKEO;

/** 
 * Action 日志
 * @author luanyoubo  
 * @date 2014年3月11日 下午3:00:59 
 * @version 1.0
 *  
 */
@Entity
@Table(name = "OECP_SYS_EVENTLOG")
public class ActionEventLog extends StringPKEO{
	private static final long serialVersionUID = -6449654790031082341L;
	private String operator;// 操作人
	private Date operateDate;// 操作事件
	private String eventDes;// 事件描述
	private String args;// 事件参数
    private String ipAddress;//IP地址
	
	public String getOperator() {
		return operator;
	}

	public void setOperator(String operator) {
		this.operator = operator;
	}

	public Date getOperateDate() {
		return operateDate;
	}

	public void setOperateDate(Date operateDate) {
		this.operateDate = operateDate;
	}

	public String getEventDes() {
		return eventDes;
	}

	public void setEventDes(String eventDes) {
		this.eventDes = eventDes;
	}
	@Lob
	@Column(columnDefinition="CLOB", nullable=true) 
	public String getArgs() {
		return args;
	}

	public void setArgs(String args) {
		this.args = args;
	}

	public String getIpAddress() {
		return ipAddress;
	}

	public void setIpAddress(String ipAddress) {
		this.ipAddress = ipAddress;
	}
	
}
