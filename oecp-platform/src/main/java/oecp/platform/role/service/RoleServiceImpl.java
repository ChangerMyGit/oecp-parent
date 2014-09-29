package oecp.platform.role.service;

import java.io.Serializable;
import java.util.LinkedHashMap;
import java.util.List;

import javax.annotation.Resource;

import oecp.framework.dao.QueryResult;
import oecp.framework.exception.BizException;
import oecp.platform.base.service.PlatformBaseServiceImpl;
import oecp.platform.org.service.OrgService;
import oecp.platform.role.eo.Role;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service("roleService")
public class RoleServiceImpl extends PlatformBaseServiceImpl<Role> implements RoleService {

	@Autowired
	private OrgService			orgService;

	@Resource
	private OrgRoleService		orgRoleService;

	@Override
	@Transactional
	public List<Role> getRolesByOrgId(String orgId) {
		return getDao().queryByWhere(Role.class, "o.org.id=?", new Object[] { orgId });
	}

	public QueryResult<Role> getAllOrgRole(String currentOrgPk, String org_pk, int start, int limit) {
		QueryResult<Role> rs = null;
		rs = getDao().getScrollData(Role.class, start, limit, "EXISTS(SELECT o2 FROM o.orgRoles o2 where o2.org.id=?) OR o.org.id=?",
				new Object[] { org_pk, org_pk }, new LinkedHashMap<String, String>() {
					private static final long	serialVersionUID	= 1L;
					{
						put("id", "desc");
					}
				});
		return rs;
	}

	/**
	 * 删除角色
	 */
	@Override
	public void delete(Serializable id) throws BizException {
		// 先删除公司角色信息
		orgRoleService.deleteByRoleId((String)id);
		// 删除角色
		super.delete(id);
	}
}
