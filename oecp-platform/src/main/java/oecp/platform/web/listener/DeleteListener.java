/**
 * oecp-platform - DeleteListener.java
 * copyright 2009-2012 OECP www.oecp.cn
 * 创建人:luanyoubo	创建时间:下午3:59:48		版本:v1
 * ============================================
 * 修改人：			修改时间:					版本:
 * 修改原因:
 *
 * ============================================
 */

package oecp.platform.web.listener;

import java.util.Date;

import oecp.platform.log.eo.DeleteLog;

import org.hibernate.Session;
import org.hibernate.event.PostDeleteEvent;
import org.hibernate.event.PostDeleteEventListener;
/** 
 *
 * @author luanyoubo  
 * @date 2014年3月11日 下午3:59:48 
 * @version 1.0
 *  
 */
public class DeleteListener implements PostDeleteEventListener {
	private static final long serialVersionUID = -4136828439188969473L;

	@Override
	public void onPostDelete(PostDeleteEvent event) {
		Object entity = event.getEntity();
		// 删除后保存日志
		DeleteLog log = new DeleteLog();
		log.setDeleteTime(new Date());
		log.setEntryClass(entity.getClass().getName());
		log.setEntryContent(entity.toString());
		Session session = event.getPersister().getFactory().openSession();
		try {
			session.save(log);
			session.flush();
		} catch (Exception e) {
		} finally {
			session.close();
		}
	}
}
