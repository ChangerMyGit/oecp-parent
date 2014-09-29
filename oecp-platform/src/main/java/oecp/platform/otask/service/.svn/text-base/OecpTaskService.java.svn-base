package oecp.platform.otask.service;

import java.util.List;

import oecp.framework.dao.QueryCondition;
import oecp.framework.dao.QueryResult;
import oecp.framework.exception.BizException;
import oecp.framework.service.BaseService;
import oecp.platform.otask.eo.OecpTask;
import oecp.platform.otask.eo.OecpTaskGroup;
import oecp.platform.otask.eo.OecpTaskLog;
import oecp.platform.user.eo.User;
import oecp.platform.warning.enums.WarningType;
import oecp.platform.warning.eo.Warn;

public interface OecpTaskService  extends BaseService<OecpTask>{
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
	public  QueryResult<OecpTask> list(List<QueryCondition> conditions,int start,int limit);
	
	/**
	 * 日志列表
	 * @author Administrator
	 * @date 2012-11-30下午01:49:47
	 * @param conditions
	 * @param start
	 * @param limit
	 * @return
	 */
	public  QueryResult<OecpTaskLog> listLog(OecpTask task,int start,int limit);
	
	/**
	 * 获取OecpTask
	 * 
	 * @author YangTao
	 * @date 2012-3-8上午10:45:01
	 * @param warnId
	 * @return
	 */
	public OecpTask getOecpTaskById(String taskId)throws BizException;
	
	
	/**
	 * 启用或停用
	 * 
	 * @author YangTao
	 * @date 2012-3-16上午10:00:00
	 * @param warn
	 */
	public void startOecpTask(String taskId,User user)throws BizException;
	
	/**
	 * 
	 * @Desc 任务组列表 
	 * @author yangtao
	 * @date 2012-4-1
	 *
	 * @param conditions
	 * @param start
	 * @param limit
	 * @return
	 */
	public QueryResult<OecpTaskGroup> listGroup(List<QueryCondition> conditions,
			int start, int limit);
	/**
	 * 
	 * @Desc 新增任务组的保存 
	 * @author yangtao
	 * @date 2012-4-1
	 *
	 * @throws BizException
	 */
	public void groupSave(OecpTaskGroup taskGroup)throws BizException;
	
	/**
	 * 执行任务
	 * @author Administrator
	 * @date 2012-6-27下午01:21:24
	 * @param taskId
	 * @throws BizException
	 */
	public void runTask(String taskId)throws BizException;
	
	/**
	 * 定期去删除任务日志
	 * @author Administrator
	 * @date 2012-6-27下午02:44:54
	 * @throws BizException
	 */
	public void deleteTaskLog()throws BizException;
}
