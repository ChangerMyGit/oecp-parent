/**
 * oecp-platform - FunctionViewAssignAction.java
 * copyright 2009-2012 OECP www.oecp.cn
 * 创建人:slx	创建时间:2011-11-9上午11:24:02		版本:v1
 * ============================================
 * 修改人：			修改时间:					版本:
 * 修改原因:
 *
 * ============================================
 */
package oecp.platform.uiview.assign.web;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;

import oecp.framework.dao.QueryCondition;
import oecp.framework.dao.QueryResult;
import oecp.framework.exception.BizException;
import oecp.framework.util.FastJsonUtils;
import oecp.framework.web.JsonResult;
import oecp.platform.bcfunction.eo.Function;
import oecp.platform.bcfunction.service.BcFunctionService;
import oecp.platform.org.eo.Post;
import oecp.platform.org.service.PostService;
import oecp.platform.query.setting.eo.QueryScheme;
import oecp.platform.role.eo.OrgRole;
import oecp.platform.role.service.OrgRoleService;
import oecp.platform.uiview.assign.eo.FunctionView;
import oecp.platform.uiview.assign.eo.FunctionViewAssign;
import oecp.platform.uiview.assign.service.FunctionViewAssignService;
import oecp.platform.uiview.assign.service.FunctionViewService;
import oecp.platform.user.eo.User;
import oecp.platform.user.service.UserService;
import oecp.platform.web.BasePlatformAction;

import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.Namespace;
import org.apache.struts2.convention.annotation.ParentPackage;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

/**
 * 功能UI视图分配Action
 * 
 * @author slx
 * @date 2011-11-9
 */
@Controller
@Scope("prototype")
@ParentPackage("default-package")
@Namespace("/funviewassign")
public class FunctionViewAssignAction extends BasePlatformAction {
	private static final long serialVersionUID = 1L;

	private String functionId;
	@Resource
	private FunctionViewService functionViewService;
	@Resource
	private UserService userService;
	@Resource
	private PostService postService;
	@Resource
	private OrgRoleService orgRoleService;
	@Resource
	private FunctionViewAssignService functionViewAssignService;
	@Resource
	private BcFunctionService bcFunctionService;

	private String functionViewId;
	private String bizTypeId;
	private String[] userIds;
	private String[] postIds;
	private String[] orgRoleIds;
	private String functionCode;
	private String querySchemeId;

	List<QueryCondition> conditions = new ArrayList<QueryCondition>();

	/**
	 * 根据指定功能获得功能视图列表
	 * 
	 * @author liujt
	 * @date 2011-11-10上午9:36:10
	 * @return
	 */
	@Action("getFunctionViews")
	public String getFunctionViews() {
		String orgId = getOnlineUser().getLoginedOrg().getId();
		List<FunctionView> list = functionViewService.getViewsByFunctionID(
				functionId, orgId);
		String json = FastJsonUtils.toJson(list,
				new String[] { "id", "viewcode", "viewname", "org", "shared",
						"name", "func", "code" });
		setJsonString(json);
		return SUCCESS;
	}

	@Action(value = "queryUserList")
	public String queryUserList() {
		String orgId = getOnlineUser().getLoginedOrg().getId();

		// TODO 判断要查询的公司是否拥有权限，如果没有权限，则默认还是自己所在的公司
		QueryResult<User> qr = this.userService.getUsersByOrgID(orgId,false,
				conditions, -1, -1, getOrderBy());
		JsonResult jr = new JsonResult(qr.getTotalrecord().intValue(),
				qr.getResultlist());
		jr.setContainFields(new String[] { "id", "loginId", "name", "email",
				"createTime" });
		this.setJsonString(jr.toJSONString());
		return SUCCESS;
	}

	@Action("getPostList")
	public String getPostList() throws BizException {
		String orgId = getOnlineUser().getLoginedOrg().getId();
		List<Post> posts = postService.getPostsByOrgId(orgId);
		String json = "";
		json = FastJsonUtils.toJson(posts, new String[] { "id", "name", "code",
				"charge", "parent", "dept" });
		json = json.replaceAll("\"parent\":null", "\"parent\":{}");
		setJsonString(json);
		return SUCCESS;
	}

	@Action("getOrgRoleList")
	public String getOrgRoleList() throws BizException {
		String orgId = getOnlineUser().getLoginedOrg().getId();
		List<OrgRole> orgRoles = orgRoleService.getAllOrgRole(orgId);
		String json = FastJsonUtils.toJson(orgRoles, new String[] { "id",
				"role", "code", "name" });

		setJsonString(json);
		return SUCCESS;
	}

	@Action("updateFunViewAssign")
	public String updateFunViewAssign() throws BizException {
		functionViewAssignService.updateFunViewAssign(functionViewId,
				bizTypeId, userIds, postIds, orgRoleIds);
		setJsonString("{success : true,msg : '保存成功'}");
		return SUCCESS;
	}

	@Action("queryFunViewAssign")
	public String queryFunViewAssign() {
		List<FunctionViewAssign> list = functionViewAssignService
				.getFunctionsViewAssigns(functionViewId, bizTypeId);
		List<String> userIds = new ArrayList<String>();
		List<String> postIds = new ArrayList<String>();
		List<String> orgroleIds = new ArrayList<String>();
		for (FunctionViewAssign functionViewAssign : list) {
			if (functionViewAssign.getUser() != null) {
				userIds.add(functionViewAssign.getUser().getId());
			} else if (functionViewAssign.getPost() != null) {
				postIds.add(functionViewAssign.getPost().getId());
			} else if (functionViewAssign.getOrgrole() != null) {
				orgroleIds.add(functionViewAssign.getOrgrole().getId());
			}
		}
		String userJson = FastJsonUtils.toJson(userIds);
		String postJson = FastJsonUtils.toJson(postIds);
		String orgroleJson = FastJsonUtils.toJson(orgroleIds);

		setJsonString("{userJson : " + userJson + ",postJson : " + postJson
				+ ",orgroleJson : " + orgroleJson + "}");
		return SUCCESS;
	}

	@Action("getAssigns")
	public String getAssigns() throws BizException {
		Function fun = bcFunctionService.getFunctionByCode(functionCode);
		List<FunctionView> list = functionViewAssignService.getFunctionViews(
				getOnlineUser().getUser().getId(), fun.getId(), getOnlineUser()
						.getLoginedOrg().getId(), bizTypeId);
		String json = FastJsonUtils.toJson(list, new String[] { "id",
				"viewcode", "viewname" });
		setJsonString(json);
		return SUCCESS;
	}
	
	@Action("getQS")
	public String getQueryScheme() throws BizException {
		QueryScheme qs = functionViewAssignService.getQuerySchemeByViewId(functionViewId);
		JsonResult jr = new JsonResult(qs);
		setJsonString(jr.toJSONString());
		return SUCCESS;
	}
	
	@Action("saveFuncViewQS")
	public String saveQuerySchemeSetting() throws BizException {
		functionViewAssignService.setFunctionViewQueryScheme(functionViewId, querySchemeId);
		setJsonString("{success : true,msg : '保存成功'}");
		return SUCCESS;
	}

	public String getFunctionId() {
		return functionId;
	}

	public void setFunctionId(String functionId) {
		this.functionId = functionId;
	}

	public FunctionViewService getFunctionViewService() {
		return functionViewService;
	}

	public void setFunctionViewService(FunctionViewService functionViewService) {
		this.functionViewService = functionViewService;
	}

	public UserService getUserService() {
		return userService;
	}

	public void setUserService(UserService userService) {
		this.userService = userService;
	}

	public PostService getPostService() {
		return postService;
	}

	public void setPostService(PostService postService) {
		this.postService = postService;
	}

	public OrgRoleService getOrgRoleService() {
		return orgRoleService;
	}

	public void setOrgRoleService(OrgRoleService orgRoleService) {
		this.orgRoleService = orgRoleService;
	}

	public List<QueryCondition> getConditions() {
		return conditions;
	}

	public void setConditions(List<QueryCondition> conditions) {
		this.conditions = conditions;
	}

	public FunctionViewAssignService getFunctionViewAssignService() {
		return functionViewAssignService;
	}

	public void setFunctionViewAssignService(
			FunctionViewAssignService functionViewAssignService) {
		this.functionViewAssignService = functionViewAssignService;
	}

	public String getFunctionViewId() {
		return functionViewId;
	}

	public void setFunctionViewId(String functionViewId) {
		this.functionViewId = functionViewId;
	}

	public String getBizTypeId() {
		return bizTypeId;
	}

	public void setBizTypeId(String bizTypeId) {
		this.bizTypeId = bizTypeId;
	}

	public String[] getUserIds() {
		return userIds;
	}

	public void setUserIds(String[] userIds) {
		this.userIds = userIds;
	}

	public String[] getPostIds() {
		return postIds;
	}

	public void setPostIds(String[] postIds) {
		this.postIds = postIds;
	}

	public String[] getOrgRoleIds() {
		return orgRoleIds;
	}

	public void setOrgRoleIds(String[] orgRoleIds) {
		this.orgRoleIds = orgRoleIds;
	}

	public BcFunctionService getBcFunctionService() {
		return bcFunctionService;
	}

	public void setBcFunctionService(BcFunctionService bcFunctionService) {
		this.bcFunctionService = bcFunctionService;
	}

	public String getFunctionCode() {
		return functionCode;
	}

	public void setFunctionCode(String functionCode) {
		this.functionCode = functionCode;
	}

	public String getQuerySchemeId() {
		return querySchemeId;
	}

	public void setQuerySchemeId(String querySchemeId) {
		this.querySchemeId = querySchemeId;
	}

}
