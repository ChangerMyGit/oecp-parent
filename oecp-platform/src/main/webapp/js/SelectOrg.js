Ext.ns("App");
App.SelectOrg = function() {
	var a = new Ext.form.FormPanel({
				id : "SelectOrgFormPanel",
				bodyStyle : "padding-top:6px",
				defaultType : "textfield",
				columnWidth : 0.75,
				labelAlign : "right",
				labelWidth : 55,
				labelPad : 0,
				border : false,
				defaults : {
					anchor : "90%,120%",
					selectOnFocus : true
				},
				items : [{
							name : "username",
							fieldLabel : "账      号",
							cls : "text-user",
							allowBlank : false,
							blankText : "账号不能为空"
						}, {
							name : "password",
							fieldLabel : "密      码",
							cls : "text-lock",
							allowBlank : false,
							blankText : "密码不能为空",
							inputType : "password"
						}, new OECP.core.ComboBoxTree({
									fieldLabel : "公      司",
									cls : "text-lock",
									allowBlank:false
								}), {
							xtype : "container",
							style : "padding-left:57px",
							layout : "column",
							items : [{
										xtype : "checkbox",
										name : "remember_me",
										boxLabel : "让系统记住我 "
									}]
						}]
			});

	var e = function() {
		if (a.form.isValid()) {
			a.form.submit({
						waitTitle : "请稍候",
						waitMsg : "正在登录......",
						url : __ctxPath + "/login.do",
						success : function(h, i) {
							handleLoginResult(i.result);
						},
						failure : function(h, i) {
							handleLoginResult(i.result);
							h.findField("password").setRawValue("");
							h.findField("username").focus(true);
						}
					});
		}
	};
	var c = new Ext.Window({
				id : "LoginWin",
				title : "用户登录",
				iconCls : "login-icon",
				bodyStyle : "background-color: white",
				border : true,
				closable : false,
				resizable : false,
				buttonAlign : "center",
				height : 275,
				width : 460,
				layout : {
					type : "vbox",
					align : "stretch"
				},
				keys : {
					key : Ext.EventObject.ENTER,
					fn : e,
					scope : this
				},
				items : [{
							xtype : "panel",
							border : false,
							bodyStyle : "padding-left:60px",
							html : '<img src="' + __loginImage + '" />',
							height : 50
						}, {
							xtype : "panel",
							border : false,
							layout : "column",
							items : [a, {
								xtype : "panel",
								border : false,
								columnWidth : 0.2,
								html : '<img src="' + __ctxPath
										+ '/images/login_user.jpg"/>'
							}]
						}],
				buttons : [{
							text : "登录",
							iconCls : "btn-login",
							handler : e
						}, {
							text : "重置",
							iconCls : "btn-login-reset",
							handler : function() {
								a.getForm().reset();
							}
						}]
			});
	return c;
};
function handleLoginResult(a) {
	if (a.success) {
		Ext.getCmp("LoginWin").hide();
		var b = new Ext.ProgressBar({
					text : "正在登录..."
				});
		b.show();
		window.location.href = __ctxPath + "/index.jsp";
	} else {
		Ext.MessageBox.show({
					title : "操作信息",
					msg : a.msg,
					buttons : Ext.MessageBox.OK,
					icon : Ext.MessageBox.ERROR
				});
	}
}