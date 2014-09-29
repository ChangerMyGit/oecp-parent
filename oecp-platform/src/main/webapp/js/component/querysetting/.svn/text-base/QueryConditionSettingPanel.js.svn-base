/**
 * 查询条件设置panel
 * 可设置为只读。
 * 
 */
Ext.ns('OECP.query');
OECP.query.QueryConditionSettingPanel = Ext.extend(Ext.TabPanel,{
	
    /**
     * 查询条件gird设置
     * @cfg {Array} 
     * 示例：
     * cfgs : [
     * 	{title:'固定隐藏条件',prefix:'fixedconditions',readonly:false},
     *  {title:'常用条件',prefix:'commonconditions',readonly:false},
     *  {title:'其他可用条件',prefix:'otherconditions',readonly:false}
     * ]
     */
	cfgs 	: [],
	initComponent:function(){
		OECP.query.QueryConditionSettingPanel.superclass.initComponent.call(this);
		this.grids = [];
		for(var i=0 ;i< this.cfgs.length;i++){
			var cfg = this.cfgs[i];
			var grid = this.createConditionGrid(cfg.title,cfg.prefix,cfg.readonly);
			this.grids.push(grid);
			this.add(grid);
		}
	},
	setConditions : function(datas){
		for(var i=0; i<this.grids.length;i++){
			this.grids[i].setValues(datas[this.grids[i].prefix]);
		}
	},
	getConditions : function(){
		var gridDatas = {},_gv={};
		for(var i=0; i<this.grids.length;i++){
			_gv = this.grids[i].getValues();
			Ext.apply(gridDatas,_gv);
		}
		return gridDatas;
	},
	// private 创建各种类型的条件grid
	createConditionGrid : function(title,prefix,readOnly){
		var jstore = new Ext.data.JsonStore({
							storeId : 'id',
							fields : ['id','field', 'dispname', 'operators', 'defaultvalue', 'fieldType', 'required','editorcfg'],
							data : []
						});
		var sm = new Ext.grid.CheckboxSelectionModel();
		var grid = new Ext.grid.EditorGridPanel({
							tbar : [],
							title : title,
							store : jstore,
							autoScroll : true,
							height:360,
							prefix : prefix,
							getValues : function(){
								var _v = {};
								var grid_store = this.getStore();
								grid_store.each(function(record,i) {
											var values = record.data;
											for (_gkey in values) {
												// 操作符多选处理
												if(_gkey == "operators"){
													var opr_v = values[_gkey];
													if(!Ext.isEmpty(opr_v)){
														var arr_orp_v = opr_v.split(','),j=0;
														for(;j<arr_orp_v.length;j++){
															_v['qs.'+this.prefix+"["+i+"].operators[" +j +"]"] = arr_orp_v[j];
														}
													}
												}else{
													_v['qs.'+this.prefix+"["+i+"]." +_gkey] = values[_gkey];
												}
											}
										},this);
								return _v;
							},
							setValues : function(datas){
								datas = datas||[];
								var i=0;
								for(;i<datas.length;i++){
									if(datas[i].operators){// 将json中的operators数组变为lovcombo识别的字符串
										if(datas[i].operators instanceof Array )
											datas[i].operators = datas[i].operators.join(',');
									}
								}
								this.getStore().loadData(datas);
							},
							sm : sm,
							columns : [sm, {
										header : "字段名",
										dataIndex : "field",
										editor : {xtype:'textfield',allowBlank : false,readOnly:readOnly}
									}, {
										header : "显示名称",
										dataIndex : "dispname",
										editor : {xtype:'textfield',allowBlank : false,readOnly:readOnly}
									}, {
										header : "可用操作符",
										dataIndex : "operators",
										renderer : function(value, metadata, record, rowIndex, colIndex, store) {
											if(value){
												var editor = this.editor;
												var vals = value.split(',');
												for(vi in vals){
													rec = editor.findRecord(editor.valueField, vals[vi]);
													if(rec){
														vals[vi] = rec.get(editor.displayField);
													}else{
														vals[vi] = null;
													}
												}
												return vals.join(',');
											}else{
												return '';
											}
										},
										editor : {
											width : 60,
											xtype : prefix=='otherconditions'?'lovcombo':'combo',// 除其他可选条件外，固定条件和常用条件，都只允许选一个操作符
											valueField : 'value',
											displayField : 'name',
											emptyText : '请选择',
											mode : 'local',
											editable : false,
											readOnly:readOnly,
											triggerAction : 'all',
											store : {
												xtype : 'jsonstore',
												url : __ctxPath + "/enums/ref.do",
												root : 'result',
												fields : ['value', 'name'],
												autoLoad : true,
												baseParams : {
													className : 'oecp.platform.query.setting.enums.Operator'
												}
											}

										}
									}, {
										header : "默认值",
										dataIndex : "defaultvalue",
										editor : {xtype:'textfield',readOnly:readOnly}
									}, {
										header : "字段类型",
										dataIndex : "fieldType",
										editor :
										{xtype:'textfield',allowBlank : false,readOnly:readOnly}
									}, {
										header : "是否必填条件",
										dataIndex : "required",
										readOnly:readOnly,
										editable:prefix=='fixedconditions'?false:true,
										processEvent : function(name, e, grid, rowIndex, colIndex) {
											if(prefix=='fixedconditions'){
												return Ext.grid.ActionColumn.superclass.processEvent.apply(this, arguments);
											} else {
												if (name == 'mousedown') {
													var record = grid.store.getAt(rowIndex);
													record.set(this.dataIndex, !record.data[this.dataIndex]);
													return false;
												} else {
													return Ext.grid.ActionColumn.superclass.processEvent.apply(this, arguments);
												}
											}
										},
										xtype : 'checkcolumn'
									},{
										header : "编辑控件设置",
										dataIndex : "editorcfg",
										editor :
										{xtype:'textfield',readOnly:readOnly}
									}]
						});
		if(!readOnly){
			var btn_addrow = new Ext.Button({text : '新增',iconCls : 'btn-select',xtype:'oecpbutton',disabled:false,
			handler:function(){
				var _record = new grid.store.recordType({required:prefix=='fixedconditions'?true:false,fieldType:'java.lang.String'});
				var row = grid.getStore().getCount();
				grid.stopEditing();
				grid.getStore().insert(row, _record);
				grid.startEditing(0, 0);
			}});						
			var btn_delrow = new Ext.Button({text : '删除',iconCls : 'btn-clear',xtype:'oecpbutton',disabled:true,
			handler:function(){
				var rows = grid.getSelectionModel().getSelections();
				grid.getStore().remove(rows);
			}});						
			grid.getTopToolbar().addButton([btn_addrow,btn_delrow]);
			grid.getSelectionModel().on('selectionChange',function(){
				if(grid.getSelectionModel().getSelections().length>0){
					btn_delrow.setDisabled(false);
				}else{
					btn_delrow.setDisabled(true);
				}
			});
		}			
		
		return grid;
	},
	destroy: function(){ 
		for(var i=0 ;i< this.grids.length;i++){
			this.grids[i].destroy();
		}
		delete this.grids[i];
        OECP.query.QueryConditionSettingPanel.superclass.destroy.call(this); 
	} 

});
	