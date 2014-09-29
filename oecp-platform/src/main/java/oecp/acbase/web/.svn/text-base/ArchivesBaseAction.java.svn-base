/**
 * oecp-platform - ArchivesBaseAction.java
 * copyright 2009-2012 OECP www.oecp.cn
 * 创建人:Administrator	创建时间:下午2:59:27		版本:v1
 * ============================================
 * 修改人：			修改时间:					版本:
 * 修改原因:
 *
 * ============================================
 */

package oecp.acbase.web;

import java.util.LinkedHashMap;
import java.util.List;

import oecp.acbase.eo.BaseMasArchivesEO;
import oecp.acbase.service.ArchivesBaseService;
import oecp.bcbase.utils.BizServiceHelper;
import oecp.framework.dao.QueryCondition;
import oecp.framework.dao.QueryResult;
import oecp.framework.exception.BizException;
import oecp.framework.util.FastJsonUtils;
import oecp.framework.web.JsonResult;
import oecp.platform.user.eo.User;
import oecp.platform.web.BasePlatformAction;

import org.apache.commons.lang.StringUtils;
import org.apache.struts2.convention.annotation.Action;

/** 
 *
 * @author luanyoubo  
 * @date 2014年2月25日 下午2:59:27 
 * @version 1.0
 *  
 */
public abstract class ArchivesBaseAction<T extends BaseMasArchivesEO> extends BasePlatformAction {
	private static final long serialVersionUID = 1L;
	/** 功能编号 **/
	protected String functionCode;
	/** 单据ID **/
	protected String id;
	private String[] ids;
	/** 档案 **/
	protected T bill;
	/** 查询条件 **/
	protected List<QueryCondition> conditions;
	
	public void setFunctionCode(String functionCode) {
		this.functionCode = functionCode;
	}
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String[] getIds() {
		return ids;
	}
	public void setIds(String[] ids) {
		this.ids = ids;
	}
	public T getBill() {
		return bill;
	}
	public void setBill(T bill) {
		this.bill = bill;
	}
	public List<QueryCondition> getConditions() {
		return conditions;
	}
	public void setConditions(List<QueryCondition> conditions) {
		this.conditions = conditions;
	}
	/** 获取业务服务类 **/
	protected abstract ArchivesBaseService<T> getArchivesService();
	
	
	/**
	 * 通用灵活条件查询
	 * @author luanyoubo
	 * @date 2014-2-25
	 * @return
	 * @throws BizException
	 */
	@Action(value="query")
	public String query() throws BizException{
		QueryResult<T> result = getArchivesService().query(getOnlineUser().getUser().getId(),functionCode,getOnlineUser().getLoginedOrg().getId(),conditions, start,limit);
		JsonResult jr = new JsonResult(result.getResultlist());
		if(result.getResultlist() == null || result.getResultlist().size()<1){
			setJsonString("{success : true,msg : '无数据!',result:[]}");
			return SUCCESS;
		}
		jr.setTotalCounts(result.getTotalrecord().intValue());
		jr.setContainFields(result.getResultlist().get(0).getBeanUtility().getAttributeNamesNoLazy());
		jr.setDateFormat("yyyy-MM-dd");
		setJsonString(jr.toJSONString());
		return SUCCESS;
	}
	
	@Action(value="comboQuery")
	public String comboQuery() throws BizException{
		if (bill != null) { // 将code和name都作为搜索条件
			Object v = bill.getAttributeValue("code");
			if (v == null) {
				v = bill.getAttributeValue("name");
				bill.setAttributeValue("code", v);
			} else {
				bill.setAttributeValue("name", v);
			}
		}
		LinkedHashMap<String, String> orderby = new LinkedHashMap<String, String>();
		if(StringUtils.isNotEmpty(getSort())){
			orderby.put(getSort(), StringUtils.isNotEmpty(getDir())?getDir():"ASC");
			orderby.put("id", StringUtils.isNotEmpty(getDir())?getDir():"ASC");
		}
		QueryResult<T> result = getArchivesService().comboQuery(getOnlineUser().getUser().getId(),functionCode,getOnlineUser().getLoginedOrg().getId(),bill, start,limit,orderby);
		JsonResult jr = new JsonResult(result.getResultlist());
		if(result.getResultlist() == null || result.getResultlist().size()<1){
			setJsonString("{success : true,msg : '无数据!',result:[]}");
			return SUCCESS;
		}
		jr.setTotalCounts(result.getTotalrecord().intValue());
		jr.setContainFields(result.getResultlist().get(0).getBeanUtility().getAttributeNamesNoLazy());
		jr.setDateFormat("yyyy-MM-dd");
		setJsonString(jr.toJSONString());
		return SUCCESS;
	}
	
	/**
	 * 
	 * 保存数据
	 */
	@Action(value="save")
	public String save() throws BizException{
		getArchivesService().saveArchives(bill,getOnlineUser().getUser(),functionCode);
		setJsonString("{success : true,msg : '保存成功'}");
		return SUCCESS;
	}
	
	/**
	 * 加载数据
	 * @author slx
	 * @date 2011-12-26
	 * @return
	 * @throws BizException
	 */
	@Action(value="load")
	public String load() throws BizException{
		if(bill == null)
			bill = getArchivesService().find_full(id);
		this.setJsonString("{'success':true,'result':"+FastJsonUtils.billToJson(bill, BizServiceHelper.getItemClasses(bill), null)+"}");
		return SUCCESS;
	}
	
	/**
	 * 
	 * 编辑数据
	 */
	@Action(value="edit")
	public String edit() throws BizException{
		bill = getArchivesService().find_full(id);
		// 只有制单人才允许修改单据
		User onlineuser = getOnlineUser().getUser();
		if(!onlineuser.getId().equals(bill.getCreater())){
			throw new BizException("只有创建人才可以修改档案！");
		}
		return load();
	}
	
	/**
	 * 
	 * 删除数据
	 */
	@Action(value="delete")
	public String delete() throws BizException{
		getArchivesService().deleteArchives(ids,getOnlineUser().getUser(),functionCode);
		setJsonString("{success:true,msg:'删除成功!'}");
		return SUCCESS;
	}
}
