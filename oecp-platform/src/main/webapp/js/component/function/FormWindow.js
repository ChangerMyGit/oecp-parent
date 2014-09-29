Ext.ns('OECP.fun');

/**
 * @author liujt
 * @class OECP.ui.FormWindow
 * @extends Ext.Window
 */
OECP.fun.FormWindow = Ext.extend(Ext.Window, {
			title : ' ',
			width : 400,
			autoHeight : true,
			closeAction : 'hide',
			plain : true,
			modal : true,
			layout : 'fit',
			formItems : [],
			isEdit : true,
			buttons : [],
			saveURL : '',
			buttonAlign : 'center',
			setFormData : function(formdata) {
				this.formPanel.getForm().setValues(formdata);
			},

			initComponent : function() {
				var formWindow = this;

				formWindow.formPanel = new Ext.form.FormPanel({
							autoHeight : true,
							height : 400,
							width : 300,
							bodyStyle : 'padding:10px',
							defaults: {width: 180},
							defaultType : 'textfield',
							items : formWindow.formItems
						});

				var btn_save = new Ext.Button({
							text : '保存',
							handler : function() {
								if(formWindow.formPanel.getForm().isValid()){
									formWindow.formPanel.getForm().submit({
										url : formWindow.saveURL,
										success : function(form, msg) {
											Ext.ux.Toast.msg("信息", msg.result.msg);
											formWindow.fireEvent('dataSaved', form
															.findField('id').value);
											formWindow.hide();
										},
										failure : function(form, msg) {
											Ext.Msg.show({
														title : "错误",
														msg : msg.result.msg,
														buttons : Ext.Msg.OK
													});
										}
									});
								}
							}
						});
				var btn_close = new Ext.Button({
							text : '关闭',
							handler : function() {
								formWindow.hide();
							}
						});
				formWindow.on('hide', function() {
							formWindow.formPanel.getForm().reset();
						});
				formWindow.addEvents('dataSaved');

//				formWindow.buttons.push(btn_save);
//				formWindow.buttons.push(btn_close);
				formWindow.buttons = [btn_save,btn_close];

				formWindow.items = formWindow.formPanel;

				OECP.fun.FormWindow.superclass.initComponent.call(this);

			}
		});
