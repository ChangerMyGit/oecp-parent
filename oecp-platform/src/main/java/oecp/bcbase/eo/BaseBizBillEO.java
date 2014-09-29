/**
 * oecp-platform - BaseBizBillEO.java
 * copyright 2009-2012 OECP www.oecp.cn
 * 创建人:slx	创建时间:2011-12-28下午4:03:42		版本:v1
 * ============================================
 * 修改人：			修改时间:					版本:
 * 修改原因:
 *
 * ============================================
 */
package oecp.bcbase.eo;

import java.util.List;

import javax.persistence.MappedSuperclass;
import javax.persistence.Transient;


/**
 * 参与业务流的单据EO基类
 * @author slx
 * @date 2011-12-28
 */
@MappedSuperclass
public abstract class BaseBizBillEO extends BaseBillEO {
	private static final long serialVersionUID = 1L;
	/** 业务类型 **/
	private String bizType;
	/**上游单据ID**/
	private String preBillID;
	/**下游单据ID**/
	private String nextBillID;
	
	/** 上游单据与当前单据对应关系 **/
	private List<String> relations;
	
	public String getBizType() {
		return bizType;
	}
	public void setBizType(String bizType) {
		this.bizType = bizType;
	}
	public String getPreBillID() {
		return preBillID;
	}
	public void setPreBillID(String preBillID) {
		this.preBillID = preBillID;
	}
	public String getNextBillID() {
		return nextBillID;
	}
	public void setNextBillID(String nextBillID) {
		this.nextBillID = nextBillID;
	}
	@Transient
	public List<String> getRelations() {
		return relations;
	}
	public void setRelations(List<String> relations) {
		this.relations = relations;
	}
}
