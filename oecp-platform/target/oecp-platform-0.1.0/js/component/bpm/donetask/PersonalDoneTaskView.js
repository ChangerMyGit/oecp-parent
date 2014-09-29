/**
 * @inclule "../../../ext-oecp/ui/GridPanel.js"
 */
Ext.ns("OECP.bpm.donetask");

/**

 * 流程已办任务页面
 * 
 * @author yangtao
 * @class OECP.bpm.PersonalDoneTaskView
 * @extends Ext.Panel
 */
OECP.bpm.PersonalDoneTaskView =  Ext.extend(Ext.Panel, {
	/**
	 * 面板id
	 */
	id : 'PersonalDoneTaskViewForAdmin',
	/**
	 * 标题
	 */
	title : "流程已办任务列表(所有)",
	/**
	 * 关闭
	 */
	closable : true,
	/**
	 * 面板布局
	 */
	layout:'table',
	/**
	 * 面板布局配置
	 */
    layoutConfig: {
        columns: 1
    },
    /**
     * 滚动条
     */
	autoScroll : true,
	/**
	 * 流程已办任务列表(所有)页面记录数
	 * @type Number
	 */
	pageNum : 15,
	/**
	 * 初始化方法
	 */
	initComponent : function() {
		var scope = this;
		/**
		 * 初始化父类
		 */
		OECP.bpm.PersonalDoneTaskView.superclass.initComponent.call(this);
		
		var pageNum = this.pageNum;
		/**
		 * 查询form
		 */
		var searchForm = new Ext.FormPanel({
				layout:'table',
			    layoutConfig: {
			        columns: 10
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
			            text:'用户账号',
			            style:'font-size:13px'
			        },{
		        		xtype:'textfield',
		                name: '_userName',
		                width :150,
		                listeners:{focus:function(){this.selectText();}}
		            },{
			            text:'起始时间',
			            style:'font-size:13px'
		            },{
						xtype: 'datetimefield',
						name : 'beginTime',
						listeners:{focus:function(){this.selectText();}}
					 },{
						 text:'结束时间',
						 style:'font-size:13px'
					 },{
						xtype: 'datetimefield',
						name : 'endTime',
						listeners:{focus:function(){this.selectText();}}
					 },{
		            	xtype:'button',
		            	text: '查询',
		            	iconCls : 'btn-search',
			            handler : function(){
			            	var userName = searchForm.form.findField('_userName').getValue();
			            	var beginTime = searchForm.form.findField('beginTime').getValue();
			            	var endTime = searchForm.form.findField('endTime').getValue();
			            	var billInfo = searchForm.form.findField('_billInfo').getValue();
		            	
			            	scope.store.removeAll();
			            	scope.store.baseParams.userName = userName;
			            	scope.store.baseParams.beginTime = beginTime;
			            	scope.store.baseParams.endTime = endTime;
			            	scope.store.baseParams.billInfo = billInfo;
			            	scope.store.baseParams.limit = pageNum;
			            	
							scope.store.load();
			            }
		            },{
		            	xtype:'button',
		            	text: '重置',
		            	iconCls : 'btn-reset',
			            handler : function(){
			            	searchForm.form.findField('_userName').setValue("");
			            	searchForm.form.findField('beginTime').setValue("");
			            	searchForm.form.findField('endTime').setValue("");
			            	searchForm.form.findField('_billInfo').setValue("");
			            }
		            }
		        ]
		    });
		
		/**
		 * store
		 */
		this.store = new Ext.data.JsonStore({
			url :__fullPath+"/bpm/task/doneTask.do",
			baseParams : {
						userName : "",
						billInfo : "",
						beginTime : "",
						endTime : "",
						limit : pageNum
			},
			root : "result",
			remoteSort : true,
			fields : ["id","billInfo","taskId","billKey","processInstanceId","deployId","processName","activityName","status","startTime","endTime","auditUserName","auditDecision","auditOpinion","counterSignRuleId","counterSignRuleName","operate","formResourceName","agentUserName"],
			totalProperty : 'totalCounts'
		});
		
		var sm = new Ext.grid.CheckboxSelectionModel();
		this.cm = new Ext.grid.ColumnModel([{
			header : "历史任务ID",
			dataIndex : "id",
			hidden : true,
			width : 100
		},{
			header : "业务单据号",
			dataIndex : "billInfo",
			hidden : false,
			width : 100
		}, {
			header : "流程名称",
			dataIndex : "processName",
			width : 100
		}, {
			header : "任务节点名称",
			dataIndex : "activityName",
			width : 100
		},{
			header : "会签规则",
			dataIndex : "counterSignRuleName",
			width : 100
		}, {
			header : "开始时间",
			dataIndex : "startTime",
			width : 200
		}, {
			header : "结束时间",
			dataIndex : "endTime",
			width : 200
		}, {
			header : "应办人",
			dataIndex : "auditUserName",
			width : 100
		}, {
			header : "实际办理人",
			dataIndex : "agentUserName",
			width : 100
		}, {
			header : "审批决定",
			dataIndex : "auditDecision",
			width : 100
		}, {
			header : "审批意见",
			dataIndex : "auditOpinion",
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
                    var historyId = grid.getStore().getAt(rowIndex).data.id;
                    var processInstanceHisView = new OECP.bpm.ProcessInstanceHisView({billKey:billKey,historyId:historyId});
					var win = new Ext.Window({
							title : '流程实例历史',
							width : 800,
							height : 500,
							autoScroll : true,
							modal : true,
							items : [processInstanceHisView]
							
						})
				
					win.show();
                }
            },'  ',{
                icon   : __fullPath+"/images/btn/read_document.png",  // Use a URL in the icon config
                tooltip: '查看任务',
                handler: function(grid, rowIndex, colIndex) {
                    var billKey = grid.getStore().getAt(rowIndex).data.billKey;
					var taskId = grid.getStore().getAt(rowIndex).data.taskId;
					var taskName = grid.getStore().getAt(rowIndex).data.activityName;
					var auditDecision = grid.getStore().getAt(rowIndex).data.auditDecision;
					var auditOpinion = grid.getStore().getAt(rowIndex).data.auditOpinion;
					var processName = grid.getStore().getAt(rowIndex).data.processName;
					taskName = "“流程：【"+processName+"】步骤：【"+taskName+"】”";
					/**
					 * 获取单据流程查看页面路径
					 */
					Ext.Ajax.request({
							url:__ctxPath + "/bpm/task/getFormResourceName.do",
							params:{
								billKey:billKey
							},
							success:function(request){
								var formResourceName = request.responseText;
								if(Ext.isEmpty(formResourceName)){
									Ext.Msg.alert('提示','功能没有配置流程查看页面路径',function(){});
								}
								var bill_URL = __fullPath+formResourceName;
								bill_URL = bill_URL+(bill_URL.indexOf('?')>=0?'&':'?')+'billId='+billKey;
								var fromPanel = new Ext.create({
									xtype : "iframepanel",
									title : '单据信息',
									height : 500,
									loadMask : {
										msg : "正在加载...,请稍等..."
									},
									defaultSrc : bill_URL,
									listeners : {
									    documentloaded : function(d) {
										    if(Ext){
											updateTheme(d.dom.contentWindow);
										    }
										}
									}
								}); 
								var auditForm = new Ext.FormPanel({
											title:'审批信息',
									        frame:true,
									        buttonAlign:"center",    
									        height : 215,
									        defaultType: 'textfield',
									        items: [{
									                fieldLabel: '审批意见',
									                name: 'auditOpinion',
									                xtype: 'textarea',
									                width: 400,
									        		height : 100,
									        		value : auditOpinion,
													readOnly : true
									            },{
									            	name: 'auditDecision',
										            fieldLabel: '审批决定',
										            value :auditDecision,
										            readOnly : true
										        }
									        ],
									        buttons: [{
									            text: '关闭',
									            handler : function(){
									            	viewTaskWin.close();
									            }
									        }]
								});
								
								var viewTaskWin = new Ext.Window({
										width : 800,
										height : 500,
										modal : true,
										autoScroll:true,
										items : [fromPanel,auditForm]
										
								});
								
								viewTaskWin.title = '查看'+taskName+'任务';
								viewTaskWin.show();
							},
							failure:function(flag){
								Ext.Msg.alert('提示',flag.responseText,function(){});
							}
					});
                }
            },{
                icon   : __fullPath+"/images/btn/flow/back.gif",  // Use a URL in the icon config
                tooltip: '取回任务',
                handler: function(grid, rowIndex, colIndex) {
                    var histaskEoId = grid.getStore().getAt(rowIndex).data.id;
                    var activityName = grid.getStore().getAt(rowIndex).data.activityName;
                    var actionUtil = new ActionUtil();
					var store = grid.getStore();
					Ext.Msg.confirm('提示框', '您确定要取回任务：'+activityName+' ?', function(button) {
						if (button == 'yes') {
							actionUtil.backTask({histaskEoId:histaskEoId},store);
						}
					}, this);
                }
             }]
         }]);
		
		var viewConfig = Ext.apply({
			forceFit : true
		}, this.gridViewConfig);
		
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
		 * 列表grid
		 */
		this.grid = new Ext.grid.GridPanel({
			height : 500,
			store : this.store,
			cm : this.cm,
//			sm : sm,
			trackMouseOver : false,
			loadMask : true,
			bbar : this.pageBar,
			viewConfig : viewConfig
		});

		/**
		 * 增加组件
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
