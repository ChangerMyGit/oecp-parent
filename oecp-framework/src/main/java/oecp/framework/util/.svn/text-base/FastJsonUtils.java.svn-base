package oecp.framework.util;

import java.util.List;
import java.util.Map;

import oecp.framework.entity.base.BaseEO;

import com.alibaba.fastjson.serializer.JSONSerializer;
import com.alibaba.fastjson.serializer.OECPJSONSerializer;
import com.alibaba.fastjson.serializer.PropertyFilter;
import com.alibaba.fastjson.serializer.SerializeWriter;
import com.alibaba.fastjson.serializer.SerializerFeature;

/**
 * FastJson工具类
 * 
 * @author wangliang
 * @date 2011-5-11
 */
public class FastJsonUtils {
	private final static String RELATION_PROPERTIEY = "relations";
	private final static String PREITEMID = "preItemID";
	/**
	 * 指定对象中需要输出json的字段，并获得json字符串。
	 * @param obj
	 *            转换源
	 * @param fields
	 *            输出字段
	 * @return
	 * @author wangliang
	 */
	public static String toJson(final Object obj, final String[] fields) {
		PropertyFilter filter = null;
		if (fields != null && fields.length > 0) {
			filter = new PropertyFilter() {// 属性过滤器
				public boolean apply(Object source, String name, Object value) {
					return isInArray(name, fields);
				}
			};
		}
		return toJson(obj, filter);
	}
	
	/**
	 * 默认的json输出。
	 * 对EO实体，只输出非延迟字段。
	 * @param obj
	 * @return
	 */
	public static String toJson(final Object obj) {
		PropertyFilter filter = new PropertyFilter() { // 默认过滤器，如果是BaseEO的实例，则默认输出所有非延迟加载字段
			@Override
			public boolean apply(Object source, String name, Object value) {
				if(source instanceof BaseEO){
					String[] nolazyfields = ((BaseEO)source).getBeanUtility().getAttributeNamesNoLazy();
					return isInArray(name, nolazyfields);
				}else{
					return true;
				}
			}
			
		};
		return toJson(obj,filter);
	}
	
	/**
	 * 把一张单据输出为json
	 * <br/>单据eo自身和子eo，输出所有字段。其他参照eo默认输出id、code和name三个字段。
	 * @param bill
	 * 		单据主对象
	 * @param bodyeoclasses
	 * 		单据子对象类，包括多主表的其他类和子表类
	 * @param except
	 * 		特例的EO类，指定类名和需要输出的字段（主要处理特殊的参照）
	 * @return
	 */
	public static String billToJson(final BaseEO bill,final Class<? extends BaseEO>[] bodyeoclasses ,final Map<Class<?>,String[]> except){
		PropertyFilter filter = null;
		if(bill != null){
			filter = new PropertyFilter() {// 过滤器
				String[] fields = null;
				public boolean apply(Object source, String name, Object value) {
					if(except!= null && except.containsKey(source.getClass())){ // 优先处理特例类对象的输出
						fields = except.get(source.getClass());
						return isInArray(name,fields);
					}else if(bill.equals(source)){ // 当前对象是主对象时,输出所有属性字段
						fields = bill.getAttributeNames();
						return isInArray(name,fields);
					}else if(isInArray(source.getClass(), bodyeoclasses)){ // 当前对象是子对象时，输出除主对象外的其他所有字段（防止死递归）。
						fields = ((BaseEO)source).getAttributeNames();
						return isInArray(name,fields) && !bill.equals(value);
					}else if(source instanceof List && name.equals(RELATION_PROPERTIEY)){// 如果是对照关系的集合，输出
						return true ;
					}else if(source instanceof BaseEO){ // 其他BaseEO的子类对象，只输出id/code/name三个字段
						return (name.equals("id") || name.indexOf("code") > -1 || name.indexOf("name") > -1);
					}else{	// 其他类型对象直接输出，不判断。
						return true;
					}
				}
			};
		}
		return toJson(bill, filter);
	}
	
	/**
	 * 判断是否在数组中
	 * @param obj
	 * @param objs
	 * @return
	 */
	private static boolean isInArray(Object obj,Object... objs){
		if(objs == null){
			return false;
		}
		for (int i = 0; i < objs.length; i++) {
			if(obj.equals(objs[i])){
				return true;
			}
		}
		if(RELATION_PROPERTIEY.equals(obj))
		   return true;
		if(PREITEMID.equals(obj))
		   return true;
		return false;
	}
	
	/**
	 * 使用过滤器输出json
	 * @param obj
	 * @param filter
	 * @return
	 */
	public static String toJson(Object obj ,PropertyFilter filter){
		SerializeWriter out = new SerializeWriter();
		JSONSerializer serializer = new OECPJSONSerializer(out); 
		// serializer.config(SerializerFeature.WriteMapNullValue, true);
		serializer.config(SerializerFeature.WriteEnumUsingToString, true);
		serializer.config(SerializerFeature.UseISO8601DateFormat, true);
		if(filter!=null)
			serializer.getPropertyFilters().add(filter);
		serializer.write(obj);
		return out.toString();
	}
	
}
