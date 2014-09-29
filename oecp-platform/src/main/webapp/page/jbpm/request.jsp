<%@ page language="java" import="java.util.*,org.jbpm.api.*,org.jbpm.api.task.*" pageEncoding="utf-8"%>
<%
String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
  <head>
    <base href="<%=basePath%>">
    
    <title>My JSP 'request.jsp' starting page</title>
    
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
  String taskId = request.getParameter("taskId");
  Task task = taskService.getTask(taskId);
   %>
    <form action="submit.jsp" method="post">
    	<input type="hidden" name="taskId" value="${param.taskId}"/>
    	<input type="hidden" name="executionId" value="${param.executionId}"/>
        username:<input type="text" name="owner" value="${sessionScope['username'] }"/><br/>
    	day:<input type="text" name="day" value="<%=taskService.getVariable(taskId,"day") %>"/><br/>
    	reason:<input type="text" name="reason" value="<%=taskService.getVariable(taskId,"reason")%>"/>
    	<input type="submit"/>
    </form>
  </body>
</html>
