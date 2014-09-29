/**
 * oecp-framework - MailUtil.java
 * copyright 2009-2012 OECP www.oecp.cn
 * 创建人:songlixiao	创建时间:2014年2月28日 上午11:16:29		版本:v1
 * ============================================
 * 修改人：			修改时间:					版本:
 * 修改原因:
 *
 * ============================================
 */

package oecp.framework.util.mail;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.Map;
import java.util.Properties;
import java.util.Set;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeUtility;

import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.mail.javamail.MimeMessageHelper;

/**
 * 邮件工具
 * 
 * @author songlixiao
 * 
 */
public class MailUtil {

	JavaMailSenderImpl			senderImpl		= new JavaMailSenderImpl();
	/** 默认设置 **/
	private static Properties	default_prop	= new Properties();

	/** 邮件发送设置 **/
	private Properties			prop			= null;

	public MailUtil() {
		default_prop.put("mail.smtp.auth", true); // 将这个参数设为true，让服务器进行认证,认证用户名和密码是否正确
		default_prop.put("mail.smtp.timeout", "25000");
		default_prop.put("mail.smtp.port", "25");
	}

	/**
	 * 初始化
	 * 
	 * @param host
	 *            邮件发送地址
	 * @param user
	 *            邮箱用户名
	 * @param pwd
	 *            邮箱密码
	 * @param propfile
	 *            邮件配置文件(适合使用配置文件的在此处设置)。
	 * @param props
	 *            邮件配置（适合在代码里设置的在此设置）。
	 */
	public void init(String host, String user, String pwd, File propfile, Map<String, Object> porps) {
		senderImpl.setHost(host);
		senderImpl.setUsername(user);
		senderImpl.setPassword(pwd);
		senderImpl.setDefaultEncoding("UTF-8");
		if (prop == null) {
			prop = new Properties(default_prop);
		}
		loadPropsFile(propfile);
		if (porps != null) {
			prop.putAll(porps);
		}
	}

	/**
	 * 加载邮箱设置文件
	 * 
	 * @param file
	 *            邮箱配置文件
	 * @throws FileNotFoundException
	 * @throws IOException
	 */
	public void loadPropsFile(File file) {
		if (file != null && file.exists()) {
			try {
				prop.load(new FileInputStream(file));
			} catch (Exception e) {
				throw new IllegalArgumentException("无效的配置文件!", e);
			}
		}
	}

	/**
	 * 发送邮件
	 * 
	 * @param mail
	 * @throws MessagingException
	 */
	public void sendMail(MailBean mail) throws MessagingException {
		senderImpl.setJavaMailProperties(prop);

		MimeMessage mailMessage = senderImpl.createMimeMessage();
		MimeMessageHelper messageHelper = new MimeMessageHelper(mailMessage, true);
		messageHelper.setTo(mail.getTo());
		if (mail.getCc() != null) {
			messageHelper.setCc(mail.getCc());
		}
		if (mail.getBcc() != null) {
			messageHelper.setBcc(mail.getBcc());
		}
		if (mail.getFormName() == null) { // 显示的发送者名
			messageHelper.setFrom(senderImpl.getUsername());
		} else {
			messageHelper.setFrom(mail.getFormName() + " <" + senderImpl.getUsername() + ">");
		}
		messageHelper.setReplyTo(mail.getReplyTo());
		messageHelper.setSubject(mail.getSubject());
		messageHelper.setText(mail.getText(), mail.isHtml());
		// 附件
		if (mail.getAttachments() != null && !mail.getAttachments().isEmpty()) {
			Set<String> keys = mail.getAttachments().keySet();
			for (String key : keys) {
				messageHelper.addAttachment(fileNameEncode(key), mail.getAttachments().get(key));
			}
		}
		// 邮件内嵌文件
		if (mail.getInlines() != null && !mail.getInlines().isEmpty()) {
			Set<String> keys = mail.getInlines().keySet();
			for (String key : keys) {
				messageHelper.addInline(fileNameEncode(key), mail.getInlines().get(key));
			}
		}
		senderImpl.send(mailMessage);
	}

	/**
	 * 解决附件中文名乱码问题
	 * 
	 * @param filename
	 * @return
	 */
	private String fileNameEncode(String filename) {
		try {
			return MimeUtility.encodeWord(filename);
		} catch (UnsupportedEncodingException e) {
			return filename;
		}
	}
}
