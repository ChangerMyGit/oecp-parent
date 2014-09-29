<%@ page language="java" pageEncoding="utf-8"%>
<%@ include file="/includefiles.jsp" %>
<html>
<head>
<title>报表管理</title>
<%
	String path = request.getContextPath();
	String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>
<link rel="stylesheet" type="text/css" href="<%=path%>/extjs/ux/css/GroupSummary.css" />
<link rel="stylesheet" type="text/css" href="<%=path%>/extjs/ux/css/LockingGridView.css" />
<script type="text/javascript" src="<%=path%>/ext-oecp/ui/combo/EnumsCombo.js"></script>
<script type="text/javascript" src="<%=path%>/js/component/uiview/editor/js/FunViewMngEditPanel.component.js"></script>
<script type="text/javascript" src="<%=path%>/js/component/uiview/editor/js/FunViewMngEditPanel.js"></script>
<script type="text/javascript" src="ReportViewDesignPanel.js"></script>
<script type="text/javascript" src="ReportList.js"></script>
<SCRIPT src="<%=path%>/ext-oecp/ui/grid/OECPGrid.js"></SCRIPT>
<SCRIPT src="<%=path%>/ext-oecp/ui/grid/GridSummary.js"></SCRIPT>
<SCRIPT src="<%=path%>/ext-oecp/ui/grid/LockingGridView_fixed.js"></SCRIPT>
<script type="text/javascript" src="<%=path%>/ext-oecp/ui/chart/anychart/js/AnyChart.js"></script>
<script type="text/javascript" src="<%=path%>/ext-oecp/ui/chart/Ext-AnyChart.js"></script>
<script type="text/javascript" src="main.js"></script>
</head>
<body>
</body>

</html>