Ext.ns('OECP.ui.comp');

/**
 * Form面板
 * @author slx
 * @class OECP.ui.comp.OECPFormPanel
 * @extends Ext.FormPanel
 */
OECP.ui.comp.OECPFormPanel = Ext.extend(Ext.FormPanel,{
	fileUpload : true,
	autoHeight : true,
	isEdit : true,
	height : 400,
	width : 300,
	bodyStyle:'padding:10px',
	defaultType : 'textfield',
	
	initComponent : function(){
		var OECPFormPanel = this;
		
		this.addEvents('stateChange');
		// 如果是查看状态，所有控件设置为只读。
		if(!OECPFormPanel.isEdit){
			var items_len = OECPFormPanel.items.length;
			for(var i=0 ; i < items_len; i++){
				OECPFormPanel.items[i].prototype.readOnly = true;
			}
		}
		
		OECP.ui.comp.OECPFormPanel.superclass.initComponent.call(this);
		
		this.fireEvent('stateChange');
	},
	
	setFormState : function(state){
		this.formState = state;
		this.fireEvent('stateChange');
	},
	getFormState : function(){return this.formState}
	
});

Ext.reg('oecpFormPanel', OECP.ui.comp.OECPFormPanel);

