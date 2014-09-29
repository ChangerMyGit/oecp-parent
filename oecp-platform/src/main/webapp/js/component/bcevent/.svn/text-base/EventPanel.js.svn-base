/**  
 * @include "../../../ext-oecp/core/Toast.js"  
 * @include "../../../ext-oecp/ui/Button.js"  
 * @include "../../../ext-oecp/ui/FormWindow.js"  
 * @include "BCEventView.js"  
 */
Ext.ns('OECP.bcevent');

/**
 * 业务事件信息维护主界面
 * @class OECP.bcevent.EventPanel
 * @extends Ext.grid.GridPanel
 */
OECP.bcevent.EventPanel = Ext.extend(Ext.grid.GridPanel,{
	height : 260,
	title : '业务事件信息',
	
	initComponent : function(){
		this.addEvents('eventChanged');
		this.initUIComponent();
		OECP.bcevent.EventPanel.superclass.initComponent.call(this);
	},
	initUIComponent : function(){
		var THIS = this;
		this.store = new Ext.data.JsonStore({
			storeId : 'id',
			url : __ctxPath + '/event/list.do',
			root : 'result',
			totalProperty : 'totalCounts',
			autoLoad :true,
			autoDestroy : true,
			fields:[ 
				{name : "id",type : "string"}, 
				{name : "code",type : "string"}, 
				{name : "name",type : "string"}, 
				{name : "bc.name",type : "string"}
			]
		});
		sm = new Ext.grid.CheckboxSelectionModel({singleSelect:true});
		this.sm = sm;
		
		this.columns = [
			sm,
			{header: "组件名称", dataIndex: 'bc.name',width:150,sortable:true},
	        {header: "事件代号", dataIndex: 'code',width:200,sortable:true},
	        {header: "事件名称", dataIndex: 'name',width:260,sortable:true}
		];
		
		sm.on('selectionchange',function(m){
			THIS.fireEvent('eventChanged',m.getSelections(),THIS);
		});
		this.initTBar();
		this.initAddWindow();
		this.initEditWindow();
		this.initEventGetWindow();
	},
	/**
	 * @type {OECP.ui.Button} updateBCEventBtn
	 */
	/**
	 * @type {OECP.ui.Button} addBtn
	 */
	/**
	 * @type {OECP.ui.Button} editBtn
	 */
	/**
	 * @type {OECP.ui.Button} delBtn
	 */
	initTBar : function(){
		var THIS = this;
		this.updateBCEventBtn = new OECP.ui.Button({text : '获取组件事件',iconCls : 'btn-add',xtype:'oecpbutton',handler:function(){THIS.onUpdateBCEvent(THIS)}}),
		this.addBtn = new OECP.ui.Button({text : '注册事件',iconCls : 'btn-add',xtype:'oecpbutton',handler:function(){THIS.onAdd(THIS)}}),
		this.editBtn = new OECP.ui.Button({text : '编辑',iconCls : 'btn-edit',xtype:'oecpbutton',disabled:true,handler:function(){THIS.onEdit(THIS)}}),
		this.delBtn = new OECP.ui.Button({text : '删除',iconCls : 'btn-delete',xtype:'oecpbutton',disabled:true,handler:function(){THIS.onDel(THIS)}}),
		this.tbar = new Ext.Toolbar({items:[
			this.addBtn,this.editBtn,this.delBtn,this.updateBCEventBtn
		]});
		// 改变按钮状态
		this.sm.on('selectionchange',function(m){
			var data = m.getSelections();
			if(data.length == 1){
				THIS.editBtn.setDisabled(false);
				THIS.delBtn.setDisabled(false);
			}else{
				THIS.editBtn.setDisabled(true);
				THIS.delBtn.setDisabled(true);
			}
		});
	},
	/**
	 * @cfg {OECP.ui.FormWindow} addWindow
	 */
	initAddWindow : function(){
		if(this.addWindow == undefined){
			this.addWindow = new OECP.ui.FormWindow({
				title : '注册新事件',
				width : 800,
				height : 500,
				closeAction	: 'hide',
				saveURL : __ctxPath + '/event/regist.do',
				formItems : [
				{dataIndex:'bc.id',fieldLabel : "所属组件",name:'eventInfo.bc.id',xtype:'bccombo'},
				{dataIndex:'code',fieldLabel : "事件代号",name:'eventInfo.code'},
				{dataIndex:'name',fieldLabel : "事件名称",name:'eventInfo.name'},
				{dataIndex:'description',fieldLabel : "描述",name:'eventInfo.description',xtype:'textarea',width:300,height:140},
				{dataIndex:'structinfo',fieldLabel : "数据源描述",name:'eventInfo.structinfo',xtype:'textarea',width:600,height:400}
				]
			});
			var scope = this;
			this.addWindow.on('dataSaved' ,function(){scope.store.reload();});
		}
	},
	onAdd : function(scope){
		scope.addWindow.resetForm();
		scope.addWindow.show();
	},
	
	/**
	 * @cfg {OECP.ui.FormWindow} editWindow
	 */
	initEditWindow : function(){
		if(this.editWindow == undefined){
			this.editWindow = new OECP.ui.FormWindow({
				title : '修改事件信息',
				width : 800,
				height : 500,
				closeAction	: 'hide',
				saveURL : __ctxPath + '/event/update.do',
				formItems : [
				{dataIndex:'id',fieldLabel : "id",name:'eventInfo.id',hidden:true},
				{dataIndex:'bc.id',fieldLabel : "所属组件",name:'eventInfo.bc.id',xtype:'bccombo',hiddenName:'eventInfo.bc.id'},
				{dataIndex:'code',fieldLabel : "事件代号",name:'eventInfo.code'},
				{dataIndex:'name',fieldLabel : "事件名称",name:'eventInfo.name'},
				{dataIndex:'description',fieldLabel : "描述",name:'eventInfo.description',xtype:'textarea',width:300,height:140},
				{dataIndex:'structinfo',fieldLabel : "数据源描述",name:'eventInfo.structinfo',xtype:'textarea',width:600,height:400}
				]
			});
			var scope = this;
			this.editWindow.on('dataSaved' ,function(){scope.store.reload();});
		}
	},
	/**
	 * @cfg {OECP.ui.FormWindow} eventGetWindow
	 */
	initEventGetWindow : function(){
		if(this.eventGetWindow == undefined){
			this.eventGetWindow = new OECP.ui.FormWindow({
				title : '从组件更新事件信息',
				width : 300,
				height : 100, 
				closeAction	: 'hide',
				saveURL : __ctxPath + '/event/updateFromBC.do',
				formItems : [
				{fieldLabel : "请选择组件",name:'bcID',xtype:'bccombo',hiddenName:'bcID'}
				]
			});
			this.eventGetWindow.saveBtn.text = "更新数据";
			var scope = this;
			this.eventGetWindow.on('dataSaved' ,function(){scope.store.reload();});
		}
	},
	onEdit : function(scope){
		scope.loadEventInfo(scope.getCurrentEvent().id,function(eventinfo){
			scope.editWindow.resetForm();
			scope.editWindow.setFormData(eventinfo);
			scope.editWindow.show();
		},scope);
	},
	onDel : function(scope){
		Ext.Ajax.request({
		   	url:  __ctxPath + '/event/remove.do',
		   	params: {eventID : scope.getCurrentEvent().id},
		   	success: function(res){
		   		var msg = eval("("+Ext.util.Format.trim(res.responseText)+")");
		   		if(msg.success){
			   		Ext.ux.Toast.msg("信息", msg.msg);
			   		scope.store.reload();
		   		}else{
		   			Ext.Msg.show({title:"错误",msg:msg.msg,buttons:Ext.Msg.OK});
		   		}
		   	},
		   	failure: function(){Ext.Msg.show({title:"错误",msg:'提交失败，您的网络可能不通畅，请稍后再试。',buttons:Ext.Msg.OK});}
		});
	},
	
	onUpdateBCEvent : function(scope){
		scope.eventGetWindow.show();
	},
	
	/**
	 * 获得当前选中的事件信息对象
	 * @return {Object} 事件信息对象
	 */
	getCurrentEvent : function(){
		var data = this.getSelectionModel().getSelections();
		var event = null;
		if(data.length > 0){
			event = data[0].data;
		}
		return event;
	},
	loadEventInfo : function(id,callback,scope){
		if(scope.eventInfoStore == undefined){
			scope.eventInfoStore = new Ext.data.JsonStore({
				storeId : 'id',
				url : __ctxPath + '/event/eventInfo.do',
				autoDestroy : true,
				fields:[ 
					{name : "id",type : "string"}, 
					{name : "code",type : "string"}, 
					{name : "name",type : "string"}, 
					{name : "description",type : "string"}, 
					{name : "structinfo",type : "string"}, 
					{name : "bc.id",type : "string"}
				]
			});
		}
		
		scope.eventInfoStore.load({params: {eventID : id},callback : function(data,options,success){
			if(data.length == 1){
		   		callback.call(scope,data[0].data);
			}else{
	   			Ext.Msg.show({title:"错误",msg:'加载数据失败，可能数据已经被他人修改或删除。',buttons:Ext.Msg.OK});
	   		}
		}});
	},
	
	
	onDisable : function(){
        this.addWindow.destroy();
        this.editWindow.destroy();
        this.eventGetWindow.destroy();
        this.eventInfoStore.destroy();
        OECP.bcevent.EventPanel.superclass.onDisable.call(this);
    }
});