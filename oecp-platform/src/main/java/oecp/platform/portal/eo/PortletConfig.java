/**
 * oecp-platform - PortletConfig.java
 * copyright 2009-2012 OECP www.oecp.cn
 * 创建人:songlixiao	创建时间:下午2:36:23		版本:v1
 * ============================================
 * 修改人：			修改时间:					版本:
 * 修改原因:
 *
 * ============================================
 */

package oecp.platform.portal.eo;

import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import oecp.framework.entity.base.StringPKEO;

/**
 * 门户小窗设置信息
 * 
 * @author songlixiao
 * @date 2014年3月19日 下午2:36:23
 * @version 1.0
 * 
 */
@Entity
@Table(name = "OECP_SYS_PORTAL_CFG_PORTLET")
public class PortletConfig extends StringPKEO {

	private static final long	serialVersionUID	= 1L;
	/** portlet所属区域 **/
	private PortalArea			portalArea;
	/** portlet小窗体 **/
	private Portlet				portlet;
	/** 标题 **/
	private String				title;
	/** 高度 **/
	private Double				height;
	/** 是否隐藏头部 **/
	private Boolean				hideHeader;
	/** 是否隐藏边框 **/
	private Boolean				hideBorder;
	/** 上边距 **/
	private Double				marginTop;
	/** 下边距 **/
	private Double				marginBottom;
	/** 左边距 **/
	private Double				marginLeft;
	/** 右边距 **/
	private Double				marginRight;

	@ManyToOne
	public PortalArea getPortalArea() {
		return portalArea;
	}

	public void setPortalArea(PortalArea portalArea) {
		this.portalArea = portalArea;
	}

	@OneToOne
	public Portlet getPortlet() {
		return portlet;
	}

	public void setPortlet(Portlet portlet) {
		this.portlet = portlet;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public Double getHeight() {
		return height;
	}

	public void setHeight(Double height) {
		this.height = height;
	}

	public Boolean getHideHeader() {
		return hideHeader;
	}

	public void setHideHeader(Boolean hideHeader) {
		this.hideHeader = hideHeader;
	}

	public Boolean getHideBorder() {
		return hideBorder;
	}

	public void setHideBorder(Boolean hideBorder) {
		this.hideBorder = hideBorder;
	}

	public Double getMarginTop() {
		return marginTop;
	}

	public void setMarginTop(Double marginTop) {
		this.marginTop = marginTop;
	}

	public Double getMarginBottom() {
		return marginBottom;
	}

	public void setMarginBottom(Double marginBottom) {
		this.marginBottom = marginBottom;
	}

	public Double getMarginLeft() {
		return marginLeft;
	}

	public void setMarginLeft(Double marginLeft) {
		this.marginLeft = marginLeft;
	}

	public Double getMarginRight() {
		return marginRight;
	}

	public void setMarginRight(Double marginRight) {
		this.marginRight = marginRight;
	}
}
