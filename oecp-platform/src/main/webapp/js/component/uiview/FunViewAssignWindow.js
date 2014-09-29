// 界面视图分配窗口
Ext.ns('OECP.uiview');

OECP.uiview.FunViewAssignWindow = Ext.extend(Ext.Window, {
	title : '界面视图分配窗口',
	width : 600,
	height : 450,
	closeAction : 'hide',
	layout : {
		type : 'vbox',
		align : 'stretch'
	},

	bizTypePanel : null,
	userPanel : null,
	rolePanel : null,
	postPanel : null,
	tabsPanel : null,
	buttons : [],
	updateUrl : __ctxPath + '/funviewassign/updateFunViewAssign.do',
	queryFunViewAssignsUrl : __ctxPath + '/funviewassign/queryFunViewAssign.do',
	functionViewId : '',
	bizTypeId : '',

	initComponent : function() {
		var master = this;
		// 业务类型
		master.bizType_sm = new Ext.grid.CheckboxSelectionModel({
					singleSelect : true
				});
		master.bizType_cm = new Ext.grid.ColumnModel([master.bizType_sm,
				new Ext.grid.RowNumberer(), {
					header : '编号',
					dataIndex : 'code'
				}, {
					header : '名称',
					dataIndex : 'name'
				}, {
					header : '公司 ',
					dataIndex : 'org.name'
				}, {
					header : '描述',
					dataIndex : 'description'
				}]);
		master.bizType_store = new Ext.data.JsonStore({
					url : __ctxPath + '/billflow/loadBizTypes.do',
					baseParams : {
						functionCode : ""
					},
					fields : ['id', 'code', 'name', 'org.name', 'description']
				});
		master.bizTypePanel = new Ext.grid.GridPanel({
					title : '业务类型',
					flex : 3,
					enableDD : false,
					enableDrag : false,
					autoScroll : true,
					sm : master.bizType_sm,
					cm : master.bizType_cm,
					store : master.bizType_store
				});

		// 用户
		master.userSearchForm = new Ext.FormPanel({
			height : 35,
			frame : true,
			region : 'north',
			id : 'UserSearchForm',
			layout : 'column',
			defaults : {
				xtype : 'label'
			},
			items : [{
						text : '用户账号'
					}, {
						xtype : 'hidden',
						name : 'conditions[0].field',
						value : 'loginId'
					}, {
						xtype : 'hidden',
						name : 'conditions[0].operator',
						value : 'like'
					}, {
						width : 80,
						xtype : 'textfield',
						name : 'conditions[0].value'
					}, {
						text : '用户姓名'
					}, {
						xtype : 'hidden',
						name : 'conditions[1].field',
						value : 'name'
					}, {
						xtype : 'hidden',
						name : 'conditions[1].operator',
						value : 'like'
					}, {
						width : 80,
						xtype : 'textfield',
						name : 'conditions[1].value'
					}, {
						xtype : 'button',
						text : '查询',
						iconCls : 'search',
						handler : function() {
							if (master.userSearchForm.getForm().isValid()) {
								master.userSearchForm.getForm().submit({
									waitMsg : '正在提交查询',
									url : __ctxPath
											+ '/funviewassign/queryUserList.do',
									success : function(formPanel, action) {
										var result = Ext.util.JSON
												.decode(action.response.responseText);
										master.user_store.loadData(result);
									}
								});
							}
						}
					}]
		});

		master.user_sm = new Ext.grid.CheckboxSelectionModel();
		master.user_cm = new Ext.grid.ColumnModel([master.user_sm,
				new Ext.grid.RowNumberer(), {
					header : "id",
					dataIndex : 'id',
					hidden : true
				}, {
					header : "账号",
					dataIndex : 'loginId'
				}, {
					header : "用户名",
					dataIndex : 'name'
				}, {
					header : "邮箱",
					dataIndex : 'email'
				}, {
					header : "创建时间",
					dataIndex : 'createTime'
				}]);
		master.user_store = new Ext.data.JsonStore({
					url : __ctxPath + '/funviewassign/queryUserList.do',
					autoLoad : true,
					root : "result",
					fields : ['id', 'loginId', 'name', 'email', 'createTime']
				});
		master.userPanel = new Ext.grid.GridPanel({
					title : '用户',
					tbar : [master.userSearchForm],
					region : 'center',
					enableDD : false,
					enableDrag : false,
					autoScroll : true,
//					autoHeight : true,
					sm : master.user_sm,
					cm : master.user_cm,
					store : master.user_store
				});
		// 角色
		master.role_sm = new Ext.grid.CheckboxSelectionModel();
		master.role_cm = new Ext.grid.ColumnModel([master.role_sm,
				new Ext.grid.RowNumberer(), {
					header : "id",
					dataIndex : 'id',
					hidden : true
				}, {
					header : '角色编号',
					dataIndex : 'role.code'
				}, {
					header : '角色名称',
					dataIndex : 'role.name'
				}]);
		master.role_store = new Ext.data.JsonStore({
					url : __ctxPath + '/funviewassign/getOrgRoleList.do',
					autoLoad : true,
					fields : ['id', 'role.code', 'role.name']
				});
		master.rolePanel = new Ext.grid.GridPanel({
					title : '角色',
					enableDD : false,
					enableDrag : false,
					autoScroll : true,
					sm : master.role_sm,
					cm : master.role_cm,
					store : master.role_store
				});

		// 岗位
		master.post_sm = new Ext.grid.CheckboxSelectionModel();
		master.post_cm = new Ext.grid.ColumnModel([master.post_sm,
				new Ext.grid.RowNumberer(), {
					header : "主键",
					dataIndex : "id",
					hidden : true
				}, {
					header : "岗位名称",
					dataIndex : "name"
				}, {
					header : "岗位编码",
					dataIndex : "code"
				}]);
		master.post_store = new Ext.data.JsonStore({
					url : __ctxPath + '/funviewassign/getPostList.do',
					autoLoad : true,
					fields : ['id', 'name', 'code']
				});
		master.postPanel = new Ext.grid.GridPanel({
					title : '岗位',
					enableDD : false,
					enableDrag : false,
					autoScroll : true,
					sm : master.post_sm,
					cm : master.post_cm,
					store : master.post_store
				});

		master.tabsPanel = new Ext.TabPanel({
					flex : 4,
					activeTab : 0,
					deferredRender : false,
					defaults : {
						autoScroll : true
					},
					items : [master.userPanel, master.postPanel,
							master.rolePanel]
				});

		master.bizSaveButton = new OECP.ui.button.BizSaveButton({
					handler : function() {
						var uidata = {};
						uidata['functionViewId'] = master.functionViewId;
						uidata['bizTypeId'] = '';
						var bizType_sele = master.bizTypePanel
								.getSelectionModel().getSelections();
						if (bizType_sele.length == 1) {
							uidata['bizTypeId'] = bizType_sele[0].data['id'];
						}

						var user_sele = master.userPanel.getSelectionModel()
								.getSelections();
						uidata['userIds'] = [];
						for (i = 0; i < user_sele.length; i++) {
							uidata['userIds'].push(user_sele[i].data['id']);
						}

						var post_sele = master.postPanel.getSelectionModel()
								.getSelections();
						uidata['postIds'] = [];
						for (i = 0; i < post_sele.length; i++) {
							uidata['postIds'].push(post_sele[i].data['id']);
						}

						var role_sele = master.rolePanel.getSelectionModel()
								.getSelections();
						uidata['orgRoleIds'] = [];
						for (i = 0; i < role_sele.length; i++) {
							uidata['orgRoleIds'].push(role_sele[i].data['id']);
						}

						Ext.Ajax.request({
									url : master.updateUrl,
									success : function(response) {
										var msg = Ext.util.JSON
												.decode(response.responseText);
										if (msg.success) {
											Ext.ux.Toast.msg("信息", msg.msg);
											master.hide();
										}
									},
									failure : function() {
										Ext.Msg.show({
													title : "错误",
													msg : msg.result.msg,
													buttons : Ext.Msg.OK
												});
									},
									params : uidata
								});
					}
				});
		master.bizCloseButton = new OECP.ui.button.BizCloseButton({
					handler : function() {
						master.hide();
					}
				});

		// 回填视图分配信息
		master.idscheck = function() {
			Ext.Ajax.request({
						url : master.queryFunViewAssignsUrl,
						success : function(response) {
							var funViewAssignJson = Ext.util.JSON
									.decode(response.responseText);
							var userJson = funViewAssignJson.userJson;
							var postJson = funViewAssignJson.postJson;
							var orgroleJson = funViewAssignJson.orgroleJson;
							// user
							var user_rows = [];
							for (i = 0; i < master.user_store.getCount(); i++) {
								var u = master.user_store.getAt(i);
								if (userJson.indexOf(u.get('id')) != -1) {
									user_rows.push(i);
								}
							}
							master.user_sm.selectRows(user_rows);
							// post
							var post_rows = [];
							for (i = 0; i < master.post_store.getCount(); i++) {
								var p = master.post_store.getAt(i);
								if (postJson.indexOf(p.get('id')) != -1) {
									post_rows.push(i);
								}
							}
							master.post_sm.selectRows(post_rows);
							// orgrole
							var orgrole_rows = [];
							for (i = 0; i < master.role_store.getCount(); i++) {
								var r = master.role_store.getAt(i);
								if (orgroleJson.indexOf(r.get('id')) != -1) {
									orgrole_rows.push(i);
								}
							}
							master.role_sm.selectRows(orgrole_rows);
						},
						params : {
							functionViewId : master.functionViewId,
							bizTypeId : master.bizTypeId
						}
					});
		};

		//业务类型改变
		
		master.bizType_sm.on('selectionchange',function(bizType_model){
			var bizType_sele = bizType_model.getSelections();
			if(bizType_sele.length == 0){
				master.bizTypeId = '';
			}else if(bizType_sele.length == 1){
				master.bizTypeId = bizType_sele[0].data['id'];
			}
			master.idscheck();
		});
		
		this.items = [master.bizTypePanel, master.tabsPanel];
		this.buttons = [master.bizSaveButton, master.bizCloseButton];
		OECP.uiview.FunViewAssignWindow.superclass.initComponent.call(this);
	}
});