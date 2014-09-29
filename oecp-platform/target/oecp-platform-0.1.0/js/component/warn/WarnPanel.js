Ext.ns("OECP.Warn")

/**
 * 预警配置面板
 * @class OECP.Warn.WarnPanel
 * @extends Ext.Panel
 */
OECP.Warn.WarnPanel = Ext.extend(Ext.Panel, {
	id : 'OECP.Warn.WarnPanel',
	title : '预警信息',
	layout : 'fit',
	/**
	 * 初始化方法
	 */
	initComponent : function(){
		this.WarnFormPanel = new Ext.FormPanel({
	        labelWidth: 80, // label settings here cascade unless overridden
	        frame:true,
	        bodyStyle:'padding:5px 5px 0',
	        width: 350,
	        defaults: {width: 230},
	        defaultType: 'textfield',
	        reader : new Ext.data.JsonReader({
				root : 'result'
			}, [{
				name : 'warn.id',
				mapping : 'id'
			},{
				name : 'warn.name',
				mapping : 'name'
			}, {
				name : 'warn.warningitf',
				mapping : 'warningitf'
			}, {
				name : 'warn.noticeNum',
				mapping : 'noticeNum'
			}]),
	        items: [{
                	xtype:'hidden',
	                name: 'warn.id'
	            },{
	                fieldLabel: '预警名称',
	                name: 'warn.name',
	                allowBlank:false,
	                width:300
	            },{
	                fieldLabel: '预警插件',
	                name: 'warn.warningitf',
	                allowBlank:false,
	                width:300
	            },{
		            xtype: 'checkboxgroup',
		            fieldLabel: '通知方式',
		            items: [
		                {boxLabel: '门户消息通知', name: 'warn.warnNoticeItem',inputValue:OECP.Warn.StaticParam.warnNoticePortal},
		                {boxLabel: '邮件通知', name: 'warn.warnNoticeItem',inputValue:OECP.Warn.StaticParam.warnNoticeEmail},
		                {boxLabel: '短信通知', name: 'warn.warnNoticeItem',inputValue:OECP.Warn.StaticParam.warnNoticeMessage}
		            ]
		        },{
	                fieldLabel: '通知最多次数',
	                name: 'warn.noticeNum'
	        }]
	    });
		this.items = [this.WarnFormPanel]; 
		OECP.Warn.WarnPanel.superclass.initComponent.call(this);
	},
	/**
	 * 获取预警信息form的数据
	 * @return {}
	 */
	getFormValues : function(){
		var result = new Array();
		var noticeNum = this.WarnFormPanel.form.findField('warn.noticeNum').getValue();
		var warningitf = this.WarnFormPanel.form.findField('warn.warningitf').getValue();
		var name = this.WarnFormPanel.form.findField('warn.name').getValue();
		if(Ext.isEmpty(name)){
		 	Ext.Msg.alert('提示','预警名称不能为空');
		 	result[0] = false;
		 	return result;
		}else{
		 	result[0] = true;
		}
		if(Ext.isEmpty(warningitf)){
		 	Ext.Msg.alert('提示','预警插件不能为空');
		 	result[0] = false;
		 	return result;
		}else{
		 	result[0] = true;
		}
		if(!Ext.isEmpty(noticeNum)&&!(/^\d{0,10}$/.test(noticeNum))){
		 	Ext.Msg.alert('提示','通知最多次数为数字，并且不能超过10位数');
		 	result[0] = false;
		 	return result;
		}else{
		 	result[0] = true;
		}
		result[1] = this.WarnFormPanel.getForm().getValues();
		return result;
	}
});