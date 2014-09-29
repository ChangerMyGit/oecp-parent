/*
 * Copyright (c) 2011 OECP All Rights Reserved.                	
 * <a href="http://www.oecp.cn">OECP</a> 
*/                                                                
  

package oecp.platform.message.eo;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

import oecp.framework.entity.base.StringPKEO;

/** 
 *
 * @author lintao  
 * @date 2011-7-8 上午08:47:11 
 * @version 1.0
 *  
 */
@Entity
@Table(name="OECP_SYS_MESSAGE_CONTENT")
public class MessageContent extends StringPKEO{

	private static final long serialVersionUID = 1L;

	/** 消息标题 **/
	private String title;
	/** 消息内容 **/
	private String content;
	/** 消息接收时间 **/
	private String createtime;
	/** 用户所要连接打开的内容 **/
	private String openPath;
	/** 用户所有打开内容的类型 false:js对象 :true超链接 */
	private Boolean frame;
	/** 单据ID **/
	private String billId;
	
	
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	@Column(length=4000)
	public String getContent() {
		return content;
	}
	public void setContent(String content) {
		this.content = content;
	}
	public String getCreatetime() {
		return createtime;
	}
	public void setCreatetime(String createtime) {
		this.createtime = createtime;
	}
	public String getBillId() {
		return billId;
	}
	public void setBillId(String billId) {
		this.billId = billId;
	}
	public Boolean getFrame() {
		return frame;
	}
	public void setFrame(Boolean frame) {
		this.frame = frame;
	}
	public String getOpenPath() {
		return openPath;
	}
	public void setOpenPath(String openPath) {
		this.openPath = openPath;
	}
}
