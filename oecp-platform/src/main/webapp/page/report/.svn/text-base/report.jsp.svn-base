<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%> 
<%@ page import = "java.util.*,oecp.framework.web.WebConstant" %>
<%@ include file="/includefiles.jsp" %>
<html>
	<head>
		<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
		<title>单据查看</title>
		<%
			String path = request.getContextPath();
			String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
		%>
		<link rel="stylesheet" type="text/css" href="<%=path%>/extjs/ux/css/GroupSummary.css" />
		<link rel="stylesheet" type="text/css" href="<%=path%>/extjs/ux/css/LockingGridView.css" />
		
		<style type= "text/css" > 
			.x-selectable, .x-selectable * { 
				-moz-user-select: text! important ; 
				-khtml-user-select: text! important ; 
			} 
		</style>
		<script type="text/javascript" src="<%=path%>/ext-oecp/ui/combo/EnumsCombo.js"></script>
		<script type="text/javascript" src="<%=path%>/ext-oecp/ui/bill/PrintTempletManage.js"></script>
		<script type="text/javascript" src="<%=path%>/js/component/uiview/editor/js/FunViewMngEditPanel.component.js"></script>
		<script type="text/javascript" src="<%=path%>/js/component/uiview/editor/js/FunViewMngEditPanel.js"></script>
		<SCRIPT src="<%=path%>/ext-oecp/ui/grid/OECPGrid.js"></SCRIPT>
		<SCRIPT src="<%=path%>/ext-oecp/ui/grid/GridSummary.js"></SCRIPT>
		<SCRIPT src="<%=path%>/ext-oecp/ui/grid/LockingGridView_fixed.js"></SCRIPT>
		<script type="text/javascript" src="<%=path%>/ext-oecp/ui/chart/anychart/js/AnyChart.js"></script>
		<script type="text/javascript" src="<%=path%>/ext-oecp/ui/chart/Ext-AnyChart.js"></script>
		<script type="text/javascript" src="<%=path%>/extjs/src/ext-core/src/util/JSON.js"></script>
		<script type="text/javascript" src="<%=path%>/js/component/printTemplate/LodopFuncs.js"></script>
		<script type="text/javascript" src="report.js"></script>
		<script type="text/javascript">
			<%
			  String reportCode = request.getParameter("reportCode");
			%>
			var reportCode='<%=reportCode%>';
			Ext.onReady(function() {
				var panel=new OECP.report.ReportView({reportCode:reportCode});
				panel.render('reportarea');
				panel.doLayout();
			});
			
			if (!Ext.grid.GridView.prototype.templates) { 
			Ext.grid.GridView.prototype.templates = {}; 
			} 
			Ext.grid.GridView.prototype.templates.cell = new Ext.Template( 
			'<td class="x-grid3-col x-grid3-cell x-grid3-td-{id} x-selectable {css}" style="{style}" tabIndex="0" {cellAttr}>' , 
			'<div class="x-grid3-cell-inner x-grid3-col-{id}" {attr}>{value}</div>' , 
			'</td>' 
			);
	</script>
	</head>
	<body>
	<div id='reportarea'>
	</div>
	<div height='0px' width='0px'>
	<object id="LODOP_OB" classid="clsid:2105C259-1E0C-4534-8141-A753534CB4CA" width=0 height=0> 
		<embed id="LODOP_EM" type="application/x-print-lodop" width=0 height=0 pluginspage="install_lodop.exe"></embed>
	</object> 
	</div>
	</body>
</html>