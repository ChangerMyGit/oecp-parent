package oecp.platform.warning.eo;

import java.util.List;

import oecp.platform.user.eo.User;

/**
 * 预警插件返回对象
 *
 * @author YangTao
 * @date 2012-3-16上午09:25:31
 * @version 1.0
 */
public class WarnItfContent {
	//代表是否发出预警
	private Boolean isWarning;
	//代表预警通知人员，如果不为空，通知人员以这个为准；
	//如果为空，以后台配置通知人员为准
	private List<User> users;
	//预警通知消息内容
	private String messageContent;
	//表示具体业务类别：如：A商品、B商品等等
	private String key;
	
	public Boolean getIsWarning() {
		return isWarning;
	}
	public void setIsWarning(Boolean isWarning) {
		this.isWarning = isWarning;
	}
	public List<User> getUsers() {
		return users;
	}
	public void setUsers(List<User> users) {
		this.users = users;
	}
	public String getMessageContent() {
		return messageContent;
	}
	public void setMessageContent(String messageContent) {
		this.messageContent = messageContent;
	}
	public String getKey() {
		return key;
	}
	public void setKey(String key) {
		this.key = key;
	}
	
	
}
