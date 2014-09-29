/**  
 * @include "../../../ext-oecp/ui/Button.js" 
 */
Ext.ns('OECP.BillFlow');
/**
 * 单据在单据流上的配置信息维护窗口。
 * @class OECP.BillFlow.BillFlowConfigWindow
 * @extends Ext.Window
 */
OECP.BillFlow.BillFlowConfigWindow = Ext.extend(Ext.Window ,{
	/**
	 * 窗口标题
	 */
	title 	: '流程单据配置',
	/**
	 * body的背景为透明的背景
	 */
	plain 		: true,
	/**
	 * 关闭模式
	 */
	closeAction	: 'hide',
	/**
	 * 遮罩窗体后面的内容
	 */
    modal 		: true,
    /**
     * 布局
     */
    layout 		: 'fit',
    /**
     * 高度
     */
	height	: 600,
	/**
	 * 宽度
	 */
	width	: 700,
	/**
	 * 配置信息表单项目
	 * @cfg {Array} 
	 */
	configFormField : ['id','function.id','bizType.id','function.code','function.name','byHand','byBussiness',"qlType","daobeanname","preQuerySQL",'description','billCreaterFromPre','billCreaterToNext','billPreWriteBacker','billPreRollBackWriter','queryDialog','billCreaterFromPreCheck','preBillFunction','nextBillFunction'],
	/**
	 * 配置信息panel
	 * @type Ext.FormPanel
	 */
	bfConfigPanel : undefined,
	/**
	 * 前置查询字段信息panel
	 * @type Ext.grid.EditorGridPanel
	 */
	fieldPanel : undefined,
	/**
	 * 流程单据配置信息
	 */
	mainPanel : undefined,
	/**
	 * 按钮：上一步
	 */
	btnPre : undefined,
	/**
	 * 按钮：下一步
	 */
	btnNext: undefined,
	/**
	 * 按钮：保存
	 */
	btnSave: undefined,
	/**
	 * 按钮：关闭
	 */
	btnClose:undefined,
	
	/**
	 * 给表单赋值
	 */
	setFormData : function(formdata){
		this.bfConfigPanel.getForm().setValues(formdata);
	},
	/**
	 * 当前功能
	 */
	funComboBox:undefined,
	/**
	 * 上游功能菜单
	 */
	preBillFunComboBox:undefined,
	/**
	 * 下游功能菜单
	 */
	nextBillFunComboBox:undefined,
	/**
	 * 初始化方法
	 */
	initComponent : function(){
		OECP.BillFlow.BillFlowConfigWindow.superclass.initComponent.call(this);
		this.addEvents('configAdded','configChanged');
		this.initMainPanel();
		this.initBFConfigPanel();
		this.initFieldPanel();
		this.initBtns();
	},
	/**
	 * 初始化主面板
	 */
	initMainPanel : function(){
		if(!Ext.isDefined(this.mainPanel)){
			this.mainPanel = new Ext.Panel({
				layout : 'card',
				autoScroll	: true,
				layoutConfig: {
					deferredRender : false
				}
			});
		}
		this.add(this.mainPanel);
	},
	/**
	 * @type Ext.data.JsonStore
	 */
	bfConfigStore : undefined,
	/**
	 * 初始化面板中信息
	 */
	initBFConfigPanel : function(){
		var combstore = new Ext.data.ArrayStore({
	        fields: ['key', 'tip'],
	        data : [['HQL','Hibernate查询，可支持生成字段名。']
	        	,['SQL','标准SQL查询，咱不支持根据查询语句生成字段名！']]
	    });
	    var comboWithTooltip = new Ext.form.ComboBox({
	        tpl: '<tpl for="."><div ext:qtip="{tip}" class="x-combo-list-item">{key}</div></tpl>',
	        store: combstore,
	        displayField:'key',
	        valueField:'key',
	        editable : false,
	        typeAhead: true,
	        forceSelection: true,
	        emptyText:'请选择查询类型',
	        mode: 'local',
	        selectOnFocus:true,
	        triggerAction: 'all',
	        width : 120,
	        dataIndex:'qlType',fieldLabel : "查询语句类型",name:'bfConfig.qlType',mapping : "qlType"
	    });
	    var scope = this;
	    //功能菜单
	    scope.funComboBox = new OECP.core.FunComboBox({
			rootVisible : true,
			fieldLabel : "功能菜单",
			
			nodeClick : function(node) {
				if (node.id != null && node.id != '') {
					if (node.id == 'none' || node.isLeaf()) {
						scope.funComboBox.setValueAndText(node.id, node.text);
						scope.funComboBox.menu.hide();
						var functionid = node.id.substring(4);
						scope.bfConfigPanel.form.findField('function.id').setValue(functionid)
					} else {
						Ext.Msg.alert("提示", "此节点无效，请重新选择!")
					}
				}
			}
		});
	    //上游单据功能菜单
	    scope.preBillFunComboBox = new OECP.core.FunComboBox({
			rootVisible : true,
			fieldLabel : "上游单据功能菜单",
			editable :true,
			nodeClick : function(node) {
				if (node.id != null && node.id != '') {
					if (node.id == 'none' || node.isLeaf()) {
						scope.preBillFunComboBox.setValueAndText(node.id, node.text);
						scope.preBillFunComboBox.menu.hide();
						var functionid = node.id.substring(4);
						scope.bfConfigPanel.form.findField('preBillFunction.id').setValue(functionid)
					} else {
						Ext.Msg.alert("提示", "此节点无效，请重新选择!")
					}
				}
			},
			listeners:{change : function(){
				if(this.value=='')
					scope.bfConfigPanel.form.findField('preBillFunction.id').setValue('');
			}}
		});
	    //下游单据功能菜单
	    scope.nextBillFunComboBox = new OECP.core.FunComboBox({
			rootVisible : true,
			fieldLabel : "下游单据功能菜单",
			editable :true,
			nodeClick : function(node) {
				if (node.id != null && node.id != '') {
					if (node.id == 'none' || node.isLeaf()) {
						scope.nextBillFunComboBox.setValueAndText(node.id, node.text);
						scope.nextBillFunComboBox.menu.hide();
						var functionid = node.id.substring(4);
						scope.bfConfigPanel.form.findField('nextBillFunction.id').setValue(functionid)
					} else {
						Ext.Msg.alert("提示", "此节点无效，请重新选择!")
					}
				}
			},
			listeners:{change : function(){
				if(this.value=='')
					scope.bfConfigPanel.form.findField('nextBillFunction.id').setValue('');
			}}
		});
	    
		if(!Ext.isDefined(this.bfConfigPanel)){
			this.bfConfigPanel = new Ext.FormPanel({
				autoScroll	: true,
				defaultType : 'textfield',
				items	:  [
			{dataIndex:'id',fieldLabel : "id",name:'bfConfig.id',mapping : "id",hidden:true},
			scope.funComboBox,
			{dataIndex:'bizType.id',fieldLabel : "业务类型id",name:'bfConfig.bizType.id',mapping : "bizType.id",hidden:true},
			{dataIndex:'function.id',fieldLabel : "功能id",name:'bfConfig.function.id',mapping : "function.id",hidden:true},
//			{dataIndex:'function.code',fieldLabel : "功能编号",name:'bfConfig.function.code',mapping : "function.code",hidden:true},
//			{dataIndex:'function.name',fieldLabel : "功能名称",name:'bfConfig.function.name',mapping : "function.name",hidden:true},
			{dataIndex:'byHand',fieldLabel : "是否可手工制单",name:'bfConfig.byHand',inputName:'',mapping : "byHand",xtype :'checkbox',inputValue:true ,defaultValue :false},
			{dataIndex:'byBussiness',fieldLabel : "是否可业务制单",name:'bfConfig.byBussiness',inputName:'',mapping : "byBussiness",xtype :'checkbox',inputValue:true ,defaultValue :false,listeners:{check :function(){scope.selectCheck(this.getValue(),scope);}}},
			{dataIndex:'description',fieldLabel : "描述",name:'bfConfig.description',mapping : "description",width:300},
			{dataIndex:'billCreaterFromPre',fieldLabel : "当前单据创建器",name:'bfConfig.billCreaterFromPre',mapping : "billCreaterFromPre",width:300},
			{dataIndex:'billCreaterFromPreCheck',fieldLabel : "当前单据保存校验",name:'bfConfig.billCreaterFromPreCheck',mapping : "billCreaterFromPreCheck",width:300},
			{dataIndex:'billCreaterToNext',fieldLabel : "后置单据生成器",name:'bfConfig.billCreaterToNext',mapping : "billCreaterToNext",width:300},
			{dataIndex:'billPreWriteBacker',fieldLabel : "上游单据回写器",name:'bfConfig.billPreWriteBacker',mapping : "billPreWriteBacker",width:300},
			scope.preBillFunComboBox,
			{dataIndex:'preBillFunction.id',fieldLabel : "上游单据",name:'bfConfig.preBillFunction.id',mapping : "preBillFunction.id",width:300,hidden:true},
			scope.nextBillFunComboBox,
			{dataIndex:'nextBillFunction.id',fieldLabel : "下游单据",name:'bfConfig.nextBillFunction.id',mapping : "nextBillFunction.id",width:300,hidden:true},
//			{dataIndex:'billPreRollBackWriter',fieldLabel : "上游单据回滚器",name:'bfConfig.billPreRollBackWriter',mapping : "billPreRollBackWriter",width:300},
			comboWithTooltip,
			{width:90,xtype:'combo',dataIndex:'daobeanname',fieldLabel : "查询DAO",name:'bfConfig.daobeanname',mapping : "daobeanname",
					store:{xtype:'jsonstore',root:'result',url:__ctxPath+'/report/setting/daonames.do',fields:['name'],autoLoad:true},
				triggerAction:'all',mode: 'local',valueField:'name',displayField:'name',hiddenName:'bfConfig.daobeanname'},
//			{dataIndex:'queryDialog',fieldLabel : "查询对话框Ext类",name:'bfConfig.queryDialog',mapping : "queryDialog",xtype:'textarea',width:400,height:200}
//			,
			{dataIndex:'preQuerySQL',fieldLabel : "查询语句",name:'bfConfig.preQuerySQL',mapping : "preQuerySQL",xtype:'textarea',width:500,height:260}
			]});
		}
		this.mainPanel.add(this.bfConfigPanel);
		if(!Ext.isDefined(this.bfConfigStore)){
			this.bfConfigStore = new Ext.data.JsonStore({
				url	:	__ctxPath + '/billflowMng/loadBFConfig.do',
				storeId : 'id',
				fields : this.configFormField});
			//加载完之后，把那个功能菜单下拉选框赋值
			this.bfConfigStore.on('load', function(store, records, options){
				var functionid;
				var functionname;
				var preBillfunctionid;
				var preBillfunctionname;
				var nextBillfunctionid;
				var nextBillfunctionname;
				Ext.each(records, function(rec) {
					 functionid = rec.get('function.id');
					 functionname = rec.get('function.name');
					 scope.funComboBox.setValueAndText('fun_'+functionid, functionname);
					 scope.funComboBox.menu.hide();
					 var preBillFunction = rec.get('preBillFunction');
					 if(preBillFunction!=null){
						 preBillfunctionid = preBillFunction["id"];
						 preBillfunctionname =  preBillFunction["name"];
						 scope.preBillFunComboBox.setValueAndText('fun_'+preBillfunctionid, preBillfunctionname);
						 scope.preBillFunComboBox.menu.hide();
						 scope.bfConfigPanel.form.findField('preBillFunction.id').setValue(preBillfunctionid)
					 }
					 var nextBillFunction = rec.get('nextBillFunction')
					 if(nextBillFunction!=null){
						 nextBillfunctionid = nextBillFunction['id'];
						 nextBillfunctionname = nextBillFunction['name'];
						 scope.nextBillFunComboBox.setValueAndText('fun_'+nextBillfunctionid, nextBillfunctionname);
						 scope.nextBillFunComboBox.menu.hide();
						 scope.bfConfigPanel.form.findField('nextBillFunction.id').setValue(nextBillfunctionid)
					 }
			    });
			});
		}
		
	},
	/**
	 * 没选择业务制单时，这些都不可用
	 */
	initBFConfigPanelCheck : function(scope){
		scope.bfConfigPanel.form.findField("bfConfig.billCreaterFromPre").disable();
		scope.bfConfigPanel.form.findField("bfConfig.billCreaterFromPreCheck").disable();
		scope.bfConfigPanel.form.findField("bfConfig.billPreWriteBacker").disable();
//		scope.bfConfigPanel.form.findField("bfConfig.billPreRollBackWriter").disable();
		scope.bfConfigPanel.form.findField("bfConfig.qlType").disable();
//		scope.bfConfigPanel.form.findField("bfConfig.queryDialog").disable();
		scope.bfConfigPanel.form.findField("bfConfig.preQuerySQL").disable();
		scope.bfConfigPanel.form.findField("bfConfig.preBillFunction.id").disable();
		scope.bfConfigPanel.form.findField("bfConfig.nextBillFunction.id").disable();
		scope.preBillFunComboBox.disable();
		scope.nextBillFunComboBox.disable();
	},
	/**
	 * 如果选择业务制单时的限制
	 */
	selectCheck : function(selectedValue,scope){
		if(!selectedValue){
			scope.bfConfigPanel.form.findField("bfConfig.billCreaterFromPre").disable();
			scope.bfConfigPanel.form.findField("bfConfig.billCreaterFromPreCheck").disable();
			scope.bfConfigPanel.form.findField("bfConfig.billPreWriteBacker").disable();
//			scope.bfConfigPanel.form.findField("bfConfig.billPreRollBackWriter").disable();
			scope.bfConfigPanel.form.findField("bfConfig.qlType").disable();
			//scope.bfConfigPanel.form.findField("bfConfig.qlType").setValue('');
//			scope.bfConfigPanel.form.findField("bfConfig.queryDialog").disable();
			scope.bfConfigPanel.form.findField("bfConfig.daobeanname").disable();
			scope.bfConfigPanel.form.findField("bfConfig.preQuerySQL").disable();
			scope.bfConfigPanel.form.findField("bfConfig.preBillFunction.id").disable();
			scope.bfConfigPanel.form.findField("bfConfig.nextBillFunction.id").disable();
			scope.preBillFunComboBox.disable();
			scope.nextBillFunComboBox.disable();
		}else{
			scope.bfConfigPanel.form.findField("bfConfig.billCreaterFromPre").enable();
			scope.bfConfigPanel.form.findField("bfConfig.billCreaterFromPreCheck").enable();
			scope.bfConfigPanel.form.findField("bfConfig.billPreWriteBacker").enable();
//			scope.bfConfigPanel.form.findField("bfConfig.billPreRollBackWriter").enable();
			scope.bfConfigPanel.form.findField("bfConfig.qlType").enable();
			//scope.bfConfigPanel.form.findField("bfConfig.qlType").setValue('HQL');
//			scope.bfConfigPanel.form.findField("bfConfig.queryDialog").enable();
			scope.bfConfigPanel.form.findField("bfConfig.daobeanname").enable();
			scope.bfConfigPanel.form.findField("bfConfig.preQuerySQL").enable();
			scope.bfConfigPanel.form.findField("bfConfig.preBillFunction.id").enable();
			scope.bfConfigPanel.form.findField("bfConfig.nextBillFunction.id").enable();
			scope.preBillFunComboBox.enable();
			scope.nextBillFunComboBox.enable();
		}
	},
	/**
	 * 初始化前置单据信息
	 */
	initFieldPanel : function(){
		if(!Ext.isDefined(this.fieldPanel)){
			var THIS = this,
			 	sm = new Ext.grid.CheckboxSelectionModel();
				PreDataFiled = Ext.data.Record.create([{name: 'id', type: 'string'},
			{name: 'name',type: 'string'}, 
		    {name: 'dispName',type: 'string'},
		    {name: 'dataType',type: 'string' },
		    {name: 'maxlength',type: 'int' },
		    {name: 'uiClass',type: 'string'},
		    {name: 'supplement',type: 'string'},
		    {name: 'editable', type: 'boolean'},
		    {name: 'hidden',type: 'boolean'}]);
			var store = new Ext.data.JsonStore({
					url	:	__ctxPath + '/billflowMng/listConfigFields.do',
					storeId : 'id',
					fields : PreDataFiled
						});
						
			var btn_addrow = new OECP.ui.Button({text : '增行',iconCls : 'btn-edit',disabled:false,handler:function(){
				THIS.fieldPanel.stopEditing();
				var newData = new PreDataFiled();
				store.add(newData);
				THIS.fieldPanel.stopEditing();
			}});
			var btn_delrow = new OECP.ui.Button({text : '删行',iconCls : 'btn-del',disabled:true,handler:function(){
				THIS.fieldPanel.stopEditing();
				store = THIS.fieldPanel.getStore();
				var rows = THIS.fieldPanel.getSelectionModel().getSelections();
				if(!THIS.fieldPanel.getSelectionModel().hasSelection()){
					return;
				}
				store.remove(rows);
				THIS.fieldPanel.stopEditing();
			}});
			var tbar = new Ext.Toolbar({items : [btn_addrow,btn_delrow]});
			var combstore = new Ext.data.ArrayStore({
	        fields: ['key', 'tip'],
	        data : [['STRING','字符串']
	        	,['INTEGER','整形']
	        	,['LONG','长整形']
	        	,['DOUBLE','小数']
	        	,['ENUM','枚举，补充信息请填写枚举对应的java类']
	        	,['DATE','日期类型，对应java.sql.Date']
	        	,['DATETIME','时间类型，对应java.util.Date']
	        	]
		    });
		    var comboWithTooltip = new Ext.form.ComboBox({
		        tpl: '<tpl for="."><div ext:qtip="{tip}" class="x-combo-list-item">{key}</div></tpl>',
		        store: combstore,
		        displayField:'key',
		        valueField:'key',
		        editable : false,
		        typeAhead: true,
		        forceSelection: true,
		        emptyText:'请选择查询类型',
		        mode: 'local',
		        selectOnFocus:true,
		        triggerAction: 'all',
		        width : 120,
		        dataIndex:'qlType',fieldLabel : "查询语句类型",name:'bfConfig.qlType',mapping : "qlType"
		    });
			
			this.fieldPanel = new Ext.grid.EditorGridPanel({
				title : '前置单据数据项',
				tbar : tbar,
				autoScroll	: true,
				sm : sm,
				store :store,
				clicksToEdit : 1,
				columns : [sm,{header : "列名",dataIndex : "name"},
							{header : "显示名",dataIndex : "dispName",editor :{xtype : "textfield",selectOnFocus:true}},
							{header : "数据类型",dataIndex : "dataType",editor :comboWithTooltip},
//							{header : "最小长度",dataIndex : "maxlength",editor :{xtype : "textfield"}},
//							{header : "Ext编辑控件名",dataIndex : "uiClass",editor :{xtype : "textfield"}},
//							{header : "补充信息",dataIndex : "supplement",editor :{xtype : "textfield"}},
//							{header : "是否可编辑",dataIndex : "editable",xtype : "checkcolumn"},
							{header : "是否隐藏",dataIndex : "hidden",xtype : "checkcolumn"}
							]
			});
			this.fieldPanel.getSelectionModel().on('selectionchange',function(m){
				if(m.hasSelection()){
					btn_delrow.setDisabled(false);
				}else{
					btn_delrow.setDisabled(true);
				}
			});
		}
		
		
		this.mainPanel.add(this.fieldPanel);
	},
	/**
	 * 初始化按钮
	 */
	initBtns : function(){
		var THIS = this;
		this.btnPre = new OECP.ui.Button({text:'<-上一步',disabled:true,handler:function(){THIS.showPrePanel(THIS)}});
		this.btnSave= new OECP.ui.Button({text:'保存',iconCls : 'btn-save',handler:function(){THIS.onSave(THIS)}});
		this.btnClose= new OECP.ui.Button({text:'关闭',iconCls : 'btn-cancel',handler:function(){THIS[THIS.closeAction]()}});
		this.btnNext= new OECP.ui.Button({text:'下一步->',handler:function(){THIS.showNextPanel(THIS)}});
		this.addButton([THIS.btnPre,THIS.btnSave,THIS.btnClose,THIS.btnNext]);
	},
	/**
	 * 获得流程单据配置的id
	 */
	getConfigID : function(){
		return this.bfConfigPanel.getForm().getValues()['bfConfig.id'];
	},
	showPanelIndex : 0,
	/**
	 * 展现上一步按钮
	 */
	showPrePanel : function(THIS)
	{ 
		THIS.mainPanel.getLayout().setActiveItem(0); 
		THIS.showPanelIndex = 0;
		this.btnPre.setDisabled(true);
		this.btnNext.setDisabled(false);
	},
	/**
	 * 展现下一步按钮
	 */
	showNextPanel : function(THIS)
	{ 
		this.mainPanel.getLayout().setActiveItem(1); 
		this.showPanelIndex = 1;
		this.btnPre.setDisabled(false);
		this.btnNext.setDisabled(true);
		
		if(this.fieldPanel.getStore().data.length < 1){
			this.fieldPanel.getStore().load({params:{configID:this.getConfigID()}});
		}
	},
	/**
	 * 保存方法
	 */
	onSave : function(THIS){
		if(THIS.showPanelIndex == 0){
			THIS.saveConfig(THIS);
		}else{
			THIS.saveFields(THIS);
		}
	},
	/**
	 * 配置信息保存方法
	 */
	saveConfig : function(THIS){
		this.bfConfigPanel.getForm().submit({
					method : "POST",
					url	: __ctxPath + '/billflowMng/saveBFConfig.do',
					waitMsg : "正在保存数据...",
					success : function(form , msg){
						Ext.ux.Toast.msg("信息", msg.result.msg);
						if(THIS.isConfigEdit){
							THIS.fireEvent('configChanged',msg.result.configID);
						}else{
							THIS.bfConfigPanel.getForm().setValues({id:msg.result.configID});
							THIS.isConfigEdit = true,
							THIS.btnNext.setDisabled(false);
							THIS.fireEvent('configAdded',msg.result.configID);
						}
						THIS[THIS.closeAction]();
					},
					failure : function(form , msg){
						Ext.Msg.show({title:"错误",msg:msg.result.msg,buttons:Ext.Msg.OK});
					}
				});
	},
	/**
	 * 前置单据字段信息保存
	 */
	saveFields : function(THIS){
		var url	= __ctxPath + '/billflowMng/saveBFPreFields.do';
		var gridData = this.fieldPanel.getStore().data.items;
		var params = {configID : this.getConfigID()};
		var idx = 0;
		for(var idx = 0 ; idx < gridData.length ; idx++){
			var objname = 'fields[' + idx + '].';
			var row = gridData[idx];
			for(var para in row.data){
				params[objname+para] = row.data[para];
			}
		}
		this.doAjaxRequest(this,url,params,function(){THIS[THIS.closeAction]();});
	},
	show : function(args){
		OECP.BillFlow.BillFlowConfigWindow.superclass.show.call(this,args);
		this.showPrePanel(this);
	},
	isConfigEdit : false,
	/**
	 * 增加页面
	 */
	showAdd : function(bizTypeID){
		this.initBFConfigPanelCheck(this);
		this.setFormData(this.createNewConfig(bizTypeID));
		this.funComboBox.setValueAndText('','');//清除功能选项
		this.preBillFunComboBox.setValueAndText('','');
		this.nextBillFunComboBox.setValueAndText('','');
		this.fieldPanel.getStore().clearData();
		this.isConfigEdit = false;
		this.show();
		this.btnNext.setDisabled(true);
	},
	/**
	 * 编辑页面
	 */
	showEdit : function(configID){
		this.preBillFunComboBox.setValueAndText('','');
		this.nextBillFunComboBox.setValueAndText('','');
		var THIS = this;
		this.bfConfigStore.load({params:{configID:configID},callback:function(record,success){
			if(success){
				var data = record[0].data;
				THIS.setFormData(data);
				THIS.isConfigEdit = true;
				THIS.show();
				THIS.fieldPanel.getStore().clearData();
				THIS.btnNext.setDisabled(false);
			}
		}});
	},
	// private
	createNewConfig : function(bizTypeID){
		var newconfig = {};
		Ext.each(this.configFormField,function(f){
			newconfig[f] = '';
		},this);
		newconfig['bizType.id'] = bizTypeID;
		return newconfig;
	},
	onDisable : function(){
        delete this.bfConfigStore;
        OECP.BillFlow.BillFlowConfigWindow.superclass.onDisable.call(this);
    },
    /**
     * ajax提交
     * @param {OECP.BillFlow.BillFlowConfigWindow} THIS
     * @param {String} url
     * @param {Array} params
     * @param {function} callback
     */
    doAjaxRequest : function(THIS,url,params,callback){
		Ext.Ajax.request({
			method : "POST",
		   	url:  url,
		   	params: params,
		   	success: function(res){
		   		var msg = eval("("+Ext.util.Format.trim(res.responseText)+")");
		   		if(msg.success){
			   		Ext.ux.Toast.msg("信息", msg.msg);
			   		callback.call(THIS,res);
		   		}else{
		   			Ext.Msg.show({title:"错误",msg:msg.msg,buttons:Ext.Msg.OK})
		   		}
		   	},
		   	failure: function(){Ext.Msg.show({title:"错误",msg:msg.result.msg,buttons:Ext.Msg.OK})}
		});
	}
    
})