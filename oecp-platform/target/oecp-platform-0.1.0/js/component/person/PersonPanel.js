/**  
 * @include "../../../ext-oecp/ui/Button.js"  
 * @include "../../../ext-oecp/ui/ToftPanel.js"  
 * @include "../../../ext-oecp/ui/FormWindow.js"  
 */
Ext.ns("OECP.Person")
/**
 * 员工管理面板
 * @class OECP.Person.PersonPanel
 * @extends Ext.Panel
 */
OECP.Person.PersonPanel = Ext.extend(Ext.Panel ,{
	/**
	 * boder布局的数据区域
	 * @type String
	 */
	region	:	'center',
	/**
	 * border布局
	 * @type String
	 */
	layout	:	'border',
	/**
	 * 部门树panel
	 * @type 
	 */
	deptTreePanel : undefined,
	/**
	 * 部门树加载数据URL
	 * @type 
	 */
	deptTreeUrl : __ctxPath + "/dept/depttree.do",
	/**
	 * 选中的部门表示
	 * @type 
	 */
	selectedDeptId : undefined,
	/**
	 * 数据区域panel
	 * @type 
	 */
	centerPanel : undefined,
	/**
	 * 数据区域panel中的主Grid信息
	 * @type 
	 */
	parentGrid : undefined,
	addBtn : undefined,
	editBtn : undefined,
	delBtn : undefined,
	parentUrl : __ctxPath + '/person/list.do',
	deleteUrl : __ctxPath + '/person/delete.do',
	parentGridField : ['id','no','name','email','mobile','post.name'],
	pageNum : 10,
	/**
	 * 数据区域panel中的从Grid信息
	 * @type 
	 */
	childGrid : undefined,
	childUrl : __ctxPath + '/person/loadOtherPosts.do',
	childGridField : ['id','code','name','charge','parent.id','parent.name', 'dept.id','dept.name'],
	/**
	 * 初始化方法
	 */
	initComponent : function(){
		var THIS = this;
		OECP.Person.PersonPanel.superclass.initComponent.call(this);
		this.initWestContent(THIS);
		this.initCenterContent(THIS);
	},
	/**
	 * 初始化west内容
	 * @param {} THIS
	 */
	initWestContent : function(THIS){
		if(!Ext.isDefined(THIS.deptTreePanel)){
			THIS.deptTreePanel =  new Ext.tree.TreePanel({
				title : '部门列表',
				animate : true,
				region : 'west',
				collapsible : true,// 面板是否可收缩
				margins : '0 0 0 3',
				autoScroll : true,
				rootVisible : false,
				enableDD : false,
				enableDrag : false,
				split : true,
				width : 170,
				lines : true,
				deptid : '1',
				deptname : '2',
				root : new Ext.tree.AsyncTreeNode({
							id : "dept_root",
							text : "所有部门"
						}),
	
				loader : new Ext.tree.TreeLoader({
							dataUrl : THIS.deptTreeUrl,
							createNode : function(attr){
								attr.icon = __ctxPath+'/images/menus/personal/personal.png';
						        if(this.baseAttrs){
						            Ext.applyIf(attr, this.baseAttrs);
						        }
						        if(this.applyLoader !== false && !attr.loader){
						            attr.loader = this;
						        }
						        if(Ext.isString(attr.uiProvider)){
						           attr.uiProvider = this.uiProviders[attr.uiProvider] || eval(attr.uiProvider);
						        }
						        if(attr.nodeType){
						            return new Ext.tree.TreePanel.nodeTypes[attr.nodeType](attr);
						        }else{
						            return attr.leaf ?
						                        new Ext.tree.TreeNode(attr) :
						                        new Ext.tree.AsyncTreeNode(attr);
						        }
						    }
						}),
				listeners : {
					"click" : function(node) {
						if (node.id == "dept_root") {
							THIS.addBtn.setDisabled(true);
							THIS.editBtn.setDisabled(true);
							THIS.delBtn.setDisabled(true);
						} else {
							THIS.addBtn.setDisabled(false);
							THIS.editBtn.setDisabled(false);
							THIS.delBtn.setDisabled(false);
						}
						THIS.selectedDeptId = node.id;
						THIS.childGrid.getStore().removeAll();
						THIS.parentGrid.getStore().removeAll();
						THIS.parentGrid.getStore().baseParams.deptId = node.id;
						THIS.parentGrid.getStore().baseParams.limit = THIS.pageNum;
						THIS.parentGrid.getStore().load();
					}
				}
			});
			THIS.add(THIS.deptTreePanel);
		}
	},
	/**
	 * 初始化center内容
	 * @param {} THIS
	 */
	initCenterContent : function(THIS){
		if(!Ext.isDefined(THIS.centerPanel)){
			if(!Ext.isDefined(THIS.parentGrid)){
				THIS.initBtns(THIS);
				var sm = new Ext.grid.CheckboxSelectionModel();
				var parentStore = new Ext.data.JsonStore({
					url	: THIS.parentUrl,
					root : "result",
					fields : THIS.parentGridField,
					totalProperty : 'totalCounts'
				});
				THIS.parentGrid = new Ext.grid.GridPanel({
					region	: 'center',
					height : 200,
					sm : sm,
					columns : [sm,{dataIndex : "id",hidden:true},
					   {header : "人员编号",dataIndex : "no",width:90},
					   {header : "人员姓名",dataIndex : "name",width:100},
					   {header : "邮箱",dataIndex : "email",width:100},
					   {header : "手机号码",dataIndex : "mobile",width:100},
					   {header : "任职岗位",dataIndex : "post.name",width:100}],
					store	:  parentStore,
					bbar : new Ext.PagingToolbar({
						pageSize : THIS.pageNum,
						store : parentStore,
						displayInfo : true,
						displayMsg : '当前显示 {0}-{1}条记录 /共{2}条记录',
						emptyMsg : "无显示数据"
					}),
					tbar : [THIS.addBtn,THIS.editBtn,THIS.delBtn]
				});
				
				THIS.parentGrid.store.on('load',function(){
					if(THIS.parentGrid.store.getCount()>0){
						THIS.parentGrid.selModel.selectRow(0,true);
					}
				});
				
				THIS.parentGrid.getSelectionModel().on('rowselect',function(){
					var data = THIS.parentGrid.getSelectionModel().getSelected();
			        THIS.childGrid.getStore().removeAll();
			        THIS.childGrid.getStore().baseParams = {"personId":data.get("id")};
					THIS.childGrid.getStore().load();
				});
			}
			if(!Ext.isDefined(THIS.childGrid)){
				THIS.childGrid = new Ext.grid.GridPanel({
					region	: 'south',
					height : 200,
					columns : [{dataIndex : "id",hidden:true},
							   {header : "岗位编码",dataIndex : "code",width:90},
							   {header : "岗位名称",dataIndex : "name",width:100},
							   {header : "是否管理岗位",dataIndex : "charge",width:100},
							   {header : "上级岗位名称",dataIndex : "parent.name"},
							   {header : "部门名称",dataIndex : "dept.name"}],
					store	:  new Ext.data.JsonStore({
						url	:	THIS.childUrl,
						storeId : 'id',
						fields : THIS.childGridField
					})
				});
			}
			THIS.centerPanel = new Ext.Panel({
				title : '员工信息',
				region : 'center',
				layout : 'border',
				autoScroll	: true,
				items : [THIS.parentGrid,THIS.childGrid]
			});
			THIS.add(THIS.centerPanel);
		}
	},
	/**
	 * 初始化按钮
	 * @param {} THIS
	 */
	initBtns : function(THIS){
		if(!Ext.isDefined(THIS.addBtn)){
			THIS.addBtn = new OECP.ui.Button({
				text : "新增",
				disabled : true,
				iconCls : 'btn-add',
				listeners : {
					'click' :function() {
						var personWindow = new OECP.Person.PersonWindow({isEdit:false,selectedDeptId:THIS.selectedDeptId,title :'新增员工'});
						personWindow.on("doSaved",function(){
							THIS.childGrid.getStore().removeAll();
							THIS.parentGrid.getStore().removeAll();
							THIS.parentGrid.getStore().baseParams.deptId = THIS.selectedDeptId;
							THIS.parentGrid.getStore().load();
						});
						personWindow.show();
					}
				}
			});
		}

		if(!Ext.isDefined(THIS.editBtn)){
			THIS.editBtn = new OECP.ui.Button({
				text : "编辑",
				disabled : true,
				iconCls : 'btn-edit',
				listeners : {
					'click' :function() {
						var records = THIS.parentGrid.getSelectionModel().getSelections();
						if(records.length==0||records.length>1){
							Ext.Msg.alert('提示','请选择一条记录进行编辑',function(){})
							return;
						}
						var personId = records[0].get("id");
						var personWindow = new OECP.Person.PersonWindow({isEdit:true,personId:personId,selectedDeptId:THIS.selectedDeptId,title :'修改员工信息'});
						personWindow.on("doSaved",function(){
							THIS.childGrid.getStore().removeAll();
							THIS.parentGrid.getStore().removeAll();
							THIS.parentGrid.getStore().baseParams.deptId = THIS.selectedDeptId;
							THIS.parentGrid.getStore().load();
						});
						personWindow.show();
					}
				}
			});
		}

		if(!Ext.isDefined(THIS.delBtn)){
			THIS.delBtn = new OECP.ui.Button({
				text : "删除",
				disabled : true,
				iconCls : 'btn-delete',
				listeners : {
					'click' :function() {
						var records = THIS.parentGrid.getSelectionModel().getSelections();
						if(records.length==0){
							Ext.Msg.alert('提示','请至少选择一条记录进行删除',function(){})
							return;
						}
						
						Ext.Msg.confirm('提示框','您确定要删除吗！',function(button){
							if(button=='yes'){
								var personIds = new Array();
								Ext.each(records,function(record){
									personIds.push(record.get("id"));
								});
								Ext.Ajax.request({
									url : THIS.deleteUrl,
									params : {
										"personIds":personIds
									},
									success : function(request) {
										var json = Ext.decode(request.responseText);
										if (json.success) {
											Ext.ux.Toast.msg('信息',	json.msg);
											THIS.childGrid.getStore().removeAll();
											THIS.parentGrid.getStore().removeAll();
											THIS.parentGrid.getStore().baseParams.deptId = THIS.selectedDeptId;
											THIS.parentGrid.getStore().load();
										} else {
											Ext.Msg.show({title:'错误',msg:json.msg,buttons:Ext.Msg.OK	});
										}
									},
									failure : function(request) {
										var json = Ext.decode(request.responseText);
										Ext.Msg.show({title:'错误',msg:json.msg,buttons:Ext.Msg.OK});
									}
								});
							}
						},this);
					}
				}
			});
		}
	}
	
})