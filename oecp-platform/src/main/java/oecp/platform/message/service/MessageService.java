package oecp.platform.message.service;

import java.util.List;

import oecp.framework.exception.BizException;
import oecp.platform.message.eo.MessageContent;

/**
 * 消息服务
 * @author   lintao
 * @date 2011-6-30 上午09:18:15
 * @version 1.0
 */
public interface MessageService {

	/**
	 * 根据登录用户查找出此用户未读消息内容
	 * 
	 * @author lintao
	 * @date 2011-6-30 上午09:18:15
	 * @param userId
	 *            登录用户ID
	 * @param state
	 *              消息状态 0未读  1已读
	 * @return List<MessageContent>
	 *             相关所有未读消息
	 */
	List<MessageContent>  findMessageByUserId(String userId,String state);

	/**
	 * 根据登录用户ID查找出此用户未读消息条数
	 * 
	 * @author lintao
	 * @date 2011-6-30 上午09:18:15
	 * @param userId
	 *            登录用户ID
	 * @param state
	 *              消息状态 0未读  1已读
	 * @return long
	 *             相关所有未读消息的条数
	 */
	
	long getMessageCount(String userId,String state);
	
	/**
	 * 根据用户发送普通消息
	 * 
	 * @author lintao
	 * @date 2011-6-30 上午11:26:15
	 * @param title
	 *            消息标题
	 * @param content
	 *             消息内容
	 * @param userId
	 *               对指定用户发送消息
	 */
	
	void sendMessage(String title,String content,String userId) throws BizException;
	
	
	/**
	 * 根据用户发送通知消息
	 * 
	 * @author lintao
	 * @date 2011-7-1 上午10:14:15
	 * @param title
	 *            消息标题
	 * @param content
	 *             消息内容
	 *@param billId
	 *             单据id
	 * @param openPath
	 *            窗口路径
	 * @param frame           
	 *        		false : js对象
	 *        		true : url
	 * @param userId
	 *               对指定用户发送消息
	 */
	
	void sendMessage(String title,String content,String billId,String openPath,boolean frame,String userId) throws BizException;
	
	/**
	 * 
	 * 更新消息,主要更改消息的状态,由未读状态-->已读状态
	 * @author lintao
	 * @date 2011-7-1 上午11:23:15
	 * @param mcId
	 * 				更改过的消息id
	 */
	void updateMessageState(String contentId,String userId)throws BizException;
	
	
	
	
	
}
