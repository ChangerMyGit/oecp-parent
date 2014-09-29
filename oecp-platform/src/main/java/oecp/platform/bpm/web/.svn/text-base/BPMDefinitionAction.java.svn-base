/*
 * Copyright (c) 2011 OECP All Rights Reserved.                	
 * <a href="http://www.oecp.cn">http://www.oecp.cn</a>                                                                 
 */

package oecp.platform.bpm.web;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.annotation.Resource;
import javax.imageio.ImageIO;
import javax.servlet.http.HttpServletResponse;

import oecp.framework.dao.QueryCondition;
import oecp.framework.dao.QueryResult;
import oecp.framework.exception.BizException;
import oecp.framework.util.FastJsonUtils;
import oecp.framework.web.JsonResult;
import oecp.framework.web.ext.JsonTreeVO;
import oecp.framework.web.ext.JsonTreeVOBuilder;
import oecp.platform.api.bpm.ExecutionAPIService;
import oecp.platform.bcfunction.eo.Function;
import oecp.platform.bcfunction.service.BcFunctionService;
import oecp.platform.bcinfo.eo.BizComponent;
import oecp.platform.bpm.enums.ActivityType;
import oecp.platform.bpm.enums.ExceptionMsgType;
import oecp.platform.bpm.eo.ProDefinition;
import oecp.platform.bpm.eo.VirProActivity;
import oecp.platform.bpm.jpdl.JpdlModel;
import oecp.platform.bpm.jpdl.JpdlModelDrawer;
import oecp.platform.bpm.service.ProActivityService;
import oecp.platform.bpm.service.ProDefinitionService;
import oecp.platform.bpm.service.ProExecutionService;
import oecp.platform.bpm.util.ZipCompressor;
import oecp.platform.bpm.vo.ProDefinitionInfo;
import oecp.platform.bpm.vo.VirProcessDefinitionInfo;
import oecp.platform.bpm.vo.VirTaskActivityInfo;
import oecp.platform.org.eo.Organization;
import oecp.platform.org.eo.Post;
import oecp.platform.org.service.PostService;
import oecp.platform.role.eo.OrgRole;
import oecp.platform.role.eo.Role;
import oecp.platform.role.service.OrgRoleService;
import oecp.platform.role.vo.RoleVO;
import oecp.platform.user.eo.User;
import oecp.platform.user.service.UserService;
import oecp.platform.web.BasePlatformAction;

import org.apache.commons.lang.StringUtils;
import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.Namespace;
import org.apache.struts2.convention.annotation.ParentPackage;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;

import com.alibaba.fastjson.JSON;

/**
 * 流程定义action
 * 
 * @author yongtree
 * @date 2011-6-16 下午02:36:42
 * @version 1.0
 */
@Controller
@Scope("prototype")
@ParentPackage("default-package")
@Namespace("/bpm/def")
public class BPMDefinitionAction extends BasePlatformAction {

	private static final long serialVersionUID = 1L;

	@Resource
	private BcFunctionService bcFunctionService;

	@Resource
	private ProDefinitionService bpDefinitionService;
	
	@Resource
	private ProExecutionService proExecutionService;
	
	@Resource
	private ProActivityService proActivityService;
	
	@Resource
	private PostService postService;
	
	@Resource
	private OrgRoleService orgRoleService;
	
	@Resource
	private UserService userService;
	
	@Resource
	private ExecutionAPIService executionAPIService;

	private ProDefinition def;

	private String orgIds;
	
	List<QueryCondition> conditions = new ArrayList<QueryCondition>();//查询条件


	/**
	 * 
	 * 流程定义处，功能列表
	 * @author YangTao
	 * @date 2011-12-31下午12:03:40
	 * @return
	 */
	@Action(value = "functionTree")
	public String loadFunctionTree() {
		try {
			List<JsonTreeVO> list = JsonTreeVOBuilder.reverseBuildTree(
					this.bcFunctionService.getHasApprovalFunctions(), "id",
					"name", "parent", JsonTreeVOBuilder.CheckShow.NONE);
			List<BizComponent> bcs = new ArrayList<BizComponent>();
			List<JsonTreeVO> bcnodes = new ArrayList<JsonTreeVO>();
			for (JsonTreeVO vo : list) {
				Function function = this.bcFunctionService.find(vo.getId());
				BizComponent bc = function.getBc();
				JsonTreeVO node = null;
				if (!bcs.contains(bc)) {
					node = new JsonTreeVO();
					node.setId(bc.getId()+"");
					node.setText(bc.getName());
					node.getChildren().add(vo);
					bcnodes.add(node);
					bcs.add(bc);
				} else {
					for (JsonTreeVO o : bcnodes) {
						if (o.getId().equals(bc.getId().toString())) {
							node = o;
							break;
						}
					}
					node.getChildren().add(vo);
				}
			}

			this.setJsonString(FastJsonUtils.toJson(bcnodes));
		} catch (BizException e) {
			e.printStackTrace();
			returnErrorMsg("加载异常！");
		}
		return SUCCESS;

	}

	/**
	 * 
	 * 流程定义，选择某个功能，该功能下面发布的流程定义列表
	 * @author YangTao
	 * @date 2011-12-31下午12:03:55
	 * @return
	 */
	@Action("list")
	public String list() {
		String funcId = getRequest().getParameter("id");
		List<ProDefinitionInfo> list = this.bpDefinitionService.getProDefinitions(
				getOnlineUser().getLoginedOrg(), StringUtils.isEmpty(funcId)?null:funcId);
		JsonResult jr = new JsonResult(list.size(), list);
		jr.setContainFields(new String[] { "deployId","proDefId","id", "name",
				"description","version" ,"createdByOrgName","belongFunctionName" ,"createTime","webPictureString"});
		this.setJsonString(jr.toJSONString());
		return SUCCESS;
	}
	
	/**
	 * 
	 * 流程定义配置时，选择流程定义的查询列表（combo）
	 * @author yangtao
	 * @date 2011-9-14上午10:46:11
	 * @return
	 */
	@Action("getProDefinition")
	public String getProDefinition() {
		Organization org = getOnlineUser().getLoginedOrg();
		QueryResult<ProDefinitionInfo> qr = this.bpDefinitionService.getProDefinitions(conditions,org,start,limit);
		//封装向前台展示的数据
		JsonResult jr = new JsonResult(qr.getTotalrecord().intValue(), qr.getResultlist());
		jr.setContainFields(new String[] { "deployId","proDefId","id", "name","description","version"  });
		this.setJsonString(jr.toJSONString());
		return SUCCESS;
	}


	/**
	 * 启动（测试时用到)
	 * @author YangTao
	 * @date 2011-12-31下午12:05:37
	 * @return
	 */
	@Action(value = "start")
	public String startProcessDefinition(){
		String proDefId = getRequest().getParameter("proDefId");//jbpm4中流程定义id
		String deployId = getRequest().getParameter("deployId");//jbpm4中部署id
		String billKey = "";
		Map<String, Object> variables = new HashMap<String, Object>();
		//在启动流程的时候，把真个流程用到的参数放进去
		variables.put("proDefId", proDefId);
		variables.put("deployId", deployId);
		System.out.println("enter into start");
		this.proExecutionService.startProcess(proDefId, billKey, variables);
		return SUCCESS;
	}
	
	/**
	 * 
	 * 删除流程定义(流程定义处，删除)
	 * @author yangtao
	 * @date 2011-8-9上午08:54:27
	 * @return
	 */
	@Action(value = "delete")
	public String deleteProcessDefinition(){
		try {
			//从前台获取参数
			String param = getRequest().getParameter("param");
			param = param.substring(0, param.length()-1);
			String[] ids = param.split(",");
			for(String id : ids){
				String proDefinitionId = id.split("#")[0];//本平台封装的流程定义的ID
				String deployId = id.split("#")[1];//jbpm4中流程定义的部署ID
				this.bpDefinitionService.delete(deployId, proDefinitionId,getOnlineUser().getLoginedOrg());
			}
		} catch (Exception e) {
			e.printStackTrace();
			handExcetionMsg(this.getResponse(),e.getMessage());
		}
		return SUCCESS;
	}
	
	/**
	 * 
	 * 获得某个流程下面的所有任务节点
	 * @author yangtao
	 * @date 2011-8-9上午08:46:25
	 * @return
	 */
	@Action(value = "getTaskActivity")
	public String getTaskActivity(){
		//从前台获取参数
		String virProcessDefId = getRequest().getParameter("virProcessDefId");
		String processName = getRequest().getParameter("processName");
		//调用服务
		List<VirProActivity> list = this.bpDefinitionService.FindAllVirTaskActivities(virProcessDefId);
		List<VirTaskActivityInfo> resultList = new ArrayList<VirTaskActivityInfo>();
		for(VirProActivity ac : list){
			VirTaskActivityInfo ta = new VirTaskActivityInfo();
			ta.setVirProActivityId(ac.getId());
			ta.setVirProDefinitionId(virProcessDefId);
			ta.setDeployId(ac.getVirProDefinition().getProDefinition().getDeployId());
			ta.setProcessName(processName);
			ta.setActivityName(ac.getActivityName());
			ta.setAssignOrgId(ac.getVirProDefinition().getAssignedOrg().getId());
			resultList.add(ta);
		}
		JsonResult jr = new JsonResult(resultList.size(), resultList);
		jr.setContainFields(new String[] { "virProActivityId","virProDefinitionId","deployId","processName", "activityName","assignOrgId"});
		this.setJsonString(jr.toJSONString());
		return SUCCESS;
	}
	
	/**
	 * 
	 * 分配组织流程
	 * @author yangtao
	 * @date 2011-8-1下午03:34:06
	 */
	@Action(value = "addVirProcessDefinition")
	public String  addVirProcessDefinition(){
		try{
			//从前台获取参数
			String processDefinitionId = getRequest().getParameter("processDefinitionId");//ProDefinition的id
			String orgIds = getRequest().getParameter("orgIds");//分配的组织ID
			this.bpDefinitionService.addVirProcessDefinition(processDefinitionId, orgIds);
		} catch (Exception e) {
			e.printStackTrace();
			handExcetionMsg(this.getResponse(),e.getMessage());
		}
		return SUCCESS;
	}
	

	/**
	 * 
	 * 流程定义配置处，流程列表
	 * @author yangtao
	 * @date 2011-8-1下午04:06:16
	 * @return
	 */
	@Action(value = "listVirProcessDefinition")
	public String listVirProcessDefinition(){
		//从前台获取参数
		String proDefinitionId = this.getRequest().getParameter("proDefinitionId");
		String functionid = this.getRequest().getParameter("functionid");
		String[] params = new String[]{proDefinitionId,functionid};
		Organization og = getOnlineUser().getLoginedOrg();
		QueryResult<VirProcessDefinitionInfo> qr = this.bpDefinitionService.queryVirProcessDefinition(params,start,limit,og);
		JsonResult jr = new JsonResult(qr.getTotalrecord().intValue(), qr
				.getResultlist());
		jr.setContainFields(new String[] { "id","name","proDefinitionId","proDefinitionName","deployId","processDefinitionId","assignedOrgId","assignedOrgName","isUseId","isUseName","belongFunctionId","belongFunctionName"});
		this.setJsonString(jr.toJSONString());
		return SUCCESS;
	}
	
	/**
	 * 
	 * 流程定义配置处，删除流程
	 * @author yangtao
	 * @date 2011-8-2上午08:54:45
	 * @return
	 */
	@Action(value="deleteVirProcessDefinition")
	public String deleteVirProcessDefinition(){
		try{
			//从前台获取参数
			String virProcessDefIds = getRequest().getParameter("virProcessDefIds");
			this.bpDefinitionService.deleteVirProcessDefinition(virProcessDefIds);
		} catch (Exception e) {
			e.printStackTrace();
			handExcetionMsg(this.getResponse(),e.getMessage());
		}
		return SUCCESS;
	}
	
	/**
	 * 
	 * 给任务节点配置信息
	 * @author yangtao
	 * @date 2011-8-2下午02:51:04
	 * @return
	 */
	@Action(value="assignVirProActivity")
	public String assignVirProActivity(){
		try{
			//从前台获取参数
			String virProActivityId = getRequest().getParameter("virProActivityId");
			String virProDefinitionId = getRequest().getParameter("virProDefinitionId");
			String counterSignRule = getRequest().getParameter("counterSignRule");
			String passRate = getRequest().getParameter("passRate");
			String assignFlag = getRequest().getParameter("assignFlag");
			String ids = getRequest().getParameter("ids");
			String nextTask = getRequest().getParameter("nextTask");
			String nextTaskUser = getRequest().getParameter("nextTaskUser");
			String editBill = getRequest().getParameter("editBill");
			Organization org = getOnlineUser().getLoginedOrg();
			//调用服务
			this.bpDefinitionService.assignVirProActivity(virProActivityId, virProDefinitionId,counterSignRule,passRate, assignFlag, ids,org,new String[]{nextTask,nextTaskUser,editBill});
		} catch (Exception e) {
			e.printStackTrace();
			handExcetionMsg(this.getResponse(),e.getMessage());
		}
		return SUCCESS;
	}
	
	/**
	 * 
	 * 获取任务节点配置信息
	 * @author YangTao
	 * @date 2012-1-30上午10:27:00
	 * @return
	 */
	@Action(value="getTaskConfig")
	public String getTaskConfig(){
		try{
			//从前台获取参数
			String virProActivityId = getRequest().getParameter("virProActivityId");
			String virProDefinitionId = getRequest().getParameter("virProDefinitionId");
			String[] assignValue = this.bpDefinitionService.getVirProActivityAssign(virProActivityId, virProDefinitionId);
			setJsonString("{counterSign:'"+assignValue[2]+"',passRate:'"+assignValue[3]+"',nextTask:'"+assignValue[4]+"',nextTaskUser:'"+assignValue[5]+"',editBill:'"+assignValue[6]+"',assignFlag:'"+assignValue[0]+"',assignValue:'"+assignValue[1]+"'}");
		} catch (Exception e) {
			e.printStackTrace();
			handExcetionMsg(this.getResponse(),e.getMessage());
		}
		return SUCCESS;
	}
	
	/**
	 * 
	 * 给条件节点配置信息
	 * @author yangtao
	 * @date 2011-8-26下午02:17:20
	 * @return
	 */
	@Action(value="assignDecisionCondition")
	public String assignDecisionCondition(){
		try{
			//从前台获取参数
			String virProActivityId = getRequest().getParameter("virProActivityId");
			String virProDefinitionId = getRequest().getParameter("virProDefinitionId");
			String decisionScript = getRequest().getParameter("decisionScript");
			this.bpDefinitionService.assignDecisionCondition(virProActivityId, virProDefinitionId, "", decisionScript);
		} catch (Exception e) {
			e.printStackTrace();
			handExcetionMsg(this.getResponse(),e.getMessage());
		}
		return SUCCESS;
	}
	
	/**
	 * 
	 * 获取条件节点配置信息
	 * @author YangTao
	 * @date 2012-1-30上午10:27:00
	 * @return
	 */
	@Action(value="getDecisionCondition")
	public String getDecisionCondition(){
		try{
			//从前台获取参数
			String virProActivityId = getRequest().getParameter("virProActivityId");
			String virProDefinitionId = getRequest().getParameter("virProDefinitionId");
			String result = this.bpDefinitionService.getDecisionConditionScript(virProActivityId, virProDefinitionId);
			setJsonString(result);
		} catch (Exception e) {
			e.printStackTrace();
			handExcetionMsg(this.getResponse(),e.getMessage());
		}
		return SUCCESS;
	}
	
	
	/**
	 * 
	 * 启动虚拟流程（测试时用）
	 * @author yangtao
	 * @date 2011-8-9上午08:45:53
	 * @return
	 */
	@Action(value = "startVirProcessDefinition")
	public String startVirProcessDefinition(){
		try {
			//从前台获取参数
			String virProDefinitionId = getRequest().getParameter("virProDefinitionId");//虚拟流程定义ID
			String billKey = "";
			String billInfo = "";
			Map<String, Object> variables = new HashMap<String, Object>();
			//流程发起人
			User user = this.getOnlineUser().getUser();
			//调用服务
			this.proExecutionService.startVirProcess(user,virProDefinitionId, billKey,billInfo, variables,true);
		} catch (Exception e) {
			e.printStackTrace();
			handExcetionMsg(this.getResponse(),e.getMessage());
		}
		return SUCCESS;
	}
	/**
	 * 
	 * 流程定义配置处，启用流程
	 * @author YangTao
	 * @date 2011-10-19上午11:32:36
	 * @return
	 */
	@Action(value="use")
	public String useVirProcessDefinition(){
		try {
			//从前台获取参数
			String virProDefinitionId = getRequest().getParameter("virProDefinitionId");//虚拟流程定义ID
			String isUseId = getRequest().getParameter("isUseId");
			//流程发起人
			User user = this.getOnlineUser().getUser();
			//调用服务
			this.bpDefinitionService.useVirProcessDefinition(user,virProDefinitionId,isUseId);
		} catch (Exception e) {
			e.printStackTrace();
			handExcetionMsg(this.getResponse(),e.getMessage());
		}
		return SUCCESS;
	}
	
	
	/**
	 * 
	 * 获得流程定义图片，进行节点信息配置
	 * @author yangtao
	 * @date 2011-8-11上午09:25:27
	 */
	@Action("getProcessImage")
	public void getProcessImage(){
		String path = this.getRequest().getContextPath();
		String basePath = this.getRequest().getScheme()+"://"+this.getRequest().getServerName()+":"+this.getRequest().getServerPort()+path+"/";
		PrintWriter out = null;
		try {
			//获取参数
			String deployId = this.getRequest().getParameter("deployId");
			String virProDefinitionId = this.getRequest().getParameter("virProDefinitionId");
			String assignOrgId = this.getRequest().getParameter("assignOrgId");
			//获取该虚拟流程的所有虚拟节点
			List<VirProActivity> list = this.bpDefinitionService.FindAllVirTaskActivities(virProDefinitionId);
			
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
						String virProActivityId = "";
						for(VirProActivity vpa : list){
							if(activityName.equals(vpa.getActivityName())){
								virProActivityId = vpa.getId();
//								String[] assignValue = this.bpDefinitionService.getVirProActivityAssign(virProActivityId, virProDefinitionId);
								int[] value = (int[])taskMap.get(activityName);
								out.write("<area shape=\"rect\" coords=\""+value[0]+","+value[1]+","+value[2]+","+value[3]+"\" style=\"cursor:hand\" onMouseOver=\"mouseOver('"+virProActivityId+"',"+value[0]+","+value[1]+","+(value[2]-value[0])+","+(value[3]-value[1])+");\" onMouseOut=\"mouseOut('"+virProActivityId+"');\" onclick=\"OECP.bpm.VirProcessDefView.assign('"+virProActivityId+"','"+virProDefinitionId+"','"+assignOrgId+"','"+activityName+"');\" />");
							}
						}
					}
				}
				//decision节点增加配置单元
				if(ActivityType.DECISION.toString().equals(type)){
					Map decisionMap = (Map)map.get(type);
					Set decisionSet = decisionMap.keySet();
					Iterator decisionIt = decisionSet.iterator();
					while(decisionIt.hasNext()){
						String activityName = (String)decisionIt.next();
						String virProActivityId = "";
						for(VirProActivity vpa : list){
							if(activityName.equals(vpa.getActivityName())){
								virProActivityId = vpa.getId();
//								String result = this.bpDefinitionService.getDecisionConditionScript(virProActivityId, virProDefinitionId);
								int[] value = (int[])decisionMap.get(activityName);
								out.write("<area shape=\"rect\" coords=\""+value[0]+","+value[1]+","+value[2]+","+value[3]+"\" style=\"cursor:hand;\" onMouseOver=\"mouseOver('"+virProActivityId+"',"+value[0]+","+value[1]+","+(value[2]-value[0])+","+(value[3]-value[1])+");\" onMouseOut=\"mouseOut('"+virProActivityId+"');\" onclick=\"OECP.bpm.VirProcessDefView.condition('"+virProActivityId+"','"+virProDefinitionId+"','"+activityName+"');\" />");
							}
						}
					}
				}
			}
			out.write("</map>");
			out.write("</div>");
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
	 * jbpm4.4流程定义web设计 直接发布 待检测
	 * @author yangtao
	 * @date 2011-9-20下午04:55:05
	 * @return
	 */
	@Action(value = "designProcessDefinition")
	public void designProcessDefinition(){
		OutputStream os = null;
		InputStream is = null;
		InputStream iss = null;
		try {
			//从前台获取参数
			/**
			 * "{states:{rect1:{type:'start',text:{text:'开始'}, attr:{ x:523,
			 * y:38, width:50, height:50}, props:{text:{value:'开始'},
			 * temp1:{value
			 * :''},temp2:{value:''}}},rect8:{type:'end',text:{text:'结束'},
			 * attr:{ x:518, y:414, width:50, height:50},
			 * props:{text:{value:'结束'}, temp1:{value:
			 * ''},temp2:{value:''}}},rect20:{type:'task',text:{text:'任务'},
			 * attr:{ x:493, y:227, width:100, height:50},
			 * props:{text:{value:'任务'}, assignee:{value:
			 * ''},form:{value:''},desc:{value:''}}}},paths:{path21:{from:'rect1',to:'rect20'
			 * , dots:[],text:{text:'TO 任务'},textPos:{x:0,y:-10},
			 * props:{text:{value:''}}}, path22:{from:'rect20',to:'rect8',
			 * dots:[],text:{text:'TO 结束'},textPos:{x:0,y:-10},
			 * props:{text:{value:
			 * ''}}}},props:{props:{name:{value:'新建流程'},key:{value:''},desc:{value:''}}}}"
			 * ;
			 */
			String data = this.getRequest().getParameter("data");
			String functionId = this.getRequest().getParameter("functionId");
			//封装流程定义参数
			ProDefinition proDefinition = new ProDefinition();
			proDefinition.setCreatedByOrg(getOnlineUser().getLoginedOrg());
			proDefinition.setCreateTime(new Date());
			Function function = new Function();
			function.setId(functionId);
			proDefinition.setBelongFunction(function);
			proDefinition.setWebPictureString(data);
			
			JSONObject o = new JSONObject(data);
			Map<Object,Object> map = this.getJsonObject(o);
			String sb = this.getProcessXml(map, proDefinition);
			try {
				//先生成*.jpdl.xml和*.png文件
				if(!new File("D:/BpmWebDesign").exists()){
					new File("D:/BpmWebDesign").mkdir();
				}
				File xmlFile = new File("D:/BpmWebDesign/test.jpdl.xml");
				os = new FileOutputStream(xmlFile);
				os.write(sb.toString().getBytes("utf-8"));
				File file = new File("D:/BpmWebDesign/test.jpdl.xml");
				is = new FileInputStream(file);
				JpdlModel jpdlModel = new JpdlModel(is);     
				ImageIO.write(new JpdlModelDrawer().draw(jpdlModel), "png", new File("D:/BpmWebDesign/test.png"));
				//把上面生成的两个文件再生成一个压缩文件
				File zipFile = new File("D:/BpmWebDesign/test.zip");
				File[] files = new File[]{xmlFile,new File("D:/BpmWebDesign/test.png")};
				ZipCompressor.zipFiles(files, zipFile);
				//发布
				this.bpDefinitionService.deploy(orgIds, proDefinition, zipFile);
			} catch (UnsupportedEncodingException e) {
				e.printStackTrace();
				throw e;
			} catch (IOException e) {
				e.printStackTrace();
				throw e;
			}finally{
				if(os!=null)
					os.close();
				if(is!=null)
					is.close();
				if(iss!=null)
					iss.close();
			}
		} catch (Exception e) {
			e.printStackTrace();
			handExcetionMsg(this.getResponse(),e.getMessage());
		}
	}

	//得到流程的xml内容
	private String getProcessXml(Map<Object,Object> map,ProDefinition proDefinition)throws Exception{
		StringBuffer sb = new StringBuffer();
		//流程名称防止重复，系统自动在原有流程名称的基础上增加一些标识
		String pname = (String)((Map)((Map)((Map)map.get("props")).get("props")).get("name")).get("value");
		String desc = (String)((Map)((Map)((Map)map.get("props")).get("props")).get("desc")).get("value");
		String processName = pname+(System.currentTimeMillis()+"").substring(9);
		proDefinition.setName(pname);
		proDefinition.setDescription(desc);
		sb.append("<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n");
		sb.append("<process name=\""+processName+"\" xmlns=\"http://jbpm.org/4.4/jpdl\">\n");
		Map stateMap = (Map)map.get("states");
		Set set = stateMap.keySet();
		Iterator it = set.iterator();
		while(it.hasNext()){
			String rect = (String)it.next();
			Map stateMap1 = (Map)stateMap.get(rect);
			String type = (String)stateMap1.get("type");
			Map stateMap2 = (Map)stateMap1.get("text");
			String name = (String)stateMap2.get("text");
			Map stateMap3 = (Map)stateMap1.get("attr");
			String x = (String)stateMap3.get("x");
			String y = (String)stateMap3.get("y");
			String height = (String)stateMap3.get("height");
			String width = (String)stateMap3.get("width");
			sb.append("<"+type+" name=\""+name+"\" g=\""+x+","+y+","+width+","+height+"\">\n");
			if(ActivityType.DECISION.toString().equalsIgnoreCase(type))
				sb.append("<handler class=\"oecp.platform.bpm.handler.DefaultDecisionHandler\"></handler>\n");
			if(ActivityType.TASK.toString().equalsIgnoreCase(type))
				sb.append("<assignment-handler class=\"oecp.platform.bpm.handler.DefaultTaskAssignHandler\"/>\n");
			 
			sb.append(this.getTransitionInfo(map, rect));
			sb.append("</"+type+">\n");
		}
		sb.append("</process>");
		return sb.toString();
	}

	//得到节点的transition
	private String getTransitionInfo(Map<Object,Object> map,String rect)throws Exception{
		StringBuffer sb = new StringBuffer();
		if("{}".equals(map.get("paths")))
			throw new Exception("节点之间没有连线");
		Map pathMap = (Map)map.get("paths");
		Set set = pathMap.keySet();
		Iterator it = set.iterator();
		while(it.hasNext()){
			String path = (String)it.next();
			Map pathMap1 = (Map)pathMap.get(path);
			String from = (String)pathMap1.get("from");
			if(from.equals(rect)){
				String to = (String)pathMap1.get("to");
				String toName = (String)(((Map)((Map)((Map)map.get("states")).get(to)).get("text")).get("text"));
				Map pathMap2 = (Map)pathMap1.get("text");
				String name = (String)pathMap2.get("text");
				Map pathMap3 = (Map)pathMap1.get("textPos");
				String x = (String)pathMap3.get("x");
				String y = (String)pathMap3.get("y");
				sb.append("<transition ");
				if(!StringUtils.isEmpty(name))
					sb.append("name=\""+name+"\"");
				sb.append(" to=\""+toName+"\"");
				if(!StringUtils.isEmpty(x)&&!StringUtils.isEmpty(y))
					sb.append(" g=\""+x+","+y+"\"");
				sb.append("/>\n");
			}
		}
		return sb.toString();
	}
	
	//处理JSONObject
	private Map<Object,Object> getJsonObject(JSONObject js){
		Map<Object,Object> map = new HashMap<Object,Object>();
		Iterator set = js.keys();
		while (set.hasNext()) {
			String key = (String) set.next();
			String value = js.getString(key);
			try {
				if(value.contains(":"))
					map.put(key,this.getJsonObject(new JSONObject(value)));
				else
					map.put(key, value);
			} catch (ParseException e) {
				e.printStackTrace();
			}
			
		}
		return map;
	}
	
	/**
	 * 
	 * 根据公司Id获取公司创建的和上级由公司分配的角色列表 
	 * @author yangtao
	 * @date 2011-8-15下午02:56:34
	 * @return
	 * @throws BizException
	 */
	@Action(value = "queryRoleByOrgId")
	@Transactional
	public String queryRoleByOrgId() throws BizException {
		String orgId = this.getRequest().getParameter("orgId");
		if (!StringUtils.isEmpty(orgId)) {
			// 获取当前公司创建和上级公司分配的角色
			List<OrgRole> list = orgRoleService.getAllOrgRole( orgId);
			QueryResult<OrgRole> rs = new QueryResult<OrgRole>();
			rs.setResultlist(list);
			rs.setTotalrecord(new Long(list.size()));
			setRoleSuccessJson(rs);
		}
		return SUCCESS;
	}
	
	/**
	 * 装配Json返回值
	 * 
	 * @param rs
	 * @return
	 */
	private void setRoleSuccessJson(QueryResult<OrgRole> rs) {
		if (rs.getResultlist() != null && rs.getResultlist().size() > 0) {
			String currentOrgId = getOnlineUser().getLoginedOrg().getId();
			List<RoleVO> vos = orgRoleEO2RoleEO(rs.getResultlist(),currentOrgId);
			// JsonTree 转换
			String resultJson = JSON.toJSONString(vos, true);
			StringBuffer json = new StringBuffer();
			json.append("{success:true");
			json.append(",totalCounts:");
			json.append(rs.getTotalrecord());
			json.append(",result:");
			json.append(resultJson);
			json.append("}");
			setJsonString(json.toString());
		} else {
			setJsonString("{success:true,totalCounts:0,result:[]}");
		}
	}
	/**
	 * 组织角色列表获取角色
	 * 
	 * @param from
	 * @param orgId
	 * @return
	 */
	private List<RoleVO> orgRoleEO2RoleEO(List<OrgRole> from, String orgId) {
		ArrayList<RoleVO> list = new ArrayList<RoleVO>();
		for (int i = 0; i < from.size(); i++) {
			Role eo = from.get(i).getRole();
			RoleVO vo = new RoleVO();
			vo.setId(eo.getId());
			vo.setCode(eo.getCode());
			vo.setName(eo.getName());
			vo.setLocked(eo.getLocked());
			vo.setIsEdit(orgId.equals(eo.getOrg().getId()) ? true : false);
			vo.setOrgRoleId(from.get(i).getId());
			list.add(vo);
		}
		return list;
	}
	
	/**
	 * 
	 * 根据组织查询出该组织下面的所有用户 自己创建的和上面分配的角色的用户
	 * @author yangtao
	 * @date 2011-8-15下午02:55:53
	 * @return
	 */
	@Action(value = "queryUserList")
	public String queryUserList() {
		String orgId = this.getRequest().getParameter("orgId");
		if (StringUtils.isEmpty(orgId)) {
			orgId = getOnlineUser().getLoginedOrg().getId();
		}

		// 判断要查询的公司是否拥有权限，如果没有权限，则默认还是自己所在的公司
		QueryResult<User> qr = this.userService.getUsersByOrgID(orgId,false,
				conditions, -1, -1, getOrderBy());
		JsonResult jr = new JsonResult(qr.getTotalrecord().intValue(), qr
				.getResultlist());
		jr.setContainFields(new String[] { "id", "loginId", "name",
				"createTime", "lastLoginTime", "email", "personId", "state" });
		this.setJsonString(jr.toJSONString());
		return SUCCESS;
	}
	
	/**
	 * 
	 * 获得某个组织下面的岗位
	 * @author yangtao
	 * @date 2011-8-8下午03:03:41
	 * @return
	 * @throws BizException
	 */
	@Action("getPostList")
	public String getPostList() throws BizException {
		String assignOrgId = getRequest().getParameter("assignOrgId");
		if (!StringUtils.isEmpty(assignOrgId)) {
			List<Post> posts = postService.getPostsByOrgId(assignOrgId);
			String json = "";
			json = FastJsonUtils.toJson(posts, new String[] { "id", "name",
					"code", "charge", "parent", "dept" });
			json = json.replaceAll("\"parent\":null", "\"parent\":{}");
			setJsonString(json);
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
			e.printStackTrace();
		}finally{
			if(out!=null)
				out.close();
		}
	}
	public String getOrgIds() {
		return orgIds;
	}

	public void setOrgIds(String orgIds) {
		this.orgIds = orgIds;
	}

	public ProDefinition getDef() {
		return def;
	}

	public void setDef(ProDefinition def) {
		this.def = def;
	}

	public void setBcFunctionService(BcFunctionService bcFunctionService) {
		this.bcFunctionService = bcFunctionService;
	}

	public void setBpDefinitionService(ProDefinitionService bpDefinitionService) {
		this.bpDefinitionService = bpDefinitionService;
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

	/**
	 * @return the postService
	 */
	public PostService getPostService() {
		return postService;
	}

	/**
	 * @param postService the postService to set
	 */
	public void setPostService(PostService postService) {
		this.postService = postService;
	}

	/**
	 * @return the orgRoleService
	 */
	public OrgRoleService getOrgRoleService() {
		return orgRoleService;
	}

	/**
	 * @param orgRoleService the orgRoleService to set
	 */
	public void setOrgRoleService(OrgRoleService orgRoleService) {
		this.orgRoleService = orgRoleService;
	}

	/**
	 * @return the bcFunctionService
	 */
	public BcFunctionService getBcFunctionService() {
		return bcFunctionService;
	}

	/**
	 * @return the bpDefinitionService
	 */
	public ProDefinitionService getBpDefinitionService() {
		return bpDefinitionService;
	}

	/**
	 * @return the userService
	 */
	public UserService getUserService() {
		return userService;
	}

	/**
	 * @param userService the userService to set
	 */
	public void setUserService(UserService userService) {
		this.userService = userService;
	}

	/**
	 * @return the conditions
	 */
	public List<QueryCondition> getConditions() {
		return conditions;
	}

	/**
	 * @param conditions the conditions to set
	 */
	public void setConditions(List<QueryCondition> conditions) {
		this.conditions = conditions;
	}

	/**
	 * @return the proActivityService
	 */
	public ProActivityService getProActivityService() {
		return proActivityService;
	}

	/**
	 * @param proActivityService the proActivityService to set
	 */
	public void setProActivityService(ProActivityService proActivityService) {
		this.proActivityService = proActivityService;
	}

	public ExecutionAPIService getExecutionAPIService() {
		return executionAPIService;
	}

	public void setExecutionAPIService(ExecutionAPIService executionAPIService) {
		this.executionAPIService = executionAPIService;
	}
	
	
}
