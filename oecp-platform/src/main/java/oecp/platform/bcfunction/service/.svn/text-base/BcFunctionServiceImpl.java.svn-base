package oecp.platform.bcfunction.service;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;

import javax.annotation.Resource;

import oecp.framework.dao.QueryResult;
import oecp.framework.exception.BizException;
import oecp.framework.util.OECPBeanUtils;
import oecp.framework.util.soap.SoapUtils;
import oecp.platform.base.service.OECPValidator;
import oecp.platform.base.service.PlatformBaseServiceImpl;
import oecp.platform.bcfunction.eo.Function;
import oecp.platform.bcfunction.eo.FunctionField;
import oecp.platform.bcfunction.eo.FunctionUI;
import oecp.platform.bcfunction.eo.UIElement;
import oecp.platform.bcinfo.service.BizComponentService;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service("bcFunctionService")
@Transactional
public class BcFunctionServiceImpl extends PlatformBaseServiceImpl<Function>
		implements BcFunctionService {

	@Resource(name = "functionValidator")
	private OECPValidator validator;
	@Resource
	private BizComponentService bizComponentService;

	@Override
	public List<Function> getRootFunctions(String bc_pk) {
		List<Function> funcList = getDao().queryByWhere(Function.class,
				"o.bc.id=? and o.parent is null", new Object[] { bc_pk },
				new LinkedHashMap<String, String>() {
					private static final long serialVersionUID = 1L;
					{
						put("displayOrder", "asc");
					}
				});
		return funcList;
	}

	@Override
	public QueryResult<Function> getFunctions(String bc_pk, int start, int limit) {
		return getDao().getScrollData(Function.class, start, 20, "o.bc.id=?",
				new Object[] { bc_pk }, new LinkedHashMap<String, String>() {
					private static final long serialVersionUID = 1L;
					{
						put("displayOrder", "asc");
					}
				});
	}

	@Override
	public void save(Function entity) throws BizException {
		if (entity.getId() == null) {
			validator.validator("addFunction", entity, getDao());
			getDao().create(entity);
		} else {
			validator.validator("updateFunction", entity, getDao());
			getDao().update(entity);
		}
	}

	@Override
	public void delete(Serializable id) throws BizException {
		validator.validator("deleteFunction", id, getDao());
		super.delete(id);
	}

	@Override
	public List<Function> getHasApprovalFunctions() {
		return getDao().queryByWhere(Function.class,
				"o.wsuserd=? AND o.runable=?",
				new Object[] { Boolean.TRUE, Boolean.TRUE });
	}

	public void setValidator(OECPValidator validator) {
		this.validator = validator;
	}

	@Override
	public Function getFunctionByCode(String code) {
		return getDao().findByWhere(Function.class, "o.code=?",
				new Object[] { code });
	}
	
	@Override
	public void delFunctionsByBcId(String bcId) {
		getDao().deleteByWhere(Function.class, "o.bc.id=?", new Object[] { bcId });
	}

	@Override
	@Transactional
	public void initBcFunctions(String id) throws Exception {
		String wsUrl = "http://localhost:8080/oecp_bc/BCRegister?wsdl";
		Object[] object = SoapUtils.callService(wsUrl, "getBizFunctions");
		StringBuffer str = new StringBuffer();
//		"[package1.package1.Class:stringValue]"
//		注意：当使用默认值公式时，默认值的所属类型，必须要有字符串为参数的构造函数。如 Boolean("true")。
//		 a.list->b.list[List:ClassC:alistField1->blistField1,alistField2->blistField2]
		str.append("code->code,").append("name->name,").append("description->description,").
			append("[java.lang.String:").append(id).append("]->bc.id,").
			append("parentCode->parent.code,").
			append("runable->runable,").append("wfused->wsuserd,").append("displayOrder->displayOrder,");
		str.append("fields->functionFields[List:oecp.platform.bcfunction.eo.FunctionField:").
			append("code->name,").append("name->dispName");
		//fields list
		str.append("],");
		
		str.append("uies->uis[List:oecp.platform.bcfunction.eo.FunctionUI:").append("code->code,").
			append("title->name,").append("description->description,").append("sign->sign,").append("defaultUI->isDefault,");
		
		str.append("elements->elements[List:oecp.platform.bcfunction.eo.UIElement:").append("id->elementId,").
			append("description->description,").append("visibleParameterName->visibleParameterName");
		//elements list
		str.append("]");
		
		//uies list	
		str.append("]");
		List<Function> bc_funs = OECPBeanUtils.createObjectList((ArrayList)object[0], Function.class, str.toString());
		
		dealFunctions(bc_funs);
//		getDao().createBatch(bc_funs);
		for(Function fun : bc_funs){
			if(fun.getId() == null){
				getDao().create(fun);
			} else {
				//update fields
				List<FunctionField> fields = fun.getFunctionFields();
				if(fields != null){
					for(FunctionField field : fields){
						if(field.getId() == null){
							getDao().create(field);
						} else {
							getDao().update(field);
						}
					}
				}
				
				//update uis
				List<FunctionUI> uis = fun.getUis();
				if(uis != null){
					for(FunctionUI ui : uis){
						if(ui.getId() == null){
							getDao().create(ui);
						} else {
							//update eles
							List<UIElement> eles = ui.getElements();
							if(eles != null){
								for(UIElement ele : eles){
									if(ele.getId() == null){
										getDao().create(ele);
									} else {
										getDao().update(ele);
									}
								}
							}
							
							getDao().update(ui);
						}
					}
				}
				
				getDao().update(fun);
			}
		}
		System.out.println(bc_funs);
	}

	//处理 访问web service生成的功能列表（id相关）
	private void dealFunctions(List<Function> bc_funs){
		if(bc_funs == null){
			return;
		}
		for(Function bc_fun : bc_funs){
			//parentId
			if(bc_fun.getParent().getCode() == null){
				bc_fun.setParent(null);
			} else {
				for(Function bc_fun_p : bc_funs){
					if(bc_fun.getParent().getCode().equals(bc_fun_p.getCode())){
						bc_fun.setParent(bc_fun_p);
						List<Function> list = bc_fun_p.getChildren();
						if(list == null){
							list = new ArrayList<Function>();
						}
						list.add(bc_fun);
						bc_fun_p.setChildren(list);
						break;
					}
					bc_fun.setParent(null);
				}
			}
			//funId
			Function fun = getFunctionByCode(bc_fun.getCode());
			if(fun != null){
				bc_fun.setId(fun.getId());
			}
			dealFields(fun, bc_fun);
			dealUIs(fun, bc_fun);
		}
	}
	//处理 访问web service生成的功能数据权限列表（id相关）
	private void dealFields(Function fun, Function bc_fun){
		if(bc_fun == null || bc_fun.getFunctionFields() == null){
			return;
		}
		for(FunctionField bc_field : bc_fun.getFunctionFields()){
			bc_field.setFunction(bc_fun);
			if(fun != null && fun.getFunctionFields() != null){
				for(FunctionField field : fun.getFunctionFields()){
					if(field.getName().equals(bc_field.getName())){
						bc_field.setId(field.getId());
						break;
					}
				}
			}
		}
	}
	
	//处理 访问web service生成的功能界面列表（id相关）
	private void dealUIs(Function fun, Function bc_fun){
		if(bc_fun == null || bc_fun.getUis() == null){
			return;
		}
		for(FunctionUI bc_ui : bc_fun.getUis()){
			bc_ui.setFunction(bc_fun);
			FunctionUI uiTmp = null;
			if(fun != null && fun.getUis() != null){
				for(FunctionUI ui : fun.getUis()){
					if(ui.getCode().equals(bc_ui.getCode())){
						bc_ui.setId(ui.getId());
						uiTmp = ui;
						break;
					}
				}
			}
			dealEles(uiTmp,bc_ui);
		}
	}
	
	//处理 访问web service生成的界面元素列表（id相关）
	private void dealEles(FunctionUI ui, FunctionUI bc_ui){
		if(bc_ui == null || bc_ui.getElements() == null){
			return;
		}
		for(UIElement bc_ele : bc_ui.getElements()){
			bc_ele.setFunctionUI(bc_ui);
			if(ui != null && ui.getElements() != null){
				for(UIElement ele : ui.getElements()){
					if(ele.getElementId().equals(bc_ele.getElementId())){
						bc_ele.setId(ele.getId());
						break;
					}
				}
			}
		}
	}
	
	public BizComponentService getBizComponentService() {
		return bizComponentService;
	}

	public void setBizComponentService(BizComponentService bizComponentService) {
		this.bizComponentService = bizComponentService;
	}
}
