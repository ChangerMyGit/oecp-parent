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

import java.util.List;

import oecp.bcbase.eo.BaseBizBillEO;
import oecp.framework.vo.base.DataVO;
import oecp.platform.biztype.eo.BizType;

/**
 * 从上游数据创建单据的创建类接口
 * </br> 创建单据EO的创建者类实现此接口，接收数据数组创建单据实体然后返回。
 * </br> 根据上游单据生成当前单据，为临时数据，DB中没有
 * </br>
 * @author slx
 * @date 2011-6-16 下午04:32:03
 * @version 1.0
 */
public interface BillCreaterFromPre {

	public  List<? extends BaseBizBillEO> createBill(List<DataVO> perDatas,String executeUser,BizType bizType);
}
