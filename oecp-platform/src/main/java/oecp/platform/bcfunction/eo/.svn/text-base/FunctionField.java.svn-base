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

package oecp.platform.bcfunction.eo;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.xml.bind.annotation.XmlType;

import oecp.framework.entity.base.StringPKEO;
import oecp.platform.maindata.eo.MDResource;

/**
 * </br>目前主要用于数据权限，以后可能会有其他用途时再做修改。
 * 
 * @author slx
 * @date 2011 6 13 11:24:45
 * @version 1.0
 */
@Entity
@Table(name = "OECP_SYS_FUNCTION_FIELD")
@XmlType(namespace = "http://www.oecp.cn")
public class FunctionField extends StringPKEO {

	private static final long serialVersionUID = 1L;
	/** 所属功能 **/
	private Function function;
	/** 字段名 **/
	private String name;
	/** 字段显示名 **/
	private String dispName;
	/** 字段类型 **/
	private String className;
	/** 对应的主数据资源类型 **/
	private MDResource mdType;

	@ManyToOne
	public Function getFunction() {
		return function;
	}

	public void setFunction(Function function) {
		this.function = function;
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
	@Column(length=100)
	public String getClassName() {
		return className;
	}

	public void setClassName(String className) {
		this.className = className;
	}

	@ManyToOne
	public MDResource getMdType() {
		return mdType;
	}

	public void setMdType(MDResource mdType) {
		this.mdType = mdType;
	}
}
