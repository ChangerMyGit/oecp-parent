Ext.ns("OECP.notice");
/**
 * 参数(枚举值)
 * @type 
 */
OECP.notice.StaticParam = {
	/**
	 * 预警类型枚举值
	 * @type String
	 */
	noticeTypecorp : 'ture',
	warnTypeEvent : '0',
	warnTypeTimer : '1',
};

/**
 * 预警视图
 * @class OECP.notice.NoticeView
 * @extends Ext.Panel
 */
OECP.notice.NoticeView = Ext.extend(Ext.Panel, {
	id : 'OECP.notice.NoticeView',
	layout : 'border',
	title : '公告发布',
	listUrl : __ctxPath +'/notice/manage/list.do',
	deleteUrl : __ctxPath + '/notice/manage/delete.do',
	startUrl : __ctxPath + '/notice/manage/start.do',
	fieldData : ["id","title","message","createcorp","createUser.name","createTime","author.name","publishDate","isStart","discorp"],
	//listGroupUrl : __ctxPath +'/task/manage/listgroup.do',
	/**
	 * 任务组下拉页面记录数
	 */
	comboPageNum : 10,
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
								var noticeWin = new OECP.notice.NoticeConfigWindow({isEdit:false,isView:false});
								noticeWin.show();
								noticeWin.on("doAfterSaved",function(){
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
									var noticeWin = new OECP.notice.NoticeConfigWindow({isEdit:true,isView:false,noticeId:id});
									noticeWin.show();
									noticeWin.on("doAfterSaved",function(){
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
											var noticeIds = Array();
											for (var i = 0, r; r = rows[i]; i++) {
												noticeIds.push(r.id);
											}
											Ext.Ajax.request({
												url : master.deleteUrl,
												params : {
													"noticeIds":noticeIds
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
						text : '公告发布',
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
									var msg = isStart=='0'?'公告发布':'撤销发布';
									Ext.Msg.confirm('提示框', '您确定要'+msg+'该任务?', function(button) {
										if (button == 'yes') {
											var rows = master.grid.getSelectionModel().getSelections();
											var data = rows[0].data;
											Ext.Ajax.request({
												url : master.startUrl,
												params : {
													"noticeId":id
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
						limit:master.pageNum,type:"1"
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
			header : '公告标题',
			dataIndex : 'title',
			width : 150,
			type : 'string'
		},{
			header : '公告内容',
			dataIndex : 'message',
			width : 150,
			type : 'string'
		},{
			header : '创建公司',
			dataIndex : 'createcorp',
			width : 150,
			type : 'string'
		},{
			header : '创建人',
			dataIndex : 'createUser.name',
			width : 100,
			type : 'string'
		},{
			header : '创建时间',
			dataIndex : 'createTime',
			width : 140,
			type : 'string'
		},{
			header : '发布人',
			dataIndex : 'author.name',
			width : 100,
			type : 'string'
		},{
			header : '发布时间',
			dataIndex : 'publishDate',
			width : 140,
			type : 'string'
		},{
			header : '发布状态',
			dataIndex : 'isStart',
			width : 80,
			type : 'string',
			renderer : function(value){
				var h = "";
				if(value=='0')
					h = "<p style=\"color:red;\">待发布</p>";
				else
					h = "<p style=\"color:green;\">已发布</p>";
				return h;
			}
		},{
			header : '是否下级公司可见',
			dataIndex : 'discorp',
			hidden : true,
			unShowAble : true,
			width : 100,
			type : 'checkbox'
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
				buttonText = "撤销发布";
				master.grid.getTopToolbar().items.items[2].disable();
				master.grid.getTopToolbar().items.items[4].disable();
			}else{
				buttonText = "发布公告";
				master.grid.getTopToolbar().items.items[2].enable();
				master.grid.getTopToolbar().items.items[4].enable();
			}
			master.grid.getTopToolbar().items.items[6].setText(buttonText);
		},this);
		//双击
		master.grid.on("rowdblclick",function(){
			var record = master.grid.getSelectionModel().getSelected();
			if (!record) {
				Ext.Msg.alert("提示", "请先选择任务!");
				return;
			}
			var id = record.get('id');
			var isStart = record.get('isStart');
			if(isStart=='1'){
				var noticeWin = new OECP.notice.NoticeConfigWindow({isEdit:true,isView:true,noticeId:id});
				    noticeWin.show();
			}else{
				var noticeWin = new OECP.notice.NoticeConfigWindow({isEdit:true,isView:false,noticeId:id});
				noticeWin.show();
			}
		},this);
		this.items = [master.grid,master.queryPanel];
		// master.store.removeAll();
		master.store.load();
		OECP.notice.NoticeView.superclass.initComponent.call(this);
	},
	/**
	 * 初始化表单
	 */
	initQueryPanel : function(){
		var master = this;
//		var store = new Ext.data.JsonStore({
//			url : master.listGroupUrl ,
//			root : "result",
//			fields : ['id','name','description'],
//			remoteSort : false,
//			timeout : 8000,
//			totalProperty: 'totalCounts'
//		});
//		var comboPageNum = master.comboPageNum;
//		var groupCombo = new Ext.form.ComboBox({
//			columnWidth:0.52,
//			hiddenName:'conditions[1].value',
//			fieldLabel: '任务组',
//		    store: store,
//		    width:200,
//		    displayField:'name',
//		    valueField:'id',
//		    typeAhead: true,
//		    mode: 'remote',
//		    triggerAction: 'all',
//		    emptyText:'请选择状态',
//		    selectOnFocus:true,
//		    pageSize:this.comboPageNum,
//		    listeners : {
//				blur : function(combo) {// 当其失去焦点的时候
//					if (combo.getRawValue() == '') {
//						combo.reset();
//					}
//				},
//				beforequery : function(queryEvent) {
//					var param = queryEvent.query;
//					this.store.load({
//						params : {
//							limit : comboPageNum,
//							'conditions[0].field':'name',
//							'conditions[0].operator':'like',
//							'conditions[0].value' : '%'+param+'%'
//						},
//						callback : function(r, options, success) {
//
//						}
//					});
//					return true;
//				}
//			}
//		});
		var codes =[[OECP.notice.StaticParam.warnTypeEvent,'待发布'], 
					[OECP.notice.StaticParam.warnTypeTimer,'已发布']];
		var store = new Ext.data.SimpleStore({
		    fields: ['id', 'name'],
		    data : codes
		});
		var typeCombo = new Ext.form.ComboBox({
				fieldLabel: '发布状态',
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
		        emptyText:'请选择发布状态'
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
						{text : '标题名称:',xtype : 'label',style:'font-size:11px'},
						{name:'conditions[0].value',xtype:'textfield',listeners:{focus:function(){this.selectText();}}},
						{name:'conditions[0].field',value:'title',hidden:true,xtype:'textfield'},
						{name:'conditions[0].operator',value:'like',hidden:true,xtype:'textfield'},
						{name:'conditions[1].field',value:'isStart',hidden:true,xtype:'textfield'},
						{name:'conditions[1].operator',value:'=',hidden:true,xtype:'textfield'},
						{text : '发布状态:',xtype : 'label',style:'font-size:11px'},
						//groupCombo,
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