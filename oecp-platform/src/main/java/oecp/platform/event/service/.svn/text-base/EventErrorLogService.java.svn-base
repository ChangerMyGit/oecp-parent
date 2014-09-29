/**
 * oecp-platform - EventHandleErrorLogService.java
 * copyright 2009-2012 OECP www.oecp.cn
 * 创建人:Administrator	创建时间:下午1:13:13		版本:v1
 * ============================================
 * 修改人：			修改时间:					版本:
 * 修改原因:
 *
 * ============================================
 */

package oecp.platform.event.service;

import oecp.framework.dao.QueryResult;
import oecp.framework.service.BaseService;
import oecp.platform.event.eo.EventHandleErrorLog;

/** 
 *
 * @author luanyoubo  
 * @date 2013年12月19日 下午1:13:13 
 * @version 1.0
 *  
 */
public interface EventErrorLogService extends BaseService<EventHandleErrorLog>{
	 /**
	  * 获取事件错误日志
	  * @author luanyoubo
	  * @date 2013年12月19日下午3:44:42
	  * @return
	  */
     public QueryResult<EventHandleErrorLog> getEventErrorLogs(int start,int limit);
     
     /**
      * 重新执行事件
      * @author luanyoubo
      * @date 2013年12月19日下午4:41:00
      * @param id
      */
     public void restartEvent(String id) throws Exception;
     
     /**
      * 
      * @author luanyoubo
      * @date 2013年12月20日上午9:43:40
      */
     public void removeAllLog();
}
