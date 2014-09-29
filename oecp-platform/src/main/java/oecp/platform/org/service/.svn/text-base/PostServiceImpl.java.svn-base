package oecp.platform.org.service;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;

import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Service;

import oecp.framework.exception.BizException;
import oecp.platform.base.service.PlatformBaseServiceImpl;
import oecp.platform.org.eo.Department;
import oecp.platform.org.eo.Person;
import oecp.platform.org.eo.Post;
import oecp.platform.user.eo.User;
import oecp.platform.user.service.UserService;

/**
 * 部门岗位服务实现
 *
 * @author liujingtao
 * @date 2011-6-17 上午09:26:32
 * @version 1.0
 */
@Service("postService")
public class PostServiceImpl extends PlatformBaseServiceImpl<Post> implements PostService{
	@Resource
	private UserService userService;
	@Resource
	private PersonService personService;

	/**
	*
	* @author liujingtao
	* @date 2011-6-17 上午09:40:19
	*/
	@Override
	public List<Post> getPostsByDeptId(String deptid) {
		return getDao().queryByWhere(Post.class, "o.dept.id=?", new Object[]{deptid});
	}

	/**
	 * 
	 * 得到某个组织下面的所有的岗位
	 * @author yangtao
	 * @date 2011-8-8下午04:08:14
	 * @param orgid
	 * @return
	 */
	public List<Post> getPostsByOrgId(String orgid) {
		return getDao().queryByWhere(Post.class, "o.org.id=?", new Object[]{orgid});
	}

	@Override
	public List<Post> getPosts(String userId, String orgId) throws BizException {
		List<Post> posts = new ArrayList<Post>();
		User user = userService.find(userId);
		String personId = user.getPersonId();
		if (StringUtils.isNotEmpty(personId)) {
			Person person = personService.find(personId);
			if (person != null) {
				Post post = person.getPost();
				List<Post> otherPosts = person.getOtherPosts();
				if (orgId.equals(post.getOrg().getId())) {
					posts.add(post);
				}
				for (Post otherPost : otherPosts) {
					if (orgId.equals(otherPost.getOrg().getId())) {
						posts.add(otherPost);
					}
				}
			}
		}

		return posts;
	}

	public UserService getUserService() {
		return userService;
	}

	public void setUserService(UserService userService) {
		this.userService = userService;
	}

	public PersonService getPersonService() {
		return personService;
	}

	public void setPersonService(PersonService personService) {
		this.personService = personService;
	}
	
}
