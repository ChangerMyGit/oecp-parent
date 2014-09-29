Ext.ns("OECP.Task")

/**
 * 新增任务组窗口
 * @class OECP.Task.AddGroupWindow
 * @extends OECP.ui.CommonWindow
 */
OECP.Task.AddGroupWindow = Ext.extend(OECP.ui.CommonWindow,{
	width : 250,
	height : 150,
	
	/**
	 * 保存URL
	 * @type 
	 */
	saveUrl : __ctxPath +'/task/manage/groupsave.do',
	
	/**
	 * 初始化方法
	 */
	initComponent : function(){
		var me = this;
		
		this.initWindowBtns(me);
		this.initContent(me);
		this.addEvents("doAfterSaved");
		OECP.Task.AddGroupWindow.superclass.initComponent.call(this);
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
					var data = me.formpanel.getForm().getValues();
					var name = me.formpanel.form.findField('taskGroup.name').getValue();
					if(Ext.isEmpty(name)){
					 	Ext.Msg.alert('提示','任务组名称不能为空');
					 	return;
					}
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
		if(!Ext.isDefined(me.formpanel)){
			me.formpanel = new Ext.FormPanel({
		        labelWidth: 70, 
		        frame:true,
		        width:230,
		        height:80,
		        defaultType: 'textfield',
		        items:[{
	                fieldLabel: '任务组名称',
	                name: 'taskGroup.name',
	                allowBlank:false,
	                width:100
	            }]
		 });
		}
		
		me.componetArray = [me.formpanel];
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
	}
});