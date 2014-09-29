Ext.ns("OECP.Warn")

/**
 * 参数(枚举值)
 * @type 
 */
OECP.Warn.StaticParam = {
	/**
	 * 预警类型枚举值
	 * @type String
	 */
	warnTypeEvent : 'EVENT_WARNING',
	warnTypeTimer : 'TIMER_WARNING',
	/**
	 * 预警通知方式枚举值
	 * @type String
	 */
	warnNoticePortal : 'PORTAL_NOTICE',
	warnNoticeEmail : 'EMAIL_NOTICE',
	warnNoticeMessage : 'SHORT_MESSAGE_NOTICE',
	/**
	 * 
	 * @type String
	 */
	warnNoticeUserRole : 'NOTICE_ROLE',
	warnNoticeUserPost : 'NOTICE_POST',
	warnNoticeUser : 'NOTICE_USER'
};
/**
 * 预警视图
 * @class OECP.Warn.WarnView
 * @extends Ext.Panel
 */
OECP.Warn.WarnView = Ext.extend(Ext.Panel, {
	id : 'OECP.Warn.WarnView',
	layout : 'border',
	title : '预警列表',
	listUrl : __ctxPath +'/warn/manage/list.do',
	deleteUrl : __ctxPath + '/warn/manage/delete.do',
	startUrl : __ctxPath + '/warn/manage/start.do',
	fieldData : ["id", "name","warningType","warningitf","warnNoticeItem","noticeNum","isStart"],
	/**
	 * 列表页面记录数
	 * @type Number
	 */
	pageNum : 10,
	getToolBar : function(master){
		var toolBar = new Ext.Toolbar({
			items : [{
						text : '添加',
						tooltip : '添加一条新的记录',
						iconCls : 'add-user',
						listeners : {
							'click' : function(btn, e) {
								var warnConWin = new OECP.Warn.WarnConfigWindow({isEdit:false,isView:false});
								warnConWin.show();
								warnConWin.tabPanel.hideTabStripItem(1);
								warnConWin.on("doAfterSaved",function(){
									master.store.removeAll();
									master.store.load();
								});
							}
						}
					}, new Ext.Toolbar.Separator(), {
						text : '编辑',
						tooltip : '修改选中的记录',
						iconCls : 'btn-edit',
						listeners : {
							'click' : function(btn, e) {
								var rows = master.grid.getSelectionModel()
										.getSelections();
								if (rows.length <= 0) {
									Ext.Msg.alert("提示信息", "请选择一条记录！");
								} else if (rows.length > 1) {
									Ext.Msg.alert("提示信息", "请选择一条记录！");
								} else if (rows.length == 1) {
									var id = rows[0].get("id");
									var isStart = rows[0].get("isStart");
									if(isStart=='1'){
										Ext.Msg.alert("提示信息", "启用状态不能修改！");
										return;
									}
									var warnConWin = new OECP.Warn.WarnConfigWindow({isEdit:true,isView:false,warnId:id});
									warnConWin.show();
									warnConWin.on("doAfterSaved",function(){
										master.store.removeAll();
										master.store.load();
									});
								}
							}
						}
					}, new Ext.Toolbar.Separator(), {
						text : '删除',
						tooltip : '删除选中的的记录',
						iconCls : 'btn-del',
						listeners : {
							'click' : function(btn, e) {
								var rows = master.grid.getSelectionModel()
										.getSelections();
								if (rows.length == 0) {
									Ext.Msg.alert("提示信息", "请选择一条记录！");
								} else {
									var isStart = rows[0].get("isStart");
									if(isStart=='1'){
										Ext.Msg.alert("提示信息", "启用状态不能删除！");
										return;
									}
									Ext.Msg.confirm('提示框','您确定要删除吗！',function(button){
										if(button=='yes'){
											var warnIds = Array();
											for (var i = 0, r; r = rows[i]; i++) {
												warnIds.push(r.id);
											}
											Ext.Ajax.request({
												url : master.deleteUrl,
												params : {
													"warnIds":warnIds
												},
												success : function(request) {
													var json = Ext.decode(request.responseText);
													if (json.success) {
														Ext.ux.Toast.msg('信息',	json.msg);
														master.store.removeAll();
														master.store.load();
													} else {
														Ext.Msg.show({title:'错误',msg:json.msg,buttons:Ext.Msg.OK	});
													}
												},
												failure : function(request) {
													var json = Ext.decode(request.responseText);
													Ext.Msg.show({title:'错误',msg:json.msg,buttons:Ext.Msg.OK});
												}
											});
										}
									},this);
								}

							}
						}
					}, new Ext.Toolbar.Separator(), {
						text : '启用',
						iconCls : 'btn-edit',
						listeners : {
							'click' : function(btn, e) {
								var rows = master.grid.getSelectionModel()
										.getSelections();
								if (rows.length <= 0) {
									Ext.Msg.alert("提示信息", "请选择一条记录！");
								} else if (rows.length > 1) {
									Ext.Msg.alert("提示信息", "请选择一条记录！");
								} else if (rows.length == 1) {
									var id = rows[0].get("id");
									var isStart = rows[0].get("isStart");
									var msg = isStart=='0'?'启用':'停用';
									Ext.Msg.confirm('提示框', '您确定要'+msg+'该预警?', function(button) {
										if (button == 'yes') {
											Ext.Ajax.request({
												url : master.startUrl,
												params : {
													"warn.id":id
												},
												success : function(request) {
													var json = Ext.decode(request.responseText);
													if (json.success) {
														Ext.ux.Toast.msg('信息',	json.msg);
														master.store.removeAll();
														master.store.load();
													} else {
														Ext.Msg.show({title:'错误',msg:json.msg,buttons:Ext.Msg.OK	});
													}
												},
												failure : function(request) {
													var json = Ext.decode(request.responseText);
													Ext.Msg.show({title:'错误',msg:json.msg,buttons:Ext.Msg.OK});
												}
											});
										}
									}, this);
								}
							}
						}
					}]
		});
		
		return toolBar;
	},
	/**
	 * 初始化方法
	 */
	initComponent : function(){
		var master = this;
		master.queryBtn	 = new OECP.ui.Button({
			text : "查询",
			iconCls : 'btn-search',
			handler : function(){
				master.query(master);
			}
		});//查询按钮
		this.initQueryPanel();
		// 初始化数据仓库
		master.store = new Ext.data.JsonStore({
					storeId : 'id',
					root : 'result',
					url : master.listUrl,
					totalProperty : "totalCounts",
					fields : master.fieldData,
					baseParams : {
						limit:master.pageNum
					}
		});
		// 初始化按钮
		var toolBar = master.getToolBar(master);
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
					header : '预警名称',
					dataIndex : 'name',
					width : 150,
					type : 'string'
				}, {
					header : '预警类别',
					dataIndex : 'warningType',
					width : 100,
					type : 'string',
					renderer : function(value){
						var h = "";
						if(value==OECP.Warn.StaticParam.warnTypeEvent)
							h = "事件预警";
						else
							h = "定时器预警";
						return h;
					}
				}, {
					header : '预警插件',
					dataIndex : 'warningitf',
					width : 250,
					type : 'string'
				}, {
					header : '预警通知方式',
					dataIndex : 'warnNoticeItem',
					width : 150,
					hidden : true,
					type : 'string'
				}, {
					header : '通知次数',
					dataIndex : 'noticeNum',
					width : 100,
					type : 'string'
				}, {
					header : '启用状态',
					dataIndex : 'isStart',
					width : 100,
					type : 'string',
					renderer : function(value){
						var h = "";
						if(value=='0')
							h = "<p style=\"color:red;\">停用</p>";
						else
							h = "<p style=\"color:green;\">启用</p>";
						return h;
					}
				}]
		// 把列表编号，复选框放入列中
		var columns = master.structure;
		
		var pageBar = new Ext.PagingToolbar({
			pageSize : master.pageNum,
			store : master.store,
			displayInfo : true,
			displayMsg : '当前显示 {0}-{1}条记录 /共{2}条记录',
			emptyMsg : "无显示数据"
		});
		
		// 初始化Grid
		master.grid = new Ext.grid.EditorGridPanel({
			region : 'center',
			store : master.store,
			cm : new Ext.grid.ColumnModel({
						columns : columns
					}),
			sm : sm,
			loadMask : true,
			tbar : toolBar,
			bbar : pageBar
		});
		sm.on("rowselect",function(){
			var record = master.grid.getSelectionModel().getSelected();
			if (!record) {
				Ext.Msg.alert("提示", "请先选择预警!");
				return;
			}
			//启用、停用按钮
			var isStart = record.get('isStart');
			var buttonText = "";
			if(isStart=='1'){
				buttonText = "停用";
				master.grid.getTopToolbar().items.items[2].disable();
				master.grid.getTopToolbar().items.items[4].disable();
			}else{
				buttonText = "启用";
				master.grid.getTopToolbar().items.items[2].enable();
				master.grid.getTopToolbar().items.items[4].enable();
			}
			master.grid.getTopToolbar().items.items[6].setText(buttonText);
		},this);
		//双击
		master.grid.on("rowdblclick",function(){
			var record = master.grid.getSelectionModel().getSelected();
			if (!record) {
				Ext.Msg.alert("提示", "请先选择预警!");
				return;
			}
			var id = record.get('id');
			var isStart = record.get('isStart');
			if(isStart=='1'){
				var warnConWin = new OECP.Warn.WarnConfigWindow({isEdit:true,isView:true,warnId:id});
				warnConWin.show();
			}else{
				var warnConWin = new OECP.Warn.WarnConfigWindow({isEdit:true,isView:false,warnId:id});
				warnConWin.show();
			}
		},this);
		this.items = [master.grid,master.queryPanel];
		master.store.removeAll();
		master.store.load();
		OECP.Warn.WarnView.superclass.initComponent.call(this);
	},
	/**
	 * 初始化表单
	 */
	initQueryPanel : function(){
		var master = this;
		var codes =[[OECP.Warn.StaticParam.warnTypeEvent,'事件预警'], 
					[OECP.Warn.StaticParam.warnTypeTimer,'定时器预警']];
		var store = new Ext.data.SimpleStore({
		    fields: ['id', 'name'],
		    data : codes
		});
		var typeCombo = new Ext.form.ComboBox({
				fieldLabel: '事件类型',
				hiddenName:'conditions[1].value',
				width:190,
				store:store,
		        valueField  : 'id',
		        displayField:'name',
		        mode: 'local',
		        forceSelection: true,
		        triggerAction: 'all',
		        selectOnFocus:true,
		        editable:true,
		        emptyText:'请选择预警类型'
		});
		master.queryPanel = new Ext.FormPanel({
			height : 40,
			frame : true,
			region : 'north',
			layout : 'table',
			layoutConfig : {
				columns : 10
			},
			items : [
						{text : '预警名称:',xtype : 'label',style:'font-size:11px'},
						{name:'conditions[0].value',xtype:'textfield',listeners:{focus:function(){this.selectText();}}},
						{name:'conditions[0].field',value:'name',hidden:true,xtype:'textfield'},
						{name:'conditions[0].operator',value:'like',hidden:true,xtype:'textfield'},
						{name:'conditions[1].field',value:'warningType',hidden:true,xtype:'textfield'},
						{name:'conditions[1].operator',value:'=',hidden:true,xtype:'textfield'},
						{text : '预警类型:',xtype : 'label',style:'font-size:11px'},
						typeCombo,
						master.queryBtn					
			]
		});
	},
	query : function(master){
		var values = master.queryPanel.getForm().getValues();
		if(Ext.isEmpty(values['conditions[0].value'])&&Ext.isEmpty(values['conditions[1].value'])){
			master.store.load();
		}else{
			master.store.load({
				params : values
			});
		}
	}
});