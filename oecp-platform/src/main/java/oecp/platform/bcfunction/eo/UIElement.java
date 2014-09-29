package oecp.platform.bcfunction.eo;

import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.xml.bind.annotation.XmlType;

import oecp.framework.entity.base.StringPKEO;

/**
 * 界面元素实体
 * 
 * @author wangliang
 * @date 2011 4 11 11:31:00
 * @version 1.0
 */
@Entity
@Table(name = "OECP_SYS_UIELEMENT")
@XmlType(namespace = "http://www.oecp.cn")
public class UIElement extends StringPKEO {

	private static final long serialVersionUID = 1L;
	/** 在页面上的唯一标识id */
	private String elementId;
	/** 描述 */
	private String description;
	/** 功能界面 */
	private FunctionUI functionUI;
	/** 控制显示的属性的名称 */
	private String visibleParameterName;

	public String getElementId() {
		return elementId;
	}

	public void setElementId(String elementId) {
		this.elementId = elementId;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	@ManyToOne
	public FunctionUI getFunctionUI() {
		return functionUI;
	}

	public void setFunctionUI(FunctionUI functionUI) {
		this.functionUI = functionUI;
	}

	public String getVisibleParameterName() {
		return visibleParameterName;
	}

	public void setVisibleParameterName(String visibleParameterName) {
		this.visibleParameterName = visibleParameterName;
	}
}
