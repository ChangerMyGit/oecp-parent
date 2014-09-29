// 组织角色的权限分配界面
Ext.ns("OECP.permission");
/**
 * @author liujingtao
 * @class PermissionManagerView
 * @extends Ext.Panel
 */
OECP.permission.PermissionManagerView = Ext.extend(Ext.Panel, {
	id : 'OECP.permission.PermissionManagerView',
	title : '权限分配',
	layout : 'border',
	funtreedata : {},
	orgRoleTreeData : {},
	orgRoleId : null,
	initComponent : function() {
		var t = this;
		OECP.permission.PermissionManagerView.superclass.initComponent.call(this);
		
		var role_org_panel = new Ext.tree.TreePanel({
				title : '角色列表',
				animate : true,
				region : 'west',
				collapsible : true,// 面板是否可收缩
				margins : '0 0 0 3',
				autoScroll : true,
				rootVisible : true,
				enableDD : false,
				enableDrag : false,
				split : true,
				width : 170,
				lines : true,
				root : new Ext.tree.AsyncTreeNode({
							id : "root",
							text : "登录用户"
						}),

				loader : new Ext.tree.TreeLoader({
							dataUrl : __ctxPath + "/permission/roleTreeCode.do",
							listeners:{'load' : function(loader,node,data){
								t.orgRoleTreeData = Ext.util.JSON.decode(data.responseText);
							}}
						}),
				listeners : {
					"click" : function(node) {
						if (node.isLeaf()) {
							permission_panel.getLoader().baseParams = {
								orgRoleId : node.id
							};
							permission_panel.root.reload();
							editBtn.setDisabled(false);
							panStatePanel.setTitle('<font size="2">当前角色: '+ node.parentNode.text + ' 组织: ' + node.text + '</font>');
						}else{
							editBtn.setDisabled(true);
						}

					}
					
				}
			
		});
		var editBtn = new Ext.Button({
			text:'分配',
			disabled:true,
			listeners:
					{'click':function(){
						var win = new OECP.permission.PermissionEditWindow({
							orgRoleId : t.orgRoleId,
							treeData : t.funtreedata,
							orgRoleTreeData:t.orgRoleTreeData
						}); 
						win.on('dataSaved', function(id) {
							permission_panel.root.reload();
						});
						win.show();
					}}
		});
		var permission_panel = new Ext.tree.TreePanel({
				region : 'center',
				margins : '0 0 0 3',
				enableDD : false,
				enableDrag : false,
				autoScroll : true,
				rootVisible : false,
				tbar:[editBtn],
				root : new Ext.tree.AsyncTreeNode(),

				loader : new Ext.tree.TreeLoader({
							dataUrl : __ctxPath + "/permission/permissionTreeCode.do",
							listeners:{'load' : function(loader,node,data){
								t.funtreedata = Ext.util.JSON.decode(data.responseText);
								t.orgRoleId = loader.baseParams.orgRoleId;
							}}
						}),
				listeners : {
					"expandnode" : function(node){
						for(i=0;i<node.childNodes.length;i++){
							node.childNodes[i].getUI().checkbox.disabled = true;
						}
					}
				}
		});
		
		// 空白部分
		var panStatePanel = new Ext.Panel({
					height : 23,
					layout : 'fit',
					region : 'south',
					title : '&nbsp;',
					baseCls : 'x-plain'
				});
		
		this.add(role_org_panel);
		this.add(permission_panel);
		this.add(panStatePanel);
	}
});

