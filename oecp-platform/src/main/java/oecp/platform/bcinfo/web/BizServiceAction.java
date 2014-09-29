package oecp.platform.bcinfo.web;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;
import javax.wsdl.WSDLException;

import oecp.framework.dao.QueryCondition;
import oecp.framework.dao.QueryObject;
import oecp.framework.dao.QueryResult;
import oecp.framework.exception.BizException;
import oecp.framework.web.JsonResult;
import oecp.platform.bcinfo.eo.BizComponent;
import oecp.platform.bcinfo.eo.BizService;
import oecp.platform.bcinfo.eo.OperationDescription;
import oecp.platform.bcinfo.service.BizComponentService;
import oecp.platform.bcinfo.service.BizServiceService;
import oecp.platform.bcinfo.service.OperationDescriptionService;
import oecp.platform.web.BasePlatformAction;

import org.apache.commons.lang.xwork.StringUtils;
import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.Namespace;
import org.apache.struts2.convention.annotation.ParentPackage;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;

/**
 * 组件服务 Action类
 * 
 * @author wangliang
 * @date 2011-8-9下午04:11:24
 * @version 1.0
 */
@Controller("bizServiceAction")
@Scope("prototype")
@ParentPackage("default-package")
@Namespace("/bizservice")
public class BizServiceAction extends BasePlatformAction {

	private static final long serialVersionUID = 1L;

	private String id;

	private BizService service;

	private List<OperationDescription> operations;

	@Resource
	private BizComponentService bizComponentService;

	@Resource
	private BizServiceService bizServiceService;

	@Resource
	private OperationDescriptionService operationDescriptionService;

	/**
	 * 
	 * 
	 * @author wangliang
	 * @date 2011-8-12下午02:42:07
	 * @return
	 * @throws BizException
	 */
	@Transactional
	@Action(value = "addOrModifyServices")
	public String addOrModifyServices() throws BizException {
		List<QueryCondition> list = new ArrayList<QueryCondition>();
		QueryCondition query1 = new QueryCondition();
		query1.setField("o.serviceName");
		query1.setOperator("=");
		query1.setValue(service.getServiceName());
		list.add(query1);
		QueryObject qo = new QueryObject();
		if (service != null && service.getId() != null) {
			QueryCondition query2 = new QueryCondition();
			query2.setField("o.id");
			query2.setOperator("!=");
			query2.setValue(service.getId());
			list.add(query2);
			qo.setQueryConditions(list);
			List<BizService> s = bizServiceService.query(qo);
			if (s.size() > 0) {
				setJsonString("{success:false,msg:'服务名已存在！'}");
				return SUCCESS;
			}
			// 判断是更新
			if (operations != null) {
				operationDescriptionService.delOperations(operations);
			}
			bizServiceService.saveService(service);
		} else if (service != null && service.getId() == null) {
			qo.setQueryConditions(list);
			List<BizService> s = bizServiceService.query(qo);
			if (s.size() > 0) {
				setJsonString("{success:false,msg:'服务名已存在！'}");
				return SUCCESS;
			}
			// 判断是新增
			bizServiceService.save(service);
		}
		JsonResult jr = new JsonResult(null);
		setJsonString(jr.toJSONString());
		return SUCCESS;
	}

	@Action(value = "queryServiceById")
	@Transactional
	public String queryServiceById() throws NumberFormatException, BizException {
		if (!StringUtils.isEmpty(id)) {
			String rs = bizServiceService.getServiceJsonById(id);
			setJsonString(rs);
		} else {
			JsonResult jr = new JsonResult(false, "请选择一条记录！");
			setJsonString(jr.toJSONString());
		}
		return SUCCESS;
	}

	/**
	 * 初始化组件服务
	 * 
	 * @author wangliang
	 * @date 2011-8-10下午03:01:02
	 * @return
	 * @throws BizException
	 * @throws WSDLException
	 */
	@Action(value = "initBizServices")
	public String initBizServices() throws BizException, WSDLException {
		BizComponent bc = bizComponentService.find(id);
		// 清空历史
		bizServiceService.delBizServiceByBcId(bc.getId());
		bizServiceService.initBizServices(bc);
		JsonResult jr = new JsonResult("");
		setJsonString(jr.toJSONString());
		return SUCCESS;
	}

	/**
	 * 根据组件获取服务
	 * 
	 * @author wangliang
	 * @date 2011-8-11下午02:04:36
	 * @return
	 * @throws BizException
	 * @throws NumberFormatException
	 */
	@Action(value = "getBizServiceByBcId")
	public String getBizServiceByBcId() throws NumberFormatException,
			BizException {
		if (!StringUtils.isEmpty(id)) {
			QueryResult<BizService> qr = bizServiceService
					.queryBizServiceByBcId(start, limit, id);
			if (qr.getTotalrecord() > 0) {
				JsonResult jr = new JsonResult(qr.getResultlist());
				jr.setTotalCounts(qr.getTotalrecord().intValue());
				jr.setContainFields(new String[] { "id", "bc.id",
						"serviceName", "description" });
				setJsonString(jr.toJSONString());
			} else {
				setJsonString("{success:true,msg:'',result:[]}");
			}
		}
		return SUCCESS;
	}

	/**
	 * 根据服务Id获取方法
	 * 
	 * @author wangliang
	 * @date 2011-8-11下午02:05:59
	 * @return
	 * @throws BizException
	 * @throws NumberFormatException
	 */
	@Action(value = "getOperationByServiceId")
	@Transactional
	public String getOperationByServiceId() throws NumberFormatException,
			BizException {
		BizService bs = bizServiceService.find(id);
		List<OperationDescription> operations = bs.getOperations();
		JsonResult jr = new JsonResult(operations);
		jr
				.setContainFields(new String[] { "id", "operationName",
						"description" });
		setJsonString(jr.toJSONString());
		return SUCCESS;
	}

	/**
	 * 删除服务
	 * 
	 * @author wangliang
	 * @throws BizException
	 * @throws NumberFormatException
	 * @date 2011-8-15下午03:11:44
	 */
	@Action(value = "delServiceById")
	public String delServiceById() throws NumberFormatException, BizException {
		JsonResult jr = new JsonResult("");
		if (!StringUtils.isEmpty(id)) {
			bizServiceService.delete(id);
		} else {
			jr.setSuccess(false);
			jr.setMsg("参数错误，未找到PK!");
		}
		setJsonString(jr.toJSONString());
		return SUCCESS;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public void setBizComponentService(BizComponentService bizComponentService) {
		this.bizComponentService = bizComponentService;
	}

	public void setBizServiceService(BizServiceService bizServiceService) {
		this.bizServiceService = bizServiceService;
	}

	public BizService getService() {
		return service;
	}

	public void setService(BizService service) {
		this.service = service;
	}

	public List<OperationDescription> getOperations() {
		return operations;
	}

	public void setOperations(List<OperationDescription> operations) {
		this.operations = operations;
	}

	public void setOperationDescriptionService(
			OperationDescriptionService operationDescriptionService) {
		this.operationDescriptionService = operationDescriptionService;
	}
}
