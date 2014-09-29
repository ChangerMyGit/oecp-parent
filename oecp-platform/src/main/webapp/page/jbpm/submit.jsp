<%@ page language="java" import="java.util.*,org.jbpm.api.*,org.jbpm.api.task.*" pageEncoding="utf-8"%>
<%
String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
  <head>
    <base href="<%=basePath%>">
    
    <title>My JSP 'submit.jsp' starting page</title>
    
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
    	String taskId = request.getParameter("taskId");
		TaskService taskService = processEngine.getTaskService();
		int day = Integer.parseInt(request.getParameter("day").toString());
		String reason = request.getParameter("reason").toString();
		Map map = new HashMap();
		map.put("day",day);
		map.put("reason",reason);
		String executionId = request.getParameter("executionId");
		ExecutionService executionService = processEngine.getExecutionService();
		executionService.createVariable(executionId,"id",1,true);
		taskService.completeTask(taskId,"领导审批",map);
		response.sendRedirect("index.jsp");
     %>
  </body>
</html>
