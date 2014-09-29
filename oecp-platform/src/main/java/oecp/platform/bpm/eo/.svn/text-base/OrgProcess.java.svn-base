/*
 * Copyright (c) 2011 OECP All Rights Reserved.                	
 * <a href="http://www.oecp.cn">http://www.oecp.cn</a>                                                                 
 */    

package oecp.platform.bpm.eo;

import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import oecp.framework.entity.base.StringPKEO;
import oecp.platform.org.eo.Organization;

/**
 * 组织流程
 * @author yongtree
 * @date 2011-6-24 下午02:07:39
 * @version 1.0
 */
@Entity
@Table(name="OECP_T_ORG_PROCESS")
public class OrgProcess extends StringPKEO{

	private Organization org;
	
	private ProDefinition def;

	@ManyToOne
	public Organization getOrg() {
		return org;
	}

	public void setOrg(Organization org) {
		this.org = org;
	}

	@ManyToOne
	public ProDefinition getDef() {
		return def;
	}

	public void setDef(ProDefinition def) {
		this.def = def;
	}
	
	
	
}
