Ext.ns("OECP.Warn")

/**
 * 预警通知人员配置
 * @param {} assignOrgId
 * @param {} assignFlag
 * @return {}
 */
OECP.Warn.WarnNoticeUser = Ext.extend(Ext.Panel,{
	title : "预警通知人员配置",
	width : 590,
	frame : true,
	roleGrid : undefined,
	postGrid : undefined,
	userGrid : undefined,
	/**
	 * 初始化方法
	 */
	initComponent : function(){
		var me = this;
		this.isEdit = this.isEdit;
		var assignOrgId = curUserInfo.orgId;
		me.initRole(me,assignOrgId);
		me.initPost(me,assignOrgId);
		me.initUser(me,assignOrgId);
		me.initContentPanel(me);
		OECP.Warn.WarnNoticeUser.superclass.initComponent.call(this);
	},
	/**
	 * 初始化角色
	 * @param {} me
	 * @param {} assignOrgId
	 */
	initRole : function(me,assignOrgId){
		/****begin角色*********************/
		var sm = new Ext.grid.CheckboxSelectionModel();
		var store = new Ext.data.JsonStore({
			url : __ctxPath + '/bpm/def/queryRoleByOrgId.do?orgId='+assignOrgId ,
			root : "result",
			fields : [ "id","name","code","orgRoleId"],
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
		

		me.roleGrid = new Ext.grid.GridPanel({
			height : 430,
			store : store,
			cm : cm,
			sm : sm,
			trackMouseOver : false,
			loadMask : true,
			autoScroll : true,
			frame : true,
			hidden : true
		});
	
		/****end角色*********************/
	},
	/**
	 * 初始化岗位
	 * @param {} me
	 * @param {} assignOrgId
	 */
	initPost : function(me,assignOrgId){
		/****begin岗位*********************/
		var postSm = new Ext.grid.CheckboxSelectionModel();
		var postStore = new Ext.data.JsonStore({
			url : __ctxPath + "/bpm/def/getPostList.do",
			baseParams : {
				assignOrgId : assignOrgId
			},
			storeId : 'id',
			fields : ['id', 'name', 'code', 'charge', 'parent.id',
					'parent.name', 'dept.id', 'dept.name']
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
		
		
		me.postGrid = new Ext.grid.GridPanel({
			height : 430,
			store : postStore,
			cm : postCm,
			sm : postSm,
			trackMouseOver : false,
			loadMask : true,
			autoScroll : true,
			frame : true,
			hidden : true
		});

		/****end岗位*********************/
	},
	/**
	 * 初始化用户
	 * @param {} me
	 * @param {} assignOrgId
	 */
	initUser : function(me,assignOrgId){
		/****begin用户*********************/
		me.userSearchForm = new Ext.FormPanel({
			height : 35,
			frame : true,
			layout : 'column',
			defaults : {
				xtype : 'label'
			},
			hidden : true,
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
							var grid = me.userGrid;
							if (me.userSearchForm.getForm().isValid()) {
								me.userSearchForm.getForm().submit({
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
		
		
		me.userGrid = new Ext.grid.GridPanel({
			height : 395,
			store : userStore,
			cm : userCm,
			sm : userSm,
			trackMouseOver : false,
			loadMask : true,
			autoScroll : true,
			frame : true,
			hidden : true
		});
		
		/****end用户*********************/
	},
	/**
	 * 初始化内容面板
	 * @param {} me
	 */
	initContentPanel : function(me){
		
		me.items = [{
			            xtype: 'radiogroup',
			            items: [{
				                boxLabel: '角色', 
				                name: 'warn.warnNoticeUserType', 
				                inputValue: OECP.Warn.StaticParam.warnNoticeUserRole, 
				                listeners:{
					                check : function(){
	                					if(this.checked){
	                						me.roleGrid.show();
											me.postGrid.hide();
											me.userGrid.hide();
											me.userSearchForm.hide();
	                					}
					                }
				                }
			                },{
				                boxLabel: '岗位', 
				                name: 'warn.warnNoticeUserType', 
				                inputValue: OECP.Warn.StaticParam.warnNoticeUserPost, 
				                listeners:{
					                check : function(){
	                					if(this.checked){
	                						me.postGrid.show();
											me.roleGrid.hide();
											me.userGrid.hide();
											me.userSearchForm.hide();
	                					}
					                }
				                }
			                },{
				                boxLabel: '人员',
				                name: 'warn.warnNoticeUserType', 
				                inputValue: OECP.Warn.StaticParam.warnNoticeUser, 
				                listeners:{
					                check : function(){
	                					if(this.checked){
	                						me.userSearchForm.show();
	                						me.userGrid.show();
											me.roleGrid.hide();
											me.postGrid.hide();
	                					}
					                }
				                }
			                }
	            ]
        	},me.roleGrid,me.postGrid,me.userSearchForm,me.userGrid];
	},
	listeners : {
		afterrender : function(){
	    						//加载数据
						    	this.roleGrid.getStore().removeAll();
								this.roleGrid.getStore().load();
								this.postGrid.getStore().removeAll();
								this.postGrid.getStore().load();
								this.userGrid.getStore().removeAll();
								this.userGrid.getStore().load();
								if(!this.isEdit)
									this.items.items[0].setValue(OECP.Warn.StaticParam.warnNoticeUserRole);
	    }
   },
   /**
    * 取值
    * @return {}
    */
   getWarnNoticeUserData : function(){
   		var warnNoticeUserType = "";
   		var r = this.items.items[0];
   		r.eachItem(function(item){   
		    if(item.checked==true){   
		        warnNoticeUserType = item.inputValue;   
		    }   
		});
		var tmp_grid;
		var tmp_name;
		var data = {'warn.warnNoticeUserType':warnNoticeUserType};
		if(warnNoticeUserType==OECP.Warn.StaticParam.warnNoticeUserRole){//角色
			tmp_grid = this.roleGrid;
			tmp_name = "noticeRoles";
		}else if(warnNoticeUserType==OECP.Warn.StaticParam.warnNoticeUserPost){//岗位
			tmp_grid = this.postGrid;
			tmp_name = "noticePosts";
		}else{//人员
			tmp_grid = this.userGrid;
			tmp_name = "noticeUsers";
		}
		var record = tmp_grid.getSelectionModel().getSelected();
		var records = tmp_grid.getSelectionModel().getSelections();
		//角色做一下特殊处理
		if(warnNoticeUserType==OECP.Warn.StaticParam.warnNoticeUserRole){
			for (var i = 0; i < records.length; i++) {
				var record = records[i];
				var id = record.get('orgRoleId');
				data['warn.'+tmp_name+'['+i+'].id'] = id;
			}
		}else{
			for (var i = 0; i < records.length; i++) {
				var record = records[i];
				var id = record.get('id');
				data['warn.'+tmp_name+'['+i+'].id'] = id;
			}
		}
		
		
		return data;
   },
   /**
    * 赋值
    * @param {} warnNoticeUserType
    * @param {} noticeData
    */
   setFormValues : function(warnNoticeUserType,noticeData,me){
   		me.items.items[0].setValue(warnNoticeUserType);
   		var tmp_grid;
   		var s = me.roleGrid.getStore();
   		if(warnNoticeUserType==OECP.Warn.StaticParam.warnNoticeUserRole){//角色
			tmp_grid = me.roleGrid;
		}else if(warnNoticeUserType==OECP.Warn.StaticParam.warnNoticeUserPost){//岗位
			tmp_grid = me.postGrid;
		}else{//人员
			tmp_grid = me.userGrid;
		}
		var store = tmp_grid.getStore();
		//角色特殊处理
		if(warnNoticeUserType==OECP.Warn.StaticParam.warnNoticeUserRole){
			store.on('load', function(store, records, options) {   
		        tmp_grid.getSelectionModel().clearSelections();   //清空数据
		        Ext.each(records, function(rec) {
		        	Ext.each(noticeData,function(r){
		        		if (r['role']['id']==rec.get('id')) {
			              tmp_grid.getSelectionModel().selectRow(store.indexOfId(rec.get('id')),true);   
			           }  
		        	});
		        }); 
	    	},this,{delay:300});
		}else{
			store.on('load', function(store, records, options) {   
		        tmp_grid.getSelectionModel().clearSelections();   //清空数据
		        Ext.each(records, function(rec) {
		        	Ext.each(noticeData,function(r){
		        		if (r['id']==rec.get('id')) {
			              tmp_grid.getSelectionModel().selectRow(store.indexOfId(rec.get('id')),true);   
			           }  
		        	});
		        }); 
	   	 	},this,{delay:300});
		}
		
		store.removeAll();
		store.load();
   }
})
