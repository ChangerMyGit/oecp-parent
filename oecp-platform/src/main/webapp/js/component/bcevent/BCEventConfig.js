Ext.ns("OECP.bcevent");

/**
 * 
 * @class OECP.bcevent.BCEventConfig
 * @extends Ext.Panel
 */

OECP.bcevent.BCEventConfig = Ext.extend(Ext.Panel, {
	id : 'OECP.bcevent.BCEventConfig',
	layout : 'border',
	// 标题
	title : '事件配置',
	/**
	 * 
	 * @type String列表的url
	 */
	listUrl : __fullPath + 'eventConfige/list.do',
	/**
	 * 启动事件监听器
	 */
	startUrl : __fullPath + 'eventConfige/start.do',
	/**
	 * 启动事件监听器
	 */
	stopUrl : __fullPath + 'eventConfige/stop.do',
	/**
	 * 启动事件监听器
	 */
	synUrl : __fullPath + 'eventConfige/synConfige.do',
	// 设置复选框还是单选，默认false多选。
	selectCheckboxModel : false,
	width : '99%',
	/**
	 * 
	 * @type 列表数据项
	 */
	filedDate : ['id,','eventSource','listenerClass','action','startFlag','synFlag'],
	structure : [],
	grid : null,
	// 初始化方法
	initComponent : function(){
		var master = this;
		// 初始化数据仓库
		master.store = new Ext.data.JsonStore({
			storeId : 'id',
			root : 'result',
			url : master.listUrl,
			totalProperty : "totalCounts",
			autoLoad : true,
			fields : master.filedDate
		});
		master.store.load({params:{start:0,limit:25	}});
		// 按钮
		var toolBar = new Ext.Toolbar({
			items : [
					{
						text : '启动',
						tooltip : '启动选中的的监听器',
						iconCls : 'btn-edit',
						listeners : {
							'click' : function(btn, e) {
								var rows = master.grid.getSelectionModel()
										.getSelections();
								if (rows.length == 0) {
									Ext.Msg.alert("提示信息", "请选择一条记录！");
								} else {
									var recordes = Array();
									for (var i = 0, r; r = rows[i]; i++) {
										recordes.push(r.id);
									}
									master.start(recordes, rows, master.grid.getStore());
								}
							}
						}
					},
					new Ext.Toolbar.Separator(),
					{
						text : '停用',
						tooltip : '停用选中的的监听器',
						iconCls : 'btn-del',
						listeners : {
							'click' : function(btn, e) {
								var rows = master.grid.getSelectionModel().getSelections();
								if (rows.length <= 0) {
									Ext.Msg.alert("提示信息", "请选择一条记录！");
								} else {
									var recordes = Array();
									for (var i = 0, r; r = rows[i]; i++) {
										recordes.push(r.id);
									}
									master.stop(recordes, rows, master.grid.getStore());
								}
							}
						}
					},
					new Ext.Toolbar.Separator(),
					{
						text : '同步',
						tooltip : '设置选中的监听器同步',
						iconCls : 'btn-refresh',
						listeners : {
							'click' : function(btn, e) {
								var rows = master.grid.getSelectionModel().getSelections();
								if (rows.length <= 0) {
									Ext.Msg.alert("提示信息", "请选择一条记录！");
								} else {
									var recordes = Array();
									for (var i = 0, r; r = rows[i]; i++) {
										recordes.push(r.id);
									}
									master.synConfige(recordes, 'Y');
								}
							}
						}
					},
					new Ext.Toolbar.Separator(),
					{
						text : '异步',
						tooltip : '设置选中的的监听器异步',
						iconCls : 'btn-refresh',
						listeners : {
							'click' : function(btn, e) {
								var rows = master.grid.getSelectionModel().getSelections();
								if (rows.length <= 0) {
									Ext.Msg.alert("提示信息", "请选择一条记录！");
								} else {
									var recordes = Array();
									for (var i = 0, r; r = rows[i]; i++) {
										recordes.push(r.id);
									}
									master.synConfige(recordes, 'N');
								}
							}
						}
					} ]
		});
		
		// 初始化列模式
		var sm = new Ext.grid.CheckboxSelectionModel({
			singleSelect : master.selectCheckboxModel
		});
		master.structure = [sm, new Ext.grid.RowNumberer(), {
			header : '主键',
			dataIndex : 'id',
			hidden : true,
			unShowAble : true
		}, {
			header : '事件源',
			dataIndex : 'eventSource',
			width : 260,
			sortable : true,
			required : true,
			type : 'string'
		}, {
			header : '监听器',
			dataIndex : 'listenerClass',
			width : 350,
			sortable : true,
			required : true,
			type : 'string'
		}, {
			header : '动作',
			dataIndex : 'action',
			width : 150,
			sortable : true,
			required : true,
			type : 'string'
		} , {
			header : '启动',
			dataIndex : 'startFlag',
			width : 60,
			sortable : true,
			required : true,
			type : 'string'
		}, {
			header : '同步',
			dataIndex : 'synFlag',
			width : 60,
			sortable : true,
			required : true,
			type : 'string'
		}];
		// 把列表编号，复选框放入列中
		var columns = master.structure;
		master.grid = new Ext.grid.EditorGridPanel({
			height : 550,
			store : master.store,
			cm : new Ext.grid.ColumnModel({
				columns : columns
			}),
			sm : sm,
			tbar : toolBar,
			bbar : new Ext.PagingToolbar({
				pageSize : 25,
				store : master.store,
				displayInfo : true,
				displayMsg : '当前显示从{0}至{1}， 共{2}条记录',
				emptyMsg : "当前没有记录"
			})
		});
		this.items = [master.grid];
		OECP.bcevent.BCEventConfig.superclass.initComponent.call(this);
	},
	
	/**
	 * 启动事件监听器
	 */
	start : function(ids, rows, store) {
		var master = this;
		Ext.Msg.confirm("信息确认", "您确认要启动所选事件监听器吗？", function(confirm) {
			if (confirm == 'yes') {
				Ext.Ajax.request({
					url : master.startUrl,
					params : {
						ids : ids
					},
					method : "POST",
					success : function(res) {
						var msg = Ext.util.JSON.decode(res.responseText);
						if (msg.success) {
							Ext.ux.Toast.msg("信息", msg.msg);
							master.grid.getStore().reload();
						} else {
							Ext.Msg.show({title : "错误",msg : msg.msg,buttons : Ext.Msg.OK});
						}
					},
					failure : function() {
						Ext.Msg.show({title : "错误",msg : '提交失败，您的网络可能不通畅，请稍后再试。',buttons : Ext.Msg.OK});
					}
				});
			}
		});
	} ,
	
	/**
	 * 停用事件监听器
	 */
	stop : function(ids, rows, store) {
		var master = this;
		Ext.Msg.confirm("信息确认", "您确认要停用所选事件监听器吗？", function(confirm) {
			if (confirm == 'yes') {
				Ext.Ajax.request({
					url : master.stopUrl,
					params : {
						ids : ids
					},
					method : "POST",
					success : function(res) {
						var msg = Ext.util.JSON.decode(res.responseText);
						if (msg.success) {
							Ext.ux.Toast.msg("信息", msg.msg);
							master.grid.getStore().reload();
						} else {
							Ext.Msg.show({title : "错误",msg : msg.msg,buttons : Ext.Msg.OK});
						}
					},
					failure : function() {
						Ext.Msg.show({title : "错误",msg : '提交失败，您的网络可能不通畅，请稍后再试。',buttons : Ext.Msg.OK});
					}
				});
			}
		});
	},
	
	/**
	 * 设置同步/异步
	 */
	synConfige : function(ids,synParam){
		var master = this;
		Ext.Ajax.request({
			url : master.synUrl,
			params : {
				ids : ids,
				synParam : synParam
			},
			method : "POST",
			success : function(res) {
				var msg = Ext.util.JSON.decode(res.responseText);
				if (msg.success) {
					Ext.ux.Toast.msg("信息", msg.msg);
					master.grid.getStore().reload();
				} else {
					Ext.Msg.show({title : "错误",msg : msg.msg,buttons : Ext.Msg.OK});
				}
			},
			failure : function() {
				Ext.Msg.show({title : "错误",msg : '提交失败，您的网络可能不通畅，请稍后再试。',buttons : Ext.Msg.OK});
			}
		});
	}
});