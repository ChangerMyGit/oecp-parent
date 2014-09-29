/************************* 版权声明 *********************************
 * 
 * 版权所有：百洋软件
 * Copyright (c) 2009 by Pearl Ocean.
 * 
 ************************* 变更记录 *********************************
 *
 * 创建者：slx   创建日期： 2009-7-8
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

package oecp.framework.dao;

import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.lang.reflect.Method;
import java.util.Collection;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.persistence.Entity;
import javax.persistence.Id;

import org.hibernate.Query;


/**
 * SQL语句构建工具类
 * @author slx
 * @date 2009-7-8 上午10:35:28
 * @version 1.0
 */
public class QLBuilder {
	
	/**
	 * 获取实体的名称
	 * 
	 * @param <T>
	 * @param entityClass
	 *            实体类
	 * @return
	 */
	public static <T> String getEntityName(Class<T> entityClass) {
		String entityname = entityClass.getName();
		Entity entity = entityClass.getAnnotation(Entity.class);
		if(entity!=null){
			if (entity.name() != null && !"".equals(entity.name())) {
				entityname = entity.name();
			}
		}
		return entityname;
	}
	
	/**
	 * 创建Select后所要查询的字段名称字符串
	 * @author slx
	 * @date 2009-7-8 上午10:01:02
	 * @modifyNote
	 * @param fields 
	 * 			需要查询的字段
	 * @param alias  
	 * 			表的别名
	 * @return
	 * 			拼接成的字段名字符串
	 */
	public static String buildSelect(String[] fields, String alias) {
		StringBuffer sf_select = new StringBuffer("SELECT");
		for (String field : fields) {
			sf_select.append(" ").append(alias).append(".").append(field)
					.append(",");
		}
		return (sf_select.substring(0, sf_select.length() - 1)).toString();
	}
	
	/**
	 * 创建Select后所要查询的字段名称字符串，并作为实体类的构造函数
	 * @author yongtree
	 * @date 2010-4-13 上午11:59:04
	 * @modifyNote
	 * @param fields
	 * @param alias
	 * @return
	 */
	public static String buildSelect(String className,String[] fields, String alias) {
		StringBuffer sf_select = new StringBuffer("SELECT new ").append(className).append("(");
		for (String field : fields) {
			sf_select.append(" ").append(alias).append(".").append(field)
					.append(",");
		}
		return (sf_select.substring(0, sf_select.length() - 1))+")";
	}
	
	
	/**
	 * 组装order by语句
	 * 
	 * @param orderby
	 * 		列名为key ,排序顺序为value的map
	 * @return
	 * 		Order By 子句
	 */
	public static String buildOrderby(LinkedHashMap<String, String> orderby) {
		StringBuffer orderbyql = new StringBuffer("");
		if (orderby != null && orderby.size() > 0) {
			orderbyql.append(" order by ");
			for (String key : orderby.keySet()) {
				orderbyql.append("o.").append(key).append(" ").append(
						orderby.get(key)).append(",");
			}
			orderbyql.deleteCharAt(orderbyql.length() - 1);
		}
		return orderbyql.toString();
	}
	
	/**
	 * 得到Count聚合查询的聚合字段,既是主键列
	 * @author slx
	 * @date 2009-7-8 上午10:26:11
	 * @modifyNote
	 * @param <T>
	 * 				实体类型
	 * @param clazz		
	 * 				实体类
	 * @param alias
	 * 				表别名
	 * @return
	 * 				聚合字段名(主键名)
	 */
	public static <T> String getPkField(Class<T> clazz, String alias) {
		String out = alias;
		try {
			PropertyDescriptor[] propertyDescriptors = Introspector
					.getBeanInfo(clazz).getPropertyDescriptors();
			for (PropertyDescriptor propertydesc : propertyDescriptors) {
				Method method = propertydesc.getReadMethod();
				if (method != null && method.isAnnotationPresent(Id.class)) {
//					PropertyDescriptor[] ps = Introspector.getBeanInfo(
//							propertydesc.getPropertyType())
//							.getPropertyDescriptors();
					out = alias
							+ "."
							+ propertydesc.getName();
//							+ "."
//							+ (!ps[1].getName().equals("class") ? ps[1]
//									.getName() : ps[0].getName()
//									);
					break;
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return out;
	}
	
	/**
	 * 设置查询参数
	 * @author slx
	 * @date 2009-7-8 上午10:02:55
	 * @modifyNote
	 * @param query	
	 * 			查询
	 * @param queryParams
	 * 			查询参数
	 */
	public static Query setQueryParams(Query query, Object queryParams) {
		if (queryParams != null) {
			if (queryParams instanceof Object[]) {
				Object[] params = (Object[]) queryParams;
				if (params.length > 0) {
					for (int i = 0; i < params.length; i++) {
						query.setParameter(i, params[i]);
					}
				}
			} else if (queryParams instanceof Map) {
				Map params = (Map) queryParams;
				Iterator<String> it = params.keySet().iterator();
				while(it.hasNext()){
					String key = it.next();
					query.setParameter(key, params.get(key));
				}
			}
		}
		return query;
	}
	
	/**
	 * 将集合中的字符串拼接成为SQL语句中 in的形式 'aaa','bbb','ccc'
	 * @author slx
	 * @date 2009-5-26 上午10:30:17
	 * @modifyNote
	 * @param values
	 * @return
	 */
	public static String toSQLIn(Collection<String> values){
		if(values == null || values.isEmpty())
			return null;
		
		String[] strvalues = new String[0];
		strvalues = (String[]) values.toArray(new String[values.size()]);
		
		return toSQLIn(strvalues);
	}
	
	/**
	 * 将字符串数组中的字符串拼接成为SQL语句中 in的形式 'aaa','bbb','ccc'
	 * @author slx
	 * @date 2009-5-26 上午10:30:17
	 * @modifyNote
	 * @param values
	 * @return
	 */
	public static String toSQLIn(String[] values){
		StringBuffer bf_sqlin = new StringBuffer();
		if(values == null || values.length == 0)
			return null;
		
		int len = values.length;
		for(int i = 0 ; i < len ; i++){
			bf_sqlin = bf_sqlin.append(", '").append(values[i]).append("' ");
		}
		String str_sqlin = bf_sqlin.substring(1).toString();
		
		return str_sqlin;
	}

	/**
	 * 给当前的Hql语句追加Where查询条件
	 * @author slx
	 * @date 2011-12-21
	 * @param hql
	 * 		需要追加查询条件的Hql
	 * @param where
	 * 		查询条件
	 * @return
	 * 		追加查询条件后的查询语句
	 */
	public static String appendWhere(String hql ,String where) {
		hql = keywordsToUpperCase(hql);
		if(org.apache.commons.lang.StringUtils.isEmpty(where)){
			return hql;
		}
		// 由于HQL中子查询只能用在where中不能用在from中，所以查询语句中第一个where一定是外层主查询的
		int idx_where = wordIndexOf(hql, WHERE);
		// 找到非子查询中的Order 或者 Group 关键字位置 x
		int idx_group = wordLastIndexOf(hql, GROUP);
		int idx_order = wordLastIndexOf(hql, ORDER);
		int idx_endsub = hql.lastIndexOf(")"); // 最后一个子查询的结束位置
		int idx_beginsub = hql.lastIndexOf("("); // 最后一个子查询的开始位置
		int idx_x = -1;
		idx_x = idx_group < idx_endsub && idx_group > idx_beginsub  ? -1 : idx_group;	// group by 位置出现在'()'之间，说明group位置出现的是子查询中的关键字，主查询中没有group by。
		if(idx_x == -1){ // 如果主查询中没有 group by 则看主查询中有没有 order by
			idx_x = idx_order < idx_endsub && idx_order > idx_beginsub  ? -1 : idx_order; 
		}
		// 如果有Where，追加‘And’+where加条件;如果没有Where
		if (idx_where > 0) {
			where = "AND ".concat(where);
		}else{
			where = WHERE.concat(" ").concat(where);
		}
		// 将where放到x之前，如果x大于0的话。否则直接追加到查询语句最后
		if (idx_x > 0) {
			hql = hql.substring(0, idx_x).concat(" ".concat(where).concat(" ")).concat(hql.substring(idx_x));
		}else{
			hql = hql.concat(" ".concat(where).concat(" "));
		}
		return hql;
	}

	private static final String SELECT = "SELECT";
	private static final String DISTINCT = "DISTINCT";
	private static final String FROM = "FROM";
	private static final String WHERE = "WHERE";
	private static final String ORDER = "ORDER BY";
	private static final String GROUP = "GROUP BY";
	private static final String AS = "AS";
	private static final String[] QLKEYWORDS = new String[]{SELECT,DISTINCT,FROM,WHERE,ORDER,GROUP,AS};
	/**
	 * 将一句查询对象HQL的语句转换为查询行数的HQL语句
	 * <br/><b>注意：查询语句中自身不能带有Group By子句，否则转换后的行数查询执行的结果将不正确</b>
	 * <br/><b>注意：查询语句中自身带有DISTINCT，主实体别名必须为o</b>
	 * @author slx
	 * @date 2011-12-21
	 * @param queryhql
	 * 		查询语句
	 * @return
	 */
	public static String transformQyeryHql2CountHql(String queryhql) {
		queryhql = keywordsToUpperCase(queryhql);
		boolean hasDist = queryhql.substring(0, queryhql.indexOf(FROM)).indexOf(DISTINCT)!=-1;
		queryhql = queryhql.substring(queryhql.indexOf(FROM)); // 只保留 FROM及以后语句
		queryhql = SELECT.concat(hasDist?" COUNT(DISTINCT o) ":" COUNT(*) ").concat(queryhql); // 追加SELECT COUNT
		if(queryhql.indexOf(ORDER)!=-1){ // 带有ORDER BY的需要剔除ORDER BY子句
			queryhql.substring(0,queryhql.indexOf(ORDER));
		}
		return queryhql;
	}
	
	/**
	 * 将查询语句中的关键字全部转为大写
	 * @author slx
	 * @date 2011-12-21
	 * @param ql
	 * @return
	 */
	public static String keywordsToUpperCase(String ql){
		for (int i = 0; i < QLKEYWORDS.length; i++) {
			String keyword = QLKEYWORDS[i];
			ql = ql.replaceAll(toWordRegex(keyword), keyword); // 整词、忽略大小写替换。
		}
		return ql;
	}
	
	/**
	 * 查找单词出现的位置，与String.indexOf差异在于，此方法是查找整个单词
	 * @author slx
	 * @date 2011-12-22
	 * @param str
	 * @param word
	 * @return
	 */
	public static int wordIndexOf(String str , String word){
		Matcher mt = Pattern.compile(toWordRegex(word)).matcher(str);
		if(mt.find())
			return mt.toMatchResult().start();
		else
			return -1;
	}
	
	/**
	 * 查找一个整词在字符串中最后出现的位置
	 * @author slx
	 * @date 2011-12-22
	 * @param str
	 * @param word
	 * @return
	 */
	public static int wordLastIndexOf(String str , String word){
		Matcher mt = Pattern.compile(toWordRegex(word)).matcher(str);
		int idx = -1;
		while(mt.find()){
			idx = mt.toMatchResult().start();
		}
		return idx;
	}
	
	private static String toWordRegex(String word){
		word = word.replaceAll(" ", " +"); // 忽略单词间空格的次数。
		return "\\b(?i)".concat(word).concat("\\b");
	}
	
	
	
	public static void main(String[] args) {
		String teststr = "SELEcT o From WHERE1 o wHere bb = (SELECT count(*) from bbtable bt WHERE bt.id=111 Group BY bb.id ORDER  by bb.ORDER) AND AA=by1 AND aaa=enum.Order ORDER  by bb.ORDER";
//		String teststr = "SELEcT o From WHERE1 o wHere bb = (SELECT count(*) from bbtable bt WHERE bt.id=111 Group BY bb.id ORDER  by bb.ORDER) AND AA=by1 AND aaa=enum.Order Group BY bb.id ORDER  by bb.ORDER";
		System.out.println(teststr);
		System.out.println(appendWhere(teststr,"o.id=100 "));
	}

}
