<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
<title>功能视图设计</title>
	<%
		String path = request.getContextPath();
		String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
	%>
	<link rel="stylesheet" type="text/css" href="<%=path%>/extjs/resources/css/ext-all.css" />
	<link rel="stylesheet" type="text/css" href="<%=path%>/extjs/ux/css/ux-all.css" />
	<link rel="stylesheet" type="text/css" href="<%=path%>/extjs/ux/css/GroupSummary.css" />
	<link rel="stylesheet" type="text/css" href="<%=path%>/extjs/ux/css/LockingGridView.css" />
	<link rel="stylesheet" type="text/css" href="<%=path%>/ext-oecp/css/ext-oecp.css" />
	<link rel="stylesheet" type="text/css" href="<%=path%>/js/component/uiview/editor/css/main.css" />
	<SCRIPT src="<%=path%>/extjs/adapter/ext/ext-base.js"></SCRIPT>
	<SCRIPT src="<%=path%>/extjs/ext-all-debug.js"></SCRIPT>
	<SCRIPT src="<%=path%>/extjs/ux/ux-all-debug.js"></SCRIPT>
	<script src="<%=path%>/extjs/ext-lang-zh_CN.js"></script>
	<script src="<%=path%>/js/core/AppUtil.js"></script>
	<script src="<%=path%>/js/core/ScriptMgr.js"></script>
	<script src="<%=path%>/js/context-path.jsp"></script>
	<script src="<%=path%>/js/App.js"></script>
	<SCRIPT src="<%=path%>/ext-oecp/ui/Button.js"></SCRIPT>
	<SCRIPT src="<%=path%>/ext-oecp/ui/grid/OECPGrid.js"></SCRIPT>
	<SCRIPT src="<%=path%>/ext-oecp/ui/combo/EnumsCombo.js"></SCRIPT>
	<SCRIPT src="<%=path%>/ext-oecp/ui/grid/GridSummary.js"></SCRIPT>
	<SCRIPT src="<%=path%>/ext-oecp/ui/grid/LockingGridView_fixed.js"></SCRIPT>
	<SCRIPT src="<%=path%>/ext-oecp/ui/button/ButtonManager.js"></SCRIPT>
	<SCRIPT src="<%=path%>/ext-oecp/ui/AccordionTreePanel.js"></SCRIPT>
	<SCRIPT src="<%=path%>/ext-oecp/ui/combo/OECPComboRef.js"></SCRIPT>
	<script src="<%=path%>/ext-oecp/core/Toast.js"></script>
	<SCRIPT src="<%=path%>/js/component/uiview/editor/js/FunViewMngEditPanel.component.js"></SCRIPT>
	<SCRIPT src="<%=path%>/js/component/uiview/editor/js/FunViewMngEditPanel.js"></SCRIPT>
	<SCRIPT src="<%=path%>/js/component/uiview/editor/js/FunViewMngPanel.js"></SCRIPT>
	<SCRIPT src="<%=path%>/js/component/uiview/editor/js/FunViewMngListPanel.js"></SCRIPT>
	<SCRIPT type="text/javascript">
	Ext.onReady(function(){
		Ext.QuickTips.init();
		Ext.Ajax.request({
			url : __ctxPath + "/portal/getOnlineUser.do?random="
					+ Math.random(),
			method : "Get",
			success : function(d, g) {
				var f = Ext.util.JSON.decode(d.responseText);
				var c = f.result;
				curUserInfo = new UserInfo(c);
				var p = new OECP.ui.view.FunViewMngPanel();
				var v = new Ext.Viewport({layout:'fit',items:[p]});
				v.render(document.body);
				p.showListView(p);
			}
		});
	});
	</SCRIPT>
</head>
<body>
</body>
</html>