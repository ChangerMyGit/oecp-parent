Ext.ns("OECP.bpm.config");
/**
 * 流程定义配置页面
 * @author yangtao
 * @class OECP.bpm.VirProcessDefView
 * @extends Ext.Panel
 */
OECP.bpm.VirProcessDefView  = Ext.extend(Ext.Panel, {
	/**
	 * id
	 */
	id : "VirProcessDefView",
	/**
	 * 标题
	 */
	title : '流程定义配置列表',
	/**
	 * 面板是否可关闭
	 */
	closable : true,
	/**
	 * 布局
	 */
	layout : 'table',
	/**
	 * 布局配置
	 */
	layoutConfig: {
        columns: 1
    },
    /**
     * 内容
     */
	items : this.items,
	/**
	 * 重置方法
	 */
	reset : function() {
		this.fp.form.reset();
	},
	/**
	 * 流程定义列表页面记录数
	 * @type Number
	 */
	pageNum : 10,
	/**
	 * combo列表记录数
	 * @type Number
	 */
	comboPageNum : 10,
	/**
	 * 搜索方法
	 */
	search : function() {
		var functionid = this.funComboBox.getValue().substring(4);
		var id = this.processSearch.getValue();
		var param = '';
		param = {
				functionid : functionid,
				proDefinitionId : id,
				limit : this.pageNum
		};

		this.store.removeAll();
		this.store.baseParams = param;
		this.store.load();
	},
	/**
	 * 加载流程节点信息 
	 */
	reloadDetail : function() {
		var records = this.grid.getSelectionModel().getSelections();
		var record = this.grid.getSelectionModel().getSelected();
		if (!records&&records.length!=0) {
			Ext.Msg.alert("提示", "请先选择流程定义!");
			return;
		}
		var id = record.get('id');//虚拟流程ID
		var deployId = record.get('deployId');//jbpm4中部署ID
		var assignOrgId = record.get('assignedOrgId');//分配组织ID
		
		var param = {
			virProDefinitionId : id,
			deployId : deployId,
			assignOrgId : assignOrgId
		};
		this.panel.load({
		    url: __fullPath+'/bpm/def/getProcessImage.do',
		    params: param, // or a URL encoded string
		    discardUrl: false,
		    nocache: false,
		    text: 'Loading...',
		    timeout: 30,
		    scripts: false
		});
		//启用、停用按钮
		var isUseId = record.get('isUseId');
		var buttonText = "";
		if(isUseId=='1'){
			buttonText = "停用";
		}else{
			buttonText = "启用";
		}
		this.grid.getTopToolbar().get(7).setText(buttonText);
	},
	/**
	 * 不选择时
	 */
	deselect : function(){
		this.panel.body.update('<p style="color:red;font-size:20px" align="center">请选择上面列表中的某个流程定义,进行节点信息配置！</p>');
	}, 
	/**
	 * 根据流程定义分配给多个组织时形成虚拟流程定义
	 */
	addVirtualProcess : function() {
		// 功能菜单
		var param = this.processSearch.getValue();
		if (param == null || param == '') {
			Ext.Msg.alert("提示", "请先选择流程定义!");
			return;
		}
		// 功能ID
		var functionid = this.funComboBox.getValue().substring(4);
		if (functionid == null || functionid == '') {
			Ext.Msg.alert("提示", "请先选择功能菜单!");
			return;
		}
		var tree = new Ext.tree.TreePanel({
				title : '选择要分配的组织',
				height : 300,
				// width : 400,
				useArrows : true,
				autoScroll : true,
				animate : true,
				enableDD : true,
				containerScroll : true,
				rootVisible : false,
				frame : false,
				root : {
					nodeType : 'async',
					id : "source"
				},
				loader : new Ext.tree.TreeLoader({
							dataUrl : __ctxPath + "/app/org/getDefOrgTree.do?cb=0&functionid="+functionid,
							baseParams : {}
				 })
			});
	
		tree.expandAll();

		var store = this.store;
		var win = new Ext.Window({
			title : '增加流程信息',
			width : 530,
			height : 220,
			modal : true,
			buttons : [{
				xtype : 'button',
				text : "分配",
				iconCls : 'save',
				handler : function() {
					var nodes = tree.getChecked();
					var ids = '';
					if (nodes.length == 0) {
						Ext.MessageBox.show({
									title : "警告",
									msg : '请选择要分配到的组织！',
									buttons : Ext.MessageBox.OK,
									icon : Ext.MessageBox.ERROR
								});
					} else {
						Ext.each(nodes, function(node) {
									ids = ids + "," + node.id
						});
					}			
					var actionUtil = new ActionUtil();
					actionUtil.addVirProcessDefinition({processDefinitionId:param,orgIds:ids},win,store);
				}
			}, {
				xtype : 'button',
				handler : function() {
					win.close();
				},
				text : '关闭',
				scope : this
			}],
			items : [tree]
		});
		win.show();
	},
	/**
	 * 删除虚拟流程定义
	 */
	deleteVirtualProcess : function() {
		var removeIds = '';
		var actionUtil = new ActionUtil();
		var record = this.grid.getSelectionModel().getSelected();
		var records = this.grid.getSelectionModel().getSelections();
		if (!record) {
			Ext.Msg.alert("提示", "请先选择要删除的行!");
			return;
		}
		if (records.length == 0) {
			Ext.MessageBox.alert('警告', '最少选择一条信息，进行删除!');
			return;
		}

		Ext.Msg.confirm('提示框', '您确定要进行删除操作吗?', function(button) {
			if (button == 'yes') {
				for (var i = 0; i < records.length; i++) {
					var record = records[i];
					var id = record.get('id');
					removeIds += id;
					removeIds += ",";
//					this.store.remove(records[i]);
				}
				// 删除需要删除的行信息
				actionUtil.deleteVirtualProcessDefinition(removeIds,this.store);
				// 清空删除列表
				removeIds = '';
			}
		}, this);
	},
	
	/**
	 *  初始化
	 */
	initComponent : function() {
		var scope = this;
		/**
		 * 初始化父类
		 */
		OECP.bpm.VirProcessDefView.superclass.initComponent.call(this);
		
		 //功能菜单
	    this.funComboBox = new OECP.core.FunComboBox({
			rootVisible : true,
			fieldLabel : "功能菜单",
			editable :true,
			nodeClick : function(node) {
				if (node.id != null && node.id != '') {
					if (node.id == 'none' || node.isLeaf()) {
						scope.funComboBox.setValueAndText(node.id, node.text);
						scope.funComboBox.menu.hide();
					} else {
						Ext.Msg.alert("提示", "此节点无效，请重新选择!")
					}
				}
			}
		});
		/**
		 * jbpm中流程定义store
		 */
		var processStore = new Ext.data.JsonStore({
			id : Ext.id(),
			url : __ctxPath+'/bpm/def/getProDefinition.do' ,
			root : "result",
			fields : ['id','name','description'],
			remoteSort : false,
			timeout : 8000,
			totalProperty: 'totalCounts'
			});
		var comboPageNum = this.comboPageNum;
		/**
		 * jbpm中流程定义下拉选框
		 */
		this.processSearch = new Ext.form.ComboBox({
			xtype : 'combo',
			fieldLabel : "流程名称",
			name : "processDefinitionName",
			store : processStore,
			displayField : 'name',
			valueField : "id",
			emptyText : '请选择',
			typeAhead : true,
			mode : 'remote',
			triggerAction : 'all',
			selectOnFocus : true,
			allowBlank : true,
			width : 180,
			minListWidth:210,   
			pageSize:comboPageNum, 
			listeners : {
				blur : function(combo) {// 当其失去焦点的时候
					if (combo.getRawValue() == '') {
						combo.reset();
					}
				},
				beforequery : function(queryEvent) {
					var param = queryEvent.query;
					this.store.load({
						params : {
							limit : comboPageNum,
							'conditions[0].field':'name',
							'conditions[0].operator':'like',
							'conditions[0].value' : '%'+param+'%'
						},
						callback : function(r, options, success) {

						}
					});
					return true;
				}
			}
		});

		var sm = new Ext.grid.CheckboxSelectionModel();
		this.store = new Ext.data.JsonStore({
			url : __ctxPath+'/bpm/def/listVirProcessDefinition.do' ,
			baseParams :{
				limit : this.pageNum,
				proDefinitionId : ''
			},
			root : "result",
			fields : [ "id","name","proDefinitionId","proDefinitionName","deployId","processDefinitionId","assignedOrgId","assignedOrgName","isUseId","isUseName","belongFunctionId","belongFunctionName"],
			totalProperty : 'totalCounts'
		});
		this.cm = new Ext.grid.ColumnModel([sm, {
			header : "虚拟流程ID",
			hidden : true,
			dataIndex : "id",
			width : 150
		}, {
			header : "功能名称",
			dataIndex : "belongFunctionName",
			width : 200
		}, {
			header : "分配组织名称",
			dataIndex : "assignedOrgName",
			width : 200
		}, {
			header : "流程定义名称",
			dataIndex : "proDefinitionName",
			width : 300
		},{
			header : "虚拟流程名称",
			dataIndex : "name",
			width : 200
		}, {
			header : "是否启用",
			dataIndex : "isUseName",
			width : 200,
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
		}]);

		this.pageBar = new Ext.PagingToolbar({
			pageSize : this.pageNum,
			store : this.store,
			displayInfo : true,
			displayMsg : '当前显示 {0}-{1}条记录 /共{2}条记录',
			emptyMsg : "无显示数据"
		});
		/**
		 * 主表grid 虚拟流程列表
		 */
		this.grid = new Ext.grid.GridPanel({
			height : 240,
			width : 1170,
			store : this.store,
			cm : this.cm,
			sm : sm,
			trackMouseOver : false,
			loadMask : true,
			autoScroll : true,
			frame : true,
			bbar : this.pageBar,
			tbar : [new Ext.Toolbar.TextItem('功能菜单：'),this.funComboBox,new Ext.Toolbar.TextItem('流程定义：'), this.processSearch, {
				xtype : 'button',
				style : 'margin:4px 10px 4px 10px',
				text : '查询',
				scope : this,
				pressed : true,
				iconCls : 'btn-search',
				handler : this.search
			}, {
				xtype : 'button',
				style : 'margin:4px 10px 4px 10px',
				iconCls : 'btn-add',
				text : '分配组织流程',
				scope : this,
				pressed : true,
				handler : this.addVirtualProcess
			}, {
				xtype : 'button',
				style : 'margin:4px 10px 4px 10px',
				iconCls : 'btn-delete',
				text : '删除',
				scope : this,
				pressed : true,
				handler : this.deleteVirtualProcess
			},{
				xtype : 'button',
				style : 'margin:4px 10px 4px 10px',
				text : '启用',
				scope : this,
				pressed : true,
				iconCls : 'btn-edit',
				handler : function() {
					var records = this.grid.getSelectionModel().getSelections();
					if (records.length == 0||records.length >1) {
						Ext.MessageBox.alert('提示', '请选择一条流程定义，进行启用/停用!');
						return;
					}
					var record = records[0];
					var virProDefinitionId = record.get('id');//虚拟流程定义ID
					var isUseId = record.get('isUseId');
					var actionUtil = new ActionUtil();
					var store = this.store;
					var msg = isUseId=='0'?'启用':'停用';
					Ext.Msg.confirm('提示框', '您确定要'+msg+'该流程?', function(button) {
						if (button == 'yes') {
							actionUtil.useProcessDefinition({virProDefinitionId:virProDefinitionId,isUseId:isUseId},store,msg);
						}
					}, this);
				}
			}
//			,{
//				xtype : 'button',
//				style : 'margin:4px 10px 4px 10px',
//				text : '启动流程',
//				scope : this,
//				pressed : true,
//				iconCls : 'edit',
//				handler : function() {
//					var records = this.grid.getSelectionModel().getSelections();
//					if (records.length == 0||records.length >1) {
//						Ext.MessageBox.alert('警告', '请选择一条流程定义，进行启动!');
//						return;
//					}
//					var record = records[0];
//					var virProDefinitionId = record.get('id');//虚拟流程定义ID
//					var actionUtil = new ActionUtil();
//					actionUtil.startVirProcessDefinition(virProDefinitionId);
//				}
//			}
			]

		});
		/**
		 * 主表选中上加事件
		 */
		sm.on("rowselect",this.reloadDetail,this);
		sm.on("rowdeselect",this.deselect,this);
		
		/**
		 * 流程图片
		 */
		 this.panel = new Ext.Panel({
					title : "点击任务节点进行人员配置",
					height : 500,
					width : 1170,
					frame : true,
					autoScroll : true,
					html : '<p style="color:red;font-size:20px" align="center">请选择上面列表中的某个流程定义,进行节点信息配置！</p>'
					//html:'<image src="'+__fullPath+'/bpm/instance/getImage2.do?deployId='+this.deployId+'"></image>'
					//autoLoad :{url:__fullPath+'/bpm/def/getProcessImage.do?deployId=930001&virProDefinitionId=5a0166p58qgi0016&assignOrgId=0000'}
		});
		
		/**
		 * 加载数据
		 */
		this.store.removeAll();
		this.store.load();
		
		this.add(this.grid);
		this.add(this.panel);
	}
});

/**
 * 给任务节点分配人员
 */
OECP.bpm.VirProcessDefView.assign=function(virProActivityId,virProDefinitionId,assignOrgId,activityName){
	Ext.Ajax.request({
			url:__ctxPath + "/bpm/def/getTaskConfig.do",
			params:{
				virProActivityId:virProActivityId,
				virProDefinitionId:virProDefinitionId
			},
			success:function(request){
				var json = Ext.decode(request.responseText);
				OECP.bpm.VirProcessDefView.assignValue(virProActivityId,virProDefinitionId,assignOrgId,activityName,json);
			},
			failure:function(flag){
				Ext.Msg.alert('提示',flag.responseText,function(){});
			}
	});
}
/**
 * 给任务节点分配人员赋值
 */
OECP.bpm.VirProcessDefView.assignValue=function(virProActivityId,virProDefinitionId,assignOrgId,activityName,json){
		var counterSign = json.counterSign;
		var passRate = json.passRate;
		var assignFlag = json.assignFlag;
		var assignValue = json.assignValue;
		var nextTask = json.nextTask;
		var nextTaskUser = json.nextTaskUser;
		var editBill = json.editBill;
		/****begin角色*********************/
		var sm = new Ext.grid.CheckboxSelectionModel();
		var store = new Ext.data.JsonStore({
			url : __ctxPath + '/bpm/def/queryRoleByOrgId.do?orgId='+assignOrgId ,
			root : "result",
			fields : [ "id","name","code"],
			totalProperty : 'totalCounts'
		});
		var cm = new Ext.grid.ColumnModel([sm, {
				header : "主键",
				dataIndex : "id",
				hidden : false
			}, {
				header : "编码",
				dataIndex : "code"
			}, {
				header : "名称",
				dataIndex : "name"
			}]);
		

		var grid = new Ext.grid.GridPanel({
			height : 430,
			store : store,
			cm : cm,
			sm : sm,
			trackMouseOver : false,
			loadMask : true,
			autoScroll : true,
			frame : true,
			hidden : false
		});

		
		/**
		 * store加载完之后，把已经选过的值展现出来
		 */
		if(assignFlag=="ROLE"){
			grid.store.on('load', function(store, records, options) {   
		        sm.clearSelections();   //清空数据
		         Ext.each(records, function(rec) {
		        	Ext.each(assignValue.split(","),function(r){
		        		if (r==rec.get('id')) {  
			              sm.selectRow(grid.store.indexOfId(rec.get('id')),true);   
			           }  
		        	});
		        });  
	      },this,{delay:300});
		}
		/****end角色*********************/
		
		
		/****begin岗位*********************/
		var postSm = new Ext.grid.CheckboxSelectionModel();
		var postStore = new Ext.data.JsonStore({
			url : __ctxPath + "/bpm/def/getPostList.do",
			baseParams : {
				assignOrgId : assignOrgId
			},
			storeId : 'id',
			fields : ['id', 'name', 'code', 'charge', 'parent.id',
					'parent.name', 'dept.id', 'dept.name'],
			autoLoad : true
		});
		var postCm = new Ext.grid.ColumnModel([postSm, {
					header : "主键",
					dataIndex : "id",
					hidden : false
				}, {
					header : "岗位名称",
					dataIndex : "name"
				}, {
					header : "岗位编码",
					dataIndex : "code"
				}]);
		
		
		var postGrid = new Ext.grid.GridPanel({
			height : 430,
			store : postStore,
			cm : postCm,
			sm : postSm,
			trackMouseOver : false,
			loadMask : true,
			autoScroll : true,
			frame : true,
			hidden : false
		});

		/**
		 * store加载完之后，把已经选过的值展现出来
		 */
		if(assignFlag=="POST"){
			postGrid.store.on('load', function(store, records, options) {   
		        postSm.clearSelections();   //清空数据
		        Ext.each(records, function(rec) {
		        	Ext.each(assignValue.split(","),function(r){
		        		if (r==rec.get('id')) {  
			              postSm.selectRow(postGrid.store.indexOfId(rec.get('id')),true);   
			           }  
		        	})
		        })  
	      },this,{delay:300});
		}
		/****end岗位*********************/
		
		/****begin用户*********************/
		var userSearchForm = new Ext.FormPanel({
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
						width : 80,
						xtype : 'hidden',
						name : 'orgId',
						value : assignOrgId
					}, {
						xtype : 'button',
						text : '查询',
						iconCls : 'search',
						handler : function() {
							var grid = userGrid;
							if (userSearchForm.getForm().isValid()) {
								userSearchForm.getForm().submit({
									waitMsg : '正在提交查询',
									url : __ctxPath + '/bpm/def/queryUserList.do',
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
		var userSm = new Ext.grid.CheckboxSelectionModel();
		

		var userStore = new Ext.data.JsonStore({
			url : __ctxPath + '/bpm/def/queryUserList.do' ,
			baseParams : {
				orgId : assignOrgId
			},
			root : "result",
			fields : ['id', 'loginId', 'name', 'email','createTime'],
			totalProperty : 'totalCounts'
		});
		var userCm = new Ext.grid.ColumnModel([userSm, {
							header : "id",
							dataIndex : 'id',
							hidden : true
						}, {
							header : "账号",
							dataIndex : 'loginId',
							width : 60
						}, {
							header : "用户名",
							dataIndex : 'name',
							width : 60
						}, {
							header : "邮箱",
							dataIndex : 'email',
							width : 120
						}, {
							header : "创建时间",
							dataIndex : 'createTime',
							width : 100
						}]);
		
		
		var userGrid = new Ext.grid.GridPanel({
			height : 395,
			store : userStore,
			cm : userCm,
			sm : userSm,
			trackMouseOver : false,
			loadMask : true,
			autoScroll : true,
			frame : true,
			hidden : false
		});
		
		/**
		 * store加载完之后，把已经选过的值展现出来
		 */
	 	if(assignFlag=="USER"){
			userGrid.store.on('load', function(store, records, options) {   
		        userSm.clearSelections();   //清空数据
		        Ext.each(records, function(rec) {
		        	Ext.each(assignValue.split(","),function(r){
		        		if (r==rec.get('id')) {
			              userSm.selectRow(userGrid.store.indexOfId(rec.get('id')),true);   
			           }  
		        	});
		        }); 
	      },this,{delay:300});
		}
		/****end用户*********************/
		
		/**
		 * 是否会签tab页
		 */
		var isCounterSignRulePanel = new Ext.Panel({
					id : "isCounterSignRule",
					title : "是否会签",
					frame : true,
					width : 590,
					items : [{
				            xtype:'fieldset',
				            title: '是否会签',
				            collapsible: false,
				            height : 60,
				            items :[{
						            xtype: 'radiogroup',
						            items: [{
							                boxLabel: '是', 
							                name: 'isCounterSignRule', 
							                inputValue: 'SHI',
							                listeners:{check : function(){
							                					if(this.checked){
							                						Ext.getCmp('_counterSign').show();
							                						Ext.getCmp('_noCcounterSign').hide();
							                						isCounterSignRulePanel.items.items[1].items.items[0].items.items[0].show();
							                						var counterSignRule = "";
							                						var counterSignRuleGroup = isCounterSignRulePanel.items.items[1].items.items[0].items.items[0];
																	counterSignRuleGroup.eachItem(function(item){   
																	    if(item.checked==true){   
																	        counterSignRule = item.inputValue;   
																	    }   
																	});
																	if(counterSignRule=="PROPORTION"){
								                						isCounterSignRulePanel.items.items[1].items.items[0].items.items[1].show();
								                						isCounterSignRulePanel.items.items[1].items.items[0].items.items[2].show();
																	}
							                						
							                					}
							                }}
						                },{
							                boxLabel: '否', 
							                name: 'isCounterSignRule', 
							                inputValue: 'FOU',
							                checked: true,listeners:{check : function(){
							                					if(this.checked){
							                						Ext.getCmp('_counterSign').hide();
							                						Ext.getCmp('_noCcounterSign').show();
							                						isCounterSignRulePanel.items.items[1].items.items[0].items.items[0].hide();
							                						isCounterSignRulePanel.items.items[1].items.items[0].items.items[1].hide();
							                						isCounterSignRulePanel.items.items[1].items.items[0].items.items[2].hide();
							                						isCounterSignRulePanel.items.items[1].items.items[0].items.items[3].hide();
							                					}
							                }}
						                }
						            ]
						        }
				            ]
				        },{
				            xtype:'fieldset',
				            id:'_counterSign',
				            title: '会签规则',
				            collapsible: false,
				            height : 100,
				            items :[{
						        	xtype:'panel',
						        	layout:'table',
								    layoutConfig: {
								        columns: 6
								    },
						        	items:[{
											hidden : true,
											title : '会签规则',
								            xtype: 'radiogroup',
								            items: [{
									                boxLabel: '一票通过', 
									                width:100,
									                name: 'counterSignRule', 
									                inputValue: 'ONE_TICKET_PASS',
									                checked: true,
									                listeners:{check : function(){
						                					if(this.checked){
						                						isCounterSignRulePanel.items.items[1].items.items[0].items.items[1].hide();
						                						isCounterSignRulePanel.items.items[1].items.items[0].items.items[2].hide();
						                						isCounterSignRulePanel.items.items[1].items.items[0].items.items[3].hide();
						                					}
									                }}
								                },{
									                boxLabel: '一票否决', 
									                width:100,
									                name: 'counterSignRule', 
									                inputValue: 'ONE_TICKET_NO_PASS',
									                listeners:{check : function(){
						                					if(this.checked){
						                						isCounterSignRulePanel.items.items[1].items.items[0].items.items[1].hide();
						                						isCounterSignRulePanel.items.items[1].items.items[0].items.items[2].hide();
						                						isCounterSignRulePanel.items.items[1].items.items[0].items.items[3].hide();
						                					}
									                }}
								                },{
									                boxLabel: '比例通过',
									                width:100,
									                name: 'counterSignRule', 
									                inputValue: 'PROPORTION',
									                listeners:{check : function(){
						                					if(this.checked){
						                						isCounterSignRulePanel.items.items[1].items.items[0].items.items[1].show();
						                						isCounterSignRulePanel.items.items[1].items.items[0].items.items[2].show();
						                						isCounterSignRulePanel.items.items[1].items.items[0].items.items[3].show();
						                					}
									                }}
								                }
								            ]
								        },{
								        	text:'通过比例：',
								        	style:'font-size:12px;',
								        	hidden : true,
								        	xtype:'label'
								        },{
								        	hidden : true,
								        	xtype:'textfield'
								        },{
								        	text:'%',
								        	style:'font-size:12px;',
								        	hidden : true,
								        	xtype:'label'
								        }
						        	]
					           }
				            ]
			        },{
				            xtype:'fieldset',
				            id:'_noCcounterSign',
				            title: '配置',
				            collapsible: false,
				            height : 250,
				            items :[
				            	{
						            xtype:'fieldset',
						            title: '是否指定下个任务',
						            collapsible: false,
						            height : 60,
						            items :[{
								            xtype: 'radiogroup',
								            items: [{
									                boxLabel: '是', 
									                name: 'nextTask', 
									                inputValue: 'true'
								                },{
									                boxLabel: '否', 
									                name: 'nextTask', 
									                inputValue: 'false',
									                checked: true
								                }
								            ]
								        }
						            ]
						        },{
						            xtype:'fieldset',
						            title: '是否指定下个任务人员',
						            collapsible: false,
						            height : 60,
						            items :[{
								            xtype: 'radiogroup',
								            items: [{
									                boxLabel: '是', 
									                name: 'nextTaskUser', 
									                inputValue: 'true'
								                },{
									                boxLabel: '否', 
									                name: 'nextTaskUser', 
									                inputValue: 'false',
									                checked: true
								                }
								            ]
								        }
						            ]
						        },{
						            xtype:'fieldset',
						            title: '是否可编辑单据',
						            collapsible: false,
						            height : 60,
						            items :[{
								            xtype: 'radiogroup',
								            items: [{
									                boxLabel: '是', 
									                name: 'editBill', 
									                inputValue: 'true'
								                },{
									                boxLabel: '否', 
									                name: 'editBill', 
									                inputValue: 'false',
									                checked: true
								                }
								            ]
								        }
						            ]
						        }
				            ]
			        }
		],
		listeners : {afterrender : function(){
			if(counterSign!=''&&counterSign!='NO_COUNTERSIGN_RULE'){
				Ext.getCmp('_counterSign').show();
				Ext.getCmp('_noCcounterSign').hide();
				isCounterSignRulePanel.items.items[1].items.items[0].items.items[0].show();
	            isCounterSignRulePanel.items.items[0].items.items[0].setValue("SHI");
	            isCounterSignRulePanel.items.items[1].items.items[0].items.items[0].setValue(counterSign);
	            if(counterSign=='PROPORTION'){
	            	isCounterSignRulePanel.items.items[1].items.items[0].items.items[2].setValue(passRate);
	            }
			}else{
				isCounterSignRulePanel.items.items[1].items.items[0].items.items[0].hide();
				isCounterSignRulePanel.items.items[0].items.items[0].setValue("FOU");
				Ext.getCmp('_counterSign').hide();
				Ext.getCmp('_noCcounterSign').show();
				isCounterSignRulePanel.items.items[2].items.items[0].items.items[0].setValue(nextTask);
				isCounterSignRulePanel.items.items[2].items.items[1].items.items[0].setValue(nextTaskUser);
				isCounterSignRulePanel.items.items[2].items.items[2].items.items[0].setValue(editBill);
			}
		}}
	});
		
		/**
		 * 任务人员指派tab页
		 */
		var assignFlagForm = new Ext.Panel({
						id : "assignFlagForm",
						title : "任务人员指派",
						width : 590,
						frame : true,
						items : [{
						            xtype: 'radiogroup',
						            items: [{
							                boxLabel: '角色', 
							                name: 'assignFlag', 
							                inputValue: 'ROLE', 
							                checked: true,listeners:{check : function(){
							                					if(this.checked){
							                						grid.show();
																	postGrid.hide();
																	userGrid.hide();
																	userSearchForm.hide();
							                					}
							                }}
						                },{
							                boxLabel: '岗位', 
							                name: 'assignFlag', 
							                inputValue: 'POST', 
							                listeners:{check : function(){
							                					if(this.checked){
							                						postGrid.show();
																	grid.hide();
																	userGrid.hide();
																	userSearchForm.hide();
							                					}
							                }}
						                },{
							                boxLabel: '人员',
							                name: 'assignFlag', 
							                inputValue: 'USER', 
							                listeners:{check : function(){
							                					if(this.checked){
							                						userSearchForm.show();
							                						userGrid.show();
																	grid.hide();
																	postGrid.hide();
							                					}
							                }}
						                },{
							                boxLabel: '流程发起人',
							                name: 'assignFlag', 
							                inputValue: 'COMMITUSER', 
							                listeners:{check : function(){
							                					if(this.checked){
							                						userSearchForm.hide();
							                						userGrid.hide();
																	grid.hide();
																	postGrid.hide();
							                					}
							                }}
						                }
				            ]
			        	},grid,postGrid,userSearchForm,userGrid
			       ],
    			listeners : {afterrender : function(){
    						//加载数据
					    	store.removeAll();
							store.load();
							postStore.removeAll();
							postStore.load();
							userStore.removeAll();
							userStore.load();
    							
    						if(assignFlag!='')
    							assignFlagForm.items.items[0].setValue(assignFlag);
    			}}
		});
		
		/**
		 *  填充显示内容
		 */
		var cpanel = new Ext.TabPanel({
			height : 530,
			frame : true,
		    activeTab: 0,
		    //默认是true,只激活activeTab指定的panel，设为false，所有panel都激活
		    deferredRender : false,
		    items: [isCounterSignRulePanel,assignFlagForm]
		});

		
		/**
		 * 任务节点指派人员配置窗口
		 */
		var win = new Ext.Window({
			title : activityName+'--任务节点配置',
			width : 600,
			height : 560,
			modal : true,
			buttonAlign : 'center',
			buttons : [
						{
							xtype : 'button',
							text : "保存",
							iconCls : 'save',
							handler : function() {
								//是否会签
								var isCounterSignRule = "";
								var isCounterSignRuleGroup = isCounterSignRulePanel.items.items[0].items.items[0];
								isCounterSignRuleGroup.eachItem(function(item){   
								    if(item.checked==true){   
								        isCounterSignRule = item.inputValue;   
								    }   
								});
								//会签规则
								var counterSignRule = "";
								var passRate = "";
								var nextTask2 = "";
								var nextTaskUser2 = "";
								var editBill2 = "";
								if(isCounterSignRule=='SHI'){
									var counterSignRuleGroup = isCounterSignRulePanel.items.items[1].items.items[0].items.items[0];
									counterSignRuleGroup.eachItem(function(item){   
									    if(item.checked==true){   
									        counterSignRule = item.inputValue;   
									    }   
									});
									if(counterSignRule=="PROPORTION"){
										passRate = isCounterSignRulePanel.items.items[1].items.items[0].items.items[2].getValue();
										if(passRate>100){
											Ext.Msg.alert('提示','通过比例不能超过100');
											return;
										}
									}
								}else{
									//是否指派下个任务结点
									var nextTaskGroup = isCounterSignRulePanel.items.items[2].items.items[0].items.items[0];
									nextTaskGroup.eachItem(function(item){   
									    if(item.checked==true){   
									        nextTask2 = item.inputValue;   
									    }   
									});
									//是否给下个任务结点指派人员
									var nextTaskUserGroup = isCounterSignRulePanel.items.items[2].items.items[1].items.items[0];
									nextTaskUserGroup.eachItem(function(item){   
									    if(item.checked==true){   
									        nextTaskUser2 = item.inputValue;   
									    }   
									});
									//是否可编辑单据
									var editBillGroup = isCounterSignRulePanel.items.items[2].items.items[2].items.items[0];
									editBillGroup.eachItem(function(item){   
									    if(item.checked==true){   
									        editBill2 = item.inputValue;   
									    }   
									});
								}
								
								//委派类型
								var assignFlag2 = "";
								var assignFlagRadioGroup = assignFlagForm.items.items[0];
								assignFlagRadioGroup.eachItem(function(item){   
								    if(item.checked==true){   
								        assignFlag2 = item.inputValue;   
								    }   
								});
								var tmp_grid = null;
								if(assignFlag2=='ROLE'){
									tmp_grid = grid;
								}else if(assignFlag2=='POST'){
									tmp_grid = postGrid;
								}else if(assignFlag2=='USER'){
									tmp_grid = userGrid;
								}
								
								var actionUtil = new ActionUtil();
								var ids = "";
								if(tmp_grid!=null){
									var record = tmp_grid.getSelectionModel().getSelected();
									var records = tmp_grid.getSelectionModel().getSelections();
									
									for (var i = 0; i < records.length; i++) {
										var record = records[i];
										var id = record.get('id');
										ids += id;
										ids += ",";
									}
								}
								
								actionUtil.assignVirProActivity({virProActivityId:virProActivityId,virProDefinitionId:virProDefinitionId,counterSignRule:counterSignRule,passRate:passRate,ids:ids,assignFlag:assignFlag2,nextTask:nextTask2,nextTaskUser:nextTaskUser2,editBill:editBill2},win);
						
							}
						},{
							xtype : 'button',
							handler : function() {
								win.close();
							},
							text : '关闭',
							scope : this
						}
			],
			items : [cpanel]
		});
		
		/**
		 * 窗口展现
		 */
		win.show();
}

function GetAbsoluteLocation(element) 
{ 
    if ( arguments.length != 1 || element == null ) 
    { 
        return null; 
    } 
    var offsetTop = element.offsetTop; 
    var offsetLeft = element.offsetLeft; 
    var offsetWidth = element.offsetWidth; 
    var offsetHeight = element.offsetHeight; 
    while( element = element.offsetParent ) 
    { 
        offsetTop += element.offsetTop; 
        offsetLeft += element.offsetLeft; 
    } 
    return { absoluteTop: offsetTop, absoluteLeft: offsetLeft, 
        offsetWidth: offsetWidth, offsetHeight: offsetHeight }; 
} 

/**
 * 鼠标放上面触发事件
 * @param id
 * @param x
 * @param y
 * @param width
 * @param height
 */
function mouseOver(id,x,y,width,height){
//	var img = document.getElementById("imageDivId");
//		
//	var node = img;/*求坐标的元素*/
//    var xy=[];/*保存XY坐标*/
//    xy[0]=0;
//    xy[1]=0;
//    var scrollTop = 0;
//    var scrollLeft = 0;
//    //累计元素每级offsetParent属性元素的偏移量
//    while ((node = node.offsetParent)) {
//      xy[0]  = xy[0]+node.offsetLeft;
//      xy[1]  = xy[1]+node.offsetTop;
//    }
//    node = img;
////	xy[0] -= node.scrollLeft;xy[1] -= node.scrollTop;
//    //计算每级父元素有滚动条的情况
//    while ((node = node.parentNode) && node.tagName) {
//	  scrollTop = node.scrollTop;
//	  scrollLeft = node.scrollLeft;
//	  if (scrollTop || scrollLeft) {
//	    xy[0] = xy[0] - scrollLeft;
//	    xy[1] = xy[1] - scrollTop;
//	  }
//	}
//	if(document.getElementById(id)==null){
//		var createDiv = document.createElement("div");
//		
//		createDiv.id = id;
//		createDiv.style.position = 'absolute';
//		createDiv.style.left =  x+xy[0];
//		createDiv.style.top =  y+xy[1];
//		createDiv.style.width = width;
//		createDiv.style.height = height;
//		createDiv.style.border = '4px red solid';
//		createDiv.style.display = "block";
//		document.body.appendChild(createDiv);	
//	}else{
//		document.getElementById(id).style.position = 'absolute';
//		document.getElementById(id).style.left =  x+xy[0];
//		document.getElementById(id).style.top =  y+xy[1];
//		document.getElementById(id).style.width = width;
//		document.getElementById(id).style.height = height;
//		document.getElementById(id).style.border = '4px red solid';
//		document.getElementById(id).style.display = "block";
//	}
}

/**
 * 鼠标离开触发事件
 * @param id
 */
function mouseOut(id){
//	document.getElementById(id).style.display = "none";
}

/**
 * 给decision节点配置信息
 */
OECP.bpm.VirProcessDefView.condition=function(virProActivityId,virProDefinitionId,activityName){
	//去掉字符串的前后空格
	String.prototype.trim = function() {
		return this.replace(/^\s+|\s+$/g, "");
	}

	Ext.Ajax.request({
			url:__ctxPath + "/bpm/def/getDecisionCondition.do",
			params:{
				virProActivityId:virProActivityId,
				virProDefinitionId:virProDefinitionId
			},
			success:function(request){
//			var json = Ext.decode(request.responseText);
				var reasignForm = new Ext.FormPanel({
			        frame:false,
			        width: 570,
			        height : 370,
			        modal: true,
			        items: [{
			                fieldLabel: '条件Groovy脚本 (单据实体变量名是eo)',
			                name: '_decisionScript',
			                value : request.responseText.trim(),
			                width:450,
			                height:300,
			                xtype:'textarea'
			            }
			        ],
			        buttons: [{
			            text: '保存',
			            handler : function(){
			            	var _decisionScript = reasignForm.form.findField('_decisionScript').getValue();
			            	var actionUtil = new ActionUtil();
			            	actionUtil.assignDecisionCondition({virProActivityId:virProActivityId,virProDefinitionId:virProDefinitionId,decisionScript:_decisionScript},win);
			            }
			        },{
			            text: '关闭',
			            handler : function(){
			            	win.close();
			            }
			        }]
			    });
			    
				
				var win = new Ext.Window({
						title : '条件配置',
						width : 600,
						height : 430,
						modal : true,
						items : [reasignForm]
						
				});
		
				win.show();
			},
			failure:function(flag){
				Ext.Msg.alert('提示',flag.responseText,function(){});
			}
	});
}