package oecp.platform.bcfunction.service;

import java.util.List;

import oecp.framework.dao.QueryResult;
import oecp.framework.exception.BizException;
import oecp.framework.service.BaseService;
import oecp.platform.bcfunction.eo.Function;

/**
 * 组件服务接口类
 * 
 * @author wangliang
 * @date 2011-4-11
 */
public interface BcFunctionService extends BaseService<Function> {

	/**
	 * 获取组件列表Json字符串
	 * 
	 * @param bc_pk
	 * @return
	 */
	public List<Function> getRootFunctions(String bc_pk);

	/**
	 * 获取组件信息Json
	 * 
	 * @param bc_pk
	 * @return
	 */
	public QueryResult<Function> getFunctions(String bc_pk, int start, int limit);

	/**
	 * 获取所有带审批流的功能
	 * 
	 * @return
	 */
	public List<Function> getHasApprovalFunctions();
	/**
	 * 获取指定功能编号的功能
	 * 
	 * @return
	 */
	public Function getFunctionByCode(String code);
	/**
	 * 删除指定组件下的所有功能
	 * @author liujt
	 * @date 2011-8-23上午09:51:00
	 * @param bcId
	 */
	public void delFunctionsByBcId(String bcId);
	
	/**
	 * 访问组件WebService导入功能
	 * @author liujt
	 * @date 2011-8-12下午05:01:46
	 * @param id 业务组件id
	 * @throws BizException 
	 */
	public void initBcFunctions(String id) throws Exception;
}
