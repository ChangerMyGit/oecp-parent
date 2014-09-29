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

package oecp.platform.billflow.itf;

import oecp.bcbase.eo.BaseBillEO;
import oecp.bcbase.eo.BaseBizBillEO;

/**
 * 上有单据回写类接口
 * </br> 根据当前单据内容回写上游单据。
 * </br> 当前单据保存到DB、状态改变、删除时，向上游单据回写
 * @author slx
 * @date 2011-6-17 上午09:57:11
 * @version 1.0
 */
public interface BillPreWriteBacker {

	/**
	 * 接收单据vo回写上游单据。
	 * @author slx
	 * @date 2011-6-17 上午09:58:45
	 * @modifyNote
	 * @param <T>
	 * @param billeo
	 */
	public <T extends BaseBizBillEO> void writeBack(T billeo,String executeUser);
	
	public <T extends BaseBillEO> void onBillDelete(T bill,String executeUser);
}
