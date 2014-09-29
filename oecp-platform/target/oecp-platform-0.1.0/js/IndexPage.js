var IndexPage = Ext.extend(Ext.Viewport, {
	constructor : function() {
		var master = this;
		this.top = new Ext.Panel({
					region : "north",
					id : "north-panel",
					contentEl : "app-header",
					height : 60
				});
		this.west = new OECP.ui.AccordionTreePanel({
					collapsible : true,
					region : "west",
					id : "west-panel",
					title : "导航",
					iconCls : "menu-navigation",
					split : true,
					width : 180,
					fullDataUrl : __ctxPath + '/portal/menu.do',
					treeEvent : {
						"click" : App.clickNode
						,"contextmenu":App.showNodeMenu
					}
				});
		this.west.on('afterloaded',function(){
			var westpanel = this.west;
			App.initShortcut(function(shortcutPanel){// 显示快捷方式
				if(shortcutPanel.items.length>0){
					if(shortcutPanel != westpanel.items.items[0]){
						westpanel.insert(0,shortcutPanel);
						westpanel.doLayout();
						shortcutPanel.expand();
					}
				}
			});
			App.MyDesktopClick(); // 显示我的桌面
		},this);
		this.south = new Ext.Panel({
			region : "south",
			height : 28,
			border : false,
			bbar : [
					{
						text : "退出系统",
						iconCls : "btn-logout",
						handler : function() {
							App.Logout();
						}
					},
					"-",
					{
						text : "在线用户",
						iconCls : "btn-onlineUser",
						handler : function() {
							OECP.selector.OnlineUserSelector.getView().show();
						}
					},
					"-",
					{
						text : "意见箱",
						iconCls : "btn-suggest-box",
						handler : function() {
							App.clickTopTab("SuggestBoxView", {
										title : "我的意见箱",
										userId : curUserInfo.userId
									});
						}
					},
					"-",
					{
						text : "修改密码",
						iconCls : "btn-update-password",
						handler : function() {
							OECP.user.UpdatePassWordWin().show();
						}
					},
					"-",
					{
						id : "messageTip",
						xtype : "button",
						hidden : true,
						width : 50,
						height : 20,
						handler : function() {
							var a = Ext.getCmp("messageTip");
							var b = Ext.getCmp("win");
							if (b == null) {
								var win = new OECP.message.MessageWin();
								win.show();
								win.on('destroy', function() {
											master.afterPropertySet();
										});
								win.btns.on('click',function(){
									win.close(); 
									var node = {id : 'foo',text : 'foo',leaf : true,attributes : {id : 'foo',text : 'foo',iframe : win.frame,
													iframeUrl : win.openPath,leaf : true,text : 'foo'}};
									App.clickNode(node);
								});
							}
							a.hide();
						}
					},
					"-",
					{
						id : "warningTip",
						xtype : "button",
						hidden : true,
						width : 50,
						height : 20,
						handler : function() {
							var a = Ext.getCmp("warningTip");
							var b = Ext.getCmp("win");
							if (b == null) {
								var win = new OECP.Warn.SendWarnWin();
								win.show();
								win.on('destroy', function() {
											master.afterPropertyWarningSet();
										});
								win.btns.on('click',function(){
									win.close(); 
									var node = {id : 'foo',text : 'foo',leaf : true,attributes : {id : 'foo',text : 'foo',iframe : win.frame,
													iframeUrl : win.openPath,leaf : true,text : 'foo'}};
									App.clickNode(node);
								});
							}
//							a.hide();
						}
					},
					"->",
					{
						xtype : "tbfill"
					},
					{
						xtype : "tbtext",
						text : __groupName,
						id : "toolbarCompanyName"
					},
					{
						xtype : "tbseparator"
					},
					new Ext.Toolbar.TextItem(''),
					"-", {
						text : "收展",
						iconCls : "btn-expand",
						handler : function() {
							var np = Ext.getCmp("north-panel");
							if (np.collapsed) {
								np.expand(true);
							} else {
								np.collapse(true);
							}
							var wp = Ext.getCmp("west-panel");
							if (wp.collapsed) {
								wp.expand(true);
							} else {
								wp.collapse(true);
							}
						}
					}, "-", {
						xtype : "combo",
						mode : "local",
						editable : false,
						value : "切换皮肤",
						width : 100,
						triggerAction : "all",
						store : [["ext-all", "缺省浅蓝"], 
						         ["xtheme-gray", "哑光灰色"],
						         ["ext-all-xtheme-blue03", "浅灰主题"],
						         ["ext-all-xtheme-gray", "灰色主题"],
						         ["ext-all-xtheme-black", "暗灰主题"],
						         ["ext-all-xtheme-brown02", "暗黄主题"],
						         ["ext-all-xtheme-green", "绿色主题"],
						         ["ext-all-xtheme-pink", "淡粉主题"],
						         ["ext-all-xtheme-purple", "粉蓝主题"],
						         ["ext-all-xtheme-red03", "粉红主题"],
						         ["ext-all-xtheme-brown", "暗红主题"],
							["xtheme-access", "Access风格"]],
						listeners : {
							scope : this,
							"select" : function(d, b, c) {
								if (d.value != "") {
									var a = new Date();
									a.setDate(a.getDate() + 300);
									setCookie("theme", d.value, a, __ctxPath);
									updateTheme();
									for(var i=0 ; i<window.frames.length; i++){
									    updateTheme(window.frames[i]);
									}
								}
							}
						}
					}]
		});
		this.center = new Ext.TabPanel({
					id : "centerTabPanel",
					region : "center",
					deferredRender : true,
					enableTabScroll : true,
					activeTab : 0,
					defaults : {
						autoScroll : true,
						closable : true
					},
					items : [],
					plugins : new Ext.ux.TabCloseMenu(),
					listeners : {
//						"add" : function(c, a, b) {
//							if (c.items.length > 8) {
//								c.remove(c.items.get(0));
//								c.doLayout();
//							}
//						}
					}
				});
		IndexPage.superclass.constructor.call(this, {
					layout : "border",
					items : [this.top, this.west, this.center, this.south]
				});
		this.afterPropertySet();
		this.afterPropertyWarningSet();
	},
	//发送的消息
	afterPropertySet : function() {
		var a = this.center;
		var c = function(f) {
			var d = Ext.getCmp("messageTip");
			var g = Ext.getCmp("win");
			var e = Ext.getCmp("wind");
			if (f > 0 && g == null && e == null) {
				d
						.setText('<div style="height:25px;"><img src="'
								+ __ctxPath
								+ '/images/newpm.gif" style="height:12px;"/>你有<strong style="color: red;">'
								+ f + "条</strong>信息</div>");
				d.show();
			} else {
				d.hide();
			}
		};
		setInterval(function() {
			Ext.Ajax.request({
						url : __ctxPath + "/message/selectCount.do",
						method : "POST",
						success : function(e, f) {
							var d = Ext.util.JSON.decode(e.responseText);
							count = d.count;
							c(count);
						},
						failure : function(d, e) {
						}
					});
		}, 60000);
	},
	//预警信息
	afterPropertyWarningSet : function() {
		var a = this.center;
		var c = function(f) {
			var d = Ext.getCmp("warningTip");
			if(f>0){
				d.setText('<div style="height:25px;"><img src="'
						+ __ctxPath
						+ '/images/warning.gif" style="height:20px;width:40px;"/></div>');
				d.show();
			}else{
				d.hide();
			}
		};
		setInterval(function() {
			Ext.Ajax.request({
						url : __ctxPath + "/warn/manage/sendWarnCount.do",
						method : "POST",
						success : function(e, f) {
							var d = Ext.util.JSON.decode(e.responseText);
							count = d.count;
							c(count);
						},
						failure : function(d, e) {
						}
					});
		}, 60000);
	}
});

Ext.onReady(function() {
			App.init();
			new IndexPage();
		})
