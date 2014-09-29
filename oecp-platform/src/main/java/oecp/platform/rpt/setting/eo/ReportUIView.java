/**
 * oecp-platform - UIViewParser.java
 * copyright 2009-2012 OECP www.oecp.cn
 * 创建人:slx	创建时间:2011-11-7上午10:41:16		版本:v1
 * ============================================
 * 修改人：			修改时间:					版本:
 * 修改原因:
 *
 * ============================================
 */

package oecp.platform.rpt.setting.eo;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Transient;

import oecp.framework.entity.base.StringPKEO;
import oecp.framework.vo.base.DataVO;
import oecp.platform.uiview.eo.UIComponent;
import oecp.platform.uiview.vo.UIComponentVO;

/**
 * 报表视图EO
 * 
 * @author slx
 * @date 2012-4-24 上午11:11:12
 * @version 1.0
 */
@Entity
@Table(name = "OECP_SYS_RPT_VIEW")
public class ReportUIView extends StringPKEO implements DataVO {
	private static final long serialVersionUID = 1L;
	/** 报表视图编号 **/
	private String code;
	/** 报表视图标题 **/
	private String title;
	/** UI控件 **/
	private UIComponent mainui;
	/** UI控件VO **/
	@Transient
	private UIComponentVO mainuivo;

	@Column(length = 40, nullable = false)
	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	@Column(length = 40, nullable = false)
	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	@ManyToOne(cascade = { CascadeType.PERSIST, CascadeType.MERGE, CascadeType.REMOVE })
	public UIComponent getMainui() {
		return mainui;
	}

	public void setMainui(UIComponent mainui) {
		this.mainui = mainui;
	}
	@Transient
	public UIComponentVO getMainuivo() {
		return mainuivo;
	}

	public void setMainuivo(UIComponentVO mainuivo) {
		this.mainuivo = mainuivo;
	}

	@Transient
	private String[] voflieds = null;

	@Transient
	@Override
	public String[] getFieldNames() {
		if (voflieds == null) {
			voflieds = getAttributeNames();
			for (int i = 0; i < voflieds.length; i++) {
				if ("mainui".equals(voflieds[i])) {
					voflieds[i] = "mainuivo";
				}
			}
		}
		return voflieds;
	}

	@Transient
	@Override
	public Class<?> getFieldType(String attrname) {
		try {
			if("id".equals(attrname)){
				return String.class;
			}
			return getAttributeType(attrname);
		} catch (NoSuchFieldException e) {
			if("mainuivo".equals(attrname)){
				return UIComponentVO.class;
			}
			return String.class;
		}
	}

	@Transient
	@Override
	public void setValue(String fieldname, Object value) {
		if("mainuivo".equals(fieldname)){
			setMainuivo((UIComponentVO)value);
		}else{
			setAttributeValue(fieldname, value);
		}
	}

	@Transient
	@Override
	public Object getValue(String fieldname) {
		if("mainuivo".equals(fieldname)){
			return getMainuivo();
		}else{
			return getAttributeValue(fieldname);
		}
	}
}
