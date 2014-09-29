package oecp.framework.service;

import java.io.Serializable;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import oecp.framework.dao.DAO;
import oecp.framework.dao.QueryObject;
import oecp.framework.entity.base.BaseEO;
import oecp.framework.entity.base.ManualPKEO;
import oecp.framework.exception.BizException;
import oecp.framework.util.GenericsUtils;

/**
 * 服务类父类
 * 
 * @author slx
 * @date 2011 4 7 11:05:58
 * @version 1.0
 * @param <T>
 */
@Transactional(rollbackFor=Exception.class,propagation=Propagation.REQUIRED)
public abstract class BaseServiceImpl<T extends BaseEO> implements BaseService<T> {
	private Class<T> cla;

	@SuppressWarnings("unchecked")
	public BaseServiceImpl() {
		this.cla = GenericsUtils.getSuperClassGenricType(getClass());
	}

	protected abstract DAO getDao();

	@Override
	public void create(T t) throws BizException {
		this.getDao().create(t);
	}
	
	public void update(T t) throws BizException {
		this.getDao().update(t);
	}

	@Override
	public void delete(Serializable[] ids) throws BizException {
		for (Serializable id : ids) {
			delete(id);
		}
	}

	@Override
	public void delete(Serializable id) throws BizException {
		this.getDao().delete(cla, id);
	}

	@Override
	public T find(Serializable id) throws BizException {
		return this.getDao().find(cla, id);
	}

	@Override
	public T find_full(Serializable id) throws BizException {
		T t = this.getDao().find(cla, id);
		t.loadLazyAttributes();
		return t;
	}

	public void save(T entity) throws BizException {
		// 如果是自输入主键类型的EO,检查是否存在然后决定是更新还是创建.
		if(entity instanceof ManualPKEO){
			boolean existed = getDao().isExistedByWhere(entity.getClass(), "o.id=? ", new Object[]{entity.getId()});
			if(existed){
				update(entity);
			}else{
				create(entity);
			}
		}else{// 其他非自输入主键的EO,通过主键是否为空来判断是否是新对象,进行更新或者创建操作.
			if (entity.getId() == null || StringUtils.isEmpty(entity.getId().toString())) {
				entity.setId(null); // 创建新对象前确保主键为空.
				create(entity);
			} else {
				boolean existed = this.getDao().isExistedByWhere(entity.getClass(), " id=? ", new Object[] { entity.getId() });
				if (existed) {
					update(entity);
				} else {
					entity.setExistId(entity.getId());
					entity.setId(null);
					create(entity);
				}
			}
		}
	}

	@Override
	public List<T> query(QueryObject queryObj) {
		if(queryObj == null){
			return getDao().queryByWhere(cla, null,null);
		}
		return getDao().queryByWhere(cla, queryObj.getWhereQL(), queryObj.getQueryParams());
	}
}
