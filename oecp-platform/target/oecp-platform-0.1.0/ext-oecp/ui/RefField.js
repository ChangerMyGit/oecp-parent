/**
 * @author wangliang
 *         <p>
 *         例子:
 *         </p>
 * 
 * <pre><code>
 * var ref = new OECP.ui.RefField({
 * 			label : '商品',
 * 			hiddenName : 'myref',
 * 			valueField : 'id',
 * 			displayField : 'name',
 * 			refColumns : [{
 * 						header : '主键',
 * 						dataIndex : 'id',
 * 						hidden : true,
 * 						width : 30
 * 					}, {
 * 						header : '商品名称',
 * 						dataIndex : 'name',
 * 						width : 80
 * 					}, {
 * 						header : '商品规格',
 * 						dataIndex : 'packing',
 * 						width : 80
 * 					}],
 * 			storeFields : ['id', 'name', 'packing'],
 * 			refUrl : 'refData.json',
 * 			multiple : true
 * 		});
 * myFormPanel = new Ext.form.FormPanel({
 * 			standardSubmit : true,
 * 			frame : true,
 * 			items : [{
 * 						hiddenName : 'numCombo',
 * 						xtype : 'combo',
 * 						valueField : 'value',
 * 						displayField : 'text',
 * 						emptyText : '请选择',
 * 						mode : 'local',
 * 						editable : false,
 * 						triggerAction : 'all',
 * 						store : [['1', '一'], ['2', '二']]
 * 					}, ref],
 * 			refUrl : '/MyExtJs/jsonAction'
 * 		});
 * </code></pre>
 */
Ext.ns('OECP.ui');
/**
 * 档案参选录入框
 * 
 * @class OECP.ui.RefField
 * @extends Ext.form.ComboBox
 */
OECP.ui.RefField = Ext.extend(Ext.form.ComboBox, {
	/**
	 * @cfg {Ext.Window} box 数据展示Window
	 */
	/**
	 * @cfg {String} xtype 默认xtype
	 */
	xtype : 'reffield',
	/**
	 * 
	 * @cfg {String} autoLoad 是否自动加载<br>
	 *      参照可能与其他字段有联动，可以设置autoLoad=false 禁止自动加载。<br>
	 *      前置字段选择后调用doQuery()去执行查询
	 */
	autoLoad : true,
	/**
	 * @cfg {String} displayField 参照显示字段,用于快速定位数据位置
	 */
	displayField : '',
	/**
	 * @cfg {String} valueField 参照值字段
	 */
	valueField : '',
	/**
	 * @cfg {String} codeField 编码字段<br>
	 *      业务人员录入数据时通常是录入的数据编码，字段用于手工录入数据过滤使用
	 */
	codeField : '',
	/**
	 * @cfg{String} triggerClass 图标css样式
	 */
	triggerClass : 'x-form-ref-trigger',
	/**
	 * @cfg {Array} refColumns 参照字段属性
	 */
	refColumns : [],
	/**
	 * @cfg {Array} refStoreFields Store的fields属性
	 */
	storeFields : [],
	/**
	 * @cfg {Object} queryParams Store查询默认参数
	 */
	refQueryParams : {},
	emptyText : '请选择一条数据',
	/**
	 * @cfg {Boolean} multiple 是否多选,默认为false单选
	 */
	multiple : false,
	/**
	 * 
	 * @cfg {Boolean} showCheckBox 是否显示单选框,默认显示
	 */
	showCheckBox : true,
	/**
	 * @cfg {Boolean} showRowNum 是否显示行号,默认显示
	 */
	showRowNum : true,
	/**
	 * @cfg {Boolean} editable 能否编辑<br>
	 *      true:可编辑,false:禁用编辑
	 */
	editable : true,
	/**
	 * @cfg {Boolean} page 参选框是否分页，默认为false不分页
	 */
	page : false,
	/**
	 * @cfg {Number} pageSize 参选框每页显示数量，默认为25
	 */
	pageSize : 25,
	/**
	 * @cfg {Ext.data.JsonStore} store 重载combo的store.
	 * 
	 */
	store : undefined,
	filters : undefined,
	// private gird
	refGrid : undefined,
	// private 已选中的record数组
	refRecords : [],
	// init
	initComponent : function() {
		this.initBox();
		OECP.ui.RefField.superclass.initComponent.call(this);
		this.addEvents(
				/**
				 * @event refselect 参照选择事件，点击‘确定’按钮或双击数据行时触发
				 * @param {OECP.ui.RefField}
				 *            refselect 作用域 this
				 */
				'refselect');
	},
	// private 确定按钮点击操作，回填数值。
	onOkClick : function(refField) {
		var refval = '';
		refField.refRecords = [];
		refField.refRecords = refField.refGrid.getSelectionModel()
				.getSelections();
		if (refField.refRecords.length > 0) {
			if (!refField.multiple) {// 单选
				refval = refField.refRecords[0][refField.valueField];
			} else {// 多选
				for (var i = 0; i < refField.refRecords.length; i++) {
					refval = refval
							+ refField.refRecords[i][refField.valueField] + ',';
				}
				refval = refval.substring(0, refval.length - 1);
			}
		}
		refField.setValue(refval);// 赋值
		this.fireEvent('refselect', this);
		refField.box.hide();// 隐藏窗体
	},
	listeners : {
		'change' : function(ref) {
			if (this.editable) {
				if (this.store.getCount() <= 0) {
					this.setValue('');
				}
			}
		}
	},
	// 重载控件点击事件
	onTriggerClick : function() {
		this.onFocus();
		this.store.clearFilter();
		this.box.show();
	},
	// private 初始化查询界面
	initBox : function() {
		var master = this;
		if (!master.filters) {
			var _filters = [];
			for (var i = 0; i < master.storeFields.length; i++) {
				var obj = {};
				var _storeField = master.storeFields[i];
				var _type = _storeField.type ? _storeField.type : 'string';
				if (_storeField.type) {
					// 数值型过滤器 float int
					if (_storeField.type === 'float'
							|| _storeField.type === 'int') {
						_type = 'numeric';
					} else {
						// 其他过滤器类型 string date boolean
						_type = _storeField.type;
					}
				}
				obj.type = _type;
				obj.dataIndex = _storeField.name
						? _storeField.name
						: _storeField;
				_filters.push(obj);
			}
			master.filters = new Ext.ux.grid.GridFilters({
						encode : false, // json encode the filter query
						local : true, // defaults to false (remote filtering)
						filters : _filters
					});
		}
		if (!master.box) {
			var cols = [];
			// 添加选择器
			var sm = new Ext.grid.CheckboxSelectionModel({
						hidden : !master.showCheckBox,
						singleSelect : !master.multiple
					});
			cols.push(sm);
			// 显示行号
			if (this.showRowNum) {
				cols.push(new Ext.grid.RowNumberer());
			}
			// 追加表格字段
			for (var i = 0; i < master.refColumns.length; i++) {
				cols.push(master.refColumns[i]);
			}
			// 初始化数据
			if (!Ext.isDefined(this.store)) {
				this.store = new Ext.data.JsonStore({
							autoLoad : this.autoLoad,
							remoteSort : true,
							url : this.refUrl,
							root : 'result',
							baseParams : master.refQueryParams,
							totalProperty : 'totalCounts',
							remoteSort : true,
							fields : master.storeFields
						});
			}
			var bbar = undefined;
			if (!master.page) {
				bbar = new Ext.PagingToolbar({
							pageSize : master.pageSize,
							store : master.store,
							displayInfo : true,
							displayMsg : "当前页记录索引{0}-{1}， 共{2}条记录",
							emptyMsg : "当前没有记录",
							plugins : [master.filters]
						});

			}
			// 初始化表格面板
			if (!Ext.isDefined(master.refGrid)) {
				master.refGrid = new Ext.grid.GridPanel({
							region : 'center',
							store : master.store,
							remoteSort : true,
							cm : new Ext.grid.ColumnModel(cols),
							sm : sm,
							listeners : {
								'rowdblclick' : function(grid, row, event) {// 表格行双击事件
									master.onOkClick(master);
								}
							},
							plugins : [master.filters],
							bbar : bbar
						});
			}
			// 初始化弹出框
			if (!Ext.isDefined(master.box)) {
				master.box = new Ext.Window({
							title : master.title ? master.title : '' + '参选',
							height : '400',
							width : '420',
							modal : true,
							closable : true,
							closeAction : 'hide',
							layout : 'border',
							items : [master.refGrid],
							defaultButton : 0,
							buttons : ['->', '-', {
										text : '确定',
										listeners : {
											// 按钮点击事件 获取并处理显示值与实际值
											'click' : function(e) {
												master.onOkClick(master);
											}
										}
									}, '-', {
										text : '关闭',
										listeners : {
											'click' : function(e) {
												master.box.hide();
											}
										}
									}, '-']
						});
			}
		}
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
						this.store.filter(this.codeField || this.displayField,
								q);
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
	onDestroy : function() {
		Ext.destroy(this.box);
		OECP.ui.RefField.superclass.onDestroy.call(this);
	}
});
Ext.reg('reffield', OECP.ui.RefField);