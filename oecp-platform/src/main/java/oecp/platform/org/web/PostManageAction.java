package oecp.platform.org.web;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;

import oecp.framework.exception.BizException;
import oecp.framework.util.FastJsonUtils;
import oecp.platform.org.eo.Organization;
import oecp.platform.org.eo.Post;
import oecp.platform.org.service.PostService;
import oecp.platform.web.BasePlatformAction;

import org.apache.commons.lang.StringUtils;
import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.Namespace;
import org.apache.struts2.convention.annotation.ParentPackage;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;

/**
 * 
 * @author liujingtao
 * @date 2011-6-17 上午09:56:30
 * @version 1.0
 */
@Controller("PostManageAction")
@Scope("prototype")
@ParentPackage("default-package")
@Namespace("/post")
public class PostManageAction extends BasePlatformAction {
	private static final long serialVersionUID = 1L;
	@Resource
	private PostService postService;

	private Post post;
	//要删除的岗位主键数组
	private String[] postids;
	/**
	 * 获得该部门的所有岗位信息
	 * 
	 * @author liujingtao
	 * @date 2011-6-17 上午09:58:11
	 * @return
	 * @throws BizException
	 */
	@Action("getPostList")
	public String getPostList() throws BizException {
		String deptid = getRequest().getParameter("deptid");
		String postid = getRequest().getParameter("postid");
		if (!StringUtils.isEmpty(deptid)) {
			List<Post> posts = postService.getPostsByDeptId(deptid);
			for (Post post : posts) {
				if(post.getParent() == null){
					post.setParent(new Post());
				}
			}
			String json = "";
			if (!StringUtils.isEmpty(postid)) {
				Post post = postService.find(postid);
				List<Post> parentPosts = new ArrayList<Post>();
				for (Post post_tmp : posts) {
					if (isNotParent(post_tmp, post)) {
						parentPosts.add(post_tmp);
					}
				}
				json = FastJsonUtils.toJson(parentPosts, new String[] { "id",
						"name", "code", "charge", "parent", "dept" });
			} else {
				json = FastJsonUtils.toJson(posts, new String[] { "id", "name",
						"code", "charge", "parent", "dept" });
			}
			setJsonString(json);
		}
		return SUCCESS;
	}

	/**
	 * 岗位添加或修改
	 * 
	 * @author liujingtao
	 * @date 2011 6 20 17:32:35
	 * @return
	 * @throws BizException
	 */
	@Action("savePost")
	@Transactional
	public String saveDept() throws BizException {
		try {
			if (post != null) {
				Organization org = new Organization();
				org.setId(this.getOnlineUser().getLoginedOrg().getId());
				post.setOrg(org);
				if (StringUtils.isEmpty(post.getId())) {
					post.setId(null);
				}
				if (StringUtils.isEmpty(post.getParent().getId())) {
					post.setParent(null);
				}
				if (StringUtils.isEmpty(post.getDept().getId())){
					setJsonString("{success:false,msg:'保存失败，请正确选择所属部门！'}");
					return SUCCESS;
				}
				postService.save(post);
			}

			setJsonString("{success:true,msg:'保存成功！'}");
		} catch (Exception ex) {
			ex.printStackTrace();
			setJsonString("{success:false,msg:'保存失败，请联系管理员！'}");
		}
		return SUCCESS;
	}

	/**
	 * 删除岗位信息
	 * 
	 * @author liujingtao
	 * @date 2011 6 23 08:59:46
	 * @return
	 */
	@Action("deletePost")
	@Transactional
	public String deleteDept() {
		try {
			if (postids != null && postids.length > 0) {
				for(String postid : postids){
					postService.delete(postid);
				}
				setJsonString("{success : true , msg : '删除成功'}");
			}
		} catch (Exception e) {
			setJsonString("{success : false , msg : '" + e.getMessage() + "'}");
		}
		return SUCCESS;
	}

	
	private boolean isNotParent(Post post_tmp, Post post) {
		if (post.equals(post_tmp)) {
			return false;
		} else if (post_tmp.getParent() == null) {
			return true;
		} else {
			return isNotParent(post_tmp.getParent(), post);
		}
	}

	public PostService getPostService() {
		return postService;
	}

	public void setPostService(PostService postService) {
		this.postService = postService;
	}

	/**
	 * @return the post
	 */
	public Post getPost() {
		return post;
	}

	/**
	 * @param post
	 *            the post to set
	 */
	public void setPost(Post post) {
		this.post = post;
	}

	/**
	 * @return the postids
	 */
	public String[] getPostids() {
		return postids;
	}

	/**
	 * @param postids the postids to set
	 */
	public void setPostids(String[] postids) {
		this.postids = postids;
	}

}
