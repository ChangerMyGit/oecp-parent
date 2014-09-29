/**
 * oecp-platform - EventConfigService.java
 * copyright 2009-2012 OECP www.oecp.cn
 * 创建人:luanyoubo	创建时间:上午10:10:20		版本:v1
 * ============================================
 * 修改人：			修改时间:					版本:
 * 修改原因:
 *
 * ============================================
 */

package oecp.platform.event.service;

import java.util.Map;

import oecp.framework.dao.QueryResult;
import oecp.framework.service.BaseService;
import oecp.platform.event.eo.EventConfige;

/** 
 *
 * @author luanyoubo  
 * @date 2013年12月16日 上午10:10:20 
 * @version 1.0
 *  
 */
public interface EventConfigeService extends BaseService<EventConfige>{
	/**
	 * 获取监听器列表
	 * @author luanyoubo
	 * @date 2013年12月16日上午10:18:27
	 * @return
	 */
     public QueryResult<EventConfige> getListeners(int start, int limit);
     
     /**
      * 启动监听器
      * @author luanyoubo
      * @date 2013年12月16日下午3:35:16
      * @param id
      */
     public void start(String id);
     
     /**
      * 关闭监听器
      * @author luanyoubo
      * @date 2013年12月16日下午3:48:58
      * @param id
      */
      public void close(String id);
     
     /**
      * 根据事件源，监听器，动作获取配置
      * @author Administrator
      * @date 2013年12月16日下午1:57:10
      * @param param
      * @return
      */
     public EventConfige getEventConfige(Map<String, String> param);
}
