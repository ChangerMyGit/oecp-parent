package oecp.platform.api.permission.webservice;

import java.io.Serializable;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlType;

/**
 * 功能
 * 
 * @date 2011 4 8 10:21:06
 * @version 1.0
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "Function")
public class Function implements Serializable {

	private static final long serialVersionUID = 1L;
	
	private String code;				// 功能编号
	private String name;				// 功能名称
	private String description;			// 功能描述
	private boolean runable;			// 是否可运行
	private boolean wfused;				// 是否启用了审批流
	private String wfDataXSD;			// 提供给流程引擎的数据结构描述XSD
	private String parentCode;			// 上级功能编码
	private FunctionUI[] uis;			// 包含的功能界面
	private DataField[] fields;			// 可用来控制数据权限的字段
	public String getCode() {
		return code;
	}
	public void setCode(String code) {
		this.code = code;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public boolean isRunable() {
		return runable;
	}
	public void setRunable(boolean runable) {
		this.runable = runable;
	}
	public boolean isWfused() {
		return wfused;
	}
	public void setWfused(boolean wfused) {
		this.wfused = wfused;
	}
	public String getWfDataXSD() {
		return wfDataXSD;
	}
	public void setWfDataXSD(String wfDataXSD) {
		this.wfDataXSD = wfDataXSD;
	}
	public String getParentCode() {
		return parentCode;
	}
	public void setParentCode(String parentCode) {
		this.parentCode = parentCode;
	}
	public FunctionUI[] getUis() {
		return uis;
	}
	public void setUis(FunctionUI[] uis) {
		this.uis = uis;
	}
	public DataField[] getFields() {
		return fields;
	}
	public void setFields(DataField[] fields) {
		this.fields = fields;
	}
}
