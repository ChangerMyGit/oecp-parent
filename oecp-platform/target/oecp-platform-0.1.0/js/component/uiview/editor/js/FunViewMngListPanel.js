Ext.ns('OECP.ui.view');

/**
 * 功能视图管理的列表查看页面
 * 
 * @class OECP.ui.view.FunViewMngListPanel
 * @extends Ext.Panel
 */
OECP.ui.view.FunViewMngListPanel = Ext.extend(Ext.Panel, {
	layout : 'border',
	anchor : '0,0',
	initComponent : function() {
		scope = this;
		scope.assignWin = new OECP.uiview.FunViewAssignWindow();
		OECP.ui.view.FunViewMngListPanel.superclass.initComponent.call(this);
		this.initTree();
		this.initGrid();
		this.initButtons();
		this.initCloneWindow();
	},
	initTree : function() {
		scope = this;
		this.tree = new OECP.ui.AccordionTreePanel({
					width : 150,
					collapsible : false,
					region : 'west',
					title : '功能列表',
					accordionUrl : __ctxPath + '/function/listAllBCs.do',
					treeUrl : __ctxPath + '/function/fuctionTreeCode.do',
					treeEvent : {
						'click' : function(node) {
							if (node.id != 'root') {
								scope.updateGridData(node.id, node.text);
							}
							if(scope._win){
								scope._win[scope._win.closeAction]();
							}
						},
						'containercontextmenu' : function(comp, e) {
						},
						'contextMenu' : function(node, e) {
						}
					}
				});
		this.add(this.tree);
	},
	initGrid : function() {
		scope = this;
		this.listStore = new Ext.data.JsonStore({
					url : __ctxPath + '/funview/list.do',
					storeId : 'id',
					root : "result",
					totalProperty : "totalCounts",
					idProperty : "id",
					fields : [{
								name : "id",
								type : "string"
							}, {
								name : "viewcode",
								type : "string"
							}, {
								name : "viewname",
								type : "string"
							}, {
								name : "org.id",
								type : "string"
							}, {
								name : "org.name",
								type : "string"
							}, {
								name : "shared",
								type : "boolean"
							}, {
								name : "func.id",
								type : "string"
							}, {
								name : "func.code",
								type : "string"
							}]
				});
		this.grid = new Ext.grid.GridPanel({
					region : 'center',
					tbar : [],
					store : this.listStore,
					title : "视图列表",
					columns : [{
								header : "视图编号",
								dataIndex : "viewcode"
							}, {
								header : "视图名称",
								dataIndex : "viewname"
							}, {
								header : "创建公司",
								dataIndex : "org.name"
							}, {
								header : "是否共享",
								dataIndex : "shared"
							}]
				});
		this.grid.getSelectionModel().singleSelect = true;
		this.grid.getSelectionModel().on('selectionchange', function(m) {
					if(scope._win){
						scope._win[scope._win.closeAction]();
					}
					var data = m.getSelections();
					if (data.length > 0) {
						// 按钮状态控制，调试完成后放开注释掉的代码。
						data = data[0].data;
						if (data['org.id'] == curUserInfo.orgId) {
							scope.editbtn.setDisabled(false);
							scope.delbtn.setDisabled(false);
							scope.clonebtn.setDisabled(false);
							scope.previewbtn.setDisabled(false);
							scope.printbtn.setDisabled(false);
							scope.queryAssignbtn.setDisabled(false);
							scope.assignbtn.setDisabled(false);
						} else {
							scope.editbtn.setDisabled(true);
							scope.delbtn.setDisabled(true);
							scope.queryAssignbtn.setDisabled(true);
							scope.assignbtn.setDisabled(true);
							scope.clonebtn.setDisabled(false);
							scope.previewbtn.setDisabled(false);
							scope.printbtn.setDisabled(false);
						}
					} else {
						scope.clonebtn.setDisabled(true);
						scope.editbtn.setDisabled(true);
						scope.delbtn.setDisabled(true);
						scope.previewbtn.setDisabled(true);
						scope.printbtn.setDisabled(true);
						scope.queryAssignbtn.setDisabled(true);
						scope.assignbtn.setDisabled(true);
					}
				});
		this.add(this.grid);
	},
	initButtons : function() {
		var scope = this;
		this.editbtn = new OECP.ui.button.EditButton({
					disabled : true,
					handler : function() {
						scope.onEdit(scope);
					}
				});
		this.delbtn = new OECP.ui.button.DelButton({
					disabled : true,
					handler : function() {
						scope.onDel(scope);
					}
				});
		this.clonebtn = new Ext.Button({
					text : "复制视图",
					disabled : true,
					handler : function() {
						scope.onClone(scope);
					}
				});
		this.previewbtn = new Ext.Button({
					text : "预览视图",
					disabled : true,
					handler : function() {
						scope.onPreview(scope);
					}
				});
		this.printbtn = new Ext.Button({
			text : '打印分配',
			disabled : true,
			handler : function(){
				var sdata = scope.grid.getSelectionModel().getSelections();
				var _functionid= scope.listStore.lastOptions['params']['functionid'];
				var _params={viewId:sdata[0].id,functionid:_functionid};
				scope._win = new Ext.Window({
					title:'打印分配',
					items:[{
						xtype : 'lovcombo',
						store : {
							xtype : 'jsonstore',
							url : __ctxPath+'/funview/listPrintTemplate.do',
							root : 'result',
							autoLoad : true,
							baseParams : _params,
							fields : ['id','name'],
							listeners:{
								'load':function(stroe, records, options){
									Ext.Ajax.request({
										params: _params,
										url : __ctxPath+ '/funview/checkedPrintTemplate.do',
										success: function(response, opts) {
											var obj = Ext.decode(response.responseText);
											if(obj.success){
												var _combo = scope._win.items.get(0);
												var _val = '';
												for(var i=0; i< obj.result.length; i++){
													_val = _val + "," +obj.result[i]['id'];
												}
												_combo.setValue(_val.substring(1));
											}
										}
									});
								}
							}
						},
						valueField:'id',
						displayField:'name',
						hiddenName:'id',
						triggerAction:'all',
						mode:'remote'
					}],
					bbar:new Ext.Toolbar({
				        items : ['->', '-',{
							text : '保存',
							handler : function(){
								var sdata = scope.grid.getSelectionModel().getSelections();
								var _functionid= scope.listStore.lastOptions['params']['functionid'];
								var _printTemplateIds = scope._win.items.get(0).getValue();
								var _params={viewId: sdata[0].id, functionid: _functionid, printTemplateIds: _printTemplateIds};
								Ext.Ajax.request({
									url: __ctxPath + '/funview/savePrintTemplate.do',
									params: _params,
									success: function(response, opts) {
										var obj = Ext.decode(response.responseText);
										if(obj.success){
											scope._win[scope._win.closeAction]();
										} else {
											Ext.Msg.alert("错误","保存失败！");
										}
									},
									failure: function(response, opts) {
										Ext.Msg.alert("错误","保存失败！");
								   }
								});
							}
						}, '-',{
							text : '取消',
							handler : function(){
								scope._win[scope._win.closeAction]();
							}
						}]
					 })
				});
				scope._win.show();
			}
		});
		this.queryAssignbtn = new Ext.Button({
			text : "关联查询方案",
			disabled : true,
			handler : function() {
				// 关联查询方案
				scope.onQueryAssign(scope);
			}
		});
		this.assignbtn = new Ext.Button({
			text : "权限分配",
			disabled : true,
			handler : function() {
				var ui = scope.grid.getSelectionModel().getSelections();
				if (ui.length == 1) {
					scope.assignWin.show();
					scope.assignWin.bizType_store.baseParams.functionCode = ui[0].data['func.code'];
					scope.assignWin.bizType_store.load();

					scope.assignWin.bizType_sm.clearSelections();
					scope.assignWin.functionViewId = ui[0].data['id'];
					scope.assignWin.bizTypeId = '';
					scope.assignWin.idscheck();
				}
			}
		});
		this.grid.getTopToolbar().addButton(this.editbtn);
		this.grid.getTopToolbar().addButton(this.delbtn);
		this.grid.getTopToolbar().addButton(this.clonebtn);
		this.grid.getTopToolbar().addButton(this.previewbtn);
		this.grid.getTopToolbar().addButton(this.queryAssignbtn);
		this.grid.getTopToolbar().addButton(this.printbtn);
		this.grid.getTopToolbar().addButton(this.assignbtn);
	},
	initCloneWindow : function() {
		this.clonewin = new OECP.ui.view.FunViewCloneWindow();
	},
	updateGridData : function(funid, funtitle) {
		this.grid.setTitle('[' + funtitle + ']的视图');
		this.listStore.reload({
					params : {
						'functionid' : funid
					}
				});
	},
	// 编辑
	onEdit : function(scope) {
		var sdata = scope.grid.getSelectionModel().getSelections();
		scope.parentPanel.showEditView(scope.parentPanel, sdata[0].id);
	},
	// 删除
	onDel : function(scope) {
		var sdata = scope.grid.getSelectionModel().getSelections();
		doAjaxRequest(scope, __ctxPath + '/funview/del.do', {
					"viewvo.id" : sdata[0].id
				}, function() {
					this.listStore.remove(sdata[0]);
				});
	},
	// 复制
	onClone : function(scope) {
		var srcviewid = scope.grid.getSelectionModel().getSelections()[0].id;
		this.clonewin.fn = function(clonecfg) {
			doAjaxRequest(scope, __ctxPath + '/funview/clone.do', {
						"viewvo.id" : srcviewid,
						"viewvo.viewcode" : clonecfg.viewcode,
						"viewvo.viewname" : clonecfg.viewname,
						"viewvo.shared" : clonecfg.shared
					}, function(rs) {
						scope.listStore.reload();
					});
		};
		this.clonewin.show();
	},
	// 预览
	onPreview : function(scope) {
		var srcview = scope.grid.getSelectionModel().getSelections()[0];
		var formview, listview;
		var previewWin = new OECP.ui.view.PreviewWindow({
					srcview : srcview
				});
		previewWin.show();
	},
	// 关联查询方案
	onQueryAssign : function(scope){
		if(!scope.queryAssignWin){
			scope.queryAssignWin = new OECP.ui.view.QueryAssignWindow();
		}
		scope.queryAssignWin.setFuncView(scope.grid.getSelectionModel().getSelections()[0]);
		scope.queryAssignWin.show();
	}
});
/**
 * 关联查询方案窗体
 */
OECP.ui.view.QueryAssignWindow = Ext.extend(Ext.Window, {
	title:'关联查询方案',
	width : 600,
	height : 500,
	closeAction : 'hide',
	modal : true,
	layout : 'border',
	initComponent : function() {
		this.initTree();
		this.initListGrid();
		this.initButtons();
		this.items = [this.accordionTreePanel,this.querySchemePanel];
		OECP.ui.view.QueryAssignWindow.superclass.initComponent.call(this);
	},
	initButtons : function(){
		var scope = this;
		var saveBtn = new OECP.ui.button.BizSaveButton({handler:function(){
			// 保存查询方案关联关系
			doAjaxRequest(scope,__ctxPath + '/funviewassign/saveFuncViewQS.do'
			, {functionViewId:scope.funcViewId,querySchemeId:scope.querySchemeId}
			,function(data){
				scope[scope.closeAction]();
			});
		}});
		var cancelBtn = new OECP.ui.button.BizCloseButton({handler:function(){
			scope[scope.closeAction]();
		}});
		this.buttons = [saveBtn,cancelBtn];
	},
	setFuncView : function(funcView){
		// 设置功能视图id
		this.funcViewId = funcView?funcView.id:null;
		Ext.Ajax.request({
			scope :this,
			url : __ctxPath + '/funviewassign/getQS.do',
			params : {functionViewId:this.funcViewId},
			success : function(res) {
				var data = eval("(" + Ext.util.Format.trim(res.responseText) + ")");
				if (data.success) {
					if(data.result){
						this.querySchemeId = data.result.id;
					}else{
						this.querySchemeId = null;
					}
					this.selectDefaultRow();
				} else {
					Ext.Msg.show({
								title : "错误",
								msg : data.msg,
								buttons : Ext.Msg.OK
							});
				}
			},
			failure : function() {
				Ext.Msg.show({
							title : "错误",
							msg : data.result.msg,
							buttons : Ext.Msg.OK
						});
			}
		});
	},
	selectDefaultRow : function(){
		if(this.querySchemeId && this.qsgrid_store.data.length>0){
			this.qsgrid_store.data;
			var idx = this.qsgrid_store.findBy(function(record,id){
				return id == this.querySchemeId;
			},this);
			this.qsgrid_sm.selectRow(idx);
		}else{
			this.qsgrid_sm.clearSelections();
		}
	},
	initTree : function(){
		var scope = this;
		scope.accordionTreePanel = new OECP.ui.AccordionTreePanel({
			width : 150,
			title : '组件与功能',
			collapsible : false,
			accordionUrl : __ctxPath + '/function/listAllBCs.do',
			treeUrl : __ctxPath + '/function/fuctionTreeCode.do',
			treeEvent : {
				'click' : function(node) {
					scope.funcId = node.id;
					scope.qsgrid_store.baseParams.funcId = node.id;
					scope.qsgrid_store.load();
				}
			}
		});
	},
	initListGrid : function(){
		var scope = this;
		scope.qsgrid_store = new Ext.data.JsonStore({
			url : __ctxPath + '/query/setting/list.do',
			baseParams : {
				funcId : ""
			},
			root:'result',
			fields : ['id', 'code', 'name']
		});

		scope.qsgrid_sm = new Ext.grid.CheckboxSelectionModel({singleSelect:true});
		scope.qsgrid_cm = new Ext.grid.ColumnModel([scope.qsgrid_sm,
			new Ext.grid.RowNumberer(), {
				header : "主键",
				dataIndex : "id",
				hidden : true
			}, {
				header : "查询方案编号",
				dataIndex : "code"
			}, {
				header : "查询方案名称",
				dataIndex : "name"
			}]);
		scope.querySchemePanel = new Ext.grid.GridPanel({
			region : 'center',
			enableDD : false,
			enableDrag : false,
			autoScroll : true,
			sm : scope.qsgrid_sm,
			cm : scope.qsgrid_cm,
			store : scope.qsgrid_store
		});
		
		scope.qsgrid_sm.on('rowselect',function(sm,idx,record){
			scope.querySchemeId = record.id;
		},scope);
		
		scope.qsgrid_sm.on('rowdeselect',function(sm,idx,record){
			if(idx >= 0 && scope.querySchemeId == record.id){
				scope.querySchemeId = null;
			}
		},scope);
		scope.qsgrid_store.on('load',function(store,records,options){
			if(records.length>0){
				for(var i=0 ; i<records.length ;i++){
					if(records[i].id==scope.querySchemeId){
						scope.qsgrid_sm.selectRow(i);
					}
				}
			}
		});
	}
});
/**
 * 复制视图对话框
 * 
 * @class OECP.ui.view.FunViewMngListPanel
 * @extends Ext.Window
 */
OECP.ui.view.FunViewCloneWindow = Ext.extend(Ext.Window, {
			width : 300,
			height : 200,
			closeAction : 'hide',
			/**
			 * @cfg {Ext.FormPanel} form
			 */

			/**
			 * @type Boolean
			 */
			modal : true,

			/**
			 * @cfg {function) fn
			 */

			initComponent : function() {
				OECP.ui.view.FunViewCloneWindow.superclass.initComponent.call(this);
				this.form = new Ext.FormPanel({
							height : 200,
							items : [{
										fieldLabel : '视图编号',
										name : 'viewcode',
										allowBlank : false,
										xtype : 'textfield'
									}, {
										fieldLabel : '视图名称',
										name : 'viewname',
										allowBlank : false,
										xtype : 'textfield'
									}, {
										fieldLabel : '是否共享',
										name : 'shared',
										xtype : 'checkbox'
									}]
						});
				this.add(this.form);
				var scope = this;
				this.addButton({
							xtype : 'determinbtn'
						}, function() {
							scope.onBtnOk(scope);
						}, scope);
				this.addButton({
							xtype : 'closebtn'
						}, function() {
							scope.onBtnClose(scope);
						}, scope);
			},
			onBtnOk : function(scope) {
				if (scope.form.getForm().isValid()) {
					var values = scope.form.getForm().getValues();
					scope.fn.call(scope.form.getForm(), values);
					scope[scope.closeAction]();
				}
			},
			onBtnClose : function(scope) {
				scope[scope.closeAction]();
			}
		});
// 预览窗体
OECP.ui.view.PreviewWindow = Ext.extend(Ext.Window, {
			width : 800,
			height : 450,
			layout : 'card',
			modal : true,
			closeAction : 'close',
			viewid : undefined,
			buttonAlign : 'center',
			layoutConfig : {
				deferredRender : true
			},
			initComponent : function() {
				var me = this;
				OECP.ui.view.PreviewWindow.superclass.initComponent.call(this);
				this.setTitle('视图预览[' + this.srcview.data.viewname + ']');
				this.formradio = new Ext.form.Radio({
							name : 'view',
							boxLabel : '预览表单界面',
							checked : true
						});
				this.listradio = new Ext.form.Radio({
							name : 'view',
							boxLabel : '预览列表界面',
							xtype : 'radio'
						});
				this.formradio.on('check', function(a, b) {
							if (b)
								this.showForm();
							else
								this.showList();
						}, this);
				this.addButton(this.formradio);
				this.addButton(this.listradio);
				this.listviewPanel = new Ext.Panel({
							autoScroll : true,
							layout:'fit',
							frame : true
						});
				this.formviewPanel = new Ext.Panel({
							autoScroll : true,
							layout:'fit',
							frame : true
						});
				this.add(this.formviewPanel);
				this.add(this.listviewPanel);
			},
			showForm : function() {
				var scope =me= this;
				if (!scope.formcfg) {
					Ext.Ajax.request({
								url : __ctxPath + '/funview/formpreview.do',
								scope  : me,
								params : {
									"viewvo.id" : this.srcview.id
								},
								success : function(res) {
									var msg = Ext.decode(res.responseText);
									if (msg.success) {
										scope.formcfg = msg.result;
										scope.formviewPanel.add(scope.formcfg);
										scope.formviewPanel.doLayout();
										scope.getLayout().setActiveItem(0);
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
								}
							});
				} else {
					this.getLayout().setActiveItem(0);
				}
			},
			showList : function() {
				var scope=me= this;
				if (!scope.listcfg) {
					Ext.Ajax.request({
								url : __ctxPath + '/funview/listpreview.do',
								method : 'POST',
								scope  : me,
								params : {
									"viewvo.id" : this.srcview.id
								},
								success : function(res) {
									var msg = Ext.decode(res.responseText);
									if (msg.success) {
										scope.listcfg = msg.result;
										scope.listviewPanel.add(scope.listcfg);
										scope.listviewPanel.doLayout();
										scope.getLayout().setActiveItem(1);
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
								}
							});
				} else {
					this.getLayout().setActiveItem(1);
				}
			},
			show : function() {
				OECP.ui.view.PreviewWindow.superclass.show.call(this);
				// this.showList();
				this.showForm();
			}

		});
function doAjaxRequest(scope, url, params, callback) {
	Ext.Ajax.request({
				url : url,
				params : params,
				success : function(res) {
					var msg = eval("(" + Ext.util.Format.trim(res.responseText) + ")");
					if (msg.success) {
						Ext.ux.Toast.msg("信息", msg.msg);
						if (typeof callback == 'function') {
							callback.call(scope, msg);
						}
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
				}
			});
}