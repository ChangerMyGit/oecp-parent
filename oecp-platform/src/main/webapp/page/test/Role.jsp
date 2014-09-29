<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=iso-8859-1"> 
<title>Tree</title>
<link rel="stylesheet" type="text/css" href="../../extjs/resources/css/ext-all.css" />
<script type="text/javascript" src="../../js/context-path.jsp"></script>
<script type="text/javascript" src="../../extjs/adapter/ext/ext-base.js"></script>
<script type="text/javascript" src="../../extjs/ext-all.js"></script>

<script type="text/javascript" src="../../extjs/ux/MultiSelect.js"></script>
<script type="text/javascript" src="../../extjs/ux/ItemSelector.js"></script>

<script type="text/javascript" src="../../ext-oecp/ui/ToftPanel.js"></script>
<script type="text/javascript" src="../../ext-oecp/ui/TreeGridPanel.js"></script>
<script type="text/javascript" src="../../js/component/Role/RoleView.js"></script>
<script type="text/javascript" src="../../js/component/Role/OECP_CheckboxTreeWindow.js"></script>
<script type="text/javascript" src="../../js/component/Role/OECP_RoleAppointWindow.js"></script>
<script type="text/javascript" src="../../js/component/OrgManage/OECP_FormWindow.js"></script>
<script type="text/javascript" src="../../js/component/OrgManage/OECP_FormPanel.js"></script>
<script type="text/javascript" src="../../js/component/function/Toast.js"></script>
	<script type="text/javascript">
	Ext.onReady(function() {
		Ext.QuickTips.init();
		var panel = new OECP.role.RoleView();
		panel.render(document.body);
	});
	</script>
</head>
<body>
	<div id="panel"></div>
</body>
</html>