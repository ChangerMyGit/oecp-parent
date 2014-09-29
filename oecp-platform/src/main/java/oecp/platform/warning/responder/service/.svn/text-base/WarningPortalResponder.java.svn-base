package oecp.platform.warning.responder.service;

import java.util.List;

import javax.annotation.Resource;

import oecp.framework.exception.BizException;
import oecp.platform.message.service.MessageService;
import oecp.platform.user.eo.User;
import oecp.platform.warning.responder.WarningResponder;

import org.springframework.stereotype.Service;

/**
 * portal响应器，现在是发送消息
 *
 * @author YangTao
 * @date 2012-3-12上午11:00:37
 * @version 1.0
 */
@Service("warningPortalResponder")
public class WarningPortalResponder implements WarningResponder {

	@Resource
	private MessageService messageService;
	
	@Override
	public void doWarning(String title,String message,User user) throws BizException{
		// TODO Auto-generated method stub
			messageService.sendMessage(title, message, user.getId());
	}
	
	public MessageService getMessageService() {
		return messageService;
	}
	public void setMessageService(MessageService messageService) {
		this.messageService = messageService;
	}

	
}
