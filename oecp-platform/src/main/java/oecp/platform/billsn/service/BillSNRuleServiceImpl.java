package oecp.platform.billsn.service;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import oecp.framework.dao.DAO;
import oecp.framework.exception.BizException;
import oecp.platform.billsn.eo.BillSNRuleEO;


/**
 * 单据号维护实体bean
 * 
 * @author slx
 * @date 2009-5-25 下午04:54:58
 * @version 1.0
 */
@Service("billSNRuleService")
public class BillSNRuleServiceImpl implements BillSNRuleService{
	@Resource(name="platformDao")
	private DAO dao;
	
	public void setDao(DAO dao) {
		this.dao = dao;
	}
	
	public void addNewSNRule(BillSNRuleEO rule) throws BizException{
		// 查询此单据类型的规则本公司是否已经存在，如存在则不准许新增。
		BillSNRuleEO queryrule = querySNRuleByBilltype(rule.getBilltype_id(),rule.getOrgid());
		if(queryrule!=null){
			throw new BizException("此单据类型的规则已经存在,不允许再次新增!");
		}
		
		dao.create(rule);
	}

	public void deleteSNRule(BillSNRuleEO rule) {
		dao.delete(rule.getClass(), rule.getId());
	}
	
	public void updateSNRule(BillSNRuleEO rule) {
		dao.update(rule);
	}

	public List<BillSNRuleEO> queryAllSNRule() {
		return dao.queryByWhere(BillSNRuleEO.class, null, null);
	}
	

	public BillSNRuleEO querySNRuleByBilltype(String billtype, String orgId) {
		// 获取单据号规则
		BillSNRuleEO rule = queryOrgSNRule(billtype, orgId);
		// 先查公司规则，没有的话再查集团规则
		if(rule == null){
			rule = queryGlobalSNRule(billtype);
		}
		// 集团没有使用默认规则
		if(rule == null){
			rule = queryDefaultSNRule();
		}
		return rule;
	}
	
	/**
	 * 查询当前公司的单据号规则
	 * @author slx
	 * @date 2009-5-25 下午05:33:07
	 * @modifyNote
	 * @param billtype
	 * @param orgId
	 * @return
	 */
	BillSNRuleEO queryOrgSNRule(String billtype, String orgId){
		// 获取单据号规则
		List<BillSNRuleEO> lt_rule = dao.queryByWhere(BillSNRuleEO.class, " orgId=? AND billtype_id=? ",new Object[]{orgId,billtype });
		if(lt_rule.size()==0)
			return null;
		else
			return lt_rule.get(0);
	}
	
	/**
	 * 查询集团的单据号规则
	 * @author slx
	 * @date 2009-5-25 下午05:33:07
	 * @modifyNote
	 * @param billtype
	 * @param orgId
	 * @return
	 */
	BillSNRuleEO queryGlobalSNRule(String billtype){
		// 获取单据号规则
		return queryOrgSNRule(billtype,"0");
	}
	
	/**
	 * 获取系统提供的默认的的单据号规则
	 * @author slx
	 * @date 2009-5-25 下午05:33:07
	 * @modifyNote
	 * @param billtype
	 * @param orgId
	 * @return
	 */
	BillSNRuleEO queryDefaultSNRule(){
		// 获取单据号规则
		return queryGlobalSNRule("defuletbilltype");
	}
}
