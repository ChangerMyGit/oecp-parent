package oecp.framework.quartz.service;

import java.util.Date;
import java.util.Map;

import oecp.framework.exception.BizException;

import org.quartz.JobListener;
import org.quartz.SchedulerException;

/**
 * 定时服务
 * 基于 Quartz框架
 * @author YangTao
 * @date 2012-3-15上午09:06:25
 * @version 1.0
 */
public interface TimerService {
	/**
	 * simpletrigger开始定时任务,执行一次
	 * @param id
	 * @param jobClass
	 * @param jobListenerName jobListener中的名字
	 * @param startTime 开始日期
	 * @param endTime 结束日期
	 * @param map job中所需参数,传进去参数必须是序列化的
	 * @throws BizException
	 */
	public void startSimpleJob(String id,Class jobClass,String jobListenerName, Date startTime,Date endTime,Map<Object,Object> map)throws SchedulerException;
	
	/**
	 * simpletrigger开始定时任务,执行多次
	 * @param id
	 * @param jobClass
	 * @param jobListenerName jobListener中的名字
	 * @param startTime 开始日期
	 * @param endTime 结束日期
	 * @param circleMinute 循环时间 ：分钟
	 * @param circleNum 循环次数 ：-1代表循环无限次，如果设置0就只会执行1次
	 * @param map job中所需参数,传进去参数必须是序列化的
	 * @throws BizException
	 */
	public void startSimpleJob(String id,Class jobClass,String jobListenerName, Date startTime,Date endTime,int circleMinute,int circleNum,Map<Object,Object> map)throws SchedulerException;
	
	/**
	 * crontigger根据时间表达式启动任务
	 * @param id
	 * @param jobClass
	 * @param jobListenerName jobListener中的名字
	 * @param startTime 开始日期
	 * @param endTime 结束日期
	 * @param cronExpression
	 * @param map job中所需参数,传进去参数必须是序列化的
	 * @throws BizException
	 */
	public void startCronJob(String id,Class jobClass,String jobListenerName, Date startTime,Date endTime,String cronExpression,Map<Object,Object> map)throws SchedulerException;
	/**
	 * 删除定时任务
	 * 
	 * @author YangTao
	 * @date 2012-3-15上午09:08:30
	 * @param id
	 * @param jobClass
	 * @throws SchedulerException
	 */
	void deleteJob(String id,Class jobClass)throws SchedulerException;

}
