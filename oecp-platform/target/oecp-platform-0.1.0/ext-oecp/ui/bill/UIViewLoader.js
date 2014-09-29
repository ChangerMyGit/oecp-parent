Ext.ns('OECP.uiview');
/**
 * UI视图加载器
 */
OECP.uiview.Loader = Ext.extend(Ext.Panel,{
	views : {'list':{},'form':{}},
	baseCls : '',
	bodyBorder :false,
	frame:false,
	border:false,
	layout : 'column',
	flushListView : Ext.emptyFn,
	flushFormView : Ext.emptyFn,
	/**
	 * @cfg {string} functionCode 功能编号
	 */
	/**
	 * @cfg {string} bizTypeId 业务类型主键
	 */
	initComponent : function() {
		var scope = this;
		this.combo = new Ext.form.ComboBox({
			valueField : 'id',
			displayField : 'viewname',
			width : 100,
			mode : 'local',
			triggerAction : 'all',
			autoSelect : true,
			editable : false
		});
		
		this.combo.store = new Ext.data.JsonStore({
					url : __ctxPath + "/funviewassign/getAssigns.do",
					fields : ['id','viewcode','viewname']
				});
		this.combo.store.setBaseParam('functionCode',this.functionCode);
		if(this.bizTypeId){
			this.combo.store.setBaseParam('bizTypeId',this.bizTypeId);
		}
		this.combo.store.on('load',function(){
			if(this.combo.store.getAt(0)){
				this.combo.setValue(this.combo.store.getAt(0).get("id"));
				this.combo.fireEvent('select', this, this.combo.store.getAt(0), 0);
			}
			if(this.combo.store.getCount()>1){
				this.show();
			}else{
				this.hide();
			}
		},this);
		this.combo.on('beforeselect',function(scope,data,c,d,e){
			if(data.id === scope.getValue()){
				return false;
			}
		},this);
		this.combo.on('select',function(scope,data){
			this.loadview(true);
			this.loadview(false);
		},this);
		this.items = [{xtype:'tbtext',text:'界面选择：'},this.combo];
		OECP.uiview.Loader.superclass.initComponent.call(this);
		if(this.combo.store.getCount()>-1){
			this.show();
		}else{
			this.hide();
		}
	},
	getlistview : function(){
		var cfgstr = OECP.core.util.clone(this.views.list[this.getValue()]);
		return cfgstr;
	},
	getformview : function(){
		var cfgstr = OECP.core.util.clone(this.views.form[this.getValue()]);
		return cfgstr;
	},
	load : function(funcode,bizid){
		funcode && this.combo.store.setBaseParam('functionCode',funcode);
		bizid && this.combo.store.setBaseParam('bizTypeId',bizid);
		this.combo.store.reload();
	},
	loadview : function(loadlistview){
		// 客户端内存中有了，就不要再load了
		if(loadlistview && this.views.list[this.getValue()]){
			this.flushListView(this.getlistview());
			return;
		}
		if(!loadlistview && this.views.form[this.getValue()]){
			this.flushFormView(this.getformview());
			return;
		}
		Ext.Ajax.request({
				url : __ctxPath + (loadlistview?"/funview/listpreview.do":"/funview/formpreview.do"),
				scope : this,
				params : {
					'viewvo.id' : this.getValue()
				},
				success : function(request) {
					var json = Ext.util.JSON.decode(request.responseText);
					if (json.success) {
						if(loadlistview){
							this.views.list[this.getValue()] = request.responseText;
							this.flushListView(this.getlistview());
						}else{
							this.views.form[this.getValue()] = request.responseText;
							this.flushFormView(this.getformview());
						}
					}else{
						Ext.Msg.show({title:"错误",msg:'视图加载失败！可能是视图配置错误引起的。'+json.msg ,buttons:Ext.Msg.OK});
					}
				},
				failure : function(request) {
					Ext.Msg.show({title:"错误",msg:'您的网络可能不通畅，请稍后再试。',buttons:Ext.Msg.OK});
				}
		});
	},
	getValue : function(){
		return this.combo.getValue();
	},
	onDisable : function(){
		delete this.views;
        OECP.uiview.Loader.superclass.onDisable.apply(this, arguments);
        if(this.hiddenField){
            this.hiddenField.disabled = true;
        }
    }
});
