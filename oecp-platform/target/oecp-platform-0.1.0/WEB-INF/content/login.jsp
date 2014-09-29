<%@ page language="java" pageEncoding="UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags" %>
<%
String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
	<head>
	<base href="<%=basePath%>">
		<title>登录${appConfig.system_name_default}</title>
		<link rel="stylesheet" type="text/css"
			href="extjs/resources/css/ext-all.css" />
		<link rel="stylesheet" type="text/css" href="css/login.css" />

		<script type="text/javascript">
			var __ctxPath="<%=path%>";
			var __loginImage=__ctxPath+"/images/logo.png";
		</script>

		<script type="text/javascript" src="extjs/adapter/ext/ext-base.js"></script>
		<script type="text/javascript" src="extjs/ext-all.js"></script>
		<script type="text/javascript" src="extjs/ext-lang-zh_CN.js"></script>
		<script type="text/javascript" src="js/App.js"></script>
		<script type="text/javascript" src="js/core/md5.js"></script>
		<script type="text/javascript" src="js/LoginWin.js"></script>
		<script type="text/javascript">
	 		Ext.onReady(function(){
		 		Ext.QuickTips.init(); 
		 		new App.LoginWin().show();
			});	
		</script>
	</head>

	<body>
		<div style="text-align: center;">
			<div id="loginArea">
			
			</div>
		</div>
	</body>
</html>
