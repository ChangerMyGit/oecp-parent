package oecp.platform.bcfunction.eo;

import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.xml.bind.annotation.XmlType;

import oecp.framework.entity.base.StringPKEO;

/**
 * 功能界面实体类
 * 
 * @author wangliang
 * @date 2011 4 11 11:31:00
 * @version 1.0
 */
@Entity
@Table(name = "OECP_SYS_FUNCTIONUI")
@XmlType(namespace = "http://www.oecp.cn")
public class FunctionUI extends StringPKEO {

	private static final long serialVersionUID = 1L;
	/** 功能编号 */
	private String code;
	/** 功能名称 */
	private String name;
	/** 功能描述 */
	private String description;
	/** 界面标识 (用作权限过滤的唯一标识,例如B/S组件的URL) */
	private String sign;
	/** 默认界面 */
	private Boolean isDefault;
	/** 所属功能 */
	private Function function;
	/** 界面元素 */
	private List<UIElement> elements;
	/**是否是流程默认页面*/
	private Boolean isDefaultForProcess;

	@ManyToOne
	public Function getFunction() {
		return function;
	}

	public void setFunction(Function function) {
		this.function = function;
	}

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

	public String getSign() {
		return sign;
	}

	public void setSign(String sign) {
		this.sign = sign;
	}

	public Boolean getIsDefault() {
		return isDefault;
	}

	public void setIsDefault(Boolean isDefault) {
		this.isDefault = isDefault;
	}

	@OneToMany(mappedBy = "functionUI", cascade = {CascadeType.MERGE,CascadeType.PERSIST, CascadeType.REMOVE },orphanRemoval=true)
	public List<UIElement> getElements() {
		return elements;
	}

	public void setElements(List<UIElement> elements) {
		this.elements = elements;
	}

	/**
	 * @return the isDefaultForProcess
	 */
	public Boolean getIsDefaultForProcess() {
		return isDefaultForProcess;
	}

	/**
	 * @param isDefaultForProcess the isDefaultForProcess to set
	 */
	public void setIsDefaultForProcess(Boolean isDefaultForProcess) {
		this.isDefaultForProcess = isDefaultForProcess;
	}

	
}
