// 功能UI 查看界面
Ext.ns("OECP.bc.func");
OECP.bc.func.FunctionUIGridPanel = Ext.extend(Ext.Panel, {
	searchPanel : null,
	gridPanel : null,
	store : null,
	topbar : null,
	detailGrid : null,
	detailStore : null,
	closable : true,
	headDataUrl : null,
	bodyDataUrl : null,
	region : 'center',
	layout : {
		type : 'vbox',
		padding : '0 0 0 0',
		align : 'stretch'
	},
	defaults : {
		margins : '0 0 5 0'
	},
	iconCls : 'menu-reportprice',
	functionId : '',
	initComponent : function() {
		var scope = this;
		this.store = new Ext.data.JsonStore({
					url : __ctxPath + '/function/queryFuncUIsByFuncId.do',
					root : 'result',
					totalProperty : 'totalCounts',
					remoteSort : true,
					fields : [{
								name : 'id'
							}, 'code', 'name', 'description', 'isDefault','isDefaultForProcess'],
					listeners : {
						'load' : function() {
							var body = scope.detailGrid;
							body.getStore().removeAll();
							body.setTitle('界面元素');
							scope.addBtn.setDisabled(false);
						}
					}
				});
		this.store.setDefaultSort('id', 'desc');
		var c = new Ext.grid.CheckboxSelectionModel();
		// 主表
		var a = new Ext.grid.ColumnModel({
					columns : [c, new Ext.grid.RowNumberer(), {
								header : '主键',
								dataIndex : 'id',
								hidden : true
							}, {
								header : '功能编号',
								dataIndex : 'code'
							}, {
								header : '界面名称',
								dataIndex : 'name'
							}, {
								header : '描述',
								dataIndex : 'description'
							}, {
								header : '默认界面',
								dataIndex : 'isDefault',
								renderer : function(k, j, g, i, l) {
									var flag = g.data.isDefault;
									if (flag) {
										return '是';
									} else {
										return '否';
									}
								}
							}, {
								header : '流程默认界面',
								dataIndex : 'isDefaultForProcess',
								renderer : function(k, j, g, i, l) {
									var flag = g.data.isDefaultForProcess;
									if (flag) {
										return '是';
									} else {
										return '否';
									}
								}
							}],
					defaults : {
						sortable : true,
						menuDisabled : false,
						width : 100
					}
				});

		var initFunsWindow = new OECP.fun.FormWindow({
					formItems : [{
						dataIndex:'id',
						fieldLabel : "所属组件",
						name:'bc.id',
						xtype:'bccombo',
						hiddenName:'bc.id'}
					],
					saveURL : __ctxPath + '/function/initFunctions.do'
				});		
				
		this.addBtn = new Ext.Button({
					iconCls : 'btn-add',
					text : '添加',
					disabled : true,
					handler : scope.createRecord.createDelegate(scope, [scope],
							true)
				});
		this.delBtn = new Ext.Button({
					iconCls : 'btn-delete',
					text : '删除',
					disabled : true,
					handler : this.delRecords.createDelegate(scope, [scope],
							true)
				});
		this.editBtn = new Ext.Button({
					iconCls : 'btn-edit',
					text : '编辑',
					disabled : true,
					handler : this.editRecord.createDelegate(scope, [scope],
							true)
				});
		this.initFunsBtn = new Ext.Button({
					text : '&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;组件功能导入',
					handler : function(){
						initFunsWindow.show();
					}
				});
		this.topbar = new Ext.Toolbar({
					height : 30,
					bodyStyle : 'text-align:left',
					items : [scope.addBtn, scope.delBtn, scope.editBtn, scope.initFunsBtn]
				});
		this.gridPanel = new Ext.grid.GridPanel({
					flex : 6,
					autoScroll : true,
					stripeRows : true,
					tbar : this.topbar,
					store : this.store,
					trackMouseOver : true,
					disableSelection : false,
					loadMask : true,
					cm : a,
					sm : c,
					viewConfig : {
						forceFit : true,
						autoFill : true
					},
					bbar : new Ext.PagingToolbar({
								pageSize : 10,
								store : this.store,
								displayInfo : true,
								displayMsg : '当前页记录索引{0}-{1}， 共{2}条记录',
								emptyMsg : '当前没有记录'
							})
				});
		this.detailStore = new Ext.data.JsonStore({
					fields : [{
								name : 'id'
							}, 'elementId', 'description',
							'visibleParameterName']
				});
		// 子表
		var b = new Ext.grid.ColumnModel({
					columns : [new Ext.grid.RowNumberer(), {
								header : 'id',
								dataIndex : 'id',
								hidden : true
							}, {
								header : '标识编号',
								dataIndex : 'elementId'
							}, {
								header : '描述',
								dataIndex : 'description'
							}, {
								header : '属性名称',
								dataIndex : 'visibleParameterName'
							}],
					defaults : {
						sortable : true,
						menuDisabled : false,
						width : 100
					}
				});
		this.detailGrid = new Ext.grid.GridPanel({
					flex : 3,
					title : '界面元素',
					stripeRows : true,
					store : this.detailStore,
					trackMouseOver : true,
					disableSelection : false,
					loadMask : true,
					cm : b,
					viewConfig : {
						forceFit : true,
						autoFill : true
					}
				});
		this.gridPanel.addListener('rowclick', function(f, d, g) {

					var rows = f.getSelectionModel().getSelections();
					if (rows.length <= 0) {
						scope.delBtn.setDisabled(true);
						scope.editBtn.setDisabled(true);
					} else if (rows.length == 1) {
						scope.delBtn.setDisabled(false);
						scope.editBtn.setDisabled(false);
					} else {
						scope.delBtn.setDisabled(false);
						scope.editBtn.setDisabled(true);
					}
					// 修改表体数据标题
					f.getSelectionModel().each(function(i) {
						var body = scope.detailGrid;
						body.getStore().removeAll();
						body.setTitle('界面元素' + i.data.code);
						// 获取子表数据
						Ext.Ajax.request({
									method : 'POST',
									url : __ctxPath
											+ '/function/queryUIElementDatasById.do',
									success : function(request) {
										var message = request.responseText;
										var jsonObject = Ext.util.JSON
												.decode(message);// 转换为Json对象
										var e = scope.detailGrid;
										var bodyStore = e.getStore();
										bodyStore.loadData(jsonObject);
									},
									failure : function() { // 发送失败的回调函数
										Ext.Msg.alert('信息', '数据载入失败！');
									},
									params : {
										id : i.get('id')
									}// 参数
								});
					});
				});
		this.gridPanel.addListener(
				// 双击事件
				'rowdblclick', function(grid, rowIndex, event) {
					var tmp_data = grid.getStore().data;
					if (tmp_data) {
						var functionUiId = tmp_data.get(rowIndex).get('id');
						var functionUI = new OECP.bc.func.FunctionUIEditForm({
									functionUiId : functionUiId,
									isEdit : false
								});
						functionUI.show();
					}
				});
		this.items = [this.gridPanel, this.detailGrid];
		OECP.bc.func.FunctionUIGridPanel.superclass.initComponent.call(this);
	},
	createRecord : function() {
		var scope = this;
		fid = this.gridPanel.getStore().baseParams.id
		var functionUI = new OECP.bc.func.FunctionUIEditForm({
					functionId : fid,
					isEdit : true
				});
		functionUI.show();
		functionUI.on('functionuisaved', function(fui) {
					scope.loadUIData(scope.functionId);
				});
	},
	editRecord : function() {
		var scope = this;
		var b = this.gridPanel.getSelectionModel().getSelections();
		if (b.length == 0) {
			Ext.ux.Toast.msg('信息', '请选择要编辑的记录！');
			return;
		}
		if (b.length > 1) {
			Ext.ux.Toast.msg('信息', '请选择一条记录！');
			return;
		}
		var functionUI = new OECP.bc.func.FunctionUIEditForm({
					functionId : this.functionId,
					functionUiId : b[0].get('id'),
					isEdit : true
				});
		functionUI.show();
		functionUI.on('functionuisaved', function(fui) {
					scope.loadUIData(scope.functionId);
				});
	},
	delRecords : function() {
		var scope = this;
		var d = this.gridPanel;
		var b = d.getSelectionModel().getSelections();
		var len = b.length;
		if (len == 0) {
			Ext.ux.Toast.msg('信息', '请选择要删除的记录！');
			return;
		}
		var ids = Array();
		Ext.each(b, function(b1) {
					ids.push(b1.data.id);
				}, this);
		var a = false;
		Ext.Msg.confirm('信息确认', '您确认要删除所选记录吗？', function(b) {
					if (b == 'yes') {
						Ext.Ajax.request({
									url : __ctxPath
											+ '/function/deleteFunUIs.do',
									params : {
										ids : ids
									},
									method : 'POST',
									success : function(res) {
										var msg = Ext.util.JSON
												.decode(res.responseText);
										if (msg.success) {
											Ext.ux.Toast.msg('信息', msg.msg);
											scope.loadUIData(scope.functionId);
										} else {
											Ext.Msg.show({
														title : '错误',
														msg : msg.msg,
														buttons : Ext.Msg.OK
													})
										}
									},
									failure : function() {
										Ext.Msg.show({
													title : '错误',
													msg : msg.result.msg,
													buttons : Ext.Msg.OK
												})
									}
								});
					}
				});
	},
	loadUIData : function(functionId) {
		this.functionId = functionId;
		this.store.baseParams.id = functionId
		this.store.load({
					params : {
						start : 0,
						limit : 10
					}
				});
	}
});

Ext.reg('functionUIGridPanel', OECP.bc.func.FunctionUIGridPanel);
