Ext.ns('OECP.user');

/**
 * 用户管理功能界面
 * 
 * @author yongtree
 * @class UserGridView
 * @extends Ext.Panel
 */
OECP.user.UserGridView=Ext.extend(Ext.Panel,{
	id:'OECP.user.UserGridView',
	layout:'border',
	height:600,
	title:'用户管理',
	initComponent:function() {
		var panel=this;
		panel.items=[new Ext.FormPanel({
			height:35,
			frame:true,
			region:'north',
			id:'UserSearchForm',
			layout:'column',
			defaults:{xtype:'label'},
			items:[{text:'用户账号'},
				{xtype:'hidden',name:'conditions[0].field',value:'loginId'},
				{xtype:'hidden',name:'conditions[0].operator',value:'like'},
				{width:80,xtype:'textfield',name:'conditions[0].value'},
				{text:'用户姓名'},{xtype:'hidden',name:'conditions[1].field',value:'name'},
				{xtype:'hidden',name:'conditions[1].operator',value:'like'},
				{width:80,xtype:'textfield',name:'conditions[1].value'},
				{text:'所属机构'},
				new OECP.core.OrgComboBox({hiddenName:'orgId',id:'OECP.user.orgId',editable:true}),
				{xtype:'button',text:'查询',iconCls:'search',
						handler:function() {
							var userSearchForm=Ext.getCmp('UserSearchForm');
							var grid=Ext.getCmp('OECP.user.UserGrid');
							if (userSearchForm.getForm().isValid()) {
								grid.store.baseParams = userSearchForm.getForm().getValues();
								grid.getStore().load({waitMsg:'正在提交查询'});
							}
						}
					}]
		}),this.grid()];
		panel.doLayout();
		OECP.user.UserGridView.superclass.initComponent.call(this);
	}
});

OECP.user.UserGridView.prototype.grid=function() {
	var store=this.initData();
	var toolbar=this.initToolbar();
	store.load({params:{start:0,limit:25	}});
	var sm=new Ext.grid.CheckboxSelectionModel({
				singleSelect:true,
				listeners:{
					rowselect:function(t,i,r) {
						OECP.user.selectedUser=r;
					},
					rowdeselect:function(t,i,r) {
						OECP.user.selectedUser=null;
					}
				}
			});
	var cm=new Ext.grid.ColumnModel({
		columns:[sm,
			new Ext.grid.RowNumberer(),
			{header:"id",dataIndex:'id',hidden:true},
			{header:"账号",dataIndex:'loginId',width:60},
			{header:"用户名",dataIndex:'name',width:60},
			{header:"邮箱",dataIndex:'email',width:120},
			{header:"创建组织名称",dataIndex:'createdByOrg.name',width:120},
			{header:"创建时间",dataIndex:'createTime',width:100	},
			{header:"状态",dataIndex:'state',width:50,renderer:function(value) {return (value=='disabled'?"禁用":"激活"); }}
		],
		defaults:{sortable:true,menuDisabled:true,width:100	}
	});

	var grid=new Ext.grid.GridPanel({
				id:'OECP.user.UserGrid',region:'center',tbar:toolbar,store:store,	height:400,cm:cm,sm:sm,// shim:true,trackMouseOver:true,disableSelection:false,loadMask:true,
				viewConfig:{forceFit:true,enableRowBody:false,showPreview:false},
				bbar:new Ext.PagingToolbar({pageSize:25,store:store,displayInfo:true,
						displayMsg:'当前显示从{0}至{1}， 共{2}条记录',emptyMsg:"当前没有记录"
				})
			});
	// // 为Grid增加双击事件,双击行可编辑
	// grid.addListener('rowdblclick',rowdblclickFn);
	// function rowdblclickFn(grid,rowindex,e) {
	// grid.getSelectionModel().each(function(rec) {
	// });
	// }
	return grid;
};

OECP.user.UserGridView.prototype.initData=function() {
	var store=new Ext.data.Store({
				proxy:new Ext.data.HttpProxy({url:__ctxPath + '/user/list.do'}),
				reader:new Ext.data.JsonReader({
					id:'id',root:'result',totalProperty:'totalCounts',
					fields:['id','loginId','name','email','state','createTime','createdByOrg.name']
				}),
				remoteSort:true
			});
	return store;
};
// 初始化操作菜单
OECP.user.UserGridView.prototype.initToolbar=function() {
	var toolbar=new Ext.Toolbar({
		width:'100%',
		height:30,
		items:[{text:'添加',iconCls:'add-user',handler:function(){openUserWin();}	},
			{text:'编辑',iconCls:'btn-edit',handler:function() {openUserWin(1);}},
			{text:'删除',handler:function() {
				Ext.Ajax.request({
					url:__ctxPath + "/user/deleteUser.do",
					success:function(res) {
						var msg=Ext.util.JSON	.decode(res.responseText);
						if (msg.success) {
							Ext.Msg.alert('操作信息','删除成功！');
							var userview=Ext.getCmp('OECP.user.UserGrid');
							if (userview != null) {// 假如员工列表不为空,则重载数据
								userview.getStore().reload({start:0,limit:25});
							}
						} else {
							Ext.Msg.show({title:"错误",msg:msg.msg,buttons:Ext.Msg.OK});
						}
					},
					failure:function(res) {
						var f=Ext.util.JSON.decode(res.response.responseText);
						Ext.MessageBox.show({	title:"警告",msg:f.result,buttons:Ext.MessageBox.OK,icon:Ext.MessageBox.ERROR});
					},
					params:{id:OECP.user.selectedUser.id}
				});
			}
		},{
			text:'分配角色',handler:function(){	openUserWin(2);}
		},{
			text:'重置密码',
			handler:function() {
				if (OECP.user.selectedUser==null) {
					Ext.ux.Toast.msg("信息","您没有选择任一行数据！");
				} else {
					Ext.MessageBox.prompt('请确认','您确定要重置密码是?',
							function(btn,text) {
								if (btn=='ok') {
									Ext.Ajax.request({
										url:__ctxPath+"/user/resetPWD.do",
										params:{id:OECP.user.selectedUser.id,resetPWD:text},
										success:function(d,g) {
											var f=Ext.util.JSON.decode(d.responseText);
											Ext.MessageBox.alert('密码重置','密码已经被重置为：'+f.result);
										}
									});
								}
							});
				}
			}
		},{
			text:'查看权限',
			handler:function() {
				showViewPermission();
			}
		}]
	});
	//private 查看权限
	function showViewPermission(){
		var _u=OECP.user.selectedUser;
		if (Ext.isEmpty(_u,false)) {
			Ext.ux.Toast.msg("信息","您没有选择任一行数据！");
			return;
		}
		var _win=new Ext.Window({
			title:'权限列表',width:400,height:500,closeAction:'close',	layout:'form',modal:true,
			items:[{
				fieldLabel:'所属机构',xtype:'combo',
				store:new Ext.data.JsonStore({
					url:__ctxPath+"/user/getOrgs4User.do",root: 'result',    
					baseParams:{id:_u.id},autoLoad:true,fields:['id', 'name']}),
				displayField:'name',valueField:'id',editable:false,mode:'local',triggerAction:'all',
				listeners:{
					'select':function(combo,record,index  ){
						var _p=_win.items.get(1);
						Ext.Msg.wait('正在加载，请稍候......', '提示');
						Ext.Ajax.request({
							url:__ctxPath + '/portal/menu4userid.do',
							params:{userid:combo.store.baseParams.id,orgid:record.get('id')},
							success:function(response, options) {
								var arr=eval(response.responseText);
								_p.removeAll();
								for (var i=0; i < arr.length; i++) {
									_p.add(new Ext.tree.TreePanel({
										title:arr[i].text,	layout:"fit",animate:true,border:false,autoScroll:true,rootVisible:false,
										root:new Ext.tree.AsyncTreeNode({
											id:arr[i].id,text:arr[i].text,expanded:true,leaf:false,children:arr[i].children
										})
									}));
								}
								_p.layout.activeItem=_p.items.get(0);
								_p.doLayout();
								Ext.Msg.hide(); 
							},
							failure: function(response, opts) {
						      Ext.Msg.hide();
						      Ext.ux.Toast.msg("错误","加载失败！\n状态代码:"+response.status);
						   }
						});
						_win.doLayout();
					}
				}
			},{
				xtype:'panel',frame:true,layout:"accordion",split:true,height:400,
				html:"<font color='red'>请先选择\"所属机构\"</font>"
			}],
			buttons:[{text:'关闭',handler:function(){
				_win[_win.closeAction]();
			}}]	
		});
		_win.show();
	}
	function openUserWin(action) {
		if (action) {
			if (OECP.user.selectedUser==null) {
				Ext.ux.Toast.msg("信息","您没有选择任一行数据！");
			} else {
				if (action==1) {
					if (OECP.user.queryOrgId != null
							&& OECP.user.queryOrgId != ''
							&& OECP.user.queryOrgId != curUserInfo.orgId) {
						Ext.MessageBox.show({
							title:"警告",
							msg:'不是所在公司创建，您无权操作',
							buttons:Ext.MessageBox.OK,
							icon:Ext.MessageBox.ERROR
						});
					} else {
						new OECP.user.UserWin("编辑用户--"+OECP.user.selectedUser.get('name'),OECP.user.selectedUser.id,action).show();
					}
				} else {
					new OECP.user.UserWin("分配角色--"+OECP.user.selectedUser.get('name'),OECP.user.selectedUser.id,action).show();
				}
			}
		} else {
			new OECP.user.UserWin('添加用户').show();
		}
	}
	return toolbar;
};
OECP.user.queryOrgId=null;
OECP.user.selectedUser=null;
