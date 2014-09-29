/************************* 版权声明 *********************************
 * 
 * 版权所有：百洋软件
 * Copyright (c) 2009 by Pearl Ocean.
 * 
 ************************* 变更记录 *********************************
 *
 * 创建者：宋黎晓   创建日期： 2011年4月6日
 * 创建记录：创建此基于hibernate实现的Dao父类,封装常见的增删改查操作。
 * 		此类和此类基于的DAO接口基本与OECP社区使用的相同,只是换掉JPA实现为Hibernate实现.
 * 
 ************************* 随   笔 *********************************
 *
 * 此类在使用时,必须在此类或者此类的调用者上标注@Transactional注解,
 * 否则Spring将不会为dao创建HibernateSession.
 * 
 ******************************************************************
 */

package oecp.framework.dao;

import java.io.Serializable;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import oecp.framework.entity.base.BaseEO;
import oecp.framework.vo.base.DataVO;
import oecp.framework.vo.base.SimpleDataVO;

import org.apache.commons.lang.StringUtils;
import org.hibernate.Query;
import org.hibernate.Session;
import org.springframework.transaction.annotation.Transactional;

/**
 * 封装常用增删改查操作
 * 
 * @author slx
 * @date 2011年4月6日9:45:57
 * @version 1.0
 * 
 */
@SuppressWarnings("unchecked")
@Transactional
public abstract class BaseHibernateDao implements DAO {

	private QLBuilder sqlBuilder = new QLBuilder();

	public void clear() {
		getHibernateSession().clear();
	}

	public <T extends BaseEO> void create(T entity) {
		getHibernateSession().persist(entity);
	}

	public <T extends BaseEO> void createBatch(List<T> entitys) {
		for (T entity : entitys) {
			create(entity);
		}
	}

	public <T extends BaseEO> void update(T entity) {
		getHibernateSession().merge(entity);
	}

	public <T extends BaseEO> void delete(Class<T> entityClass,
			Serializable entityid) {
		getHibernateSession().delete(
				getHibernateSession().get(entityClass, entityid));
	}

	public <T extends BaseEO> void delete(Class<T> entityClass,
			Serializable[] entityids) {
		// StringBuffer sf_QL = new StringBuffer(" DELETE FROM ").append(
		// sqlBuilder.getEntityName(entityClass)).append(" o WHERE ")
		// .append(sqlBuilder.getPkField(entityClass, "o")).append("=? ");
		// Query query = getEntityManager().createQuery(sf_QL.toString());
		for (Serializable id : entityids) {
			delete(entityClass, id);
			// query.setParameter(1, id).executeUpdate();
		}
	}

	public <T extends BaseEO> void deleteByWhere(Class<T> entityClass,
			String where, Object[] delParams) {
		StringBuffer sf_QL = new StringBuffer("DELETE FROM ").append(
				sqlBuilder.getEntityName(entityClass)).append(" o WHERE 1=1 ");
		if (where != null && where.length() != 0) {
			sf_QL.append(" AND ").append(where);
		}
		Query query = getHibernateSession().createQuery(sf_QL.toString());
		this.setQueryParams(query, delParams);

		query.executeUpdate();
	}

	public <T extends BaseEO> T find(Class<T> entityClass, Serializable entityId) {
		return (T) getHibernateSession().get(entityClass, entityId);
	}

	public <T extends BaseEO> long getCount(Class<T> entityClass) {
		return getCountByWhere(entityClass, null, null);
	}

	public <T extends BaseEO> long getCountByWhere(Class<T> entityClass,
			String whereql, Object[] queryParams) {
		StringBuffer sf_QL = new StringBuffer("SELECT COUNT(").append(
				sqlBuilder.getPkField(entityClass, "o")).append(") FROM ")
				.append(sqlBuilder.getEntityName(entityClass)).append(
						" o WHERE 1=1 ");
		if (whereql != null && whereql.length() != 0) {
			sf_QL.append(" AND ").append(whereql);
		}
		Query query = getHibernateSession().createQuery(sf_QL.toString());
		this.setQueryParams(query, queryParams);
		return (Long) query.uniqueResult();
	}

	public <T extends BaseEO> boolean isExistedByWhere(Class<T> entityClass,
			String whereql, Object[] queryParams) {
		long count = getCountByWhere(entityClass, whereql, queryParams);
		return count > 0 ? true : false;
	}

	public <T extends BaseEO> QueryResult<T> getScrollData(
			Class<T> entityClass, int firstindex, int maxresult,
			String wherejpql, Object[] queryParams,
			LinkedHashMap<String, String> orderby) {
		return scroll(entityClass, firstindex, maxresult, wherejpql,
				queryParams, orderby);
	}

	public <T extends BaseEO> QueryResult<T> getScrollData(
			Class<T> entityClass, int firstindex, int maxresult,
			String wherejpql, List<Object> queryParams,
			LinkedHashMap<String, String> orderby) {
		Object[] ps = null;
		if (queryParams != null) {
			ps = queryParams.toArray();
		}
		return getScrollData(entityClass, firstindex, maxresult, wherejpql, ps,
				orderby);
	}

	@Override
	public <T extends BaseEO> QueryResult<T> getScrollData(
			Class<T> entityClass, int firstindex, int maxresult,
			String wherejpql, Map<String, Object> queryParams,
			LinkedHashMap<String, String> orderby) {
		return scroll(entityClass, firstindex, maxresult, wherejpql,
				queryParams, orderby);
	}

	@Override
	public <T extends BaseEO> QueryResult<T> getScrollData(
			Class<T> entityClass, int firstindex, int maxresult,
			List<QueryCondition> conditions,
			LinkedHashMap<String, String> orderby) {
		QueryObject qo = new QueryObject();
		qo.setQueryConditions(conditions);
		return getScrollData(entityClass, firstindex, maxresult, qo
				.getWhereQL(), qo.getQueryParams(), orderby);
	}

	@Override
	public <T extends BaseEO> QueryResult<T> getScrollData(
			Class<T> entityClass, String[] queryfields, int firstindex,
			int maxresult, List<QueryCondition> conditions,
			LinkedHashMap<String, String> orderby) {
		QueryObject qo = new QueryObject();
		qo.setQueryConditions(conditions);
		return getScrollData(entityClass, queryfields, firstindex, maxresult,
				qo.getWhereQL(), qo.getQueryParams(), orderby);
	}

	/**
	 * 根据条件查询某个实体的列表
	 * 
	 * @author slx
	 * @param <T>
	 * @param entityClass
	 *            实体类型
	 * @param firstindex
	 *            开始行
	 * @param maxresult
	 *            结束行
	 * @param wherejpql
	 *            where条件
	 * @param queryParams
	 *            参数
	 * @param orderby
	 *            排序条件
	 * @return
	 */
	private <T extends BaseEO> QueryResult<T> scroll(Class<T> entityClass,
			int firstindex, int maxresult, String wherejpql,
			Object queryParams, LinkedHashMap<String, String> orderby) {
		QueryResult<T> qr = new QueryResult<T>();
		String entityname = sqlBuilder.getEntityName(entityClass);
		Query query = getHibernateSession()
				.createQuery(
						"SELECT o FROM "
								+ entityname
								+ " o "
								+ (StringUtils.isEmpty(wherejpql) ? ""
										: "WHERE " + wherejpql)
								+ sqlBuilder.buildOrderby(orderby));
		setQueryParams(query, queryParams);
		if (firstindex != -1 && maxresult != -1)
			query.setFirstResult(firstindex).setMaxResults(maxresult)
					.setCacheable(true);
		qr.setResultlist(query.list());
		query = getHibernateSession().createQuery(
				"SELECT COUNT("
						+ sqlBuilder.getPkField(entityClass, "o")
						+ ") FROM "
						+ entityname
						+ " o "
						+ (StringUtils.isEmpty(wherejpql) ? "" : "WHERE "
								+ wherejpql));
		setQueryParams(query, queryParams);
		qr.setTotalrecord((Long) query.uniqueResult());
		return qr;
	}

	/**
	 * 根据条件查询实体指定字段的值并回填到实体内. <br/>
	 * <b>注意:</b> <br/>
	 * 实体必须有包括要查询的字段为参数的构造函数.
	 * 
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
	private <T extends BaseEO> QueryResult<T> scroll(Class<T> entityClass,
			String[] queryfields, int firstindex, int maxresult,
			String wherejpql, Object queryParams,
			LinkedHashMap<String, String> orderby) {
		QueryResult<T> qr = new QueryResult<T>();
		String entityname = sqlBuilder.getEntityName(entityClass);
		Query query = getHibernateSession()
				.createQuery(
						(sqlBuilder.buildSelect(entityname, queryfields, "o")
								+ "FROM "
								+ entityname
								+ " o "
								+ (StringUtils.isEmpty(wherejpql) ? ""
										: "WHERE " + wherejpql) + sqlBuilder
								.buildOrderby(orderby)));
		setQueryParams(query, queryParams);
		if (firstindex != -1 && maxresult != -1)
			query.setFirstResult(firstindex).setMaxResults(maxresult)
					.setCacheable(true);
		qr.setResultlist(query.list());
		query = getHibernateSession().createQuery(
				"SELECT COUNT("
						+ sqlBuilder.getPkField(entityClass, "o")
						+ ") FROM "
						+ entityname
						+ " o "
						+ (StringUtils.isEmpty(wherejpql) ? "" : "WHERE "
								+ wherejpql));
		setQueryParams(query, queryParams);
		qr.setTotalrecord((Long) query.uniqueResult());
		return qr;
	}

	@Override
	public <T extends BaseEO> QueryResult<T> getScrollData(
			Class<T> entityClass, String[] queryfields, int firstindex,
			int maxresult, String wherejpql, List<Object> queryParams,
			LinkedHashMap<String, String> orderby) {
		return this.scroll(entityClass, queryfields, firstindex, maxresult,
				wherejpql, queryParams, orderby);
	}

	@Override
	public <T extends BaseEO> QueryResult<T> getScrollData(
			Class<T> entityClass, String[] queryfields, int firstindex,
			int maxresult, String wherejpql, Map<String, Object> queryParams,
			LinkedHashMap<String, String> orderby) {
		return this.scroll(entityClass, queryfields, firstindex, maxresult,
				wherejpql, queryParams, orderby);
	}

	@Override
	public <T extends BaseEO> QueryResult<T> getScrollData(String queryhql, List<QueryCondition> conditions,
			int firstindex, int maxresult) {
		QueryResult<T> qr = new QueryResult<T>();
		return this.getScrollData(queryhql, conditions, firstindex, maxresult,null);
	}
	
	public <T extends BaseEO> QueryResult<T> getScrollData(
			String queryhql, List<QueryCondition> conditions,int firstindex, int maxresult,LinkedHashMap<String, String> orderby){
		QueryResult<T> qr = new QueryResult<T>();
		QueryObject qobj = new QueryObject();
		qobj.setQueryConditions(conditions);
		queryhql = sqlBuilder.appendWhere(queryhql,qobj.getWhereQL());
		Object[] queryParams = qobj.getQueryParams();
		String counthql = sqlBuilder.transformQyeryHql2CountHql(queryhql);
		Query query = getHibernateSession().createQuery(queryhql+sqlBuilder.buildOrderby(orderby));
		setQueryParams(query, queryParams);
		if (firstindex != -1 && maxresult != -1)
			query.setFirstResult(firstindex).setMaxResults(maxresult)
					.setCacheable(true);
		qr.setResultlist(query.list());
		query = getHibernateSession().createQuery(counthql);
		setQueryParams(query, queryParams);
		qr.setTotalrecord((Long) query.uniqueResult());
		return qr;
	}
	@Override
	public <T extends BaseEO> QueryResult<T> getScrollData(
			Class<T> entityClass, String[] queryfields, int firstindex,
			int maxresult, String wherejpql, Object[] queryParams,
			LinkedHashMap<String, String> orderby) {
		return this.scroll(entityClass, queryfields, firstindex, maxresult,
				wherejpql, queryParams, orderby);
	}

	protected void setQueryParams(Query query, Object queryParams) {
		sqlBuilder.setQueryParams(query, queryParams);
	}

	@Override
	public <T extends BaseEO> List<T> queryByWhere(Class<T> entityClass,
			String wheresql, Object[] queryParams) {
		return queryByWhere(entityClass, wheresql, queryParams, -1, -1);
	}

	@Override
	public <T extends BaseEO> List<T> queryByWhere(Class<T> entityClass,
			String wheresql, Object[] queryParams,
			LinkedHashMap<String, String> orderby) {
		return queryByWhere(entityClass, wheresql, queryParams, -1, -1, orderby);
	}

	@Override
	public <T extends BaseEO> List<T> queryByWhere(Class<T> entityClass,
			String wheresql, Object[] queryParams, int startRow, int rows) {
		return queryByWhere(entityClass, wheresql, queryParams, startRow, rows,
				null);
	}

	@Override
	public <T extends BaseEO> List<T> queryByWhere(Class<T> entityClass,
			String wheresql, Object[] queryParams, int startRow, int rows,
			LinkedHashMap<String, String> orderby) {
		String entityname = sqlBuilder.getEntityName(entityClass);
		Query query = getHibernateSession().createQuery(
				"SELECT o FROM "
						+ entityname
						+ " o "
						+ ((wheresql == null || wheresql.length() == 0) ? ""
								: "WHERE " + wheresql)
						+ sqlBuilder.buildOrderby(orderby));
		setQueryParams(query, queryParams);
		if (startRow >= 0) {
			query.setFirstResult(startRow);
		}
		if (rows > 0) {
			query.setMaxResults(rows);
		}
		query.setCacheable(true);
		return query.list();
	}

	@Override
	public <T extends BaseEO> List<T> queryByWhere(Class<T> entityClass,
			String[] queryfields, String wheresql, Object[] queryParams) {
		return queryByWhere(entityClass, queryfields, wheresql, queryParams,
				-1, -1);
	}

	@Override
	public <T extends BaseEO> List<T> queryByWhere(Class<T> entityClass,
			String[] queryfields, String wheresql, Object[] queryParams,
			int startRow, int rows) {
		String entityname = sqlBuilder.getEntityName(entityClass);
		Query query = getHibernateSession().createQuery(
				sqlBuilder.buildSelect(entityname, queryfields, "o") + " FROM "
						+ entityname + " o "
						+ (wheresql == null ? "" : "WHERE " + wheresql));
		setQueryParams(query, queryParams);
		if (startRow >= 0) {
			query.setFirstResult(startRow);
		}
		if (rows > 0) {
			query.setMaxResults(rows);
		}
		return query.list();
	}

	public <T extends BaseEO> List<Object[]> queryFieldValues(
			Class<T> entityClass, String[] queryfields, String wheresql,
			Object[] queryParams) {
		return queryFieldValues(entityClass, queryfields, wheresql,
				queryParams, -1, -1);
	}

	@Override
	public <T extends BaseEO> List<Object[]> queryFieldValues(
			Class<T> entityClass, String[] queryfields, String wheresql,
			Object[] queryParams, int startRow, int rows) {
		String entityname = sqlBuilder.getEntityName(entityClass);
		Query query = getHibernateSession().createQuery(
				sqlBuilder.buildSelect(queryfields, "o") + " FROM "
						+ entityname + " o "
						+ (wheresql == null ? "" : "WHERE " + wheresql));
		setQueryParams(query, queryParams);
		if (startRow >= 0) {
			query.setFirstResult(startRow);
		}
		if (rows > 0) {
			query.setMaxResults(rows);
		}
		return query.list();
	}

	/**
	 * 设置查询参数
	 * 
	 * @author slx
	 * @date 2009-7-8 上午10:02:55
	 * @modifyNote
	 * @param query
	 *            查询
	 * @param queryParams
	 *            查询参数
	 */
	protected void setQueryParams(Query query, Object[] queryParams) {
		sqlBuilder.setQueryParams(query, queryParams);
	}

	/**
	 * 返回实体管理器
	 * 
	 * @desc 由slx在2010-6-8下午04:06:55重写父类方法
	 */
	public abstract Session getHibernateSession();

	@Override
	public <T extends BaseEO> T load(Class<T> entityClass, Serializable entityId) {
		try {
			return (T) getHibernateSession().load(entityClass, entityId);
		} catch (Exception e) {
			return null;
		}
	}

	@Override
	public <T extends BaseEO> T findByWhere(Class<T> entityClass, String where,
			Object[] params) {
		List<T> l = queryByWhere(entityClass, where, params);
		if (l != null && l.size() == 1) {
			return l.get(0);
		} else if (l.size() > 1) {
			throw new RuntimeException("查寻到的结果大于一个.");
		} else {
			return null;
		}
	}

	@Override
	public List<SimpleDataVO> queryVOs(String ql, QLType qlType ,Object[] prams) {
		return queryVOs(ql,qlType,prams,0,0);
	}
	
	@Override
	public List<SimpleDataVO> queryVOs(String ql, QLType qlType ,Object[] prams ,int startRow, int rows) {
		return queryVOs(SimpleDataVO.class, ql,qlType,prams,startRow,rows);
	}
	
	@Override
	public <T extends DataVO> List<T> queryVOs(Class<T> voClass, String ql, QLType qlType ,Object[] prams ,int startRow, int rows) {
		Query query = null;
		// 根据查询类型使用不同的Query对象
		if(qlType == QLType.HQL){
			query = this.getHibernateSession().createQuery(ql);
		}else{
			query = this.getHibernateSession().createSQLQuery(ql);
		}
		// 设置结果转换器为转换成DataVO的转换器
		query.setResultTransformer(AliasToDataVOTransformer.newInstance(voClass));
		QLBuilder qlb = new QLBuilder();
		qlb.setQueryParams(query, prams);
		if (startRow >= 0) {
			query.setFirstResult(startRow);
		}
		if (rows > 0) {
			query.setMaxResults(rows);
		}
		
		return query.list();
	}
	
	@Override
	public <T extends DataVO> QueryResult<T> queryScrollVOs(Class<T> voClass, String ql, QLType qlType, Object[] prams, int startRow, int rows) {
		Query query_Count = null;
		// 根据查询类型使用不同的Query对象和查询语句
		if(qlType == QLType.HQL){
			// 对于HQL语句，查询count使用 替换原句的SELECT xxx,yyy,zzz 为 SELECT COUNT(*),然后删除掉最后的order by 子句
			query_Count = this.getHibernateSession().createQuery(QLBuilder.transformQyeryHql2CountHql(ql));
		}else{
			// 对于SQL语句，查询count语句 使用这种模式： SELECT COUNT(*) FORM (原始查询语句)
			query_Count = this.getHibernateSession().createSQLQuery("SELECT COUNT(*) FROM (" +ql +")");
		}
		QLBuilder.setQueryParams(query_Count, prams);
		QueryResult<T> qr = new QueryResult<T>();
		// 执行行数计算
		qr.setTotalrecord(Long.parseLong(query_Count.uniqueResult().toString()));
		// 获得查询结果
		qr.setResultlist(this.queryVOs(voClass, ql, qlType, prams, startRow, rows));
		return qr;
	}
}
