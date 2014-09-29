package oecp.platform.billflow.itf;


import oecp.bcbase.eo.BaseBizBillEO;
import oecp.framework.exception.BizException;

/**
 * 
 * 从上游数据创建单据的保存之前的校验
 * 从上游创建单据EO必须实现此接口，完成对EO保存数据时的校验。
 * 当前单据的临时数据保存到DB中，之前的校验
 * @author YangTao
 * @date 2011-11-7下午04:46:21
 * @version 1.0
 */
public interface BillCreaterFromPreCheck {
	/**
	 * 
	 * 从上游数据创建单据之后，用户进行修改，点击保存时的校验
	 * @author YangTao
	 * @date 2011-11-7下午04:51:15
	 * @param <T>
	 * @param billeo
	 * @throws BizException
	 */
	public <T extends BaseBizBillEO> void beforeSaveCheckBill(T billeo) throws BizException;
}
