/**  
 * @include "../../../ext-oecp/ui/Button.js"  
 * @include "../../../ext-oecp/ui/ToftPanel.js"  
 * @include "../../../ext-oecp/ui/FormWindow.js"  
 */
Ext.ns("OECP.MyAgent")
/**
 * 我的代理人管理面板
 * @class OECP.MyAgent.MyAgentUserPanel
 * @extends Ext.Panel
 */
OECP.MyAgent.MyAgentUserPanel = Ext.extend(Ext.Panel ,{
	region	:	'center',
	layout	:	'border',
	/**
	 * 是否是管理员
	 * @type 
	 */
	isAdminUser : this.isAdminUser,
	/**
	 * 代理用户列表
	 * @type 
	 */
	userList : undefined,
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
	 * 代理人代理的流程定义列表数据
	 * @type 
	 */
	processDefRecords : undefined,
	
	/**
	 * 初始化方法
	 */
	initComponent : function(){
		var THIS = this;
		OECP.MyAgent.MyAgentUserPanel.superclass.initComponent.call(this);
		this.initUserList(THIS);
		this.initProcessDefinitionList(THIS);
	},
	/**
	 * 初始化代理用户列表
	 */
	initUserList : function(THIS){
		var sm = new Ext.grid.CheckboxSelectionModel();
		if(!Ext.isDefined(THIS.userList)){
			THIS.userList = new Ext.grid.GridPanel({
				title	: '我的代理用户列表',
				region	: 'west',
				width	: '450',
				sm : sm,
				columns : [sm,{dataIndex : "id",hidden:true},
							{dataIndex : "agent.id",hidden:true},
							{header : "账号",dataIndex : "agent.loginId",width:90},
							{header : "用户名",dataIndex : "agent.name",width:100},
							{header : "邮箱",dataIndex : "agent.email",width:100},
							{header : "创建时间",dataIndex : "agent.createTime",width:100}],
				store	:  new Ext.data.JsonStore({
					url	:	__ctxPath + '/bpm/person/task/getMyAgentUser.do',
					root : "result",
					storeId : 'id',
					fields : ['id','agent.id','agent.loginId','agent.name','agent.email','agent.createTime']
				})
			});
			THIS.userList.getSelectionModel().singleSelect = true;// 单选
			THIS.userList.store.on('load',function(){
				if(THIS.userList.store.getCount()>0){
					THIS.userList.selModel.selectRow(0,true);
					THIS.userList.selModel.fireEvent('rowselect');
				}
			});
			THIS.userList.getSelectionModel().on('rowselect',function(){
				var data = THIS.userList.getSelectionModel().getSelected();
		        THIS.userRecord = data;
				THIS.processDefChanged(THIS,data);
			});
			THIS.add(THIS.userList);
			if(!THIS.isAdminUser)
				THIS.userList.store.load();
		}
	},
	/**
	 * 初始化代理人代理的流程定义列表
	 */
	initProcessDefinitionList : function(THIS){
		if(!Ext.isDefined(THIS.processDefinitionList)){
			THIS.processDefinitionList = new Ext.grid.GridPanel({
				title	: '流程定义列表',
				region	: 'center',
				width	: '200',
				columns : [{header : "功能名称",dataIndex : "belongFunctionName",width:90},
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
					url	:	__ctxPath + '/bpm/person/task/getMyAgentUserProcessDef.do',
					root : "result",
					storeId : 'id',
					fields : ['id','belongFunctionName','assignedOrgName','proDefinitionName','isUseId','isUseName']
				})
			});
			THIS.processDefinitionList.store.on('load', function(store, records, options) {  
		       THIS.processDefRecords = records;
	        });
			THIS.add(THIS.processDefinitionList);
		}
	},
	/**
	 * 选中代理用户，加载流程定义
	 */
	processDefChanged : function(THIS,data){
		var myAgentId = data.get('id');
		THIS.processDefinitionList.getStore().load({params:{'myAgenter.id':myAgentId}});
	},
	// 显示进度条   
	showMessageBox : function(msg){
        Ext.MessageBox.show({   
            msg : msg+'中，请稍后...',   
            width : 300,   
            wait : true,   
            progress : true,   
            closable : true,   
            waitConfig : {   
                interval : 200  
            },   
            icon : Ext.Msg.INFO   
        });
	}
})