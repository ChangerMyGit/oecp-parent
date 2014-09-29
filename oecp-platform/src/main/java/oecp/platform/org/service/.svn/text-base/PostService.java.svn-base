package oecp.platform.org.service;

import java.util.List;

import oecp.framework.exception.BizException;
import oecp.framework.service.BaseService;
import oecp.platform.org.eo.Post;

/**
 * 岗位服务接口
 * 
 * @author liujingtao
 * @date 2011-6-22 下午03:14:06
 * @version 1.0
 */
public interface PostService extends BaseService<Post> {
	/**
	 * 得到指定部门的所有岗位
	 * 
	 * @author liujingtao
	 * @date 2011 6 22 03:16:03
	 * @param deptid
	 *            部门id
	 * @return 该部门的岗位列表
	 */
	public List<Post> getPostsByDeptId(String deptid);

	/**
	 * 
	 * 得到某个组织下面的所有的岗位
	 * 
	 * @author yangtao
	 * @date 2011-8-8下午04:08:14
	 * @param orgid
	 * @return
	 */
	public List<Post> getPostsByOrgId(String orgid);

	/**
	 * 获得用户在指定公司下的所有岗位
	 * @author liujt
	 * @date 2011-11-18下午4:13:56
	 * @param userId
	 * @param orgId
	 * @return
	 * @throws BizException
	 */
	List<Post> getPosts(String userId, String orgId) throws BizException;
}
