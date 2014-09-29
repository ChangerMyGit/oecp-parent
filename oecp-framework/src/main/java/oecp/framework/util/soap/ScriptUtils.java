/*
 * Copyright (c) 2011 OECP All Rights Reserved.                	
 * <a href="http://www.oecp.cn">http://www.oecp.cn</a>                                                                 
 */    

package oecp.framework.util.soap;

import groovy.lang.GroovyShell;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * 脚本工具。
 * 
 * @author slx
 * @date 2011-9-1下午04:25:26
 * @version 1.0
 */
public class ScriptUtils {

	public static void main(String[] args) {
		GroovyShell shell = new GroovyShell();
		
		ArrayList ls = new ArrayList();
		Map a1 = new HashMap<String, Integer>();
		a1.put("minOccurs", 2);
		ls.add(a1);
		Map a2 = new HashMap<String, Object>();
		a2.put("minOccurs", 3);
		ls.add(a2);
		ArrayList<Integer> ls2 = new ArrayList<Integer>();
		ls2.add(1);
		ls2.add(2);
		String script = "ls.minOccurs.sum()/ls.minOccurs.size()";
		
		Object rs = getResultFormObj(ls,"ls",script);
		System.out.println(rs);
	}
	
	/**
	 * 根据脚本从源对象中获得计算结果.
	 * </br>例如：</br><code><pre>
	 * 	ArrayList ls = new ArrayList();
	 *	Map a1 = new HashMap<String, Integer>();
	 *	a1.put("minOccurs", 2);
	 *	ls.add(a1);
	 *	Map a2 = new HashMap<String, Object>();
	 *	a2.put("minOccurs", 3);
	 *	ls.add(a2);
	 *	ArrayList<Integer> ls2 = new ArrayList<Integer>();
	 *	ls2.add(1);
	 *	ls2.add(2);
	 *	String script = "ls.minOccurs.sum()/ls.minOccurs.size()"; //计算集合ls中所有成员minOccurs属性的平均值
	 *	
	 *	Object rs = getResultFormObj(ls,"ls",script);
	 *	</pre></code>
	 * @author slx
	 * @date 2011-9-2上午10:12:52
	 * @param srcobj
	 * 		数据来源对象（可以是pojo，也可以是map）
	 * @param objname
	 * 		对象名，对应于脚本中的根对象名
	 * @param script
	 * 		获取数据的脚本
	 * @return
	 */
	public static Object getResultFormObj(Object srcobj,String objname,String script){
		GroovyShell shell = new GroovyShell();
		
		shell.setProperty(objname, srcobj);
		Object rs =shell.evaluate(script);
		return rs;
	}
}
