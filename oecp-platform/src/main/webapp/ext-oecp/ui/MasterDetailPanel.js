/**
 * @author wangliang
 *         <p>
 *         主从表界面
 *         </p>
 *         主表、从表数据Json数据格式 ：<br>
 *         <code>
 * {success:true,totalCounts:6,result:[{'id':'1001','code':'a1','name':'a1'},{'id':'1002','code':'a2','name':'a2'}]}
 * </code><br>
 * 
 * 例子：
 * 
 * <pre><code>
 * var test = new OECP.ui.MasterDetailPanel({
 * 			height : 400,
 * 			headCheckBox : true,
 * 			headDataUrl : '/role/testJson.do',
 * 			headStoreParams : {
 * 				id : 'abc'
 * 			},
 * 			headColumns : [{
 * 						header : '主键',
 * 						dataIndex : 'id',
 * 						hidden : true
 * 					}, {
 * 						header : '编码',
 * 						dataIndex : 'code'
 * 					}, {
 * 						header : '名称',
 * 						dataIndex : 'name'
 * 					}],
 * 			headStoreFields : ['id', 'code', 'name'],
 * 			bodyDataUrl : '/role/testJson.do',
 * 			bodyColumns : [{
 * 						header : '主键',
 * 						dataIndex : 'id',
 * 						hidden : true
 * 					}, {
 * 						header : '编码',
 * 						dataIndex : 'code'
 * 					}, {
 * 						header : '名称',
 * 						dataIndex : 'name'
 * 					}],
 * 			bodyStoreFields : ['id', 'code', 'name']
 * 		})
 * test.render(document.body);
 * test.on('headrowclick', function(masterDetailPanel, grid, row, event) {
 * 			grid.getSelectionModel().each(function(i) {
 * 						Ext.ux.Toast
 * 								.msg('信息', '点击' + row + '行 主键：' + i.data.id);
 * 					});
 * 		});
 * </code></pre>
 */
Ext.ns('OECP.ui');
OECP.ui.MasterDetailPanel = Ext.extend(Ext.Panel, {
	/**
	 * @cfg {Array} btnBar 按钮条
	 */
	/**
	 * @cfg {Object} headGrid 主表GridPanel Config参数
	 */
	/**
	 * @cfg {String} headDataUrl 表头数据地址
	 */
	/**
	 * @cfg {Array} headColumns 表头字段属性
	 */
	/**
	 * @cfg {Array} headStoreFields 表头数据字段
	 * 
	 * <pre><code>
	 * ['id', 'code', {
	 * 			name : 'name',
	 * 			type : 'string'
	 * 		}]
	 * </code></pre>
	 */
	/**
	 * @cfg {Object} headStoreParams 表头查询参数
	 * 
	 * <pre><code>
	 * {
	 * 	id : 'abc',
	 *  code:'xxx'
	 * }
	 * </code></pre>
	 */
	/**
	 * @cfg {Object} bodyGrid 从表GridPanel Config参数
	 */
	/**
	 * @cfg {String} bodyDataUrl 从表数据URL地址
	 */
	/**
	 * @cfg {Array} bodyColumns 从表字段属性
	 */
	/**
	 * @cfg {Array} bodyStoreFields 从表数据字段
	 */
	/**
	 * @cfg {Object} refs 字段参照<br>
	 * 
	 */
	/**
	 * @cfg {Number} headPageSize 表头分页每页显示数量；当等于0时不分页.
	 */
	headPageSize : 25,
	/**
	 * 
	 * @cfg {Number} bodyPageSize 表体分页每页显示数量；当等于0时不分页.
	 */
	/**
	 * 
	 * @cfg {Number} height 高度
	 */
	// height : 600,
	/**
	 * @cfg {Number} width 宽度
	 */
	// width : 500,
	/**
	 * @cfg {Boolean} 主表显示选择框 <br>
	 *      <code>true</code> 显示选择框<br>
	 *      <code>false</code> 不显示选择框
	 */
	headCheckBox : false,
	/**
	 * @cfg {Boolean} 主表显示行号<br>
	 *      <code>true</code>显示行号 <br>
	 *      <code>false</code> 不显示行号
	 */
	headRowNum : true,
	/**
	 * @cfg {Boolean} 从表显示选择框<br>
	 *      <code>true</code> 显示选择框<br>
	 *      <code>false</code> 不显示选择框
	 */
	bodyCheckBox : false,
	/**
	 * @cfg {Boolean} 从表显示行号<br>
	 *      <code>true</code> 显示行号<br>
	 *      <code>false</code> 不显示行号
	 */
	bodyRowNum : true,
	/**
	 * @cfg {Boolean} 主表显示行号<br>
	 *      <code>true</code>显示行号 <br>
	 *      <code>false</code> 不显示行号
	 */
	/**
	 * @cfg {Object} 主表参数
	 */
	headGrid : undefined,
	/**
	 * @cfg {Object} 主表数据集
	 */
	headStore : undefined,
	/**
	 * @cfg {Boolean} 表头自动加载
	 */
	headStoreAutoLoad : false,
	/**
	 * @cfg {Object} 从表参数
	 * 
	 */
	bodyGrid : undefined,
	/**
	 * @cfg{Object} 子表数据集
	 */
	bodyStore : undefined,
	/**
	 * @cfg {Boolean} 表体自动加载
	 */
	bodyStoreAutoLoad : false,
	/**
	 * @cfg {string} 查询地址
	 */
	queryUrl : '',
	/**
	 * @cfg {Array} 按钮属性,除增删改查等按钮外,初始化时可追加自定义按钮
	 */
	btnBar : [],
	/**
	 * @cfg {String} 主表主键字段名<br>
	 *      用于主表行点击后从表数据联动时，传递参数取值
	 */
	headPrimaryKey : 'id',
	/**
	 * @cfg {Object} topItem 顶部控件
	 */
	/**
	 * @cfg {Object} endItem 底部控件
	 */
	/**
	 * @cfg {OECP.ui.QueryWindow} queryWindow 查询面板
	 */
	/**
	 * @cfg {Ext.Button} queryBtn 查询按钮
	 */
	/**
	 * @cfg {Ext.Button} addBtn 增加按钮
	 */
	/**
	 * @cfg {Ext.Button} editBtn 编辑按钮
	 */
	/**
	 * @cfg {Ext.Button} delBtn 删除按钮
	 */
	// private 主表选中行的主键，用于判断是否进行从表刷新判断
	headCurrentPK : '',
	/**
	 * 清除表体数据
	 */
	clearBodyStore : function() {
		if (this.bodyGrid) {
			this.bodyGrid.store.removeAll();
		}
	},
	// layout : 'border',
	layout : {
		type : 'vbox',
		align : 'stretch'
	},
	initComponent : function() {
		var scope = this;
		var hg = this.headGrid;
		if (!hg) {
			this.headStore = new Ext.data.JsonStore({
						url : this.headDataUrl,
						baseParams : this.headStoreParams,
						root : 'result',
						remoteSort : true,
						totalProperty : 'totalCounts',
						autoLoad : this.headStoreAutoLoad,
						fields : this.headStoreFields
					});
			// 构建表头字段
			var headCols = [];
			var sm_h = null;
			// add checkbox
			sm_h = new Ext.grid.CheckboxSelectionModel({
						hidden : !this.headCheckBox
					});
			headCols.push(sm_h);
			// add rownum
			if (this.headRowNum) {
				headCols.push(new Ext.grid.RowNumberer());
			}
			for (var i = 0; i < this.headColumns.length; i++) {
				headCols.push(this.headColumns[i]);
			}
			// init pageToolbar
			var _head_bbar = undefined;
			if (this.headPageSize) {
				_head_bbar = new Ext.PagingToolbar({
							pageSize : this.headPageSize,
							store : this.headStore,
							displayInfo : true,
							displayMsg : '当前页记录索引{0}-{1}， 共{2}条记录',
							emptyMsg : '当前没有记录'
						});
			}
			// headGrid
			hg = new Ext.grid.GridPanel({
						flex : 1,
						cm : new Ext.grid.ColumnModel(headCols),
						sm : sm_h,
						store : scope.headStore,
						bbar : _head_bbar,
						listeners : {
							'rowclick' : function(grid, rowIndex, event) {
								var p = grid.store.getAt(rowIndex)
										.get(scope.headPrimaryKey);
								if(p!=null&&p!='null'){
									if (scope.headCurrentPK != p) {
										scope.headCurrentPK = p;
										scope.reloadBodyStore();
									}
								}else{
									var d = grid.store.getAt(rowIndex).data['details'];
									Ext.each(d,function(dd){
										dd['id'] = 'null';
									})
									var data = Ext.util.JSON.encode(d);
									var result = '{"result":'+data+'}';
									//var data = {"result":[{"id":"f9b16fn88gtv0005","doubleValue":1.23333333E8,"booleanValue":true}]};
									var dataresult = Ext.util.JSON.decode(result);
									scope.bodyStore.loadData(dataresult);
								}
							}
						}
					});
		} else if (Ext.isObject(hg)) {
			hg = new Ext.grid.GridPanel(hg);
		}
		this.headGrid = hg;
		// bodyGrid
		var bg = this.bodyGrid;
		if (!bg) {
			this.bodyStore = new Ext.data.JsonStore({
						remoteSort : true,
						root : "result",
						url : this.bodyDataUrl,
						totalProperty : 'totalCounts',
						fields : this.bodyStoreFields,
						autoLoad : this.bodyStoreAutoLoad,
						baseParams : {
							limit:this.bodyPageSize||0,
							id : this.headCurrentPK
						}
					});
			var bodyCols = [];
			var sm_b = null;
			// add checkbox
			if (this.bodyCheckBox) {
				sm_b = new Ext.grid.CheckboxSelectionModel();
				bodyCols.push(sm_b);
			}
			// add rownum
			if (this.bodyRowNum) {
				bodyCols.push(new Ext.grid.RowNumberer());
			}
			for (var i = 0; i < this.bodyColumns.length; i++) {
				bodyCols.push(this.bodyColumns[i]);
			}
			// init pageToolbar
			var _body_bbar = undefined;
			if (this.bodyPageSize) {
				_body_bbar = new Ext.PagingToolbar({
							pageSize : this.bodyPageSize,
							store : this.bodyStore,
							displayInfo : true,
							displayMsg : '当前页记录索引{0}-{1}， 共{2}条记录',
							emptyMsg : '当前没有记录'
						})
			}
			bg = new Ext.grid.GridPanel({
						flex : 1,
						cm : new Ext.grid.ColumnModel(bodyCols),
						store : this.bodyStore,
						bbar : _body_bbar
					})
		} else if (Ext.isObject(bg)) {
			bg = new Ext.grid.GridPanel(bg);
		}
		this.bodyGrid = bg;
		var _items = [];
		if (this.topItem) {
			_items.push(this.topItem);
		}
		_items.push(this.headGrid);
		_items.push(this.bodyGrid);
		if (this.endItem) {
			_items.push(this.endItem);
		}
		this.items = _items;
		// add button
		if (!this.queryBtn) {
			this.queryBtn = new OECP.ui.button.QueryButton({
				listeners : {
					'click' : function(btn, event) {
						var queryFieldData = [];
						var rowIndex = 0;
						// 拼装查询条件
						for (var i = 0; i < scope.headColumns.length; i++) {
							var tmp = [];
							if (!scope.headColumns[i].hidden) {
								tmp.push(scope.headColumns[i].dataIndex);
								tmp.push(scope.headColumns[i].header);
								tmp.push(scope.headColumns[i].fieldType?scope.headColumns[i].fieldType:'java.lang.String');
								// TODO 查询条件默认值 默认查询条件控制
								queryFieldData[rowIndex] = tmp;
								rowIndex += 1;
							}
						}
						if (!scope.queryWindow) {
							scope.queryWindow = new OECP.ui.QueryWindow({
										fieldData : queryFieldData,
										defaultCondition : scope.headStoreParams,
										refs : scope.refs || null,
										persOperator : {
											name : [['=', '等于']]
										}
									});
							scope.queryWindow.on('afterquery', function(query) {
								if (typeof query.conditionResult != 'undefined') {
									scope.headGrid.getStore().removeAll();
									scope.headGrid.getStore().baseParams = query.conditionResult;
									scope.headGrid.getStore().load();
								}
							});
						}
						scope.queryWindow.show();

					}
				}
			})
		}
		if (!this.addBtn) {
			this.addBtn = new OECP.ui.button.AddButton();
		}
		if (!this.editBtn) {
			this.editBtn = new OECP.ui.button.EditButton();
		}
		if (!this.delBtn) {
			this.delBtn = new OECP.ui.button.DelButton();
		}
		var btns = [this.queryBtn, this.addBtn, this.editBtn, this.delBtn];
		if (this.btnBar && this.btnBar.length > 0) {
			for (var i = 0; i < this.btnBar.length; i++) {
				btns.push(this.btnBar[i]);
			}
		}
		this.tbar = new Ext.Toolbar({
					bodyStyle : "text-align:left",
					items : btns
				});
		OECP.ui.MasterDetailPanel.superclass.initComponent.call(this);
	},
	/**
	 * 重新加载表体
	 */
	reloadBodyStore : function() {
		this.clearBodyStore();
		this.bodyStore.baseParams['id'] = this.headCurrentPK;
		// 加载表体数据
		this.bodyStore.load();
	},
	// private
	onDestroy : function() {
		if (this.queryWindow) {
			Ext.destroy(this.queryWindow);
		}
		OECP.ui.MasterDetailPanel.superclass.onDestroy.call(this);
	}
});
// 注册xtype
Ext.reg('masterdetail', OECP.ui.MasterDetailPanel);
