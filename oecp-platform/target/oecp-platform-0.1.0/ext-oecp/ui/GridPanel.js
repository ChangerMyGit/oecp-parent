Ext.ns('OECP.ui');
/**
 *  @author chengzhengliang
 * @class OECP.ui.GridPanelView
 * @extends Ext.Panel {
 * 实现单表的增、删、改的封装--（新增编辑时弹出窗口）
 * 。
 * 包含基本功能： 查询、添加、修改、刷新、导出（需要引入gridToExcel.js）。
 * 
 * 目前支持 string 、number、date、combo
 * 
 * 例子及说明：
 * -----------------------------------------代码参数说明---------------------------------------------------
 * var panel = new OECP.ui.GridPanelView({ 
	  title:'测试列表', 
	  objectName:'test',                             			//action中的eo对象名
	  listUrl:"http://localhost:8090/test/list.do", 			// 列表的url
	  deleteUrl:"http://localhost:8090/test/deleteTests.do", 	// 删除数据的url
	  saveUrl:"http://localhost:8090/test/save.do", 			// 保存数据的url
	  findUrl:"http://localhost:8090/test/find.do", 			//根据Id加载数据的url
	  updateUrl:"http://localhost:8090/test/update.do",  		// 更新数据的url
	  filedDate:[],   											//列表中字段及类型对应。
	  queryFiles:[], 											//查询条件参数  
	  structure[]   											//数据结构的参数设置
 * });
 **/

/** -----------------------------------------代码使用举例----------------------------------------------------
 * <pre><code>
	var panel = new OECP.ui.GridPanelView({
			title:'测试列表',
			objectName:'test',
			listUrl:__fullPath+"test/list.do",
			deleteUrl:__fullPath+"test/deleteTests.do",
			saveUrl:__fullPath+"test/save.do",
			findUrl:__fullPath+"test/find.do",
			updateUrl:__fullPath+"test/save.do",
			filedDate: ['id', 'czl_level','levelName','sumWay', 'amount','isDefaultLevel','description',
			   {
				   name : 'addDate',
				   type : 'date',
				   dateFormat: 'Y-m-d H:i:s'
			   }]
				,
			queryFiles:	[['czl_level', '等级'],
						['levelName', '等级名称'],
						['sumWay', '累计方式'], 
						['amount', '累计金额'],
						['isDefaultLevel', '是否默认等级'],
						['description', '描述']],
			structure:[{
						header : "主键",
						dataIndex : "id",
						hidden : true,  //是否需要在列表中显示
						unShowAble:true  //是否需要在添加、编辑页面显示，默认	为false，即显示					
					}, {
						header : "会员等级",
						dataIndex : 'czl_level',
						width : 70,
						sortable:true,
						required:true,
						type:'string'
					}, {
						header : "会员等级名称",
						dataIndex : 'levelName',
						width : 100,
						sortable:true
					}, {
						header : "累计方式",
						dataIndex : 'sumWay',
						width : 80,
						sortable:true
					}, {
						header : "累计金额",
						dataIndex : 'amount',
						width : 100,
						sortable:true,
						type:'number'
					}, {
						header : "是否默认等级",
						dataIndex : 'isDefaultLevel',
						width : 100,
						renderer : function(value, metaData, record, rowIndex,
								colIndex, store) {
							var h = "";
							if (record.data.isDefaultLevel) {
								h = '是';
							} else {
								h = '否';
							}
							return h;

						},
						sortable:true,
						type:'combo',
						mode : "local",
						store : [[false, "否"], [true, "是"]]
						
					}, {
						header : "描述",
						dataIndex : 'description',
						width : 100,
						sortable:true
					},
					{
						header : "添加时间",
						dataIndex : 'addDate',
						width : 120,
						sortable:true,
						renderer:function(value, metaData, record, rowIndex,
								colIndex, store){
								var addDate=record.data.addDate;
								if(addDate!=null){
									addDate=record.data.addDate.format('Y-m-d H:i:s');
								}else{
									addDate='';
								}
								return addDate;
						},
						type:'date'
					}]
		});
 */



OECP.ui.GridPanelView = Ext.extend(Ext.Panel, {
	//id : 'GridPanelView',
	/**
	 * 
	 * @type String  列表的标题
	 */
	title : '', 
	/**
	 * 
	 * @type String列表的url
	 */
	listUrl : '', 
	/**
	 * 
	 * @type String 删除的链接url
	 */
	deleteUrl : '', 
	/**
	 * 
	 * @type String 保存链接的url
	 */
	saveUrl : '',
	/**
	 * 
	 * @type String 根据id获得数据的url
	 */
	findUrl : '',
	/**
	 * 
	 * @type String  更新的url
	 */
	updateUrl : '', 
	/**
	 * 
	 * @type String 保存action的对象名称
	 */
	objectName : '',  
	width : '99%',
	// autoWidth:true,
	height : 550,
	/**
	 * 
	 * @type 列表数据项
	 */
	filedDate : [],
	/**
	 * 
	 * @type列表及新增页面的数据结构,主要包含下面一些参数：
	 * header : "主键",
	   dataIndex : "id",
	   hidden : true,  //是否需要在列表中显示
	   unShowAble:true  //是否需要在添加、编辑页面显示，默认	为false，即显示
	   sortable:true,   //是否按该字段排序
	   required:true,	//是否必填
	   type:'string'	//该字段的类型
	   renderer:function(){}	//特殊显示时的处理方法
	 *  
	 */
	structure : [],
	/**
	 * 查询需要的参数
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
		//初始化数据仓库
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
										master.query();
									}
								}
							}, {
								text : '添加',
								tooltip : '添加一条新的记录',
								iconCls : 'add-user',
								listeners : {
									'click' : function(btn, e) {
										master.doEdit("增加界面", 0, null);

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
		//初始化列模式
		var sm = new Ext.grid.CheckboxSelectionModel({
			singleSelect : master.selectCheckboxModel
				// handleMouseDown : Ext.emptyFn
			});
		// 把列表编号，复选框放入列中
		var columns = master.structure;
		columns.unshift(new Ext.grid.RowNumberer(), sm);
		//初始化Grid
		master.grid = new Ext.grid.EditorGridPanel({
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
	/**
	 * 查询按钮调用的方法
	 * @param {} master 对象
	 */
	query : function() {
		var master =this;
		if (master.queryWindow == null) {
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
	 * s删除方法
	 * @param {} ids
	 * @param {} rows
	 * @param {} store
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
	/**
	 * 导出Excel 方法
	 * @param {} grid
	 */
	exportToExcel : function(grid) {
		var vExportContent = grid.getExcelXml();
		if (Ext.isIE6 || Ext.isIE7 ||Ext.isIE8 ||Ext.isChrome|| Ext.isSafari || Ext.isSafari2
				|| Ext.isSafari3) {
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
	// ----------------------处理编辑、增加操作-------------------------------
	/**
	 * 处理添加和编辑方法
	 * @param {} title
	 * @param {} action
	 * @param {} id
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
			column.type = column.type || 'string';					// 设置默认类型为String
			column.unShowAble = column.unShowAble || false;		    // 设置默认为false，即默认显示

			// 根据不同的类型设置界面显示
			switch (column.type) {
				case 'string' :
					oField[oField.length] = {
						xtype : 'textfield',
						id : objectName + '.' + column.dataIndex,
						mapping : column.dataIndex,
						fieldLabel : column.header,
						anchor : '90%',
						hidden : column.unShowAble,
						allowBlank : column.required ? false : true,
						blankText : column.header + "不能为空",
						rederer: function(value, metaData, record,
									rowIndex, colIndex, store) {
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
						blankText : "请选择"+ column.header ,
						store :new Ext.data.SimpleStore({
									fields : ['value', 'text'],
									data : column.store
						}) ,
						valueField : 'value',
						displayField : 'text',
						hiddenName : objectName + '.' + column.dataIndex,
						mode:column.mode,
//						emptyText : '请选择',
						editable : false,
						triggerAction : "all",
						grow :false
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
					autoWidth:true,
					autoHeight : true,
					// layout:'fit',
					items : oField,
					reader : new Ext.data.JsonReader({
								root : 'result'
							}, readerArray),
					url : __ctxPath + action == 0 ? saveUrl : updateUrl,
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
					buttonAlign :'center'
				});
		//如果为编辑时，加载form中的数据。
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
							Ext.ux.Toast.msg("信息", "数据载入成功！");
						},
						failure : function(c, d) {
							Ext.ux.Toast.msg("信息", "数据载入失败！");
						}
					});
		}
		// 初始化增加和编辑的窗口
		var addWin = new Ext.Window({
					title : title,
					labelWidth : 100,
					frame : true,
					autoHeight : true,
//					height : 450,
					width : 500,
					closable:true,
					resizable:true,
					modal : true,
					items : editForm
				});
		addWin.show();
		//form 提交方法
		function doSubmit() {
			if (editForm.form.isValid()) {
				editForm.form.submit({
							waitTitle : "请稍候",
							waitMsg : "正在提交表单数据，请稍候......",
							success : function() {
								addWin.hide();
								addWin.destroy();
								master.grid.getStore().reload();
								Ext.Msg.alert('提示', '操作成功');
							},
							failure : function() {
								Ext.Msg.alert('提示', '操作失败');
							}
						});
			}
		}

	}
}
);

