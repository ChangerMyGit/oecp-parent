/*
 * Copyright (c) 2011 OECP All Rights Reserved.                	
 * <a href="http://www.oecp.cn">OECP</a> 
*/                                                                
package oecp.demo.uiviewtest.service;

import oecp.bcbase.service.BizBaseServiceImpl;
import oecp.demo.uiviewtest.eo.Uiviewtest;
import oecp.framework.exception.BizException;
import oecp.platform.api.bpm.ExecutionResult;
import oecp.platform.bpm.bizservice.BizServiceForBpm;
import oecp.platform.org.eo.Organization;
import oecp.platform.user.eo.User;


/** 
 * 单据服务实现类
 * @author 
 * @date 
 * @version 1.0
 *  
 */
//@Service("uiviewtestServices")
public class UiviewtestServicesImpl extends BizBaseServiceImpl<Uiviewtest> implements UiviewtestServices,BizServiceForBpm {

	@Override
	protected void validateBill(Uiviewtest bill) {
	}

	@Override
	protected void beforeBillSave(Uiviewtest bill) {
	}

	@Override
	protected void afterBillSave(Uiviewtest bill) {
	}

	@Override
	protected String getBillInfoString(Uiviewtest bill) {
		return bill.getBillsn();
	}

	@Override
	protected void beforeCommit(Uiviewtest bill, User operator, Organization org) throws BizException {
	}
	@Override
	protected void afterCommit(Uiviewtest bill, User operator, Organization org, ExecutionResult exeresult) throws BizException {
	}

	@Override
	protected void beforeDelete(Uiviewtest bill, User operator) {
	}
	
}
