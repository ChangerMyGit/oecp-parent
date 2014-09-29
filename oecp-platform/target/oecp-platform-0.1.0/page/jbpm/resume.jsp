<%@ page language="java" import="java.util.*,org.jbpm.api.*" pageEncoding="utf-8"%>
<%@page import="java.util.zip.ZipInputStream"%>
<%@page import="org.jbpm.pvm.internal.model.ExecutionImpl"%>
<%@page import="org.jbpm.pvm.internal.model.op.AtomicOperation"%>
<%
String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
  <head>
    <base href="<%=basePath%>">
    
    <title>My JSP 'deploy.jsp' starting page</title>
    
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
   		RepositoryService repositoryService = processEngine.getRepositoryService();
   		ExecutionService executionService = processEngine.getExecutionService();
   		String id = request.getParameter("id");
   		repositoryService.resumeDeployment(id);
   		response.sendRedirect("index.jsp");
    %>
  </body>
</html>
