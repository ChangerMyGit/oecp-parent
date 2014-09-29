package oecp.platform.query.setting.web;

import java.beans.IntrospectionException;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.util.List;

import javax.annotation.Resource;
import javax.persistence.Transient;

import oecp.framework.entity.base.BaseEO;
import oecp.framework.exception.BizException;
import oecp.framework.util.FastJsonUtils;
import oecp.framework.vo.base.DataVO;
import oecp.framework.web.JsonResult;
import oecp.platform.bcfunction.service.FunctionQSService;
import oecp.platform.query.service.QuerySchemeService;
import oecp.platform.query.setting.eo.QueryScheme;
import oecp.platform.web.BasePlatformAction;

import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.Namespace;
import org.apache.struts2.convention.annotation.ParentPackage;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import com.alibaba.fastjson.serializer.PropertyFilter;

/**
 * 查询方案设置Action
 *
 * @author songlixiao  
 * @date 2014年1月28日 上午11:09:39 
 * @version 1.0
 *
 */
@Controller
@Scope("prototype")
@ParentPackage("default-package")
@Namespace("/query/setting")
public class QueryConditionSettingAction extends BasePlatformAction{

	private static final long serialVersionUID = 1L;
	@Resource(name="functionQSService")
	private FunctionQSService fqsService;
	
	@Resource(name="querySchemeService")
	private QuerySchemeService qsService;

	private QueryScheme qs; // 功能查询方案

	private String funcId; // 功能id
	
	private String qsId; // 查询方案id
	
	/** =========== Actions ===========*/
	/**
	 * 列出一个业务功能下所有的查询方案
	 * @author songlixiao
	 * @date 2014年1月28日上午11:26:17
	 * @return
	 */
	@Action("list")
	public String listQuerySchemes(){
		List<QueryScheme> qss = fqsService.getQuerySchemesByFuncId(funcId);
		JsonResult jr = new JsonResult(qss);
		jr.setContainFields(new String[]{"id","code","name"});
		setJsonString(jr.toJSONString());
		return SUCCESS;
	}
	
	/**
	 * 加载单个查询方案的数据
	 * @author songlixiao
	 * @date 2014年1月28日上午11:28:44
	 * @return
	 */
	@Action("load")
	public String loadQueryScheme() throws BizException{
		QueryScheme qs = qsService.find_full(qsId);
		JsonResult jr = new JsonResult(qs);
//		jr.setContainFields(new String[]{"id","code","name","fixedconditions","commonconditions","otherconditions"
//				,"fixedconditions.id","fixedconditions.field","fixedconditions.dispname","fixedconditions.operators","fixedconditions.defaultvalue","fixedconditions.fieldType","fixedconditions.required","fixedconditions.editorcfg",
//				});
		String json = FastJsonUtils.toJson(jr,new PropertyFilter() {
			@Override
			public boolean apply(Object source, String name, Object value) {
				if(source instanceof BaseEO){
					if(source instanceof DataVO){
						return true;
					}
					PropertyDescriptor[] propertyDescriptors;
					try {
						propertyDescriptors = Introspector.getBeanInfo(source.getClass())
								.getPropertyDescriptors();
						for (int i = 0; i < propertyDescriptors.length; i++) {
							if(name.equals(propertyDescriptors[i].getName())){
								boolean istransient = propertyDescriptors[i].getReadMethod().isAnnotationPresent(Transient.class);
								return !istransient;
							}
						}
					} catch (IntrospectionException e) {
						e.printStackTrace();
					}
					return false;
				}else{
					return true;
				}
			}
		});
		setJsonString(json);
		return SUCCESS;
	}
	/**
	 * 保存查询方案
	 * @author songlixiao
	 * @date 2014年1月28日上午11:37:07
	 * @return
	 * @throws BizException 
	 */
	@Action("save")
	public String saveQueryScheme() throws BizException{
		fqsService.saveQueryScheme(funcId, qs);
		JsonResult jr = new JsonResult(true,"保存成功！");
		setJsonString(jr.toJSONString());
		return SUCCESS;
	}
	
	/**
	 * 删除查询方案
	 * @author songlixiao
	 * @date 2014年1月28日上午11:39:41
	 * @return
	 * @throws BizException
	 */
	@Action("del")
	public String delQueryScheme() throws BizException{
		fqsService.deleteQueryScheme(funcId, qsId);
		JsonResult jr = new JsonResult(true,"删除成功！");
		setJsonString(jr.toJSONString());
		return SUCCESS;
	}
	
	/** =========== getters & setters ===========*/
	public QueryScheme getQs() {
		return qs;
	}
	
	public void setQs(QueryScheme qs) {
		this.qs = qs;
	}

	public String getQsId() {
		return qsId;
	}

	public void setQsId(String qsId) {
		this.qsId = qsId;
	}

	public String getFuncId() {
		return funcId;
	}

	public void setFuncId(String funcId) {
		this.funcId = funcId;
	}

	public void setFqsService(FunctionQSService fqsService) {
		this.fqsService = fqsService;
	}

	public void setQsService(QuerySchemeService qsService) {
		this.qsService = qsService;
	}
}
