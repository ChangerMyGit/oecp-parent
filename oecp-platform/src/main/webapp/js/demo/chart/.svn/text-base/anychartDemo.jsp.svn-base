<%@ page language="java"  pageEncoding="UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<%
String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>
<script type="text/javascript">
var __ctxPath="<%=path%>";
var __fullPath="<%=basePath%>";
</script>
后台Action示例请看 oecp.demo.charttest.web.ChartDemoAction<br/>
本页面访问url：demo/chart/demo.do
<html>
	<head>
		<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
		<title>视图测试页</title>
		<link rel="stylesheet" type="text/css"
			href="${pageContext.request.contextPath}/extjs/resources/css/ext-all.css" />
		<script type="text/javascript" src="<%=path%>/ext-oecp/ui/chart/anychart/js/AnyChart.js"></script>
		<script type="text/javascript" src="<%=path%>/extjs/adapter/ext/ext-base.js"></script>
		<script type="text/javascript" src="<%=path%>/extjs/ext-all-debug.js"></script>
		<script type="text/javascript" src="<%=path%>/ext-oecp/ui/chart/Ext-AnyChart.js"></script>
		<script type="text/javascript">
//		JSP方式写法
			var chartData = <s:property value="chartJson" escapeHtml='false' />;
			var chart = new AnyChart('<%=path%>/ext-oecp/ui/chart/anychart/swf/AnyChart.swf','<%=path%>/ext-oecp/ui/chart/anychart/swf/Preloader.swf');
			chart.wMode = "transparent";
			chart.messages = {
	                preloaderInit: "预加载初始化中... ",
	                preloaderLoading: "加载中... ",
	                init: "初始化...",
	                loadingConfig: "加载配置中...",
	                loadingResources: "加载资源中...",
	                loadingTemplates: "加载模板中...",
	                noData: "没有数据...",
	                waitingForData: "等待数据中..."
	            };
			chart.width = 600;
			chart.height = 300;
			chart.setJSONData(chartData);
			chart.write();
		</script>
		<script type="text/javascript">
		function showLineChart(){
//		AJAX方式写法
			Ext.Ajax.request({
				url : __ctxPath + '/demo/chart/line.do',
				success : function(response, opts) {
					var obj = Ext.util.JSON.decode(response.responseText);
					chart.setJSONData(obj);
// 					chart.saveAsImage();
					var img = chart.getPNGImage();
					var png = chart.getPNG();
					c.setValues(obj);
				}
			});
		}
		</script>
		<script type="text/javascript">
//		Ext方式写法
			var c = new OECP.AnyChart({title:'图表',height: 300,width:500,chartData:chartData,renderTo:Ext.getBody()});
			var p = new Ext.Panel({title:'Ext Panel',height: 300,width:500,renderTo:Ext.getBody()
				,items:[{xtype:'oecpchart',height: 300,width:500,chartData:chartData}]
			});
			function toPanel(){
				chart.write(p.id);
			}
		</script>
	</head>
	<body>
	<input type="button" value="AJAX方式调用" onclick="showLineChart()"/>
	<input type="button" value="重置ExtPanle" onclick="toPanel()"/>
	</body>
</html>