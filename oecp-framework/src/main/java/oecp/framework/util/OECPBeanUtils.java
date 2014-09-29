/************************* 版权声明 *********************************
 *                                                                  *
 *                     版权所有：百洋软件                          				 	*
 *          Copyright (c) 2011 by www.oecp.cn                	*
 *                                                                  *
 ************************* 变更记录 *********************************
 *
 * 创建者：宋黎晓   创建日期： 2011 5 23
 * 备注：
 * 
 * 修改者：       修改日期：
 * 备注：
 * 
 */

package oecp.framework.util;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.EmptyStackException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Stack;

import org.apache.commons.lang.StringUtils;

/**
 * 实体工具
 * 
 * @author slx
 * @date 2011 5 23 13:37:44
 * @version 1.0
 */
public class OECPBeanUtils {

	/**
	 * 在两个对象之间复制值。支持formObj.user.id -> toObj.userid形式的内部对象值读取和复制。
	 * 注意：<b>不支持List或者数组内部对象的复制。</b>
	 * 
	 * @author slx
	 * @date 2011 5 23 10:44:39
	 * @modifyNote
	 * @param formObj
	 *            数据来源对象
	 * @param toObj
	 *            复制数据目的对象
	 * @param formula
	 *            复制公式形式如
	 *            "code->code,name->userName,parent.id->org.id",逗号分隔开字段对应关系
	 *            ，->符号表示左边复制到右边。 来源和目的字段都可以使用aaa.bbb的形式，表示当前对象aaa属性内的bbb属性。
	 *            </br> 符号“->”左边为“取值表达式”，右边为“赋值表达式” </br>
	 *            取值表达式的有特殊形式，用来设置取出一个默认值
	 *            ，形式为："[package1.package1.Class:stringValue]"
	 *            以中括号包围，冒号前为全类名，冒号后为字符串类型的值。
	 *            <b>注意：当使用默认值公式时，默认值的所属类型，必须要有字符串为参数的构造函数。如 Boolean("true")。
	 *            取值特殊公司也支持枚举Class
	 *            。另外，如果要将一个字符串赋值给枚举，同样也支持，但有个前提，即字符串能够对应上目的字段枚举内的name属性。</b>
	 *            </br> </br> 赋值表达式，也有特殊形式，是为了解决对象内部list赋值问题。例如a.list需要赋值给
	 *            b.list,b.list内的对象是ClassC的对象,可以用这种形式表达： </br>
	 *            a.list->b.list[List
	 *            :ClassC:alistField1->blistField1,alistField2->blistField2]
	 *            a.list表示数据来源
	 *            ，b.list表示目标字段，[]表示特殊公式，List表明目标字段为List，ClassC表明目标List中需要存什么对象
	 *            ，ClassC：后面是内部对象赋值公式。
	 *            内部对象赋值公式（子公式）与外部对象赋值公式规则相同，并支持子公式内再次嵌套特殊公式和子公式。 </br>
	 */
	public static void copyObjectValue(Object formObj, Object toObj,
			String formula) {
		// 先把特殊公式分离，用占位符替代。
		FormulaPlaceholderUtils fu = new FormulaPlaceholderUtils();
		formula = fu.replaceSubFormulaWithPlaceholder(formula);
		String[] rowfms = formula.split(","); // 分解出每行的公式
		String[] rowfm;
		String from;
		String to;
		Object value = null;
		// 循环实现，每个公式的取值和复制
		for (int i = 0; i < rowfms.length; i++) {
			rowfm = rowfms[i].split("->"); // 分解出公式的来源于目的字段
			if (rowfm == null || rowfm.length != 2) {
				throw new RuntimeException("Formula Error : ".concat(formula)
						.concat(", wrong at :").concat(rowfms[i]));
			}
			// 将占位符换回特殊公式
			from = fu.recoverExpressionPlaceholder(rowfm[0].trim());
			to = fu.recoverExpressionPlaceholder(rowfm[1].trim());

			// 根据取值公式取值，然后执行赋值公式赋值。

			value = getFieldValueByFormule(formObj, from);
			setFieldValueByFormule(toObj, to, value);
		}
	}

	static class FormulaPlaceholderUtils {

		private Map<String, String> placeholderMap = new HashMap<String, String>();

		/**
		 * 将公式中的特殊表达式替换为占位符。
		 * 
		 * @author slx
		 * @date 2011-8-12下午03:27:20
		 * @param formula
		 * @return
		 */
		String replaceSubFormulaWithPlaceholder(String formula) {
			// 找到特殊公式，并换为占位符，将占位符和公式存到map中。
			Stack<Integer> opl = new Stack<Integer>();
			int len = formula.length();
			try {
				int is = 0;// 特殊表达式开始索引
				int ie = 0;// 结束索引
				for (int i = 0; i < len; i++) {
					char c = formula.charAt(i);
					if (c == '[') {
						opl.push(i);
					} else if (c == ']') {
						is = opl.pop(); // 如果从栈顶取值异常，则说明]多了，公式错误。
						if (opl.size() == 0) {
							ie = i;
							break;
						}
					}
				}

				if (opl.size() > 0) { // 如果循环完成后，栈内还有值，说明[多了，公式错误。
					throw new RuntimeException("Formula format error !! ");
				}
				if (ie == 0) { // 结束索引等于0，说明不包含特殊表达式。
					return formula;
				} else { // 包含特殊表达式
					String subf = formula.substring(is, ie + 1);
					String placeholder = "@" + is + ie + subf.hashCode();
					placeholderMap.put(placeholder, subf);
					String resultFormula = formula.substring(0, is)
							.concat(placeholder)
							.concat(formula.substring(ie + 1));
					// 递归调用，解决同级别的其他特殊表达式。
					return replaceSubFormulaWithPlaceholder(resultFormula);
				}
			} catch (EmptyStackException e) {
				throw new RuntimeException("Formula format error !! ");
			}
		}

		/**
		 * 将带有占位符的表达式还原
		 * 
		 * @author slx
		 * @date 2011-8-12下午03:28:00
		 * @param formula
		 * @return
		 */
		String recoverExpressionPlaceholder(String formula) {
			// 从公式中找占位符，如果找到就用占位符从map中取出公式，然后替换掉占位符。
			int idx = formula.indexOf("@");
			if (idx >= 0) {
				String placeholder = formula.substring(idx);
				formula = formula.replaceFirst(placeholder,
						placeholderMap.get(placeholder));
			}
			return formula;
		}

	}

	/**
	 * 根据公式获取对象中的值。公式支持(aaa.bbb.ccc)的属性取值形式和默认值形式。
	 * 默认值形式为："[package.Class:stringValue]" 以中括号包围，冒号前为全类名，冒号后为字符串类型的值。
	 * <b>注意：当使用默认值公式时，默认值的所属类型，必须要有字符串为参数的构造函数。如 Boolean("true");
	 * 
	 * @author slx
	 * @date 2011 5 23 15:08:38
	 * @modifyNote
	 * @param obj
	 * @param getterformule
	 * @return
	 */
	public static Object getFieldValueByFormule(Object obj, String getterformule) {
		Object value = null;
		/** 默认值公式 [xxx.xxx.xxx.ClassA:stringValue] **/
		if (getterformule.startsWith("[")) {
			getterformule = getterformule.replaceAll("\\[", "");
			getterformule = getterformule.replaceAll("\\]", "");
			String[] config = getterformule.split(":");
			String className = config[0];
			String strValue = config[1];

			try {
				Class clazz = Class.forName(className);
				if (clazz.isEnum()) {// 处理枚举
					value = Enum.valueOf(clazz, strValue);
				} else {
					Constructor constructor = clazz
							.getConstructor(String.class);
					value = constructor.newInstance(strValue);
				}
			} catch (ClassNotFoundException e) {
				throw new RuntimeException(
						"Formula Error ,ClassNotFound : ".concat(className));
			} catch (NoSuchMethodException e) {
				throw new RuntimeException(
						"Formula Error , no Constructor(String) : "
								.concat(className));
			} catch (Exception e) {
				throw new RuntimeException(
						"Formula Error , Constructor(String) can not create newInstance: "
								.concat(className).concat(" ").concat(strValue));
			}
		} else {
			/** 普通取字段值公式 **/
			// 取来源的值，如果第一级没取到，则认为值为null。
			String[] fields = getterformule.split("\\.");
			for (int j = 0; j < fields.length; j++) {
				String field = fields[j];
				try {
					if (j == 0) // 第一级字段从原始fromObj中取值，其他均从下级取到的值内部再取。
						value = ReflectionUtils.getFieldValue(obj, field);
					else
						value = ReflectionUtils.getFieldValue(value, field);
					if (value == null)
						break;
				} catch (IllegalArgumentException e) {
					throw new RuntimeException("Formula Error : "
							.concat(getterformule)
							.concat(", Field is not exist :")
							.concat(obj.getClass().getName()).concat(" ")
							.concat(field));
				}
			}
			if (value != null && value.getClass().isEnum()) {
				value = value.toString();
			}
		}
		return value;
	}

	/**
	 * 按照公式给一个字段赋值（仅支持aaa.bbb.ccc形式）
	 * 
	 * @author slx
	 * @date 2011 5 23 15:18:51
	 * @modifyNote
	 * @param toObj
	 * @param setterformule
	 * @param value
	 */
	public static void setFieldValueByFormule(Object toObj,
			String setterformule, Object value) {
		int idxl = setterformule.indexOf("[");
		/** 如果赋值公式是特殊公式，则进行特殊处理*（创建列表）。 */
		if (idxl > 0) {
			int idxcs = setterformule.indexOf(":");
			String st = setterformule.substring(idxcs + 1);
			int idxce = st.indexOf(":");
			String className = st.substring(0, idxce); // list内class类名
			String subFormula = st.substring(idxce + 1, st.length() - 1); // 子公式
			try {
				// 用创建的新列表替代原来的value
				value = createObjectList((List) value,
						Class.forName(className), subFormula);
			} catch (ClassNotFoundException e) {
				throw new RuntimeException("Formula Error : ClassNotFound "
						.concat(className).concat("!!"));
			}

			setterformule = setterformule.substring(0, idxl); // 重新设置外层公式
		}

		/** 普通表达式的执行逻辑 */
		// 设置目的字段值，如果没取到下级字段则自动创建一个下级的实例，然后赋值。
		Object realTo = null;
		String[] fields = setterformule.split("\\.");
		// 取的最终需要赋值的对象，如： aaa.bbbb.cccc.dddd,这里是取得对象cccc。
		for (int j = 0; j < fields.length - 1; j++) { // 如果只有一级字段，则不进入循环，直接setvalue。
			String field = fields[j];
			try {
				if (j == 0) {
					realTo = ReflectionUtils.getFieldValue(toObj, field);
					if (realTo == null) { // 中间目标为空，则创建一个新的对象，并放入到上级字段中。
						Field f = ReflectionUtils.getAccessibleField(toObj,
								field);
						if (f == null) { // 没有取到字段则说明字段不存在，公式错误！
							throw new RuntimeException("Formula Error : "
									.concat(setterformule)
									.concat(", Field is not exist :")
									.concat(setterformule).concat(" ")
									.concat(field));
						}
						Class fieldClass = f.getType();
						realTo = fieldClass.newInstance();
						ReflectionUtils.setFieldValue(toObj, field, realTo);
					}
				} else {
					Object tempRealTo = ReflectionUtils.getFieldValue(realTo,
							field);
					if (tempRealTo == null) { // 中间目标为空，则创建一个新的对象，并放入到上级字段中。
						Field f = ReflectionUtils.getAccessibleField(realTo,
								field);
						if (f == null) { // 没有取到字段则说明字段不存在，公式错误！
							throw new RuntimeException("Formula Error : "
									.concat(setterformule)
									.concat(", Field is not exist :")
									.concat(setterformule).concat(" ")
									.concat(field));
						}
						Class fieldClass = f.getType();
						tempRealTo = fieldClass.newInstance();
						ReflectionUtils
								.setFieldValue(realTo, field, tempRealTo);
					}
					realTo = tempRealTo;
				}

			} catch (IllegalArgumentException e) {
				e.printStackTrace();
				throw new RuntimeException("Formula Error : "
						.concat(setterformule).concat(", Field is not exist :")
						.concat(field));
			} catch (InstantiationException e) {
				e.printStackTrace();
				throw new RuntimeException(
						"Object can not Instance : in field ".concat(field)
								.concat(". ").concat(e.getMessage()));
			} catch (IllegalAccessException e) {
				e.printStackTrace();
				throw new RuntimeException(
						"Object can not Instance : in field ".concat(field)
								.concat(". ").concat(e.getMessage()));
			}
		}
		// 真实需要赋值的对象没有取到，则说明只有一级，没有进入循环。真实对象直接等于toObj。
		if (realTo == null) {
			realTo = toObj;
		}
		// 为对象赋值，fields[fields.length - 1]嵌套级联字段的最后一个。

		Field tofild = ReflectionUtils.getAccessibleField(realTo,
				fields[fields.length - 1]);
		Class toClass = tofild.getType();
		// 如果目标字段为枚举，则将值转换为枚举后，再赋值。
		if (toClass.isEnum() && value != null) {
			value = Enum.valueOf(toClass, value.toString());
		}
		ReflectionUtils.setFieldValue(realTo, fields[fields.length - 1], value);
	}

	/**
	 * 并按照指定类型创建列表，并根据公式从源列表复制值。
	 * 
	 * @author slx
	 * @date 2011 5 23 14:19:50
	 * @modifyNote
	 * @param <T>
	 * @param formlist
	 *            数据来源列表
	 * @param toClass
	 *            要创建的类表内存储的对象类型
	 * @param formula
	 *            请参照 copyFieldValue的说明
	 * @return List内包含toClass类型的对象
	 */
	public static <T> List<T> createObjectList(List formlist, Class<T> toClass,
			String formula) {
		if (formlist == null) {
			return null;
		}
		int len = formlist.size();
		List<T> toObjs = new ArrayList<T>(len);
		for (int i = 0; i < len; i++) {
			T toObj;
			try {
				toObj = toClass.newInstance();
			} catch (Exception e) {
				e.printStackTrace();
				throw new RuntimeException("Object can not Instance : Class "
						.concat(toClass.getName()).concat(". ")
						.concat(e.getMessage()));
			}
			copyObjectValue(formlist.get(i), toObj, formula);
			toObjs.add(toObj);
		}

		return toObjs;
	}

	/**
	 * 将java bean中的某些属性转换成map
	 * 
	 * @author yongtree
	 * @date 2011-5-25 下午05:28:44
	 * @param o
	 * @param properties
	 *            要转换的属性 支持a.b.c 支持a->a1 和a->a1.b->b1.c
	 * @return
	 */
	public static Map toMap(Object o, String[] properties) {
		return toMap(o, properties, null);
	}

	/**
	 * 将java bean中的某些属性转换成map
	 * 
	 * @author yongtree
	 * @date 2011-5-25 下午05:28:44
	 * @param o
	 * @param properties
	 *            要转换的属性，支持a.b.c 支持a->a1 和a->a1.b->b1.c
	 * @param dateformat
	 *            日期转换格式
	 * @return
	 */
	public static Map toMap(Object o, String[] properties, String dateformat) {
		if (o instanceof Collection)
			throw new RuntimeException("Object can't be Collection");
		Map map = new HashMap();
		for (String field : properties) {
			getFieldValue(map, o, null, field, dateformat);
		}
		return map;
	}

	private static Map getFieldValue(Map map, Object o, String parentField,
			String field, String dateformat) {
		if (o == null)
			return null;
		Map parent = map;
		if (parentField != null) {
			parent = (Map) map.get(parentField);
		}
		if (field.contains(".")) {
			String f = field.substring(0, field.indexOf("."));
			String[] tf = getTranFields(f);
			Object o1 = ReflectionUtils.getFieldValue(o, tf[0]);
			if (!parent.containsKey(tf[1])) {
				parent.put(tf[1], new HashMap());
			}
			getFieldValue(parent, o1, tf[1],
					field.substring(field.indexOf(".") + 1, field.length()),
					dateformat);
		} else {
			String tf[] = getTranFields(field);
			Object value = ReflectionUtils.getFieldValue(o, tf[0]);
			if ((value instanceof Date) && StringUtils.isNotEmpty(dateformat)) {
				value = DateUtil.getDateStr((Date) value, dateformat);
			}
			parent.put(tf[1], value);
		}
		return parent;
	}

	private static String[] getTranFields(String field) {
		String[] f = field.split("->");
		if (f.length == 2) {
			return new String[] { f[0], f[1] };
		} else {
			return new String[] { field, field };
		}
	}

}
