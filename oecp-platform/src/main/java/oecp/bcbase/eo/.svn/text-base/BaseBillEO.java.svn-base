package oecp.bcbase.eo;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.MappedSuperclass;

import oecp.bcbase.enums.BillState;
import oecp.framework.entity.base.StringPKEO;

/**
 * 业务单据（主表）EO父类
 * 
 * @author slx
 * @date 2011-12-26
 */
@MappedSuperclass
public abstract class BaseBillEO extends StringPKEO {
	private static final long serialVersionUID = 1L;
	/** 单据号 **/
	private String billsn;
	/** 制单人 **/
	private String creater;
	/** 制单时间 **/
	private Date createdate;
	/** 最后修改人 **/
	private String changer;
	/** 最后修改时间 **/
	private Date changedate;
	/** 审批状态 **/
	private BillState state;
	
	@Column(length=20)
	public String getBillsn() {
		return billsn;
	}
	public void setBillsn(String billsn) {
		this.billsn = billsn;
	}
	public String getCreater() {
		return creater;
	}
	public void setCreater(String creater) {
		this.creater = creater;
	}
	public Date getCreatedate() {
		return createdate;
	}
	public void setCreatedate(Date createdate) {
		this.createdate = createdate;
	}
	public String getChanger() {
		return changer;
	}
	public void setChanger(String changer) {
		this.changer = changer;
	}
	public Date getChangedate() {
		return changedate;
	}
	public void setChangedate(Date changedate) {
		this.changedate = changedate;
	}
	@Enumerated(EnumType.STRING)
	public BillState getState() {
		return state;
	}
	public void setState(BillState state) {
		this.state = state;
	}

}
