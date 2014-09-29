/************************* 版权声明 *********************************
 *                                                                  *
 *                     版权所有：百洋软件                          				 	*
 *          Copyright (c) 2011 by www.oecp.cn                	*
 *                                                                  *
 ************************* 变更记录 *********************************
 *
 * 创建者：宋黎晓   创建日期： 2011-6-16
 * 备注：
 * 
 * 修改者：       修改日期：
 * 备注：
 * 
 */

package oecp.framework.vo.base;

import java.io.Serializable;
import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedHashMap;

/**
 * 简单数据VO 将属性值都存储在HashMap中，不区分属性类型。
 * 
 * @author slx
 * @date 2011-6-16 下午04:03:30
 * @version 1.0
 */
public class SimpleDataVO implements DataVO,Serializable {

	private HashMap<String, Object> values = new LinkedHashMap<String, Object>();

	@Override
	public Object getValue(String fieldname) {
		return values.get(fieldname);
	}

	@Override
	public void setValue(String fieldname, Object value) {
		values.put(fieldname, value);
	}

	@Override
	public String[] getFieldNames() {
			return values.keySet().toArray(new String[0]);
	}

	/**
	 * 如果对属性的类型有要求，请覆盖此方法！！！
	 */
	@Override
	public Class<?> getFieldType(String attrname) {
		return String.class;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + Arrays.hashCode(getFieldNames());
		result = prime * result + ((values == null) ? 0 : values.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		SimpleDataVO other = (SimpleDataVO) obj;
		if (!Arrays.equals(getFieldNames(), other.getFieldNames()))
			return false;
		for (String field : getFieldNames()) {
			if ((this.getValue(field) == null && other.getValue(field) != null)
					|| (this.getValue(field) != null && other.getValue(field) == null)
					|| ((this.getValue(field) != null
							&& other.getValue(field) != null && !this
							.getValue(field).equals(other.getValue(field)))))
				return false;
		}
		return true;
	}
}
