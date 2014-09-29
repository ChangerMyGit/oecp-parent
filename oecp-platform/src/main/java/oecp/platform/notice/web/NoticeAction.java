package oecp.platform.notice.web;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import oecp.framework.dao.QueryCondition;
import oecp.framework.dao.QueryResult;
import oecp.framework.exception.BizException;
import oecp.framework.util.DateUtil;
import oecp.framework.web.JsonResult;
import oecp.platform.notice.eo.NoticeEO;
import oecp.platform.notice.service.NoticeService;
import oecp.platform.org.eo.Organization;
import oecp.platform.org.service.OrgService;
import oecp.platform.otask.eo.OecpTask;
import oecp.platform.otask.eo.OecpTaskGroup;
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
@Namespace("/notice/manage")
public class NoticeAction  extends BasePlatformAction {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;


	
	@Autowired
	private NoticeService noticeService;
	private OrgService orgService;	
	private List<QueryCondition> conditions = new ArrayList<QueryCondition>();
	private NoticeEO noticeEO;
	private String noticeId;
	private String type;
	private String[] noticeIds;
	private String cronExpression;
	
	/**
	 * 列表
	 * 
	 * @author YangTao
	 * @date 2012-3-6下午04:30:40
	 * @return
	 * @throws BizException 
	 */
	@Action("list")
	public String list() throws BizException{
		type = getType();
		if(type.equals("0")){
		conditions = getConditions();
		QueryCondition conditon =new QueryCondition();
		conditon.setField("isStart");
		conditon.setOperator("=");
		conditon.setValue("1");
		conditions.add(conditon);
		}
		QueryResult<NoticeEO> qr = noticeService.list(conditions, start,limit);
		for(NoticeEO eo:qr.getResultlist()){
			if(eo.getAuthor()==null) eo.setAuthor(new User());
			if(eo.getCreateUser()==null) eo.setCreateUser(new User());
			Organization  org = noticeService.getCreateOrgs(eo.getCreatecorp());
			if(org.getParent() == null)org.setParent(new Organization());
			eo.setCreatecorp(org.getName());		
		}
		Organization root = noticeService.getChildOrgs(this.getOnlineUser().getLoginedOrg().getId());
		JsonResult jr = new JsonResult(qr.getTotalrecord().intValue(), qr.getResultlist());
		jr.setContainFields(new String[] { "id","title","message","createcorp","createUser","createTime","author","publishDate","isStart","discorp"});
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
		noticeEO= this.noticeService.getNoticeById(noticeId);
		JsonResult jr = new JsonResult(this.noticeEO);
		jr.setContainFields(new String[] { "id","title","message","createcorp","createUser","createTime","author","publishDate","isStart","discorp"});
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
		if(StringUtils.isEmpty(noticeEO.getId())){
			noticeEO.setCreateTime(DateUtil.getDateStr(new Date(),"yyyy-MM-dd HH:mm:ss"));
			noticeEO.setCreateUser(getOnlineUser().getUser());
		    noticeEO.setCreatecorp(this.getOnlineUser().getLoginedOrg().getId());
			noticeEO.setIsStart("0");		
		}else{
			NoticeEO OldernoticeEO = this.noticeService.find(noticeEO.getId());
			noticeEO.setCreateUser(OldernoticeEO.getCreateUser());
			noticeEO.setAuthor(OldernoticeEO.getAuthor());
		}
		this.noticeService.save(noticeEO);
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
		this.noticeService.delete(noticeIds);
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
			noticeEO = this.noticeService.find(noticeId);
			noticeEO.setPublishDate(DateUtil.getDateStr(new Date(),"yyyy-MM-dd HH:mm:ss"));
			noticeEO.setAuthor(getOnlineUser().getUser());
			if(noticeEO.getIsStart()!=null&&noticeEO.getIsStart().equals("0"))
		    noticeEO.setIsStart("1");
			else
			noticeEO.setIsStart("0");	
		this.noticeService.save(noticeEO);
		setJsonString("{success:true,msg:'发布成功！'}");
		return SUCCESS;
	}
	
	/**
	 * 列表
	 * 
	 * @author YangTao
	 * @date 2012-3-6下午04:30:40
	 * @return
	 */
//	@Action("listgroup")
//	public String listGroup(){
//		QueryResult<OecpTaskGroup> qr = oecpBulletinService.listGroup(conditions, start, limit);
//		JsonResult jr = new JsonResult(qr.getTotalrecord().intValue(), qr.getResultlist());
//		jr.setContainFields(new String[] { "id","name"});
//		this.setJsonString(jr.toJSONString());
//		return SUCCESS;
//	}

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

	public NoticeService getNoticeService() {
		return noticeService;
	}

	public void setNoticeService(NoticeService noticeService) {
		this.noticeService = noticeService;
	}

	public List<QueryCondition> getConditions() {
		return conditions;
	}

	public void setConditions(List<QueryCondition> conditions) {
		this.conditions = conditions;
	}

	public NoticeEO getNotice() {
		return noticeEO;
	}

	public void setNotice(NoticeEO noticeEO) {
		this.noticeEO = noticeEO;
	}

	public String getNoticeId() {
		return noticeId;
	}

	public void setNoticeId(String noticeId) {
		this.noticeId = noticeId;
	}

	public String[] getNoticeIds() {
		return noticeIds;
	}

	public void setNoticeIds(String[] noticeIds) {
		this.noticeIds = noticeIds;
	}

	public String getCronExpression() {
		return cronExpression;
	}

	public void setCronExpression(String cronExpression) {
		this.cronExpression = cronExpression;
	}
	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}



}
