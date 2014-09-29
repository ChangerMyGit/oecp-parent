// 主数据资源选择窗口
Ext.ns('OECP.bc.func');
OECP.bc.func.MDResourceChooseComb = Ext.extend(Ext.form.ComboBox, {
			initComponent : function() {
				this.initBox();
				OECP.bc.func.MDResourceChooseComb.superclass.initComponent
						.call(this);
			},

			onTriggerClick : function() {
				this.onFocus();
				this.box.show();
			},
			initBox : function() {
				var master = this;

				master.store = new Ext.data.JsonStore({
							url : __ctxPath
									+ "/datapermission/getmdResources.do",
							fields : ['id', 'name', 'code', 'jsClassName'],
							autoLoad : true
						});

				var mdResource_sm = new Ext.grid.CheckboxSelectionModel();// 选择器

				var mdResource_cm = new Ext.grid.ColumnModel([mdResource_sm,
						new Ext.grid.RowNumberer(), {
							header : "主键",
							dataIndex : "id",
							hidden : true
						}, {
							header : "资源编码",
							dataIndex : "code"
						}, {
							header : "资源名称",
							dataIndex : "name"
						}]);

				// 详细信息显示
				var mdResource_panel = new Ext.grid.GridPanel({
							title : '详细信息',
							region : 'center',
							enableDD : false,
							enableDrag : false,
							autoScroll : true,
							rootVisible : false,
							flex : 1,
							viewConfig : {
								forceFit : true
							},
							sm : mdResource_sm,
							cm : mdResource_cm,
							store : master.store
						});

				master.box = new Ext.Window({
							layout : 'hbox',
							title : '数据资源离散分配',
							height : 400,
							width : 400,
							modal : true,
							closable : true,
							closeAction : 'hide',
							layoutConfig : {
								padding : '5',
								align : 'stretch'
							},
							items : [mdResource_panel],
							buttons : ['->', '-', {
								text : '确定',
								listeners : {
									'click' : function(e) {
										if (mdResource_sm.getCount() == 0) {
											Ext.ux.Toast.msg("信息", "请选择一条记录！");
										} else if (mdResource_sm.getCount() > 1) {
											Ext.ux.Toast.msg("信息",
													"只能选择一个上级部门！");
										} else {
											var b = mdResource_panel
													.getSelectionModel()
													.getSelections();
											master
													.setValue(b[0][master.valueField]);// 赋值
											master.box.hide();// 隐藏窗体
										}
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
		});