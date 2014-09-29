<%@ page language="java" import="java.util.*,org.jbpm.api.*,org.jbpm.api.task.*" pageEncoding="UTF-8"%>
<%@page import="java.io.InputStream"%>
<%
String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
  <head>
    <base href="<%=basePath%>">
    
    <title>My JSP 'pic.jsp' starting page</title>
    
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
    	String processDefinitionId = processInstance.getProcessDefinitionId();
    	ProcessDefinition processDefinition = repositoryService.createProcessDefinitionQuery().processDefinitionId(processDefinitionId).uniqueResult();
    	InputStream is = repositoryService.getResourceAsStream(processDefinition.getDeploymentId(),"leave.png"); 
    	byte[] b = new byte[1024];
    	int len = -1;
    	while( (len = is.read(b,0,1024)) != -1){
    		response.getOutputStream().write(b,0,len);
    	}
     %>
  </body>
</html>
