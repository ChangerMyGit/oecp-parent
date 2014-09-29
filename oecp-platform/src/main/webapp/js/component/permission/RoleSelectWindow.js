Ext.ns('OECP.permission');


/**
 * 复制角色选择窗口
 * @author slx
 * @class OECP.permission.RoleSelectWindow
 * @extends Ext.Window
 */
OECP.permission.RoleSelectWindow = Ext.extend(Ext.Window,{
	title : '角色选择',
	width : 450,
	height : 400,
//    autoHeight : true,
//    closeAction : 'close',
    plain : true,
    modal : true,
    layout : 'border',
	buttonAlign : 'center',
	orgRoleId : null,
	treeData : {},
	returnValue : {},
	
	/**
	 * 设置树节点的数据
	 * @param {} treedata
	 */
	setTreeData : function(treedata){
		this.treePanel.root.attributes.children = treedata;
		this.treePanel.root.reload();
	},
	
	initComponent : function(){
		var roleSelectWindow = this;
		OECP.permission.RoleSelectWindow.superclass.initComponent.call(this);
		
		var treeRoot = new Ext.tree.AsyncTreeNode({children :roleSelectWindow.treeData});
		roleSelectWindow.treePanel = new Ext.tree.TreePanel({
			width : 450,
    		height : 400,
    		region : 'center',
    		enableDD : false,
			enableDrag : false,
			autoScroll : true,
			rootVisible : false,
			root : treeRoot,
			listeners : {
				'click' : function(node){
					roleSelectWindow.returnValue = node.attributes;
					
					if(roleSelectWindow.returnValue.leaf == true){
						okBtn.setDisabled(false);
					}else{
						okBtn.setDisabled(true);
					}
				}
			}
		});
		this.add(roleSelectWindow.treePanel);
		
		var okBtn = new Ext.Button({
			text:'确定',
    		disabled : true,
    		listeners:{'click':
    				function(){
    					roleSelectWindow.fireEvent('ok',roleSelectWindow.returnValue);
    					roleSelectWindow.close();
    				}
    			}
		});
		
    	this.addButton([
    		okBtn,
    		{text:'关闭',
    		listeners:{'click':
    				function(){
    					roleSelectWindow.close();
    				}}}
    		]);
    		
    	this.addEvents('ok');
	}
});
