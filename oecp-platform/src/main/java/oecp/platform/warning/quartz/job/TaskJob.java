package oecp.platform.warning.quartz.job;

import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.util.Date;
import java.util.Map;

import oecp.framework.util.SpringContextUtil;
import oecp.platform.otask.eo.OecpTask;
import oecp.platform.otask.eo.OecpTaskLog;
import oecp.platform.otask.itf.TaskItf;
import oecp.platform.otask.service.OecpTaskService;

import org.apache.commons.lang.StringUtils;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

/**
 * 
 * @Desc 任务管理处调用的job
 * @author yangtao
 * @date 2012-3-31
 *
 */
public class TaskJob implements Job{
	
	@Override
	public void execute(JobExecutionContext context)
			throws JobExecutionException {
		try {
			OecpTaskService oecpTaskService = (OecpTaskService)SpringContextUtil.getBean("oecpTaskService");
			//通过此方法获得需要的参数  
			Map parameters=context.getJobDetail().getJobDataMap();   
			String taskId = (String)parameters.get("taskId");
			oecpTaskService.runTask(taskId);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			throw new JobExecutionException(e);
		} 
	}

}
