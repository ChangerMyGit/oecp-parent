/**
 * @author wangliang
 * @class OECP.ui.QueryWindow
 * @extends Ext.Window
 *          <p>
 *          查询对话框
 *          </p>
 * 例子：
 * <pre><code>
			var queryWindow = new OECP.ui.QueryWindow({
						fieldData : [['name', '名称','java.lang.String'], ['code', '编码','java.lang.String'],['type','产品类型','java.lang.String']],
						persOperator:{name:['=','like'],code:['<','>']},
						fieldDefaultValue: {name : '哈密瓜',code : 'a'},
						defaultCondition : {corp:'1001'},
						refs : {
							code : new Ext.grid.GridEditor(new Ext.form.ComboBox(
									{
										store : new Ext.data.SimpleStore({
													fields : ['value', 'text'],
													data : [['a', '早饭'],
															['b', '午饭'],
															['c', '晚饭']]
												}),
										valueField : 'value',
										displayField : 'text',
										mode : 'local',
										editable : false,
										triggerAction : 'all'
									}))
						}
					});
			queryWindow.on('afterquery', function(query) {
						if (typeof query.conditionResult != 'undefined') {
							alert(Ext.encode(query.conditionResult));
							return false;
						}
					});
			var btn = new Ext.Button({
						text : '查询',
						renderTo : Ext.getBody(),
						handler : function() {
							queryWindow.show();
						}
					});
 * </code></pre>
 * 
 * @param conditionResult
 *            封装后的参数对象<br>
 *            <p>
 *            对象样式<br>
 * <pre><code>
		{
			"condition[0].field" : "code",
			"condition[0].operator" : "<",
			"condition[0].value" : "a",
			"condition[0].fieldType" : "java.lang.String",
			"condition[1].field" : "name",
			"condition[1].operator" : "=",
			"condition[1].value" : "哈密瓜",
			"condition[1].fieldType" : "java.lang.String",
			"condition[2].field" : "type",
			"condition[2].operator" : "=",
			"condition[2].value" : "水果",
			"condition[2].fieldType" : "java.lang.String",
			"corp" : "1001"
		}
 * </code></pre>
 * </p>
 */
Ext.ns('OECP.ui.grid');
/**
 * 自定义一个ColumnModel，用于根据列的下标获取返回控件
 * 
 * @param {} 	grid gridPanel
 * @param {}	 store 数据仓库
 * @param {} 	column 列
 * @param {} 	ref 自定义参照数组 obj 类似下列
 * 
 * <pre><code>
 * 			{ 
 * 				'GENDER':new Ext.grid.GridEditor({
 * 					store:new Ext.data.SimpleStore({
 * 					fields:['value','text'],
 * 					data:	[['0100','男'],['0101','女']]
 * 					}),
 * 					emptyText:'请输入',
 * 					mode:'local',
 * 					triggerAction:'all',
 * 					valueField:'value',
 * 					displayField:'text',
 * 					readOnly:true
 * 			})},
 * 			'AGE',new Ext.grid.GridEditor(new Ext.form.NumberField({selectOnFocus:true,style:'text-align:left;'}))
 * 			}
 * </code></pre>
 */
OECP.ui.grid.QueryColumnModel=function(win,store,column,ref) {
	this.win=win,this.store=store,this.customEditors=ref;
	OECP.ui.grid.QueryColumnModel.superclass.constructor.call(this,column);
};
Ext.extend(OECP.ui.grid.QueryColumnModel,Ext.grid.ColumnModel,{
	/**
	 * 重载父类方法 返回编辑器中定义的单元格/列.
	 * 
	 * @param {Number}
	 *            colIndex 列索引
	 * @param {Number}
	 *            rowIndex 行索引
	 * @return {Ext.Editor} The {@link Ext.Editor Editor} that was
	 *         created to wrap the {@link Ext.form.Field Field} used to
	 *         edit the cell.
	 */
	getCellEditor:function(colIndex,rowIndex) {
		var _editor;
		switch(colIndex)  {
			case 2:
				this.updateOperator(rowIndex);
				_editor=this.config[colIndex].getCellEditor(rowIndex);
			break
			case 3:
				var p=this.store.getAt(rowIndex),field_str=p.data.field;
				if (this.customEditors && this.customEditors[field_str])  _editor= this.customEditors[field_str];	// 返回自定义编辑器
		     	break
		   }
		return _editor|| this.config[colIndex].getCellEditor(rowIndex);
	},
	// private 更新下拉菜单store
	updateOperator:function(row) {
		var _wc=this.win.operatorCombo;
		var _record =this.win.queryGrid.getStore().getAt(row),_cs=this.win.columnsStore;
		var _fieldname=_record.get(_cs[0][0]),_operator=_record.get(_cs[1][0]);// 条件
		var _operatordata=[],_po=this.win.persOperator;
		if(Ext.isDefined( _po[_fieldname])&& _po[_fieldname].length>0){// 获取个性条件
			for(var j=0;j<_po[_fieldname].length;j++){
				for(var i=0;i<this.win.operatorData.length;i++){
					if(this.win.operatorData[i][0]===_po[_fieldname][j]){
						_operatordata.push(this.win.operatorData[i]);
						break;
					}
				}
			}
		}

		if (Ext.isEmpty(_operatordata)) 	_operatordata=this.win.operatorData;
		_wc.getStore().loadData(_operatordata);
		// 判断已经选择的条件符号是否存在，如不存在，默认选择第一个条件。
		var _exists=false;
		for (var i=0; i<_operatordata.length; i++) {
			if (_operator==_operatordata[i][0]) {
				_exists=true;
				break;
			}
		}
		if (!_exists) {
			this.win.queryGrid.getStore().data.items[row].data[_cs[1][0]]=_operatordata[0][0];
			_wc.setValue(_operatordata[0][0]);
		}
	}
});
		
Ext.ns('OECP.ui');
OECP.ui.QueryWindow=Ext.extend(Ext.Window,{
	/**
	 * @cfg {Object} refs 参照列表<br>
	 *      key:对应Grid中的Column<br>
	 *      value:对应自定义参照<br>
	 *      <code>
	 *      {
	 *      name: new OECP.grid.RefGridEditor(new OECP.ref.OrgRef()),
	 *		number:new Ext.grid.GridEditor(new Ext.form.NumberField()),
	 *		date:new Ext.grid.GridEditor(new Ext.form.DateField()),
	 *		combo:new Ext.grid.GridEditor(new Ext.form.ComboBo())
	 *      }</code>
	 * 
	 */
	/**
	 * @cfg {Ext.form.ComboBox} fieldCombo 字段下拉控件
	 */
	/**
	 * @cfg {Ext.form.ComboBox} operatorCombo 条件下拉控件
	 */
	title:'查询',
	height:400,
	width:500,
	/**
	 * @cfg {Boolean} 背景窗口灰化
	 */
	modal:true,
	/**
	 * @cfg {Ext.grid.EditorGridPanel} queryGrid 查询表格,查询字段、条件、值录入控件
	 */
	/**
	 * @cfg {Ext.data.Store} queryStore 查询表格的Store
	 */
	/**
	 * @cfg {object} 查询属性数据源,二维数组
	 * 
	 * <pre><code>
	 * [['code','编码'],['name','名称']]
	 * </code></pre>
	 */
	fieldData:[],
	/**
	 * @cfg {Array} columnsStore 字段dataIndex和header属性
	 * 其中字段类型为隐藏字段，后台根据类型进行数据转换
	 */
	columnsStore:[['field','属性名称'],['operator','条件'],['value','属性值'],['fieldType','字段类型']],
	/**
	 * @cfg {Object}  fieldDefaultValue 字段默认值
	 * */
	fieldDefaultValue:{},
	/**
	 * @cfg {Object} persOperator 特有的查询条件。<br>
	 *      例如 name属性默认条件只有=、like。age属性只有>、<条件 <br>
	 *      <code>
	 *      {name:['=','like'],age:['>','<',']}
	 *      </code>
	 */
	persOperator:{},
	/**
	 * 
	 * @cfg {Array} 默认的查询条件
	 */
	operatorData:[['>','大于'],['<','小于'],['>=','大于或等于'],['<=','小于或等于'],['=','等于'],['<>','不等于'],['like','包含'],['not like','不包含'],['like ','开头等于'],[' like','结尾等于'],['not like ','开头不等于'],[' not like','结尾不等于']],
	/**
	 * @cfg {Array} 查询默认值 最终会拼装到返回结果中
	 * <pre><code>
	 * {id:'10010',corp:'1001' }
	 * </code>
	 */
	defaultCondition:undefined,
	/**
	 * @cfg {Array} 查询后返回值
	 * 
	 * 数据格式:
	 * 
	 * <pre><code>
	 * { 'condition[0].field':'abc',
	 * 'condition[0].operator':'=',
	 * 'condition[0].value':'10010',
	 * 'condition[1].field':'aaaaaaaa',
	 * 'condition[1].operator':'&gt;=',
	 * 'condition[1].value':'20010'}
	 * </code></pre>
	 */
	conditionResult:undefined,
	/**
	 * @cfg {string} 对应后台查询条件数组名称<br>
	 * 
	 * <pre>
	 * 如后台Action中定义了
	 * <code>
	 *      List&lt;QueryCondition&gt; abc=new ArrayList&lt;QueryCondition&gt;();
	 * </code>
	 *      就需要把conditionKey属性设置为'abc'
	 * </pre>
	 */
	conditionKey:'condition',
	/**
	 * @cfg {Boolean} window 关闭模式
	 */
	closeAction:'hide',
	// init
	initComponent:function() {
		var scope=this;
		this.addEvents(
				/**
				 * @event beforequery 查询前事件
				 * 
				 * @param {QueryWindow}
				 *            this
				 * @return {boolean} true:提交查询操作,false:不提交查询操作
				 */
				'beforequery',
				/**
				 * @event afterquery 查询后事件
				 * @param {QueryWindow}
				 *            this
				 */
				'afterquery',
				/**
				 * @event addbuttonclick “增加条件”按钮点击事件
				 * @param {button}
				 *            this
				 * @param {event}
				 *            e
				 */
				'addbuttonclick',
				/**
				 * @event delbuttonclick “删除条件”按钮点击事件
				 * @param {button}
				 *            this
				 * @param {event}
				 *            e
				 */
				'delbuttonclick',
				/**
				 * @event closebuttonclick “关闭”按钮点击事件
				 * @param {QueryWindows}
				 *            this
				 */
				'closebuttonclick');
		var sm=new Ext.grid.CheckboxSelectionModel({handleMouseDown:Ext.emptyFn});
		var _cfg={valueField:'value',displayField:'text',mode:'local',editable:false,triggerAction:'all'};
		// 属性下拉框
		if (!scope.fieldCombo) {
			scope.fieldCombo=new Ext.form.ComboBox({
						store:new Ext.data.SimpleStore({	fields:['value','text'],data:scope.fieldData}),
						valueField:'value',
						displayField:'text',
						mode:'local',
						editable:false,
						triggerAction:'all'
					});
		}
		// 条件下拉框
		if (!scope.operatorCombo) {
			scope.operatorCombo=new Ext.form.ComboBox({
						store:new Ext.data.SimpleStore({
									fields:['value','text'],
									data:scope.operatorData
								}),
						valueField:'value',
						displayField:'text',
						mode:'local',
						editable:false,
						triggerAction:'all'
					});
		}
		var column=[sm,{
					header:scope.columnsStore[0][1],
					dataIndex:scope.columnsStore[0][0],
					editor:new Ext.grid.GridEditor(scope.fieldCombo),
					renderer:function(k,j,g,i,l) {return scope.attributeRenderer(scope.columnsStore[0][0],k);	}
				},{
					header:scope.columnsStore[1][1],
					dataIndex:scope.columnsStore[1][0],
					editor:new Ext.grid.GridEditor(scope.operatorCombo),
					renderer:function(k,j,g,i,l) {return scope.attributeRenderer(scope.columnsStore[1][0],k,j);}
				},{
					header:scope.columnsStore[2][1],
					dataIndex:scope.columnsStore[2][0],
					editor:new Ext.grid.GridEditor(new Ext.form.TextField({selectOnFocus:true})),
					renderer:function(k,j,g,i,l) {
						var ref=null;
						if (scope.refs && scope.refs[g.data.field]) {
							ref=scope.refs[g.data.field];
						}
						return scope.attributeRenderer(scope.columnsStore[2][0],k,ref);
					}
				},{
					hidden:true,
					header:scope.columnsStore[3][1],
					dataIndex:scope.columnsStore[3][0],
					editor:new Ext.grid.GridEditor(new Ext.form.TextField({selectOnFocus:true}))
				}];
		if (!scope.queryGrid) {
			// 初始化查询条件
			if (!scope.queryStore) {
				/**
				 * 数据格式: <code> [['名称','=','10010'],['编码','=','迪巧']]</code>
				 */
				var gdata=[];
				for (var i=0; i<scope.fieldData.length; i++) {
					var tmp=[],_defOper=this.persOperator[scope.fieldData[i][0]];
					tmp.push(scope.fieldData[i][0]);
					if(_defOper) {//判断有自有条件符号
						tmp.push(_defOper[0]);
					}else{
						tmp.push('=');
					}
					if(this.fieldDefaultValue){
						tmp.push(this.fieldDefaultValue[scope.fieldData[i][0]]||'');
					}else tmp.push('');
					tmp.push(scope.fieldData[i][2]);
					gdata[i]=tmp;
				}
				scope.queryStore=new Ext.data.SimpleStore({
							fields:[{name:scope.columnsStore[0][0]},
								{name:scope.columnsStore[1][0]},
								{name:scope.columnsStore[2][0]},
								{name:scope.columnsStore[3][0]}],
							data:gdata
						});
			}

			this.queryGrid=new Ext.grid.EditorGridPanel({
				height:scope.height-40,
				store:scope.queryStore,
				enableHdMenu:false,
				clicksToEdit:1,
				sm:sm,
				cm:new OECP.ui.grid.QueryColumnModel(this,scope.queryStore,column,scope.refs || null),
				tbar:new Ext.Toolbar(['-',{text:' 添加条件',handler:_onAddClick.createDelegate(this)	},'-',{text:'删除条件',handler:_onDelClick.createDelegate(this)},'-']),
				listeners:{
					'afteredit':function(e) {
						// 条件字段变化后,清空查询值字段
						if (e.column==1 && e.originalValue != e.value) {
							var _record=scope.queryGrid.getStore().getAt(e.row);
							var _fieldName=_record.get(scope.columnsStore[0][0]);
							if(scope.persOperator[_fieldName]){//更新查询条件
								_record.set(scope.columnsStore[1][0],scope.persOperator[_fieldName][0]);
							}else{
								_record.set(scope.columnsStore[1][0],'=');
							}
							if(scope.fieldDefaultValue[_fieldName]){
								_record.set(scope.columnsStore[2][0],scope.fieldDefaultValue[_fieldName]);//默认值
							}else{
								_record.set(scope.columnsStore[2][0],'');//清空查询内容
							}
							for(var i=0;i<scope.fieldData.length;i++){//更新字段类型
								if(scope.fieldData[i][0]==e.value){
									_fieldType=scope.fieldData[i][2];
									break;
								}
							}
							_record.set(scope.columnsStore[3][0],_fieldType);
							scope.queryGrid.view.refresh(); //刷新解决界面脏数据问题。
						}
					}
				}
			});
		}
		/**
		 * “添加条件”按钮事件:查询界面最后一行添加一条记录
		 */
		function _onAddClick() {
			var row=scope.queryGrid.getStore().data.length,_cs=scope.columnsStore;
			var _data={};
			_data[_cs[0][0]]=scope.fieldData[0][0];_data[_cs[1][0]]='=',_data[_cs[2][0]]='',	
			_data[_cs[3][0]]=scope.fieldData[0][3];//类型
			if(this.persOperator && this.persOperator[scope.fieldData[0][0]]){
				_data[_cs[1][0]]=this.persOperator[scope.fieldData[0][0]][0]//查询条件
			}
			if(this.fieldDefaultValue){
				for(var _name in this.fieldDefaultValue){
					if(_name===scope.fieldData[0][0]) _data[_cs[2][0]]=this.fieldDefaultValue[_name];
				}
			}
			var pr=new scope.queryStore.recordType(_data);
			scope.queryGrid.stopEditing();
			scope.queryGrid.getStore().insert(row,pr);
			scope.queryGrid.startEditing(0,0);
		}
		/**
		 * “删除条件”按钮事件，删除选定条件
		 */
		function _onDelClick() {
			var rows=scope.queryGrid.getSelectionModel().getSelections();
			if (rows.length <= 0) {
				Ext.Msg.alert("信息","请选择一条记录！");
			} else {
				for (var i=0,r; r=rows[i]; i++) {
					scope.queryGrid.getStore().remove(r);
				}
			}
		}
		this.items=[this.queryGrid];
		if (!this.buttons) {
			this.buttons=[{
				text:"查询",
				listeners:{
					'click':this.formatReturnValue.createDelegate(this)
				}
			},{
				text:"关闭",
				listeners:{
					'click':function(btn,e) {
						this.fireEvent('closebuttonclick',scope);
						scope[scope.closeAction]();
					}
				}
			}];
		}
		OECP.ui.QueryWindow.superclass.initComponent.call(this);
	},
	//格式化返回值。
	formatReturnValue:function(){
		var scope=this,j=0;
		if (scope.fireEvent('beforequery',this) === false)  return;
		// 获取查询数据
		var qStore=scope.queryGrid.getStore();
		scope.conditionResult={};
		var _val=scope.conditionResult,_key=scope.conditionKey,_cs=scope.columnsStore;
		for (var i=0; i<qStore.getCount(); i++) {
			var r=scope.queryStore.getAt(i);
			if (Ext.isDefined(r.get(_cs[2][0]))) {
				_val[_key+'['+j+'].field']=r.get(_cs[0][0]);
				_val[_key+'['+j+'].operator']=r.get(_cs[1][0]);
				_val[_key+'['+j+'].value']=r.get(_cs[2][0]);
				_val[_key+'['+j+'].fieldType']=r.get(_cs[3][0]);
				j += 1;
			};
		}
		if (Ext.isDefined(scope.defaultCondition)) {
			scope.conditionResult=Ext.applyIf(scope.conditionResult,scope.defaultCondition);
		}
		scope.fireEvent('afterquery',scope);
		scope[scope.closeAction]();
	},
	// private 返回编码对应的显示值
	attributeRenderer:function(fieldName,val,e){
		var scope=this;
		if (fieldName==scope.columnsStore[1][0]){
			for (var i=0; i<this.operatorData.length; i++) {
				if (this.operatorData[i][0]==val) {
					val=this.operatorData[i][1];
				}
			}
		} else if (fieldName==scope.columnsStore[0][0]) {
			for (var i=0; i<this.fieldData.length; i++) {
				if (this.fieldData[i][0]==val){
					val=this.fieldData[i][1];
				}
			}
		} else if (fieldName==scope.columnsStore[2][0]) {
			// 参照数据转换
			if (e) {
				var ref=e.field;
				if (ref instanceof Ext.form.ComboBox) {
					var record=ref.findRecord(ref.valueField,val);
					if (record) {
						val=record.data[ref.displayField];
					} else {
						val='';
					}
				}else if(ref instanceof Ext.form.DateField){
					val=ref.formatDate(val) || val || '';
				}
			}
		}
		return val;
	}
});