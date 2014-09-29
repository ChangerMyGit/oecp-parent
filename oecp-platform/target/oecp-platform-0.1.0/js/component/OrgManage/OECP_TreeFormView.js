
Ext.ns('OECP.ui.comp');
/**
 * 左树右表单样式界面
 * @author slx
 * @class OECP.ui.comp.TreeFormView
 * @extends OECP.ui.base.ToftPanel
 */
OECP.ui.comp.TreeFormView = Ext.extend(OECP.ui.base.ToftPanel,{
	treeDataUrl : undefined,// 数据来源URL
	
	/** 树设置 **/
	treePanelTitle : '目录结构',	// 树面板标题
	treeRootText : '目录',	// 树根节点标题
	parentIDField : 'parent.id',
	parentNameField : 'parent.name',
//	treeClick : Ext.emptyFn(),
	/** form区域设置 **/
	formTitle : '详细信息',	// form上的按钮
	defaultBtns : true, //是否使用默认按钮
	formBtns : [],	// form上的按钮
	formItems : [],	// 表单界面元素
	formDataURL : '',	// 获取Form数据的URL
	formSaveURL : '',	// Form保存的RUL
	formDeleteURL : '',	// Form保存的RUL
	treeNodeIcon : null,// 树节点图标
	showInfoPanel : null,
	getSelectedId : function(){
		return this.dataid;
	},
	getButtonById : function(btnid){
		return this.showInfoPanel.getTopToolbar().getComponent(btnid);
	},
	
	// 整体初始化
	initComponent : function(){
		var TreeFormView = this;
		// 树panel
		var treePanel = new Ext.tree.TreePanel({
				id : 'oecp_tree',
				stateId : 'oecp_tree',
				title:this.treePanelTitle,
			    useArrows: true,
			    region:'west',
			    split : true,
			    collapsible:true,
			    autoScroll: true,
			    draggable : false,
			    border: true,
			    stateful : true,
			    width : 200,
			    height : 1000,
				header : true,
				root:new Ext.tree.AsyncTreeNode({
					text: TreeFormView.treeRootText,
			        id: 'tree-root'
				}),
			    loader : new Ext.tree.TreeLoader({
					dataUrl : TreeFormView.treeDataURL,
					createNode : function(attr){
						if(TreeFormView.treeNodeIcon){
							attr.icon = TreeFormView.treeNodeIcon;
						}
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
		});

		var treeExNodes = []; // 树展开的节点
		treePanel.on('expandnode',function(node){
			treeExNodes.push(node.getPath());
		});
		treePanel.on('collapsenode',function(node){
			treeExNodes.splice( treeExNodes.indexOf(node.getPath()), 1 );
		});
		
		/** 内容只读panel **/
		/** 内容只读panel -> 准备DataView所需要的Store和XTemplate **/
		var storeField = [];
		var xtpl = ['<tpl for=".">'];// 显示模板
		var len = TreeFormView.formItems.length;
		for(var i=0 ; i < len ;i++ ){
			var item = TreeFormView.formItems[i];
			var itemname = item.mapping == undefined ? item.name : item.mapping;
			storeField.push(itemname);
			if(item.hidden != true){
				var valuetpl = '{' + itemname +'}';
				if(item.xtype=='checkbox'){ // 显示逻辑型值
					valuetpl = '<tpl if="'+itemname+'==true">是</tpl><tpl if="'+itemname+'==false">否</tpl>';
				}
				
				xtpl.push('<div><span>'+ item.fieldLabel + ' : </span><span> '+ valuetpl +' </span></div>');
			}
		}
		xtpl.push('</tpl>');
		
		var jstore = new Ext.data.JsonStore({
			storeId : 'id',
			root : 'result',
			url : TreeFormView.formDataURL,
		    fields: storeField
		});
		// 只读界面显示模板
		var tpl = new Ext.XTemplate(
				xtpl
            );
		var dataview = new Ext.DataView({
		        store: jstore,
		        tpl: tpl,
		        autoHeight:true,
		        width : 200,
		        emptyText: ''
		    });

		
		    // 创建只读Panel
		TreeFormView.showInfoPanel = new Ext.Panel({
			title : TreeFormView.formTitle,
			bodyStyle:'padding:10px',
			region:'center',
		    border : true,
		    width : 800,
		    tbar :TreeFormView.formBtns,
		   	items : dataview
		});
		function getData(){
			var v = {};
			if(jstore.data.items.length>0)
				v = jstore.data.items[0].data;
			return v;
		}
		
		/** 详细信息面板上的按钮 **/
		if(this.defaultBtns){
			this.addBtn = new Ext.Button({
				text:'新增',
				iconCls : "btn-add",
				handler:function add(){
					win.show();
					win.setTitle(TreeFormView.formTitle + '-新增');
					var dt = {};
					dt[TreeFormView.parentIDField] = getData()['id'];
					dt[TreeFormView.parentNameField] = getData()['name'];
					win.setFormData(dt);
				}
			});
			this.editBtn = new Ext.Button({
				id : 'btn_edit',
				text : '编辑',
				iconCls : "btn-edit",
				disabled:true,
				handler : function edit(){
					win.show();
					win.setTitle(TreeFormView.formTitle + '-编辑');
					win.setFormData(getData());
				}
			});
			this.delBtn = new Ext.Button({
				id : 'btn_delete',
				text:'删除',
				iconCls : "btn-delete",
				disabled:true,
				handler:function del(){
					Ext.Msg.confirm("信息确认", "您确认要删除数据吗？", function(b) {
						if (b == 'yes') {
							Ext.Ajax.request({
							   	url: TreeFormView.formDeleteURL,
							   	success: function(res){
							   		var msg = eval("("+Ext.util.Format.trim(res.responseText)+")");
							   		if(msg.success){
								   		Ext.ux.Toast.msg("信息", msg.msg);
								   		jstore.removeAll();
								   		sNode.remove();
								   		treePanel.selectPath(treePanel.root.getPath());
							   		}else{
							   			Ext.Msg.show({title:"错误",msg:msg.msg,buttons:Ext.Msg.OK});
							   		}
							   	},
							   	failure: function(){Ext.Msg.show({title:"错误",msg:msg.result.msg,buttons:Ext.Msg.OK});},
							   	params: { id: getData()['id'] }
							});
						}
					});
				}
			});
			var defaultBtn = [this.addBtn,this.editBtn,this.delBtn];
			TreeFormView.showInfoPanel.getTopToolbar().addButton(defaultBtn);
		}
		
		// 点击tree节点，加载form数据
		var sNode = null;
		var sNodePaht = null;
		treePanel.on('click',function(node){
			if(node.id != 'tree-root'){
				
				jstore.reload({
					params:{id:node.id},
					waitTitle:'提示',
					waitMsg:'正在加载数据请稍后...'
				});
				
				TreeFormView.dataid = node.id;
				setEditBtnDisabled(false);
			}else{
				jstore.removeAll();
				TreeFormView.dataid = null;
				setEditBtnDisabled(true);
			}
			sNode = node;
			sNodePath = node.getPath();
			TreeFormView.fireEvent('treeNodeSelected',node);
		});
		
		function setEditBtnDisabled(disabled){
			if(TreeFormView.defaultBtns){
				if(TreeFormView.editBtn!=undefined){
					TreeFormView.editBtn.setDisabled(disabled);
				}
				if(TreeFormView.delBtn!=undefined){
					TreeFormView.delBtn.setDisabled(disabled);
				}
			}
		}
		/** 编辑保存数据的弹出窗体 Window **/
		var win = new OECP.ui.comp.OECPFormWindow({
			formItems : TreeFormView.formItems,
			saveURL : TreeFormView.formSaveURL
		});
		// 数据保存事件
		win.on("dataSaved",function(id){
			
			treePanel.root.reload(function(){
				for(var i=0 ; i < treeExNodes.length  ; i++){
			   	 treePanel.expandPath(treeExNodes[i]); 
				}
			});
			
			if(id!=''){
				jstore.reload({
						params:{id:id},
						waitTitle:'提示',
						waitMsg:'正在加载数据请稍后...'
					});
					
				treePanel.selectPath(sNodePath);
			}
		});
		
		// 包含tree和form放入中间区域
		this.cpanel = new Ext.Panel({
			defaultType: 'panel',
			layout:'border',
		    height : 10000,
			baseCls: 'x-plain',
			items : [treePanel,TreeFormView.showInfoPanel]
		});
		
		TreeFormView.addEvents('treeNodeSelected');
		OECP.ui.comp.TreeFormView.superclass.initComponent.call(this);
	}
});
