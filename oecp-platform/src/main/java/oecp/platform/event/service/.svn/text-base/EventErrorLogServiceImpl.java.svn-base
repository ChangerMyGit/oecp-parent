/**
 * oecp-platform - EventHandleErrorLogServiceImpl.java
 * copyright 2009-2012 OECP www.oecp.cn
 * 创建人:Administrator	创建时间:下午1:22:54		版本:v1
 * ============================================
 * 修改人：			修改时间:					版本:
 * 修改原因:
 *
 * ============================================
 */

package oecp.platform.event.service;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.codehaus.jackson.map.ObjectMapper;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSON;

import oecp.framework.dao.QueryResult;
import oecp.framework.util.Constants;
import oecp.platform.base.service.PlatformBaseServiceImpl;
import oecp.platform.event.eo.EventHandleErrorLog;

/**
 * 
 * @author luanyoubo
 * @date 2013年12月19日 下午1:22:54
 * @version 1.0
 * 
 */
@Service("eventErrorLogService")
public class EventErrorLogServiceImpl extends PlatformBaseServiceImpl<EventHandleErrorLog> implements
		EventErrorLogService {

	 /**
	  * 获取事件错误日志
	  * @author luanyoubo
	  * @date 2013年12月19日下午3:44:42
	  * @return
	  */
	public QueryResult<EventHandleErrorLog> getEventErrorLogs(int start,int limit) {
		QueryResult<EventHandleErrorLog> qr = getDao().getScrollData(EventHandleErrorLog.class, start, limit, "  status = 'Y' ", new Object[]{}, null);
		return qr ;
	}

    /**
     * 重新执行事件
     * @author luanyoubo
     * @date 2013年12月19日下午4:41:00
     * @param id
     * @throws Exception 
     */
	public void restartEvent(String id) throws Exception {
		ObjectMapper mapper = new ObjectMapper();
		EventHandleErrorLog errorLog = getDao().find(EventHandleErrorLog.class, id);
		String[] argsType = mapper.readValue(errorLog.getArgsType(),String[].class);
		String[] argsValue = mapper.readValue(errorLog.getAgrsValue(),String[].class);
		Object[] args = new Object[argsType.length];
		Class[] clazzs = new Class[argsType.length];
		for (int i = 0; i < argsType.length; i++) {
			if (i != argsType.length - 1) {
				String className = argsType[i];
				String argValue = argsValue[i];
				Object obj = JSON.parseObject(argValue,Class.forName(className));
				args[i] = obj;
				clazzs[i] = Class.forName(className);
			} else if (i == argsType.length - 1) {
                if(StringUtils.isNotBlank(argsValue[i])){
                	List returnList = mapper.readValue(argsValue[i], ArrayList.class);
            		Object[] parObjs = new Object[returnList.size()];
            		parseToArray(parObjs,returnList);
            		args[i] = parObjs;
                } else {
                	args[i] = new Object[]{};
                }
                clazzs[i] = Object[].class;
			}
		}
		Class clazz = Class.forName(errorLog.getBeanClassName());
		Object invankeObj = clazz.newInstance();
		Method method = Class.forName(errorLog.getBeanClassName()).getMethod(errorLog.getMethodName(), clazzs);
		if (method != null) {
			method.invoke(invankeObj, args);
		}
		// 更改状态
		errorLog.setStatus(Constants.FALSE);
		getDao().update(errorLog);
	}

	// 根据Json解析成数组对象
	private void parseToArray(Object[] parObjs , List<Map<String,Object>> list) throws Exception  {
		for (int i = 0; i < list.size(); i++) {
			Map<String,Object> map = list.get(i);
			String className = (String) map.get("ClassName");
			if(!"Array".equals(className)){
				String jsonValue = (String) map.get(className);
				Object obj = JSON.parseObject(jsonValue, Class.forName(className));
				parObjs[i] = obj;
			} else {
				List<Map<String,Object>> childList = (List<Map<String, Object>>) map.get(className);
				Object[] childObjs = new Object[childList.size()];
				parseToArray(childObjs,childList);
				parObjs[i] = childObjs;
			}
		}
	}

	public void removeAllLog() {
		getDao().deleteByWhere(EventHandleErrorLog.class, null, null);
	}
}
