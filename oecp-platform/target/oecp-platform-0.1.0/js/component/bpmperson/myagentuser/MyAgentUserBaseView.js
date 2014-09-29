/**  
 * @include "../../../ext-oecp/ui/Button.js"  
 * @include "../../../ext-oecp/ui/ToftPanel.js"  
 * @include "MyAgentUserPanel.js"  
 */
Ext.ns("OECP.MyAgent")
/**
 * 我的代理人管理
 * @class OECP.MyAgent.MyAgentUserBaseView
 * @extends OECP.ui.base.ToftPanel
 */
OECP.MyAgent.MyAgentUserBaseView = Ext.extend(OECP.ui.base.ToftPanel ,{
	/**
	 * 是否是管理员
	 * @type 
	 */
	isAdminUser : this.isAdminUser,
	/**
	 * 添加代理用户按钮
	 * @type 
	 */
	addAgentUser: undefined,
	/**
	 * 修改代理权限按钮
	 * @type 
	 */
	updateAgentUser : undefined,
	/**
	 * 收回代理用户按钮
	 * @type 
	 */
	takeAgentUser : undefined,
	/**
	 * 收回代理用户URL
	 * @type 
	 */
	takeBackAgentUrl : __ctxPath + '/bpm/person/task/takeBackAgent.do',
	/**
	 * 用户Combo
	 * @type 
	 */
	userSearchCombo : undefined,
	/**
	 * 用户combo选择的用户
	 * @type 
	 */
	selectedUserId : undefined,
	/**
	 * 初始化方法
	 */
	initComponent : function(){
		this.initCpanel();
		this.initBtns();
		OECP.MyAgent.MyAgentUserBaseView.superclass.initComponent.call(this);
	},
	/**
	 * 初始化按钮
	 */
	initBtns : function(){
		var me = this;
		if(this.isAdminUser && !Ext.isDefined(this.userSearchCombo)){
			/**
			 * 用户
			 */
			this.userSearchCombo = new OECP.ui.combobox.UserCombo({
				listeners : {
					refselect : function(){
						me.selectedUserId = this.getValue();
						me.cpanel.processDefinitionList.getStore().removeAll();
						me.cpanel.userList.getStore().load({params:{'selectedUser.id':me.selectedUserId}});
					}
					
				}
			});
		}
		this.addAgentUser = new OECP.ui.Button({text:'添加代理用户',iconCls : 'btn-add',handler:function(){me.addAgent(me);}});
		this.updateAgentUser = new OECP.ui.Button({text:'修改代理权限',iconCls : 'btn-edit',handler:function(){me.updateAgent(me);}});
		this.takeAgentUser = new OECP.ui.Button({text:'收回代理用户',iconCls : 'btn-delete',handler:function(){me.takeAgent(me);}});
		if(this.isAdminUser)
			this.btns = [new Ext.Toolbar.TextItem('选择用户：'),this.userSearchCombo,me.addAgentUser,me.updateAgentUser,me.takeAgentUser];
		else
			this.btns = [me.addAgentUser,me.updateAgentUser,me.takeAgentUser];
		
	},
	/**
	 * 初始化主面板
	 */
	initCpanel : function(){
		if(!Ext.isDefined(this.cpanel)){
			this.cpanel = new OECP.MyAgent.MyAgentUserPanel({isAdminUser : this.isAdminUser});
		}
	},
	/**
	 * 添加代理用户
	 */
	addAgent : function(me){
		if(me.isAdminUser && !Ext.isDefined(me.selectedUserId)){
			Ext.Msg.alert("提示", "请先选择用户!");
			return;
		}
		var configWindow = new OECP.MyAgent.MyAgentUserConWindow({cpanel:this.cpanel,selectedUserId:me.selectedUserId});
		configWindow.show();
	},
	/**
	 * 修改代理权限
	 */
	updateAgent : function(me){
		var userRecords = this.cpanel.userList.getSelectionModel().getSelections();
		if (userRecords.length==0) {
			Ext.Msg.alert("提示", "请先选择代理用户!");
			return;
		}
		this.cpanel.userList.selModel.fireEvent('rowselect');
		var configWindow = new OECP.MyAgent.MyAgentUserConWindow({cpanel:this.cpanel,selectedUserId:me.selectedUserId,userRecord:this.cpanel.userRecord,processDefRecords:this.cpanel.processDefRecords});
		configWindow.show();
	},
	/**
	 * 收回代理人
	 */
	takeAgent : function(me){
		var userRecords = this.cpanel.userList.getSelectionModel().getSelections();
		if (userRecords.length==0) {
			Ext.Msg.alert("提示", "请先选择代理用户进行收回!");
			return;
		}
		this.cpanel.userList.selModel.fireEvent('rowselect');
		var myagentuserId = ''; 
		if(Ext.isDefined(this.cpanel.userRecord)){
			myagentuserId = this.cpanel.userRecord.get('id');
		}
    	if(!Ext.isEmpty(myagentuserId)){
    		var preDatas = {};
    		preDatas['myAgenter.id'] = myagentuserId;
    		Ext.Msg.confirm('提示框','您确定要收回代理用户吗?',function(button){
    			if (button == 'yes') {
    				Ext.Msg.wait('正在收回代理用户，请稍候......', '提示');
    				//处理参数数据，传向后台，进行制单处理
					Ext.Ajax.request({
						url : me.takeBackAgentUrl,
						params : preDatas,
						success : function(request) {
							var json = Ext.decode(request.responseText);
							Ext.Msg.hide();
							if (json.success) {
								Ext.ux.Toast.msg('信息',	json.msg);
								me.cpanel.processDefinitionList.getStore().removeAll();
								me.cpanel.userList.getStore().load({params:{'selectedUser.id':me.selectedUserId}});
							} else {
								Ext.Msg.show({title:'错误',msg:json.msg,buttons:Ext.Msg.OK	});
							}
						},
						failure : function(request) {
							var json = Ext.decode(request.responseText);
							Ext.Msg.hide();
							Ext.Msg.show({title:'错误',msg:json.msg,buttons:Ext.Msg.OK});
						}
					});
    			}
    		});
    	}
	}
})