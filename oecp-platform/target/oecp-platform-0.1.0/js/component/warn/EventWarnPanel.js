Ext.ns("OECP.Warn")

/**
 * 事件预警配置面板
 * @class OECP.Warn.EventWarnPanel
 * @extends Ext.Panel
 */
OECP.Warn.EventWarnPanel = Ext.extend(Ext.Panel, {
	id : 'OECP.Warn.EventWarnPanel',
	title : '事件预警',
	layout : 'fit',
	/**
	 * 初始化方法
	 */
	initComponent : function(){
		this.EventWarnFormPanel = new Ext.FormPanel({
		        labelWidth: 75, 
		        frame:true,
		        bodyStyle:'padding:5px 5px 0',
		        width: 350,
		        reader : new Ext.data.JsonReader({
					root : 'result'
				}, [{
					name : 'warn.eventWarn.id',
					mapping : 'eventWarn.id'
				}, {
					name : 'warn.eventWarn.eventSource',
					mapping : 'eventWarn.eventSource'
				}, {
					name : 'warn.eventWarn.event',
					mapping : 'eventWarn.event'
				}]),
		        items: [{
		                	xtype:'hidden',
			                name: 'warn.eventWarn.id'
			            },{
		                	xtype:'textfield',
			                fieldLabel: '事件源',
			                name: 'warn.eventWarn.eventSource',
			                allowBlank:false,
			                width:300
			            },{
		                	xtype:'textfield',
			                fieldLabel: '事件名称',
			                name: 'warn.eventWarn.event',
			                allowBlank:false,
			                width:300
			            }]
		 });
		this.items = [this.EventWarnFormPanel]; 
		OECP.Warn.EventWarnPanel.superclass.initComponent.call(this);
	},
	/**
	 * 获取事件form的数据
	 * @return {}
	 */
	getFormValues : function(){
		var result = new Array();
		var eventSource = this.EventWarnFormPanel.form.findField('warn.eventWarn.eventSource').getValue();
		var event = this.EventWarnFormPanel.form.findField('warn.eventWarn.event').getValue();
		if(Ext.isEmpty(eventSource)){
		 	Ext.Msg.alert('提示','事件源不能为空');
		 	result[0] = false;
			return result;
		 }else{
		 	result[0] = true;
		 }
		 if(Ext.isEmpty(event)){
		 	Ext.Msg.alert('提示','事件不能为空');
		 	result[0] = false;
		 	return result;
		 }else{
		 	result[0] = true;
		 }
		 result[1] = this.EventWarnFormPanel.getForm().getValues();
		 return result;
	}
});