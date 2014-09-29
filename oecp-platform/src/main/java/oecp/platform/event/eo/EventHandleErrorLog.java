/**
 * oecp-platform - EventHandleErrorLog.java
 * copyright 2009-2012 OECP www.oecp.cn
 * 创建人:Administrator	创建时间:上午10:52:34		版本:v1
 * ============================================
 * 修改人：			修改时间:					版本:
 * 修改原因:
 *
 * ============================================
 */

package oecp.platform.event.eo;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

import oecp.framework.entity.base.StringPKEO;

/** 
 * 事件失败日志
 * @author luanyoubo  
 * @date 2013年12月19日 上午10:52:34 
 * @version 1.0
 *  
 */
@Entity
@Table(name = "OECP_SYS_EVENTHANDLEERRORLOG")
public class EventHandleErrorLog extends StringPKEO {
	private static final long serialVersionUID = -6057547066692261790L;
	private String beanClassName; // bean类完整名称
	private String methodName;    // 执行的方法名称
	private String argsType;      // 参数类型
	private String agrsValue;     // 参数值
	private String status;        // 状态

	public String getBeanClassName() {
		return beanClassName;
	}

	public void setBeanClassName(String beanClassName) {
		this.beanClassName = beanClassName;
	}

	public String getMethodName() {
		return methodName;
	}

	public void setMethodName(String methodName) {
		this.methodName = methodName;
	}

	public String getArgsType() {
		return argsType;
	}

	public void setArgsType(String argsType) {
		this.argsType = argsType;
	}

	@Column(length = 4000)
	public String getAgrsValue() {
		return agrsValue;
	}

	public void setAgrsValue(String agrsValue) {
		this.agrsValue = agrsValue;
	}

	@Column(length = 2)
	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

}
