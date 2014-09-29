package oecp.platform.app.service;

import java.util.List;

import oecp.framework.entity.base.StringPKEO;
import oecp.framework.service.BaseService;

public interface RefService extends BaseService<StringPKEO>{
	/**
	 * 分页查询资源集合
	 * @author liujt
	 * @date 2011-10-20上午10:10:17
	 * @param hql
	 * @param start
	 * @param limit
	 * @return
	 */
	public List getDatas(String hql, int start, int limit);
	/**
	 * 资源集合总数
	 * @author liujt
	 * @date 2011-10-20上午10:10:36
	 * @param hql
	 * @return
	 */
	public long getTotalCount(String hql);
}
