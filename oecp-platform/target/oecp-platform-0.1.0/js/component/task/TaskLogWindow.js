Ext.ns("OECP.Task")

OECP.Task.TaskLogWindow=Ext.extend(Ext.Window,{
	title:'查看任务日志窗口',
	modal : true,
	frame : true,
	autoScroll : true,
	closeAction:'close',
	buttonAlign : 'center',
	width:700,
	height:300,
	/**
	 * 初始化方法
	 */
	initComponent:function(){
		var master = this;
		this.initGridPanel(master);
		this.initBtns();
		this.items = [master.TaskLogGridPanel];
		OECP.Task.TaskLogWindow.superclass.initComponent.call(this);
	},
	/**
	 * 初始化时间模板明细子表
	 * @param {} master
	 */
	initGridPanel : function(master){
		var sm = new Ext.grid.CheckboxSelectionModel();
		var cm = new Ext.grid.ColumnModel([new Ext.grid.RowNumberer(),
				sm, {
					header : '任务日志id',
					dataIndex : 'id',
					hidden : true
				} ,{
					header : '日志内容',
					dataIndex : 'content',
					width : 380
				},{
					header : '任务开始时间',
					dataIndex : 'beginTime',
					width : 120
				},{
					header : '任务结束时间',
					dataIndex : 'endTime',
					width : 120
				}]);
		var store = new Ext.data.JsonStore({
				storeId : 'id',
				totalProperty : 'totalCounts',
				root : 'result',
				url : __ctxPath + '/task/manage/viewlog.do',
				baseParams:{
					'task.id':master.taskId
				},
				autoLoad : true,
				fields : ["id","content", "beginTime","endTime"]
		});
		master.TaskLogGridPanel = new Ext.grid.GridPanel({
			region : 'center',
			height : 235,
			autoWidth : true,
			autoScroll : true,
			store : store,
			cm : cm,
			sm : sm
		});
		
	},
	/**
	 * 初始化按钮
	 */
	initBtns:function(){
		var master=this;
		
		var closeBtn = new OECP.ui.Button({
				text : "关闭",
				iconCls : 'btn-cancel',
				handler : function(){
					master[master.closeAction]();
				}
		});
		master.buttons=[closeBtn];
		
	}
});