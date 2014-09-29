/*
 * Copyright (c) 2011 OECP All Rights Reserved.                	
 * <a href="http://www.oecp.cn">http://www.oecp.cn</a>                                                                 
 */    

package oecp.platform.bpm.framework.impl;

import oecp.platform.role.eo.Role;

import org.jbpm.api.identity.Group;

/**
 *
 * @author yangtao
 * @date 2011-8-9下午01:52:18
 * @version 1.0
 */
public class JbpmGroup implements Group {

	private Role role;
	
	public JbpmGroup(Role role){
		this.role = role;
	}
	/* (non-Javadoc)
	 * @see org.jbpm.api.identity.Group#getId()
	 */
	@Override
	public String getId() {
		// TODO Auto-generated method stub
		return this.role.getId();
	}

	/* (non-Javadoc)
	 * @see org.jbpm.api.identity.Group#getName()
	 */
	@Override
	public String getName() {
		// TODO Auto-generated method stub
		return this.role.getName();
	}

	/* (non-Javadoc)
	 * @see org.jbpm.api.identity.Group#getType()
	 */
	@Override
	public String getType() {
		// TODO Auto-generated method stub
		return this.role.getCode();
	}

}
