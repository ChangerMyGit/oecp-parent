package oecp.platform.datapermission.service;

import java.util.List;

import oecp.framework.exception.BizException;
import oecp.platform.base.service.PlatformBaseServiceImpl;
import oecp.platform.bcfunction.eo.Function;
import oecp.platform.datapermission.eo.DataDiscretePermission;
import oecp.platform.maindata.eo.MDResource;
import oecp.platform.org.eo.Post;

import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * 
 * @author liujingtao
 * @date 2011-7-1 下午03:07:16
 * @version 1.0
 */
@Service("dataDiscretePermissionService")
public class DataDiscretePermissionServiceImpl extends
		PlatformBaseServiceImpl<DataDiscretePermission> implements
		DataDiscretePermissionService {

	@Override
	public List getAllClassDatas(String hql, int start, int limit) {
		return getDao().getHibernateSession().createQuery(hql).setFirstResult(
				start).setMaxResults(limit).list();
	}

	@Override
	public long getTotalCount(String hql) {
		List list = getDao().getHibernateSession().createQuery(hql).list();
		Long totalCount = (Long) list.get(0);
		return totalCount.longValue();
	}

	@Override
	public List<DataDiscretePermission> getDatasByPostAndMD(String postId,
			String mdResourceId) {
		return getDao().queryByWhere(DataDiscretePermission.class,
				"o.post.id=? AND o.mdType.id=?",
				new Object[] { postId, mdResourceId });
	}

	@Override
	public List<DataDiscretePermission> getDatas(String postId,
			String mdResourceId, String funId) {
		if (funId == null) {
			return getDao().queryByWhere(DataDiscretePermission.class,
					"o.post.id=? AND o.mdType.id=? AND o.function.id is null",
					new Object[] { postId, mdResourceId });
		} else {
			return getDao().queryByWhere(DataDiscretePermission.class,
					"o.post.id=? AND o.mdType.id=? AND o.function.id=?",
					new Object[] { postId, mdResourceId, funId });
		}
	}

	@Override
	@Transactional
	public void saveDatasDisPermission(String postId, String mdResourceId,
			String[] addDatas, String[] delDatas, String funId)
			throws BizException {
		for(String addData : addDatas){
			if(StringUtils.isNotEmpty(addData)){
				saveDataDisPermission(postId,mdResourceId,addData,funId);
			}
		}
		for(String delData : delDatas){
			if(StringUtils.isNotEmpty(delData)){
				deleteDataDisPermission(postId,mdResourceId,delData,funId);
			}
		}
	}
	
	@Override
	@Transactional
	public void saveDataDisPermission(String postId, String mdResourceId,
			String dataId, String funId) throws BizException {

		DataDiscretePermission ddp = null;
		if (funId == null) {
			ddp = getDao().findByWhere(DataDiscretePermission.class,
					"o.post.id=? AND o.mdType.id=? AND o.dataid=? AND o.function.id is null",
					new Object[] { postId, mdResourceId, dataId });
		} else {
			ddp = getDao().findByWhere(DataDiscretePermission.class,
					"o.post.id=? AND o.mdType.id=? AND o.dataid=? AND o.function.id=?",
					new Object[] { postId, mdResourceId, dataId, funId });
		}
		if (ddp == null) {
			DataDiscretePermission dataDis = new DataDiscretePermission();
			dataDis.setDataid(dataId);
			MDResource md = new MDResource();
			md.setId(mdResourceId);
			Post post = new Post();
			post.setId(postId);
			dataDis.setPost(post);
			dataDis.setMdType(md);
			if(funId != null){
				Function function = new Function();
				function.setId(funId);
				dataDis.setFunction(function);
			}
			save(dataDis);
		}
	}

	@Override
	@Transactional
	public void deleteDataDisPermission(String postId, String mdResourceId,
			String dataId, String funId) throws BizException {
		DataDiscretePermission ddp = null;
		if (funId == null) {
			ddp = getDao().findByWhere(DataDiscretePermission.class,
					"o.post.id=? AND o.mdType.id=? AND o.dataid=? AND o.function.id is null",
					new Object[] { postId, mdResourceId, dataId });
		} else {
			ddp = getDao().findByWhere(DataDiscretePermission.class,
					"o.post.id=? AND o.mdType.id=? AND o.dataid=? AND o.function.id=?",
					new Object[] { postId, mdResourceId, dataId, funId });
		}
		if (ddp != null) {
			delete(ddp.getId());
		}
	}

}
