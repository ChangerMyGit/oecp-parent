Ext.ns('OECP.ui');
/**
 * 单选框的树
 * 
 * @class OECP.ui..Tree2TreeWindow
 * @extends Ext.Window
 */
OECP.ui.CheckboxTreeWindow = Ext.extend(Ext.Window, {
	title : '选择',
	width : 250,
	height : 350,
	// autoHeight : true,
	plain : true,
	modal : true,
	layout : "fit",
	treeTitle : '',
	selectedDataUrl : '',
	saveDataUrl : '',
	treeDataUrl : '',
	paramId : '',// 参数Id
	checkedOrgId : '',// 选择的公司
	initFlag : false,
	initComponent : function() {
		var CheckboxTreeWindow = this;
		this.addEvents('checboxTreeSave');
		var tree = new Ext.tree.TreePanel({
					id : 'CheckboxTree',
					title : CheckboxTreeWindow.treeTitle,
					height : 300,
					width : 400,
					useArrows : true,
					autoScroll : true,
					animate : true,
					enableDD : true,
					containerScroll : true,
					rootVisible : false,
					frame : true,
					root : {
						nodeType : 'async',
						id : "source"
					},
					loader : new Ext.tree.TreeLoader({
								dataUrl : __ctxPath
										+ CheckboxTreeWindow.treeDataUrl,
								baseParams : {
									id : CheckboxTreeWindow.checkedOrgId
								}
							}),
					buttons : [{
						text : '保存',
						handler : function() {
							var selNodes = tree.getChecked();
							OECP.ui.CheckboxTreeWindow.allNodeValue = '';
							var msg = OECP.ui.CheckboxTreeWindow
									.getAllRoot(tree);
							// ajax 提交
							Ext.Ajax.request({
										method : 'POST',
										url : __ctxPath
												+ CheckboxTreeWindow.saveDataUrl,
										params : {
											orgRoles : msg,
											id : CheckboxTreeWindow.paramId
										},
										method : "POST",
										success : function(request) {
											var json = Ext.util.JSON
													.decode(request.responseText);
											var result = json.msg;
											Ext.ux.Toast.msg("信息", result);
											CheckboxTreeWindow
													.fireEvent('checboxTreeSave');
											CheckboxTreeWindow.close();
										},
										failure : function(request) {
											Ext.ux.Toast.msg("信息", "数据加载失败！");
											CheckboxTreeWindow.close();
										}
									});
						}
					}],
					listeners : {
						"load" : function(node) {
						},
						'checkchange' : function(node, checked) {
							checkchange(node, checked);
						}
					}
				});

		function checkchange(node, checked) {
			if (CheckboxTreeWindow.initFlag) {
				if (checked == true) {
					// 选中状态时，自动选择上级
					selectAllParents(node, checked);
				}
				// 下级选择与当前选择保持一致
				selectAllChilds(node, checked);
			}
		}

		/**
		 * 全选/全消，所有下级
		 */
		function selectAllChilds(node, checked) {

			if (node.hasChildNodes()) {
				if (!node.childrenRendered) {
					node.expand();
				}
				Ext.each(node.childNodes, function(child) {
							setNodeCheck(child, checked);
							selectAllChilds(child, checked);
						}, this);
			}
		}
		/**
		 * 全选/全消，所有上级
		 */
		function selectAllParents(node, checked) {
			if (node.parentNode && !node.parentNode.isRoot) {
				setNodeCheck(node.parentNode, checked);
				selectAllParents(node.parentNode, checked);
			}
		}

		function setNodeCheck(node, checked) {
			if (node.attributes && (node.attributes.checked != checked)) {
				node.attributes.checked = checked; // 设置属性值
				node.getUI().checkbox.checked = checked; // 设置UI选中状态
				// node.fireEvent('checkchange',node,checked);
			}
		}

		tree.getRootNode().expand(true);
		// 获取已选择的数据
		Ext.Ajax.request({
					method : 'POST',
					url : __ctxPath + CheckboxTreeWindow.selectedDataUrl,
					params : {
						id : CheckboxTreeWindow.paramId,
						orgId : CheckboxTreeWindow.checkedOrgId
					},
					method : "POST",
					success : function(request) {
						var json = Ext.util.JSON.decode(request.responseText);
						if (json.success) {
							var result = json.result;
							var rootNode = tree.getRootNode();
							OECP.ui.CheckboxTreeWindow.initNode(rootNode,
									result);
						} else {
						}
						CheckboxTreeWindow.initFlag = true;
					},
					failure : function(request) {
						Ext.ux.Toast.msg("信息", "数据加载失败！");
					}
				});
		CheckboxTreeWindow.items = [tree];
		OECP.ui.CheckboxTreeWindow.superclass.initComponent.call(this);
	}
});
// 初始化
OECP.ui.CheckboxTreeWindow.initNode = function(node, orgRoles) {
	node.expand();
	if (node.attributes.id != "source") {
		for (var i = 0; i < orgRoles.length; i++) {
			if (node.attributes.id == orgRoles[i].id) {
				node.getUI().toggleCheck(true);
				node.attributes.checked = true;
				node.attributes.orgRoleId = orgRoles[i].orgRoleId;
				break;
			}
		}
	}
	if (node.hasChildNodes()) {
		node.eachChild(function(child) {
					OECP.ui.CheckboxTreeWindow.initNode(child, orgRoles);
				})
	}
}
OECP.ui.CheckboxTreeWindow.allNodeValue = '';
// ///////////////////////////////
OECP.ui.CheckboxTreeWindow.getAllRoot = function(value) {
	var rootNode = value.getRootNode();// 获取根节点
	OECP.ui.CheckboxTreeWindow.findchildnode(rootNode); // 开始递归
	OECP.ui.CheckboxTreeWindow.allNodeValue = '['
			+ OECP.ui.CheckboxTreeWindow.allNodeValue + ']';
	return OECP.ui.CheckboxTreeWindow.allNodeValue;
};
OECP.ui.CheckboxTreeWindow.findchildnode = function(node) {
	var childnodes = node.childNodes;
	Ext.each(childnodes, function(n) { // 从节点中取出子节点依次遍历
				if (OECP.ui.CheckboxTreeWindow.allNodeValue.length > 0) {
					OECP.ui.CheckboxTreeWindow.allNodeValue += ','
				}
				OECP.ui.CheckboxTreeWindow.allNodeValue += '{"text":"'
						+ n.attributes.text + '",';
				OECP.ui.CheckboxTreeWindow.allNodeValue += '"id":"'
						+ n.attributes.id + '",';
				OECP.ui.CheckboxTreeWindow.allNodeValue += '"orgRoleId":"'
						+ n.attributes.orgRoleId + '",';
				OECP.ui.CheckboxTreeWindow.allNodeValue += '"checked":'
						+ n.attributes.checked + '}';
				if (n.hasChildNodes()) { // 判断子节点下是否存在子节点
					OECP.ui.CheckboxTreeWindow.findchildnode(n); // 如果存在子节点
					// 递归
				}
			});
}
