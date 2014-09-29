/**
 * oecp-platform - BaseDetailBillEO.java
 * copyright 2009-2012 OECP www.oecp.cn
 * 创建人:luanyoubo	创建时间:下午2:22:30		版本:v1
 * ============================================
 * 修改人：			修改时间:					版本:
 * 修改原因:
 *
 * ============================================
 */

package oecp.bcbase.eo;

import java.util.List;

import javax.persistence.Transient;

import oecp.framework.entity.base.StringPKEO;

/** 
 *
 * @author luanyoubo  
 * @date 2014年1月22日 下午2:22:30 
 * @version 1.0
 *  
 */
public class BaseBizDetailBillEO extends StringPKEO {
	private static final long serialVersionUID = 3114639537132893280L;

	private String preItemID;
	
	/** 上游单据明细与当前单据明细对应关系 **/
	private List<String[]> relations;
	@Transient
	public List<String[]> getRelations() {
		return relations;
	}
	public void setRelations(List<String[]> relations) {
		this.relations = relations;
	}
	@Transient
	public String getPreItemID() {
		return preItemID;
	}
	public void setPreItemID(String preItemID) {
		this.preItemID = preItemID;
	}
}
