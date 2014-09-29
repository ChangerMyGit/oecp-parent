/**
 * oecp-framework - BaseNormalVOUtility.java
 * copyright 2009-2012 OECP www.oecp.cn
 * 创建人:slx	创建时间:2011-11-2下午4:13:51		版本:v1
 * ============================================
 * 修改人：			修改时间:					版本:
 * 修改原因:
 *
 * ============================================
 */
package oecp.framework.vo.base;

import java.beans.IntrospectionException;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.HashMap;

import javax.persistence.Transient;

import oecp.framework.exception.BizException;

/**
 * BaseNormalVOVO工具类
 * @author slx
 * @date 2011-11-2
 */
public class BaseNormalVOUtility {
	protected PropertyDescriptor[] propertyDescriptors = null;
	protected HashMap<String, Method> hm_Geters = new HashMap<String, Method>();
	protected HashMap<String, Method> hm_Seters = new HashMap<String, Method>();
	
	public BaseNormalVOUtility(Class<?> voclass) {
		init(voclass);
	}
	
	private void init(Class<?> voclass){
		Field[] fields = voclass.getDeclaredFields();
		String[] fieldnames = new String[fields.length];
		for (int i = 0; i < fields.length; i++) {
			if(fields[i].isAnnotationPresent(Transient.class)){// 非串行化字段不理会
				continue;
			}
			fieldnames[i] = fields[i].getName();
			try {
				propertyDescriptors = Introspector.getBeanInfo(voclass).getPropertyDescriptors();
				for (PropertyDescriptor property : propertyDescriptors) {
					if (fieldnames[i].equals(property.getName())) {
					Method reader = property.getReadMethod();
					Method writer = property.getWriteMethod();
						if (reader != null && (!reader.isAnnotationPresent(Transient.class)|| "id".equals(property.getName()))){
							hm_Geters.put(fieldnames[i], reader);
							if (writer != null){
								hm_Seters.put(fieldnames[i], writer);
							}
						}
					}
				}
			}catch (IntrospectionException e) {
			}
		}
		// 当前类不是 Object时，递归调用
		if (!voclass.equals(Object.class)) {
			init(voclass.getSuperclass());
		}
	}
	
	public String[] getFieldNames() {
		String[] fieldNames = new String[0];
		fieldNames = hm_Geters.keySet().toArray(fieldNames);
		return fieldNames;
	}
	
	public Object getValue(Object normalvo ,String fieldname) {
		Method m = hm_Geters.get(fieldname);
		if(m!=null){
			try {
				return m.invoke(normalvo, null);
			} catch (Exception e) {
				e.printStackTrace();
			} 
		}
		return null;
	}
	
	public void setValue(Object normalvo,String fieldname, Object value) {
		Method m = hm_Seters.get(convertFieldName(fieldname));
		if(m!=null){
			try {
				if(value!=null){ 
					value = convertValue(value , m.getParameterTypes()[0]);
					m.invoke(normalvo, value);
				}
			} catch (Exception e) {
				e.printStackTrace();
			} 
		}
	}
	
	public Class<?> getFieldType(String fieldname) {
		return hm_Geters.get(fieldname).getReturnType();
	}
	
	public void removeField(String... fieldname){
		for (int i = 0; i < fieldname.length; i++) {
			hm_Geters.remove(fieldname[i]);
		}
	}
	
	/**
	 * 转换字段名称。
	 * 将字段的名称转换为小写（因为ORACLE数据库查询语句会将字段名都返回为大写，这里进行处理。字段命名最好全都小写）
	 * 子类如有自己的转换方法可覆盖。
	 * @author slx
	 * @date 2011-8-30上午09:06:18
	 * @param fieldname
	 * @return
	 */
	protected String convertFieldName(String fieldname){
		return fieldname.toLowerCase();
	}
	/**
	 * 将值转换为指定类型。
	 * 	解决数据库查询出的类型，与vo类型不一致问题。
	 * 如有必要，子类自行覆盖。
	 * @author slx
	 * @date 2011-8-30上午09:10:55
	 * @param fromvalue
	 * @param toClass
	 * @return
	 * @throws BizException 
	 */
	protected Object convertValue(Object fromvalue , Class toClass) throws BizException{
		// 类型不同就做转换
		if( !toClass.equals(Object.class) && !fromvalue.getClass().equals(toClass)){
			try {
				fromvalue = toClass.getConstructor(String.class).newInstance(fromvalue.toString());
			} catch (Exception e) {
				throw new BizException("value类型转换失败：" .concat(fromvalue.getClass().getName()).concat(" -> ").concat(toClass.getName()));
			}
		}
		return fromvalue;
	}
}
