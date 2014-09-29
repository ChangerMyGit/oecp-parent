/**
 * @inclule "../../../ext-oecp/ui/GridPanel.js"
 */
Ext.ns("OECP.bpm.instance");

/**
 * 流程实例页面
 * 
 * @author yangtao
 * @class OECP.bpm.ProcessInstanceView
 * @extends Ext.Panel
 */
OECP.bpm.ProcessInstanceView =  Ext.extend(Ext.Panel, {
	/**
	 * id
	 */
	id : "ProcessInstanceView",
	/**
	 * 标题
	 */
	title : "流程实例列表",
	/**
	 * 关闭
	 */
	closable : true,
	/**
	 * 布局
	 */
	layout:'table',
	/**
	 * 布局配置
	 */
    layoutConfig: {
        columns: 1
    },
    /**
     * 滚动条
     */
	autoScroll : true,
	/**
	 * 流程实例列表页面记录数
	 * @type Number
	 */
	pageNum : 15,
	/**
	 * 查看历史
	 */
	view : function() {
		var record = this.grid.getSelectionModel().getSelected();
		var records = this.grid.getSelectionModel().getSelections();
		if (!record) {
			Ext.Msg.alert("提示", "请先选择要查看的流程实例!");
			return;
		}
		if (records.length > 1) {
			Ext.Msg.alert("提示", "查看时只能选择一行!");
			return;
		}
		var billKey = record.get("billKey");
		var processInstanceHisView = new OECP.bpm.ProcessInstanceHisView({billKey:billKey});
		var win = new Ext.Window({
				title : '流程实例历史',
				width : 800,
				height : 400,
				autoScroll : true,
				modal : true,
				items : [processInstanceHisView]
				
			});
		win.show();
		
	},
	/**
	 * 停止流程实例
	 */
	stop : function(){
		var removeIds = "";
		var record = this.grid.getSelectionModel().getSelected();
		var records = this.grid.getSelectionModel().getSelections();
		if (!record) {
			Ext.Msg.alert("提示", "请先选择要停止的流程实例!");
			return;
		}
		if (records.length == 0) {
			Ext.MessageBox.alert('警告', '最少选择一条信息，进行停止!');
			return;
		}
		Ext.Msg.confirm('提示框', '您确定要进行删除操作吗?', function(button) {
			if (button == 'yes') {
				for (var i = 0; i < records.length; i++) {
					var record = records[i];
					var id = record.get('processInstanceId');
					if(id=='undefined' || id =='' || id == null || id == 'null'){
						this.store.remove(records[i]);
					}else{
						removeIds += id;
						removeIds += ",";
					}
				}
				if(removeIds==''){
						
				}else{
					var actionUtil = new ActionUtil();
					actionUtil.stopProcessInstance(removeIds,this.store);
					// 清空删除列表
					removeIds = '';
				}
			}
		}, this);
		
	},
	/**
	 * 列表顶部工具
	 * @returns {Array}
	 */
	getTopbar : function() {
		return ['   ', new OECP.ui.Button({
			text : '查看历史',
			pressed : true,
			handler : this.view,
			scope : this,
			iconCls : 'search'
		}), '   ', new OECP.ui.Button({
			text : '停止流程实例',
			pressed : true,
			handler : this.stop,
			scope : this,
			iconCls : 'search'
		})];
	},
	
	/**
	 * 初始化方法
	 */
	initComponent : function() {
		var scope = this;
		/**
		 * 初始化父类
		 */
		OECP.bpm.ProcessInstanceView.superclass.initComponent.call(this);
		var pageNum = this.pageNum;
		/**
		 * 查询form
		 */
		var searchForm = new Ext.FormPanel({
				layout:'table',
			    layoutConfig: {
			        columns: 6
			    },
		        frame:true,
		        width:1175,
		        defaults : {
					xtype : 'label'
				},
		        items: [{
			        	text:'单据号',
			        	style:'font-size:13px'
		        	},{
		        		xtype:'textfield',
		                name: '_billInfo',
		                width :150,
		                listeners:{focus:function(){this.selectText();}}
		            },{
			            text:'流程名称',
			            style:'font-size:13px'
		            },{
		        		xtype:'textfield',
		                name: '_processName',
		                width :300,
						listeners:{focus:function(){this.selectText();}}
		            },{
		            	xtype:'button',
		            	iconCls : 'btn-search',
		            	text: '查询',
			            handler : function(){
			            	var processName = searchForm.form.findField('_processName').getValue();
			            	var billInfo = searchForm.form.findField('_billInfo').getValue();
			            	scope.store.removeAll();
			            	if(Ext.isEmpty(processName)&&Ext.isEmpty(billInfo))
				            	scope.store.baseParams = {
									limit : pageNum
								}
			            	else if(!Ext.isEmpty(processName)&&Ext.isEmpty(billInfo))
			            		scope.store.baseParams = {
									limit : pageNum,
									'conditions[0].field':'virProDefinition.name',
									'conditions[0].operator':'like',
									'conditions[0].value' : '%'+processName+'%'
								}
			            	else if(Ext.isEmpty(processName)&&!Ext.isEmpty(billInfo))
			            		scope.store.baseParams = {
									limit : pageNum,
									'conditions[1].field':'billInfo',
									'conditions[1].operator':'like',
									'conditions[1].value' : '%'+billInfo+'%'
								}
			            	else  if(!Ext.isEmpty(processName)&&!Ext.isEmpty(billInfo))
			            		scope.store.baseParams = {
									limit : pageNum,
									'conditions[0].field':'virProDefinition.name',
									'conditions[0].operator':'like',
									'conditions[0].value' : '%'+processName+'%',
									'conditions[1].field':'billInfo',
									'conditions[1].operator':'like',
									'conditions[1].value' : '%'+billInfo+'%'
								}
							scope.store.load();
			            }
		            },{
		            	xtype:'button',
		            	iconCls : 'btn-reset',
		            	text: '重置',
			            handler : function(){
			            	searchForm.form.findField('_processName').setValue("");
			            	searchForm.form.findField('_billInfo').setValue("");
			            }
		            }
		        ]
		    });
		
		/**
		 * store
		 */
		this.store = new Ext.data.JsonStore({
			url :__fullPath+"/bpm/instance/list.do",
			baseParams : {
						processName : "",
						limit : pageNum
			},
			root : "result",
			remoteSort : true,
			fields : ["billInfo","billKey","virProcessInstanceId","processInstanceId","processDefinitionId","deployId","processName","processVersion","createUserLoginId","createTime","status","operate"],
			totalProperty : 'totalCounts'
		});
		
		var sm = new Ext.grid.CheckboxSelectionModel();
		this.cm = new Ext.grid.ColumnModel([sm, {
			header : "虚拟流程实例ID",
			dataIndex : "virProcessInstanceId",
			hidden : true,
			width : 100
		},{
			header : "业务单据号",
			dataIndex : "billInfo",
			hidden : false,
			width : 100
		}, {
			header : "流程定义名称",
			dataIndex : "processName",
			width : 200
		}, {
			header : "发起人",
			dataIndex : "createUserLoginId",
			width : 100
		}, {
			header : "发起时间",
			dataIndex : "createTime",
			width : 200
		}, {
			header : "流程实例状态",
			dataIndex : "status",
			width : 100
		}, {
            xtype: 'actioncolumn',
            width: 150,
            items: [{
                icon   : __fullPath+"/images/btn/flow/view.png",  // Use a URL in the icon config
                tooltip: '查看历史',
                width : 50,
                handler: function(grid, rowIndex, colIndex) {
                    var billKey = grid.getStore().getAt(rowIndex).data.billKey;
                    var taskId = grid.getStore().getAt(rowIndex).data.taskId;
                    var processInstanceHisView = new OECP.bpm.ProcessInstanceHisView({billKey:billKey});
					var win = new Ext.Window({
							title : '流程实例历史',
							width : 800,
							height : 500,
							autoScroll : true,
							modal : true,
							items : [processInstanceHisView]
							
						});
					win.show();
                }
            },{
                icon   : __fullPath+"/images/btn/remove.png",  // Use a URL in the icon config
                tooltip: '停止流程实例',
                width : 50,
                handler: function(grid, rowIndex, colIndex) {
                	var processInstanceId = grid.getStore().getAt(rowIndex).data.processInstanceId;
                    var store = grid.getStore();
					var actionUtil = new ActionUtil();
					
					Ext.Msg.confirm('提示框', '您确定要停止该流程实例吗?', function(button) {
							if (button == 'yes') {
								actionUtil.stopProcessInstance(processInstanceId,store);
							}
					}, this);
                }
            }]
         }]);
	
		/**
		 * 列表底部工具栏
		 */
		this.pageBar = new Ext.PagingToolbar({
			pageSize : pageNum,
			store : this.store,
			displayInfo : true,
			displayMsg : '当前显示 {0}-{1}条记录 /共{2}条记录',
			emptyMsg : "无显示数据"
		});
		/**
		 * 列表顶部工具栏
		 */
		this.topbar = this.getTopbar();
		/**
		 * 列表grid
		 */
		this.grid = new Ext.grid.GridPanel({
			id : 'processInstanceGrid',
			height : 500,
			store : this.store,
			cm : this.cm,
			sm : sm,
			trackMouseOver : false,
			loadMask : true,
//			tbar : this.topbar,
			bbar : this.pageBar
		});
		
		/**
		 * 向容器里面增加组件
		 */
		this.add(searchForm);
		this.add(this.grid);
		
		/**
		 * 列表加载数据
		 */
		this.store.removeAll();
		this.store.load();
	}
});

/**
 * 查看历史功能
 */
OECP.bpm.ProcessInstanceView.viewHistory = function(billKey){
	var processInstanceHisView = new OECP.bpm.ProcessInstanceHisView({billKey:billKey});
	var win = new Ext.Window({
			title : '流程实例历史',
			width : 800,
			height : 500,
			autoScroll : true,
			modal : true,
			items : [processInstanceHisView]
			
		});
	win.show();
}
/**
 * 停止流程实例
 */
OECP.bpm.ProcessInstanceView.stopProcessInstance = function(processInstanceId){
	var store = Ext.getCmp("processInstanceGrid").getStore();
	var actionUtil = new ActionUtil();
	
	Ext.Msg.confirm('提示框', '您确定要停止该流程实例吗?', function(button) {
			if (button == 'yes') {
				actionUtil.stopProcessInstance(processInstanceId,store);
			}
	}, this);
	
}
