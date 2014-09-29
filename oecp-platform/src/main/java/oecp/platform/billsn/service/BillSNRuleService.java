package oecp.platform.billsn.service;

import java.util.List;

import oecp.framework.exception.BizException;
import oecp.platform.billsn.eo.BillSNRuleEO;

/**
 * 单据号规则维护接口
 * 
 * @author slx
 * @date 2009-5-25 下午04:54:01
 * @version 1.0
 */
public interface BillSNRuleService {


	/**
	 * 添加新的单据号规则
	 * @author slx
	 * @date 2009-5-25 下午04:20:56
	 * @modifyNote
	 * @param rule
	 */
	public void addNewSNRule(BillSNRuleEO rule) throws BizException;
	
	/**
	 * 更新单据号规则
	 * @author slx
	 * @date 2009-5-25 下午04:21:11
	 * @modifyNote
	 * @param rule
	 */
	public void updateSNRule(BillSNRuleEO rule);
	
	/**
	 * 删除单据号规则
	 * @author slx
	 * @date 2009-5-25 下午04:21:27
	 * @modifyNote
	 * @param rule
	 */
	public void deleteSNRule(BillSNRuleEO rule);
	
	/**
	 * 查询所有的单据号规则
	 * @author slx
	 * @date 2009-5-25 下午04:22:07
	 * @modifyNote
	 * @return
	 */
	public List<BillSNRuleEO> queryAllSNRule();
	
	/**
	 * <br> 先查询当前公司、当前单据类型的单据号规则。
	 * <br> 如果没有则查询集团当前单据类型的规则。
	 * <br> 仍然没有则返回默认单据号规则。
	 * @author slx
	 * @date 2009-5-25 下午04:24:08
	 * @modifyNote
	 * @param billtype
	 * @return
	 */
	public BillSNRuleEO querySNRuleByBilltype(String billtype , String corpId);
	
}
