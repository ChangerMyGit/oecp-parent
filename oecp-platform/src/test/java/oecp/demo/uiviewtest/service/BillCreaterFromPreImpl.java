package oecp.demo.uiviewtest.service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import oecp.bcbase.enums.BillState;
import oecp.demo.uiviewtest.eo.Uiviewtest;
import oecp.demo.uiviewtest.eo.UiviewtestDetail;
import oecp.framework.vo.base.DataVO;
import oecp.platform.billflow.itf.BillCreaterFromPre;
import oecp.platform.biztype.eo.BizType;

/**
 * 
 *	当前单据创建器:根据上游单据生成当前单据，为临时数据，DB中没有
 * @author YangTao
 * @date 2012-1-5上午11:13:49
 * @version 1.0
 */
public class BillCreaterFromPreImpl implements BillCreaterFromPre {

	/**
	 * 注意：这个生成的新实体Uiviewtest必须要有
	 * 		bizType
	 * 		state
	 * 		createdate
	 * 		changedate
	 * 		creater
	 * 		changer
	 */
	@Override
	public List<Uiviewtest> createBill(List<DataVO> perDatas,
			String executeUser, BizType bizType) {
		System.out.println("1:enter into BillCreaterFromPreImpl");
		ArrayList<Uiviewtest> list = new ArrayList<Uiviewtest>();
		for(DataVO dv : perDatas){
			Uiviewtest u1 = new Uiviewtest();
			u1.setNote((String)dv.getValue("note"));
			u1.setPreBillID((String)dv.getValue("id"));
			
			/**begin*****must have**/
			u1.setBizType(bizType.getId());
			u1.setState(BillState.TEMPORARY);
			u1.setCreatedate(new Date());
			u1.setChangedate(new Date());
			u1.setCreater(executeUser);
			u1.setChanger(executeUser);
			/**end*****must have**/
			
			UiviewtestDetail ud1=new UiviewtestDetail();
			ud1.setNum(new Double(500));
			UiviewtestDetail ud2=new UiviewtestDetail();
			ud2.setNum(new Double(888));
			ArrayList<UiviewtestDetail> l2 = new ArrayList<UiviewtestDetail>();
			l2.add(ud1);l2.add(ud2);
			u1.setDetails(l2);
			list.add(u1);
		}
		
		return list;
	}
}
