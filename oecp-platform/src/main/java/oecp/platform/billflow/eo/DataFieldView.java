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

import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.Transient;

import oecp.framework.entity.base.StringPKEO;
import oecp.framework.entity.enums.DataType;

/**
 * 数据列视图
 * @author slx
 * @date 2011-6-16 下午03:21:12
 * @version 1.0
 */
@Entity
@Table(name="OECP_SYS_DATAFIELDVIEW")
public class DataFieldView extends StringPKEO {
	private static final long serialVersionUID = 1L;
	/** 列名 **/
	private String name;
	/** 显示名 **/
	private String dispName;
	/** 数据类型 **/
	private DataType dataType;
	/** 长度 **/
	private int maxlength;
	/** FIXME 此处绑定了EXT 前台EXT控件名 **/
	private String uiClass;
	/** 字段补充信息（小数填写小数位数，枚举或参照填写对应的UI控件的构造参数） **/
	private String supplement;
	/** 是否可编辑 **/
	private Boolean editable;
	/** 是否隐藏 **/
	private Boolean hidden;
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
	public DataType getDataType() {
		return dataType;
	}
	public void setDataType(DataType dataType) {
		this.dataType = dataType;
	}

	public Boolean getEditable() {
		return editable;
	}
	public void setEditable(Boolean editable) {
		this.editable = editable;
	}
	public Boolean getHidden() {
		return hidden;
	}
	public void setHidden(Boolean hidden) {
		this.hidden = hidden;
	}
	public int getMaxlength() {
		return maxlength;
	}
	public void setMaxlength(int maxlength) {
		this.maxlength = maxlength;
	}
	public String getSupplement() {
		return supplement;
	}
	public void setSupplement(String supplement) {
		this.supplement = supplement;
	}
	public String getUiClass() {
		return uiClass;
	}
	public void setUiClass(String uiClass) {
		this.uiClass = uiClass;
	}
	@Transient
	public String getDeafultUIClass(){
		String defaultUI = "textfield";
		if(DataType.DATE.equals(dataType)){
			defaultUI = "";
		}else if(DataType.INTEGER.equals(dataType)){
			defaultUI = "";
		}else if(DataType.DOUBLE.equals(dataType)){
			defaultUI = "";
		}else if(DataType.DATETIME.equals(dataType)){
			defaultUI = "";
		}else if(DataType.LONG.equals(dataType)){
			defaultUI = "";
		}else if(DataType.ENUM.equals(dataType)){
			defaultUI = "";
		}
		return defaultUI;
	}
}
