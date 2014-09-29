/************************* 版权声明 *********************************
 * 
 * 版权所有：百洋软件
 * Copyright (c) 2010 by Pearl Ocean.
 * 
 ************************* 变更记录 *********************************
 *
 * 创建者：yongtree   创建日期： 2010-3-30
 * 创建记录：创建类结构。
 * 
 * 修改者：       修改日期：
 * 修改记录：
 ************************* 随   笔 *********************************
 *
 * 这里可以写写感想，感慨，疑问什么的。
 * 
 ******************************************************************
 */

package oecp.framework.util.web.struts2;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

import com.opensymphony.xwork2.ActionContext;

/**
 * Struts2的flashscope实现
 * 
 * @author yongtree
 * @date 2010-3-30 上午09:10:16
 * @version 1.0
 */
public class StrutsFlashScope implements FlashScope {

	private Map current = new ConcurrentHashMap();
	private Map next = new ConcurrentHashMap();

	public StrutsFlashScope() {
	}

	public void next() {
		current.clear();
		current = new ConcurrentHashMap(next);
		next.clear();
	}

	public Map getNow() {
		return current;
	}

	public int size() {
		return current.size() + next.size();
	}

	public void clear() {
		current.clear();
		next.clear();
	}

	public boolean isEmpty() {
		return size() == 0;
	}

	public boolean containsKey(Object key) {
		return (current.containsKey(key) || next.containsKey(key));
	}

	public boolean containsValue(Object value) {
		return (current.containsValue(value) || next.containsValue(value));
	}

	public Collection values() {
		Collection c = new ArrayList();
		c.addAll(current.values());
		c.addAll(next.values());
		return c;
	}

	public void putAll(Map t) {
		for (Map.Entry<Object, Object> entry : ((Map<Object, Object>) t)
				.entrySet()) {
			put(entry.getKey(), entry.getValue());
		}
	}

	public Set entrySet() {
		Set keySet = new HashSet();
		keySet.addAll(current.entrySet());
		keySet.addAll(next.entrySet());
		return keySet;
	}

	public Set keySet() {
		Set keySet = new HashSet();
		keySet.addAll(current.keySet());
		keySet.addAll(next.keySet());
		return keySet;
	}

	public Object get(Object key) {
		if (next.containsKey(key))
			return next.get(key);
		return current.get(key);
	}

	public Object remove(Object key) {
		if (current.containsKey(key))
			return current.remove(key);
		else
			return next.remove(key);
	}

	public Object put(Object key, Object value) {
		// create the session if it doesn't exist
		registerWithSessionIfNecessary();
		if (current.containsKey(key)) {
			current.remove(key);
		}

		if (value == null)
			return next.remove(key);
		else
			return next.put(key, value);
	}

	

	private void registerWithSessionIfNecessary() {
		Map<String, Object> session = ActionContext.getContext().getSession();
		if (session.get(FLASH_SCOPE) == null)
			session.put(FLASH_SCOPE, this);
		
	}

}
