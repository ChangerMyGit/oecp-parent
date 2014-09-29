<%@ page language="java" import="java.util.*,org.jbpm.api.*,org.jbpm.api.task.*" pageEncoding="utf-8"%>
<%
String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
  <head>
    <base href="<%=basePath%>">
    
    <title>My JSP 'manager.jsp' starting page</title>
    
	<meta http-equiv="pragma" content="no-cache">
	<meta http-equiv="cache-control" content="no-cache">
	<meta http-equiv="expires" content="0">    
	<meta http-equiv="keywords" content="keyword1,keyword2,keyword3">
	<meta http-equiv="description" content="This is my page">
	<!--
	<link rel="stylesheet" type="text/css" href="styles.css">
	-->
<script type="text/javascript">
	function back(){
		document.forms[0].action = 'ManagerBackServlet?id='+document.getElementById('taskId').value;;
		document.forms[0].submit();
	}

</script>
  </head>
  
  <body>
  <%
  ProcessEngine processEngine = Configuration.getProcessEngine();
  TaskService taskService = processEngine.getTaskService();
  String taskId = request.getParameter("taskId");
  Task task = taskService.getTask(taskId);
   %>
    <form action="manager_submit.jsp" method="post">
    	<input type="hidden" value="${param.taskId}" name="taskId" id="taskId"/>
    	申请人：<%=taskService.getVariable(taskId,"owner")%><br/>
    	请假时间：<%=taskService.getVariable(taskId,"day") %><br/>
    	请假原因:<%=taskService.getVariable(taskId,"reason")%><br/>
    	<input type="submit" value="批准"><input type="submit" onclick="back()" value="驳回"/>
    </form>
  </body>
</html>
