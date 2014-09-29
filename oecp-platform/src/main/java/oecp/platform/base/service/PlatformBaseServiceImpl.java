package oecp.platform.base.service;

import javax.annotation.Resource;

import oecp.framework.dao.DAO;
import oecp.framework.entity.base.BaseEO;
import oecp.framework.service.BaseServiceImpl;

/**
 * 平台基础服务基类
 * 
 * @author slx
 * @date 2011 4 7 11:17:51
 * @version 1.0
 * @param <T>
 */
public abstract class PlatformBaseServiceImpl<T extends BaseEO> extends BaseServiceImpl<T> {

	@Resource(name="platformDao")
	private DAO dao;
	
	public void setDao(DAO dao) {
		this.dao = dao;
	}

	@Override
	protected DAO getDao() {
		return dao;
	}
}
