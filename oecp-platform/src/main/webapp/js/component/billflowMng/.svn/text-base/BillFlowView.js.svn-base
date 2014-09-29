/**  
 * @include "../../../ext-oecp/ui/Button.js"  
 * @include "../../../ext-oecp/ui/ToftPanel.js"  
 * @include "BizTypePanel.js"  
 */
Ext.ns('OECP.BillFlow');
/**
 * 单据流配置界面。包含业务类型注册、单据流程配置、单据流前置数据界面字段配置的功能。
 * @author slx
 * @class OECP.BillFlow.BillFlowView
 * @extends OECP.ui.base.ToftPanel
 */
OECP.BillFlow.BillFlowView = Ext.extend(OECP.ui.base.ToftPanel ,{
	id		: 'OECP.BillFlow.BillFlowView',
	title 	: '单据流管理',
	
	initComponent : function(){
		this.initCpanel();
		OECP.BillFlow.BillFlowView.superclass.initComponent.call(this);
	},
	
	initCpanel : function(){
		if(!Ext.isDefined(this.cpanel)){
			this.cpanel = new OECP.BillFlow.BizTypePanel();
		}
	}

})
