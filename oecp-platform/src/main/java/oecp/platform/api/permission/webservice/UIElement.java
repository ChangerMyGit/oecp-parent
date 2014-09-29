package oecp.platform.api.permission.webservice;

import java.io.Serializable;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlType;

/**
 * 界面元素
 * 
 * @date 2011 4 8 11:29:04
 * @version 1.0
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "UIElement")
public class UIElement implements Serializable {
	private static final long serialVersionUID = -5775114426771042032L;

	private String id;						// 在页面上的唯一标示id
	private String description;				// 描述
	private String visibleParameterName;	// 控制显示的属性的名称
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public String getVisibleParameterName() {
		return visibleParameterName;
	}
	public void setVisibleParameterName(String visibleParameterName) {
		this.visibleParameterName = visibleParameterName;
	}
}
