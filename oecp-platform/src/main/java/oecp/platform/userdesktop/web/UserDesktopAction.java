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

package oecp.platform.userdesktop.web;

import java.io.File;
import java.util.List;

import javax.annotation.Resource;

import oecp.framework.exception.BizException;
import oecp.framework.util.FastJsonUtils;
import oecp.platform.userdesktop.eo.UserDesktop;
import oecp.platform.userdesktop.service.UserDesktopService;
import oecp.platform.web.BasePlatformAction;

import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.Namespace;
import org.apache.struts2.convention.annotation.ParentPackage;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

/** 
 * 桌面快捷方式action
 * @author slx  
 * @date 2012-6-12 下午3:59:50 
 * @version 1.0
 *  
 */
@Controller
@Scope("prototype")
@ParentPackage("default-package")
@Namespace("/desktop")
public class UserDesktopAction extends BasePlatformAction {
	private static final long serialVersionUID = 1L;

	private String funcId;
	
	private String alias;
	private String ico;
	private Integer idx;
	@Resource(name="userDesktopService")
	private UserDesktopService service;
	
	@Action(value = "addtodesktop")
	public String addDesktopFunc() throws BizException{
		service.addDesktopFunc(getOnlineUser().getUser(), funcId);
		setJsonString("{success:true,msg:'桌面快捷添加成功.'}");
		return SUCCESS;
	}

	@Action(value = "deldesktop")
	public String deleteDesktopFunc() throws BizException{
		service.deleteDesktopFunc(getOnlineUser().getUser(), funcId);
		setJsonString("{success:true,msg:'桌面快捷删除成功.'}");
		return SUCCESS;
	}
	
	@Action(value = "updatedesktop")
	public String updateDesktopFunc() throws BizException{
		service.updateDeskTopFuncName(getOnlineUser().getUser(), funcId, alias,ico,idx);
		setJsonString("{success:true,msg:'桌面快捷重命名成功.'}");
		return SUCCESS;
	}
	
	@Action(value = "list")
	public String listFuncs(){
		List<UserDesktop> uds = service.getDesktopFuncs(getOnlineUser().getUser(), getOnlineUser().getLoginedOrg());
		setJsonString("{success:true,msg:'加载桌面快捷成功.',data:"+ FastJsonUtils.toJson(uds, new String[]{"id","alias","ico","idx","name","function","code"}) +"}");
		return SUCCESS;
	}
	
	@Action(value = "icons")
	public String scanicons(){
		String s_root = getSession().getServletContext().getRealPath("/");
		File f_root = new File(s_root.concat("images\\desktopicons\\"));
		String[] icons = f_root.list();
		StringBuffer sf_json = new StringBuffer("{success:true,msg:'快捷菜单图片加载完成.',urls:[");
		for (int i = 0; i < icons.length; i++) {
			if(i!=0){
				sf_json.append(",");
			}
			sf_json.append("{url:'images/desktopicons/").append(icons[i]).append("'}");
		}
		sf_json.append("]}");
		setJsonString(sf_json.toString());
		return SUCCESS;
	}
	
	public String getFuncId() {
		return funcId;
	}

	public void setFuncId(String funcId) {
		this.funcId = funcId;
	}

	public String getAlias() {
		return alias;
	}

	public void setAlias(String alias) {
		this.alias = alias;
	}

	public void setService(UserDesktopService service) {
		this.service = service;
	}

	public String getIco() {
		return ico;
	}

	public void setIco(String ico) {
		this.ico = ico;
	}

	public Integer getIdx() {
		return idx;
	}

	public void setIdx(Integer idx) {
		this.idx = idx;
	}
}
