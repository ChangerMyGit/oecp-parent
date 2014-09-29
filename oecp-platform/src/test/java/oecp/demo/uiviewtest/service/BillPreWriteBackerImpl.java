package oecp.demo.uiviewtest.service;

import oecp.bcbase.eo.BaseBillEO;
import oecp.bcbase.eo.BaseBizBillEO;
import oecp.platform.billflow.itf.BillPreWriteBacker;

/**
 * 
 * 上游单据回写器:当前单据保存到DB、状态改变、删除时，向上游单据回写
 * @author YangTao
 * @date 2012-1-5上午11:13:49
 * @version 1.0
 */
public class BillPreWriteBackerImpl implements BillPreWriteBacker {

	@Override
	public <T extends BaseBizBillEO> void writeBack(T billeo, String executeUser) {
		System.out.println("3:enter into BillPreWriteBackerImpl writeBack");
	}

	@Override
	public <T extends BaseBillEO> void onBillDelete(T bill, String executeUser) {
		System.out.println("3:enter into BillPreWriteBackerImpl onBillDelete");
	}



}
