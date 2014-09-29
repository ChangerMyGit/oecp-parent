package oecp.platform.bcinfo.service;

import javax.wsdl.WSDLException;

import oecp.framework.dao.QueryResult;
import oecp.framework.exception.BizException;
import oecp.framework.service.BaseService;
import oecp.platform.bcinfo.eo.BizComponent;
import oecp.platform.bcinfo.eo.BizService;

/**
 * 
 * 
 * @author wangliang
 * @date 2011-8-9上午10:04:59
 * @version 1.0
 */
public interface BizServiceService extends BaseService<BizService> {
	/**
	 * 根据组件编号获取组件服务列表
	 * 
	 * @author wangliang
	 * @date 2011-8-11下午02:09:15
	 * @param bc_id
	 * @return
	 */
	public QueryResult<BizService> queryBizServiceByBcId(int start, int limit,
			String bcId);

	/**
	 * 根据组件编号删除服务
	 * 
	 * @author wangliang
	 * @date 2011-8-12上午11:15:49
	 * @param bcId
	 */
	public void delBizServiceByBcId(String bcId) throws BizException;

	/**
	 * 初始化组件服务(会清除已初始化的历史数据)
	 */
	public void initBizServices(BizComponent bc) throws BizException,
			WSDLException;

	/**
	 * 保存实体
	 * 
	 * @author wangliang
	 * @date 2011-8-15下午05:06:55
	 * @param service
	 * @throws BizException
	 */
	public void saveService(BizService service) throws BizException;

	/**
	 * 根据主键获取服务Json数据
	 * 
	 * @author wangliang
	 * @date 2011-8-15下午05:09:29
	 * @param serviceId
	 * @return
	 */
	public String getServiceJsonById(String serviceId) throws BizException,
			NumberFormatException;
}
