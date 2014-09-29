
Ext.ns('OECP.ui.view');
OECP.ui.view.ViewPanel = Ext.extend(Ext.Panel,{
	layout 		: 'border',
	hideBorders :true,
	/**
	 * @cfg {Array}  editorType 字段控件类型列表
	 */
	editorType		: [['textfield', '文本'],['numberfield','数值'],['combo', '下拉'],['checkbox', '复选框'],['datefield', '日期'],['other','其他']],
	/**
	 * @cfg  {Object} 单据头、体、尾三部分公共属性
	 */
	billPartConfig	: {type : 'Panel', layout : 'table',  border : false},
	/**
	 * @cfg {Object} panelConfig 新增面板时的默认面板属性 
	 */
	panelConfig		: {cancommit: false, colspan: 3, colspan: 1, height: 200 ,idx :1,rowspan:1, title: '',type: 'Panel', layout: 'table', width:800},
	/**
	 * @cfg {Object} fieldConfig 新增字段时的默认面板属性 
	 */
	fieldConfig		: {cancommit: true,onlist:true,onform:true, colspan: 1, colspan:1, hidden:false, idx:1, rowspan:1, type:'Field',allowBlank:true, editorcfg:''},
	/**
	 * @cfg {Object} tabConfig 新增标签面板时的默认面板属性 
	 */
	tabConfig		: {cancommit: false, colspan: 1, colspan:1, hidden:false, idx:1, rowspan:1, type:'Tab', activeTab:0, height:300},
	/**
	 * @cfg {Object} gridConfig 新增表格面板时的默认属性
	 */
	gridConfig		: {cancommit: false, colspan: 1, colspan:1, hidden:false, idx:1, rowspan:1, type:'Grid',height:200 ,singleSelect:false, total:false,editable:true},
	/**
	 * @cfg {Object} FIELDS 控件属性列表
	 */
//	FIELDS			: {
	currentType : 'None',
	uicompAttrs : {
		None	:	{},
		Root	:	{
			viewcode	: {desc: '模板编号',type :'string',displayName:'模板编号'},
			viewname	: {desc: '模板名称',type :'string',displayName:'模板名称'},
			shared		: {desc: '是否共享	',type: 'boolean',displayName:'是否共享',readOnly:true}
		},
		Panel	:	{
			title		: {desc: '如果想在控件的顶端显示标题请设置',type: 'string',displayName:'标题'},
			height		: {desc: '控件高度',type: 'number',displayName:'高度'},
			width		: {desc: '控件宽度',type: 'number',displayName:'宽度'},
			cols		: {desc: '面板划分为多少个列，用于承载子控件。',type :'number',displayName:'布局列数'},
			colspan		: {desc: '当前控件占用上级控件的列数',type: 'number',displayName:'占用列数'},
			rowspan		: {desc: '当前控件占用上级控件的行数',type: 'number',displayName:'占用行数'},
			idx			: {desc: '显示顺序',type: 'number',displayName:'显示顺序'},
			cancommit	: {desc: '单据保存时是否需要提交',type :'boolean',displayName:'是否提交'}
		},
		Grid	:	{
			title		: {desc: '如果想在控件的顶端显示标题请设置',type: 'string',displayName:'标题'},
			eoname		: {desc: '对应后台实体变量的名称如：bodyeos',type: 'string',displayName:'实体名'},
			editable	: {desc: '表格是否可编辑',type: 'boolean',displayName:'是否可编辑'},
			height		: {desc: '控件高度',type: 'number',displayName:'高度'},
			width		: {desc: '控件宽度',type: 'number',displayName:'宽度'},
			colspan		: {desc: '当前控件占用上级控件的列数',type: 'number',displayName:'占用列数'},
			rowspan		: {desc: '当前控件占用上级控件的行数',type: 'number',displayName:'占用行数'},
			cancommit	: {desc: '能否提交',type :'boolean',displayName:'是否提交'},
			hidden		: {desc: '当前组件处于隐藏状态',type: 'boolean',displayName:'是否隐藏'},
			idx			: {desc: '显示顺序',type: 'number',displayName:'显示顺序'},
			onlist		: {desc: '列表显示',type: 'boolean',displayName:'列表显示'},
			onform		: {desc: '表单显示',type: 'boolean',displayName:'表单显示'},
			showSM		: {desc: '是否在表格前显示复选框，用来显示数据行的选择状态。',type: 'boolean',displayName:'显示行选择列'},
			singleSelect: {desc: '是否只允许选中1行',type:'boolean',displayName:'行单选'},
			issummary	: {desc: '显示合计行',type:'boolean',displayName:'显示合计'}
		},
		Tab		:	{
			title		: {desc: '如果想在控件的顶端显示标题请设置',type: 'string',displayName:'标题'},
			height		: {desc: '控件高度',type: 'number',displayName:'高度'},
			width		: {desc: '控件宽度',type: 'number',displayName:'宽度'},
			colspan		: {desc: '当前控件占用上级控件的列数',type: 'number',displayName:'占用列数'},
			rowspan		: {desc: '当前控件占用上级控件的行数',type: 'number',displayName:'占用行数'},
			activeTab	: {desc: '默认激活标签的索引位置',type: 'number'	,displayName:'默认激活'},
			tabWidth	: {desc: '标签标题显示的宽度,默认为120。',type: 'number'	,displayName:'标签宽度'},
			cancommit	: {desc: '能否提交',type :'boolean',displayName:'是否提交'},
			hidden		: {desc: '当前控件处于隐藏状态',type: 'boolean',displayName:'是否隐藏'}
		},
		Field	:	{
			title		: {desc: '字段的显示名',type: 'string',displayName:'显示名'},
			name		: {desc: '提交保存时，编辑字段对应后台属性的属性名。',type:'string'	,displayName:'属性名'},
			dataIndex	: {desc: '数据标示是对应json中的名称',type:'string'	,displayName:'数据标示'},
			editor		: {desc: '编辑控件类型',type: 'combo',displayName:'编辑控件',values:[['textfield', '文本'],['numberfield','数值'],['combo', '下拉'],['checkbox', '复选框'],['datefield', '日期'],['other','其他']]},
			editorcfg	: {desc: '编辑器的其他特殊属性设置，使用（属性名:值,名:值）的格式输入。',type:'object',displayName:'编辑器设置'},
			readOnly	: {desc: '字段是否可只读',type: 'boolean',displayName:'是否只读'},
			allowBlank	: {desc: '允许为空',type :'boolean',displayName:'允许为空'},
			minLength	: {desc: '最小长度',type :'number',displayName:'最小长度'},
			maxLength	: {desc: '最大长度',type :'number',displayName:'最大长度'},
			decimalPrecision	: {desc: '小数点后保留多少位',type :'number',displayName:'小数精度'},
			height		: {desc: '控件高度',type: 'number',displayName:'高度'},
			width		: {desc: '控件宽度',type: 'number',displayName:'宽度'},
			cols		: {desc: '表格布局的列数',type :'number',displayName:'布局列数'},
			colspan		: {desc: '占用列数',type: 'number',displayName:'占用列数'},
			rowspan		: {desc: '占用行数',type: 'number',displayName:'占用行数'},
			hidden		: {desc: '当前组件处于隐藏状态',type: 'boolean',displayName:'是否隐藏'},
			cancommit	: {desc: '能否提交',type :'boolean',displayName:'是否提交'},
			summarytype	: {desc: '当字段被放在表格上时，合计行计算的方式。',type:'combo',displayName:'合计方式',type:'combo',values:[['sum', '求和'],['count','取行数'],['average', '平均值'],['min', '最大值'],['max', '最小值']]},
			idx			: {desc: '显示顺序',type: 'number',displayName:'显示顺序'},
			onlist		: {desc: '列表显示',type: 'boolean',displayName:'列表显示'},
			onform		: {desc: '表单显示',type: 'boolean',displayName:'表单显示'}
		}
	},
	initComponent:function(){
		var scope = me = this;
		/**
		 * @event viewsave 视图保存事件
		 * @param {Object} scope 作用域
		 */
		this.addEvents('viewsave');
		this.builderPanel = new Ext.Panel({
				height:500,
				width:400,
				defaults: {   
				    frame: true
				},
				layout: 'fit'
			});

		this.idCounter = 0;
		this.autoUpdate = true;
		this.initTreePanel();
		this.initEditPanel();
		this.initFieldConfigWin();
		this.items = [{
					region  : 'north',
					title   : '界面生成器',
					height  : 52,
					tbar    : [{
							iconCls : 'icon-update',
							text    : '刷新',
							tooltip : '刷新修改后的内容',
							scope   : scope,
							handler : function() { 
								this.doUpdateForm();
							}
						},{
							text	: '保存',
							tooltip	: '视图保存到数据库',
							scope	: scope,
							handler	: function(){
								this.doSave();
							}
						},{
							text	: '取消',
							tooltip	: '不保存本次的编辑，返回视图列表界面。',
							scope	: scope,
							handler	: function(){
								this.doCancel();
							}
						}
					]
				}, {
					region: 'west',
					border: false,
					width : 255,
					split : true,
					xtype : 'panel',
					layout: 'border',
					items : [
						scope.treePanel,
						scope.editPanel
					]
				}, {
					region:'center',
					layout:'fit',
					items : [scope.builderPanel]
			}];
			var root = this.treePanel.root;
			root.fEl = this.builderPanel;
			root.elConfig = this.builderPanel.initialConfig;
			this.builderPanel._node = root;
			OECP.ui.view.ViewPanel.superclass.initComponent.call(this);
	},
	//初始化Dom树
	initTreePanel : function() {
		var scope = this;
		var tree = new Ext.tree.TreePanel({
			region          : 'north',
			title           : "DOM查看器",
			iconCls         : "icon-el",
			collapsible     : true,
			floatable       : false,
			autoScroll      : true,
			height          : 200,
			split           : true,
			animate         : false,
			enableDD        : true,
			ddGroup         : 'component',
			containerScroll : true,
			rootVisible     : false,
			selModel        : new Ext.tree.DefaultSelectionModel(),
			bbar            : [{
				text    : '全部展开',
				tooltip : '展开所有元素',
				scope   : this,
				handler : function() {this.treePanel.expandAll();}
			},{
				text    : '全部收起',
				tooltip : '收起所有元素',
				scope   : this,
				handler : function() {this.treePanel.collapseAll();}
			}]
		});

	    var root = new Ext.tree.TreeNode({
	        text      : '界面生成器顶层元素',
			id        : this.getNewId(),
	        draggable : false
	    });
	    tree.setRootNode(root);

		tree.selModel.on('selectionchange', function(e, node) {
//			e.preventDefault();
			this.setCurrentNode(node);
			window.node = node; // debug
		}, this);

		// clone a node
		var cloneNode = function(node) {
			var config = OECP.core.util.clone(node.elConfig);
			delete config.id;
			var newNode = new Ext.tree.TreeNode({id:this.getNewId(),text: "拷贝自 <b>"+config.title+"</b>"});
			newNode.elConfig = config;
			newNode.ftype = config.type;
			// clone children
			for(var i = 0; i < node.childNodes.length; i++){
				n = node.childNodes[i];
				if(n) { newNode.appendChild(cloneNode(n)); }
			}
			return newNode;
		}.createDelegate(this);

		// 判断节点是否可拖拽
		tree.on('nodedragover', function(de) {
			if(de.dropNode.ftype && ( //禁止表头表体表尾元素拖拽
				 de.dropNode.ftype == 'head'
				|| de.dropNode.ftype == 'body'
				|| de.dropNode.ftype == 'foot')){
				return false;
			}
			var p = de.point, t= de.target;
			if(p == "above" || t == "below") {
				t = t.parentNode;
			}
			if (!t) { return false; }
			return (this.canAppend({}, t) === true);
		}, this);
		// 节点移动后事件 更新显示顺序。
		tree.on('movenode',function(tree,node ,oldParent,newParent,index){
			if(node.parentNode && node.parentNode.ftype != 'bill'){
				if(oldParent != newParent){
					// 被拖动的节点父节点变化时，清空节点的id和下面子节点的id，否则数据保存时将出错。
					function clearId(n){
						delete n.elConfig['id'];
						var ccn = n.childNodes;
						if(ccn){
							for(var i=0;i<ccn.length;i++){
								clearId(ccn[i]);
							}
						}
					}
					clearId(node);
				}
				var cn = node.parentNode.childNodes;
				for(var i=0;i<cn.length;i++){
					cn[i].elConfig['idx'] = i+1;
				}
			}
		});
		// copy node on 'ctrl key' drop
		tree.on('beforenodedrop', function(de) {
							if (!de.rawEvent.ctrlKey) {
								return true;
							}
							var ns = de.dropNode, p = de.point, t = de.target;
							if (!(ns instanceof Array)) {
								ns = [ns];
							}
							var n = null;
							for (var i = 0, len = ns.length; i < len; i++) {
								n = cloneNode(ns[i]);
								if (p == "above") {
									t.parentNode.insertBefore(n, t);
								} else if (p == "below") {
									t.parentNode.insertBefore(n, t.nextSibling);
								} else {
									t.appendChild(n);
								}
							}
							n.ui.focus();
							if (de.tree.hlDrop) {
								n.ui.highlight();
							}
							t.ui.endDrop();
							de.tree.fireEvent("nodedrop", de);
							return false;
						}, this);

		// update on node drop
				tree.on('nodedrop', function(de) {
							var node = de.target;
							if (de.point != 'above' && de.point != 'below') {
								node = node.parentNode || node;
							}
							this.doUpdateForm();
						}, this, {
							buffer : 100
						});

		// get first selected node
				tree.getSelectedNode = function() {
					return this.selModel.getSelectedNode();
				};
		// 初始化右键菜单选项
				var del_item = new Ext.menu.Item({
							text : '删除此元素',
							iconCls : 'icon-deleteEl',
							scope : this,
							handler : function(item) {
								var pn = this.editPanel.currentNode.parentNode;
								this.removeNode(this.editPanel.currentNode);
								this.sortNode(pn);
							}
						});
				var add_item = new Ext.menu.Item({
							text : '新建字段',
							iconCls : 'icon-addEl',
							scope : this,
							handler : function(item) {
								this.fieldConfigWin.show();
							}
						});
		// 添加单据头体尾三部分
				var addThirdItem = function(nodeText, nodeFtype, node) {
					var _cols = ['body', 'foot', 'head'].indexOf(nodeFtype) + 1;
					var _panel = {
						type : 'Panel',
						layout : 'table',
						border : false,
						cols : _cols,
						title : nodeText
					};
					var panelNode = new Ext.tree.TreeNode({
								id : scope.getNewId(),
								text : nodeText
							});
					panelNode.ftype = nodeFtype;
					panelNode.elConfig = _panel;
					scope.editPanel.currentNode.appendChild(panelNode);
					scope.editPanel.currentNode.sort(function(a, b) {// 对新增节点进行排序
								return ['head', 'body', 'foot'].indexOf(a.ftype) > ['head', 'body', 'foot'].indexOf(b.ftype);
							});
					scope.doUpdateForm();
				};
				var add_head = new Ext.menu.Item({
							text : '新建单据头',
							iconCls : 'icon-addEl',
							scope : this,
							handler : function(item) {
								addThirdItem('单据头', 'head');
							}
						});
				var add_body = new Ext.menu.Item({
							text : '新建单据体',
							iconCls : 'icon-addEl',
							scope : this,
							handler : function(item) {
								addThirdItem('单据体', 'body');
							}
						});
				var add_foot = new Ext.menu.Item({
							text : '新建单据尾',
							iconCls : 'icon-addEl',
							scope : this,
							handler : function(item) {
								addThirdItem('单据尾', 'foot');
							}
						});
				var add_panel = new Ext.menu.Item({
							text : '新建面板',
							iconCls : 'icon-addEl',
							scope : this,
							handler : function(item) {
								var _panel = scope.panelConfig;
								var panelNode = new Ext.tree.TreeNode({
											id : scope.getNewId(),
											text : 'Panel'
										});
								panelNode.ftype = 'Panel';
								panelNode.elConfig = _panel;
								scope.editPanel.currentNode.appendChild(panelNode);
								scope.doUpdateForm();
							}
						});
				var add_grid = new Ext.menu.Item({
							text : '新建表格',
							iconCls : 'icon-addEl',
							scope : this,
							handler : function(item) {
								var _grid = scope.gridConfig;
								var gridNode = new Ext.tree.TreeNode({
											text : scope.gridConfig.type
										});
								gridNode.ftype = scope.gridConfig.type;
								gridNode.elConfig = _grid;
								// 追加一个默认字段防止因缺失column报错
								var _id = Ext.applyIf({
											name : 'id',
											title : 'id',
											editor : 'textfield',
											hidden : true
										}, scope.fieldConfig);
								var idNode = new Ext.tree.TreeNode({
											text : 'id'
										});
								idNode.ftype = 'Field';
								idNode.elConfig = _id;

								gridNode.appendChild(idNode);
								scope.editPanel.currentNode.appendChild(gridNode);
								scope.doUpdateForm();
							}
						});
				var add_tab = new Ext.menu.Item({
							text : '新建标签面板',
							iconCls : 'icon-addEl',
							scope : this,
							handler : function(item) {
								var tab_panel = scope.tabConfig;
								var tabNode = new Ext.tree.TreeNode({
											id : scope.getNewId(),
											text : scope.tabConfig.type
										});
								tabNode.ftype = scope.tabConfig.type;
								tabNode.elConfig = tab_panel;
								scope.editPanel.currentNode.appendChild(tabNode);
								scope.doUpdateForm();
							}
						});
				var cm = new Ext.menu.Menu({
							add_head : add_head,
							add_body : add_body,
							add_foot : add_foot,
							add_panel : add_panel,
							add_grid : add_grid,
							add_tab : add_tab,
							del_item : del_item,
							add_item : add_item,
							items : [add_head, add_body, add_foot, del_item, add_panel, add_grid, add_tab, add_item]
						});
				eachMenuShow = function(smenu, hmenu) {
					for (var m = 0; m < smenu.length; m++) {
						cm[smenu[m]].show();
					}
					for (var m = 0; m < hmenu.length; m++) {
						cm[hmenu[m]].hide();
					}
				};
				//蔽菜单
				eachMenuDisable = function() {
					if (tree.root.firstChild) {
						for (var i = 0; i < tree.root.firstChild.childNodes.length; i++) {
							var f = tree.root.firstChild.childNodes[i].ftype;
							cm['add_' + f].hide();
						}
					}
				};

		//控制菜单显示
		transformMenu = function(node) {
			if (node.ftype) {
				if (node.ftype === 'head') {//表头菜单项
					eachMenuShow(['add_panel','add_tab','add_item','del_item'],['add_head','add_body','add_foot','add_grid']);
				} else if (node.ftype === 'body') {
					eachMenuShow(['add_tab','add_grid','del_item'],['add_head','add_body','add_foot','add_panel','add_item']);
				} else if (node.ftype === 'foot') {
					eachMenuShow(['add_tab','add_item','add_panel','del_item'],['add_head','add_body','add_foot','add_grid']);
				} else if(node.ftype === 'Grid'){
					eachMenuShow(['del_item','add_item'],['add_head','add_body','add_foot','add_tab','add_panel','add_grid']);
				} else if(node.ftype ==='Field'){
					eachMenuShow(['del_item'],['add_head','add_body','add_foot','add_item','add_grid','add_tab','add_panel']);
				} else if(node.ftype === 'Panel'){
					if(scope.isThisType(node,'body')){//表体内的tab只能增加grid
						eachMenuShow(['del_item','add_grid'],['add_head','add_body','add_foot','add_item','add_tab','add_panel']);
					} else {
						eachMenuShow(['del_item','add_item','add_tab','add_panel'],['add_head','add_body','add_foot','add_grid']);
					}
				} else if (node.ftype === 'Tab'){
					eachMenuShow(['del_item','add_panel'],['add_head','add_body','add_foot','add_grid','add_item','add_tab']);
				} else if (node.ftype === 'bill'){
					eachMenuShow(['add_head','add_body','add_foot'],['add_item','add_grid','add_tab','del_item','add_panel']);
					eachMenuDisable();
				} else {
					eachMenuShow([],['add_head','add_body','add_foot','add_item','add_grid','add_tab','del_item','add_panel']);
				}
			} else {
				eachMenuShow(['del_item','add_item'],['add_panel','add_tab','add_grid']);
			}
		};
		tree.on('contextmenu', function(node, e) {
				e.preventDefault();
				if (node != this.treePanel.root) {
					scope.treePanel.selectPath(node.getPath());
					cm.node = node;
					transformMenu(node);
					cm.showAt(e.getXY());
				}
			}, this);
		this.contextMenu = cm;
		this.treePanel = tree;
	},
	//private
	isThisType : function(node,nodetype){
		if(node.ftype == nodetype){
			return true;
		}else if(node.parentNode){
			return this.isThisType(node.parentNode,nodetype);
		}
		return false;
	},
	// customized property grid for attributes
	initEditPanel : function() {
		var scope = this;

		var grid = new Ext.grid.PropertyGrid({
				title            : '属性',
				height           : 300,
				split            : true,
				region           : 'center',
				source           : {},
				customEditors    : scope.getCustomEditors()
			});
		delete grid.propStore.store.sortInfo;
		grid.colModel.getColumnAt(0).sortable = false;
		var valueRenderer = function(value, p, r) {
			if(r.data.name && r.data.name==''){
			} else if (typeof value == 'boolean') {
				p.css = (value ? 'typeBoolTrue' : 'typeBoolFalse');
				return (value ? 'True' : 'False');
			} else if (this.attrType(r.id) == 'object') {
				p.css = "typeObject";
				return value;
			} else if (this.attrType(r.id) == 'combo') {
				var g = this.editPanel,
				es = this.editPanel.customEditors,
				data = es[r.id].field.findRecord('value',value);
				if(data){
					return data.data.name;
				}
				return value;
			} else {
				return value;
			}
		}.createDelegate(this);
		var propertyRenderer = function(value, p) {
			if(scope.currentType){
				var t = scope.uicompAttrs[scope.currentType][value];
				qtip = (t ? t.desc : '');
				p.attr = 'qtip="' + qtip.replace(/"/g,'&quot;') + '"';
				if(t){
					return t.displayName||value;
				}else{
					return value;
				}
			}
		};
		grid.colModel.getRenderer = function(col){
			return (col == 0 ? propertyRenderer : valueRenderer);
		};

		grid.on("beforeedit", function(e){ 
		    var cn = this.editPanel.currentNode,
		    	ct = this.currentType,
		    	cas;
		    if(!(cn || ct)){
			    e.cancel = true;
			    return false;
		    }
		    if(['head','body','foot'].indexOf(cn.ftype) > -1){// 表头、表体、表尾的title不能修改
//				if('title' == e.record.data.name){
//					e.cancel = true;
//			    	return false;
//				}
			}else{
				// 属性设置了readOnly的不可编辑
				cas = this.uicompAttrs[this.currentType];
				if(cas[e.record.data.name].readOnly){
					e.cancel = true;
			    	return false;
				}
			}
		},this);
		// update node text & id
		grid.store.on('update', function(s,r,t) {
			if (t == Ext.data.Record.EDIT) {
				grid.currentNode.elConfig[r.data.name] = r.data.value;
				this.updateNode(grid.currentNode);
				if(['head','body','foot','bill'].indexOf(grid.currentNode.ftype)==-1){
					grid.currentNode.setText(this.getTreeNodeText(grid.currentNode.elConfig));	
				}
				this.doUpdateForm();
			}
			var pn = scope.editPanel.currentNode.parentNode;
			if(pn.elConfig.ftype !== 'bill'){
				scope.sortNode(pn);
			}
		}, this, {buffer:100});
		
		this.editPanel = grid;
	},

	// return the node corresponding to an element (search upward)
	getNodeForEl : function(el) {
		var search = 0;
		var target = null;
		while (search < 10) {
			target = Ext.ComponentMgr.get(el.id);
			if (target && target._node) {
				return target._node;
			}
			el = el.parentNode;
			if (!el) { break; }
			search++;
		}
		return null;
	},

	// set current editing node
	setCurrentNode : function(node, select) {
		var p = this.editPanel;
		p.enable();
		if (!node || !node.elConfig) {
			this.currentType = 'None';
			p.currentNode = null;
			p.setSource({});
			p.disable();
		} else {
			var config = node.elConfig;
			if (node == this.treePanel.root.firstChild) {
				this.currentType = 'Root';
			}else{
				this.currentType = node.ftype == 'head'||node.ftype == 'body'||node.ftype == 'foot'?'Panel':node.ftype;
				for (k in config) {
					if (this.attrType(k) == 'object' && typeof config[k] == 'object') {
						try {
							var ec = Ext.encode(config[k]);
							config[k] = ec;
						} catch(e) {}
					}
				}
			}
			
			// 根据当前节点的类型设置属性编辑表中显示的属性
			var currentAttrs = this.uicompAttrs[this.currentType];
			var attrSource = {};
			for (attr in currentAttrs){
				if(currentAttrs[attr].type == 'boolean'){
					attrSource[attr] = config[attr] || false;
				}else{
					attrSource[attr] = config[attr] || '';
				}
			}
			// 重新构建编辑器
			p.customEditors = this.getCustomEditors();
			p.setSource(attrSource);
			p.currentNode = node;
		}
		if (select) {
			this.treePanel.expandPath(node.getPath());
		}
	},

	// update node text & id (if necessary)
	updateNode : function(node) {
		if (!node) { return; }
		if (node.elConfig.id && node.elConfig.id != node.id) {
			node.id = node.elConfig.id;
		}
	},
	//加载视图
	doLoad:function(fid){
		var _fid = fid || this.functionid;
		if(Ext.isDefined(_fid)){
			Ext.Ajax.request({
				scope	: this,
				params	: {"viewvo.id":_fid},
				url		: __ctxPath +'/funview/load.do',
				success	: function(response, opts){
					var json = Ext.util.JSON.decode(response.responseText);
					if(json.success){
						try{
							var root = this.treePanel.getRootNode();
							while(root.firstChild){
								root.removeChild(root.firstChild);
							}
							this.initBillConfig(json.result);
							this.editPanel.setSource({});
						}catch(e){
							Ext.MessageBox.alert('模板配置错误','模板设置出错，预览失败！<br/>错误消息：'+e);
						}
					}else{
						Ext.MessageBox.alert('错误',json.msg);
					}
				},
				failure	: function(response, opts){
					Ext.MessageBox.alert('错误','视图加载失败！');
				}			
			});
		}else{
			Ext.MessageBox.alert('错误','功能ID为空！');
		}
	},
	//保存视图到数据库
	doSave:function(){
		Ext.Ajax.request({
			scope	: this,
			params	: this.getViewDom(),
			url		: __ctxPath +'/funview/save.do',
			success	: function(response, opts){
				var json = Ext.util.JSON.decode(response.responseText);
				if(json.success){
					this.fireEvent('viewsave',this);
				}else{
					Ext.MessageBox.alert('错误',json.msg);
				}
			},
			failure	: function(response, opts){
				Ext.MessageBox.alert('错误','保存失败!');
			}			
		});
	},
	doCancel:function(){
		this.fireEvent('viewsave',this);
	},
	//提交dom数并获取预览界面
	doUpdateForm:function(){
		Ext.Ajax.request({
			scope	: this,
			params	: this.getViewDom(),
			url		: __ctxPath + '/funview/preview.do',
			success	: function(response, opts){
				try{
					var json = Ext.util.JSON.decode(response.responseText);
					if(json.success){
						this.updateBuilderPanel(json.result);
					}
				}catch(e){
					Ext.MessageBox.alert('模板配置错误','模板设置出错，预览失败！<br/>错误消息：'+e);
					throw e;
				}
			},
			failure	: function(response, opts){
				
			}
		});
	},
	//获取施视图dom对象
	getViewDom :function(){
		var root = this.treePanel.getRootNode();
		var params={};
		if(root.firstChild){
			params = this.encodeParam(params,'viewvo',root.firstChild.elConfig);
			for(var i=0;i<root.firstChild.childNodes.length;i++){
				var node = root.firstChild.childNodes[i];
				params = this.getConfig(params,'viewvo.'+node.ftype,node,undefined);
			}
		}
		return params;
	},
	encodeParam:function(params,varname,obj){
		for(var v in obj){
			if(!Ext.isEmpty(obj[v],false)){
				if(Ext.isObject(obj[v])){
					params = this.encodeParam(params,varname+'.'+v,obj[v]);
				}else{
					params[varname+'.'+v] = obj[v];
				}
			}
		}
		return params;
	},
	updateBuilderPanel:function(config){
		while(this.builderPanel.items.first()){
			this.builderPanel.remove(this.builderPanel.items.first());
		}
		this.builderPanel.add(config);
		this.builderPanel.doLayout();
	},
	//拼装数组
	getConfig:function(params,varStr,node,num){
		var elconfig = node.elConfig;
		var prefix = varStr+(Ext.isDefined(num) ? ('.children['+num+']') : '');
		for(var vname in elconfig){
			var val ="";
			if(Ext.isObject(elconfig[vname])){
				val = Ext.encode(elconfig[vname]);
			}else{
				val = elconfig[vname];
			}
			if(!Ext.isEmpty(val,false)){
				params[prefix +'.'+ vname] = val;
			}
		}
		if(node.hasChildNodes()){
			var i=0;
			node.eachChild(function(cnode){
				this.getConfig(params,prefix,cnode,i);
				i++;
			},this);
		}
		return params;
	},
	// node text created from config of el
	configToText : function(c) {
		var txt = [];
		c = c || {};
		if (c.type)      { txt.push(c.type); }
		if (c.title)      { txt.push('<b>' + c.title + '</b>'); }
		return (txt.length == 0 ? "Element" : txt.join(" "));
	},

	// return type of attribute
	attrType : function(name) {
		if (!this.uicompAttrs[this.currentType][name]) { return 'unknown'; }
		return this.uicompAttrs[this.currentType][name].type;
	},
	// get a new ID
	getNewId : function() {
		return "form-gen-" + (this.idCounter++);
	},
	// return true if config can be added to node, or an error message if it cannot
	canAppend : function(config, node) {
		if (node == this.treePanel.root && this.treePanel.root.hasChildNodes()) {
			return "Only one element can be directly under the GUI Builder";
		}
		var ftype = node.ftype;
		if (ftype && ['Field','bill'].indexOf(ftype) != -1) {
			return 'You cannot add element under xtype "'+ftype+'"';
		}
		return true;
	},
	initBillConfig: function(json){
		var billNode = new Ext.tree.TreeNode({
			text: (json.viewname ? json.viewname : '单据')
		});
		billNode.ftype = 'bill';
		var _items =[],_names=[],_types=[];
		if(json.head){
			_items.push(json.head);
			delete json.head;
			_names.push('单据头');
			_types.push('head');
		}
		if(json.body){
			_items.push(json.body);
			delete json.body;
			_names.push('单据体');
			_types.push('body');
		}
		if(json.foot){
			_items.push(json.foot);
			delete json.foot;
			_names.push('单据尾');
			_types.push('foot');
		}
		for(var i=0;i<_items.length;i++){
			var nodeItem = new Ext.tree.TreeNode({
				text:_names[i]
			});
//			if(_items[i].children){
				nodeItem = this.appendChildrenNode(_items[i].children,nodeItem);
				delete _items[i].children;
				nodeItem.ftype=_types[i];
				nodeItem.elConfig = _items[i];
				billNode.appendChild(nodeItem);
//			}
		}
		this.treePanel.root.appendChild(billNode);
		billNode.elConfig = json;
		this.doUpdateForm();
	},
	getTreeNodeText : function(data){
		var pix = data.type=='bill'?'视图':
			data.type=='head'?'表头':
			data.type=='body'?'表体':
			data.type=='foot'?'表尾':
			data.type=='Panel'?'面板':
			data.type=='Grid'?'表格':
			data.type=='Tab'?'标签页':
			data.type=='Field'?'字段':'';
			
		return pix + ' - ' + (data.title || data.name || '');
	},
	//追加子节点
	appendChildrenNode:function(json,parentNode){
		var _children = null;
		if(!json){
		    return parentNode;
		}
		if(Ext.isArray(json)){
			for(var i=0;i<json.length;i++){
				_children = null;
				var _node = new Ext.tree.TreeNode({
					text: this.getTreeNodeText(json[i])
				});
				if(json[i].children){
					_children = json[i].children;
					delete json[i].children;
				}
				_node.ftype = json[i].type;
				if(_node.ftype && _node.ftype ==='Field'){
					_node.elConfig = Ext.applyIf(json[i],this.fieldConfig);
				}else{
					_node.elConfig = json[i];
				}
				if(_children){
					_node = this.appendChildrenNode(_children,_node);
				}
				parentNode.appendChild(_node);
			}
		}else if(Ext.isObject(json)){
			var _node = new Ext.tree.TreeNode({
					text: this.getTreeNodeText(json[i])
				});
			if(json.children){
				_children = OECP.core.util.clone(json.children);
				delete json.children;
			}
			_node.ftype = json.type;
			_node.elConfig = json;
			if(_children){
				_node = this.appendChildrenNode(_children,_node);
			}
			parentNode.appendChild(_node);
		}
		return parentNode;
	},
	//初始化
	appendBillConfig :function(){
		this.bill = {title:'单据',type:'Panel',layout:'table',height:500,cols:1,viewcode:'viewcode',viewname:'viewname'};
		var billNode= new Ext.tree.TreeNode({
			id:this.getNewId(),
			text:'单据'
		});
		billNode.ftype = 'bill';
		billNode.elConfig = this.bill;
		this.treePanel.root.appendChild(billNode);
	},

	// remove a node
	removeNode : function(node) {
			if (!node || node == this.treePanel.root) { return; }
			var nextNode = node.nextSibling || node.parentNode;
			var pNode = node.parentNode;
			pNode.removeChild(node);
			this.doUpdateForm();
//			this.setCurrentNode(nextNode, true);
	},

	initFieldConfigWin : function(){
		var scope = this ;
		if(!this.fieldConfigWin){
			this.fieldConfigWin = new Ext.Window({
				closeAction : 'hide',
				width 		: 400,
				height		: 300,
				title 		: '字段属性',
				onShow		: function(){
					var cn = scope.editPanel.currentNode,num=1;
					if(cn.childNodes){
						for(var i=0;i<cn.childNodes.length;i++){
							var n = cn.childNodes[i];
							if(! n.elConfig.hidden){
								num++;
							}
						}
					}
					scope.fieldConfigWin.items.first().form.setValues({idx:num});
				},
				items 		: [{
							xtype : 'form',
							frame   :true,
							hideBorders:false,
							items : [{name : 'type',
										hidden : true,
										xtype : 'textfield',
										value : 'Field'
									},{width	   : 200,
										xtype 	   : 'textfield',
										fieldLabel : '显示名',
										allowBlank : false,
										name	   : 'title'
									},{width	   : 200,
										xtype 	   : 'textfield',
										fieldLabel : '属性名',
										allowBlank : false,
										name	   : 'name'
									},{width	   : 200,
										xtype 	   : 'textfield',
										fieldLabel : '数据标示',
										allowBlank : false,
										name	   : 'dataIndex'
									}, {width	   	: 200,
										xtype 	   	: 'combo',
										fieldLabel 	: '字段类型',
										allowBlank 	: false,
										name		: 'editor',
										valueField 	: 'value',
										displayField: 'text',
										mode 		: 'local',
										hiddenName	: 'editor',
										value		: 'textfield',
										editable 	: false,
										store		: new Ext.data.SimpleStore({
													fields	: ['value', 'text'],
													data	: scope.editorType
												}),
										listeners:{
											'select':function(combo,record,index){
												var cfgField = scope.fieldConfigWin.items.first().items.last();
												if(['other','combo'].indexOf(record.data.value)!=-1){
													cfgField.allowBlank = false,
													cfgField.show();
												}else{
													cfgField.setValue(null);
													cfgField.allowBlank = true,
													cfgField.hide();
												}
											}
										},
										triggerAction: 'all'
									},{width		: 200,
										xtype		: 'numberfield',
										fieldLabel	: '显示顺序',
										allowBlank	: false,
										name		: 'idx'
									},{width		: 200,
										height		: 120,
										xtype		: 'textarea',
										fieldLabel	: '参数',
										hidden		: true,
										name		: 'editorcfg'
									}]
						}],
				buttons:[{
					text:'确定',
					handler:function(){
							if(scope.fieldConfigWin.items.first().form.isValid()){
								var _data = scope.fieldConfigWin.items.first().form.getValues();
								for(var v in _data){
									if(Ext.isEmpty(_data[v],false))
										delete _data[v];
								}
								_data = Ext.applyIf(_data,scope.fieldConfig);
								var filedNode= new Ext.tree.TreeNode({
									id:scope.getNewId(),
									text: '字段 - ' + (_data.title || _data.name)
								});
								filedNode.ftype = 'Field';
								filedNode.elConfig = _data;
								scope.editPanel.currentNode.appendChild(filedNode);
								scope.doUpdateForm();
								scope.fieldConfigWin.items.first().form.reset();
								scope.fieldConfigWin.hide();
							}
						}
					},{
					text:'取消',
					handler:function(){
						scope.fieldConfigWin.hide();
					}
				}]
			});
		}
	},
	//树节点排序
	sortNode:function(node){
		node.sort(function(a,b){
			if(a.elConfig.idx && b.elConfig.idx){
				return a.elConfig.idx>b.elConfig.idx;
			}
			return true;
		});
	},

	// remove all nodes
	removeAll : function() {
		var root = this.treePanel.root;
		while(root.firstChild){
				root.removeChild(root.firstChild);
		}
	},

	/**
	 * 设置属性表格的编辑控件
	 * @return {}
	 */
	getCustomEditors : function() {
		var g = Ext.grid;
		var f = Ext.form;
		var cmEditors = new g.PropertyColumnModel().editors;
		var eds = {};
		var fields = this.uicompAttrs[this.currentType];
		for (i in fields) {
			if (fields[i].values) {
				var values = fields[i].values;
				var data = [];
				for (var j=0;j<values.length;j++) { 
					data.push([values[j][0],values[j][1]]); 
				}
				eds[i] = new g.GridEditor(new f.SimpleCombo({forceSelection:false,data:data,editable:true}));
			} else {
				if (fields[i].type == "boolean") {
					eds[i] = cmEditors['boolean'];
				} 
				if (fields[i].type == "number") {
					eds[i] = cmEditors['number'];
				}
				if (fields[i].type == "string") {
					eds[i] = cmEditors['string'];
				}
			}
		}
		return eds;
	},
	onDestory:function(){//销毁
		if(!this.fieldConfigWin){
			Ext.destroy(this.fieldConfigWin);
		}
		OECP.ui.view.ViewPanel.superclass.onDestroy.call(this);
	}
});

