Ext.ns('OECP.ui');
/**
 * @author wangliang
 * @class OECP.ui.MasterDetailEditPanel
 * @extends Ext.FormPanel
 *          <p>
 *          主子表编辑界面
 *          </p>
 *          <b>在bodyColumns中可以添加defaultReadOnly记录字段是否只读的原始属性。设置为true后setEditable()方法无法改变此column的只读属性</b>
 * 
 * <pre>
 * 
 * 例子
 * ：
 * <code>
 * var form = new OECP.ui.MasterDetailEditPanel({
 * 			height : 300,
 * 			weight : 400,
 * 			state : 'edit',
 * 			modifiedVarName : 'testVar',
 * 			detailVarName : 'uis',
 * 			submitUrl : '/role/test.do',
 * 			queryUrl : '/role/test2.do',
 * 			items : [{
 * 						fieldLabel : '主键',
 * 						xtype : 'textfield',
 * 						dataIndex : 'id',
 * 						name : 'id',
 * 						mapping : 'id',
 * 						hidden : true
 * 					}, {
 * 						fieldLabel : '功能编号',
 * 						xtype : 'textfield',
 * 						dataIndex : 'code',
 * 						name : 'code',
 * 						mapping : 'code'
 * 					}, {
 * 						fieldLabel : '功能名称 ',
 * 						xtype : 'textfield',
 * 						dataIndex : 'name',
 * 						name : 'name',
 * 						mapping : 'name'
 * 					}, {
 * 						fieldLabel : ' 功能描述 ',
 * 						xtype : 'textfield',
 * 						dataIndex : 'description',
 * 						name : 'description',
 * 						mapping : 'description'
 * 					}, {
 * 						fieldLabel : ' 是否可运行 ',
 * 						xtype : 'textfield',
 * 						dataIndex : 'runable',
 * 						name : 'runable',
 * 						mapping : 'runable'
 * 					}],
 * 			bodyStore : new Ext.data.JsonStore({
 * 						fields : ['code', 'name', 'description', 'runable']
 * 					}),
 * 			bodyColumns : [{
 * 						header : '组织编码',
 * 						dataIndex : 'code',
 * 						editor : new Ext.form.TextField()
 * 					}, {
 * 						header : '组织名称',
 * 						dataIndex : 'name',
 * 						editor : new Ext.form.ComboBox({
 * 									store : new Ext.data.SimpleStore({
 * 												fields : ['value', 'text'],
 * 												data : [['a', '早饭'],
 * 														['b', '午饭'],
 * 														['c', '晚饭']]
 * 											}),
 * 									valueField : 'value',
 * 									displayField : 'text',
 * 									emptyText : '请选择',
 * 									mode : 'local',
 * 									defaultReadOnly : true,
 * 									triggerAction : 'all'
 * 								})
 * 					}, {
 * 						header : '功能描述',
 * 						dataIndex : 'description',
 * 						xtype : 'checkcolumn'
 * 					}]
 * 		});
 * }
 * </code>
 * <p>
 * 查询
 * <code>
 * form.doQuery(Object);
 * </code>
 * 参数同Ext.Ajax.request()中的一样.
 * </p>
 * <p>
 * 赋值
 * <code>
 * form.setValues(Object / Array)
 * </code>
 * 赋值内容可以是js对象, 或者是js数组
 * </p>
 * <p>
 * 提交
 * <code>
 * form.doSubmit(Object);
 * </code>
 * 提交后数据能封装到2个后台变量中
 * ，一个是提交的发生变化对象(对应属性为 modifiedVarName)，一个是删除的原始子表数据数组或集合(对应属性为 detailVarName)
 * </p>
 * </pre>
 */
OECP.ui.MasterDetailEditPanel = Ext.extend(Ext.FormPanel, {
	/**
	 * @cfg {Boolean} editable 能否编辑; true:可编辑,false:不可编辑
	 */
	editable : true,
	/**
	 * 
	 * @cfg {String} submitUrl 单据保存url地址
	 */
	/**
	 * @cfg {String} queryUrl 单据查询url地址。
	 */
	/**
	 * @cfg {Ext.grid.EditorGridPanel} bodyGrid 表体网格
	 */
	/**
	 * @cfg {Array} bodyColumns 表体字段
	 */
	/**
	 * @cfg {Array} bodyStoreColumns 表体Store的Columns
	 */
	/**
	 * @cfg {Object} bodyRefs 表体参照，字段名对应相应的控件
	 *      <p>
	 *      例子:<br>
	 *      <code>{org:new OECP.ui.OrgRefField({...}),
	 *      quantity:new Ext.form.NumberField({...}),
	 *      status: new Ext.form.ComboBox({...})
	 *      }</code>
	 *      <p>
	 */
	/**
	 * @cfg {Ext.data.Store} bodyStore 表体数据
	 */
	/**
	 * @XXX 略显多余,如果添加,参数过多;如果不添加,需要使用者必须传入store给grid
	 * @cfg {String} bodyQueryUrl 表体数据url地址
	 */
	/**
	 * @XXX 略显多余,如果添加,参数过多;如果不添加,需要使用者必须传入store给grid
	 * @cfg {Object} bodyQueryParams 表体stor查询默认条件参数
	 */
	/**
	 * @cfg {Boolean} bodyMultiple 表体数据多选;true:多选;false:单选
	 */
	/**
	 * @cfg {Array} bodyBtns 表体自定义按钮
	 */
	/**
	 * @cfg {String} modifiedVarName 对应后台Action变量名称。数据封装使用。如果没有默认使用form属性中的前缀.
	 */
	/**
	 * @cfg {String} delVarName 对应后台Action变量名称。数据封装使用，如果没有默认使用detailVarName属性名称.
	 */
	/**
	 * @cfg {Array/Object} tailItem 表尾控件
	 */
	/**
	 * @cfg {String} detailVarName 类似JsonStroe 定义root,指明子表数据集名称.数据加载和提交时使用.
	 */
	/**
	 * @cfg {Array} bodyDelRecords 记录删除的子表数据
	 */
	bodyDelRecords : [],
	/**
	 * /**
	 * 
	 * @cfg {Boolean} showBodyNum 显示表体行号
	 */
	showBodyNum : true,
	/**
	 * @cfg {Boolean} showBodyCheckbox 显示表体选择器;默认为true，显示。
	 */
	showBodyCheckbox : true,
	/**
	 * @cfg {Ext.Button} rowAddBtn 表体增行按钮
	 */
	rowAddBtn : new OECP.ui.button.RowAddButton(),
	/**
	 * @cfg {ext.Button} rowDelBtn 表体删行按钮
	 */
	rowDelBtn : new OECP.ui.button.RowDelButton(),
	/**
	 * @cfg {String} state 单据界面状态;'add':增加状态,'edit':编辑状态
	 */
	state : 'add',
	/**
	 * @cfg {Object} stateEnum 表单界面状态码
	 */
	stateEnum : {
		add : 'add',
		edit : 'edit'
	},
	/**
	 * @cfg {String} rowState 行状态字段名,表体行状态判断使用
	 */
	rowState : 'row_state',
	/**
	 * @cfg {Object} rowStateEnum 行状态的枚举值
	 */
	rowStateEnum : {
		normal : 'normal',
		add : 'add',
		del : 'del',
		update : 'update'
	},
	/**
	 * @cfg {Array} submitFilterField 过滤字段，数组内的字段名不提交
	 */
	submitFilterField : [],
	// private
	// 获取Columns中的编辑器和xtype
	findField : function(obj) {
		var rs = {
			xtype : null,
			field : null
		};
		if (!obj) {
			return rs;
		}
		if (obj.xtype || Ext.isFunction(obj.getXType)) {
			rs.xtype = obj.xtype || obj.getXType();
			rs.field = obj;
		} else if (obj.editor
				&& (obj.editor.xtype || Ext.isFunction(obj.editor.getXType))) {
			rs.xtype = obj.editor.xtype || obj.editor.getXType();
			rs.field = obj.editor;
		} else if (obj.editor
				&& obj.editor.field
				&& (obj.editor.field.xtype || Ext
						.isFunction(obj.editor.field.getXType))) {
			rs.xtype = obj.editor.field.xtype || obj.editor.field.getXType();
			rs.field = obj.editor.field;
		}
		return rs;
	},
	// private store 添加一列
	storeAddField : function(store, field) {
		var _store = store;
		var _field = field;
		if (typeof field == 'string') {
			_field = new Ext.data.Field({
						name : field
					});
		}
		_store.recordType.prototype.fields.replace(_field);
		if (typeof _field.defaultValue != 'undefined') {
			store.each(function(r) {
						if (typeof r.data[_field.name] == 'undefined') {
							r.data[_field.name] = _field.defaultValue;
						}
					});
		}
		return _store;
	},
	// private form组件的renderer方法,获取显示值使用
	findDisplayValue : function(value, metaData, record, rowIndex, colIndex,
			store, rs) {
		if (!rs) {
			return value;
		} else if (!rs.xtype) {
			return value;
		}
		var field = rs.field;
		var fieldXType = rs.xtype;
		var val = '';
		switch (fieldXType) {
			case 'combo' :
			case 'reffield' :
				var record = field.findRecord(field.valueField, value);
				if (record) {
					val = record.data[field.displayField];
				}
				break;
			case 'datefield' :
			case 'datetimefield' :
				val = field.formatDate(value);
				break;
			default :
				val = value;
		}
		return val;
	},
	/**
	 * 更新行状态
	 * 
	 * @param {}
	 *            row 行号
	 * @param {}
	 *            type 类型
	 */
	updateRowState : function(row, type) {
		this.bodyStore.getAt(row).data[scope.rowState] = type;
	},
	// private 更新行状态
	rowValidateEdit : function(e, scope) {
		var _originalValue = e.originalValue || '';
		var _value = e.value || '';
		var _rowstate = e.grid.getStore().getAt(e.row).data[scope.rowState];
		if (_originalValue != _value && _rowstate == scope.rowStateEnum.normal) {
			// 如果值发生变化并且数据不是正常属性。就设置为update
			e.grid.getStore().getAt(e.row).data[scope.rowState] = scope.rowStateEnum.update;
		}
	},
	/**
	 * private 由于后台EO没有数据状态属性，无法获取哪些是新增/修改那些是删除的数据，所以提交br>
	 * 用于保存的数据，用于删除的数据两部分，后台分别进行处理
	 */
	formatSubmitData : function() {
		var scope = this;
		var flag = true;
		var _params = {}, _headData = this.getForm().getValues(), varName = this.modifiedVarName;
		// 表头数据封装
		for (_id in _headData) {
			if (!Ext.isEmpty(_headData[_id], true) && _headData[_id] !== "") {//过滤空值字段
				flag = true;
				for (var z = 0; z < scope.submitFilterField.length; z++) {
					if (_id == scope.submitFilterField[z]) {
						flag = false;
						break;
					}
				}
				if (flag) {
					_params[varName + '.' + _id] = _headData[_id];
				}
			}
		}

		// 表体数据加载
		var _bodyStore = scope.bodyGrid.getStore(), index = 0;
		_bodyStore.each(function(record) {
			var values = record.data;
			for (_id2 in values) {
				flag = true;
				for (var i = 0; i < scope.submitFilterField.length; i++) {
					if ((scope.detailVarName + "." + _id2) == scope.submitFilterField[i]) {
						flag = false;
						break;
					}
				}
				if (_id2 != scope.rowState && flag) {// 去除行状态字段
					// 去除过滤字段
					_params[varName + '.' + scope.detailVarName + '[' + index + '].' + _id2] = values[_id2];
				}
			}
			index += 1;
		});
		// 删除数据加载
		if (scope.bodyDelRecords.length > 0) {
			var _delVarName = scope.delVarName || scope.detailVarName;
			for (var i = 0; i < scope.bodyDelRecords.length; i++) {
				var _data = scope.bodyDelRecords[i].data;
				for (id in _data) {
					_params[_delVarName + '[' + i + '].' + id] = _data[id];
				}
			}
		}
		return _params;
	},
	/**
	 * 表单提交 <br>
	 * 对数据封装,使用ajax提交.<br>
	 * 不使用Form提交是考虑到store中的内容可以不用展现在页面上(如主键、单据状态等字段).
	 * 
	 * @param {}
	 *            options 参数对象
	 *            参考Ext.Ajax.request的options参数,其中success,failure方法追加了作用域作为参数
	 */
	doSubmit : function(options) {
		if (!options) {
			options = {};
		}
		var scope = this;
		var _params = this.formatSubmitData();
		var _success = null;
		// 存在success函数就追加个scope参数
		if (options.success && Ext.isFunction(options.success)) {
			_success = options.success.createDelegate(this, [scope], true);// 追加个参数
		} else {
			_success = function(response, opts) {
				scope.fireEvent('submitsuccess', response, opts, scope);
			};
		}
		var _failure = null;
		// 存在failure函数就追加个scope参数
		if (options.failure && Ext.isFunction(options.failure)) {
			_failure = options.failure.createDelegate(this, [scope], true);
		} else {
			_failure = function(response, opts) {
				if (scope.fireEvent('submitfailure', response, opts, scope) === false) {
					return;
				}
				Ext.ux.Toast.msg("信息", '加载失败！请联系管理员。');
			};
		}
		// 追加参数
		if (options.params) {
			for (var _id in options.params) {
				_params[_id] = options.params[_id];
			}
		}
		// ajax 提交
		Ext.Ajax.request({
					method : 'post',
					params : _params || {},
					url : scope.submitUrl || '',
					success : _success,
					failure : _failure
				});
	},
	/**
	 * 查询
	 * 
	 * @param {Object}
	 *            options
	 *            参考Ext.Ajax.request的options参数,其中success,failure方法追加了作用域作为参数
	 */
	doQuery : function(options) { 
		Ext.Ajax.request(Ext.apply({ 
				url:options.url || this.queryUrl || '',
				success:function(response,opts) {
					var json = Ext.util.JSON.decode(response.responseText);
					if (json.success){this.setValues(json.result);}else{Ext.Msg.alert("错误",json.msg);}
				},
				failure:function(response,opts) {Ext.ux.Toast.msg("信息", '加载失败！请联系管理员。');},
				scope:this
			},options));
	},
	/**
	 * 界面赋值
	 * 
	 * @param {Array/Object}
	 *            对象或数组,数据格式参照BasicForm中的setValues注释
	 * 
	 * @return {MasterDetailEditPanel} this
	 */
	setValues : function(values) {
		var scope = this;
		var detailData = null;
		scope.dataClear();// 清空数据
		if (Ext.isArray(values)) { // 判断是数组还是对象，获取子表明细
			for (var i = 0, len = values.length; i < len; i++) {
				var v = values[i];
				if (scope.detailVarName == v.id) {
					detailData = v.value;
					v.value = null;
					break;
				}
			}
		} else {
			detailData = values[scope.detailVarName];
//			values[scope.detailVarName] = null;// 清空
		}

		if (this.reader) {// 判断是否有render,对数据进行转换
			var rs = this.reader.readRecords(values);
			values = rs.records && rs.records[0] ? rs.records[0].data : null;
		}
		this.getForm().setValues(values);
		if (Ext.isDefined(detailData)) {
			this.bodyGrid.getStore().loadData(detailData, false);// 子表表格赋值
		}
		scope.fireEvent('loaddata', scope);
	},
	/**
	 * 设置组件编辑,并添加renderer方法。
	 * 
	 * @param {Boolean}
	 *            editable true:可编辑,false:不可编辑
	 */
	setEditable : function(editable) {
		var scope = this;
		var items = this.bodyGrid.getColumnModel().config;// 列组件数组
		for (var i = 0; i < items.length; i++) {
			var rs = {
				xtype : null,
				field : null
			};
			rs = scope.findField(items[i]);// 获取xtype和控件
			if (!Ext.isEmpty(rs.xtype, false)) {
				// checkcolumn 控件自带renderer函数，不添加。
				if (items[i].renderer && rs.xtype && rs.xtype != 'checkcolumn') {
					items[i].renderer = scope.findDisplayValue.createDelegate(this, [rs], true);
				}
				if (editable) {// 可编辑
					if (rs.xtype == 'checkcolumn') {
						// XXX 通过事件控制对checkcolumn进行编辑控制，应该封装一个column
						// 对editable单据进行处理
						// 添加原有事件。
						rs.field.processEvent = function(name, e, grid,
								rowIndex, colIndex) {
							if (name == 'mousedown') {
								var record = grid.store.getAt(rowIndex);
								record.set(this.dataIndex,
										!record.data[this.dataIndex]);
								return false; // Cancel row selection.
							} else {
								return Ext.grid.ActionColumn.superclass.processEvent
										.apply(this, arguments);
							}
						};
					} else {
						var flag = editable;
						if (Ext.isDefined(rs.field.defaultReadOnly)) {
							flag = !rs.field.defaultReadOnly;
						}
						rs.field.setReadOnly(!flag);
					}
				} else {// 不可编辑
					if (rs.xtype == 'checkcolumn') {
						// 去除基类中对mousedown事件的逻辑
						rs.field.processEvent = function(name, e, grid,
								rowIndex, colIndex) {
							return Ext.grid.ActionColumn.superclass.processEvent
									.apply(this, arguments);
						};
					} else {
						rs.field.setReadOnly(true);
					}
				}
			}
		}
		scope.editable = editable;
	},
	// private 设置单据状态
	setPanelState : function(_state) {
		if (!_state && !this.stateEnum[_state]) {
			return;
		}
		if (_state == this.stateEnum.add) {
			this.state = _state;
			this.dataClear();
		} else if (_state == this.stateEnum.edit) {
			this.state = _state;
		}
	},
	/**
	 * 清空数据
	 */
	dataClear : function() {
		this.bodyDelRecords = [];
//		if (!Ext.isEmpty(this.getForm())) {
//			this.getForm().reset();
//		}
		if (!Ext.isEmpty(this.getForm()) && this.getForm().getEl() && this.getForm().getEl().dom) {
			this.getForm().getEl().dom.reset();
		}
		this.bodyStore.removeAll();
		this.bodyStore.modified = [];
	},
	// 删行事件
	delRowAction : function(btn, e, scope) {
		var rows = scope.bodyGrid.getSelectionModel().getSelections();
		var _e = {
			scope : scope,
			grid : scope.bodyGrid,
			record : rows,
			btn : btn,
			btnEvent : e
		};
		if (scope.on('beforerowdel', _e) === false) {
			return;
		}
		if (rows.length <= 0) {
			Ext.Msg.alert("信息", "请选择一条记录！");
		} else {
			for (var i = 0; i < rows.length; i++) {
				scope.bodyGrid.getStore().remove(rows[i]);
				if (rows[i].data[this.rowState] != this.rowStateEnum.add) {
					this.bodyDelRecords.push(rows[i]);
				}
			}
		}
		scope.bodyGrid.getView().refresh();//刷新规避行号错误
		scope.fireEvent('afterrowadd', e);
	},
	// 增行事件
	addRowAction : function(btn, e, scope) {
		var _data = {};
		_data[this.rowState] = this.rowStateEnum.add;
		var _record = new scope.bodyStore.recordType(_data);// 获取一个空record
		var _e = {
			scope : scope,
			grid : scope.bodyGrid,
			record : _record,
			btn : btn,
			btnEvent : e
		};
		if (scope.fireEvent('beforerowadd', _e) === false) {
			return;
		}
		var row = scope.bodyGrid.getStore().getCount();
		scope.bodyGrid.stopEditing();
		scope.bodyGrid.getStore().insert(row, _record);
		scope.bodyGrid.startEditing(0, 0);
		scope.fireEvent('afterrowadd', e);

	},
	initComponent : function() {
		var scope = this;
		this.addEvents(
				/**
				 * @event submitsuccess 提交成功事件
				 * @param {Object}
				 *            response 包含数据的xhr对象
				 * @param {Object}
				 *            opts 请求所调用的参数
				 * @param {MasterDetailEditPanel}
				 *            scope this
				 */
				'submitsuccess',
				/**
				 * @event submitfailure 提交失败事件 *
				 * @param {Object}
				 *            response 包含数据的xhr对象
				 * @param {Object}
				 *            opts 请求所调用的参数
				 * @param {MasterDetailEditPanel}
				 *            scope this
				 */
				'submitfailure',
				/**
				 * @event beforerowadd 增行前事件
				 * @param {Object}
				 *            event <br>
				 *            事件源包含以下属性 <br>
				 *            scope - 作用域编辑面本本身<br>
				 *            grid - 表体网格<br>
				 *            record - 要增加的数据集<br>
				 *            btn - 按钮<br>
				 *            btnEvent - 按钮事件源
				 */
				'beforerowadd',
				/**
				 * @event afterrowadd 增行后事件
				 * @param {Object}
				 *            event <br>
				 *            事件源同beforerowadd
				 */
				'afterrowadd',
				/**
				 * @event beforerowdel 删行前事件
				 * @param {Object}
				 *            event <br>
				 *            事件源包含以下属性 <br>
				 *            scope - 作用域编辑面本本身<br>
				 *            grid - 表体网格<br>
				 *            record - 要增加的数据集数组<br>
				 *            btn - 按钮<br>
				 *            btnEvent - 按钮事件源
				 */
				'beforerowdel',
				/**
				 * @event afterrowdel 删行后事件
				 * @param {Object}
				 *            event <br>
				 *            事件源同beforerowdel
				 */
				'afterrowdel',
				/**
				 * @event loaddata 加载数据
				 * @param {this}
				 *            scope
				 */
				'loaddata');
		if (!scope.bodyStore) {
			// FIXME UI内参数过多。这部分可以考虑省略。使用者直接提供store。
			// 分2次加载数据 繁琐。使用setValues赋值即可。
			var _fields = scope.bodyStoreColumns;
			scope.bodyStore = new Ext.data.JsonStore({
						url : scope.bodyQueryUrl || '',
						baseParams : scope.bodyQueryParams || null,
						root : 'result',
						fields : _fields,
						autoLoad : true
					});
		}

		this.storeLoaded = false;
		// FIXME 如果直接修改store或使用before事件会出现数据无法加载的情况，暂时未找到好的解决方法
		this.bodyStore.on('load', function(store, records, obj) {
					if (!scope.storeLoaded) {
						store = scope.storeAddField(store, new Ext.data.Field({
											name : scope.rowState,
											defaultValue : scope.rowStateEnum.normal
										}));
						// @XXX reader的ef属性缺少row_static方法,重加载一下,否则再次赋值时会报错
						store.reader.ef = undefined;
						store.reader.buildExtractors();
						scope.storeLoaded = true;
					}
				});
		this.setPanelState(this.state);
		if (!this.bodyGrid) {
			// 选择器
			var _sm = new Ext.grid.CheckboxSelectionModel({
						singleSelect : scope.bodyMultiple,
						hidden : !scope.showBodyCheckbox
					});
			// 行号
			var _rownum = new Ext.grid.RowNumberer({
						hidden : !scope.showBodyNum
					});
			var _bodycolumns = [_sm, _rownum];
			if (this.bodyColumns) {
				for (var i = 0; i < scope.bodyColumns.length; i++) {
					_bodycolumns.push(scope.bodyColumns[i]);
				}
			}
			// 表体按钮事件
			this.rowAddBtn.on('click', scope.addRowAction.createDelegate(this,
							[scope], true));
			this.rowDelBtn.on('click', scope.delRowAction.createDelegate(this,
							[scope], true));
			var _baritems = [scope.rowAddBtn, scope.rowDelBtn];
			// 追加自定义按钮
			if (scope.bodyBtns) {
				for (var i = 0; i < scope.bodyBtn.length; i++)
					_baritems.push(scope.bodyBtns[i]);
			}
			var _tbar = new Ext.Toolbar({
						bodyStyle : 'text-align:left',
						items : _baritems
					});
			// 初始化子表编辑框体
			this.bodyGrid = new Ext.grid.EditorGridPanel({
						layout : 'fit',
						autoScroll : true,
						height : 200,
						store : scope.bodyStore,
						clicksToEdit : 1,
						sm : _sm,
						cm : new Ext.grid.ColumnModel(_bodycolumns),
						tbar : _tbar
					});
		}
		// 追加编辑事件,如果单元格数值发生变化更新为update状态
		this.bodyGrid.on('validateedit', this.rowValidateEdit.createDelegate(
						this, [scope], true));
		this.items.push(scope.bodyGrid);
		if (scope.tailItem) {
			this.items.push(scope.tailItem);
		}
		this.setEditable(scope.editable);
		OECP.ui.MasterDetailEditPanel.superclass.initComponent.call(this);
	}
});
