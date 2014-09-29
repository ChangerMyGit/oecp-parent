package oecp.platform.warning.service;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import oecp.framework.dao.DAO;
import oecp.framework.exception.BizException;
import oecp.framework.quartz.service.TimerService;
import oecp.framework.util.DateUtil;
import oecp.framework.util.SpringContextUtil;
import oecp.platform.org.eo.Organization;
import oecp.platform.user.eo.User;
import oecp.platform.warning.enums.TimerWarnStartFlag;
import oecp.platform.warning.enums.TimerWarnType;
import oecp.platform.warning.enums.WarnNoticeItem;
import oecp.platform.warning.enums.WarningType;
import oecp.platform.warning.eo.SendWarn;
import oecp.platform.warning.eo.Warn;
import oecp.platform.warning.eo.WarnItfContent;
import oecp.platform.warning.itf.WarningItf;
import oecp.platform.warning.quartz.job.WarningJob;
import oecp.platform.warning.responder.factory.WarningResponderFactory;

import org.springframework.stereotype.Service;

/**
 * 预警引擎
 *
 * @author YangTao
 * @date 2012-3-12上午10:11:21
 * @version 1.0
 */
@Service("warningEngine")
public class WarningEngineImpl implements WarningEngine {
	
	@Resource
	private WarnManageService warnManageService;
	@Resource
	private TimerService timerService;
	
	/**
	 * 事件预警
	 * 
	 * @author YangTao
	 * @date 2012-3-12上午10:26:39
	 */
	@Override
	public void onEventWarn(Object source, String eventName, Organization org,Object... objects) throws BizException{
		try {
			List<Warn> list = warnManageService.getWarns(WarningType.EVENT_WARNING, source.getClass().getName(),eventName);
			DAO dao = (DAO)SpringContextUtil.getBean("platformDao");
			for(Warn warn : list){
				//调取预警插件,做出预警响应
				WarningItf wif = warn.getClassWarningItf();
				List<WarnItfContent> li = wif.isWarningOnEvent(source, eventName, org, objects);
				for(WarnItfContent warnItfContent : li){
					if((Boolean)warnItfContent.getIsWarning()){//发出预警
						this.doEventWarningResponder(warn, warnItfContent, dao);
					}else{
						dao.deleteByWhere(SendWarn.class, "warn=? and key=?", new Object[]{warn,warnItfContent.getKey()});
					}
				}
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			throw new BizException(e);
		} 
	}
	
	/**
	 * 定时器预警
	 * 
	 * @author YangTao
	 * @date 2012-3-15上午09:24:48
	 * @throws BizException
	 */
	@Override
	public void onTimerWarn(Warn warn,TimerWarnStartFlag timerWarnStartFlag) throws BizException {
		// TODO Auto-generated method stub
		try {
			if(timerWarnStartFlag == TimerWarnStartFlag.TIMER_START){
				Date startTime = warn.getTimerWarn().getStartTime()!=null?(DateUtil.parseDate(warn.getTimerWarn().getStartTime(),"yyyy-MM-dd HH:mm:ss")):new Date();
				Date endTime = warn.getTimerWarn().getEndTime()!=null?(DateUtil.parseDate(warn.getTimerWarn().getEndTime(),"yyyy-MM-dd HH:mm:ss")):null;
				Map<Object,Object> map = new HashMap<Object,Object>();
				map.put("warnId", warn.getId());
				if(warn.getTimerWarn().getTimerWarnType()==TimerWarnType.TIMER_CIRCLE){
					this.timerService.startSimpleJob(warn.getId(), WarningJob.class,"OECP-JobListener", startTime,endTime,Integer.parseInt(warn.getTimerWarn().getCircleValue()),Integer.parseInt(warn.getTimerWarn().getCircleNum()),map);
				}else if(warn.getTimerWarn().getTimerWarnType()==TimerWarnType.TIMER_SELECTED){
					this.timerService.startCronJob(warn.getId(), WarningJob.class,"OECP-JobListener", startTime,endTime, warn.getTimerWarn().getConExpression(), map);
				}else{
					this.timerService.startCronJob(warn.getId(), WarningJob.class,"OECP-JobListener", startTime,endTime, warn.getTimerWarn().getInputExpression(), map);
				}
			}else
				timerService.deleteJob(warn.getId(), WarningJob.class);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			throw new BizException(e);
		}
	}

	private void doEventWarningResponder(Warn warn,WarnItfContent warnItfContent,DAO dao)throws BizException {
		try {
			List<User> users = (List<User>)warnItfContent.getUsers();
			if(users!=null){//预警插件返回通知人员优先级高
				
			}else{//其次才是后台配置的通知人员
				users = warnManageService.getUsersOnWarn(warn);
			}
			WarnNoticeItem[] wni = warn.getWarnNoticeItem();
			//保存已经发送的预警
			for(User user : users){
				SendWarn sdw = dao.findByWhere(SendWarn.class, "user=? and warn=? and key=?", new Object[]{user,warn,warnItfContent.getKey()});
				if(sdw == null){
					SendWarn sw = new SendWarn();
					sw.setTitle("关于"+warn.getName()+"的预警");
					sw.setMessageContent(warnItfContent.getMessageContent());
					sw.setNoticedNum(1);
					sw.setWarn(warn);
					sw.setUser(user);
					sw.setKey(warnItfContent.getKey());
					dao.create(sw);
					if(wni!=null){
						for(WarnNoticeItem wn : wni){
							WarningResponderFactory.getWarningResponder(wn).doWarning("来自"+warn.getName()+"的预警", warnItfContent.getMessageContent(), user);
						}
					}
				}else{
					if(sdw.getNoticedNum()<warn.getNoticeNum()){
						sdw.setNoticedNum(sdw.getNoticedNum()+1);
						dao.update(sdw);
						if(wni!=null){
							for(WarnNoticeItem wn : wni){
								WarningResponderFactory.getWarningResponder(wn).doWarning("来自"+warn.getName()+"的预警", warnItfContent.getMessageContent(), user);
							}
						}
					}
				}
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			throw new BizException(e);
		}
	}
	
	public WarnManageService getWarnManageService() {
		return warnManageService;
	}

	public void setWarnManageService(WarnManageService warnManageService) {
		this.warnManageService = warnManageService;
	}

	public TimerService gettimerService() {
		return timerService;
	}

	public void settimerService(TimerService timerService) {
		this.timerService = timerService;
	}
	
	
	
}
