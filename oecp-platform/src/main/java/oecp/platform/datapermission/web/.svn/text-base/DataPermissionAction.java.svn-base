package oecp.platform.datapermission.web;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;

import oecp.framework.exception.BizException;
import oecp.framework.util.FastJsonUtils;
import oecp.platform.bcfunction.eo.Function;
import oecp.platform.bcfunction.eo.FunctionField;
import oecp.platform.bcfunction.service.BcFunctionService;
import oecp.platform.datapermission.eo.DataDiscretePermission;
import oecp.platform.datapermission.eo.DataPermission;
import oecp.platform.datapermission.service.DataDiscretePermissionService;
import oecp.platform.datapermission.service.DataPermissionService;
import oecp.platform.maindata.eo.MDResource;
import oecp.platform.maindata.eo.MDResourceField;
import oecp.platform.maindata.service.MDResourceFieldService;
import oecp.platform.maindata.service.MDResourceService;
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
 * 数据权限管理Action
 * 
 * @author liujingtao
 * @date 2011-6-24 上午09:50:16
 * @version 1.0
 */
@Controller("DataPermissionAction")
@Scope("prototype")
@ParentPackage("default-package")
@Namespace("/datapermission")
public class DataPermissionAction extends BasePlatformAction {
	private static final long serialVersionUID = -745705305676653216L;
	@Resource
	private MDResourceService mdResourceService;
	@Resource
	private MDResourceFieldService mdResourceFieldService;
	@Resource
	private DataPermissionService dataPermissionService;
	@Resource
	private DataDiscretePermissionService dataDiscretePermissionService;
	@Resource
	private BcFunctionService bcFunctionService;

	private List<DataPermission> dplist;

	private List<String> idlist;
	// 功能Id
	private String funId;
	
	private String[] addDatas;
	
	private String[] delDatas;

	/**
	 * 所有主数据资源详细信息
	 * 
	 * @author liujingtao
	 * @date 2011 6 24 10:43:44
	 * @modifyNote
	 * @return
	 * @throws BizException
	 */
	@Action("getmdResources")
	public String getmdResources() throws BizException {
		String funId = getRequest().getParameter("funId");
		String json = "";
		if (funId == null || "none".equals(funId) || "".equals(funId)) {
			List<MDResource> list = mdResourceService.getAllMDResources();
			json = FastJsonUtils.toJson(list,new String[]{"id","code","name","eoClassName","tableName","jsClassName"});
		} else {
			List<MDResource> list = new ArrayList<MDResource>();
			if (funId.startsWith("fun_")) {
				funId = funId.substring(4);
				Function fun = bcFunctionService.find_full(funId);
				;
				if (fun.getFunctionFields() != null) {
					for (FunctionField field : fun.getFunctionFields()) {
						if (field.getMdType() != null) {
							list.add(field.getMdType());
						}
					}
				}
			}
			json = FastJsonUtils.toJson(list,new String[]{"id","code","name","eoClassName","tableName","jsClassName"});
		}
		setJsonString(json);
		return SUCCESS;
	}

	/**
	 * 所有主数据资源列信息
	 * 
	 * @author liujingtao
	 * @date 2011 6 27 11:27:17
	 * @modifyNote
	 * @return
	 * @throws BizException
	 */
	@Action("getmdResourceFields")
	public String getmdResourceFields() throws BizException {
		String mdResourceid = getRequest().getParameter("mdResourceid");
		List<MDResourceField> list = mdResourceFieldService
				.getMDResourceFields(mdResourceid);
		String json = FastJsonUtils.toJson(list, new String[] { "id","name",
				"dispName","uiClass","isDisplay" });

		setJsonString(json);
		return SUCCESS;
	}

	/**
	 * 指定岗位资源的数据权限列表
	 * 
	 * @author liujingtao
	 * @date 2011 6 27 20:29:41
	 * @modifyNote
	 * @return
	 * @throws BizException
	 */
	@Action("getDataPermissions")
	public String getDataPermissions() throws BizException {
		String postId = getRequest().getParameter("postId");
		String mdResourceId = getRequest().getParameter("mdResourceId");
		String functionId = parseFunId();
		List<DataPermission> list = dataPermissionService.getDataPermissions(
				postId, mdResourceId, functionId);
		String json = FastJsonUtils.toJson(list, new String[] { "id",
				"operator", "value", "mdField","name", "function" });
		setJsonString(json);
		return SUCCESS;
	}

	/**
	 * 保存指定岗位资源的数据权限列表
	 * 
	 * @author liujingtao
	 * @date 2011 6 28 11:30:58
	 * @modifyNote
	 * @throws BizException
	 */
	@Transactional
	@Action("updateDataPermissions")
	public String updateDataPermissions() throws BizException {
		try {
			String postId = getRequest().getParameter("postId");
			String mdResourceId = getRequest().getParameter("mdResourceId");
			dealDplist();
			String functionId = parseFunId();
			dataPermissionService.saveDataPermissions(dplist, postId,
					mdResourceId, functionId);

			setJsonString("{success:true,msg:'保存成功！'}");
		} catch (Exception ex) {
			ex.printStackTrace();
			setJsonString("{success:false,msg:'保存失败，请联系管理员！'}");
		}
		return SUCCESS;
	}

	@Action("getAllmdData")
	public String getAllmdData() throws BizException, ClassNotFoundException {
		// String postId = getRequest().getParameter("postId");
		String mdResourceId = getRequest().getParameter("mdResourceId");
		int start = Integer.parseInt(getRequest().getParameter("start"));
		int limit = Integer.parseInt(getRequest().getParameter("limit"));
		MDResource mdResource = mdResourceService.find(mdResourceId);
		String hql = "from " + mdResource.getEoClassName();
		List list = dataDiscretePermissionService.getAllClassDatas(hql, start,
				limit);

		hql = "select count(*) from " + mdResource.getEoClassName();
		long totalCount = dataDiscretePermissionService.getTotalCount(hql);

		List<MDResourceField> mdrfs = mdResourceFieldService
				.getMDResourceFields(mdResourceId);

		String[] strarr = new String[mdrfs.size() + 1];
		strarr[0] = "id";
		for (int i = 0; i < mdrfs.size(); i++) {
			strarr[i + 1] = mdrfs.get(i).getName();
		}
		String json = FastJsonUtils.toJson(list, strarr);
		json = "{success:true,totalCounts:" + totalCount + ",result:" + json
				+ "}";
		setJsonString(json);
		return SUCCESS;
	}

	/**
	 * 查找当前页Checked的数据
	 * 
	 * @author liujt
	 * @date 2011-7-4上午10:26:43
	 * @return
	 * @throws BizException
	 */
	@Action("getCheckedDataIds")
	public String getCheckedDataIds() throws BizException {
		String postId = getRequest().getParameter("postId");
		String mdResourceId = getRequest().getParameter("mdResourceId");
		String functionId = parseFunId();
		List<DataDiscretePermission> dataDisList = dataDiscretePermissionService
				.getDatas(postId, mdResourceId,functionId);
		List<String> disIdList = new ArrayList<String>();
		as: for (String id : idlist) {
			for (DataDiscretePermission dataDis : dataDisList) {
				if (id.equals(dataDis.getDataid())) {
					disIdList.add(id);
					continue as;
				}
			}
		}
		String json = FastJsonUtils
				.toJson(disIdList, new String[] { "dataid" });
		setJsonString(json);
		return SUCCESS;
	}

	/**
	 * 保存离散数据权限
	 * @author liujt
	 * @date 2011-9-1上午10:49:51
	 * @return
	 * @throws BizException
	 */
	@Action("saveDatas")
	public String saveDatas() throws BizException {
		try{
			String postId = getRequest().getParameter("postId");
			String mdResourceId = getRequest().getParameter("mdResourceId");
			dataDiscretePermissionService.saveDatasDisPermission(postId,
					mdResourceId, addDatas,delDatas, parseFunId());
			setJsonString("{success:true,msg:'保存成功！'}");
		}catch(Exception e){
			e.printStackTrace();
			setJsonString("{success:false,msg:'保存失败，请联系管理员！'}");
		}
		return SUCCESS;
	}
	
	/**
	 * 保存离散数据权限
	 * 
	 * @author liujt
	 * @date 2011-7-4下午04:21:36
	 * @return
	 * @throws BizException
	 */
	@Action("saveData")
	public String saveData() throws BizException {
		String postId = getRequest().getParameter("postId");
		String mdResourceId = getRequest().getParameter("mdResourceId");
		String dataId = getRequest().getParameter("dataId");
		dataDiscretePermissionService.saveDataDisPermission(postId,
				mdResourceId, dataId, parseFunId());

		return SUCCESS;
	}

	/**
	 * 删除离散数据权限
	 * 
	 * @author liujt
	 * @date 2011-7-4下午04:53:18
	 * @return
	 * @throws BizException
	 */
	@Action("deleteData")
	public String deleteData() throws BizException {
		String postId = getRequest().getParameter("postId");
		String mdResourceId = getRequest().getParameter("mdResourceId");
		String dataId = getRequest().getParameter("dataId");
		dataDiscretePermissionService.deleteDataDisPermission(postId,
				mdResourceId, dataId, parseFunId());

		return SUCCESS;
	}
	
	/**
	 * 
	 * @author liujt
	 * @date 2011-9-2上午09:57:01
	 * @return
	 * @throws BizException
	 */
	@Action("getFormItems")
	public String getFormItems() throws BizException {
		String mdId = getRequest().getParameter("mdId");
		List<MDResourceField> mdResourceFields = mdResourceFieldService.getDisplayMDFields(mdId,true);
		String json = FastJsonUtils.toJson(mdResourceFields, new String[]{"name","dispName","isDisplay","uiClass"});

		setJsonString(json);
		return SUCCESS;
	}

	// 转换功能主键
	private String parseFunId() {
		if (StringUtils.isEmpty(funId) || "none".equals(funId)) {
			return null;
		} else {
			return funId.substring(4);
		}
	}
	
	private void dealDplist(){
		String functionId = parseFunId();
		if(functionId == null){
			return;
		}
		for(DataPermission dp : dplist){
			Function fun = new Function();
			fun.setId(functionId);
			dp.setFunction(fun);
		}
	}

	public MDResourceService getMdResourceService() {
		return mdResourceService;
	}

	public void setMdResourceService(MDResourceService mdResourceService) {
		this.mdResourceService = mdResourceService;
	}

	public MDResourceFieldService getMdResourceFieldService() {
		return mdResourceFieldService;
	}

	public void setMdResourceFieldService(
			MDResourceFieldService mdResourceFieldService) {
		this.mdResourceFieldService = mdResourceFieldService;
	}

	public DataPermissionService getDataPermissionService() {
		return dataPermissionService;
	}

	public void setDataPermissionService(
			DataPermissionService dataPermissionService) {
		this.dataPermissionService = dataPermissionService;
	}

	public List<DataPermission> getDplist() {
		return dplist;
	}

	public void setDplist(List<DataPermission> dplist) {
		this.dplist = dplist;
	}

	public DataDiscretePermissionService getDataDiscretePermissionService() {
		return dataDiscretePermissionService;
	}

	public void setDataDiscretePermissionService(
			DataDiscretePermissionService dataDiscretePermissionService) {
		this.dataDiscretePermissionService = dataDiscretePermissionService;
	}

	public List<String> getIdlist() {
		return idlist;
	}

	public void setIdlist(List<String> idlist) {
		this.idlist = idlist;
	}

	public BcFunctionService getBcFunctionService() {
		return bcFunctionService;
	}

	public void setBcFunctionService(BcFunctionService bcFunctionService) {
		this.bcFunctionService = bcFunctionService;
	}

	public String getFunId() {
		return funId;
	}

	public void setFunId(String funId) {
		this.funId = funId;
	}

	public String[] getAddDatas() {
		return addDatas;
	}

	public void setAddDatas(String[] addDatas) {
		this.addDatas = addDatas;
	}

	public String[] getDelDatas() {
		return delDatas;
	}

	public void setDelDatas(String[] delDatas) {
		this.delDatas = delDatas;
	}

}
