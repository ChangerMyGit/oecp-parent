/**  
 * @include "../../../ext-oecp/ui/Button.js" 
 */
Ext.ns('OECP.MyAgent');
/**
 * 添加或修改我的代理人窗口
 * @class OECP.MyAgent.MyAgentUserConWindow
 * @extends Ext.Window
 */
OECP.MyAgent.MyAgentUserConWindow = Ext.extend(Ext.Window ,{
	/**
	 * 窗口标题
	 */
	title 	: '添加我的代理人',
	/**
	 * body的背景为透明的背景
	 */
	plain 		: true,
	/**
	 * 遮罩窗体后面的内容
	 */
    modal 		: true,
    /**
     * 滚动条
     * @type Boolean
     */
    autoScroll : true,
    /**
     * 布局
     */
    layout 		: 'fit',
     /**
     * 高度
     */
	height	: 500,
	/**
	 * 宽度
	 */
	width	: 800,
	/**
	 * 窗口中按钮位置
	 * @type String
	 */
	buttonAlign : 'center',
	/**
	 * 用户查询form
	 * @type 
	 */
	userSearchForm : undefined,
	/**
	 * 用户列表
	 * @type 
	 */
	usersList : undefined,
	/**
	 * 主面板
	 * @type 
	 */
	mainPanel :　undefined,
	/**
	 * 第一个面板（用户查询form、用户列表）
	 * @type 
	 */
	firstPanel : undefined,
	/**
	 * 第二个面板（流程定义列表）
	 * @type 
	 */
	secondPanel : undefined,
	/**
	 * 我的代理人管理面板
	 * @type 
	 */
	cpanel : undefined,
	/**
	 * 选中的代理用户列表数据
	 * @type 
	 */
	userRecord : undefined,
	/**
	 * 代理人代理的流程定义列表
	 * @type 
	 */
	processDefinitionList : undefined,
	/**
	 * myagentuser ID
	 * @type 
	 */
	myagentuserId : undefined,
	/**
	 * 选中的用户
	 * @type 
	 */
	selectedUserId : this.selectedUserId,
	/**
	 * url
	 * @type 
	 */
	userListUrl : __ctxPath + '/bpm/person/task/queryUserList.do',
	processDefListUrl : __ctxPath + '/bpm/person/task/getMyProcessDef.do',
	saveUrl : __ctxPath + '/bpm/person/task/saveMyAgentUser.do',
	/**
	 * 初始化方法
	 */
	initComponent : function(){
		var scope = this;
		this.cpanel = this.cpanel
		this.userRecord = this.userRecord;
		if(Ext.isDefined(this.userRecord)){
	    	this.myagentuserId = this.userRecord.get('id'); 
		}
		this.processDefinitionList = this.processDefinitionList;
		this.initUserSearchForm(scope);
		this.initUserList(scope);
		this.initFirstPanel(scope);
		this.initSecondPanel(scope);
		this.initMainPanel(scope);
		this.initBtns(scope);
		OECP.MyAgent.MyAgentUserConWindow.superclass.initComponent.call(this);
	},
	/**
	 * 初始化用户列表
	 * @param {} scope
	 */
	initUserSearchForm : function(scope){
		this.userSearchForm = new Ext.FormPanel({
			height : 35,
			frame : true,
			layout : 'column',
			defaults : {
				xtype : 'label'
			},
			items : [{
						text : '用户账号'
					}, {
						xtype : 'hidden',
						name : 'conditions[0].field',
						value : 'loginId'
					}, {
						xtype : 'hidden',
						name : 'conditions[0].operator',
						value : 'like'
					}, {
						width : 80,
						xtype : 'textfield',
						name : 'conditions[0].value'
					}, {
						text : '用户姓名'
					}, {
						xtype : 'hidden',
						name : 'conditions[1].field',
						value : 'name'
					}, {
						xtype : 'hidden',
						name : 'conditions[1].operator',
						value : 'like'
					}, {
						width : 80,
						xtype : 'textfield',
						name : 'conditions[1].value'
					},{
						xtype : 'hidden',
						name : 'myAgenter.id',
						value : scope.myagentuserId
					},{
						xtype : 'hidden',
						name : 'selectedUser.id',
						value : scope.selectedUserId
					}, {
						xtype : 'button',
						text : '查询',
						iconCls : 'search',
						handler : function() {
							var grid = scope.usersList;
							if (scope.userSearchForm.getForm().isValid()) {
								scope.userSearchForm.getForm().submit({
									waitMsg : '正在提交查询',
									url : scope.userListUrl,
									success : function(formPanel, action) {
										var result = Ext.util.JSON
												.decode(action.response.responseText);
										grid.getStore().loadData(result);
									}
								});
							}
						}
					}]
		});
	},
	/**
	 * 
	 * @param {} THIS
	 */
	initUserList : function(scope){
			var sm = new Ext.grid.CheckboxSelectionModel();
			this.usersList = new Ext.grid.GridPanel({
				height	: 365,
				width	: 417,
				autoScroll : true,
				sm : sm,
				columns : [sm,{header : "账号",dataIndex : "loginId",width:90},
							{header : "用户名",dataIndex : "name",width:100},
							{header : "邮箱",dataIndex : "email",width:100},
							{header : "创建时间",dataIndex : "createTime",width:100}],
				store	:  new Ext.data.JsonStore({
					url	:	scope.userListUrl,
					baseParams : {
						'myAgenter.id':scope.myagentuserId,
						'selectedUser.id':scope.selectedUserId
					},
					root : "result",
					storeId : 'id',
					autoLoad : true,
					fields : ['id','loginId','name','email','createTime']
				})
			});
			this.usersList.getSelectionModel().singleSelect = true;// 单选
			if(Ext.isDefined(this.userRecord)){
				this.usersList.store.on('load', function(store, records, options) {   
				        sm.clearSelections();   //清空数据
				        Ext.each(records, function(rec) {
			        		if(rec.get('id')==scope.userRecord.get('agent.id'))
			        			sm.selectRow(store.indexOfId(rec.get('id')),true); 
				        });  
			    },this,{delay:300});
			}
	},
	/**
	 * 初始化第一个面板
	 * @param {} THIS
	 */
	initFirstPanel : function(scope){
		this.firstPanel = new Ext.Panel({
			title	: '选择代理用户',
			region : 'west',
			height : 400,
			width :  420,
			autoScroll	: true,
			layout : 'table',
		    layoutConfig : {
		        columns: 1
		    },
			items : [scope.userSearchForm,scope.usersList]
		});
	},
	/**
	 * 初始化第二面板
	 * @param {} scope
	 */
	initSecondPanel : function(scope){
		var sm = new Ext.grid.CheckboxSelectionModel();
		this.secondPanel = new Ext.grid.GridPanel({
			title	: '选择流程定义',
			region	: 'center',
			autoScroll	: true,
			sm : sm,
			width : 430,
			columns : [sm,{header : "功能名称",dataIndex : "belongFunctionName",width:90},
						{header : "分配组织名称",dataIndex : "assignedOrgName",width:100},
						{header : "流程定义名称",dataIndex : "proDefinitionName",width:100},
						{header : "是否启用",dataIndex : "isUseName",width:100,
							renderer : function(value, metaData, record, rowIndex,colIndex, store){
								var h = "";
								var isUseId = record.data.isUseId;
				                if(isUseId=='0'){
				                	h = "<p style=\"color:red;\">未启用</p>";
				                }else{
				                	h = "<p style=\"color:green;\">已启用</p>";
				                }
				                return h;  
				            }
				          }],
			store	:  new Ext.data.JsonStore({
				url	:	scope.processDefListUrl,
				baseParams : {
					'myAgenter.id':scope.myagentuserId,
					'selectedUser.id':scope.selectedUserId
				},
				root : "result",
				storeId : 'id',
				autoLoad : true,
				fields : ['id','belongFunctionName','assignedOrgName','proDefinitionName','isUseId','isUseName']
			})
		});
		if(Ext.isDefined(this.processDefRecords)){
			this.secondPanel.store.on('load', function(store, records, options) {   
			        sm.clearSelections();   //清空数据
			        Ext.each(records, function(rec) {
			        	Ext.each(scope.processDefRecords,function(rr){
			        		if(rec.get('id')==rr.get('id')){
			        			sm.selectRow(store.indexOfId(rr.get('id')),true); 
			        		}
			        	});
			        });  
			},this,{delay:300});
		}
	},
	/**
	 * 初始化主面板
	 * @param {} scope
	 */
	initMainPanel : function(scope){
		this.mainPanel =  new Ext.Panel({
			layout : 'border',
			autoScroll	: true,
			items : [scope.firstPanel,scope.secondPanel]
		});
		this.items = [this.mainPanel];
	},
	initBtns : function(scope){
		var saveBtn = new OECP.ui.Button({text:'保存',iconCls : 'btn-save',handler:function(){scope.doSave(scope);}});
		var closeBtn = new OECP.ui.Button({text:'关闭',iconCls : 'btn-cancel',handler:function(){scope.close();}});
		this.buttons = [saveBtn,closeBtn];
	},
	/**
	 * 保存
	 */
	doSave : function(scope){
		var userRecords = scope.usersList.getSelectionModel().getSelections();
		var processDefRecords = scope.secondPanel.getSelectionModel().getSelections();
		if (userRecords.length==0) {
			Ext.Msg.alert("提示", "请先选择代理用户!");
			return;
		}
		if (processDefRecords.length==0) {
			Ext.Msg.alert("提示", "请选择流程定义!");
			return;
		}
		var myagentuserId = "";
		if(Ext.isDefined(scope.userRecord)){
			myagentuserId = scope.userRecord.get('id'); 
		}
		//组装参数
		var userId = userRecords[0].get('id');
		var preDatas = {};
		if(!Ext.isEmpty(myagentuserId))
			preDatas['myAgenter.id'] = myagentuserId;
		if(!Ext.isEmpty(scope.selectedUserId))
			preDatas['selectedUser.id'] = scope.selectedUserId;			
		preDatas['myAgenter.agent.id'] = userId;
		for (var i = 0; i < processDefRecords.length; i++) {
			var value = processDefRecords[i].get('id');
			preDatas['myAgenter.virProDefinitions['+i+'].id'] = value;
		}
//		scope.cpanel.showMessageBox('保存');
		Ext.Msg.wait('正在保存，请稍候......', '提示');
		//处理参数数据，传向后台，进行制单处理
		Ext.Ajax.request({
			url : this.saveUrl,
			params : preDatas,
			success : function(request) {
				var json = Ext.decode(request.responseText);
				Ext.Msg.hide();
				if (json.success) {
					Ext.ux.Toast.msg('信息',	json.msg);
					scope.close();
					scope.cpanel.processDefinitionList.getStore().removeAll();
					scope.cpanel.userList.getStore().load({params:{'selectedUser.id':scope.selectedUserId}});
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
	
})