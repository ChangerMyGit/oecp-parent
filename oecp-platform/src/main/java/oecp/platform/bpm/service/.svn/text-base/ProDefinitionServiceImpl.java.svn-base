/*
 * Copyright (c) 2011 OECP All Rights Reserved.                	
 * <a href="http://www.oecp.cn">http://www.oecp.cn</a>                                                                 
 */

package oecp.platform.bpm.service;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.annotation.Resource;

import oecp.framework.dao.QueryCondition;
import oecp.framework.dao.QueryResult;
import oecp.framework.exception.BizException;
import oecp.platform.base.service.PlatformBaseServiceImpl;
import oecp.platform.bcfunction.eo.Function;
import oecp.platform.bcfunction.service.BcFunctionService;
import oecp.platform.bpm.enums.CounterSignRule;
import oecp.platform.bpm.enums.ExceptionMsgType;
import oecp.platform.bpm.enums.TaskActivityAuditType;
import oecp.platform.bpm.eo.OrgProcess;
import oecp.platform.bpm.eo.ProActivity;
import oecp.platform.bpm.eo.ProDefinition;
import oecp.platform.bpm.eo.VirProActivity;
import oecp.platform.bpm.eo.VirProDefinition;
import oecp.platform.bpm.eo.VirProcessInstance;
import oecp.platform.bpm.vo.Node;
import oecp.platform.bpm.vo.ProDefinitionInfo;
import oecp.platform.bpm.vo.VirProcessDefinitionInfo;
import oecp.platform.event.service.EventEngine;
import oecp.platform.org.eo.Organization;
import oecp.platform.org.eo.Post;
import oecp.platform.org.service.OrgService;
import oecp.platform.role.eo.OrgRole;
import oecp.platform.user.eo.User;

import org.apache.commons.lang.StringUtils;
import org.jbpm.api.ProcessDefinition;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


/**
 * 
 * @author yongtree
 * @date 2011-6-21 上午09:25:11
 * @version 1.0
 */
@Transactional
@Service(value = "bpDefinitionService")
public class ProDefinitionServiceImpl extends
		PlatformBaseServiceImpl<ProDefinition> implements ProDefinitionService {

	@Resource
	private JbpmService jbpmService;

	@Resource
	private OrgService orgService;

	@Resource
	private BcFunctionService bcFunctionService;
	
	@Resource
	private ProActivityService proActivityServcie;

	@Resource
	private EventEngine eventEngine;

	private SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	
	
	/**
	 * 获取流程定义列表
	 * @author luanyoub
	 * @date 2014年1月10日上午10:16:02
	 * @param org
	 * @param funcId
	 * @return
	 */
	@Override
	public List<ProDefinitionInfo> getProDefinitions(Organization org, String funcId) {
		List<ProDefinition> defs = new ArrayList();
		List<ProDefinitionInfo> result = new ArrayList();
		if(funcId!=null){
			defs = this.getDao().queryByWhere(ProDefinition.class,
					"belongFunction.id=?", new Object[]{funcId});
		}else{
			defs = this.getDao().queryByWhere(ProDefinition.class,null,null);
		}
		for(ProDefinition pr :defs){
			ProDefinitionInfo pdi = new ProDefinitionInfo();
			pdi.setId(pr.getId());
			pdi.setDeployId(pr.getDeployId());
			pdi.setBelongFunctionId(pr.getBelongFunction().getId());
			pdi.setBelongFunctionName(pr.getBelongFunction().getName());
			pdi.setCreatedByOrgId(pr.getCreatedByOrg().getId());
			pdi.setCreatedByOrgName(pr.getCreatedByOrg().getName());
			pdi.setCreateTime(format.format(pr.getCreateTime()));
			pdi.setName(pr.getName());
			pdi.setVersion(pr.getVersion());
			pdi.setDescription(pr.getDescription());
			pdi.setWebPictureString(pr.getWebPictureString());
			result.add(pdi);
		}
		return result;
	}

	/**
	 * 
	 * 根据流程定义名称获取流程
	 * @author yangtao
	 * @date 2011-9-14上午10:44:03
	 * @param processName
	 * @return
	 */
	@Override
	public QueryResult<ProDefinitionInfo> getProDefinitions(List<QueryCondition> conditions,Organization org,int start,int limit) {
		QueryCondition qc = new QueryCondition();
		qc.setField("createdByOrg.id");
		qc.setOperator("=");
		qc.setValue(org.getId());
		conditions.add(qc);
		QueryResult<ProDefinitionInfo> resultqr = new QueryResult<ProDefinitionInfo>();
		QueryResult<ProDefinition> qr = this.getDao().getScrollData(ProDefinition.class,start,limit, conditions, null);
		List<ProDefinitionInfo> result = new ArrayList<ProDefinitionInfo>();
		for(ProDefinition pr :qr.getResultlist()){
				ProDefinitionInfo pdi = new ProDefinitionInfo();
				pdi.setId(pr.getId());
				pdi.setDeployId(pr.getDeployId());
				pdi.setBelongFunctionId(pr.getBelongFunction().getId());
				pdi.setBelongFunctionName(pr.getBelongFunction().getName());
				pdi.setCreatedByOrgId(pr.getCreatedByOrg().getId());
				pdi.setCreatedByOrgName(pr.getCreatedByOrg().getName());
				pdi.setCreateTime(pr.getCreateTime().toString());
				pdi.setName(pr.getName());
				pdi.setVersion(pr.getVersion());
				pdi.setDescription(pr.getDescription());
				result.add(pdi);
		}
		resultqr.setResultlist(result);
		resultqr.setTotalrecord(qr.getTotalrecord());
		return resultqr;
	}
	
	public ProDefinition getDistriProDefinition(Organization org, Long funcId) {
		String sql = "o.org=?";
		List params = new ArrayList();
		params.add(org);
		if (funcId != null && funcId != 0) {
			sql = sql + " and o.def.belongFunction.id=?";
			params.add(funcId);
		}
		List<OrgProcess> list = this.getDao().queryByWhere(OrgProcess.class,
				sql, params.toArray());
		for (OrgProcess op : list) {
			return op.getDef();
		}
		return null;
	}

	/**
	 * 发布流程定义
	 */
	public void deploy(String orgIds, ProDefinition proDefinition, File zip)
			throws BizException {
		try {
			Function function = bcFunctionService.find(proDefinition
					.getBelongFunction().getId());
			if (function == null || !function.getWsuserd()) {
				throw new BizException(ExceptionMsgType.NOT_CREATE_PROCESS_TO_FUNCTION);
			}

			//jbpm4中保存流程定义
			String deployId = jbpmService.deploy(zip);

			ProcessDefinition pd = jbpmService
					.getProcessDefinitionByDeployId(deployId);
			// 添加或者更新流程节点
			List<Node> nodes = jbpmService.getTaskNodesFromXml(jbpmService
					.getDefinitionXmlByDpId(deployId), false);
			List<ProActivity> list = new ArrayList<ProActivity>();
			// TODO 将新增和修改分成两个处理方法
			if (proDefinition.getId() == null) {
				for (Node node : nodes) {
					ProActivity pa = new ProActivity();
					pa.setActivityName(node.getName());
					pa.setProDef(proDefinition);
					list.add(pa);
				}
			} else {
				if (proDefinition.getDeployId() != null) {
					// 不进行删除所有旧流程的东西，保留旧流程运行中的信息。
					jbpmService.deleteDeployment(proDefinition.getDeployId(),
							false);
				}
				for (Node node : nodes) {
					ProActivity activity = getActivityByName(proDefinition
							.getActivities(), node.getName());
					if (activity == null) {
						activity = new ProActivity();
						activity.setProDef(proDefinition);
						activity.setActivityName(node.getName());
					}
					list.add(activity);
				}
			}
			proDefinition.setBelongFunction(function);
			proDefinition.setActivities(list);
			proDefinition.setDeployId(deployId);
			proDefinition.setProDefId(pd.getId());
			proDefinition.setVersion(pd.getVersion());
			this.save(proDefinition);

		} catch (Exception e) {
			e.printStackTrace();
			throw new BizException(e);
		}

	}
	
	/**
	 * 删除流程定义
	 */
	public void delete(String deployId,String proDefinitionId,Organization org)
	throws Exception {
		try {
			ProDefinition pd = this.find(proDefinitionId);
			if(!(pd.getCreatedByOrg().getId()).equals(org.getId())){
				throw new BizException(ExceptionMsgType.DELETE_PROCESS_DEFINITION_YOURSELF_CREATED);
			}
			List<VirProDefinition> vps = this.getDao().queryByWhere(VirProDefinition.class, "proDefinition.id=?", new Object[] { proDefinitionId});
			if(vps.size()!=0){
				throw new BizException(ExceptionMsgType.DELETE_PROCESS_ASSIGNED);
			}
			//删除jbpm4中保存流程定义
			jbpmService.deleteDeployment(deployId, true);
			//删除封装的流程节点
			List<ProActivity> pas = this.getDao().queryByWhere(ProActivity.class, "proDef.id=?", new Object[] { proDefinitionId});
			for(ProActivity pa : pas){
				this.getDao().delete(ProActivity.class, pa.getId());
			}
			//删除封装的流程定义
			this.delete(proDefinitionId);
			
		} catch (Exception e) {
			throw e;
		}
	}
	
	
	/**
	 * 
	 * 根据jbpm4中的流程定义得到封装的流程定义
	 * @author yangtao
	 * @date 2011-7-27上午10:44:53
	 * @param pd
	 * @return
	 */
	public ProDefinition getProDefinition(ProcessDefinition pd){
		ProDefinition p = null;
		 try {
			p = this.getDao().queryByWhere(ProDefinition.class,
					"proDefId=?", new Object[] { pd.getId()}).get(0);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return p;
	}

	private ProActivity getActivityByName(List<ProActivity> list,
			String activityName) {
		for (ProActivity a : list) {
			if (activityName.equals(a.getActivityName()))
				return a;
		}
		return null;
	}
	
	/**
	 * 根据某个流程定义，取出该流程中所有的任务节点和开始节点
	 * 
	 * @param deployId
	 *            是否包括启动节点
	 * @return
	 */
	public List<Node> FindAllActivities(String deployId){
		return this.jbpmService.FindAllActivities(deployId);
	}
	
	/**
	 * 
	 * 分配组织流程
	 * @author yangtao
	 * @date 2011-8-1下午03:37:27
	 * @param processDefinitionId
	 * @param orgIds
	 */
	public void addVirProcessDefinition(String processDefinitionId,String orgIds)throws Exception{
			ProDefinition pd = this.find(processDefinitionId);
			List<Node> list = this.FindAllActivities(pd.getDeployId());
			String[] ids = orgIds.split(",");
			for(String id : ids){
				if(id!=null&&!"".equals(id)){
					//先根据组织和流程定义查询是否已分配
//					ProDefinition proDefinition = this.getDao().queryByWhere(ProDefinition.class, "o.id=?", new Object[]{processDefinitionId}).get(0);
					
					List list2 = this.getDao().queryByWhere(VirProDefinition.class, "proDefinition.id=? and proDefinition.belongFunction.id=? and assignedOrg.id=? ", new Object[]{pd.getId(),pd.getBelongFunction().getId(),id});
					if(list2.size()>0)
						throw new BizException(ExceptionMsgType.ONE_FUNCTION_ONE_ORG);
					Organization org = this.orgService.find(id);
					VirProDefinition vp = new VirProDefinition();
					vp.setName(pd.getName()+"--"+org.getName());
					vp.setProDefinition(pd);
					vp.setAssignedOrg(org);
					vp.setVersion(pd.getVersion());
					vp.setCreateTime(new Date());
					vp.setIsUse(false);
					this.getDao().create(vp);
					for(Node node : list){
						VirProActivity vpa = new VirProActivity();
						vpa.setCounterSignRule(CounterSignRule.NO_COUNTERSIGN_RULE);
						vpa.setActivityName(node.getName());
						vpa.setVirProDefinition(vp);
						this.getDao().create(vpa);
					}
				}
			}
	}
	
	/**
	 * 
	 * 虚拟流程列表
	 * @author yangtao
	 * @date 2011-8-1下午03:47:11
	 * @param condition
	 * @param start
	 * @param limit
	 * @return
	 */
	public QueryResult<VirProcessDefinitionInfo> queryVirProcessDefinition(String[] params,int start,int limit,Organization og){
		String proDefinitionId = params[0];
		String functionId = params[1];
		String sql = "(assignedOrg.id=? or proDefinition.createdByOrg.id=?)";
		List list = new ArrayList();
		list.add(og.getId());
		list.add(og.getId());
		if(StringUtils.isNotEmpty(proDefinitionId)){
			sql+=" and proDefinition.id=? ";
			list.add(proDefinitionId);;
		}
		if(StringUtils.isNotEmpty(functionId)){
			sql+=" and proDefinition.belongFunction.id=? ";
			list.add(functionId);;
		}
		Object[] ob = list.toArray();
		LinkedHashMap<String,String> map = new LinkedHashMap<String,String>();
		map.put("isUse", "DESC");
		map.put("id", "ASC");
		
		QueryResult<VirProDefinition> qr = this.getDao().getScrollData(VirProDefinition.class, start, limit, sql, ob, map);
		QueryResult<VirProcessDefinitionInfo> qr2 = new QueryResult<VirProcessDefinitionInfo>();
		List<VirProcessDefinitionInfo> resultList = new ArrayList<VirProcessDefinitionInfo>();
		for(VirProDefinition v : qr.getResultlist()){
			VirProcessDefinitionInfo vi = new VirProcessDefinitionInfo();
			vi.setId(v.getId());
			vi.setName(v.getName());
			vi.setProDefinitionId(v.getProDefinition().getId());
			vi.setProDefinitionName(v.getProDefinition().getName());
			vi.setDeployId(v.getProDefinition().getDeployId());
			vi.setProcessDefinitionId(v.getProDefinition().getProDefId());
			vi.setAssignedOrgId(v.getAssignedOrg().getId());
			vi.setAssignedOrgName(v.getAssignedOrg().getName());
			vi.setIsUseId(v.getIsUse()?"1":"0");
			vi.setIsUseName(v.getIsUse()?"已启用":"未启用");
			vi.setBelongFunctionId(v.getProDefinition().getBelongFunction().getId());
			vi.setBelongFunctionName(v.getProDefinition().getBelongFunction().getName());
			resultList.add(vi);
		}
		qr2.setResultlist(resultList);
		qr2.setTotalrecord(qr.getTotalrecord());
		return qr2;
	}

	/**
	 * 
	 * 根据虚拟流程ID获得所有虚拟任务节点
	 * @author yangtao
	 * @date 2011-8-2上午08:49:47
	 * @param virProcessDefId
	 * @return
	 */
	public List<VirProActivity> FindAllVirTaskActivities(String virProcessDefId){
		List<VirProActivity> list = this.getDao().queryByWhere(VirProActivity.class, "virProDefinition.id=?", new Object[]{virProcessDefId});
		return list;
	}
	
	/**
	 * 
	 * 删除虚拟流程
	 * @author yangtao
	 * @date 2011-8-2上午08:56:31
	 */
	public void deleteVirProcessDefinition(String virProcessDefIds)throws Exception {
		try {
			String[] ids = virProcessDefIds.split(",");
			for(String id : ids){
				if(id!=null&&!"".equals(id)){
					List<VirProcessInstance> vpis = this.getDao().queryByWhere(VirProcessInstance.class, "virProDefinition.id=?", new Object[] { id});
					if(vpis.size()!=0){
						throw new BizException(ExceptionMsgType.DELETE_PROCESS_DEFINITION_FAILURE);
					}
					List<VirProActivity> vpas = this.getDao().queryByWhere(VirProActivity.class, "virProDefinition.id=?", new Object[] { id});
					for(VirProActivity vpa : vpas){
						//删除虚拟节点
						this.getDao().delete(VirProActivity.class, vpa.getId());
					}
					//删除虚拟流程定义
					this.getDao().delete(VirProDefinition.class, id);
				}
			}
		} catch (Exception e) {
			throw e;
		}	
	}
	
	/**
	 * 
	 * 给虚拟节点进行分配
	 * @author yangtao
	 * @date 2011-8-2下午02:54:04
	 * @param virProActivityId
	 * @param virProDefinitionId
	 * @param assignFlag
	 * @param ids
	 * @return
	 */
	public String assignVirProActivity(String virProActivityId,String virProDefinitionId,String counterSignRule,String passRate,String assignFlag,String ids,Organization org,String[] config){
		VirProActivity vpa = this.getDao().find(VirProActivity.class, virProActivityId);
		VirProDefinition vpd = vpa.getVirProDefinition();
		//会签规则
		if(!StringUtils.isEmpty(counterSignRule)){
			if(CounterSignRule.ONE_TICKET_PASS.toString().equals(counterSignRule)){
				vpa.setCounterSignRule(CounterSignRule.ONE_TICKET_PASS);
				vpa.setPassRate(null);
			}else if(CounterSignRule.ONE_TICKET_NO_PASS.toString().equals(counterSignRule)){
				vpa.setCounterSignRule(CounterSignRule.ONE_TICKET_NO_PASS);
				vpa.setPassRate(null);
			}else if(CounterSignRule.PROPORTION.toString().equals(counterSignRule)){
				vpa.setCounterSignRule(CounterSignRule.PROPORTION);
				vpa.setPassRate(passRate);
			}
		}else{
			vpa.setCounterSignRule(CounterSignRule.NO_COUNTERSIGN_RULE);
			vpa.setNextTask(config[0]);
			vpa.setNextTaskUser(config[1]);
			vpa.setEditBill(config[2]);
		}
		
		if(!StringUtils.isEmpty(ids)){
			String[] idss = ids.split(",");
			if(idss.length==0){//什么也不选，都是空
				vpa.setAssignUsers(null);
				vpa.setAssignPosts(null);
				vpa.setAssignRoles(null);
				vpa.setIsCommitUser(false);
			}else{
				if(TaskActivityAuditType.USER.toString().equals(assignFlag)){
					List<User> assignUsers = new ArrayList<User>();
					for(String id : idss){
						if(!StringUtils.isEmpty(id)){
							User user = this.getDao().find(User.class, id);
							assignUsers.add(user);
						}
					}
					vpa.setAssignUsers(assignUsers);
					vpa.setAssignPosts(null);
					vpa.setAssignRoles(null);
					vpa.setIsCommitUser(false);
				}else if(TaskActivityAuditType.POST.toString().equals(assignFlag)){
					List<Post> assignPosts = new ArrayList<Post>();
					for(String id : idss){
						if(!StringUtils.isEmpty(id)){
							Post post = this.getDao().find(Post.class, id);
							assignPosts.add(post);
						}
					}
					vpa.setAssignUsers(null);
					vpa.setAssignPosts(assignPosts);
					vpa.setAssignRoles(null);
					vpa.setIsCommitUser(false);
				}else if(TaskActivityAuditType.ROLE.toString().equals(assignFlag)){
					List<OrgRole> assignRoles = new ArrayList<OrgRole>();
					for(String id : idss){
						if(!StringUtils.isEmpty(id)){
							OrgRole orgRole = this.getDao().queryByWhere(OrgRole.class, "role.id=? and org.id=?", new Object[]{id,vpd.getAssignedOrg().getId()}).get(0);
							assignRoles.add(orgRole);
						}
					}
					vpa.setAssignUsers(null);
					vpa.setAssignPosts(null);
					vpa.setAssignRoles(assignRoles);
					vpa.setIsCommitUser(false);
				}else if(TaskActivityAuditType.COMMITUSER.toString().equals(assignFlag)){
					vpa.setAssignUsers(null);
					vpa.setAssignPosts(null);
					vpa.setAssignRoles(null);
					vpa.setIsCommitUser(true);
				}
			}
		}else{
			vpa.setAssignUsers(null);
			vpa.setAssignPosts(null);
			vpa.setAssignRoles(null);
			if(TaskActivityAuditType.COMMITUSER.toString().equals(assignFlag))
				vpa.setIsCommitUser(true);
			else
				vpa.setIsCommitUser(false);
		}
		
		this.getDao().update(vpa);
		//触发 任务节点配置保存监听器 来改变代理人信息
		eventEngine.fireEvent(vpa, "changeAgentUser", org,new Object[]{});
		return null;
	}
	/**
	 * 
	 * 给decision节点配置条件信息
	 * @author yangtao
	 * @date 2011-8-26下午02:19:44
	 * @param virProActivityId
	 * @param virProDefinitionId
	 * @param paramName
	 * @param borderValue
	 */
	public void assignDecisionCondition(String virProActivityId,String virProDefinitionId,String paramName,String decisionScript){
		VirProActivity vpa = this.getDao().find(VirProActivity.class, virProActivityId);
		vpa.setDecisionScript(decisionScript);
		this.getDao().update(vpa);
	}
	/**
	 * 
	 * 获得流程图中每个节点的位置的值 x y x1 y1
	 * @author yangtao
	 * @date 2011-8-11上午10:46:05
	 * @param deployId
	 */
	public Map<String,Map> getAllActivityPlaceValue(String deployId){
		return this.jbpmService.getAllActivityPlaceValue(deployId);
	}
	
	
	/**
	 * 
	 * 根据条件获取VirProDefinition
	 * @author yangtao
	 * @date 2011-8-16下午12:01:41
	 * @param conditions
	 * @return
	 */
	public List<VirProDefinition> getVirProDefinitionByConditions(List<QueryCondition> conditions){
		String sqlFiled = "1=1";
		Object[] sqlParams = new Object[conditions.size()];
		int i = 0;
		for(QueryCondition qc : conditions){
			sqlFiled+=" and "+qc.getField()+qc.getOperator()+"? ";
			sqlParams[i] = qc.getValue();
			i++;
		}
		return this.getDao().queryByWhere(VirProDefinition.class, sqlFiled, sqlParams);

	}
	
	
	/**
	 * 
	 * 获取虚拟节点分配人员 角色 岗位的情况
	 * @author yangtao
	 * @date 2011-8-16上午10:08:53
	 * @param virProActivityId
	 * @param virProDefinitionId
	 * @return
	 */
	public String[] getVirProActivityAssign(String virProActivityId,String virProDefinitionId){
		VirProActivity vpa = this.getDao().queryByWhere(VirProActivity.class, "virProDefinition.id=? and id=?", new Object[]{virProDefinitionId,virProActivityId}).get(0);
		String[] result = new String[]{"","","","","","",""};
		List<oecp.platform.user.eo.User> assignUsers = vpa.getAssignUsers();//参与的用户
		List<Post> assignPosts = vpa.getAssignPosts();//参与的岗位
		List<OrgRole> assignRoles = vpa.getAssignRoles();//参与的角色
		Boolean isCommitUser = vpa.getIsCommitUser();//是否是流程发起人
		if(assignUsers!=null && assignUsers.size()!=0){
			result[0] = TaskActivityAuditType.USER.toString();
			for(oecp.platform.user.eo.User user : assignUsers){
				result[1]+=user.getId();
				result[1]+=",";
			}
		}else if(assignPosts!=null && assignPosts.size()!=0){
			result[0] = TaskActivityAuditType.POST.toString();
			for(Post post : assignPosts){
				result[1]+=post.getId();
				result[1]+=",";
			}
		}else if(assignRoles!=null && assignRoles.size()!=0){
			result[0] = TaskActivityAuditType.ROLE.toString();
			for(OrgRole orgRole : assignRoles){
				result[1]+=orgRole.getRole().getId();
				result[1]+=",";
			}
		}else{
			result[0] = TaskActivityAuditType.COMMITUSER.toString();
		}
		result[2] = vpa.getCounterSignRule()==null?"":vpa.getCounterSignRule().toString();
		result[3] = vpa.getPassRate()==null?"":vpa.getPassRate();
		result[4] = StringUtils.isEmpty(vpa.getNextTask())?"false":vpa.getNextTask();
		result[5] = StringUtils.isEmpty(vpa.getNextTaskUser())?"false":vpa.getNextTaskUser();
		result[6] = StringUtils.isEmpty(vpa.getEditBill())?"false":vpa.getEditBill();
		return result;
	}

	/**
	 * 
	 * 得到某个条件节点配置的信息(停用）
	 * @author yangtao
	 * @date 2011-8-26下午02:52:37
	 * @param virProActivityId
	 * @param virProDefinitionId
	 * @return
	 */
	public String[] getDecisionConditionAssign(String virProActivityId,String virProDefinitionId){
		VirProActivity vpa = this.getDao().queryByWhere(VirProActivity.class, "virProDefinition.id=? and id=?", new Object[]{virProDefinitionId,virProActivityId}).get(0);
		String paramName = "";
		String borderValue = "";
		if(vpa.getDecisionConditions().size()!=0){
			paramName = vpa.getDecisionConditions().get(0).getName();
			borderValue = vpa.getDecisionConditions().get(0).getValue();
		}
		
		return new String[]{paramName,borderValue};
	}
	
	/**
	 * 
	 * 获取条件节点上配置的GROOVY脚本
	 * @author YangTao
	 * @date 2012-1-30上午09:20:55
	 * @param virProActivityId
	 * @param virProDefinitionId
	 * @return
	 */
	public String getDecisionConditionScript(String virProActivityId,String virProDefinitionId){
		VirProActivity vpa = this.getDao().queryByWhere(VirProActivity.class, "virProDefinition.id=? and id=?", new Object[]{virProDefinitionId,virProActivityId}).get(0);
		return vpa.getDecisionScript();
	}
	/**
	 * 
	 * 启用流程
	 * @author YangTao
	 * @date 2011-10-19上午11:39:55
	 * @param user
	 * @param virProDefinitionId
	 */
	public void useVirProcessDefinition(User user,String virProDefinitionId,String isUseId)throws Exception{
		try {
			VirProDefinition vpd = this.getDao().find(VirProDefinition.class, virProDefinitionId);
			List<VirProDefinition> list = this.getDao().queryByWhere(VirProDefinition.class, "proDefinition.belongFunction.id=? and assignedOrg.id=? ", new Object[]{vpd.getProDefinition().getBelongFunction().getId(),vpd.getAssignedOrg().getId()});
			for(VirProDefinition vp : list){
				if(vp.getIsUse()){//停用
					vp.setIsUse(isUseId.equals("0")?false:true);
					vp.setUseDateTime(format.format(new Date()));
					vp.setUseLoginId(user.getLoginId());
					this.getDao().update(vp);
				}
			}
			//启用
			vpd.setIsUse(isUseId.equals("0")?true:false);
			vpd.setUseDateTime(format.format(new Date()));
			vpd.setUseLoginId(user.getLoginId());
			this.getDao().update(vpd);
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}
	}
	
	/**
	 * 根据功能编号和组织获取已经启用的流程定义
	 * 
	 * @author YangTao
	 * @date 2012-2-9上午10:55:29
	 * @param functionCode
	 * @param orgId
	 * @return
	 * @throws Exception
	 */
	public VirProDefinition getVirProDefinition(String functionCode,String orgId)throws Exception{
		//流程定义
		VirProDefinition virdef = null;
		try {
			//先判断功能上面是有发布的流程
			Function function = this.bcFunctionService.getFunctionByCode(functionCode);
			if (function == null) {
				throw new BizException(ExceptionMsgType.HAVE_NO_FUNCTION);
			}
			if (!function.getWsuserd()){
				throw new BizException(ExceptionMsgType.NO_PROCESS_ON_FUNCTION);
			}
			
			//根据组织和功能编号查询出所配置的虚拟流程定义
			List<QueryCondition> conditions = new ArrayList<QueryCondition> ();
			QueryCondition qc = new QueryCondition("o.assignedOrg.id","=",orgId);//组织
			QueryCondition qc2 = new QueryCondition("o.proDefinition.belongFunction.code","=",functionCode);//功能
			QueryCondition qc3 = new QueryCondition("o.isUse","=",true);//已经启用的流程
			conditions.add(qc);
			conditions.add(qc2);
			conditions.add(qc3);
			
			virdef = null;
			List<VirProDefinition> list = this.getVirProDefinitionByConditions(conditions);
			if(list.size()!=0){
				virdef = list.get(0);
			}
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}
		
		return virdef;
	}
	
	/**
	 * 获取某个虚拟流程定义上面的所分配的所有人员
	 * 
	 * @author YangTao
	 * @date 2012-2-24下午04:39:49
	 * @param virdef
	 * @return
	 */
	public List<User> getUsersOnVirProDefinition(VirProDefinition virdef){
		Set<User> set = new HashSet<User>();
		List<User> resultList = null;
		List<VirProActivity> list = this.getDao().queryByWhere(VirProActivity.class, "virProDefinition=? ", new Object[]{virdef});
		for(VirProActivity vp : list){
			set.addAll(this.proActivityServcie.getVirProActivityAssignedUsers(vp));
		}
		resultList = Arrays.asList(set.toArray(new User[0]));
		return resultList;
	}

	public JbpmService getJbpmService() {
		return jbpmService;
	}

	public void setJbpmService(JbpmService jbpmService) {
		this.jbpmService = jbpmService;
	}

	public OrgService getOrgService() {
		return orgService;
	}

	public void setOrgService(OrgService orgService) {
		this.orgService = orgService;
	}

	public BcFunctionService getBcFunctionService() {
		return bcFunctionService;
	}

	public void setBcFunctionService(BcFunctionService bcFunctionService) {
		this.bcFunctionService = bcFunctionService;
	}

	public ProActivityService getProActivityServcie() {
		return proActivityServcie;
	}

	public void setProActivityServcie(ProActivityService proActivityServcie) {
		this.proActivityServcie = proActivityServcie;
	}

	public EventEngine getEventEngine() {
		return eventEngine;
	}

	public void setEventEngine(EventEngine eventEngine) {
		this.eventEngine = eventEngine;
	}
	
	
}
