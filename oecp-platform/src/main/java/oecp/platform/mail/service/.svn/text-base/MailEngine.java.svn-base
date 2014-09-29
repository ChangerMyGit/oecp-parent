package oecp.platform.mail.service;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.mail.MessagingException;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.core.io.ClassPathResource;
import org.springframework.mail.MailException;
import org.springframework.mail.MailSender;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.ui.freemarker.FreeMarkerTemplateUtils;

import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateException;

public class MailEngine {
	protected static final Log log = LogFactory.getLog(MailEngine.class);

//	private FreeMarkerConfigurationFactoryBean freeMarkerConfigurer;
	private MailSender mailSender;
	private Configuration freeMarkerConfigurer;

	
//	public void setFreeMarkerConfigurer(
//			FreeMarkerConfigurationFactoryBean freeMarkerConfigurer) {
//		this.freeMarkerConfigurer = freeMarkerConfigurer;
//	}

	public void setFreeMarkerConfigurer(Configuration freeMarkerConfigurer) {
		this.freeMarkerConfigurer = freeMarkerConfigurer;
	}


	public void setMailSender(MailSender mailSender) {
		this.mailSender = mailSender;
	}


	/**
	 * 通过模板产生邮件正文
	 * 
	 * @param templateName
	 *            邮件模板名称
	 * @param map
	 *            模板中要填充的对象
	 * @return 邮件正文（HTML）
	 */
	public String generateEmailContentByFreeMarker(String templateName, Map<String, Object> map) {
		// 使用FreeMaker模板
		try {
//			Configuration configuration = freeMarkerConfigurer.createConfiguration();
			Configuration configuration = freeMarkerConfigurer;
			Template t = configuration.getTemplate(templateName);
			return FreeMarkerTemplateUtils.processTemplateIntoString(t, map);
		} catch (TemplateException e) {
			log.error("Error while processing FreeMarker template ", e);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
			// log.error("Error while open template file ", e);
		} catch (IOException e) {
			log.error("Error while generate Email Content ", e);
		}

		return null;
	}

	/**
	 * 发送邮件
	 * 
	 * @param emailAddress
	 *            收件人Email地址的数组
	 * @param fromEmail
	 *            寄件人Email地址, null为默认寄件人web@vnvtrip.com
	 * @param bodyText
	 *            邮件正文
	 * @param subject
	 *            邮件主题
	 * @param attachmentName
	 *            附件名
	 * @param resource
	 *            附件
	 * @throws MessagingException
	 * @throws UnsupportedEncodingException 
	 */
	public void sendMessage(String[] emailAddresses, String fromEmail,
			String bodyText, String subject, String attachmentName,
			ClassPathResource resource) throws MessagingException{
		MimeMessage message = ((JavaMailSenderImpl) mailSender)
				.createMimeMessage();

		// use the true flag to indicate you need a multipart message
		MimeMessageHelper helper = new MimeMessageHelper(message, true,"UTF-8");

		helper.setTo(emailAddresses);
		if (fromEmail != null) {
			helper.setFrom(getFromInternetAddress(fromEmail));
		}
		helper.setText(bodyText, true);
		helper.setSubject(subject);

		if (attachmentName != null && resource != null)
			helper.addAttachment(attachmentName, resource);

		((JavaMailSenderImpl) mailSender).send(message);
	}
	/**
	 * 发件人会显示尖括号内的回复地址
	 */
	@Deprecated
	private String decodeFrom(String from){
		try {
			return javax.mail.internet.MimeUtility.encodeText(from , "utf-8","B");
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	public final String regex1 = ".*[<][^>]*[>].*"; 	//判断是 xxxx <xxx>格式文本
	public final String regex2 = "<([^>]*)>";		//括号匹配
	/**
	 * 获取发件人
	 * @param from
	 * @return
	 */
	public InternetAddress getFromInternetAddress(String from) {
		String personal = null;
		String address = null;

		if(from.matches(regex1)){
			personal = from.replaceAll(regex2, "").trim();
			Matcher m = Pattern.compile(regex2).matcher(from);
			if(m.find()){
				address = m.group(1).trim();
			}
			try {
				return new InternetAddress(address, personal, "gb2312");
			} catch (UnsupportedEncodingException e) {
				e.printStackTrace();
			}
		}else{
			try {
				return new InternetAddress(from);
			} catch (AddressException e) {
				e.printStackTrace();
			}
		}
		return null;
	}

	/**
	 * 发送简单邮件
	 * 
	 * @param msg
	 */
	public void send(SimpleMailMessage msg) {
		try {
			((JavaMailSenderImpl) mailSender).send(msg);
		} catch (MailException ex) {
			// log it and go on
			log.error(ex.getMessage());
		}
	}

	/**
	 * 使用模版发送HTML格式的邮件
	 * 
	 * @param msg
	 *            装有to,from,subject信息的SimpleMailMessage
	 * @param templateName
	 *            模版名,模版根路径已在配置文件定义于freemakarengine中
	 * @param model
	 *            渲染模版所需的数据
	 * @throws UnsupportedEncodingException 
	 */
	public void send(SimpleMailMessage msg, String templateName, Map<String, Object> model){
		// 生成html邮件内容
		String content = generateEmailContentByFreeMarker(templateName, model);
		MimeMessage mimeMsg = null;
		try {
			mimeMsg = ((JavaMailSenderImpl) mailSender).createMimeMessage();
			MimeMessageHelper helper = new MimeMessageHelper(mimeMsg, true,
					"utf-8");
			helper.setTo(msg.getTo());

			if (msg.getSubject() != null)
				helper.setSubject(msg.getSubject());

			if (msg.getFrom() != null)
				helper.setFrom(getFromInternetAddress(msg.getFrom()));

			helper.setText(content, true);

			((JavaMailSenderImpl) mailSender).send(mimeMsg);
		} catch (MessagingException ex) {
			log.error(ex.getMessage(), ex);
		}

	}

}
