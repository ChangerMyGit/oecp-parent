package oecp.platform.bcfunction.web;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;

import oecp.framework.dao.QueryResult;
import oecp.framework.exception.BizException;
import oecp.framework.util.FastJsonUtils;
import oecp.framework.web.ext.JsonTreeVO;
import oecp.platform.bcfunction.eo.Function;
import oecp.platform.bcfunction.eo.FunctionField;
import oecp.platform.bcfunction.eo.FunctionUI;
import oecp.platform.bcfunction.eo.UIElement;
import oecp.platform.bcfunction.service.BcFunctionService;
import oecp.platform.bcfunction.service.FunctionFieldService;
import oecp.platform.bcfunction.service.FunctionUIService;
import oecp.platform.bcfunction.service.UIElementService;
import oecp.platform.bcinfo.eo.BizComponent;
import oecp.platform.bcinfo.service.BizComponentService;
import oecp.platform.web.BasePlatformAction;

import org.apache.commons.lang.StringUtils;
import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.Namespace;
import org.apache.struts2.convention.annotation.ParentPackage;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;

import com.alibaba.fastjson.JSON;

/**
 * 组件功能Action
 * 
 * @author wangliang
 * 
 */
@Controller("functionAction")
@Scope("prototype")
@ParentPackage("default-package")
@Namespace("/function")
public class BcFunctionAction extends BasePlatformAction {

	private static final long serialVersionUID = -7457053015064832876L;
	// 组件功能接口类
	@Resource
	private BcFunctionService bcFunctionService;
	// 业务组件接口类
	@Resource
	private BizComponentService bizComponentService;
	// 功能界面接口类
	@Resource
	private FunctionUIService functionUIService;
	// 界面元素接口类
	@Resource
	private UIElementService uiElementService;
	// 功能可控数据权限字段接口类
	@Resource
	private FunctionFieldService functionFieldService;
	
	// 组件界面
	private FunctionUI functionUI;
	// 业务组件
	private Function function;
	// extjs 提交的数据
	private String data;
	// ExtJs传递的主键参数,用于查询使用
	private String id;
	// Long型主键
	private String pk;

	private String[] ids;
	
	private FunctionField functionField;
	
	private String[] functionFieldIds;

	public static final String FAULTJSON = "{success:false}";
	public static final String SUCCESSJSON = "{success:true}";

	/**
	 * 获取所有已安装的组件列表
	 * 
	 * @author slx
	 * @date 2011 5 31 10:49:43
	 * @modifyNote
	 * @return
	 * @throws BizException
	 */
	@Action("listAllBCs")
	@Transactional
	public String listAllBCs() throws BizException {
		List<BizComponent> bcs = bizComponentService.query(null);
		String json = FastJsonUtils.toJson(bcs, new String[] { "id", "code",
				"name", "description" });
		json = json.replaceAll("name", "text");
		setJsonString(json);
		return SUCCESS;
	}

	/**
	 * 获取功能树
	 * 
	 * @param bcCode
	 *            组件编号
	 * @return Json树形结构
	 * @throws BizException
	 * @throws CloneNotSupportedException
	 */
	@Action(value = "fuctionTreeCode")
	@Transactional
	public String fuctionTreeCode() throws IOException, BizException,
			CloneNotSupportedException {
		if (this.parsePk()) {
			// BizComponent bc = bizComponentService.find(pk);
			// 获取功能列表
			List<Function> funcList = bcFunctionService.getRootFunctions(pk.toString());
			List<JsonTreeVO> treevos = eo2vo(funcList);
			// 转换为Extjs树形结构Json
			setJsonString(JSON.toJSONString(treevos));
		}
		return SUCCESS;
	}

	/**
	 * 获取组件功能
	 * 
	 * @return
	 * @throws BizException
	 */
	@Action(value = "queryBCFunctionById")
	public String queryBCFunctionById() throws BizException {
		if (parsePk()) {
			Function bf = bcFunctionService.find_full(pk);
			if(bf.getParent() == null){
				bf.setParent(new Function());
			}
			String json = FastJsonUtils.toJson(bf, new String[] { "id", "code",
					"name", "description", "runable", "wsuserd", "bc",
					"parent", "displayOrder" ,"bizServiceForBpm","mainEntity"});
			
			setJsonString("{success:true,result:[" + json + "]}");
		}
		return SUCCESS;
	}

	/**
	 * 保存组件功能
	 * 
	 * @return
	 * @throws BizException
	 */
	@Action(value = "saveBCFunction")
	public String saveBCFunction() throws BizException {
		if (function != null) {
			// 过滤掉页面提交过来的空parent
			if (function.getParent() != null
					&& function.getParent().getId() == null) {
				function.setParent(null);
			}
			try {
				bcFunctionService.save(function);
				setJsonString("{success:true,msg:'保存成功'}");
			} catch (Exception e) {
				e.printStackTrace();
				setJsonString("{success:false,msg:'".concat(e.getMessage())
						.concat("'}"));
			}
		}
		return SUCCESS;
	}

	/**
	 * 删除组件功能
	 * 
	 * @return
	 * @throws BizException
	 */
	@Action("deleteFunction")
	public String deleteFunction() throws BizException {
		try {
			parsePk();
			bcFunctionService.delete(pk);
			setJsonString("{success:true,msg:'删除成功'}");
		} catch (Exception e) {
			e.printStackTrace();
			setJsonString("{success:false,msg:'".concat(e.getMessage()).concat(
					"'}"));
		}
		return SUCCESS;
	}

	/**
	 * Function界面查看 根据Function 主键 获取FunctionUI Json内容
	 * 
	 * @return
	 */
	@Action(value = "queryFuncUIsByFuncId")
	@Transactional
	public String queryFuncUIsByFuncId() {
		if (parsePk()) {
			QueryResult<FunctionUI> uis = functionUIService.getFunctionUIs(pk,
					start, limit);
			setJsonString(queryResultToJson(uis, new String[] { "id", "code",
					"name", "description", "isDefault", "isDefaultForProcess", "function" }));
		}
		return SUCCESS;
	}

	/**
	 * FunctionUI查看 根据FunctionUI主键获取Json内容
	 * 
	 * @return
	 * @throws BizException
	 */
	@Action(value = "queryFuncUIsById")
	@Transactional
	public String queryFuncUIsById() throws BizException {
		if (parsePk()) {
			FunctionUI ui = functionUIService.find(pk);
			if (ui != null) {
				String head = FastJsonUtils.toJson(ui, new String[] { "id",
						"code", "name", "description", "sign", "isDefault", "isDefaultForProcess",
						"function" });
				String body = FastJsonUtils.toJson(ui.getElements(),
						new String[] { "id", "elementId", "description",
								"visibleParameterName" });
				String json = head.substring(0, head.length() - 1)
						+ ",BodyDetails:" + body + "}";
				setJsonString(" {success: true,result:[" + json + "]}");
			}
		} else {
			setJsonString(FAULTJSON);
		}
		return SUCCESS;
	}

	@Action("deleteFunUIs")
	public String deleteFunUIs() {
		try {
			functionUIService.delete(ids);
			setJsonString("{success:true,msg:'删除成功'}");
		} catch (Exception e) {
			e.printStackTrace();
			setJsonString("{success:false,msg:'".concat(e.getMessage()).concat(
					"'}"));
		}
		return SUCCESS;
	}

	/**
	 * 界面元素Action 获取Json数据
	 * 
	 * @return
	 */
	@Action(value = "queryUIElementDatasById")
	@Transactional
	public String queryUIElementDatasById() {
		if (parsePk()) {
			List<UIElement> el = uiElementService.getElements(pk);
			setJsonString(FastJsonUtils.toJson(el, new String[] { "id",
					"elementId", "description", "visibleParameterName" }));
		}
		return SUCCESS;
	}

	/**
	 * 保存功能界面
	 * 
	 * @return
	 * @throws BizException
	 */
	@Action(value = "saveFunctionUI")
	// @Transactional
	public String saveFunctionUI() {
		try {
			data = data.replaceAll("\"id\":\"\",", "");
			List<UIElement> uis = JSON.parseArray(data, UIElement.class);
//			if (functionUI.getId() != null && functionUI.getId() > 0) {
			if (functionUI.getId() != null ) {
				List<UIElement> uis_tmp = uiElementService
						.getElements(functionUI.getId());
				boolean flag = true;
				for (UIElement ui_tmp : uis_tmp) {
					flag = true;
					for (UIElement uie : uis) {
//						if (uie.getId() != null && uie.getId() > 0
						if (uie.getId() != null
								&& ui_tmp.getId() == uie.getId()) {
							flag = false;
							break;
						}
					}
					if (flag) {
						// FIXME 添加事务后报错。
						uiElementService.delete(ui_tmp.getId());
					}
				}
			}
			for (UIElement ui : uis) {
				ui.setFunctionUI(functionUI);
			}
			functionUI.setElements(uis);
			if (functionUI.getIsDefault()) {
				functionUIService.setDefault(functionUI);
			} else {
				functionUIService.save(functionUI);
			}
			setJsonString(SUCCESSJSON);
		} catch (Exception ex) {
			ex.printStackTrace();
			setJsonString(FAULTJSON);
		}
		return SUCCESS;
	}

	/**
	 * 查询指定功能下的所有可控数据权限字段
	 * @author liujt
	 * @date 2011-8-9上午09:13:44
	 * @return
	 */
	@Action(value = "queryFunctionFieldsByFunId")
	public String queryFunctionFieldsByFunId(){
		if (parsePk()) {
			List<FunctionField> list = functionFieldService.getFunctionFieldsByFunId(pk);
			String json = FastJsonUtils.toJson(list, new String[]{"id","function","name","dispName","className","mdType","eoClassName"});
//			json = json.replaceAll("\"mdType\":null", "\"mdType\":{}");
			setJsonString(json);
		}
		return SUCCESS;
	}
	
	/**
	 * 保存可控数据权限字段
	 * @author liujt
	 * @date 2011-8-9上午09:20:11
	 * @return
	 */
	@Action(value = "saveFunctionField")
	@Transactional
	public String saveFunctionField() {
		if (functionField.getFunction() != null && functionField.getMdType() != null) {
			try {
				functionFieldService.save(functionField);
				setJsonString("{success:true,msg:'保存成功'}");
			} catch (Exception e) {
				e.printStackTrace();
				setJsonString("{success:false,msg:'".concat(e.getMessage())
						.concat("'}"));
			}
		}
		return SUCCESS;
	}
	
	/**
	 * 删除可控数据权限字段
	 * @author liujt
	 * @date 2011-8-9上午09:20:11
	 * @return
	 */
	@Action(value = "deleteFunctionField")
	@Transactional
	public String deleteFunctionField() {
		try {
			if (functionFieldIds != null && functionFieldIds.length > 0) {
				for(String functionFieldId : functionFieldIds){
					functionFieldService.delete(functionFieldId);
				}
				setJsonString("{success : true , msg : '删除成功'}");
			}
		} catch (Exception e) {
			e.printStackTrace();
			setJsonString("{success:false,msg:'".concat(e.getMessage()).concat(
					"'}"));
		}
		return SUCCESS;
	}
	
	@Action(value = "initFunctions")
	@Transactional
	public String initFunctions() {
		try {
			bcFunctionService.initBcFunctions(2l+"");
			setJsonString("{success:true,msg:'保存成功'}");
		} catch (Exception e) {
			e.printStackTrace();
			setJsonString("{success:false,msg:'".concat(e.getMessage())
					.concat("'}"));
		}
		return SUCCESS;
	}
	/**
	 * EO转换为vo
	 * 
	 * @param funcs
	 * @return
	 */
	private List<JsonTreeVO> eo2vo(List<Function> funcs) {
		List<JsonTreeVO> treeList = new ArrayList<JsonTreeVO>();
		for (Function func : funcs) {
			JsonTreeVO vo = new JsonTreeVO();
			vo.setId(String.valueOf(func.getId()));
			vo.setText(func.getName());
			vo.setLeaf(func.getRunable());
			vo.setChildren(eo2vo(func.getChildren()));
			treeList.add(vo);
		}
		return treeList;
	}

	/**
	 * 将QueryResult转换为带分页的Json数据
	 * 格式如下:{success:true,totalCounts:1,result:[{"name"
	 * :"产品列表UI","code":"ui001","isDefault":true,"id":10001,"id":10001}]}
	 */
	private String queryResultToJson(QueryResult rs, String[] filterColumns) {
		if (rs != null && rs.getResultlist() != null) {
			String json = FastJsonUtils.toJson(rs.getResultlist(),
					filterColumns);
			StringBuffer buffer = new StringBuffer();
			buffer.append("{success:true,totalCounts:");
			buffer.append(rs.getTotalrecord());
			buffer.append(",result:");
			buffer.append(json);
			buffer.append("}");
			return buffer.toString();
		}
		return "{success:falst}";
	}

	// 转换主键
	public boolean parsePk() {
		boolean flag = true;
		pk = null;
		if (!StringUtils.isEmpty(id)) {
			try {
				pk = id;
			} catch (NumberFormatException ex) {
				flag = false;
			}
		}
		return flag;
	}

	public void setData(String data) {
		this.data = data;
	}

	public String getData() {
		return data;
	}

	public void setFunctionUI(FunctionUI functionUI) {
		this.functionUI = functionUI;
	}

	public FunctionUI getFunctionUI() {
		return functionUI;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public Function getFunction() {
		return function;
	}

	public void setFunction(Function function) {
		this.function = function;
	}

	public String[] getIds() {
		return ids;
	}

	public void setIds(String[] ids) {
		this.ids = ids;
	}

	public void setBcFunctionService(BcFunctionService bcFunctionService) {
		this.bcFunctionService = bcFunctionService;
	}

	public void setBizComponentService(BizComponentService bizComponentService) {
		this.bizComponentService = bizComponentService;
	}

	public void setFunctionUIService(FunctionUIService functionUIService) {
		this.functionUIService = functionUIService;
	}

	public void setUiElementService(UIElementService uiElementService) {
		this.uiElementService = uiElementService;
	}

	public void setFunctionFieldService(FunctionFieldService functionFieldService) {
		this.functionFieldService = functionFieldService;
	}

	public FunctionField getFunctionField() {
		return functionField;
	}

	public void setFunctionField(FunctionField functionField) {
		this.functionField = functionField;
	}

	public String[] getFunctionFieldIds() {
		return functionFieldIds;
	}

	public void setFunctionFieldIds(String[] functionFieldIds) {
		this.functionFieldIds = functionFieldIds;
	}

}
