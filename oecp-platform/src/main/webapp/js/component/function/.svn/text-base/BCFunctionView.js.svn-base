// 组件功能管理界面
Ext.ns('OECP.bc.func');
OECP.bc.func.BCFunctionView = Ext.extend(Ext.Panel, {
	id : 'OECP.bc.func.BCFunctionView',
	title : '功能注册',
	treePanel : null,
	bcId : null,
	layout : 'border',
	initComponent : function() {
		var scope = this;
		this.accordionTreePanel = new OECP.ui.AccordionTreePanel({
			width : 150,
			title : '功能注册',
			accordionUrl : __ctxPath + '/function/listAllBCs.do',
			treeUrl : __ctxPath + '/function/fuctionTreeCode.do',
			treeEvent : {
				'click' : function(node) {
					if (node.id != 'root' && node.leaf == true) {
						scope.addtab(node.id, node.text);
					}
				},
				'containercontextmenu' : function(comp, e) {
					e.preventDefault();
					var treeMenu = new Ext.menu.Menu([{
						text : '新增',
						pressed : true,
						handler : function() {
							var func = new OECP.bc.func.FunctionEditForm({
										// bcId : comp.id,
								        bcId : scope.getBcId(comp.id),
								        isEdit : true
									});
							func.show();
							func.on('saveSucceeded', function(formdata) {
										var node = comp.getSelectionModel()
												.getSelectedNode();
										if (node) {
											var path = node.getPath();
										}
										var troot = comp.root;
										troot.reload();
										if (path) {
											comp.expandPath(path);
											comp.selectPath(path);
										}
									});
						}
					}]);
					treeMenu.showAt(e.getXY());
				},
				'contextMenu' : function(node, e) {// 树右键菜单
					e.preventDefault();
					node.select();
					var depth = node.getDepth();
					var treeMenu = new Ext.menu.Menu([{
								text : '查看',
								pressed : true,
								handler : function() {
									var func = new OECP.bc.func.FunctionEditForm(
											{
												functionId : node.id,
												isEdit : false
											});
									func.show();
								}
							}, {
								text : '新增',
								pressed : true,
								disabled : node.leaf,
								handler : function() {
									var func = new OECP.bc.func.FunctionEditForm(
											{
												//bcId : node.ownerTree.id,
												bcId : scope.getBcId(node.ownerTree.id),
												parentId : node.id,
												isEdit : true
											});
									func.show();
									func.on('saveSucceeded',
											function(formdata) {
												var path = node.getPath();
												var troot = node.ownerTree.root;
												troot.reload();
												troot.ownerTree
														.expandPath(path);
												troot.ownerTree
														.selectPath(path);
											});
								}
							}, {
								text : '编辑',
								pressed : true,
								handler : function() {
									var func = new OECP.bc.func.FunctionEditForm(
											{
												// bcId : node.ownerTree.id,
												bcId : scope.getBcId(node.ownerTree.id),
												functionId : node.id,
												isEdit : true
											});
									func.show();
									func.on('saveSucceeded',
											function(formdata) {
												var path = node.getPath();
												var troot = node.ownerTree.root;
												troot.reload();
												troot.ownerTree
														.expandPath(path);
												troot.ownerTree
														.selectPath(path);
											});
								}
							}, {
								text : '删除',
								pressed : true,
								disabled : node.hasChildNodes(),
								handler : function() {
									Ext.Msg.confirm('信息确认', '您确定要删除【功能】吗？',
											function(c) {
												if (c == 'yes') {
													Ext.Ajax.request({
														url : __ctxPath
																+ '/function/deleteFunction.do',
														params : {
															id : node.id
														},
														success : function(res) {
															var msg = Ext.util.JSON
																	.decode(res.responseText);
															if (msg.success) {
																Ext.ux.Toast
																		.msg(
																				'信息',
																				msg.msg);
																node.remove();
															} else {
																Ext.Msg.show({
																	title : '错误',
																	msg : msg.msg,
																	buttons : Ext.Msg.OK
																});
															}
														},
														failure : function() {
															Ext.Msg.show({
																title : '错误',
																msg : msg.result.msg,
																buttons : Ext.Msg.OK
															});
														}
													});
												}
											});
								}
							}]);
					treeMenu.showAt(e.getXY());
				}
			}
		});
		this.functionUIGridPanel = new OECP.bc.func.FunctionUIGridPanel({
					region : 'center',
					title : '功能界面注册'
				});
		this.items = [this.accordionTreePanel, this.functionUIGridPanel];
		OECP.bc.func.BCFunctionView.superclass.initComponent.call(this);
	},
	addtab : function(id, text) {
		this.functionUIGridPanel.loadUIData(id);
	},
	getBcId : function(idStr){
		var bdId = idStr.split("_").pop();
		return bdId;
	}
});
