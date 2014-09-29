/*
 * Copyright (c) 2011 OECP All Rights Reserved.                	
 * <a href="http://www.oecp.cn">http://www.oecp.cn</a>                                                                 
 */    

package oecp.platform.bpm.web;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.servlet.http.HttpServletResponse;

import oecp.framework.dao.QueryResult;
import oecp.framework.exception.BizException;
import oecp.framework.fs.Uploader;
import oecp.framework.web.JsonResult;
import oecp.platform.bpm.enums.ActivityType;
import oecp.platform.bpm.enums.ExceptionMsgType;
import oecp.platform.bpm.eo.BizConfigEo;
import oecp.platform.bpm.eo.BizProActivity;
import oecp.platform.bpm.eo.BizProDefinition;
import oecp.platform.bpm.service.BizProDefinitionService;
import oecp.platform.bpm.service.ProDefinitionService;
import oecp.platform.web.BasePlatformAction;

import org.apache.commons.lang.StringUtils;
import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.InterceptorRef;
import org.apache.struts2.convention.annotation.Namespace;
import org.apache.struts2.convention.annotation.ParentPackage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

/**
 * 业务流程action
 * @author yangtao
 * @date 2011-9-5上午10:38:51
 * @version 1.0
 */
@Controller
@Scope("prototype")
@ParentPackage("default-package")
@Namespace("/bizbpm/def")
public class BizBPMDefinitionAction extends BasePlatformAction {
	
	@Autowired
	private BizProDefinitionService bizProDefinitionService;
	
	@Autowired
	private ProDefinitionService bpDefinitionService;
	
	private Uploader uploader;

	private BizProDefinition bizDef;
	
	@Action("list")
	public String list(){
		QueryResult<BizProDefinition> qr = this.bizProDefinitionService.list(null, start, limit);
		JsonResult jr = new JsonResult(qr.getTotalrecord().intValue(), qr.getResultlist());
		jr.setContainFields(new String[] { "id","proDefId","deployId","name","description","version"});
		this.setJsonString(jr.toJSONString());
		return SUCCESS;
	}
	
//	@Action(value = "deploy", interceptorRefs = {
//			@InterceptorRef("indexStack") ,
//			@InterceptorRef(params = { "allowedTypes",
//					"application/zip,application/octet-stream,application/x-zip-compressed", "maximumSize",
//					"2000000" }, value = "fileUpload")
//			})
//	public String deploy() throws BizException {
//		System.out.println("enter into deploy");
//		if (uploader.getUpload().length == 0)
//			throw new BizException("没有上传文件！");
//		bizDef.setCreatedByOrg(getOnlineUser().getLoginedOrg());
//		bizDef.setCreateTime(new Date());
//		this.bizProDefinitionService.deploy( bizDef, uploader.getUpload()[0]);
//		return SUCCESS;
//	}

	@Action("config")
	public String config(){
		//获取参数
		String bizProActivityId = this.getRequest().getParameter("bizProActivityId");
		String bizProDefinitionId = this.getRequest().getParameter("bizProDefinitionId");
		String _webserviceValue = this.getRequest().getParameter("_webserviceValue");
		this.bizProDefinitionService.saveConfigInfo(bizProActivityId, _webserviceValue);
		return SUCCESS;
	}
	
	@Action("getProcessImage")
	public void getProcessImage(){
		String path = this.getRequest().getContextPath();
		String basePath = this.getRequest().getScheme()+"://"+this.getRequest().getServerName()+":"+this.getRequest().getServerPort()+path+"/";
		PrintWriter out = null;
		try {
			//获取参数
			String deployId = this.getRequest().getParameter("deployId");
			String bizProDefinitionId = this.getRequest().getParameter("bizProDefinitionId");
			
			List<BizProActivity> list = this.bizProDefinitionService.getBizProActivities(bizProDefinitionId);
			
			//流程图片路径
			String imageLink = basePath+"bpm/instance/getDefinitionImage.do?deployId="+deployId;
			
			//向流里面写入流程图片
			this.getResponse().setCharacterEncoding("utf-8");
			this.getResponse().setContentType("text/xml;charset=utf-8");
			out = this.getResponse().getWriter();

			out.write("<div id=\"imageDivId\">");
			out.write("<img id=\"processDefinitionId\" src=\""+imageLink+"\" border=\"0\" usemap=\"#Map\" />");
			out.write("<map name=\"Map\" id=\"Map\">");
			//获取流程图中每个任务节点的位置
			Map<String,Map> map = this.bpDefinitionService.getAllActivityPlaceValue(deployId);
			Set set = map.keySet();
			Iterator it = set.iterator();
			while(it.hasNext()){
				String type = (String)it.next();
				//任务节点增加配置单元
				if(ActivityType.TASK.toString().equals(type)){
					Map taskMap = (Map)map.get(type);
					Set taskSet = taskMap.keySet();
					Iterator taskIt = taskSet.iterator();
					while(taskIt.hasNext()){
						String activityName = (String)taskIt.next();
						String bizProActivityId = "";
						for(BizProActivity vpa : list){
							if(activityName.equals(vpa.getActivityName())){
								bizProActivityId = vpa.getId();
							}
						}
						String[] result = this.bizProDefinitionService.getBizConfigEoByBizProActivityId(bizProActivityId);
						int[] value = (int[])taskMap.get(activityName);
						out.write("<area shape=\"rect\" coords=\""+value[0]+","+value[1]+","+value[2]+","+value[3]+"\" style=\"cursor:hand;\"  href=\"javascript:OECP.bpmbiz.BizProDefinitionView.config('"+bizProActivityId+"','"+bizProDefinitionId+"','"+(result[0]==null?"":result[0])+"');\" />");
				
					}
				}
			}
			out.write("</map>");
			out.write("</div>");
			out.flush();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			handExcetionMsg(this.getResponse(),e.getMessage());
		}finally{
			if(out!=null)
				out.close();
		}
	}
	
	@Action("start")
	public String start(){
		try {
			String proDefId = this.getRequest().getParameter("proDefId");
			this.bizProDefinitionService.start(proDefId);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			handExcetionMsg(this.getResponse(),e.getMessage());
		}
		return SUCCESS;
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
			// TODO Auto-generated catch block
			e.printStackTrace();
		}finally{
			if(out!=null)
				out.close();
		}
	}
	

	/**
	 * @return the bizProDefinitionService
	 */
	public BizProDefinitionService getBizProDefinitionService() {
		return bizProDefinitionService;
	}


	/**
	 * @param bizProDefinitionService the bizProDefinitionService to set
	 */
	public void setBizProDefinitionService(
			BizProDefinitionService bizProDefinitionService) {
		this.bizProDefinitionService = bizProDefinitionService;
	}


	/**
	 * @return the uploader
	 */
	public Uploader getUploader() {
		return uploader;
	}


	/**
	 * @param uploader the uploader to set
	 */
	public void setUploader(Uploader uploader) {
		this.uploader = uploader;
	}


	/**
	 * @return the bizDef
	 */
	public BizProDefinition getBizDef() {
		return bizDef;
	}


	/**
	 * @param bizDef the bizDef to set
	 */
	public void setBizDef(BizProDefinition bizDef) {
		this.bizDef = bizDef;
	}

	
	
}
