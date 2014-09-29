Ext.ns("OECP.Task")

/**
 * 预警配置面板
 * @class OECP.Task.TaskPanel
 * @extends Ext.Panel
 */
OECP.Task.TaskPanel = Ext.extend(Ext.Panel, {
	id : 'OECP.Task.TaskPanel',
	title : '任务信息',
	layout : 'fit',
	listGroupUrl : __ctxPath +'/task/manage/listgroup.do',
	/**
	 * 任务组下拉页面记录数
	 */
	comboPageNum : 10,
	/**
	 * 初始化方法
	 */
	initComponent : function(){
		var me = this;
		this.isView = this.isView;
		var comboPageNum = this.comboPageNum;
		me.groupCombo = new Ext.form.ComboBox({
			columnWidth:0.44,
			hiddenName:'task.oecpTaskGroup.id',
			name: 'task.oecpTaskGroup.id',
			fieldLabel: '任务组',
		    store: new Ext.data.JsonStore({
				url : this.listGroupUrl ,
				root : "result",
				fields : ['id','name'],
				autoLoad:true,
				totalProperty: 'totalCounts'
			}),
		    width:200,
		    displayField:'name',
		    valueField:'id',
		    mode: 'local',
		    emptyText:'请选择任务组',
		    triggerAction:'all',
		    pageSize:this.comboPageNum,
		    listeners : {
				blur : function(combo) {// 当其失去焦点的时候
					if (combo.getRawValue() == '') {
						combo.reset();
					}
				},
				beforequery : function(queryEvent) {
					var param = queryEvent.query;
					this.store.load({
						params : {
							limit : comboPageNum,
							'conditions[0].field':'name',
							'conditions[0].operator':'like',
							'conditions[0].value' : '%'+param+'%'
						},
						callback : function(r, options, success) {

						}
					});
					return true;
				}
			}
		});
		var addBtn = new OECP.ui.Button({
			columnWidth:0.2,
			text : "新增	",
			iconCls : 'btn-add',
			handler : function(){
				me.addGroup(me);
			}
		});
		var items = [];
		if(me.isView)
			items = [{columnWidth:0.35,xtype:'label',text:'任务组:',style:'font-size:13px;',align:'center'},me.groupCombo]
		else
			items = [{columnWidth:0.35,xtype:'label',text:'任务组:',style:'font-size:13px;',align:'center'},me.groupCombo,addBtn];
		this.TaskFormPanel = new Ext.FormPanel({
	        labelWidth: 100, // label settings here cascade unless overridden
	        frame:true,
	        bodyStyle:'padding:5px 5px 0',
	        width: 350,
	        defaults: {width: 230},
	        defaultType: 'textfield',
	        reader : new Ext.data.JsonReader({
				root : 'result'
			}, [{
				name : 'task.id',
				mapping : 'id'
			},{
				name : 'task.createTime',
				mapping : 'createTime'
			},{
				name : 'task.name',
				mapping : 'name'
			},{
				name : 'task.taskitf',
				mapping : 'taskitf'
			},{
				name : 'task.methodName',
				mapping : 'methodName'
			},{
				name : 'task.methodParams',
				mapping : 'methodParams'
			},{
				name : 'task.description',
				mapping : 'description'
			}]),
	        items: [{
                	xtype:'hidden',
	                name: 'task.id'
	            },{
                	xtype:'hidden',
	                name: 'task.createTime'
	            },{
	                fieldLabel: '任务名称',
	                name: 'task.name',
	                allowBlank:false,
	                width:300
	            },{
	                fieldLabel: '任务插件',
	                name: 'task.taskitf',
	                allowBlank:false,
	                width:300
	            },{
	                fieldLabel: '任务插件方法名称',
	                name: 'task.methodName',
	                allowBlank:false,
	                width:300
	            },{
	                fieldLabel: '任务插件方法参数',
	                name: 'task.methodParams',
	                width:300
	            },{
	            	xtype:'panel',
	            	width:300,
		           	layout:'column',
		           	items:items
		            },{
		            xtype: 'textarea',
		            fieldLabel: '任务描述',
		            name:'task.description',
	            	width:300,
	                height:200
		        }]
	    });
		this.items = [this.TaskFormPanel]; 
		OECP.Task.TaskPanel.superclass.initComponent.call(this);
	},
	/**
	 * 获取预警信息form的数据
	 * @return {}
	 */
	getFormValues : function(){
		var result = new Array();
		var name = this.TaskFormPanel.form.findField('task.name').getValue();
		var taskitf = this.TaskFormPanel.form.findField('task.taskitf').getValue();
		var methodName = this.TaskFormPanel.form.findField('task.methodName').getValue();
		var oecpTaskGroup = this.TaskFormPanel.form.findField('task.oecpTaskGroup.id').getValue();
		if(Ext.isEmpty(name)){
		 	Ext.Msg.alert('提示','任务名称不能为空');
		 	result[0] = false;
		 	return result;
		}else{
		 	result[0] = true;
		}
		if(Ext.isEmpty(taskitf)){
		 	Ext.Msg.alert('提示','任务插件不能为空');
		 	result[0] = false;
		 	return result;
		}else{
		 	result[0] = true;
		}
		if(Ext.isEmpty(methodName)){
		 	Ext.Msg.alert('提示','任务插件的方法名称不能为空');
		 	result[0] = false;
		 	return result;
		}else{
		 	result[0] = true;
		}
		if(Ext.isEmpty(oecpTaskGroup)){
		 	Ext.Msg.alert('提示','请选择任务组');
		 	result[0] = false;
		 	return result;
		}else{
		 	result[0] = true;
		}
		result[1] = this.TaskFormPanel.getForm().getValues();
		return result;
	},
	/**
	 * 新增任务组
	 * @param me
	 */
	addGroup : function(me){
		var window = new OECP.Task.AddGroupWindow();
		window.show();
	}
});