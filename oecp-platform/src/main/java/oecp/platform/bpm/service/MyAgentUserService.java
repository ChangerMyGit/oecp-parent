package oecp.platform.bpm.service;

import java.util.LinkedHashMap;
import java.util.List;

import oecp.framework.dao.QueryCondition;
import oecp.framework.dao.QueryResult;
import oecp.framework.exception.BizException;
import oecp.framework.service.BaseService;
import oecp.platform.bpm.eo.MyAgentUser;
import oecp.platform.bpm.vo.VirProcessDefinitionInfo;
import oecp.platform.org.eo.Organization;
import oecp.platform.user.eo.User;

public interface MyAgentUserService  extends BaseService<MyAgentUser>{
	/**
	 * 获得我的代理用户
	 * 
	 * @author YangTao
	 * @date 2012-2-6下午02:37:23
	 * @param user
	 * @return
	 */
	public QueryResult<MyAgentUser> getMyAgentUser(User user);
	
	/**
	 * 获取代理用户代理的流程列表范围
	 * 
	 * @author YangTao
	 * @date 2012-2-6下午02:45:00
	 * @param user
	 * @return
	 */
	public QueryResult<VirProcessDefinitionInfo> getMyAgentProcessDef(MyAgentUser myAgenter)throws BizException;
	/**
	 * 获取某个人在某个流程上面的代理人
	 * 
	 * @author YangTao
	 * @date 2012-2-3上午09:44:56
	 * @param virProDefinitionId
	 * @param user
	 * @return
	 */
	public User getMyAgentUser(String virProDefinitionId,User user);
	
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
			LinkedHashMap<String, String> orderby)throws BizException;
	/**
	 * 获取有我参与审批的流程定义列表
	 * 
	 * @author YangTao
	 * @date 2012-2-7上午11:15:55
	 * @param user
	 * @return
	 */
	public QueryResult<VirProcessDefinitionInfo> getMyProcessDef(MyAgentUser myAgenter,User user)throws BizException;
	
}
