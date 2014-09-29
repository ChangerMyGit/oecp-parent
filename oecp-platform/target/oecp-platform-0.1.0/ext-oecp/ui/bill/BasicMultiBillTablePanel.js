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
OECP.ui.BasicMultiListBill = Ext.extend(OECP.ui.BasicListBill, {
	initComponent:function(){
		OECP.ui.BasicMultiListBill.superclass.initComponent.call(this);
	},
	initBillWin:function(){
		var master = this;
		if(this.billwin){this.billwin.destroy();};
		this.billwin = new OECP.ui.BasicMultiCardWin({
			transmit:function(){//传递参数,把listpanel里的子表store传递到window内
				var scope = this;
				if(master.bodyEntityName && Ext.isString(master.bodyEntityName)){
					scope[master.bodyEntityName+'_cardstore_cfg'] = me[master.bodyEntityName+'_cardstore_cfg'];
				}else if(master.bodyEntityName && Ext.isArray(master.bodyEntityName)){
					for(var i=0;i<master.bodyEntityName.length;i++){
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
			bodyEntityName:me.bodyEntityName||'',
			bodyMutilEntityName:me.bodyMutilEntityName||''
		});
		this.billwin.bill.on('submitsuccess',function(response, opts, scope){
			//业务制单
			if(me.bussinessAddflag){
				var r = me.getHeadGrid().store.getById(me.selectRecordId);
				master.getHeadGrid().store.remove(r);
			}else{
			//TODO  保存后事件未作
				//hd = this.billwin.bill.getValues();
				//业务制单
					//如果是增加 不处理
					//如果是编辑 更新状态
				//手工制单
					//如果是新增 插入数据
					//如果是修改 更新原始数据
			}
			Ext.ux.Toast.msg('信息',	"保存成功！");
		});
		if(this.viewWin){this.viewWin.destroy();};
		this.viewWin = new OECP.ui.BasicMultiCardWin({
			transmit:function(){//传递参数,把listpanel里的子表store传递到window内
				var scope = this;
				if(master.bodyEntityName && Ext.isString(master.bodyEntityName)){
					scope[master.bodyEntityName+'_cardstore_cfg'] = me[master.bodyEntityName+'_cardstore_cfg'];
				}else if(master.bodyEntityName && Ext.isArray(master.bodyEntityName)){
					for(var i=0;i<master.bodyEntityName.length;i++){
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
			bodyEntityName:me.bodyEntityName||'',
			bodyMutilEntityName:me.bodyMutilEntityName||''
		});
		
	},
	/**
	 * 获取主表数据仓库
	 * 
	 * @return {Ext.data.JsonStore}
	 */
	getHeadStroe : function() {
		if (!this.headStore && this.headEntityName) {
			var me = this;
			cloneCfg= OECP.core.util.clone(this[this.headEntityName + '_liststore_cfg']);//克隆对象，防止地址引用对原始config做初始化
			this.headStore = new Ext.data.JsonStore(cloneCfg);
			var bp = {limit:this.headPageSize,start:0};//追加参数,用于默认查询
			this.headStore.baseParams = this.headStore.baseParams?Ext.applyIf(bp,this.headStore.baseParams):bp;
			this.headStore.on({
				'load': {
		            fn: function(store, records, options){
		              me.selectHeadAt(0);
		            },
		            scope: me
				},
				'loadexception': {
					fn: function(obj, options, response, e){
						Ext.MessageBox.alert("表头数据加载异常",e);
			        },
			        scope: me
			      }
			});
		}
		return this.headStore;
	},
	
	// 选择表头第idx行
	selectHeadAt : function(idx) {
		if (this.getHeadStroe().getCount() > 0) {
			this.getHeadGrid().getSelectionModel().selectRow(idx);
			var pk = this.getHeadStroe().getAt(idx).get(this.headPrimaryKey);
			var pkIsNull = Ext.isEmpty(pk) || pk == 'null';
			if (pk != null && this.headCurrentPK !== pk) {
				this.headCurrentPK = pk;
				this.removeAllBodyStroe();
				this.reloadBodyStore();
			}
		} else {
			var bs = this.getBodyStore();
			if (!bs) return;
			if (Ext.isObject(bs)) {
				bs.removeAll();
			} else if (Ext.isArray(bs)) {
				var propertyNum = 0;
				for (var j in bs) {
					propertyNum++; // 获取属性数量
				}
				for (var i = 0; i <= propertyNum-2; i++) {
				   bs[i].removeAll();
				}
			}
			this.changeButtonState();
		}
	},
	
	removeAllBodyStroe : function() {
		var bs = this.getBodyStore();
		if (!bs) return;
		if (Ext.isObject(bs)) {
			bs.removeAll();
		} else if (Ext.isArray(bs)) {
			var propertyNum = 0;
			for (var j in bs) {
				propertyNum++; // 获取属性数量
			}
			for (var i = 0; i <= propertyNum-2; i++) {
			   //bs[i].removeAll();
			}
		}
	},
});


/**
 * 多子表单据编辑窗口  继承自主子表
 * @class OECP.ui.BasicMultiCardWin
 * @extends OECP.ui.BasicCardBill
 */
OECP.ui.BasicMultiCardWin = Ext.extend(OECP.ui.BasicCardWin, {
	initBillPanel:function(){//初始化单据
		var me = this;
		if(!this.bill){
			this.bill = new OECP.ui.BasicMultiCardBill({
				defaults:{frame:true},
//				height:me.height-50,
				width:me.width-32,
				formView:me.formView,
				submitUrl:me.submitUrl||'',
				queryBillUrl:me.queryBillUrl||'',
				headEntityName:me.headEntityName||'',
				bodyEntityName:me.bodyEntityName||'',
				bodyMutilEntityName:me.bodyMutilEntityName||'',
				transmit:function(){
					//把window里定义的store赋值到bill中
					if(me.bodyEntityName && Ext.isString(me.bodyEntityName)){
						this[me.bodyEntityName+'_cardstore_cfg'] = me[me.bodyEntityName+'_cardstore_cfg'];
					}else if(me.bodyEntityName && Ext.isArray(me.bodyEntityName)){
						for(var i=0;i<me.bodyEntityName.length;i++){
							this[me.bodyEntityName[i]+'_cardstore_cfg'] = me[me.bodyEntityName[i]+'_cardstore_cfg'];
						}
					}
				}
			});
			//保存成功事件
			this.bill.on('submitsuccess',function(){
				me[me.closeAction]();
			});
		}
		this.items=[this.bill];
	}
});


/********************************************************************************
主表编辑界面
********************************************************************************/

/**
* 单据多子表 列表界面 (面板) 继承自主子表
* 
* @class Ext.ui.BasicMultiCardBill
* @extends OECP.ui.BasicCardBill
*/
OECP.ui.BasicMultiCardBill = Ext.extend(OECP.ui.BasicCardBill, {
	/**
	 * 覆盖父类方法
	 */
	doSubmit : function(options) {
		var scope = this;
		var valid = true;
		// 校验必填参数
		valid = scope.getForm().isValid();
		// 多子表会获得多个grid
		var grids = scope.bodyGrid;
		var propertyNum = 0;
		for ( var i in grids) {
			propertyNum++; // 获取属性数量
		}
		// 移除掉两个不计算的属性
		for (var i = 0; i <= propertyNum-2; i++) {
			var grid = grids[i];
			// 如果没有维护明细列表
			if(grid.getStore().data.length == 0){
				Ext.ux.Toast.msg("信息", '请维护明细信息。');
				return;
			}
			var vRecords = grid.getStore().data.items; // 获取需要校验的数据数据  
			var cum = grid.getColumnModel(); // 获取列名
			// 遍历每一行数据
			for(var row = 0;row<vRecords.length;row++){
				// 遍历panel每一列  第一列ID不参与数据校验
				for(var col = 1 ; col < cum.getColumnCount(true);col++){
					// 获取编辑器
					var editor = cum.getCellEditor(col,row);
					var record = vRecords[row].data;
					var value = record[cum.getDataIndex(col)];
					if(editor){
            					// 一定要设置值才能校验 泪水。。
            					editor.field.setValue(value);
            					// 如果没有验证通过
            					if(!editor.field.validateValue(value)){
            						//给不通过校验的具体空格增加错误css样式（Ext中form的样式）  
                        	                    Ext.get(grid.getView().getCell(row, col)).addClass('x-form-invalid'); 
                        	                    valid = false;
        					}
					}
				}
			}
		}

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
		options.url = options.url || scope.submitUrl || '';
		options.method = options.method || 'post';
		Ext.Ajax.request(options);
	},
	
	// 清空结果
	dataClear : function() {
		this.bodyDelRecords = {};
		if (this.getForm().getEl() && this.getForm().getEl().dom) {
			this.getForm().getEl().dom.reset();
		}
		this.resetAllCheckbox();
		if (Ext.isObject(this.getBodyStore())) {
			this.getBodyGrid().store.removeAll();
			this.getBodyGrid().store.modified = [];
		} else if (Ext.isArray(this.getBodyStore())) {
			// 修改 getBodyGrid().store 与 getBodyStroe 不对应
			var bodyStores = this.getBodyStore();
			var propertyNum = 0;
			for ( var i in bodyStores) {
				propertyNum++; // 获取属性数量
			}
		// 移除掉两个不计算的属性
			for (var i = 0; i <this.getBodyGrid().length; i++) {
				this.getBodyGrid()[i].store.removeAll();
				this.getBodyGrid()[i].store.modified = [];
		}
			
		}
	}
});
