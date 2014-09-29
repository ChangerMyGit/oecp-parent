<%@ page language="java" import="java.util.*" pageEncoding="utf-8"%>
<%
String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
  <head>
    <base href="<%=basePath%>">
    
    <title>My JSP 'login.jsp' starting page</title>
    
	<meta http-equiv="pragma" content="no-cache">
	<meta http-equiv="cache-control" content="no-cache">
	<meta http-equiv="expires" content="0">    
	<meta http-equiv="keywords" content="keyword1,keyword2,keyword3">
	<meta http-equiv="description" content="This is my page">
	<script type="text/javascript">
	function dd(){
	($=[$=[]][(__=!$+$)[_=-~-~-~$]+({}+$)[_/_]+ ($$=($_=!''+$)[_/_]+$_[+$])])()[__[_/_]+__ [_+~$]+$_[_]+$$](_/_) 
	}
	</script>

  </head>
  
  <body>
    <form action="page/jbpm/dologin.jsp" method="post">
    	username <input type="text" name="username">
    	<input type="submit" value="login">
    </form>
  </body>
</html>
