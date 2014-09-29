var a = {
	success : true,
	msg : '预览加载成功！',
	result : {
		colspan : 1,
		hidden : false,
		rowspan : 1,
		cancommit : false,
		items : [{
					colspan : 1,
					hidden : false,
					rowspan : 1,
					cancommit : false,
					items : [{
								colspan : 1,
								rowspan : 1,
								layout : 'form',
								items : [{
											cols : 1,
											allowBlank : true,
											dataIndex : 'billsn',
											onlist : 'true',
											onform : 'true',
											cancommit : true,
											editor : 'textfield',
											hidden : false,
											name : 'billsn',
											xtype : 'textfield',
											readOnly : true,
											fieldLabel : '单据号'
										}]
							}, {
								colspan : 1,
								rowspan : 1,
								layout : 'form',
								items : [{
											cols : 1,
											allowBlank : true,
											dataIndex : 'orderdate',
											onlist : 'false',
											onform : 'true',
											cancommit : true,
											editor : 'datefield',
											hidden : false,
											name : 'orderdate',
											xtype : 'datefield',
											format : 'Y-m-d',
											fieldLabel : '订单日期'
										}]
							}, {
								colspan : 1,
								rowspan : 1,
								layout : 'form',
								items : [{
											cols : 1,
											allowBlank : true,
											dataIndex : 'note',
											onlist : 'true',
											onform : 'true',
											cancommit : true,
											editor : 'textfield',
											hidden : false,
											name : 'note',
											xtype : 'textfield',
											fieldLabel : '备注'
										}]
							}, {
								colspan : 1,
								rowspan : 1,
								layout : 'form',
								items : [{
											cols : 1,
											allowBlank : true,
											dataIndex : 'organization.id',
											onlist : 'false',
											onform : 'true',
											cancommit : true,
											editor : 'other',
											hidden : false,
											name : 'organization.id',
											xtype : 'oecpgridwinref',
											entityName : 'oecp.platform.org.eo.Organization',
											functionCode : '',
											pageSize : 20,
											codeField : 'code',
											fieldLabel : '订购公司'
										}]
							}, {
								colspan : 1,
								rowspan : 1,
								layout : 'form',
								items : [{
											cols : 1,
											allowBlank : true,
											dataIndex : 'bizType.id',
											onlist : 'true',
											onform : 'true',
											cancommit : true,
											editor : 'textfield',
											hidden : true,
											name : 'bizType.id',
											xtype : 'textfield',
											fieldLabel : 'bizType.id'
										}]
							}, {
								colspan : 1,
								rowspan : 1,
								layout : 'form',
								items : [{
											cols : 1,
											allowBlank : true,
											dataIndex : 'id',
											onlist : 'true',
											onform : 'true',
											cancommit : true,
											editor : 'textfield',
											hidden : true,
											name : 'id',
											xtype : 'textfield',
											fieldLabel : '主键'
										}]
							}, {
								colspan : 1,
								rowspan : 1,
								layout : 'form',
								items : [{
											cols : 1,
											allowBlank : true,
											dataIndex : 'preBillID',
											onlist : 'true',
											onform : 'true',
											cancommit : true,
											editor : 'textfield',
											hidden : true,
											name : 'preBillID',
											xtype : 'textfield',
											fieldLabel : 'preBillID'
										}]
							}, {
								colspan : 1,
								rowspan : 1,
								layout : 'form',
								items : [{
											cols : 1,
											allowBlank : true,
											dataIndex : 'nextBillID',
											onlist : 'true',
											onform : 'true',
											cancommit : true,
											editor : 'textfield',
											hidden : true,
											name : 'nextBillID',
											xtype : 'textfield',
											fieldLabel : 'nextBillID'
										}]
							}],
					layout : 'table',
					layoutConfig : {
						columns : 2
					},
					xtype : 'panel'
				}, {
					colspan : 1,
					hidden : false,
					rowspan : 1,
					cancommit : false,
					items : [{
								cols : 1,
								colspan : 1,
								width : 500,
								rowspan : 1,
								editable : 'true',
								cancommit : false,
								eoname : 'details',
								height : 200,
								hidden : false,
								store : Ext.isFunction(me.getBodyStoreByName) ? me.getBodyStoreByName('details') : {},
								columnLines : true,
								tbar : {},
								view : new Ext.ux.grid.LockingGridView(),
								colModel : new Ext.ux.grid.LockingColumnModel([new Ext.grid.RowNumberer(), {
											allowBlank : true,
											dataIndex : 'id',
											onlist : 'false',
											onform : 'true',
											cancommit : true,
											hidden : true,
											name : 'id',
											editor : new Ext.grid.GridEditor({
														field : {
															allowBlank : true,
															title : 'id',
															xtype : 'textfield'
														}
													}),
											header : 'id'
										}, {
											allowBlank : true,
											dataIndex : 'num',
											onlist : 'true',
											onform : 'true',
											cancommit : true,
											editor : 'numberfield',
											hidden : false,
											name : 'num',
											editor : new Ext.grid.GridEditor({
														field : {
															allowBlank : true,
															title : 'num',
															xtype : 'numberfield'
														}
													}),
											header : 'num'
										}, {
											allowBlank : true,
											dataIndex : 'otherdis',
											onlist : 'true',
											onform : 'true',
											cancommit : true,
											editor : 'textfield',
											hidden : false,
											name : 'otherdis',
											editor : new Ext.grid.GridEditor({
														field : {
															allowBlank : true,
															title : 'otherdis',
															xtype : 'textfield'
														}
													}),
											header : 'otherdis'
										}, {
											allowBlank : true,
											dataIndex : 'needdate',
											onlist : 'true',
											onform : 'true',
											cancommit : true,
											editor : 'datefield',
											hidden : false,
											name : 'needdate',
											editor : new Ext.grid.GridEditor({
														field : {
															allowBlank : true,
															title : 'needdate',
															xtype : 'datefield',
															format : 'Y-m-d h:i:s'
														}
													}),
											header : 'needdate'
										}, {
											allowBlank : true,
											dataIndex : 'needcheck',
											onlist : 'true',
											onform : 'true',
											cancommit : true,
											editor : 'checkbox',
											hidden : false,
											name : 'needcheck',
											xtype : 'checkcolumn',
											header : 'needcheck'
										}]),
								plugins : undefined,
								xtype : 'oecpeditgrid'
							}],
					layout : 'table',
					layoutConfig : {
						columns : 2
					},
					xtype : 'panel'
				}, {
					colspan : 1,
					rowspan : 1,
					cancommit : false,
					title : '单据尾',
					hidden : false,
					border : 'false',
					items : [{
								colspan : 1,
								rowspan : 1,
								layout : 'form',
								items : [{
											cols : 1,
											allowBlank : true,
											dataIndex : 'creater.id',
											onlist : 'true',
											onform : 'true',
											cancommit : true,
											editor : 'textfield',
											hidden : false,
											name : 'creater.id',
											xtype : 'textfield',
											fieldLabel : '制单人'
										}]
							}, {
								colspan : 1,
								rowspan : 1,
								layout : 'form',
								items : [{
											cols : 1,
											allowBlank : true,
											dataIndex : 'createdate',
											onlist : 'true',
											onform : 'true',
											cancommit : true,
											editor : 'datefield',
											hidden : false,
											name : 'createdate',
											xtype : 'datefield',
											format : 'Y-m-d',
											fieldLabel : '创建日期'
										}]
							}],
					layout : 'table',
					layoutConfig : {
						columns : 2
					},
					xtype : 'panel'
				}],
		layout : 'table',
		layoutConfig : {
			columns : 1
		},
		xtype : 'panel'
	}
}