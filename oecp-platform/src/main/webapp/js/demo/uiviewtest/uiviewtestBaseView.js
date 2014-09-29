Ext.ns('OECPBC.demo.uiviewtest');
/**
 * 单据列表界面
 */
Ext.QuickTips.init();

OECPBC.demo.uiviewtest.uiviewtestBaseView = Ext.extend(Ext.Panel, {
	layout : 'border',
	//查询 重置按钮
	functionCode : null,
	billId : null,
	//单据信息
	billinfo : '',
	quickButton : null,
	replyButton : null,
	//审批
	auditButton : null,
	//编辑
	bizEditButton : null,
	//保存
	bizSaveButton : null,
	//打印
	printButton : null,
	//关闭 
	bizCloseButton : null,
	//查看流程图
	viewBPMButton : null,
	//提交审批
	commitButton : null,
	
	initComponent : function() {
		var scope = this;
		this.quickButton = new  OECP.ui.button.QueryButton();//查询
		this.replyButton = new  OECP.ui.button.ResetButton();//重置
		this.viewBPMButton = new OECP.ui.button.ViewProcessButton();//查看流程状态
		this.commitButton = new OECP.ui.button.SubmitAuditButton();//提交审批
		this.auditButton = new OECP.ui.button.AuditButton();//审批
		this.bizSaveButton = new OECP.ui.button.BizSaveButton();//保存
		this.bizCloseButton = new OECP.ui.button.BizCloseButton();//关闭
		this.bizEditButton = new OECP.ui.button.EditButton({hidden : true});//编辑
		this.printButton = new OECP.ui.button.PrintButton({hidden : true});//打印
		
		if(!this.topItem){
			//快速查询form
			this.topItem =  new Ext.FormPanel({
							layout : 'table',
							layoutConfig : {
								columns : 28
							},
							autoHeight : true,
							frame : true,
							//为子类提供的变量
							items:this._topItems
						});
			this.topItem.add(this.quickButton);
			this.topItem.add(this.replyButton);
		}
		
		//单据查询页面
		if (!this.billPanel) {
			this.billPanel = new OECP.ui.MasterDetailPanel({
				headStoreParams : {functionCode : scope.functionCode},
				region : 'center',
				headCheckBox : true,
				headDataUrl : scope.headDataUrl,
				headPageSize : 25,
				topItem : scope.topItem,
				headColumns : scope.headColumns,
				headStoreFields : scope.headStoreFields,
				bodyDataUrl : scope.bodyDataUrl,
				bodyColumns : scope.bodyColumns,
				bodyStoreFields : scope.bodyStoreFields
			});
			this.billPanel.headGrid.on('cellclick',function(grid, rowIndex, columnIndex, e){
				scope.billId = grid.getStore().getAt(rowIndex).get('id');
				//TODO 点击单据获取单据信息
				//scope.billinfo = grid.getStore().getAt(rowIndex).get('billsn');
				scope.changeButtonState(scope.billId,grid.getStore().getAt(rowIndex).get('state'));
			});
			this.billPanel.topToolbar.addButton(scope.viewBPMButton);
			this.billPanel.topToolbar.addButton(scope.commitButton);
			this.billPanel.topToolbar.addButton(scope.auditButton);
			
		}
		//单据管理界面
		if (!scope._form) {
			scope._form = new OECPBC.demo.uiviewtest.uiviewtestPanel();
		}
		//创建窗体
		if (!scope._window) {
			scope._window = new Ext.Window({
				autoHeight : true, 
				width : 800,
				items : scope._form,
				closeAction : 'hide',
				buttons : [scope.bizEditButton, scope.bizSaveButton,scope.printButton,scope.bizCloseButton]
			});
		};
		//启用按钮
		this.useButton();
		
		/**begin****控制权限按钮***add by yangtao**********/
		var buttons=[]; 
        Ext.ComponentMgr.all.each(function(cmp) { 
          if (cmp instanceof OECP.ui.Button) { 
              buttons.push(cmp); 
          } 
        }); 
		var btnPerArray = this.btnPermission;
		var flag = '0';
		for(var j=0;j<buttons.length;j++){
			for(var i=0;i<btnPerArray.length;i++){
				if(buttons[j].pid == btnPerArray[i]){
					buttons[j].hidden = true;
					flag = '1';
				}
			}
			if(flag=='0')
				buttons[j].hidden = false;
		}
		/**end******控制权限按钮***add by yangtao**********/
		
		this.items = [this.billPanel];
		OECPBC.demo.uiviewtest.uiviewtestBaseView.superclass.initComponent.call(this);
	},
	
	
	useButton : function(){
		var scope = this;
		//审批
		scope.auditButton.on('click',function(){
					Ext.Ajax.request({
						url : scope.auditUrl,
						params : {
							id : scope.billId
						},
						success : function(request) {
							var json = Ext.util.JSON
									.decode(request.responseText);
							if (json.success) {
								var auditForm = new OECP.bpm.PersonalTaskAuditView({counterSignRuleId:json.msg,funcKey:scope.functionCode,bizKey:scope.billId,billInfo:scope.billinfo});
								var executeTaskwin = new Ext.Window({
									    id : 'executeTaskwin',
										width : 620,
										height : 340,
										modal : true,
										autoScroll:true,
										closeAction : 'hide',
										listeners : {
											'hide' : function(){
												scope.billPanel.headGrid.store.reload();
											}
										},
										items : [auditForm]
								});
								executeTaskwin.show();
								
							} else {
								Ext.Msg.show({
											title : '错误',
											msg : json.msg,
											buttons : Ext.Msg.OK
										})
							}
						},
						failure : function(request) {
							Ext.Msg.show({title:"错误",msg:'您的网络可能不通畅，请稍后再试。',buttons:Ext.Msg.OK});
						}
				});
		});
		//提交审批
		scope.commitButton.on('click',function(){
					Ext.Ajax.request({
					url : scope.commitUrl,
					params : {
						id : scope.billId,
						functionCode : scope.functionCode
					},
					success : function(request) {
						var json = Ext.util.JSON
								.decode(request.responseText);
						if (json.success) {
							Ext.ux.Toast.msg('信息',
									json.msg);
							scope.billPanel.headGrid.store.reload();
						} else {
							Ext.Msg.show({
										title : '错误',
										msg : json.msg,
										buttons : Ext.Msg.OK
									})
						}
					},
					failure : function(request) {
						Ext.Msg.show({title:"错误",msg:'您的网络可能不通畅，请稍后再试。',buttons:Ext.Msg.OK});
					}
				});
		});
		//查看流程状态
		scope.viewBPMButton.on('click',function(){
			var rows = scope.billPanel.headGrid
						.getSelectionModel().getSelections();
				if (rows.length == 1) {
					scope.billPanel.headGrid.getSelectionModel()
							.each(function(_record) {
								var processInstanceHisView = new OECP.bpm.ProcessInstanceHisView({billKey:_record.data.id});
									var win = new Ext.Window({
										title : '流程实例历史',
										width : 885,
										height : document.body.clientHeight,
										autoScroll : true,
										modal : true,
										items : [processInstanceHisView]
									})
								win.show();
					});
				}else{
					Ext.MessageBox.alert('错误', '请选择一条记录！');
				}
				
			
		});
		
		
		//window编辑按钮
		scope.bizEditButton.on('click',function(){
			scope._form.doQuery({
				url : scope.editUrl,
				params : {
					id :scope.billId
				},
				success : function(response){
					var msg = Ext.util.JSON.decode(response.responseText);
					if(msg.success){
						scope.bizEditButton.hide();
						scope._form.setDefaultState(true);
					}else{
							Ext.Msg.show({
								title : '错误',
								msg : msg.msg,
								buttons : Ext.Msg.OK
							});
					}
				}
			});
			
		});
		//保存按钮
		scope.bizSaveButton.on('click',function(){
			if(scope.validate()){
				scope._form.doSubmit({
							params : {
								functionCode : scope.functionCode
							},
							success : function(request) {
								var json = Ext.util.JSON
										.decode(request.responseText);
								if (json.success) {
									Ext.ux.Toast
											.msg('信息', json.msg);
									scope._window.hide();
//									scope.billPanel.headGrid.store.reload();
//									scope.billPanel.bodyGrid.store.reload();
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
		});
		//关闭按钮
		scope.bizCloseButton.on('click',function(){
			scope._window.hide();
		});
		
		//双击按钮
		scope.billPanel.headGrid.on('rowdblclick',function(grid,index,e){
			scope.billId = grid.getStore().getAt(index).get('id');
			scope._form.doQuery({
						url : scope.queryUrl,
						params : {
							id : scope.billId
						}
					});
			scope._window.show();
			scope.bizEditButton.show();
			scope._form.setDefaultState(false);
			
		});
		//添加按钮
		this.billPanel.addBtn.on('click', function(btn, e) {
					scope.bizEditButton.hide();
					scope.onDestroy();
					scope._form.setDefaultState(true);
					scope._window.show();
		});
		//编辑按钮
		this.billPanel.editBtn.on('click', function(btn, e) {
				var rows = scope.billPanel.headGrid
						.getSelectionModel().getSelections();
				if (rows.length == 1) {
					scope.billPanel.headGrid.getSelectionModel()
							.each(function(_record) {
										scope.bizEditButton.hide();
										scope._form.setDefaultState(true);
										scope._form.setPanelState('edit');
										scope._form.doQuery({
													url : scope.editUrl,
													params : {
														id : _record.data.id
													},
													success : function(request){
														var json = Ext.util.JSON
																.decode(request.responseText);
														if(!json.success){
																scope._form.setDefaultState(false);
																Ext.Msg.show({
																	title : '错误',
																	msg : json.msg,
																	buttons : Ext.Msg.OK
																});
														}else{
															scope._form.setValues(json.result);
														}
														}
												});
												scope._window.show();
									});
				} else {
					Ext.MessageBox.alert('错误', '请选择一条记录！');
				}
			});
			
		//刪除按鈕
		this.billPanel.delBtn.on('click', function(btn, e) {
				var rows = scope.billPanel.headGrid.getSelectionModel()
						.getSelections();
				if (rows.length == 1) {
					Ext.MessageBox.confirm("提示", "是否确认要删除",
							function(btn) {
								if (btn === 'yes') {
									Ext.Ajax.request({
												url : scope.headDelUrl,
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
													scope.billPanel.headGrid.store.removeAll();
													scope.billPanel.headGrid.store.reload();
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
		//快速查询按钮
		this.quickButton.on('click',function(){
			scope.billPanel.headGrid.store.removeAll();
				scope.billPanel.headGrid.store.baseParams = scope.topItem.getForm().getValues();
				scope.billPanel.headGrid.store.baseParams['functionCode']=scope.functionCode;
				scope.billPanel.headGrid.store.load({
						method : 'post',
						failure : function(form,action){
							Ext.Msg.show({title:"错误",msg:'您的网络可能不通畅，请稍后再试。',buttons:Ext.Msg.OK});
						}
					});
		});
		//重置按钮
		this.replyButton.on('click',function(){
			scope.topItem.getForm().reset();
		});
		
		
	},
// 結束 按钮启用
		
	/*
	 * 销毁窗体
	 */
	onDestroy : function() {
		this._form.bodyStore.removeAll();
		this._form.getForm().reset();
	},
	/*
	 * 校验数据
	 */
	validate : function(){
			var scope = this;
			if(!scope._form.getForm().isValid()){
				return false;
			}
			if(scope._form.bodyGrid.store.getCount() <= 0){
				Ext.Msg.show({
							title : '错误',
							msg : '无子表数据',
							buttons : Ext.Msg.OK
						});
				return false;
			}
			//校验从表内容填的是否准确
			for(var i = 0; i < scope._form.bodyStore.getCount(); i++){
				var record = scope._form.bodyStore.getAt(i);
				var fields = record.fields.keys;
				for(var j = 0; j < fields.length-1; j++){
					var name = fields[j];
					var value = record.data[name];
					var colIndex = scope._form.bodyGrid.getColumnModel().findColumnIndex(name);
					if(Ext.isDefined(colIndex) && colIndex != -1){
						var rowIndex =scope._form.bodyStore.indexOfId(record.id);
						if(Ext.isDefined(rowIndex) && rowIndex != -1){
							var editor = scope._form.bodyGrid.getColumnModel().getCellEditor(colIndex).field;
							if(! editor.validateValue(value)){
								Ext.Msg.alert('提示','<'+scope._form.bodyGrid.getColumnModel().getColumnHeader(colIndex)+'>必须输入',function(){
									scope._form.bodyGrid.startEditing(rowIndex,colIndex);
								});
								return false;
							}
						}
					}
				}
			}
			return true;
	},
	//流程按钮状态
	changeButtonState : function(billId,billState){
		var scope = this;
		if('EDIT' == billState){
			scope.commitButton.setDisabled(false);
			scope.viewBPMButton.setDisabled(true);
			scope.auditButton.setDisabled(true);
		}else if('BPMING' == billState){
			scope.commitButton.setDisabled(true);
			scope.viewBPMButton.setDisabled(false);
			scope.auditButton.setDisabled(false);
		}
		else if('EFFECTIVE' == billState){
			scope.commitButton.setDisabled(true);
			scope.viewBPMButton.setDisabled(false);
			scope.auditButton.setDisabled(true);
		}
		else if('INVALID' == billState){
			scope.commitButton.setDisabled(true);
			scope.viewBPMButton.setDisabled(false);
			scope.auditButton.setDisabled(true);
		}

	}
	
	

});