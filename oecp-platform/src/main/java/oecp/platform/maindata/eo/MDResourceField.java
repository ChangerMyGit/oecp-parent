/************************* 版权声明 *********************************
 *                                                                  *
 *                     版权所有：百洋软件                          				 	*
 *          Copyright (c) 2011 by www.oecp.cn                	*
 *                                                                  *
 ************************* 变更记录 *********************************
 *
 * 创建者：宋黎晓   创建日期： 2011 6 13
 * 备注：
 * 
 * 修改者：       修改日期：
 * 备注：
 * 
 */

package oecp.platform.maindata.eo;

import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import oecp.framework.entity.base.StringPKEO;

/**
 * 主数据资源列
 * 
 * @author slx
 * @date 2011 6 13 13:52:27
 * @version 1.0
 */
@Entity
@Table(name = "OECP_SYS_MDRESOURCE_FIELD")
public class MDResourceField extends StringPKEO {
	private static final long serialVersionUID = 1L;
	/** 所属资源 **/
	private MDResource md;
	/** 字段名 **/
	private String name;
	/** 显示名称 **/
	private String dispName;
	/** 是否显示 **/
	private Boolean isDisplay;
	/** 相关资源（当前列对应的另一个主数据时填写，如：商品档案上的商品分类列，对应商品分类。） **/
	private MDResource relatedMD;
	/** 对应的UI类（如：公司选择窗口类） **/
	private String uiClass;

	@ManyToOne()
	public MDResource getMd() {
		return md;
	}

	public void setMd(MDResource md) {
		this.md = md;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getDispName() {
		return dispName;
	}

	public void setDispName(String dispName) {
		this.dispName = dispName;
	}

	@ManyToOne()
	public MDResource getRelatedMD() {
		return relatedMD;
	}

	public void setRelatedMD(MDResource relatedMD) {
		this.relatedMD = relatedMD;
	}

	public String getUiClass() {
		return uiClass;
	}

	public void setUiClass(String uiClass) {
		this.uiClass = uiClass;
	}

	public Boolean getIsDisplay() {
		return isDisplay;
	}

	public void setIsDisplay(Boolean isDisplay) {
		this.isDisplay = isDisplay;
	}

}
