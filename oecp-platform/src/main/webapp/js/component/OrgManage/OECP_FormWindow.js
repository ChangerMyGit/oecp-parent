Ext.ns('OECP.ui.comp');

/**
 * Form弹出窗口
 * 
 * @author slx
 * @class OECP.ui.comp.OECPFormWindow
 * @extends Ext.Window
 */
OECP.ui.comp.OECPFormWindow = Ext.extend(Ext.Window, {
			title : '编辑',
			width : 400,
			autoHeight : true,
			closeAction : 'hide',
			plain : true,
			modal : true,
			layout : 'fit',
			formItems : [],
			isEdit : true,
			buttons : [],
			// resizable : false,
			saveURL : '',
			buttonAlign : 'center',
			setFormData : function(formdata) {
				this.formPanel.getForm().setValues(formdata);
				if(formdata){
					this.formPanel.doLayout();
				}		
			},

			initComponent : function() {
				var OECPFormWindow = this;

				this.formPanel = new OECP.ui.comp.OECPFormPanel({
							isEdit : OECPFormWindow.isEdit,
							items : OECPFormWindow.formItems
						});

				/** 根据字段name构建reader */
				// var readerRecord =
				// Ext.data.Record.create(OECPFormWindow.formItems);
				// var formReader = new Ext.data.JsonReader({
				// idProperty: "id"
				// },readerRecord);
				// formPanel.getForm().reader = formReader;
				var btn_save = new Ext.Button({
							id : 'winbtn_save',
							iconCls : 'btn-save',
							text : '保存',
							handler : function() {
								OECPFormWindow.formPanel.getForm().submit({
									url : OECPFormWindow.saveURL,
									success : function(form, msg) {
										Ext.ux.Toast.msg("信息", msg.result.msg);
										OECPFormWindow.fireEvent('dataSaved',
												form.findField('id').value);
										OECPFormWindow.hide();
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
						});
				var btn_close = new Ext.Button({
							id : 'winbtn_close',
							iconCls : 'btn-cancel',
							text : '关闭',
							handler : function() {
								OECPFormWindow.hide();
							}
						});
				OECPFormWindow.on('hide', function() {
							OECPFormWindow.formPanel.getForm().reset();
						});
				OECPFormWindow.addEvents('dataSaved');

				if (OECPFormWindow.isEdit) {
					OECPFormWindow.buttons.push(btn_save);
				}
				OECPFormWindow.buttons.push(btn_close);

				OECPFormWindow.items = OECPFormWindow.formPanel;

				OECP.ui.comp.OECPFormWindow.superclass.initComponent.call(this);

			},
			onDestroy : function() {
			}
		});
