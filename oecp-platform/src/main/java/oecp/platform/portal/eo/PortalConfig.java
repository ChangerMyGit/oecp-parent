/**
 * oecp-platform - PortalConfig.java
 * copyright 2009-2012 OECP www.oecp.cn
 * 创建人:songlixiao	创建时间:下午2:27:12		版本:v1
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
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import oecp.framework.entity.base.StringPKEO;
import oecp.platform.user.eo.User;

/**
 * 门户（桌面）设置EO
 * 
 * @author songlixiao
 * @date 2014年3月19日 下午2:27:12
 * @version 1.0
 * 
 */
@Entity
@Table(name = "OECP_SYS_PORTAL_CFG")
public class PortalConfig extends StringPKEO {

	private static final long	serialVersionUID	= 1L;
	/** 是否系统默认设置 **/
	private boolean				sysDefault;
	/** 门户设置的所有者 **/
	private User				owner;
	/** 门户划分的几个区域 **/
	private List<PortalArea>	areas;

	public boolean isSysDefault() {
		return sysDefault;
	}

	public void setSysDefault(boolean sysDefault) {
		this.sysDefault = sysDefault;
	}

	@OneToOne
	public User getOwner() {
		return owner;
	}

	public void setOwner(User owner) {
		this.owner = owner;
	}

	@OneToMany(mappedBy = "portalConfig", cascade = { CascadeType.ALL }, orphanRemoval = true)
	public List<PortalArea> getAreas() {
		return areas;
	}

	public void setAreas(List<PortalArea> areas) {
		this.areas = areas;
	}
}
