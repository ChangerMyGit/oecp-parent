package oecp.platform.ref.web;

import java.util.List;

import javax.annotation.Resource;

import oecp.framework.exception.BizException;
import oecp.framework.util.FastJsonUtils;
import oecp.platform.api.datapermission.DataPermissionSQLService;
import oecp.platform.app.service.RefService;
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

/**
 * Ref控件服务
 * 
 * @author liujt
 * @date 2011-10-19 下午4:39:36
 * @version 1.0
 */
@Controller
@Scope("prototype")
@ParentPackage("default-package")
@Namespace("/app/ref")
public class RefAction extends BasePlatformAction {

	private static final long serialVersionUID = 1L;
	@Resource
	private MDResourceService mdResourceService;
	@Resource
	private MDResourceFieldService mdResourceFieldService;
	@Resource
	private DataPermissionSQLService dataPermissionSQLService;
	@Resource
	private RefService refService;
	// 实体名
	private String entityName;
	// 组件编号
	private String functionCode;
	// 字段名
	private String fieldName;
	// 查询值
	private String query;
	// 查询字段
	private String codeField;
	// 过滤
	private List<QueryFilter> filter;

	/**
	 * 获得Ref控件的数据
	 * 
	 * @author liujt
	 * @date 2011-10-19下午4:40:30
	 * @return
	 * @throws BizException
	 */
	@SuppressWarnings("rawtypes")
	@Action(value = "getRefDatas")
	public String getRefDatas() throws BizException {
		boolean whereflag = false;
		String userId = getOnlineUser().getUser().getId();
		String orgId = getOnlineUser().getLoginedOrg().getId();
		MDResource mdResource = mdResourceService
				.getMdResourceByEOClassName(entityName);
		String datasHQL = "SELECT o FROM " + entityName + " o ";
		String totalCountsSQL = "SELECT COUNT(*) FROM " + entityName + " o ";
		if (fieldName != null && !"".equals(fieldName)) {
			String conditionHQL = dataPermissionSQLService
					.getDataPermissionSQL(userId, orgId, functionCode,
							fieldName, "o");
			if (conditionHQL != null) {
				datasHQL = datasHQL + " WHERE " + conditionHQL;
				totalCountsSQL = totalCountsSQL + " WHERE " + conditionHQL;
				whereflag = true;
			}
		}
		if (!StringUtils.isEmpty(query)) {// 判断过滤查询是否有值
			datasHQL = datasHQL + (whereflag ? " AND o." : " WHERE o.")
					+ codeField + " LIKE ('%" + query + "%')";
			whereflag = true;
		}
		if (filter != null && filter.size() > 0) {
			for (QueryFilter qf : filter) {
				datasHQL = datasHQL + (whereflag ? " AND o." : " WHERE o.")
						+ qf.buildWhereHql();
				whereflag = true;
			}
		}
		long totalCounts = refService.getTotalCount(totalCountsSQL);
		List list = refService.getDatas(datasHQL, start, limit);
		List<MDResourceField> mdResourceFields = mdResourceFieldService
				.getDisplayMDFields(mdResource.getId(), true);

		String[] strArr = new String[mdResourceFields.size() + 1];
		strArr[0] = "id";
		for (int i = 0; i < mdResourceFields.size(); i++) {
			strArr[i + 1] = mdResourceFields.get(i).getName();
		}
		String json = FastJsonUtils.toJson(list, strArr);
		json = "{success:true,totalCounts:" + totalCounts + ",result:" + json
				+ "}";
		setJsonString(json);

		return SUCCESS;
	}

	/**
	 * 获得Ref控件的显示字段
	 * 
	 * @author liujt
	 * @date 2011-10-20上午10:03:30
	 * @return
	 */
	@Action(value = "getRefFields")
	public String getRefFields() {
		MDResource mdResource = mdResourceService
				.getMdResourceByEOClassName(entityName);
		List<MDResourceField> mdResourceFields = mdResourceFieldService
				.getDisplayMDFields(mdResource.getId(), true);
		String json = FastJsonUtils.toJson(mdResourceFields, new String[] {
				"name", "dispName" });
		setJsonString(json);
		return SUCCESS;
	}

	public String getEntityName() {
		return entityName;
	}

	public void setEntityName(String entityName) {
		this.entityName = entityName;
	}

	public String getFunctionCode() {
		return functionCode;
	}

	public void setFunctionCode(String functionCode) {
		this.functionCode = functionCode;
	}

	public String getFieldName() {
		return fieldName;
	}

	public void setFieldName(String fieldName) {
		this.fieldName = fieldName;
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

	public DataPermissionSQLService getDataPermissionSQLService() {
		return dataPermissionSQLService;
	}

	public void setDataPermissionSQLService(
			DataPermissionSQLService dataPermissionSQLService) {
		this.dataPermissionSQLService = dataPermissionSQLService;
	}

	public RefService getRefService() {
		return refService;
	}

	public void setRefService(RefService refService) {
		this.refService = refService;
	}

	public String getQuery() {
		return query;
	}

	public void setQuery(String query) {
		this.query = query;
	}

	public String getCodeField() {
		return codeField;
	}

	public void setCodeField(String codeField) {
		this.codeField = codeField;
	}

	public List<QueryFilter> getFilter() {
		return filter;
	}

	public void setFilter(List<QueryFilter> filter) {
		this.filter = filter;
	}

}
