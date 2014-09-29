<%@ page language="java" import="java.util.*,org.jbpm.api.*,org.jbpm.api.task.*" pageEncoding="UTF-8"%>
<%@page import="org.jbpm.api.model.ActivityCoordinates"%>
<%
String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
  <head>
    <base href="<%=basePath%>">
    
    <title>My JSP 'view.jsp' starting page</title>
    
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
    	String id = request.getParameter("id");
    	ProcessEngine processEngine = Configuration.getProcessEngine();
    	RepositoryService repositoryService = processEngine.getRepositoryService();
    	ExecutionService executionService = processEngine.getExecutionService();
    	ProcessInstance processInstance = executionService.findProcessInstanceById(id);
    	Set<String> activityNames = processInstance.findActiveActivityNames();
    	ActivityCoordinates activityCoordinates = repositoryService.getActivityCoordinates(processInstance.getProcessDefinitionId(),activityNames.iterator().next()); 
     %>
     <img src="pic.jsp?id=<%=id%>" style="position:absolute;left:0px;top:0px">
     <div style="position:absolute;border:1px solid red;left:<%=activityCoordinates.getX()%>px;top:<%=activityCoordinates.getY()%>px;width:<%=activityCoordinates.getWidth()%>px;height:<%=activityCoordinates.getHeight()%>px"></div>
  </body>
</html>
