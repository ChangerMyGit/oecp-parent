package oecp.platform.message.eo;

import java.text.SimpleDateFormat;
import java.util.Date;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.persistence.Table;


import oecp.framework.entity.base.StringPKEO;
import oecp.platform.user.eo.User;

/**
 * 
 * 消息和用户之间的关联关系
 * @author  lintao
 * @date 2011-6-30 上午09:18:15
 * @version 1.0
 */

@Entity
@Table(name="OECP_SYS_MESSAGE")
public class Message extends StringPKEO{

	private static final long serialVersionUID = 1L;

	/** 关联用户 **/
	private User user;
	/** 关联消息体 */
	private MessageContent content;
	/** 消息状态 **/
	private String state;
	
	

	@ManyToOne
	public User getUser() {
		return user;
	}
	public void setUser(User user) {
		this.user = user;
	}
	
	@ManyToOne
	public MessageContent getContent() {
		return content;
	}
	public void setContent(MessageContent content) {
		this.content = content;
	}
	public String getState() {
		return state;
	}
	public void setState(String state) {
		this.state = state;
	}
	

}
