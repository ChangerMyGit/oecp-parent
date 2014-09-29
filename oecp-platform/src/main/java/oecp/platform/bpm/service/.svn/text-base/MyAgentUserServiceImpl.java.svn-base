package oecp.platform.bpm.service;


import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;

import javax.annotation.Resource;

import oecp.framework.dao.QLBuilder;
import oecp.framework.dao.QueryCondition;
import oecp.framework.dao.QueryResult;
import oecp.framework.exception.BizException;
import oecp.platform.base.service.PlatformBaseServiceImpl;
import oecp.platform.bpm.eo.MyAgentUser;
import oecp.platform.bpm.eo.VirProDefinition;
import oecp.platform.bpm.vo.VirProcessDefinitionInfo;
import oecp.platform.org.eo.Organization;
import oecp.platform.org.eo.Post;
import oecp.platform.org.service.PostService;
import oecp.platform.user.eo.User;
import oecp.platform.user.service.UserService;

import org.apache.commons.lang.StringUtils;
import org.hibernate.Session;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


/**
 * 我的代理用户管理
 *
 * @author YangTao
 * @date 2012-3-2下午01:28:23
 * @version 1.0
 */
@Service("myAgentUserService")
@Transactional
public class MyAgentUserServiceImpl extends PlatformBaseServiceImpl<MyAgentUser> implements MyAgentUserService {

	@Resource
	PostService postService;
	@Resource
	private UserService userService;
	/**
	 * 获得我的代理用户
	 * 
	 * @author YangTao
	 * @date 2012-2-6下午02:37:23
	 * @param user
	 * @return
	 */
	public QueryResult<MyAgentUser> getMyAgentUser(User user){
		QueryResult<MyAgentUser> qr = new QueryResult<MyAgentUser>();
		List<MyAgentUser> list = this.getDao().queryByWhere(MyAgentUser.class, "o.user=?  and o.endTime is null", new Object[]{user});
		qr.setResultlist(list);
		qr.setTotalrecord(new Long(list.size()));
		return qr;
	}
	
	/**
	 * 获取代理用户代理的流程列表范围
	 * 
	 * @author YangTao
	 * @date 2012-2-6下午02:45:00
	 * @param user
	 * @return
	 */
	public QueryResult<VirProcessDefinitionInfo> getMyAgentProcessDef(MyAgentUser myAgenter)throws BizException{
		QueryResult<VirProcessDefinitionInfo> qr = new QueryResult<VirProcessDefinitionInfo>();
		if(myAgenter!=null){
			MyAgentUser myAgent = this.find(myAgenter.getId());
			List<VirProcessDefinitionInfo> resultList = new ArrayList<VirProcessDefinitionInfo>();
			for(VirProDefinition v : myAgent.getVirProDefinitions()){
				resultList.add(this.setVirProcessDefVo(v));
			}
			qr.setResultlist(resultList);
			qr.setTotalrecord(new Long(resultList.size()));
		}
		return qr;
	}
	
	/**
	 * 获取某个人在某个流程上面的代理人
	 * 
	 * @author YangTao
	 * @date 2012-2-3上午09:44:56
	 * @param virProDefinitionId
	 * @param user
	 * @return
	 */
	public User getMyAgentUser(String virProDefinitionId,User user){
		List<MyAgentUser> list = this.getDao().queryByWhere(MyAgentUser.class, "(SELECT rr FROM VirProDefinition rr WHERE rr.id=?) MEMBER OF o.virProDefinitions  and o.user=? and o.endTime is null", new Object[]{virProDefinitionId,user});
		User agent = null;
		if(list.size()!=0){
			MyAgentUser mac = list.get(0);
			agent = mac.getAgent();
		}
		return agent;
	}
	
	/**
	 * 添加或修改代理人时的用户列表（自己、自己已经有的代理人不在里面）
	 * 
	 * @author YangTao
	 * @date 2012-2-10上午09:25:04
	 * @param orgId
	 * @param conditions
	 * @param start
	 * @param limit
	 * @param orderby
	 * @return
	 */
	public QueryResult<User> getAllUsers(MyAgentUser myAgenter,User user,List<QueryCondition> conditions, int start, int limit,
			LinkedHashMap<String, String> orderby)throws BizException{
		user = this.userService.find(user.getId());
		String orgId = user.getCreatedByOrg().getId();
		QueryCondition condition = new QueryCondition("id", "!=",
				user.getId());
		conditions.add(condition);
		List<MyAgentUser> list = this.getMyAgentUser(user).getResultlist();
		for(MyAgentUser mau : list){
			if(myAgenter==null||(myAgenter!=null&&!mau.getId().equals(myAgenter.getId()))){
				QueryCondition con = new QueryCondition("id", "!=",
						mau.getAgent().getId());
				conditions.add(con);
			}
		}
		QueryResult<User> qr = this.userService.getUsersByOrgID(orgId,false,
				conditions, -1, -1, orderby);
		return qr;
	}
	
	/**
	 * 获取有我参与审批的流程定义列表
	 * 先根据配置的角色、用户、岗位获取我参与的流程定义，
	 * 然后再去除掉我的代理人配置的流程定义，
	 * 
	 * @author YangTao
	 * @date 2012-2-7上午11:15:55
	 * @param user
	 * @return
	 */
	public QueryResult<VirProcessDefinitionInfo> getMyProcessDef(MyAgentUser myAgenter,User user)throws BizException{
		user = this.userService.find(user.getId());
		Organization org = user.getCreatedByOrg();
		List<VirProDefinition> list1 = new ArrayList<VirProDefinition>();
		List<VirProDefinition> list2 = new ArrayList<VirProDefinition>();
		List<VirProDefinition> list3 = new ArrayList<VirProDefinition>();
		List<VirProDefinition> list4 = new ArrayList<VirProDefinition>();
		Session session = this.getDao().getHibernateSession();
		String sql = "SELECT DISTINCT o.virProDefinition FROM VirProActivity o ";
		//根据岗位查询
		List<Post> li = postService.getPosts(user.getId(), org.getId());
		if(li.size()!=0){
			List<String> lis = new ArrayList<String>();
			for(Post post : li){
				lis.add(post.getId());
			}
			String sqlparam = QLBuilder.toSQLIn(lis);
			String postSql = " LEFT JOIN o.assignPosts ap WHERE ap IN("+sqlparam+")";
			list2 = session.createQuery(sql+postSql).list();
		}
		//根据用户查询
		String userSql = " LEFT JOIN o.assignUsers au WHERE au.loginId='"+user.getLoginId()+"' ";
		//根据角色查询
		String orgRoleSql = " LEFT JOIN o.assignRoles ar LEFT JOIN ar.users aru WHERE aru.loginId='"+user.getLoginId()+"'";
		list1 = session.createQuery(sql+userSql).list();
		list3 = session.createQuery(sql+orgRoleSql).list();
		//去重,不保持顺序
		HashSet<VirProDefinition> set = new HashSet<VirProDefinition>();
		set.addAll(list1);
		set.addAll(list2);
		set.addAll(list3);
		List<VirProDefinition> list = new ArrayList(Arrays.asList(set.toArray()));
		String hassql = "SELECT DISTINCT vpd FROM MyAgentUser o join o.virProDefinitions vpd WHERE o.endTime is null  AND o.user.id='"+user.getId()+"'";
		if(StringUtils.isNotEmpty(myAgenter.getId())){
			hassql+=" AND o.id!='"+myAgenter.getId()+"'";
		}
		list4 = session.createQuery(hassql).list();
		list.removeAll(list4);
		QueryResult<VirProcessDefinitionInfo> qr = new QueryResult<VirProcessDefinitionInfo>();
		List<VirProcessDefinitionInfo> resultList = new ArrayList<VirProcessDefinitionInfo>();
		for(VirProDefinition v : list){
			resultList.add(this.setVirProcessDefVo(v));
		}
		qr.setResultlist(resultList);
		qr.setTotalrecord(new Long(resultList.size()));
		return qr;
	}
	
	
	/**
	 * 组装VirProcessDefinitionInfo
	 * 
	 * @author YangTao
	 * @date 2012-2-7下午02:44:49
	 * @param v
	 * @return
	 */
	private VirProcessDefinitionInfo setVirProcessDefVo(VirProDefinition v){
		VirProcessDefinitionInfo vi = new VirProcessDefinitionInfo();
		vi.setId(v.getId());
		vi.setName(v.getName());
		vi.setProDefinitionId(v.getProDefinition().getId());
		vi.setProDefinitionName(v.getProDefinition().getName());
		vi.setDeployId(v.getProDefinition().getDeployId());
		vi.setProcessDefinitionId(v.getProDefinition().getProDefId());
		vi.setAssignedOrgId(v.getAssignedOrg().getId());
		vi.setAssignedOrgName(v.getAssignedOrg().getName());
		vi.setIsUseId(v.getIsUse()?"1":"0");
		vi.setIsUseName(v.getIsUse()?"已启用":"未启用");
		vi.setBelongFunctionId(v.getProDefinition().getBelongFunction().getId());
		vi.setBelongFunctionName(v.getProDefinition().getBelongFunction().getName());
		return vi;
	}

	public UserService getUserService() {
		return userService;
	}

	public void setUserService(UserService userService) {
		this.userService = userService;
	}
	
	
}
