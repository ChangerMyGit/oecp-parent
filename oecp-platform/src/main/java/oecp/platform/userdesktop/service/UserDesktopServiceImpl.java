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

package oecp.platform.userdesktop.service;

import java.util.List;

import javax.annotation.Resource;

import oecp.framework.exception.BizException;
import oecp.platform.base.service.PlatformBaseServiceImpl;
import oecp.platform.bcfunction.eo.Function;
import oecp.platform.org.eo.Organization;
import oecp.platform.permission.eo.Permission;
import oecp.platform.permission.service.PermissionService;
import oecp.platform.user.eo.User;
import oecp.platform.userdesktop.eo.UserDesktop;

import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Service;

/** 
 * 用户桌面服务类
 * @author slx  
 * @date 2012-6-12 下午3:31:26 
 * @version 1.0
 *  
 */
@Service(value="userDesktopService")
public class UserDesktopServiceImpl extends PlatformBaseServiceImpl<UserDesktop> implements UserDesktopService {
	
	@Resource(name="permissionService")
	private PermissionService prmService;
	
	@Override
	public void addDesktopFunc(User user, String functionId) throws BizException{
		UserDesktop ud = getDao().findByWhere(UserDesktop.class, "o.user=? AND o.function.id=?", new Object[]{user,functionId});
		if(ud == null){
			ud = new UserDesktop();
			ud.setUser(user);
			Function fun = new Function();
			fun.setId(functionId);
			ud.setFunction(fun);
			getDao().create(ud);
		}else{
			throw new BizException("桌面快捷已经存在，请不要重复添加！");
		}
	}
	

	public void addDesktopFunc(User user , String functionId,int order) throws BizException{
		UserDesktop ud = getDao().findByWhere(UserDesktop.class, "o.user=? AND o.function.id=?", new Object[]{user,functionId});
		if(ud == null){
			ud = new UserDesktop();
			ud.setUser(user);
			Function fun = new Function();
			fun.setId(functionId);
			ud.setFunction(fun);
			ud.setIdx(order);
			getDao().create(ud);
		}else{
			throw new BizException("桌面快捷已经存在，请不要重复添加！");
		}
	}

	@Override
	public void deleteDesktopFunc(User user, String functionId) throws BizException{
		UserDesktop ud = getDao().findByWhere(UserDesktop.class, "o.user=? AND o.function.id=?", new Object[]{user,functionId});
		if(ud != null){
			getDao().getHibernateSession().delete(ud);
		}else{
			throw new BizException("桌面快捷不存在，不能删除！");
		}
	}

	@Override
	public void updateDeskTopFuncName(User user, String functionId, String alias,String ico,Integer idx) throws BizException {
		UserDesktop ud = getDao().findByWhere(UserDesktop.class, "o.user=? AND o.function.id=?", new Object[]{user,functionId});
		if(ud != null){
			ud.setAlias(alias);
			ud.setIco(ico);
			ud.setIdx(idx);
			getDao().update(ud);
		}else{
			throw new BizException("桌面快捷不存在，不能修改别名！");
		}
	}

	@Override
	public List<UserDesktop> getDesktopFuncs(User user,Organization org) {
		List<UserDesktop> uds = getDao().queryByWhere(UserDesktop.class, " o.user=? ORDER BY o.idx",  new Object[]{user});
		List<Permission> prms = prmService.getUserPermission(user.getId(), org.getId());
		return desktopInPermission(uds,prms);
	}

	/**
	 * 过滤快捷方式列表，只留下在当前用户在本公司内有权限的快捷方式。
	 * @author slx
	 * @date 2012-6-13下午1:39:38
	 * @param uds
	 * @param prms
	 * @return
	 */
	private List<UserDesktop> desktopInPermission(List<UserDesktop> uds,List<Permission> prms){
		if(uds==null){
			return uds;
		}
		for (int i = 0; i < uds.size(); i++) {
			UserDesktop ud = uds.get(i);
			Function udfunc =  ud.getFunction();
			boolean inprms = false; // 是否在权限内
			for (int j = 0; j < prms.size(); j++) {
				Function prfunc = prms.get(j).getFunction();
				if(udfunc.equalsPK(prfunc)){
					inprms = true;
					if(StringUtils.isEmpty(ud.getAlias())){ // 无别名的默认用功能名称
						ud.setAlias(udfunc.getName());
					}
					break;
				}
			}
			if(!inprms){ // 经过循环遍历 不在权限内的需要移除
				uds.remove(i);
				i -- ;
			}
		}
		return uds;
	}
	
	public void setPrmService(PermissionService prmService) {
		this.prmService = prmService;
	}
}
