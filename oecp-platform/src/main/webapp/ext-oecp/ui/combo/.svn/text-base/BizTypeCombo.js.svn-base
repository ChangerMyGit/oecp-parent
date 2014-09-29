Ext.ns('OECP.ui.combobox')

/**
 * 业务类型下拉框。提供给支持单据流的单据使用。
 * 需要传入功能编号。
 * @author slx
 * @class OECP.ui.combobox.BizTypeCombo
 * @extends Ext.form.ComboBox
 */
OECP.ui.combobox.BizTypeCombo = Ext.extend(Ext.form.ComboBox, {
	
	/**
	 * @cfg {string} functionCode 当前功能编号
	 */
	functionCode : undefined,
	// private 下拉框内的数据源
	store : undefined,
    displayField:'name',
    valueField:'id',
    editable : false,
    typeAhead: true,
    forceSelection: true,
    emptyText:'无业务类型',
    mode: 'local',
    selectOnFocus:true,
    triggerAction: 'all',
    width : 100,
	initComponent : function() {
		this.initStore();
		OECP.ui.combobox.BizTypeCombo.superclass.initComponent.call(this);
	},
	
	initStore : function(){
		if(this.store == undefined){
			this.store = new Ext.data.JsonStore({
				autoLoad : true,
				url : __ctxPath + '/billflow/loadBizTypes.do',
				baseParams :{functionCode : this.functionCode},
				storeId : 'id',
				fields : ['id','code','name','description','shared','org.id','org.name']
			});
		}
	}
	
});

Ext.reg('bizTypeCombo', OECP.ui.combobox.BizTypeCombo);