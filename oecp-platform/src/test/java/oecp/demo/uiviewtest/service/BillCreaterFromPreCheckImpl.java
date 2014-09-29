package oecp.demo.uiviewtest.service;

import oecp.bcbase.eo.BaseBizBillEO;
import oecp.framework.exception.BizException;
import oecp.platform.billflow.itf.BillCreaterFromPreCheck;
/**
 * 
 *	当前单据保存校验:当前单据的临时数据保存到DB中，之前的校验
 * @author YangTao
 * @date 2012-1-5上午11:13:49
 * @version 1.0
 */
public class BillCreaterFromPreCheckImpl implements BillCreaterFromPreCheck {

	@Override
	public <T extends BaseBizBillEO> void beforeSaveCheckBill(T billeo)
			throws BizException {
		// TODO Auto-generated method stub
		System.out.println("2:enter into BillCreaterFromPreCheckImpl");
	}

	

}
