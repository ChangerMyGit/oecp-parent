package oecp.framework.dao;
/************************* 版权声明 *********************************
 * 
 * 版权所有：百洋软件
 * Copyright (c) 2009 by Pearl Ocean.
 * 
 ************************* 变更记录 *********************************
 *
 * 创建者：yongtree   创建日期： 2009-4-30
 * 创建记录：创建类结构。
 * 
 * 修改者：宋黎晓       修改日期：2010-1-12
 * 修改记录：修改接口定义中的泛型T ,将其从接口定义挪到每个方法的声明.
 * 
 ************************* 随   笔 *********************************
 *
 * 这里可以写写感想，感慨，疑问什么的。
 * 
 ******************************************************************
 */


import java.io.Serializable;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import oecp.framework.entity.base.BaseEO;
import oecp.framework.vo.base.DataVO;
import oecp.framework.vo.base.SimpleDataVO;

import org.hibernate.Session;

/**
 * 底层数据操作的工具类
 * 
 * @author yongtree
 * @date 2009-4-30 上午08:55:32
 * @version 2.0
 * @since 2.0 泛型T挪到方法声明出,声明对象时不需要指定泛型.
 */
public interface DAO {

	/**
	 * 清除一级缓存的数据
	 */
	public void clear();

	/**
	 * 新增实体
	 * 
	 * @param entity
	 *            实体
	 */
	public <T extends BaseEO> void create(T entity);

	/**
	 * 批量新增实体
	 * 
	 * @param entitys
	 *            实体列表
	 */
	public <T extends BaseEO> void createBatch(List<T> entitys);

	/**
	 * 更新实体
	 * 
	 * @param entity
	 *            实体
	 */
	public <T extends BaseEO> void update(T entity);

	/**
	 * 删除实体
	 * 
	 * @param entityClass
	 *            实体类
	 * @param entityid
	 *            实体id
	 */
	public <T extends BaseEO> void delete(Class<T> entityClass,
			Serializable entityId);

	/**
	 * 删除实体
	 * 
	 * @param entityClass
	 *            实体类
	 * @param entityids
	 *            实体id数组
	 */
	public <T extends BaseEO> void delete(Class<T> entityClass,
			Serializable[] entityids);

	/**
	 * 根据条件删除
	 * 
	 * @author slx
	 * @date 2009-11-24 下午05:52:04
	 * @modifyNote
	 * @param entityClass
	 * @param where
	 * @param delParams
	 */
	public <T extends BaseEO> void deleteByWhere(Class<T> entityClass,
			String where, Object[] delParams);

	/**
	 * 获取实体
	 * 
	 * @param <T>
	 * @param entityClass
	 *            实体类
	 * @param entityId
	 *            实体id
	 * @return
	 */
	public <T extends BaseEO> T find(Class<T> entityClass, Serializable entityId);

	/**
	 * 根据where条件查询单个对象
	 * 
	 * @author slx
	 * @date 2010-7-19 上午10:33:20
	 * @modifyNote
	 * @param <T>
	 * @param entityClass
	 *            类型
	 * @param where
	 *            条件
	 * @param params
	 *            参数
	 * @return
	 */
	public <T extends BaseEO> T findByWhere(Class<T> entityClass, String where,
			Object[] params);

	/**
	 * 获取实体，具有延迟加载的作用（和find相比）
	 * 
	 * @param <T>
	 * @param entityClass
	 *            实体类
	 * @param entityId
	 *            实体id
	 * @return
	 */
	public <T extends BaseEO> T load(Class<T> entityClass, Serializable entityId);

	/**
	 * 根据条件判断实体是否存在
	 * 
	 * @author slx
	 * @date 2009-7-8 上午11:49:13
	 * @modifyNote
	 * @param entityClass
	 *            实体类
	 * @param whereql
	 *            查询条件(可空,可为 field1=? and field2=? 形式,也可为field1='value1' and
	 *            field2='value2'的形式)
	 * @param queryParams
	 *            参数(可空，但是当条件使用了field1=? and field2=? 的形式后参数不能为空)
	 * @return 是否存在
	 */
	public <T extends BaseEO> boolean isExistedByWhere(Class<T> entityClass,
			String whereql, Object[] queryParams);

	/**
	 * 获取记录总数
	 * 
	 * @param entityClass
	 *            实体类
	 * @return
	 */
	public <T extends BaseEO> long getCount(Class<T> entityClass);

	/**
	 * 根据条件和参数获取记录总数
	 * 
	 * @author slx
	 * @date 2009-7-8 上午11:34:41
	 * @modifyNote
	 * @param <T>
	 * @param entityClass
	 *            实体类
	 * @param whereql
	 *            查询条件(可空,可为 field1=? and field2=? 形式,也可为field1='value1' and
	 *            field2='value2'的形式)
	 * @param queryParams
	 *            参数(可空，但是当条件使用了field1=? and field2=? 的形式后参数不能为空)
	 * @return 记录行数
	 */
	public <T extends BaseEO> long getCountByWhere(Class<T> entityClass,
			String whereql, Object[] queryParams);

	/**
	 * 获取分页数据
	 * 
	 * @param <T>
	 * @param entityClass
	 *            实体类
	 * @param firstindex
	 *            开始索引
	 * @param maxresult
	 *            需要获取的记录数
	 * @return
	 */
	public <T extends BaseEO> QueryResult<T> getScrollData(
			Class<T> entityClass, int firstindex, int maxresult,
			String wherejpql, Object[] queryParams,
			LinkedHashMap<String, String> orderby);

	public <T extends BaseEO> QueryResult<T> getScrollData(
			Class<T> entityClass, int firstindex, int maxresult,
			String wherejpql, List<Object> queryParams,
			LinkedHashMap<String, String> orderby);

	public <T extends BaseEO> QueryResult<T> getScrollData(
			Class<T> entityClass, int firstindex, int maxresult,
			String wherejpql, Map<String, Object> queryParams,
			LinkedHashMap<String, String> orderby);
	
	/**
	 * 获取分页数据
	 * 
	 * @author yongtree
	 * @date 2011-5-26 上午09:21:23
	 * @param <T>
	 * @param entityClass
	 * @param firstindex
	 * @param maxresult
	 * @param conditions
	 *            条件列表
	 * @param orderby
	 * @return
	 */
	public <T extends BaseEO> QueryResult<T> getScrollData(
			Class<T> entityClass, int firstindex, int maxresult,
			List<QueryCondition> conditions,
			LinkedHashMap<String, String> orderby);

	/**
	 * 根据查询语句获取翻页结果集
	 * @param queryhql
	 * 		HQL查询语句
	 * @param conditions
	 * 		查询条件数组
	 * @param firstindex
	 * 		从第几行开始
	 * @param maxresult
	 * 		取多少行数据
	 * @return
	 * 		带翻页信息的结果集
	 * @author slx
	 * @date 2011-12-21
	 */
	public <T extends BaseEO> QueryResult<T> getScrollData(
			String queryhql, List<QueryCondition> conditions,int firstindex, int maxresult);
	
	public <T extends BaseEO> QueryResult<T> getScrollData(
			String queryhql, List<QueryCondition> conditions,int firstindex, int maxresult,LinkedHashMap<String, String> orderby);
	
	/**
	 * 查询实体部分字段，获取分页数据
	 * 
	 * 返回的结果将重新组装到实体属性中，没有查询的字段为NULL<br>
	 * 注意：使用该接口时，要确保实体类中有对应的查询字段的有参数构造方法，并且参数的顺序要和此处的queryfields数组的元素一致
	 * 
	 * @author yongtree
	 * @date 2010-4-13 下午12:56:03
	 * @modifyNote
	 * @param <T>
	 * @param entityClass
	 * @param queryfields
	 * @param firstindex
	 * @param maxresult
	 * @param wherejpql
	 * @param queryParams
	 * @param orderby
	 * @return
	 */
	public <T extends BaseEO> QueryResult<T> getScrollData(
			Class<T> entityClass, String[] queryfields, int firstindex,
			int maxresult, String wherejpql, Object[] queryParams,
			LinkedHashMap<String, String> orderby);

	public <T extends BaseEO> QueryResult<T> getScrollData(
			Class<T> entityClass, String[] queryfields, int firstindex,
			int maxresult, String wherejpql, List<Object> queryParams,
			LinkedHashMap<String, String> orderby);

	public <T extends BaseEO> QueryResult<T> getScrollData(
			Class<T> entityClass, String[] queryfields, int firstindex,
			int maxresult, String wherejpql, Map<String, Object> queryParams,
			LinkedHashMap<String, String> orderby);

	/**
	 * 查询实体部分字段，获取分页数据
	 * 
	 * @author yongtree
	 * @date 2011-5-26 上午09:22:36
	 * @param <T>
	 * @param entityClass
	 * @param queryfields
	 * @param firstindex
	 * @param conditions
	 *            条件列表
	 * @param orderby
	 * @return
	 */
	public <T extends BaseEO> QueryResult<T> getScrollData(
			Class<T> entityClass, String[] queryfields, int firstindex,int maxresult, 
			List<QueryCondition> conditions,
			LinkedHashMap<String, String> orderby);

	/**
	 * 根据条件查询实体中的指定几个字段 <br>
	 * 返回结果List<String[]>格式如下： <br>
	 * 行1： 字段1value , 字段2value , 字段3value <br>
	 * 行2： 字段1value , 字段2value , 字段3value
	 * 
	 * @author slx
	 * @date 2009-5-14 下午01:14:23
	 * @modifyNote
	 * @param <T>
	 * @param entityClass
	 * @param queryfields
	 * @param wheresql
	 * @param queryParams
	 * @return
	 */
	public <T extends BaseEO> List<Object[]> queryFieldValues(
			Class<T> entityClass, String[] queryfields, String wheresql,
			Object[] queryParams);

	public <T extends BaseEO> List<Object[]> queryFieldValues(
			Class<T> entityClass, String[] queryfields, String wheresql,
			Object[] queryParams, int startRow, int rows);

	/**
	 * 根据条件查询实体中的指定几个字段 <br>
	 * 返回的结果将重新组装到实体属性中，没有查询的字段为NULL<br>
	 * 注意：使用该接口时，要确保实体类中有对应的查询字段的有参数构造方法，并且参数的顺序要和此处的queryfields数组的元素一致
	 * 
	 * @author yongtree
	 * @date 2010-4-13 上午11:45:27
	 * @modifyNote
	 * @param <T>
	 * @param entityClass
	 * @param queryfields
	 * @param wheresql
	 * @param queryParams
	 * @return
	 */
	public <T extends BaseEO> List<T> queryByWhere(Class<T> entityClass,
			String[] queryfields, String wheresql, Object[] queryParams);

	public <T extends BaseEO> List<T> queryByWhere(Class<T> entityClass,
			String[] queryfields, String wheresql, Object[] queryParams,
			int startRow, int rows);

	/**
	 * 根据where条件查询实体bean列表 <br>
	 * where和queryParams可空
	 * 
	 * @author slx
	 * @date 2009-5-14 下午01:20:19
	 * @modifyNote
	 * @param <T>
	 * @param entityClass
	 * @param wheresql
	 * @param queryParams
	 * @return
	 */
	public <T extends BaseEO> List<T> queryByWhere(Class<T> entityClass,
			String wheresql, Object[] queryParams);

	/**
	 * 根据where条件查询实体bean列表 <br>
	 * where和queryParams可空,可排序.
	 * 
	 * @author slx
	 * @date 2011 4 12 09:43:37
	 * @modifyNote
	 * @param <T>
	 * @param entityClass
	 * @param wheresql
	 * @param queryParams
	 * @param orderby
	 *            排序规则<key,value> == <列,升序降序>
	 * @return
	 */
	public <T extends BaseEO> List<T> queryByWhere(Class<T> entityClass,
			String wheresql, Object[] queryParams,
			LinkedHashMap<String, String> orderby);

	/**
	 * 根据where条件查询实体bean列表,可指定取第几行到第几行 <br>
	 * where和queryParams可空
	 * 
	 * @author slx
	 * @date 2009-5-14 下午01:20:19
	 * @modifyNote
	 * @param <T>
	 * @param entityClass
	 * @param wheresql
	 * @param queryParams
	 * @param startRow
	 *            开始行
	 * @param rows
	 *            共多少行
	 * @return
	 */
	public <T extends BaseEO> List<T> queryByWhere(Class<T> entityClass,
			String wheresql, Object[] queryParams, int startRow, int rows);

	/**
	 * 
	 * @author slx
	 * @date 2011 4 12 09:40:43
	 * @modifyNote
	 * @param <T>
	 * @param entityClass
	 * @param wheresql
	 * @param queryParams
	 * @param startRow
	 * @param rows
	 * @param orderby
	 *            排序规则<key,value> == <列,升序降序>
	 * @return
	 */
	public <T extends BaseEO> List<T> queryByWhere(Class<T> entityClass,
			String wheresql, Object[] queryParams, int startRow, int rows,
			LinkedHashMap<String, String> orderby);

	/**
	 * 根据查询语句查询vo列表，可以是HQL和SQL
	 * </br> 注意：
	 * </br><b>使用HQL查询时，请务必给列起别名，否则查询结果将默认列名为1,2,3,4.....</b>
	 * </br><b>另外，当数据库类型为ORACLE时，SQL查询得到的列名将自动标示为“大写”！使用时请注意！！</b>
	 * 
	 * @author slx
	 * @date 2011-7-19下午05:32:56
	 * @param QL
	 * @param qlType
	 * @param prams
	 * @return
	 */
	public List<SimpleDataVO> queryVOs(String QL, QLType qlType, Object[] prams);
	
	/**
	 * 根据查询语句查询vo列表，可以是HQL和SQL
	 * </br> 注意：
	 * </br><b>使用HQL查询时，请务必给列起别名，否则查询结果将默认列名为1,2,3,4.....</b>
	 * </br><b>另外，当数据库类型为ORACLE时，SQL查询得到的列名将自动标示为“大写”！使用时请注意！！</b>
	 * @author slx
	 * @date 2011-7-19下午05:44:30
	 * @param ql
	 * @param qlType
	 * @param prams
	 * @param startRow
	 * @param rows
	 * @return
	 */
	public List<SimpleDataVO> queryVOs(String ql, QLType qlType, Object[] prams, int startRow, int rows);
	
	/**
	 * 根据查询语句查询vo列表，可以是HQL和SQL
	 * </br> 注意：
	 * </br><b>使用HQL查询时，请务必给列起别名，否则查询结果将默认列名为1,2,3,4.....</b>
	 * </br><b>另外，当数据库类型为ORACLE时，SQL查询得到的列名将自动标示为“大写”！使用时请注意！！</b>
	 * @author slx
	 * @date 2011-7-19下午05:44:45
	 * @param voClass
	 * @param ql
	 * @param qlType
	 * @param prams
	 * @param startRow
	 * @param rows
	 * @return
	 */
	public <T extends DataVO> List<T> queryVOs(Class<T> voClass, String ql, QLType qlType, Object[] prams, int startRow, int rows);
	
	/**
	 * 查询带翻页的VO数据
	 * @author slx
	 * @date 2012-5-24下午1:52:44
	 * @param voClass
	 * @param ql
	 * @param qlType
	 * @param prams
	 * @param startRow
	 * @param rows
	 * @return
	 */
	public <T extends DataVO> QueryResult<T> queryScrollVOs(Class<T> voClass, String ql, QLType qlType, Object[] prams, int startRow, int rows);
	
	/**
	 * 得到HibernateSession，不建议经常使用，以免使用过于随意造成系统的维护难度加大和扩展性变差
	 * 
	 * @author yongtree
	 * @date 2009-11-5 上午11:32:49
	 * @modifyNote
	 * @return
	 */
	public abstract Session getHibernateSession();
	
}
