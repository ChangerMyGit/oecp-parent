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

import javax.persistence.Transient;

/**
 * 数据VO接口
 * @author slx
 * @date 2011-6-16 下午04:06:52
 * @version 1.0
 */
public interface DataVO {
	
	/**
	 * 获得vo包含的字段数组
	 * @author slx
	 * @date 2011-6-28 下午07:27:49
	 * @modifyNote
	 * @return
	 */
	@Transient
	public String[] getFieldNames();
	
	/**
	 * 获得字段的类型
	 * @param attrname
	 * @return
	 */
	@Transient
	public Class<?> getFieldType(String attrname);
	
	/**
	 * 设置字段值
	 * @author slx
	 * @date 2011-6-16 下午04:08:03
	 * @modifyNote
	 * @param fieldname
	 * @param value
	 */
	@Transient
	public void setValue(String fieldname, Object value);
	/**
	 * 获得字段值
	 * @author slx
	 * @date 2011-6-16 下午04:08:37
	 * @modifyNote
	 * @param fieldname
	 * @return
	 */
	@Transient
	public Object getValue(String fieldname);
}
