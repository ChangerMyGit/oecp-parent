/************************* 版权声明 *********************************
 *                                                                  *
 *                     版权所有：百洋软件                          				 	*
 *          Copyright (c) 2011 by www.oecp.cn                	*
 *                                                                  *
 ************************* 变更记录 *********************************
 *
 * 创建者：宋黎晓   创建日期： 2011 5 5
 * 备注：
 * 
 * 修改者：       修改日期：
 * 备注：
 * 
 */

package oecp.platform.role.service;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;

import javax.annotation.Resource;

import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Service;

import oecp.framework.dao.QueryCondition;
import oecp.framework.dao.QueryObject;
import oecp.framework.dao.QueryResult;
import oecp.framework.exception.BizException;
import oecp.platform.user.eo.User;
import oecp.platform.user.service.UserService;
import oecp.platform.base.service.PlatformBaseServiceImpl;
import oecp.platform.permission.eo.Permission;
import oecp.platform.permission.service.PermissionService;
import oecp.platform.role.eo.OrgRole;
import oecp.platform.role.eo.Role;

/**
 * 公司角色服务实现
 * 
 * @author slx
 * @date 2011 5 5 17:15:42
 * @version 1.0
 */
@Service("orgRoleService")
public class OrgRoleServiceImpl extends PlatformBaseServiceImpl<OrgRole> implements OrgRoleService {
	// 用户服务
	@Resource
	private UserService	userService;
	
	@Resource
	private PermissionService	permissionService;

	@Override
	public List<OrgRole> getUserOrgRole(String userId) {
		return getDao().queryByWhere(OrgRole.class, "EXISTS (SELECT u FROM o.users u WHERE u.id=?)", new Object[] { userId });
	}

	@Override
	public List<OrgRole> getUserOrgRole(String userId, String orgId) {
		User user = getDao().find(User.class, userId);
		if (user == null)
			return null;

		return getDao().queryByWhere(OrgRole.class, " ? MEMBER OF o.users AND o.org.id=? ", new Object[] { user, orgId });
	}

	@Override
	public List<OrgRole> getUserOrgRoleByRoleId(String roleId) {
		return getDao().queryByWhere(OrgRole.class, "o.role.id=?", new Object[] { roleId });
	}

	@Override
	public List<OrgRole> getUserOrgRoleByRoleIdOrgId(String roleId, String[] orgIds) {
		// 拼装orgIdWhere语句
		String whereSql = "";
		for (int i = 0; i < orgIds.length; i++) {
			whereSql += "?,";
		}
		whereSql = whereSql.substring(0, whereSql.length() - 1);
		whereSql = "o.role.id=? AND o.org.id in (" + whereSql + ")";
		// 拼装参数
		Object[] whereObj = new Object[orgIds.length + 1];
		whereObj[0] = roleId;
		for (int i = 0; i < orgIds.length; i++) {
			whereObj[i + 1] = orgIds[i];
		}
		return getDao().queryByWhere(OrgRole.class, whereSql, whereObj);
	}

	@Override
	public List<OrgRole> getAllOrgRole(String org_pk) {
		return getDao().queryByWhere(OrgRole.class, "o.org.id=?", new Object[] { org_pk });
	}

	@Override
	public boolean isAllocated(String org_pk, Role role) {
		return getDao().isExistedByWhere(OrgRole.class, "o.role.id=? and o.org.id=?", new String[] { role.getId(), org_pk });
	}

	@Override
	public List<User> getHasRoleUsers(String orgId, String orgRoleId) {
		List<User> orgRoles = getDao()
				.queryByWhere(User.class, "o.createdByOrg.id=? AND (SELECT rr FROM OrgRole rr WHERE rr.id=?) MEMBER OF o.orgRoles",
						new Object[] { orgId, orgRoleId });

		return orgRoles;
	}

	@Override
	public List<User> getNotHasRoleUsers(String orgId, String orgRoleId) {
		return getDao().queryByWhere(User.class,
				"o.createdByOrg.id=? AND (SELECT rr FROM OrgRole rr WHERE rr.id=?) NOT MEMBER OF o.orgRoles ",
				new Object[] { orgId, orgRoleId });
	}

	@Override
	public List<OrgRole> getOrgRolesByRoleIds(String[] orgRoleIds) throws BizException {
		// 拼装in条件
		String where = "";
		for (int i = 0; i < orgRoleIds.length; i++) {
			where += "?,";
		}
		where = "o.id IN (" + where.substring(0, where.length() - 1) + ")";
		// 查询组织角色
		return getDao().queryByWhere(OrgRole.class, where, orgRoleIds);
	}

	@Override
	public void saveOrgRoleByUserIds(String currentOrgId, String orgRoleId, String[] userIds) throws BizException {
		// 查询出用户实体
		List<User> users = userService.getUserListByIds(userIds);
		OrgRole orgRole = this.find(orgRoleId);
		List<User> orgRoleUsers = orgRole.getUsers();
		if (orgRoleUsers != null && orgRoleUsers.size() > 0) {
			// 获取非当前公司用户
			List<User> notMyOrgUsers = new ArrayList<User>();
			for (User u : orgRoleUsers) {
				if (!u.getCreatedByOrg().getId().equals(currentOrgId)) {
					notMyOrgUsers.add(u);
				}
			}
			// 追加非本公司用户
			users.addAll(notMyOrgUsers);
		}
		orgRole.setUsers(users);
		getDao().update(orgRole);
	}

	@Override
	public void deleteByRoleId(String roleId) throws BizException {
		List<OrgRole> orgRoles = getUserOrgRoleByRoleId(roleId);
		if(orgRoles == null){
			return ;
		}
		for (OrgRole orgRole : orgRoles) {
			// 如果公司角色下有用户存在，抛出异常不允许删除。
			if (orgRole.getUsers() != null && orgRole.getUsers().size() > 0) {
				throw new BizException("角色<b>‘" + orgRole.getRole().getName() + "’</b>在公司<b>‘" + orgRole.getOrg().getName()
						+ "’</b>中已经委派了用户。<br/>如确实要删除，请先撤销这些用户的角色！");
			} else {
				// 验证是否分配权限
				List<Permission> permissList = permissionService.getRolePermission(orgRole.getId());
				if (permissList.size() > 0) {
					throw new BizException("角色<b>‘" + orgRole.getRole().getName() + "’</b>在公司<b>‘" + orgRole.getOrg().getName()
							+ "’</b>中已经分配了权限。不能直接删除！<br/>如确实要删除，请先取消分配给此角色的权限！");
				} else {
					delete(orgRole.getId());
				}
			}
		}
	}
}
