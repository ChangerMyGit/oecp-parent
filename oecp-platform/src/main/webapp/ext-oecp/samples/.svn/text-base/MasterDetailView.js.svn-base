Ext.onReady(function() {
	var r1 = new OECP.grid.RefGridEditor(new OECP.ui.RefField({
				label : '商品',
				mode : "local",
				hiddenName : 'myref',
				displayField : 'name',
				valueField : 'id',
				codeField : 'id',
				valueNotFoundText : '',
				refColumns : [{
							header : '主键',
							dataIndex : 'id',
							hidden : true,
							width : 30
						}, {
							header : '商品名称',
							dataIndex : 'name',
							width : 80
						}, {
							header : '商品规格',
							dataIndex : 'packing',
							width : 80
						}],
				storeFields : ['id', 'name', 'packing'],
				refUrl : 'refData.json'
			}));
	var r2 = new Ext.grid.GridEditor(new Ext.form.ComboBox({
				store : new Ext.data.SimpleStore({
							fields : ['value', 'text'],
							data : [['a', '早饭'], ['b', '午饭'], ['c', '晚饭']]
						}),
				valueField : 'value',
				displayField : 'text',
				emptyText : '请选择',
				mode : 'local',
				editable : false,
				triggerAction : 'all'
			}));
	var refs = {
		code : r1,
		name : r2,
		num : new Ext.grid.GridEditor(new Ext.form.NumberField())
	};
	var test = new OECP.ui.MasterDetailPanel({
				height : 400,
				headCheckBox : true,
				headDataUrl : 'MasterDetailData.json',
				headStoreParams : {
					id : 'abc'
				},
				headPageSize : 25,
				bodyPageSize : 25,
				headColumns : [{
							header : "主键",
							dataIndex : "id",
							hidden : true
						}, {
							header : "编码",
							dataIndex : "code"
						}, {
							header : "名称",
							dataIndex : "name"
						}, {
							header : "数字",
							dataIndex : "num",
							fieldType:'int'
						}],
				headStoreFields : ['id', 'code', 'name', 'num'],
				bodyDataUrl : 'MasterDetailData.json',
				bodyColumns : [{
							header : "主键",
							dataIndex : "id",
							hidden : true
						}, {
							header : "编码",
							dataIndex : "code"
						}, {
							header : "名称",
							dataIndex : "name"
						}],
				bodyStoreFields : ['id', 'code', 'name'],
				refs : refs
			});
	test.addBtn.on('click', function(btn, e) {
		var scope = this;
		if (!scope._form) {
			scope._form = new OECP.ui.MasterDetailEditPanel({
						height : 300,
						weight : 400,
						status : 'edit',
						modifiedVarName : 'testVar',
						detailVarName : 'uis',
						submitUrl : '/role/test.do',
						queryUrl : '/role/test2.do',
						bodyRefs : {
							name : new Ext.form.ComboBox({
										store : new Ext.data.SimpleStore({
													fields : ['value', 'text'],
													data : [['a', '早饭'],
															['b', '午饭'],
															['c', '晚饭']]
												}),
										valueField : 'value',
										displayField : 'text',
										emptyText : '请选择',
										mode : 'local',
										editable : false,
										triggerAction : 'all'
									})
						},
						items : [{
									fieldLabel : '主键',
									xtype : 'textfield',
									dataIndex : 'id',
									name : 'id',
									mapping : 'id',
									hidden : true
								}, {
									fieldLabel : '功能编号',
									xtype : 'textfield',
									dataIndex : 'code',
									name : 'code',
									mapping : 'code'
								}, {
									fieldLabel : '功能名称 ',
									xtype : 'textfield',
									dataIndex : 'name',
									name : 'name',
									mapping : 'name'
								}, {
									fieldLabel : ' 功能描述 ',
									xtype : 'textfield',
									dataIndex : 'description',
									name : 'description',
									mapping : 'description'
								}, {
									fieldLabel : ' 是否可运行 ',
									xtype : 'checkbox',
									dataIndex : 'runable',
									name : 'runable',
									mapping : 'runable'
								}],
						bodyStore : new Ext.data.JsonStore({
									fields : ['code', 'name', 'description',
											'runable']
								}),
						bodyColumns : [{
									header : '组织编码',
									dataIndex : 'code',
									editor : new Ext.form.TextField()
								}, {
									header : '组织名称',
									dataIndex : 'name',
									editor : new Ext.form.ComboBox({
												store : new Ext.data.SimpleStore(
														{
															fields : ['value',
																	'text'],
															data : [
																	['a', '集团'],
																	['b',
																			'青岛分公司'],
																	['c',
																			'北京分公司']]
														}),
												valueField : 'value',
												displayField : 'text',
												emptyText : '请选择',
												mode : 'local',
												// defaultReadOnly : true,
												triggerAction : 'all'
											})
								}, {
									header : '功能描述',
									dataIndex : 'description',
									xtype : 'checkcolumn'
								}]
					});
		}
		if (!scope._window) {
			scope._window = new Ext.Window({
						height : 300,
						width : 500,
						items : scope._form,
						closeAction : 'hide',
						buttons : [{
									text : '加载',
									listeners : {
										'click' : function(btn, e) {
											scope._form.doQuery();

											// var _editable =
											// !scope._form.editable;
											// scope._form.editable
											// = _editable;
											// scope._form.setEditable(_editable);

										}
									}
								}, {
									'text' : '提交',
									listeners : {
										'click' : function(btn, e) {
											scope._form.doSubmit({params:{myTestParams:'helloworld'}});
											// scope._window
											// .hide();
										}
									}
								}]
					});
		};
		scope._window.show();
	});

	test.render("panel");

	var ref = new OECP.ui.GridRefField({
				minChars : 2,
				mode : 'local',
				valueField : 'id',
				codeField : 'id',
				displayField : 'name',
				refUrl : '/warehouse/ref.do',
				storeFields : ['id', 'name', {
							name : 'person',
							mapping : 'person.name'
						}, 'address', 'phone', {
							name : 'org',
							mapping : 'org.name'
						}],
				refColumns : [{
							header : '主键',
							dataIndex : 'id',
							hidden : true,
							width : 30
						}, {
							header : '仓库名称',
							dataIndex : 'name',
							width : 80
						}, {
							header : '仓库主管',
							dataIndex : 'person',
							width : 80
						}, {
							header : '地址',
							dataIndex : 'address',
							width : 80
						}, {
							header : '电话',
							dataIndex : 'phone',
							width : 80
						}, {
							header : '所属机构',
							dataIndex : 'org',
							width : 80
						}]
			});
	ref.render('ref');
});