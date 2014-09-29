/*
 * Copyright (c) 2012 OECP All Rights Reserved.                	
 * <a href="http://www.oecp.cn">OECP</a> 
 */

package oecp.platform.ed.service;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import oecp.bcbase.eo.BaseBillEO;
import oecp.bcbase.eo.annotations.BillItems;
import oecp.framework.entity.base.BaseEO;
import oecp.platform.base.service.PlatformBaseServiceImpl;
import oecp.platform.ed.eo.EntityAttribute;
import oecp.platform.ed.eo.EntityDictionary;

import org.springframework.stereotype.Service;

/**
 * 
 * @author wangliang
 * @date 2012-3-19 下午4:47:59
 * @version 1.0
 * 
 */
@Service(value="entityAttributeServiceImpl")
public class EntityDictionaryServiceImpl extends	PlatformBaseServiceImpl<EntityDictionary> implements	EntityDictionaryService {

	@Override
	public List<EntityDictionary> getEntityByTableName(String tableName) {
		return getDao().queryByWhere(EntityDictionary.class, "o.tablename=?", new Object[]{tableName});
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	@Override
	public Map<String,?> getEntityParams(BaseEO<?> eo) throws ClassNotFoundException, InstantiationException, IllegalAccessException {
		String tableName = eo.getTableName();
		List<EntityDictionary> dicts = this.getEntityByTableName(tableName);//获取数据字典
		List<Map<String,String>> list = new ArrayList<Map<String,String>>();//实体属性返回值
		List details = new ArrayList();//子表属性
		if(dicts != null && dicts.size()>0){//判断数据字典内注册
			EntityDictionary dict = dicts.get(0);
			List<EntityAttribute> attributes = dict.getEntityAttributes();
			boolean masterField = true;
			for(int i=0;i<attributes.size();i++){
				Map<String,String> map = new HashMap<String,String>();
				String attrname = attributes.get(i).getAttrname();//字段名
				String _type =attributes.get(i).getAttrclass(); //字段类型
				Class cls = Class.forName(_type);
				masterField =true;
				if(BaseEO.class.isAssignableFrom(cls)){//判断eo子类
					Method _m = eo.getBeanUtility().getGetter(attrname);//获取get方法
					if(_m.isAnnotationPresent(BillItems.class)){//判断是子表
						addDetailsPrams(cls,attrname, details ,false,eo.getClass());
					}else{//是参选关联，添加.name，用于获取汉字
						map.put("attrname", attrname+".name");//属性名
						masterField = false;
					}
				} else if(Collection.class.isAssignableFrom(cls)){//判断是子表
					Method md =  eo.getBeanUtility().getGetter(attrname);
					ParameterizedType pt = (ParameterizedType)md.getGenericReturnType();
					Class<? extends BaseEO<?>> realType = (Class<? extends BaseEO<?>>)pt.getActualTypeArguments()[0];
					addDetailsPrams(realType,attrname,details,true,eo.getClass());
				} else {
					map.put("attrname", attributes.get(i).getAttrname());//属性名
				}
				if(masterField){
					map.put("dispname",attributes.get(i).getDispname());//显示名（汉字）
					list.add(map);
				}
			}
		}else{//未注册的直接从eo获取字段属性名，忽略显示名（汉字）
			String[] nolazyfields = eo.getBeanUtility().getAttributeNamesNoLazy();
			boolean masterField = true;
			for(int i=0;i<nolazyfields.length;i++){//拼装属性
				Method method = eo.getBeanUtility().getGetter(nolazyfields[i]);
				Class<?> returnType = method.getReturnType();
				Map<String,String> map = new HashMap<String,String>();
				masterField = true;
				if(BaseEO.class.isAssignableFrom(returnType)){
					Method _m = eo.getBeanUtility().getGetter(nolazyfields[i]);//获取get方法
					if(_m.isAnnotationPresent(BillItems.class)){//判断是子表
						masterField=false;
						addDetailsPrams((Class<? extends BaseEO<?>>)returnType,nolazyfields[i],details,false,eo.getClass());
					}else{
						map.put("attrname",nolazyfields[i]+".name");
					}
				}else if(!Collection.class.isAssignableFrom(returnType)){
					map.put("attrname",nolazyfields[i]);
				}
				if(masterField){
					map.put("dispname", "undefined");
					list.add(map);
				}
			}
			List<Map<String,Class<?>>> children = getListType(eo.getClass());
			if(children != null && children.size()>0){
				for(Map map : children){
					Set set = map.entrySet();
					Iterator it = set.iterator();
					while(it.hasNext()){
						Map.Entry<String, Class<?>> _m =(Map.Entry<String, Class<?>>)it.next();
						addDetailsPrams((Class<? extends BaseEO<?>>) _m.getValue(), _m.getKey(), details, true,eo.getClass());
					}
				}
			}
		}
		Map _m = new HashMap();
		if(details != null && details.size()>0){
			_m.put("children_bill", details);
		}
		_m.put("main_bill", list);
		return _m;
	}
	/**
	 * 获取子表
	 * 
	 * @author wangliang
	 * @date 2012-3-23上午9:27:57
	 * @param clazz
	 * @return
	 */
	private List<Map<String, Class<?>>> getListType(Class<?> clazz) {
		Field[] fs = clazz.getDeclaredFields(); // 得到所有的fields
		ArrayList<Map<String, Class<?>>> list = new ArrayList<Map<String, Class<?>>>();
		for (Field f : fs) {
			Class<?> fieldClazz = f.getType(); // 得到field的class及类型全路径
			if (fieldClazz.isPrimitive())
				continue; // 判断是否为基本类型
			if (fieldClazz.getName().startsWith("java.lang"))
				continue; // getName()返回field的类型全路径；
			if (fieldClazz.isAssignableFrom(List.class)) //
			{
				Type fc = f.getGenericType(); // 关键的地方，如果是List类型，得到其Generic的类型
				if (fc == null)
					continue;
				if (fc instanceof ParameterizedType) // 如果是泛型参数的类型
				{
					ParameterizedType pt = (ParameterizedType) fc;
					Class<?> genericClazz = (Class<?>) pt
							.getActualTypeArguments()[0]; // 得到泛型里的class类型对象
					if (BaseEO.class.isAssignableFrom(genericClazz)) {// 判断是实体
						Map<String, Class<?>> m = new HashMap<String, Class<?>>();
						m.put(f.getName(), genericClazz);
						list.add(m);
					}
				}
			}
		}
		return list;
	}
	
	/**
	 * 拼子表信息 格式如 ：
	 * <code>[ { 'isList' : true , 'objname' : 'xxx' , 'params' : [ { 'dispname' : '主键' , 'attrname' : 'id' } ... ... {'dispname' : '名称' , 'attrname' : 'name' } ] }, 
	 * 									  { 'isList' : fase , 'objname' : 'sss' , 'params' : ... ... } ]
	 * <code>
	 * 
	 * @author wangliang
	 * @date 2012-3-22下午4:54:36
	 * @param cls
	 *            子表class
	 * @param name
	 *            子表在主表中的属性名
	 * @param details
	 *            返回值list
	 * @param isList
	 *            子表是否是List
	 * @throws IllegalAccessException
	 * @throws InstantiationException
	 * @throws ClassNotFoundException
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	private void addDetailsPrams(Class<? extends BaseEO<?>> cls, String name,
			List details, boolean isList, Class masterCls)
			throws InstantiationException, IllegalAccessException,
			ClassNotFoundException {
		Map map = new HashMap();
		map.put("islist", isList);
		map.put("objname", name);
		List list = new ArrayList();
		BaseEO eo = cls.newInstance();
		String tableName = eo.getTableName();
		List<EntityDictionary> dicts = this.getEntityByTableName(tableName);// 获取数据字典
		if (dicts != null && dicts.size() > 0) {
			EntityDictionary dict = dicts.get(0);
			List<EntityAttribute> attributes = dict.getEntityAttributes();
			for (int i = 0; i < attributes.size(); i++) {
				String clsname = attributes.get(i).getAttrclass();
				Class<?> c = Class.forName(clsname);
				Map<String, String> _map = new HashMap<String, String>();
				if (BaseBillEO.class.isAssignableFrom(c) && !masterCls.isAssignableFrom(c)) {// 判断是参选
					_map.put("attrname", attributes.get(i).getAttrname()
							+ ".name");
				} else {
					_map.put("attrname", attributes.get(i).getAttrname());
				}
				_map.put("dispname", attributes.get(i).getDispname());
				list.add(_map);
			}
		} else {
			String[] attributes = eo.getAttributeNames();
			for (int i = 0; i < attributes.length; i++) {
				Method method = eo.getBeanUtility().getGetter(attributes[i]);
				Class<?> attributeCls = method.getReturnType();
				if (BaseEO.class.isAssignableFrom(attributeCls) && !masterCls.isAssignableFrom(attributeCls)) {
					Map<String, String> _map = new HashMap<String, String>();
					_map.put("attrname", attributes[i] + ".name");
					_map.put("dispname", "undefined");
					list.add(_map);
				} else {
					Map<String, String> _map = new HashMap<String, String>();
					_map.put("attrname", attributes[i]);
					_map.put("dispname", "undefined");
					list.add(_map);
				}
			}
		}
		map.put("params", list);
		details.add(map);
	}
}
