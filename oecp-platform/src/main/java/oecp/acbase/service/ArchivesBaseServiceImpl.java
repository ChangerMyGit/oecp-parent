/**
 * oecp-platform - ArchivesBaseServiceImpl.java
 * copyright 2009-2012 OECP www.oecp.cn
 * 创建人:Administrator	创建时间:下午2:38:32		版本:v1
 * ============================================
 * 修改人：			修改时间:					版本:
 * 修改原因:
 *
 * ============================================
 */

package oecp.acbase.service;

import java.util.Collection;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;

import javax.annotation.Resource;

import oecp.acbase.eo.BaseMasArchivesEO;
import oecp.bcbase.utils.BizServiceHelper;
import oecp.framework.dao.QueryCondition;
import oecp.framework.dao.QueryResult;
import oecp.framework.entity.base.BaseEO;
import oecp.framework.exception.BizException;
import oecp.framework.util.GenericsUtils;
import oecp.platform.api.datapermission.DataPermissionSQLService;
import oecp.platform.base.service.PlatformBaseServiceImpl;
import oecp.platform.user.eo.User;

import org.apache.commons.lang.StringUtils;

/**
 * 业务档案服务基类
 * 
 * @author luanyoubo
 * @date 2014年2月25日 下午2:38:32
 * @version 1.0
 * @param <T>
 * 
 */
public abstract class ArchivesBaseServiceImpl<T extends BaseMasArchivesEO> extends PlatformBaseServiceImpl<T> implements
		ArchivesBaseService<T> {
	/** 数据权限类 **/
	@Resource(name = "dataPermissionSQLService")
	protected DataPermissionSQLService	dataPerService;

	public void setDataPerService(DataPermissionSQLService dataPerService) {
		this.dataPerService = dataPerService;
	}

	@Override
	public QueryResult<T> query(String userID, String functionCode, String orgId, List<QueryCondition> conditions, int startRow, int rows)
			throws BizException {
		// 数据权限
		String data_sql = dataPerService.getDataPermissionSQL(userID, orgId, functionCode);
		String query = buildQueryHQL(conditions, data_sql);
		query = query.concat(StringUtils.isEmpty(data_sql) ? "" : " WHERE " + data_sql);
		// 使用DAO的分页查询查询出结果
		return getDao().getScrollData(query, conditions, startRow, rows);
	}

	@Override
	public QueryResult<T> comboQuery(String userID, String functionCode, String orgId, T eo, int startRow, int rows,
			LinkedHashMap<String, String> orderby) throws BizException {
		String data_sql = dataPerService.getDataPermissionSQL(userID, orgId, functionCode);
		List<QueryCondition> conditions = new java.util.ArrayList<QueryCondition>();
		String query = buildQueryHQL(conditions, data_sql);
		String like = buildWhereLike(eo);
		if (like != null) {
			like = "(".concat(like).concat(")");
			data_sql = StringUtils.isEmpty(data_sql) ? like : (data_sql + " AND " + like);
		}
		query = query.concat(StringUtils.isEmpty(data_sql) ? "" : " WHERE " + data_sql);
		// 使用DAO的分页查询查询出结果
		return getDao().getScrollData(query, conditions, startRow, rows, orderby);
	}

	private String buildWhereLike(T eo) {
		if (eo == null) {
			return null;
		}
		String like = null;
		String[] fields = eo.getAttributeNames();
		for (String f : fields) {
			try {
				if (eo.getAttributeType(f) == String.class) {
					if (eo.getAttributeValue(f) != null) {
						String v = eo.getAttributeValue(f).toString();
						if (StringUtils.isNotEmpty(v)) {
							if (like == null) {
								like = " o." + f + " LIKE '%" + v + "%' ";
							} else {
								like = like + " OR o." + f + " LIKE '%" + v + "%' ";
							}
						}
					}
				}
			} catch (NoSuchFieldException e) { // 忽略异常
			}
		}
		return like;
	}

	@Override
	public void saveArchives(T archives, User operator, String functionCode) throws BizException {
		// 清掉主表空id 和子表空id
		if (archives.getId() == null || StringUtils.isEmpty(archives.getId().toString().trim())
				|| "null".equalsIgnoreCase(archives.getId())) {
			archives.setId(null);
			archives.setCreater(operator.getId());
			archives.setCreatedate(new Date());
			archives.setChanger(operator.getId());
			archives.setChangedate(archives.getCreatedate());
		} else {
			archives.setChanger(operator.getId());
			archives.setChangedate(new Date());
		}

		filtId(archives, true);

		beforeArchivesSave(archives);
		validateArchives(archives);
		// 保存档案
		save(archives);
		afterArchivesSave(archives);
	}

	/**
	 * 过滤整理单据内主子表对象之间的关系,剔除对象的“”id,更新单据默认值。
	 * 
	 * @author luanyoubo
	 * @date 2014-2-26
	 * @param bill
	 * @return
	 */
	public BaseEO<?> filtId(BaseEO<?> mdeo, boolean checkExist) {
		String[] items = getItemFieldNames(mdeo);
		String billfield = null;
		for (int i = 0; i < items.length; i++) {
			Object itemvalue = mdeo.getAttributeValue(items[i]);
			if (itemvalue != null) {
				if (itemvalue instanceof BaseEO) {
					BaseEO itemeo = (BaseEO) itemvalue;
					billfield = getMainBillFieldName(itemeo);
					// 设置子表的主表对象关联
					if (billfield != null)
						itemeo.setAttributeValue(billfield, mdeo);
					if (itemeo.getId() == null || StringUtils.isEmpty(itemeo.getId().toString().trim())) {
						itemeo.setId(null);
					} else {// 有id并且数据库中没有的
						boolean exist = false;
						if (checkExist) {
							exist = getDao().isExistedByWhere(itemeo.getClass(), " id=? ", new Object[] { itemeo.getId() });
						}
						itemeo.setExistId(itemeo.getId());
						itemeo.setId(null);
					}
				} else if (itemvalue instanceof List) {
					List<BaseEO<?>> itemslist = (List<BaseEO<?>>) itemvalue;
					for (BaseEO itemeo : itemslist) {
						billfield = getMainBillFieldName(itemeo);
						if (billfield != null)
							itemeo.setAttributeValue(billfield, mdeo);
						if (itemeo.getId() == null || StringUtils.isEmpty(itemeo.getId().toString().trim())) {
							itemeo.setId(null);
						} else {
							boolean exist = false;
							if (checkExist) {
								exist = getDao().isExistedByWhere(itemeo.getClass(), " id=? ", new Object[] { itemeo.getId() });
							}
							itemeo.setExistId(itemeo.getId());
							itemeo.setId(null);
						}
					}
				}
			}
			billfield = null;
		}
		return mdeo;
	}

	/**
	 * 从子表中获得与主表关联的属性
	 * 
	 * @author slx
	 * @date 2011-12-23
	 * @param itemeo
	 * @return
	 */
	protected String getMainBillFieldName(BaseEO<?> itemeo) {
		return BizServiceHelper.getBillFieldInItem(itemeo);
	}

	/**
	 * 获得主表对象中的子表对象声明的属性名称
	 * 
	 * @author slx
	 * @date 2011-12-23
	 * @param bill
	 * @return
	 */
	protected String[] getItemFieldNames(BaseEO<?> archives) {
		return BizServiceHelper.getItemFieldNames(archives);
	}

	@Override
	public void deleteArchives(String[] ids, User operator, String functionCode) throws BizException {
		for (String id : ids) {
			T archives = find(id);
			beforeDelete(archives, operator);
		}
		this.delete(ids);
	}

	protected abstract void afterArchivesSave(T archives) throws BizException;

	protected abstract void validateArchives(T archives) throws BizException;

	protected abstract void beforeArchivesSave(T archives) throws BizException;

	protected abstract void beforeDelete(T archives, User operator) throws BizException;

	/**
	 * 构建并返回查询表单的hql语句
	 * 
	 * @author luanyoubo
	 * @date 2014-2-25
	 * @return
	 * @throws BizException
	 */
	protected String buildQueryHQL(List<QueryCondition> conditions, String data_sql) throws BizException {
		// 从实体中得到引用的list( 只需要List添加到join语句中，其他的用[主表.对象名]就可以查询 )
		Class<T> t = GenericsUtils.getSuperClassGenricType(this.getClass());
		BaseEO<?> eo = null;
		try {
			eo = t.newInstance();
			StringBuffer sf_hql = new StringBuffer("SELECT o FROM ").append(t.getName()).append(" o ");
			String[] attrnames = eo.getAttributeNames();
			for (int i = 0; i < attrnames.length; i++) {
				try {
					Class<?> fc = eo.getAttributeType(attrnames[i]);
					if (Collection.class.isAssignableFrom(fc)) {
						// 检查是否需要拼接这个子表到JOIN中，如果查询条件和数据权限的sql中出现过，则拼接。
						String asname = "o_".concat(attrnames[i]); // 子表别名如：
																	// eo.children
																	// 别名为eo_children
						boolean needtable = false;
						needtable = (data_sql != null && data_sql.indexOf(asname) > -1);
						if (!needtable) {
							if (conditions != null && conditions.size() > 0) {
								for (QueryCondition qcon : conditions) {
									if (qcon.getValue() != null && qcon.getField().indexOf(asname) > -1) {
										needtable = true;
										break;
									}
								}
							}
						}
						if (needtable) {
							// 将其拼接成HQL的JOIN查询语句
							sf_hql.append(" LEFT JOIN o.").append(attrnames[i]).append(asname);
						}
					}
				} catch (NoSuchFieldException e) {
					throw new BizException(e);
				}
			}
			return sf_hql.toString();
		} catch (Exception e) {
			throw new BizException(e);
		}
	}
}
