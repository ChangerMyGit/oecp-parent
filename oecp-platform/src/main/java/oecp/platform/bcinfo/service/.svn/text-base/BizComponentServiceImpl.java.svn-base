package oecp.platform.bcinfo.service;

import java.io.Serializable;
import java.util.List;

import javax.annotation.Resource;

import oecp.acbase.service.ArchivesBaseServiceImpl;
import oecp.framework.exception.BizException;
import oecp.framework.util.OECPBeanUtils;
import oecp.framework.util.soap.SoapUtils;
import oecp.platform.bcdepend.service.BizDependService;
import oecp.platform.bcevent.service.EventService;
import oecp.platform.bcfunction.service.BcFunctionService;
import oecp.platform.bcinfo.eo.BizComponent;
import oecp.platform.event.service.EventEngine;
import oecp.platform.org.service.OrgService;
import oecp.platform.user.eo.User;
import oecp.platform.web.WebContext;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * 
 * 业务组件服务类
 * @author lintao  
 * @date 2011-8-22 下午03:00:24 
 * @version 1.0
 *
 */
@Service("bizComponentService")
public class BizComponentServiceImpl extends ArchivesBaseServiceImpl<BizComponent> implements
		BizComponentService {
	
	@Resource
	private BizDependService bizDependService;
	@Resource
	private BizServiceService bizServiceService;
	@Resource
	private EventService eventService;
	@Resource
	private BcFunctionService bcFunctionService;
	
	@Resource
	private OrgService orgService;
	
	@Resource(name="eventEngine")
	private EventEngine eventEngine;

	@Override
	public List<BizComponent> findAll() {
		List<BizComponent> list = getDao().queryByWhere(BizComponent.class, null, null);
		return list;
	}

	@Override
	@Transactional
	public void saveComponentInfo(String id) throws BizException {
		BizComponent bc = find(id);
		String wsUrl = bc.getWebServiceDomainUrl()+"/BCRegister?wsdl";
		Object[] res = null;
		try {
			res = SoapUtils.callService(wsUrl, "getCompInfo", new Object());
			StringBuffer str = new StringBuffer();
			str.append("bcIp->host,").append("bcPort->servicePort,").append("bcHTTPPort->webPort,").append("bcName->name,").
				append("bcDescrption->discription,").append("dbIp->dbIp,").append("dbPort->dbPort,").append("dbUser->dbUser,").
				append("dbPwd->dbPwd,").append("dbType->dbType");
			OECPBeanUtils.copyObjectValue(res[0], bc, str.toString());
			save(bc);
		} catch (Exception e) {
			e.printStackTrace();
			throw new BizException("识别组件失败");
		}

	}

	@Override
	public void deleteComponent(String id) throws BizException {
		//首先 判断当前组件是否启用
		boolean flag = orgService.getUserdBCs(id);
		if(flag){
			throw new BizException("组件已被启用,不能删除");
		}
		//FIXME LINTAO 调用其它的服务
		//然后进行删除 依次调用服务、事件、依赖、功能删除的服务
		//服务
		bizServiceService.delBizServiceByBcId(id.toString());
		//事件
		
		//依赖
		bizDependService.deleteByBizComponentId(id);
		//功能
		
		delete(id);
		
	}

	@Override
	public void initComponentInfo(String id) throws BizException {
		BizComponent bc = find(id);
		//FIXME  LINTAO 平台的IP 和 端口如何传入
		try {
			SoapUtils.callService(bc.getWebServiceDomainUrl()+"/BCInitialize?wsdl", "initialize", new Object[]{"http://localhost:8080/oecp","8080"});
		} catch (Exception e) {
			e.printStackTrace();
			throw new BizException("初始化失败");
		}
		bc.setInitNum(bc.getInitNum()+1);
		save(bc);
		
	}

	@Override
	public void connection(String id) throws BizException {
		//FIXME  LINTAO 依次调用服务、事件、依赖、功能注册的服务
		BizComponent bc = find(id);
		//依賴
		bizDependService.registerBizDepend(bc);
		bc.setIsConnection(true);
		save(bc);
		
	}
	
	@Override
	public void create(BizComponent t) throws BizException {
		t.setExistId(t.getId());
		t.setId(null);
		filtId(t, false);
		super.create(t);
		eventEngine.fireEvent(t, "create", WebContext.getCurrentUser().getLoginedOrg());
	}
	
	@Override
	public void update(BizComponent t) throws BizException {
		filtId(t, true);
		super.update(t);
		eventEngine.fireEvent(t, "update", WebContext.getCurrentUser().getLoginedOrg());
	}
	
	/* (non-Javadoc)
	 * @see oecp.framework.service.BaseServiceImpl#delete(java.io.Serializable)
	 */
	@Override
	public void delete(Serializable id) throws BizException {
		super.delete(id);
		BizComponent bc = new BizComponent();
		bc.setId((String)id);
		eventEngine.fireEvent(bc, "delete", WebContext.getCurrentUser().getLoginedOrg(),(String)id);
	}
	
	
	public BizDependService getBizDependService() {
		return bizDependService;
	}

	public void setBizDependService(BizDependService bizDependService) {
		this.bizDependService = bizDependService;
	}

	public BizServiceService getBizServiceService() {
		return bizServiceService;
	}

	public void setBizServiceService(BizServiceService bizServiceService) {
		this.bizServiceService = bizServiceService;
	}

	public OrgService getOrgService() {
		return orgService;
	}

	public void setOrgService(OrgService orgService) {
		this.orgService = orgService;
	}

	public EventEngine getEventEngine() {
		return eventEngine;
	}

	public void setEventEngine(EventEngine eventEngine) {
		this.eventEngine = eventEngine;
	}

	/* (non-Javadoc)
	 * @see oecp.acbase.service.ArchivesBaseServiceImpl#afterArchivesSave(oecp.acbase.eo.BaseMasArchivesEO)
	 */
	@Override
	protected void afterArchivesSave(BizComponent archives) throws BizException {
		
	}

	/* (non-Javadoc)
	 * @see oecp.acbase.service.ArchivesBaseServiceImpl#validateArchives(oecp.acbase.eo.BaseMasArchivesEO)
	 */
	@Override
	protected void validateArchives(BizComponent archives) throws BizException {
		
	}

	/* (non-Javadoc)
	 * @see oecp.acbase.service.ArchivesBaseServiceImpl#beforeArchivesSave(oecp.acbase.eo.BaseMasArchivesEO)
	 */
	@Override
	protected void beforeArchivesSave(BizComponent archives) throws BizException {
		
	}

	/* (non-Javadoc)
	 * @see oecp.acbase.service.ArchivesBaseServiceImpl#beforeDelete(oecp.acbase.eo.BaseMasArchivesEO, oecp.platform.user.eo.User)
	 */
	@Override
	protected void beforeDelete(BizComponent archives, User operator) throws BizException {
		
	}
}
