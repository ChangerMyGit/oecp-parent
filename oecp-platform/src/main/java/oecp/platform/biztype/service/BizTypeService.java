/************************* 版权声明 *********************************
 *                                                                  *
 *                     版权所有：百洋软件                          				 	*
 *          Copyright (c) 2011 by www.oecp.cn                	*
 *                                                                  *
 ************************* 变更记录 *********************************
 *
 * 创建者：宋黎晓   创建日期： 2011-6-20
 * 备注：
 * 
 * 修改者：       修改日期：
 * 备注：
 * 
 */    

package oecp.platform.biztype.service;

import java.io.Serializable;
import java.util.List;

import oecp.framework.exception.BizException;
import oecp.framework.service.BaseService;
import oecp.platform.biztype.eo.BizType;

/**
 * 业务类型服务
 * @author slx
 * @date 2011-6-20 下午03:49:38
 * @version 1.0
 */
public interface BizTypeService extends BaseService<BizType> {

	/**
	 * 获得本公司的可用的业务类型
	 * @author slx
	 * @date 2011-6-20 下午03:16:06
	 * @modifyNote
	 * @param orgID
	 * @return
	 */
	public List<BizType> getBizTypeByOrg(String orgID);
	
	@Override
	public void delete(Serializable id) throws BizException;
}
