package oecp.platform.otask.service;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import oecp.framework.dao.QueryCondition;
import oecp.framework.dao.QueryResult;
import oecp.framework.exception.BizException;
import oecp.framework.quartz.service.TimerService;
import oecp.framework.util.DateUtil;
import oecp.framework.util.SpringContextUtil;
import oecp.platform.base.service.PlatformBaseServiceImpl;
import oecp.platform.otask.enums.TimerType;
import oecp.platform.otask.eo.OecpTask;
import oecp.platform.otask.eo.OecpTaskGroup;
import oecp.platform.otask.eo.OecpTaskLog;
import oecp.platform.otask.eo.OecpTaskTimer;
import oecp.platform.otask.itf.TaskItf;
import oecp.platform.user.eo.User;
import oecp.platform.warning.quartz.job.TaskJob;

import org.apache.commons.lang.StringUtils;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
/**
 * 
 * @Desc 任务服务
 * @author yangtao
 * @date 2012-3-31
 *
 */
@Service("oecpTaskService")
public class OecpTaskServiceImpl extends PlatformBaseServiceImpl<OecpTask> implements OecpTaskService{

	@Resource
	private TimerService timerService;
	
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
	@Override
	public QueryResult<OecpTask> list(List<QueryCondition> conditions,
			int start, int limit) {
		LinkedHashMap<String,String> map = new LinkedHashMap<String,String>();
		map.put("createTime", "DESC");
		return this.getDao().getScrollData(OecpTask.class, start, limit, conditions, map);
	}
	
	/**
	 * 日志列表
	 * @author Administrator
	 * @date 2012-11-30下午01:49:47
	 * @param conditions
	 * @param start
	 * @param limit
	 * @return
	 */
	public  QueryResult<OecpTaskLog> listLog(OecpTask task,int start,int limit){
		List<QueryCondition> conditions = new ArrayList<QueryCondition>();
		QueryCondition qc = new QueryCondition();
		qc.setField("oecpTask.id");
		qc.setOperator("=");
		qc.setValue(task.getId());
		conditions.add(qc);
		LinkedHashMap<String,String> map = new LinkedHashMap<String,String>();
		map.put("beginTime", "DESC");
		return this.getDao().getScrollData(OecpTaskLog.class, start, limit, conditions, map);
	}
	/**
	 * 获取OecpTask
	 * 
	 * @author YangTao
	 * @date 2012-3-8上午10:45:01
	 * @param warnId
	 * @return
	 */
	@Override
	public OecpTask getOecpTaskById(String taskId) throws BizException {
		OecpTask oecpTask = this.find(taskId);
		oecpTask.loadLazyAttributes();
		return oecpTask;
	}
	
	/**
	 * 启用或停用
	 * 
	 * @author YangTao
	 * @date 2012-3-16上午10:00:00
	 * @param warn
	 */
	@Override
	public void startOecpTask(String taskId,User user) throws BizException {
		try {
			OecpTask oecpTask = this.find(taskId);
			OecpTaskTimer oecpTaskTimer = oecpTask.getOecpTaskTimer();
			if(oecpTask.getIsStart()){
				oecpTask.setIsStart(false);
				oecpTask.setExecuteNum(null);
				timerService.deleteJob(taskId, TaskJob.class);
			}else{
				Map<Object,Object> map = new HashMap<Object,Object>();
				map.put("taskId", taskId);
				map.put("name", oecpTask.getName());
				Date currentDate = new Date();
				Date startTime = oecpTaskTimer.getStartTime()!=null?(DateUtil.parseDate(oecpTaskTimer.getStartTime(),"yyyy-MM-dd HH:mm:ss")):currentDate;
				Date endTime = oecpTaskTimer.getEndTime()!=null?(DateUtil.parseDate(oecpTaskTimer.getEndTime(),"yyyy-MM-dd HH:mm:ss")):null;
				if(endTime!=null&&currentDate.compareTo(endTime)>0){
					throw new BizException("启用失败：结束日期小于当前日期,不能启动。");
				}
				if(oecpTaskTimer.getTimerType() == TimerType.TIMER_CIRCLE)
					timerService.startSimpleJob(taskId, TaskJob.class,"OECP-JobListener",startTime , endTime, Integer.parseInt(oecpTaskTimer.getCircleValue()), Integer.parseInt(oecpTaskTimer.getCircleNum()), map);
				else if(oecpTaskTimer.getTimerType() == TimerType.TIMER_SELECTED)
					timerService.startCronJob(taskId, TaskJob.class,"OECP-JobListener",startTime , endTime, oecpTaskTimer.getConExpression(), map);
				else
					timerService.startCronJob(taskId, TaskJob.class,"OECP-JobListener",startTime , endTime, oecpTaskTimer.getInputExpression(), map);
					
				oecpTask.setIsStart(true);
				oecpTask.setStartUser(user);
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			throw new BizException(e);
		}
	}

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
	@Override
	public QueryResult<OecpTaskGroup> listGroup(List<QueryCondition> conditions,
			int start, int limit) {
		LinkedHashMap<String,String> map = new LinkedHashMap<String,String>();
		return this.getDao().getScrollData(OecpTaskGroup.class, start, limit, conditions, map);
	}
	/**
	 * 
	 * @Desc 新增任务组的保存 
	 * @author yangtao
	 * @date 2012-4-1
	 *
	 * @throws BizException
	 */
	public void groupSave(OecpTaskGroup taskGroup)throws BizException{
		List<OecpTaskGroup> list = this.getDao().queryByWhere(OecpTaskGroup.class, "name=?", new Object[]{taskGroup.getName()});
		if(list.size()!=0){
			throw new BizException("任务组已经存在！");
		}else{
			this.getDao().create(taskGroup);
		}
	}
	
	/**
	 * 执行任务
	 * @author Administrator
	 * @date 2012-6-27下午01:21:24
	 * @param taskId
	 * @throws BizException
	 */
	@Transactional(propagation=Propagation.NOT_SUPPORTED)
	public void runTask(String taskId)throws BizException{
		OecpTaskLog log = new OecpTaskLog();
		try {
			OecpTask oecpTask = this.find(taskId);
			oecpTask.setExecuteNum(StringUtils.isNotEmpty(oecpTask.getExecuteNum())?(Integer.parseInt(oecpTask.getExecuteNum())+1)+"":1+"");
			this.save(oecpTask);
			//获取上一个日志
			LinkedHashMap<String,String> paramMap = new LinkedHashMap<String,String>();
			paramMap.put("endTime", "DESC");
			List<OecpTaskLog> list = this.getDao().queryByWhere(OecpTaskLog.class, "oecpTask=? and endTime is not null", new Object[]{oecpTask},paramMap);
			OecpTaskLog aboveLog = null;
			if(list!=null&&list.size()>0)
				aboveLog = list.get(0);
			//记录当前日志
			log.setBeginTime(new Date());
			log.setOecpTask(oecpTask);
			this.getDao().create(log);
			//调取插件,获取参数
			TaskItf itf = oecpTask.getTaskitfClass();
			Map map = oecpTask.getMethodMapParams();
			Class<?> currentClass = Class.forName(itf.getClass().getName());
			Object instance = SpringContextUtil.getApplicationContext().getBean(currentClass);
			//调用基类初始化方法
			this.reflectInvokeMethod(currentClass, instance, "init", new Class[]{OecpTaskLog.class,OecpTaskLog.class}, new Object[]{log,aboveLog});
			//调用注册任务方法
			this.reflectInvokeMethod(currentClass, instance, oecpTask.getMethodName(), new Class[]{Map.class}, new Object[]{map!=null?oecpTask.getMethodMapParams():null});
			log.setEndTime(new Date());
			this.getDao().update(log);
		}  catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			log.setEndTime(new Date());
			StringWriter sw = new StringWriter();
			PrintWriter pw = new PrintWriter(sw);
			e.printStackTrace(pw);
			log.setContent(sw.toString());
			this.getDao().update(log);
			throw new BizException(e);
		}
	}
	
	
	/**
	 * 反射调用方法
	 * @author YangTao
	 * @date 2012-4-9下午05:03:01
	 * @param className
	 * @param methodName
	 * @param paramsTypes
	 * @param paramsValues
	 * @return
	 * @throws Exception
	 */
	public Object reflectInvokeMethod(Class<?> currentClass,Object instance,String methodName,Class<?>[] paramsTypes,Object[] paramsValues)throws Exception{
		Object object = null;
		try {
			Method m = currentClass.getMethod(methodName,paramsTypes);
			object = m.invoke(instance,paramsValues);
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}
		return object;
	}
	
	/**
	 * 定期去删除任务日志
	 * @author Administrator
	 * @date 2012-6-27下午02:44:54
	 * @throws BizException
	 */
	public void deleteTaskLog()throws BizException{
		this.getDao().deleteByWhere(OecpTaskLog.class, null, null);
	}
	
	public TimerService getTimerService() {
		return timerService;
	}

	public void setTimerService(TimerService timerService) {
		this.timerService = timerService;
	}
	
}
