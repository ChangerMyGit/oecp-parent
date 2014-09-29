Ext.ns('OECP.ui.view');

/**
 * 功能视图管理界面
 * 
 * @class OECP.ui.view.FunViewMngPanel
 * @extends Ext.Panel
 */
OECP.ui.view.FunViewMngPanel = Ext.extend(Ext.Panel, {
			title:'功能视图管理界面',
			id:'OECP.ui.view.FunViewMngPanel',
			layout : 'card',
			autoScroll : true,
			layoutConfig : {
				deferredRender : true
			},
			initComponent : function() {
				var me = this;
				OECP.ui.view.FunViewMngPanel.superclass.initComponent.call(this);
				this.initListPanel();
				this.initEditPanel();
				var task = new Ext.util.DelayedTask(function(){
				    me.showListView();
				});
				task.delay(500);//延迟500毫秒响应
			},
			initListPanel : function() {
				var scope = this;
				this.listPanel = new OECP.ui.view.FunViewMngListPanel({
						});
				this.listPanel.parentPanel = this;
				this.add(this.listPanel);
			},
			initEditPanel : function() {
				var scope = this;
				this.editPanel = new OECP.ui.view.ViewPanel({
						});
				this.editPanel.on("viewsave", function() {
							this.showListView();
						},scope);
				this.add(this.editPanel);
			},
			showListView : function() {
				this.getLayout().setActiveItem(0);
				this.listPanel.listStore.reload();
			},
			showEditView : function(scope, viewid) {
				this.getLayout().setActiveItem(1);
				this.editPanel.doLoad(viewid);
			}
		});