/************************* 版权声明 *********************************
 *                                                                  *
 *                     版权所有：百洋软件                          				 	*
 *          Copyright (c) 2011 by www.oecp.cn                	*
 *                                                                  *
 ************************* 变更记录 *********************************
 *
 * 创建者：宋黎晓   创建日期： 2011-6-16
 * 备注：
 * 
 * 修改者：       修改日期：
 * 备注：
 * 
 */    

package oecp.platform.billflow.eo;

import java.util.List;



import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.Lob;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Transient;

import oecp.framework.dao.QLType;
import oecp.framework.entity.base.StringPKEO;
import oecp.platform.bcfunction.eo.Function;
import oecp.platform.billflow.itf.BillCreaterFromPre;
import oecp.platform.billflow.itf.BillCreaterFromPreCheck;
import oecp.platform.billflow.itf.BillCreaterToNext;
import oecp.platform.billflow.itf.BillPreWriteBacker;
import oecp.platform.biztype.eo.BizType;

import org.apache.commons.lang.StringUtils;

/**
 * 单据流设置
 * @author slx
 * @date 2011-6-16 上午11:11:23
 * @version 1.0
 */
@Entity
@Table(name="OECP_SYS_BILLFLOWCONFIG")
public class BillFlowConfig extends StringPKEO {

	private static final long serialVersionUID = 1L;
	/** 业务类型 **/
	private BizType bizType;
	/** 功能编号 **/
	private Function function;
	/** 是否可手工制单 **/
	private Boolean byHand;
	/** 是否可业务制单 **/
	private Boolean byBussiness;
	/** 查询语句类型 **/
	private QLType qlType;
	/** FIXME 此处绑定了EXT 查询对话框类（EXT类） **/
	private String queryDialog;
	/** 查询前置单据使用的dao名称，2013-11-29由于分离组件数据库添加 **/
	private String daobeanname;
	/** 查询上游数据的查询语句 **/
	private String preQuerySQL;
	/** 描述说明 **/
	private String description;
	/** 数据列显示视图 **/
	private List<DataFieldView> preDatafields;
	/** 当前单据的创建类的类名（该类必须实现BillCreaterFromPre接口） **/
	private String billCreaterFromPre;
	/** 当前单据的创建类的保存时的校验 **/
	private String billCreaterFromPreCheck;
	/** 下游单据创建类的类名（该类必须实现BillCreaterToNext接口） **/
	private String billCreaterToNext;
	/** 上游单据回写类（该类必须实现BillPreWriteBacker接口） **/
	private String billPreWriteBacker;
	/**上游单据功能**/
	private Function preBillFunction;
	/**下游单据功能**/
	private Function nextBillFunction;
	/**
	 * 获得从前置数据生成表单的创建者类实例
	 * @author slx
	 * @date 2011-6-17 下午03:30:06
	 * @modifyNote
	 * @return
	 * @throws ClassNotFoundException
	 * @throws InstantiationException
	 * @throws IllegalAccessException
	 */
	@Transient
	public BillCreaterFromPre getClassBillCreaterFromPre() throws ClassNotFoundException, InstantiationException, IllegalAccessException{
		return (BillCreaterFromPre)newInstance(getBillCreaterFromPre());
	}
	/**
	 * 获得生成后置单据的创建者实例
	 * @author slx
	 * @date 2011-6-17 下午03:31:54
	 * @modifyNote
	 * @return
	 * @throws ClassNotFoundException
	 * @throws InstantiationException
	 * @throws IllegalAccessException
	 */
	@Transient
	public BillCreaterToNext getClassBillCreaterToNext() throws ClassNotFoundException, InstantiationException, IllegalAccessException{
		return (BillCreaterToNext)newInstance(getBillCreaterToNext());
	}
	/**
	 * 获得上游单据回写类实例
	 * @author slx
	 * @date 2011-6-17 下午03:32:16
	 * @modifyNote
	 * @return
	 * @throws ClassNotFoundException
	 * @throws InstantiationException
	 * @throws IllegalAccessException
	 */
	@Transient
	public BillPreWriteBacker getClassBillPreWriteBacker() throws ClassNotFoundException, InstantiationException, IllegalAccessException{
		return (BillPreWriteBacker)newInstance(getBillPreWriteBacker());
	}
	
	/**
	 * 获得上游单据保存之前的校验接口
	 * 
	 * @author YangTao
	 * @date 2011-11-7下午04:55:01
	 * @return
	 * @throws InstantiationException
	 * @throws IllegalAccessException
	 * @throws ClassNotFoundException
	 */
	@Transient
	public BillCreaterFromPreCheck getClassBillCreaterFromPreCheck()throws InstantiationException, IllegalAccessException, ClassNotFoundException{
		return (BillCreaterFromPreCheck)newInstance(getBillCreaterFromPreCheck());
	}
	/**
	 * 生成类实例
	 * @author slx
	 * @date 2011-6-17 下午03:32:59
	 * @modifyNote
	 * @param className
	 * @return
	 * @throws InstantiationException
	 * @throws IllegalAccessException
	 * @throws ClassNotFoundException
	 */
	private Object newInstance(String className) throws InstantiationException, IllegalAccessException, ClassNotFoundException{
		if(StringUtils.isEmpty(className)){
			return null;
		}else{
			return Class.forName(className).newInstance();
		}
	}
	
	@ManyToOne
	public BizType getBizType() {
		return bizType;
	}
	public void setBizType(BizType bizType) {
		this.bizType = bizType;
	}
	@ManyToOne
	public Function getFunction() {
		return function;
	}
	public void setFunction(Function function) {
		this.function = function;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public Boolean getByHand() {
		return byHand;
	}
	public void setByHand(Boolean byHand) {
		this.byHand = byHand;
	}
	public QLType getQlType() {
		return qlType;
	}
	public void setQlType(QLType qlType) {
		this.qlType = qlType;
	}
	public String getDaobeanname() {
		return daobeanname;
	}
	public void setDaobeanname(String daobeanname) {
		this.daobeanname = daobeanname;
	}
	@Lob
	public String getQueryDialog() {
		return queryDialog;
	}
	public void setQueryDialog(String queryDialog) {
		this.queryDialog = queryDialog;
	}
	@Column(length=2000)
	public String getPreQuerySQL() {
		return preQuerySQL;
	}
	public void setPreQuerySQL(String preQuerySQL) {
		this.preQuerySQL = preQuerySQL;
	}
	@OneToMany(cascade = {CascadeType.MERGE,CascadeType.PERSIST,CascadeType.REMOVE})
	@JoinTable(name="OECP_SYS_BILLFLOWCONFIG_FIELD",
			joinColumns={@JoinColumn(name="billflowconfig_pk")},
			inverseJoinColumns={@JoinColumn(name="field_pk")})
	public List<DataFieldView> getPreDatafields() {
		return preDatafields;
	}
	public void setPreDatafields(List<DataFieldView> preDatafields) {
		this.preDatafields = preDatafields;
	}
	public String getBillCreaterFromPre() {
		return billCreaterFromPre;
	}
	public void setBillCreaterFromPre(String billCreaterFromPre) {
		this.billCreaterFromPre = billCreaterFromPre;
	}
	public String getBillCreaterToNext() {
		return billCreaterToNext;
	}
	public void setBillCreaterToNext(String billCreaterToNext) {
		this.billCreaterToNext = billCreaterToNext;
	}
	public String getBillPreWriteBacker() {
		return billPreWriteBacker;
	}
	public void setBillPreWriteBacker(String billPreWriteBacker) {
		this.billPreWriteBacker = billPreWriteBacker;
	}
	public String getBillCreaterFromPreCheck() {
		return billCreaterFromPreCheck;
	}
	public void setBillCreaterFromPreCheck(String billCreaterFromPreCheck) {
		this.billCreaterFromPreCheck = billCreaterFromPreCheck;
	}
	public Boolean getByBussiness() {
		return byBussiness;
	}
	public void setByBussiness(Boolean byBussiness) {
		this.byBussiness = byBussiness;
	}
	@ManyToOne
	public Function getPreBillFunction() {
		return preBillFunction;
	}
	public void setPreBillFunction(Function preBillFunction) {
		this.preBillFunction = preBillFunction;
	}
	@ManyToOne
	public Function getNextBillFunction() {
		return nextBillFunction;
	}
	public void setNextBillFunction(Function nextBillFunction) {
		this.nextBillFunction = nextBillFunction;
	}
	
	
}
