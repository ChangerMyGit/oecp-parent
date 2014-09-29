Ext.ns("OECP.BizDepend");

/**
 * 
 * @class OECP.BizDepend.BizDependView
 * @extends OECP.ui.GridPanelView
 */

OECP.BizDepend.BizDependView = Ext.extend(Ext.Panel, {
	id : 'OECP.BizDepend.BizDependView',
	/**
	 * 
	 * @type String 列表的标题
	 */
	title : '依赖管理',
	/**
	 * 
	 * @type String列表的url
	 */
	listUrl : __fullPath + 'bcDepend/list.do',
	/**
	 * 
	 * @type String 删除的链接url
	 */
	deleteUrl : __fullPath + 'bcDepend/delete.do',
	/**
	 * 
	 * @type String 保存链接的url
	 */
	saveUrl : __fullPath + 'bcDepend/add.do',
	/**
	 * 
	 * @type String 根据id获得数据的url
	 */
	findUrl : __fullPath + 'bcDepend/find.do',
	/**
	 * 
	 * @type String 更新的url
	 */
	updateUrl : __fullPath + 'bcDepend/update.do',
	/**
	 * 
	 * @type String 保存action的对象名称
	 */
	objectName : 'bizDepend',
	width : '99%',
	// autoWidth:true,
	height : 550,
	/**
	 * 
	 * @type 列表数据项
	 */
	filedDate : ['id', 'url', 'dependName_EN', 'dependName_CN', 'dependDesc','bc.id'],
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
	structure : null,
	layout : 'border',
	grid : null,
	queryWindow : null,
	selectCheckboxModel : false,// 设置复选框还是单选，默认false多选。
	bcPanel : null, //组件选择面板
	bcCombo : null,//组件选择框

	// 初始化方法
	initComponent : function() {
		var master = this;
		//组件选择面板
		master.bcCombo = new OECP.ui.combobox.BCCombo({
						fieldLabel : '组件',
						border : false
					});
		master.bcPanel = new Ext.Panel({
			region : 'north',
			layout : 'column',
			height : 32,
			items : [{
					layout : 'form',
					columnWidth : 1,
					border : false,
					items : [master.bcCombo]
				},{
					xtype : 'button',
					text : '查询',
					handler : function(){
							var bcId = master.bcCombo.getValue();
							if (Ext.isEmpty(bcId)) {
								Ext.MessageBox.alert('错误', '请选择组件');
							} else {
								master.store.removeAll();
								master.store.baseParams['id'] = bcId;
								master.store.reload();
							}
					}
				},{
					xtype : 'label',
					text : ''
				},{
					xtype : 'button',
					text : '初始化服务'
				}]
			
		});
		// 初始化数据仓库
		master.store = new Ext.data.JsonStore({
					storeId : 'id',
					root : 'result',
					url : master.listUrl,
					totalProperty : "totalCounts",
					autoLoad : false,
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
										var bcId = master.bcCombo.getValue();
										if(bcId == null){
											Ext.Msg.alert("提示信息", "请选择一个组件");
										}else{
											master.doEdit("增加界面", 0, null);
										}
									}
								}
							}, new Ext.Toolbar.Separator(), {
								text : '修改',
								tooltip : '修改选中的记录',
								iconCls : 'btn-edit',
								listeners : {
									'click' : function(btn, e) {
										var rows = master.grid
												.getSelectionModel()
												.getSelections();
										if (rows.length <= 0) {
											Ext.Msg.alert("提示信息", "请选择一条记录！");
										} else if (rows.length > 1) {
											Ext.Msg.alert("提示信息", "请选择一条记录！");
										} else if (rows.length == 1) {
											master.doEdit("修改界面", 1,
													rows[0].data.id);
										}
									}
								}
							}, new Ext.Toolbar.Separator(), {
								text : '删除',
								tooltip : '删除选中的的记录',
								iconCls : 'btn-del',
								listeners : {
									'click' : function(btn, e) {
										var rows = master.grid
												.getSelectionModel()
												.getSelections();
										if (rows.length == 0) {
											Ext.Msg.alert("提示信息", "请选择一条记录！");
										} else {
											var recordes = Array();
											for (var i = 0, r; r = rows[i]; i++) {
												recordes.push(r.id);
											}
											master.deleteRecorde(recordes,
													rows, master.grid
															.getStore());
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
		// 把列表编号，复选框放入列中
		
//		columns.unshift(new Ext.grid.RowNumberer(), sm);
		master.structure = [new Ext.grid.RowNumberer(),sm,{
				header : '主键',
				dataIndex : 'id',
				hidden : true,
				unShowAble : true
			}, {
				header : '组件依赖URL',
				dataIndex : 'url',
				width : 150,
				sortable : true,
				required : true,
				type : 'string'
			}, {
				header : '组件依赖英文名称',
				dataIndex : 'dependName_EN',
				width : 100,
				sortable : true,
				required : true,
				type : 'string'
			}, {
				header : '组件依赖中文名称',
				dataIndex : 'dependName_CN',
				width : 100,
				sortable : true,
				required : false,
				type : 'string'
			}, {
				header : '组件依赖描述',
				dataIndex : 'dependDesc',
				width : 200,
				sortable : true,
				required : true,
				type : 'bigstring'
			},{
				header : '业务组件主键',
				dataIndex : 'bc.id',
				hidden : true,
				unShowAble : true
			}]
		
		var columns = master.structure;
		
		// 初始化Grid
		master.grid = new Ext.grid.EditorGridPanel({
					region : 'center',
					store : master.store,
					cm : new Ext.grid.ColumnModel({
								columns : columns
							}),
					sm : sm,
					tbar : toolBar
				});
		this.items = [master.bcPanel,master.grid];
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
		Ext.Msg.confirm("信息确认", "您确认要删除所选记录吗？", function(b) {
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
									failure : function() {
										Ext.Msg.show({
													title : "错误",
													msg : '提交失败，您的网络可能不通畅，请稍后再试。',
													buttons : Ext.Msg.OK
												})
									}

								});
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
						anchor : '90%',
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
						store : new Ext.data.SimpleStore({
									fields : ['value', 'text'],
									data : column.store
								}),
						valueField : 'value',
						displayField : 'text',
						hiddenName : objectName + '.' + column.dataIndex,
						mode : column.mode,
						// emptyText : '请选择',
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
		oField[oField.length-1].value = master.bcCombo.getValue();;
		oField[oField.length-1].readOnly = true;
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
						failure : function(c, d) {
								Ext.Msg.show({
													title : "错误",
													msg : '提交失败，您的网络可能不通畅，请稍后再试。',
													buttons : Ext.Msg.OK
												})
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
							success : function(form,action) {
								if(action.result.success){
									addWin.hide();
									addWin.destroy();
									master.grid.getStore().reload();
									Ext.ux.Toast.msg("信息", action.result.msg);
								}else{
									Ext.Msg.show({title:"错误",msg:action.result.msg,buttons:Ext.Msg.OK});
								}
							},
							failure : function() {
								Ext.Msg.show({title:"错误",msg:'提交失败，您的网络可能不通畅，请稍后再试。',buttons:Ext.Msg.OK});
							}
						});
			}
		}

	}
});
