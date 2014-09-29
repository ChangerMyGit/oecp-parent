Ext.ns('OECP.BizService');

OECP.BizService.BizServiceView = Ext.extend(Ext.Panel, {
	id : 'OECP.BizService.BizServiceView',
	title : '组件服务管理',
	layout : 'border',
	/**
	 * @cfg {OECP.ui.combobox.BCCombo} bcCombo 组件查询框
	 */
	/**
	 * @cfg {OECP.ui.MasterDetailPanel} bizServicePanel 服务展示列表
	 */
	/**
	 * @cfg {OECP.ui.MasterDetailEditPanel} editPanel 编辑界面
	 */
	initComponent : function() {
		var scope = this;
		if (!this.bcCombo) {
			this.bcCombo = new OECP.ui.combobox.BCCombo({
						fieldLabel : '组件',
						border : false
					});
		}
		if (!this.bizServicePanel) {
			this.bizServicePanel = new OECP.ui.MasterDetailPanel({
				region : 'center',
				headCheckBox : true,
				headDataUrl : __ctxPath + '/bizservice/getBizServiceByBcId.do',
				headPageSize : 25,
				headColumns : [{
							header : '主键',
							dataIndex : 'id',
							hidden : true
						}, {
							header : '组件PK',
							dataIndex : 'bc.id',
							hidden : true
						}, {
							header : '服务名称',
							dataIndex : 'serviceName'
						}, {
							header : '服务描述',
							dataIndex : 'description'
						}],
				headStoreFields : ['id', 'bc.id', 'description', 'serviceName'],
				bodyDataUrl : __ctxPath
						+ '/bizservice/getOperationByServiceId.do',
				bodyColumns : [{
							header : '主键',
							dataIndex : 'id',
							hidden : true
						}, {
							header : '方法名',
							dataIndex : 'operationName'
						}, {
							header : '功能描述',
							dataIndex : 'description'
						}],
				bodyStoreFields : ['id', 'operationName', 'description']
			});
		}
		if (!scope._form) {
			scope._form = new OECP.ui.MasterDetailEditPanel({
						height : 300,
						weight : 400,
						status : 'edit',
						modifiedVarName : 'service',
						detailVarName : 'operations',
						delVarName : 'operations',
						submitUrl : __ctxPath
								+ '/bizservice/addOrModifyServices.do',
						queryUrl : __ctxPath
								+ '/bizservice/queryServiceById.do',
						items : [{
									fieldLabel : '主键',
									xtype : 'textfield',
									dataIndex : 'id',
									name : 'id',
									mapping : 'id',
									hidden : true
								}, {
									fieldLabel : '组件主键',
									xtype : 'textfield',
									name : 'bc.id',
									dataIndex : 'bc.id',
									mapping : 'bc.id',
									hidden : true
								}, {
									fieldLabel : '服务名称',
									xtype : 'textfield',
									name : 'serviceName',
									dataIndex : 'serviceName',
									mapping : 'serviceName'
								}, {
									fieldLabel : '服务描述',
									xtype : 'textfield',
									name : 'description',
									dataIndex : 'description',
									mapping : 'description'
								}],
						bodyStore : new Ext.data.JsonStore({
									fields : ['id', 'service.id',
											'operationName', 'description']
								}),
						bodyColumns : [{
									header : '主键',
									dataIndex : 'id',
									editor : new Ext.form.TextField({
												defaultReadOnly : true
											}),
									hidden : true
								}, {
									header : '服务主键',
									dataIndex : 'service.id',
									editor : new Ext.form.TextField({
												defaultReadOnly : true
											}),
									hidden : true
								}, {
									header : '方法名',
									dataIndex : 'operationName',
									editor : new Ext.form.TextField()
								}, {
									header : '功能描述',
									dataIndex : 'description',
									editor : new Ext.form.TextField()
								}]
					});
			scope._form.on('loaddata', function(e) {
						var rows = scope.bizServicePanel.headGrid
								.getSelectionModel().getSelections();
						e.getForm().findField('bc.id')
								.setValue(rows[0].data['bc.id']);
					})
		}
		if (!scope._window) {
			scope._window = new Ext.Window({
				height : 300,
				width : 500,
				items : scope._form,
				closeAction : 'hide',
				buttons : [{
					text : '保存',
					listeners : {
						'click' : function(btn, e) {
							scope._form.doSubmit({
										success : function(request) {
											var json = Ext.util.JSON
													.decode(request.responseText);
											if (json.success) {
												Ext.ux.Toast
														.msg('信息', json.msg);
												scope.loadData();
												scope._window.hide();
											} else {
												Ext.Msg.show({
															title : '错误',
															msg : json.msg,
															buttons : Ext.Msg.OK
														});
											}
										},
										failure : function(request) {
											var json = Ext.util.JSON
													.decode(request.responseText);
											var result = json.msg;
											Ext.Msg.show({
														title : '错误',
														msg : result,
														buttons : Ext.Msg.OK
													});
										}
									});
						}
					}
				}, {
					'text' : '取消',
					listeners : {
						'click' : function(btn, e) {
							scope._window.hide();
						}
					}
				}]
			});
		};
		this.bizServicePanel.queryBtn.hidden = true;
		this.bizServicePanel.addBtn.on('click', function(btn, e) {
					var bcId = scope.bcCombo.getValue();
					if (!Ext.isEmpty(bcId)) {
						scope._form.setPanelState('add');
						scope._form.getForm().findField('bc.id')
								.setValue(scope.bcCombo.getValue());
						scope._window.show();
					} else {
						Ext.MessageBox.alert('错误', '请选择一条记录！');
					}
				});

		this.bizServicePanel.editBtn.on('click', function(btn, e) {
					var rows = scope.bizServicePanel.headGrid
							.getSelectionModel().getSelections();
					if (rows.length == 1) {
						scope.bizServicePanel.headGrid.getSelectionModel()
								.each(function(_record) {
											scope._form.setPanelState('edit');
											scope._form.doQuery({
														params : {
															id : _record.data.id
														}
													});
											scope._window.show();
										});
					} else {
						Ext.MessageBox.alert('错误', '请选择一条记录！');
					}
				})
		this.bizServicePanel.delBtn.on('click', function(btn, e) {
			var rows = scope.bizServicePanel.headGrid.getSelectionModel()
					.getSelections();
			if (rows.length == 1) {
				Ext.MessageBox.confirm("提示", "初始化服务将清除历史数据，是否继续？",
						function(btn) {
							if (btn === 'yes') {
								Ext.Ajax.request({
											url : __ctxPath
													+ '/bizservice/delServiceById.do',
											params : {
												id : rows[0].data.id
											},
											success : function(request) {
												var json = Ext.util.JSON
														.decode(request.responseText);
												if (json.success) {
													Ext.ux.Toast.msg('信息',
															json.msg);
												} else {
													Ext.Msg.show({
																title : '错误',
																msg : json.msg,
																buttons : Ext.Msg.OK
															})
												}
												scope.loadData();
											},
											failure : function(request) {
												var json = Ext.util.JSON
														.decode(request.responseText);
												var result = json.msg;
												Ext.Msg.show({
															title : '错误',
															msg : result,
															buttons : Ext.Msg.OK
														})
											}
										});
							}
						}, scope);
			} else {
				Ext.MessageBox.alert('错误', '请选择一条记录！');
			}

		});
		this.items = [new Ext.Panel({
			region : 'north',
			layout : 'column',
			items : [{
						layout : 'form',
						columnWidth : 1,
						border : false,
						items : [this.bcCombo]
					}, {
						xtype : 'button',
						text : '查询',
						handler : function() {
							var bcId = scope.bcCombo.getValue();
							if (Ext.isEmpty(bcId)) {
								Ext.MessageBox.alert('错误', '请选择组件');
							} else {
								scope.bizServicePanel.headGrid.getStore().baseParams['id'] = bcId;
								scope.bizServicePanel.headGrid.getStore()
										.load();
								scope.bizServicePanel.clearBodyStore();
							}
						}
					}, {
						xtype : 'label',
						text : ''
					}, {
						xtype : 'button',
						text : '初始化服务',
						handler : function() {
							var bcId = scope.bcCombo.getValue();
							if (Ext.isEmpty(bcId)) {
								Ext.MessageBox.alert('错误', '请选择组件');
								return;
							}
							Ext.MessageBox.confirm("提示", "初始化服务将清除历史数据，是否继续？",
									function(btn) {
										if (btn === 'yes') {
											Ext.MessageBox.wait('初始化中', '请等待');
											Ext.Ajax.request({
												timeout : 1000000,
												url : __ctxPath
														+ '/bizservice/initBizServices.do',
												params : {
													id : bcId
												},
												success : function(res) {
													Ext.MessageBox.hide();
													var msg = Ext.util.JSON
															.decode(res.responseText);
													if (msg.success) {
														Ext.ux.Toast.msg('信息',
																msg.msg);
														scope.loadData();
													} else {
														Ext.Msg.show({
															title : '错误',
															msg : msg.msg,
															buttons : Ext.Msg.OK
														})
													}
												},
												failure : function() {
													Ext.MessageBox.hide();
													Ext.Msg.show({
																title : '错误',
																msg : msg.result.msg,
																buttons : Ext.Msg.OK
															})
												}
											});
										}
									}, scope);
						}
					}],
			height : 32
		}), this.bizServicePanel];
		OECP.BizService.BizServiceView.superclass.initComponent.call(this);
	},
	loadData : function() {
		var scope = this;
		var bcId = scope.bcCombo.getValue();
		scope.bizServicePanel.headGrid.getStore().baseParams['id'] = bcId;
		scope.bizServicePanel.headGrid.getStore().load();
		scope.bizServicePanel.clearBodyStore();
		scope.bizServicePanel.reloadBodyStore();
	},
	onDestroy : function() {
		Ext.destroy(this._window);
	}
});