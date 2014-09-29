package oecp.platform.permission.service;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;

import oecp.framework.util.entity.EOUtility;
import oecp.platform.base.service.PlatformBaseServiceImpl;
import oecp.platform.bcinfo.eo.BizComponent;
import oecp.platform.permission.eo.Permission;
import oecp.platform.permission.eo.PermissionFuncUI;
import oecp.platform.permission.vo.BizComponentVO;
import oecp.platform.role.service.OrgRoleService;

import org.springframework.stereotype.Service;

@Service("permissionService")
public class PermissionServiceImpl extends PlatformBaseServiceImpl<Permission> implements PermissionService {

	@Override
	public boolean checkUserPermission(String userID, String orgID, String sign) {
		boolean hasPermission = getDao()
				.isExistedByWhere(
						PermissionFuncUI.class,
						// 公司角色的用户列表中有此用户，并且公司角色的公司ID相符。这一类公司角色，在分配过UI权限的公司角色中。
						"o.permission.orgRole IN (SELECT r FROM OrgRole r WHERE (SELECT u FROM User u WHERE u.id=?) MEMBER OF r.users AND r.org.id=?) AND o.functionUI.sign=? ",
						new Object[] { userID, orgID, sign });
		return hasPermission;
	}

	@Override
	public List<BizComponentVO> getPermissionBCs(String userID, String orgID) {
		// TODO Auto-generated method stub
		List<Object[]> values = getDao().queryFieldValues(BizComponent.class,
				new String[] { "id", "code", "name", "discription" },
				// 当前组织已经建账的业务组件
				" o.id IN (SELECT uc.bc.id FROM OrgUseBC uc WHERE uc.org.id=?) "
						+
						// 当前用户所具有的角色可用的业务组件
						"AND o.id IN (SELECT p.function.bc.id FROM Permission p WHERE p.orgRole IN "
						+ "(SELECT r FROM OrgRole r WHERE (SELECT u FROM User u WHERE u.id=?) MEMBER OF r.users AND r.org.id=?) )"
						+ " ORDER BY o.displayOrder ASC", new Object[] { orgID, userID, orgID });
		List<BizComponentVO> vos = new ArrayList<BizComponentVO>();
		for (Object[] objects : values) {
			BizComponentVO vo = new BizComponentVO();
			vo.setId(objects[0].toString());
			vo.setCode((String) objects[1]);
			vo.setName((String) objects[2]);
			vo.setDiscription((String) objects[3]);
			vos.add(vo);
		}
		return vos;
	}

	@Override
	public List<Permission> getUserBCPermissions(String bcCode, String userID, String orgID) {
		List<Permission> prms = null;
		StringBuffer sf_hql = new StringBuffer();
		sf_hql.append(" SELECT p FROM Permission p ").append("JOIN p.orgRole r ").append("JOIN r.users u ")
				.append("WHERE p.function.bc.code = ? AND u.id = ? AND r.org.id = ? ");
		prms = getDao().getHibernateSession().createQuery(sf_hql.toString()).setParameter(0, bcCode).setParameter(1, userID)
				.setParameter(2, orgID).list();
		EOUtility.loadLazyField(prms, "permissionFuncUIs.permissionUIElements");
		return prms;
	}

	@Override
	public List<Permission> getUserPermission(String userID, String orgID) {
		List<Permission> prms = null;
		StringBuffer sf_hql = new StringBuffer();
		sf_hql.append(" SELECT p FROM Permission p ").append("JOIN p.orgRole r ").append("JOIN r.users u ")
				.append("WHERE u.id = ? AND r.org.id = ? ")
				// 组织已经启用的bc
				.append("AND p.function.bc.id IN (SELECT uc.bc.id FROM OrgUseBC uc WHERE uc.org.id = ?)");
		prms = getDao().getHibernateSession().createQuery(sf_hql.toString()).setParameter(0, userID).setParameter(1, orgID)
				.setParameter(2, orgID).list();
		EOUtility.loadLazyField(prms, "permissionFuncUIs.permissionUIElements");
		return prms;
	}

	@Override
	public List<Permission> getRolePermission(String orgRoleId) {
		return getDao().queryByWhere(Permission.class, "o.orgRole.id=?", new Object[] { orgRoleId });
	}

	@Override
	public List<Permission> updateRolePermission(String orgRoleId, List<Permission> permissionList) {
		List<Permission> list = getRolePermission(orgRoleId);
		for (Permission permission : list) {
			getDao().getHibernateSession().delete(permission);
		}
		if (permissionList != null)
			getDao().createBatch(permissionList);
		return null;
	}

}
