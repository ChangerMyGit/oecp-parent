<%@ page language="java" import="java.util.*,org.jbpm.api.*,org.jbpm.api.task.*" pageEncoding="utf-8"%>
<%@page import="org.jbpm.pvm.internal.model.ExecutionImpl"%>
<%@page import="org.jbpm.pvm.internal.task.TaskImpl"%>
<%
String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
  <head>
    <base href="<%=basePath%>">
    
    <title>My JSP 'manager_submit.jsp' starting page</title>
    
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
    <%
    	response.setCharacterEncoding("UTF-8");
    	request.setCharacterEncoding("UTF-8");
    	ProcessEngine processEngine = Configuration.getProcessEngine();
    	TaskService taskService = processEngine.getTaskService();
    	String taskId = request.getParameter("taskId");
    	Task task = taskService.getTask(taskId);
		//防止用户打开任务办理页面，但此时任务已通过其他方式完成
		if(task != null){
			//请假例子
			//taskService.completeTask(taskId,"批准");
			//会签例子
			taskService.completeTask(taskId);
		}else{
			out.print("error");
		}
		response.sendRedirect("index.jsp");
     %>
  </body>
</html>
