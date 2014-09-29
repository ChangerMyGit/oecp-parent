// 功能编辑窗口
Ext.ns('OECP.bc.func');
OECP.bc.func.FunctionEditForm = Ext.extend(Ext.Window, {
	formPanel : null,
	id : 'FunctionEditForm',
	layout : 'border',
	maximizable : true,
	modal : true,
	border : false,
	y : 10,
	height : 430,
	width : '80%',
	minHeight : 230,
	minWidth : 750,
	autoScroll : true,
	buttonAlign : 'center',
	isEdit : false,
	functionId : '',
	initComponent : function() {
		var scope = this;
		this.addEvents('saveSucceeded');
		this.title = '组件功能 - ' + (this.isEdit ? '编辑' : '查看');
		this.formPanel = new Ext.form.FormPanel({
			region : 'north',
			layout : 'form',
			height : 160,
			bodyStyle : 'padding:10px 10px 10px 10px',
			autoScroll : true,
			border : false,
			defaults : {
				anchor : '98%,98%'
			},
			url : __ctxPath + '/function/saveBCFunction.do',
			id : 'FunctionEditFormPanel',
			defaultType : 'textfield',
			reader : new Ext.data.JsonReader({
						root : 'result',
						successProperty : 'success'
					}, [{
								name : 'function.bc.id',
								mapping : 'bc.id'
							}, {
								name : 'function.parent.id',
								mapping : 'parent.id'
							}, {
								name : 'function.code',
								mapping : 'code'
							}, {
								name : 'function.name',
								mapping : 'name'
							}, {
								name : 'function.description',
								mapping : 'description'
							}, {
								name : 'function.runable',
								mapping : 'runable'
							}, {
								name : 'function.wsuserd',
								mapping : 'wsuserd'
							}, {
								name : 'function.displayOrder',
								mapping : 'displayOrder'
							}, {
								name : 'function.bizServiceForBpm',
								mapping : 'bizServiceForBpm'
							}, {
								name : 'function.mainEntity',
								mapping : 'mainEntity'
							}]),
			items : [{
						name : 'function.id',
						dataIndex : 'function.id',
						xtype : 'hidden',
						value : this.functionId || ''
					}, {
						name : 'function.bc.id',
						dataIndex : 'function.bc.id',
						xtype : 'hidden',
						value : this.bcId || ''
					}, {
						name : 'function.parent.id',
						dataIndex : 'function.parent.id',
						xtype : 'hidden',
						value : this.parentId || ''
					}, {
						xtype : 'fieldset',
						width : 690,
						height : 150,
						layout : 'column',
						bodyStyle : 'padding:12px;',
						defaults : {
							anchor : '98%,98%'
						},
						items : [{
							columnWidth : 0.6,
							xtype : 'container',
							layout : 'form',
							items : [{
										fieldLabel : '功能编号',
										xtype : 'textfield',
										readOnly : !this.isEdit,
										name : 'function.code',
										dataIndex : 'function.code',
										allowBlank : false,
										blankText : '必须输入编号',
										width : 280
									}, {
										fieldLabel : '可运行',
										xtype : 'combo',
										readOnly : !this.isEdit,
										hiddenName : 'function.runable',
										dataIndex : 'function.runable',
										editable : false,
										allowBlank : false,
										blankText : '可运行必须选择',
										mode : 'local',
										width : 160,
										triggerAction : 'all',
										value : false,
										store : [[false, '不可运行'], [true, '可运行']]
									}, {
										fieldLabel : '功能描述',
										xtype : 'textfield',
										readOnly : !this.isEdit,
										width : 280,
										name : 'function.description',
										dataIndex : 'function.description'
									}, {
										fieldLabel : '审批流',
										xtype : 'combo',
										readOnly : !this.isEdit,
										hiddenName : 'function.wsuserd',
										dataIndex : 'function.wsuserd',
										editable : false,
										allowBlank : false,
										blankText : '支持审批流必须选择',
										mode : 'local',
										width : 160,
										triggerAction : 'all',
										value : false,
										store : [[false, '不支持'], [true, '支持']],
										listeners:{
													//使用审批流时，流程调用业务服务展示
													select : function(){
					                					if(this.getValue())
					                						Ext.getCmp("bizServiceForBpm").show();
					                					else{
					                						Ext.getCmp("bizServiceForBpm").setValue("");
					                						Ext.getCmp("bizServiceForBpm").hide();
					                					}	
									                }
								        }
								}]
						}, {
							xtype : 'container',
							layout : 'form',
							columnWidth : 0.4,
							items : [{
										fieldLabel : '名称',
										xtype : 'textfield',
										readOnly : !this.isEdit,
										width : 280,
										name : 'function.name',
										dataIndex : 'function.name'
									}, {
										fieldLabel : '显示顺序',
										xtype : 'numberfield',
										readOnly : !this.isEdit,
										width : 280,
										name : 'function.displayOrder',
										dataIndex : 'function.displayOrder'
									}, {
										fieldLabel : '主实体',
										xtype : 'textfield',
										readOnly : !this.isEdit,
										width : 280,
										name : 'function.mainEntity',
										dataIndex : 'function.mainEntity'
									},{
										fieldLabel : '流程调用业务服务',
										id : 'bizServiceForBpm',
										xtype : 'textfield',
										readOnly : !this.isEdit,
										width : 280,
										hidden : true,
										name : 'function.bizServiceForBpm',
										dataIndex : 'function.bizServiceForBpm'
								 }]
						}]
					}]
		});

		// 添加按钮
		var addBtn = new OECP.ui.button.AddButton({
					disabled : true
				});
		// 编辑按钮
		var editBtn = new OECP.ui.button.EditButton({
					disabled : true
				});
		// 删除按钮
		var delBtn = new OECP.ui.button.DelButton({
					disabled : true
				});
		// 可控字段store
		var funfield_store = new Ext.data.JsonStore({
					url : __ctxPath + '/function/queryFunctionFieldsByFunId.do',
					baseParams : {
						id : scope.functionId
					},
					fields : ['id', 'name', 'dispName','className', 'function.id',
							'mdType.id', 'mdType.name', 'mdType.eoClassName']
				});
		// 可控字段sm
		var funfield_sm = new Ext.grid.CheckboxSelectionModel();
		// 可控字段cm
		var funfield_cm = new Ext.grid.ColumnModel([funfield_sm,
				new Ext.grid.RowNumberer(), {
					header : "字段名",
					dataIndex : "name"
				}, {
					header : "字段显示名",
					dataIndex : "dispName"
				},  {
					header : "字段类名",
					dataIndex : "className"
				}, {
					header : "主数据资源名称",
					dataIndex : "mdType.name"
				}, {
					header : "对应实体",
					dataIndex : "mdType.eoClassName"
				}]);

		// 可控字段列表面板
		this.funfield_grid = new Ext.grid.GridPanel({
					title : "功能可控数据权限字段",
					enableDD : false,
					region : 'center',
					enableDrag : false,
					autoScroll : true,
					sm : funfield_sm,
					cm : funfield_cm,
					store : funfield_store,
					tbar : [addBtn, editBtn, delBtn]
				});

		// 加载数据
		if (!Ext.isEmpty(this.functionId)) {
			this.formPanel.getForm().load({
				deferredRender : false,
				url : __ctxPath + '/function/queryBCFunctionById.do',
				params : {
					id : this.functionId
				},
				waitMsg : '正在载入数据...',
				method : 'GET',
				success : function() {
					var runable = scope.formPanel.getForm().getValues()['function.runable'];
					//使用审批流时，流程调用业务服务展示
					if(scope.formPanel.getForm().getValues()['function.wsuserd']=='true')
						Ext.getCmp("bizServiceForBpm").show();
					else{
						Ext.getCmp("bizServiceForBpm").setValue("");
						Ext.getCmp("bizServiceForBpm").hide();
					}	
					if(runable == 'true'){
							funfield_store.load();
					}
					if (runable == 'true' && scope.isEdit) {
						addBtn.setDisabled(false);
						editBtn.setDisabled(false);
						delBtn.setDisabled(false);
					} else {
						addBtn.setDisabled(true);
						editBtn.setDisabled(true);
						delBtn.setDisabled(true);
					}
				},
				failure : function(c, d) {
					Ext.ux.Toast.msg('信息', '数据载入失败！');
				}
			});
		}
		this.saveBtn = new Ext.Button({
					text : '保存',
					iconCls : 'btn-save',
					hidden : !this.isEdit,
					handler : function(a, b, c, d, e) {
						scope.save();
					}
				});
		this.closeBtn = new Ext.Button({
					text : '关闭',
					iconCls : 'btn-cancel',
					handler : function() {
						scope.destroy();
					}
				});
		this.buttons = [this.saveBtn, this.closeBtn];
		this.items = [this.formPanel, this.funfield_grid];
//		this.on('beforeclose', function(b) {
//					Ext.Msg.confirm('信息确认', '您确认要关闭窗口吗？', function(c) {
//								if (c == 'yes') {
//									b.destroy();
//								}
//							});
//					return false;
//				});

		// 主数据资源选择
		var mdResourceComboBox = new OECP.bc.func.MDResourceChooseComb({
					hiddenName : 'functionField.mdType.id',
					displayField : 'name',
					valueField : 'id',
					dataIndex : 'mdType.id',
					fieldLabel : '主数据资源',
					allowBlank : false,
					mapping : 'mdType.id'

				});

		// 新增、编辑窗口
		var functionFieldWindow = new OECP.fun.FormWindow({
					formItems : [{
								fieldLabel : 'id',
								name : 'functionField.id',
								dataIndex : 'id',
								mapping : 'id',
								hidden : true
							}, {
								fieldLabel : 'functionId',
								name : 'functionField.function.id',
								dataIndex : 'function.id',
								mapping : 'function.id',
								hidden : true
							}, {
								fieldLabel : '字段名称',
								name : 'functionField.name',
								dataIndex : 'name',
								mapping : 'name',
								allowBlank : false,
								maxLength : 20,
								minLength : 2
							}, {
								fieldLabel : '字段显示名称',
								name : 'functionField.dispName',
								dataIndex : 'dispName',
								mapping : 'dispName',
								allowBlank : false,
								maxLength : 20,
								minLength : 2
							},{
								fieldLabel : '字段显示名称',
								name : 'functionField.className',
								dataIndex : 'className',
								mapping : 'className',
								allowBlank : true,
								maxLength : 50,
								minLength : 3
							}, mdResourceComboBox],
					saveURL : __ctxPath + '/function/saveFunctionField.do'
				});
		functionFieldWindow.on('dataSaved', function(id) {
					funfield_store.load();
				});
		// 添加按钮事件
		addBtn.on('click', function() {
					functionFieldWindow.setTitle('增加');
					functionFieldWindow.show();
					functionFieldWindow.setFormData({
								'function.id' : scope.functionId
							});
				});
		editBtn.on('click', function() {
			if (funfield_sm.getCount() == 0) {
				Ext.ux.Toast.msg("信息", "请选择一条记录！");
			} else if (funfield_sm.getCount() > 1) {
				Ext.ux.Toast.msg("信息", "只能编辑一条记录！");
			} else {
				functionFieldWindow.setTitle('编辑');
				functionFieldWindow.show();
				var b = scope.funfield_grid.getSelectionModel().getSelections();
				functionFieldWindow.setFormData(b[0].data);
			}
		});
		delBtn.on('click', function() {
					if (funfield_sm.getCount() == 0) {
						Ext.ux.Toast.msg("信息", "请选择要删除的记录！");
						return;
					}
					var b = scope.funfield_grid.getSelectionModel().getSelections();
					var e = [];
					for (var i = 0; i < b.length; i++) {
						e.push(b[i].data.id);
					}
					Ext.Ajax.request({
								url : __ctxPath + '/function/deleteFunctionField.do',
								success : function(res) {
									var msg = eval("(" + Ext.util.Format.trim(res.responseText) + ")");
									if (msg.success) {
										Ext.ux.Toast.msg("信息", msg.msg);
										funfield_store.load();
									} else {
										Ext.Msg.show({
													title : "错误",
													msg : msg.msg,
													buttons : Ext.Msg.OK
												});
									}
								},
								failure : function() {
									Ext.Msg.show({
												title : "错误",
												msg : msg.result.msg,
												buttons : Ext.Msg.OK
											});
								},
								params : {
									functionFieldIds : e
								}
							});
				});
		OECP.bc.func.FunctionEditForm.superclass.initComponent.call(this);
	},
	// 定义保存方法
	save : function() {
		var j = this.formPanel;
		var t = this;
		if (j.getForm().isValid()) {
			j.getForm().submit({
						method : 'POST',
						waitMsg : '正在提交数据...',
						success : function(form, msg) {
							Ext.ux.Toast.msg('信息', msg.result.msg);
							j.getForm().getValues();
							t.fireEvent('saveSucceeded', j.getForm().getValues());
							t.destroy();

						},
						failure : function(form, msg) {
							Ext.Msg.show({
										title : '错误',
										msg : msg.result.msg,
										buttons : Ext.Msg.OK
									});
						}
					});
		}
	}
});
