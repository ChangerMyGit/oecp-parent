Ext.ns('OECP.role');

OECP.role.RoleView = Ext.extend(OECP.ui.TreeGridView, {
	title : '角色管理',
	id:'OECP.role.RoleView',
	treePanelTitle:'组织机构目录',
	treeDataUrl : __ctxPath + '/role/orgsTree.do',
	queryRecordUrl : __ctxPath + '/role/queryRoleByOrgId.do',
	showCheckBox : true,
	showRowNum : true,
	treeNodeIcon : __ctxPath + '/images/menus/personal/holiday.png',
	gridItems : [{
				header : "主键",
				dataIndex : "id",
				hidden : true
			}, {
				header : "编码",
				dataIndex : "code"
			}, {
				header : "名称",
				dataIndex : "name"
			}, {
				header : "创建组织名称",
				dataIndex : "createOrgName"
			}, {
				header : "是否锁定",
				dataIndex : "locked",
				renderer : function(k, j, g, i, l) {
					var lockflag = g.data.locked;
					var h = "";
					if (lockflag) {
						h += "<font color='red'>锁定</font>";
					} else {
						h += "启用";
					}
					return h;
				}
			}, {
				header : "isEdit",
				dataIndex : "isEdit",
				type : "boolean",
				hidden : true
			}, {
				header : "orgRoleId",
				dataIndex : "orgRoleId",
				hidden : true
			}],
	gridStoreFields : [{
				name : "id",
				type : "string"
			}, {
				name : "code",
				type : "string"
			}, {
				name : "name",
				type : "string"
			}, {
				name : "locked",
				type : "boolean"
			}, {
				name : "isEdit",
				type : "boolean"
			}, {
				name : "orgRoleId",
				type : "string"
			}, {
				name : "createOrgName",
				type : "string"
			}],
	initComponent : function() {
		var scope = this;

		this.addBtn = new Ext.Button({
					text : '增加',
					iconCls : 'x-btn-add-trigger',
					listeners : {
						'click' : function(btn, e) {
							scope.showRoleWindow(false);
						}
					}
				});
		this.editBtn = new Ext.Button({
					text : "编辑",
					iconCls : 'x-btn-edit-trigger',
					listeners : {
						'click' : function(btn, e) {
							scope.showRoleWindow(true);
						}
					}
				});
		this.delBtn = new Ext.Button({
			text : "删除",
			iconCls : 'x-btn-del-trigger',
			listeners : {
				'click' : function(btn, e) {
					var d = scope.grid;
					var b = d.getSelectionModel().getSelections();
					if (b.length == 0) {
						Ext.ux.Toast.msg("信息", "请选择要删除的记录！");
						return;
					}
					var e = [];
					for (var i = 0; i < b.length; i++) {
						e.push(b[i].data.id);
					}
					var a = false;
					Ext.Msg.confirm("信息确认", "您确认要删除所选记录吗？", function(b) {
						if (b == "yes") {
							Ext.Ajax.request({
										method : 'POST',
										url : __ctxPath + "/role/delRole.do",
										params : {
											ids : e
										},
										method : "POST",
										success : function(request) {
											var json = Ext.util.JSON
													.decode(request.responseText);
											var result = json.msg;
											Ext.ux.Toast.msg("信息", result);
											d.getStore().reload();
										},
										failure : function(request) {
											var message = request.responseText;
											Ext.ux.Toast.msg("信息", message);
										}
									});
						}
					});
				}
			}
		});
		this.distriBtn = new Ext.Button({
					text : '分配',
					listeners : {
						'click' : function(btn, e) {
							var record = scope.selected();
							if (record.length == 0) {
								Ext.ux.Toast.msg("信息", "请选择要编辑的记录！");
								return;
							}
							if (record.length > 1) {
								Ext.ux.Toast.msg("信息", "请选择一条记录！");
								return;
							}
							var treeWindow = new OECP.ui.CheckboxTreeWindow({
										paramId : record[0].data.id,
										treeDataUrl : "/role/lowerOrgsTree.do",
										selectedDataUrl : "/role/getHasRoleOrgs.do",
										saveDataUrl : "/role/saveOrgRole.do",
										checkedOrgId : scope.currentNodeId
									});
							treeWindow.show();
							treeWindow.on('checboxTreeSave', function() {
										treeWindow.destroy();
										scope.grid.getStore().reload();
									});
						}
					}
				});
		this.appointBtn = new Ext.Button({
			text : '委派',
			listeners : {
				'click' : function(btn, e) {
					var record = scope.selected();
					if (record.length == 0) {
						Ext.ux.Toast.msg("信息", "请选择要编辑的记录！");
						return;
					}
					if (record.length > 1) {
						Ext.ux.Toast.msg("信息", "请选择一条记录！");
						return;
					}
					var roleAppointWindow = new OECP.role.RoleAppointWindow({
								hasRoleUserUrl : '/role/getHasRoleUsers.do',
								notHasRoleUserUrl : '/role/getNotHasRoleUsers.do',
								orgRoleId : record[0].data.orgRoleId
							});
					roleAppointWindow.show();
				}
			}
		});
		scope.gridBtns = ['-', this.addBtn, '-', this.editBtn, '-',
		                  this.delBtn, '-', this.distriBtn, '-', this.appointBtn, '-'];
		OECP.role.RoleView.superclass.initComponent.call(this);
		/** 自定义事件 */
		// 显示角色编辑窗体
		scope.showRoleWindow = function(isEdit) {
			if (!Ext.isDefined(scope.roleWindow)) {
				scope.roleWindow = new OECP.ui.comp.OECPFormWindow({
							id : 'addRole_window',
							formItems : [{
										id : 'id',
										fieldLabel : 'id',
										name : 'role.id',
										mapping : 'id',
										hidden : true
									}, {
										id : 'code',
										fieldLabel : '编码',
										name : 'role.code',
										mapping : 'code',
										allowBlank : false,
										maxLength : 20,
										minLength : 4
									}, {
										id : 'name',
										fieldLabel : '名称',
										name : 'role.name',
										mapping : 'name',
										allowBlank : false,
										maxLength : 20,
										minLength : 4
									}, {
										id : 'locked',
										xtype : 'combo',
										fieldLabel : '是否锁定',
										name : 'role.locked',
										mapping : 'locked',
										allowBlank : false,
										value : false,
										triggerAction : "all",// 不添加不显示所有下拉
										mode : "local",
										store : [[false, "启用"], [true, "锁定"]]
									}, {
										id : 'orgRoleId',
										fieldLabel : 'orgRoleId',
										name : 'orgRoleId',
										mapping : 'orgRoleId',
										hidden : true
									}],
							saveURL : __ctxPath + '/role/saveRole.do'
						});
				// 保存事件
				scope.roleWindow.on('dataSaved', function(id) {
							scope.reloadGrid();
						});
			}
			if (isEdit) {
				// 编辑时 判断是否未选择或选择多条记录
				var record = scope.selected();
				if (record.length == 0) {
					Ext.ux.Toast.msg("信息", "请选择要编辑的记录！");
					return;
				}
				if (record.length > 1) {
					Ext.ux.Toast.msg("信息", "请选择一条记录！");
					return;
				}
				if (!record[0].data.isEdit) {
					Ext.ux.Toast.msg("信息", "非本公司创建，不能修改！");
					return;
				}
				scope.roleWindow.setFormData(record[0].data);
			} else {
				scope.roleWindow.setFormData(null);
			}
			scope.roleWindow.show();
		};
		// 刷新数据
		scope.reloadGrid = function() {
			scope.grid.getStore().reload();
		};
		scope.grid.on('rowclick', function(f, d, g) {
					f.getSelectionModel().each(function(i) {
								// 判断数据是否可以编辑
								if (i.data.isEdit) {
									scope.editBtn.enable();
								} else {
									scope.editBtn.disable();
								}
							});
					scope.delBtn.enable();
					var tmp_store = f.getSelectionModel().getSelections();
					for (var i = 0; i < tmp_store.length; i++) {
						if (!tmp_store[i].data.isEdit) {
							scope.delBtn.disable();
						}
					}

				});
	},
	// private
	onDestory : function() {
		Ext.destroy(this.roleWindow);
	}
});