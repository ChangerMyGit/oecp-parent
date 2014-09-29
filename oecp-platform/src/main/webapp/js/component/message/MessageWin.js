Ext.ns('OECP.message');

/**
 * 消息窗口
 * 
 * @author lintao
 * @class OECP.message.MessageWin
 * 
 */
OECP.message.MessageWin = Ext.extend(Ext.Window, {
	id : 'OECP.message.MessageWin',
	title : '消息',
	closable : true,
	width : 600,
	height : 350,
	layout : 'border',
	btns : {},//查看按钮  
	openPath : null,
	billId : null,
	frame : true,
	border : true,
	modal : true,
	store : new Ext.data.JsonStore({
				storeId : 'id',
				url : __ctxPath + "/message/findMessages.do",
				fields : ['id', 'title', 'content', 'createtime','billId','openPath','frame']
			}),
	initComponent : function() {
		var master = this;
		OECP.message.MessageWin.superclass.initComponent.call(this);
		this.store.reload();
		var cm = new Ext.grid.ColumnModel([new Ext.grid.RowNumberer(),  {
					header : '消息标题',
					dataIndex : 'title',
					width : 170
				}]);
		/**
		 * 消息目录面板
		 */
		var westPanel = new Ext.grid.GridPanel({
			title : '消息目录',
			region : 'west',
			collapsible : true,
			width : 200,
			cm : cm,
			store : master.store,
			listeners : {
				'rowclick' : function(grid, rowIndex, r) {
					var content = master.store.getAt(rowIndex).get('content')+
							"</br>消息发送时间是:"
							+ master.store.getAt(rowIndex).get('createtime');
					content = content.replace(/\n/g, "<br/>");
					content = content.replace(/\s/gi, "&nbsp");
					centerPanel.get(1).body.update(content);
					master.openPath = master.store.getAt(rowIndex).get('openPath');
					master.billId = master.store.getAt(rowIndex).get('billId');
					master.frame = master.store.getAt(rowIndex).get('frame');
					if(master.openPath != null){
						centerPanel.get(2).show();
					}else{
						centerPanel.get(2).hide();
					}
					var id  = master.store.getAt(rowIndex).get('id');
					Ext.Ajax.request({
						url : __ctxPath + "/message/updateMessages.do",
						method : "POST",
						params : {contentId : id},
						scope : this
					});
				}
			}
		});

		/**
		 * 消息正文面板
		 */
		var centerPanel = new Ext.Panel({
					id : 'contentPanel',
					region : 'center',
					layout : 'absolute',
					title : '消息内容',
					items : [
						 {
							xtype : 'panel',
							border : false,
							html : '尊敬的用户:',
							y: 25,
							anchor : '40%'
						},{
							xtype : 'panel',
							border : false,
							html : '欢迎使用消息管理器.',
							x : 20,
							y : 60,
							anchor : '60%'
						},{
							xtype : 'button',
							text : '查看',
							hidden : true
						}
					]
				});
		//将查看按钮赋值给btns属性
		master.btns = centerPanel.get(2);
		/**
		 * 添加面板
		 */
		this.add(westPanel);
		this.add(centerPanel);

	}

});
