/**
 * 用户选择器
 */
Ext.ns("OECP.selector");
OECP.selector.OnlineUserSelector = {
	getView : function(callback, isSingle) {
		var panel = this.initPanel(isSingle);
		var window = new Ext.Window({
			title : '选择在线用户',
			width : 600,
			height : 420,
			layout : 'fit',
			items : [panel],
			modal : true,
			buttonAlign : 'center',
			buttons : [{
						text : '确认',
						iconCls : 'btn-ok',
						scope : 'true',
						handler : function() {
							var grid = Ext.getCmp('contactGrid');
							var rows = grid.getSelectionModel().getSelections();
							var userIds = '';
							var fullnames = '';
							for (var i = 0; i < rows.length; i++) {

								if (i > 0) {
									userIds += ',';
									fullnames += ',';
								}
								userIds += rows[i].data.userId;
								fullnames += rows[i].data.fullname;
							}

							if (callback != null) {
								callback.call(this, userIds, fullnames);
							}
							window.close();
						}
					}, {
						text : '关闭',
						iconCls : 'btn-cancel',
						handler : function() {
							window.close();
						}
					}]
		});
		return window;
	},

	initPanel : function(isSingle) {

		var store = new Ext.data.Store({
					proxy : new Ext.data.HttpProxy({
								url : __ctxPath + '/portal/onlineUser/list.do'
							}),
					reader : new Ext.data.JsonReader({
								root : 'result',
								totalProperty : 'totalCounts',
								id : 'userId',
								fields : ['username', 'orgName', 'loginId',
										'orgId', 'orgCode', 'loginTime',
										'lastActiveTime']
							})
					//remoteSort : true
				});
		store.load({
					params : {
						start : 0,
						limit : 12
					}
				});
		var sm = null;
		if (isSingle) {
			var sm = new Ext.grid.CheckboxSelectionModel({
						singleSelect : true
					});
		} else {
			sm = new Ext.grid.CheckboxSelectionModel();
		}
		var cm = new Ext.grid.ColumnModel({
					columns : [sm, {
								header : "用户",
								dataIndex : 'username',
								renderer : function(value, meta, record) {
									var loginId = record.data.loginId;
									return value + "[" + loginId + "]";
								},
								width : 40
							}, {
								header : "登录机构",
								dataIndex : 'orgName',
								renderer : function(value, meta, record) {
									var orgCode = record.data.orgCode;
									return value + "[" + orgCode + "]";
								},
								width : 60
							}, {
								header : "登陆时间",
								dataIndex : 'loginTime',
								width : 60
							}],
					defaults : {
						sortable : true,
						menuDisabled : true,
						width : 120
					},
					listeners : {
						hiddenchange : function(cm, colIndex, hidden) {
							saveConfig(colIndex, hidden);
						}
					}
				});

		var orgPanel = new Ext.tree.TreePanel({
					id : 'treePanels',
					title : '按机构分类 ',
					iconCls : 'org-user',
					loader : new Ext.tree.TreeLoader({
								url : __ctxPath + '/org/tree.do'
							}),
					root : new Ext.tree.AsyncTreeNode({
								expanded : true
							}),
					rootVisible : false,
					listeners : {
						'click' : this.clickNode
					}
				});

		var onlinePanel = new Ext.Panel({
					id : 'onlinePanel',
					iconCls : 'online-user',
					title : '所有在线人员  ',
					listeners : {
						'expand' : this.clickOnlinePanel
					}
				});

		var contactGrid = new Ext.grid.GridPanel({
					id : 'contactGrid',
					height : 345,
					store : store,
					shim : true,
					trackMouseOver : true,
					disableSelection : false,
					loadMask : true,
					cm : cm,
					sm : sm,
					viewConfig : {
						forceFit : true,
						enableRowBody : false,
						showPreview : false
					},
					bbar : new Ext.PagingToolbar({
								pageSize : 12,
								store : store,
								autoWidth : true,
								displayInfo : true,
								displayMsg : '当前显示从{0}至{1}， 共{2}条记录',
								emptyMsg : "当前没有记录"
							})
				});

		var contactPanel = new Ext.Panel({
					id : 'contactPanel',
					width : 400,
					height : 400,
					layout : 'border',
					border : false,
					items : [{
								region : 'west',
								split : true,
								collapsible : true,
								width : 130,
								margins : '5 0 5 5',
								layout : 'accordion',
								items : [orgPanel, onlinePanel]
							}, {
								region : 'center',
								margins : '5 0 5 5',
								width : 220,
								items : [contactGrid]
							}]
				});
		return contactPanel;
	},

	clickNode : function(node) {
		if (node != null) {
			var users = Ext.getCmp('contactGrid');
			var store = users.getStore();
			store.proxy.conn.url = __ctxPath + '/portal/onlineUser/list.do';
			store.baseParams = {
				orgId : node.id
			};
			store.load({
						params : {
							start : 0,
							limit : 12
						}
					});
		}
	},
	clickOnlinePanel : function() {
		var users = Ext.getCmp('contactGrid');
		var store = users.getStore();
		store.baseParams = {
			orgId : null
		};
		store.proxy.conn.url = __ctxPath + '/portal/onlineUser/list.do';
		store.load({
					params : {
						start : 0,
						limit : 200
					}
				});
	}
};
