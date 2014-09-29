/************************* 版权声明 *********************************
 * 
 * 版权所有：百洋软件
 * Copyright (c) 2009 by Pearl Ocean.
 * 
 ************************* 变更记录 *********************************
 *
 * 创建者：slx   创建日期： 2009-5-14
 * 创建记录：创建类结构。
 * 
 * 修改者：       修改日期：
 * 修改记录：
 ************************* 随   笔 *********************************
 *
 * 这里可以写写感想，感慨，疑问什么的。
 * 
 ******************************************************************
 */    

package oecp.platform.api.billsn;


import oecp.framework.entity.base.BaseEO;

/**
 * 单据号获取服务
 * @author slx
 * @date 2009-5-14 下午03:05:43
 * @version 1.0
 */
public interface BillSNGetterService {

	/**
	 * 获取单据号
	 * @author slx
	 * @date 2009-6-30 下午04:46:30
	 * @modifyNote
	 * @param bizbean 单据主表对象(如果单据号上不需要单据某个属性作为号码的一部分,此参数可以为null)
	 * @param billType 在单据号规则中注册的单据类型
	 * @return 单据号
	 */
	public String getBillCode(BaseEO bizbean,String billType);
}
