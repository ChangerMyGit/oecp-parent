// 主数据资源编辑界面
Ext.ns("OECP.mdresource");

OECP.mdresource.MDResourceEditWindow = Ext.extend(Ext.Window, {
	autoHeight : true,
	width : 800,
	items : null,
	closeAction : 'hide',
	mdresourceUrl : __ctxPath + "/mdresource/getMDResources.do",
	bizSaveButton : null,
	bizCloseButton : null,
	bizEditButton : null,
	printButton : null,
	buttons : [],
	initComponent : function() {
		var master = this;
		master.bizSaveButton = new OECP.ui.button.BizSaveButton({
					handler : function() {
						master.editPanel.doSubmit({
									success : function(response, opts) {
										var msg = Ext.util.JSON
												.decode(response.responseText);
										if (msg.success) {
											master.fireEvent('dataSaved');
										} else {
											Ext.Msg.show({
														title : "错误",
														msg : msg.msg,
														buttons : Ext.Msg.OK
													});
										}
									}
								});
						master.hide();
					}
				});
		master.bizCloseButton = new OECP.ui.button.BizCloseButton({
					handler : function() {
						master.hide();
					}
				});
		master.buttons = [master.bizSaveButton, master.bizCloseButton];
		
		master.ref = new OECP.ui.RefField(
								{
									label : '关联主数据资源',
									valueField : 'id',
									displayField : 'name',
									refColumns : [{
												header : '主键',
												dataIndex : 'id',
												hidden : true
											}, {
												header : '主资源编码',
												dataIndex : 'code'
											}, {
												header : '主资源名称',
												dataIndex : 'name'
											}, {
												header : '对应实体类名',
												dataIndex : 'eoClassName'
											}, {
												header : '对应表名',
												dataIndex : 'tableName'
											}],
									storeFields : ['id', 'code', 'name',
											'eoClassName', 'tableName'],
									refUrl : master.mdresourceUrl,
									mode : 'local',
									editable : false
								}),
		
		this.editPanel = new OECP.ui.MasterDetailEditPanel({
			frame : true,
			state : 'edit',
			modifiedVarName : 'mdResource',
			detailVarName : 'fields',
			submitFilterField : ['fields.row_state'],
			submitUrl : __ctxPath + "/mdresource/saveMDResource.do",
			queryUrl : __ctxPath + "/mdresource/queryMDResource.do",
			
			items : [{
						layout : 'column',
						items : [{
									columnWidth : .4,
									layout : 'form',
									items : [{
												dataIndex : 'id',
												xtype : 'textfield',
												fieldLabel : 'id',
												name : 'id',
												hidden : true
											}, {
												xtype : 'textfield',
												dataIndex : 'code',
												fieldLabel : '主资源编码',
												name : 'code',
												width : 160,
												allowBlank : false,
												maxLength : 20,
												minLength : 2
											}, {
												xtype : 'textfield',
												dataIndex : 'name',
												fieldLabel : '主资源名称',
												name : 'name',
												width : 160,
												allowBlank : false,
												maxLength : 20,
												minLength : 2
											}]
								}, {
									columnWidth : .4,
									layout : 'form',
									items : [{
												xtype : 'textfield',
												dataIndex : 'eoClassName',
												fieldLabel : '对应实体类名',
												name : 'eoClassName',
												width : 160,
												allowBlank : false,
												minLength : 2
											}, {
												xtype : 'textfield',
												dataIndex : 'tableName',
												fieldLabel : '对应表名',
												name : 'tableName',
												width : 160,
												allowBlank : false,
												maxLength : 20,
												minLength : 2
											}]
								}]
					}],
			bodyStore : new Ext.data.JsonStore({
						fields : ['id', 'name', 'dispName', 'uiClass',
								'isDisplay', 'relatedMD.id','relatedMD.name']
					}),
			bodyColumns : [{
						header : '字段名',
						dataIndex : 'name',
						editor : new Ext.form.TextField()
					}, {
						header : '显示名称',
						dataIndex : 'dispName',
						editor : new Ext.form.TextField()
					}, {
						header : '对应的UI类',
						dataIndex : 'uiClass',
						editor : new Ext.form.TextField()
					}, {
						header : '是否显示',
						dataIndex : 'isDisplay',
						editor : new Ext.grid.GridEditor(new Ext.form.ComboBox(
								{
									store : new Ext.data.SimpleStore({
												fields : ['value', 'text'],
												data : [['true', '是'],
														['false', '否']]
											}),
									valueField : 'value',
									displayField : 'text',
									emptyText : '请选择',
									mode : 'local',
									defaultReadOnly : true,
									editable : false,
									triggerAction : 'all'
								}))
					}, {
						header : '关联主数据资源',
						dataIndex : 'relatedMD.id',
//						renderer:function(v){
//							if(v){
//								r = combo.findRecord(combo.valueField,v);
//								v=r.data[combo.displayField];
//							}
//							return v
//						},
						renderer : function(mid){
							for(i=0;i<master.editPanel.bodyStore.getCount();i++){
								if(mid == master.editPanel.bodyStore.getAt(i).get('relatedMD.id')){
									var v = master.editPanel.bodyStore.getAt(i).get('relatedMD.name');
									if(v != null && v!= ''){
										return v;
									}else{
										break;
									}
								}
							}
							for (i=0;i<master.ref.store.getCount();i++){
								if(mid == master.ref.store.getAt(i).get('id')){
									return master.ref.store.getAt(i).get('name');
								}
							}
						},
						editor : new OECP.grid.RefGridEditor(master.ref)
					}]
		});
		this.items = [this.editPanel];
		OECP.mdresource.MDResourceEditWindow.superclass.initComponent
				.call(this);
	}
});