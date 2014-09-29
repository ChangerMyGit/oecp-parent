/**
 * oecp-platform - PortalConfigService.java
 * copyright 2009-2012 OECP www.oecp.cn
 * 创建人:songlixiao	创建时间:下午3:52:40		版本:v1
 * ============================================
 * 修改人：			修改时间:					版本:
 * 修改原因:
 *
 * ============================================
 */

package oecp.platform.portal.service;

import java.util.List;

import oecp.framework.service.BaseService;
import oecp.platform.portal.eo.PortalArea;
import oecp.platform.portal.eo.PortalConfig;
import oecp.platform.portal.eo.Portlet;
import oecp.platform.user.eo.User;

/**
 * 
 * @author songlixiao
 * @date 2014年3月19日 下午3:52:40
 * @version 1.0
 * 
 */
public interface PortalConfigService extends BaseService<PortalConfig> {

	/**
	 * 保存各人门户设置
	 * 
	 * @author songlixiao
	 * @date 2014年3月19日下午3:56:08
	 * @param owner
	 * @param areas
	 */
	public void saveMyPortalConfig(User owner, List<PortalArea> areas);

	/**
	 * 保存系统门户设置
	 * 
	 * @author songlixiao
	 * @date 2014年3月19日下午3:56:25
	 * @param areas
	 */
	public void saveSysPortalConfig(List<PortalArea> areas);

	/**
	 * 获取各人门户设置 如果没有找到各人的门户设置，则返回系统默认的门户设置
	 * 
	 * @author songlixiao
	 * @date 2014年3月19日下午3:56:37
	 * @param owner
	 * @return
	 */
	public PortalConfig getMyPortalConfig(User owner);

	/**
	 * 获取系统默认门户设置
	 * 
	 * @author songlixiao
	 * @date 2014年3月19日下午3:57:10
	 * @return
	 */
	public PortalConfig getSysPortalConfig();

	/**
	 * 获得所有可用的Portlet
	 * 
	 * @author songlixiao
	 * @date 2014年3月21日上午8:54:02
	 * @return
	 */
	public List<Portlet> getAllPortlets();

}
