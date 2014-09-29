/*
 * Copyright (c) 2011 OECP All Rights Reserved.                	
 * <a href="http://www.oecp.cn">http://www.oecp.cn</a>                                                                 
 */    

package oecp.platform.bpm.framework.impl;

import org.jbpm.api.identity.User;

/**
 *
 * @author yangtao
 * @date 2011-8-9下午01:51:36
 * @version 1.0
 */
public class JbpmUser implements User {
	private oecp.platform.user.eo.User user;
	
	
	public JbpmUser(oecp.platform.user.eo.User user){
		this.user = user;
	}

	/* (non-Javadoc)
	 * @see org.jbpm.api.identity.User#getBusinessEmail()
	 */
	@Override
	public String getBusinessEmail() {
		// TODO Auto-generated method stub
		return this.user.getEmail();
	}

	/* (non-Javadoc)
	 * @see org.jbpm.api.identity.User#getFamilyName()
	 */
	@Override
	public String getFamilyName() {
		// TODO Auto-generated method stub
		return this.user.getName();
	}

	/* (non-Javadoc)
	 * @see org.jbpm.api.identity.User#getGivenName()
	 */
	@Override
	public String getGivenName() {
		// TODO Auto-generated method stub
		return this.user.getName();
	}

	/* (non-Javadoc)
	 * @see org.jbpm.api.identity.User#getId()
	 */
	@Override
	public String getId() {
		// TODO Auto-generated method stub
		return this.user.getId();
	}

}
