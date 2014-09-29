<%@ page language="java" import="java.util.*,org.jbpm.api.*,org.jbpm.api.task.*" pageEncoding="utf-8"%>
<%
String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
  <head>
    <base href="<%=basePath%>">
    
    <title>My JSP 'shenpi1.jsp' starting page</title>
    
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
  ProcessEngine processEngine = Configuration.getProcessEngine();
  TaskService taskService = processEngine.getTaskService();
  String taskId = request.getParameter("id");
  Task task = taskService.getTask(taskId);
   %>
    <form action="boss_submit.jsp" method="post">
    	<input type="hidden" value="${param.id}" name="taskId"/>
    	申请人：<%=taskService.getVariable(taskId,"owner")%><br/>
    	请假时间：<%=taskService.getVariable(taskId,"day") %><br/>
    	请假原因:<%=taskService.getVariable(taskId,"reason")%><br/>
    	<input type="submit" value="批准">
    </form>
  </body>
</html>
