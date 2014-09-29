// 岗位管理界面
Ext.ns("OECP.post");
/**
 * @author liujingtao
 * @class PostManageView
 * @extends Ext.Panel
 */
OECP.post.PostManageView = Ext.extend(Ext.Panel, {
	id : 'OECP.post.PostManageView',
	title : '岗位管理',
	layout : 'border',
	initComponent : function() {
		var PostManageView = this;
		OECP.post.PostManageView.superclass.initComponent.call(this);

		// 部门树
		var deptTreePanel = new Ext.tree.TreePanel({
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
						dataUrl : __ctxPath + "/dept/depttree.do",
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
						addBtn.setDisabled(true);
						editBtn.setDisabled(true);
						delBtn.setDisabled(true);
					} else {
						addBtn.setDisabled(false);
						editBtn.setDisabled(false);
						delBtn.setDisabled(false);
					}
					postData_store.removeAll();
					postData_store.baseParams.deptid = node.id;
					deptTreePanel.deptid = node.id;
					deptTreePanel.deptname = node.text;
					postData_store.load();
				}
			}
		});
		// 岗位资料
		var postData_store = new Ext.data.JsonStore({
					url : __ctxPath + "/post/getPostList.do",
					baseParams : {
						deptid : ""
					},
					storeId : 'id',
					fields : ['id', 'name', 'code', 'charge', 'parent.id',
							'parent.name', 'dept.id', 'dept.name'],
					autoLoad : true
				});

		var sm = new Ext.grid.CheckboxSelectionModel();// 选择器

		var postData_cm = new Ext.grid.ColumnModel([sm,
				new Ext.grid.RowNumberer(), {
					header : "主键",
					dataIndex : "id",
					hidden : true
				}, {
					header : "岗位名称",
					dataIndex : "name"
				}, {
					header : "岗位编码",
					dataIndex : "code"
				}, {
					header : "是否管理岗位",
					dataIndex : "charge"
				}, {
					header : "上级岗位主键",
					dataIndex : "parent.id",
					hidden : true
				}, {
					header : "上级岗位名称",
					dataIndex : "parent.name"
				}, {
					header : "所属部门主键",
					dataIndex : "dept.id",
					hidden : true
				}]);

		var addBtn = new Ext.Button({
					text : "新增",
					disabled : true,
					listeners : {
						'click' : function() {
							PostManageView.postWindow.show();
							PostManageView.postWindow.setTitle('新增');
							// 填充所属部门comboBox对应store
							var RecordType = Ext.data.Record.create([{
										name : deptcomboBox.valueField
									}, {
										name : deptcomboBox.displayField
									}]);
							var data = {};
							data[deptcomboBox.valueField] = deptTreePanel.deptid;
							data[deptcomboBox.displayField] = deptTreePanel.deptname;
							var record = new RecordType(data);
							deptcomboBox.getStore().insert(0, record);

							PostManageView.postWindow.setFormData({
										'dept.id' : deptTreePanel.deptid
									});
						}
					}
				});

		var editBtn = new Ext.Button({
			text : "编辑",
			disabled : true,
			listeners : {
				'click' : function() {
					if (sm.getCount() == 0) {
						Ext.ux.Toast.msg("信息", "请选择一条记录！");
					} else if (sm.getCount() > 1) {
						Ext.ux.Toast.msg("信息", "只能编辑一条记录！");
					} else {
						var b = postDataPanel.getSelectionModel()
								.getSelections();
						PostManageView.postWindow.show();
						PostManageView.postWindow.setTitle('编辑');
						// 填充所属部门comboBox对应store
						var RecordType = Ext.data.Record.create([{
									name : deptcomboBox.valueField
								}, {
									name : deptcomboBox.displayField
								}]);
						var data = {};
						data[deptcomboBox.valueField] = b[0].data['dept.id'];
						data[deptcomboBox.displayField] = b[0].data['dept.name'];
						var record = new RecordType(data);
						deptcomboBox.getStore().insert(0, record);
						// 上级岗位列表postid
						ppostcomboBox.store = postData_store;
						ppostcomboBox.postid = b[0].data['id'];
						// 填充详细信息
						PostManageView.postWindow.setFormData(b[0].data);
					}
				}
			}
		});

		var delBtn = new Ext.Button({
					text : "删除",
					disabled : true,
					listeners : {
						'click' : function del() {
							if (sm.getCount() == 0) {
								Ext.ux.Toast.msg("信息", "请选择要删除的记录！");
								return;
							}
							var b = postDataPanel.getSelectionModel()
									.getSelections();
							var e = [];
							for (var i = 0; i < b.length; i++) {
								e.push(b[i].data.id);
							}
							Ext.Ajax.request({
										url : __ctxPath + "/post/deletePost.do",
										success : function(res) {
											var msg = eval("("
													+ Ext.util.Format
															.trim(res.responseText)
													+ ")");
											if (msg.success) {
												Ext.ux.Toast.msg("信息", msg.msg);
												postData_store.load();
											} else {
												Ext.Msg.show({
															title : "错误",
															msg : msg.msg,
															buttons : Ext.Msg.OK
														});
											}
										},
										failure : function() {
											Ext.Msg.show({
														title : "错误",
														msg : msg.result.msg,
														buttons : Ext.Msg.OK
													});
										},
										params : {
											postids : e
										}
									});

						}
					}
				});
		// 详细信息显示
		var postDataPanel = new Ext.grid.GridPanel({
					title : '详细信息',
					region : 'center',
					margins : '0 0 0 3',
					enableDD : false,
					enableDrag : false,
					autoScroll : true,
					sm : sm,
					cm : postData_cm,
					store : postData_store,

					tbar : [addBtn, editBtn, delBtn]
				});
		// 空白部分
		var panStatePanel = new Ext.Panel({
					height : 23,
					layout : 'fit',
					region : 'south',
					title : '&nbsp;',
					baseCls : 'x-plain'
				});

		// 下拉部门树(选择所属部门)
		var deptcomboBox = new OECP.core.DeptComboBox({
					hiddenName : 'post.dept.id',
					displayField : 'text',
					valueField : 'value',
					dataIndex : 'dept.id',
					fieldLabel : '所属部门',
					width: 160,
					mapping : 'dept.id'
				});
		// 上级岗位选择
		var ppostcomboBox = new OECP.post.ParentPostSelectComboBox({
					hiddenName : 'post.parent.id',
					displayField : 'name',
					valueField : 'id',
					dataIndex : 'parent.id',
					fieldLabel : '上级岗位',
					mapping : 'parent.id',
					width: 160,
					postid : ''
					
				});
		// 新增、编辑窗口
		PostManageView.postWindow = new OECP.fun.FormWindow({
					formItems : [{
								dataIndex : 'id',
								fieldLabel : 'id',
								name : 'post.id',
								mapping : 'id',
								hidden : true
							}, {
								dataIndex : 'code',
								fieldLabel : '岗位编码',
								name : 'post.code',
								mapping : 'code',
								width: 160,
								allowBlank : false,
								maxLength : 20,
								minLength : 2
							}, {
								dataIndex : 'name',
								fieldLabel : '岗位名称',
								name : 'post.name',
								mapping : 'name',
								width: 160,
								allowBlank : false,
								maxLength : 20,
								minLength : 2
							}, {
								dataIndex : 'charge',
								xtype : 'combo',
								fieldLabel : '是否管理岗位',
								hiddenName : 'post.charge',
								mapping : 'charge',
								allowBlank : false,
								value : false,
								valueField : 'value',
								displayField : 'text',
								triggerAction : "all",// 不添加不显示所有下拉
								mode : "charge",
								width: 160,
								store : [[true, "是"], [false, "否"]]
							}, ppostcomboBox, deptcomboBox],
					saveURL : __ctxPath + '/post/savePost.do'
				});

		PostManageView.postWindow.on('dataSaved', function(id) {
					postData_store.load();
				});
		this.add(deptTreePanel);
		this.add(postDataPanel);
		this.add(panStatePanel);
	},
	onDestroy:function(){
		if(this.postWindow){
			Ext.destroy(this.postWindow);
		}
		OECP.post.PostManageView.superclass.onDestroy.call(this);
	}
});
