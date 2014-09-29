/**
 * oecp-platform - EventConfigeServiceImpl.java
 * copyright 2009-2012 OECP www.oecp.cn
 * 创建人:luanyoubo	创建时间:上午10:11:23		版本:v1
 * ============================================
 * 修改人：			修改时间:					版本:
 * 修改原因:
 *
 * ============================================
 */

package oecp.platform.event.service;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.annotation.PostConstruct;

import oecp.framework.dao.QueryResult;
import oecp.framework.util.Constants;
import oecp.platform.base.service.PlatformBaseServiceImpl;
import oecp.platform.event.annotation.Listener;
import oecp.platform.event.eo.EventConfige;

import org.springframework.stereotype.Service;

/** 
 *
 * @author luanyoubo  
 * @date 2013年12月16日 上午10:11:23 
 * @version 1.0
 *  
 */
@Service("eventConfigeService")
public class EventConfigeServiceImpl extends PlatformBaseServiceImpl<EventConfige> implements EventConfigeService {
	/** 扫包过滤条件，（包名为oecp开头的包） */
	public static final String JarPrefix = "oecp";
	
    /**
     * 初始化扫描所有的监听器
     * @author luanyoubo
     * @date 2013年12月16日上午9:51:26
     * @throws Exception
     */
	@PostConstruct
	public void init() throws Exception {
		List<String> classFilters = new ArrayList<String>();
		classFilters.add("*");
		ClassPathScanHandler handler = new ClassPathScanHandler(true, true,classFilters);
		Set<Class<?>> packageClass = handler.getPackageAllClasses(JarPrefix,true);
		// 循环赋值(初始化)监听队列
		for (Class<?> cla : packageClass) {
			Listener a = cla.getAnnotation(Listener.class);
			if (a != null) {
				// 获取监听器的事件源
				String sourceName = a.source();
				Method[] methods = cla.getMethods();
				for (Method method : methods) {
					if (method.getName().startsWith("on")) {
                        Map<String, String> map = new HashMap<String, String>();
                        map.put("sourceName", sourceName);
                        map.put("listener", cla.getName());
                        map.put("action", method.getName());
                        EventConfige confige =  getEventConfige(map);           
                        // 如果是新添加的监听器 默认不启动，同步
						if (confige == null) {
							confige = new EventConfige();
							confige.setEventSource(sourceName);
							confige.setListenerClass(cla.getName());
							confige.setAction(method.getName());
							confige.setStartFlag(Constants.FALSE);
							confige.setSynFlag(Constants.TRUE);
							getDao().create(confige);
						}                      
					}
				}
			}
		}
	}

    // 获取监听器列表
	public QueryResult<EventConfige> getListeners(int start, int limit) {
		return getDao().getScrollData(EventConfige.class, start, limit, null, null);
	}

	// 启动监听器
	public void start(String id) {
		EventConfige confige = getDao().find(EventConfige.class, id);
		confige.setStartFlag(Constants.TRUE);
		getDao().update(confige);
	}

	// 关闭监听器
	public void close(String id) {
		EventConfige confige = getDao().find(EventConfige.class, id);
		confige.setStartFlag(Constants.FALSE);
		getDao().update(confige);
	}
	
	
    /**
     * 根据事件源，监听器，动作获取配置
     * @author Administrator
     * @date 2013年12月16日下午1:57:10
     * @param param
     * @return
     */
	public EventConfige getEventConfige(Map<String, String> param) {
		String eventSource = param.get("sourceName");
		String listenerClass = param.get("listener");
		String action = param.get("action");
		List<EventConfige> eventConfigeList = getDao().queryByWhere(EventConfige.class,
				" eventSource = ? AND listenerClass = ? AND action = ?",
				new Object[] { eventSource, listenerClass, action });
		if (eventConfigeList.size() == 0 || eventConfigeList == null)
			return null;
		else
			return eventConfigeList.get(0);
	}

}
