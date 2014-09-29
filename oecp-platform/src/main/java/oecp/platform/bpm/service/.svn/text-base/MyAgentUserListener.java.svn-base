package oecp.platform.bpm.service;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import oecp.framework.dao.DAO;
import oecp.framework.util.SpringContextUtil;
import oecp.platform.bpm.eo.MyAgentUser;
import oecp.platform.bpm.eo.VirProActivity;
import oecp.platform.event.annotation.Listener;
import oecp.platform.org.eo.Organization;
import oecp.platform.user.eo.User;

import org.springframework.stereotype.Component;

/**
 * 任务节点配置保存监听器
 *
 * @author YangTao
 * @date 2012-2-24下午04:26:34
 * @version 1.0
 */
@Component
@Listener(source="oecp.platform.bpm.eo.VirProActivity")
public class MyAgentUserListener {
	
	private SimpleDateFormat dateformat=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	
	/**
	 * 如果在任务节点配置的时候，去掉已经选好的候选人，
	 * 而且这个流程定义被候选人委派代理人的时候，去删除委派代理人的信息
	 * 
	 * @author YangTao
	 * @date 2012-2-24下午05:05:23
	 * @param vpa
	 * @param org
	 * @param sources
	 */
	public void onChangeAgentUser(VirProActivity vpa,Organization org,Object...sources){
		ProDefinitionServiceImpl bpDefinitionService = (ProDefinitionServiceImpl)SpringContextUtil.getBean("bpDefinitionService");
		DAO dao = (DAO)SpringContextUtil.getBean("platformDao");
		List<MyAgentUser> list = dao.queryByWhere(MyAgentUser.class, "(SELECT rr FROM VirProDefinition rr WHERE rr=?) MEMBER OF o.virProDefinitions  and o.endTime is null", new Object[]{vpa.getVirProDefinition()});
		List<User> userList = null;
		if(list.size()!=0)
			userList = bpDefinitionService.getUsersOnVirProDefinition(vpa.getVirProDefinition());
		for(MyAgentUser myAgentUser : list){
			if(!userList.contains(myAgentUser.getUser())){
				myAgentUser.getVirProDefinitions().remove(vpa.getVirProDefinition());
				if(myAgentUser.getVirProDefinitions().size()==0)
					myAgentUser.setEndTime(dateformat.format(new Date()));
				dao.update(myAgentUser);
			}
		}
	}

}
