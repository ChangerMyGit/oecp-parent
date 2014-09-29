/**
 * 
 */
package oecp.platform.uiview.eo;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import oecp.framework.entity.base.StringPKEO;

/**
 * UI控件的特殊属性
 * @author slx
 *
 */
@Entity
@Table(name = "OECP_SYS_UI_SPECIALATTR")
public class SpecialAttribute extends StringPKEO {

	private static final long serialVersionUID = 1L;
	private UIComponent comp;
	private String attrname;
	private String attrvalue;
	@ManyToOne()
	public UIComponent getComp() {
		return comp;
	}
	public void setComp(UIComponent comp) {
		this.comp = comp;
	}
	@Column(length=30)
	public String getAttrname() {
		return attrname;
	}
	public void setAttrname(String attrname) {
		this.attrname = attrname;
	}
	@Column(length=1000)
	public String getAttrvalue() {
		return attrvalue;
	}
	public void setAttrvalue(String attrvalue) {
		this.attrvalue = attrvalue;
	}
}
