package oecp.platform.datapermission.service;

import java.util.List;

import oecp.framework.exception.BizException;
import oecp.platform.base.service.PlatformBaseServiceImpl;
import oecp.platform.datapermission.eo.DataPermission;

import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * 数据权限服务实现
 * 
 * @author liujingtao
 * @date 2011-6-27 下午08:01:18
 * @version 1.0
 */
@Service("dataPermissionService")
public class DataPermissionServiceImpl extends
		PlatformBaseServiceImpl<DataPermission> implements
		DataPermissionService {

	@Override
	public List<DataPermission> getDataPermissions(String postId,
			String mdResourceId) {
		return getDao().queryByWhere(DataPermission.class,
				"o.post.id=? AND o.mdField.md.id=? AND o.function.id is null",
				new Object[] { postId, mdResourceId });
	}

	@Override
	public List<DataPermission> getDataPermissions(String postId,
			String mdResourceId, String funId) {
		if (funId == null) {
			return getDataPermissions(postId, mdResourceId);
		} else {
			return getDao().queryByWhere(DataPermission.class,
					"o.post.id=? AND o.mdField.md.id=? AND o.function.id=?",
					new Object[] { postId, mdResourceId, funId });
		}
	}

	@Override
	@Transactional
	public void saveDataPermissions(List<DataPermission> list, String postId,
			String mdResourceId) throws BizException {
		List<DataPermission> listTmp = getDataPermissions(postId, mdResourceId);
		for (DataPermission dataPermission : listTmp) {
			getDao().getHibernateSession().delete(dataPermission);
		}
		if (list != null) {
			for (DataPermission dataPermission : list) {
				if (!StringUtils.isEmpty(dataPermission.getValue())
						&& !StringUtils.isEmpty(dataPermission.getMdField()
								.getId())) {
					save(dataPermission);
				}
			}
		}
	}

	@Override
	@Transactional
	public void saveDataPermissions(List<DataPermission> list, String postId,
			String mdResourceId, String funId) throws BizException {
		List<DataPermission> listTmp = getDataPermissions(postId, mdResourceId,
				funId);
		for (DataPermission dataPermission : listTmp) {
			getDao().getHibernateSession().delete(dataPermission);
		}
		if(list == null){
			return;
		}
		for (DataPermission dataPermission : list) {
			if (!StringUtils.isEmpty(dataPermission.getValue())
					&& !StringUtils
							.isEmpty(dataPermission.getMdField().getId())) {
				save(dataPermission);
			}
		}
	}
}
