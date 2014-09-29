Ext.ns('OECP.ui');
/**
 * @author wangliang
 *         <p>
 *         左树右表界面
 *         </p>
 *         例子:
 * 
 * <pre><code>
 * var treePanel = OECP.ui.TreeGridView({
 * 			title : '角色管理',
 * 			id : 'OECP.role.RoleView',
 * 			treeDataUrl : __ctxPath + '/role/orgsTree.do',
 * 			queryRecordUrl : __ctxPath + '/role/queryRoleByOrgId.do',
 * 			gridItems : [{
 * 						header : '主键',
 * 						dataIndex : 'id',
 * 						hidden : true
 * 					}, {
 * 						header : '编码',
 * 						dataIndex : 'code'
 * 					}, {
 * 						header : '名称',
 * 						dataIndex : 'name'
 * 					}],
 * 			gridStoreFields : ['id', 'code', 'name'],
 * 			gridBtns[new Ext.button({
 * 				text : '增加',
 * 				listeners:{'click',function(btn,e){
 * 					......
 * 				}}
 * 			})]
 * 		});
 * </code></pre>
 * 
 * <p>
 * 按钮控制
 * </p>
 * 
 * <pre><code>
 * treePanel.sysBtns[0].hidden = true;
 * </code></pre>
 */
OECP.ui.TreeGridView = Ext.extend(OECP.ui.base.ToftPanel, {
	/**
	 * @cfg {String} treeDataUrl 树数据Url
	 * @type
	 */
	treeDataUrl : null,
	/**
	 * @cfg {String} treePanelTitle 树标题
	 */
	treePanelTitle : '目录结构',
	/**
	 * @cfg {String} treeRootText 树根节点标题
	 */
	treeRootText : '目录',
	
	/**
	 * @cfg {Array} gridItems 表格列属性
	 */
	gridItems : undefined,
	/**
	 * @cfg {Array} gridStoreFields 表格数据属性
	 * @type
	 */
	gridStoreFields : undefined,
	/**
	 * @cfg {Array} gridBtns 自定义表头按钮
	 */
	gridBtns : undefined,
	/**
	 * @cfg {String} queryRecordUrl 表格数据查询地址
	 */
	queryRecordUrl : undefined,
	/**
	 * @cfg {Number} pageSize Grid每页显示记录数,当pageSize=0时,不分页
	 */
	pageSize : 25,
	/**
	 * @cfg {Ext.tree.TreePanel} tree 树
	 */
	tree : undefined,
	/**
	 * @cfg {Ext.data.JsonSore} store 表格store
	 */
	stroe : undefined,
	/**
	 * @cfg {Ext.grid.GridPanel} grid 表格
	 */
	grid : undefined,
	/**
	 * @cfg {Boolean} multiple 是否多选; false:单选, true:多选
	 */
	multiple : false,
	/**
	 * @cfg {Boolean} showCheckBox 显示选择器; false:不显示,true:显示
	 */
	showCheckBox : true,
	/**
	 * @cfg {Boolean} showRowNum 显示行号; false:不显示,true:显示
	 */
	showRowNum : true,
	/**
	 * 
	 * @cfg {Boolean} 点击是否只支持叶子 true：只有点击叶子才会刷新列表，false：点击任何节点都刷新列表
	 */
	clickOnlyLeaf : false,
	/**
	 * @function selected 获取选中的数据集合
	 * @return {Array}
	 */
	selected : function() {
		return this.grid.getSelectionModel().getSelections();
	},
	// private 当前已选择的节点Id
	currentNodeId : '',
	currentNode : undefined,
	// 初始化
	initComponent : function() {
		var master = this;
		if (!Ext.isDefined(master.tree)) {
			master.tree = new Ext.tree.TreePanel({
				stateId : 'TreeGridView_tree',
				title : master.treePanelTitle,
				region : "west",
				enableDD : false,// 禁用节点拖拽
				enableDrag : false,
				rootVisible : false,
				autoScroll : true,
				collapsible : true,
				split : true,
				margins : '0 0 0 5',// 西部添加5个像素宽度
				width : 150,
				lines : true,
				header : true,
				loader : new Ext.tree.TreeLoader({
							dataUrl : master.treeDataUrl,
							createNode : function(attr){
								if(master.treeNodeIcon){
									attr.icon = master.treeNodeIcon;
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
				root : {
					id : "root",
					text : master.treeRootText,
					expand : true
				},
				listeners : {
					"click" : function(node) {
						if (node.id != 'root') {
							if (!master.clickOnlyLeaf
									|| (master.clickOnlyLeaf && node.isLeaf())) {
								// 已选节点赋值
								master.currentNodeId = node.id;

								master.currentNode = node;
								// 清除历史记录
								master.store.removeAll();
								// 加载表格数据
								master.store.reload({
											params : {
												id : node.id
											}
										});
							} else {
								master.currentNodeId = '';

								master.currentNode = undefined;
							}
						}
					}
				}
			});
		}
		// 按钮条
		var topbar = new Ext.Toolbar({
					height : 30,
					bodyStyle : "text-align:left",
					items : master.gridBtns
				});
		// 构造表格列
		var columnDatas = master.gridItems;
		var gridItemlen = master.gridItems.length;
		var sm = new Ext.grid.CheckboxSelectionModel({
					singleSelect : master.multiple,
					hidden : !master.showCheckBox
				});// 选择器
		var colArray = new Array();
		colArray[0] = sm;
		colArray[1] = new Ext.grid.RowNumberer({
					hidden : !master.showRowNum
				});// 行号
		for (var i = 0; i < gridItemlen; i++) {// 拼装列属性
			colArray[i + 2] = columnDatas[i]
		}
		var cm = new Ext.grid.ColumnModel(colArray);
		// 数据仓库
		if (!Ext.isDefined(master.stroe)) {
			master.store = new Ext.data.JsonStore({
						url : master.queryRecordUrl,
						baseParams : {
							id : master.currentNodeId
						},
						root : "result",
						totalProperty : "totalCounts",
						remoteSort : true,
						fields : master.gridStoreFields,
						listeners : {
							"beforeload" : function() {
								this.baseParams = {
									id : master.currentNodeId
								};
							}
						}
					});
		}

		// 表格面板
		if (!Ext.isDefined(master.gird)) {
			master.grid = new Ext.grid.GridPanel({
						flex : master.gridItems.length + 1,
						region : "center",
						cm : cm,
						sm : sm,
						store : master.store,
						tbar : topbar,
						autoScroll : true,
						stripeRows : true,
						// TODO 自适应Panle高度
						height : 500,
						viewConfig : {
							forceFit : true,
							autoFill : true
						},
						bbar : master.pageSize > 0 ? new Ext.PagingToolbar({
									pageSize : master.pageSize,
									store : master.store,
									displayInfo : true,
									displayMsg : "当前页记录索引{0}-{1}， 共{2}条记录",
									emptyMsg : "当前没有记录"
								}) : null
					});
		}
		// 填充显示内容
		this.cpanel = new Ext.Panel({
					defaultType : 'panel',
					layout : 'border',
					height : 10000,
					baseCls : 'x-plain',
					items : [master.tree, new Ext.Panel({
										id : "girdPanel",
										region : "center",
										items : [this.grid]
									})]
				});
		OECP.ui.TreeGridView.superclass.initComponent.call(this);
	}
})