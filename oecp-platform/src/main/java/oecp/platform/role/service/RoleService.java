/************************* 版权声明 *********************************
 *                                                                  *
 *                     版权所有：百洋软件                          				 	*
 *          Copyright (c) 2011 by www.oecp.cn                	*
 *                                                                  *
 ************************* 变更记录 *********************************
 *
 * 创建者：刘璟涛   创建日期： 2011 5 23
 * 备注：
 * 
 * 修改者：       修改日期：
 * 备注：
 * 
 */

package oecp.platform.role.service;

import java.util.List;

import oecp.framework.dao.QueryResult;
import oecp.framework.service.BaseService;
import oecp.platform.role.eo.Role;

/**
 * 角色服务接口
 * 
 * @author liujingtao
 * @date 2011 5 23 12:53 :15
 * @version 1.0
 */
public interface RoleService extends BaseService<Role> {

	/**
	 * 得到指定组织所创建的角色
	 * 
	 * @author liujingtao
	 * @date 2011 5 23 13:06:14
	 * @param orgId
	 *            公司id
	 * @return 包含指定组织所创建的所有角色
	 */
	public List<Role> getRolesByOrgId(String orgId);

	/**
	 * 获取某公司下已分配的角色和当前公司创建的角色
	 * 
	 * @param currentOrgPk
	 * @param org_pk
	 * @param start
	 * @param limit
	 * @return
	 */
	public QueryResult<Role> getAllOrgRole(String currentOrgPk, String org_pk,
			int start, int limit);
}
