<%@ page language="java" import="java.util.*,org.jbpm.api.*,org.jbpm.api.task.*,org.jbpm.api.history.*" pageEncoding="UTF-8"%>
<%
String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
  <head>
    <base href="<%=basePath%>">
    
    <title>My JSP 'MyJsp.jsp' starting page</title>
    
	<meta http-equiv="pragma" content="no-cache">
	<meta http-equiv="cache-control" content="no-cache">
	<meta http-equiv="expires" content="0">    
	<meta http-equiv="keywords" content="keyword1,keyword2,keyword3">
	<meta http-equiv="description" content="This is my page">
	<!--
	<link rel="stylesheet" type="text/css" href="styles.css">
	-->
  </head>
  
  <body>
    <a href="deploy.jsp">deploy</a>
    &nbsp;&nbsp;&nbsp;Username:${sessionScope['username']}
    <br/>
     <%
     	response.setCharacterEncoding("UTF-8");
    	request.setCharacterEncoding("UTF-8");
    	ProcessEngine processEngine = Configuration.getProcessEngine();
		RepositoryService repositoryService = processEngine.getRepositoryService();
		ExecutionService executionService = processEngine.getExecutionService();
		TaskService taskService = processEngine.getTaskService();
		List<ProcessDefinition> list = repositoryService.createProcessDefinitionQuery().list();
		List<ProcessInstance> processInstanceList = executionService.createProcessInstanceQuery().list();
		String userName = session.getAttribute("username").toString();
		List<Task> taskList = taskService.findPersonalTasks(userName);
		HistoryService historyService = processEngine.getHistoryService();
		List<HistoryTask> historyTaskList = historyService.createHistoryTaskQuery().list();
	%>
	流程部署
    <table border="1" width="100%">
    	<tr>
    		<td>
    			id
    		</td>
    		<td>
    			name
    		</td>
    		<td>
    			version
    		</td>
    		<td>
    			start
    		</td>
    		<td>
    			remove
    		</td>
    		<td>
    			suspend
    		</td>
    		<td>
    			resume
    		</td>
    	</tr>
    	<%
			for(ProcessDefinition processDefinition :list){
		%>
		<tr>
    		<td>
    			<%=processDefinition.getId()%>
    		</td>
    		<td>
    			<%=processDefinition.getName() %>
    		</td>
    		<td>
    			<%=processDefinition.getVersion()%>
    		</td>
    		<td>
    			<a href="start.jsp?&id=<%=processDefinition.getId()%>">start</a>
    		</td>
    		<td>
    			<a href="delete.jsp?&id=<%=processDefinition.getDeploymentId()%>">remove</a>
    		</td>
    		<td>
    			<a href="suspend.jsp?&id=<%=processDefinition.getDeploymentId()%>">suspend</a>
    		</td>
    		<td>
    			<a href="resume.jsp?&id=<%=processDefinition.getDeploymentId()%>">resume</a>
    		</td>
    	</tr>
		<% 
			}    
		%>
    </table>
    流程实例
    <table border="1" width="100%">
    	<tr>
    		<td>id</td><td>activity</td><td>state</td><td>view</td>
    	</tr>
    	<%
    		for(ProcessInstance processInstance :processInstanceList){
    	 %>
    	 <tr>
    	 	<td><%=processInstance.getId()%></td>
    	 	<td><%=processInstance.findActiveActivityNames()%></td>
    	 	<td><%=processInstance.getState()%></td>
    	 	<td><a href="view.jsp?id=<%=processInstance.getId()%>">view</a></td>
    	 </tr>
    	 <% 
			}    
		%>
    </table>
	任务待办
     <table border="1" width="100%">
    	<tr>
    		<td>id</td><td>name</td><td>办理</td><td>委托</td>
    	</tr>
    	<%
    		for(Task task :taskList){
    	 %>
    	 <tr>
    	 	<td><%=task.getId()%></td>
    	 	<td><%=task.getName()%></td>
    	 	<td><a href="<%=task.getFormResourceName()%>?taskId=<%=task.getId()%>&executionId=<%=task.getExecutionId()%>">办理</a></td>
    	 	<td><a href="delegate.jsp?id=<%=task.getId()%>">委托</a></td>
    	 </tr>
    	 <% 
			}    
		%>
    </table>
          历史任务
    <table border="1" width="100%">
    	<tr>
    		<td>id</td><td>instanceId</td><td>endTime</td><td>assignee</td><td>outcome</td><td>fetch</td>
    	</tr>
    	<%for(HistoryTask historyTask: historyTaskList){ %>
    	<tr>
    		<td><%=historyTask.getId()%></td>
    		<td><%=historyTask.getExecutionId()%></td>
    		<td><%=historyTask.getEndTime()%></td>
    		<td><%=historyTask.getAssignee()%></td>
    		<td><%=historyTask.getOutcome()%></td>
    		<td><%if(historyTask.getAssignee().equals(session.getAttribute("username"))){%>
    			<a href="fetch.jsp?id=<%=historyTask.getExecutionId()%>">fetch</a>
    		<%} %></td>
    	</tr>
    	 <% 
			}    
		%>
    </table>
  </body>
</html>
