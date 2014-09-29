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

import org.springframework.stereotype.Service;

import oecp.framework.exception.BizException;
import oecp.platform.base.service.PlatformBaseServiceImpl;
import oecp.platform.billflow.eo.BillFlowConfig;
import oecp.platform.biztype.eo.BizType;

/**
 * 业务类型服务实现类
 * @author slx
 * @date 2011-6-20 下午03:50:57
 * @version 1.0
 */
@Service("bizTypeService")
public class BizTypeServiceImpl extends PlatformBaseServiceImpl<BizType> implements BizTypeService {

	@Override
	public List<BizType> getBizTypeByOrg(String orgID) {
		return getDao().queryByWhere(BizType.class, "o.org.id=? OR o.shared=true ", new Object[]{orgID});
	}
	
	@Override
	public void delete(Serializable id) throws BizException {
		boolean isexisted = getDao().isExistedByWhere(BillFlowConfig.class, " o.bizType.id=? ", new Object[]{id});
		if(isexisted){
			throw new BizException("业务类型下已经配置了单据信息，不能进行删除操作！");
		}
		super.delete(id);
	}
}
