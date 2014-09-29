Ext.ns("OECP.BizComponent");

/**
 * 
 * @class OECP.BizComponent.BizComponentView
 * @extends OECP.ui.GridPanelView
 */

OECP.BizComponent.BizComponentView = Ext.extend(Ext.Panel, {
	id : "OECP.BizComponent.BizComponentView",
	/**
	 * 
	 * @type String 列表的标题
	 */
	title : '组件注册',
	/**
	 * 
	 * @type String列表的url
	 */
	listUrl : __fullPath + 'bcComponent/list.do',
	/**
	 * 
	 * @type String 删除的链接url
	 */
	deleteUrl : __fullPath + 'bcComponent/delete.do',
	/**
	 * 
	 * @type String 保存链接的url
	 */
	saveUrl : __fullPath + 'bcComponent/add.do',
	/**
	 * 
	 * @type String 根据id获得数据的url
	 */
	findUrl : __fullPath + 'bcComponent/find.do',
	/**
	 * 
	 * @type String 更新的url
	 */
	updateUrl : __fullPath + 'bcComponent/update.do',
	/**
	 * 
	 * @type String 保存action的对象名称
	 */
	objectName : 'bizComponent',
	bcInfoUrl : __fullPath + 'bcComponent/getBcInfo.do',
	initBcInfoUrl : __fullPath + 'bcComponent/initBcInfo.do',
	connectionUrl : __fullPath + 'bcComponent/connection.do',
	width : '99%',
	// autoWidth:true,
	height : 550,
	/**
	 * 
	 * @type 列表数据项
	 */
	filedDate : ['id', 'code', 'name', 'host', 'servicePort', 'webPort',
			'contextPath', 'dbType', 'dbIp', 'dbPort', 'dbUser', 'dbPwd',
			'discription', 'initNum', 'isConnection','displayOrder'],
	selectCheckboxModel : false,// 设置复选框还是单选，默认false多选。
	/**
	 * 
	 * @type列表及新增页面的数据结构,主要包含下面一些参数： header : "主键", dataIndex : "id", hidden :
	 *                               true, //是否需要在列表中显示 unShowAble:true
	 *                               //是否需要在添加、编辑页面显示，默认 为false，即显示
	 *                               sortable:true, //是否按该字段排序 required:true,
	 *                               //是否必填 type:'string' //该字段的类型
	 *                               renderer:function(){} //特殊显示时的处理方法
	 * 
	 */
	structure : [],
	layout : 'fit',
	grid : null,
	queryWindow : null,

	// 初始化方法
	initComponent : function() {
		var master = this;
		// 初始化数据仓库
		master.store = new Ext.data.JsonStore({
					storeId : 'id',
					root : 'result',
					url : master.listUrl,
					totalProperty : "totalCounts",
					autoLoad : true,
					fields : master.filedDate
				});
		// 初始化按钮
		var toolBar = new Ext.Toolbar({
			items : [{
						text : '添加',
						tooltip : '添加一条新的记录',
						iconCls : 'add-user',
						listeners : {
							'click' : function(btn, e) {
								master.doEdit("增加界面", 0, null);

							}
						}
					}, new Ext.Toolbar.Separator(), {
						text : '查看/编辑',
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
									master.doEdit("修改界面", 1, rows[0].data.id);
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
									var recordes = Array();
									for (var i = 0, r; r = rows[i]; i++) {
										recordes.push(r.id);
									}
									master.deleteRecorde(recordes, rows,
											master.grid.getStore());
								}

							}
						}
					}, new Ext.Toolbar.Separator(), {
						text : '识别组件',
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
									master.getBcInfo(rows[0].data.id);
								}
							}
						}
					}, new Ext.Toolbar.Separator(), {
						text : '初始化',
						tooltip : '初始化',
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
									if (rows[0].data.initNum != 0) {
										Ext.Msg.confirm("信息确认",
												"组件已经初始化过是否要重新初始化？",
												function(b) {
													if (b == 'yes') {
														master
																.init(rows[0].data.id);
													}
												});
									} else {
										master.init(rows[0].data.id);
									}
								}
							}
						}
					}, new Ext.Toolbar.Separator(), {
						text : '组件对接',
						tooltip : '组件对接',
						iconCls : 'btn-refresh',
						listeners : {
							'click' : function(btn, e) {
								var rows = master.grid.getSelectionModel()
										.getSelections();
								if (rows.length <= 0) {
									Ext.Msg.alert("提示信息", "请选择一条记录！");
								} else if (rows.length > 1) {
									Ext.Msg.alert("提示信息", "请选择一条记录！");
								} else if (rows.length == 1) {
									master.bcConnection(rows[0].data.id);
								}
							}
						}

					}, new Ext.Toolbar.Separator(), {
						text : '刷新',
						tooltip : '刷新',
						iconCls : 'btn-refresh',
						listeners : {
							'click' : function(btn, e) {
								master.grid.getStore().removeAll();
								master.grid.getStore().reload();
							}
						}
					}]
		});
		// 初始化列模式
		var sm = new Ext.grid.CheckboxSelectionModel({
			singleSelect : master.selectCheckboxModel
				// handleMouseDown : Ext.emptyFn
			});
		master.structure = [sm, new Ext.grid.RowNumberer(), {
					header : '主键',
					dataIndex : 'id',
					hidden : true,
					unShowAble : true
				}, {
					header : '组件编号',
					dataIndex : 'code',
					width : 100,
					sortable : true,
					required : true,
					type : 'string'
				}, {
					header : '组件名',
					dataIndex : 'name',
					width : 100,
					sortable : true,
					required : true,
					type : 'string'
				}, {
					header : '主机地址',
					dataIndex : 'host',
					width : 150,
					sortable : true,
					required : false,
					type : 'string'
				}, {
					header : ' web service端口',
					dataIndex : 'servicePort',
					width : 100,
					sortable : true,
					required : true,
					type : 'string'
				}, {
					header : 'http web端口',
					dataIndex : 'webPort',
					width : 80,
					sortable : true,
					required : false,
					type : 'string'
				}, {
					header : '应用上下文路径',
					dataIndex : 'contextPath',
					width : 80,
					hidden : true,
					required : false,
					type : 'string'
				},
				{
					header : '数据源DAO',
					dataIndex : 'daoName',
					width : 80,
					hidden : true,
					required : false,
					// store :{xtype:'jsonstore',root:'result',url:__fullPath+'/report/setting/daonames.do',fields:['name'],autoLoad:true},
					type : 'combo'
				},
				/* {
					header : '数据库类型',
					dataIndex : 'dbType',
					width : 80,
					hidden : true,
					required : false,
					type : 'string'
				}, 
				{
					header : '数据库IP地址',
					dataIndex : 'dbIp',
					width : 80,
					hidden : true,
					required : false,
					type : 'string'
				}, {
					header : '数据库端口',
					dataIndex : 'dbPort',
					width : 80,
					hidden : true,
					required : false,
					type : 'string'
				}, {
					header : '数据库用户名',
					dataIndex : 'dbUser',
					width : 80,
					hidden : true,
					required : false,
					type : 'string'

				}, {
					header : '数据库密码',
					dataIndex : 'dbPwd',
					width : 80,
					hidden : true,
					required : false,
					type : 'string'
				},*/
				{
					header : '组件描述',
					dataIndex : 'discription',
					width : 80,
					hidden : true,
					required : false,
					type : 'bigstring'
				},{
					header :'菜单显示顺序',
					dataIndex:'displayOrder',
					width : 80,
					hidden : true,
					required : false,
					type : 'number'
				}, {
					header : '初始化次数',
					dataIndex : 'initNum',
					width : 80,
					hidden : true,
					required : false,
					unShowAble : true,
					type : 'number'
				}, {
					header : '是否对接',
					dataIndex : 'isConnection',
					width : 80,
					hidden : true,
					required : false,
					unShowAble : true,
					type : 'number'
				}]
		// 把列表编号，复选框放入列中
		var columns = master.structure;
		// columns.unshift(new Ext.grid.RowNumberer(), sm);
		// 初始化Grid
		master.grid = new Ext.grid.EditorGridPanel({
					store : master.store,
					cm : new Ext.grid.ColumnModel({
								columns : columns
							}),
					sm : sm,
					tbar : toolBar
				});
		this.items = [master.grid];
		OECP.ui.GridPanelView.superclass.initComponent.call(this);
	},
	/**
	 * s删除方法
	 * 
	 * @param {}
	 *            ids
	 * @param {}
	 *            rows
	 * @param {}
	 *            store
	 */
	deleteRecorde : function(ids, rows, store) {
		var master = this;
		Ext.Msg.confirm("信息确认", "您确认要删除所选组件信息吗？", function(b) {
					if (b == 'yes') {
						Ext.Ajax.request({
									url : master.deleteUrl,
									params : {
										ids : ids
									},
									method : "POST",
									success : function(res) {
										var msg = Ext.util.JSON
												.decode(res.responseText);

										if (msg.success) {
											Ext.ux.Toast.msg("信息", msg.msg);
											Ext.each(rows, function(row) {
														store.remove(row);
													});

										} else {
											Ext.Msg.show({
														title : "错误",
														msg : msg.msg,
														buttons : Ext.Msg.OK
													})
										}
									},
									failure : function(f,res) {
									    if(res){
										Ext.Msg.show({
											title : "错误",
											msg : res.result.msg,
											buttons : Ext.Msg.OK
										});
									    }else{
										Ext.Msg.show({
													title : "错误",
													msg : '提交失败，您的网络可能不通畅，请稍后再试。',
													buttons : Ext.Msg.OK
												});
									    }
									}

								});
					}

				});

	},
	/**
	 * 组件初始化
	 */
	init : function(id) {
		var master = this;
		Ext.Msg.wait('正在初始化，请稍候......', '提示');
		Ext.Ajax.request({
					url : master.initBcInfoUrl,
					params : {
						id : id
					},
					method : "POST",
					success : function(res) {
						Ext.Msg.hide();
						var msg = Ext.util.JSON.decode(res.responseText);
						if (msg.success) {
							master.grid.getStore().reload();
							Ext.ux.Toast.msg("信息", msg.msg);
						} else {
							Ext.Msg.show({
										title : "错误",
										msg : msg.msg,
										buttons : Ext.Msg.OK
									});
						}
					},
					failure : function(f,res) {
						Ext.Msg.hide();
						if(res){
							Ext.Msg.show({
								title : "错误",
								msg : res.result.msg,
								buttons : Ext.Msg.OK
							});
						    }else{
							Ext.Msg.show({
										title : "错误",
										msg : '提交失败，您的网络可能不通畅，请稍后再试。',
										buttons : Ext.Msg.OK
									});
						    }
					}

				});
	},
	/**
	 * 
	 * 组件对接
	 */
	bcConnection : function(id) {
		var master = this;
		Ext.Msg.wait('正在对接，请稍候......', '提示');
		Ext.Ajax.request({
					url : master.connectionUrl,
					params : {
						id : id
					},
					method : "POST",
					success : function(res) {
						Ext.Msg.hide();
						var msg = Ext.util.JSON.decode(res.responseText);
						if (msg.success) {
							Ext.ux.Toast.msg("信息", msg.msg);
						} else {
							Ext.Msg.show({
										title : "错误",
										msg : msg.msg,
										buttons : Ext.Msg.OK
									});
						}
					},
					failure : function(f,res) {
						Ext.Msg.hide();
						if(res){
							Ext.Msg.show({
								title : "错误",
								msg : res.result.msg,
								buttons : Ext.Msg.OK
							});
						    }else{
							Ext.Msg.show({
										title : "错误",
										msg : '提交失败，您的网络可能不通畅，请稍后再试。',
										buttons : Ext.Msg.OK
									});
						    }
					}

				});
	},
	/**
	 * 获取组件详细信息
	 */
	getBcInfo : function(id) {
		var master = this;
		Ext.Msg.wait('正在获取组件详细信息，请稍候......', '提示');
		Ext.Ajax.request({
					url : master.bcInfoUrl,
					params : {
						id : id
					},
					method : "POST",
					success : function(res) {
						Ext.Msg.hide();
						var msg = Ext.util.JSON.decode(res.responseText);
						if (msg.success) {
							Ext.ux.Toast.msg("信息", msg.msg);
							master.grid.getStore().reload();
						} else {
							Ext.Msg.show({
										title : "错误",
										msg : msg.msg,
										buttons : Ext.Msg.OK
									});
						}
					},
					failure : function(f,res) {
						Ext.Msg.hide();
						if(res){
							Ext.Msg.show({
								title : "错误",
								msg : res.result.msg,
								buttons : Ext.Msg.OK
							});
						    }else{
							Ext.Msg.show({
										title : "错误",
										msg : '提交失败，您的网络可能不通畅，请稍后再试。',
										buttons : Ext.Msg.OK
									});
						    }
					}

				});
	},

	// ----------------------处理编辑、增加操作-------------------------------
	/**
	 * 处理添加和编辑方法
	 * 
	 * @param {}
	 *            title
	 * @param {}
	 *            action
	 * @param {}
	 *            id
	 */
	doEdit : function(title, action, id) {
		var master = this;
		var saveUrl = master.saveUrl;
		var updateUrl = master.updateUrl;
		var findUrl = master.findUrl;
		var structure = master.structure;
		var objectName = master.objectName;
		var buttonText = ""; // 按钮名称“保存”或者“更新”
		if (action == 0) {
			buttonText = "保存";
		} else {
			buttonText = "更新";
		}

		var oField = new Array();//
		var readerArray = new Array();
		for (var i = 2; i < structure.length; i++) {
			var column = structure[i];
			column.type = column.type || 'string'; // 设置默认类型为String
			column.unShowAble = column.unShowAble || false; // 设置默认为false，即默认显示

			// 根据不同的类型设置界面显示
			switch (column.type) {
				case 'string' :
					oField[oField.length] = {
						xtype : 'textfield',
						id : objectName + '.' + column.dataIndex,
						mapping : column.dataIndex,
						fieldLabel : column.header,
						anchor : '70%',
						hidden : column.unShowAble,
						allowBlank : column.required ? false : true,
						blankText : column.header + "不能为空",
						rederer : function(value, metaData, record, rowIndex,
								colIndex, store) {
							return "";
						}
					}
					break;
				case 'bigstring' :
					oField[oField.length] = {
						xtype : 'textarea',
						id : objectName + '.' + column.dataIndex,
						mapping : column.dataIndex,
						fieldLabel : column.header,
						anchor : '70%',
						hidden : column.unShowAble,
						allowBlank : column.required ? false : true,
						blankText : column.header + "不能为空",
						rederer : function(value, metaData, record, rowIndex,
								colIndex, store) {
							return "";
						}
					}
					break;
				case 'number' :
					oField[oField.length] = {
						xtype : 'numberfield',
						id : objectName + '.' + column.dataIndex,
						mapping : column.dataIndex,
						fieldLabel : column.header,
						anchor : '70%',
						hidden : column.unShowAble,
						allowBlank : column.required ? false : true,
						blankText : column.header + "不能为空"
					}
					break;
				case 'date' :
					oField[oField.length] = {
						xtype : 'datefield',
						id : objectName + '.' + column.dataIndex,
						mapping : column.dataIndex,
						fieldLabel : column.header,
						format : 'Y-m-d H:i:s',
						anchor : '90%',
						hidden : column.unShowAble,
						allowBlank : column.required ? false : true,
						blankText : column.header + "不能为空"
					}
					break;
				case 'combo' : // 暂时未处理
					oField[oField.length] = {
						xtype : 'combo',
						mapping : column.dataIndex,
						fieldLabel : column.header,
						hidden : column.unShowAble,
						allowBlank : column.required ? false : true,
						blankText : "请选择" + column.header,
						store : {xtype:'jsonstore',root:'result',url:__fullPath+'/report/setting/daonames.do',fields:['name'],autoLoad:true},
						valueField : 'name',
						displayField : 'name',
						hiddenName : objectName + '.' + column.dataIndex,
						mode : column.mode,
						emptyText : '请选择',
						editable : false,
						triggerAction : "all",
						grow : false
					}
					break;
			};
			// 设置form的reader
			readerArray[readerArray.length] = {
				name : objectName + '.' + column.dataIndex,
				mapping : column.dataIndex
			}
		}
		// 初始化增加和编辑的form
		var editForm = new Ext.form.FormPanel({
					labelWidth : 80,
					frame : true,
					border : false,
					hideBorders : true,
					autoWidth : true,
					autoHeight : true,
					// layout:'fit',
					items : oField,
					reader : new Ext.data.JsonReader({
								root : 'result'
							}, readerArray),
					url : action == 0 ? saveUrl : updateUrl,
					buttons : [{
								iconCls : 'btn-save',
								text : buttonText,
								handler : function() {
									doSubmit();
								}
							}, {
								iconCls : 'btn-close',
								text : '关闭',
								handler : function() {
									addWin.hide();
									addWin.destroy();
								}
							}],
					buttonAlign : 'center'
				});
		// 如果为编辑时，加载form中的数据。
		if (action == 1) {
			editForm.getForm().load({
						deferredRender : false,
						url : findUrl,
						params : {
							id : id
						},
						waitMsg : "正在载入数据...",
						method : 'POST',
						success : function(c, d) {
						},
						failure : function(c, res) {
						    if(res){
							Ext.Msg.show({
								title : "错误",
								msg : res.result.msg,
								buttons : Ext.Msg.OK
							});
						    }else{
							Ext.Msg.show({
										title : "错误",
										msg : '提交失败，您的网络可能不通畅，请稍后再试。',
										buttons : Ext.Msg.OK
									});
						    }
						}
					});
		}
		// 初始化增加和编辑的窗口
		var addWin = new Ext.Window({
					title : title,
					labelWidth : 100,
					frame : true,
					autoHeight : true,
					// height : 450,
					width : 500,
					closable : true,
					resizable : true,
					modal : true,
					items : editForm
				});
		addWin.show();
		// form 提交方法
		function doSubmit() {
			if (editForm.form.isValid()) {
				editForm.getForm().submit({
							waitTitle : "请稍候",
							waitMsg : "正在提交表单数据，请稍候......",
							success : function(form, action) {
								if (action.result.success) {
									addWin.hide();
									addWin.destroy();
									master.grid.getStore().reload();
									Ext.ux.Toast.msg("信息", action.result.msg);
								} else {
									Ext.Msg.show({
												title : "错误",
												msg : action.result.msg,
												buttons : Ext.Msg.OK
											});
								}
							},
							failure : function(form , res) {
							    if(res){
								Ext.Msg.show({
									title : "错误",
									msg : res.result.msg,
									buttons : Ext.Msg.OK
								});
							    }else{
								Ext.Msg.show({
											title : "错误",
											msg : '提交失败，您的网络可能不通畅，请稍后再试。',
											buttons : Ext.Msg.OK
										});
							    }
							}
						});
			}
		}

	}
});
