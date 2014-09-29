/**
 * @inclule "../../../ext-oecp/ui/GridPanel.js"
 */
Ext.ns("OECP.bpm.instance");

/**
 * 流程实例页面
 * 
 * @author yangtao
 * @class OECP.bpm.ProcessInstanceView
 * @extends OECP.ui.GridPanelView
 */
OECP.bpm.ProcessActivityView =  Ext.extend(Ext.Panel, {
	id : "ProcessActivityView",
	closable : true,
	//面板布局
	layout:'fit',
    
	autoScroll : true,
	reset : function() {
		
	},
	// 查询
	search : function() {
		
	},
	// 删除
	removeData : function() {
	},
	// 导出
	exportExcel : function() {
	},
	// 更新
	upload : function() {
	},
	//新建
	view : function() {
		
		
	},
	//停止流程实例
	stop : function(){
		
	},
	//列表顶部工具
	getTopbar : function() {
		
	},
	
	// 初始化方法
	initComponent : function() {
		//初始化父类
		OECP.bpm.ProcessActivityView.superclass.initComponent.call(this);
		
		//查询列表
		this.store = new Ext.data.JsonStore({
			url :__fullPath+"/bpm/def/getTaskActivity.do?deployId="+this.deployId,
			root : "result",
			remoteSort : true,
			fields : ["deployId","processName","activityId", "activityName"],
			totalProperty : 'totalCounts'
		});
		var obj=[{
			header : "实例ID",
			dataIndex : "deployId",
			width : 100
		}, {
			header : "流程名称",
			dataIndex : "processName",
			width : 100
		},{
			header : "节点名称",
			dataIndex : "activityName",
			width : 300
		}, {
			header : "节点状态",
			dataIndex : "activityId",
			hidden : true,
			width : 300
		},{
				header : "管理",
				dataIndex : "management",
				renderer : function() {
					var h = "";
					h += '<a href="javascript:void(0)" onclick="OECP.ui.TreeGridView.roleDistribution(\'\')">分配</a>';
					return h;
				}
			}];
		var sm = new Ext.grid.CheckboxSelectionModel();
		var columns = new Array();
		var fields = new Array();
		columns[0] = sm;
		for (var i = 0; i < obj.length; i++) {
			var headItem = obj[i];
			var title = headItem.header;
			var alignStyle = 'left';

			var propertyName = headItem.dataIndex;

			var isHidden = headItem.hidden;
			
			var columnItem = {
				header : title,
				dataIndex : propertyName,
				sortable : true,
				hidden : isHidden,
				align : alignStyle,
				width : headItem.width,
				renderer : headItem.renderer
			};
			columns[i + 1] = columnItem;
			fields[i] = propertyName;
		}

		this.storeMapping = fields;
		this.cm = new Ext.grid.ColumnModel(columns);

		
		this.store.paramNames.sort = "orderBy";
		this.store.paramNames.dir = "orderType";
		this.cm.defaultSortable = true;

		var viewConfig = Ext.apply({
			forceFit : true
		}, this.gridViewConfig);

		this.pageBar = new Ext.PagingToolbar({
			pageSize : 10,
			store : this.store,
			displayInfo : true,
			displayMsg : '当前显示 {0}-{1}条记录 /共{2}条记录',
			emptyMsg : "无显示数据"
		});
		this.formValue = '';
		this.pageBar.formValue = this.formValue;
		this.topbar = this.getTopbar();
		this.grid = new Ext.grid.GridPanel({
			title : '流程节点配置',
			height : 400,
			store : this.store,
			cm : this.cm,
			sm : sm,
			trackMouseOver : false,
			loadMask : true,
			tbar : this.topbar,
			bbar : this.pageBar
		});
		// this.grid.on("rowdblclick",this.openDetail,this);

		this.add(this.grid);
		
		var param = {
			
		};
		param.processName = this.processName;
		this.formValue = param;
		this.pageBar.formValue = this.formValue;
		this.store.removeAll();
		this.store.load({
			params : param
		});
	},
	fitWidth : function(grid, columnIndex, e) {
		var c = columnIndex;
		var w = grid.view.getHeaderCell(c).firstChild.scrollWidth;
		for (var i = 0, l = grid.store.getCount(); i < l; i++) {
			w = Math.max(w, grid.view.getCell(i, c).firstChild.scrollWidth);
		}
		grid.colModel.setColumnWidth(c, w);
	}

});
