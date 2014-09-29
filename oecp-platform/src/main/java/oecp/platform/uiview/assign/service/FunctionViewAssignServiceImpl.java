package oecp.platform.uiview.assign.service;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;

import oecp.framework.exception.BizException;
import oecp.platform.base.service.PlatformBaseServiceImpl;
import oecp.platform.biztype.eo.BizType;
import oecp.platform.org.eo.Post;
import oecp.platform.org.service.PostService;
import oecp.platform.query.setting.eo.QueryScheme;
import oecp.platform.role.eo.OrgRole;
import oecp.platform.role.service.OrgRoleService;
import oecp.platform.uiview.assign.eo.FunctionView;
import oecp.platform.uiview.assign.eo.FunctionViewAssign;
import oecp.platform.uiview.assign.eo.FunctionViewQueryScheme;
import oecp.platform.user.eo.User;

import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service("functionViewAssignServiceImpl")
public class FunctionViewAssignServiceImpl extends
		PlatformBaseServiceImpl<FunctionViewAssign> implements
		FunctionViewAssignService {

	@Resource
	private PostService postService;
	@Resource
	private OrgRoleService orgRoleService;

	@Transactional
	@Override
	public void updateFunViewAssign(String functionViewId, String bizTypeId,
			String[] userIds, String[] postIds, String[] orgRoleIds)
			throws BizException {
		BizType bizType = null;
		if (StringUtils.isEmpty(bizTypeId)) {
			bizTypeId = null;
		} else {
			bizType = new BizType();
			bizType.setId(bizTypeId);
		}

		if (userIds.length == 1 && "".equals(userIds[0])) {
			userIds = new String[0];
		}
		if (postIds.length == 1 && "".equals(postIds[0])) {
			postIds = new String[0];
		}
		if (orgRoleIds.length == 1 && "".equals(orgRoleIds[0])) {
			orgRoleIds = new String[0];
		}

		FunctionView functionView = new FunctionView();
		functionView.setId(functionViewId);

		List<FunctionViewAssign> list = new ArrayList<FunctionViewAssign>();
		// 用户
		for (String userId : userIds) {
			FunctionViewAssign funViewAssign = findFunctionViewAssign(
					functionViewId, bizTypeId, userId, null, null);
			if (funViewAssign == null) {
				funViewAssign = new FunctionViewAssign();
				funViewAssign.setFuncview(functionView);
				funViewAssign.setBiztype(bizType);
				User user = new User();
				user.setId(userId);
				funViewAssign.setUser(user);
				save(funViewAssign);
			}
			list.add(funViewAssign);
		}
		// 岗位
		for (String postId : postIds) {
			FunctionViewAssign funViewAssign = findFunctionViewAssign(
					functionViewId, bizTypeId, null, postId, null);
			if (funViewAssign == null) {
				funViewAssign = new FunctionViewAssign();
				funViewAssign.setFuncview(functionView);
				funViewAssign.setBiztype(bizType);
				Post post = new Post();
				post.setId(postId);
				funViewAssign.setPost(post);
				save(funViewAssign);
			}
			list.add(funViewAssign);
		}
		// 角色
		for (String orgRoleId : orgRoleIds) {
			FunctionViewAssign funViewAssign = findFunctionViewAssign(
					functionViewId, bizTypeId, null, null, orgRoleId);
			if (funViewAssign == null) {
				funViewAssign = new FunctionViewAssign();
				funViewAssign.setFuncview(functionView);
				funViewAssign.setBiztype(bizType);
				OrgRole orgRole = new OrgRole();
				orgRole.setId(orgRoleId);
				funViewAssign.setOrgrole(orgRole);
				save(funViewAssign);
			}
			list.add(funViewAssign);
		}

		// 删除
		List<FunctionViewAssign> currentList = getFunctionsViewAssigns(
				functionViewId, bizTypeId);

		for (FunctionViewAssign functionViewAssign : currentList) {
			if (!list.contains(functionViewAssign)) {
				delete(functionViewAssign.getId());
			}
		}
	}

	@Override
	public FunctionViewAssign findFunctionViewAssign(String functionViewId,
			String bizTypeId, String userId, String postId, String orgRoleId) {

		if (userId != null) {
			if (bizTypeId == null) {
				return getDao()
						.findByWhere(
								FunctionViewAssign.class,
								"o.funcview.id=? AND o.biztype.id is NULL AND o.user.id=?",
								new Object[] { functionViewId, userId });
			} else {
				return getDao().findByWhere(FunctionViewAssign.class,
						"o.funcview.id=? AND o.biztype.id=? AND o.user.id=?",
						new Object[] { functionViewId, bizTypeId, userId });
			}
		} else if (postId != null) {
			if (bizTypeId == null) {
				return getDao()
						.findByWhere(
								FunctionViewAssign.class,
								"o.funcview.id=? AND o.biztype.id is NULL AND o.post.id=?",
								new Object[] { functionViewId, postId });
			} else {
				return getDao().findByWhere(FunctionViewAssign.class,
						"o.funcview.id=? AND o.biztype.id=? AND o.post.id=?",
						new Object[] { functionViewId, bizTypeId, postId });
			}
		} else if (orgRoleId != null) {
			if (bizTypeId == null) {
				return getDao()
						.findByWhere(
								FunctionViewAssign.class,
								"o.funcview.id=? AND o.biztype.id is NULL AND o.orgrole.id=?",
								new Object[] { functionViewId, orgRoleId });
			} else {
				return getDao()
						.findByWhere(
								FunctionViewAssign.class,
								"o.funcview.id=? AND o.biztype.id=? AND o.orgrole.id=?",
								new Object[] { functionViewId, bizTypeId,
										orgRoleId });
			}
		}
		return null;
	}

	@Override
	public List<FunctionViewAssign> getFunctionsViewAssigns(
			String functionViewId, String bizTypeId) {
		if (bizTypeId == null || "".equals(bizTypeId)) {
			return getDao().queryByWhere(FunctionViewAssign.class,
					"o.funcview.id=? AND o.biztype.id is NULL",
					new Object[] { functionViewId });
		} else {
			return getDao().queryByWhere(FunctionViewAssign.class,
					"o.funcview.id=? AND o.biztype.id=?",
					new Object[] { functionViewId, bizTypeId });
		}
	}

	@Override
	public List<FunctionView> getFunctionViews(String userId,
			String functionId, String orgId, String bizTypeId)
			throws BizException {
		// 按user查找
		List<FunctionViewAssign> list = null;
		if (StringUtils.isEmpty(bizTypeId)) {
			list = getDao()
					.queryByWhere(
							FunctionViewAssign.class,
							"o.user.id=? AND o.funcview.func.id=? AND o.biztype.id is NULL",
							new Object[] { userId, functionId });
		} else {
			list = getDao().queryByWhere(FunctionViewAssign.class,
					"o.user.id=? AND o.funcview.func.id=? AND o.biztype.id=?",
					new Object[] { userId, functionId, bizTypeId });
		}

		// 按post查找
		if (list == null || list.size() == 0) {
			List<Post> posts = postService.getPosts(userId, orgId);
			if (posts == null || posts.size() > 0) {
				StringBuffer postStr = new StringBuffer();
				postStr.append("(");
				for (int i = 0; i < posts.size(); i++) {
					if (i != 0) {
						postStr.append(",");
					}
					postStr.append("'");
					postStr.append(posts.get(i).getId());
					postStr.append("'");
				}
				postStr.append(")");
				if (StringUtils.isEmpty(bizTypeId)) {
					list = getDao()
							.queryByWhere(
									FunctionViewAssign.class,
									"o.post.id in "
											+ postStr.toString()
											+ " AND o.funcview.func.id=? AND o.biztype.id is NULL",
									new Object[] { functionId });
				} else {
					list = getDao()
							.queryByWhere(
									FunctionViewAssign.class,
									"o.post.id in "
											+ postStr.toString()
											+ " AND o.funcview.func.id=? AND o.biztype.id=?",
									new Object[] { functionId, bizTypeId });
				}
			}
		}

		// 按orgrole查找
		if (list == null || list.size() == 0) {
			List<OrgRole> orgroles = orgRoleService.getUserOrgRole(userId,
					orgId);
			if (orgroles == null || orgroles.size() > 0) {
				StringBuffer orgroleStr = new StringBuffer();
				orgroleStr.append("(");
				for (int i = 0; i < orgroles.size(); i++) {
					if (i != 0) {
						orgroleStr.append(",");
					}
					orgroleStr.append("'");
					orgroleStr.append(orgroles.get(i).getId());
					orgroleStr.append("'");
				}
				orgroleStr.append(")");
				if (StringUtils.isEmpty(bizTypeId)) {
					list = getDao()
							.queryByWhere(
									FunctionViewAssign.class,
									"o.orgrole.id in "
											+ orgroleStr.toString()
											+ " AND o.funcview.func.id=? AND o.biztype.id is NULL",
									new Object[] { functionId });
				} else {
					list = getDao()
							.queryByWhere(
									FunctionViewAssign.class,
									"o.orgrole.id in "
											+ orgroleStr.toString()
											+ " AND o.funcview.func.id=? AND o.biztype.id=?",
									new Object[] { functionId, bizTypeId });
				}
			}
		}

		// 查找 按业务类型查询但没有查到的，无业务类型分配的视图
		if(!StringUtils.isEmpty(bizTypeId) && (list == null || list.size() == 0)){
			list = getDao()
					.queryByWhere(
							FunctionViewAssign.class,
							"o.user.id=? AND o.funcview.func.id=? AND o.biztype.id is NULL",
							new Object[] { userId, functionId });
			
			if (list == null || list.size() == 0) {
				List<Post> posts = postService.getPosts(userId, orgId);
				if (posts == null || posts.size() > 0) {
					StringBuffer postStr = new StringBuffer();
					postStr.append("(");
					for (int i = 0; i < posts.size(); i++) {
						if (i != 0) {
							postStr.append(",");
						}
						postStr.append("'");
						postStr.append(posts.get(i).getId());
						postStr.append("'");
					}
					postStr.append(")");
					list = getDao()
							.queryByWhere(
									FunctionViewAssign.class,
									"o.post.id in "
											+ postStr.toString()
											+ " AND o.funcview.func.id=? AND o.biztype.id is NULL",
									new Object[] { functionId });
				}
			}
			if (list == null || list.size() == 0) {
				List<OrgRole> orgroles = orgRoleService.getUserOrgRole(userId,
						orgId);
				if (orgroles == null || orgroles.size() > 0) {
					StringBuffer orgroleStr = new StringBuffer();
					orgroleStr.append("(");
					for (int i = 0; i < orgroles.size(); i++) {
						if (i != 0) {
							orgroleStr.append(",");
						}
						orgroleStr.append("'");
						orgroleStr.append(orgroles.get(i).getId());
						orgroleStr.append("'");
					}
					orgroleStr.append(")");
					list = getDao()
							.queryByWhere(
									FunctionViewAssign.class,
									"o.orgrole.id in "
											+ orgroleStr.toString()
											+ " AND o.funcview.func.id=? AND o.biztype.id is NULL",
									new Object[] { functionId });
				}
			}
		}
		

		// 查找默认页面
		if (list == null || list.size() == 0) {
			return getDao().queryByWhere(FunctionView.class,
					"o.func.id=? AND o.sysdefault=true",
					new Object[] { functionId });
		}

		if (list != null && list.size() > 0) {
			List<FunctionView> functionViews = new ArrayList<FunctionView>();
			as: for (FunctionViewAssign assign : list) {
				for (FunctionView view : functionViews) {
					if (view.getId().equals(assign.getFuncview().getId())) {
						continue as;
					}
				}
				functionViews.add(assign.getFuncview());
			}
			return functionViews;
		}

		return null;
	}
	
	@Override
	public QueryScheme getQuerySchemeByViewId(String viewId) throws BizException {
		FunctionViewQueryScheme fvqs = getDao().findByWhere(FunctionViewQueryScheme.class, " o.funcView.id=? ", new Object[]{viewId});
		if(fvqs == null){
			return null;
		}else{
			QueryScheme qs = fvqs.getQueryScheme();
			qs.loadLazyAttributes();
			return qs;
		}
	}

	@Override
	public void setFunctionViewQueryScheme(String viewId, String querySchemeId) throws BizException {
		getDao().deleteByWhere(FunctionViewQueryScheme.class, " o.funcView.id=? ", new Object[]{viewId});
		if(StringUtils.isNotBlank(querySchemeId)){
			FunctionViewQueryScheme fvqs = new FunctionViewQueryScheme();
			fvqs.setFuncView(new FunctionView());
			fvqs.setQueryScheme(new QueryScheme());
			fvqs.getFuncView().setId(viewId);
			fvqs.getQueryScheme().setId(querySchemeId);
			
			getDao().create(fvqs);
		}
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

}
