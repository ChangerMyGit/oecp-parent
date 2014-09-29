/**  
 * @include "../../../ext-oecp/ui/Button.js"  
 * @include "../../../ext-oecp/ui/ToftPanel.js"  
 * @include "MyAgentUserPanel.js"  
 */
Ext.ns("OECP.MyAgent")
/**
 * 我的代理人管理
 * @class OECP.MyAgent.MyAgentUserView
 * @extends OECP.ui.base.ToftPanel
 */
OECP.MyAgent.MyAgentUserView = Ext.extend(Ext.Panel ,{
	id : 'OECP.MyAgent.MyAgentUserView',
	title 	: '我的代理人管理',
	/**
	 * 初始化方法
	 */
	initComponent : function(){
		var view = new OECP.MyAgent.MyAgentUserBaseView({isAdminUser : false});
		this.items = [view];
		OECP.MyAgent.MyAgentUserView.superclass.initComponent.call(this);
	}
})