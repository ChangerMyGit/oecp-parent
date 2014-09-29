package oecp.platform.notice.eo;

import java.text.ParseException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.apache.commons.lang.xwork.StringUtils;
import org.json.JSONObject;

import oecp.framework.entity.base.StringPKEO;
import oecp.framework.util.SpringContextUtil;
import oecp.platform.otask.itf.TaskItf;
import oecp.platform.user.eo.User;
import oecp.platform.warning.itf.WarningItf;

/**
 * 
 * @Desc 任务实体 
 * @author yangtao
 * @date 2012-4-6
 *
 */
@Entity
@Table(name="OECP_notice")
public class NoticeEO  extends StringPKEO {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	//公告标题
	private String title;
	//公告内容
	private String message;
	//创建公司
	private String createcorp;
	//创建人
	private User createUser;
	//创建时间
	private String createTime;
	//发布人
	private User author;
	//发布时间
	private String publishDate;
	//发布状态
	private String isStart;
	//发布状态
	private String discorp;
	
	public String getDiscorp() {
		return discorp;
	}
	public void setDiscorp(String discorp) {
		this.discorp = discorp;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public String getMessage() {
		return message;
	}
	public void setMessage(String message) {
		this.message = message;
	}
	//@ManyToOne
	public String getCreatecorp() {
		return createcorp;
	}
	public void setCreatecorp(String createcorp) {
		this.createcorp = createcorp;
	}
	@ManyToOne
	public User getCreateUser() {
		return createUser;
	}
	public void setCreateUser(User createUser) {
		this.createUser = createUser;
	}
	public String getCreateTime() {
		return createTime;
	}
	public void setCreateTime(String createTime) {
		this.createTime = createTime;
	}
	@ManyToOne
	public User getAuthor() {
		return author;
	}
	public void setAuthor(User author) {
		this.author = author;
	}
	public String getPublishDate() {
		return publishDate;
	}
	public void setPublishDate(String publishDate) {
		this.publishDate = publishDate;
	}
	public String getIsStart() {
		return isStart;
	}
	public void setIsStart(String isStart) {
		this.isStart = isStart;
	}

}
