package oecp.platform.uiview.assign.service;

import java.util.List;

import oecp.framework.exception.BizException;
import oecp.framework.service.BaseService;
import oecp.platform.query.setting.eo.QueryScheme;
import oecp.platform.uiview.assign.eo.FunctionView;
import oecp.platform.uiview.assign.eo.FunctionViewAssign;

/**
 * 功能视图分配关系服务接口
 * 
 * @author liujt
 * @date 2011-11-16 上午9:22:17
 * @version 1.0
 */
public interface FunctionViewAssignService extends
		BaseService<FunctionViewAssign> {
	/**
	 * 保存视图分配关系
	 * 
	 * @author liujt
	 * @date 2011-11-16上午9:23:50
	 * @param bizTypeId
	 * @param userIds
	 * @param postIds
	 * @param orgRoleIds
	 * @throws BizException
	 */
	void updateFunViewAssign(String functionViewId, String bizTypeId,
			String[] userIds, String[] postIds, String[] orgRoleIds)
			throws BizException;

	/**
	 * 根据指定参数获得视图分配关系
	 * 
	 * @author liujt
	 * @date 2011-11-16上午11:11:15
	 * @param functionViewId
	 * @param bizTypeId
	 * @param userId
	 * @param postId
	 * @param orgRoleId
	 * @return
	 */
	FunctionViewAssign findFunctionViewAssign(String functionViewId,
			String bizTypeId, String userId, String postId, String orgRoleId);

	/**
	 * 根据指定参数获得视图分配关系列表
	 * 
	 * @author liujt
	 * @date 2011-11-16上午11:11:36
	 * @param functionViewId
	 * @param bizTypeId
	 * @return
	 */
	List<FunctionViewAssign> getFunctionsViewAssigns(String functionViewId,
			String bizTypeId);

	/**
	 * 根据提供的参数获得该用户在当前公司所分配的视图
	 * @author liujt
	 * @date 2011-11-18上午11:20:29
	 * @param userId
	 * @param functionId
	 * @param orgId
	 * @param bizTypeId
	 * @return
	 * @throws BizException 
	 */
	List<FunctionView> getFunctionViews(String userId, String functionId,
			String orgId, String bizTypeId) throws BizException;
	/**
	 * 根据视图id获得对应的查询方案
	 * @author songlixiao
	 * @date 2014年2月11日上午8:59:50
	 * @param viewId
	 * @return
	 * @throws BizException
	 */
	QueryScheme getQuerySchemeByViewId(String viewId) throws BizException;
	
	/**
	 * 设置ui视图对应的查询方案
	 * 如果查询方案id为null则，撤销当前视图所关联过的查询方案。
	 * @author songlixiao
	 * @date 2014年2月11日上午9:01:32
	 * @param viewId
	 * @param querySchemeId
	 * @throws BizException
	 */
	void setFunctionViewQueryScheme(String viewId,String querySchemeId) throws BizException;
}
