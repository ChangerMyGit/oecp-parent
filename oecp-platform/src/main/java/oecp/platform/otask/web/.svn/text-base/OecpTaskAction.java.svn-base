package oecp.platform.otask.web;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import oecp.framework.dao.QueryCondition;
import oecp.framework.dao.QueryResult;
import oecp.framework.exception.BizException;
import oecp.framework.util.DateUtil;
import oecp.framework.web.JsonResult;
import oecp.platform.otask.eo.OecpTask;
import oecp.platform.otask.eo.OecpTaskGroup;
import oecp.platform.otask.eo.OecpTaskLog;
import oecp.platform.otask.service.OecpTaskService;
import oecp.platform.user.eo.User;
import oecp.platform.web.BasePlatformAction;

import org.apache.commons.lang.StringUtils;
import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.Namespace;
import org.apache.struts2.convention.annotation.ParentPackage;
import org.quartz.CronExpression;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

/**
 * 
 * @Desc 任务管理Action 
 * @author yangtao
 * @date 2012-4-6
 *
 */
@Controller
@Scope("prototype")
@ParentPackage("default-package")
@Namespace("/task/manage")
public class OecpTaskAction  extends BasePlatformAction {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;


	
	@Autowired
	private OecpTaskService oecpTaskService;
	
	private List<QueryCondition> conditions = new ArrayList<QueryCondition>();
	private OecpTask task;
	private String taskId;
	private String[] taskIds;
	private OecpTaskGroup taskGroup;
	private String cronExpression;
	
	/**
	 * 列表
	 * 
	 * @author YangTao
	 * @date 2012-3-6下午04:30:40
	 * @return
	 */
	@Action("list")
	public String list(){
		QueryResult<OecpTask> qr = oecpTaskService.list(conditions, start, limit);
		JsonResult jr = new JsonResult(qr.getTotalrecord().intValue(), qr.getResultlist());
		jr.setContainFields(new String[] { "id","name","oecpTaskGroup.name","createTime","taskitf","isStart","executeNum","description"});
		this.setJsonString(jr.toJSONString());
		return SUCCESS;
	}
	
	/**
	 * 日志列表
	 * @author Administrator
	 * @date 2012-11-30下午01:53:31
	 * @return
	 */
	@Action("viewlog")
	public String viewlog(){
		QueryResult<OecpTaskLog> qr = oecpTaskService.listLog(task, start, limit);
		JsonResult jr = new JsonResult(qr.getTotalrecord().intValue(), qr.getResultlist());
		jr.setContainFields(new String[] { "id","content","beginTime","endTime"});
		this.setJsonString(jr.toJSONString());
		return SUCCESS;
	}
	/**
	 * 编辑页面加载数据
	 * 
	 * @author YangTao
	 * @date 2012-3-8上午10:42:35
	 * @return
	 * @throws BizException
	 */
	@Action("loadData")
	public String loadData()throws BizException{
		task= this.oecpTaskService.getOecpTaskById(taskId);
		JsonResult jr = new JsonResult(this.task);
		jr.setContainFields(new String[] { "id","name","oecpTaskGroup.id","oecpTaskGroup.name","createTime","taskitf","methodName","methodParams","isStart","executeNum","description",
				"oecpTaskTimer.id","oecpTaskTimer.timerType","oecpTaskTimer.circleValue","oecpTaskTimer.circleNum","oecpTaskTimer.month","oecpTaskTimer.timerDayType",
				"oecpTaskTimer.day","oecpTaskTimer.week","oecpTaskTimer.hour",
				"oecpTaskTimer.minute","oecpTaskTimer.second","oecpTaskTimer.inputExpression","oecpTaskTimer.startTime","oecpTaskTimer.endTime"
				});
		this.setJsonString(jr.toJSONString());
		return SUCCESS;
	}
	/**
	 * 保存
	 * 
	 * @author YangTao
	 * @date 2012-3-7下午04:10:15
	 * @return
	 */
	@Action("save")
	public String save()throws BizException{
		if(StringUtils.isEmpty(task.getId()))
			task.setCreateTime(DateUtil.getDateStr(new Date(),"yyyy-MM-dd HH:mm:ss"));
		this.oecpTaskService.save(task);
		setJsonString("{success:true,msg:'保存成功！'}");
		return SUCCESS;
	}
	
	/**
	 * 删除
	 * 
	 * @author YangTao
	 * @date 2012-3-9下午03:17:01
	 * @return
	 * @throws BizException
	 */
	@Action("delete")
	public String delete()throws BizException{
		this.oecpTaskService.delete(taskIds);
		setJsonString("{success:true,msg:'删除成功！'}");
		return SUCCESS;
	}
	
	/**
	 * 启用或停用
	 * 
	 * @author YangTao
	 * @date 2012-3-16上午09:58:19
	 * @return
	 * @throws BizException
	 */
	@Action("start")
	public String start()throws BizException{
		User user = this.getOnlineUser().getUser();
		this.oecpTaskService.startOecpTask(taskId,user);
		setJsonString("{success:true,msg:'启用或停用成功！'}");
		return SUCCESS;
	}
	
	/**
	 * 列表
	 * 
	 * @author YangTao
	 * @date 2012-3-6下午04:30:40
	 * @return
	 */
	@Action("listgroup")
	public String listGroup(){
		QueryResult<OecpTaskGroup> qr = oecpTaskService.listGroup(conditions, start, limit);
		JsonResult jr = new JsonResult(qr.getTotalrecord().intValue(), qr.getResultlist());
		jr.setContainFields(new String[] { "id","name"});
		this.setJsonString(jr.toJSONString());
		return SUCCESS;
	}
	/**
	 * 新增任务组的保存
	 * @Desc 
	 * @author yangtao
	 * @date 2012-4-1
	 *
	 * @return
	 * @throws BizException
	 */
	@Action("groupsave")
	public String  groupSave()throws BizException{
		this.oecpTaskService.groupSave(taskGroup);
		setJsonString("{success:true,msg:'保存成功！'}");
		return SUCCESS;
	}
	
	/**
	 * 
	 * @Desc 校验cron表达式 
	 * @author yangtao
	 * @date 2012-4-5
	 *
	 * @return
	 * @throws BizException
	 */
	@Action("validateCronExp")
	public String validateCronExp()throws BizException{
		boolean re = CronExpression.isValidExpression(cronExpression);
		if(!re)
			setJsonString("{success:false,msg:'时间表达式校验未通过！'}");
		else
			setJsonString("{success:true,msg:'校验通过！'}");
		return SUCCESS;
	}
	
	public OecpTaskService getOecpTaskService() {
		return oecpTaskService;
	}
	public void setOecpTaskService(OecpTaskService oecpTaskService) {
		this.oecpTaskService = oecpTaskService;
	}
	public List<QueryCondition> getConditions() {
		return conditions;
	}
	public void setConditions(List<QueryCondition> conditions) {
		this.conditions = conditions;
	}
	public OecpTask getTask() {
		return task;
	}
	public void setTask(OecpTask task) {
		this.task = task;
	}
	public String getTaskId() {
		return taskId;
	}
	public void setTaskId(String taskId) {
		this.taskId = taskId;
	}
	
	public String[] getTaskIds() {
		return taskIds;
	}
	public void setTaskIds(String[] taskIds) {
		this.taskIds = taskIds;
	}
	public OecpTaskGroup getTaskGroup() {
		return taskGroup;
	}
	public void setTaskGroup(OecpTaskGroup taskGroup) {
		this.taskGroup = taskGroup;
	}
	public String getCronExpression() {
		return cronExpression;
	}
	public void setCronExpression(String cronExpression) {
		this.cronExpression = cronExpression;
	}

}
