package oecp.platform.warning.quartz.job;

import java.util.List;
import java.util.Map;

import oecp.framework.dao.DAO;
import oecp.framework.util.SpringContextUtil;
import oecp.platform.user.eo.User;
import oecp.platform.warning.enums.WarnNoticeItem;
import oecp.platform.warning.eo.SendWarn;
import oecp.platform.warning.eo.Warn;
import oecp.platform.warning.eo.WarnItfContent;
import oecp.platform.warning.itf.WarningItf;
import oecp.platform.warning.responder.factory.WarningResponderFactory;
import oecp.platform.warning.service.WarnManageService;

import org.hibernate.Session;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
/**
 * 定时器预警时，定时执行的任务，任务里面去调用预警插件进行预警
 *
 * @author YangTao
 * @date 2012-3-15上午09:36:52
 * @version 1.0
 */
public class WarningJob implements Job {

	@Override
	public void execute(JobExecutionContext arg0) throws JobExecutionException {	
		WarnManageService warnManageService = (WarnManageService)SpringContextUtil.getBean("warnManageService");
		//通过此方法获得需要的参数  
		Map parameters=arg0.getJobDetail().getJobDataMap();   
		String warnId = (String)parameters.get("warnId");
		try {
			warnManageService.runWarn(warnId);
		} catch (Exception e) {
			e.printStackTrace();
			throw new JobExecutionException(e);
		}
	}

}
