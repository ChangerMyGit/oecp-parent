<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%> 
<%@ page import = "java.util.*,oecp.framework.web.WebConstant" %>
<html>
	<head>
		<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
		<title>单据查看</title>
		<%
			String path = request.getContextPath();
			String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
		%>
		<link rel="stylesheet" type="text/css" href="<%=path%>/extjs/resources/css/ext-all.css" />
		<script type="text/javascript" src="<%=path%>/js/context-path.jsp"></script>
		<script type="text/javascript" src="<%=path%>/extjs/adapter/ext/ext-base-debug.js"></script>
		<script type="text/javascript" src="<%=path%>/extjs/ext-all-debug.js"></script>
		<script type="text/javascript" src="<%=path%>/extjs/ux/ux-all-debug.js"></script>
		<script type="text/javascript" src="<%=path%>/extjs/ext-lang-zh_CN.js"></script>
		
		<script type="text/javascript" src="<%=path%>/ext-oecp/ui/Button.js"></script>
		<script type="text/javascript" src="<%=path%>/ext-oecp/ui/Adapter.js"></script>
		<script type="text/javascript" src="<%=path%>/ext-oecp/ui/button/ButtonManager.js"></script>
		<script type="text/javascript" src="<%=path%>/ext-oecp/ui/QueryWindow.js"></script>
		<script type="text/javascript" src="<%=path%>/ext-oecp/ui/Adapter.js"></script>
		<script type="text/javascript" src="<%=path%>/ext-oecp/ui/datatimefield/DateTimeField.js"></script>
		<script type="text/javascript" src="<%=path%>/ext-oecp/ui/GridRefField.js"></script>
		<script type="text/javascript" src="<%=path%>/ext-oecp/ui/QueryColumnModel.js"></script>
		<script type="text/javascript" src="<%=path%>/ext-oecp/ui/combo/BCCombo.js"></script>
		<script type="text/javascript" src="<%=path%>/ext-oecp/ui/combo/EnumsCombo.js"></script>
		<script type="text/javascript" src="<%=path%>/ext-oecp/core/ExtUtil.js"></script>
		<script type="text/javascript" src="<%=path%>/ext-oecp/core/Toast.js"></script>
		<script type="text/javascript" src="<%=path%>/ext-oecp/ui/GridPanel.js"></script>
		<script type="text/javascript" src="<%=path%>/ext-oecp/ui/bill/BasicMasterTablePanel.js"></script>
		<script type="text/javascript" src="<%=path%>/ext-oecp/ui/bill/UIViewLoader.js"></script>
		<script type="text/javascript" src="<%=path%>/ext-oecp/ui/MasterDetailPanel.js"></script>
		<script type="text/javascript" src="<%=path%>/ext-oecp/ui/MasterDetailEditPanel.js"></script>


		<script type="text/javascript" src="<%=path%>/ext-oecp/ui/combo/OECPComboRef.js"></script>
		<script type="text/javascript" src="<%=path%>/ext-oecp/ui/grid/OECPGrid.js"></script>
		<script type="text/javascript" src="<%=path%>/ext-oecp/ui/bill/BasicMasterTablePanel.js"></script>
		<script type="text/javascript" src="<%=path%>/ext-oecp/ui/grid/LockingGridView_fixed.js"></script>
		<script type="text/javascript" src="<%=path%>/ext-oecp/ui/CommonWindow.js"></script>
		<script type="text/javascript" src="<%=path%>/js/component/bpmperson/PersonalTaskAuditView.js"></script>
		<script type="text/javascript" src="<%=path%>/js/component/bpm/instance/ProcessInstanceHisView.js"></script>
		<script type="text/javascript" src="<%=path%>/js/bc/billFlowHistory/Drawing.js"></script>
		<script type="text/javascript" src="<%=path%>/js/bc/billFlowHistory/BillFlowHistoryWindow.js"></script>

		<script type="text/javascript" src="<%=path%>/js/App.js"></script>
		<script type="text/javascript" src="<%=path%>/js/bc/Ref.js"></script>
		<script type="text/javascript" src="<%=path%>/js/demo/uiviewtest/BasicMasterTablePanelTestMain.js"></script>
		<script type="text/javascript" src="<%=path%>/js/demo/uiviewtest/uiviewtestBaseView.js"></script>
		<script type="text/javascript" src="<%=path%>/js/demo/uiviewtest/uiviewtestPanel.js"></script>
		<script type="text/javascript" src="<%=path%>/js/demo/uiviewtest/uiviewtestView.js"></script>
		<script type="text/javascript">
			<%
			  String functionCode = request.getParameter("functionCode");
			  List list = (List)((Map)session.getAttribute(WebConstant.OECP_UIELEMENT_PERMISSION)).get(functionCode);
			%>
			//隐藏的按钮数组
			var btnPerArray = new Array();
			<% if(list!=null)for(int i=0;i< list.size(); i++) {%>   
				btnPerArray[<%=i%>] = "<%=list.get(i)%>";     
			<%}%>
			Ext.onReady(function() {
				var panel = new OECPBC.demo.uiviewtest.uiviewtestView({
					height : document.body.clientHeight,
					functionCode : '<%=functionCode%>',
					btnPermission : btnPerArray
				});
				panel.render(document.body);  
			});
	</script>
	</head>
	<body>
	</body>
</html>