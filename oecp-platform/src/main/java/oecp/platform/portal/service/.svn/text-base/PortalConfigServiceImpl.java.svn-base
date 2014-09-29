/**
 * oecp-platform - PortalConfigServiceImpl.java
 * copyright 2009-2012 OECP www.oecp.cn
 * 创建人:songlixiao	创建时间:下午3:59:09		版本:v1
 * ============================================
 * 修改人：			修改时间:					版本:
 * 修改原因:
 *
 * ============================================
 */

package oecp.platform.portal.service;

import java.util.List;

import oecp.platform.base.service.PlatformBaseServiceImpl;
import oecp.platform.portal.eo.PortalArea;
import oecp.platform.portal.eo.PortalConfig;
import oecp.platform.portal.eo.Portlet;
import oecp.platform.portal.eo.PortletConfig;
import oecp.platform.user.eo.User;

import org.springframework.stereotype.Service;

/**
 * 
 * @author songlixiao
 * @date 2014年3月19日 下午3:59:09
 * @version 1.0
 * 
 */
@Service("portalConfigService")
public class PortalConfigServiceImpl extends PlatformBaseServiceImpl<PortalConfig> implements PortalConfigService {

	@Override
	public void saveMyPortalConfig(User owner, List<PortalArea> areas) {
		PortalConfig config = getDao().findByWhere(PortalConfig.class, " o.owner=? ", new Object[] { owner });
		if (config == null) {
			config = new PortalConfig();
			config.setOwner(owner);
			config.setSysDefault(false);
			config.setAreas(areas);
			fixParent(config);
			getDao().create(config);
		} else {
			updateAreasInConfig(config,areas);
			fixParent(config);
			getDao().update(config);
		}
	}

	@Override
	public void saveSysPortalConfig(List<PortalArea> areas) {
		PortalConfig config = getDao().findByWhere(PortalConfig.class, " o.sysDefault=? ", new Object[] { true });
		if (config == null) {
			config = new PortalConfig();
			config.setSysDefault(true);
			config.setAreas(areas);
			fixParent(config);
			getDao().create(config);
		} else {
			updateAreasInConfig(config,areas);
			fixParent(config);
			getDao().update(config);
		}
	}
	
	@Override
	public List<Portlet> getAllPortlets(){
		List<Portlet> plets = getDao().queryByWhere(Portlet.class, " 1=1 ", null);
		return plets;
	}
	
	
	/**
	 * 更新config中的区域area信息。
	 * 从数据库中查询出的config内的areas为pBag形式，不能直接用setAreas来替换掉，必须clear后逐个添加，否则hibernate保存时会出错。
	 * @author songlixiao
	 * @date 2014年3月21日上午8:45:37
	 * @param config
	 * @param areas
	 */
	private void updateAreasInConfig(PortalConfig config ,List<PortalArea> areas){
		config.getAreas().clear();
		config.getAreas().addAll(areas);
	}

	/**
	 * 添加子实体内对上级实体的引用，以便保存主外键关系
	 * 
	 * @author songlixiao
	 * @date 2014年3月20日下午2:42:28
	 * @param config
	 */
	private void fixParent(PortalConfig config) {
		List<PortalArea> areas = config.getAreas();
		if (areas != null) {
			for (PortalArea area : areas) {
				area.setPortalConfig(config);
				List<PortletConfig> plets = area.getPortletConfigs();
				if (plets != null) {
					for (PortletConfig plet : plets) {
						plet.setPortalArea(area);
					}
				}
			}
		}
	}

	@Override
	public PortalConfig getMyPortalConfig(User owner) {
		PortalConfig config = getDao().findByWhere(PortalConfig.class, " o.owner=? ", new Object[] { owner });
		if (config == null) {
			return getSysPortalConfig();
		} else {
			loadLazyAttributes(config);
			return config;
		}
	}

	@Override
	public PortalConfig getSysPortalConfig() {
		PortalConfig config = getDao().findByWhere(PortalConfig.class, " o.sysDefault=? ", new Object[] { true });
		loadLazyAttributes(config);
		return config;
	}

	/**
	 * 加载所有懒加载属性（eo自带的只能加载一层）
	 * 
	 * @author songlixiao
	 * @date 2014年3月19日下午4:07:40
	 * @param config
	 */
	private void loadLazyAttributes(PortalConfig config) {
		int len_area = config.getAreas().size();
		if (len_area > 0) {
			List<PortalArea> areas = config.getAreas();
			for (PortalArea area : areas) {
				area.loadLazyAttributes();
			}
		}
	}

}
