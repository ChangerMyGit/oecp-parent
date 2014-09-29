Ext.ns('OECP.ui');
/**
 * @author chengzhengliang
 * @class OECP.ui.GridPanelView
 * @extends Ext.Panel { 实现单表的增、删、改的封装--在Grid中） 。 包含基本功能：
 *          查询、添加、保存、刷新、导出（需要引入“gridToExcel.js”）。
 * 
 * 目前支持 string 、number、date、combo
 * 
 * 例子及说明：
 * -----------------------------------------代码参数说明---------------------------------------------------
 * var panel = new OECP.ui.GridPanelView({ title:'测试列表', objectName:'test',
 * //action中的eo对象名 listUrl:"http://localhost:8090/test/list.do", // 列表的url
 * deleteUrl:"http://localhost:8090/test/deleteTests.do", // 删除数据的url
 * saveUrl:"http://localhost:8090/test/save.do", // 保存数据的url
 * filedDate:[],//列表中字段及类型对应。 queryFiles:[], //查询条件参数 structure[] //数据结构的参数设置
 * });
 */

/**
 * -----------------------------------------代码使用举例----------------------------------------------------
 * 
 * <pre><code>
 * var panel = new OECP.ui.GridPanelView({
 * 			title : '测试列表',
 * 			objectName : 'test',
 * 			listUrl : __fullPath + 'test/list.do',
 * 			deleteUrl : __fullPath + 'test/deleteTests.do',
 * 			saveUrl : __fullPath + 'test/save.do', //批量保存
 * 			filedDate : ['id', 'czl_level', 'levelName', 'sumWay', 'amount',
 * 					'isDefaultLevel', 'description', {
 * 						name : 'addDate',
 * 						type : 'date',
 * 						dateFormat : 'Y-m-d H:i:s'
 * 					}],
 * 			queryFiles : [['czl_level', '等级'], ['levelName', '等级名称'],
 * 					['sumWay', '累计方式'], ['amount', '累计金额'],
 * 					['isDefaultLevel', '是否默认等级'], ['description', '描述']],
 * 			structure : [{
 * 				header : '主键',
 * 				dataIndex : 'id',
 * 				hidden : true, //是否需要在列表中显示
 * 				unShowAble : true
 * 					//是否需要在添加、编辑页面显示，默认	为false，即显示					
 * 				}, {
 * 				header : '会员等级',
 * 				dataIndex : 'czl_level',
 * 				width : 70,
 * 				sortable : true,
 * 				required : true,
 * 				type : 'string'
 * 
 * 			}, {
 * 				header : '会员等级名称',
 * 				dataIndex : 'levelName',
 * 				width : 100,
 * 				sortable : true
 * 			}, {
 * 				header : '累计方式',
 * 				dataIndex : 'sumWay',
 * 				width : 80,
 * 				sortable : true
 * 			}, {
 * 				header : '累计金额',
 * 				dataIndex : 'amount',
 * 				width : 100,
 * 				sortable : true,
 * 				type : 'number'
 * 			}, {
 * 				header : '是否默认等级',
 * 				dataIndex : 'isDefaultLevel',
 * 				width : 100,
 * 				sortable : true,
 * 				type : 'combo', //combo
 * 				mode : 'local', //本地模式
 * 				store : [[false, '否'], [true, '是']]
 * 					//数据。
 * 
 * 				}, {
 * 				header : '描述',
 * 				dataIndex : 'description',
 * 				width : 100,
 * 				sortable : true
 * 			}, {
 * 				header : '添加时间',
 * 				dataIndex : 'addDate',
 * 				width : 120,
 * 				sortable : true,
 * 				type : 'date'
 * 			}]
 * 		});
 * 
 */

OECP.ui.GridPanelView = Ext.extend(Ext.Panel, {
	// id : 'GridPanelView',
	/**
	 * 列表名称
	 * 
	 * @type String
	 */
	title : '',
	/**
	 * 列表url
	 * 
	 * @type String
	 */
	listUrl : '',
	/**
	 * 删除的url
	 * 
	 * @type String
	 */
	deleteUrl : '',
	/**
	 * 保存链接的url
	 * 
	 * @type String
	 */
	saveUrl : '',
	/**
	 * 保存action的对象名称
	 * 
	 * @type String
	 */
	objectName : '',
	width : '99%',
	// autoWidth:true,
	height : 550,
	/**
	 * 
	 * @type
	 */
	filedDate : [],
	/**
	 * 数据的结构
	 * 
	 * @type
	 */
	structure : [],
	/**
	 * 查询条件
	 * 
	 * @type
	 */
	queryFiles : [],
	layout : 'fit',
	grid : null,
	queryWindow : null,
	selectCheckboxModel : false,// 设置复选框还是单选，默认false多选。
	// 初始化方法
	initComponent : function() {
		var master = this;
		master.store = new Ext.data.JsonStore({
					storeId : 'id',
					root : 'result',
					url : master.listUrl,// 
					totalProperty : "totalCounts",
					autoLoad : true,
					fields : master.filedDate
				});
		// 初始化按钮
		var toolBar = new Ext.Toolbar({
					items : [{
								text : '查询',
								tooltip : '查询',
								iconCls : 'search',
								listeners : {
									'click' : function(btn, e) {
										master.query(master);
									}
								}
							}, {
								text : '添加',
								tooltip : '添加一条新的记录',
								iconCls : 'add-user',
								listeners : {
									'click' : function(btn, e) {
										master.doClick();

									}
								}
							}, new Ext.Toolbar.Separator(), {
								text : '保存',
								tooltip : '保存该页中所有修改或新增的数据',
								iconCls : 'btn-save',
								listeners : {
									'click' : function(btn, e) {
										master.doSave();
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
										master.grid.getStore().reload();
									}
								}
							}, new Ext.Toolbar.Separator(), {
								text : '导出',
								tooltip : '导出',
								iconCls : 'export',
								listeners : {
									'click' : function(btn, e) {
										master.exportToExcel(master.grid);
									}
								}
							}]

				});
		var sm = new Ext.grid.CheckboxSelectionModel({
			singleSelect : master.selectCheckboxModel
				// handleMouseDown : Ext.emptyFn
			});

		var columns = master.initEditColumns();// 初始化列
		columns.unshift(new Ext.grid.RowNumberer(), sm);
		// 初始化表格--
		master.grid = new Ext.grid.EditorGridPanel({
					frame : true,
					stripeRows : true, // 隔行变色，区分表格行
					clicksToEdit : 2, // 表示点击多少次数才可以编辑表格
					trackMouseOver : true, // 鼠标在行上移动时显示高亮
					enableColumnMove : false, // 禁止用户拖动表头
					store : master.store,
					cm : new Ext.grid.ColumnModel({
								columns : columns
							}),
					sm : sm,
					tbar : toolBar,
					bbar : new Ext.PagingToolbar({
								pageSize : 25,
								store : master.store,
								displayInfo : true,
								displayMsg : '当前显示从{0}至{1}， 共{2}条记录',
								emptyMsg : "当前没有记录"
							})
				});
		this.items = [master.grid];
		OECP.ui.GridPanelView.superclass.initComponent.call(this);
	},
	// 查询方法 调用查询窗口
	query : function(master) {
		if (!master.queryWindow) {
			master.queryWindow = new OECP.ui.QueryWindow({
						fieldData : master.queryFiles,
						defaultCondition : {

			}
					});
		}
		master.queryWindow.show();
		master.queryWindow.on('afterquery', function(query) {
					if (typeof query.conditionResult != 'undefined')
						master.grid.getStore().load({
									params : query.conditionResult
								});
				});
		new OECP.ui.QueryWindow();
	},
	/**
	 * 删除方法
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
													msg : msg.result.msg,
													buttons : Ext.Msg.OK
												})
									}

								});
					}

				});

	},
	// 导出Excel
	exportToExcel : function(grid) {
		var vExportContent = grid.getExcelXml();
		if (Ext.isIE6 || Ext.isIE7 || Ext.isIE8 || Ext.isChrome || Ext.isSafari
				|| Ext.isSafari2 || Ext.isSafari3) {
			var fd = Ext.get('frmDummy');
			if (!fd) {
				fd = Ext.DomHelper.append(Ext.getBody(), {
							tag : 'form',
							method : 'post',
							id : 'frmDummy',
							action : '/exportexcel.jsp',
							target : '_blank',
							name : 'frmDummy',
							cls : 'x-hidden',
							cn : [{
										tag : 'input',
										name : 'exportContent',
										id : 'exportContent',
										type : 'hidden'
									}]
						}, true);
			}
			fd.child('#exportContent').set({
						value : vExportContent
					});
			fd.dom.submit();
		} else {
			document.location = 'data:application/vnd.ms-excel;base64,'
					+ Base64.encode(vExportContent);
		}
	},
	/**
	 * 点击增加时调用的方法
	 */
	doClick : function() {
		this.grid.stopEditing();
		this.grid.getStore().add(new Ext.data.Record({
					length : 0,
					width : 0,
					height : 0,
					qty : 0,
					weight : 0,
					newRecord : true
				}));
		this.grid.startEditing();

	},
	/**
	 * 点击保存时调用的方法
	 */
	doSave : function() {
		// 保存数组
		var master = this;
		Ext.Msg.confirm("信息确认", "您确认要保存所有记录吗？", function(b) {
					if (b == 'yes') {
						var mf = master.grid.getStore().modified;
						var data = new Array();
						for (var i = 0; i < mf.length; i++) {
							data.push(mf[i].data);
						}
						Ext.Ajax.request({
									url : master.saveUrl,
									params : {
										datas : Ext.encode(data)
									},
									waitMsg : "正在保存数据...",
									method : 'POST',
									success : function(c, d) {
										Ext.ux.Toast.msg("信息", "数据保存成功！");
										master.grid.getStore().reload();
									},
									failure : function(c, d) {
										Ext.ux.Toast.msg("信息", "数据保存失败！");
									}
								});
					}
				});

	},
	findDisplayValue : function(value, metaData, record, rowIndex, colIndex,
			store, scope) {
		var field = scope.editor.field;// 获取form组件
		var fieldXType = field.getXType();// 获取xtype
		var val = '';
		switch (fieldXType) {
			case 'combo' :
				var record = field.findRecord(field.valueField, value);
				val = record.data[field.displayField];
				break;
			case 'datefield' :
				if (value != null && value != '') {
					val = value.format('Y-m-d H:i:s');
				} else {
					val = '';
				}

		}
		return val;
	},
	/**
	 * 初始化列表
	 * 
	 * @return {}
	 */
	initEditColumns : function() {
		var master = this;
		var oField = new Array();//
		// 把列表编号，复选框放入列中
		var structure = master.structure;
		for (var i = 0; i < structure.length; i++) {
			var column = structure[i];
			column.type = column.type || 'string'; // 设置默认类型为String
			column.unShowAble = column.unShowAble || false; // 设置默认为false，即默认显示
			column.sortable = column.unShowAble || true;
			column.readonly = column.readonly || false;// 默认设置该字段可编辑

			if (column.readonly) {
				oField[oField.length] = {
					dataIndex : column.dataIndex,
					header : column.header,
					hidden : column.hidden,
					sortable : column.sortable,
					width : column.width
				}
			} else {
				// 根据不同的类型设置界面显示
				switch (column.type) {
					case 'string' :
						oField[oField.length] = {
							dataIndex : column.dataIndex,
							header : column.header,
							hidden : column.hidden,
							sortable : column.sortable,
							width : column.width,
							editor : new Ext.grid.GridEditor(new Ext.form.TextField(
									{
										id : this.objectName + '.'
												+ column.dataIndex,
										selectOnFocus : true,
										allowBlank : column.required
												? false
												: true,
										blankText : column.header + "不能为空"

									}))
						}
						break;
					case 'number' :
						oField[oField.length] = {
							dataIndex : column.dataIndex,
							header : column.header,
							hidden : column.hidden,
							renderer : column.renderer,
							sortable : column.sortable,
							width : column.width,
							editor : new Ext.grid.GridEditor(new Ext.form.NumberField(
									{
										id : this.objectName + '.'
												+ column.dataIndex,
										selectOnFocus : true,
										allowBlank : column.required
												? false
												: true,
										blankText : column.header + "不能为空"

									}))
						}
						break;
					case 'date' :
						oField[oField.length] = {
							dataIndex : column.dataIndex,
							header : column.header,
							hidden : column.hidden,
							sortable : column.sortable,
							width : column.width,
							editor : new Ext.grid.GridEditor(new Ext.form.DateField(
									{
										selectOnFocus : true,
										allowBlank : column.required
												? false
												: true,
										blankText : column.header + "不能为空",
										format : 'Y-m-d H:i:s'

									})),
							renderer : function(value, metaData, record,
									rowIndex, colIndex, store) {
								return master
										.findDisplayValue(value, metaData,
												record, rowIndex, colIndex,
												store, this);
							}

						}
						break;
					case 'combo' : // 暂时未处理
						oField[oField.length] = {
							dataIndex : column.dataIndex,
							header : column.header,
							hidden : column.hidden,
							width : column.width,
							editor : new Ext.grid.GridEditor(new Ext.form.ComboBox(
									{
										filed : column.dataIndex,
										store : new Ext.data.SimpleStore({
													fields : ['value', 'text'],
													data : column.store
												}),
										valueField : 'value',
										displayField : 'text',
										emptyText : '请选择',
										mode : column.mode,
										editable : false,
										triggerAction : "all"
									})),
							renderer : function(value, metaData, record,
									rowIndex, colIndex, store) {

								return master
										.findDisplayValue(value, metaData,
												record, rowIndex, colIndex,
												store, this);
							}

						}
						break;
				};
			}
		}
		return oField;

	}
}

);
