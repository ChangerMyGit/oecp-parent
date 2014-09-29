<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%> 
<html>
	<head>
		<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
		<title>单据查看</title>
		<%
			String path = request.getContextPath();
			String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
		%>
		<link rel="stylesheet" type="text/css" href="<%=path%>/extjs/resources/css/ext-all.css" />
		<link rel="stylesheet" type="text/css" href="<%=path%>/extjs/ux/css/LockingGridView.css" />
		<link rel="stylesheet" type="text/css" href="<%=path%>/extjs/ux/css/GroupSummary.css" />
		<link rel="stylesheet" type="text/css" href="<%=path%>/ext-oecp/css/ext-oecp.css" />
		<script type="text/javascript" src="<%=path%>/js/context-path.jsp"></script>
		<script type="text/javascript" src="<%=path%>/extjs/adapter/ext/ext-base-debug.js"></script>
		<script type="text/javascript" src="<%=path%>/extjs/ext-all-debug.js"></script>
		<script type="text/javascript" src="<%=path%>/extjs/ext-lang-zh_CN.js"></script>
		<script type="text/javascript" src="<%=path%>/extjs/ux/ux-all-debug.js"></script>
		
		<script type="text/javascript" src="<%=path%>/ext-oecp/ui/Button.js"></script>
		<script type="text/javascript" src="<%=path%>/ext-oecp/ui/button/ButtonManager.js"></script>
		<script type="text/javascript" src="<%=path%>/ext-oecp/ui/QueryWindow.js"></script>
		<script type="text/javascript" src="<%=path%>/ext-oecp/ui/Adapter.js"></script>
		<script type="text/javascript" src="<%=path%>/ext-oecp/ui/datatimefield/DateTimeField.js"></script>
		<script type="text/javascript" src="<%=path%>/ext-oecp/ui/GridRefField.js"></script>
		<script type="text/javascript" src="<%=path%>/ext-oecp/ui/combo/BCCombo.js"></script>
		<script type="text/javascript" src="<%=path%>/ext-oecp/ui/combo/EnumsCombo.js"></script>
		<script type="text/javascript" src="<%=path%>/ext-oecp/core/ExtUtil.js"></script>
		<script type="text/javascript" src="<%=path%>/ext-oecp/core/Toast.js"></script>
		<script type="text/javascript" src="<%=path%>/ext-oecp/ui/GridPanel.js"></script>
		<script type="text/javascript" src="<%=path%>/ext-oecp/ui/grid/GridSummary.js"></script>

		<script type="text/javascript" src="<%=path%>/js/App.js"></script>
		<script type="text/javascript" src="<%=path%>/js/bc/Ref.js"></script>
		
		<script type="text/javascript" src="<%=path%>/ext-oecp/ui/combo/OECPComboRef.js"></script>
		<script type="text/javascript" src="<%=path%>/ext-oecp/ui/grid/OECPGrid.js"></script>
		<script type="text/javascript" src="<%=path%>/ext-oecp/ui/bill/BasicMasterTablePanel.js"></script>
		<script type="text/javascript" src="<%=path%>/ext-oecp/ui/grid/LockingGridView_fixed.js"></script>
		<script type="text/javascript" src="<%=path%>/ext-oecp/ui/CommonWindow.js"></script>
		<script type="text/javascript" src="<%=path%>/js/component/bpmperson/PersonalTaskAuditView.js"></script>
		<script type="text/javascript" src="<%=path%>/js/component/bpm/instance/ProcessInstanceHisView.js"></script>
		<script type="text/javascript" src="<%=path%>/js/bc/billFlowHistory/Drawing.js"></script>
		<script type="text/javascript" src="<%=path%>/js/bc/billFlowHistory/BillFlowHistoryWindow.js"></script>
		
		<script type="text/javascript" src="<%=path%>/js/demo/uiviewtest/BasicMasterTablePanelTestMain.js"></script>
		<script type="text/javascript" src="<%=path%>/js/demo/uiviewtest/UIViewLoader.js"></script>
		<script type="text/javascript">
			var me = this;
			Ext.onReady(function() {
				var billid = '<%=request.getParameter("billId")%>';
				var funcCode = '<%=request.getParameter("functionCode")%>';
				var panel = new OECP.ui.BasicCardBill({
					defaults:{frame:true},
					tbar : {},
					formView:'{result:{}}',
					viewEditable : false,
					details_cardstore_cfg : test.TestBillView.prototype.details_cardstore_cfg||{},
					submitUrl: test.TestBillView.prototype.submitUrl||'',
					queryBillUrl:test.TestBillView.prototype.queryBillUrl||'',
					bodyEntityName:test.TestBillView.prototype.bodyEntityName||''
				});
				// 获取数据，获得单据数据中的业务类型
				var billdata = {};
				me = panel;
				
				panel.doQuery({
					params:{id:billid,functionCode:funcCode},
					success : function(response, opts){
						var msg = Ext.util.JSON.decode(response.responseText);
						if (msg.success){
							billdata = msg.result;// 赋值
							// 获取UI视图，并加载
							var viewloader = new OECP.uiview.Loader({
								bizTypeId : billdata.bizType.id,
								functionCode : funcCode,
								flushFormView : function(viewconfig){
									panel.initView(viewconfig);
									// 将bill数据set到panel上
									panel.setValues(billdata);
								},
							});
							panel.render(document.body);
							panel.getTopToolbar().add(viewloader);
							viewloader.load();
						}else{
							Ext.Msg.show({title : "错误",msg : msg.msg,buttons : Ext.Msg.OK});
						}
					}
				});
			}); 
	</script>
	</head>
	<body>
	</body>
</html>