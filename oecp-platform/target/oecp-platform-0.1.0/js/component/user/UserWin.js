Ext.ns("OECP.user");

/**
 * 用户信息窗口
 * 
 * @author yongtree
 * @class OECP.user.UserWin
 * @param _title
 *            显示标题
 * @param _userId
 *            用户的主键 不传入该值，则为添加
 * @param _action
 *            1--编辑；2--分配 是否编辑
 * @return Ext.window
 */
OECP.user.UserWin = function(_title, _userId, _action) {
	return this.setup(_title, _userId, _action);
};

OECP.user.UserWin.prototype.setup = function(_title, userId, action) {
//	var footToolbar = this.initFooterToolbar(userId);
	var person=new OECP.user.PersonCombo({	id:'user.personId',fieldLabel:'员工',hiddenName:'user.personId'});
	var userform = new Ext.form.FormPanel({
		id:'OECP.user.UserWin',
		iconCls:'menu-customer',
		border:false, // 不要边框
//		fileUpload:true, // 允许上传
//		tbar:footToolbar,
		bodyStyle:"margin-top:5px;margin-left: 4%; background-color:transparent;",
		labelAlign:"right",
		autoScroll:true,
		defaultType:"textfield",
		url:__ctxPath+'/user/'+(action?(action==1?'save.do':'assignRoles.do'):'create.do'),
		reader:new Ext.data.JsonReader({root:'result'	},
				[{name:'user.id',mapping:'id'},
				{name:'user.personId',mapping:'personId'},
				{name:'user.personName',mapping:'personName'},
				{name:'user.loginId',mapping:'loginId'},
				{name:'user.name',mapping:'name'},
				{name:'user.password',mapping:'password'}, 
				{name:'user.email',mapping:'email'},
				{name:'user.createTime',mapping:'createTime'},
				{name:'user.state',mapping:'state'}]),
		items:[{
			xtype:"panel",
			width:600,
			layout:'column',
			layoutConfig:{columns:2},
			items:[{
						xtype:"panel",
						columnWidth:.50,
						title:"基本信息(必填)",
						layout:'form',
						defaultType:"textfield",
						defaults:{width:163},
						border:false,
						disabled:action == 2 ? true:false,
						labelWidth:70,
						labelAlign:"right",
						hideLabels:false,
						items:[{
									xtype:'hidden',
									name:'user.id',
									id:'user.id'
								}, {
									fieldLabel:'登录账号',
									name:'user.loginId',
									id:'user.loginId',
									allowBlank:false,
									blankText:"账号不能为空"
								}, {
									fieldLabel:'用户名',
									name:'user.name',
									id:'user.name',
									allowBlank:false,
									blankText:"请填写容易理解的名字，关联员工可直接采用员工姓名"
								}, {
									fieldLabel:'登录密码',
									name:'user.password',
									id:'user.password',
									xtype:action ? "hidden":"textfield",
									allowBlank:false,
									blankText:"请设置初始密码"
									// value:Math.floor(Math.random()
								// * (999999 - 100000)+100000)
							}]
					}, {
						xtype:"panel",
						columnWidth:.50,
						title:"扩展信息(选填)",
						disabled:action == 2 ? true:false,
						layout:'form',
						defaultType:'textfield',
						labelWidth:70,
						defaults:{width:163},
						border:false,
						hideLabel:false,
						items:[{
									fieldLabel:'E-mail',
									name:'user.email',
									id:'user.email',
									vtype:'email',
									vtypeText:'邮箱格式不正确!'
								}, {
									fieldLabel:'创建时间',
									xtype:userId == undefined?'datefield':'hidden',
									readOnly:true,
									format:'Y-m-d',
									name:'user.createTime',
									id:'user.createTime',
									hidden:true,
									length:50
								},person,{
									id:'user.status',
									fieldLabel:'状态',
									hiddenName:'user.state',
									xtype:'combo',
									mode:'local',
									editable:false,
									triggerAction:'all',
									store:[['enabled', '可用'],	['disabled', '禁用']],
									value:'enabled'
								}]
					}]
		}, {
			xtype:'panel',
			title:'用户角色',
			width:600,
			height:220,
			colspan:2,
			items:[{
				xtype:'itemselector',
				id:'AppUserRoles',
				name:'selectedRoles',
				fromLegend:'',
				imagePath:__ctxPath+'/extjs/ux/images/',
				multiselects:[{
							id:'chooseRoles',
							title:'可选角色',
							width:247,
							height:190,
							store:new Ext.data.SimpleStore({
								autoLoad:true,
								baseParams:{id:userId},
								url:__ctxPath+'/user/selectableRoles.do',
								fields:['roleId', 'roleName']
							}),
							displayField:'roleName',
							valueField:'roleId'
						}, {
							id:'selectedRoles',
							title:'已有角色',
							width:247,
							height:190,
							store:new Ext.data.SimpleStore({
										autoLoad:true,
										baseParams:{id:userId},
										url:__ctxPath+'/user/selectedRoles.do',
										fields:['roleId','roleName']
									}),
							tbar:[{
								text:'清除所选',
								handler:function() {
									Ext.getCmp('OECP.user.UserWin').getForm().findField('AppUserRoles').reset();
								}
							}],
							displayField:'roleName',
							valueField:'roleId'
						}]

			}]

		}]
	});

	// 加载数据
	if (action) {
		userform.getForm().load({
			deferredRender:false,
			url:__ctxPath+'/user/find.do',
			params:{id:userId},
			waitMsg:"正在载入数据...",
			method:'GET',
			success:function(action,rsponse){
				var _d=rsponse.result.data,_s=person.store;
				var _r=new _s.recordType({id:_d['user.personId'],name:_d['user.personName']});
				_s.removeAll();
				_s.add(_r);
				person.setValue(_d['user.personId']);
			},
			failure:function(c, d) {
				Ext.ux.Toast.msg("信息", "数据载入失败！");
			}
		});
	}

	var win = new Ext.Window({
		id:"LoginWin",
		title:_title,
		iconCls:"login-icon",
		bodyStyle:"background-color: white",
		border:true,
		closable:true,
		resizable:true,
		modal:true,
		buttonAlign:"center",
		height:500,
		width:700,
		layout:{type:"vbox",align:"stretch"	},
		items:[userform],
		buttons:[new Ext.Button({
					text:'保存',
					iconCls:'btn-save',
					handler:function() {
						var userform = Ext.getCmp('OECP.user.UserWin');
						userform.getForm().submit({
							waitMsg:'正在提交用户信息',
							success:function(o) {
								Ext.Msg.alert('操作信息', '保存成功！');
								var userview = Ext.getCmp('OECP.user.UserGrid');
								if (userview != null) {// 假如员工列表不为空,则重载数据
									userview.getStore().reload({start:0,limit:25});
								}
								win.close();
							},
							failure:function(form, action) {
								var f = Ext.util.JSON.decode(action.response.responseText);
								Ext.MessageBox.show({
											title:"警告",
											msg:f.result,
											buttons:Ext.MessageBox.OK,
											icon:Ext.MessageBox.ERROR
										});
							}
						});
					}
				}), new Ext.Button({
					text:'关闭',
					handler:function() {
						win.close();
					}
				})]
	});
	return win;
};
/**
 * 员工餐选框
 * @class OECP.user.PersonCombo
 * @extends Ext.form.ComboBox
 */
OECP.user.PersonCombo=Ext.extend(Ext.form.ComboBox,{
	valueField:'id',displayField:'name',mode:'local',editable:false,triggerAction:'all',
	store:{xtype:'jsonstore',autoload:false,fields:['id','name']},
	onTriggerClick:function(){
		if (this.readOnly || this.disabled)return;
		if(!this.refWin) this.initRefWin();
		this.refWin.show();
	},
	//private 初始化弹出框
	initRefWin:function(){
		var me=this,orgCombo=new OECP.core.OrgComboBox({width:130,fieldLabel:'组织机构'});
		orgCombo.tree.on('click',function(node){
			deptTreePanel.loader.baseParams['orgid']=node.id;
			deptTreePanel.root.reload();
			gp.store.removeAll();
		});
		var deptTreePanel = new Ext.tree.TreePanel({title:'部门列表',autoScroll:true,rootVisible:false,split:true,width:200,height:440,
			lines:true,root:new Ext.tree.AsyncTreeNode({id:"dept_root",text:"所有部门"}),
			loader:new Ext.tree.TreeLoader({dataUrl:__ctxPath+"/dept/depttree.do"}),
			listeners:{
				"click":function(node) {	
					gp.store.removeAll();
					gp.store.baseParams={deptId:node.id,limit:25};
					gp.store.load();
				}
			}
		});
		var _g=Ext.grid,sm=new _g.CheckboxSelectionModel({singleSelect:true});
		var ps=new Ext.data.JsonStore({url:__ctxPath+"/person/list.do",root:"result",totalProperty:'totalCounts',fields:['id','no','name','email','mobile','post.name']});
		var selectPerson=function(){
			var rs=gp.getSelectionModel().getSelections();
			if(rs && rs.length>0){
				var _id=rs[0].get('id');_name=rs[0].get('name');
				var _r=new me.store.recordType({id:_id,name:_name});
				me.store.removeAll();
				me.store.add(_r);
				me.setValue(_id);
			}
			me.refWin[me.refWin.closeAction]();
		};
		gp=new Ext.grid.GridPanel({
			region:'center',autoScroll:true,sm:sm,store:ps,
			cm:new _g.ColumnModel([sm,new _g.RowNumberer(),{header:"人员编号",dataIndex:"no",width:90},
				{header:"人员姓名",dataIndex:"name",width:100},{header:"邮箱",dataIndex:"email",width:100},
				{header:"手机号码",dataIndex:"mobile",width:100},{header:"任职岗位",dataIndex:"post.name",width:100}
			]),
			bbar:new Ext.PagingToolbar({pageSize:25,store:ps,displayInfo:true,displayMsg:'当前显示 {0}-{1}条记录 /共{2}条记录',emptyMsg:"无显示数据"}),
			listeners:{
				'rowdblclick':selectPerson
			}
		});
		this.refWin=new Ext.Window({
			title:'员工档案',height:500,width:760,modal:true,layout:'border',closeAction:'hide',
			items:[{xtype:'form',labelWidth:60,width:200,region:'west',items:[orgCombo,deptTreePanel]},gp],
			buttons:[{text:'确定',handler:selectPerson},	{text:'关闭',handler:function(){me.refWin[me.refWin.closeAction]();}}]
		})
	},
	//private
	onDestroy:function(){
		if(this.refWin) Ext.destroy(this.refWin);
		Ext.form.ComboBox.superclass.onDestroy.call(this);
	}
});

// 初始化操作菜单
// OECP.user.UserWin.prototype.initFooterToolbar = function(userId) {
//
//	var toolbar = new Ext.Toolbar({
//				id:'AppUserFormToolbar',
//				width:'100%',
//				height:30,
//				items:[{
//					text:'保存',
//					iconCls:'btn-save',
//					handler:function() {
//						var userform = Ext.getCmp('OECP.user.UserWin');
//						userform.getForm().submit({
//							waitMsg:'正在提交用户信息',
//							success:function(userform, o) {
//								Ext.Msg.alert('操作信息', '保存成功！');
//								var userview = Ext.getCmp('OECP.user.UserGrid');
//								if (userview != null) {// 假如员工列表不为空,则重载数据
//									userview.getStore().reload({
//												start:0,
//												limit:25
//											});
//								}
//							},
//							failure:function(form, action) {
//								var f = Ext.util.JSON
//										.decode(action.response.responseText);
//								Ext.MessageBox.show({
//											title:"警告",
//											msg:f.result,
//											buttons:Ext.MessageBox.OK,
//											icon:Ext.MessageBox.ERROR
//										});
//							}
//						});
//					}
//
//				}, '-', {
//					text:'关联员工',
//					handler:function() {
//						// 此处弹出关联员工的参选框
//					}
//
//				}]
//			});
//	return toolbar;
//};
