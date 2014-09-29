/************************* 版权声明 *********************************
 *                                                                  *
 *                     版权所有：百洋软件                          				 	*
 *          Copyright (c) 2011 by www.oecp.cn                	*
 *                                                                  *
 ************************* 变更记录 *********************************
 *
 * 创建者：宋黎晓   创建日期： 2011-6-17
 * 备注：
 * 
 * 修改者：       修改日期：
 * 备注：
 * 
 */    

package oecp.platform.billflow.service;

import java.util.List;

import oecp.framework.exception.BizException;
import oecp.framework.service.BaseService;
import oecp.platform.billflow.eo.BillFlowConfig;
import oecp.platform.billflow.eo.DataFieldView;

/**
 * 单据流配置服务类
 * @author slx
 * @date 2011-6-17 下午03:06:05
 * @version 1.0
 */
public interface BillFlowConfigService extends BaseService<BillFlowConfig> {

	/**
	 * 获得一个业务类型相关的单据配置
	 * @author slx
	 * @date 2011-6-20 下午03:16:29
	 * @modifyNote
	 * @param biztypeID
	 * @return
	 */
	public List<BillFlowConfig> getBillFlowConfigByBiz(String biztypeID);

	/**
	 * 获得一个单据流程配置的前置数据项目配置
	 * @author slx
	 * @date 2011-6-22 下午01:59:05
	 * @modifyNote
	 * @param cfgID
	 * @return
	 * @throws BizException
	 */
	public List<DataFieldView> getPreDataFieldsByCfgID(String cfgID) throws BizException;

	/**
	 * 保存单据前置数据项配置
	 * @author slx
	 * @date 2011-6-22 下午01:59:40
	 * @modifyNote
	 * @param dataFields
	 * @param cfgID
	 * @throws BizException
	 */
	public void savePreDataFields(List<DataFieldView> dataFields, String cfgID) throws BizException;
	
}
