/**  
 * @include "../../../ext-oecp/ui/Button.js"  
 * @include "../../../ext-oecp/ui/ToftPanel.js"  
 * @include "MyAgentUserPanel.js"  
 */
Ext.ns("OECP.Person")

/**
 * 员工视图
 * @class OECP.Person.PersonView
 * @extends OECP.ui.base.ToftPanel
 */
OECP.Person.PersonView=Ext.extend(OECP.ui.base.ToftPanel ,{
	id		: 'OECP.Person.PersonView',
	title 	: '员工管理',
	
	initComponent : function(){
		this.initCpanel();
		OECP.Person.PersonView.superclass.initComponent.call(this);
	},
	
	initCpanel : function(){
		if(!Ext.isDefined(this.cpanel)){
			this.cpanel = new OECP.Person.PersonPanel();
		}
	}

})