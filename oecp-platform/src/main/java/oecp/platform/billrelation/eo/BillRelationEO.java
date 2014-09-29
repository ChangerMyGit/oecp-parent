/**
 * oecp-platform - BillRelationEO.java
 * copyright 2009-2012 OECP www.oecp.cn
 * 创建人:Administrator	创建时间:下午4:03:15		版本:v1
 * ============================================
 * 修改人：			修改时间:					版本:
 * 修改原因:
 *
 * ============================================
 */

package oecp.platform.billrelation.eo;

import oecp.framework.entity.base.StringPKEO;

/** 
 *
 * @author lyb  
 * @date 2014年1月21日 下午4:03:15 
 * @version 1.0
 *  
 */
public class BillRelationEO extends StringPKEO {
	private static final long serialVersionUID = 4488709716689063074L;

	/** 上游单据ID **/
	private String preBillID;
	/** 当前单据ID **/
	private String currentBillID;
	
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
}
