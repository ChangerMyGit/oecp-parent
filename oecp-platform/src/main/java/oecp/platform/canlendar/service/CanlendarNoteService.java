/**
 * oecp-platform - CanlendarNoteService.java
 * copyright 2009-2012 OECP www.oecp.cn
 * 创建人:Administrator	创建时间:上午11:16:12		版本:v1
 * ============================================
 * 修改人：			修改时间:					版本:
 * 修改原因:
 *
 * ============================================
 */

package oecp.platform.canlendar.service;

import java.util.List;

import oecp.framework.exception.BizException;
import oecp.framework.service.BaseService;

/**
 * 日历
 * 
 * @author luanyoubo
 * @date 2014年3月31日 上午11:16:12
 * @version 1.0
 * 
 */
public interface CanlendarNoteService<CanlendarNote> extends BaseService<CanlendarNote> {
	/**
	 * 获取当前用户的备忘事项
	 * @author luanyoubo
	 * @date 2014年4月1日上午10:05:40
	 * @param userID
	 * @return
	 */
	public List<CanlendarNote> getNotes(String userID)throws BizException; 
}
