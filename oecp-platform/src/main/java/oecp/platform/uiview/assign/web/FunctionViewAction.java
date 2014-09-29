/**
 * oecp-platform - FunctionViewAction.java
 * copyright 2009-2012 OECP www.oecp.cn
 * 创建人:slx	创建时间:2011-11-9上午11:24:02		版本:v1
 * ============================================
 * 修改人：			修改时间:					版本:
 * 修改原因:
 *
 * ============================================
 */
package oecp.platform.uiview.assign.web;

import java.io.IOException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import oecp.framework.exception.BizException;
import oecp.framework.web.JsonResult;
import oecp.platform.bcfunction.eo.Function;
import oecp.platform.print.eo.PrintTemplate;
import oecp.platform.print.service.PrintTemplateService;
import oecp.platform.query.service.QuerySchemeService;
import oecp.platform.query.setting.eo.QueryScheme;
import oecp.platform.uiview.assign.eo.FunctionView;
import oecp.platform.uiview.assign.service.FunctionViewAssignService;
import oecp.platform.uiview.assign.service.FunctionViewService;
import oecp.platform.uiview.assign.service.FunctionViewServiceImpl;
import oecp.platform.uiview.assign.vo.FunctionViewVO;
import oecp.platform.uiview.vo.UIComponentVO;
import oecp.platform.uiview.web.parser.UIViewParser;
import oecp.platform.web.BasePlatformAction;

import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.Namespace;
import org.apache.struts2.convention.annotation.ParentPackage;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import com.alibaba.fastjson.serializer.JSONSerializer;
import com.alibaba.fastjson.serializer.OECPJSONSerializer;
import com.alibaba.fastjson.serializer.PropertyFilter;
import com.alibaba.fastjson.serializer.SerializeWriter;
import com.alibaba.fastjson.serializer.SerializerFeature;

/**
 * 功能UI视图的配置维护和预览
 * 
 * @author slx
 * @date 2011-11-9
 */
@Controller
@Scope("prototype")
@ParentPackage("default-package")
@Namespace("/funview")
public class FunctionViewAction extends BasePlatformAction {
	private static final long serialVersionUID = 1L;
	@Resource
	private UIViewParser parser;
	@Resource(name="functionViewServiceImpl")
	private FunctionViewService fvservice;
	@Resource
	private PrintTemplateService printTemplateService;
	@Resource
	private FunctionViewAssignService functionViewAssignService;
	@Resource
	private QuerySchemeService querySchemeService;
	
	private FunctionViewVO viewvo;
	private String functionid;
	private List<FunctionView> views;
	private String viewId;
	private String printTemplateIds;
	
	@Action("list")
	public String list() {
		List<FunctionView> views = fvservice.getViewsByFunctionID(functionid, this.getOnlineUser().getLoginedOrg().getId());
		JsonResult jr = new JsonResult(views.size(), views);
		jr.setContainFields(new String[] { "id", "viewcode", "viewname", "shared", "org.name","org.id", "func.id", "func.code" });
		this.setJsonString(jr.toJSONString());
		return SUCCESS;
	}

	@Action("load")
	public String load() throws BizException {
		viewvo = fvservice.findViewVO(viewvo.getId());

		viewvo.getFunc().setChildren(null);
		String viewjson = this.toJsonExclude(viewvo,new HashMap<Class<?>, String[]>(){
			private static final long serialVersionUID = 1L;
		{
			put(Function.class, new String[]{"id"});
			put(oecp.platform.org.eo.Organization.class, new String[]{"id"});
		}});
		this.setJsonString("{success:true,msg:'加载成功',result:".concat(viewjson).concat("}"));
		return SUCCESS;
	}
	
	public String toJsonExclude(final Object obj, final Map<Class<?>,String[]> expfields) {
		// 组装Json字符串
		SerializeWriter out = new SerializeWriter();
		JSONSerializer serializer = new OECPJSONSerializer(out); 
		serializer.config(SerializerFeature.WriteMapNullValue, true);
		serializer.config(SerializerFeature.WriteEnumUsingToString, true);
		serializer.config(SerializerFeature.UseISO8601DateFormat, true);
		if (expfields != null ) {
			PropertyFilter filter = new PropertyFilter() {// 属性过滤器
				public boolean apply(Object source, String name, Object value) {
					if(source instanceof FunctionView && "printTemplates".equals(name)){
						return false;
					}
					Iterator<Class<?>> classes = expfields.keySet().iterator();
					while (classes.hasNext()) {
						Class<?> clazz = classes.next();
						if(clazz.isAssignableFrom(source.getClass())){
							String[] fields = expfields.get(clazz);
							if(fields == null){
								return false;
							}
							boolean hasf = false;
							for (String field : fields) {
								if (field.equals(name)) {
									hasf =  true;
									break;
								}
							}
							return hasf;
						}
					}
					
					return true;
				}
			};
			serializer.getPropertyFilters().add(filter);
		}
		serializer.write(obj);
		return out.toString();
	}

	/**
	 * 预览当前设置的界面 </br> 读取view中的头、体、尾，生成拼接成ext代码。
	 * 
	 * @return
	 * @throws ClassNotFoundException
	 * @throws IOException
	 */
	@Action("preview")
	public String preview() throws IOException, ClassNotFoundException {
		UIComponentVO uivo = ((FunctionViewServiceImpl) fvservice).getFormPanelVOFormFuncView(viewvo);
		setJsonString("{success:true,msg:'预览加载成功！',result:".concat(parser.parserToSourceCode(uivo).concat("}")));
		return SUCCESS;
	}
	
	@Action("formpreview")
	public String previewForm() throws BizException, IOException, ClassNotFoundException {
		UIComponentVO uivo = (fvservice).getFormPanelByViewID(viewvo.getId());
		setJsonString("{success:true,msg:'预览加载成功！',result:".concat(parser.parserToSourceCode(uivo).concat("}")));
		return SUCCESS;
	}
	
	@Action("listpreview")
	public String previewList() throws BizException, IOException, ClassNotFoundException {
		UIComponentVO uivo = (fvservice).getListPanelByViewID(viewvo.getId());
		setJsonString("{success:true,msg:'预览加载成功！',result:".concat(parser.parserToSourceCode(uivo).concat("}")));
		return SUCCESS;
	}

	@Action("save")
	public String save() throws BizException {
		fvservice.saveViewVO(viewvo);
		setJsonString("{success:true,msg:'保存成功！'}");
		return SUCCESS;
	}

	@Action("clone")
	public String copy() throws BizException {
		System.out.println(viewvo);
		FunctionViewVO cloneview = fvservice.cloneViewByID(viewvo.getId(), viewvo.getViewcode(), viewvo.getViewname(), getOnlineUser().getLoginedOrg(), viewvo.getShared());
		JsonResult jr = new JsonResult(true,"复制成功！",cloneview);
		jr.setContainFields(new String[] { "id", "viewcode", "viewname", "shared", "org.name" });
		this.setJsonString(jr.toJSONString());
		return SUCCESS;
	}

	@Action("del")
	public String delete() throws BizException {
		fvservice.delete(viewvo.getId());
		setJsonString("{success:true,msg:'删除成功！'}");
		return SUCCESS;
	}

	/**
	 * 获取视图对应的打印模板
	 * @author Administrator
	 * @date 2012-3-28上午9:36:08
	 * @return
	 * @throws BizException 
	 */
	@Action("listPrintTemplate")
	public String getPrintTemplates() throws BizException {
		JsonResult jr = new JsonResult("");
		List<PrintTemplate>  all_print_list =printTemplateService.getTemplatesByFunId(functionid);
		jr.setResult(all_print_list);
		jr.setContainFields(new String[]{"id","name"});
		setJsonString(jr.toJSONString());
		return SUCCESS;
	}
	/**
	 * 获取已分配的打印模板
	 * @author wangliang
	 * @date 2012-3-29下午3:59:21
	 * @return
	 * @throws BizException
	 */
	@Action("checkedPrintTemplate")
	public String getCheckedPrintTemplate() throws BizException{
		List<PrintTemplate> list = printTemplateService.getHasCheckedTemplates(viewId);
		JsonResult jr = new JsonResult(list);
		jr.setContainFields(new String[]{"id","name",});
		setJsonString(jr.toJSONString());
		return SUCCESS;
	}
	
	@Action("getPrintTemplate")
	public String getPrintTemplate() throws BizException{
		PrintTemplate template = printTemplateService.find(printTemplateIds);
		JsonResult jr = new JsonResult(template);
		jr.setContainFields(new String[]{"id","name","vtemplate"});
		setJsonString(jr.toJSONString());
		return SUCCESS;
	}
	/**
	 *  保存视图对应打印模板
	 * @author wangliang
	 * @date 2012-3-28上午9:37:01
	 * @return
	 * @throws BizException 
	 */
	@Action("savePrintTemplate")
	public String savePrintTemplates() throws BizException{
		fvservice.saveViewPrintTemplates(viewId, printTemplateIds);
		JsonResult jr = new JsonResult("");
		setJsonString(jr.toJSONString());
		return SUCCESS;
	}
	
	/**
	 * 获得绑定到UIView上的查询方案
	 * @author songlixiao
	 * @date 2014年3月10日上午9:51:45
	 * @return
	 * @throws BizException
	 * @throws SecurityException 
	 * @throws NoSuchFieldException 
	 */
	@Action("getQS")
	public String getQueryScheme() throws BizException, NoSuchFieldException, SecurityException {
		QueryScheme qs = functionViewAssignService.getQuerySchemeByViewId(viewId);
		Map operator = querySchemeService.getOperator();
		Map val = new HashMap();
		val.put("conditions", qs.getOtherconditions());
		val.put("commoncondConditions", qs.getCommonconditions());
		val.put("operator",operator);
		JsonResult jr = new JsonResult(val);
		setJsonString(jr.toJSONString());
		return SUCCESS;
	}
	
	public FunctionViewVO getViewvo() {
		return viewvo;
	}

	public void setViewvo(FunctionViewVO viewvo) {
		this.viewvo = viewvo;
	}

	public void setParser(UIViewParser parser) {
		this.parser = parser;
	}

	public void setFvservice(FunctionViewService fvservice) {
		this.fvservice = fvservice;
	}

	public String getFunctionid() {
		return functionid;
	}

	public void setFunctionid(String functionid) {
		this.functionid = functionid;
	}

	public List<FunctionView> getViews() {
		return views;
	}

	public void setViews(List<FunctionView> views) {
		this.views = views;
	}

	public String getViewId() {
		return viewId;
	}

	public void setViewId(String viewId) {
		this.viewId = viewId;
	}

	public void setPrintTemplateService(PrintTemplateService printTemplateService) {
		this.printTemplateService = printTemplateService;
	}

	public String getPrintTemplateIds() {
		return printTemplateIds;
	}

	public void setPrintTemplateIds(String printTemplateIds) {
		this.printTemplateIds = printTemplateIds;
	}

	public void setFunctionViewAssignService(FunctionViewAssignService functionViewAssignService) {
		this.functionViewAssignService = functionViewAssignService;
	}

	public void setQuerySchemeService(QuerySchemeService querySchemeService) {
		this.querySchemeService = querySchemeService;
	}
}
