package oecp.platform.api.billsn;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

import javax.annotation.Resource;

import oecp.framework.dao.DAO;
import oecp.framework.entity.base.BaseEO;
import oecp.platform.billsn.eo.BillLastSNEO;
import oecp.platform.billsn.eo.BillSNRuleEO;
import oecp.platform.billsn.service.BillSNRuleService;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


/**
 * 单据号获取服务实现类
 * @author slx
 *
 */
@Transactional
@Service("billSNGetterService")
public class BillSNGetterServiceImpl implements 
		BillSNGetterService {
	@Resource(name="billSNRuleService")
	private BillSNRuleService billSNRuleSevice ;
	public void setBillSNRuleSevice(BillSNRuleService billSNRuleSevice) {
		this.billSNRuleSevice = billSNRuleSevice;
	}
	@Resource(name="platformDao")
	private DAO dao;
	
	public void setDao(DAO dao) {
		this.dao = dao;
	}
	
	public synchronized String getBillCode(BaseEO bizbean,String billType) {

		// 获取当前单据的单据号规则
		BillSNRuleEO rule = getSNRule(billType,bizbean);
		// 根据规则拼接日期前缀
		String dateprefix = createDatePerfix(rule) ;
		// 获取当前单据的最大流水号
		BillLastSNEO lastsn = getLastSN(rule,bizbean,billType);
		// 组合标示符前缀、日期前缀和流水号
		String billcode = createSignPerfix(rule, bizbean).concat(dateprefix).concat(getSNString(rule.getSnLenth() ,lastsn.getSn()));
		
		return billcode;
	}
	
	/**
	 * 获取当前单据的当前规则的最大流水号，同时更新流水号表。
	 * <br> 如果规则设置了公司唯一，则根据单据上的公司为条件进行查询和更新流水号，否则根据规则所属的公司操作。
	 * <br> 如果规则设置了需要单据的某个属性作为前缀，则查询更新最大流水号时考虑规则中的字段值。否则忽略此条件。
	 * @author slx
	 * @date 2009-5-18 下午03:03:27
	 * @modifyNote
	 * @param rule
	 * @param bizbean
	 * @return
	 */
	protected BillLastSNEO getLastSN(BillSNRuleEO rule,BaseEO bizbean , String billType){
		String billtype = billType;
		String orgid = getOrgidFormEO(bizbean);
		String dateprefix = createDatePerfix(rule) ;
		String whereeql = null;
		String billAttributeValue = null;
		Object[] params = null;
		// 如果是公司唯一，则以单据上的公司id为准，否则取规则的公司id
		String snorg = rule.getOrgRule()?orgid:rule.getOrgid();
		// 判断是否需要属性字段标示符
		if(rule.getBillAttribute()){
			billAttributeValue = bizbean.getAttributeValue(rule.getBillAttributeName()).toString();
			whereeql = " orgid=? AND billtype=? AND prefix=? AND billAttributeValue=? ";
			params =  new Object[]{snorg,billtype ,dateprefix , billAttributeValue};
		}else{
			whereeql = " orgid=? AND billtype=? AND prefix=?";
			params =  new Object[]{snorg,billtype ,dateprefix };
		}
		 
		List<BillLastSNEO> ls_lastsn = dao.queryByWhere(BillLastSNEO.class, whereeql ,params);
		BillLastSNEO lastsn = null;
		// 有则 +1 更新回数据库，无则新建一个流水号对象存入数据库
		if(ls_lastsn.size()==0){
			lastsn = new BillLastSNEO();
			lastsn.setOrgid(snorg);
			lastsn.setBilltype(billtype);
			lastsn.setRule_id(rule.getId());
			lastsn.setPrefix(dateprefix);
			lastsn.setBillAttributeValue(billAttributeValue);
			lastsn.setSn(new Long(1));
			dao.create(lastsn);
		}else{
			lastsn = ls_lastsn.get(0);
			lastsn.setSn(lastsn.getSn()+1);
			dao.update(lastsn);
		}
		
		return lastsn;
	}
	
	/**
	 * 查询并的到当前单据的单据号规则。
	 * @author slx
	 * @date 2009-5-18 下午03:08:05
	 * @modifyNote
	 * @param billtype
	 * @return
	 */
	protected BillSNRuleEO getSNRule(String billtype,BaseEO bizbean){
		String orgid = getOrgidFormEO(bizbean);

		return billSNRuleSevice.querySNRuleByBilltype(billtype, orgid);
	}
	
	/**
	 * 从实体中得到公司id，如果没有得到，则默认为0
	 * @author slx
	 * @date 2011 6 13 17:54:19
	 * @modifyNote
	 * @param bizbean
	 * @return
	 */
	private String getOrgidFormEO(BaseEO bizbean){
		Object o_orgid = bizbean.getAttributeValue("orgid");
		return o_orgid==null?"0":o_orgid.toString();
	}
	

	/**
	 * 将sn转为字符串，长度不够的左边补零。
	 * @author slx
	 * @date 2009-5-15 下午01:33:58
	 * @modifyNote
	 * @param length
	 * @param sn
	 * @return
	 */
	protected String getSNString(int length , long sn){
		String str_SN = "" + sn;
		for(int i = str_SN.length() ; i < length ; i++ ){
			str_SN = "0" + str_SN ;
		}
		return str_SN;
	}
	
	/**
	 * 根据规则组合前缀标示的部分
	 * @author slx
	 * @date 2009-5-15 下午01:33:09
	 * @modifyNote
	 * @param rule
	 * @param bizbean
	 * @return
	 */
	protected String createSignPerfix(BillSNRuleEO rule , BaseEO bizbean){
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(new Date());
		
		StringBuffer sbf_prefix = new StringBuffer();
		if(rule.getOrg())
			sbf_prefix.append(rule.getOrgcode());
		if(rule.getBilltype())
			sbf_prefix.append(rule.getBilltype_prefix());
		// 如果设置了需要某个属性字段作为前缀的一部分，则取这个字段的值加入到单据号的前缀中。
		if(rule.getBillAttribute())
			sbf_prefix.append(bizbean.getAttributeValue(rule.getBillAttributeName().trim()));
	
		return sbf_prefix.toString();
	}
	
	/**
	 * 根据规则组合前缀中日期的部分
	 * @author slx
	 * @date 2009-5-15 下午01:33:09
	 * @modifyNote
	 * @param rule
	 * @return
	 */
	protected String createDatePerfix(BillSNRuleEO rule ){
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(new Date());
		
		StringBuffer sbf_prefix = new StringBuffer();
		if(rule.getYear())
			sbf_prefix.append(calendar.get(Calendar.YEAR));
		if (rule.getMonth()){
				String month = ("0" + (calendar.get(Calendar.MONTH)+1));
				sbf_prefix.append(month.substring(month.length() - 2));
		}
		if(rule.getDay()){
			String date = ("0" + calendar.get(Calendar.DATE));
			sbf_prefix.append(date.substring(date.length() - 2));
		}
		
		return sbf_prefix.toString();
	}

}
