package oecp.platform.maindata.web;

import java.util.List;

import javax.annotation.Resource;

import oecp.bcbase.enums.BillState;
import oecp.framework.exception.BizException;
import oecp.framework.util.FastJsonUtils;
import oecp.platform.maindata.eo.MDResource;
import oecp.platform.maindata.eo.MDResourceField;
import oecp.platform.maindata.service.MDResourceFieldService;
import oecp.platform.maindata.service.MDResourceService;
import oecp.platform.web.BasePlatformAction;

import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.Namespace;
import org.apache.struts2.convention.annotation.ParentPackage;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import com.alibaba.fastjson.JSON;

/**
 * 
 * @author liujt
 * @date 2011-10-31 上午9:16:45
 * @version 1.0
 * 
 */
@Controller("MDResourceAction")
@Scope("prototype")
@ParentPackage("default-package")
@Namespace("/mdresource")
public class MDResourceAction extends BasePlatformAction {

	private static final long serialVersionUID = 8461234600260598596L;

	@Resource
	private MDResourceService mdResourceService;
	@Resource
	private MDResourceFieldService mdResourceFieldService;

	private String id;
	private String code;
	private MDResource mdResource;
	private List<MDResourceField> fields;
	// 需要删除的主数据资源id数组
	private String[] ids;
	/**
	 * 获取主数据资源
	 * 
	 * @author liujt
	 * @date 2011-10-31上午9:18:58
	 * @return
	 */
	@Action("getMDResources")
	public String getMDResources() {
		List<MDResource> list = null;
		if(code==null || "".equals(code)){
			list = mdResourceService.getMDResources(start, limit);
		}else{
			code = "%"+code.trim()+"%";
			list = mdResourceService.queryMDResource(code, start, limit);
		}
		long totalCounts = mdResourceService.getTotalCount();

		String json = FastJsonUtils.toJson(list, new String[] { "id", "code",
				"name", "eoClassName", "tableName" });
		json = "{success:true,totalCounts:" + totalCounts + ",result:" + json
				+ "}";
		setJsonString(json);
		return SUCCESS;
	}

	/**
	 * 获取主数据资源列
	 * @author liujt
	 * @date 2011-11-4下午12:23:00
	 * @return
	 */
	@Action("getMDResourceFields")
	public String getMDResourceFields() {
		List<MDResourceField> list = mdResourceFieldService
				.getMDResourceFields(id);
		for (MDResourceField mdf : list) {
			if(mdf.getRelatedMD() == null){
				mdf.setRelatedMD(new MDResource());
			}
		}
		String json = FastJsonUtils.toJson(list, new String[] {"id", "name","dispName","uiClass","isDisplay","relatedMD"});
//		json = json.replaceAll("\"relatedMD\":null",
//				"\"relatedMD\":{\"name\":null}");
		json = "{success:true,result:" + json + "}";
		setJsonString(json);
		return SUCCESS;
	}
	
	@Action("saveMDResource")
	public String saveMDResource() throws BizException{
		mdResourceService.saveMDResource(mdResource, fields);
		setJsonString("{success : true,msg : '保存成功'}");
		return SUCCESS;
	}
	
	@Action(value="deleteMDResource")
	public String deleteMDResource() throws BizException{
		if (ids != null && ids.length > 0) {
			for(String id : ids){
				mdResourceService.delete(id);
			}
			setJsonString("{success : true , msg : '删除成功'}");
		}
		return SUCCESS;
	}
	
	@Action("queryMDResource")
	public String queryMDResource() throws BizException {

		MDResource mdResource = mdResourceService.find_full(id);
		String head = FastJsonUtils.toJson(mdResource,new String[]{"id", "code",
				"name", "eoClassName", "tableName"});
		for (MDResourceField mdf : mdResource.getFields()) {
			if(mdf.getRelatedMD() == null){
				mdf.setRelatedMD(new MDResource());
			}
		}
		String body = FastJsonUtils.toJson(mdResource.getFields(),new String[]{"id", "name","dispName","uiClass","isDisplay","relatedMD"});
		
		String json = head.substring(0, head.length() - 1) + ",fields:" + body + "}";
//		json = json.replaceAll("\"relatedMD\":null",
//				"\"relatedMD\":{\"name\":null}");
		json = "{success:true,result:" + json + "}";
		setJsonString(json);
		return SUCCESS;
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

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public MDResource getMdResource() {
		return mdResource;
	}

	public void setMdResource(MDResource mdResource) {
		this.mdResource = mdResource;
	}

	public List<MDResourceField> getFields() {
		return fields;
	}

	public void setFields(List<MDResourceField> fields) {
		this.fields = fields;
	}

	public String[] getIds() {
		return ids;
	}

	public void setIds(String[] ids) {
		this.ids = ids;
	}


}
