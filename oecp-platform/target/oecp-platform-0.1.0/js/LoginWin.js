Ext.ns("App");
App.LoginWin = function() {
	var loginForm = new Ext.form.FormPanel({
				id : "LoginFormPanel",
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
							id:'loginId',
							name : "loginId",
							fieldLabel : "账      号",
							cls : "text-user",
							allowBlank : false,
							blankText : "账号不能为空"
						}, {
							id:'pwd',
							name : "md5Pwd",
							fieldLabel : "密      码",
							cls : "text-lock",
							allowBlank : false,
							blankText : "密码不能为空",
							inputType : "password"
						}]
			});
	loginForm.items.first().focus(true, 500);
	var doLogin = function() {
		if (loginForm.form.isValid()) {
			Ext.getCmp('pwd').setValue(hex_md5(Ext.getCmp('pwd').getValue()));
			var loginId = Ext.getCmp('loginId').getValue();
			if(!Ext.isEmpty(loginId,false)){//判断录入用户非空
				loginId = loginId.replace(/(^\s*)|(\s*$)/g, "");
				if(!Ext.isEmpty(loginId,false)){
					var _date = new Date().add(Date.DAY,365);//缓存1年
					Ext.util.Cookies.set("oecp_loginId",loginId,_date);
				}
			}
			
			loginForm.form.submit({
						waitTitle : "请稍候",
						waitMsg : "正在登录......",
						url : __ctxPath + "/auth/login.do",
						success : function(h, data) {
							handleLoginResult(data.result);
							h.findField("md5Pwd").setRawValue("");
							h.findField("loginId").focus(true);
						},
						failure : function(h, data) {
							handleLoginResult(data.result);
							h.findField("md5Pwd").setRawValue("");
							h.findField("loginId").focus(true);
						}
					});
		}
	};
	var win = new Ext.Window({
				id : "LoginWin",
				title : "用户登录",
				iconCls : "login-icon",
				bodyStyle : "background-color: white",
				border : true,
				closable : false,
				resizable : false,
				buttonAlign : "center",
				height : 200,
				width : 460,
				layout : {
					type : "vbox",
					align : "stretch"
				},
				keys : {
					key : Ext.EventObject.ENTER,
					fn : doLogin,
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
							items : [loginForm, {
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
							handler : doLogin
						}, {
							text : "重置",
							iconCls : "btn-login-reset",
							handler : function() {
								loginForm.getForm().reset();
							}
						}]
			});
	win.on('show',function(){
		var uid = Ext.util.Cookies.get ("oecp_loginId");
		if(!Ext.isEmpty(uid,false)){
			Ext.getCmp('loginId').setValue(uid);
		}
	})
	return win;
};
function handleLoginResult(result) {
	if (result.success) {
		Ext.getCmp("LoginWin").hide();
		// 判断是否有多个公司，如果有的话，弹出选择框
		if (result.result && result.result.length > 0) {
			var orgCombox=new Ext.form.ComboBox({
						store : new Ext.data.JsonStore({
									data : result.result,
									fields : ['id', 'name']
								}),
						displayField : 'name',
						valueField : 'id',
						typeAhead : true,
						editable : false,
						mode : 'local',
						triggerAction : 'all',
						emptyText : '选择机构进入',
						allowBlank:false,
						blankText:'请选择机构！',
						selectOnFocus : true
					});
			orgCombox.setValue(result.result[0].id);
			var _win = new Ext.Window({
						id : "SelectOrg",
						bodyStyle : "background-color: white",
						border : true,
						closable : false,
						resizable : false,
						buttonAlign : "center",
						height : 70,
						width : 180,
						layout : {
							type : "vbox",
							align : "stretch"
						},
						items : [{
									xtype : "panel",
									border : false,
									layout : "column",
									items : [orgCombox]
								}],
						buttons : [{
									text : "进入系统",
									handler : function(a) {
										var orgid = orgCombox.getValue();
										if(orgid){
											window.location.href = __ctxPath + "/portal/select.jspx?orgid="+orgid;
										}
										
									}
								}]
					});
			_win.show();
			var task = new Ext.util.DelayedTask(function(){
			    _win.buttons[0].focus(true, 500);
			});
			task.delay(500);//延迟500毫秒响应
		} else {
			var b = new Ext.ProgressBar({
						text : "正在登录..."
					});
			b.show();
			window.location.href = __ctxPath + "/index.jspx";
		}
	} else {
		Ext.MessageBox.show({
					title : "操作信息",
					msg : result.msg,
					buttons : Ext.MessageBox.OK,
					icon : Ext.MessageBox.ERROR
				});
	}
}