/**  
 * @include "../GridRefField.js"  
 */
Ext.ns('OECP.ui.combobox');

/**
 * 用户选择下拉控件。
 * 显示所有用户
 * 
 * @author yt
 * @class OECP.ui.combobox.UserCombo
 * @extends OECP.ui.GridRefField
 */
OECP.ui.combobox.UserCombo = Ext.extend(OECP.ui.GridRefField, {
	minChars : 2,
	emptyText : '请选择用户',
	mode : 'local',
	valueField : 'id',
	codeField : 'loginId',
	displayField : 'name',
	refUrl : __ctxPath + '/user/list.do',
	storeFields : ['id', 'loginId', 'name'],
	refColumns : [{
				header : '登录账号',
				dataIndex : 'loginId',
				width : 100
			}, {
				header : '用户名称',
				dataIndex : 'name',
				width : 140
			}]
			
});

Ext.reg('usercombo', OECP.ui.combobox.UserCombo);