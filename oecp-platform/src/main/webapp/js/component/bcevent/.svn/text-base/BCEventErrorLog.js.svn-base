Ext.ns("OECP.bcevent");

/**
 * 
 * @class OECP.bcevent.BCEventErrorLog
 * @extends Ext.Panel
 */

OECP.bcevent.BCEventErrorLog = Ext.extend(Ext.Panel, {
	id : 'OECP.bcevent.BCEventErrorLog',
	layout : 'border',
	// 标题
	title : '事件错误日志',
	// 获取错误日志列表URL
	listUrl : __fullPath + 'eventConfige/errors.do',
	restartUrl : __fullPath + 'eventConfige/restartEvent.do',
	// 设置复选框还是单选，默认false多选。
	selectCheckboxModel : true,
	width : '99%',
	/**
	 * 
	 * @type 列表数据项
	 */
	filedDate : ['id','beanClassName','methodName','status'],
	structure : [],
	grid : null,
	/**
	 * 加载方法
	 */
	initComponent : function(){
		var master = this;
		// 初始化数据仓库
		master.store = new Ext.data.JsonStore({
			storeId : 'id',
			root : 'result',
			url : master.listUrl,
			totalProperty : "totalCounts",
			autoLoad : true,
			fields : master.filedDate
		});
		master.store.load({params:{start:0,limit:25	}});
		// 按钮
		var toolBar = new Ext.Toolbar({
			items : [ {
				text : '启动',
				tooltip : '重新执行选中的的监听器',
				iconCls : 'btn-edit',
				listeners : {
					'click' : function(btn, e) {
						var rows = master.grid.getSelectionModel()
								.getSelections();
						if (rows.length == 0) {
							Ext.Msg.alert("提示信息", "请选择一条记录！");
						} else {
							var recordes = Array();
							for (var i = 0, r; r = rows[i]; i++) {
								recordes.push(r.id);
							}
							master.reStart(recordes);
						}
					}
				}
			} ]
		});
		// 初始化列模式
		var sm = new Ext.grid.CheckboxSelectionModel({
			singleSelect : master.selectCheckboxModel
		});
		master.structure = [ sm, new Ext.grid.RowNumberer(), {
			header : '主键',
			dataIndex : 'id',
			hidden : true,
			unShowAble : true
		}, {
			header : '业务Bean类名称',
			dataIndex : 'beanClassName',
			width : 260,
			sortable : true,
			required : true,
			type : 'string'
		}, {
			header : '方法名称',
			dataIndex : 'methodName',
			width : 260,
			sortable : true,
			required : true,
			type : 'string'
		}, {
			header : '状态',
			dataIndex : 'status',
			width : 260,
			sortable : true,
			required : true,
			type : 'string'
		} ];
		// 把列表编号，复选框放入列中
		var columns = master.structure;
		master.grid = new Ext.grid.EditorGridPanel({
			height : 550,
			store : master.store,
			cm : new Ext.grid.ColumnModel({
						columns : columns
					}),
			sm : sm,
			tbar : toolBar,
			bbar : new Ext.PagingToolbar({
				pageSize : 25,
				store : master.store,
				displayInfo : true,
				displayMsg : '当前显示从{0}至{1}， 共{2}条记录',
				emptyMsg : "当前没有记录"
			})
		});
		this.items = [master.grid];
		OECP.bcevent.BCEventErrorLog.superclass.initComponent.call(this);
	},
	// 重新运行
    reStart : function(ids){
    	var master = this;
    	Ext.Msg.confirm("信息确认", "您确认要重新运行所选事件监听器吗？", function(confirm) {
    		if (confirm == 'yes') {
				Ext.Ajax.request({
					url : master.restartUrl,
					params : {
						ids : ids
					},
					method : "POST",
					success : function(res) {
						var msg = Ext.util.JSON.decode(res.responseText);
						if (msg.success) {
							Ext.ux.Toast.msg("信息", msg.msg);
							master.grid.getStore().reload();
						} else {
							Ext.Msg.show({title : "错误",msg : msg.msg,buttons : Ext.Msg.OK});
						}
					},
					failure : function() {
						Ext.Msg.show({title : "错误",msg : '提交失败，您的网络可能不通畅，请稍后再试。',buttons : Ext.Msg.OK});
					}
				});
    		}
    	});
    }
});