/**
 * oecp-framework - MailBean.java
 * copyright 2009-2012 OECP www.oecp.cn
 * 创建人:songlixiao	创建时间:2014年2月28日 上午11:15:55		版本:v1
 * ============================================
 * 修改人：			修改时间:					版本:
 * 修改原因:
 *
 * ============================================
 */

package oecp.framework.util.mail;

import java.io.File;
import java.util.Map;

/**
 * 邮件
 * @author songlixiao
 *
 */
public class MailBean {
	/** 接收者列表 **/
	private String[] to;
	/** 邮件发送者姓名 **/
	private String formName;
	/** 回复地址 **/
	private String replyTo;
	/** 抄送 **/
	private String[] cc;
	/** 密送 **/
	private String[] bcc;
	/** 标题 **/
	private String subject;
	/** 内容 **/
	private String text;
	/** 是否html邮件 **/
	private boolean html;
	/** 邮件内嵌文件<标示名，文件> **/
	private Map<String,File> inlines;
	/** 附件<显示名，文件> **/
	private Map<String,File> attachments;
	
	public String[] getTo() {
		return to;
	}
	public void setTo(String[] to) {
		this.to = to;
	}
	public String getFormName() {
		return formName;
	}
	public void setFormName(String formName) {
		this.formName = formName;
	}
	public String[] getCc() {
		return cc;
	}
	public void setCc(String[] cc) {
		this.cc = cc;
	}
	public String[] getBcc() {
		return bcc;
	}
	public void setBcc(String[] bcc) {
		this.bcc = bcc;
	}
	public String getSubject() {
		return subject;
	}
	public void setSubject(String subject) {
		this.subject = subject;
	}
	public String getText() {
		return text;
	}
	public void setText(String text) {
		this.text = text;
	}
	public boolean isHtml() {
		return html;
	}
	public void setHtml(boolean html) {
		this.html = html;
	}
	public Map<String, File> getInlines() {
		return inlines;
	}
	public void setInlines(Map<String, File> inlines) {
		this.inlines = inlines;
	}
	public Map<String, File> getAttachments() {
		return attachments;
	}
	public void setAttachments(Map<String, File> attachments) {
		this.attachments = attachments;
	}
	public String getReplyTo() {
		return replyTo;
	}
	public void setReplyTo(String replyTo) {
		this.replyTo = replyTo;
	}
}
