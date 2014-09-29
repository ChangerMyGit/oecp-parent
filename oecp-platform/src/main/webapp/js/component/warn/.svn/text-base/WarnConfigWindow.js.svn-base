Ext.ns("OECP.Warn")

/**
 * 预警窗口 新增、编辑
 * @class OECP.Warn.WarnConfigWindow
 * @extends OECP.ui.CommonWindow
 */
OECP.Warn.WarnConfigWindow = Ext.extend(OECP.ui.CommonWindow,{
	width : 600,
	height : 520,
	title : '预警',
	/**
	 * 是否是编辑页面
	 * @type 
	 */
	isEdit : null,
	/**
	 * 编辑时，预警ID
	 * @type 
	 */
	warnId : null,
	/**
	 * 预警类型set
	 * @type 
	 */
	fieldSet : undefined,
	/**
	 * 窗口里面内容
	 * @type 
	 */
	tabPanel : undefined,
	/**
	 * 事件panel信息
	 * @type 
	 */
	eventWarnPanel : undefined,
	/**
	 * 定时器panel信息
	 * @type 
	 */
	timerWarnPanel : undefined,
	/**
	 * 预警panel信息
	 * @type 
	 */
	warnPanel : undefined,
	/**
	 * 预警通知人员panel信息
	 * @type 
	 */
	warnNoticeUser : undefined,
	/**
	 * 保存URL
	 * @type 
	 */
	saveUrl : __ctxPath +'/warn/manage/save.do',
	/**
	 * 编辑时加载数据
	 * @type 
	 */
	loadUrl : __ctxPath + '/warn/manage/loadData.do',
	/**
	 * 初始化方法
	 */
	initComponent : function(){
		var me = this;
		this.isEdit = this.isEdit;
		this.warnId = this.warnId;
		this.isView = this.isView;
		this.initWindowBtns(me);
		this.initFieldSet(me);
		this.initContent(me);
		if(this.isEdit)
			this.putValues(me);
		this.addEvents("doAfterSaved");
		OECP.Warn.WarnConfigWindow.superclass.initComponent.call(this);
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
					var data;
					//1、预警类型数据
					var warnTypeData = "";
					var warnTypeGroup = me.fieldSet.items.items[0];
					warnTypeGroup.eachItem(function(item){   
					    if(item.checked==true){   
					        warnTypeData = item.inputValue;   
					    }   
					});
					//2、事件预警或定时器预警数据
					var warnData;
					if(warnTypeData==OECP.Warn.StaticParam.warnTypeEvent){
						warnData = me.eventWarnPanel.getFormValues();
					}else{
						warnData = me.timerWarnPanel.getFormValues();
					}
					if(warnData[0])
						data = Ext.apply(warnData[1],{'warn.warningType':warnTypeData});
					else
						return;
					//3、预警信息数据
					var dd = me.warnPanel.getFormValues();
					if(dd[0])
						data = Ext.apply(data,dd[1]);
					else
						return;
					//4、预警通知人员信息数据
					var ddd = me.warnNoticeUser.getWarnNoticeUserData();
					data = Ext.apply(data,ddd);
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
	initFieldSet : function(me){
		if(!Ext.isDefined(me.fieldSet)){
			me.fieldSet = new Ext.form.FieldSet({
		            region:'north',
		            title: '选择预警类型',
		            collapsible: false,
		            height : 70,
		            items :[{
			            xtype: 'radiogroup',
			            items: [{
				                boxLabel: '事件预警', 
				                name: 'warn.warningType', 
				                inputValue: OECP.Warn.StaticParam.warnTypeEvent, 
				                checked : true,
				                listeners:{check : function(){
	            					if(this.checked){
	            						me.tabPanel.hideTabStripItem(1);
	            						me.tabPanel.unhideTabStripItem(0);
	            						me.tabPanel.setActiveTab(0);
	            					}
				                }}
			                },{
				                boxLabel: '定时器预警', 
				                name: 'warn.warningType', 
				                inputValue: OECP.Warn.StaticParam.warnTypeTimer, 
				                listeners:{check : function(){
	            					if(this.checked){
	            						me.tabPanel.hideTabStripItem(0);
	            						me.tabPanel.unhideTabStripItem(1);
	            						me.tabPanel.setActiveTab(1);
	            					}
				                }}
			               }]
			        	}
		            ]
			     }
			); 
		}
	},
	/**
	 * 初始化窗口里面的内容
	 * @param {} me
	 */
	initContent : function(me){
		if(!Ext.isDefined(me.eventWarnPanel)){
			me.eventWarnPanel = new OECP.Warn.EventWarnPanel();
		}
		if(!Ext.isDefined(me.timerWarnPanel)){
			me.timerWarnPanel = new OECP.Warn.TimerWarnPanel();
		}
		if(!Ext.isDefined(me.warnPanel)){
			me.warnPanel = new OECP.Warn.WarnPanel();
		}
		if(!Ext.isDefined(me.warnNoticeUser)){
			me.warnNoticeUser = new OECP.Warn.WarnNoticeUser({isEdit : me.isEdit,warnId : me.warnId});
		}
		if(!Ext.isDefined(me.tabPanel)){
			me.tabPanel = new Ext.TabPanel({
				region : 'center',
				height : 300,
				frame : true,
			    activeTab: 0,
			    //默认是true,只激活activeTab指定的panel，设为false，所有panel都激活
			    deferredRender : false,
			    items: [me.eventWarnPanel,me.timerWarnPanel,me.warnPanel,me.warnNoticeUser]
			});
		}
		
		var contentPanel = new Ext.Panel({
			layout : 'border',
			width : 585,
			height : 450,
			autoScroll	: true,
			align : 'center',
			items : [me.fieldSet,me.tabPanel]
		});
		
		me.componetArray = [contentPanel];
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
				"warnId":me.warnId
			},
			success : function(request) {
				var json = Ext.decode(request.responseText);
				if (json.success) {
					//加载预警类型数据
					var warnType = json.result['warningType'];
					me.fieldSet.items.items[0].setValue(warnType);
					//加载事件面板信息
					if(warnType==OECP.Warn.StaticParam.warnTypeEvent){
						me.tabPanel.hideTabStripItem(1);
	            		me.tabPanel.unhideTabStripItem(0);
						var rs = me.eventWarnPanel.EventWarnFormPanel.reader.readRecords(json);
						var _data = rs.records && rs.records[0] ? rs.records[0].data : null;
						me.eventWarnPanel.EventWarnFormPanel.getForm().setValues(_data);
					}else{
						//加载定时器面板信息
						me.tabPanel.hideTabStripItem(0);
	            		me.tabPanel.unhideTabStripItem(1);
						var rs = me.timerWarnPanel.TimerFormPanel.reader.readRecords(json);
						var _data = rs.records && rs.records[0] ? rs.records[0].data : null;
						me.timerWarnPanel.TimerFormPanel.getForm().setValues(_data);
					}
					
					//加载预警面板信息
					var rs = me.warnPanel.WarnFormPanel.reader.readRecords(json);
					var _data = rs.records && rs.records[0] ? rs.records[0].data : null;
					me.warnPanel.WarnFormPanel.getForm().setValues(_data);
					if(json.result['warnNoticeItem']!=null){
						for(var i =0;i<json.result['warnNoticeItem'].length;i++){
							me.warnPanel.WarnFormPanel.items.items[3].setValue(json.result['warnNoticeItem'][i]);
						}
					}
					
					
					//加载通知人员信息配置
					var warnNoticeUserType = json.result['warnNoticeUserType'];
					var noticeData;
					if(warnNoticeUserType==OECP.Warn.StaticParam.warnNoticeUserRole){
						noticeData = json.result['noticeRoles'];
					}else if(warnNoticeUserType==OECP.Warn.StaticParam.warnNoticeUserPost){
						noticeData = json.result['noticePosts'];
					}else{
						noticeData = json.result['noticeUsers'];
					}
					me.warnNoticeUser.setFormValues(warnNoticeUserType,noticeData,me.warnNoticeUser);
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