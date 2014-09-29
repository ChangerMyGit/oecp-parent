Ext.ns("OECP.user")

/**
 *  修改密码窗口
 * 
 * 
 * */
OECP.user.UpdatePassWordWin = function() {
	
	var userform = new Ext.FormPanel({
				layout:'table',
			    layoutConfig: {
			        // 这里指定总列数The total column count must be specified here
			        columns: 2
			    },
		        frame:false,
		        height:250,
		        buttonAlign : "center",
		        defaults : {
					xtype : 'label'
				},
		        items: [ new Ext.form.FieldSet({
			                height:240,
			                width:500,
			                defaultType: 'textfield',
			                items: [{
				                        fieldLabel: '请输入旧密码',
				                        name: 'oldPassWord',
				                        inputType: 'password' ,
							            allowBlank:false,
							            maxLength:10,
				                        width:220
				                    }, {
				                        fieldLabel: '请输入新密码',
				                        name: 'newPassWord1',
				                        inputType: 'password' ,
							            allowBlank:false,
							            maxLength:10,
				                        width:220
				                    }, {
				                        fieldLabel: '请重新输入新密码',
				                        name: 'newPassWord2',
				                        inputType: 'password' ,
							            allowBlank:false,
							            maxLength:10,
				                        width:220
				                    }
				                ]
				            })
		        ],
		        buttons: [{
		            text: '保存',
		            handler : function(){
		            	var oldPassWord = userform.form.findField('oldPassWord').getValue();
			            var newPassWord1 = userform.form.findField('newPassWord1').getValue();
			            var newPassWord2 = userform.form.findField('newPassWord2').getValue();
			            if(oldPassWord==''){
			            	Ext.Msg.alert('提示','旧密码不能为空');
			            	return;
			            }
			            if(newPassWord1==''||newPassWord2==''){
			            	Ext.Msg.alert('提示','新密码不能为空');
			            	return;
			            }
			            if(newPassWord1!=newPassWord2){
			            	Ext.Msg.alert('提示','新密码两次输入不一样，请重新输入');
			            	userform.form.findField('newPassWord1').setValue("");
			            	userform.form.findField('newPassWord2').setValue("");
			            	return;
			            }
			            Ext.Ajax.request({
							url : __ctxPath
									+ "/user/updatePWD.do",
							method : "POST",
							params : {
								oldPassWord : oldPassWord,
								newPassWord1 : newPassWord1,
								newPassWord2 : newPassWord2
							},
							success : function(d, g) {
								Ext.Msg.alert('提示','修改成功',function(){
									win.close();
								});
							},
							failure: function(flag){
								Ext.Msg.alert('提示',flag.responseText,function(){});
							}
						});
		            }
		        },{
		            text: '关闭',
		            handler : function(){
		            	win.close();
		            }
		        }]
		    });


	var win = new Ext.Window({
				id : "LoginWin",
				title :'修改密码',
				iconCls : "login-icon",
				bodyStyle : "background-color: white",
				border : true,
				closable : true,
				resizable : true,
				modal : true,
				height : 280,
				width : 500,
				items : [userform]
			});
	return win;
};