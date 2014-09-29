package oecp.platform.bpm.handler;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import oecp.framework.dao.DAO;
import oecp.framework.dao.QueryCondition;
import oecp.framework.exception.BizException;
import oecp.framework.util.SpringContextUtil;
import oecp.platform.bpm.enums.ExceptionMsgType;
import oecp.platform.bpm.enums.ProcessVariableName;
import oecp.platform.bpm.eo.CandidateUser;
import oecp.platform.bpm.eo.TaskEo;
import oecp.platform.bpm.eo.VirProActivity;
import oecp.platform.bpm.eo.VirProcessInstance;
import oecp.platform.bpm.service.MyAgentUserService;
import oecp.platform.bpm.service.ProActivityService;
import oecp.platform.message.service.MessageService;
import oecp.platform.user.eo.User;
import oecp.platform.user.service.UserService;

import org.apache.commons.lang.xwork.StringUtils;
import org.jbpm.api.model.OpenExecution;
import org.jbpm.api.task.Assignable;
import org.jbpm.api.task.AssignmentHandler;
import org.jbpm.pvm.internal.task.TaskImpl;


/**
 * 
 * 每个流程中的每个任务都加上这个默认的任务指派人员接口
 * @author yangtao
 * @date 2011-7-28下午03:26:58
 * @version 1.0
 */
public class DefaultTaskAssignHandler implements AssignmentHandler {
	

	@Override
	public void assign(Assignable arg0, OpenExecution arg1) throws Exception {
		System.out.println("------begin enter into DefaultTaskAssignHandler");
		
		SimpleDateFormat dateformat1=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		//该节点任务指派人员是否成功
		boolean assignSuccess = false;
		
		//从spring中取出服务方法
		ProActivityService proActivityService = (ProActivityService)SpringContextUtil.getBean("proActivityService");
		MyAgentUserService myAgentUserService = (MyAgentUserService)SpringContextUtil.getBean("myAgentUserService");
		UserService userService = (UserService)SpringContextUtil.getBean("userService");
		MessageService messageService = (MessageService)SpringContextUtil.getBean("messageService");
		DAO dao = (DAO)SpringContextUtil.getBean("platformDao");
		
		//虚拟流程ID在启动流程时就放在流程变量里面
		String virProDefinitionId = (String)arg1.getVariables().get(ProcessVariableName.VIR_PRO_DEFINITION_ID);
		oecp.platform.user.eo.User creator = (oecp.platform.user.eo.User)arg1.getVariables().get(ProcessVariableName.CREATOR);
		//根据业务主键获取表单信息
		String bizPK = (String)arg1.getVariables().get(ProcessVariableName.BUSINESS_KEY);
		String preTaskId = (String)arg1.getVariables().get(ProcessVariableName.PRE_TASKID);
		String nextTaskUser = (String)arg1.getVariables().get(ProcessVariableName.NEXT_TASK_USER);
		//当前任务节点的名称
		String activityName = arg1.getActivity().getName();
		//查询到当前虚拟任务节点，取出后台配置的信息
		List<QueryCondition> conditions = new ArrayList<QueryCondition>();
		QueryCondition qc = new QueryCondition("virProDefinition.id","=",virProDefinitionId);
		QueryCondition qc2 = new QueryCondition("activityName","=",activityName);
		conditions.add(qc);
		conditions.add(qc2);
		List<VirProActivity> vpas = proActivityService.getVirProActivityByConditons(conditions);
		if(vpas.size()!=0){
			VirProActivity vpa = vpas.get(0);
			List<oecp.platform.user.eo.User> list = proActivityService.getVirProActivityAssignedUsers(vpa);
			//获得前一个任务节点的名称
			String preActivityName = proActivityService.getPreTaskByCurrentTask(activityName, vpa.getVirProDefinition().getProDefinition().getDeployId());
			//获取当前任务
			TaskImpl taskImpl = (TaskImpl)arg0;
			//封装平台的任务
			VirProcessInstance vpi = (VirProcessInstance)arg1.getVariables().get(ProcessVariableName.VIR_PRO_INSTANCE);
			TaskEo te = new TaskEo();
			te.setVirProcessInstance(vpi);
			te.setTaskId(taskImpl.getId());
			te.setPreTaskId(preTaskId);
			te.setTaskName(taskImpl.getName());
			te.setStartTime(dateformat1.format(taskImpl.getCreateTime()));
			te.setCounterSignRule(vpa.getCounterSignRule());
			te.setPassRate(vpa.getPassRate());
			te.setNextTask(vpa.getNextTask());
			te.setNextTaskUser(vpa.getNextTaskUser());
			te.setEditBill(vpa.getEditBill());
			te.setIsEnd(false);
			
			List<CandidateUser> li = new ArrayList<CandidateUser>();
			//第一 没有上一个任务节点的任务是第一个默认的任务，应该是创建人
			if(StringUtils.isEmpty(preActivityName)){
					arg0.addCandidateUser(creator.getLoginId());
					oecp.platform.user.eo.User agent = myAgentUserService.getMyAgentUser(virProDefinitionId, creator);
					CandidateUser cd = new CandidateUser(creator,agent);
					dao.create(cd);
					li.add(cd);
					assignSuccess = true;
			}else{//第二  给当前任务指派人员
				if(StringUtils.isNotBlank(nextTaskUser)){//1 以前一个任务指派人员优先
					String[] userIds = nextTaskUser.split(",");
					for(String userId : userIds){
						User user = userService.find(userId);
//						if(!user.getLoginId().equals(vpi.getCreateUserLoginId())){//剔除表单录入人（流程发起者不用审核);
							//先指定任务的候选人，再给该候选人发消息
							oecp.platform.user.eo.User agent = myAgentUserService.getMyAgentUser(virProDefinitionId, user);
							CandidateUser cd = new CandidateUser(user,agent);
							dao.create(cd);
							li.add(cd);
							arg0.addCandidateUser(user.getLoginId());
							assignSuccess = true;
//						}
					}
					arg1.setVariable(ProcessVariableName.NEXT_TASK_USER,null);
				}else{//2 后台给当前任务节点配置的人员,都作为这个任务的候选人
					if(vpa.getIsCommitUser()!=null&&vpa.getIsCommitUser()){//流程发起者审核
						arg0.addCandidateUser(creator.getLoginId());
						oecp.platform.user.eo.User agent = myAgentUserService.getMyAgentUser(virProDefinitionId, creator);
						CandidateUser cd = new CandidateUser(creator,agent);
						dao.create(cd);
						li.add(cd);
						assignSuccess = true;
					}else{
						for(oecp.platform.user.eo.User user : list){
//							if(!user.getLoginId().equals(vpi.getCreateUserLoginId())){//剔除表单录入人（流程发起者不用审核);
								//先指定任务的候选人，再给该候选人发消息
								oecp.platform.user.eo.User agent = myAgentUserService.getMyAgentUser(virProDefinitionId, user);
								CandidateUser cd = new CandidateUser(user,agent);
								dao.create(cd);
								li.add(cd);
								arg0.addCandidateUser(user.getLoginId());
								assignSuccess = true;
//							}
						}
					}
				}
			}
			te.setCandidateUserCount(li.size());
			te.setCandidateUsers(li);
			proActivityService.saveTaskEo(te);
		}
		if(!assignSuccess)
			throw new BizException(ExceptionMsgType.NEXT_TASK_NO_CANDIDATE);
		
		System.out.println("------end enter into DefaultTaskAssignHandler");
	}
}
