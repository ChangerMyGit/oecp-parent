package oecp.platform.warning.eo;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Lob;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import oecp.framework.entity.base.StringPKEO;
import oecp.platform.user.eo.User;
/**
 * 已经发送的预警消息
 *
 * @author YangTao
 * @date 2012-3-19下午03:23:12
 * @version 1.0
 */
@Entity
@Table(name="OECP_WARN_SENDED")
public class SendWarn  extends StringPKEO {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	//发送人
	private User user;
	
	//预警标题
	private String title;
	
	//发送消息内容
	private String messageContent;
	
	//已经通知多少次数
	private int noticedNum;
	
	//必须填写，表示具体业务类别：如：A商品、B商品等等
	private String key;
	
	//删除标志
	private Boolean deleteFlag;
	
	//预警
	private Warn warn;

	@ManyToOne
	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	@Lob
	public String getMessageContent() {
		return messageContent;
	}

	public void setMessageContent(String messageContent) {
		this.messageContent = messageContent;
	}

	public int getNoticedNum() {
		return noticedNum;
	}

	public void setNoticedNum(int noticedNum) {
		this.noticedNum = noticedNum;
	}

	@ManyToOne
	public Warn getWarn() {
		return warn;
	}

	public void setWarn(Warn warn) {
		this.warn = warn;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	@Column(name="bizkey")
	public String getKey() {
		return key;
	}

	public void setKey(String key) {
		this.key = key;
	}

	public Boolean getDeleteFlag() {
		return deleteFlag;
	}

	public void setDeleteFlag(Boolean deleteFlag) {
		this.deleteFlag = deleteFlag;
	}
	
	
	
	
}
