package oecp.platform.datapermission.service;

import java.util.List;

import oecp.framework.exception.BizException;
import oecp.framework.service.BaseService;
import oecp.platform.datapermission.eo.DataDiscretePermission;

/**
 * 
 * @author liujingtao
 * @date 2011-7-1 下午03:05:32
 * @version 1.0
 */
public interface DataDiscretePermissionService extends
		BaseService<DataDiscretePermission> {
	/**
	 * 分页查询资源集合
	 * 
	 * @author liujingtao
	 * @date 2011 7 1 20:04:49
	 * @return 资源集合
	 */
	public List getAllClassDatas(String hql, int start, int limit);

	/**
	 * 资源集合总数
	 * 
	 * @author liujingtao
	 * @date 2011 7 2 10:06:22
	 * @return 资源集合总数
	 */
	public long getTotalCount(String hql);

	/**
	 * 获得指定岗位、主资源的离散数据权限集合
	 * 
	 * @author liujt
	 * @date 2011-7-4上午10:29:46
	 * @return
	 */
	public List<DataDiscretePermission> getDatasByPostAndMD(String postId,
			String mdResourceId);

	/**
	 * 获得指定岗位、功能、主资源的离散数据权限集合
	 * 
	 * @author liujt
	 * @date 2011-7-4上午10:29:46
	 * @return
	 */
	public List<DataDiscretePermission> getDatas(String postId,
			String mdResourceId, String funId);

	/**
	 * 根据指定的岗位、主资源id、数据数组id，保存或删除离散数据权限
	 * @author liujt
	 * @date 2011-9-1上午11:00:16
	 * @param postId
	 * @param mdResourceId
	 * @param addDatas
	 * @param delDatas
	 * @param funId
	 * @throws BizException
	 */
	public void saveDatasDisPermission(String postId, String mdResourceId,
			String[] addDatas, String[] delDatas, String funId) throws BizException;
	
	/**
	 * 根据指定的岗位、主资源id、数据id，保存离散数据权限
	 * 
	 * @author liujt
	 * @throws BizException
	 * @date 2011-7-4下午04:23:23
	 */
	public void saveDataDisPermission(String postId, String mdResourceId,
			String dataId, String funId) throws BizException;

	/**
	 * 根据指定的岗位、主资源id、数据id，删除离散数据权限
	 * 
	 * @author liujt
	 * @throws BizException
	 * @date 2011-7-4下午04:23:23
	 */
	public void deleteDataDisPermission(String postId, String mdResourceId,
			String dataId, String funId) throws BizException;
}
