<%@ page language="java" import="java.util.*,org.jbpm.api.*" pageEncoding="utf-8"%>
<%@page import="java.util.zip.ZipInputStream"%>
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
   		//请假例子
   		//repositoryService.createDeployment().addResourceFromClasspath("qingjia.jpdl.xml").deploy();
   		//请假带流程图例子
   		//ZipInputStream zis = new ZipInputStream(this.getClass().getResourceAsStream("/leave.zip"));
   		//repositoryService.createDeployment().addResourcesFromZipInputStream(zis).deploy();
   		//会签例子
   		repositoryService.createDeployment().addResourceFromClasspath("cn/oecp/test/jbpm/huiqian.jpdl.xml").deploy();
   		response.sendRedirect("index.jsp");
    %>
  </body>
</html>
