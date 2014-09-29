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

import javax.mail.MessagingException;

import org.springframework.core.task.TaskExecutor;
import org.springframework.mail.SimpleMailMessage;

/**
 * 
 * @author slx
 * @date 2009-11-17 上午09:46:17
 * @version 1.0
 */
public class MailServiceImpl implements MailService {

	private MailEngine engine;

	private SimpleMailMessage mailMessage;

	private TaskExecutor taskExecutor;

	public void setTaskExecutor(TaskExecutor taskExecutor) {
		this.taskExecutor = taskExecutor;
	}

	public void setEngine(MailEngine engine) {
		this.engine = engine;
	}

	public void setMailMessage(SimpleMailMessage mailMessage) {
		this.mailMessage = mailMessage;
	}

	@Override
	public void sendTemplateMail(final String title, final String templateName,
			final String toMail, final Map<String, Object> params) {
		taskExecutor.execute(new Runnable() {
			public void run() {
				mailMessage.setTo(toMail);
				mailMessage.setSubject(title);
				engine.send(mailMessage, templateName, params);
			}
		});
	}

	@Override
	public void sendTemplateMail(final String title, final String templateName,
			final String[] toMails, final Map<String, Object> params) {
		taskExecutor.execute(new Runnable() {
			public void run() {
				mailMessage.setTo(toMails);
				mailMessage.setSubject(title);
				engine.send(mailMessage, templateName, params);
			}
		});
	}

	@Override
	public void sendTextMail(final String title, final String text,
			final String toMail) {
		taskExecutor.execute(new Runnable() {
			public void run() {
				mailMessage.setTo(toMail);
				mailMessage.setSubject(title);
				mailMessage.setText(text);
				engine.send(mailMessage);
			}
		});
	}

	@Override
	public void sendTextMail(final String title, final String text,
			final String[] toMails) {
		taskExecutor.execute(new Runnable() {
			public void run() {
				mailMessage.setTo(toMails);
				mailMessage.setSubject(title);
				mailMessage.setText(text);
				engine.send(mailMessage);
			}
		});
	}

	@Override
	public void sendHtmlMail(String title, String content, String toMail) {
		sendHtmlMail(title, content, new String[] { toMail });
	}

	@Override
	public void sendHtmlMail(final String title, final String content,
			final String[] toMails) {
		taskExecutor.execute(new Runnable() {
			public void run() {
				try {
					engine.sendMessage(toMails, mailMessage.getFrom(), content,
							title, null, null);
				} catch (MessagingException e) {
					e.printStackTrace();
				}
			}
		});
	}

}
