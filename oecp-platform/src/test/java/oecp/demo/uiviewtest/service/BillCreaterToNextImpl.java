package oecp.demo.uiviewtest.service;

import oecp.bcbase.eo.BaseBizBillEO;
import oecp.platform.billflow.itf.BillCreaterToNext;

/**
 * 
 * 后置单据生成器:当前单据状态为生效时，调用后置单据生成器，生成后置单据
 * @author YangTao
 * @date 2012-1-5上午11:13:49
 * @version 1.0
 */
public class BillCreaterToNextImpl implements BillCreaterToNext {

	@Override
	public <T extends BaseBizBillEO> void createBill(T billeo, String executeUser,String functionCode) {
		System.out.println("4:enter into BillCreaterToNextImpl");
	}



}
