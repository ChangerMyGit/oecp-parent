Ext.ns('OECP.ui.ref');
/**
 * 下拉框
 * 
 * @class OECP.ui.ref.ComboRef
 * @extends Ext.form.ComboBox
 */
OECP.ui.ref.ComboRef = Ext.extend(Ext.form.ComboBox, {
			/**
			 * @cfg {String} codeField 编码字段<br>
			 *      业务人员录入数据时通常是录入的数据编码，字段用于手工录入数据过滤使用
			 */
			codeField : 'id',
			/**
			 * @cfg {String} entityName 实体className
			 */
			/**
			 * @cfg {String} functionCode 功能编号
			 */
			/**
			 * @cfg {String} fieldName 字段名
			 */
			fields : ['id'],
			triggerAction : 'all',
			minListWidth : 238,
			/**
			 * @cfg {Number} pageSize 每页行数，0为不分页.
			 */
			pageSize : 20,
			store : new Ext.data.JsonStore(),
			// private
			initComponent : function() {
				var scope = this;
				scope.initStore();
				OECP.ui.ref.ComboRef.superclass.initComponent.call(this);
			},
			// private
			initStore : function() {
				var scope = this;
				Ext.Ajax.request({
							url : __ctxPath + '/app/ref/getRefFields.do',
							params : {entityName : this.entityName},
							success : function(response, opts) {
								var obj = Ext.util.JSON.decode(response.responseText);
								if (obj && obj.length > 0) {
									scope.fields.push(obj[0].name);
									if (!scope.displayField) {
										scope.displayField = scope.fields[1];
									}
									if (!scope.valueField) {
										scope.valueField = scope.fields[0];
									}
									// 重新初始化
									scope.store.constructor({
												root : 'result',
												totalProperty : 'totalCounts',
												url : __ctxPath+ '/app/ref/getRefDatas.do',
												baseParams : {
													entityName : scope.entityName,
													functionCode : scope.functionCode,
													fieldName : scope.fieldName,
													codeField : scope.codeField
												},
												fields : scope.fields
											});
								}
							},
							failure : function(response, opts) {
								alert("参选框数据加载错误");
							}
						});
			},
			onDestroy : function() {
				OECP.ui.ref.ComboRef.superclass.onDestroy(this);
			}
		});
Ext.reg('oecpcomboref',OECP.ui.ref.ComboRef);

/**
 * 下拉表格
 * 
 * @class OECP.ui.ref.GridWinRef
 * @extends Ext.form.ComboBox
 */
OECP.ui.ref.GridMenuRef = Ext.extend(Ext.form.ComboBox, {

	/**
	 * @cfg {String} entityName 实体className
	 */
	/**
	 * @cfg {String} functionCode 功能编号
	 */
	/**
	 * @cfg {String} fieldName 字段名
	 */
	fields : ['id'],
	/**
	 * @cfg {String} codeField 编码字段<br>
	 *      业务人员录入数据时通常是录入的数据编码，字段用于手工录入数据过滤使用
	 */
	codeField : 'id',
	/**
	 * @cfg {Ext.data.JsonStore} store 数据仓库
	 */
	store : new Ext.data.JsonStore(),
	cm : new Ext.grid.ColumnModel([{
				header : 'id'
			}]),
	grid : new Ext.grid.GridPanel({
				cm : this.cm,
				store : this.store
			}),
	onTriggerClick : function() {
		this.store.baseParams['query'] = undefined;
		this.store.load();
		if (this.menu) {
			this.menu.show(this.el, "tl-bl?");
		}
	},
	// private 表格行点击事件
	onRowclick : function(grid, rowIndex, e, scope) {
		if (!scope) {
			scope = this;
		}
		var refval = '';
		scope.refRecords = [];
		scope.refRecords = scope.grid.getSelectionModel().getSelections();
		if (scope.refRecords.length > 0) {
			refval = scope.refRecords[0][scope.valueField];
		}
		scope.setValue(refval);
		this.fireEvent('refselect', this);
		scope.menu.hide();
	},
	// private 初始化菜单
	initMenu : function() {
		var scope = this;
		this.ajaxLoadFieldsData(scope);
		if (!this.menu) {
			this.menu = new Ext.menu.Menu({
						items : [new Ext.menu.Adapter(scope.grid)]
					});
		}
	},
	// 加载表格数据,重新初始化界面
	ajaxLoadFieldsData : function(scope) {
		Ext.Ajax.request({
					url : __ctxPath + '/app/ref/getRefFields.do',
					params : {
						entityName : scope.entityName
					},
					success : function(response, opts) {
						var obj = Ext.util.JSON.decode(response.responseText);
						if (obj && obj.length > 0) {
							if (!scope.valueField) {
								scope.valueField = scope.fields[0];
							}
							if (!scope.displayField) {
								scope.displayField = scope.fields[1];
							}
							var _sm = new Ext.grid.CheckboxSelectionModel({
										singleSelect : true
									});
							var _cm = [new Ext.grid.RowNumberer(), _sm];
							for (var i = 0; i < obj.length; i++) {
								_cm.push({
											header : obj[i]['dispName'],
											dataIndex : obj[i]['name'],
											width : 80
										});
								scope.fields.push(obj[i]['name']);
							}
							// 重新初始化store
							scope.store.constructor({
										autoLoad : scope.autoLoad || true,
										url : __ctxPath+ '/app/ref/getRefDatas.do',
										root : 'result',
										totalProperty : 'totalCounts',
										remoteSort : true,
										baseParams : {
											entityName : scope.entityName,
											functionCode : scope.functionCode,
											fieldName : scope.fieldName,
											codeField : scope.codeField,
											start : 0,
											limit : scope.pageSize===0?-1:scope.pageSize
										},
										fields : scope.fields
									});
							// 重新初始化sm
							scope.cm.constructor(_cm);
							var gridbbar = undefined;
							if (scope.pageSize) {
								gridbbar = new Ext.PagingToolbar({
											pageSize : scope.pageSize,
											store : scope.store,
											displayInfo : true,
											displayMsg : "当前页记录索引{0}-{1}， 共{2}条记录",
											emptyMsg : "当前没有记录"
										});
							}
							// 重新初始化gird
							scope.grid.constructor({
										enableHdMenu : false,
										title : scope.title || '',
										height : scope.gridHeight || 300,
										width : scope.gridWidth || 400,
										region : 'center',
										store : scope.store,
										remoteSort : false,
										cm : scope.cm,
										sm : _sm,
										listeners : {
											'rowclick' : scope.onRowclick.createDelegate(scope,[scope], true)
										},
										bbar : gridbbar
									});
						}
					},
					failure : function(response, opts) {
						alert("参选框数据加载错误");
					}
				});
	},
	// private
	initEvents : function() {
		OECP.ui.ref.GridMenuRef.superclass.initEvents.call(this);
	},
	initComponent : function() {
		this.initMenu();
		OECP.ui.ref.GridMenuRef.superclass.initComponent.call(this);
	},
	onDestroy : function() {
		if (this.menu) {
			Ext.destroy(this.menu);
		}
		OECP.ui.ref.GridMenuRef.superclass.onDestroy(this);
	}
});
Ext.reg('oecpgridmenuref',OECP.ui.ref.GridMenuRef);
/**
 * 弹出表格
 * <br>
 * 需引入下列js,解决不同问题.<br>
 * ext-oecp\ui\form\Form.js  	解决带分页控件可能只显示主键不显示编码问题<br>
 * ext-oecp\ui\GridRefField.js	解决表格中焦点丢失数据不会写问题<br>
 * extjs\ext-basex.js			解决Ajax同步调用问题<br>
 * 
 * @class OECP.ui.ref.GridWinRef
 * @extends Ext.form.ComboBox
 */
OECP.ui.ref.GridWinRef = Ext.extend(Ext.form.ComboBox, {
	// private 选中的record数组窗口中使用
	refRecords : [],
	fields : ['id'],
	windowShow : false,
	initComponent : function() {
		var scope = this;
		this.store=new Ext.data.JsonStore();
		/**
		 * @cfg {Ext.util.MixedCollection} historicalData 历史数据集<br>
		 *      用于缓存已经选择过或过滤过的数据集合.分页时使用
		 */
		this.historicalData = new Ext.util.MixedCollection(false, function(field) {
					return field[this.valueField || 'id'];
				});
		this.cm=new Ext.grid.ColumnModel([{
					header : 'id'
				}]);
		this.grid=new Ext.grid.GridPanel({
					cm : this.cm,
					store : this.store 
				});
		
		this.initBox();
		this.addEvents(
				/**
				 * @event refselect 参照选择事件，点击‘确定’按钮或双击数据行时触发
				 * @param {OECP.ui.RefField}
				 *            refselect 作用域 this
				 */
				'refselect');
		OECP.ui.ref.GridWinRef.superclass.initComponent.call(this);
	},
	// private 初始化查询界面
	initBox : function() {
		var scope = this;
		if (!scope.box) {
			scope.box = new Ext.Window({
						title : scope.title ? scope.title : '',
						height : scope.boxHeight || 400,
						width : scope.boxWidth || 420,
						modal : true,
						closable : true,
						closeAction : 'hide',
						items : [scope.grid],
						defaultButton : 0,
						buttons : [{
									text : '确定',
									listeners : {
										'click' : function() {
											scope.onOkClick(scope);
										}
									}
								}, {
									text : '关闭',
									listeners : {
										'click' : function(e) {
											scope.windowShow = false;
											scope.box.hide();
										}
									}
								}]
					});
			scope.box.on('hide', function() {
						scope.windowShow = false;
					});
			scope.box.on('close', function() {
						scope.windowShow = false;
					});
			this.ajaxLoadFieldsData(scope);
		};
	}, // private 确定按钮点击操作，回填数值。
	onOkClick : function(scope) {
		var refval = '';
		scope.refRecords = [];
		scope.refRecords = scope.grid.getSelectionModel().getSelections();
		if (scope.refRecords.length > 0) {
			refval = scope.refRecords[0][scope.valueField];
		}
		scope.setValue(refval);
		scope.fireEvent('refselect', this);
		scope.box.hide();// 隐藏窗体
		scope.focus(false, 100);
	},
	ajaxLoadFieldsData : function(scope) {
		Ext.Ajax.request({
					url : __ctxPath + '/app/ref/getRefFields.do',
					params : {
						entityName : scope.entityName
					},
					success : function(response, opts) {
						var obj = Ext.util.JSON.decode(response.responseText);
						var _sm = new Ext.grid.CheckboxSelectionModel({
									singleSelect : true
								});
						if (obj && obj.length > 0) {
							if (!scope.valueField) {
								scope.valueField = scope.fields[0];
							}
							if (!scope.displayField) {
								scope.displayField = obj[0].name;
							}

							var _cm = [new Ext.grid.RowNumberer(), _sm];
							for (var i = 0; i < obj.length; i++) {
								_cm.push({
											header : obj[i]['dispName'],
											dataIndex : obj[i]['name'],
											width : 80
										});
								scope.fields.push(obj[i]['name']);
							}
							// 重新初始化store
							scope.store.constructor({
										autoLoad : scope.autoLoad || true,
										remoteSort : true,
										url : __ctxPath+ '/app/ref/getRefDatas.do',
										root : 'result',
										totalProperty : 'totalCounts',
										remoteSort : true,
										baseParams : {
											entityName : scope.entityName,
											functionCode : scope.functionCode,
											fieldName : scope.fieldName,
											codeField : scope.codeField,
											start : 0,
											limit : scope.pageSize===0?-1:scope.pageSize
										},
										fields : scope.fields
									});
							// 重新初始化sm
							scope.cm.constructor(_cm);
						}
						if (!scope.filters) {
							var _filters = [];
							for (var i = 0; i < obj.length; i++) {
								var _f = {};
								var _storeField = scope.fields[i];
								var _type = 'string';
								if (_storeField.type) {
									if (_storeField.type === 'float'
											|| _storeField.type === 'int'
											|| _storeField.type === 'integer'
											|| _storeField.type === 'float') {
										_type = 'numeric';
									} else {
										_type = _storeField.type;
									}
								}
								_f.type = _type;
								_f.dataIndex = obj[i].name;
								_filters.push(_f);
							}
							scope.filters = new Ext.ux.grid.GridFilters({
										encode : false,
										local : false,
										filters : _filters
									});
						}
						var gridbbar = undefined;
						if (scope.pageSize) {
							gridbbar = new Ext.PagingToolbar({
										pageSize : scope.pageSize,
										store : scope.store,
										displayInfo : true,
										displayMsg : "当前页记录索引{0}-{1}， 共{2}条记录",
										emptyMsg : "当前没有记录",
										plugins : [scope.filters]
									});
						}
						// 重新初始化gird
						scope.grid.constructor({
									title : scope.title || '',
									height : scope.gridHeight || 300,
									width : scope.gridWidth || 400,
									region : 'center',
									store : scope.store,
									remoteSort : false,
									cm : scope.cm,
									sm : _sm,
									listeners : {
										'rowdblclick' : function() {
											scope.onOkClick(scope);
										}
									},
									bbar : gridbbar,
									plugins : [scope.filters]
								});
						scope.store.on('load', function() {// 勾选历史记录
									if (scope.getValue() !== '') {
										var _index = scope.store.find(
												scope.fields[0], scope.getValue());
										if (_index != -1) {
											if (scope.grid && scope.grid.getSelectionModel() && scope.windowShow) {
												scope.grid.getSelectionModel().selectRow(_index);
											}
										}
									}
								});
					},
					failure : function(response, opts) {
						alert("参选框数据加载错误");
					}
				});
	},
	// 重载，处理分页显示问题
	setValue : function(v) {
		var text = v;
		if (this.valueField && !Ext.isEmpty(v, false)) {
			var r = this.findRecord(this.valueField, v);
			if (r) {
				text = r.data[this.displayField];
				if (this.pageSize) {// 加载到历史数据
					this.historicalData.add(r.data[this.valueField], r);
				}
			} else if (Ext.isDefined(this.valueNotFoundText)) {
				text = this.valueNotFoundText;
			} else {// 非法数据清空
				text = '',v = '';
			}
		}
		this.lastSelectionText = text;
		if (this.hiddenField) {
			this.hiddenField.value = Ext.value(v, '');
		}
		Ext.form.ComboBox.superclass.setValue.call(this, text);
		this.value = v;
		return this;
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
						this.store.filter(this.codeField || this.displayField, q);
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
	// 赋值并添加到历史数据，用于解决表格界面不显示编码问题
	setValueEx : function(val, displayVal) {
		var text = displayVal;
		Ext.form.ComboBox.superclass.setValue.call(this, text);
		this.lastSelectionText = text;
		if (this.hiddenField) {
			this.hiddenField.value = val;
		}
		this.value = val;
		if (this.pageSize) {
			var defaultObj = {};
			defaultObj[this.valueField] = val;
			defaultObj[this.displayField] = displayVal;
			if (!this.historicalData.item(val)) {
				this.historicalData.add(val,
						new this.store.recordType(defaultObj));
			}
		}
		return this;
	},
	// 重载方法 对分页时存在的历史数据做过滤
	findRecord : function(prop, value) {
		var record;
		if (this.store.getCount() > 0) {
			this.store.each(function(r) {
						if (r.data[prop] == value) {
							record = r;
							return false;
						}
					});
		}
		if (!record) {
			if (this.pageSize) {
				this.historicalData.each(function(r2) {
							if (r2.data[prop] == value) {
								record = r2;
								return false;
							}
						});
			}
		}
		if (!record) {
			r3 = this.ajaxFindRecord(prop, value);
			if (!Ext.isEmpty(r3)) {
				re = new this.store.recordType(r3[0]);
				this.historicalData.add(re.data[this.valueField], re);
				record = re;
			}
		}
		return record;
	},
	ajaxFindRecord : function(prop, value) {
		var record;
		if (value !== '' && value !== "") {
			Ext.Ajax.request({
						url : __ctxPath + '/app/ref/getRefDatas.do',
						params : {
							entityName : this.entityName,
							functionCode : this.functionCode,
							fieldName : this.fieldName,
							codeField : this.codeField,
							start : 0,
							limit : this.pageSize===0?-1:this.pageSize,
							query : value
						},
						async : false,
						success : function(response, opts) {
							var obj = Ext.util.JSON.decode(response.responseText);
							record = Ext.isEmpty(obj.result) ? null : obj.result;
						}
					});
		}
		return record;
	},
	onTriggerClick : function() {
		if (this.readOnly || this.disabled) {
			return;
		}
		if (this.isExpanded()) {
			this.collapse();
			this.el.focus();
		} else {
			if (this.store.baseParams[this.queryParam]) {
				this.store.baseParams[this.queryParam] = undefined;
				this.store.load();
			} else if (this.store.getCount() <= 0) {
				this.store.load();
			}
			this.store.clearFilter();
			this.windowShow = true;
			this.box.show();
			this.hasFocus = false;
			this.box.focus(100, false);
		}
	},
	onDestroy : function() {
		Ext.destroy(this.box, this.historicalData);
		OECP.ui.ref.GridWinRef.superclass.onDestroy(this);
	}
});
Ext.reg('oecpgridwinref',OECP.ui.ref.GridWinRef)