<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%> 
<html>
	<head>
		<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
		<title>Tree</title>
		<%
			String path = request.getContextPath();
		%>
		<script type="text/javascript">
			var path = "<%=path%>";
		</script>
		<link rel="stylesheet" type="text/css"
			href="/<%=path%>/extjs/resources/css/ext-all.css" />
		<link rel="stylesheet" type="text/css"
			href="/<%=path%>/extjs/ux/css/ux-all.css" />
		<script type="text/javascript"
			src="/<%=path%>/extjs/adapter/ext/ext-base.js"></script>
		<script type="text/javascript"
			src="/<%=path%>/extjs/ext-all-debug-w-comments.js"></script>
		<script type="text/javascript" src="/<%=path%>/extjs/src/data/Store.js"></script>
		<script type="text/javascript"
			src="/<%=path%>/extjs/src/widgets/form/Combo.js"></script>
		<script type="text/javascript"
			src="/<%=path%>/extjs/src/util/MixedCollection.js"></script>
		<script type="text/javascript"
			src="/<%=path%>/ext-oecp/ui/RefGridEditor.js"></script>
		<script type="text/javascript" src="/<%=path%>/extjs/src/widgets/Editor.js"></script>
		<script type="text/javascript"
			src="/<%=path%>/extjs/src/widgets/form/Combo.js"></script>
		<script type="text/javascript"
			src="/<%=path%>/extjs/src/widgets/form/DateField.js"></script>
		<script type="text/javascript"
			src="/<%=path%>/extjs/src/widgets/form/BasicForm.js"></script>
		<script type="text/javascript"
			src="/<%=path%>/extjs/src/widgets/grid/GridEditor.js"></script>
		<script type="text/javascript"
			src="/<%=path%>/extjs/src/data/JsonReader.js"></script>


		<script type="text/javascript"
			src="/<%=path%>/ext-oecp/ui/MasterDetailPanel.js"></script>
		<script type="text/javascript" src="/<%=path%>/ext-oecp/ui/Button.js"></script>
		<script type="text/javascript"
			src="/<%=path%>/ext-oecp/ui/button/ButtonManager.js"></script>
		<script type="text/javascript" src="/<%=path%>/ext-oecp/ui/RefField.js"></script>
		<script type="text/javascript" src="/<%=path%>/ext-oecp/ui/QueryWindow.js"></script>
		
		<script type="text/javascript" src="/<%=path%>/ext-oecp/ui/Adapter.js"></script>
		<script type="text/javascript" src="/<%=path%>/ext-oecp/ui/GridRefField.js"></script>
		
		<script type="text/javascript"
			src="/<%=path%>/ext-oecp/ui/MasterDetailEditPanel.js"></script>
		<script type="text/javascript"
			src="/<%=path%>/ext-oecp/ui/QueryColumnModel.js"></script>
		<script type="text/javascript"
			src="/<%=path%>/js/component/function/Toast.js"></script>
		<script type="text/javascript"
			src="/<%=path%>/extjs/src/widgets/form/DateField.js"></script>
		<script type="text/javascript" src="RefTestView.js"></script>
		<script type="text/javascript" src="/<%=path%>/extjs/ux/ux-all-debug.js"></script>
		   <style type=text/css> 
        /* style rows on mouseover */
        .x-grid3-row-over .x-grid3-cell-inner {
            font-weight: bold;
        }
 
        /* style for the "buy" ActionColumn icon */
        .x-action-col-cell img.buy-col {
            height: 16px;
            width: 16px;
            background-image: url(/oecp-platform/extjs/resources/images/yourtheme/dd/drop-add.gif);
        }
 
        /* style for the "alert" ActionColumn icon */
        .x-action-col-cell img.alert-col {
            height: 16px;
            width: 16px;
            background-image: url(/oecp-platform/extjs/resources/images/yourtheme/dd/drop-yes.gif);
        }
 
    </style> 
	</head>
	<body>
		<div id="panel"></div>
		<div id="ref1"></div>
		<div id="ref2"></div>
		<div id="btn1"></div>
		<div id="btn2"></div>
		<br/>
		<br/>
		<br/>
		<br/>
		<br/>
		<br/>
		<br/>
		<div id="grid-example"></div>
		<br/>
		<br/>
		<br/>
		<br/>
		<br/>
		<br/>
		<div id="panel2"></div>
	</body>
</html>