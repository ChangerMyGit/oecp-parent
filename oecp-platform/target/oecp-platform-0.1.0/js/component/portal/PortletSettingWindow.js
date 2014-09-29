/**  
 * @include "Button.js"  
 * @include "../core/Toast.js"  
 */

Ext.ns('OECP.portal');

/**
 * 带有form的window
 * @class OECP.ui.FormWindow
 * @extends Ext.Window
 */
OECP.portal.PortletSettingWindow = Ext.extend(Ext.Window,{
	width : 300,
	height : 280,
	title : "修改设置",
	autoScroll	: true,
    closeAction : 'hide',
    plain 		: true,
    modal 		: true,
    layout 		: 'fit',
	buttonAlign : 'center',
	/**
	 * 设置form数据
	 * @param {Object} formdata
	 */
	setFormData : function(formdata){
		this.formPanel.getForm().setValues(formdata);
	},
	getFormData : function(){
		return this.formPanel.getForm().getValues();
	},
	resetForm : function(){
		this.formPanel.getForm().reset();
	},
	initComponent : function(){
		/**
		 * 数据保存完成
		 */
		this.addEvents('dataSaved');
		
		OECP.portal.PortletSettingWindow.superclass.initComponent.call(this);
		this.initForm();
		this.initButtons();
	},
	
	initForm : function(){
		if(this.formPanel == undefined){
			this.formPanel = new Ext.FormPanel({
				autoScroll	: true,
				defaultType	: 'textfield',
				autoHeight	: true,
				items : [
				         {dataIndex:'title',fieldLabel: '标题',name:'title',maxLength:20,minLength:0,mapping:'title'},
				         {dataIndex:'height',fieldLabel: '高度',name:'height',maxLength:3,minLength:0,mapping:'height',xytpe:'numberfield'},
				         {dataIndex:'hideHeader',fieldLabel: '隐藏标题栏',name:'hideHeader',xtype:'checkbox',inputValue :true,mapping:'hideHeader'},
				         {dataIndex:'hideBorder',fieldLabel: '隐藏边框',name:'hideBorder',xtype:'checkbox',inputValue :true,mapping:'hideBorder'},
				         {dataIndex:'marginTop',fieldLabel: '上边距',name:'marginTop',maxLength:2,minLength:0,wight:10,mapping:'marginTop',xtype:'numberfield',allowNegative:false,defaultValue:5},
				         {dataIndex:'marginBottom',fieldLabel: '下边距',name:'marginBottom',maxLength:2,minLength:0,wight:2,mapping:'marginBottom',xtype:'numberfield',allowNegative:false,defaultValue:5},
				         {dataIndex:'marginLeft',fieldLabel: '左边距',name:'marginLeft',maxLength:2,minLength:0,wight:10,mapping:'marginLeft',xtype:'numberfield',allowNegative:false,defaultValue:5},
				         {dataIndex:'marginRight',fieldLabel: '右边距',name:'marginRight',maxLength:2,minLength:0,wight:10,mapping:'marginRight',xtype:'numberfield',allowNegative:false,defaultValue:5}
				         ]
			});
		}
		this.add(this.formPanel);
	},
	
	initButtons : function(){
		var THIS = this;
		if(!Ext.isDefined(this.saveBtn)){
			this.saveBtn = new OECP.ui.Button({
				iconCls : 'btn-save',
				text : '确定',
				handler : function(){ // 
						THIS.fireEvent('dataSaved',THIS.formPanel.getForm().getValues());
						THIS[THIS.closeAction]();
				}
			});
		}
		if(!Ext.isDefined(this.closeBtn)){
			this.closeBtn = new OECP.ui.Button({
				iconCls : 'btn-cancel',
				text 	: '取消',
				handler	: function(){
					THIS[THIS.closeAction]();
	            }
			});
		}
		this.addButton(this.saveBtn);
		this.addButton(this.closeBtn);
		THIS.on('hide',function(){
			THIS.formPanel.getForm().reset();
		});
	},
    onDestroy : function(){
        Ext.Window.superclass.onDestroy.call(this);
    }
});