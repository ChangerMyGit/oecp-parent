Ext.ns("OECP.ui");

/*******************************************************************************
 * 单表表卡片界面
 ******************************************************************************/
/**
 * 单据主从表 主要定义公共方法和属性 继承自主子表
 * 
 * @class Ext.ui.BasicSingleListBill
 * @extends OECP.ui.BasicListBill
 */
OECP.ui.BasicSingleListBill = Ext.extend(OECP.ui.BasicListBill, {
	initComponent:function(){
		this.initUiLoader();
		OECP.ui.BasicSingleListBill.superclass.initComponent.call(this);
	},
	/** 重新加载表体 */
	reloadBodyStore : function() {},
	/** 覆盖父类的loader，目的为：单表的时候列表界面表格高度全屏 **/
	initUiLoader : function(){
		var me = this;
		this.loader = new OECP.uiview.Loader({
			functionCode:me.functionCode,
			flushListView:function(config){
				var json = Ext.decode(config);
				var _view=json.result;
				var pagewidth = document.body.clientWidth;
				var pageheight = document.body.clientHeight;
				Ext.apply(_view,{region:'center',baseCls:'',width:pagewidth});
				var addPageBarFlag = false;//是否追加分页标记
				var forEachsetWidthFun = function(_items){//递归设置 oecpgrid控件宽度
					if(Ext.isDefined(_items) && Ext.isArray(_items)){
						Ext.each(_items,function(i){forEachsetWidthFun(i);},this);
					}else if(Ext.isDefined(_items) && Ext.isObject(_items)){
						if(['oecpgrid','oecpeditgrid'].indexOf(_items.xtype) !== -1){
							_items.width = pagewidth;
							_items.height = pageheight-50; // 此处增加高度设置
							if(!addPageBarFlag){//为第一个表格默认追加分页控件
								_items.bbar = new Ext.PagingToolbar({
									displayInfo:true,pageSize:me.headPageSize,store:{},
									displayMsg:'当前页记录索引{0}-{1}， 共{2}条记录',
									emptyMsg:'当前没有记录'
								});
								addPageBarFlag=true;
							}
						}
						if(Ext.isDefined(_items.items)){
							forEachsetWidthFun(_items.items);}
					}
				};
				forEachsetWidthFun(_view);
				me.removeAll();
				me.clearBillProperty.call(me);//清理内部变量
				
				me.mainPanel = new Ext.Panel({layout:'border'});//面板，容纳查询和视图模板
				if(me.quickQueryCfg){
					me.initQuickQuery();
					me.mainPanel.add(me.quickQueryPanel);
				}
				me.mainPanel.add(_view);//
				me.add(me.mainPanel);//
				me.initBillProperty.call(me);
//				me.doLayout();
//				me.initQueryWin.call(me);//初始化查询框
				me.loadQueryScheme(this.getValue());
				me.initSuccessCallback.call(me);
				me.initPrintTemplateMenu(this.getValue());
				me.changeButtonState();
				me.setUIReady('listui');
			},
			flushFormView:function(config){
				me.formView = config;
				me.initBillWin();//初始化编辑界面
			}
		});
	},
	initBillWin:function(){
		var master = this;
		if(this.billwin){this.billwin.destroy();};
		this.billwin = new OECP.ui.BasicSingleCardWin({
			transmit:function(){//传递参数,把listpanel里的子表store传递到window内
				var scope = this;
				if(master.bodyEntityName && Ext.isString(master.bodyEntityName)){
					scope[master.bodyEntityName+'_cardstore_cfg'] = me[master.bodyEntityName+'_cardstore_cfg'];
				}else if(master.bodyEntityName && Ext.isArray(master.bodyEntityName)){
					for(var i=0;i<master.bodyEntityName;i++){
						scope[master.bodyEntityName[i]+'_cardstore_cfg'] = me[master.bodyEntityName[i]+'_cardstore_cfg'];
					}
				}
			},
			modal:true,
			stopBtnDisplay : this.stopBtnDisplay,
			closeAction:'hide',
			functionCode:master.functionCode,
			height:master.billWinHeight,
			width:master.billWinWidth,
			formView:master.formView,
			submitUrl:master.submitUrl||'',
			queryBillUrl:master.queryBillUrl||'',
			stopUrl:master.stopUrl||'',
			headEntityName:master.headEntityName||'',
			bodyEntityName:me.bodyEntityName||''
		});
		this.billwin.bill.on('submitsuccess',function(response, opts, scope){
			//业务制单
			if(me.bussinessAddflag){
				var r = me.getHeadGrid().store.getById(me.selectRecordId);
				master.getHeadGrid().store.remove(r);
			}else{}
			// Ext.ux.Toast.msg('信息',	"保存成功！");
			var msg = eval("("+Ext.util.Format.trim(response.responseText)+")");
			if(msg.success){
				Ext.ux.Toast.msg('信息',	"保存成功！");
			} else {
				Ext.Msg.show({title:"错误",msg:msg.msg,buttons:Ext.Msg.OK});
			}
		});
		if(this.viewWin){this.viewWin.destroy();};
		this.viewWin = new OECP.ui.BasicSingleCardWin({
			transmit:function(){//传递参数,把listpanel里的子表store传递到window内
				var scope = this;
				if(master.bodyEntityName && Ext.isString(master.bodyEntityName)){
					scope[master.bodyEntityName+'_cardstore_cfg'] = me[master.bodyEntityName+'_cardstore_cfg'];
				}else if(master.bodyEntityName && Ext.isArray(master.bodyEntityName)){
					for(var i=0;i<master.bodyEntityName;i++){
						scope[master.bodyEntityName[i]+'_cardstore_cfg'] = me[master.bodyEntityName[i]+'_cardstore_cfg'];
					}
				}
			},
			modal:true,
			closeAction:'hide',
			functionCode:master.functionCode,
			height:master.billWinHeight,
			width:master.billWinWidth,
			formView:master.formView,
			submitUrl:master.submitUrl||'',
			queryBillUrl:master.queryBillUrl||'',
			headEntityName:master.headEntityName||'',
			bodyEntityName:me.bodyEntityName||''
		});
	}
});


/**
 * 单据编辑窗口  继承自主子表
 * @class OECP.ui.BasicSingleCardWin
 * @extends Ext.Window
 */
OECP.ui.BasicSingleCardWin = Ext.extend(OECP.ui.BasicCardWin,{
	initBillPanel:function(){//初始化单据
		var me = this;
		if(!this.bill){
			this.bill = new OECP.ui.BasicSingleCardBill({
				defaults:{frame:true},
				width:me.width-32,
				formView:me.formView,
				submitUrl:me.submitUrl||'',
				queryBillUrl:me.queryBillUrl||'',
				headEntityName:me.headEntityName||'',
				bodyEntityName:me.bodyEntityName||'',
				stopUrl:me.stopUrl||'',
				transmit:function(){
					//把window里定义的store赋值到bill中
					if(me.bodyEntityName && Ext.isString(me.bodyEntityName)){
						this[me.bodyEntityName+'_cardstore_cfg'] = me[me.bodyEntityName+'_cardstore_cfg'];
					}
				}
			});
			//保存成功事件
			this.bill.on('submitsuccess',function(){
				me[me.closeAction]();
			});
		}
		this.items=[this.bill];
	},
});

/********************************************************************************
	主表编辑界面
********************************************************************************/

/**
* 单据主表 列表界面 (面板) 继承自主子表
* 
* @class Ext.ui.BasicSingleCardBill
* @extends OECP.ui.BasicCardBill
*/
OECP.ui.BasicSingleCardBill = Ext.extend(OECP.ui.BasicCardBill, {
	stop : function(options){
		var scope = this;
		if (!options) 
			options = {};
		if (!options.params) {
			options.params = this.formatSubmitData();
		} else {
			options.params = Ext.apply(options.params, this.formatSubmitData());
		}
		if (options.success && Ext.isFunction(options.success)) {
			options.success.createDelegate(this, [scope], true);// 追加个参数
		} else {
			options.success = function(response, opts) {
				var msg = Ext.decode(response.responseText);
				if (msg.success){
					scope.fireEvent('submitsuccess', response, opts, scope);
				}else{
					Ext.Msg.show({title : "错误",msg : msg.msg,buttons : Ext.Msg.OK});
				}
			};
		}
		// 存在failure函数就追加个scope参数
		if (options.failure && Ext.isFunction(options.failure)) {
			options.failure.createDelegate(this, [scope], true);
		} else {
			options.failure = function(response, opts) {
				if (scope.on('submitfailure', response, opts, scope) === false) {
					return;
				}
				Ext.ux.Toast.msg("信息", '加载失败！请联系管理员。');
			};
		}		 
		options.url = options.url || scope.stopUrl || '';
		options.method = options.method || 'post';
		Ext.Ajax.request(options);
	},
	doSubmit : function(options) {
		var scope = this;
		var valid = true;
		// 校验必填参数
		valid = scope.getForm().isValid();
		// 验证失败
		if(!valid) return;
		
		if (!options) 
			options = {};
		if (!options.params) {
			options.params = this.formatSubmitData();
		} else {
			options.params = Ext.apply(options.params, this.formatSubmitData());
		}
		if (options.success && Ext.isFunction(options.success)) {
			options.success.createDelegate(this, [scope], true);// 追加个参数
		} else {
			options.success = function(response, opts) {
				scope.fireEvent('submitsuccess', response, opts, scope);
			};
		}
		// 存在failure函数就追加个scope参数
		if (options.failure && Ext.isFunction(options.failure)) {
			options.failure.createDelegate(this, [scope], true);
		} else {
			options.failure = function(response, opts) {
				if (scope.on('submitfailure', response, opts, scope) === false) {
					return;
				}
				Ext.ux.Toast.msg("信息", '加载失败！请联系管理员。');
			};
		}
		options.url = options.url || scope.submitUrl || '';
		options.method = options.method || 'post';
		Ext.Ajax.request(options);
	
	},
	
	/**
	 * 清空数据
	 */
	dataClear : function() {
		this.bodyDelRecords = {};
		if (this.getForm().getEl() && this.getForm().getEl().dom) {
			this.getForm().getEl().dom.reset();
		}
		this.resetAllCheckbox();
	}
});