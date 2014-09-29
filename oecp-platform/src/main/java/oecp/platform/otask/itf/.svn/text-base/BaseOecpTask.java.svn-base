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

package oecp.platform.otask.itf;

import oecp.platform.otask.eo.OecpTaskLog;

/** 
 * 任务的基类，获取当前、上一个日志
 * @author Administrator  
 * @date 2012-6-27 下午12:02:55 
 * @version 1.0
 *  
 */
public abstract class BaseOecpTask {
	/**当前日志**/
	private OecpTaskLog currentlog;
	/**上一个日志日志**/
	private OecpTaskLog aboveLog;
	
	/**
	 * 初始化方法
	 * @author Administrator
	 * @date 2012-6-27下午01:32:52
	 * @param aboveLog
	 * @param currentlog
	 */
	public void init(OecpTaskLog currentlog,OecpTaskLog aboveLog){
		this.aboveLog = aboveLog;
		this.currentlog = currentlog;
	}
	
	public OecpTaskLog getCurrentLog(){
		return currentlog;
	}
	
	public OecpTaskLog getAboveLog(){
		return aboveLog;
	}

}
