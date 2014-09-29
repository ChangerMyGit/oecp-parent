/**
 * @inclule "../../../ext-oecp/ui/GridPanel.js"
 */
Ext.ns("OECP.bpm.undotask");

/**

 * 流程待办任务页面
 * 
 * @author yangtao
 * @class OECP.bpm.PersonalTaskView
 * @extends Ext.Panel
 */
OECP.bpm.PersonalTaskView =  Ext.extend(Ext.Panel, {
	/**
	 * id
	 */
	id : 'PersonalTaskViewForAdmin',
	/**
	 * 标题
	 */
	title : "流程待办任务列表(所有)",
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
	 * 流程待办任务列表(所有)页面记录数
	 * @type Number
	 */
	pageNum : 15,
	/**
	 * 查看历史
	 * @param billKey
	 */
	viewHistory : function(billKey) {
		var processInstanceHisView = new OECP.bpm.ProcessInstanceHisView({billKey:billKey});
		var win = new Ext.Window({
				title : '流程实例历史',
				width : 800,
				height : 500,
				autoScroll : true,
				modal : true,
				items : [processInstanceHisView]
				
			})
	
		win.show();
	},
	/**
	 * 执行任务
	 * @param grid
	 * @param billKey
	 * @param taskId
	 * @param taskName
	 * @param counterSignRuleId
	 * @param formResourceName
	 */
	executeTask : function(grid,billKey,taskId,taskName,counterSignRuleId,nextTask,nextTaskUser,editBill){
		
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
					bill_URL = bill_URL+(bill_URL.indexOf('?')>=0?'&':'?')+'billId='+billKey+'&editBill='+editBill;
					/**
					 * 当前业务单据
					 */
					var fromPanel = new Ext.create({
						xtype : "iframepanel",
						title:"单据信息",
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
					
					/**
					 * 驳回时，选择上一个任务节点进行驳回
					 */
					var taskStore = new Ext.data.JsonStore({
						id : Ext.id(),
						url : __ctxPath+'/bpm/task/getPreTaskByCurrentTask.do' ,
						baseParams :{
							billKey : billKey
						},
						root : "result",
						fields : ['taskName'],
						remoteSort : false,
						timeout : 8000,
						totalProperty: 'totalCounts'
						});
					var taskLabel = new Ext.form.Label ({
			        	text:'选择驳回节点:',
			        	style:'font-size:12px;'
					});
					var taskCombo = new Ext.form.ComboBox({
						xtype : 'combo',
						fieldLabel : "上面任务名称",
						name : "processDefinitionName",
						store : taskStore,
						displayField : 'taskName',
						valueField : "taskName",
						// readOnly : true,
						typeAhead : true,
						mode : 'local',
						editable:false,
						triggerAction : 'all',
						selectOnFocus : true,
						allowBlank : true,
						width : 180,
						listeners:{
							select : function(){
								var preTaskName = taskCombo.getValue();
							    bpmAuditFormForUndoTaskForNoCounterSign.form.findField('auditOpinion').setValue("");
							    bpmAuditFormForUndoTaskForNoCounterSign.form.findField('auditOpinion').setValue("审批未通过，驳回到任务:"+preTaskName);
							}
						}
					});
					
					taskStore.on("load",function(){
						  if(taskStore.getCount()==0){
						  	Ext.Msg.alert("提示","没有上一个任务无法驳回",function(){});
						  	bpmAuditFormForUndoTaskForNoCounterSign.form.findField('auditDecision').setValue("AGREE");
						  	return;
						  }
						  var firstValue = taskStore.getAt(0).get('taskName');
						  taskCombo.setValue(firstValue);//同时下拉框会将与name为firstValue值对应的 text显示
						  taskLabel.show();
						  taskCombo.show();
						  taskCombo.fireEvent('select',taskCombo,taskStore.getAt(0),0);
					});
					
					
					/**
					 * 同意时，选择下一个任务节点（多个任务结点时）
					 */
					var nextTaskStore = new Ext.data.JsonStore({
						id : Ext.id(),
						url : __ctxPath+'/bpm/task/getNextTasksByCurrentTask.do' ,
						baseParams :{
							billKey : billKey
						},
						root : "result",
						fields : ['taskName','incomeTransitionName'],
						remoteSort : false,
						timeout : 8000,
						totalProperty: 'totalCounts'
						});
					var nextTaskLabel = new Ext.form.Label ({
			        	text:'选择下个节点：',
			        	style:'font-size:12px;'
					});
					var nextTaskCombo = new Ext.form.ComboBox({
						xtype : 'combo',
						fieldLabel : "下面任务名称",
						store : nextTaskStore,
						displayField : 'taskName',
						valueField : "incomeTransitionName",
						// readOnly : true,
						typeAhead : true,
						mode : 'local',
						editable:false,
						triggerAction : 'all',
						selectOnFocus : true,
						allowBlank : true,
						width : 180
					});
					nextTaskStore.on("load",function(){
						 if(nextTaskStore.getCount()!=0){
    					 	 var firstValue = nextTaskStore.getAt(0).get('incomeTransitionName');
							  nextTaskCombo.setValue(firstValue);//同时下拉框会将与name为firstValue值对应的 text显示
							  nextTaskCombo.fireEvent('select',nextTaskCombo,nextTaskStore.getAt(0),0);
    					 }		
					});
					nextTaskCombo.on('select', function(combo, record, index) {
						nextTaskUserCombo.store.removeAll();
						// TODO 加校验
						nextTaskUserCombo.getStore().load({
									params : {
										billKey : billKey,
										activityName : record.get('taskName')
									}
								})
					});
					nextTaskStore.load();
					
					/**
					 * 同意时，给下一个任务指派人员
					 */
					var nextTaskUserLabel = new Ext.form.Label ({
			        	text:'下个结点人员指派：',
			        	style:'font-size:12px;'
					});
					var nextTaskUserCombo = new Ext.ux.form.LovCombo({
						fieldLabel : '下个结点人员指派',triggerAction:'all',mode:'local',anchor:'85%'
						,valueField:'id',displayField:'name',
						store:{
							xtype:'jsonstore',url:__ctxPath+'/bpm/def/queryUserList.do',
							fields:['id', 'loginId', 'name'],root:'result',totalProperty:'totalCounts',autoLoad:false
						}
					});
					
					var nextTaskVar = [];
					if(nextTask=='true'&&nextTaskUser=='true')
						nextTaskVar = [nextTaskLabel,nextTaskCombo,nextTaskUserLabel,nextTaskUserCombo];
					else if(nextTask=='true'&&nextTaskUser!='true')	
						nextTaskVar = [nextTaskLabel,nextTaskCombo];
					else if(nextTask!='true'&&nextTaskUser=='true')	
						nextTaskVar = [nextTaskUserLabel,nextTaskUserCombo];
					
						
					if(counterSignRuleId=='NO_COUNTERSIGN_RULE'){
						//没有会签的审批表单
						var bpmAuditFormForUndoTaskForNoCounterSign = new Ext.FormPanel({
									title:'审批信息',
							        frame:true,
							        buttonAlign:"center",    
							        height : 270,
							        autoScroll : true,
							        defaultType: 'textfield',
							        items: [{
							                fieldLabel: '审批意见',
							                name: 'auditOpinion',
							                xtype: 'textarea',
							                width: 400,
							        		height : 100,
							        		value : '审批通过',
							        		listeners:{focus : function(){this.focus(true,true);}}
							            },{
							                name: 'taskId',
							                xtype: 'hidden',
							                value:taskId
							            },{
								            xtype: 'radiogroup',
								            fieldLabel: '审批决定',
								            items: [
								                {
									                boxLabel: '同意', 
									                name: 'auditDecision', 
									                inputValue: 'AGREE', 
									                checked: true,
									                listeners:{check : function(){
							            					if(this.checked){
							            						bpmAuditFormForUndoTaskForNoCounterSign.form.findField('auditOpinion').setValue("审批通过");
							            						if(nextTask!='true'&&nextTaskUser!='true'){
							            							Ext.getCmp('_nextTaskConfig').hide();
							            						}else{
							            							if(nextTaskStore.getCount()==0)
									            						Ext.getCmp('_nextTaskConfig').hide();
									            					else
									            						Ext.getCmp('_nextTaskConfig').show();
							            						}
							            						Ext.getCmp('_preTaskConfig').hide();
							            					}
													}}
												},{
									                boxLabel: '不同意',
									                name: 'auditDecision', 
									                inputValue: 'NO_AGREE',
									                listeners:{check : function(){
							            					if(this.checked){
							            						bpmAuditFormForUndoTaskForNoCounterSign.form.findField('auditOpinion').setValue("不同意，该流程结束");
							            						Ext.getCmp('_preTaskConfig').hide();
							            						Ext.getCmp('_nextTaskConfig').hide();
							            					}
													}}
												},{
									                 boxLabel: '驳回',
									                 name: 'auditDecision', 
									                 inputValue: 'BACK',
									                 listeners:{check : function(){
							            					if(this.checked){
							            						taskStore.load();
							            						Ext.getCmp('_preTaskConfig').show();
							            						Ext.getCmp('_nextTaskConfig').hide();
							            					}
													 }}
												 }
								            ]
								        },{
								            xtype:'fieldset',
								            id :'_nextTaskConfig',
								            title: '下个任务结点配置',
								            hidden:((nextTask!='true'&&nextTaskUser!='true')?true:false),
								            collapsible: false,
								            height : 60,
								            items :[{
										        	xtype:'panel',
										        	layout:'table',
												    layoutConfig: {
												        columns: 6
												    },
										        	items:nextTaskVar
									           }
								            ]
							        	},{
								            xtype:'fieldset',
								            id :'_preTaskConfig',
								            title: '上一个个任务结点配置',
								            hidden : true,
								            collapsible: false,
								            height : 60,
								            items :[{
										        	xtype:'panel',
										        	layout:'table',
												    layoutConfig: {
												        columns: 6
												    },
										        	items:[
										        		taskLabel,taskCombo
										        	]
									           }
								            ]
							        	}
							        ],
							        buttons: [{
							            text: '办理',
							            handler : function(){
							            	var store = grid.getStore();
							            	var taskId = bpmAuditFormForUndoTaskForNoCounterSign.form.findField('taskId').getValue();
							            	var nextperson = '';//bpmauditFormForUndoTask.form.findField('nextperson').getValue();
							            	var auditOpinion = bpmAuditFormForUndoTaskForNoCounterSign.form.findField('auditOpinion').getValue();
							            	var auditDecision = bpmAuditFormForUndoTaskForNoCounterSign.form.findField('auditDecision').getGroupValue();
							            	var preTaskName = taskCombo.getValue();
							            	var nextTransitionName = nextTaskCombo.getValue();
							            	var nextTaskUser = nextTaskUserCombo.getValue();
							            	if(auditOpinion.length>249){
							            		Ext.Msg.alert('提示','审批意见不能超过250个汉字');
							            		return;
							            	}
							            	var actionUtil = new ActionUtil();
							            	actionUtil.executeTask({taskId:taskId,nextperson:nextperson,auditOpinion:auditOpinion,auditDecision:auditDecision,preTaskName:preTaskName,nextTransitionName:nextTransitionName,nextTaskUser:nextTaskUser},executeTaskwin,store);
							            }
							        },{
							            text: '关闭',
							            handler : function(){
							            	executeTaskwin.close();
							            }
							        }]
						});
						var executeTaskwin = new Ext.Window({
								width : 800,
								height : 500,
								modal : true,
								border:false,
								autoScroll:true,
								items : [fromPanel,bpmAuditFormForUndoTaskForNoCounterSign]
								
						});
					}else{
						//有会签的审批表单
						var bpmAuditFormForUndoTaskForCounterSign = new Ext.FormPanel({
									title:'审批信息',
							        frame:true,
							        buttonAlign:"center",    
							        height : 215,
							        autoScroll : true,
							        defaultType: 'textfield',
							        items: [{
							                fieldLabel: '审批意见',
							                name: 'auditOpinion',
							                xtype: 'textarea',
							                width: 400,
							        		height : 100,
							        		value : '审批通过',
							        		listeners:{focus : function(){this.focus(true,true);}}
							            },{
							                name: 'taskId',
							                xtype: 'hidden',
							                value:taskId
							            },{
								            xtype: 'radiogroup',
								            fieldLabel: '审批决定',
								            items: [
								                {boxLabel: '同意', 
								                name: 'auditDecision', 
								                inputValue: 'AGREE', 
								                checked: true,
								                listeners:{check : function(){
						            					if(this.checked){
						            						bpmAuditFormForUndoTaskForCounterSign.form.findField('auditOpinion').setValue("审批通过");
						            					}
												}}},
								                {boxLabel: '不同意',
								                name: 'auditDecision', 
								                inputValue: 'NO_AGREE',
								                listeners:{check : function(){
						            					if(this.checked){
						            						bpmAuditFormForUndoTaskForCounterSign.form.findField('auditOpinion').setValue("不同意，该流程结束");
						            					}
												}}}
								            ]
								        }
							        ],
							        buttons: [{
							            text: '办理',
							            handler : function(){
							            	var store = grid.getStore();
							            	var taskId = bpmAuditFormForUndoTaskForCounterSign.form.findField('taskId').getValue();
							            	var nextperson = '';//bpmauditFormForUndoTask.form.findField('nextperson').getValue();
							            	var auditOpinion = bpmAuditFormForUndoTaskForCounterSign.form.findField('auditOpinion').getValue();
							            	var auditDecision = bpmAuditFormForUndoTaskForCounterSign.form.findField('auditDecision').getGroupValue();
							            	if(auditOpinion.length>249){
							            		Ext.Msg.alert('提示','审批意见不能超过250个汉字');
							            		return;
							            	}
							            	var actionUtil = new ActionUtil();
							            	actionUtil.executeTask({taskId:taskId,nextperson:nextperson,auditOpinion:auditOpinion,auditDecision:auditDecision},executeTaskwin,store);
							            }
							        },{
							            text: '关闭',
							            handler : function(){
							            	executeTaskwin.close();
							            }
							        }]
						});
						
						var executeTaskwin = new Ext.Window({
								width : 800,
								height : 500,
								modal : true,
								border:false,
								autoScroll:true,
								items : [fromPanel,bpmAuditFormForUndoTaskForCounterSign]
								
						});
					}
						
					executeTaskwin.title = '执行'+taskName+'任务';
					executeTaskwin.show();
				},
				failure:function(flag){
					Ext.Msg.alert('提示',flag.responseText,function(){});
				}
		});
	},
	/**
	 * 重新指派
	 * @param mainGrid
	 * @param taskId
	 * @param activityName
	 * @param assignOrgId
	 */
	reasign : function(mainGrid,taskId,activityName,assignOrgId){
		/**************begin角色*******************/
		var sm = new Ext.grid.CheckboxSelectionModel();
		var store = new Ext.data.JsonStore({
			url : __ctxPath + '/bpm/def/queryRoleByOrgId.do?orgId='+assignOrgId ,
			root : "result",
			fields : [ "id","name","code"],
			totalProperty : 'totalCounts'
		});
		var cm = new Ext.grid.ColumnModel([sm, {
				header : "主键",
				dataIndex : "id",
				hidden : false
			}, {
				header : "编码",
				dataIndex : "code"
			}, {
				header : "名称",
				dataIndex : "name"
			}]);
		

		var grid = new Ext.grid.GridPanel({
			height : 300,
			store : store,
			cm : cm,
			sm : sm,
			trackMouseOver : false,
			loadMask : true,
			autoScroll : true,
			frame : true,
			hidden : false
		});

		
		store.removeAll();
		store.load();
		/**************end角色*******************/
		
		/**************begin岗位*******************/
		var postSm = new Ext.grid.CheckboxSelectionModel();
		var postStore = new Ext.data.JsonStore({
			url : __ctxPath + "/bpm/def/getPostList.do",
			baseParams : {
				assignOrgId : assignOrgId
			},
			storeId : 'id',
			fields : ['id', 'name', 'code', 'charge', 'parent.id',
					'parent.name', 'dept.id', 'dept.name'],
			autoLoad : true
		});
		var postCm = new Ext.grid.ColumnModel([postSm, {
					header : "主键",
					dataIndex : "id",
					hidden : false
				}, {
					header : "岗位名称",
					dataIndex : "name"
				}, {
					header : "岗位编码",
					dataIndex : "code"
				}]);
		
		
		var postGrid = new Ext.grid.GridPanel({
			height : 300,
			store : postStore,
			cm : postCm,
			sm : postSm,
			trackMouseOver : false,
			loadMask : true,
			autoScroll : true,
			frame : true,
			hidden : false
		});


		
		postStore.removeAll();
		postStore.load();
		/**************end岗位*******************/
		
		/**************begin用户*******************/
		var userSearchForm = new Ext.FormPanel({
			height : 35,
			frame : true,
			layout : 'column',
			defaults : {
				xtype : 'label'
			},
			items : [{
						text : '用户账号'
					}, {
						xtype : 'hidden',
						name : 'conditions[0].field',
						value : 'loginId'
					}, {
						xtype : 'hidden',
						name : 'conditions[0].operator',
						value : 'like'
					}, {
						width : 80,
						xtype : 'textfield',
						name : 'conditions[0].value'
					}, {
						text : '用户姓名'
					}, {
						xtype : 'hidden',
						name : 'conditions[1].field',
						value : 'name'
					}, {
						xtype : 'hidden',
						name : 'conditions[1].operator',
						value : 'like'
					}, {
						width : 80,
						xtype : 'textfield',
						name : 'conditions[1].value'
					},{
						width : 80,
						xtype : 'hidden',
						name : 'orgId',
						value : assignOrgId
					},  {
						xtype : 'button',
						text : '查询',
						iconCls : 'search',
						handler : function() {
							var grid = userGrid;
							if (userSearchForm.getForm().isValid()) {
								userSearchForm.getForm().submit({
									waitMsg : '正在提交查询',
									url : __ctxPath + '/bpm/def/queryUserList.do',
									success : function(formPanel, action) {
										var result = Ext.util.JSON
												.decode(action.response.responseText);
										grid.getStore().loadData(result);
									}
								});
							}
						}
					}]
		});
		var userSm = new Ext.grid.CheckboxSelectionModel();
		var userStore = new Ext.data.JsonStore({
			url : __ctxPath + '/bpm/def/queryUserList.do' ,
			baseParams : {
				orgId : assignOrgId
			},
			root : "result",
			fields : ['id', 'loginId', 'name', 'email','createTime'],
			totalProperty : 'totalCounts'
		});
		var userCm = new Ext.grid.ColumnModel([userSm, {
							header : "id",
							dataIndex : 'id',
							hidden : true
						}, {
							header : "账号",
							dataIndex : 'loginId',
							width : 60
						}, {
							header : "用户名",
							dataIndex : 'name',
							width : 60
						}, {
							header : "邮箱",
							dataIndex : 'email',
							width : 120
						}, {
							header : "创建时间",
							dataIndex : 'createTime',
							width : 100
						}]);
		
		
		var userGrid = new Ext.grid.GridPanel({
			height : 260,
			store : userStore,
			cm : userCm,
			sm : userSm,
			trackMouseOver : false,
			loadMask : true,
			autoScroll : true,
			frame : true,
			hidden : false
		});
		
		
		userStore.removeAll();
		userStore.load();
		/**************end用户*******************/
		
		/**
		 * 填充显示内容
		 */
		var cpanel = new Ext.Panel({
					defaultType : 'panel',
					layout : 'border',
					height : 340,
					baseCls : 'x-plain',
					autoScroll : true,
					items : [new Ext.Panel({
										id : "assignFlagForm",
										region : "center",
										frame : true,
										items : [{
											id : 'assignFlagRadioGroup',
								            xtype: 'radiogroup',
								            items: [
								                {boxLabel: '角色', 
								                name: 'assignFlag', 
								                inputValue: 'ROLE', 
								                checked: true,listeners:{check : function(){
								                					if(this.checked){
								                						grid.show();
																		postGrid.hide();
																		userGrid.hide();
								                					}
								                }}},
								                {boxLabel: '岗位', 
								                name: 'assignFlag', 
								                inputValue: 'POST', 
								                listeners:{check : function(){
								                					if(this.checked){
								                						postGrid.show();
																		grid.hide();
																		userGrid.hide();
								                					}
								                }}},
								                {boxLabel: '人员',
								                name: 'assignFlag', 
								                inputValue: 'USER', 
								                listeners:{check : function(){
								                					if(this.checked){
								                						userGrid.show();
																		grid.hide();
																		postGrid.hide();
								                					}
								                }}}
			            ]
			        },grid,postGrid,userSearchForm,userGrid]
				})]
		});
		
		/**
		 * 任务节点指派人员配置窗口
		 */
		var win = new Ext.Window({
			title : activityName+'--任务节点指派人员',
			width : 500,
			height : 400,
			modal : true,
			buttons : [{
				xtype : 'button',
				text : "保存",
				iconCls : 'save',
				handler : function() {
					var assignFlag = "";
					var assignFlagRadioGroup=cpanel.items.items[0].items.items[0];   
					assignFlagRadioGroup.eachItem(function(item){   
					    if(item.checked===true){   
					       assignFlag = item.inputValue;   
					    }   
					});
					var tmp_grid;
					if(assignFlag=='ROLE'){
						tmp_grid = grid;
					}else if(assignFlag=='POST'){
						tmp_grid = postGrid;
					}else if(assignFlag=='USER'){
						tmp_grid = userGrid;
					}
					var record = tmp_grid.getSelectionModel().getSelected();
					var records = tmp_grid.getSelectionModel().getSelections();
					
					var ids = "";
					for (var i = 0; i < records.length; i++) {
						var record = records[i];
						var id = record.get('id');
						ids += id;
						ids += ",";
					};
					var store = mainGrid.getStore();
					var actionUtil = new ActionUtil();
					actionUtil.reassignVirProActivity({taskId:taskId,assignOrgId:assignOrgId,ids:ids,assignFlag:assignFlag},win,store);
				}
			}, {
				xtype : 'button',
				handler : function() {
					win.close();
				},
				text : '关闭',
				scope : this
			}],
			items : [cpanel]
		});
		win.show();
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
			text : '执行任务',
			pressed : true,
			handler : this.execute,
			scope : this,
			iconCls : 'search'
		})/**, '   ', new OECP.ui.Button({
			text : '委派',
			pressed : true,
			handler : this.reasign,
			scope : this,
			iconCls : 'search'
		})**/
		];
	},
	
	/**
	 * 初始化方法
	 */
	initComponent : function() {
		var scope = this;
		/**
		 * 初始化父类
		 */
		OECP.bpm.PersonalTaskView.superclass.initComponent.call(this);
		
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
			            text:'开始时间',
			            style:'font-size:13px'
		            },{
						xtype: 'datetimefield',
						name : 'beginTime',
						listeners:{focus:function(){this.selectText();}}
					 },{
						 text:'到',
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
			url :__fullPath+"/bpm/task/list.do",
			baseParams : {
						billInfo : "",
						userName : "",
						beginTime : "",
						endTime : "",
						limit : pageNum
			},
			root : "result",
			remoteSort : true,
			fields : ["id","billInfo","billKey","taskId","taskName","assignee","executionId","formResourceName","deployId","processName","assignOrgId","createTime","taskCandiateUser","counterSignRuleId","counterSignRuleName","operate","nextTask","nextTaskUser","editBill"],
			totalProperty : 'totalCounts'
		});
		
		var sm = new Ext.grid.CheckboxSelectionModel();
		this.cm = new Ext.grid.ColumnModel([{
			header : "待办任务ID",
			dataIndex : "id",
			hidden : true,
			width : 100
		},{
			header : "业务单据号",
			dataIndex : "billInfo",
			hidden : false,
			width : 100
		}, {
			header : "当前任务节点",
			dataIndex : "taskName",
			width : 100
		},  {
			header : "流程名称",
			dataIndex : "processName",
			width : 100
		}, {
			header : "开始时间",
			dataIndex : "createTime",
			width : 200
		},{
			header : "会签规则",
			dataIndex : "counterSignRuleName",
			width : 100
		}, {
			header : "该任务的候选人",
			dataIndex : "taskCandiateUser",
			width : 400
		},{
            xtype: 'actioncolumn',
            width: 150,
            items: [{
                icon   : __fullPath+"/images/btn/flow/view.png",  // Use a URL in the icon config
                tooltip: '查看历史',
                width : 50,
                handler: function(grid, rowIndex, colIndex) {
                    var billKey = grid.getStore().getAt(rowIndex).data.billKey;
                    scope.viewHistory(billKey);
                }
            },{
                icon   : __fullPath+"/images/btn/task/task.gif",  // Use a URL in the icon config
                tooltip: '执行任务',
                handler: function(grid, rowIndex, colIndex) {
                    var billKey = grid.getStore().getAt(rowIndex).data.billKey;
					var taskId = grid.getStore().getAt(rowIndex).data.taskId;
					var taskName = grid.getStore().getAt(rowIndex).data.taskName;
					var counterSignRuleId = grid.getStore().getAt(rowIndex).data.counterSignRuleId;
					var formResourceName = grid.getStore().getAt(rowIndex).data.formResourceName;
					var nextTask = grid.getStore().getAt(rowIndex).data.nextTask;
					var nextTaskUser = grid.getStore().getAt(rowIndex).data.nextTaskUser;
					var editBill = grid.getStore().getAt(rowIndex).data.editBill;
					var processName = grid.getStore().getAt(rowIndex).data.processName;
					taskName = "“流程：【"+processName+"】步骤：【"+taskName+"】”";
					scope.executeTask(grid,billKey,taskId,taskName,counterSignRuleId,nextTask,nextTaskUser,editBill);
					
                }
            },{
                icon   : __fullPath+"/images/btn/task/myAssign.png",  // Use a URL in the icon config
                tooltip: '委派任务',
                handler: function(grid, rowIndex, colIndex) {
                    var taskId = grid.getStore().getAt(rowIndex).data.taskId;
					var taskName = grid.getStore().getAt(rowIndex).data.taskName;
					var assignOrgId = grid.getStore().getAt(rowIndex).data.assignOrgId;
                    scope.reasign(grid,taskId,taskName,assignOrgId);
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
		 * grid列表
		 */
		this.grid = new Ext.grid.GridPanel({
			height : 500,
			store : this.store,
			cm : this.cm,
//			sm : sm,
			trackMouseOver : false,
			loadMask : true,
//			tbar : this.topbar,
			bbar : this.pageBar
		});

		/**
		 * 增加组件
		 */
		this.add(searchForm);
		this.add(this.grid);
		
		/**
		 * 加载列表数据
		 */
		this.store.removeAll();
		this.store.load();
	}
});
