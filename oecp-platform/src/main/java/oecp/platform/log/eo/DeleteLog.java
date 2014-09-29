/**
 * oecp-platform - DeleteLog.java
 * copyright 2009-2012 OECP www.oecp.cn
 * 创建人:luanyoubo	创建时间:上午10:40:48		版本:v1
 * ============================================
 * 修改人：			修改时间:					版本:
 * 修改原因:
 *
 * ============================================
 */

package oecp.platform.log.eo;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

import oecp.framework.entity.base.StringPKEO;

/** 
 *
 * @author luanyoubo  
 * @date 2014年3月12日 上午10:40:48 
 * @version 1.0
 *  
 */
@Entity
@Table(name="OECP_SYS_DELETE_LOG")
public class DeleteLog extends StringPKEO {
	private static final long serialVersionUID = 8744241371540289217L;

	private Date deleteTime;// 操作时间
	private String entryClass;// 实体类名称
	private String entryContent;// 实体内容

	public Date getDeleteTime() {
		return deleteTime;
	}

	public void setDeleteTime(Date deleteTime) {
		this.deleteTime = deleteTime;
	}

	public String getEntryClass() {
		return entryClass;
	}

	public void setEntryClass(String entryClass) {
		this.entryClass = entryClass;
	}

	@Column(length=4000)
	public String getEntryContent() {
		return entryContent;
	}

	public void setEntryContent(String entryContent) {
		this.entryContent = entryContent;
	}

}
