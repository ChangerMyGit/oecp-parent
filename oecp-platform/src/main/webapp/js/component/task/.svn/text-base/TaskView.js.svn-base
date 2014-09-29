Ext.ns("OECP.Task");

/**
 * 预警视图
 * @class OECP.Warn.WarnView
 * @extends Ext.Panel
 */
OECP.Task.TaskView = Ext.extend(Ext.Panel, {
	id : 'OECP.Task.TaskView',
	layout : 'border',
	title : '任务列表',
	listUrl : __ctxPath +'/task/manage/list.do',
	deleteUrl : __ctxPath + '/task/manage/delete.do',
	startUrl : __ctxPath + '/task/manage/start.do',
	fieldData : ["id","name","oecpTaskGroup.name","createTime","isStart","executeNum","description"],
	listGroupUrl : __ctxPath +'/task/manage/listgroup.do',
	/**
	 * 任务组下拉页面记录数
	 */
	comboPageNum : 10,
	/**
	 * 列表页面记录数
	 * @type Number
	 */
	pageNum : 10,
	getToolBar : function(master){
		var toolBar = new Ext.Toolbar({
			items : [{
						text : '添加',
						tooltip : '添加一条新的记录',
						iconCls : 'add-user',
						listeners : {
							'click' : function(btn, e) {
								var taskConWin = new OECP.Task.TaskConfigWindow({isEdit:false,isView:false});
								taskConWin.show();
								taskConWin.on("doAfterSaved",function(){
									master.store.removeAll();
									master.store.load();
								});
							}
						}
					}, new Ext.Toolbar.Separator(), {
						text : '编辑',
						tooltip : '修改选中的记录',
						iconCls : 'btn-edit',
						listeners : {
							'click' : function(btn, e) {
								var rows = master.grid.getSelectionModel()
										.getSelections();
								if (rows.length <= 0) {
									Ext.Msg.alert("提示信息", "请选择一条记录！");
								} else if (rows.length > 1) {
									Ext.Msg.alert("提示信息", "请选择一条记录！");
								} else if (rows.length == 1) {
									var id = rows[0].get("id");
									var isStart = rows[0].get("isStart");
									if(isStart=='1'){
										Ext.Msg.alert("提示信息", "启用状态不能修改！");
										return;
									}
									var taskConWin = new OECP.Task.TaskConfigWindow({isEdit:true,isView:false,taskId:id});
									taskConWin.show();
									taskConWin.on("doAfterSaved",function(){
										master.store.removeAll();
										master.store.load();
									});
								}
							}
						}
					}, new Ext.Toolbar.Separator(), {
						text : '删除',
						tooltip : '删除选中的的记录',
						iconCls : 'btn-del',
						listeners : {
							'click' : function(btn, e) {
								var rows = master.grid.getSelectionModel()
										.getSelections();
								if (rows.length == 0) {
									Ext.Msg.alert("提示信息", "请选择一条记录！");
								} else {
									var isStart = rows[0].get("isStart");
									if(isStart=='1'){
										Ext.Msg.alert("提示信息", "启用状态不能删除！");
										return;
									}
									Ext.Msg.confirm('提示框','您确定要删除吗！',function(button){
										if(button=='yes'){
											var taskIds = Array();
											for (var i = 0, r; r = rows[i]; i++) {
												taskIds.push(r.id);
											}
											Ext.Ajax.request({
												url : master.deleteUrl,
												params : {
													"taskIds":taskIds
												},
												success : function(request) {
													var json = Ext.decode(request.responseText);
													if (json.success) {
														Ext.ux.Toast.msg('信息',	json.msg);
														master.store.removeAll();
														master.store.load();
													} else {
														Ext.Msg.show({title:'错误',msg:json.msg,buttons:Ext.Msg.OK	});
													}
												},
												failure : function(request) {
													var json = Ext.decode(request.responseText);
													Ext.Msg.show({title:'错误',msg:json.msg,buttons:Ext.Msg.OK});
												}
											});
										}
									},this);
								}

							}
						}
					}, new Ext.Toolbar.Separator(), {
						text : '启用',
						iconCls : 'btn-edit',
						listeners : {
							'click' : function(btn, e) {
								var rows = master.grid.getSelectionModel()
										.getSelections();
								if (rows.length <= 0) {
									Ext.Msg.alert("提示信息", "请选择一条记录！");
								} else if (rows.length > 1) {
									Ext.Msg.alert("提示信息", "请选择一条记录！");
								} else if (rows.length == 1) {
									var id = rows[0].get("id");
									var isStart = rows[0].get("isStart");
									var msg = isStart=='0'?'启用':'停用';
									Ext.Msg.confirm('提示框', '您确定要'+msg+'该任务?', function(button) {
										if (button == 'yes') {
											Ext.Ajax.request({
												url : master.startUrl,
												params : {
													"taskId":id
												},
												success : function(request) {
													var json = Ext.decode(request.responseText);
													if (json.success) {
														Ext.ux.Toast.msg('信息',	json.msg);
														master.store.removeAll();
														master.store.load();
													} else {
														Ext.Msg.show({title:'错误',msg:json.msg,buttons:Ext.Msg.OK	});
													}
												},
												failure : function(request) {
													var json = Ext.decode(request.responseText);
													Ext.Msg.show({title:'错误',msg:json.msg,buttons:Ext.Msg.OK});
												}
											});
										}
									}, this);
								}
							}
						}
					}]
		});
		
		return toolBar;
	},
	/**
	 * 初始化方法
	 */
	initComponent : function(){
		var master = this;
		master.queryBtn	 = new OECP.ui.Button({
			text : "查询",
			iconCls : 'btn-search',
			handler : function(){
				master.query(master);
			}
		});//查询按钮
		this.initQueryPanel();
		// 初始化数据仓库
		master.store = new Ext.data.JsonStore({
					storeId : 'id',
					root : 'result',
					url : master.listUrl,
					totalProperty : "totalCounts",
					fields : master.fieldData,
					baseParams : {
						limit:master.pageNum
					}
		});
		// 初始化按钮
		var toolBar = master.getToolBar(master);
		// 初始化列模式
		var sm = new Ext.grid.CheckboxSelectionModel({
			singleSelect : master.selectCheckboxModel
		});
		
		master.structure = [sm, new Ext.grid.RowNumberer(), {
					header : '主键',
					dataIndex : 'id',
					hidden : true,
					unShowAble : true
				}, {
					header : '任务名称',
					dataIndex : 'name',
					width : 150,
					type : 'string'
				},{
					header : '任务组',
					dataIndex : 'oecpTaskGroup.name',
					width : 150,
					type : 'string'
				},{
					header : '创建时间',
					dataIndex : 'createTime',
					width : 150,
					type : 'string'
				},{
					header : '执行次数',
					dataIndex : 'executeNum',
					width : 100,
					type : 'string'
				},{
					header : '任务描述',
					dataIndex : 'description',
					width : 100,
					type : 'string'
				},{
					header : '启用状态',
					dataIndex : 'isStart',
					width : 100,
					type : 'string',
					renderer : function(value){
						var h = "";
						if(value=='0')
							h = "<p style=\"color:red;\">停用</p>";
						else
							h = "<p style=\"color:green;\">启用</p>";
						return h;
					}
				}, {
					header : '查看日志',
		            xtype: 'actioncolumn',
		            width: 150,
		            items: [{
		                icon   : __fullPath+"/images/btn/flow/view.png",  // Use a URL in the icon config
		                tooltip: '查看日志',
		                width : 50,
		                handler: function(grid, rowIndex, colIndex) {
		                    var id = grid.getStore().getAt(rowIndex).data.id;
		                    var win = new OECP.Task.TaskLogWindow({taskId:id});
							win.show();
		                }
		            }]
		         }]
		// 把列表编号，复选框放入列中
		var columns = master.structure;
		
		var pageBar = new Ext.PagingToolbar({
			pageSize : master.pageNum,
			store : master.store,
			displayInfo : true,
			displayMsg : '当前显示 {0}-{1}条记录 /共{2}条记录',
			emptyMsg : "无显示数据"
		});
		
		// 初始化Grid
		master.grid = new Ext.grid.EditorGridPanel({
			region : 'center',
			store : master.store,
			cm : new Ext.grid.ColumnModel({
						columns : columns
					}),
			sm : sm,
			loadMask : true,
			tbar : toolBar,
			bbar : pageBar
		});
		sm.on("rowselect",function(){
			var record = master.grid.getSelectionModel().getSelected();
			if (!record) {
				Ext.Msg.alert("提示", "请先选择预警!");
				return;
			}
			//启用、停用按钮
			var isStart = record.get('isStart');
			var buttonText = "";
			if(isStart=='1'){
				buttonText = "停用";
				master.grid.getTopToolbar().items.items[2].disable();
				master.grid.getTopToolbar().items.items[4].disable();
			}else{
				buttonText = "启用";
				master.grid.getTopToolbar().items.items[2].enable();
				master.grid.getTopToolbar().items.items[4].enable();
			}
			master.grid.getTopToolbar().items.items[6].setText(buttonText);
		},this);
		//双击
		master.grid.on("rowdblclick",function(){
			var record = master.grid.getSelectionModel().getSelected();
			if (!record) {
				Ext.Msg.alert("提示", "请先选择任务!");
				return;
			}
			var id = record.get('id');
			var isStart = record.get('isStart');
			if(isStart=='1'){
				var taskConWin = new OECP.Task.TaskConfigWindow({isEdit:true,isView:true,taskId:id});
				taskConWin.show();
			}else{
				var taskConWin = new OECP.Task.TaskConfigWindow({isEdit:true,isView:false,taskId:id});
				taskConWin.show();
			}
		},this);
		this.items = [master.grid,master.queryPanel];
		master.store.removeAll();
		master.store.load();
		OECP.Task.TaskView.superclass.initComponent.call(this);
	},
	/**
	 * 初始化表单
	 */
	initQueryPanel : function(){
		var master = this;
		var store = new Ext.data.JsonStore({
			url : master.listGroupUrl ,
			root : "result",
			fields : ['id','name','description'],
			remoteSort : false,
			timeout : 8000,
			totalProperty: 'totalCounts'
		});
		var comboPageNum = master.comboPageNum;
		var groupCombo = new Ext.form.ComboBox({
			columnWidth:0.52,
			hiddenName:'conditions[1].value',
			fieldLabel: '任务组',
		    store: store,
		    width:200,
		    displayField:'name',
		    valueField:'id',
		    typeAhead: true,
		    mode: 'remote',
		    triggerAction: 'all',
		    emptyText:'请选择任务组',
		    selectOnFocus:true,
		    pageSize:this.comboPageNum,
		    listeners : {
				blur : function(combo) {// 当其失去焦点的时候
					if (combo.getRawValue() == '') {
						combo.reset();
					}
				},
				beforequery : function(queryEvent) {
					var param = queryEvent.query;
					this.store.load({
						params : {
							limit : comboPageNum,
							'conditions[0].field':'name',
							'conditions[0].operator':'like',
							'conditions[0].value' : '%'+param+'%'
						},
						callback : function(r, options, success) {

						}
					});
					return true;
				}
			}
		});
		master.queryPanel = new Ext.FormPanel({
			height : 40,
			frame : true,
			region : 'north',
			layout : 'table',
			layoutConfig : {
				columns : 10
			},
			items : [
						{text : '任务名称:',xtype : 'label',style:'font-size:11px'},
						{name:'conditions[0].value',xtype:'textfield',listeners:{focus:function(){this.selectText();}}},
						{name:'conditions[0].field',value:'name',hidden:true,xtype:'textfield'},
						{name:'conditions[0].operator',value:'like',hidden:true,xtype:'textfield'},
						{name:'conditions[1].field',value:'oecpTaskGroup.id',hidden:true,xtype:'textfield'},
						{name:'conditions[1].operator',value:'=',hidden:true,xtype:'textfield'},
						{text : '任务组:',xtype : 'label',style:'font-size:11px'},
						groupCombo,
						master.queryBtn					
			]
		});
	},
	query : function(master){
		var values = master.queryPanel.getForm().getValues();
		if(Ext.isEmpty(values['conditions[0].value'])&&Ext.isEmpty(values['conditions[1].value'])){
			master.store.load();
		}else{
			master.store.load({
				params : values
			});
		}
	}
});