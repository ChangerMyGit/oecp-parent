/**
 * oecp-platform - PortalConfigAction.java
 * copyright 2009-2012 OECP www.oecp.cn
 * 创建人:songlixiao	创建时间:下午4:13:58		版本:v1
 * ============================================
 * 修改人：			修改时间:					版本:
 * 修改原因:
 *
 * ============================================
 */

package oecp.platform.portal.web;

import java.util.List;

import javax.annotation.Resource;

import oecp.framework.util.FastJsonUtils;
import oecp.platform.portal.eo.PortalArea;
import oecp.platform.portal.eo.PortalConfig;
import oecp.platform.portal.eo.Portlet;
import oecp.platform.portal.service.PortalConfigService;
import oecp.platform.web.BasePlatformAction;

import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.Namespace;
import org.apache.struts2.convention.annotation.ParentPackage;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

/**
 * 
 * @author songlixiao
 * @date 2014年3月19日 下午4:13:58
 * @version 1.0
 * 
 */
@Controller
@Scope("prototype")
@ParentPackage("default-package")
@Namespace("/portalCfg")
public class PortalConfigAction extends BasePlatformAction {

	private static final long	serialVersionUID	= 1L;
	@Resource(name = "portalConfigService")
	private PortalConfigService	cfgService;
	/** protal内的区域设置 **/
	private List<PortalArea>	areas;

	/**
	 * 保存系统默认设置
	 * 
	 * @author songlixiao
	 * @date 2014年3月19日下午4:38:33
	 * @return
	 */
	@Action(value = "saveSysSetting")
	public String saveSysSetting() {
		cfgService.saveSysPortalConfig(areas);
		setJsonString("{success:true,msg:'保存成功！'}");
		return SUCCESS;
	}

	/**
	 * 保存各人门户设置
	 * 
	 * @author songlixiao
	 * @date 2014年3月19日下午4:21:13
	 * @return
	 */
	@Action(value = "saveMySetting")
	public String saveMySetting() {
		cfgService.saveMyPortalConfig(getOnlineUser().getUser(), areas);
		setJsonString("{success:true,msg:'保存成功！'}");
		return SUCCESS;
	}

	/**
	 * 加载各人门户设置
	 * 
	 * @author songlixiao
	 * @date 2014年3月19日下午4:21:23
	 * @return
	 */
	@Action(value = "loadMySetting")
	public String loadMySetting() {
		PortalConfig config = cfgService.getMyPortalConfig(getOnlineUser().getUser());
		String[] fields = new String[] { "areas", "columnWidth", "portletConfigs", "portlet", "id", "displayName", "xtype", "initParams",
				"jsFiles", "title", "height", "hideHeader", "hideBorder", "marginTop", "marginBottom", "marginLeft", "marginRight" };
		String json = FastJsonUtils.toJson(config, fields);
		setJsonString("{success:true,msg:'加载成功！',result:" + json + "}");
		return SUCCESS;
	}

	/**
	 * 加载目前所有可用的portlet
	 * 
	 * @author songlixiao
	 * @date 2014年3月21日上午9:01:41
	 * @return
	 */
	@Action(value = "loadAllPortlets")
	public String loadAllPortlets() {
		List<Portlet> plets = cfgService.getAllPortlets();
		String[] fields = new String[] { "id", "displayName", "xtype", "initParams", "jsFiles" };
		String json = FastJsonUtils.toJson(plets, fields);
		setJsonString("{success:true,msg:'加载成功！',result:" + json + "}");
		return SUCCESS;
	}

	/********************* ↓ getters & setters ***********************/
	public List<PortalArea> getAreas() {
		return areas;
	}

	public void setAreas(List<PortalArea> areas) {
		this.areas = areas;
	}

	public void setCfgService(PortalConfigService cfgService) {
		this.cfgService = cfgService;
	}
}
