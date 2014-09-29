/**
 * oecp-platform - BaseDetailBillEO.java
 * copyright 2009-2012 OECP www.oecp.cn
 * 创建人:Administrator	创建时间:下午3:00:01		版本:v1
 * ============================================
 * 修改人：			修改时间:					版本:
 * 修改原因:
 *
 * ============================================
 */

package oecp.platform.billrelation.eo;

import javax.persistence.MappedSuperclass;

import oecp.framework.entity.base.StringPKEO;

/** 
 *
 * 业务单据明细基类 
 * @author luanyoubo  
 * @date 2014年1月21日 下午3:00:01 
 * @version 1.0
 *  
 */
@MappedSuperclass
public class DetailBillRelationEO extends StringPKEO {
	private static final long serialVersionUID = 3114639537132893280L;

	/** 上游单据ID **/
	private String preBillID;
	/** 当前单据ID **/
	private String currentBillID;
	/** 上游单据明细ID **/
	private String preBillDetailID;
	/** 上游单据明细数量 **/
	private Long preBillDetailQty;
	/** 当前单据明细ID **/
	private String currentBillDetailID;
	/** 当前单据明细数量 **/
	private Long currentBillDetailQty;

	public String getPreBillID() {
		return preBillID;
	}

	public void setPreBillID(String preBillID) {
		this.preBillID = preBillID;
	}

	public String getCurrentBillID() {
		return currentBillID;
	}

	public void setCurrentBillID(String currentBillID) {
		this.currentBillID = currentBillID;
	}

	public String getPreBillDetailID() {
		return preBillDetailID;
	}

	public void setPreBillDetailID(String preBillDetailID) {
		this.preBillDetailID = preBillDetailID;
	}

	public Long getPreBillDetailQty() {
		return preBillDetailQty;
	}

	public void setPreBillDetailQty(Long preBillDetailQty) {
		this.preBillDetailQty = preBillDetailQty;
	}

	public String getCurrentBillDetailID() {
		return currentBillDetailID;
	}

	public void setCurrentBillDetailID(String currentBillDetailID) {
		this.currentBillDetailID = currentBillDetailID;
	}

	public Long getCurrentBillDetailQty() {
		return currentBillDetailQty;
	}

	public void setCurrentBillDetailQty(Long currentBillDetailQty) {
		this.currentBillDetailQty = currentBillDetailQty;
	}

}
