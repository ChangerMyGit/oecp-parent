/*
 * Copyright (c) 2011 OECP All Rights Reserved.                	
 * <a href="http://www.oecp.cn">OECP</a> 
 */

package oecp.platform.bcdepend.service;

import java.util.List;
import javax.wsdl.Definition;
import org.springframework.stereotype.Service;
import oecp.framework.exception.BizException;
import oecp.framework.util.soap.SoapUtils;
import oecp.platform.base.service.PlatformBaseServiceImpl;
import oecp.platform.bcdepend.eo.BizDepend;
import oecp.platform.bcinfo.eo.BizComponent;

/**
 * 
 * 依赖服务类
 * @author lintao
 * @date 2011-8-8 下午01:39:49
 * @version 1.0
 * 
 */
@Service("bizDependService")
public class BizDependServiceImpl extends PlatformBaseServiceImpl<BizDepend>
		implements BizDependService {

	@Override
	public List<BizDepend> queryByBizComponentId(String bc_pk) {
		List<BizDepend> bizDependList = getDao().queryByWhere(BizDepend.class,
				"o.bc.id=?", new Object[] { bc_pk });
		return bizDependList;
	}

	@Override
	public void registerBizDepend(BizComponent bc)
			throws BizException {
		String ws_url = bc.getWebServiceDomainUrl();
		try {
			Object[] res = SoapUtils.callService(ws_url + "/BCRegister?wsdl", "getBizDependWSDLURLs",
					new Object());
			if (res == null || res.length <= 0) {
				throw new BizException("对接失败");
			} else {
				List<String> list = (List<String>) res[0];
				for (String wsdlUrl : list) {
					if(wsdlUrl.startsWith("/")){
						wsdlUrl = ws_url + wsdlUrl;
					}else{
						wsdlUrl = ws_url + "/"+wsdlUrl;
					}
					if(!wsdlUrl.endsWith("?wsdl")){
						wsdlUrl+="?wsdl";
					}
					Definition def = SoapUtils.getDefinition(wsdlUrl);
					BizDepend bizDepend = new BizDepend();
					bizDepend.setBc(bc);
					bizDepend.setUrl(wsdlUrl);
					String en = SoapUtils.getServiceName(def);
					bizDepend.setDependName_EN(en);
					String desc = SoapUtils.getServiceDesc(def);
					bizDepend.setDependDesc(desc);
					create(bizDepend);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			throw new BizException("对接失败");
		}

	}

	@Override
	public void deleteByBizComponentId(String bc_pk) {
		getDao().deleteByWhere(BizDepend.class, "o.bc.id=?",
				new Object[] { bc_pk });

	}

}
