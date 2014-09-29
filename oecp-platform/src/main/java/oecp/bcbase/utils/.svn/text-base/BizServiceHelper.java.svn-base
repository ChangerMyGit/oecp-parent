package oecp.bcbase.utils;

import java.lang.reflect.Method;
import java.lang.reflect.ParameterizedType;
import java.util.ArrayList;
import java.util.Collection;

import oecp.bcbase.eo.annotations.BillItems;
import oecp.bcbase.eo.annotations.BillMain;
import oecp.framework.entity.base.BaseEO;

/**
 * 业务EO工具
 * 
 * @author slx
 * @date 2011-12-26
 */
public class BizServiceHelper {

	public static Class<? extends BaseEO<?>>[] getItemClasses(BaseEO bill){
		String[] attrnames = bill.getAttributeNames();
		ArrayList<Class<? extends BaseEO<?>>> itemnames = new ArrayList<Class<? extends BaseEO<?>>>();
		for (int i = 0; i < attrnames.length; i++) {
			Method md = bill.getBeanUtility().getGetter(attrnames[i]);
			if (md.isAnnotationPresent(BillItems.class)) {
				Class<?> clazz = md.getReturnType();
				if(BaseEO.class.isAssignableFrom(clazz)){
					itemnames.add((Class<? extends BaseEO<?>>)clazz);
				}else if(Collection.class.isAssignableFrom(clazz)){
					ParameterizedType pt = (ParameterizedType)md.getGenericReturnType();
					Class<? extends BaseEO<?>> realType = (Class<? extends BaseEO<?>>)pt.getActualTypeArguments()[0];
					itemnames.add(realType);
				}
			}
		}
		return itemnames.toArray(new Class[0]);
	}
	
//	public static Class<? extends BaseEO<?>>[] getItemClasses(BaseArchivesEO archives){
//		String[] attrnames = archives.getAttributeNames();
//		ArrayList<Class<? extends BaseEO<?>>> itemnames = new ArrayList<Class<? extends BaseEO<?>>>();
//		for (int i = 0; i < attrnames.length; i++) {
//			Method md = archives.getBeanUtility().getGetter(attrnames[i]);
//			if (md.isAnnotationPresent(BillItems.class)) {
//				Class<?> clazz = md.getReturnType();
//				if(BaseEO.class.isAssignableFrom(clazz)){
//					itemnames.add((Class<? extends BaseEO<?>>)clazz);
//				}else if(Collection.class.isAssignableFrom(clazz)){
//					ParameterizedType pt = (ParameterizedType)md.getGenericReturnType();
//					Class<? extends BaseEO<?>> realType = (Class<? extends BaseEO<?>>)pt.getActualTypeArguments()[0];
//					itemnames.add(realType);
//				}
//			}
//		}
//		return itemnames.toArray(new Class[0]);
//	}
	
	/**
	 * 获得单据主表对象中，所有子表对象的对应字段名称。
	 * @author slx
	 * @date 2011-12-26
	 * @param bill
	 * @return
	 */
	public static String[] getItemFieldNames(BaseEO bill){
		String[] attrnames = bill.getAttributeNames();
		ArrayList<String> itemnames = new ArrayList<String>();
		for (int i = 0; i < attrnames.length; i++) {
			Method md = bill.getBeanUtility().getGetter(attrnames[i]);
			if (md.isAnnotationPresent(BillItems.class)) {
				itemnames.add(attrnames[i]);
			}
		}
		return itemnames.toArray(new String[0]);
	}
	
	/**
	 * 获得子表对象中，主表对象对应的字段名称。
	 * @author slx
	 * @date 2011-12-26
	 * @param item
	 * @return
	 */
	public static String getBillFieldInItem(BaseEO<?> item){
		if (item == null)
			return null;
		String[] attrnames = item.getAttributeNames();
		for (int i = 0; i < attrnames.length; i++) {
			Method md = item.getBeanUtility().getGetter(attrnames[i]);
			if (md.isAnnotationPresent(BillMain.class)) {
				return attrnames[i];
			}
		}
		return null;
	}
	
	public Object reflectInvokeMethod(String className,String methodName,Class<?>[] paramsTypes,Object[] paramsValues)throws Exception{
		Object object = null;
		try {
			Class<?> currentClass = Class.forName(className);
			Method m = currentClass.getMethod(methodName,paramsTypes);
			object = m.invoke(currentClass.newInstance(),paramsValues);
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}
		return object;
	}
}
