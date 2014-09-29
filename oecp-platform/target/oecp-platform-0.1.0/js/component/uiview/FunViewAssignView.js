// 界面视图分配
Ext.ns('OECP.uiview');
OECP.uiview.FunViewAssignView = Ext.extend(Ext.Panel, {
	id : 'OECP.uiview.FunViewAssignView',
	title : '界面视图分配',
	treePanel : null,
	bcId : null,
	layout : 'border',
	accordionTreePanel : null,
	uiviewPanel : null,
	getFunctionViewsUrl : __ctxPath + '/funviewassign/getFunctionViews.do',
	initComponent : function() {
		var master = this;
		master.accordionTreePanel = new OECP.ui.AccordionTreePanel({
					width : 150,
					title : '组件功能',
					accordionUrl : __ctxPath + '/function/listAllBCs.do',
					treeUrl : __ctxPath + '/function/fuctionTreeCode.do',
					treeEvent : {
						'click' : function(node) {
							if (node.id != 'root' && node.leaf == true) {
								master.uiview_store.baseParams.functionId = node.id;
								master.uiview_store.load();
							}
						}
					}
				});

		master.uiview_store = new Ext.data.JsonStore({
					url : master.getFunctionViewsUrl,
					baseParams : {
						functionId : ""
					},
					fields : ['id', 'viewcode', 'viewname', 'org.name',
							'shared', 'func.code']
				});

		master.uiview_sm = new Ext.grid.CheckboxSelectionModel();

		master.uiview_cm = new Ext.grid.ColumnModel([master.uiview_sm,
				new Ext.grid.RowNumberer(), {
					header : "主键",
					dataIndex : "id",
					hidden : true
				}, {
					header : "功能视图编号",
					dataIndex : "viewcode"
				}, {
					header : "功能视图名称",
					dataIndex : "viewname"
				}, {
					header : "创建公司",
					dataIndex : "org.name"
				}, {
					header : "是否共享",
					dataIndex : "shared"
				}]);

		master.assignBtn = new Ext.Button({
			text : "分配",
			listeners : {
				'click' : function() {
					var ui = master.uiviewPanel.getSelectionModel()
							.getSelections();
					if (ui.length == 1) {
						master.win.show();
						master.win.bizType_store.baseParams.functionCode = ui[0].data['func.code'];
						master.win.bizType_store.load();

						master.win.bizType_sm.clearSelections();
						master.win.functionViewId = ui[0].data['id'];
						master.win.bizTypeId = '';
						master.win.idscheck();
					}
				}
			}
		});

		master.uiviewPanel = new Ext.grid.GridPanel({
					title : '界面视图',
					region : 'center',
					enableDD : false,
					enableDrag : false,
					autoScroll : true,
					sm : master.uiview_sm,
					cm : master.uiview_cm,
					store : master.uiview_store,
					tbar : [master.assignBtn]
				});

		master.win = new OECP.uiview.FunViewAssignWindow();

		this.items = [master.accordionTreePanel, master.uiviewPanel];
		OECP.uiview.FunViewAssignView.superclass.initComponent.call(this);
	}
});
