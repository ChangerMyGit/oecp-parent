/**
 * @author wangliang
 * 
 * 需要 Adapter.js 支持<br>
 * 存在部分问题未修改：<br>
 * 1)点击列筛选时焦点丢失，菜单隐藏，无法进行选择操作<br>
 * 2)默认选择器无法进行勾选
 * <p>
 * 例子:
 * </p>
 * 
 * <pre><code>
 * var ref = new OECP.ui.GridRefField({
 * 			minChars : 2,
 * 			mode : 'local',
 * 			valueField : 'id',
 * 			codeField : 'id',
 * 			displayField : 'name',
 * 			refUrl : '/warehouse/ref.do',
 * 			storeFields : ['id', 'name', {
 * 						name : 'person',
 * 						mapping : 'person.name'
 * 					}, 'address', 'phone', {
 * 						name : 'org',
 * 						mapping : 'org.name'
 * 					}],
 * 			refColumns : [{
 * 						header : '主键',
 * 						dataIndex : 'id',
 * 						hidden : true,
 * 						width : 30
 * 					}, {
 * 						header : '仓库名称',
 * 						dataIndex : 'name',
 * 						width : 80
 * 					}, {
 * 						header : '仓库主管',
 * 						dataIndex : 'person',
 * 						width : 80
 * 					}, {
 * 						header : '地址',
 * 						dataIndex : 'address',
 * 						width : 80
 * 					}, {
 * 						header : '电话',
 * 						dataIndex : 'phone',
 * 						width : 80
 * 					}, {
 * 						header : '所属机构',
 * 						dataIndex : 'org',
 * 						width : 80
 * 					}]
 * 		});
 * ref.render('ref');
 * </code></pre>
 */
Ext.ns('OECP.ui');
/**
 * 档案参选录入框 <br>
 * 表格菜单
 * 
 * @class OECP.ui.RefField
 * @extends Ext.form.ComboBox
 */
OECP.ui.GridRefField = Ext.extend(Ext.form.ComboBox, {
			/**
			 * @cfg {Number} gridHeight 表格高度
			 */
			gridHeight : 200,
			/**
			 * @cfg {Number} gridWeight 表格宽度
			 */
			gridWidth : 300,
			/**
			 * @cfg {String} title 标题
			 */
			/**
			 * @cfg {Ext.data.Stroe} store 数据仓库
			 */
			/**
			 * @cfg {Ext.grid.GridPanel} grid 表格
			 */
			/**
			 * @cfg {Ext.menu.Menu} menu 下拉菜单
			 */
			/**
			 * @cfg {String} refUrl 数据加载Url
			 */
			/**
			 * @cfg {Array} storeFields 数据仓库字段列表
			 */
			/**
			 * @cfg {Array} refColumns 表格列模型
			 */
			/**
			 * @cfg {Boolean} multiple 是否多选,默认为false单选
			 */
			multiple : false,
			emptyText : '请选择一条数据',
			/**
			 * 
			 * @cfg {Boolean} showCheckBox 是否显示单选框,默认显示
			 */
			showCheckBox : true,
			/**
			 * @cfg {String} codeField 编码字段<br>
			 *      业务人员录入数据时通常是录入的数据编码，字段用于手工录入数据过滤使用
			 */
			codeField : '',
			/**
			 * @cfg {Boolean} showRowNum 是否显示行号,默认显示
			 */
			showRowNum : true,
			/**
			 * @type {Number} 每页显显示数量
			 */
			pageSize : 25,
			/**
			 * @cfg {Boolean} autoLoad 自动加载
			 */
			autoLoad : true,
			// private 已选中的record数组
			refRecords : [],
			onTriggerClick : function() {
				if (this.menu) {
					this.menu.show(this.el, "tl-bl?");
				}
			},
			// private 表格双击事件
			onRowclick : function(grid, rowIndex, e, scope) {
				if (!scope) {
					scope = this;
				}
				var refval = '';
				scope.refRecords = [];
				scope.refRecords = scope.grid.getSelectionModel()
						.getSelections();
				if (scope.refRecords.length > 0) {
					if (!scope.multiple) {// 单选
						refval = scope.refRecords[0][scope.valueField]
					} else {// 多选
						for (var i = 0; i < scope.refRecords.length; i++) {
							refval = refval
									+ scope.refRecords[i][scope.valueField]
									+ ',';
						}
						refval = refval.substring(0, refval.length - 1);
					}
				}
				scope.setValue(refval);// 赋值
				this.fireEvent('refselect', this);
				scope.menu.hide();
			},
			initComponent : function() {
				this.initMenu();
				this.addEvents(
						/**
						 * @event refselect 参照选择事件，点击‘确定’按钮或双击数据行时触发
						 * @param {OECP.ui.GirdRefField}
						 *            refselect 作用域 this
						 */
						'refselect');
				OECP.ui.GridRefField.superclass.initComponent.call(this);
			},
			/**
			 * 重载Combo方法，根据业务录入习惯，添加编码对照过滤
			 */
			doQuery : function(q, forceAll) {
				q = Ext.isEmpty(q) ? '' : q;
				var qe = {
					query : q,
					forceAll : forceAll,
					combo : this,
					cancel : false
				};
				if (this.fireEvent('beforequery', qe) === false || qe.cancel) {
					return false;
				}
				q = qe.query;
				forceAll = qe.forceAll;
				if (forceAll === true || (q.length >= this.minChars)) {
					if (this.lastQuery !== q) {
						this.lastQuery = q;
						if (this.mode == 'local') {
							this.selectedIndex = -1;
							if (forceAll) {
								this.store.clearFilter();
							} else {
								this.store.filter(this.codeField
												|| this.displayField, q);
							}
							this.onLoad();
						} else {
							this.store.baseParams[this.queryParam] = q;
							this.store.load({
										params : this.getParams(q)
									});
							this.expand();
						}
					} else {
						this.selectedIndex = -1;
						this.onLoad();
					}
				}
			},
			// private 初始化菜单
			initMenu : function() {
				var scope = this;
				if (!this.store) {
					this.store = new Ext.data.JsonStore({
								autoLoad : scope.autoLoad,
								remoteSort : true,
								url : scope.refUrl,
								root : 'result',
								totalProperty : 'totalCounts',
								remoteSort : true,
								fields : scope.storeFields
							});
				}
				if (!this.grid) {
					var cols = [];
					// TODO 单击后焦点丢失，不触发事件
					// 添加选择器
					// var sm = new Ext.grid.CheckboxSelectionModel({
					// hidden : !scope.showCheckBox,
					// singleSelect : !scope.multiple
					// });
					// cols.push(sm);
					// 显示行号
					if (this.showRowNum) {
						cols.push(new Ext.grid.RowNumberer());
					}
					// 追加表格字段
					for (var i = 0; i < scope.refColumns.length; i++) {
						cols.push(scope.refColumns[i]);
					}
					var bbar;
					if (this.page) {
						bbar = new Ext.PagingToolbar({
									pageSize : scope.pageSize,
									store : scope.store,
									displayInfo : true,
									displayMsg : "当前页记录索引{0}-{1}， 共{2}条记录",
									emptyMsg : "当前没有记录"
								});
					}
					this.grid = new Ext.grid.GridPanel({
								enableHdMenu : false,
								title : scope.title || '',
								height : this.gridHeight,
								width : this.gridWidth,
								region : 'center',
								store : this.store,
								remoteSort : false,
								cm : new Ext.grid.ColumnModel(cols),
								listeners : {
									'rowclick' : scope.onRowclick
											.createDelegate(scope, [scope],
													true)
								},
								bbar : bbar
							});
				} else {
					this.grid.on('rowclick', scope.onRowclick, scope);
				}
				if (!this.menu) {
					this.menu = new Ext.menu.Menu({
								items : [new Ext.menu.Adapter(scope.grid)]
							});
				}
			},
			onDestroy : function() {
				Ext.destroy(this.menu);
				OECP.ui.GridRefField.superclass.onDestroy.call(this);
			}
		});
Ext.reg('gridreffield', OECP.ui.GridRefField);