/**  
 * @include "../../../ext-oecp/ui/Button.js"  
 * @include "../../../ext-oecp/ui/ToftPanel.js"  
 * @include "MyAgentUserPanel.js"  
 */
Ext.ns("OECP.Agent")
/**
 * 我的代理人管理
 * @class OECP.MyAgent.MyAgentUserView
 * @extends OECP.ui.base.ToftPanel
 */
OECP.Agent.AgentUserView = Ext.extend(Ext.Panel ,{
	id : 'OECP.Agent.AgentUserView',
	title 	: '代理人管理',
	/**
	 * 初始化方法
	 */
	initComponent : function(){
		var view = new OECP.MyAgent.MyAgentUserBaseView({isAdminUser : true});
		this.items = [view];
		OECP.Agent.AgentUserView.superclass.initComponent.call(this);
	}
})