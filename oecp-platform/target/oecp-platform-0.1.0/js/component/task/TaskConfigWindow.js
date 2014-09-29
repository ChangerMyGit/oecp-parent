Ext.ns("OECP.Task")

/**
 * 任务窗口 新增、编辑
 * @class OECP.Task.TaskConfigWindow 
 * @extends OECP.ui.CommonWindow
 */
OECP.Task.TaskConfigWindow = Ext.extend(OECP.ui.CommonWindow,{
	width : 600,
	height : 520,
	title : '任务',
	/**
	 * 是否是编辑页面
	 * @type 
	 */
	isEdit : null,
	/**
	 * 编辑时，任务ID
	 * @type 
	 */
	taskId : null,
	
	/**
	 * 保存URL
	 * @type 
	 */
	saveUrl : __ctxPath +'/task/manage/save.do',
	/**
	 * 编辑时加载数据
	 * @type 
	 */
	loadUrl : __ctxPath + '/task/manage/loadData.do',
	/**
	 * 初始化方法
	 */
	initComponent : function(){
		var me = this;
		this.isEdit = this.isEdit;
		this.taskId = this.taskId;
		this.isView = this.isView;
		this.initWindowBtns(me);
		this.initContent(me);
		if(this.isEdit)
			this.putValues(me);
		this.addEvents("doAfterSaved");
		OECP.Task.TaskConfigWindow.superclass.initComponent.call(this);
	},
	/**
	 * 初始化窗口里面的按钮
	 * @param {} me
	 */
	initWindowBtns : function(me){
		var saveBtn = new OECP.ui.Button({
				text : "保存",
				iconCls : 'btn-save',
				handler : function(){
					var data = me.taskPanel.getFormValues();
					if(!data[0])
						return;
					var data2 = me.timerPanel.getFormValues();
					if(data2[0])
						data = Ext.apply(data[1],data2[1]);
					else
						return;
					//保存
					me.doSave(data,me);
				}
		});

		var closeBtn = new OECP.ui.Button({
				text : "关闭",
				iconCls : 'btn-cancel',
				handler : function(){
					me.close();
				}
		});
		if(this.isView)
			me.buttonArray = [closeBtn];
		else 
			me.buttonArray = [saveBtn,closeBtn];
	},
	/**
	 * 初始化窗口里面的内容
	 * @param {} me
	 */
	initContent : function(me){
		if(!Ext.isDefined(me.taskPanel)){
			me.taskPanel = new OECP.Task.TaskPanel({isView:me.isView});
		}
		if(!Ext.isDefined(me.timerPanel)){
			me.timerPanel = new OECP.Task.TimerPanel();
		}
		if(!Ext.isDefined(me.tabPanel)){
			me.tabPanel = new Ext.TabPanel({
				region : 'center',
				height : 450,
				frame : true,
			    activeTab: 0,
			    //默认是true,只激活activeTab指定的panel，设为false，所有panel都激活
			    deferredRender : false,
			    items: [me.taskPanel,me.timerPanel]
			});
		}
		
		me.componetArray = [me.tabPanel];
	},
	/**
	 * 保存
	 * @param {} data
	 */
	doSave : function(data,me){
		Ext.Msg.wait('正在保存，请稍候......', '提示');
		Ext.Ajax.request({
			url : me.saveUrl,
			params : data,
			success : function(request) {
				Ext.Msg.hide();
				var json = Ext.decode(request.responseText);
				if (json.success) {
					Ext.ux.Toast.msg('信息',	json.msg);
					me.fireEvent('doAfterSaved');
					me.close();
				} else {
					Ext.Msg.show({title:'错误',msg:json.msg,buttons:Ext.Msg.OK	});
				}
			},
			failure : function(request) {
				Ext.Msg.hide();
				var json = Ext.decode(request.responseText);
				Ext.Msg.show({title:'错误',msg:json.msg,buttons:Ext.Msg.OK});
			}
		});
	},
	/**
	 * 编辑页面赋值
	 * @param {} me
	 */
	putValues : function(me){
		Ext.Ajax.request({
			url : me.loadUrl,
			params : {
				"taskId":me.taskId
			},
			success : function(request) {
				var json = Ext.decode(request.responseText);
				if (json.success) {
					var rs = me.taskPanel.TaskFormPanel.reader.readRecords(json);
					var _data = rs.records && rs.records[0] ? rs.records[0].data : null;
					me.taskPanel.TaskFormPanel.getForm().setValues(_data);
					//给任务组combo赋值
					var id = json.result.oecpTaskGroup.id;
					var name = json.result.oecpTaskGroup.name;
					var data  = new me.taskPanel.groupCombo.store.recordType({id:id,name:name});
					me.taskPanel.groupCombo.store.add(data);
					me.taskPanel.groupCombo.initLoadData = true;
					me.taskPanel.groupCombo.setValue(id);
					
					var rs = me.timerPanel.TimerFormPanel.reader.readRecords(json);
					var _data = rs.records && rs.records[0] ? rs.records[0].data : null;
					me.timerPanel.TimerFormPanel.getForm().setValues(_data);
				} else {
					Ext.Msg.show({title:'错误',msg:json.msg,buttons:Ext.Msg.OK	});
				}
			},
			failure : function(request) {
				var json = Ext.decode(request.responseText);
				Ext.Msg.show({title:'错误',msg:json.msg,buttons:Ext.Msg.OK});
			}
		});
	}
});