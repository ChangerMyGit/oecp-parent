package oecp.platform.org.service;

import java.util.List;

import oecp.framework.dao.QueryResult;
import oecp.framework.exception.BizException;
import oecp.framework.service.BaseService;
import oecp.platform.org.eo.Person;
import oecp.platform.org.eo.Post;

/**
 * 人员服务接口
 * 
 * @author liujingtao
 * @date 2011-6-29 上午10:12:44
 * @version 1.0
 */
public interface PersonService extends BaseService<Person>{
	/**
	 * 获得指定岗位下的所有员工（不含兼职）
	 * 
	 * @author liujt
	 * @date 2011-7-5上午09:42:14
	 * @return
	 */
	public List<Person> getPersonsByPost(String postId);
	
	/**
	 * 获得指定岗位下的所有员工（含兼职）
	 * 
	 * @author liujt
	 * @date 2011-7-5上午10:04:36
	 * @param postId
	 * @return
	 */
	public List<Person> getAllPersonsByPost(String postId);
	
	/**
	 * 
	 * 获得指定部门下的所有员工（不含兼职）
	 * @author YangTao
	 * @date 2012-3-2上午10:57:41
	 * @param deptId
	 * @return
	 */
	public QueryResult<Person> getPersonsByDept(String deptId,int start,int limit);
	
	/**
	 * 加载员工的兼职岗位
	 * 
	 * @author YangTao
	 * @date 2012-3-2下午01:38:18
	 * @param person
	 * @return
	 */
	public Person loadOtherPosts(String personId)throws BizException;
	
	/**
	 * 保存或修改
	 * 
	 * @author YangTao
	 * @date 2012-3-5上午10:37:51
	 * @param person
	 */
	public void saveOrUpdate(Person person)throws BizException;
	
	/**
	 * 获取任职或兼职岗位，排除掉已经选择的
	 * 
	 * @author YangTao
	 * @date 2012-3-5下午04:01:05
	 * @param deptid
	 * @param postIds
	 * @return
	 */
	public List<Post> getPostsByDeptId(String deptid,String[] postIds);
	
	/**
	 * 判断员工编号是否唯一
	 * 
	 * @author YangTao
	 * @date 2012-3-6上午10:22:11
	 * @param no
	 * @return
	 */
	public boolean validateOnlyNo(Person person);
}
