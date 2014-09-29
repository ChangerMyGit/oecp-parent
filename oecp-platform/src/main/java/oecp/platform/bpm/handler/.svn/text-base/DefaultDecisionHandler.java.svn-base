package oecp.platform.bpm.handler;

import groovy.lang.Binding;
import groovy.lang.GroovyShell;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import oecp.framework.dao.DAO;
import oecp.framework.dao.QueryCondition;
import oecp.framework.entity.base.BaseEO;
import oecp.framework.util.SpringContextUtil;
import oecp.platform.bpm.enums.ProcessVariableName;
import oecp.platform.bpm.eo.VirProActivity;
import oecp.platform.bpm.service.ProActivityService;

import org.jbpm.api.jpdl.DecisionHandler;
import org.jbpm.api.model.OpenExecution;

/**
 * 
 *	decision条件节点默认执行接口
 * @author YangTao
 * @date 2012-1-30上午11:44:56
 * @version 1.0
 */
public class DefaultDecisionHandler implements DecisionHandler {

	/* (non-Javadoc)
	 * @see org.jbpm.api.jpdl.DecisionHandler#decide(org.jbpm.api.model.OpenExecution)
	 */
	@Override
	public String decide(OpenExecution execution) {
		System.out.println("------begin enter into DefaultDecisionHandler");
		String result = "";
		//从spring中取出服务方法
		ProActivityService proActivityService = (ProActivityService)SpringContextUtil.getBean("proActivityService");
		
		//根据业务主键获取表单信息
		String bizPK = (String)execution.getVariables().get(ProcessVariableName.BUSINESS_KEY);
		Map<String, Object> map = null;
		
		//虚拟流程ID在启动流程时就放在流程变量里面
		String virProDefinitionId = (String)execution.getVariables().get(ProcessVariableName.VIR_PRO_DEFINITION_ID);
		//当前任务节点的名称
		String activityName = execution.getActivity().getName();
		//查询到当前虚拟任务节点，取出后台配置的信息
		List<QueryCondition> conditions = new ArrayList<QueryCondition>();
		QueryCondition qc = new QueryCondition("virProDefinition.id","=",virProDefinitionId);
		QueryCondition qc2 = new QueryCondition("activityName","=",activityName);
		conditions.add(qc);
		conditions.add(qc2);
		List<VirProActivity> vpas = proActivityService.getVirProActivityByConditons(conditions);
		if(vpas.size()!=0){
			VirProActivity vpa = vpas.get(0);
			String mainEntity = vpa.getVirProDefinition().getProDefinition().getBelongFunction().getMainEntity();
			Class<BaseEO> cla = null;
			BaseEO bbe = null;
			try {
				cla = (Class<BaseEO>)Class.forName(mainEntity);
			} catch (ClassNotFoundException e) {
				e.printStackTrace();
			}
			
			if(cla!=null){
				String bizDaoname = vpa.getVirProDefinition().getProDefinition().getBelongFunction().getBc().getDaoName();
				DAO dao = (DAO)SpringContextUtil.getBean(bizDaoname);
				bbe = (BaseEO)dao.find(cla, bizPK);
			}
			String decisionScript = vpa.getDecisionScript();
			//向Groovy脚本设置单据参数，执行脚本
			Binding bb = new Binding();
			bb.setVariable("eo", bbe);
			GroovyShell gs = new GroovyShell(bb);
			Object obj = gs.evaluate(decisionScript);
			result = (String)obj;
		}
		
		System.out.println("------end enter into DefaultDecisionHandler");
		return result;
	}

}
