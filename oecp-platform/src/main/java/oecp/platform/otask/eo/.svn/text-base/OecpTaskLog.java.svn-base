/**
 * oecp-platform - UIViewParser.java
 * copyright 2009-2012 OECP www.oecp.cn
 * 创建人:slx	创建时间:2011-11-7上午10:41:16		版本:v1
 * ============================================
 * 修改人：			修改时间:					版本:
 * 修改原因:
 *
 * ============================================
 */

package oecp.platform.otask.eo;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import oecp.framework.entity.base.StringPKEO;

/** 
 *
 * @author Administrator  
 * @date 2012-6-27 上午11:21:06 
 * @version 1.0
 *  
 */
@Entity
@Table(name="OECP_TASK_LOG")
public class OecpTaskLog   extends StringPKEO {

	private static final long serialVersionUID = 7325537028024112877L;
	
	/**任务**/
	private OecpTask oecpTask;
	/**任务开始时间**/
	private Date beginTime;
	/**任务结束时间**/
	private Date endTime;
	/**任务执行日志内容**/
	private String content;
	
	@ManyToOne
	public OecpTask getOecpTask() {
		return oecpTask;
	}
	public void setOecpTask(OecpTask oecpTask) {
		this.oecpTask = oecpTask;
	}
	@Column(name="begin_time",length=200)
	public Date getBeginTime() {
		return beginTime;
	}
	public void setBeginTime(Date beginTime) {
		this.beginTime = beginTime;
	}
	@Column(name="end_time",length=200)
	public Date getEndTime() {
		return endTime;
	}
	public void setEndTime(Date endTime) {
		this.endTime = endTime;
	}
	@Column(name="content",length=4000)
	public String getContent() {
		return content;
	}
	public void setContent(String content) {
		if(content!=null && content.length()>=4000){
			this.content = content.substring(0,4000);
		}else{
			this.content = content;
		}
	}
	
	
}
