/**
 * 
 */
package oecp.platform.uiview.eo;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OrderBy;
import javax.persistence.Table;
import javax.persistence.Transient;

import oecp.framework.entity.base.StringPKEO;
import oecp.platform.uiview.enums.ComponentType;

/**
 * UI控件描述
 * 
 * @author slx
 * 
 */
@Entity
@Table(name = "OECP_SYS_UI_COMPONENT")
public class UIComponent extends StringPKEO {

	private static final long serialVersionUID = 1L;
	/** 标题 **/
	private String title;
	/** 控件类型 **/
	private ComponentType type;
	/** 当前控件布局列数 **/
	private Integer cols;
	/** 占用父控件列数 **/
	private Integer colspan;
	/** 占用父控件行数 **/
	private Integer rowspan;
	/** 排序顺序 **/
	private Integer idx;
	/** 宽度 **/
	private Integer width;
	/** 高度 **/
	private Integer height;
	/** 是否可提交 **/
	private Boolean cancommit;
	/** 是否隐藏 **/
	private Boolean hidden;
	/** 父控件 **/
	private UIComponent parent;
	/** 子控件 **/
	private List<UIComponent> items = new ArrayList<UIComponent>();
	/** 控件其他特殊属性 **/
	private List<SpecialAttribute> attrs = new ArrayList<SpecialAttribute>();

	@Transient
	public String getSpecialAttributeValue(String attrname) {
		for (SpecialAttribute attr : attrs) {
			if (attrname.equals(attr.getAttrname())) {
				return attr.getAttrvalue();
			}
		}
		return null;
	}

	@Column(length = 30)
	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	@Enumerated(EnumType.STRING)
	public ComponentType getType() {
		return type;
	}

	public void setType(ComponentType type) {
		this.type = type;
	}

	public Integer getCols() {
		return cols == null ? 1 : cols;
	}

	public void setCols(Integer cols) {
		this.cols = cols;
	}

	public Integer getColspan() {
		return colspan == null ? 1 : colspan;
	}

	public void setColspan(Integer colspan) {
		this.colspan = colspan;
	}

	public Integer getRowspan() {
		return rowspan == null ? 1 : rowspan;
	}

	public void setRowspan(Integer rowspan) {
		this.rowspan = rowspan;
	}

	public Integer getIdx() {
		return idx == null ? 999 : idx;
	}

	public void setIdx(Integer idx) {
		this.idx = idx;
	}

	public Integer getWidth() {
		return width;
	}

	public void setWidth(Integer width) {
		this.width = width;
	}

	public Integer getHeight() {
		return height;
	}

	public void setHeight(Integer height) {
		this.height = height;
	}

	public Boolean getCancommit() {
		return cancommit == null ? false : cancommit;
	}

	public void setCancommit(Boolean cancommit) {
		this.cancommit = cancommit;
	}

	public Boolean getHidden() {
		return hidden == null ? false : hidden;
	}

	public void setHidden(Boolean hidden) {
		this.hidden = hidden;
	}

	@ManyToOne()
	public UIComponent getParent() {
		return parent;
	}

	public void setParent(UIComponent parent) {
		this.parent = parent;
	}

	@OneToMany(mappedBy = "parent", cascade = { CascadeType.ALL }, orphanRemoval = true)
	@OrderBy("idx")
	public List<UIComponent> getItems() {
		return items;
	}

	public void setItems(List<UIComponent> items) {
		this.items = items;
	}

	@OneToMany(mappedBy = "comp", cascade = { CascadeType.ALL }, orphanRemoval = true)
	public List<SpecialAttribute> getAttrs() {
		return attrs;
	}

	public void setAttrs(List<SpecialAttribute> attrs) {
		this.attrs = attrs;
	}
}
