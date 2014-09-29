/**
 * oecp-platform - PortalArea.java
 * copyright 2009-2012 OECP www.oecp.cn
 * 创建人:songlixiao	创建时间:下午2:32:24		版本:v1
 * ============================================
 * 修改人：			修改时间:					版本:
 * 修改原因:
 *
 * ============================================
 */

package oecp.platform.portal.eo;

import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import oecp.framework.entity.base.StringPKEO;

/**
 * protal上的区域
 * 
 * @author songlixiao
 * @date 2014年3月19日 下午2:32:24
 * @version 1.0
 * 
 */
@Entity
@Table(name = "OECP_SYS_PORTAL_CFG_AREA")
public class PortalArea extends StringPKEO {

	private static final long	serialVersionUID	= 1L;
	/** 所归属的portal设置 **/
	private PortalConfig		portalConfig;
	/** 列宽度 **/
	private Double				columnWidth;
	/** 包含的portlet设置 **/
	private List<PortletConfig>	portletConfigs;

	@ManyToOne
	public PortalConfig getPortalConfig() {
		return portalConfig;
	}

	public void setPortalConfig(PortalConfig portalConfig) {
		this.portalConfig = portalConfig;
	}

	public Double getColumnWidth() {
		return columnWidth;
	}

	public void setColumnWidth(Double columnWidth) {
		this.columnWidth = columnWidth;
	}

	@OneToMany(mappedBy = "portalArea", cascade = { CascadeType.ALL }, orphanRemoval = true)
	public List<PortletConfig> getPortletConfigs() {
		return portletConfigs;
	}

	public void setPortletConfigs(List<PortletConfig> portletConfigs) {
		this.portletConfigs = portletConfigs;
	}

}
