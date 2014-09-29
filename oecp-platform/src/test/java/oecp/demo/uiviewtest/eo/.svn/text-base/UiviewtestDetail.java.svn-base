/*
 * Copyright (c) 2011 OECP All Rights Reserved.                	
 * <a href="http://www.oecp.cn">http://www.oecp.cn</a>                                                                 
 */    
package oecp.demo.uiviewtest.eo;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.CascadeType;
import javax.persistence.FetchType;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OrderBy;
import javax.persistence.Transient;
import javax.persistence.Enumerated;
import javax.persistence.EnumType;
import javax.persistence.Column;
import javax.persistence.JoinColumn;

import oecp.bcbase.enums.BillState;
import oecp.bcbase.eo.annotations.BillMain;

import oecp.framework.entity.base.StringPKEO;
import oecp.platform.user.eo.User;

import java.util.Date;
import oecp.platform.bcinfo.eo.BizComponent;
import oecp.demo.uiviewtest.eo.Unit;
/**
 * 界面视图测试单据子表实体
 * @author 
 * @date 
 * @version 1.0
 */
@Entity
@Table(name="T_DEMO_UIVIEWTEST_DETAIL")
public class UiviewtestDetail extends StringPKEO {

	private static final long serialVersionUID = 1L;
	/** 主实体 **/
	private Uiviewtest uiviewtest;
	/** 订购组件 **/
	private BizComponent bc;
	/** 是否需要验货 **/
	private Boolean needcheck;
	/** 需求数量 **/
	private Double num;
	/** 需求日期 **/
	private Date needdate;
	/** 其他要求 **/
	private String otherdis;
	/** 计量单位 **/
	private Unit numunit;
	@ManyToOne()
	@BillMain
	public Uiviewtest getUiviewtest(){
		return uiviewtest;
	}
	
	public void setUiviewtest(Uiviewtest uiviewtest){
		this.uiviewtest = uiviewtest;
	}
	@ManyToOne()
	public BizComponent getBc(){
		return bc;
	}
	
	public void setBc(BizComponent bc){
		this.bc = bc;
	}
	public Boolean getNeedcheck(){
		return needcheck;
	}
	
	public void setNeedcheck(Boolean needcheck){
		this.needcheck = needcheck;
	}
	public Double getNum(){
		return num;
	}
	
	public void setNum(Double num){
		this.num = num;
	}
	public Date getNeeddate(){
		return needdate;
	}
	
	public void setNeeddate(Date needdate){
		this.needdate = needdate;
	}
	public String getOtherdis(){
		return otherdis;
	}
	
	public void setOtherdis(String otherdis){
		this.otherdis = otherdis;
	}
	public Unit getNumunit(){
		return numunit;
	}
	
	public void setNumunit(Unit numunit){
		this.numunit = numunit;
	}
}
