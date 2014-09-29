package oecp.platform.warning.eo;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.Transient;

import oecp.framework.entity.base.StringPKEO;
import oecp.framework.util.SpringContextUtil;
import oecp.platform.org.eo.Post;
import oecp.platform.role.eo.OrgRole;
import oecp.platform.user.eo.User;
import oecp.platform.warning.enums.WarnNoticeItem;
import oecp.platform.warning.enums.WarnNoticeUserType;
import oecp.platform.warning.enums.WarningType;
import oecp.platform.warning.itf.WarningItf;

import org.apache.commons.lang.StringUtils;

/**
 * 预警
 *
 * @author YangTao
 * @date 2012-3-6下午01:57:10
 * @version 1.0
 */

@Entity
@Table(name="OECP_WARN")
public class Warn extends StringPKEO {

	private static final long serialVersionUID = 1L;
	//预警名称
	private String name;
	//预警类型
	private WarningType warningType;
	//预警插件
	private String warningitf;
	//预警通知人员类型：角色、岗位、用户
	private WarnNoticeUserType warnNoticeUserType;
	//预警通知人员 
	private List<User> noticeUsers = new ArrayList<User>();
	//预警通知岗位
	private List<Post> noticePosts = new ArrayList<Post>();
	//预警通知角色
	private List<OrgRole> noticeRoles = new ArrayList<OrgRole>();
	//通知方式
	private String warnNotice;
	private WarnNoticeItem[] warnNoticeItem;
	//通知最多次数
	private int noticeNum;
	//事件预警
	private EventWarn eventWarn;
	//定时器预警
	private TimerWarn timerWarn;
	//是否启动
	private Boolean isStart;
	//启用人
	private User startUser;
	
	/**
	 * 获得插件的类实例
	 * 
	 * @author YangTao
	 * @date 2012-3-7上午08:50:41
	 * @return
	 * @throws ClassNotFoundException
	 * @throws InstantiationException
	 * @throws IllegalAccessException
	 */
	@Transient
	public WarningItf getClassWarningItf() throws ClassNotFoundException, InstantiationException, IllegalAccessException{
		WarningItf wif = null;
		if(this.warningitf.indexOf(".")!=-1)
			wif = (WarningItf)SpringContextUtil.getApplicationContext().getBean(Class.forName(this.warningitf));
		else
			wif = (WarningItf)SpringContextUtil.getBean(this.warningitf);
		return wif;
	}
	public WarningType getWarningType() {
		return warningType;
	}
	public void setWarningType(WarningType warningType) {
		this.warningType = warningType;
	}
	public String getWarningitf() {
		return warningitf;
	}
	public void setWarningitf(String warningitf) {
		this.warningitf = warningitf;
	}
	
	@ManyToMany()
	@JoinTable(name = "OECP_WARN_NOTICE_USER",joinColumns = { @JoinColumn(name = "warn_pk") }, inverseJoinColumns = { @JoinColumn(name = "user_pk") })
	public List<User> getNoticeUsers() {
		return noticeUsers;
	}
	public void setNoticeUsers(List<User> noticeUsers) {
		this.noticeUsers = noticeUsers;
	}
	
	@ManyToMany()
	@JoinTable(name = "OECP_WARN_NOTICE_POST",joinColumns = { @JoinColumn(name = "warn_pk") }, inverseJoinColumns = { @JoinColumn(name = "post_pk") })
	public List<Post> getNoticePosts() {
		return noticePosts;
	}
	public void setNoticePosts(List<Post> noticePosts) {
		this.noticePosts = noticePosts;
	}
	
	@ManyToMany()
	@JoinTable(name = "OECP_WARN_NOTICE_ORGROLE",joinColumns = { @JoinColumn(name = "warn_pk") }, inverseJoinColumns = { @JoinColumn(name = "orgrole_pk") })
	public List<OrgRole> getNoticeRoles() {
		return noticeRoles;
	}
	public void setNoticeRoles(List<OrgRole> noticeRoles) {
		this.noticeRoles = noticeRoles;
	}
	@Transient
	public WarnNoticeItem[] getWarnNoticeItem() {
		return warnNoticeItem;
	}
	public void setWarnNoticeItem(WarnNoticeItem[] warnNoticeItem) {
		this.warnNoticeItem = warnNoticeItem;
		String result = "";
		if(warnNoticeItem!=null){
			for(WarnNoticeItem wn : warnNoticeItem){
				result += wn.name();
				result += ",";
			}
			if(result.endsWith(","))
				result = result.substring(0, result.length()-1);
		}
		warnNotice = result;
	}
	
	public String getWarnNotice() {
		return warnNotice;
	}
	public void setWarnNotice(String warnNotice) {
		this.warnNotice = warnNotice;
		if(StringUtils.isNotEmpty(warnNotice)){
			String[] s = warnNotice.split(",");
			warnNoticeItem = new WarnNoticeItem[s.length]; 
			for(int i=0;i<s.length;i++){
				warnNoticeItem[i] = WarnNoticeItem.valueOf(s[i]);
			}
		}else{
			warnNoticeItem = null;
		}
	}
	
	public int getNoticeNum() {
		return noticeNum;
	}
	public void setNoticeNum(int noticeNum) {
		this.noticeNum = noticeNum;
	}
	@OneToOne(cascade = { CascadeType.PERSIST,CascadeType.MERGE })
	public EventWarn getEventWarn() {
		return eventWarn;
	}
	public void setEventWarn(EventWarn eventWarn) {
		this.eventWarn = eventWarn;
	}
	@OneToOne(cascade = { CascadeType.PERSIST,CascadeType.MERGE })
	public TimerWarn getTimerWarn() {
		return timerWarn;
	}
	public void setTimerWarn(TimerWarn timerWarn) {
		this.timerWarn = timerWarn;
	}
	public WarnNoticeUserType getWarnNoticeUserType() {
		return warnNoticeUserType;
	}
	public void setWarnNoticeUserType(WarnNoticeUserType warnNoticeUserType) {
		this.warnNoticeUserType = warnNoticeUserType;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public Boolean getIsStart() {
		return isStart;
	}
	public void setIsStart(Boolean isStart) {
		if(isStart==null)
			isStart = false;
		this.isStart = isStart;
	}
	@ManyToOne
	public User getStartUser() {
		return startUser;
	}
	public void setStartUser(User startUser) {
		this.startUser = startUser;
	}
	
}
