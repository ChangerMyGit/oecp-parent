package oecp.platform.warning.service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;

import javax.annotation.Resource;

import oecp.framework.dao.QueryCondition;
import oecp.framework.dao.QueryResult;
import oecp.framework.exception.BizException;
import oecp.platform.base.service.PlatformBaseServiceImpl;
import oecp.platform.org.eo.Person;
import oecp.platform.org.eo.Post;
import oecp.platform.org.service.PersonService;
import oecp.platform.role.eo.OrgRole;
import oecp.platform.user.eo.User;
import oecp.platform.user.service.UserService;
import oecp.platform.warning.enums.TimerWarnStartFlag;
import oecp.platform.warning.enums.WarnNoticeItem;
import oecp.platform.warning.enums.WarnNoticeUserType;
import oecp.platform.warning.enums.WarningType;
import oecp.platform.warning.eo.SendWarn;
import oecp.platform.warning.eo.Warn;
import oecp.platform.warning.eo.WarnItfContent;
import oecp.platform.warning.itf.WarningItf;
import oecp.platform.warning.responder.factory.WarningResponderFactory;

import org.hibernate.Query;
import org.springframework.stereotype.Service;


/**
 * 预警管理服务
 *
 * @author YangTao
 * @date 2012-3-6下午04:36:23
 * @version 1.0
 */
@Service("warnManageService")
public class WarnManageServiceImpl extends PlatformBaseServiceImpl<Warn> implements WarnManageService {
	
	@Resource
	private PersonService personService;

	@Resource
	private UserService userService;
	
	@Resource
	private WarningEngine warningEngine;
	
	@Override
	public QueryResult<Warn> list(List<QueryCondition> conditions, int start,
			int limit) {
		for(QueryCondition qc : conditions){
			if("warningType".equals(qc.getField())){
				if("EVENT_WARNING".equals(qc.getValue()))
					qc.setValue(WarningType.EVENT_WARNING);
				else
					qc.setValue(WarningType.TIMER_WARNING);
			}
		}
		LinkedHashMap<String,String> map = new LinkedHashMap<String,String>();
		map.put("isStart", "DESC");
		return this.getDao().getScrollData(Warn.class, start, limit, conditions, map);
	}
	
	/**
	 * 获取warn
	 * 
	 * @author YangTao
	 * @date 2012-3-8上午10:45:01
	 * @param warnId
	 * @return
	 */
	public Warn getWarnById(String warnId)throws BizException{
		Warn warn = this.find(warnId);
		warn.loadLazyAttributes();
		return warn;
	}
	
	/**
	 * 获取预警的插件
	 * 
	 * @author YangTao
	 * @date 2012-3-12上午10:14:29
	 * @param WarningType
	 * @param objects
	 * @return
	 */
	public List<Warn> getWarns(WarningType warningType,Object... objects){
		List<Warn> list = new ArrayList<Warn>();
		if(warningType==WarningType.EVENT_WARNING)
			list = this.getDao().queryByWhere(Warn.class, "isStart=? and eventWarn.eventSource=? and eventWarn.event=?", new Object[]{true,objects[0],objects[1]});
		else
			list = this.getDao().queryByWhere(Warn.class, "timerWarn.isStart=0", null);
		return list;
	}
	
	/**
	 * 获取预警配置的人员  角色、岗位转换的 
	 * 
	 * @author YangTao
	 * @date 2012-3-12上午10:41:46
	 * @param warn
	 * @return
	 */
	public List<User> getUsersOnWarn(Warn warn){
		List<User> list = new ArrayList<User>();
		try {
			warn = this.find(warn.getId());
		} catch (BizException e) {
			e.printStackTrace();
		}
		//第一  后台配置的人员
		if(warn.getWarnNoticeUserType()==WarnNoticeUserType.NOTICE_USER){
			list.addAll(warn.getNoticeUsers());
		}
		//第二  后台配置的岗位
		else if(warn.getWarnNoticeUserType()==WarnNoticeUserType.NOTICE_POST){
			for (Post p : warn.getNoticePosts()) {
				List<Person> ps = personService.getAllPersonsByPost(p
						.getId());
				// 根据岗位得到相应的用户
				for (Person person : ps) {
					oecp.platform.user.eo.User u = userService.getUserByPersonId(person.getId());
					if (u != null) {
						list.add(u);
					}
				}
			}
		}
		//第三  后台配置的角色
		else if(warn.getWarnNoticeUserType()==WarnNoticeUserType.NOTICE_ROLE){
			for (OrgRole or : warn.getNoticeRoles()) {
				List<oecp.platform.user.eo.User> rus = or.getUsers();
				list.addAll(rus);
			}
		}
		//去重,不保持顺序
		HashSet<User> set = new HashSet<User>();
		set.addAll(list);
		list = Arrays.asList(set.toArray(new User[0]));
		return list;
	}
	
	/**
	 * 启用或停用
	 * 
	 * @author YangTao
	 * @date 2012-3-16上午10:00:00
	 * @param warn
	 */
	public void startWarn(Warn warn,User user)throws BizException{
		try {
			warn = this.find(warn.getId());
		} catch (BizException e) {
			e.printStackTrace();
		}
		if(warn.getWarningType() == WarningType.EVENT_WARNING){//事件预警
			if(warn.getIsStart()){
				warn.setIsStart(false);
				//取消已经发送的预警
				this.getDao().deleteByWhere(SendWarn.class, "warn=?", new Object[]{warn});
			}
			else{
				warn.setIsStart(true);
			}
		}else{//定时器预警
			if(warn.getIsStart()){
				warn.setIsStart(false);
				//取消已经发送的预警
				this.warningEngine.onTimerWarn(warn, TimerWarnStartFlag.TIMER_STOP);
				this.getDao().deleteByWhere(SendWarn.class, "warn=?", new Object[]{warn});
			}else{
				warn.setIsStart(true);
				warn.setStartUser(user);
				this.warningEngine.onTimerWarn(warn, TimerWarnStartFlag.TIMER_START);
			}
				
		}
	}
	
	/**
	 * 获取当前登录人的预警数量
	 * 
	 * @author YangTao
	 * @date 2012-3-20上午10:58:13
	 * @param user
	 * @return
	 */
	public long getSendWarnCount(User user){
		String sql = "select count(*) from SendWarn m where m.user.id=?";
		Query query = getDao().getHibernateSession().createQuery(sql);
		query.setString(0, user.getId());
		List<Long> list = query.list();
		return list.get(0);
	}
	
	/**
	 * 
	 * 获取当前登录人的预警
	 * 
	 * @author YangTao
	 * @date 2012-3-20上午11:09:11
	 * @param user
	 * @return
	 */
	public List<SendWarn>  getSendWarns(User user){
		return this.getDao().queryByWhere(SendWarn.class, "user=?", new Object[]{user});
	}

	public PersonService getPersonService() {
		return personService;
	}

	public void setPersonService(PersonService personService) {
		this.personService = personService;
	}

	public UserService getUserService() {
		return userService;
	}

	public void setUserService(UserService userService) {
		this.userService = userService;
	}

	public WarningEngine getWarningEngine() {
		return warningEngine;
	}

	public void setWarningEngine(WarningEngine warningEngine) {
		this.warningEngine = warningEngine;
	}

	/* (non-Javadoc)
	 * @see oecp.platform.warning.service.WarnManageService#runWarn(java.lang.String)
	 */
	@Override
	public void runWarn(String warnId) throws Exception {
		try {
			Warn warn = getDao().find(Warn.class, warnId);
//			int d = 1/0;
			//调取预警插件,做出预警响应;保留之前的，删除多余的预警消息
			WarningItf wif = warn.getClassWarningItf();
			List<WarnItfContent> list = wif.isWarningOnTimer();
			if(list!=null&&list.size()!=0){//存在预警
				//设置删除状态
				List<SendWarn> lii = getDao().queryByWhere(SendWarn.class, "warn=?", new Object[]{warn});
				for(SendWarn s : lii){
					s.setDeleteFlag(true);
					getDao().update(s);
				}
				for(WarnItfContent warnItfContent : list){
					List<User> users = (List<User>)warnItfContent.getUsers();
					if(users!=null){//预警插件返回通知人员优先级高
						
					}else{//其次才是后台配置的通知人员
						users = this.getUsersOnWarn(warn);
					}
					WarnNoticeItem[] wni = warn.getWarnNoticeItem();
					
					//保存已经发送的预警
					for(User user : users){
						SendWarn sdw = getDao().findByWhere(SendWarn.class, "user=? and warn=? and key=?", new Object[]{user,warn,warnItfContent.getKey()});
						if(sdw == null){
							SendWarn sw = new SendWarn();
							sw.setTitle("关于"+warn.getName()+"的预警");
							sw.setMessageContent(warnItfContent.getMessageContent());
							sw.setNoticedNum(1);
							sw.setWarn(warn);
							sw.setUser(user);
							sw.setKey(warnItfContent.getKey());
							sw.setDeleteFlag(false);
							getDao().create(sw);
							if(wni!=null){
								for(WarnNoticeItem wn : wni){
									WarningResponderFactory.getWarningResponder(wn).doWarning("来自"+warn.getName()+"的预警", warnItfContent.getMessageContent(), user);
								}
							}
						}else{
							if(sdw.getNoticedNum()<warn.getNoticeNum()){
								sdw.setDeleteFlag(false);
								sdw.setNoticedNum(sdw.getNoticedNum()+1);
								getDao().update(sdw);
								if(wni!=null){
									for(WarnNoticeItem wn : wni){
										WarningResponderFactory.getWarningResponder(wn).doWarning("来自"+warn.getName()+"的预警", warnItfContent.getMessageContent(), user);
									}
								}
							}else{
								sdw.setDeleteFlag(false);
								getDao().update(sdw);
							}
						}
					}
				}
				//根据删除状态进行删除
				getDao().deleteByWhere(SendWarn.class, "warn=? and deleteFlag=?", new Object[]{warn,true});
			}else{//没有预警
				getDao().deleteByWhere(SendWarn.class, "warn=?", new Object[]{warn});
			}
		} catch (Exception e) {
			throw e;
		}
	}
	
}
