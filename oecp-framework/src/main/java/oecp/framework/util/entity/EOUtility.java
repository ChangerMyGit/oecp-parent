/************************* 版权声明 *********************************
 * 
 * 版权所有：百洋软件
 * Copyright (c) 2009 by Pearl Ocean.
 * 
 ************************* 变更记录 *********************************
 *
 * 创建者：宋黎晓   创建日期： 2009-4-29
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
package oecp.framework.util.entity;

import java.beans.IntrospectionException;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import javax.persistence.FetchType;
import javax.persistence.Lob;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.Transient;

import oecp.framework.entity.annotation.EODisplayName;
import oecp.framework.entity.annotation.FieldDisplayName;
import oecp.framework.entity.base.BaseEO;
import oecp.framework.util.DateUtil;
import oecp.framework.util.enums.EnumDescription;

import org.apache.commons.lang.StringUtils;

/**
 * 实体bean工具类<br>
 * <b> 注意：此工具类只能应用与BaseEntityBean及其子类！ </b><br>
 * 实现功能： <li>实体bean转换为string <li>实体bean比较 <li>得到实体bean哈希值 <li>深克隆实体bean
 * 
 * @author 宋黎晓
 * 
 */
@SuppressWarnings("unchecked")
public class EOUtility {

	// /** 当前bean中所有的字段(包括此类以及父类中的公有和非公有字段) <get方法名,字段> **/
	// protected HashMap<String,Field> hm_Field;

	/** 当前bean类以及父类中公有的get方法 <字段名,get方法> **/
	protected HashMap<String, Method> hm_Geters;

	/** 当前bean类以及父类中公有的set方法 <字段名,get方法> **/
	protected HashMap<String, Method> hm_Seters;
	
	/** 当前bean类以及父类中延迟加载字段的get方法 <字段名,get方法> **/
	protected HashMap<String, Method> hm_LazyGeters;
	/** 当前bean类以及父类中延迟加载字段的get方法 <字段名,set方法> **/
	protected HashMap<String, Method> hm_LazySeters;

	/** 应用本工具类的实体对象 **/
	protected BaseEO bean;

	/** 应用本工具类的实体对象类型 **/
	protected Class<? extends BaseEO> clazz;
	
	protected String beanDispName;
	
	/** 存储字段对应中文名 **/
	protected HashMap<String, String> hm_DispNames;

	public EOUtility(BaseEO bean) {
		init(bean);
	}

	protected void init(BaseEO bean) {
		// 当前bean与新传入的bean是同一个bean时不必进行初始化
		if (this.bean == bean)
			return;

		this.bean = bean;
		clazz = bean.getClass();

		initGetterAndSetters();
		// initField();
		// initGeters();
	}
	
	/**
	 * 构建get和set方法的map（递归方法）
	 * @author slx
	 * @date 2010-7-2 
	 * @modifyNote
	 * @param beanclass
	 */
	protected void buildGetterANDSetters(Class beanclass) {
		// 得到当前类字段名称
		Field[] fields = beanclass.getDeclaredFields();
		String fieldname = null;
		// 拼接字段对应的方法名
		for (Field field : fields) {
			fieldname = field.getName();
			for (PropertyDescriptor property : propertyDescriptors) {
				if (fieldname.equals(property.getName())) {
					Method reader = property.getReadMethod();
					Method writer = property.getWriteMethod();

					Transient t = reader.getAnnotation(Transient.class);
					if(t==null || "id".equals(fieldname)){
						if (reader != null
								&& !(isLazyField(reader)) || "id".equals(fieldname)){
							hm_Geters.put(fieldname, reader);// 非延迟
							if (writer != null)
								hm_Seters.put(fieldname, writer);
						}else{
							hm_LazyGeters.put(fieldname, reader); //延迟
							if (writer != null)
								hm_LazySeters.put(fieldname, writer);
						}
					}
				}
			}
		}
		// 当前类不是 BaseEntityBean时，递归调用
		if (!beanclass.equals(BaseEO.class)) {
			buildGetterANDSetters((Class<? extends BaseEO>) beanclass
					.getSuperclass());
		}
	}
	
	/**
	 * 得到EO要显示的中文名
	 * @author slx
	 * @date 2010-7-2 下午05:22:44
	 * @modifyNote
	 * @return
	 */
	public String getEODisplayName(){
		if(beanDispName == null){
		EODisplayName ea = clazz.getAnnotation(EODisplayName.class);
			if(ea != null){
				beanDispName = ea.value();
			}else{
				beanDispName = clazz.getSimpleName();
			}
		}
		return beanDispName;
	}

	/**
	 * 得到字段显示的名称
	 * @author slx
	 * @date 2010-7-2 下午05:23:03
	 * @modifyNote
	 * @param fieldName
	 * @return
	 */
	public String getFieldDisplayName(String fieldName){
		String dispName = hm_DispNames.get(fieldName);
		if(dispName == null){
			dispName = getFieldDisplayName(clazz,fieldName);
			hm_DispNames.put(fieldName,dispName );
		}
		
		return dispName;
	}
	
	protected String getFieldDisplayName(Class clz ,String fieldName){
		String dispName = null;
		Field f;
		try {
			f = clz.getDeclaredField(fieldName);
		} catch (SecurityException e) {
			return fieldName;
		} catch (NoSuchFieldException e) {
			if(!clz.getSuperclass().equals(BaseEO.class))
				return getFieldDisplayName(clz.getSuperclass(),fieldName);
			else
				return fieldName;
		}
		FieldDisplayName am = f.getAnnotation(FieldDisplayName.class);
		if(am!=null){
			dispName = am.value();
		}else{
			dispName = fieldName;
		}
		return dispName;
	}
	
	/*
	 * 根据注解判断是否是延迟加载的字段
	 */
	public static boolean isLazyField(Method reader) {
		if(reader.isAnnotationPresent(OneToOne.class)){
			if (FetchType.LAZY.equals(reader.getAnnotation(OneToOne.class).fetch())) {
				return true;
			}
		}
		if(reader.isAnnotationPresent(ManyToOne.class)){
			if (FetchType.LAZY.equals(reader.getAnnotation(ManyToOne.class).fetch())) {
				return true;
			}
		}
		// OneToMany 默认为延迟加载,如果没有标注立即加载则都是延迟加载
		if(reader.isAnnotationPresent(OneToMany.class)){
			if (!FetchType.EAGER.equals(reader.getAnnotation(OneToMany.class).fetch())) {
				return true;
			}
		}
		// ManyToMany 同上
		if(reader.isAnnotationPresent(ManyToMany.class)){
			if (!FetchType.EAGER.equals(reader.getAnnotation(ManyToMany.class).fetch())) {
				return true;
			}
		}
		// Lob字段内容庞大,不管是不是延迟加载,全部不进行处理
		if(reader.isAnnotationPresent(Lob.class)){
			return true;
		}
		// 非持久字段不处理
		if(reader.isAnnotationPresent(Transient.class)){
			return true;
		}

		return false;
	}

	PropertyDescriptor[] propertyDescriptors = null;

	/**
	 * 初始化get和set方法
	 * 
	 * @author slx
	 * @date 2009-7-17 上午08:51:50
	 * @modifyNote
	 */
	protected void initGetterAndSetters() {
		try {
			propertyDescriptors = Introspector.getBeanInfo(clazz)
					.getPropertyDescriptors();
			if (hm_Geters == null)
				hm_Geters = new HashMap<String, Method>();
			hm_Geters.clear();
			if (hm_LazyGeters == null)
				hm_LazyGeters = new HashMap<String, Method>();
			hm_LazyGeters.clear();
			if (hm_Seters == null)
				hm_Seters = new HashMap<String, Method>();
			hm_Seters.clear();
			if (hm_LazySeters == null)
				hm_LazySeters = new HashMap<String, Method>();
			hm_LazySeters.clear();
			if (hm_DispNames == null)
				hm_DispNames = new HashMap<String, String>();
			hm_DispNames.clear();
			buildGetterANDSetters(clazz);
		} catch (IntrospectionException e) {
		}
	}

	/**
	 * 设置指定属性的值
	 * 
	 * @author slx
	 * @date 2009-7-17 上午08:51:28
	 * @modifyNote
	 * @param attName
	 *            属性名
	 * @param value
	 *            值
	 */
	public void setAttributeValue(String attName, Object value) {
		try {
			getSetter(attName).invoke(bean, new Object[] { value });
		} catch (Exception e) {
		}
	}
	
	/**
	 * 得到get方法
	 * @param attName
	 * @return
	 */
	public Method getGetter(String attName){
		Method m = hm_Geters.get(attName);
		if(m == null) 
			m = hm_LazyGeters.get(attName);
		return m;
	}
	/**
	 * 得到set方法
	 * @param attName
	 * @return
	 */
	public Method getSetter(String attName){
		Method m = hm_Seters.get(attName);
		if(m == null) 
			m = hm_LazySeters.get(attName);
		return m;
	}
	
	public Class<?> getAttributeType(String attName) throws NoSuchFieldException{
		if(getGetter(attName) == null){
			throw new NoSuchFieldException(attName);
		}
		return getGetter(attName).getReturnType();
	}
	
	public Type getAttributeGenericType(String attName) throws NoSuchFieldException{
		if(getGetter(attName) == null){
			throw new NoSuchFieldException(attName);
		}
		return getGetter(attName).getGenericReturnType();
	}

	/**
	 * 得到指定属性的值
	 * 
	 * @author slx
	 * @date 2009-7-17 上午08:51:12
	 * @modifyNote
	 * @param attName
	 *            属性名
	 * @return 值
	 */
	public Object getAttributeValue(String attName) {
		Object o = null;
		try {
			Object[] os = null;
			o = getGetter(attName).invoke(bean, os);
		} catch (Exception e) {
		}
		return o;
	}

	/**
	 * 得到表名称
	 * 
	 * @author slx
	 * @date 2009-7-17 上午08:50:56
	 * @modifyNote
	 * @return 表名
	 */
	public String getTableName() {
		return getTableName(clazz);
	}
	/**
	 * 得到EO类对应的表名
	 * @author slx
	 * @date 2011 6 13 11:50:37
	 * @modifyNote
	 * @param eoclass
	 * @return
	 */
	public static String getTableName(Class<? extends BaseEO> eoclass){
		javax.persistence.Table table = eoclass
				.getAnnotation(javax.persistence.Table.class);
		String tablename = null;
		if (table == null) {
			Class clazzp = eoclass.getSuperclass();
			while (!clazzp.equals(BaseEO.class)) {
				table = (Table) clazzp
						.getAnnotation(javax.persistence.Table.class);
				if (table != null) {
					tablename = table.name();
					break;
				} else {
					clazzp = clazzp.getSuperclass();
				}
			}
		} else {
			tablename = table.name();
		}
		if (tablename == null || tablename.length() == 0) {
			tablename = eoclass.getSimpleName().toUpperCase();
		}
		return tablename;
	}

	protected String[] fieldNames;

	/**
	 * 得到实体中所有持久化字段名
	 * 
	 * @author slx
	 * @date 2009-7-17 上午08:53:21
	 * @modifyNote
	 * @return 字段名称数组
	 */
	public String[] getAttributeNames() {
		if (fieldNames == null) {
			Set<String> set_fieldnames = hm_Geters.keySet();
			Set<String> set_lazyfieldnames = hm_LazyGeters.keySet();
			fieldNames = new String[set_fieldnames.size()+set_lazyfieldnames.size()];
			int idx = 0;
			for (String name : set_fieldnames) {
				fieldNames[idx] = name;
				idx ++;
			}
			for (String name : set_lazyfieldnames) {
				fieldNames[idx] = name;
				idx ++;
			}
		}
		return fieldNames;
	}
	
	public String[] getAttributeNamesNoLazy(){
		Set<String> set_fieldnames = hm_Geters.keySet();
		return set_fieldnames.toArray(new String[0]);
	}
	
	public String[] getAttributeNamesLazy(){
		Set<String> set_fieldnames = hm_LazyGeters.keySet();
		return set_fieldnames.toArray(new String[0]);
	}

	/**
	 * 将一个对象String化 <br>
	 * 格式如下： <br>
	 * TABLE_NAME::表名 <br>
	 * 字段名::字段值 字段名::字段值
	 * 
	 * @param bean
	 * @return
	 */
	public String beanToString() {
		StringBuffer sb_tostring = new StringBuffer();
		sb_tostring.append("对象:'").append(bean.getEODisplayName())
				.append("' ");

		Set<String> fieldnames = hm_Geters.keySet();

		for (String fieldname : fieldnames) {
			Object obj_value = null;
			try {
				obj_value = getAttributeValue(fieldname);
				if (obj_value instanceof Date) {
					obj_value = DateUtil.getDateStr((Date) obj_value, null);
				} else if (obj_value instanceof BaseEO) {
					obj_value = ((BaseEO) obj_value).getId();
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
			sb_tostring.append(bean.getFieldDisplayName(fieldname)).append(":'").append(obj_value)
					.append("'\t");
		}

		return sb_tostring.toString();
	}

	/**
	 * 判断当前bean是否与参数对象相同
	 * 
	 * @param obj
	 * @return
	 */
	public boolean equalsBean(Object obj) {
		if (obj == null)// 对象为空不比较
			return false;

		// 不是BaseEO，不必比较
		if (!(obj instanceof BaseEO)) {
			return false;
		}

		// 类型不同不必进行比较
		if (!clazz.equals(obj.getClass())) {
			return false;
		}

		// 依次比较字段值，遇到不同的则返回false
		Set<String> fieldnames = hm_Geters.keySet();
		for (String fieldname : fieldnames) {
			boolean same = equalsField(fieldname, bean, obj);
			if (!same) {
				return false;
			}
		}

		return true;
	}

	/**
	 * 比较当前对象与另一个对象的差别，并返回值不同的字段的名称。
	 * 
	 * @author slx
	 * @date 2009-7-17 上午09:34:39
	 * @modifyNote
	 * @param antherBean
	 *            将要比较的对象
	 * @return 值不同的字段名
	 */
	public List<String> getDifferentField(BaseEO anotherBean) {
		// 类型不同不必进行比较
		if (!clazz.equals(anotherBean.getClass())) {
			throw new ClassCastException(anotherBean.getClass().getName()
					+ "Cann't Cast to " + clazz.getName());
		}
		List<String> differents = new ArrayList<String>();
		Set<String> fieldnames = hm_Geters.keySet();
		for (String fieldname : fieldnames) {
			boolean same = equalsField(fieldname, bean, anotherBean);
			if (!same) {
				differents.add(fieldname);
			}
		}
		return differents;
	}

	/**
	 * 比较两个对象，指定的字段值是否相同
	 * 
	 * @author slx
	 * @date 2009-7-17 上午09:51:58
	 * @modifyNote
	 * @param fieldName
	 *            需要比较的字段
	 * @param obj1
	 *            对象1
	 * @param obj2
	 *            对象2
	 * @return 值相同则为true
	 */
	protected boolean equalsField(String fieldName, Object obj1, Object obj2) {
		try {
			Object obj_value = null;
			Object current_value = null;
			Method getter = hm_Geters.get(fieldName);
			Object[] os = null;
			current_value = getter.invoke(obj1, os);
			obj_value = getter.invoke(obj2, os);

			if (current_value == null && obj_value == null) {
				return true;
			}else if(current_value != null && obj_value != null){
				if(current_value instanceof BaseEO && obj_value instanceof BaseEO){// 避免递归比较,内部字段如果是baseeo子类则只比较pk
					return ((BaseEO)current_value).equalsPK(obj_value);
				}
				if(current_value instanceof Date && obj_value instanceof Date){ // 日期类型比较特殊处理
					return DateUtil.equalsDate((Date)current_value, (Date)obj_value);
				}
				
				return obj_value.equals(current_value);
			}else if (current_value != null) {
				return current_value.equals(obj_value);
			} else if (obj_value != null) {
				return obj_value.equals(current_value);
			}

		} catch (Exception e) {
			throw new RuntimeException(e);
		}
		return false;
	}

	/**
	 * 返回该对象的哈希码值
	 */
	public int hashCodeBean() {

		// 生成简单的位运算hash散列码
		String key = bean.toString();
		int prime = key.hashCode();
		int hash = prime;
		for (int i = 0; i < key.length(); i++) {
			hash ^= (hash << 23 >> 17) ^ key.charAt(i) * 13131;
		}
		// 返回结果
		return (hash % prime) * 33;
	}

	/**
	 * 利用流深度克隆实体类
	 * 
	 * @return
	 * @throws IOException
	 * @throws ClassNotFoundException
	 */
	public Object cloneBean() throws IOException, ClassNotFoundException {
		return cloneObject(bean);
	}

	/**
	 * 取得一个枚举值上的描述注解.
	 * 
	 * @author slx
	 * @date 2009-9-3 上午09:13:53
	 * @modifyNote yongtree 2010-1-17修改
	 * @param emumValue
	 *            枚举值
	 * @return 如果传入的不是枚举值，则返回空串,或者枚举没有标注注解,则返回枚举toString.
	 */
	public static String getEnumDescription(Object emumValue) {
		String desValue = "";
		if (emumValue != null) {
			try {
				String enumName = ((Enum) emumValue).name();
				desValue = emumValue.getClass().getField(enumName)
						.getAnnotation(EnumDescription.class).value();
			} catch (Exception e) {
				return emumValue.toString();
			}
		}
		return desValue;
	}

	public static Object cloneObject(Object obj) throws IOException,
			ClassNotFoundException {
		ByteArrayOutputStream bo = new ByteArrayOutputStream();
		ObjectOutputStream oo = new ObjectOutputStream(bo);
		oo.writeObject(obj);
		ByteArrayInputStream bi = new ByteArrayInputStream(bo.toByteArray());
		ObjectInputStream oi = new ObjectInputStream(bi);
		Object cloneObj = (oi.readObject());
		bo.close();
		oo.close();
		bi.close();
		oi.close();
		return cloneObj;
	}
	
	/**
	 * 加载所有延迟加载的字段.
	 * 只加载一层，不加载延迟项内部的延迟字段。
	 * @author slx
	 * @date 2010年4月1日17:09:44
	 */
	public void loadLazyField(){
		Iterator<Method> i_mds = hm_LazyGeters.values().iterator();
		while(i_mds.hasNext()){
			Method m = i_mds.next();
			try {
				Object[] os = null;
				Object o = m.invoke(bean, os);
				if(o!=null)
					o.toString();
			} catch (IllegalArgumentException e) {
			} catch (IllegalAccessException e) {
			} catch (InvocationTargetException e) {
			}
		}
	}
	
	/**
	 * 加载指定的延迟加载字段。（递归方法）
	 * </br><b>注意：方法使用必须在数据库session开启的状态下。</b>
	 * @author slx
	 * @date 2011 5 9 10:11:21
	 * @modifyNote
	 * @param eo
	 * 		需要加载的对象，可以是BaseEO的子类，也可以是装载着BaseEO的List。
	 * @param fieldNames
	 * 		字段名，可以是当前对象的字段名，也可以是 org.users.roles的形式，users可以是list。
	 */
	public static void loadLazyField(Object eo ,String fieldNames){
		if(eo == null){
			return ;
		}
		if(StringUtils.isEmpty(fieldNames)){
			throw new IllegalArgumentException("Field name is null!");
		}
		
		String[] fields = fieldNames.split("\\.",2);
		String currentField = fields[0]; // 当前字段
		String innerField = null;		// 内部字段
		if(fields.length==2){
			innerField = fields[1];
		}
		
		if(eo instanceof List){// 对象为List， 循环list内所有对象。
			List ls = (List)eo;
			for (int li = 0; li < ls.size(); li++) {
				// 加载当前字段
				Object value = loadField((BaseEO)ls.get(li),currentField);
				// 有内部字段需要加载，以当前字段值为基准，递归加载内部字段。
				if(StringUtils.isNotEmpty(innerField)){
					loadLazyField(value,innerField);
				}
			}
		}else if(eo instanceof BaseEO){ // 对象为EO
			// 加载当前字段。
			Object value = loadField((BaseEO)eo,currentField);
			// 有内部字段需要加载，以当前字段值为基准，递归加载内部字段。
			if(StringUtils.isNotEmpty(innerField)){
				loadLazyField(value,innerField);
			}
		}else{ // eo 非法类型抛出异常
			throw new IllegalArgumentException("Entity mast be instance of BaseEO or List<BaseEO> . Current entity class:".concat(eo.getClass().getName()));
		}
	}
	
	/**
	 * 加载EO对象中的一个属性，并返回它的值。
	 * @author slx
	 * @date 2011 5 9 14:22:28
	 * @modifyNote
	 * @param eo
	 * @param fieldName
	 * @return
	 */
	protected static Object loadField(BaseEO eo ,String fieldName){
		boolean hasfiled = false;
		try {
			// 检查字段名是否存在
			PropertyDescriptor[] propertys = Introspector.getBeanInfo(eo.getClass()).getPropertyDescriptors();
			for (int i = 0; i < propertys.length; i++) {
				PropertyDescriptor p = propertys[i];
				String fdname = (String) p.getName();
				if(fieldName.equals(fdname)){
					hasfiled = true;
					break;
				}
			}
		} catch (IntrospectionException e) {
			e.printStackTrace();
		}
		if(hasfiled){// 字段名存在
			Object value = ((BaseEO) eo).getAttributeValue(fieldName);
			if(value != null){
				value.toString();
				return value;
			}
		}else{
			// 字段名不存在抛出异常
			throw new IllegalArgumentException(" FieldName is not exist : ".concat(eo.getClass().getName()).concat(".").concat(fieldName));
		}
		return null;
	}
	
}
