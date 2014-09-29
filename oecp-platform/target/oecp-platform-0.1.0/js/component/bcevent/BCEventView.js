/**  
 * @include "../../../ext-oecp/core/Toast.js"  
 * @include "../../../ext-oecp/ui/Button.js"  
 * @include "../../../ext-oecp/ui/ToftPanel.js"  
 * @include "EventPanel.js"  
 * @include "ListenerPanel.js"  
 */
Ext.ns('OECP.bcevent');

/**
 * 业务事件信息维护主界面
 * @class OECP.ui.comp.BCEventView
 * @extends OECP.ui.base.ToftPanel
 */
OECP.bcevent.BCEventView = Ext.extend(OECP.ui.base.ToftPanel,{
	id:'BCEventManager',
	title:'事件管理',
	initComponent : function(){
		this.initUIComponent();
		OECP.bcevent.BCEventView.superclass.initComponent.call(this);
	},
	initUIComponent:function(){
		this.initCpanel();
	},
	// 初始化中心面板
	initCpanel : function(){
		if(this.cpanel == undefined){
			this.cpanel = new Ext.Panel({layout:'border'});
			this.initEventPanel();
			this.initListenerPanel();
			// 将事件和监听器面板 添加至中心面板
			this.cpanel.add(this.eventPanel,this.listenerPanel);
		}
	},
	/**
	 * @cfg {Ext.grid.GridPanel} eventPanel 
	 */
	// 初始化事件信息面板
	initEventPanel : function(){
		if(this.eventPanel == undefined){
			this.eventPanel = new OECP.bcevent.EventPanel({region:'north'});
		}
	},
	/**
	 * @cfg {Ext.grid.GridPanel} listenerPanel 
	 */
	// 初始化监听器信息面板
	initListenerPanel : function(){
		var THIS = this;
		if(this.listenerPanel == undefined){
			this.listenerPanel = new OECP.bcevent.ListenerPanel({region:'center'});
			this.eventPanel.on('eventChanged',function(data,scope){
				if(data.length == 1){
					THIS.listenerPanel.setEventID(data[0].data.id);
				}else{
					THIS.listenerPanel.setEventID();
				}
			});
		}
	}
});