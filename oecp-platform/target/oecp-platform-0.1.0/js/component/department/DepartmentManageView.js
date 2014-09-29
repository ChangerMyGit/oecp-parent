// 部门管理界面
Ext.ns("OECP.department");
/**
 * @author liujingtao
 * @class DepartmentManageView
 * @extends Ext.Panel
 */
OECP.department.DepartmentManageView = Ext.extend(Ext.Panel, {
	id : 'OECP.department.DepartmentManageView',
	title : '部门管理',
	layout : 'border',
	initComponent : function() {
		var DepartmentManageView = this;
		OECP.department.DepartmentManageView.superclass.initComponent
				.call(this);
		// 部门树
		var deptTreePanel = new Ext.tree.TreePanel({
			title : '部门列表',
			animate : true,
			region : 'west',
			collapsible : true,
			margins : '0 0 0 3',
			autoScroll : true,
			rootVisible : true,
			enableDD : false,
			enableDrag : false,
			split : true,
			width : 170,
			lines : true,
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
						editBtn.setDisabled(true);
						delBtn.setDisabled(true);
					} else {
						editBtn.setDisabled(false);
						delBtn.setDisabled(false);
					}
					deptData_store.removeAll();
					deptData_store.baseParams.deptid = node.id;
					deptData_store.load();
				}
			}
		});
		// 部门资料
		var deptData_store = new Ext.data.JsonStore({
					url : __ctxPath + "/dept/deptInfo.do",
					baseParams : {
						deptid : ""
					},
					storeId : 'id',
					fields : ['id', 'name', 'code', 'parent.id','parent.name'],
					autoLoad : true
				});

		var tpl = new Ext.XTemplate('<tpl for=".">',
				'<div><span>部门编码 : </span><span>' + '{code}' + '</span></div>',
				'<div><span>部门名称 : </span><span>' + '{name}' + '</span></div>',
				'</tpl>');

		var dataview = new Ext.DataView({
					store : deptData_store,
					tpl : tpl,
					autoHeight : true,
					width : 200,
					emptyText : ''
				});

		var addBtn = new Ext.Button({
					text : "新增",
					listeners : {
						'click' : function() {
							deptWindow.show();
							deptWindow.setTitle('新增');
							if (deptData_store.data.length > 0) {
								var deptDataJson = deptData_store.data.items[0].data;
								initTreeComb(deptDataJson);
								deptWindow.setFormData({
											'parent.id' : deptDataJson['id'],
											'parent.name' : deptDataJson['name']
										});
							}
						}
					}
				});
		var editBtn = new Ext.Button({
					text : "编辑",
					disabled : true,
					listeners : {
						'click' : function() {
							deptWindow.show();
							deptWindow.setTitle('编辑');
							var deptDataJson = deptData_store.data.items[0].data;
							initTreeComb(deptDataJson);
							deptWindow.setFormData(deptDataJson);
						}
					}
				});
		var delBtn = new Ext.Button({
			text : "删除",
			disabled : true,
			listeners : {
				'click' : function del() {
				Ext.Msg.confirm("信息确认", "您确认要删除数据吗？", function(b) {
				if (b == 'yes') {
					Ext.Ajax.request({
								url : __ctxPath + "/dept/deleteDept.do",
								success : function(res) {
									var msg = eval("("
											+ Ext.util.Format
													.trim(res.responseText)
											+ ")");
									if (msg.success) {
										Ext.ux.Toast.msg("信息", msg.msg);
										deptTreePanel.root.reload();
										deptData_store.removeAll();
									} else {
										Ext.Msg.show({
													title : "错误",
													msg : msg.msg,
													buttons : Ext.Msg.OK
												})
									}
								},
								failure : function() {
									Ext.Msg.show({
												title : "错误",
												msg : msg.result.msg,
												buttons : Ext.Msg.OK
											})
								},
								params : {
									deptid : deptData_store.data.items[0].data['id']
								}
							});
						}
					});
				}
			}
		});
		
		function initTreeComb(d){
			var data = {};
			data[deptcomboBox.valueField] = d['parent.id'];
			data[deptcomboBox.displayField] = d['parent.name'];
			var record = new RecordType(data);
			deptcomboBox.getStore().insert(0, record);
		}
		// 详细信息显示
		var deptDataPanel = new Ext.Panel({
					title : '详细信息',
					region : 'center',
					margins : '0 0 0 3',
					enableDD : false,
					enableDrag : false,
					autoScroll : true,
					rootVisible : false,
					items : dataview,
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
		var deptcomboBox = new OECP.core.DeptComboBox({
			hiddenName : 'dept.parent.id',
			displayField : 'text',
			valueField : 'value',
			dataIndex : 'parent.id',
			fieldLabel : '上级部门',
			width: 160,
			editable: true,	
			mapping : 'parent.id'
		});
		var RecordType = Ext.data.Record.create([{
			name : deptcomboBox.valueField
		}, {
			name : deptcomboBox.displayField
		}]);
		// 新增、编辑窗口
		var deptWindow = new OECP.fun.FormWindow({
					formItems : [{
								dataIndex : 'id',
								fieldLabel : 'id',
								name : 'dept.id',
								mapping : 'id',
								hidden : true
							}, {
								dataIndex : 'code',
								fieldLabel : '部门编码',
								name : 'dept.code',
								mapping : 'code',
								allowBlank : false,
								maxLength : 20,
								minLength : 2
							}, {
								dataIndex : 'name',
								fieldLabel : '部门名称',
								name : 'dept.name',
								mapping : 'name',
								allowBlank : false,
								maxLength : 20,
								minLength : 2
							}, deptcomboBox],
					saveURL : __ctxPath + '/dept/saveDept.do'
				});
		
		deptWindow.on('dataSaved', function(id) {
					deptTreePanel.root.reload();
				});
		this.add(deptTreePanel);
		this.add(deptDataPanel);
		this.add(panStatePanel);
	},
	onDestory:function(){
		if(win){
			Ext.destroy(win);
		}
		OECP.department.DepartmentManageView.superclass.onDestory.call(this);
	}
});
