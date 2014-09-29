<%@ page language="java" pageEncoding="UTF-8"%>
<%@ page import = "java.util.*,oecp.framework.web.WebConstant" %>
<%
	String path = request.getContextPath();
	String basePath = request.getScheme() + "://"
			+ request.getServerName() + ":" + request.getServerPort()
			+ path + "/";
	String logoUrl = path+(String)session.getAttribute(WebConstant.OECP_ORG_LOGOURL);		
%>

<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">

<html>
	<head>
		<base href="<%=basePath%>">
		<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">

		<meta http-equiv="msthemecompatible" content="no">

		<title>OECP企业业务管理平台</title>
		
		<link rel="stylesheet" type="text/css"  href="extjs/resources/css/ext-all-notheme.css" />
		
		<link rel="stylesheet" type="text/css"  href="extjs/resources/css/ext-patch.css" />

		<link rel="stylesheet" type="text/css" href="extjs/ux/css/Portal.css" />

		<link rel="stylesheet" type="text/css" href="css/portal.css" />

		<link rel="stylesheet" type="text/css" href="ext-oecp/ui/datatimefield/css/Spinner.css" />
		<!-- 视图相关css -->
		<link rel="stylesheet" type="text/css" href="extjs/ux/css/GroupSummary.css" />
		<link rel="stylesheet" type="text/css" href="extjs/ux/css/LockingGridView.css" />
		<link rel="stylesheet" type="text/css" href="ext-oecp/css/ext-oecp.css" />
		<link rel="stylesheet" type="text/css" href="js/component/uiview/editor/css/main.css" />
		
		<link rel="stylesheet" type="text/css"  href="extensible/resources/css/extensible-all.css" />
		<!-- load the extjs libary -->

		<script type="text/javascript" src="js/context-path.jsp"></script>

		<!-- Ext 核心JS -->

		<script type="text/javascript" src="extjs/adapter/ext/ext-base-debug.js"></script>

		<script type="text/javascript" src="extjs/ext-all-debug.js"></script>

		<script type="text/javascript" src="extjs/ext-basex.js"></script>

		<script type="text/javascript" src="extjs/ext-lang-zh_CN.js"></script>

		<!--使用iframe加载的依赖JS  -->
		<script type="text/javascript" src="extjs/miframe-debug.js"></script>

		<!-- 需要的 JS-->
		<script type="text/javascript" src="js/core/AppUtil.js"></script>
		<script type="text/javascript" src="js/core/ScriptMgr.js"></script>

		<script type="text/javascript" src="js/App.import.js"></script>
		<!-- 工具JS -->

		<script type="text/javascript" src="js/App.js"></script>
		<script type="text/javascript" src="js/component/printTemplate/LodopFuncs.js"></script>

		<!-- ext-oecp ui -->
		<script type="text/javascript" src="extjs/ux/ux-all-debug.js"></script>
		<script type="text/javascript" src="ext-oecp/core/Toast.js"></script>
		<script type="text/javascript" src="ext-oecp/ui/ToftPanel.js"></script>
		<script type="text/javascript" src="ext-oecp/ui/Button.js"></script>
		<script type="text/javascript" src="ext-oecp/ui/FormWindow.js"></script>
		<script type="text/javascript" src="ext-oecp/ui/TreeGridPanel.js"></script>
		<script type="text/javascript" src="ext-oecp/ui/AccordionTreePanel.js"></script>
		<script type="text/javascript" src="ext-oecp/ui/chart/anychart/js/AnyChart.js"></script>
		<script type="text/javascript" src="ext-oecp/ui/chart/anychart/js/AnyChartHTML5.js"></script>
		<script type="text/javascript" src="ext-oecp/ui/chart/Ext-AnyChart.js"></script>

		<script type="text/javascript" src="js/selector/OnlineUserSelector.js"></script>
		<script type="text/javascript" src="js/component/message/MessageWin.js"></script>
		<script type="text/javascript" src="js/component/warn/SendWarnWin.js"></script>
		<script type="text/javascript" src="js/component/user/updatePassWord.js"></script>
		
		<!-- 首页样式加载 -->

		<!--		
		     <link href="css/desktop.css" rel="stylesheet" type="text/css" />
		-->
		<script type="text/javascript">

	       var __groupName="${onlineUser.loginedOrg.name}";

		   Ext.onReady(function(){
		       updateTheme();
		    });
			function updateTheme(scope){
				  if(!scope){
				      scope = this;
				  }
				  try{// 防止跨越的内嵌页面出错导致后面的页面无法应用样式
				      if(scope.Ext){
					      var storeTheme=getCookie('theme');
					   	  if(storeTheme==null || storeTheme==''){
						   	  storeTheme='ext-all';
					   	  }
						  scope.Ext.util.CSS.swapStyleSheet("theme", __ctxPath+"/extjs/resources/css/"+storeTheme+".css");  
						  scope.Ext.util.CSS.swapStyleSheet("patch",__ctxPath + "/extjs/resources/css/ext-patch.css");
					      swapStyleSheet(scope.document,"ux", __ctxPath+"/extjs/ux/css/ux-all.css");
					  }
				  }catch(e){
				      return;
				  }
				  
			}
			
			function swapStyleSheet(doc,id,cssurl){
			    
			    var existing = doc.getElementById(id);
				if(existing) 
				    existing.parentNode.removeChild(existing);
				var ss = doc.createElement("link");
				ss.setAttribute("rel", "stylesheet");
				ss.setAttribute("type", "text/css");
				ss.setAttribute("id", "ux");
				ss.setAttribute("href",  __ctxPath+"/extjs/ux/css/ux-all.css");
				doc.getElementsByTagName("head")[0].appendChild(ss);
			}
	
	</script>



		<!-- 个人首页JS -->

		<script type="text/javascript" src="js/IndexPage.js"></script>
	</head>

	<body oncontextmenu="return false">
		<object id="LODOP_OB" classid="clsid:2105C259-1E0C-4534-8141-A753534CB4CA" width=0 height=0> 
			<embed id="LODOP_EM" type="application/x-print-lodop" width=0 height=0 pluginspage="install_lodop.exe"></embed>
		</object> 

		<div id="loading">

			<div class="loading-indicator">

				<img src="<%=path%>/images/loading.gif" alt="" width="153"
					height="16" style="margin-right: 8px;" align="absmiddle" />

				<div class="clear"></div>

				正在加载，请稍候......

			</div>

		</div>

		<div id="loading-mask"></div>



		<div id="app-header" style="display: none;">

			<div id="header-left">

				<img id="CompanyLogo" src="<%=logoUrl%>" height="50"
					style="max-width: 230px;" />

			</div>

			<div id="header-main">

				<div id="topInfoPanel" style="float: left; padding-bottom: 4px">

					<div id="welcomeMsg">
						欢迎您，${onlineUser.user.name}
					</div>
				</div>

				<div class="clear"></div>

				<ul id="header-topnav">

					<li class="activeli">
						<a href="javascript:void(0);" onclick="App.MyDesktopClick()"
							style="text-indent: 25px;" class="menu-index-company"> 我的桌面</a>
					</li>

<!-- 					<li class="commonli"> -->
<!-- 						<a href="#" onclick="App.clickTopTab('PersonalMailBoxView')" -->
<!-- 							class="menu-mail_box">邮件</a> -->
<!-- 					</li> -->

					<li class="commonli">
						<a href="javascript:void(0);" onclick="App.MyCanlendarClick('Ext.ensible.cal.Canlendar')"
							class="menu-task">日历</a>
					</li>

				</ul>

			</div>

			<div id="header-right">

				<div id="setting">
					<a href="#" target="blank">帮助</a>
				</div>
				
				<div id="searchFormDisplay"
					style="width: 260px; height: 30px; float: right; padding-top: 8px;">
					&nbsp;
				</div>

			</div>

		</div>



	</body>

</html>