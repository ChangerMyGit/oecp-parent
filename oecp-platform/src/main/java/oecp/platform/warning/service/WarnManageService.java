package oecp.platform.warning.service;

import java.util.List;

import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

import oecp.framework.dao.QueryCondition;
import oecp.framework.dao.QueryResult;
import oecp.framework.exception.BizException;
import oecp.framework.service.BaseService;
import oecp.platform.user.eo.User;
import oecp.platform.warning.enums.WarningType;
import oecp.platform.warning.eo.SendWarn;
import oecp.platform.warning.eo.Warn;

/**
 * 预警管理服务
 *
 * @author YangTao
 * @date 2012-3-6下午04:36:23
 * @version 1.0
 */
public interface WarnManageService extends BaseService<Warn>{

	/**
	 * 列表数据
	 * 
	 * @author YangTao
	 * @date 2012-3-6下午04:28:03
	 * @param conditions
	 * @param start
	 * @param limit
	 * @return
	 */
	public  QueryResult<Warn> list(List<QueryCondition> conditions,int start,int limit);
	
	/**
	 * 获取warn
	 * 
	 * @author YangTao
	 * @date 2012-3-8上午10:45:01
	 * @param warnId
	 * @return
	 */
	public Warn getWarnById(String warnId)throws BizException;
	
	/**
	 * 获取预警
	 * 
	 * @author YangTao
	 * @date 2012-3-12上午10:14:29
	 * @param WarningType
	 * @param objects
	 * @return
	 */
	public List<Warn> getWarns(WarningType warningType,Object... objects);
	
	/**
	 * 获取预警配置的人员  角色、岗位转换的 
	 * 
	 * @author YangTao
	 * @date 2012-3-12上午10:41:46
	 * @param warn
	 * @return
	 */
	public List<User> getUsersOnWarn(Warn warn);
	
	/**
	 * 启用或停用
	 * 
	 * @author YangTao
	 * @date 2012-3-16上午10:00:00
	 * @param warn
	 */
	public void startWarn(Warn warn,User user)throws BizException;
	
	/**
	 * 获取当前登录人的预警数量
	 * 
	 * @author YangTao
	 * @date 2012-3-20上午10:58:13
	 * @param user
	 * @return
	 */
	public long getSendWarnCount(User user);
	
	/**
	 * 
	 * 获取当前登录人的预警
	 * 
	 * @author YangTao
	 * @date 2012-3-20上午11:09:11
	 * @param user
	 * @return
	 */
	public List<SendWarn>  getSendWarns(User user);
	
	/**
	 * 根据预警配置ID，运行预警插件。
	 * @date 2012-6-6上午11:27:25
	 * @param warnId
	 */
	public void runWarn(String warnId) throws Exception ;
	
}
