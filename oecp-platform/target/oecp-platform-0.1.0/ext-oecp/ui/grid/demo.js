/*
 * ! Ext JS Library 3.3.1 Copyright(c) 2006-2010 Sencha Inc.
 * licensing@sencha.com http://www.sencha.com/license
 */
Ext.onReady(function() {

			Ext.QuickTips.init();

			var xg = Ext.grid;

			var reader = new Ext.data.JsonReader({
						idProperty : 'taskId',
						fields : [{
									name : 'projectId',
									type : 'int'
								}, {
									name : 'project',
									type : 'int'
								}, {
									name : 'taskId',
									type : 'int'
								}, {
									name : 'description',
									type : 'string'
								}, {
									name : 'estimate',
									type : 'float'
								}, {
									name : 'rate',
									type : 'float'
								}, {
									name : 'cost',
									type : 'float'
								}, {
									name : 'due',
									type : 'date',
									dateFormat : 'm/d/Y'
								}]

					});

			// define a custom summary function
			Ext.ux.grid.GridSummary.Calculations['totalCost'] = function(v, record, field, data, rowIdx) {
				return data[field] + (record.data.estimate * record.data.rate);
			};

			var gridstore = new Ext.data.Store({
									reader : reader,
									// use local data
									data : app.grid.dummyData,
									sortInfo : {
										field : 'due',
										direction : 'ASC'
									}
								});
			// utilize custom extension for Group Summary
			var summary = new Ext.ux.grid.GridSummary();
			var sm = new Ext.grid.CheckboxSelectionModel();
			var grid = new xg.EditorGridPanel({
						ds : {},
						sm : sm,
						colModel : new Ext.ux.grid.LockingColumnModel([sm, new Ext.grid.RowNumberer(), {
									id : 'description',
									header : 'Task',
									width : 200,
									sortable : true,
									dataIndex : 'description',
									summaryType : 'count',
									hideable : false,
									summaryRenderer : function(v, params, data) {
										return ((v === 0 || v > 1) ? '(' + v + ' Tasks)' : '(1 Task)');
									},
									editor : new Ext.grid.GridEditor({field:{
												xtype:'textfield',
												allowBlank : false
											}})
								}, {
									header : 'Project',
									width : 200,
									sortable : true,
									dataIndex : 'project'
								}, {
									header : 'Due Date',
									width : 100,
									sortable : true,
									dataIndex : 'due',
									summaryType : 'max',
									renderer : Ext.util.Format.dateRenderer('m/d/Y'),
									editor : new Ext.form.DateField({
												format : 'm/d/Y'
											})
								}, {
									header : 'Estimate',
									width : 150,
									sortable : true,
									dataIndex : 'estimate',
									summaryType : 'sum',
									renderer : function(v) {
										return v + ' hours';
									},
									editor : new Ext.form.NumberField({
												allowBlank : false,
												allowNegative : false,
												style : 'text-align:left'
											})
								}, {
									header : 'Rate',
									width : 150,
									sortable : true,
									renderer : Ext.util.Format.usMoney,
									dataIndex : 'rate',
									summaryType : 'average',
									editor : new Ext.form.NumberField({
												allowBlank : false,
												allowNegative : false,
												style : 'text-align:left'
											})
								}, {
									id : 'cost',
									header : 'Cost',
									width : 150,
									sortable : false,
									groupable : false,
									renderer : function(v, params, record) {
										return Ext.util.Format.usMoney(record.data.estimate * record.data.rate);
									},
									dataIndex : 'cost',
									summaryType : 'totalCost',
									summaryRenderer : Ext.util.Format.usMoney
								}]),

						view : new Ext.ux.grid.LockingGridView(),

						plugins : summary,

						tbar : [{
									text : 'Toggle',
									tooltip : 'Toggle the visibility of summary row',
									handler : function() {
										summary.toggleSummary();
									}
								},{
									text : 'add row',
									tooltip : 'add a new row',
									handler : function() {
										var _record = new grid.store.recordType();// 获取一个空record
										var row = grid.getStore().getCount();
										grid.stopEditing();
										grid.getStore().insert(row, _record);
										grid.startEditing(0, 0);
									}
								}],

						// stripeRows: true,
						frame : true,
						width : 800,
						height : 450,
						clicksToEdit : 1,
						// collapsible: true,
						// animCollapse: false,
						// trackMouseOver: false,
						// enableColumnMove: false,
						 renderTo : document.body,
						title : 'Sponsored Projects',
						iconCls : 'icon-grid'
					});

			grid.reconfigure(this.getBodyStore(),grid.getColumnModel());

			var testpanel = new Ext.Panel({
						id : 'TTTTTTTTT_TEST_TTTTTTTTTTT',
						width : 800,
						height : 600,
						layout : 'fit',
//						renderTo : document.body,
						items : [{
									colspan : 1,
									frame : true,
									hidden : false,
									rowspan : 1,
									cancommit : false,
									items : [{
	colspan : 1,
	hidden : false,
	rowspan : 1,
	cancommit : false,
	items :[
	{
		title : '单据头',
		colspan : 1,
		hidden : false,
		rowspan : 1,
		cancommit : false,
		items :[
		{
			colspan : 1,
			rowspan : 1,
			layout : 'form',
			items : [{
				cols : 1,
				onlist : 'true',
				onform : 'true',
				cancommit : true,
				hidden : false,
				xtype:'textfield'
,
				fieldLabel : '订单号'
			}]
		}
		,		{
			colspan : 1,
			rowspan : 1,
			layout : 'form',
			items : [{
				cols : 1,
				onlist : 'true',
				onform : 'true',
				cancommit : true,
				hidden : false,
				xtype:'textfield'
,
				fieldLabel : '商品名'
			}]
		}
		,		{
			colspan : 1,
			rowspan : 1,
			layout : 'form',
			items : [{
				cols : 1,
				onlist : 'true',
				onform : 'true',
				cancommit : true,
				hidden : false,
				xtype:'textfield'
,
				fieldLabel : '数量'
			}]
		}
		,		{
			colspan : 1,
			rowspan : 1,
			layout : 'form',
			items : [{
				cols : 1,
				onlist : 'true',
				onform : 'true',
				cancommit : true,
				hidden : false,
				xtype:'textfield'
,
				fieldLabel : '主分类'
			}]
		}
		,		{
			colspan : 1,
			rowspan : 1,
			layout : 'form',
			items : [{
				cols : 1,
				onlist : 'true',
				onform : 'true',
				cancommit : true,
				editor : 'checkbox',
				hidden : false,
				name : 'isfree',
				xtype:'checkbox'
,
				fieldLabel : '是否免费'
			}]
		}
		,		{
			colspan : 1,
			rowspan : 1,
			layout : 'form',
			items : [{
				cols : 1,
				onlist : 'true',
				onform : 'true',
				cancommit : true,
				editor : 'datefield',
				hidden : false,
				name : 'createdate',
				xtype:'datefield'
,
				fieldLabel : '单据日期'
			}]
		}
		],
		layout : 'table',
		layoutConfig: {
		    columns: 2
		},
		xtype : 'panel'
	}
	,	{
		title : '单据体',
		colspan : 1,
		hidden : false,
		rowspan : 1,
		cancommit : false,
		items :[
		{
			cols : 1,
			height : 200,
			colspan : 1,
			hidden : false,
			rowspan : 1,
			cancommit : false,
			store : {},
			sm : new Ext.grid.CheckboxSelectionModel(),
			showSM : true,
			columnLines: true,
			view: new Ext.ux.grid.LockingGridView(),
			colModel : new Ext.ux.grid.LockingColumnModel([
						new Ext.grid.RowNumberer(),
			{
				onlist : 'true',
				onform : 'true',
				cancommit : true,
				hidden : true,
				editor : new Ext.grid.GridEditor({xtype:'textfield'
}),

				header : 'id'
			}
			,			{
				onlist : 'true',
				onform : 'true',
				cancommit : true,
				hidden : false,
				editor : new Ext.grid.GridEditor({xtype:'textfield'
}),

				header : 'a'
			}
			,			{
				onlist : 'true',
				onform : 'true',
				cancommit : true,
				hidden : false,
				editor : new Ext.grid.GridEditor({xtype:'textfield'
}),

				header : 'b'
			}
			,			{
				onlist : 'true',
				onform : 'true',
				cancommit : true,
				hidden : false,
				editor : new Ext.grid.GridEditor({xtype:'textfield'
}),

				header : 'c'
			}
			,			{
				onlist : 'true',
				onform : 'true',
				cancommit : true,
				hidden : false,
				editor : new Ext.grid.GridEditor({xtype:'textfield'
}),

				header : 'd'
			}
			,			{
				onlist : 'true',
				onform : 'true',
				cancommit : true,
				hidden : false,
				editor : new Ext.grid.GridEditor({xtype:'textfield'
}),

				header : 'j'
			}
			]),
			plugins : undefined,
			xtype : 'oecpgrid'
		}
		],
		layout : 'table',
		layoutConfig: {
		    columns: 1
		},
		xtype : 'panel'
	}
	,	{
		title : '单据尾',
		colspan : 1,
		hidden : false,
		rowspan : 1,
		cancommit : false,
		items :[
		{
			colspan : 1,
			rowspan : 1,
			layout : 'form',
			items : [{
				cols : 1,
				onlist : 'true',
				onform : 'true',
				cancommit : true,
				hidden : false,
				xtype:'textfield'
,
				fieldLabel : '你好'
			}]
		}
		],
		layout : 'table',
		layoutConfig: {
		    columns: 2
		},
		xtype : 'panel'
	}
	],
	layout : 'table',
	layoutConfig: {
	    columns: 1
	},
	xtype : 'panel'
}],
									layout : 'table',
									layoutConfig : {
										columns : 1
									},
									xtype : 'panel'
								}]
					});

		});

// set up namespace for application
Ext.ns('app.grid');
// store dummy data in the app namespace
app.grid.dummyData = [{
			projectId : 100,
			project : '1',
			taskId : 112,
			description : 'Integrate 2.0 Forms with 2.0 Layouts',
			estimate : 6,
			rate : 150,
			due : '06/24/2007'
		}, {
			projectId : 100,
			project : '2',
			taskId : 113,
			description : 'Implement AnchorLayout',
			estimate : 4,
			rate : 150,
			due : '06/25/2007'
		}, {
			projectId : 100,
			project : '3',
			taskId : 114,
			description : 'Add support for multiple types of anchors',
			estimate : 4,
			rate : 150,
			due : '06/27/2007'
		}, {
			projectId : 100,
			project : '4',
			taskId : 115,
			description : 'Testing and debugging',
			estimate : 8,
			rate : 0,
			due : '06/29/2007'
		}, {
			projectId : 101,
			project : '5',
			taskId : 101,
			description : 'Add required rendering "hooks" to GridView',
			estimate : 6,
			rate : 100,
			due : '07/01/2007'
		}, {
			projectId : 101,
			project : '6',
			taskId : 102,
			description : 'Extend GridView and override rendering functions',
			estimate : 6,
			rate : 100,
			due : '07/03/2007'
		}, {
			projectId : 101,
			project : '7',
			taskId : 103,
			description : 'Extend Store with grouping functionality',
			estimate : 4,
			rate : 100,
			due : '07/04/2007'
		}, {
			projectId : 101,
			project : '8',
			taskId : 121,
			description : 'Default CSS Styling',
			estimate : 2,
			rate : 100,
			due : '07/05/2007'
		}, {
			projectId : 101,
			project : '9',
			taskId : 104,
			description : 'Testing and debugging',
			estimate : 6,
			rate : 100,
			due : '07/06/2007'
		}, {
			projectId : 102,
			project : '10',
			taskId : 105,
			description : 'Ext Grid plugin integration',
			estimate : 4,
			rate : 125,
			due : '07/01/2007'
		}, {
			projectId : 102,
			project : '11',
			taskId : 106,
			description : 'Summary creation during rendering phase',
			estimate : 4,
			rate : 125,
			due : '07/02/2007'
		}, {
			projectId : 102,
			project : '12',
			taskId : 107,
			description : 'Dynamic summary updates in editor grids',
			estimate : 6,
			rate : 125,
			due : '07/05/2007'
		}, {
			projectId : 102,
			project : '13',
			taskId : 108,
			description : 'Remote summary integration',
			estimate : 4,
			rate : 125,
			due : '07/05/2007'
		}, {
			projectId : 102,
			project : '14',
			taskId : 109,
			description : 'Summary renderers and calculators',
			estimate : 4,
			rate : 125,
			due : '07/06/2007'
		}, {
			projectId : 102,
			project : '15',
			taskId : 116,
			description : 'Integrate summaries with GroupingView',
			estimate : 10,
			rate : 125,
			due : '07/11/2007'
		}, {
			projectId : 102,
			project : '16',
			taskId : 117,
			description : 'Integrate summaries with GroupingView',
			estimate : 10,
			rate : 125,
			due : '07/11/2007'
		}, {
			projectId : 102,
			project : '17',
			taskId : 110,
			description : 'Integrate summaries with GroupingView',
			estimate : 10,
			rate : 125,
			due : '07/11/2007'
		}, {
			projectId : 102,
			project : '18',
			taskId : 111,
			description : 'Testing and debugging',
			estimate : 8,
			rate : 125,
			due : '07/15/2007'
		}];