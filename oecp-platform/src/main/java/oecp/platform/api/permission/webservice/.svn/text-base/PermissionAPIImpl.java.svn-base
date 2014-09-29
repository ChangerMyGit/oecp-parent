package oecp.platform.api.permission.webservice;

import java.util.List;

import javax.annotation.Resource;
import javax.jws.WebParam;
import javax.jws.WebResult;
import javax.jws.WebService;

import oecp.platform.permission.eo.Permission;
import oecp.platform.permission.eo.PermissionFuncUI;
import oecp.platform.permission.eo.PermissionUIElement;
import oecp.platform.permission.service.PermissionService;

import org.apache.cxf.annotations.WSDLDocumentation;
import org.apache.cxf.annotations.WSDLDocumentationCollection;
import org.springframework.stereotype.Service;

/**
 * 功能权限API实现
 * 
 * @author slx
 * @date 2011 4 15 14:58:47
 * @version 1.0
 */
@Service("permissionAPI")
@WebService(targetNamespace="http://www.oecp.cn",serviceName="permissionAPI")
//@WSDLDocumentationCollection(value={
//		@WSDLDocumentation(placement=Placement.SERVICE,value="功能权限服务"),
//		@WSDLDocumentation(placement=Placement.TOP,value="功能权限服务,提供对功能权限的权限获得和验证服务.版权归<a href='http://www.oecp.cn'>OECP社区</a>所有."),
//	})
public class PermissionAPIImpl implements PermissionAPI {

	@Resource(name="permissionService")
	private PermissionService permissonService;
	
	@Override
	@WebResult(name="isPower")
	@WSDLDocumentationCollection(value={
			@WSDLDocumentation("<p>检查用户在组织内是否可拥有功能编号所表示的权限.</p><p>需要3个参数:</br>1 userID用户主键</br>2 orgID组织主键</br>3 uisign ui标识（界面注册时的URL路径）</p><p>返回值：true=有权限；false=无权限</p>")
	})
	public boolean checkUserPower(
			@WebParam(name="userID")String userID, 
			@WebParam(name="orgID") String orgID, 
			@WebParam(name="uisign") String uisign) {
		return permissonService.checkUserPermission(userID, orgID, uisign);
	}

	@Override
	@WebResult(name="functions")
	@WSDLDocumentation("<p>获得用户在组织内有权使用的功能列表.</p><p>需要2个参数:</br>1 userID用户主键</br>2 orgID组织主键。</p><p>返回值为功能列表，内包含功能内用户可使用的界面和界面上用户【不可见的界面元素】，请注意是【不】可见的界面元素！<p>")
	public Function[] getUserFunctions(
			@WebParam(name="userID",partName="用户id") String userID, 
			@WebParam(name="orgID",partName="组织id") String orgID) {
		List<Permission> prms = permissonService.getUserPermission(userID, orgID);
		return getFunctionFromPermissions(prms);
	}
	/**
	 * 从权限中得到功能列表
	 * @author slx
	 * @date 2011 5 9 15:34:28
	 * @modifyNote
	 * @param prms
	 * @return
	 */
	private Function[] getFunctionFromPermissions(List<Permission> prms){
		if(prms == null){
			return null;
		}
		int len = prms.size();
		Function[] funcs = new Function[len];
		for (int i = 0; i < len; i++) {
			funcs[i] = new Function();
			copyPermisson2Function(funcs[i],prms.get(i));
		}
		return funcs;
	}
	
	/**
	 * 复制功能
	 * @author slx
	 * @date 2011 5 9 15:31:53
	 * @modifyNote
	 * @param func
	 * @param prm
	 */
	private void copyPermisson2Function(Function func , Permission prm){
		func.setCode(prm.getFunction().getCode());
		func.setParentCode(prm.getFunction().getParent()==null?null:prm.getFunction().getParent().getCode());
		func.setDescription(prm.getFunction().getDescription());
		func.setName(prm.getFunction().getName());
		func.setRunable(prm.getFunction().getRunable());
		List<PermissionFuncUI> prmuis = prm.getPermissionFuncUIs();
		
		if(prmuis != null){
			int len = prmuis.size();
			FunctionUI[] uis = new FunctionUI[len];
			for (int i = 0; i < len; i++) {
				uis[i] = new FunctionUI();
				copyPermissonUI2FuncitonUI(uis[i],prmuis.get(i));
			}
			func.setUis(uis);
		}
	}
	/**
	 * 复制页面
	 * @author slx
	 * @date 2011 5 9 15:31:42
	 * @modifyNote
	 * @param funcui
	 * @param prmui
	 */
	private void copyPermissonUI2FuncitonUI(FunctionUI funcui , PermissionFuncUI prmui){
		funcui.setCode(prmui.getFunctionUI().getCode());
		funcui.setTitle(prmui.getFunctionUI().getName());
		funcui.setDefaultUI(prmui.getFunctionUI().getIsDefault());
		funcui.setSign(prmui.getFunctionUI().getSign());
		List<PermissionUIElement> prmuies = prmui.getPermissionUIElements();
		if(prmuies != null){
			int len = prmuies.size();
			UIElement[] uies = new UIElement[len];
			for (int i = 0; i < len; i++) {
				uies[i] = new UIElement();
				copyPremUIElement2UIElement(uies[i],prmuies.get(i));
			}
			funcui.setElements(uies);
		}
	}
	/**
	 * 复制页面元素值
	 * @author slx
	 * @date 2011 5 9 15:31:27
	 * @modifyNote
	 * @param element
	 * @param pe
	 */
	private void copyPremUIElement2UIElement(UIElement element,PermissionUIElement pe){
		element.setId(pe.getUiElement().getElementId());
		element.setDescription(pe.getUiElement().getDescription());
		element.setVisibleParameterName(pe.getUiElement().getVisibleParameterName());
	}
	
	@Override
	@WebResult(name="functions")
	@WSDLDocumentation("<p>获得用户在组织内有权使用的某个业务组件的功能列表.</p><p>需要3个参数:</br>1 userID用户主键</br>2 bcCode业务组件编号</br>3 orgID组织主键。</p><p>返回值为功能列表，内包含功能内用户可使用的界面和界面上用户【不可见的界面元素】，请注意是【不】可见的界面元素！<p>")
	public Function[] getUserBCFunctions(
			@WebParam(name="bcCode",partName="业务组件编号") String bcCode, 
			@WebParam(name="userID",partName="用户id") String userID, 
			@WebParam(name="orgID",partName="组织id") String orgID) {
		List<Permission> prms = permissonService.getUserBCPermissions(bcCode, userID, orgID);
		return getFunctionFromPermissions(prms);
	}

}
