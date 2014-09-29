<%@ page language="java" import="java.util.*,org.jbpm.api.*" pageEncoding="utf-8"%>
<%
String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
  <head>
    <base href="<%=basePath%>">
    
    <title>My JSP 'start.jsp' starting page</title>
    
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
		RepositoryService repositoryService = processEngine.getRepositoryService();
		ExecutionService executionService = processEngine.getExecutionService();
		Map map = new HashMap();
		map.put("owner",session.getAttribute("username"));
		//会签例子属性
		List<String> users = new ArrayList<String>();
		users.add("zhang");
		users.add("wang");
		users.add("li");
		map.put("users",users);
		map.put("num",users.size());
		ProcessInstance processInstance = executionService.startProcessInstanceById(request.getParameter("id"),map);
        HistoryService historyService = processEngine.getHistoryService();
        historyService.createHistoryProcessInstanceQuery().processInstanceId(processInstance.getId()).uniqueResult();
        response.sendRedirect("index.jsp");
     %>
  </body>
</html>
