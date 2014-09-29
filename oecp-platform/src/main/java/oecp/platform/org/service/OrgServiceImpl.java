package oecp.platform.org.service;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.annotation.Resource;

import oecp.framework.exception.BizException;
import oecp.platform.base.service.OECPValidator;
import oecp.platform.base.service.PlatformBaseServiceImpl;
import oecp.platform.bcinfo.eo.BizComponent;
import oecp.platform.event.service.EventEngine;
import oecp.platform.file.eo.UploadFile;
import oecp.platform.org.eo.OrgUseBC;
import oecp.platform.org.eo.Organization;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


/**
 * 组织机构服务实现
 * 
 * @author slx
 * @date 2011 4 12 08:58:44
 * @version 1.0
 */
@Service("orgService")
@Transactional
public class OrgServiceImpl extends PlatformBaseServiceImpl<Organization> implements OrgService {

	@Resource(name="orgValidator")
	private OECPValidator validator;
	@Autowired
	private EventEngine eventEngine;
	@Override
	public void save(Organization org) throws BizException {
		if(StringUtils.isEmpty(org.getId())){
			validator.validator("create", org ,getDao());
			org.setId(null);
			getDao().create(org);
		}else{
			validator.validator("update", org ,getDao());
			getDao().update(org);
		}
	}
	
	@Override
	public void delete(Serializable id) throws BizException {
		validator.validator("delete", id ,getDao());
		super.delete(id);
	}

	@Override
	public Organization getChildOrgs(String orgid) {
		Organization rootOrg = getDao().findByWhere(Organization.class, "o.id=?", new Object[]{orgid});
		loadChildOrgs(rootOrg);
		return rootOrg;
	}
	
	/**
	 * 获取某个组织下面所有的子组织，递归
	 * @author yangtao
	 * @date 2012-7-23下午03:04:06
	 * @param orgid
	 * @return
	 */
	public List<Organization> getAllChildOrgs(String orgid){
		List<Organization> list = new ArrayList<Organization>();
		Organization rootOrg = getDao().findByWhere(Organization.class, "o.id=?", new Object[]{orgid});
		list.add(rootOrg);
		loadAllChildOrgs(rootOrg,list);
		return list;
	}
	
	/**
	 * 获取某个组织下面所有的子组织，递归的字符串如：'ddd','dsss'
	 * @author yangtao
	 * @date 2012-7-23下午03:04:06
	 * @param orgid
	 * @return
	 */
	public String getAllChildOrgIds(String orgid){
		String result = "";
		List<Organization> list = getAllChildOrgs(orgid);
		for(Organization o : list){
			result += "'";
			result += o.getId();
			result += "'";
			result += ",";
		}
		if(result.endsWith(","))
			result = result.substring(0,result.length()-1);
		return result;
	}
	
	@Override
	public List<Organization> getTopOrgs() {
		List<Organization> orgs = getDao().queryByWhere(Organization.class, " o.parent IS NULL ORDER BY o.idx,o.code", null);
		loadChildOrgs(orgs);
		return orgs;
	}
	
	@Override
	public void orgStartUseBCs(Organization currentOrg,String orgid, List<String> bcids) throws BizException {
		
		if(bcids == null || orgid == null){
			throw new BizException("建账操作必须指明组织ID和业务组件ID!");
		}
		
		// 准备org和bc
		Organization org = null;
		BizComponent bc = null;
		int len = bcids.size();
		org = new Organization();
		org.setId(orgid);
		Date userDate = new Date();
		
		String vwhere = " o.org.id=? AND o.bc.id IN (";
		for (int i = 0; i < bcids.size(); i++) {
			
			if(i==0){
				vwhere = vwhere.concat("'"+bcids.get(i).toString()+"'");
			}else{
				vwhere = vwhere.concat(" ,").concat("'"+bcids.get(i).toString()+"'");
			}
		}
		vwhere = vwhere.concat(" )");
		boolean inused = getDao().isExistedByWhere(OrgUseBC.class, vwhere, new Object[]{orgid});
		if(inused){
			throw new BizException("业务组件已经建账，不允许重复建账！");
		}
		
		// 循环创建组织启用组件实体，并保存。
		for (int i = 0; i < len; i++) {
			bc = getDao().find(BizComponent.class, bcids.get(i));
			try {
				bc.getHost();
				bc.getServicePort();
				// TODO FIXME 公司建账！！！调用组件远程API初始化服务！！
				Thread.sleep(2000);
			} catch (InterruptedException e) {
				throw new BizException("组件建账出错：" + bc.getName() + "," + e.getMessage());
			}
			
			OrgUseBC orgUseBC = new OrgUseBC();
			orgUseBC.setOrg(org);
			orgUseBC.setBc(bc);
			orgUseBC.setUseDate(userDate);
			
			getDao().create(orgUseBC);
			eventEngine.fireEvent(orgUseBC, "startUseBCs", currentOrg);
		}
		//触发事件

	}
	
	@Override
	public List<OrgUseBC> getUsedBCs(String orgid) {
		return getDao().queryByWhere(OrgUseBC.class, " o.org.id=? ", new Object[]{orgid});
	}
	
	@Override
	public List<BizComponent> getNoUsedBCs(String orgid) {
		return getDao().queryByWhere(BizComponent.class, " o.id NOT IN (SELECT ob.bc.id FROM OrgUseBC ob WHERE ob.org.id=? )", new Object[]{orgid});
	}
	
	
	private void loadChildOrgs(List<Organization> orgs){
		if(orgs == null) return ;
		
		for (Organization org : orgs) {
			loadChildOrgs(org);
		}
	}
	
	/**
	 * 加载子组织<B>(递归方法)</B>
	 * @author slx
	 * @date 2011 4 12 10:24:21
	 * @modifyNote
	 * @param org
	 */
	private void loadChildOrgs(Organization org){
		List<Organization> orgs = org.getChildOrgs();
		//如果自组织列表为空,则表示没有下级,跳出递归
		if(orgs == null){
			return;
		}
		// 循环递归取子组织利用Hibernate延迟加载机制加载出数据
		for (Organization o : orgs) {
			loadChildOrgs(o);
		}
	}
	
	private void loadAllChildOrgs(Organization org,List<Organization> list){
		List<Organization> orgs = org.getChildOrgs();
		//如果自组织列表为空,则表示没有下级,跳出递归
		if(orgs == null){
			return;
		}else{
			// 循环递归取子组织利用Hibernate延迟加载机制加载出数据
			list.addAll(orgs);
			for (Organization o : orgs) {
				loadAllChildOrgs(o,list);
			}
		}
	}

	@Override
	public boolean getUserdBCs(String bcid) {
		Long count = getDao().getCountByWhere(BizComponent.class, " o.id  IN (SELECT ob.bc.id FROM OrgUseBC ob WHERE ob.bc.id=? )", new Object[]{bcid});
		if(count > 0 ){
			return true;
		}else{
			return false;
		}
	}
	
	@Override
	public boolean getUserBCs(String orgid, String bcid) {
		Long count = getDao().getCountByWhere(BizComponent.class, " o.id  IN (SELECT ob.bc.id FROM OrgUseBC ob WHERE ob.bc.id=? AND ob.org.id = ? )", new Object[]{bcid,orgid});
		if(count > 0 ){
			return true;
		}else{
			return false;
		}
	}
	
	
	/**
	 * 保存组织（logo图片）
	 * @author yangtao
	 * @date 2012-7-30上午10:38:23
	 * @param org
	 * @param files
	 */
	public void saveOrg(Organization org,List<UploadFile> files)throws BizException{
		if (StringUtils.isEmpty(org.getId())) {
//			if (files != null && files.size() > 0) {
//				OrganizationConfig organizationConfig = new OrganizationConfig();
//				organizationConfig.setLogoUrl(files.get(0).getFilepath());
//				this.getDao().create(organizationConfig);
//				org.setOrganizationConfig(organizationConfig);
//			}
			this.save(org);
		}else{
//			if(org.getOrganizationConfig()==null||StringUtils.isEmpty(org.getOrganizationConfig().getId())){
//				OrganizationConfig organizationConfig = new OrganizationConfig();
//				this.getDao().create(organizationConfig);
//				org.setOrganizationConfig(organizationConfig);
//			}
//			if (files != null && files.size() > 0) {
//				org.getOrganizationConfig().setLogoUrl(files.get(0).getFilepath());
//				this.getDao().update(org.getOrganizationConfig());
//			}
			this.update(org);
		}
		
	}

	public void setEventEngine(EventEngine eventEngine) {
		this.eventEngine = eventEngine;
	}

}
