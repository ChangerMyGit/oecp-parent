/**
 * @inclule "../../../ext-oecp/ui/GridPanel.js"
 */
Ext.ns("OECP.bpm.instance");

/**

 * 流程查看历史页面
 * 
 * @author yangtao
 * @class OECP.bpm.ProcessInstanceHisView
 * @extends Ext.Panel
 */
OECP.bpm.ProcessInstanceHisView =  Ext.extend(Ext.Panel, {
	/**
	 * id
	 */
	id : "ProcessInstanceHisView",
	/**
	 * 关闭
	 */
	closable : true,
	/**
	 * 面板布局
	 */
	layout:'table',
	/**
	 * 面板布局配置
	 */
    layoutConfig: {
        columns: 1
    },
    /**
     * 滚动条
     */
	autoScroll : true,
	
	/**
	 * 初始化方法
	 */
	initComponent : function() {
		/**
		 * 接收参数
		 */
		var scope = this;
		var historyId = this.historyId;
		var billKey = this.billKey;
		/**
		 * 初始化父类
		 */
		OECP.bpm.ProcessInstanceHisView.superclass.initComponent.call(this);
		
		/**
		 * 历史流程图
		 */
		var timestamp = Date.parse(new Date());
		var panel = new Ext.Panel({
					title : "当前流程进行时刻图",
//					height : 500,
//					width : 850,
					frame : true,
					bodyStyle:'padding:0 0 0 0',
					nocache: true,
					autoScroll : true,
					//html:'<image src="'+__fullPath+'/bpm/instance/getImage2.do?deployId='+this.deployId+'"></image>'
					autoLoad :{url:__fullPath+'/bpm/instance/getHistoryImage.do?billKey='+billKey+"&stamp="+timestamp}
		});   
		
		/**
		 * store
		 */
		this.store = new Ext.data.JsonStore({
			url :__fullPath+"bpm/instance/history.do",
			baseParams : {
						billKey:billKey,
						stamp:timestamp
			},
			root : "result",
			remoteSort : true,
			fields : ["id","taskId","processInstanceId","activityName","status","startTime","endTime","auditUserName","auditDecision","auditOpinion","agentUserName"],
			totalProperty : 'totalCounts'
		});
		
		var sm = new Ext.grid.CheckboxSelectionModel();
		// 行号
		var _rownum = new Ext.grid.RowNumberer({
					hidden : false
		});
		this.cm = new Ext.grid.ColumnModel([_rownum,{
			header : "任务ID",
			dataIndex : "taskId",
			hidden :true,
			width : 100
		},{
			header : "实例ID",
			dataIndex : "processInstanceId",
			hidden :true,
			width : 100
		}, {
			header : "节点名称",
			dataIndex : "activityName",
			width : 100
		}, {
			header : "节点状态",
			dataIndex : "status",
			width : 150
		}, {
			header : "开始时间",
			dataIndex : "startTime",
			width : 140
		}, {
			header : "结束时间",
			dataIndex : "endTime",
			width : 140
		}, {
			header : "应办人",
			dataIndex : "auditUserName",
			width : 100
		},{
			header : "实际办理人",
			dataIndex : "agentUserName",
			width : 100
		}, {
			header : "审批决定",
			dataIndex : "auditDecision",
			width : 100
		}, {
			header : "审批意见",
			dataIndex : "auditOpinion",
			width : 200
		}]);
		
		/**
		 * 列表底部工具栏
		 */
		this.pageBar = new Ext.PagingToolbar({
			store : this.store,
			displayInfo : true,
			displayMsg : '当前显示 {0}-{1}条记录 /共{2}条记录',
			emptyMsg : "无显示数据"
		});
		
		/**
		 * 列表grid
		 */
		this.grid = new Ext.grid.GridPanel({
			title : '流程任务审批历史详细信息',
			height : 200,
//			width : 850,
			store : this.store,
			cm : this.cm,
			trackMouseOver : false,
			loadMask : true
		});
		if(typeof(historyId) != "undefined"){//查看已办历史时查出当前任务在审批历史中的位置
			this.grid.store.on('load', function(store, records, options) {
		        Ext.each(records, function(rec) {
		        	if (historyId==rec.get('id')) {
		        		scope.grid.getView().getRow(scope.grid.store.indexOf(rec)).style.backgroundColor = '#FFFF00';
			        }  
		        }); 
	      	},this,{delay:300});
		}
		

		/**
		 * 向容器里面增加组件
		 */
		this.add(panel);
		this.add(this.grid);
		
		/**
		 * 列表加载数据
		 */
		this.store.removeAll();
		this.store.load();
	}
});
