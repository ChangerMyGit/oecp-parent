package oecp.framework.util;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
/**
 * 泛型操作类
 * 
 * @author yongtree
 * @date 2009-11-11 下午05:36:01
 * @version 1.0
 */
public class GenericsUtils {
	/**
	 * 通过反射,获得定义Class时声明的父类的第1个范型参数的类型.
	 */
	@SuppressWarnings("unchecked")
	public static Class getSuperClassGenricType(Class clazz) {
		return getSuperClassGenricType(clazz, 0);
	}

	/**
	 * 通过反射，获得定义Class时声明的父类的范型参数的类型�? 如没有找到符合要求的范型参数，则递归向上直到Object.
	 * 
	 * @param clazz
	 *            要进行查询的类
	 * @param index
	 *            如有多个范型声明该索引从0开始
	 * @return 在index位置的范型参数的类型，如果无法判断则返回<code>Object.class</code>
	 */
	@SuppressWarnings("unchecked")
	public static Class getSuperClassGenricType(Class clazz, int index) {
		boolean flag = true;
		Type genType = clazz.getGenericSuperclass();
		Type[] params = null;

		if (!(genType instanceof ParameterizedType))
			flag = false;
		else {
			params = ((ParameterizedType) genType).getActualTypeArguments();
			if (index >= params.length || index < 0)
				flag = false;
			if (!(params[index] instanceof Class))
				flag = false;
		}
		if (!flag) {
			clazz = clazz.getSuperclass();
			if (clazz == Object.class)
				return Object.class;
			else
				return getSuperClassGenricType(clazz, index);
		}
		return (Class) params[index];
	}
}
