Ext.ns('OECP.portlet');

/**
 * 待办任务portlet
 */
OECP.portlet.WaitJob = Ext.extend(Ext.ux.Portlet,{
    width:320,
    initComponent : function(){
		this.initCpanel();
		OECP.portlet.WaitJob.superclass.initComponent.call(this);
	},
	initCpanel : function(){
		if(!Ext.isDefined(this.cpanel)){
			this.cpanel = new OECP.bpmperson.PersonalTaskView();
		}
		this.items = [this.cpanel];
	}
	
});
Ext.reg('portlet_waitjob', OECP.portlet.WaitJob);