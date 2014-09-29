package oecp.platform.bcinfo.service;

import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.List;

import javax.wsdl.Definition;
import javax.wsdl.WSDLException;

import oecp.framework.dao.QueryResult;
import oecp.framework.exception.BizException;
import oecp.framework.util.OECPBeanUtils;
import oecp.framework.util.soap.SoapUtils;
import oecp.framework.web.JsonResult;
import oecp.platform.base.service.PlatformBaseServiceImpl;
import oecp.platform.bcinfo.eo.BizComponent;
import oecp.platform.bcinfo.eo.BizService;
import oecp.platform.bcinfo.eo.OperationDescription;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.alibaba.fastjson.JSON;

@Service("bizServiceService")
public class BizServiceServiceImpl extends PlatformBaseServiceImpl<BizService>
		implements BizServiceService {

	@Override
	public QueryResult<BizService> queryBizServiceByBcId(int start, int limit,
			String bcId) {
		return getDao().getScrollData(BizService.class, start, limit,
				"o.bc.id=?", new Object[] { bcId },
				new LinkedHashMap<String, String>() {
					private static final long serialVersionUID = 1L;
					{
						put("id", "desc");
					}
				});
	}

	@Override
	@Transactional
	public void delBizServiceByBcId(String bcId) throws BizException {
		List<BizService> services = getDao().queryByWhere(BizService.class,
				"o.bc.id=?", new Object[] { bcId });
		for (int i = 0; i < services.size(); i++) {
			this.delete(services.get(i).getId());
		}
	}

	@SuppressWarnings("unchecked")
	@Override
	public void initBizServices(BizComponent bc) throws BizException,
			WSDLException {
		// 调用ws获取组件服务信息
		String ws_url = bc.getWebServiceDomainUrl();// "http://localhost:8080/oecp_bc";//
		Object[] res = null;
		try {
			res = SoapUtils.callService(ws_url + "/BCRegister?wsdl",
					"getBizServiceWSDLURLs");
		} catch (Exception ex) {
			throw new BizException(ex);
		}
		ArrayList<String> rs = (ArrayList<String>) res[0];
		for (String wsdl_url : rs) {
			if (wsdl_url.startsWith("/")) {
				wsdl_url = ws_url + wsdl_url;
			} else {
				wsdl_url = ws_url + "/" + wsdl_url;
			}
			if (!wsdl_url.endsWith("?wsdl")) {
				wsdl_url += "?wsdl";
			}
			// 解析wsdl文件
			Definition def = SoapUtils.getDefinition(wsdl_url);
			String description = SoapUtils.getServiceDesc(def);
			ArrayList<javax.wsdl.Service> services = SoapUtils.getServices(def);
			String serviceName = services.get(0).getQName().getLocalPart();
			String[] operations = SoapUtils.getServiceOperations(def);
			String[] operationDescs = SoapUtils.getServiceOperationsDesc(def);
			BizService bizws = new BizService();
			bizws.setBc(bc);
			bizws.setDescription(description);
			bizws.setServiceName(serviceName);

			ArrayList<OperationDescription> od_list = new ArrayList<OperationDescription>();
			for (int i = 0; i < operations.length; i++) {
				String operation = operations[i];
				String desc = operationDescs[i];
				OperationDescription od = new OperationDescription();
				od.setOperationName(operation);
				od.setDescription(desc);
				od.setService(bizws);
				od_list.add(od);
			}
			bizws.setOperations(od_list);
			this.save(bizws);
		}
	}

	@Override
	public void saveService(BizService service) throws BizException {
		List<OperationDescription> operations = service.getOperations();
		if (operations != null) {
			for (OperationDescription operation : operations) {
				operation.setService(service);
			}
		}
		this.save(service);
	}

	@Override
	public String getServiceJsonById(String serviceId) throws BizException,
			NumberFormatException {
		String pk = serviceId;
		BizService service = this.find(pk);
		String body = "[]";
		if (service.getOperations() != null
				&& service.getOperations().size() > 0) {
			Collection<?> c = (Collection<?>) service.getOperations();
			List<Object> list = new ArrayList<Object>();
			for (Object o : c) {
				list.add(OECPBeanUtils.toMap(o, new String[] { "id",
						"operationName", "description", "service.id" },
						"yyyy-MM-dd HH:mm:ss"));
			}
			body = JSON.toJSONString(list);
		}
		JsonResult jr_head = new JsonResult(service);
		jr_head.setContainFields(new String[] { "id", "serviceName",
				"description", "bc.id" });
		String json = jr_head.toJSONString();
		json = json.replace("\"result\":{", "\"result\":{\"operations\":"
				+ body + ",");
		return json;
	}
}
