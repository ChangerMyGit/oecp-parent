<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
<title>打印模板设计</title>
<%
	String path = request.getContextPath();
	String basePath = request.getScheme() + "://"
			+ request.getServerName() + ":" + request.getServerPort()
			+ path + "/";
%>
<link rel="stylesheet" type="text/css"	href="<%=path%>/extjs/resources/css/ext-all.css" />
<link rel="stylesheet" type="text/css"	href="<%=path%>/extjs/ux/css/ux-all.css" /> 
<link rel="stylesheet" type="text/css"	href="<%=path%>/extjs/ux/css/GroupSummary.css" />
<link rel="stylesheet" type="text/css"	href="<%=path%>/extjs/ux/css/LockingGridView.css" />
<link rel="stylesheet" type="text/css"	href="<%=path%>/ext-oecp/css/ext-oecp.css" />
<SCRIPT src="<%=path%>/extjs/adapter/ext/ext-base.js"></SCRIPT>
<SCRIPT src="<%=path%>/extjs/ext-all-debug.js"></SCRIPT>
<SCRIPT src="<%=path%>/extjs/ux/ux-all-debug.js"></SCRIPT>
<script src="<%=path%>/extjs/ext-lang-zh_CN.js"></script>
<script src="<%=path%>/ext-oecp/ui/Adapter.js"></script>
<script src="<%=path%>/ext-oecp/ui/TreeComboBox.js"></script>
<script src="<%=path%>/ext-oecp/core/Toast.js"></script>
<script src="<%=path%>/js/core/AppUtil.js"></script>
<script src="<%=path%>/js/core/ScriptMgr.js"></script>
<script src="<%=path%>/js/context-path.jsp"></script>
<script src="<%=path%>/js/App.js"></script>
<script src="<%=path%>/js/component/core/FunComboBox.js"></script>
<style type="text/css">
body {
	font-family: Verdana; 
	font-size: 14px;
	margin: 0;
}

#container {
	margin: 0 auto;
	width: 100%;
}

#header {
	height: 30px;
	margin-bottom: 0px;
}

#mainContent {
	height: 500px;
	margin-bottom: 0px;
}

#sidebar {
	float: left;
	width: 200px;
	height: 500px;
}

#content {
	margin-left: 205px !important;
	margin-left: 202px;
	height: 500px;
	background: #
}
</style>
</head>
<body>
	<div id="container">
		<div id="header"></div>
		<div id="mainContent">
			<div id="sidebar"></div>
			<div id="content">
				<span> <object id="LODOP2"
						classid="clsid:2105C259-1E0C-4534-8141-A753534CB4CA" width=810
						height=600>
						<param name="Caption" value="内嵌显示区域">
						<param name="Border" value="1">
						<param name="Color" value="#C0C0C0">
						<embed id="LODOP_EM2" TYPE="application/x-print-lodop" width=810
							height=600 PLUGINSPAGE="install_lodop.exe">
					</object> </span>
			</div>
		</div>
<SCRIPT src="LodopFuncs.js"></SCRIPT>
<SCRIPT src="main.js"></SCRIPT>
</body>
</html>