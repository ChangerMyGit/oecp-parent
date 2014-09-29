package oecp.framework.quartz.service;

import java.util.Date;
import java.util.Map;

import javax.annotation.Resource;

import oecp.framework.exception.BizException;

import org.quartz.CronExpression;
import org.quartz.CronTrigger;
import org.quartz.JobDataMap;
import org.quartz.JobDetail;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.quartz.SimpleTrigger;
import org.springframework.stereotype.Service;
/**
 * 定时服务
 *
 * @author YangTao
 * @date 2012-3-15上午09:06:25
 * @version 1.0
 */
public class TimerServiceImpl implements TimerService {
	
	/**
	 * 定时调度
	 */
	private Scheduler scheduler;
	
	/**
	 * simpletrigger开始定时任务,执行一次
	 * @param id
	 * @param jobClass
	 * @param startTime 开始日期
	 * @param endTime 结束日期
	 * @param map job中所需参数,传进去参数必须是序列化的
	 * @throws BizException
	 */
	@Override
	public void startSimpleJob(String id, Class jobClass,String jobListenerName,Date startTime,Date endTime,Map<Object,Object> map)
			throws SchedulerException {
		try {
			JobDetail jobDetail = new JobDetail(id,jobClass.getSimpleName(),jobClass);
			//处理job抛出异常的listener
			jobDetail.addJobListener(jobListenerName); 
			this.addJobParam(jobDetail, map);
			SimpleTrigger trigger = new SimpleTrigger(id,jobClass.getSimpleName());
			trigger.setStartTime(startTime);
			trigger.setEndTime(endTime);
			scheduler.scheduleJob(jobDetail, trigger);
		} catch (SchedulerException e) {
			e.printStackTrace();
			throw e;
		}
	}
	
	/**
	 * simpletrigger开始定时任务,执行多次
	 * @param id
	 * @param jobClass
	 * @param startTime 开始日期
	 * @param endTime 结束日期
	 * @param circleMinute 循环时间 ：分钟
	 * @param circleNum 循环次数 ：-1代表循环无限次，如果设置0就只会执行1次
	 * @param map job中所需参数,传进去参数必须是序列化的
	 * @throws BizException
	 */
	@Override
	public void startSimpleJob(String id, Class jobClass,String jobListenerName, Date startTime,Date endTime,
			int circleMinute, int circleNum,Map<Object,Object> map) throws SchedulerException {
		try {
			JobDetail jobDetail = new JobDetail(id,jobClass.getSimpleName(),jobClass);
			//处理job抛出异常的listener
			jobDetail.addJobListener(jobListenerName); 
			this.addJobParam(jobDetail, map);
			SimpleTrigger trigger = new SimpleTrigger(id,jobClass.getSimpleName());
			trigger.setStartTime(startTime);
			trigger.setEndTime(endTime);
			trigger.setRepeatInterval(circleMinute*1000);//循环
			trigger.setRepeatCount(circleNum);//-1代表循环无限次，如果设置0就只会执行1次
			scheduler.scheduleJob(jobDetail, trigger);
		} catch (SchedulerException e) {
			e.printStackTrace();
			throw e;
		}
		
	}
	
	/**
	 * crontigger根据时间表达式启动任务
	 * @param id
	 * @param jobClass
	 * @param startTime 开始日期
	 * @param endTime 结束日期
	 * @param cronExpression
	 * @param map job中所需参数,传进去参数必须是序列化的
	 * @throws BizException
	 */
	@Override
	public void startCronJob(String id, Class jobClass,String jobListenerName,Date startTime,Date endTime,
			String cronExpression,Map<Object,Object> map) throws SchedulerException {
		try {
			JobDetail jobDetail = new JobDetail(id,jobClass.getSimpleName(),jobClass);
			this.addJobParam(jobDetail, map);
			//处理job抛出异常的listener
			jobDetail.addJobListener(jobListenerName); 
			CronTrigger trigger = new CronTrigger(id,jobClass.getSimpleName());
			CronExpression cexp = new CronExpression(cronExpression);
			trigger.setStartTime(startTime);
			trigger.setEndTime(endTime);
			trigger.setCronExpression(cexp);
			scheduler.scheduleJob(jobDetail, trigger);
		} catch (Exception e) {
			e.printStackTrace();
			throw new SchedulerException(e);
		}
		
	}
	/**
	 * 删除定时任务
	 * 
	 * @author YangTao
	 * @date 2012-3-15上午09:08:30
	 * @param id
	 * @param jobClass
	 * @throws SchedulerException
	 */
	@Override
	public void deleteJob(String id, Class jobClass) throws SchedulerException {
		try {
			scheduler.deleteJob(id, jobClass.getSimpleName());
		} catch (SchedulerException e) {
			e.printStackTrace();
			throw e;
		}
		
	}
	
	/**
	 * 把传入的map中参数加入到job的map中
	 * @param jobDetail
	 * @param map
	 */
	private void addJobParam(JobDetail jobDetail,Map map){
		JobDataMap jd = new JobDataMap();
		if(map!=null)
			jd.putAll(map);
		jobDetail.setJobDataMap(jd);
	}
	
	
	public Scheduler getScheduler() {
		return scheduler;
	}
	
	public void setScheduler(Scheduler scheduler) {
		this.scheduler = scheduler;
	}
}
