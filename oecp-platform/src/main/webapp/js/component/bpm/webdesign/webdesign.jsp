
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@ page contentType="text/html;charset=utf-8" %>
<html>
<head>
	<%
		String path = request.getContextPath();
		String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
	%>
	<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
	<link type="text/css"
		href="lib/jquery-ui-1.8.4.custom/css/smoothness/jquery-ui-1.8.4.custom.css"
		rel="stylesheet" />
	<link rel="stylesheet" type="text/css" href="../../../../extjs/resources/css/ext-all.css" />
	<script type="text/javascript" src="../../../../js/context-path.jsp"></script>
	<script type="text/javascript" src="../../../../extjs/adapter/ext/ext-base-debug.js"></script>
	<script type="text/javascript" src="../../../../extjs/ext-all-debug.js"></script>
	<script type="text/javascript" src="../../../../extjs/ext-lang-zh_CN.js"></script>
	<script type="text/javascript" src="../../../../ext-oecp/core/Toast.js"></script>
	<script type="text/javascript" src="lib/raphael-min.js"></script>
	<script type="text/javascript"
		src="lib/jquery-ui-1.8.4.custom/js/jquery-1.4.2.min.js"></script>
	<script type="text/javascript"
		src="lib/jquery-ui-1.8.4.custom/js/jquery.imgareaselect-0.6.2.js"></script>	
	<script type="text/javascript"
		src="lib/jquery-ui-1.8.4.custom/js/jquery-ui-1.8.4.custom.min.js"></script>
	<script type="text/javascript" src="myflow.js"></script>
	<script type="text/javascript" src="myflow.jpdl4.js"></script>
	<script type="text/javascript" src="myflow.editors.js"></script>
	<script type="text/javascript">
	
	String.prototype.replaceAll  = function(s1,s2){    
		return this.replace(new RegExp(s1,"gm"),s2);    
	}
	var webPictureString = $(this.parent.document).find('.webPictureJson').attr('jsonValue');
	//发布
	$(function() {
		$('#myflow')
				.myflow(
						{
							basePath : "",
							restore : eval("("+webPictureString+")"),
							tools : {
								save : {
									onclick : function(data) {
										data = data.replaceAll(' ','');
										//发布之前的校验
										if(data.indexOf('start')==-1){
											Ext.Msg.alert("校验没通过", "必须有一个开始节点");
											return;
										}
										if(data.indexOf('end')==-1){
											Ext.Msg.alert("校验没通过", "至少有一个结束节点");
											return;
										}
										if(data.indexOf('task')==-1){
											Ext.Msg.alert("校验没通过", "至少有一个任务节点");
											return;
										}
										if(data.indexOf('新建流程')!=-1){
											Ext.Msg.alert("校验没通过", "请重新填写流程名称");
											return;
										}
										   // 显示进度条   
								        Ext.MessageBox.show({   
						                    msg : '发布中，请稍后...',   
						                    width : 300,   
						                    wait : true,   
						                    progress : true,   
						                    closable : true,   
						                    waitConfig : {   
						                        interval : 200  
						                    },   
						                    icon : Ext.Msg.INFO   
						                }); 
										//提交
										Ext.Ajax.request({
											url:__ctxPath + "/bpm/def/designProcessDefinition.do",
											params:{
												data:data,
												functionId:'<%=request.getParameter("functionId")%>'
											},
											success : function() {
												Ext.ux.Toast.msg('信息',	'发布成功！');
												//setTimeout("window.location.reload()",1000); 
												setTimeout("parent.Ext.getCmp('win_webdesign').close()",1000); 
											},
											failure : function(flag) {
												Ext.Msg.alert('提示',flag.responseText,function(){});
											}
										});
										
									}
								}
							}
						});

	});
	
	
	</script>
<style type="text/css">
body {
	margin: 0;
	pading: 0;
	text-align: left;
	font-family: Arial, sans-serif, Helvetica, Tahoma;
	font-size: 12px;
	line-height: 1.5;
	color: black;
	background-image: url(img/tt.png);
}

.node {
	width: 70px;
	text-align: center;
	vertical-align: middle;
	border: 1px solid #fff;
}

.mover {
	border: 1px solid #ddd;
	background-color: #ddd;
}

.selected {
	background-color: #ddd;
}

.state {
	
}

#myflow_props table {
	
}

#myflow_props th {
	letter-spacing: 2px;
	text-align: left;
	padding: 6px;
	background: #ddd;
}

#myflow_props td {
	background: #fff;
	padding: 6px;
}

#pointer {
	background-repeat: no-repeat;
	background-position: center;
}

#path {
	background-repeat: no-repeat;
	background-position: center;
}

#task {
	background-repeat: no-repeat;
	background-position: center;
}

#state {
	background-repeat: no-repeat;
	background-position: center;
}
</style>
</head>
<body>
<div id="myflow_tools"
	style="position: absolute; top: 10; left: 10; background-color: #fff; width: 80px; height: 240px; cursor: default; padding: 3px;"
	class="ui-widget-content">
<div id="myflow_tools_handle" style="text-align: center;"
	class="ui-widget-header">工具集</div>


<div class="node" id="myflow_save"><img src="img/save.gif" />&nbsp;&nbsp;发布</div>
<div>
<hr />
</div>
<div class="node selectable" id="pointer"><img
	src="img/select16.gif" />&nbsp;&nbsp;选择</div>
<div class="node selectable" id="path"><img
	src="img/16/flow_sequence.png" />&nbsp;&nbsp;转换</div>
<div>
<hr />
</div>
<div class="node state" id="start" type="start"><img
	src="img/16/start_event_empty.png" />&nbsp;&nbsp;开始</div>
<%-- <div class="node state" id="state" type="state"><img
	src="img/16/task_empty.png" />&nbsp;&nbsp;状态</div>
--%>	
<div class="node state" id="task" type="task"><img
	src="img/16/task_empty.png" />&nbsp;&nbsp;任务</div>
<div class="node state" id="decision" type="decision"><img
	src="img/16/decision.png" />&nbsp;&nbsp;条件</div>	
<%-- 
<div class="node state" id="fork" type="fork"><img
	src="img/16/gateway_parallel.png" />&nbsp;&nbsp;分支</div>
<div class="node state" id="join" type="join"><img
	src="img/16/gateway_parallel.png" />&nbsp;&nbsp;合并</div>
--%>
<div class="node state" id="end" type="end"><img
	src="img/16/end_event_terminate.png" />&nbsp;&nbsp;结束</div>
<%-- 
<div class="node state" id="end-cancel" type="end-cancel"><img
	src="img/16/end_event_cancel.png" />&nbsp;&nbsp;取消</div>
<div class="node state" id="end-error" type="end-error"><img
	src="img/16/end_event_error.png" />&nbsp;&nbsp;错误</div>
--%>
</div>

<div id="myflow_props"
	style="position: absolute; top: 30; right: 50; background-color: #fff; width: 220px; padding: 3px;"
	class="ui-widget-content">
<div id="myflow_props_handle" class="ui-widget-header">属性</div>
<table border="1" cellpadding="0" cellspacing="0">
	<tr>
		<td>aaa</td>
	</tr>
	<tr>
		<td>aaa</td>
	</tr>
</table>
<div>&nbsp;</div>
</div>

<div id="myflow"></div>

</body>
</html>