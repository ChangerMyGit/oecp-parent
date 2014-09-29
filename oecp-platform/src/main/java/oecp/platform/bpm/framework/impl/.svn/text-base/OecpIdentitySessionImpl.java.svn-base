/*
 * Copyright (c) 2011 OECP All Rights Reserved.                	
 * <a href="http://www.oecp.cn">http://www.oecp.cn</a>                                                                 
 */    

package oecp.platform.bpm.framework.impl;

import java.util.ArrayList;
import java.util.List;

import oecp.framework.exception.BizException;
import oecp.framework.util.SpringContextUtil;
import oecp.platform.role.eo.OrgRole;
import oecp.platform.role.service.OrgRoleService;
import oecp.platform.role.service.RoleService;
import oecp.platform.user.service.UserService;

import org.jbpm.api.identity.Group;
import org.jbpm.api.identity.User;
import org.jbpm.pvm.internal.identity.spi.IdentitySession;

/**
 *
 * @author yangtao
 * @date 2011-8-9上午11:25:11
 * @version 1.0
 */
public class OecpIdentitySessionImpl implements IdentitySession {
	
	private UserService userService = (UserService)SpringContextUtil.getBean("userService");

	private OrgRoleService orgRoleService = (OrgRoleService)SpringContextUtil.getBean("orgRoleService");
	
	private RoleService roleService = (RoleService)SpringContextUtil.getBean("roleService");
	/* (non-Javadoc)
	 * @see org.jbpm.pvm.internal.identity.spi.IdentitySession#createGroup(java.lang.String, java.lang.String, java.lang.String)
	 */
	@Override
	public String createGroup(String arg0, String arg1, String arg2) {
		// TODO Auto-generated method stub
		return null;
	}

	/* (non-Javadoc)
	 * @see org.jbpm.pvm.internal.identity.spi.IdentitySession#createMembership(java.lang.String, java.lang.String, java.lang.String)
	 */
	@Override
	public void createMembership(String arg0, String arg1, String arg2) {
		// TODO Auto-generated method stub

	}

	/* (non-Javadoc)
	 * @see org.jbpm.pvm.internal.identity.spi.IdentitySession#createUser(java.lang.String, java.lang.String, java.lang.String, java.lang.String)
	 */
	@Override
	public String createUser(String arg0, String arg1, String arg2, String arg3) {
		// TODO Auto-generated method stub
		return null;
	}

	/* (non-Javadoc)
	 * @see org.jbpm.pvm.internal.identity.spi.IdentitySession#deleteGroup(java.lang.String)
	 */
	@Override
	public void deleteGroup(String arg0) {
		// TODO Auto-generated method stub

	}

	/* (non-Javadoc)
	 * @see org.jbpm.pvm.internal.identity.spi.IdentitySession#deleteMembership(java.lang.String, java.lang.String, java.lang.String)
	 */
	@Override
	public void deleteMembership(String arg0, String arg1, String arg2) {
		// TODO Auto-generated method stub

	}

	/* (non-Javadoc)
	 * @see org.jbpm.pvm.internal.identity.spi.IdentitySession#deleteUser(java.lang.String)
	 */
	@Override
	public void deleteUser(String arg0) {
		// TODO Auto-generated method stub

	}

	/* (non-Javadoc)
	 * @see org.jbpm.pvm.internal.identity.spi.IdentitySession#findGroupById(java.lang.String)
	 */
	@Override
	public Group findGroupById(String arg0) {
		// TODO Auto-generated method stub
		JbpmGroup jg = null;
		try {
			 jg = new JbpmGroup(this.roleService.find(arg0));
		} catch (BizException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return jg;
	}

	/* (non-Javadoc)
	 * @see org.jbpm.pvm.internal.identity.spi.IdentitySession#findGroupsByUser(java.lang.String)
	 */
	@Override
	public List<Group> findGroupsByUser(String arg0) {
		// TODO Auto-generated method stub
		List<OrgRole> list = this.orgRoleService.getUserOrgRole(arg0);
		List<Group> resultList = new ArrayList<Group>();
		for(OrgRole or : list){
			JbpmGroup jg = new JbpmGroup(or.getRole());
			resultList.add(jg);
		}
		return resultList;
	}

	/* (non-Javadoc)
	 * @see org.jbpm.pvm.internal.identity.spi.IdentitySession#findGroupsByUserAndGroupType(java.lang.String, java.lang.String)
	 */
	@Override
	public List<Group> findGroupsByUserAndGroupType(String arg0, String arg1) {
		// TODO Auto-generated method stub
		return null;
	}

	/* (non-Javadoc)
	 * @see org.jbpm.pvm.internal.identity.spi.IdentitySession#findUserById(java.lang.String)
	 */
	@Override
	public User findUserById(String arg0) {
		// TODO Auto-generated method stub
		JbpmUser bpmuser = null;
		try {
			oecp.platform.user.eo.User oecpuser = userService.find(arg0);
			bpmuser = new JbpmUser(oecpuser);
		} catch (BizException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return bpmuser;
	}

	/* (non-Javadoc)
	 * @see org.jbpm.pvm.internal.identity.spi.IdentitySession#findUsers()
	 */
	@Override
	public List<User> findUsers() {
		// TODO Auto-generated method stub
		List<User> resultList = new ArrayList();
		try {
			List<oecp.platform.user.eo.User> list = this.userService.query(null);
			for(oecp.platform.user.eo.User user : list){
				JbpmUser ju = new JbpmUser(user);
				resultList.add(ju);
			}
		} catch (BizException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return resultList;
	}

	/* (non-Javadoc)
	 * @see org.jbpm.pvm.internal.identity.spi.IdentitySession#findUsersByGroup(java.lang.String)
	 */
	@Override
	public List<User> findUsersByGroup(String arg0) {
		// TODO Auto-generated method stub
		List<User> resultList = new ArrayList();
		List<oecp.platform.user.eo.User> list;
		try {
			list = this.userService.getUsersByRoleId(arg0);
			for(oecp.platform.user.eo.User user : list){
				JbpmUser ju = new JbpmUser(user);
				resultList.add(ju);
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return resultList;
	}

	/* (non-Javadoc)
	 * @see org.jbpm.pvm.internal.identity.spi.IdentitySession#findUsersById(java.lang.String[])
	 */
	@Override
	public List<User> findUsersById(String... arg0) {
		// TODO Auto-generated method stub
		List<User> resultList = new ArrayList();
		List<oecp.platform.user.eo.User> list;
		try {
			list = this.userService.query(null);
			for(oecp.platform.user.eo.User user : list){
				JbpmUser ju = new JbpmUser(user);
				resultList.add(ju);
			}
		} catch (BizException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return resultList;
	}

}
