Ext.ns("OECP.portlet");

/**
 * 公告小窗
 * @class OECP.notice.NoticeView
 * @extends Ext.Panel
 */
OECP.portlet.NoticeView = Ext.extend(Ext.ux.Portlet, {
//	layout : 'fit',
//	title : '公告发布',
	listUrl : __ctxPath +'/notice/manage/list.do',
	fieldData : ["id","title","message","createcorp","createUser","createTime","author","publishDate","isStart","discorp"],
	//listGroupUrl : __ctxPath +'/task/manage/listgroup.do',
	/**
	 * 任务组下拉页面记录数
	 */
	comboPageNum : 10,
	/**
	 * 列表页面记录数
	 * @type Number
	 */
	pageNum : 10,
	
	/**
	 * 初始化方法
	 */
	initComponent : function(){
		var master = this;
		// 初始化数据仓库
		master.store = new Ext.data.JsonStore({
					storeId : 'id',
					root : 'result',
					url : master.listUrl,
					totalProperty : "totalCounts",
					fields : master.fieldData,
					baseParams : {
						limit:master.pageNum,type:"0"
					}
		});
		// 初始化列模式
		var sm = new Ext.grid.CheckboxSelectionModel({
		//	singleSelect : master.selectCheckboxModel
		});
		var sm = new Ext.grid.CheckboxSelectionModel();
		this.cm = new Ext.grid.ColumnModel([{
			header : '主键',
			dataIndex : 'id',
			hidden : true,
			unShowAble : true
		}, {
            xtype: 'actioncolumn',
            width: 50,
            items: [{
                icon   : __fullPath+"/images/btn/mail/mail_send.png",  // Use a URL in the icon config
                tooltip: '公告',
                width : 50,
                handler: function(grid, columnIndex, colIndex) {
                    var billKey = grid.getStore().getAt(rowIndex).data.billKey;
                    scope.viewHistory(billKey);
                }
            }]},{
			header : '公告标题',
			dataIndex : 'title',
			tooltip: '公告',
			width : 150,
			type : 'string',
			handler: function(grid, dataIndex, colIndex,e) {
                var billKey = grid.getStore().getAt(rowIndex).data.billKey;
                scope.viewHistory(billKey);
            }
		},
//		{
//			header : '公告内容',
//			dataIndex : 'message',
//			Showheader:false,
//			width : 150,
//			type : 'string'
//		},
		{
			header : '创建公司',
			dataIndex : 'createcorp',
			width : 150,
			ShowHeader:false,
			type : 'string'
		}]);
		
		var columns = master.structure;
		var pageBar = new Ext.PagingToolbar({
			pageSize : master.pageNum,
			store : master.store,
			displayInfo : true,
			displayMsg : '当前显示 {0}-{1}条记录 /共{2}条记录',
			emptyMsg : "无显示数据"
		});
		
		// 初始化Grid
		master.grid = new Ext.grid.GridPanel({
			height : 500,
			hideHeaders :true,
			border : false, 
			hideBorders :false,
			bodyBorder : false, 
			shim : false,
			//hideMode : offsets,
			store : master.store,
			cm:this.cm,
//			cm : new Ext.grid.ColumnModel({
//						columns : columns
//					}),
//			sm : sm,
			loadMask : true,
//			bbar : pageBar
		});
		//双击
		master.grid.on("cellclick",function(){
			var record = master.grid.getSelectionModel().getSelected();
			if (!record) {
				Ext.Msg.alert("提示", "请先选择任务!");
				return;
			}
			var id = record.get('id');
			var isStart = record.get('isStart');
			if(isStart=='1'){
				var noticeWin = new OECP.portlet.PortalNoticeWindow({isEdit:true,isView:true,noticeId:id});
				    noticeWin.show();
			}else{
				var noticeWin = new OECP.portlet.PortalNoticeWindow({isEdit:true,isView:false,noticeId:id});
				noticeWin.show();
			}
		},this);
		OECP.portlet.NoticeView.superclass.initComponent.call(this);
		this.add(this.grid);
//		master.store.removeAll();
		master.store.load();
	}
});
Ext.reg('portlet_notice', OECP.portlet.NoticeView);