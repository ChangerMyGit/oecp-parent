Ext.ns('OECP.ui.base');
/**
 * 功能界面基本布局
 * @author slx
 * @class OECP.Layout.BaseView
 * @extends Ext.Viewport
 */
OECP.ui.base.ToftPanel = Ext.extend(Ext.Panel,{
	layout : 'border',
	defaultType: 'panel',
	height : 600,
	btnHeight : 23, // 按钮区高度
	btns : undefined,	// 按钮组
	spHeight : 23, // 按钮区高度
	cpanel : undefined,	// 数据区panl
	spanel : undefined,	// 状态栏panl
	
	initComponent : function(){
		var toftPanle = this;
        // 按钮区panel
		if(Ext.isDefined(toftPanle.btns)){
			this.tbar = new Ext.Toolbar({plugins:new Ext.ux.ToolbarKeyMap(),items:toftPanle.btns});
		}
//		var btnpanel = new Ext.Panel({
//			defaultType: 'oecpbutton',
//			layout:'table',
//			baseCls: 'x-plain',
//			items : toftPanle.btns
//		});
		// 数据区panel
		var dtpanel = new Ext.Panel({
				defaultType: 'panel',
				region:'center',
				layout:'fit',
				baseCls: 'x-plain'
			});
		if(Ext.isDefined(this.cpanel)){
			dtpanel.add(this.cpanel);
		}
		// 状态panel
		if(this.spanel == undefined){
			this.spanel = new Ext.Panel({
				height : toftPanle.spHeight,
				layout:'fit',
				region:'south',
				title : '&nbsp;',
				baseCls: 'x-plain'
			});
		}
		this.items = [
//			{// 按钮区
//				id:'panl_btns',
//				baseCls:'x-plain',
//				items:[btnpanel],
//				split:false,
//				height:toftPanle.btnHeight,
//				region:'north'
//			},
			dtpanel,this.spanel // 状态栏
		];
		
		OECP.ui.base.ToftPanel.superclass.initComponent.call(this);
	},
	
	setStateMsg : function(msg){
		this.spanel.setTitle( msg);
	}
})
