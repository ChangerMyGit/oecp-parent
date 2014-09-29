/**
 * oecp-platform - CanlendarNote.java
 * copyright 2009-2012 OECP www.oecp.cn
 * 创建人:Administrator	创建时间:上午11:02:03		版本:v1
 * ============================================
 * 修改人：			修改时间:					版本:
 * 修改原因:
 *
 * ============================================
 */

package oecp.platform.canlendar.eo;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

import oecp.framework.entity.base.StringPKEO;

/** 
 * 日历备忘录
 * @author luanyoubo  
 * @date 2014年3月31日 上午11:02:03 
 * @version 1.0
 *  
 */
@Entity
@Table(name="OECP_SYS_CANLENDAR_NOTE")
public class CanlendarNote extends StringPKEO {
	private static final long serialVersionUID = 5614002193030786157L;

	private String title;
	private String notes;
	private Date startDate;
	private Date endDate;
    private String userID;
	
	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}
	
	@Column(length=4000)
	public String getNotes() {
		return notes;
	}

	public void setNotes(String notes) {
		this.notes = notes;
	}

	public Date getStartDate() {
		return startDate;
	}

	public void setStartDate(Date startDate) {
		this.startDate = startDate;
	}

	public Date getEndDate() {
		return endDate;
	}

	public void setEndDate(Date endDate) {
		this.endDate = endDate;
	}

	public String getUserID() {
		return userID;
	}

	public void setUserID(String userID) {
		this.userID = userID;
	}

}
