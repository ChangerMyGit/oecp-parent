// 功能UI编辑窗口
Ext.ns('OECP.bc.func');
OECP.bc.func.FunctionUIEditForm = Ext.extend(Ext.Window, {
	formPanel : null,
	uiElementGrid : null,
	isEdit : false,
	layout : 'fit',
	maximizable : true,
	modal : true,
	border : false,
	title : '功能界面',
	y : 10,
	height : 390,
	width : '90%',
	minHeight : 388,
	minWidth : 1100,
	autoScroll : true,
	buttonAlign : 'center',
	initComponent : function() {
		var scope = this;
		this.addEvents('functionuisaved');
		this.uiElementGrid = new OECP.bc.func.UIElementGrid({
					functionUiId : this.functionUiId,
					isEdit : this.isEdit
				});
		this.formPanel = new Ext.form.FormPanel({
			layout : 'form',
			bodyStyle : 'padding:10px 10px 10px 10px',
			autoScroll : true,
			border : false,
			defaults : {
				anchor : '98%,98%'
			},
			url : __ctxPath + '/function/saveFunctionUI.do',
			id : 'FunctionUIEditFormPanel',
			defaultType : 'textfield',
			reader : new Ext.data.JsonReader({
						root : 'result',
						successProperty : 'success'
					}, [{
								name : 'functionUI.id',
								mapping : 'id'
							}, {
								name : 'functionUI.function.id',
								mapping : 'function.id'
							}, {
								name : 'functionUI.code',
								mapping : 'code'
							}, {
								name : 'functionUI.name',
								mapping : 'name'
							}, {
								name : 'functionUI.description',
								mapping : 'description'
							}, {
								name : 'functionUI.sign',
								mapping : 'sign'
							}, {
								name : 'functionUI.isDefault',
								mapping : 'isDefault'
							}, {
								name : 'functionUI.isDefaultForProcess',
								mapping : 'isDefaultForProcess'
							}]),
			items : [{
						name : 'functionUI.id',
						dataIndex : 'functionUI.id',
						xtype : 'hidden',
						value : this.functionUiId || ''
					}, {
						name : 'functionUI.function.id',
						dataIndex : 'functionUI.function.id',
						xtype : 'hidden',
						value : this.functionId || ''
					}, {
						xtype : 'fieldset',
						width : 690,
						height : 130,
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
												fieldLabel : '界面编号',
												xtype : 'textfield',
												readOnly : !this.isEdit,
												name : 'functionUI.code',
												dataIndex : 'functionUI.code',
												allowBlank : false,
												blankText : '必须输入编号',
												width : 280
											}, {
												fieldLabel : '界面标识',
												xtype : 'textfield',
												readOnly : !this.isEdit,
												name : 'functionUI.sign',
												dataIndex : 'functionUI.sign',
												allowBlank : false,
												blankText : '必须输入标识',
												width : 280
											}, {
												fieldLabel : '功能描述',
												xtype : 'textfield',
												readOnly : !this.isEdit,
												width : 280,
												name : 'functionUI.description',
												dataIndex : 'functionUI.description'
											}]
								}, {
									xtype : 'container',
									layout : 'form',
									columnWidth : 0.4,
									items : [{
												fieldLabel : '名称',
												xtype : 'textfield',
												readOnly : !this.isEdit,
												width : 160,
												name : 'functionUI.name',
												dataIndex : 'functionUI.name'
											}, {
												fieldLabel : '默认界面',
												xtype : 'combo',
												readOnly : !this.isEdit,
												hiddenName : 'functionUI.isDefault',
												dataIndex : 'functionUI.isDefault',
												editable : false,
												allowBlank : false,
												blankText : '默认界面必须选择',
												mode : 'local',
												width : 160,
												triggerAction : 'all',
												value : false,
												store : [[false, '否'],
														[true, '是']]
											}, {
												fieldLabel : '流程默认界面',
												xtype : 'combo',
												readOnly : !this.isEdit,
												hiddenName : 'functionUI.isDefaultForProcess',
												dataIndex : 'functionUI.isDefaultForProcess',
												editable : false,
												allowBlank : false,
												blankText : '默认界面必须选择',
												mode : 'local',
												width : 160,
												triggerAction : 'all',
												value : false,
												store : [[false, '否'],
														[true, '是']]
											}]
								}]
					}, {
						xtype : 'fieldset',
						height : 280,
						width : 905,
						layout : 'fit',
						items : [scope.uiElementGrid]
					}]
		});
		this.on('beforeclose', function(b) {
					Ext.Msg.confirm('信息确认', '您确认要关闭窗口吗？', function(c) {
								if (c == 'yes') {
									scope.destroy();
								}
							});
					return false;
				});
		this.on('show', function() {
					// 加载数据
					if (this.functionUiId != null
							&& this.functionUiId != 'undefined') {
						this.formPanel.getForm().load({
							deferredRender : false,
							url : __ctxPath
									+ '/function/queryFuncUIsById.do?id='
									+ this.functionUiId,
							waitTitle : '提示',
							waitMsg : '正在加载数据请稍后...',
							method : 'GET',
							success : function(c, d) {
								var n = Ext.util.JSON
										.decode(d.response.responseText);
								var g = n.result[0];
								var l = g.BodyDetails;
								scope.uiElementGrid.getStore().loadData(l);
							},
							failure : function(c, d) {
								Ext.ux.Toast.msg('信息', '数据载入失败！');
							}
						});
					}
				});
		this.items = [this.formPanel];

		this.saveBtn = new Ext.Button({
					text : '保存',
					iconCls : 'btn-save',
					handler : scope.save.createDelegate(scope, [scope], this)
				});
		this.closeBtn = new Ext.Button({
					text : '关闭',
					iconCls : 'btn-cancel',
					handler : function() {
						scope.destroy();
					}
				});
		this.buttons = [this.saveBtn, this.closeBtn];
		OECP.bc.func.FunctionUIEditForm.superclass.initComponent.call(this);
	},
	save : function() {
		var scope = this;
		var bodyStore = this.uiElementGrid.getStore();// 表体
		var jsondata = [];
		var bodyCount = bodyStore.getCount();// 表头
		var k = false;
		for (var f = 0; f < bodyCount; f++) {
			var g = bodyStore.getAt(f);
			var e = g.data;
			jsondata.push(g.data);
		}
		if (this.formPanel.getForm().isValid()) {
			this.formPanel.getForm().submit({
						method : 'POST',
						params : {
							data : Ext.encode(jsondata)
						},
						waitMsg : '正在提交数据...',
						success : function(i, p) {
							Ext.ux.Toast.msg('操作信息', '成功保存信息！');
							scope.fireEvent('functionuisaved',this);
							scope.destroy();
						},
						failure : function(i, o) {
							Ext.ux.Toast.msg('操作信息', '保存失败！');
							scope.destroy();
						}
					});
		}
	}
});
