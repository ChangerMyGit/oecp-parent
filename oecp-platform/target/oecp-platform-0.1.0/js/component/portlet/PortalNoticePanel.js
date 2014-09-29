Ext.ns("OECP.portlet");

/**
 * 预警配置面板
 * @class OECP.Task.TaskPanel
 * @extends Ext.Panel
 */
OECP.portlet.PortalNoticePanel = Ext.extend(Ext.Panel, {
	id : 'OECP.portlet.PortalNoticePanel',
	title : '公告信息',
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
//		me.groupCombo = new Ext.form.ComboBox({
//			columnWidth:0.44,
//			hiddenName:'task.oecpTaskGroup.id',
//			name: 'task.oecpTaskGroup.id',
//			fieldLabel: '任务组',
//		    store: new Ext.data.JsonStore({
//				url : this.listGroupUrl ,
//				root : "result",
//				fields : ['id','name'],
//				autoLoad:true,
//				totalProperty: 'totalCounts'
//			}),
//		    width:200,
//		    displayField:'name',
//		    valueField:'id',
//		    mode: 'local',
//		    emptyText:'请选择任务组',
//		    triggerAction:'all',
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
//		var addBtn = new OECP.ui.Button({
//			columnWidth:0.2,
//			text : "新增	",
//			iconCls : 'btn-add',
//			handler : function(){
//				me.addGroup(me);
//			}
//		});
//		var items = [];
//		if(me.isView)
//			items = [{columnWidth:0.35,xtype:'label',text:'任务组:',style:'font-size:13px;',align:'center'},me.groupCombo]
//		else
//			items = [{columnWidth:0.35,xtype:'label',text:'任务组:',style:'font-size:13px;',align:'center'},me.groupCombo,addBtn];
		this.NoticeFormPanel = new Ext.FormPanel({
	        labelWidth: 100, // label settings here cascade unless overridden
	        frame:true,
	        bodyStyle:'padding:5px 5px 0',
	        width: 350,
	        defaults: {width: 230},
	        defaultType: 'textfield',
	        reader : new Ext.data.JsonReader({
				root : 'result'
			}, [{
				name : 'notice.id',
				mapping : 'id'
			},{
				name : 'notice.createTime',
				mapping : 'createTime'
			},
//			{
//				name : 'notice.createUser',
//				mapping : 'createUser.name'
//			},
			{
				name : 'notice.createcorp',
				mapping : 'createcorp'
			},{
				name : 'notice.title',
				mapping : 'title'
			},{
				name : 'notice.discorp',
				mapping : 'discorp'
			},{
				name : 'notice.isStart',
				mapping : 'isStart'
			},
//			{
//				name : 'notice.author',
//				mapping : 'author.name'
//			},
			{
				name : 'notice.publishDate',
				mapping : 'publishDate'
			},
			{
				name : 'notice.message',
				mapping : 'message'
			}]),
	        items: [{
                	xtype:'hidden',
	                name: 'notice.id'
	            },
	                	{
                	xtype:'hidden',
	                name: 'notice.createTime'
	            },
//	        	{
//                	xtype:'hidden',
//	                name: 'notice.createUser'
//	            },
	        	{
            	xtype:'hidden',
                name: 'notice.isStart'
            },
         	{
            	xtype:'hidden',
                name: 'notice.publishDate'
            },
//         	{
//            	xtype:'hidden',
//                name: 'notice.author'
//            },
	            {
                	xtype:'hidden',
	                name: 'notice.createcorp'
	            },
	                	{
	                fieldLabel: '标题名称',
	                name: 'notice.title',
	                allowBlank:false,
	                width:300
	            },
//	            {
//	                fieldLabel: '是否下级公司可见',
//	                name: 'task.taskitf',
//	                allowBlank:false,
//	                width:300
//	            },
//	            {
//	            	xtype: 'checkbox',
//	            	boxLabel: '是否下级公司可见',
//	                name: 'notice.discorp',
//	                inputValue:OECP.portlet.StaticParam.noticeTypecorp
//	            },
//	            {
//	                fieldLabel: '任务插件方法名称',
//	                name: 'task.methodName',
//	                allowBlank:false,
//	                width:300
//	            },{
//	                fieldLabel: '任务插件方法参数',
//	                name: 'task.methodParams',
//	                width:300
//	            },
//	            {
//	            	xtype:'panel',
//	            	width:300,
//		           	layout:'column',
//		           	items:items
//		            },
		            {
		            xtype: 'textarea',
		            fieldLabel: '公告内容',
		            name:'notice.message',
	            	width:300,
	                height:300
		        }]
	    });
		this.items = [this.NoticeFormPanel]; 
		OECP.portlet.PortalNoticePanel.superclass.initComponent.call(this);
	},
	/**
	 * 获取预警信息form的数据
	 * @return {}
	 */
	getFormValues : function(){
		var result = new Array();
		var title = this.NoticeFormPanel.form.findField('notice.title').getValue();
		var message = this.NoticeFormPanel.form.findField('notice.message').getValue();
		//var methodName = this.BulletinFormPanel.form.findField('task.methodName').getValue();
		//var oecpTaskGroup = this.BulletinFormPanel.form.findField('task.oecpTaskGroup.id').getValue();
		if(Ext.isEmpty(title)){
		 	Ext.Msg.alert('提示','标题名称不能为空');
		 	result[0] = false;
		 	return result;
		}else{
		 	result[0] = true;
		}
		if(Ext.isEmpty(message)){
		 	Ext.Msg.alert('提示','内容不能为空');
		 	result[0] = false;
		 	return result;
		}else{
		 	result[0] = true;
		}
//		if(Ext.isEmpty(methodName)){
//		 	Ext.Msg.alert('提示','任务插件的方法名称不能为空');
//		 	result[0] = false;
//		 	return result;
//		}else{
//		 	result[0] = true;
//		}
//		if(Ext.isEmpty(oecpTaskGroup)){
//		 	Ext.Msg.alert('提示','请选择任务组');
//		 	result[0] = false;
//		 	return result;
//		}else{
//		 	result[0] = true;
//		}
		result[1] = this.NoticeFormPanel.getForm().getValues();
		return result;
	},
	/**
	 * 新增任务组
	 * @param me
	 */
//	addGroup : function(me){
//		var window = new OECP.Task.AddGroupWindow();
//		window.show();
//	}
});