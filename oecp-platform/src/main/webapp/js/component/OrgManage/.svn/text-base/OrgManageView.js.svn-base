Ext.ns('OECP.org');

	OECP.org.OrgManagerPanel = Ext.extend(OECP.ui.comp.TreeFormView ,{
		id :'OECP.org.OrgManagerPanel',
		title : '组织管理',
		btnHeight : 0,
		// 树
		treeRootText:'所有组织',
		treePanelTitle : '组织机构目录',
//		treeDataURL : __ctxPath + '/org/tree.do',
		treeDataURL : __ctxPath + '/app/org/ownerTree.do',
//		treeClick : niyeyede,
		// 表单
		formDataURL : __ctxPath + '/org/orgInfo.do',
		formSaveURL : __ctxPath + '/org/save.do',
		formDeleteURL : __ctxPath + '/org/delete.do',
		bcsDataURL : __ctxPath + '/org/showBCs.do',
		parentIDField : 'parent.id',
		treeNodeIcon : __ctxPath + '/images/menus/personal/holiday.png',
		formItems : [
			{dataIndex:'id',fieldLabel: 'id',name:'org.id',mapping:'id',hidden:true,type:'string'},
			{dataIndex:'code',fieldLabel: '编码',name:'org.code',mapping:'code',allowBlank:false,maxLength:10,minLength:3},
			{dataIndex:'name',fieldLabel: '名称',name:'org.name',mapping:'name',allowBlank:false,maxLength:20,minLength:2},
//			{fieldLabel: '上级组织',name:'org.parent',mapping:'parent',hidden:true},
			{dataIndex:'parent.id',fieldLabel: '上级组织',name:'org.parent.id',mapping:'parent.id',hidden:true},
			{dataIndex:'parent.name',fieldLabel: '上级组织',name:'org.parent.name',mapping:'parent.name',hidden:false,disable:true,readOnly:true},
			{dataIndex:'lock',fieldLabel: '封存',inputName:'org.lock',name:'org.lock',mapping:'lock',xtype:'checkbox',inputValue:true ,defaultValue :false},
			{dataIndex:'idx',fieldLabel: '排序',name:'org.idx',mapping:'idx',hidden:true}/*,
			{dataIndex:'organizationConfig.id',fieldLabel: '配置id',name:'org.organizationConfig.id',mapping:'organizationConfig.id',hidden:true},
			{dataIndex:'organizationConfig.logoUrl',fieldLabel: 'logo路径',name:'org.organizationConfig.logoUrl',mapping:'organizationConfig.logoUrl',hidden:true},
			{
							xtype : 'textfield',
							fieldLabel : 'logo图片',
							name : 'upload',
							inputType : 'file',
							// allowBlank : false,
							blankText : 'logo图片不能为空',
							height : 25,
							width : 190
						}*/
		],
		
		
		initComponent : function(){
			orgManagerPanel = this;
			// 按钮
			this.formBtns=[{
				id : 'btn_BuildOrgBc',
				text:'公司建账',
				disabled:true,
				handler:function(){
						jstore.load({params:{id:orgManagerPanel.getSelectedId()}});
						bcswin.show();
					}
				}],
			
			this.on('treeNodeSelected' ,function(){
				var orgid = this.getSelectedId();
				if(orgid == null){
					this.getButtonById('btn_BuildOrgBc').setDisabled(true);
				}else{
					this.getButtonById('btn_BuildOrgBc').setDisabled(false);
				}
			});
			
			// 组件启用窗口
			// 组件选择表格
			var jstore = new Ext.data.JsonStore({
					storeId : 'id',
					url : __ctxPath + '/org/showBCs.do',
					root : "result",
					totalProperty : "totalCounts",
					idProperty: "bcid",
					fields:[ 
						{name : "bcid",type : "string"}, 
						{name : "orgid",type : "string"}, 
						{name : "bcCode",type : "string"}, 
						{name : "bcName",type : "string"}, 
						{name : "used",type : "boolean"}, 
						{name : "startUseDate",type : "date",dateFormat : 'time'}, 
						{name : "bcDiscription",type : "string"}
					]
				});
			
			var sm = new OECP.ui.comp.OECPCheckboxSelectionModel();
			// 取消选择前，如果获得是已经建账的数据则不允许取消选择。
			sm.on('beforerowdeselect',function(m , rowIndex, record ){
				var row = jstore.getById(record.id);
		        var isdisable = row.get('used');
				if(isdisable){
					return false;
				}
			});
			
			var validrow = 0;
			var bcs_grid = new Ext.grid.GridPanel({
					id : "bcs_grid",
					store : jstore,
					height : 300,
					autoScroll : true,
					stripeRows : true,
					trackMouseOver : true,
					sm : sm,
					viewConfig : {
						forceFit : true,
						autoFill : true
					},
					columns: [
					sm,
			        {header: "组件编号", dataIndex: 'bcCode'},
			        {header: "组件名称", dataIndex: 'bcName'},
			        {header: "组件描述", dataIndex: 'bcDiscription'},
			        {header: "启用日期", renderer: Ext.util.Format.dateRenderer('Y-m-d'),dataIndex: 'startUseDate'}
			    ]

			});
			
			jstore.on('load',function(){
					
					for(i = 0; i<jstore.getCount(); i++){
						var row = jstore.getAt(i);
						if(row.get('used')){
							sm.selectRow(i);
						}
					}
			},this,{delay:300});
			
			// 控制启用组件按钮是否可用
			sm.on('rowselect',function(m , rowIndex, record ){
				upCommitBtn();
			});
			sm.on('rowdeselect',function(m , rowIndex, record ){
				upCommitBtn();
			});
			function upCommitBtn(){
				var sdata = bcs_grid.getSelectionModel().getSelections();
				for(var idx=0 ; idx < sdata.length ; idx++){
					if(!sdata[idx].data.used){
						Ext.getCmp('btn_startusebc').setDisabled(false);
						return ;
					}
				}
				Ext.getCmp('btn_startusebc').setDisabled(true);
			}
			
			// 构建窗体
			var bcswin = new Ext.Window({
				id : 'win_bcs',
				title : '业务组件建账',
				width : 400,
			    closeAction : 'hide',
			    plain : true,
			    modal : true,
			    layout : 'fit',
				items :[{name:'orgid',hidden:true},bcs_grid],
				buttons:[{
					id:'btn_startusebc',
					text:'启用组件',
					iconCls : "btn-add",
					disabled : true,
					handler:function(){
						var orgid = orgManagerPanel.getSelectedId();
						var sdata = bcs_grid.getSelectionModel().getSelections();
						var bcsformItems = [{id:'bcs_orgid',fieldLabel: 'orgid',name:'orgid',value:orgid, hidden:true}];
						// 只取未建账的数据进行提交
						for(var idx=0 ; idx < sdata.length ; idx++){
							if(!sdata[idx].data.used)
								bcsformItems.push({id:'bcids'+sdata[idx].id,fieldLabel: 'bcids',name:'bcids',value:sdata[idx].id,hidden:true});
						}
						fp = new Ext.form.FormPanel({
							id : 'form_commitAjax',
							defaultType : 'textfield',
							renderTo : document.body,
							items:bcsformItems
						});
//						delete bcsformItems;
						Ext.Msg.confirm("信息确认", "业务组件建账操作是不可逆操作，启用后不能够取消！</br>请确认是否继续进行？", function(b) {
							if (b == "yes") {
								fp.getForm().submit({
									url:__ctxPath + "/org/startUseBCs.do",
									method : 'POST',
									waitTitle : "请等待" ,  
   									waitMsg: '正在初始化业务组件...',
									success : function(form , msg){
										Ext.ux.Toast.msg("信息", msg.result.msg);
										bcswin.hide();
									},
									failure : function(form , msg){
										Ext.Msg.show({title:"错误",msg:msg.result.msg,buttons:Ext.Msg.OK});
									}
								});
							}
						});
					}
					},
					{id:'btn_usebccancel',
					iconCls : "btn-cancel",
					text:'取消',
					handler:function(){
						bcswin.hide();
					}
					}
					]
			});
			bcswin.on('hide', function(){
				sm.clearSelections();
			});
			OECP.org.OrgManagerPanel.superclass.initComponent.call(this);
		}
	});
