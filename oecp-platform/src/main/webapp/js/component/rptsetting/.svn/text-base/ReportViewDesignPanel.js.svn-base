/**  
 * @include "../uiview/editor/js/FunViewMngEditPanel.js" 
 */
Ext.ns('OECP.report.view');
/**
 * 报表视图设计界面
 */
OECP.report.view.ReportViewDesignPanel = Ext.extend(OECP.ui.view.ViewPanel, {
	rootConfig : { code: '', title: 'xx报表视图' },
	chartConfig : { colspan: 1, height: 200 ,idx :1,rowspan:1, title: '图形',type: 'Chart',ctype:'Bar_Y', width:800,dataRoot:'chart1'},
	/**
	 * @cfg {Object} panelConfig 新增面板时的默认面板属性 
	 */
	panelConfig		: {colspan: 3, colspan: 1, height: 200 ,idx :1,rowspan:1, title: '',type: 'Panel', layout: 'table', width:800},
	/**
	 * @cfg {Object} fieldConfig 新增字段时的默认面板属性 
	 */
	fieldConfig		: {colspan: 1, colspan:1, hidden:false, idx:1, rowspan:1, type:'Field',allowBlank:true, editorcfg:''},
	/**
	 * @cfg {Object} gridConfig 新增表格面板时的默认属性
	 */
	gridConfig		: {colspan: 1, colspan:1, hidden:false, idx:1, rowspan:1, editable:false, type:'Grid',dataRoot:'grid1',height:200 ,width:800,singleSelect:false, total:false,editable:true},
	uicompAttrs : {
		None	:	{},
		Root	:	{
			code	: {desc: '视图编号',type :'string',displayName:'视图编号'},
			title	: {desc: '标题',type :'string',displayName:'标题'}
		},
		Chart	:	{
			title	: {desc: '标题',type :'string',displayName:'标题'},
			ctype	: {desc: '柱状图，饼图，折线图等',type: 'combo',displayName:'图形类型',values:[['Bar_Y', '柱状图'],['Bar_X','条形图'],['Pie', '饼图'],['Line', '折线图']]},
			dataRoot	: {desc: '对应结果集中此图形使用到的数据在查询结果集中的名称，默认为chart1。如需修改结果集中的名称请在查询结果处理脚本中处理。',type: 'string',displayName:'结果集名称'},
			height	: {desc: '控件高度',type: 'number',displayName:'高度'},
			width	: {desc: '控件宽度',type: 'number',displayName:'宽度'},
			idx		: {desc: '显示顺序',type: 'number',displayName:'显示顺序'},
			vField	: {desc: '保存数据值的字段名',type :'string',displayName:'值字段'},
			vTitle	: {desc: '保存数据值的字段标题',type :'string',displayName:'值字段标题'},
			nField	: {desc: '保存名称的字段名',type :'string',displayName:'名称字段'},
			nTitle	: {desc: '保存名称的字段标题',type :'string',displayName:'名称字段标题'},
			sField	: {desc: '保存区域的字段名',type :'string',displayName:'区域字段'},
			sTitle	: {desc: '保存区域的字段标题',type :'string',displayName:'区域字段标题'},
			colspan	: {desc: '当前控件占用上级控件的列数',type: 'number',displayName:'占用列数'},
			rowspan	: {desc: '当前控件占用上级控件的行数',type: 'number',displayName:'占用行数'}
		},
		Panel	:	{
			title		: {desc: '如果想在控件的顶端显示标题请设置',type: 'string',displayName:'标题'},
			height		: {desc: '控件高度',type: 'number',displayName:'高度'},
			width		: {desc: '控件宽度',type: 'number',displayName:'宽度'},
			cols		: {desc: '面板划分为多少个列，用于承载子控件。',type :'number',displayName:'布局列数'},
			colspan		: {desc: '当前控件占用上级控件的列数',type: 'number',displayName:'占用列数'},
			rowspan		: {desc: '当前控件占用上级控件的行数',type: 'number',displayName:'占用行数'},
			idx			: {desc: '显示顺序',type: 'number',displayName:'显示顺序'}
		},
		Grid	:	{
			title		: {desc: '如果想在控件的顶端显示标题请设置',type: 'string',displayName:'标题'},
			dataRoot	: {desc: '对应结果集中此表格使用到的数据在查询结果集中的名称，默认为grid1。如需修改结果集中的名称请在查询结果处理脚本中处理。',type: 'string',displayName:'结果集名称'},
			height		: {desc: '控件高度',type: 'number',displayName:'高度'},
			width		: {desc: '控件宽度',type: 'number',displayName:'宽度'},
			colspan		: {desc: '当前控件占用上级控件的列数',type: 'number',displayName:'占用列数'},
			rowspan		: {desc: '当前控件占用上级控件的行数',type: 'number',displayName:'占用行数'},
			hidden		: {desc: '当前组件处于隐藏状态',type: 'boolean',displayName:'是否隐藏'},
			idx			: {desc: '显示顺序',type: 'number',displayName:'显示顺序'},
			showSM		: {desc: '是否在表格前显示复选框，用来显示数据行的选择状态。',type: 'boolean',displayName:'显示行选择列'},
			singleSelect: {desc: '是否只允许选中1行',type:'boolean',displayName:'行单选'},
			issummary	: {desc: '显示合计行',type:'boolean',displayName:'显示合计'}
		},
		Field	:	{
			title		: {desc: '字段的显示名',type: 'string',displayName:'显示名'},
			dataIndex	: {desc: '数据标示是对应json中的名称',type:'string'	,displayName:'数据标示'},
			editor		: {desc: '数据类型',type: 'combo',displayName:'数据类型',values:[['textfield', '文本'],['numberfield','数值'],['checkbox', '逻辑值'],['datefield', '日期']]},
			editorcfg	: {desc: '特殊显示控件设置，如枚举：xtype:"enumscombo",className:"xxx.xxx.enums.XEnum"。',type:'object',displayName:'显示控件设置'},
			decimalPrecision	: {desc: '小数点后保留多少位',type :'number',displayName:'小数精度'},
			height		: {desc: '控件高度',type: 'number',displayName:'高度'},
			width		: {desc: '控件宽度',type: 'number',displayName:'宽度'},
			colspan		: {desc: '占用列数',type: 'number',displayName:'占用列数'},
			rowspan		: {desc: '占用行数',type: 'number',displayName:'占用行数'},
			hidden		: {desc: '当前组件处于隐藏状态',type: 'boolean',displayName:'是否隐藏'},
			summarytype	: {desc: '当字段被放在表格上时，合计行计算的方式。',type:'combo',displayName:'合计方式',type:'combo',values:[['sum', '求和'],['count','取行数'],['average', '平均值'],['min', '最大值'],['max', '最小值']]},
			idx			: {desc: '显示顺序',type: 'number',displayName:'显示顺序'}
		}
	},
			/**
			 * 初始化界面
			 */
			initComponent : function() {
				var scope = me = this;
				this.builderPanel = new Ext.Panel({
							height : 500,
							width : 400,
							defaults : {
								frame : true
							},
//							layout : 'fit'
							autoScroll : true
						});

				this.idCounter = 0;
				this.autoUpdate = true;
				this.initTreePanel();
				this.initEditPanel();
				this.initFieldConfigWin();
				this.items = [
							{
							region : 'west',
							border : false,
							width : 255,
							split : true,
							xtype : 'panel',
							layout : 'border',
							items : [scope.treePanel, scope.editPanel]
						}, {
							region : 'center',
							layout : 'fit',
							items : [scope.builderPanel]
						}];
				var root = this.treePanel.root;
				root.fEl = this.builderPanel;
				root.elConfig = this.builderPanel.initialConfig;
				this.builderPanel._node = root;
				OECP.ui.view.ViewPanel.superclass.initComponent.call(this);
				var scope = this;
//				this.builderPanel.on("afterrender",function(){
//					scope.doUpdateForm();
//				});
				this.on('render', function() {
							this.el.dom.oncontextmenu = function(e) {
								window.event ? window.event.returnValue = false : e.preventDefault();
							};
						}); 
			},
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
				 de.dropNode.ftype == 'Root')){
				return false;
			}
			var p = de.point, t= de.target;
			if(p == "above" || t == "below") {
				t = t.parentNode;
			}
			if (!t) { return false; }
			return (this.canAppend(de.dropNode, t) === true);
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
				var add_panel = new Ext.menu.Item({
							text : '新建面板',
							iconCls : 'icon-addEl',
							scope : this,
							handler : function(item) {
								var _panel = Ext.apply({},scope.panelConfig);
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
								var _grid = Ext.apply({},scope.gridConfig);
								var gridNode = new Ext.tree.TreeNode({
											text : scope.gridConfig.type
										});
								gridNode.ftype = scope.gridConfig.type;
								gridNode.elConfig = _grid;
								// 追加一个默认字段防止因缺失column报错
								var _id = Ext.applyIf({
											dataIndex : 'field1',
											title : '字段1',
											editor : 'textfield',
											hidden : false
										}, scope.fieldConfig);
								var idNode = new Ext.tree.TreeNode({
											text : 'field1'
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
								var tab_panel = Ext.apply({},scope.tabConfig);
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
				var add_chart = new Ext.menu.Item({
							text : '添加图形',
							iconCls : 'icon-addEl',
							scope : this,
							handler : function(item) {
								var chart_panel = Ext.apply({},scope.chartConfig);
								var chartNode = new Ext.tree.TreeNode({
											id : scope.getNewId(),
											text : scope.chartConfig.type
										});
								chartNode.ftype = scope.chartConfig.type;
								chartNode.elConfig = chart_panel;
								scope.editPanel.currentNode.appendChild(chartNode);
								scope.doUpdateForm();
							}
						});
				var cm = new Ext.menu.Menu({
							add_panel : add_panel,
							add_grid : add_grid,
							add_tab : add_tab,
							del_item : del_item,
							add_item : add_item,
							add_chart : add_chart,
							items : [del_item, add_panel, add_grid, add_tab, add_item,add_chart]
						});
				eachMenuShow = function(smenu) {
					for(mi in cm){
						if(cm[mi] instanceof Ext.menu.Item){
							cm[mi].hide();
						}
					}
					for (var m = 0; m < smenu.length; m++) {
						cm[smenu[m]].show();
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
			if(tree.root && tree.root.firstChild){
				if(node == tree.root.firstChild.firstChild){
					eachMenuShow(['add_panel','add_grid','add_chart']);
					return true;
				}
			}
			if (node.ftype) {
				if (node.ftype === 'Root') {//表头菜单项
					return false;
				} else if(node.ftype === 'Grid'){
					eachMenuShow(['del_item','add_item']);
				} else if(node.ftype ==='Field'){
					eachMenuShow(['del_item']);
				} else if(node.ftype === 'Panel'){
					eachMenuShow(['del_item','add_panel','add_grid','add_chart']);
				} else if(node.ftype === 'Chart'){
					eachMenuShow(['del_item']);
				}
			} else {
				return false;
			}
			return true;
		};
		tree.on('contextmenu', function(node, e) {
				e.preventDefault();
				if (node != this.treePanel.root) {
					scope.treePanel.selectPath(node.getPath());
					cm.node = node;
					if(transformMenu(node))
						cm.showAt(e.getXY());
				}
			}, this);
		this.contextMenu = cm;
		this.treePanel = tree;
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
			canAppend : function(config, node) {
				if (node == this.treePanel.root && this.treePanel.root.hasChildNodes()) {
					return "根元素只能有一个！";
				}
				if (node == this.treePanel.root.firstChild && this.treePanel.root.firstChild.hasChildNodes()) {
					return "报表视图下只允许有一个顶级控件！";
				}
				var toftype = node.ftype , ftype = config.ftype;
				if (toftype && ['Field', 'Chart'].indexOf(toftype) != -1) {
					return '字段和图形下不允许再添加其他元素！';
				}
				if(ftype == 'Chart'){
					if(toftype == 'Grid'){
						return '图形不允许放在表格内！';
					}
				}
				return true;
			},
			doLoad : function(vdata) {
				if (vdata) {
					try {
						var root = this.treePanel.getRootNode();
						while (root.firstChild) {
							root.removeChild(root.firstChild);
						}
						this.initBillConfig(vdata);
						this.editPanel.setSource({});
					} catch (e) {
						Ext.MessageBox.alert('模板配置错误', '模板设置出错，预览失败！<br/>错误消息：' + e);
					}
					// 有数据时加载数据
				} else {
					this.setDefaultData();
					this.editPanel.setSource({});
				}
			},
			initBillConfig : function(vdata) {
				var rptNode = new Ext.tree.TreeNode({
							text : (vdata.title ? vdata.title : '报表')
						});
				rptNode.ftype = 'Root';
				var _items = [];
				if(vdata.mainuivo){
					_items.push(vdata.mainuivo);
				}
				for (var i = 0; i < _items.length; i++) {
					var nodeItem = new Ext.tree.TreeNode({
								text : _items[i].title
							});
					if (_items[i].children) {
						nodeItem = this.appendChildrenNode(_items[i].children, nodeItem);
						delete _items[i].children;
						nodeItem.ftype = _items[i].type;
						nodeItem.elConfig = _items[i];
						rptNode.appendChild(nodeItem);
					}
				}
				this.treePanel.root.appendChild(rptNode);
				rptNode.elConfig = vdata;
				this.doUpdateForm();
			},
			// 无数据时设置默认数据
			setDefaultData : function(){
				var scope = this;
				var _root = Ext.apply({},scope.rootConfig);
				var rootNode = new Ext.tree.TreeNode({
							id : scope.getNewId(),
							text : '报表视图'
						});
				rootNode.ftype = 'Root';
				rootNode.elConfig = _root;
				scope.treePanel.root.appendChild(rootNode);
				var _panel = Ext.apply({},scope.panelConfig);
				var panelNode = new Ext.tree.TreeNode({
							id : scope.getNewId(),
							text : 'Panel'
						});
				panelNode.ftype = 'Panel';
				panelNode.elConfig = _panel;
				rootNode.appendChild(panelNode);
				this.doUpdateForm();
			},
			doSave : function() {

			},
			/**
			 * 获取视图配置dom树上的信息，可以作为配置提交的参数使用。
			 * 
			 * @return {}
			 */
			getViewDom : function() {
				var root = this.treePanel.getRootNode();
				var params = {};
				if (root.firstChild) {
					params = this.encodeParam(params, 'report.view', root.firstChild.elConfig);
					for (var i = 0; i < root.firstChild.childNodes.length; i++) {
						var node = root.firstChild.childNodes[i];
						params = this.getConfig(params, 'report.view.mainuivo', node, undefined);
					}
				}
				return params;
			},
			//提交dom数并获取预览界面
			doUpdateForm : function() {
				Ext.Ajax.request({
							scope : this,
							params : this.getViewDom(),
							url : __ctxPath + '/report/setting/preview.do',
							success : function(response, opts) {
								try {
									var json = Ext.util.JSON.decode(response.responseText);
									if (json.success) {
										this.updateBuilderPanel(json.result);
									}
								} catch (e) {
									Ext.MessageBox.alert('模板配置错误', '模板设置出错，预览失败！<br/>错误消息：' + e);
									throw e;
								}
							},
							failure : function(response, opts) {

							}
						});
			},
			updateBuilderPanel : function(config) {
				while (this.builderPanel.items && this.builderPanel.items.first()) {
					this.builderPanel.remove(this.builderPanel.items.first());
				}
				if(config)
					this.builderPanel.add(config);
				this.builderPanel.doLayout();
			}
		});