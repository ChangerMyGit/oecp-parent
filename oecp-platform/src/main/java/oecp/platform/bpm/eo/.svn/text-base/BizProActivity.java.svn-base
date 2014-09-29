/*
 * Copyright (c) 2011 OECP All Rights Reserved.                	
 * <a href="http://www.oecp.cn">http://www.oecp.cn</a>                                                                 
 */    

package oecp.platform.bpm.eo;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import oecp.framework.entity.base.StringPKEO;

/**
 * 业务流程节点封装
 * @author yangtao
 * @date 2011-9-5上午10:19:23
 * @version 1.0
 */
@Entity
@Table(name="OECP_BPM_BIZ_ACTIVITY")
public class BizProActivity extends StringPKEO{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 3255235741597172642L;

	//业务流程定义
	private BizProDefinition bizProDefinition;

	//流程节点名称
	private String activityName;


	/**
	 * @return the bizProDefinition
	 */
	@ManyToOne
	public BizProDefinition getBizProDefinition() {
		return bizProDefinition;
	}

	/**
	 * @param bizProDefinition the bizProDefinition to set
	 */
	public void setBizProDefinition(BizProDefinition bizProDefinition) {
		this.bizProDefinition = bizProDefinition;
	}

	/**
	 * @return the activityName
	 */
	public String getActivityName() {
		return activityName;
	}

	/**
	 * @param activityName the activityName to set
	 */
	public void setActivityName(String activityName) {
		this.activityName = activityName;
	}

	
}
