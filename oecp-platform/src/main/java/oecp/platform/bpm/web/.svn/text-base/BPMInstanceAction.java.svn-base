/*
 * Copyright (c) 2011 OECP All Rights Reserved.                	
 * <a href="http://www.oecp.cn">http://www.oecp.cn</a>                                                                 
 */

package oecp.platform.bpm.web;

import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletResponse;

import oecp.framework.dao.QueryCondition;
import oecp.framework.dao.QueryResult;
import oecp.framework.web.JsonResult;
import oecp.platform.bpm.enums.ExceptionMsgType;
import oecp.platform.bpm.eo.VirProcessInstance;
import oecp.platform.bpm.service.ProExecutionService;
import oecp.platform.bpm.vo.HistoryTaskInfo;
import oecp.platform.bpm.vo.ProcessInstanceInfo;
import oecp.platform.web.BasePlatformAction;

import org.apache.commons.lang.StringUtils;
import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.Namespace;
import org.apache.struts2.convention.annotation.ParentPackage;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.dom4j.XPath;
import org.dom4j.xpath.DefaultXPath;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

/**
 * 流程实例action
 * 
 * @author yangtao
 * @date 2011-7-25 下午02:36:42
 * @version 1.0
 */
@Controller
@Scope("prototype")
@ParentPackage("default-package")
@Namespace("/bpm/instance")
public class BPMInstanceAction extends BasePlatformAction {

	private static final long serialVersionUID = 1L;

	
	@Autowired
	private ProExecutionService proExecutionService;
	
	List<QueryCondition> conditions = new ArrayList<QueryCondition>();

	/**
	 * 
	 * 流程实例列表
	 * @author yangtao
	 * @date 2011-7-26上午09:13:06
	 * @return
	 */
	@Action("list")
	public String list() {
//		String orgId = this.getOnlineUser().getLoginedOrg().getId();
//		QueryCondition qc = new QueryCondition("o.virProDefinition.assignedOrg.id","=",orgId);
//		conditions.add(qc);
		QueryResult<ProcessInstanceInfo> qr = this.proExecutionService.queryProcessInstance(conditions,start,limit);
		JsonResult jr = new JsonResult(qr.getTotalrecord().intValue(), qr.getResultlist());
		jr.setContainFields(new String[] { "billInfo","billKey","virProcessInstanceId","processInstanceId","processDefinitionId","deployId","processName","processVersion","createUserLoginId","createTime","status"});
		this.setJsonString(jr.toJSONString());
		return SUCCESS;
	}

	/**
	 * 
	 * 停止某个流程实例
	 * @author yangtao
	 * @date 2011-7-26上午09:13:06
	 * @return
	 */
	@Action("end")
	public String end(){
		try{
			//从前台获取参数
			String param = this.getRequest().getParameter("param");
			String[] ids = param.split(",");
			for(String processInstanceId : ids){
				this.proExecutionService.endProcessInstance(processInstanceId,true);
			}
		} catch (Exception e) {
			e.printStackTrace();
			handExcetionMsg(this.getResponse(),e.getMessage());
		}
		return SUCCESS;
	}
	
	/**
	 * 
	 * 获得流程实例的历史
	 * @author yangtao
	 * @date 2011-7-26上午09:13:06
	 * @return
	 */
	@Action("history")
	public String history(){
		try {
			//从前台获取参数
			String billKey = this.getRequest().getParameter("billKey");
			
			List <HistoryTaskInfo> resultList = this.proExecutionService.queryHistoryByBillKey(billKey);
			//调用后向前台展现的数据
			JsonResult jr = new JsonResult(resultList.size(), resultList);
			jr.setContainFields(new String[] { "id","taskId","processInstanceId","activityName","status","startTime","endTime","auditUserName","auditOpinion","auditDecision","counterSignRuleName","agentUserName"});
			this.setJsonString(jr.toJSONString());
		} catch (Exception e) {
			e.printStackTrace();
			handExcetionMsg(this.getResponse(),e.getMessage());
		}
		return SUCCESS;
	}
	
	/**
	 * 
	 * 获得流程定义的图片，写到流里面
	 * @author yangtao
	 * @date 2011-7-27下午04:29:28
	 */
	@Action("getDefinitionImage")
	public void getDefinitionImage(){
		OutputStream out = null;
		try {
			//从前台获取参数
			String deployId = this.getRequest().getParameter("deployId");
			//调用服务
			byte[] bytes = this.proExecutionService.getDefinitionPngByDpId(deployId);
			out = this.getResponse().getOutputStream();
			out.write(bytes);
			out.flush();
		} catch (Exception e) {
			e.printStackTrace();
			handExcetionMsg(this.getResponse(),e.getMessage());
		}finally{
			try {
				if(out!=null)
					out.close();
			} catch (IOException e) {
				e.printStackTrace();
				handExcetionMsg(this.getResponse(),e.getMessage());
			}
		}
	}
	
	
	
	/**
	 * 
	 * 查看流程历史图片信息
	 * @author yangtao
	 * @date 2011-7-27下午04:29:58
	 */
	@Action("getHistoryImage")
	public void getHistoryImage(){
		String path = this.getRequest().getContextPath();
		String basePath = this.getRequest().getScheme()+"://"+this.getRequest().getServerName()+":"+this.getRequest().getServerPort()+path+"/";
		PrintWriter out = null;
		try {
			//从前台获取参数
			String billKey = this.getRequest().getParameter("billKey");
			//封装前台获取的参数
			List<QueryCondition> conditions = new ArrayList<QueryCondition>();
			QueryCondition qc = new QueryCondition("o.billKey","=",billKey);
			conditions.add(qc);
			//调用服务
			VirProcessInstance vpi = this.proExecutionService.getVirProcessInstanceByConditions(conditions).get(0);
			String deployId = vpi.getVirProDefinition().getProDefinition().getDeployId();
			String id = vpi.getProcessInstanceId();//实例id
			List<String> actNames = this.proExecutionService.getCurrentActivityNameByPid(id,deployId);//当前运行的节点名称
			String strGpd = this.proExecutionService.getDefinitionXmlByDpId(deployId);
			//处理图片信息
			String imageLink = basePath+"bpm/instance/getDefinitionImage.do?deployId="+deployId;
			Element rootDiagramElement = DocumentHelper.parseText(strGpd).getRootElement();
			out = this.getResponse().getWriter();
			out.write("<img src=\""+imageLink+"\">");
			for(String actName : actNames){
				String[] boxConstraint = extractBoxConstraint(rootDiagramElement,actName);
				out.write("<div style=\"position:absolute;border:4px red solid;left:"+boxConstraint[0]+";top:"+boxConstraint[1]+";width:"+boxConstraint[2]+";height:"+boxConstraint[3]+";\">");
				out.write("</div>");
			}
			out.write("</img>");
			out.flush();
		} catch (Exception e) {
			e.printStackTrace();
			handExcetionMsg(this.getResponse(),e.getMessage());
		}finally{
			if(out!=null)
				out.close();
		}
	}
	
	/**
	 * 
	 * 解析xml取出当前节点的位置值
	 * @author yangtao
	 * @date 2011-8-18下午04:53:42
	 * @param root
	 * @param actName
	 * @return
	 */
	private String[] extractBoxConstraint(Element root,String actName)
	{
		String result[] = new String[4];
		XPath xPath = new DefaultXPath("//node[@name='" + actName + "']");
		Element node = (Element)xPath.selectSingleNode(root);
 
		//不知道为什么上面的语句不能返回任何结果，加入下面语句重新赋值 -yang
		if(node==null) {
			List lnode =  root.elements();
			for(int i=0;i<lnode.size();i++) {
				Element n = (Element)lnode.get(i);
				String nn = n.attribute("name").getText();
				if(actName.equals(nn)) {
					node =  n;					
					break;
				}
			}
		}
		
		// -yang
		String value = node.attribute("g").getValue().toString();
		result = value.split(",");
		return result;
	}

	/**
	 * 
	 * 处理业务异常信息,返回到页面
	 * @author yangtao
	 * @date 2011-8-19上午11:10:55
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
	/**
	 * @return the proExecutionService
	 */
	public ProExecutionService getProExecutionService() {
		return proExecutionService;
	}

	/**
	 * @param proExecutionService the proExecutionService to set
	 */
	public void setProExecutionService(ProExecutionService proExecutionService) {
		this.proExecutionService = proExecutionService;
	}

	public List<QueryCondition> getConditions() {
		return conditions;
	}

	public void setConditions(List<QueryCondition> conditions) {
		this.conditions = conditions;
	}
}
