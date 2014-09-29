/*
 * Copyright (c) 2011 OECP All Rights Reserved.                	
 * http://www.oecp.cn                                                                 
 */

package oecp.framework.web;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import oecp.framework.util.FastJsonUtils;
import oecp.framework.util.OECPBeanUtils;

/**
 * <p>
 * 将要转化为JSON格式字符串的对象放在该容器中，统一格式返回给客户端
 * </p>
 * <p>
 * User.java:
 * </p>
 * 
 * <pre>
 * public class User {
 * 	private String id;
 * 	private String name;
 * 	//此处省略getter,setter
 * }
 * </pre>
 * 
 * <pre>
 * new JsonResult(true,"查找到用户",new User("U001","张三")).toJSONString();-->JSON:{success:true,msg:"查找到用户",data:{id:"U001",name:"张三"}}
 * <br>同样也可以得到该对象，自己在外面转换成JSON
 * </pre>
 * 
 * @author yongtree
 * @date 2011-5-4 下午03:10:10
 * @version 1.0
 */
public class JsonResult implements Serializable {

	private static final long serialVersionUID = -6070106385947762294L;

	private boolean success;

	private String msg;

	private Object result;

	private Integer totalCounts;

	private String[] containFields;

	private String dateFormat = "yyyy-MM-dd HH:mm:ss";

	public JsonResult(boolean success, String msg) {
		this.success = success;
		this.msg = msg;
	}
	

	public JsonResult(boolean success, String msg, Object result) {
		this.success = success;
		this.msg = msg;
		this.result = result;
	}

	public JsonResult(Object result) {
		this.success = true;
		this.msg = "执行成功！";
		this.result = result;
	}

	public JsonResult(Integer totalCounts, Object result) {
		this.success = true;
		this.totalCounts = totalCounts;
		this.result = result;
	}

	/*
	 * 直接输出json字符串
	 */
	@SuppressWarnings("unchecked")
	public String toJSONString() {
		// result = FastJsonUtils.toJson(this.getResult(), containFields);
		// return FastJsonUtils.toJson(this);
		if (containFields != null && containFields.length > 0) {
			if (result instanceof Collection) {
				Collection c = (Collection) result;
				List list = new ArrayList();
				for (Object o : c) {
					list.add(OECPBeanUtils.toMap(o, containFields, dateFormat));
				}
				result = list;
			} else if (result instanceof Map) {
				result = subMap((Map) result, containFields);
			} else {
				result = OECPBeanUtils.toMap(result, containFields, dateFormat);
			}
		}
		return FastJsonUtils.toJson(this);
	}

	private Map subMap(Map m, String[] properties) {
		Map subMap = new HashMap();
		for (String p : properties) {
			subMap.put(p, m.get(p));
		}
		return subMap;
	}

	public void setDateFormat(String dateFormat) {
		this.dateFormat = dateFormat;
	}

	public void setContainFields(String[] containFields) {
		this.containFields = containFields;
	}

	public Integer getTotalCounts() {
		return totalCounts;
	}

	public void setTotalCounts(Integer totalCounts) {
		this.totalCounts = totalCounts;
	}

	public boolean isSuccess() {
		return success;
	}

	public void setSuccess(boolean success) {
		this.success = success;
	}

	public String getMsg() {
		return msg;
	}

	public void setMsg(String msg) {
		this.msg = msg;
	}

	public Object getResult() {
		return result;
	}

	public void setResult(Object result) {
		this.result = result;
	}

}
