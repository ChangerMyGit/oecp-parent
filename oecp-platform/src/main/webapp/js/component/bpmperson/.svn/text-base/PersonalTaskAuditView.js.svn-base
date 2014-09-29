
Ext.ns("OECP.bpm");

/**
 * 流程审批页面
 * 
 * @author yangtao
 * @class OECP.bpm.PersonalTaskAuditView 
 * @extends Ext.Panel
 */
OECP.bpm.PersonalTaskAuditView =  Ext.extend(Ext.Panel, {
	/**
	 * id
	 */
	id : "PersonalTaskAuditView",
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
	 * 执行方法
	 * @param param
	 */
	executeTask : function(param){
		var url =  __ctxPath + "/bpm/person/task/completeTaskForBiz.do";
		Ext.Ajax.request({
			url:url,
			params:param,
			success:function(){
				Ext.Msg.alert('提示','执行成功',function(){
					Ext.getCmp("executeTaskwin").close();
				});
			},
			failure:function(flag){
				Ext.Msg.alert('提示',flag.responseText,function(){});
			}
		});
	},
	
	/**
	 * 初始化方法
	 */
	initComponent : function() {
		var scope = this;
		/**
		 * 初始化父类
		 */
		OECP.bpm.PersonalTaskAuditView.superclass.initComponent.call(this);
		/**
		 * 传入的参数
		 */
		var funcKey = this.funcKey;
		var bizKey = this.bizKey;
		var billInfo = this.billInfo;
		var counterSignRuleId = this.counterSignRuleId;
		/**
		 * 驳回时，选择上一个任务节点进行驳回
		 */
		var taskStore = new Ext.data.JsonStore({
			id : Ext.id(),
			url : __ctxPath+'/bpm/task/getPreTaskByCurrentTask.do' ,
			baseParams :{
				billKey : bizKey
			},
			root : "result",
			fields : ['taskName'],
			remoteSort : false,
			timeout : 8000,
			totalProperty: 'totalCounts'
			});
		var taskLabel = new Ext.form.Label ({
        	text:'选择驳回节点：',
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
		taskLabel.hide();
		taskCombo.hide();
		if(counterSignRuleId=='NO_COUNTERSIGN_RULE'){
			//没有会签的审批表单
			var bpmAuditFormForUndoTaskForNoCounterSign = new Ext.FormPanel({
						id :'_bpmAuditFormForUndoTaskForNoCounterSign',
						title:'审批信息',
				        frame:true,
				        buttonAlign:"center",    
				        height : 300,
				        width : 600,
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
					            xtype: 'radiogroup',
					            fieldLabel: '审批决定',
					            items: [
					                {boxLabel: '同意', 
					                name: 'auditDecision', 
					                inputValue: 'AGREE', 
					                checked: true,
					                listeners:{check : function(){
			            					if(this.checked){
			            						bpmAuditFormForUndoTaskForNoCounterSign.form.findField('auditOpinion').setValue("审批通过");
			            						taskLabel.hide();
				            					taskCombo.hide();
			            					}
									}}},
					                {boxLabel: '不同意',
					                name: 'auditDecision', 
					                inputValue: 'NO_AGREE',
					                listeners:{check : function(){
			            					if(this.checked){
			            						bpmAuditFormForUndoTaskForNoCounterSign.form.findField('auditOpinion').setValue("不同意，该流程结束");
			            						taskLabel.hide();
				            					taskCombo.hide();
			            					}
									}}},
					                {boxLabel: '驳回',
					                 name: 'auditDecision', 
					                 inputValue: 'BACK',
					                 listeners:{check : function(){
			            					if(this.checked){
			            						taskStore.load();
			            					}
									 }}},taskLabel,taskCombo
					            ]
					        }
				        ],
				        buttons: [{
				            text: '办理',
				            width:60,
				            handler : function(){
				            	var auditOpinion = bpmAuditFormForUndoTaskForNoCounterSign.form.findField('auditOpinion').getValue();
				            	var auditDecision = bpmAuditFormForUndoTaskForNoCounterSign.form.findField('auditDecision').getGroupValue();
				            	var preTaskName = taskCombo.getValue();
				            	if(auditOpinion.length>249){
				            		Ext.Msg.alert('提示','审批意见不能超过250个汉字');
				            		return;
				            	}
				            	scope.executeTask({funcKey:funcKey,bizKey:bizKey,billInfo:billInfo,auditOpinion:auditOpinion,auditDecision:auditDecision,preTaskName:preTaskName});
				            }
				        },{
				            text: '关闭',
				            width:60,
				            handler : function(){
				            	Ext.getCmp("executeTaskwin").close();
				            }
				        }]
			});
			
			this.add(bpmAuditFormForUndoTaskForNoCounterSign);
			
		}else{
			//有会签的审批表单
			var bpmAuditFormForUndoTaskForCounterSign = new Ext.FormPanel({
						id :'_bpmAuditFormForUndoTaskForCounterSign',
						title:'审批信息',
				        frame:true,
				        buttonAlign:"center",    
				        height : 300,
				        width : 600,
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
				            	var auditOpinion = bpmAuditFormForUndoTaskForCounterSign.form.findField('auditOpinion').getValue();
				            	var auditDecision = bpmAuditFormForUndoTaskForCounterSign.form.findField('auditDecision').getGroupValue();
				            	if(auditOpinion.length>249){
				            		Ext.Msg.alert('提示','审批意见不能超过250个汉字');
				            		return;
				            	}
				            	scope.executeTask({funcKey:funcKey,bizKey:bizKey,billInfo:billInfo,auditOpinion:auditOpinion,auditDecision:auditDecision});
				            }
				        },{
				            text: '关闭',
				            handler : function(){
				            	Ext.getCmp("executeTaskwin").close();
				            }
				        }]
			});
			
			this.add(bpmAuditFormForUndoTaskForCounterSign);
		}
	}
});
