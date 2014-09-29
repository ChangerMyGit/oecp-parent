package oecp.platform.warning.quartz.job.listener;

import java.util.Map;

import oecp.framework.dao.DAO;
import oecp.framework.util.SpringContextUtil;
import oecp.platform.message.service.MessageService;
import oecp.platform.otask.eo.OecpTask;
import oecp.platform.warning.eo.Warn;

import org.apache.commons.lang.xwork.StringUtils;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.quartz.JobListener;
/**
 * 
 * @Desc 处理warn task定时job执行时出错的异常 
 * @author yangtao
 * @date 2012-4-9
 *
 */
public class HandleJobExceptionListener implements JobListener {

	@Override
	public String getName() {
		// TODO Auto-generated method stub
		return "OECP-JobListener";
	}

	@Override
	public void jobToBeExecuted(JobExecutionContext context) {
		// TODO Auto-generated method stub

	}

	@Override
	public void jobExecutionVetoed(JobExecutionContext context) {
		// TODO Auto-generated method stub

	}

	/**
	 * 获取job异常，给启用人发消息
	 */
	@Override
	public void jobWasExecuted(JobExecutionContext context,
			JobExecutionException jobException) {
		if(jobException != null){
			try {
				DAO dao = (DAO)SpringContextUtil.getBean("platformDao");
				MessageService messageService = (MessageService)SpringContextUtil.getBean("messageService");
				//停止Scheduler
				Map parameters=context.getJobDetail().getJobDataMap();   
				String taskId = (String)parameters.get("taskId");
				StringBuffer title = new StringBuffer();
				if(StringUtils.isNotEmpty(taskId)){//任务管理
					OecpTask task = dao.find(OecpTask.class, taskId);
//					context.getScheduler().deleteJob(context.getJobDetail().getName(), TaskJob.class.getSimpleName());
					title.append("定时任务("+task.getName()+")执行失败，请查看任务插件和配置信息");
					// 给启用人发送消息
					messageService.sendMessage(title.toString(), jobException.getMessage(), task.getStartUser().getId());
				}else{//预警管理
					String warnId = (String)parameters.get("warnId");
					if(StringUtils.isNotEmpty(warnId)){
						Warn warn = dao.find(Warn.class, warnId);
//						context.getScheduler().deleteJob(context.getJobDetail().getName(), WarningJob.class.getSimpleName());
						title.append("预警("+warn.getName()+")执行失败，请查看预警插件和配置信息");
						// 给启用人发送消息
						messageService.sendMessage(title.toString(), jobException.getMessage(), warn.getStartUser().getId());
					}
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}
}
