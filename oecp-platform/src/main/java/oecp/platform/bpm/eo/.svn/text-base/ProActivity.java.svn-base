/*
 * Copyright (c) 2011 OECP All Rights Reserved.                	
 * <a href="http://www.oecp.cn">http://www.oecp.cn</a>                                                                 
 */

package oecp.platform.bpm.eo;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import oecp.framework.entity.base.StringPKEO;
import oecp.platform.org.eo.Post;
import oecp.platform.role.eo.OrgRole;
import oecp.platform.user.eo.User;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * 流程节点
 * 
 * @author yongtree
 * @date 2011-6-24 下午02:49:20
 * @version 1.0
 */
@Entity
@Table(name = "OECP_BPM_ACTIVITY")
public class ProActivity extends StringPKEO {

	private static final long serialVersionUID = 1L;

	private ProDefinition proDef;

	private String activityName;

	
	@ManyToOne
	public ProDefinition getProDef() {
		return proDef;
	}

	public void setProDef(ProDefinition proDef) {
		this.proDef = proDef;
	}

	public String getActivityName() {
		return activityName;
	}

	public void setActivityName(String activityName) {
		this.activityName = activityName;
	}

}
