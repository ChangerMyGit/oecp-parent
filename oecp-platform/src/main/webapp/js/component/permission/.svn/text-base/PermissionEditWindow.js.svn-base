Ext.ns('OECP.permission');


/**
 * 权限修改窗口
 * @author slx
 * @class OECP.permission.PermissionEditWindow
 * @extends Ext.Window
 */
OECP.permission.PermissionEditWindow = Ext.extend(Ext.Window,{
	title : '功能权限分配',
	width : 500,
	height : 500,
//    autoHeight : true,
//    closeAction : 'hide',
    plain : true,
    modal : true,
    layout : 'border',
	saveURL : '',
	buttonAlign : 'center',
	orgRoleId : null,
	treeData : {},
	orgRoleTreeData:{},
	
	initComponent : function(){
		var PermissionEditWindow = this;
		OECP.permission.PermissionEditWindow.superclass.initComponent.call(this);
		
		var treeDataTemp = OECP.core.util.clone(PermissionEditWindow.treeData);
		this.addEvents('dataSaved');
		var treePanel = new Ext.tree.TreePanel({
			width : 500,
    		height : 500,
    		region : 'center',
    		enableDD : false,
			enableDrag : false,
			autoScroll : true,
			rootVisible : false,
			root : new Ext.tree.AsyncTreeNode({children :treeDataTemp}),
			listeners : {
				"contextMenu" : function(node,e){
					alert(node)
				},
				'checkchange' : function(node,checked){
					checkchange(node,checked);
				},
				'load' : function (node){
					node.render();
				}
				
			}
		});
		delete PermissionEditWindow.treeData;
		this.add(treePanel);
		
		function checkchange(node,checked){
			if(checked == true){
			// 选中状态时，自动选择上级
				selectAllParents(node,checked);
			}
			// 下级选择与当前选择保持一致
			selectAllChilds(node,checked);
			
			if(checked == false  && node.attributes.type!='ele'){
			// 本级最后一个选项被取消时，第一父节点取消选择。
			// 页面元素的勾选不参与此逻辑
				deCheckParent(node);
			}
		}
		
		function deCheckParent(node){
			if(!node.isRoot && !node.parentNode.isRoot){
				var pn = node.parentNode;
				for(var i = 0 ; i < pn.childNodes.length; i++){
					if(pn.childNodes[i].attributes.checked == true)
						return;
				}
				
                setNodeCheck(pn,false);
                deCheckParent(node.parentNode);
			}
		}
		
		/**
		 * 全选/全消，所有下级
		 */
		function selectAllChilds(node,checked){

			if (node.hasChildNodes()){
				if(!node.childrenRendered){
	                node.expand();
	            }
                Ext.each(node.childNodes, function(child) {
					setNodeCheck(child,checked);
                    selectAllChilds(child, checked);
                }, this);
			}
		}
		/**
		 * 全选/全消，所有上级
		 */
		function selectAllParents(node,checked){
			if(node.parentNode && !node.parentNode.isRoot){
				setNodeCheck(node.parentNode,checked);
				selectAllParents(node.parentNode,checked);
			}
		}
		
		function setNodeCheck(node,checked){
			if(node.attributes && (node.attributes.checked != checked)){
				node.attributes.checked = checked;	// 设置属性值
				node.getUI().checkbox.checked = checked;	// 设置UI选中状态
//				node.fireEvent('checkchange',node,checked);
			}
		}
		
		
		function copyPermission(fromData){
			// 先清空目标的选择状态
			selectAllChilds(treePanel.root,false);
			for(var i=0 ;i < fromData.length ; i++){
				var tonode = treePanel.getNodeById(fromData[i].id);
				if(tonode){
					tonode.attributes.checked = fromData[i].checked;	// 设置属性值
					tonode.getUI().checkbox.checked = fromData[i].checked;	// 设置UI选中状态
					tonode.attributes.children = fromData[i].children;
					tonode.reload();
				}
			}
			
		}
		
		function showCopyWin(){
			var copyWin = new OECP.permission.RoleSelectWindow({
				treeData : PermissionEditWindow.orgRoleTreeData,
				listeners:{'ok':
					function(orgrole){
						Ext.Ajax.request({
							method : 'POST', // 发送方式.'POST'或'GET',一般是'POST'
							url : __ctxPath + '/permission/permissionTreeCode.do', // 发送到页面
							params : {
								orgRoleId : orgrole.id
							},// 参数
							success : function(request) { // 发送成功的回调函数
								var message = request.responseText; // 取得从JSP文件out.print(...)传来的文本
								var jsonObject = Ext.util.JSON.decode(message);// 转换为Json对象
								copyPermission(jsonObject);
							},
							failure : function() { // 发送失败的回调函数
								Ext.Msg.alert("信息", "数据载入失败！");
							}
						});
					}
	    		}
			});
			copyWin.show();
		}
		
    	this.addButton([
    		{text:'复制',
    		listeners:{'click':
    				function(){
    					showCopyWin();
    				}}
    		},
    		{text:'保存',
	        listeners : {
        		'click' : function() {
        		    getPermissons();
        		    Ext.Ajax.request({
        			method : 'POST', // 发送方式.'POST'或'GET',一般是'POST'
        			url : __ctxPath + '/permission/updateRolePermission.do', // 发送到页面
        			params : getPermissons(),// 参数
        			success : function(request) { // 发送成功的回调函数
        			    var message = request.responseText; // 取得从JSP文件out.print(...)传来的文本
        			    var jsonObject = Ext.util.JSON.decode(message);// 转换为Json对象
        			    Ext.ux.Toast.msg("信息", jsonObject.msg);
        			    PermissionEditWindow.fireEvent('dataSaved');
        			    PermissionEditWindow.close();
        			},
        			failure : function() { // 发送失败的回调函数
        			    Ext.Msg.alert("信息", "数据载入失败！");
        			}
        		    });
        		}
        	    }
        	},
    		{text:'关闭',
    		listeners:{'click':
    				function(){
    					PermissionEditWindow.close();
    				}}}
    		]);
    	
    		function getPermissons(){
    		    var bcs = treePanel.root.attributes.children;
        	    // bc fun ui ele
        	    var pms = {orgRoleId : PermissionEditWindow.orgRoleId},fidx = -1,uidx=-1,eleidx=-1;
        	    for (var i = 0; i < bcs.length; i++) {
        		var funcs = bcs[i].children;
        		for(var ii=0 ;ii < funcs.length ; ii++){
        		    funcPms(funcs[ii]);
        		}
        	    }
        	    
        	    function funcPms(func){
        		if(func.checked){
        		    fidx ++;
        		    pms['permissions['+fidx+'].function.id'] = func.id.substr(func.id.indexOf('_')+1);
        		    pms['permissions['+fidx+'].orgRole.id'] = PermissionEditWindow.orgRoleId;
        		    var fchild;
        		    if( fchild= func.children){
        			for(var i=0 ;i < fchild.length ; i++){
        			    if(fchild[i].type == 'fun'){
        				funcPms(fchild[i]);
        			    }else{
        				uiPms(fchild[i]);
        			    }
                		}
        			uidx = -1, eleidx = -1;
        		    }
        		}
        	    };
        	    function uiPms(ui){
        		if(ui.checked){
        		    uidx ++;
        		    pms['permissions['+fidx+'].permissionFuncUIs['+uidx+'].functionUI.id'] = ui.id.substr(ui.id.indexOf('_')+1);
        		    var echild;
        		    if(echild = ui.children){
        			for(var i=0 ; i< echild.length ; i ++){
        			    elePms(echild[i]);
        			}
        		    }
        		}
        	    };
        	    function elePms(ele){
        		if(ele.checked){
        		    eleidx++;
        		    pms['permissions['+fidx+'].permissionFuncUIs['+uidx+'].permissionUIElements['+eleidx+'].uiElement.id'] = ele.id.substr(ele.id.indexOf('_')+1);
        		}
        	    };
        	    return pms;
    		};
    		
    		function gettreejson(){
				var td = treePanel.root.attributes.children;
				var len = td.length;
				for(var i =0 ; i<len ; i++){
					delete td[i].loader
				}
				return Ext.util.JSON.encode(td);	
			}
	}
});
