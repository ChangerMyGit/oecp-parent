package oecp.platform.org.web;

import java.util.ArrayList;
import java.util.List;

import oecp.framework.dao.QueryResult;
import oecp.framework.exception.BizException;
import oecp.framework.util.FastJsonUtils;
import oecp.framework.web.JsonResult;
import oecp.platform.org.eo.Person;
import oecp.platform.org.eo.Post;
import oecp.platform.org.service.PersonService;
import oecp.platform.org.service.PostService;
import oecp.platform.web.BasePlatformAction;

import org.apache.commons.lang.StringUtils;
import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.Namespace;
import org.apache.struts2.convention.annotation.ParentPackage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

/**
 * 员工管理
 *
 * @author YangTao
 * @date 2012-3-2上午10:50:26
 * @version 1.0
 */
@Controller("personManageAction")
@Scope("prototype")
@ParentPackage("default-package")
@Namespace("/person")
public class PersonManageAction extends BasePlatformAction {

	private static final long serialVersionUID = 2993084926171300787L;

	@Autowired
	private PersonService personService;
	
	private String deptId;
	
	private Person person;
	
	private String personId;
	
	private String[] personIds;
	
	private String[] postIds;
	
	/**
	 * 选择某个部门，列出该部门下面的所有的员工
	 * 
	 * @author YangTao
	 * @date 2012-3-2上午11:17:07
	 * @return
	 */
	@Action("list")
	public String list(){
		QueryResult<Person> qr = this.personService.getPersonsByDept(deptId, start, limit);
		JsonResult jr = new JsonResult(qr.getTotalrecord().intValue(), qr
				.getResultlist());
		jr.setContainFields(new String[] { "id","no","name","email","mobile","post.name"});
		this.setJsonString(jr.toJSONString());
		return SUCCESS;
	}
	
	/**
	 * 选择某个员工，列出该员工兼职的岗位
	 * 
	 * @author YangTao
	 * @date 2012-3-2上午11:17:33
	 * @return
	 * @throws BizException
	 */
	@Action("loadOtherPosts")
	public String loadOtherPosts()throws BizException{
		person= this.personService.loadOtherPosts(personId);
		List<Post> posts = person.getOtherPosts();
		for (Post post : posts) {
			if(post.getParent()==null)
				post.setParent(new Post());
		}
		String json = FastJsonUtils.toJson(person.getOtherPosts(), new String[] { "id",
				"name", "code", "charge", "parent", "dept" });
		setJsonString(json);
		return SUCCESS;
	}
	
	/**
	 * 编辑页面加载数据
	 * 
	 * @author YangTao
	 * @date 2012-3-5上午09:37:52
	 * @return
	 */
	@Action("loadData")
	public String loadData()throws BizException{
		person= this.personService.loadOtherPosts(personId);
		JsonResult jr = new JsonResult(this.person);
		jr.setContainFields(new String[] { "id","no","name","email","mobile","post.name","post.id"});
		this.setJsonString(jr.toJSONString());
		return SUCCESS;
	}
	/**
	 * 保存
	 * 
	 * @author YangTao
	 * @date 2012-3-2下午02:54:35
	 * @return
	 */
	@Action("save")
	public String save()throws BizException{
		if(personService.validateOnlyNo(person)){
			setJsonString("{success:false,msg:'人员编号已经存在，请重新输入！'}");
			return SUCCESS;
		}
		Post p = person.getPost();
		if(p==null || p.getId()==null)
			person.setPost(null);
		this.personService.save(person);
		setJsonString("{success:true,msg:'保存成功！'}");
		return SUCCESS;
	}
	/**
	 * 删除
	 * 
	 * @author YangTao
	 * @date 2012-3-2下午02:54:35
	 * @return
	 */
	@Action("delete")
	public String delete()throws BizException{
		this.personService.delete(personIds);
		setJsonString("{success:true,msg:'删除成功！'}");
		return SUCCESS;
	}
	
	/**
	 * 获取任职或兼职岗位
	 * 
	 * @author YangTao
	 * @date 2012-3-5下午03:58:55
	 * @return
	 * @throws BizException
	 */
	@Action("getPostList")
	public String getPostList() throws BizException {
		String deptid = getRequest().getParameter("deptid");
		if (!StringUtils.isEmpty(deptid)) {
			List<Post> posts = this.personService.getPostsByDeptId(deptid,postIds);
			for (Post post : posts) {
				if(post.getParent() == null){
					post.setParent(new Post());
				}
			}
			String json = FastJsonUtils.toJson(posts, new String[] { "id", "name",
					"code", "charge", "parent", "dept" });
			setJsonString(json);
		}
		return SUCCESS;
	}
	
	/**
	 * 
	 * 判断员工编号是否唯一
	 * @author YangTao
	 * @date 2012-3-6上午10:20:31
	 * @return
	 * @throws BizException
	 */
	@Action("validateOnlyNo")
	public String validateOnlyNo()throws BizException{
		boolean re = this.personService.validateOnlyNo(person);
		if(re)
			setJsonString("{success:false,msg:'人员编号已经存在，请重新输入！'}");
		else
			setJsonString("{success:true,msg:'校验通过！'}");
		return SUCCESS;
	}
	
	public PersonService getPersonService() {
		return personService;
	}

	public void setPersonService(PersonService personService) {
		this.personService = personService;
	}

	public String getDeptId() {
		return deptId;
	}

	public void setDeptId(String deptId) {
		this.deptId = deptId;
	}

	public Person getPerson() {
		return person;
	}

	public void setPerson(Person person) {
		this.person = person;
	}

	public String getPersonId() {
		return personId;
	}

	public void setPersonId(String personId) {
		this.personId = personId;
	}

	public String[] getPersonIds() {
		return personIds;
	}

	public void setPersonIds(String[] personIds) {
		this.personIds = personIds;
	}

	public String[] getPostIds() {
		return postIds;
	}

	public void setPostIds(String[] postIds) {
		this.postIds = postIds;
	}
	
	
	
	
}
