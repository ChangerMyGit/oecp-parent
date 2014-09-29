/**  
 * @include "../../../ext-oecp/ui/Button.js"  
 * @include "../../../ext-oecp/ui/ToftPanel.js"  
 * @include "../../../ext-oecp/ui/FormWindow.js"  
 */
Ext.ns('OECP.BillFlow');

OECP.BillFlow.BizTypePanel = Ext.extend(Ext.Panel ,{
	region	:	'center',
	layout	:	'border',
	/**
	 * 业务类型列表
	 * @cfg {Ext.grid.GridPanel}
	 */
	bizlist		: undefined,
	// 业务类型信息，以及涉及的单据列表。
	bizInfo		: undefined,
	/**
	 * 功能列表
	 * @type Ext.grid.GridPanel
	 */
	funclist	: undefined,
	addBtn  : undefined,	
	editBtn : undefined,	
	delBtn  : undefined,
	addRowBtn  : undefined,	
	editRowBtn : undefined,	
	delRowBtn  : undefined,
	// 业务类型form项目
	bizitems 	: [
					{dataIndex:'org.id',fieldLabel: 'orgid',name:'bizType.org.id',mapping:'org.id',hidden:true,space:'EDITPANEL'},
					{dataIndex:'id',fieldLabel: 'id',name:'bizType.id',mapping:'id',hidden:true,space:'EDITPANEL'},
					{dataIndex:'code',fieldLabel: '业务类型编号',name:'bizType.code',mapping:'code',hidden:false,space:'ALL'},
					{dataIndex:'name',fieldLabel: '业务类型名称',name:'bizType.name',mapping:'name',hidden:false,space:'ALL'},
					{dataIndex:'org.name',fieldLabel: '所属公司',name:'bizType.org.name',mapping:'org.name',hidden:false,readOnly:true,space:'EDITPANEL'},
					{dataIndex:'shared',fieldLabel: '是否共享',name:'bizType.shared',mapping:'shared',xtype :'checkbox',inputName:'bizType.shared',inputValue:true ,defaultValue :false,hidden:false,space:'ALL'},
					{dataIndex:'description',fieldLabel: '描述',name:'bizType.description',mapping:'description',xtype:'textarea',width:230,height:200,hidden:false,space:'ALL'}
				],
	/**
	 * 业务类型窗体
	 * @type OECP.ui.FormWindow
	 */
	// private
	win_BizType : undefined,
	win_Config : undefined,
	
	initComponent : function(){
		OECP.BillFlow.BizTypePanel.superclass.initComponent.call(this);
		this.initUIComponent();
	},
	// private 整体初始化
	initUIComponent : function(){
		THIS = this;
		this.initBizList();
		this.initBizWindow();
		this.win_BizType.on('dataSaved',function(){THIS.reloadBizList(THIS)});
		// 初始化只读面板
		this.initReadBizTypePanl();
		delete this.bizitems;
		this.initFuncList();
		this.initConfigWindow();
		// 中间面板（包含上下两个面板 业务类型详细信息 和 单据配置列表）
		var centerpanel = new Ext.Panel({
			region	:	'center',
			items	:	[this.bizInfo , this.funclist]
		});
		this.add(centerpanel);
	},
	
	// ====================== 初始化各种控件的方法  Begin ====================== //
	/**
	 * 创建一个工具条
	 * @return {Ext.Toolbar}
	 */
	createTbar : function(){
		THIS = this;
		this.createBtn('addBtn',{text : '新增',iconCls : 'btn-add',xtype:'oecpbutton',handler:function(){THIS.onBizAdd(THIS)}});
		this.createBtn('editBtn',{text : '编辑',iconCls : 'btn-edit',xtype:'oecpbutton',disabled:true,handler:function(){THIS.onBizEdit(THIS)}});
		this.createBtn('delBtn',{text : '删除',iconCls : 'btn-delete',xtype:'oecpbutton',disabled:true,handler:function(){THIS.onBizDel(THIS)}});
		
		var btns = [this.addBtn,this.editBtn,this.delBtn];
		return new Ext.Toolbar({items : btns});
	},
	createGridTbar : function(){
		this.createBtn('addRowBtn',{text : '新增',iconCls : 'btn-add',xtype:'oecpbutton',disabled:true,handler:function(){THIS.onRowAdd(THIS)}});
		this.createBtn('editRowBtn',{text : '编辑',iconCls : 'btn-edit',xtype:'oecpbutton',disabled:true,handler:function(){THIS.onRowEdit(THIS)}});
		this.createBtn('delRowBtn',{text : '删除',iconCls : 'btn-delete',xtype:'oecpbutton',disabled:true,handler:function(){THIS.onRowDel(THIS)}});
		
		var rowbtns = [this.addRowBtn,this.editRowBtn,this.delRowBtn];
		return new Ext.Toolbar({items : rowbtns});
	},
	/**
	 * 创建按钮
	 * @param {String} btnname
	 * @param {Object} config
	 */
	createBtn	: function(btnname, config){
		if(!Ext.isDefined(this[btnname])){
			this[btnname] = new OECP.ui.Button(config);
		}
	},
	
	// private 初始化业务类型列表
	initBizList	: function(){
		THIS = this;
		if( !Ext.isDefined(this.bizlist)){
			this.bizlist = new Ext.grid.GridPanel({
				title	: '业务类型列表',
				region	: 'west',
				width	: '200',
				columns : [{header : "编号",dataIndex : "code",width:90},
							{header : "名称",dataIndex : "name",width:100}],
				store	:  new Ext.data.JsonStore({
					url	:	__ctxPath + '/billflowMng/listBizTypes.do',
					storeId : 'id',
					autoLoad : true,
					fields : ['id','code','name','description','shared','org.id','org.name']
				})
			});
		}
		this.bizlist.getSelectionModel().singleSelect = true;// 单选
		this.bizlist.getSelectionModel().on('selectionchange',function(m){
			var data = m.getSelections();
			if(data.length>0){
				data = data[0].data;
				// 改变按钮状态
			}else{
				data = null;
			}
				THIS.bizChanged(data);
		});
		this.add(this.bizlist);
	},
	// private 初始化只读的业务类型面板
	initReadBizTypePanl : function(){
		var bizReadItems = OECP.core.util.clone(this.bizitems);
		
		if( !Ext.isDefined(this.bizInfo)){
			
			var storeField = [];
			var xtpl = ['<tpl for="."><table width=600>'];// 显示模板
			var len = bizReadItems.length;
			for(var i=0 ; i < len ;i++ ){
				var item = bizReadItems[i];
				var itemname = item.mapping == undefined ? item.name : item.mapping;
				
				storeField.push(itemname);
				if(item.space != 'EDITPANEL'){
					var valuetpl = '{' + itemname +'}';
					if(item.xtype=='checkbox'){ // 显示逻辑型值
						valuetpl = '<tpl if="'+itemname+'==true">是</tpl><tpl if="'+itemname+'==false">否</tpl>';
					}
					
					xtpl.push('<tr valign=top><td width=130> '+ item.fieldLabel + ' : </td><td > '+ valuetpl +' </td></tr>');
				}
			}
			xtpl.push('</table></tpl>');
			var jstore = new Ext.data.JsonStore({
				storeId : 'id',
				url	:	__ctxPath + '/billflowMng/loadBizType.do',
			    fields: storeField
			});
			
			var tpl = new Ext.XTemplate(
				xtpl
            );
			var dataview = new Ext.DataView({
		        store: jstore,
		        tpl: tpl,
		        autoHeight:true,
		        width : 200,
		        emptyText: ''
		    });
		    this.bizInfo = new Ext.Panel({
				title	:	'业务类型',
				height	:	200,
				bodyStyle:'padding:10px',
				// 初始化按钮
				items	:	dataview,
				tbar 	: this.createTbar()
			});
			Ext.Panel.prototype.setValues = function(data){
				jstore.load({params:{bizTypeID:data.id}});
//				dataview.refresh();
			}
		}
	},
	// private 初始化功能流程配置列表面板
	initFuncList  : function(){
		// 单据流涉及到的单据（业务功能）配置列表
		var THIS = this;
		if( !Ext.isDefined(this.funclist)){
			var bfconfigGridsm = new Ext.grid.CheckboxSelectionModel();
			this.funclist = new Ext.grid.GridPanel({
				title	: '业务功能列表',
				height	: 500,
				sm 		: bfconfigGridsm,
				tbar 	: this.createGridTbar(),
				columns : [bfconfigGridsm,{header : "功能编号",dataIndex : "function.code"},
							{header : "功能名称",dataIndex : "function.name"},
							{header : "是否可手工制单",dataIndex : "byHand"},
							{header : "描述",dataIndex : "description",width:300},
							{header : "当前单据创建器",dataIndex : "billCreaterFromPre",width:200},
							{header : "后置单据生成器",dataIndex : "billCreaterToNext",width:200},
							{header : "上游单据回写器",dataIndex : "billPreWriteBacker",width:200}
							],
				store	:  new Ext.data.JsonStore({
					url	:	__ctxPath + '/billflowMng/listBFConfigs.do',
					storeId : 'id',
					fields : ['id','function.code','function.name','byHand','description','billCreaterFromPre','billCreaterToNext','billPreWriteBacker']
						})
			});
			bfconfigGridsm.on('selectionchange',function(m){
				// 更新按钮状态
				if(THIS.isConfigSelected(THIS)){
					THIS.delRowBtn.setDisabled(false);
					if(THIS.getConfigSelections(THIS).length == 1){
						THIS.editRowBtn.setDisabled(false);
					}else{
						THIS.editRowBtn.setDisabled(true);
					}
				}else{
					THIS.editRowBtn.setDisabled(true);
					THIS.delRowBtn.setDisabled(true);
				}
			});
		}
	},
	// 初始化业务类型编辑窗体
	initBizWindow : function(){
		// 业务类型信息项
		var bizEditItems = OECP.core.util.clone(this.bizitems);
		// 构造编辑面板信息项，去掉只显示在只读面板上的元素
		for(var i=0 ; i < bizEditItems.length; i++){
			if(bizEditItems[i].space == 'READPANEL'){
				bizEditItems.splice(i,1);
				i --;
			}
		}
		if(this.win_BizType == undefined){
			this.win_BizType = new OECP.ui.FormWindow({
				height		: 200,
				width		: 400,
				formItems	: bizEditItems,
				closeAction : 'hide',
				saveURL : __ctxPath + '/billflowMng/saveBizType.do'
			});
		}
	},
	initConfigWindow : function(){
		if(this.win_Config == undefined){
			this.win_Config = new OECP.BillFlow.BillFlowConfigWindow({
			});
			this.win_Config.on('configAdded',function(){THIS.reloadConfigList(THIS)});
			this.win_Config.on('configChanged',function(){THIS.reloadConfigList(THIS)});
		}
	},
	// ====================== 初始化各种控件的方法  END ====================== //
	// ====================== 通用方法 Begin ================================ //
	getSelectBizType : function(THIS){
		if(THIS.bizlist.getSelectionModel().hasSelection()){
			return this.bizlist.getSelectionModel().getSelections()[0].data;
		}
		return null;
	},
	getConfigSelections : function(THIS){
		if(THIS.isConfigSelected(THIS)){
			return THIS.funclist.getSelectionModel().getSelections();
		}
	},
	isConfigSelected : function(THIS){
		return THIS.funclist.getSelectionModel().hasSelection();
	},
	// private
	updateBizEditBtn : function(vdisabled){
		THIS = this;
		THIS.editBtn.setDisabled(vdisabled);
		THIS.delBtn.setDisabled(vdisabled);
		THIS.addRowBtn.setDisabled(vdisabled);
	},
	reloadBizList : function(THIS){
		var biztype = THIS.getSelectBizType(THIS);
		THIS.bizlist.getStore().reload();
		if(biztype){
			var rowidx = THIS.bizlist.getStore().indexOfId(biztype.id)
			THIS.bizlist.getSelectionModel().selectRow(rowidx,false);
		}
		THIS.bizlist.getSelectionModel().fireEvent('selectionchange', THIS.bizlist.getSelectionModel());
	},
	reloadConfigList : function(THIS){
		this.funclist.getStore().load({params:{bizTypeID:THIS.getSelectBizType(THIS).id}});
	},
	// ====================== 通用方法  END ================================== //
	// ====================== 事件处理 Begin ================================ //
	/**
	 * 业务类型更换
	 * @param {Object} newbiz
	 * @param {Object} oldbiz
	 */
	// private
	bizChanged	: function(newbiz){
//		THIS.currentBizType = newbiz;
		if(newbiz){
			this.bizInfo.setValues(newbiz);
			if(curUserInfo.orgId == newbiz['org.id']){
				this.updateBizEditBtn(false);
			}else{
				this.updateBizEditBtn(true);
			}
			this.funclist.getStore().load({params:{bizTypeID:newbiz.id}});
		}else{
			this.bizInfo.setValues({id:''});
			this.updateBizEditBtn(false);
			this.funclist.getStore().clearData();
		}
	},

	onBizAdd : function(THIS){
		this.initBizWindow();
		THIS.win_BizType.show();
		THIS.win_BizType.setFormData({'org.id':curUserInfo.orgId,'org.name':curUserInfo.orgName});
	},
	onBizEdit : function(THIS){
		this.initBizWindow();
		THIS.win_BizType.show();
		THIS.win_BizType.setFormData(THIS.getSelectBizType(THIS));
	},
	onBizDel : function(THIS){
		THIS.doAjaxRequest(THIS,__ctxPath + '/billflowMng/delBizType.do',{ bizTypeID: THIS.getSelectBizType(THIS).id },
		function(){THIS.bizlist.getStore().reload();});
	},
	onRowAdd : function(THIS){
		this.win_Config.showAdd(THIS.getSelectBizType(THIS).id);
	},
	onRowEdit : function(THIS){
		var configID = THIS.getConfigSelections(THIS)[0].data.id;
		this.win_Config.showEdit(configID);
	},
	onRowDel : function(THIS){
		var ids = [];
		var ss = THIS.getConfigSelections(THIS);
		Ext.each(ss,function(s){
			ids.push(s.data.id);
		},this);
		
		THIS.doAjaxRequest(THIS,__ctxPath + '/billflowMng/delBFConfigs.do',{ configIDs: ids },
		function(){THIS.funclist.getStore().reload();});
	},
	doAjaxRequest : function(THIS,url,params,callback){
		Ext.Ajax.request({
		   	url:  url,
		   	params: params,
		   	success: function(res){
		   		var msg = eval("("+Ext.util.Format.trim(res.responseText)+")");
		   		if(msg.success){
			   		Ext.ux.Toast.msg("信息", msg.msg);
			   		callback.call(THIS,res);
		   		}else{
		   			Ext.Msg.show({title:"错误",msg:msg.msg,buttons:Ext.Msg.OK});
		   		}
		   	},
		   	failure: function(){Ext.Msg.show({title:"错误",msg:msg.result.msg,buttons:Ext.Msg.OK});}
		});
	},
	onDisable : function(){
        this.win_BizType.destroy();
        this.win_Config.destroy();
        OECP.BillFlow.BizTypePanel.superclass.onDisable.call(this);
    }
})