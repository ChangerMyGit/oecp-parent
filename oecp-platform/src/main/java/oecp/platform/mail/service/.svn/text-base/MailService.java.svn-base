/************************* 版权声明 *********************************
 * 
 * 版权所有：百洋软件
 * Copyright (c) 2009 by Pearl Ocean.
 * 
 ************************* 变更记录 *********************************
 *
 * 创建者：slx   创建日期： 2009-11-17
 * 创建记录：创建类结构。
 * 
 * 修改者：       修改日期：
 * 修改记录：
 ************************* 随   笔 *********************************
 *
 * 这里可以写写感想，感慨，疑问什么的。
 * 
 ******************************************************************
 */    

package oecp.platform.mail.service;

import java.util.Map;

/**
 * 
 * @author slx
 * @date 2009-11-17 上午09:42:37
 * @version 1.0
 */
public interface MailService {

	public void sendTemplateMail(String title ,String templateName , String toMail ,Map<String, Object> params );
	
	public void sendTemplateMail(String title ,String templateName , String[] toMails ,Map<String, Object> params );

	public void sendHtmlMail(String title ,String content , String toMail);
	
	public void sendHtmlMail(String title ,String content, String[] toMails);
	
	public void sendTextMail(String title ,String text , String toMail );
	
	public void sendTextMail(String title ,String text , String[] toMails );
	
}
