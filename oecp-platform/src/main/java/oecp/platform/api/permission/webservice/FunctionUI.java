package oecp.platform.api.permission.webservice;

import java.io.Serializable;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlType;

/**
 * 功能页面
 * 
 * @date 2011 4 8 11:14:45
 * @version 1.0
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "FunctionUI")
public class FunctionUI implements Serializable {
	private static final long serialVersionUID = 1L;
	
	private String code;			// 界面编号
	private String title;			// 标题
	private String description;		// 描述
	private String sign;			// 用作权限过滤的唯一标识,例如B/S组件的URL
	private boolean defaultUI;		// 是否功能默认UI
	private UIElement[] elements;	// 界面元素
	
	public String getCode() {
		return code;
	}
	public void setCode(String code) {
		this.code = code;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
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
	public boolean isDefaultUI() {
		return defaultUI;
	}
	public void setDefaultUI(boolean defaultUI) {
		this.defaultUI = defaultUI;
	}
	public UIElement[] getElements() {
		return elements;
	}
	public void setElements(UIElement[] elements) {
		this.elements = elements;
	}
	
}
