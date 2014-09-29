package oecp.platform.app.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import oecp.framework.entity.base.StringPKEO;
import oecp.platform.base.service.PlatformBaseServiceImpl;

@Service("refService")
@Transactional
public class RefServiceImpl extends PlatformBaseServiceImpl<StringPKEO> implements RefService{

	@Override
	public List getDatas(String hql, int start, int limit) {
		return getDao().getHibernateSession().createQuery(hql).setFirstResult(
				start).setMaxResults(limit).list();
	}

	@Override
	public long getTotalCount(String hql) {
		List list = getDao().getHibernateSession().createQuery(hql).list();
		Long totalCount = (Long) list.get(0);
		return totalCount.longValue();
	}

}
