<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
	<head>
		<title>用户管理</title>

		<script type="text/javascript" src="../../context-path.jsp"></script>
		<link rel="stylesheet" type="text/css"
			href="../../../extjs/resources/css/ext-all.css" />
		<link rel="stylesheet" type="text/css"
			href="../../../css/portal.css" />
		<script type="text/javascript"
			src="../../../extjs/adapter/ext/ext-base.js"></script>
		<script type="text/javascript" src="../../../extjs/ext-all-debug.js"></script>
		<script type="text/javascript" src="../../../extjs/ux/MultiSelect.js"></script>
		<script type="text/javascript" src="../../../extjs/ux/ItemSelector.js"></script>
		<script type="text/javascript" src="../../core/AppUtil.js"></script>
		<script type="text/javascript" src="../../../ext-oecp/core/Toast.js"></script>
		<script type="text/javascript" src="../../../ext-oecp/ui/Adapter.js"></script>
		<script type="text/javascript" src="../../../ext-oecp/ui/TreeComboBox.js"></script>
		<script type="text/javascript" src="../../../ext-oecp/ui/FormTableLayout.js"></script>
		<script type="text/javascript" src="../core/OrgComboBox.js"></script>
		<script type="text/javascript" src="../../App.js"></script>
		<script type="text/javascript" src="UserGridView.js"></script>
		<script type="text/javascript" src="UserWin.js"></script>
	</head>

	<body>
	</body>
</html>
<script type="text/javascript">
	Ext.onReady(function() {
		Ext.QuickTips.init();
		App.init();
		var panel = new OECP.user.UserGridView();
		panel.render(document.body);
	});
	</script>