package oecp.platform.datapermission.service;

import java.util.List;

import oecp.framework.exception.BizException;
import oecp.framework.service.BaseService;
import oecp.platform.datapermission.eo.DataPermission;

/**
 * 数据权限服务接口
 * 
 * @author liujingtao
 * @date 2011-6-24 上午09:22:18
 * @version 1.0
 */
public interface DataPermissionService extends BaseService<DataPermission> {
	/**
	 * 得到指定岗位资源的数据权限分配集合
	 * 
	 * @author liujingtao
	 * @date 2011 6 27 20:04:49
	 * @return 岗位资源的数据权限分配集合
	 */
	public List<DataPermission> getDataPermissions(String postId, String mdResourceId);
	
	/**
	 * 得到指定岗位(功能)资源的数据权限分配集合
	 * 
	 * @author liujingtao
	 * @date 2011 6 27 20:04:49
	 * @return 岗位(功能)资源的数据权限分配集合
	 */
	public List<DataPermission> getDataPermissions(String postId, String mdResourceId, String funId);
	
	/**
	 * 保存指定岗位资源的数据权限分配集合
	 * 
	 * @author liujingtao
	 * @date 2011 6 28 15:10:31
	 * @throws BizException 
	 */
	public void saveDataPermissions(List<DataPermission> list,String postId,
			String mdResourceId) throws BizException;
	
	/**
	 * 保存指定岗位(功能)资源的数据权限分配集合
	 * 
	 * @author liujingtao
	 * @date 2011 6 28 15:10:31
	 * @throws BizException 
	 */
	public void saveDataPermissions(List<DataPermission> list,String postId,
			String mdResourceId, String funId) throws BizException;
}
