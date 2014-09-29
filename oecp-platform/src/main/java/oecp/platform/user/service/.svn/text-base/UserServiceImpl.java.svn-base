/*
 * Copyright (c) 2011 OECP All Rights Reserved.                	
 * http://www.oecp.cn                                                                 
 */

package oecp.platform.user.service;

import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;

import oecp.framework.dao.QueryCondition;
import oecp.framework.dao.QueryObject;
import oecp.framework.dao.QueryResult;
import oecp.framework.exception.BizException;
import oecp.platform.base.service.PlatformBaseServiceImpl;
import oecp.platform.bcfunction.eo.Function;
import oecp.platform.org.eo.Organization;
import oecp.platform.org.service.OrgService;
import oecp.platform.role.eo.OrgRole;
import oecp.platform.role.service.OrgRoleService;
import oecp.platform.user.eo.User;
import oecp.platform.userdesktop.service.UserDesktopService;

import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.lang.StringUtils;
import org.hibernate.HibernateException;
import org.hibernate.SQLQuery;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * 
 * @author yongtree
 * @date 2011-5-4 上午11:47:48
 * @version 1.0
 */
@Service("userService")
public class UserServiceImpl extends PlatformBaseServiceImpl<User> implements
		UserService {

	@Autowired
	private OrgRoleService orgRoleService;

	@Autowired
	private OrgService orgService;
	
	@Autowired
	UserDesktopService userDesktopService;

	@Override
	public User getLoginUser(String loginId, String encPwd) {
		return this.getDao().findByWhere(User.class,
				"loginId=? and password=?", new Object[] { loginId, encPwd });
	}

	@Override
	public List<Organization> getOrganizationsToUser(User user) {
		return getOrganizationsToUser(user.getId());
	}

	@Override
	public List<Organization> getOrganizationsToUser(String userId) {
		List<Organization> orgs = null;
		StringBuffer sf_hql = new StringBuffer();
		sf_hql.append(" SELECT DISTINCT r.org FROM User u ")
				.append("JOIN u.orgRoles r ").append("WHERE u.id = ? AND r.org.lock<>?");
		orgs = getDao().getHibernateSession().createQuery(sf_hql.toString())
				.setParameter(0, userId).setParameter(1, true).list();
		return orgs;
	}

	@Override
	public QueryResult<User> getUsersByOrgID(String orgId,boolean isSelectedOrg,
			List<QueryCondition> conditions, int start, int limit,
			LinkedHashMap<String, String> orderby) {
		if(orderby.isEmpty()){
			orderby.put("loginId","asc");
			orderby.put("name","asc");
		}
		String condition = null;
		if(isSelectedOrg){//选择组织
			condition  = " createdByOrg.id = '"+orgId +"'";
		}else{//没选择组织
			String result = this.orgService.getAllChildOrgIds(orgId);
			condition = " createdByOrg.id IN("+result+")";
		}
		QueryObject qo = new QueryObject();
		qo.setQueryConditions(conditions);
		String str_con = qo.getWhereQL();
		condition += StringUtils.isNotBlank(str_con)? (" AND "+str_con):"";
		return this.getDao().getScrollData(User.class, start, limit,
				condition, qo.getQueryParams(),orderby);
	}

	@Override
	public List<User> getUserListByIds(String[] userIds) {
		if (userIds == null || userIds.length == 0) {
			return null;
		}
		String where = "";
		for (int i = 0; i < userIds.length; i++) {
			where += "?,";
		}
		where = "o.id IN (" + where.substring(0, where.length() - 1) + ")";
		return getDao().queryByWhere(User.class, where, userIds);
	}

	@Transactional
	public void createUserAndAssignRoles(User user, String[] orgRoleIds)
			throws BizException {
		user.setId(null);
		if (checkLoginId(user.getLoginId(), null)) {
			user.setCreateTime(new Date());
		}
		List<OrgRole> list = this.orgRoleService
				.getOrgRolesByRoleIds(orgRoleIds);
		user.setOrgRoles(list);
		this.create(user);
//		// 启动流程
//		Map<String, Object> variables = new HashMap<String, Object>();
//		variables.put("user", user);
//		ProExecutionResult result = proExecutionService.startVirProcess(
//				"OECP.SYS.USER", user.getCreatedByOrg(), WebContext
//						.getCurrentUser().getUser(), user.getId(),user.getLoginId(), variables);
//		System.out.println(result.getState());
//		if(!ProExecutionResult.ExecutionState.SUCCESS.equals(result.getState())){//流程没有开启等
//			//自行处理一些业务逻辑
//		}
		
		//创建默认快捷方式
		LinkedHashMap<String, String> orderby = new LinkedHashMap<String, String>();
		orderby.put("displayOrder", "asc");
		List<Function> li = this.getDao().queryByWhere(Function.class, "bc.name=?", new Object[]{"协同办公"},orderby);
		for(int i=0;i<li.size();i++){
			Function fc = li.get(i);
			userDesktopService.addDesktopFunc(user, fc.getId(),i+1);
		}
	}

	@Transactional
	@Override
	public void assignRoles(User user, Organization org, String[] orgRoleIds)
			throws BizException {
		List<OrgRole> originals = user.getOrgRoles();
		List<OrgRole> assigns = this.orgRoleService
				.getOrgRolesByRoleIds(orgRoleIds);
		for (OrgRole or : originals) {
			if (!or.getOrg().equalsPK(org)) {
				assigns.add(or);
			}
		}
		user.setOrgRoles(assigns);
		this.save(user);
	}

	@Override
	public boolean checkLoginId(String loginId, String excludedUserId)
			throws BizException {
		// TODO 检查格式

		// 检查是否重复
		List list = this.getDao().getHibernateSession().createQuery(
				"SELECT id FROM User where loginId=? and id<>?").setParameter(
				0, loginId).setParameter(1, excludedUserId).list();
		if (list.size() > 0)
			throw new BizException("账号已经存在，请更换！");
		return true;
	}

	@Override
	public void updateUserAndAssignRoles(User user, Organization org,
			String[] orgRoleIds) throws BizException {
		if (StringUtils.isEmpty(user.getId()))
			throw new BizException("您在编辑用户吗？怎么没有主键！");
		if (checkLoginId(user.getLoginId(), user.getId())) {
			assignRoles(user, org, orgRoleIds);
		}
	}

	public String resetPWD(String userId, String resetPWD) throws BizException {
//		double r = Math.random() * (999999 - 100000) + 100000;
//		String pwd = String.valueOf((int) r);
		User user = this.find(userId);
		user.setPassword(DigestUtils.md5Hex(resetPWD));
		this.save(user);
		return resetPWD;
	}

	public void setOrgRoleService(OrgRoleService orgRoleService) {
		this.orgRoleService = orgRoleService;
	}

	@Override
	public User getUserByPersonId(String personId) {
		return this.getDao().findByWhere(User.class, "personId=?",
				new Object[] { personId });
	}
	
	/**
	 * 
	 * 根据roleid获得这个角色下面所有的用户
	 * @author yangtao
	 * @date 2011-8-9下午02:54:32
	 * @param roleId
	 * @return
	 */
	public List<User> getUsersByRoleId(String roleId){
		List<OrgRole> list = this.orgRoleService.getUserOrgRoleByRoleId(roleId);
		List<User> resultList = new ArrayList();
		for(OrgRole or : list){
			List<User> orgRoles = getDao()
			.queryByWhere(
					User.class,
					"(SELECT rr FROM OrgRole rr WHERE rr.id=?) MEMBER OF o.orgRoles",
					new Object[] { or.getId() });
			resultList.addAll(orgRoles);
		}
		return resultList;
	}
	
	/**
	 * 
	 * 查询某个组织下面的所有用户(自己创建和分配的角色的用户) 不带分页
	 * @author yangtao
	 * @date 2011-8-15下午02:40:38
	 * @param orgId
	 * @param conditions
	 * @param orderby
	 * @return
	 */
	public QueryResult<User> getUsersByOrgID(String orgId,List<QueryCondition> conditions,
			LinkedHashMap<String, String> orderby){
		List<OrgRole> list = this.orgRoleService.getAllOrgRole(orgId);
		List<User> resultList = new ArrayList();
		String sqlFiled = "(SELECT rr FROM OrgRole rr WHERE rr.id=?) MEMBER OF o.orgRoles";
		String[] sqlParams = new String[conditions.size()+1];
		int i = 1;
		for(QueryCondition qc : conditions){
			sqlFiled+=" and "+qc.getField()+qc.getOperator()+"? ";
			sqlParams[i] = (String)qc.getValue();
			i++;
		}
		
		for(OrgRole or : list){
			sqlParams[0] = or.getId();
			List<User> userList = this.getDao().queryByWhere(User.class, sqlFiled, sqlParams);
			resultList.addAll(userList);
		}
		QueryResult<User> qr = new QueryResult<User>();
		qr.setResultlist(resultList);
		qr.setTotalrecord(new Long(resultList.size()));
		return qr;
	}
	
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
	public List<User> getUsersByOrgRoleProArea(OrgRole orgRole,String provice,String area )throws BizException{
		List<User> resultList = new ArrayList();
		try {
			//根据角色、省、区域过滤账号
			List<oecp.platform.user.eo.User> rus = orgRole.getUsers();
			String ids = "";
			for(int i=0;i<rus.size();i++){
				if(i!=0)
					ids+=",";
				ids+="'"+((User)(rus.get(i))).getLoginId()+"'";
			}
			if(!StringUtils.isEmpty(ids)){
				SQLQuery query = this.getDao().getHibernateSession().createSQLQuery("SELECT LOGINID FROM v_LoginID_provcoms_area WHERE LOGINID IN("+ids+") AND PROVCOMS=? AND AREA=?");
				query.setParameter(0, provice);
				query.setParameter(1, area);
				List<String> list = query.list();
				//把上面过滤的账号，得到系统中的User
				String loginids = "";
				for(int i=0;i<list.size();i++){
					if(i!=0)
						loginids+=",";
					loginids+="'"+(String)list.get(i)+"'";
				}
				if(!StringUtils.isEmpty(loginids)){
					resultList = this.getDao().queryByWhere(User.class, "  o.loginId IN("+loginids+")", null);
				}
			}
			
		} catch (HibernateException e) {
			e.printStackTrace();
		}
		return resultList;
	}
	
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
	public String updatePassWord(User user,String oldPassWord,String newPassWord)throws BizException{
		if(!(DigestUtils.md5Hex(oldPassWord)).equals(user.getPassword()))
			throw new BizException("输入旧密码不正确");
		user.setPassword(DigestUtils.md5Hex(newPassWord));
		this.save(user);
		return null;
	}
}
