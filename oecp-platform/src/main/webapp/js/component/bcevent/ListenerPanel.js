/**  
 * @include "../../../ext-oecp/core/Toast.js"  
 * @include "../../../ext-oecp/ui/Button.js"  
 * @include "../../../ext-oecp/ui/FormWindow.js"  
 * @include "BCEventView.js"  
 */
Ext.ns('OECP.bcevent');

/**
 * 业务事件监听器面板
 * @class OECP.bcevent.ListenerPanel
 * @extends Ext.grid.GridPanel
 */
OECP.bcevent.ListenerPanel = Ext.extend(Ext.grid.GridPanel,{
	height : 260,
	title : '监听器信息',
	
	initComponent : function(){
		this.initUIComponent();
		OECP.bcevent.ListenerPanel.superclass.initComponent.call(this);
	},
	initUIComponent : function(){
		this.store = new Ext.data.JsonStore({
			storeId : 'id',
			url : __ctxPath + '/event/listeners.do',
			root : 'result',
			totalProperty : 'totalCounts',
			autoDestroy : true,
			fields:[ 
				{name : "id",type : "string"}, 
				{name : "name",type : "string"}, 
				{name : "beanname",type : "string"}, 
				{name : "classname",type : "string"}
			]
		});
		sm = new Ext.grid.CheckboxSelectionModel({singleSelect:true});
		this.sm = sm;
		
		this.columns = [
			sm,
			{header: "监听器名称", dataIndex: 'name',width:150},
	        {header: "SpringBean名称", dataIndex: 'beanname',width:200},
	        {header: "类名", dataIndex: 'classname',width:300}
		];
		this.initTBar();
		this.initAddWindow();
		this.initEditWindow();
	},
	
	setEventID : function(eventid){
		this.eventID = eventid
		
		if(this.eventID){
			this.getStore().reload({params:{eventID : this.eventID}});
			this.addBtn.setDisabled(false);
		}else{
			this.getStore().clearData();
			this.addBtn.setDisabled(true);
		}
	},
	
	/**
	 * 获得当前选中的监听器对象
	 * @return {Object} 事件信息对象
	 */
	getCurrentListener : function(){
		var data = this.getSelectionModel().getSelections();
		var listener = null;
		if(data.length > 0){
			listener = data[0].data;
		}
		return listener;
	},
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
		this.addBtn = new OECP.ui.Button({text : '添加监听',iconCls : 'btn-add',xtype:'oecpbutton',disabled:true,handler:function(){THIS.onAdd(THIS)}}),
		this.editBtn = new OECP.ui.Button({text : '编辑',iconCls : 'btn-edit',xtype:'oecpbutton',disabled:true,handler:function(){THIS.onEdit(THIS)}}),
		this.delBtn = new OECP.ui.Button({text : '删除',iconCls : 'btn-delete',xtype:'oecpbutton',disabled:true,handler:function(){THIS.onDel(THIS)}}),
		this.tbar = new Ext.Toolbar({items:[
			this.addBtn,this.editBtn,this.delBtn
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
				title : '添加监听',
				width : 500,
				autoHeight:true,
				closeAction	: 'hide',
				saveURL : __ctxPath + '/event/registListener.do',
				formItems : [
				{dataIndex:'event.id',fieldLabel : "事件",name:'lsinfo.event.id',xtype:'bceventcombo',readOnly:true,hiddenName:'lsinfo.event.id'},
				{dataIndex:'name',fieldLabel : "监听器名称",name:'lsinfo.name'},
				{dataIndex:'beanname',fieldLabel : "SpringBean名称",name:'lsinfo.beanname'},
				{dataIndex:'classname',fieldLabel : "类名",name:'lsinfo.classname'},
				{dataIndex:'idx',fieldLabel : "排序号",name:'lsinfo.idx'}
				]
			});
			var scope = this;
			this.addWindow.on('dataSaved' ,function(){scope.store.reload();});
		}
	},
	onAdd : function(scope){
		scope.addWindow.resetForm();
		scope.addWindow.setFormData({'event.id':scope.eventID});
		scope.addWindow.show();
	},
	
	/**
	 * @cfg {OECP.ui.FormWindow} editWindow
	 */
	initEditWindow : function(){
		if(this.editWindow == undefined){
			this.editWindow = new OECP.ui.FormWindow({
				title : '修改监听',
				width : 500,
				autoHeight:true,
				closeAction	: 'hide',
				saveURL : __ctxPath + '/event/updateListener.do',
				formItems : [
				{dataIndex:'id',fieldLabel : "id",name:'lsinfo.id',hidden:true},
				{dataIndex:'event.id',fieldLabel : "事件",name:'lsinfo.event.id',xtype:'bceventcombo',readOnly:true,hiddenName:'lsinfo.event.id'},
				{dataIndex:'name',fieldLabel : "监听器名称",name:'lsinfo.name'},
				{dataIndex:'beanname',fieldLabel : "SpringBean名称",name:'lsinfo.beanname'},
				{dataIndex:'classname',fieldLabel : "类名",name:'lsinfo.classname'},
				{dataIndex:'idx',fieldLabel : "排序号",name:'lsinfo.idx'}
				]
			});
			var scope = this;
			this.editWindow.on('dataSaved' ,function(){scope.store.reload();});
		}
	},
	onEdit : function(scope){
		scope.loadListenerInfo(scope.getCurrentListener().id,function(lsinfo){
			scope.editWindow.resetForm();
			scope.editWindow.setFormData(lsinfo);
			scope.editWindow.show();
		},scope);
	},
	onDel : function(scope){
		Ext.Ajax.request({
		   	url:  __ctxPath + '/event/romoveListener.do',
		   	params: {lsID : scope.getCurrentListener().id},
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
	loadListenerInfo : function(id,callback,scope){
		if(scope.listenerInfoStore == undefined){
			scope.listenerInfoStore = new Ext.data.JsonStore({
				storeId : 'id',
				url : __ctxPath + '/event/listenerInfo.do',
				autoDestroy : true,
				fields:[ 
					{name : "id",type : "string"}, 
					{name : "name",type : "string"}, 
					{name : "beanname",type : "string"}, 
					{name : "classname",type : "string"}, 
					{name : "idx",type : "number"}, 
					{name : "event.id",type : "string"}
				]
			});
		}
		
		scope.listenerInfoStore.load({params: {lsID : id},callback : function(data,options,success){
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
        this.listenerInfoStore.destroy();
        OECP.bcevent.ListenerPanel.superclass.onDisable.call(this);
    }
});