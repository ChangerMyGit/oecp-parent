/**
 * oecp-platform - CanlendarNoteServiceImpl.java
 * copyright 2009-2012 OECP www.oecp.cn
 * 创建人:Administrator	创建时间:上午11:24:46		版本:v1
 * ============================================
 * 修改人：			修改时间:					版本:
 * 修改原因:
 *
 * ============================================
 */

package oecp.platform.canlendar.service;

import java.util.List;

import org.springframework.stereotype.Service;

import oecp.framework.exception.BizException;
import oecp.platform.base.service.PlatformBaseServiceImpl;
import oecp.platform.canlendar.eo.CanlendarNote;

/**
 * 
 * @author luanyoubo
 * @date 2014年3月31日 上午11:24:46
 * @version 1.0
 * 
 */
@Service("canlendarNoteService")
public class CanlendarNoteServiceImpl extends PlatformBaseServiceImpl<CanlendarNote> implements
		CanlendarNoteService<CanlendarNote> {

	public List<CanlendarNote> getNotes(String userID) throws BizException {
		return getDao().queryByWhere(CanlendarNote.class, " userID = ?", new Object[]{userID});
	}

}
