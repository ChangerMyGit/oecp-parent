Ext.ns('OECP.portlet');

/**
 * 待办任务portlet
 */
OECP.portlet.Notice = Ext.extend(Ext.ux.Portlet,{
    layout:'fit',
    initComponent : function(){
		OECP.portlet.Notice.superclass.initComponent.call(this);
		this.initCpanel();
		this.doLayout();
	},
	initCpanel : function(){
		if(!Ext.isDefined(this.cpanel)){
			this.cpanel = new OECP.portlet.NoticeView();
		}
		this.add(this.cpanel);
	}
	
});
Ext.reg('portlet_notice', OECP.portlet.Notice);