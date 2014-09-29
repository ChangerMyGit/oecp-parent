/**  
 * @include "../GridRefField.js"  
 */
Ext.ns('OECP.ui.combobox')

/**
 * 组件选择下拉控件。
 * 显示所有组件
 * 注意为属性hiddenName赋值，该属性是控件提交到后台对应的字段名。
 * <br/>
 * 例如：{dataIndex:'bc.id',fieldLabel : "所属组件",name:'eventInfo.bc.id',xtype:'bccombo',hiddenName:'eventInfo.bc.id'}
 * 
 * @author slx
 * @class OECP.ui.combobox.BCCombo
 * @extends OECP.ui.GridRefField
 */
OECP.ui.combobox.BCEventCombo = Ext.extend(OECP.ui.GridRefField, {
	minChars : 2,
	mode : 'local',
	valueField : 'id',
	codeField : 'code',
	displayField : 'name',
	refUrl : __ctxPath + '/event/list.do',
	storeFields : ['id','code', 'bc.name', 'name'],
	refColumns : [{
				header : '所属组件',
				dataIndex : 'bc.name',
				width : 100
			},{
				header : '事件编号',
				dataIndex : 'code',
				width : 100
			},{
				header : '事件名称',
				dataIndex : 'name',
				width : 100
			}]
			
});

Ext.reg('bceventcombo', OECP.ui.combobox.BCEventCombo);