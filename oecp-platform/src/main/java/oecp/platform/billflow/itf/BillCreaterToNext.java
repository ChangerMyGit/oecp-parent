/************************* 版权声明 *********************************
 *                                                                  *
 *                     版权所有：百洋软件                          				 	*
 *          Copyright (c) 2011 by www.oecp.cn                	*
 *                                                                  *
 ************************* 变更记录 *********************************
 *
 * 创建者：宋黎晓   创建日期： 2011-6-16
 * 备注：
 * 
 * 修改者：       修改日期：
 * 备注：
 * 
 */    

package oecp.platform.billflow.itf;

import oecp.bcbase.eo.BaseBizBillEO;

/**
 * 创建并保存下游单据的创建者类。
 * </br> 接收当前单据作为参数，创建下游单据并保存之。
 * </br> 当前单据状态为生效时，调用下游单据的创建者类，生成后置单据
 * @author slx
 * @date 2011-6-16 下午05:10:57
 * @version 1.0
 */
public interface BillCreaterToNext {

	/**
	 * 创建并保存下游单据
	 * @author slx
	 * @date 2011-6-16 下午05:13:28
	 * @modifyNote
	 * @param <T>
	 * @param billeo
	 */
	public <T extends BaseBizBillEO> void createBill(T billeo,String executeUser,String functionCode);
}
