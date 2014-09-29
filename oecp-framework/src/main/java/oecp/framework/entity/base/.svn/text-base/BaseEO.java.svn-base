/************************* 版权声明 *********************************
 * 
 * 版权所有：百洋软件
 * Copyright (c) 2009 by Pearl Ocean.
 * 
 ************************* 变更记录 *********************************
 *
 * 创建者：yongtree   创建日期： 2009-4-29
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

package oecp.framework.entity.base;

import java.io.Serializable;
import java.util.List;

import javax.persistence.MappedSuperclass;
import javax.persistence.Transient;

import oecp.framework.util.entity.EOUtility;

/**
 * 基础实体Bean，包含系统表中公共的字段 <br>
 * 重写 toString() clone() equals() hashCode()
 * 
 * @author yongtree
 * @date 2009-4-29 下午01:43:42
 * @version 1.0
 */
@MappedSuperclass
public abstract class BaseEO<T> implements Serializable {

	private static final long serialVersionUID = 1962905939086138888L;

	private transient EOUtility eoutil ;
	
	@Transient
	public EOUtility getBeanUtility() {
		if (eoutil == null) {
			eoutil = new EOUtility(this);
		}
		return eoutil;
	}

	@Override
	@Transient
	public String toString() {
		return getBeanUtility().beanToString();
	}

	@Override
	@Transient
	public boolean equals(Object obj) {
		return getBeanUtility().equalsBean(obj);
	}

	@Override
	@Transient
	public int hashCode() {
		return getBeanUtility().hashCodeBean();
	}

	@Override
	public Object clone() throws CloneNotSupportedException {
		Object obj = null;
		try {
			obj = getBeanUtility().cloneBean();
		} catch (Exception e) {
			throw new CloneNotSupportedException(e.getMessage());
		}

		return obj;
	}

	/**
	 * 得到所有可持久化字段的名称
	 * @author slx
	 * @date 2009-7-17 上午08:59:34
	 * @modifyNote
	 * @return
	 * 		名称列表
	 */
	@Transient
	public String[] getAttributeNames(){
		return getBeanUtility().getAttributeNames();
	}
	
	/**
	 * 得到某个字段的值
	 * @author slx
	 * @date 2009-7-17 上午08:59:58
	 * @modifyNote
	 * @param attributeName
	 * 		字段名
	 * @return
	 * 		值
	 */
	@Transient
	public Object getAttributeValue(String attributeName) {
		return getBeanUtility().getAttributeValue(attributeName);
	}
	
	/**
	 * 设置某个字段的值
	 * @author slx
	 * @date 2009-7-17 上午09:00:26
	 * @modifyNote
	 * @param attributeName
	 * 		字段名
	 * @param value
	 * 		值
	 */
	@Transient
	public void setAttributeValue(String attributeName , Object value){
		getBeanUtility().setAttributeValue(attributeName,value);
	}
	
	/**
	 * 获得一个属性的类型
	 * @author slx
	 * @date 2011-12-22
	 * @param attrName
	 * @return
	 * @throws NoSuchFieldException
	 */
	@Transient
	public Class<?> getAttributeType(String attrName) throws NoSuchFieldException{
		return getBeanUtility().getAttributeType(attrName);
	}
	
	@SuppressWarnings("static-access")
	@Transient
	public String getEnumDescription(String enumAttributeName){
		Object value = getAttributeValue(enumAttributeName);
		
		return getBeanUtility().getEnumDescription(value);
	}

	/**
	 * 获得实体对应的表名
	 * 
	 * @author slx
	 * @date 2009-7-17 上午09:00:57
	 * @modifyNote
	 * @return
	 */
	@Transient
	public String getTableName() {
		return getBeanUtility().getTableName();
	}

	/**
	 * 比较此对象与另一个对象的差别，并返回值不同的字段的名称。
	 * 
	 * @author slx
	 * @date 2009-7-17 上午09:34:39
	 * @modifyNote
	 * @param antherBean
	 *            将要比较的对象
	 * @return 值不同的字段名
	 */
	@Transient
	public List<String> getDifferentField(BaseEO anotherBean) {
		return getBeanUtility().getDifferentField(anotherBean);
	}

	/**
	 * 获取主键值
	 * 
	 * @author slx
	 * @date 2009-6-12 上午09:15:11
	 * @modifyNote
	 * @return 主键值
	 */
	@Transient
	public abstract T getId();
	
	public abstract void setId(T pk);

	/** （为了兼容指定id的情况）已经有id的，需要将id临时放在此处。如果有值，id生成器会直接使用此id。 **/
	@Transient
	private T existId;
	@Transient
	public T getExistId() {
		return existId;
	}

	@Transient
	public void setExistId(T existId) {
		this.existId = existId;
	}
	/**
	 * 比较主键值是否相同
	 * 
	 * @author yongtree
	 * @date 2009-9-15 下午04:09:21
	 * @modifyNote
	 * @param obj
	 * @return
	 */
	public boolean equalsPK(Object obj) {
		if (obj == null)// 对象为空不比较
			return false;
		// 类型不同不必进行比较
		if (!this.getClass().equals(obj.getClass())) {
			return false;
		}

		// 不是BaseEO，不必比较
		if (!(obj instanceof BaseEO)) {
			return false;
		}

		BaseEO eo = (BaseEO) obj;

		if (getId()!=null
				&& eo.getId()!=null) {
			if (getId().equals(eo.getId()))
				return true;
			return false;
		} else {
			return false;
		}

	}

	/**
	 * 拷贝另一个eo对象中的字段值到当前对象中
	 * @author slx
	 * @date 2009-12-6 上午11:04:49
	 * @modifyNote
	 * @param fromEO			从哪里拷贝
	 * @param copyAttributes	拷贝哪些字段
	 */
	public void copyAttributeValue(BaseEO fromEO , String[] copyAttributes){
		if(copyAttributes == null)
			return ;
		
		for (String attr : copyAttributes) {
			this.setAttributeValue(attr, fromEO.getAttributeValue(attr));
		}
	}
	
	/**
	 * 加载所有延迟加载字段
	 * @author slx
	 * @date 2010年4月1日17:22:29
	 * @modifyNote
	 */
	public void loadLazyAttributes(){
		getBeanUtility().loadLazyField();
	}
	
	/**
	 * 获取EO的显示名称
	 * @return
	 */
	@Transient
	public String getEODisplayName(){
		return getBeanUtility().getEODisplayName();
	}
	
	/**
	 * 得到某字段的显示名称
	 * @param fieldName
	 * @return
	 */
	@Transient
	public String getFieldDisplayName(String fieldName){
		return getBeanUtility().getFieldDisplayName(fieldName);
	}
	
}
