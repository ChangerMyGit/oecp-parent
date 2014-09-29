/*
 * Copyright (c) 2011 OECP All Rights Reserved.                	
 * <a href="http://www.oecp.cn">http://www.oecp.cn</a>                                                                 
 */
package oecp.demo.uiviewtest.eo;

import java.util.Date;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import oecp.bcbase.eo.BaseBillEO;
import oecp.bcbase.eo.BaseBizBillEO;
import oecp.bcbase.eo.annotations.BillItems;
import oecp.platform.org.eo.Organization;
/**
 * 界面视图测试单据子表实体
 * @author 
 * @date 
 * @version 1.0
 */
@Entity
@Table(name="T_DEMO_UIVIEWTEST")
public class Uiviewtest extends BaseBizBillEO {

	private static final long serialVersionUID = 1L;
	/** 子实体列表 **/
	private List<UiviewtestDetail> details;
	/** 备注 **/
	private String note;
	/** 订单日期 **/
	private Date orderdate;
	/** 订购公司 **/
	private Organization organization;
	@BillItems
	@OneToMany(mappedBy="uiviewtest",cascade={CascadeType.ALL},orphanRemoval=true)
	public List<UiviewtestDetail> getDetails(){
		return details;
	}
	
	public void setDetails(List<UiviewtestDetail> details){
		this.details = details;
	}
	public String getNote(){
		return note;
	}
	
	public void setNote(String note){
		this.note = note;
	}
	public Date getOrderdate(){
		return orderdate;
	}
	
	public void setOrderdate(Date orderdate){
		this.orderdate = orderdate;
	}
	@ManyToOne()
	public Organization getOrganization(){
		return organization;
	}
	
	public void setOrganization(Organization organization){
		this.organization = organization;
	}
}
