/*
 * Copyright (c) 2012 OECP All Rights Reserved.                	
 * <a href="http://www.oecp.cn">OECP</a> 
 */

package oecp.platform.print.web;

import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import oecp.framework.entity.base.BaseEO;
import oecp.framework.exception.BizException;
import oecp.framework.web.JsonResult;
import oecp.platform.bcfunction.eo.Function;
import oecp.platform.bcfunction.service.BcFunctionService;
import oecp.platform.ed.service.EntityDictionaryService;
import oecp.platform.print.eo.PrintTemplate;
import oecp.platform.print.service.PrintTemplateService;
import oecp.platform.web.BasePlatformAction;

import org.apache.commons.lang.StringUtils;
import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.Namespace;
import org.apache.struts2.convention.annotation.ParentPackage;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

/**
 * 单据打印模板
 * 
 * @author wangliang
 * @date 2012-3-19 上午10:32:02
 * @version 1.0
 * 
 */
@Controller
@Scope("prototype")
@ParentPackage("default-package")
@Namespace("/printTemplate")
public class PrintTemplateAction extends BasePlatformAction {
	private static final long serialVersionUID = 1L;
	private String id;// 打印模板主键
	private String functionCode;// 单据功能编号
	private PrintTemplate template;// 打印模板
	@Resource
	private PrintTemplateService templateService;// 模板服务类
	@Resource
	private BcFunctionService functionService;// 功能服务类
	@Resource
	private EntityDictionaryService entityDictionaryService;

	/**
	 * 获取模板列表
	 * 
	 * @author Administrator
	 * @date 2012-3-19下午3:16:31
	 * @return
	 */
	@Action(value = "list")
	public String getTemplateList() {
		JsonResult jr = new JsonResult("");
		if (StringUtils.isNotEmpty(id)) {
			List<PrintTemplate> templates = templateService.getTemplatesByFunId(id);
			jr.setContainFields(new String[] { "name", "vtemplate", "id" });
			jr.setResult(templates);
		} else {
			setErrJsonResult(jr, "功能编号为空！");
		}
		setJsonString(jr.toJSONString());
		return SUCCESS;
	}

	/**
	 * 获取打印模板内容
	 * 
	 * @author wangliang
	 * @date 2012-3-19下午3:41:33
	 * @return
	 * @throws BizException
	 */
	@Action(value = "load")
	public String loadTemplate() throws BizException {
		JsonResult jr = new JsonResult("");
		if (StringUtils.isNotEmpty(id)) {
			PrintTemplate template = templateService.find(id);
			jr.setResult(template);
		} else {
			setErrJsonResult(jr, "打印模板id为空！");
		}
		setJsonString(jr.toJSONString());
		return SUCCESS;
	}

	/**
	 * 模板保存
	 * 
	 * @author Administrator
	 * @date 2012-3-20下午2:44:45
	 * @return
	 * @throws BizException
	 */
	@Action(value = "save")
	public String saveTemplate() throws BizException {
		if (template != null) {
			template.setOrgan(this.getOnlineUser().getLoginedOrg());
			templateService.save(template);
		}
		JsonResult jr = new JsonResult(template);
		jr.setContainFields(new String[] { "name", "vtemplate" });
		setJsonString(jr.toJSONString());
		return SUCCESS;
	}

	/**
	 * 获取实体属性，包括子表属性；主表、子表中的属性如果是BaseEO的子类，属性名添加.name后缀<br>
	 * 格式如下：<br>
	 * <code>
	 {"result":{
	 	"main_bill":[
	 		{"dispname":"主键","attrname":"id"},{"dispname":"编号","attrname":"no"},{"dispname":"名称","attrname":"name"}
	 	],
	 	"children_bill":[
	 			{"islist":false,"params":[{"dispname":"主键","attrname":"id"},{"dispname":"xxx","attrname":"aa"},{"dispname":"xxx","attrname":"org.name"},{"dispname":"xxx","attrname":"bb"},{"dispname":"xx","attrname":"master"}],"objname":"threeDetails"},
	 			{"islist":true,"params":[{"dispname":"主键","attrname":"id"},{"dispname":"xxx","attrname":"num"},{"dispname":"xxx","attrname":"name"},{"dispname":"xxx","attrname":"user.name"},{"dispname":"xx","attrname":"master"}],"objname":"details"}
	 	]},
	 	"msg":"执行成功！","success":true,"totalCounts":null}
	 * </code>
	 * @author wangliang
	 * @date 2012-3-19下午3:45:28
	 * @return
	 * @throws ClassNotFoundException
	 * @throws IllegalAccessException
	 * @throws InstantiationException
	 * @throws BizException
	 */
	@SuppressWarnings({ "rawtypes" })
	@Action(value="getEntityParams")
	public String getEntityParams() throws BizException, InstantiationException, IllegalAccessException, ClassNotFoundException {
		JsonResult jr = new JsonResult("");
		if(StringUtils.isNotEmpty(id)){
				Function fun = functionService.find(id);
				String clsName = fun.getMainEntity();//获取主实体
				BaseEO eo = (BaseEO)Class.forName(clsName).newInstance();
				Map _m = entityDictionaryService.getEntityParams(eo);
				jr.setResult(_m);
				setJsonString(jr.toJSONString());
		}else{
			setErrJsonResult(jr, "功能编号为空！");
		}
		return SUCCESS;
	}
	
	/** 拼装错误信息 */
	private void setErrJsonResult(JsonResult jr, String errMsg) {
		jr.setSuccess(false);
		jr.setMsg(StringUtils.isEmpty(errMsg) ? "查询失败" : errMsg);
	}

	/************************************* get set 方法 ************************************/
	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getFunctionCode() {
		return functionCode;
	}

	public void setFunctionCode(String functionCode) {
		this.functionCode = functionCode;
	}

	public void setTemplateService(PrintTemplateService templateService) {
		this.templateService = templateService;
	}

	public void setFunctionService(BcFunctionService functionService) {
		this.functionService = functionService;
	}

	public void setEntityDictionaryService(
			EntityDictionaryService entityDictionaryService) {
		this.entityDictionaryService = entityDictionaryService;
	}

	public PrintTemplate getTemplate() {
		return template;
	}

	public void setTemplate(PrintTemplate template) {
		this.template = template;
	}

}
