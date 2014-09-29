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

import java.util.List;

import oecp.framework.dao.QueryResult;
import oecp.framework.exception.BizException;
import oecp.framework.service.BaseService;
import oecp.platform.role.eo.OrgRole;
import oecp.platform.role.eo.Role;
import oecp.platform.user.eo.User;

/**
 * 公司角色服务
 * 
 * @author slx
 * @date 2011 5 5 17:09:15
 * @version 1.0
 */
public interface OrgRoleService extends BaseService<OrgRole> {

	/**
	 * 得到用户所具有的公司角色
	 * 
	 * @author slx
	 * @date 2011 5 5 17:12:24
	 * @modifyNote
	 * @param userId
	 *            用户ID
	 * @return 所具有的公司角色
	 */
	public List<OrgRole> getUserOrgRole(String userId);

	/**
	 * 返回用户在本公司内的所有公司角色
	 * 
	 * @author slx
	 * @date 2011 5 5 17:13:06
	 * @modifyNote
	 * @param userId
	 *            用户ID
	 * @param orgId
	 *            公司ID
	 * @return 本公司内的所具有的公司角色
	 */
	public List<OrgRole> getUserOrgRole(String userId, String orgId);

	/**
	 * 返回具有该角色的所有公司角色
	 * 
	 * @author liujingtao
	 * @date 2011 5 23 14:58:06
	 * @modifyNote
	 * @param roleId
	 *            角色ID
	 * @return 具有该角色的所有公司角色
	 */
	public List<OrgRole> getUserOrgRoleByRoleId(String roleId);

	/**
	 * 根据角色Id和公司id获取组织角色
	 * 
	 * @author wangliang
	 * @param roleId
	 *            角色ID
	 * @param orgIds
	 *            公司ID,中间以逗号间隔
	 * @return
	 */
	public List<OrgRole> getUserOrgRoleByRoleIdOrgId(String roleId, String[] orgIds);

	/**
	 * 获取公司拥有的所有加色
	 * 
	 * @param org_pk
	 *            公司主键
	 * @return
	 */
	public List<OrgRole> getAllOrgRole(String org_pk);

	/**
	 * 判断公司角色已经分配
	 * 
	 * @param org_pk
	 * @return
	 */
	public boolean isAllocated(String org_pk, Role role);

	/**
	 * 获取当前公司内没有此角色的所有用户
	 * 
	 * @param orgId
	 * @param roleId
	 * @return
	 */
	public List<User> getNotHasRoleUsers(String orgId, String roleId);

	/**
	 * 获取当前公司内拥有此角色的所有用户
	 * 
	 * @param orgId
	 *            公司主键
	 * @param roleId
	 *            角色主键
	 * @return
	 */
	public List<User> getHasRoleUsers(String orgId, String roleId);

	/**
	 * 更新组织角色关联的用户列表
	 * 
	 * @param currentOrgId
	 *            当前登录公司
	 * @param orgRoleId
	 *            组织角色主键
	 * @param userIds
	 *            用户主键列表
	 */
	public void saveOrgRoleByUserIds(String currentOrgId, String orgRoleId, String[] userIds) throws BizException;

	/**
	 * 获取组织角色
	 * 
	 * @param orgRoleIds
	 *            主键数组
	 * @return
	 * @throws BizException
	 */
	public List<OrgRole> getOrgRolesByRoleIds(String[] orgRoleIds) throws BizException;

	/**
	 * 根据角色id删除公司角色信息 如果角色所属的公司内没有分配权限，也没有指派用户则允许删除，否则不允许。
	 * 
	 * @author songlixiao
	 * @date 2014年1月15日上午10:51:55
	 * @param roleId
	 * @throws BizException
	 */
	public void deleteByRoleId(String roleId) throws BizException;
}
