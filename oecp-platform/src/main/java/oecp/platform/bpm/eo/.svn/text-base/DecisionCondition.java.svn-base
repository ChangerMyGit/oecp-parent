/*
 * Copyright (c) 2011 OECP All Rights Reserved.                	
 * <a href="http://www.oecp.cn">http://www.oecp.cn</a>                                                                 
 */    

package oecp.platform.bpm.eo;

import javax.persistence.Entity;
import javax.persistence.Table;

import oecp.framework.entity.base.StringPKEO;

/**
 * decision节点配置的条件
 * @author yangtao
 * @date 2011-8-26下午02:05:47
 * @version 1.0
 */
@Entity
@Table(name = "OECP_BPM_DECISION_CONDITION")
public class DecisionCondition extends StringPKEO {
	//条件参数的名称（在流程实例流程变量里面的名称）
	private String name;
	//条件参数的临界值
	private String value;
	/**
	 * @return the name
	 */
	public String getName() {
		return name;
	}
	/**
	 * @param name the name to set
	 */
	public void setName(String name) {
		this.name = name;
	}
	/**
	 * @return the value
	 */
	public String getValue() {
		return value;
	}
	/**
	 * @param value the value to set
	 */
	public void setValue(String value) {
		this.value = value;
	}
	
	
}
