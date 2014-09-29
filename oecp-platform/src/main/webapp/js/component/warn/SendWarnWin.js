Ext.ns('OECP.Warn');

/**
 * 预警消息窗口
 * 
 * @author yangtao
 * @class OECP.Warn.SendWarnWin 
 * 
 */
OECP.Warn.SendWarnWin = Ext.extend(Ext.Window, {
	id : 'OECP.Warn.SendWarnWin',
	title : '预警',
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
				url : __ctxPath + "/warn/manage/getSendWarns.do",
				fields : ['id', 'title','messageContent','noticedNum']
			}),
	initComponent : function() {
		var master = this;
		OECP.Warn.SendWarnWin.superclass.initComponent.call(this);
		this.store.reload();
		var cm = new Ext.grid.ColumnModel([new Ext.grid.RowNumberer(),  {
					header : '预警标题',
					dataIndex : 'title',
					width : 170
				}]);
		/**
		 * 消息目录面板
		 */
		var westPanel = new Ext.grid.GridPanel({
			title : '预警目录',
			region : 'west',
			collapsible : true,
			width : 200,
			cm : cm,
			store : master.store,
			listeners : {
				'rowclick' : function(grid, rowIndex, r) {
					var content = master.store.getAt(rowIndex).get('messageContent')+
							"</br></br></br>该预警已经给您发送的次数是:"
							+ master.store.getAt(rowIndex).get('noticedNum');
					content = content.replace(/\n/g, "<br/>");
					content = content.replace(/\s/gi, "&nbsp");
					centerPanel.get(1).body.update(content);
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
					title : '预警内容',
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
							html : '欢迎使用预警管理器.请根据预警内容，去做业务处理，来取消预警',
							x : 20,
							y : 60,
							anchor : '95%'
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
