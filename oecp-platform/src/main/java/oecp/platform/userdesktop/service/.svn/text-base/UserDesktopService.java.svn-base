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

import oecp.framework.exception.BizException;
import oecp.framework.service.BaseService;
import oecp.platform.bcfunction.eo.Function;
import oecp.platform.org.eo.Organization;
import oecp.platform.user.eo.User;
import oecp.platform.userdesktop.eo.UserDesktop;

/** 
 * 用户桌面服务类
 * @author slx  
 * @date 2012-6-12 下午3:14:16 
 * @version 1.0
 *  
 */
public interface UserDesktopService extends BaseService<UserDesktop> {
	/**
	 * 添加用户桌面快捷方式
	 * @author slx
	 * @date 2012-6-12下午3:24:59
	 * @param user
	 * @param functionId
	 */
	public void addDesktopFunc(User user , String functionId) throws BizException;
	
	/**
	 * 添加用户桌面快捷方式
	 * @author slx
	 * @date 2012-6-12下午3:24:59
	 * @param user
	 * @param functionId
	 */
	public void addDesktopFunc(User user , String functionId,int order) throws BizException;
	
	/**
	 * 删除桌面快捷
	 * @author slx
	 * @date 2012-6-12下午3:27:27
	 * @param user
	 * @param functionId
	 */
	public void deleteDesktopFunc(User user,String functionId) throws BizException;
	
	/**
	 * 修改别名
	 * @author slx
	 * @date 2012-6-12下午3:29:40
	 * @param user
	 * @param functionId
	 * @param alias
	 */
	public void updateDeskTopFuncName(User user,String functionId,String alias,String ico,Integer idx) throws BizException;
	/**
	 * 根据用户查询桌面设置
	 * @author slx
	 * @date 2012-6-12下午3:25:19
	 * @param userId
	 * @return
	 */
	public List<UserDesktop> getDesktopFuncs(User user,Organization org);
}
