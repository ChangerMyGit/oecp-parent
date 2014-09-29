// UI元素 查看界面
Ext.ns('OECP.bc.func');
OECP.bc.func.UIElementGrid = Ext.extend(Ext.grid.EditorGridPanel, {
			title : '界面元素',
			height : 160,
			width : 900,
			clicksToEdit : 1,
			trackMouseOver : true,
			autoScroll : true,
			disableSelection : false,
			loadMask : true,
			viewConfig : {
				forceFit : true,
				enableRowBody : false,
				showPreview : false
			},
			initComponent : function() {
				var scope = this;
				this.tbar = this.initToolBar();
				this.store = this.initStore();
				this.sm = new Ext.grid.CheckboxSelectionModel();
				this.cm = new Ext.grid.ColumnModel({
							columns : [this.sm, new Ext.grid.RowNumberer(), {
										header : 'id',
										dataIndex : 'id',
										hidden : true
									}, {
										header : '页面标识',
										width : 160,
										dataIndex : 'elementId',
										readOnly : !this.isEdit,
										editor : {
											xtype : 'textfield'
										}
									}, {
										header : '描述',
										width : 160,
										dataIndex : 'description',
										readOnly : !this.isEdit,
										editor : {
											xtype : 'textfield'
										}
									}, {
										header : '属性名称',
										width : 160,
										dataIndex : 'visibleParameterName',
										readOnly : !this.isEdit,
										editor : {
											name : 'visibleParameterName',
											xtype : 'textfield'
										}
									}],
							defaults : {
								sortable : false,
								menuDisabled : true,
								width : 100
							}
						});
				this.on('beforeedit', function(e) {
							if (!this.isEdit) {
								e.cancel = true;
							}
						});
				this.sm.on('selectionchange', function(m) {
							var rows = m.getSelections().length;
							if (rows <= 0) {
								scope.delBtn.setDisabled(true);
							} else {
								scope.delBtn.setDisabled(false);
							}
						});
				OECP.bc.func.UIElementGrid.superclass.initComponent.call(this);
			},
			initStore : function() {
				var a = new Ext.data.JsonStore({
							fields : ['id', 'elementId', {
										name : 'description',
										mapping : 'description'
									}, {
										name : 'visibleParameterName',
										mapping : 'visibleParameterName'
									}]
						});
				a.setDefaultSort('id', 'desc');
				return a;
			},
			initToolBar : function() {
				this.addBtn = new Ext.Button({
							text : '增行',
							iconCls : 'btn-select',
							grp : this,
							handler : this.addRow
						});
				this.delBtn = new Ext.Button({
							text : '删行',
							disabled : true,
							iconCls : 'btn-clear',
							grp : this,
							handler : this.deleteRow
						})
				return new Ext.Toolbar({
							items : [this.addBtn, this.delBtn]
						});
			},
			/**
			 * 增加行
			 */
			addRow : function() {
				this.grp.stopEditing();
				var store = this.grp.getStore();
				var uie = new OECP.bc.func.UIElement({
							id : '',
							elementId : '',
							description : '',
							visibleParameterName : ''
						});

				store.add(uie);
				this.grp.stopEditing();
			},
			/**
			 * 删除行
			 */
			deleteRow : function() {
				this.grp.stopEditing();
				var store = this.grp.getStore(), rows = this.grp
						.getSelectionModel().getSelections(), len = rows.length;
				if (len <= 0) {
					return
				}
				store.remove(rows);
				this.grp.stopEditing();
			}

		});

OECP.bc.func.UIElement = Ext.data.Record.create([{
			name : 'id',
			type : 'string'
		}, {
			name : 'elementId',
			type : 'string'
		}, {
			name : 'description',
			type : 'string'
		}, {
			name : 'visibleParameterName',
			type : 'string'
		}]);
