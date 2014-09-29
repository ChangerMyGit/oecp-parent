package oecp.platform.warning.itf.service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.annotation.Resource;

import oecp.framework.dao.DAO;
import oecp.framework.util.DateUtil;
import oecp.platform.bpm.eo.CandidateUser;
import oecp.platform.bpm.eo.TaskEo;
import oecp.platform.org.eo.Organization;
import oecp.platform.user.eo.User;
import oecp.platform.warning.eo.WarnItfContent;
import oecp.platform.warning.itf.WarningItf;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
/**
 * 待办任务超时的预警插件
 *
 * @author YangTao
 * @date 2012-3-22上午09:21:16
 * @version 1.0
 */
@Service("timeOutTaskItfImpl")
@Transactional
public class TimeOutTaskItfImpl implements WarningItf {

	@Resource(name="platformDao")
	private DAO dao;
	
	//超过多少天算作任务超时
	private int dayNum = 5;
	
	@Override
	public List<WarnItfContent> isWarningOnEvent(Object source,
			String eventName, Organization org, Object... objects) {
		// TODO Auto-generated method stub
		return null;
	}

	/**
	 * 待办任务超时5天的报警
	 */
	@Override
	public List<WarnItfContent> isWarningOnTimer() {
		// TODO Auto-generated method stub
		List<WarnItfContent> li = new ArrayList<WarnItfContent>();
		String sql = "SELECT DISTINCT o FROM TaskEo o WHERE o.isEnd=0 and (o.startTime<'"+DateUtil.getDateStr(DateUtil.addDay(new Date(),-this.dayNum),null)+"')";
		List<TaskEo> list = dao.getHibernateSession().createQuery(sql).list();
		for(TaskEo t : list){
			WarnItfContent wif = new WarnItfContent();
			wif.setIsWarning(true);
			wif.setKey("待办任务ID："+t.getId());
			wif.setMessageContent("请注意：待办任务(单据号:"+t.getVirProcessInstance().getBillInfo()+",名称："+t.getTaskName()+",组织："+t.getVirProcessInstance().getVirProDefinition().getAssignedOrg().getName()+")   已经超时，请快速办理！！！");
			List<User> u = new ArrayList<User>();
			for(CandidateUser cadu : t.getCandidateUsers()){
				if(!cadu.getIsExecutedTask()){
					u.add(cadu.getUser());
				}
			}
			wif.setUsers(u);
			li.add(wif);
		}
		return li;
	}

	public DAO getDao() {
		return dao;
	}

	public void setDao(DAO dao) {
		this.dao = dao;
	}

	

}
