/*
 * Copyright (c) 2011 OECP All Rights Reserved.                	
 * <a href="http://www.oecp.cn">http://www.oecp.cn</a>                                                                 
 */    

package oecp.platform.bpm.command;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.criterion.Restrictions;
import org.jbpm.api.cmd.Command;
import org.jbpm.api.cmd.Environment;
import org.jbpm.api.task.Task;
import org.jbpm.pvm.internal.task.ParticipationImpl;
import org.jbpm.pvm.internal.task.TaskImpl;

/**
 * 客户化任务查询
 * @author yangtao
 * @date 2011-8-17下午01:49:33
 * @version 1.0
 */
public class CustomTaskQueryCommand implements Command<Map> {
	private int taskPriority;
	private String taskDescLike;
	private Date taskCreateFrom;
	private Date taskCreateTo;
	private int start;
	private int limit;
	private String userName;
	
	//通过构造方法传入所需参数
	public CustomTaskQueryCommand(int taskPriority,String taskDescLike,Date taskCreateFrom,Date taskCreateTo,int start,int limit,String userName){
		this.taskPriority = taskPriority;
		this.taskDescLike = taskDescLike;
		this.taskCreateFrom = taskCreateFrom;
		this.taskCreateTo = taskCreateTo;
		this.start = start;
		this.limit = limit;
		this.userName = userName;
	}
	/* (non-Javadoc)
	 * @see org.jbpm.api.cmd.Command#execute(org.jbpm.api.cmd.Environment)
	 */
	@Override
	public Map execute(Environment environment) throws Exception {
		// TODO Auto-generated method stub
		Session session = environment.get(Session.class);
		Criteria criteria = session.createCriteria(TaskImpl.class);
		//增加查询的条件
		if(this.taskPriority !=0 ){
			criteria.add(Restrictions.eq("priority", this.taskPriority));
		}
		if(this.taskDescLike !=null ){
			criteria.add(Restrictions.like("description", "%"+this.taskDescLike));
		}
		if(this.taskCreateFrom !=null&&this.taskCreateTo!=null ){
			criteria.add(Restrictions.between("createTime", this.taskCreateFrom, this.taskCreateTo));
		}
		//这个增加的查询条件有点复杂，根据ParticipationImpl和TaskImpl的关系进行增加的
		if(this.userName !=null){
			List<Task> list = criteria.list();
			for(Task task : list){
				TaskImpl taskimpl = (TaskImpl)task;
				Criteria criteria2 = session.createCriteria(ParticipationImpl.class);
				criteria2.add(Restrictions.eq("userId", userName));
				criteria2.add(Restrictions.eq("type", ParticipationImpl.CANDIDATE));
				criteria2.add(Restrictions.eq("task", taskimpl));
				List list2 = criteria2.list();
				if(list2.size()==0){
					criteria.add(Restrictions.ne("dbid", taskimpl.getDbid()));
				}
			}
		}
		//查询结果
		Map map = new HashMap();
		int total = criteria.list().size();
		List<Task> resultList = criteria.setFirstResult(start).setMaxResults(limit).list();
		
		map.put("total", total);
		map.put("resultList", resultList);
		return map;
	}

}
