package oecp.platform.org.service;

import java.util.List;

import oecp.framework.dao.QueryResult;
import oecp.framework.exception.BizException;
import oecp.platform.base.service.PlatformBaseServiceImpl;
import oecp.platform.org.eo.Person;
import oecp.platform.org.eo.Post;

import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Service;

/**
 * 
 * @author liujingtao
 * @date 2011-6-29 上午10:42:57
 * @version 1.0
 */
@Service("personService")
public class PersonServiceImpl extends PlatformBaseServiceImpl<Person>
		implements PersonService {

	@Override
	public List<Person> getPersonsByPost(String postId) {
		return getDao().queryByWhere(Person.class, "o.post.id=?",
				new Object[] { postId });
	}

	@Override
	public List<Person> getAllPersonsByPost(String postId) {
		String hql = "select distinct  o from Person o left join o.otherPosts p where p.id=? or o.post.id=?";
//		String hql = "select distinct  o from Person o,Post p where p in elements(o.otherPosts) and p.id=? or o.post.id=?";
		return getDao().getHibernateSession().createQuery(hql)
				.setString(0, postId).setString(1, postId).list();
	}
	
	/**
	 * 
	 * 获得指定部门下的所有员工（不含兼职）
	 * @author YangTao
	 * @date 2012-3-2上午10:57:41
	 * @param deptId
	 * @return
	 */
	public QueryResult<Person> getPersonsByDept(String deptId,int start,int limit){
		String sql = "post.dept.id=?";
		Object[] ob = new Object[]{deptId};
		QueryResult<Person> qr = this.getDao().getScrollData(Person.class, start, limit, sql, ob, null);
		return qr;
	}

	/**
	 * 加载员工的兼职岗位
	 * 
	 * @author YangTao
	 * @date 2012-3-2下午01:38:18
	 * @param person
	 * @return
	 */
	public Person loadOtherPosts(String personId)throws BizException{
		Person person = this.find(personId);
		person.loadLazyAttributes();
		return person;
	}
	
	/**
	 * 保存或修改
	 * 
	 * @author YangTao
	 * @date 2012-3-5上午10:37:51
	 * @param person
	 */
	public void saveOrUpdate(Person person)throws BizException{
			this.save(person);
	}
	
	/**
	 * 获取任职或兼职岗位，排除掉已经选择的
	 * 
	 * @author YangTao
	 * @date 2012-3-5下午04:01:05
	 * @param deptid
	 * @param postIds
	 * @return
	 */
	public List<Post> getPostsByDeptId(String deptid,String[] postIds){
		String result = "";
		if(postIds!=null){
			for(String postId : postIds){
				if(StringUtils.isNotEmpty(postId)){
					result += "'"+postId+"'";
					result += ",";
				}
			}
			if(result.endsWith(","))
				result = result.substring(0, result.length()-1);
		}
		if(StringUtils.isNotEmpty(result)){
			return getDao().queryByWhere(Post.class, "o.dept.id=? and o.id not in("+result+")", new Object[]{deptid});
		}else{
			return getDao().queryByWhere(Post.class, "o.dept.id=?", new Object[]{deptid});
		}
	}
	
	/**
	 * 判断员工编号是否唯一
	 * true 代表已经存在该工号
	 * false 代表没有该工号
	 * @author YangTao
	 * @date 2012-3-6上午10:22:11
	 * @param no
	 * @return
	 */
	public boolean validateOnlyNo(Person person){
		List<Person> list = null;
		if(StringUtils.isNotEmpty(person.getId())){
			list = getDao().queryByWhere(Person.class, "o.no=? and o.id!=?",new Object[] { person.getNo(),person.getId()});
			
		}else{
			list = getDao().queryByWhere(Person.class, "o.no=?",new Object[] { person.getNo() });
		}
		
		if(list.size()>0)
			return true;
		else
			return false;
	}
}
