package oecp.platform.bcfunction.service;

import java.util.List;

import org.springframework.stereotype.Service;

import oecp.framework.dao.QueryResult;
import oecp.framework.exception.BizException;
import oecp.framework.service.BaseService;
import oecp.platform.bcfunction.eo.Function;
import oecp.platform.bcfunction.eo.FunctionUI;

/**
 * 功能界面服务类
 * 
 * @author wangliang
 * @date 2011-4-11
 */
public interface FunctionUIService extends BaseService<FunctionUI> {
	/**
	 * 根据功能获取功能UI列表
	 * 
	 * @author wangliang
	 * @date 2011-4-11 上午11:58:05
	 * @param function_id
	 * @return
	 */
	public QueryResult<FunctionUI> getFunctionUIs(String function_id, int start,
			int limit);

	/**
	 * 根据功能主键获取所有功能界面
	 * 
	 * @param function_id
	 * @return
	 */
	public List<FunctionUI> getFunctionUIs(String function_id);

	/**
	 * 设置默认模板
	 * 
	 * @author wangliang
	 * @date 2011-4-11 上午11:58:05
	 * @param function
	 * @param functionui
	 */
	public void setDefault(FunctionUI functionui) throws BizException;

	/**
	 * 保存功能界面
	 */
	@Override
	public void save(FunctionUI t) throws BizException;
}
