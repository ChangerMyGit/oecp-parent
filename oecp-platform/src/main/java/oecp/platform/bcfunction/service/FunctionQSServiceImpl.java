/**
 * oecp-platform - FunctionQSServiceImpl.java
 * copyright 2009-2012 OECP www.oecp.cn
 * 创建人:songlixiao	创建时间:上午10:21:45		版本:v1
 * ============================================
 * 修改人：			修改时间:					版本:
 * 修改原因:
 *
 * ============================================
 */

package oecp.platform.bcfunction.service;

import java.util.List;

import javax.annotation.Resource;

import oecp.framework.exception.BizException;
import oecp.platform.base.service.PlatformBaseServiceImpl;
import oecp.platform.bcfunction.eo.Function;
import oecp.platform.bcfunction.eo.FunctionQueryScheme;
import oecp.platform.query.service.QuerySchemeService;
import oecp.platform.query.setting.eo.QueryScheme;

import org.apache.commons.lang.StringUtils;
import org.hibernate.Query;
import org.springframework.stereotype.Service;

/** 
 * 业务功能与查询方案关系服务实现类
 * @author songlixiao  
 * @date 2014年1月28日 上午10:21:45 
 * @version 1.0
 *  
 */
@Service("functionQSService")
public class FunctionQSServiceImpl extends PlatformBaseServiceImpl<FunctionQueryScheme> implements FunctionQSService {
	
	@Resource(name = "querySchemeService")
	private QuerySchemeService qsService;
	
	@Override
	public List<QueryScheme> getQuerySchemesByFuncId(String funcId) {
		Query query = getDao().getHibernateSession().createQuery("SELECT o.queryScheme FROM FunctionQueryScheme o WHERE o.func.id=?").setParameter(0, funcId);
		return query.list();
	}
	
	@Override
	public void saveQueryScheme(String funcId, QueryScheme qs) throws BizException {
		boolean isNew = StringUtils.isBlank(qs.getId());
		
		qsService.save(qs); // 先保存查询方案
		if(isNew){ // 如果是新建的，则插入关系，否则不更新。
			FunctionQueryScheme fqs = new FunctionQueryScheme();
			Function func = getDao().find(Function.class, funcId);
			fqs.setFunc(func);
			fqs.setQueryScheme(qs);
			save(fqs);
		}
	}

	@Override
	public void deleteQueryScheme(String funcId, String qsId) throws BizException {
		qsService.delete(qsId);
		getDao().deleteByWhere(FunctionQueryScheme.class, " o.func.id=? AND o.queryScheme.id=? ", new String[]{funcId,qsId});
	}
}
