package oecp.platform.billsn.eo;

import javax.persistence.Entity;
import javax.persistence.Table;

import oecp.framework.entity.base.StringPKEO;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
@Entity
@Table(name = "OECP_SYS_BILLLASTSN")
@Cache(usage=CacheConcurrencyStrategy.READ_WRITE)
public class BillLastSNEO extends StringPKEO{

	private static final long serialVersionUID = 733147055780183052L;
	
	private String billtype;
	
	private String billAttributeValue;

	private String prefix;
	
	private String rule_id;
	
	private Long sn;
	
	private String orgid;

	public String getOrgid() {
		return orgid;
	}

	public void setOrgid(String orgid) {
		this.orgid = orgid;
	}

	public String getBilltype() {
		return billtype;
	}

	public void setBilltype(String billtype) {
		this.billtype = billtype;
	}

	public String getPrefix() {
		return prefix;
	}

	public void setPrefix(String prefix) {
		this.prefix = prefix;
	}

	public String getRule_id() {
		return rule_id;
	}

	public void setRule_id(String rule_id) {
		this.rule_id = rule_id;
	}

	public Long getSn() {
		return sn;
	}

	public void setSn(Long sn) {
		this.sn = sn;
	}

	public String getBillAttributeValue() {
		return billAttributeValue;
	}

	public void setBillAttributeValue(String billAttributeValue) {
		this.billAttributeValue = billAttributeValue;
	}
	
}
