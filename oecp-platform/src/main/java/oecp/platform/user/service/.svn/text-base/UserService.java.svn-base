/*
 * Copyright (c) 2011 OECP All Rights Reserved.                	
 * http://www.oecp.cn                                                                 
 */

package oecp.platform.user.service;

import java.util.LinkedHashMap;
import java.util.List;

import oecp.framework.dao.QueryCondition;
import oecp.framework.dao.QueryResult;
import oecp.framework.exception.BizException;
import oecp.framework.service.BaseService;
import oecp.platform.org.eo.Organization;
import oecp.platform.role.eo.OrgRole;
import oecp.platform.user.eo.User;

/**
 * 用户服务
 * 
 * @author yongtree
 * @date 2011-4-28 下午04:34:02
 * @version 1.0
 */
public interface UserService extends BaseService<User> {

	/**
	 * 根据登录名和密码得到登录用户
	 * 
	 * @author yongtree
	 * @date 2011-5-4 上午11:44:53
	 * @param loginId
	 *            登录名
	 * @param encPwd
	 *            经过md5加密过的密码
	 * @return
	 */
	public User getLoginUser(String loginId, String encPwd);

	/**
	 * 得到分配给该用户的公司
	 * 
	 * @author yongtree
	 * @date 2011-5-10 下午02:44:40
	 * @param user
	 * @return
	 */
	public List<Organization> getOrganizationsToUser(User user);

	/**
	 * 得到分配给该用户的公司
	 * 
	 * @author yongtree
	 * @date 2011-5-10 下午02:44:40
	 * @param userId
	 *            用户的主键
	 * @return
	 */
	public List<Organization> getOrganizationsToUser(String userId);

	public QueryResult<User> getUsersByOrgID(String orgId,boolean isSelectedOrg,
			List<QueryCondition> conditions, int start, int limit,
			LinkedHashMap<String, String> orderby);

	/**
	 * 根据用户主键获取用户列表
	 * 
	 * @param userIds
	 * @return
	 */
	public List<User> getUserListByIds(String[] userIds);

	public void createUserAndAssignRoles(User user, String[] orgRoleIds)
			throws BizException;

	public void updateUserAndAssignRoles(User user, Organization org,
			String[] orgRoleIds) throws BizException;

	public void assignRoles(User user, Organization org, String[] orgRoleIds)
			throws BizException;

	public String resetPWD(String userId, String resetPWD) throws BizException;

	/**
	 * 检查loginId是否重复，是否符合要求
	 * 
	 * @author yongtree
	 * @date 2011-6-2 上午09:54:14
	 * @param loginId
	 * @param excludedUserId
	 *            排除用户ID
	 * @return
	 */
	public boolean checkLoginId(String loginId, String excludedUserId)
			throws BizException;
	
	public User getUserByPersonId(String personId);
	
	/**
	 * 
	 * 根据roleid获得这个角色下面所有的用户
	 * @author yangtao
	 * @date 2011-8-9下午02:54:32
	 * @param roleId
	 * @return
	 */
	public List<User> getUsersByRoleId(String roleId);
	
	/**
	 * 
	 * 查询某个组织下面的所有用户 不带分页
	 * @author yangtao
	 * @date 2011-8-15下午02:40:38
	 * @param orgId
	 * @param conditions
	 * @param orderby
	 * @return
	 */
	public QueryResult<User> getUsersByOrgID(String orgId,List<QueryCondition> conditions,
			LinkedHashMap<String, String> orderby);
	/**
	 * 
	 * 流程中根据角色、省、区域信息过滤用户
	 * @author yangtao
	 * @date 2011-8-25上午11:47:49
	 * @param orgRole
	 * @param provice
	 * @param area
	 * @return
	 */
	public List<User> getUsersByOrgRoleProArea(OrgRole orgRole,String provice,String area )throws BizException;
	
	/**
	 * 
	 * 修改密码
	 * @author yangtao
	 * @date 2011-8-25下午03:14:26
	 * @param oldPassWord
	 * @param newPassWord
	 * @return
	 * @throws BizException
	 */
	public String updatePassWord(User user,String oldPassWord,String newPassWord)throws BizException;
}
