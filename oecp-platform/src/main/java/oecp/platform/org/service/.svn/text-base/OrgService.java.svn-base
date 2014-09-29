package oecp.platform.org.service;

import java.util.List;

import oecp.framework.exception.BizException;
import oecp.framework.service.BaseService;
import oecp.platform.bcinfo.eo.BizComponent;
import oecp.platform.file.eo.UploadFile;
import oecp.platform.org.eo.OrgUseBC;
import oecp.platform.org.eo.Organization;

/**
 * 组织机构服务接口
 * 
 * @author slx
 * @date 2011 4 11 15:32:59
 * @version 1.0
 */
public interface OrgService extends BaseService<Organization> {
	
	/**
	 * 得到指定组织以及子组织
	 * TODO 需要配置缓存(定时的可配置是否开启的缓存.因为上线后不太会变化,实施过程中不应该开启缓存)
	 * @author slx
	 * @date 2011 4 11 15:26:03
	 * @modifyNote
	 * @param orgid
	 * 		公司主键
	 * @return
	 * 		包含子组织的当前组织
	 */
	public Organization getChildOrgs(String orgid);
	
	/**
	 * 获取某个组织下面所有的子组织，递归
	 * @author yangtao
	 * @date 2012-7-23下午03:04:06
	 * @param orgid
	 * @return
	 */
	public List<Organization> getAllChildOrgs(String orgid);
	
	/**
	 * 获取某个组织下面所有的子组织，递归的字符串如：'ddd','dsss'
	 * @author yangtao
	 * @date 2012-7-23下午03:04:06
	 * @param orgid
	 * @return
	 */
	public String getAllChildOrgIds(String orgid);
	
	/**
	 * 得到顶级公司，返回列表中只有顶级公司，但顶级公司内包含子公司。
	 * @author slx
	 * @date 2011 5 12 13:57:22
	 * @modifyNote
	 * @return
	 */
	public List<Organization> getTopOrgs();
	
	/**
	 * 组织启用业务组件
	 * @author slx
	 * @date 2011 5 20 17:27:49
	 * @modifyNote
	 * @param orgid
	 * @param bcids
	 */
	public void orgStartUseBCs(Organization currentOrg,String orgid ,List<String> bcids) throws BizException ;
	
	/**
	 * 获得一个组织启用的业务组件。
	 * @author slx
	 * @date 2011 5 23 09:05:47
	 * @modifyNote
	 * @param orgid
	 * 		组织id
	 * @return
	 * 		业务组件启用记录列表
	 */
	public List<OrgUseBC> getUsedBCs(String orgid);
	
	/**
	 * 获得一个组织未启用的业务组件。
	 * @author slx
	 * @date 2011 5 23 09:06:18
	 * @modifyNote
	 * @param orgid
	 * 		组织id
	 * @return
	 * 		未启用的业务组件列表
	 */
	public List<BizComponent> getNoUsedBCs(String orgid);
	/**
	 * 
	* 查询该组件是否被组织启用
	* @author lintao
	* @date 2011-8-17上午11:23:27
	* @param bcid
	* 		组件id
	* @return true         false
	*               启用          未启用
	 */
	public boolean getUserdBCs(String bcid);
	
	/**
	 * 保存组织（logo图片）
	 * @author yangtao
	 * @date 2012-7-30上午10:38:23
	 * @param org
	 * @param files
	 */
	public void saveOrg(Organization org,List<UploadFile> files)throws BizException;
	
	/**
	 * 查询该组件是否被指定组织启用
	 * @author luanyoubo
	 * @date 2014年1月13日下午1:52:56
	 * @param orgid
	 * @param bcid
	 * @return true 启用  false 不启用
	 */
	public boolean getUserBCs(String orgid,String bcid);
	
}
