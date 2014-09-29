/**  
 * @include "Button.js"  
 * @include "../core/Toast.js"  
 */

Ext.ns('OECP.ui');

/**
 * 带有form的window
 * @class OECP.ui.FormWindow
 * @extends Ext.Window
 */
OECP.ui.FormWindow = Ext.extend(Ext.Window,{
//    autoHeight 	: true,
	autoScroll	: true,
    closeAction : 'close',
    plain 		: true,
    modal 		: true,
    layout 		: 'fit',
    /**
     * form元素
     * @cfg {Array} 
     */
	formItems 	: [],
	/**
	 * 中间的formpanel
	 * @type Ext.FormPanel
	 */
	formPanel	: undefined,
	/**
	 * 是否启用默认按钮（保存、关闭）
	 * @cfg {Boolean}
	 */
	useDefaultBtns	: true,
	/**
	 * 保存操作form提交的路径
	 * @cfg {String} saveURL
	 */
	saveURL		: undefined,
	buttonAlign : 'center',
	/**
	 * 设置form数据
	 * @param {Object} formdata
	 */
	setFormData : function(formdata){
		this.formPanel.getForm().setValues(formdata);
	},
	/**
	 * reset表单
	 */
	resetForm : function(){
		this.formPanel.getForm().reset();
	},
	initComponent : function(){
		/**
		 * 数据保存完成
		 */
		this.addEvents('dataSaved');
		
		OECP.ui.FormWindow.superclass.initComponent.call(this);
		this.initForm();
		this.initButtons();
	},
	
	initForm : function(){
		if(this.formPanel == undefined){
			this.formPanel = new Ext.FormPanel({
				autoScroll	: true,
				defaultType	: 'textfield',
				autoHeight	: true,
				items : this.formItems
			});
		}
		this.add(this.formPanel);
	},
	
	initButtons : function(){
		var THIS = this;
		if(this.useDefaultBtns){
			if(!Ext.isDefined(this.saveBtn)){
				this.saveBtn = new OECP.ui.Button({
					iconCls : 'btn-save',
					text : '保存',
					handler : function(){ // 保存提交form到保存URL
						THIS.formPanel.getForm().submit({
							url:THIS.saveURL,
							success : function(form , msg){
								Ext.ux.Toast.msg("信息", msg.result.msg);
								THIS.fireEvent('dataSaved',form.getValues());
								THIS[THIS.closeAction]();
							},
							failure : function(form , msg){
								Ext.Msg.show({title:"错误",msg:msg.result.msg,buttons:Ext.Msg.OK});
							}
						});
					}
				});
			}
			if(!Ext.isDefined(this.closeBtn)){
				this.closeBtn = new OECP.ui.Button({
					iconCls : 'btn-cancel',
					text 	: '关闭',
					handler	: function(){
						THIS[THIS.closeAction]();
		            }
				});
			}
			this.addButton(this.saveBtn);
			this.addButton(this.closeBtn);
		}
		THIS.on('hide',function(){
			THIS.formPanel.getForm().reset();
		});
	},
	/**
	 * 保存按钮
	 * @type OECP.ui.Button
	 */
	saveBtn		: undefined,
	/**
	 * 取消按钮
	 * @type OECP.ui.Button
	 */
	closeBtn	: undefined,
	
    onDestroy : function(){
        Ext.Window.superclass.onDestroy.call(this);
    }
});


Ext.reg('oecpFormWindow', OECP.ui.FormWindow);