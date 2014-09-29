/**
 * @include "../../../ext-oecp/ui/TreeComboBox.js"
 */
Ext.ns('OECP.core');

/**
 * @author ljt
 * @class OECP.core.DeptComboBox
 * @extends OECP.ui.TreeComboBox
 */
OECP.core.FunComboBox = Ext.extend(OECP.ui.TreeComboBox, {
			treeUrl : __ctxPath + '/app/fun/allFunsTree.do',
			rootVisible : false
		});