Ext.ns("App");

var UserInfo = function(a) {
	this.id = a.id;
	this.name = a.name;
	this.loginId = a.loginId;
	this.orgId = a.orgId;
	this.orgName = a.orgName;
	this.rights = a.rights;
	this.portalConfig = a.items;
};
var curUserInfo = null;
function isGranted(a) {
	if (curUserInfo.rights.indexOf("__ALL") != -1) {
		return true;
	}
	if (curUserInfo.rights.indexOf(a) != -1) {
		return true;
	}
	return false;
}
App.init = function() {
	Ext.QuickTips.init();
	Ext.BLANK_IMAGE_URL = __ctxPath + "/extjs/resources/images/default/s.gif";
	setTimeout(function() {
				Ext.get("loading").remove();
				Ext.get("loading-mask").fadeOut({
							remove : true
						});
				document.getElementById("app-header").style.display = "block";
			}, 1000);
	Ext.util.Observable.observeClass(Ext.data.Connection);
	Ext.data.Connection.on("requestcomplete", function(c, d, b) {
				if (d && d.getResponseHeader) {
					if (d.getResponseHeader("__timeout")) {
						Ext.MessageBox.show({
									title : "操作信息",
									msg : decodeURIComponent(d
											.getResponseHeader("__timeout")),
									buttons : Ext.MessageBox.OK,
									icon : Ext.MessageBox.ERROR,
									fn : function() {
										window.location.href = __ctxPath
												+ "/login.jspx";
									}
								});

					} else {
						if (d.getResponseHeader("__403_error")) {
							Ext.ux.Toast.msg("系统访问权限提示：", "你目前没有权限访问：{0}",
									b.url);
						} else {
							if (d.getResponseHeader("__500_error")) {
								Ext.MessageBox.show({
											title : "操作信息",
											msg : decodeURIComponent(d
													.getResponseHeader("__500_error")),
											buttons : Ext.MessageBox.OK,
											icon : Ext.MessageBox.ERROR
										});
							} else {
								if (d.getResponseHeader("__404_error")) {
									Ext.ux.Toast.msg("后台出错",
											"您访问的URL:{0}对应的页面不存在，具体原因请联系管理员。",
											b.url);
								}
							}
						}
					}
				}
			});
	Ext.Ajax.request({
				url : __ctxPath + "/portal/getOnlineUser.do?random="
						+ Math.random(),
				method : "Get",
				success : function(d, g) {
					var f = Ext.util.JSON.decode(d.responseText);
					var c = f.result;
					curUserInfo = new UserInfo(c);
					// alert(curUserInfo);
					// var b = Ext.getCmp("centerTabPanel");
					// App.clickTopTab("ComIndexPage");
				}
			});
	// var a = new IndexPage();
			
	// 桌面快捷编辑window初始化
	this.editDesktopWin = new OECP.ui.FormWindow({width:300,height:240,closeAction:'hide',
		saveURL : __ctxPath + "/desktop/updatedesktop.do",
		formItems:[{dataIndex:'funcId',fieldLabel: 'funid',name:'funcId',hidden:true,mapping:'funcId'},
			{dataIndex:'ico',fieldLabel: '图标',name:'ico',mapping:'ico',xtype:'combo',editable:false,valueField:'url',displayField:'url',
			store:{xtype:'jsonstore',root:'urls',url:__ctxPath+'/desktop/icons.do',fields:['url'],autoLoad:true},
				tpl :'<table><tr><tpl for="."><td class="x-combo-list-item"><img width=32 height=32 src="{url}" /></td><tpl if="xindex % 3 === 0"></tr><tr></tpl></tpl></tr></table>'
			},
			{dataIndex:'alias',fieldLabel: '别名',name:'alias',maxLength:10,minLength:3,mapping:'alias'},
			{dataIndex:'idx',fieldLabel: '排序号',name:'idx',maxLength:3,mapping:'idx'}
			]
	});
	
	this.editDesktopWin.on('dataSaved',function(from){
		App.shortcutPanel.reloadBtns();
	},this);
};
App.clickTopTab = function(viewName, params, a, e) {
	if (a != null) {
		a.call(this);
	}
	var centerTabPanel = Ext.getCmp("centerTabPanel");
	var viewItem = centerTabPanel.getItem(viewName);
	if (viewItem == null) {
		$ImportJs(viewName, function(g) {
					viewItem = centerTabPanel.add(g);
					centerTabPanel.activate(viewItem);
				}, params);
	} else {
		if (e != null) {
			e.call(this);
		}
		centerTabPanel.activate(viewItem);
	}
};
App.clickTopTabIframe = function(node) {
	if (node.id == null || node.id == "" || node.id.indexOf("xnode") != -1) {
		return;
	}
	var centerTabPanel = Ext.getCmp("centerTabPanel");
	var tabPanel = centerTabPanel.getItem(node.id);
	if (tabPanel == null) {
		var url = node.attributes.iframeUrl;
		url = url + (url.indexOf("?") > 0 ? "&" : "?") + "access_token="+Ext.util.Cookies.get("access_token")+"&error_basepath="+__fullPath;
		tabPanel = centerTabPanel.add({
					xtype : "iframepanel",
					title : node.text,
					id : node.id,
					loadMask : {
						msg : "正在加载...,请稍等..."
					},
					iconCls : node.attributes.iconCls,
					defaultSrc : url,
					listeners : {
					    documentloaded : function(d) {
						    if(Ext){
							updateTheme(d.dom.contentWindow);
						    }
						}
					}
				});
	}
	centerTabPanel.activate(tabPanel);
};
App.MyDesktopClickTopTab = function(id, params, precall, callback) {
	if (precall != null) {
		precall.call(this);
	}
	var tabs = Ext.getCmp("centerTabPanel");
	var tabItem = tabs.getItem(id);
	if (tabItem == null) {
		$ImportJs(id, function(view) {
					tabItem = tabs.add(view);
					tabs.activate(tabItem);
				}, params);
	} else {
		tabs.remove(tabItem);
		var str = "new " + id;
		if (params != null) {
			str += "(params);";
		} else {
			str += "();";
		}
		var view = eval(str);
		tabItem = tabs.add(view);
		tabs.activate(tabItem);
	}
};
/**
 * 显示桌面快捷方式
 */
App.initShortcut = function(callback){
	if(!App.shortcutPanel){
		App.shortcutPanel = new Ext.Panel({
			title : '我的快捷方式',
			animate : true,
			border : false,
			autoScroll : true,
		    autoScroll:true,
		    reloadBtns : function(){
		    	Ext.Ajax.request({
					url : __ctxPath + "/desktop/list.do",
					scope:App,
					success : function(d, g) {
						var json = Ext.util.JSON.decode(d.responseText);
						var uds = json.data;
						App.shortcutPanel.removeAll();
						for(var i=0 ; uds && i< uds.length;i++){
							var ud = uds[i];
							var btn = App.createDesktopBtn(ud.alias,ud.ico,ud['function'].id,ud['function'].code,ud.idx);
							App.shortcutPanel.add(btn);
							btn.parent = App.shortcutPanel;
						}
						App.shortcutPanel.doLayout();
						callback(App.shortcutPanel);
					}
				});
		    }
	    });
    	App.shortcutPanel.reloadBtns();
	}
};
/**
 * 创建桌面快捷按钮
 * @param {} text
 * @param {} icon
 * @param {} funid
 * @param {} funcode
 * @return {}
 */
App.createDesktopBtn = function(text,icon,funid,funcode,idx){
	var btn = new Ext.Panel({height:80,width:80,style:'float:left;',bodyStyle:'padding:6px;cursor:pointer',border:false,
	text:text,icon:icon?icon:"images/btn/signIn.gif",funid:funid,funcode:funcode,idx:idx,
	html : "<div align='center'><img width=40 height=40 src='" +(icon?icon:"images/btn/signIn.gif")+ "'/></div><div width='60' style='font-size:12' align='center'>"+text+"</div>"
	,listeners : {
			afterrender : function() {
				new Ext.ToolTip({
							target : this.id,
							html : text
						});
				this.getEl().on('click',function(){
					this.doselect();
				},this);
				this.getEl().on('contextmenu',function(e,htmldom){
					this.doselect();
					// 修改别名
					scope = this;
					if (!App.editMenu) {
						App.editMenu = new Ext.menu.Menu({
									items : [{
												text : '编辑快捷方式',
												iconCls : 'icon-editEl',
												handler : function(item, e) {
													// 显示编辑窗口
													App.editDesktopWin.setFormData({
														ico:scope.icon,funcId:scope.funid,alias:scope.text,idx:scope.idx
													});
													App.editDesktopWin.editingbtn = scope;
													App.editDesktopWin.show({model:true});
												}
											},{
												text : '删除快捷方式',
												iconCls : 'icon-deleteEl',
												handler : function(item, e) {
													// 删除快捷方式
													Ext.Ajax.request({
														url : __ctxPath + "/desktop/deldesktop.do",
														params:{funcId:scope.funid},
														success : function(d) {
															var json = Ext.util.JSON.decode(d.responseText);
															if(json.success){
																// 删除图标 后重新加载按钮
																btn.parent.reloadBtns();
																Ext.ux.Toast.msg("信息",json.msg);
															}else{
																// 添加快捷方式到窗口
																Ext.MessageBox.alert('快捷方式删除出错', json.msg);
															}
														}
													});
												}
											}]
								});
					}
					App.editMenu.showAt(e.getXY());
				}, this);
				this.getEl().on('dblclick',function(){
					// 打开界面
					var accpanel = Ext.getCmp("west-panel");
					var node = null;
					for (var i = 0; i < accpanel.items.length; i++) {// 遍历所有的tree
						var tree = accpanel.items.items[i];
						if(tree.root){
						node = App.findTreeNodeConfigBy(tree.root.attributes,"id",this.funcode);
							if(node){
								node.attributes=node;
								break;
							}
						}
					}
					if(node){
						App.clickNode(node);
					}else{
						Ext.ux.Toast.msg("信息","无效的快捷方式！");
					}
				},this);
			}
	},
	doselect : function(){
		var is = this.parent.items;
		for(var i=0; i<is.length;i++){
			if(is.items[i]==this){
				this.body.setStyle('background','#AAA');
			}else{
				is.items[i].body.setStyle('background',null);
			}
		}
	}
	});
	
	return btn;
},
/**
 * 查找树节点上的配置
 * @param {} node
 * @param {} attrname
 * @param {} value
 * @return {}
 */
App.findTreeNodeConfigBy = function(node,attrname,value){
	if(node[attrname] == value){
		return node;
	}else{
		var children = node['children'];
		for (var i = 0; i < children.length; i++) {
			var targetNode = App.findTreeNodeConfigBy(children[i],attrname,value);
			if(targetNode){
				return targetNode;
			}
		}
	}
}
App.showNodeMenu = function(node,e){
	var scope = this;
	if(!this.nodeMenu){
		this.nodeMenu = new Ext.menu.Menu({
					items : [{
								text : '创建快捷方式',
								iconCls : 'icon-addEl',
								handler : function(item,e) {
									var funid = scope.currentNode.attributes['funcid'];
									var text = scope.currentNode.text;
									Ext.Ajax.request({
										url : __ctxPath + "/desktop/addtodesktop.do",
										params:{'funcId':funid},
										scope:this,
										success : function(d, g) {
											var json = Ext.util.JSON.decode(d.responseText);
											if(json.success){
												var btn = App.createDesktopBtn(text,null,funid,scope.currentNode.id);
												if(App.shortcutPanel){
													App.shortcutPanel.add(btn);
													btn.parent = App.shortcutPanel;
													App.shortcutPanel.doLayout();
												}
												Ext.ux.Toast.msg("信息",json.msg);
											}else{
												// 添加快捷方式到窗口
												Ext.MessageBox.alert('快捷方式创建出错', json.msg);
											}
										}
									});
								}
							}]
				});
	}
	
	e.preventDefault();
	if(node.leaf){
		this.nodeMenu.showAt(e.getXY());
		this.currentNode = node;
	}
},
/**
 * 点击一个树的节点
 * 
 * @param {}
 *            node
 */
App.clickNode = function(node) {
	if (node.id == null || node.id == "" || node.id.indexOf("xnode") != -1
			|| !node.leaf) {
		return;
	}
	var title = node.text;
	var url = node.attributes.iframeUrl;
	if (eval(node.attributes.iframe)) {
		App.clickTopTabIframe(node);
	} else {
		if (url.indexOf("?") > 0) {
			var arr = url.split("?");
			var params = "";
			if (arr.length > 0) {
				viewName = arr[0];
				var paramsStr = arr[1];
				var paramsArr = paramsStr.split("&");
				for (i = 0; i < paramsArr.length; i++) {
					var param = paramsArr[i];
					var paramArr = param.split("=");
					var paramName = paramArr[0];
					var paramValue = paramArr[1];
					params += paramName + ":'" + paramValue + "',";
				}
				params += "title:'" + title + "'";
				params = "{" + params + "}";
			}
			App.clickTopTab(viewName, /*Ext.decode(params)*/params);

		} else {
			App.clickTopTab(url);
		}
	}
};
App.MyDesktopClick = function() {
//	var a = Ext.getCmp("MyDesktop");
//	a.expand(true);
	App.clickTopTab("OECP.portal.PortalView");
};
App.MyCanlendarClick = function(v) {
//	var a = Ext.getCmp("MyDesktop");
//	a.expand(true);
	Ext.util.CSS.swapStyleSheet("extensible",__ctxPath + "/extensible/resources/css/extensible-all.css");
	App.clickTopTab(v);
};

App.Logout = function() {
	Ext.Ajax.request({
				url : __ctxPath + "/auth/logout.do",
				success : function() {
					window.location.href = __ctxPath + "/login.jspx";
				}
			});
};
