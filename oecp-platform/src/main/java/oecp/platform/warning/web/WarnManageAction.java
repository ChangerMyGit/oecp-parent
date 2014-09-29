package oecp.platform.warning.web;

import java.util.ArrayList;
import java.util.List;

import oecp.framework.dao.QueryCondition;
import oecp.framework.dao.QueryResult;
import oecp.framework.exception.BizException;
import oecp.framework.util.FastJsonUtils;
import oecp.framework.web.JsonResult;
import oecp.platform.user.eo.User;
import oecp.platform.warning.enums.WarningType;
import oecp.platform.warning.eo.SendWarn;
import oecp.platform.warning.eo.Warn;
import oecp.platform.warning.service.WarnManageService;
import oecp.platform.web.BasePlatformAction;

import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.Namespace;
import org.apache.struts2.convention.annotation.ParentPackage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

/**
 * 预警管理action
 *
 * @author YangTao
 * @date 2012-3-6下午04:36:09
 * @version 1.0
 */
@Controller
@Scope("prototype")
@ParentPackage("default-package")
@Namespace("/warn/manage")
public class WarnManageAction extends BasePlatformAction {

	private static final long serialVersionUID = 1L;
	
	@Autowired
	private WarnManageService warnManageService;
	
	private List<QueryCondition> conditions = new ArrayList<QueryCondition>();
	private Warn warn;
	private String warnId;
	private String[] warnIds;
	
	/**
	 * 列表
	 * 
	 * @author YangTao
	 * @date 2012-3-6下午04:30:40
	 * @return
	 */
	@Action("list")
	public String list(){
		QueryResult<Warn> qr = warnManageService.list(conditions, start, limit);
		JsonResult jr = new JsonResult(qr.getTotalrecord().intValue(), qr.getResultlist());
		jr.setContainFields(new String[] { "id","name","warningType","warningitf","noticeNum","isStart"});
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
		warn= this.warnManageService.getWarnById(warnId);
		JsonResult jr = new JsonResult(this.warn);
		jr.setContainFields(new String[] { "id","name","warningType","warningitf","warnNoticeUserType","warnNoticeItem","noticeNum","noticeUsers","noticePosts","noticeRoles",
				"eventWarn.id","eventWarn.eventSource","eventWarn.event",
				"timerWarn.id","timerWarn.timerWarnType","timerWarn.circleValue","timerWarn.circleNum","timerWarn.month","timerWarn.timerWarnDayType","timerWarn.day","timerWarn.week","timerWarn.hour",
				"timerWarn.minute","timerWarn.second","timerWarn.inputExpression","timerWarn.startTime","timerWarn.endTime"
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
		this.warnManageService.save(warn);
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
		this.warnManageService.delete(warnIds);
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
		this.warnManageService.startWarn(warn,user);
		setJsonString("{success:true,msg:'启用或停用成功！'}");
		return SUCCESS;
	}

	/**
	 * 获取预警消息数量
	 * @return
	 */
	@Action(value = "sendWarnCount")
	public String sendWarnCount(){
		long count = warnManageService.getSendWarnCount(getOnlineUser().getUser());
		this.setJsonString("{count : "+count+" }");
		return SUCCESS;
	}
	
	/**
	 * 获取预警消息
	 * @return
	 */
	@Action(value = "getSendWarns")
	public String getSendWarns(){
		List<SendWarn> list = warnManageService.getSendWarns(getOnlineUser().getUser());
		this.setJsonString(FastJsonUtils.toJson(list,new String[]{"id","title","messageContent","noticedNum"}));
		return SUCCESS;
	}
	public WarnManageService getWarnManageService() {
		return warnManageService;
	}

	public void setWarnManageService(WarnManageService warnManageService) {
		this.warnManageService = warnManageService;
	}

	public Warn getWarn() {
		return warn;
	}

	public void setWarn(Warn warn) {
		this.warn = warn;
	}
	public List<QueryCondition> getConditions() {
		return conditions;
	}
	public void setConditions(List<QueryCondition> conditions) {
		this.conditions = conditions;
	}
	public String getWarnId() {
		return warnId;
	}
	public void setWarnId(String warnId) {
		this.warnId = warnId;
	}
	public String[] getWarnIds() {
		return warnIds;
	}
	public void setWarnIds(String[] warnIds) {
		this.warnIds = warnIds;
	}
	
}
