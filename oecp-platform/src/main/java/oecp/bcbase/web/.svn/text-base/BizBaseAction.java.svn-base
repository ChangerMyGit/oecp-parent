package oecp.bcbase.web;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;

import oecp.bcbase.enums.BillState;
import oecp.bcbase.eo.BaseBillEO;
import oecp.bcbase.service.BizBaseService;
import oecp.bcbase.utils.BizServiceHelper;
import oecp.framework.dao.QueryCondition;
import oecp.framework.dao.QueryResult;
import oecp.framework.exception.BizException;
import oecp.framework.util.FastJsonUtils;
import oecp.framework.vo.base.SimpleDataVO;
import oecp.framework.web.JsonResult;
import oecp.platform.api.bpm.ExecutionAPIService;
import oecp.platform.user.eo.User;
import oecp.platform.web.BasePlatformAction;

import org.apache.struts2.convention.annotation.Action;


/**
 * 业务单据父类。
 * 封装单据action的基本业务操作
 * @author slx
 * @date 2011-12-26
 */
public abstract class BizBaseAction<T extends BaseBillEO> extends BasePlatformAction {
	private static final long serialVersionUID = 1L;
	/** 流程服务类 **/
	@Resource(name="executionAPIService")
	protected ExecutionAPIService executionAPIService;
	public void setExecutionAPIService(ExecutionAPIService executionAPIService) {
		this.executionAPIService = executionAPIService;
	}
	/** 功能编号 **/
	protected String functionCode;
	public void setFunctionCode(String functionCode) {
		this.functionCode = functionCode;
	}
	
	/** 单据ID **/
	protected String id;
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	private String[] ids;
	public String[] getIds() {
		return ids;
	}
	public void setIds(String[] ids) {
		this.ids = ids;
	}
	/** 功能单据 **/
	protected T bill;
	public T getBill() {
		return bill;
	}
	public void setBill(T bill) {
		this.bill = bill;
	}
	/** 查询条件 **/
	protected List<QueryCondition> conditions;
	public List<QueryCondition> getConditions() {
		return conditions;
	}
	public void setConditions(List<QueryCondition> conditions) {
		this.conditions = conditions;
	}
	/** 单据业务类型**/
	private String bizTypeID;
	public String getBizTypeID() {
		return bizTypeID;
	}
	public void setBizTypeID(String bizTypeID) {
		this.bizTypeID = bizTypeID;
	}
	/** 前置生成单据所选的DataVO **/
	private List<SimpleDataVO> simpleDataVOs = new ArrayList<SimpleDataVO>();
	public List<SimpleDataVO> getSimpleDataVOs() {
		return simpleDataVOs;
	}
	public void setSimpleDataVOs(List<SimpleDataVO> simpleDataVOs) {
		this.simpleDataVOs = simpleDataVOs;
	}
	
	protected abstract BizBaseService<T> getBillService();
	
	/**
	 * 通用灵活条件查询
	 * @author slx
	 * @date 2011-12-26
	 * @return
	 * @throws BizException
	 */
	@Action(value="query")
	public String query() throws BizException{
		QueryResult<T> result = getBillService().query(getOnlineUser().getUser().getId(),functionCode,getOnlineUser().getLoginedOrg().getId(),conditions, start,limit);
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
		getBillService().saveBill(bill,getOnlineUser().getUser(),functionCode);
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
			bill = getBillService().find_full(id);
		// FastJsonUtils.billToJson(bill, BizServiceHelper.getItemClasses(bill), null);
		this.setJsonString("{'success':true,'result':"+FastJsonUtils.billToJson(bill, BizServiceHelper.getItemClasses(bill), null)+"}");
		return SUCCESS;
	}
	
	/**
	 * 
	 * 编辑数据
	 */
	@Action(value="edit")
	public String edit() throws BizException{
		bill = getBillService().find_full(id);
		// 只有制单人才允许修改单据
		User onlineuser = getOnlineUser().getUser();
		if(!onlineuser.getId().equals(bill.getCreater())){
			throw new BizException("只有制单人才可以修改单据！");
		}
		// 编辑状态和审批中状态允许编辑，因为流程有驳回功能，被驳回到制单人的单据仍然需要修改。
		if(BillState.EDIT != bill.getState()&& BillState.BPMING != bill.getState()){
			throw new BizException("当前单据的状态为【".concat(bill.getEnumDescription("state")).concat("】，不允许修改！"));
		}
		if(BillState.BPMING == bill.getState()){
			boolean isfrist = executionAPIService.isTheFirstNode(bill.getId());
			if(!isfrist){
				throw new BizException("当前单据已经在流程中，不能进行编辑操作！");
			}
		}
		return load();
	}
	
	/**
	 * 
	 * 删除数据
	 */
	@Action(value="delete")
	public String delete() throws BizException{
		getBillService().deleteBills(ids,getOnlineUser().getUser(),functionCode);
		setJsonString("{success:true,msg:'删除成功!'}");
		return SUCCESS;
	}
	
	/**
	 * 
	 * 审批
	 * @author
	 * @date
	 */
	@Action(value="audit")
	public String audit() throws BizException{
		bill = getBillService().find(id);
		try{
			Object[] obj = executionAPIService.isAudit(null, bill.getId(), "", getOnlineUser().getUser().getLoginId());
			Boolean flag = (Boolean) obj[0];
			String counterSignRuleId = (String) obj[1];
			if(flag){
				setJsonString("{success : true,msg : '"+counterSignRuleId+"'}");
			}else{
				throw new BizException("无审批权力！");
			}
		} catch (Exception e) {
			throw new BizException(e.getMessage());
		}
		return SUCCESS;
	}
	
	/**
	 * 
	 * 提交审批,发起流程
	 * @author
	 */
	@Action(value="commit")
	public String commit() throws BizException{
		getBillService().commit(id, getOnlineUser().getUser(),getOnlineUser().getLoginedOrg(),functionCode);
		setJsonString("{success:true,msg:'保存并提交审批成功!'}");
		return SUCCESS;
	}
	
	/**
	 * 
	 * 判断某张单据是否走过流程
	 * @author YangTao
	 * @date 2011-12-22下午12:03:56
	 * @return
	 */
	@Action("billIsInProcess")
	public String billIsInProcess(){
		boolean result = false;
		String json = "";
		result = this.executionAPIService.billIsInProcess(id);
		if(result){
			json = "{success:true}";
		}else{
			json = "{success:false,msg:'该单据没走流程'}";
		}
		setJsonString(json);
		return SUCCESS;
	}
	/**
	 * 
	 * 从上游单据制单时获得数据
	 * @return
	 * @throws BizException
	 */
	@Action("getFromPreDatas")
	public String getFromPreDatas() throws BizException{
		//调用服务返回结果
		List<T> list = getBillService().getFromPreDatas(bizTypeID,functionCode,getOnlineUser().getUser(),simpleDataVOs);
        //返回制单成功的第一条记录的id
		if(list == null || list.size()<1){
			setJsonString("{success : true,msg : '无数据!'}");
			return SUCCESS;
		}
		StringBuffer sf_json = new StringBuffer("{success:true,msg:'加载成功！',totalCounts:").append(list.size()).append(",result:[");
		for (int i = 0; i < list.size(); i++) {
			T tbill = list.get(i);
			if(i != 0){
				sf_json.append(",");
			}
			sf_json.append(FastJsonUtils.billToJson(tbill, BizServiceHelper.getItemClasses(tbill), null));
		}
		sf_json.append("]}");
		setJsonString(sf_json.toString());
		return SUCCESS;
	}
}
