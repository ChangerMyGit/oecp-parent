/************************* 版权声明 *********************************
 *                                                                  *
 *                     版权所有：百洋软件                          				 	*
 *          Copyright (c) 2011 by www.oecp.cn                	*
 *                                                                  *
 ************************* 变更记录 *********************************
 *
 * 创建者：宋黎晓   创建日期： 2011-6-28
 * 备注：
 * 
 * 修改者：       修改日期：
 * 备注：
 * 
 */    

package oecp.platform.billflow.web;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;

import oecp.framework.dao.QueryCondition;
import oecp.framework.dao.QueryObject;
import oecp.framework.dao.QueryResult;
import oecp.framework.exception.BizException;
import oecp.framework.util.FastJsonUtils;
import oecp.framework.vo.base.SimpleDataVO;
import oecp.framework.web.JsonResult;
import oecp.platform.billflow.eo.BillFlowConfig;
import oecp.platform.billflow.eo.DataFieldView;
import oecp.platform.billflow.service.BillFlowService;
import oecp.platform.biztype.eo.BizType;
import oecp.platform.bpm.enums.ExceptionMsgType;
import oecp.platform.uiview.enums.ComponentType;
import oecp.platform.uiview.vo.UIComponentVO;
import oecp.platform.uiview.web.parser.ExtUIViewParser;
import oecp.platform.uiview.web.parser.UIViewParser;
import oecp.platform.web.BasePlatformAction;

import org.apache.commons.lang.StringUtils;
import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.Namespace;
import org.apache.struts2.convention.annotation.ParentPackage;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
/**
 * 单据流Action
 * @author slx
 * @date 2011-6-28 下午02:16:46
 * @version 1.0
 */
@Controller("billFlowAction")
@Scope("prototype")
@ParentPackage("default-package")
@Namespace("/billflow")
public class BillFlowAction extends BasePlatformAction {

	private static final long serialVersionUID = 1L;
	@Resource(name="billFlowService")
	private BillFlowService billFlowService;
	private String functionCode;
	private String bizTypeID;
	private List<QueryCondition> queryConditions = new ArrayList<QueryCondition>();
	private List<SimpleDataVO> simpleDataVOs = new ArrayList<SimpleDataVO>();
	//单据id
	private String billId;
	//查看历史方向 ：UP DOWN
	private String seeDirection;
	/**
	 * 获得某个功能（单据）在本公司内的业务类型列表
	 * @author slx
	 * @date 2011-6-29 上午10:57:52
	 * @modifyNote
	 * @return
	 */
	@Action("loadBizTypes")
	public String loadBizTypes(){
		List<BizType> bizTypes = billFlowService.getBizTypeByFunCode(getOnlineUser().getUser(),functionCode,getOnlineUser().getLoginedOrg().getId());
		setJsonString(FastJsonUtils.toJson(bizTypes,new String[]{"id","code","name","description","shared","org"}));
		return SUCCESS;
	}
	/**
	 * 得到前置查询的查询条件对话框信息（目前只有{queryDialog:‘对话框类名’}），以后需要加入查询条件定义。
	 * @author slx
	 * @date 2011-6-28 下午02:48:02
	 * @modifyNote
	 * @return
	 */
	@Action("loadQueryDlgInfo")
	public String loadQueryDialogInfo(){
		BillFlowConfig config = billFlowService.getBillFlowConfig(bizTypeID, functionCode);
		setJsonString(FastJsonUtils.toJson(config,new String[]{"byHand","byBussiness","queryDialog"}));
		return SUCCESS;
	}
	/**
	 * 得到查询结果列的信息
	 * @author slx
	 * @date 2011-6-28 下午07:06:36
	 * @modifyNote
	 * @return
	 */
	@Action("loadRSColumns")
	public String loadRSColumnInfos(){
		BillFlowConfig config = billFlowService.getBillFlowConfig(bizTypeID, functionCode);
		List<DataFieldView> fields = config.getPreDatafields();
		
		UIViewParser parser = new ExtUIViewParser();
		UIComponentVO vo = new UIComponentVO();
		vo.setType(ComponentType.Grid);
		vo.setWidth(500);
		vo.setHeight(380);
		vo.setCancommit(true);
		List<UIComponentVO> vochildren = new ArrayList<UIComponentVO>();
		for(DataFieldView df : fields){
			UIComponentVO vo1 = new UIComponentVO();
			vo1.setType(ComponentType.Field);
			vo1.setValue("dataIndex", df.getName());
			vo1.setTitle(df.getDispName());
			vo1.setHidden(df.getHidden());
			vo1.setWidth(100);
			vo1.setHeight(50);
			vo1.setValue("editor", "");
			vochildren.add(vo1);
		}
		
		vo.setChildren(vochildren);
		String extcode = parser.parserToSourceCode(vo);
		setJsonString(extcode);
		return SUCCESS;
	}
	
	/**
	 * 执行查询获得值列表
	 * @author slx
	 * @date 2011-6-29 上午10:58:33
	 * @modifyNote
	 * @return
	 * @throws BizException
	 */
	@Action("query")
	public String queryDatas() throws BizException{
		QueryObject qyobj = new QueryObject();
		qyobj.setQueryConditions(getQueryConditions());
		QueryResult<HashMap> qr = billFlowService.getPreDatas(bizTypeID, functionCode, qyobj, start, limit);
		JsonResult jr = new JsonResult(qr.getTotalrecord().intValue(), qr
				.getResultlist());
		this.setJsonString(jr.toJSONString());
		return SUCCESS;
	}
	
	
	/**
	 * 查看单据流历史
	 * 
	 * @author YangTao
	 * @date 2011-12-13上午10:59:59
	 */
	@Action("getBillFlowHistory")
	public  String getBillFlowHistory()throws BizException{
		QueryResult<HashMap> qr = billFlowService.getCurrentBillHistory(functionCode,billId,seeDirection);
		JsonResult jr = new JsonResult(qr.getTotalrecord().intValue(), qr
				.getResultlist());
		this.setJsonString(jr.toJSONString());
		return SUCCESS;
	}
	
	/**
	 * 
	 * 处理业务异常信息,返回到页面
	 * @author YangTao
	 * @date 2011-11-17上午10:24:51
	 * @param response
	 * @param message
	 */
	private void handExcetionMsg(HttpServletResponse response,String message){
		if(StringUtils.isEmpty(message))
			message = ExceptionMsgType.EXECUTE_FAILURE;
		PrintWriter out = null;
		try {
			response.setCharacterEncoding("utf-8");
			response.setContentType("text/xml;charset=utf-8");
			out = response.getWriter();
			out.println(message);
			response.setStatus(HttpServletResponse.SC_EXPECTATION_FAILED);
			out.flush();
		} catch (IOException e) {
			e.printStackTrace();
		}finally{
			if(out!=null)
				out.close();
		}
	}
	public void setBillFlowService(BillFlowService billFlowService) {
		this.billFlowService = billFlowService;
	}
	public String getFunctionCode() {
		return functionCode;
	}
	public void setFunctionCode(String functionCode) {
		this.functionCode = functionCode;
	}
	public String getBizTypeID() {
		return bizTypeID;
	}
	public void setBizTypeID(String bizTypeID) {
		this.bizTypeID = bizTypeID;
	}
	public List<QueryCondition> getQueryConditions() {
		return queryConditions;
	}
	public void setQueryConditions(List<QueryCondition> queryConditions) {
		this.queryConditions = queryConditions;
	}
	public List<SimpleDataVO> getSimpleDataVOs() {
		return simpleDataVOs;
	}
	public void setSimpleDataVOs(List<SimpleDataVO> simpleDataVOs) {
		this.simpleDataVOs = simpleDataVOs;
	}
	public String getBillId() {
		return billId;
	}
	public void setBillId(String billId) {
		this.billId = billId;
	}
	public String getSeeDirection() {
		return seeDirection;
	}
	public void setSeeDirection(String seeDirection) {
		this.seeDirection = seeDirection;
	}
	
	
}
