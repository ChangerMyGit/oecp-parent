package oecp.platform.message.web;

import java.util.List;

import oecp.framework.exception.BizException;
import oecp.framework.util.FastJsonUtils;
import oecp.platform.message.eo.Message;
import oecp.platform.message.eo.MessageContent;
import oecp.platform.message.service.MessageService;
import oecp.platform.web.BasePlatformAction;

import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.Namespace;
import org.apache.struts2.convention.annotation.ParentPackage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

/**
 * 
 * 消息action
 * @author lintao
 * @date 2011-6-30 上午09:59:00
 * @version 1.0
 */

@Controller
@Scope("prototype")
@ParentPackage("default-package")
@Namespace("/message")
public class MessageAction extends BasePlatformAction{

	private static final long serialVersionUID = 1L;
	
	private String userId;
	private String contentId;
	@Autowired
	private MessageService messageService;
	
	
	/**
	 * 获取未读消息条目数
	 * @return
	 */
	@Action(value = "selectCount")
	public String selectCount(){
		long count = messageService.getMessageCount(getOnlineUser().getUser().getId(),"0");
		this.setJsonString("{count : "+count+" }");
		return SUCCESS;
	}
	
	/**
	 * 获取所有未读消息
	 * @return
	 */
	@Action(value = "findMessages")
	public String findMessages(){
		List<MessageContent> list = messageService.findMessageByUserId(getOnlineUser().getUser().getId(),"0");
		this.setJsonString(FastJsonUtils.toJson(list,new String[]{"id","title","content","createtime","billId","openPath","frame"}));
		return SUCCESS;
	}
	
	/**
	 * 更新消息集合
	 * @return
	 */
	@Action(value = "updateMessages")
	public String updateMessages(){
		try {
			messageService.updateMessageState(contentId,getOnlineUser().getUser().getId());
		} catch (BizException e) {
			e.printStackTrace();
		}
		return SUCCESS;
	}

	
	
	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public MessageService getMessageService() {
		return messageService;
	}

	public void setMessageService(MessageService messageService) {
		this.messageService = messageService;
	}

	public String getContentId() {
		return contentId;
	}

	public void setContentId(String contentId) {
		this.contentId = contentId;
	}


	
}
