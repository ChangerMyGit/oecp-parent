Ext.ns('OECP.ui');
/**
 * OECP平台下Accordion和Tree结合的面板 <br>
 * <p>
 * 主要参数介绍：
 * </p>
 * fullDataUrl:完整数据的请求URL，该请求将相应完整的accordion和tree的数据，而取得各tree的数据为children下的数据。例如：[{id:1,text:'accordion1',children:[{id:11,text:'tree1',leaf:true}]}]。当fullDataUrl存在值时，accordionUrl，treeUrl，treeUrlParam都失效
 * <br>
 * accordionUrl:用来返回构建accordion面板的数据。和treeUrl組合使用 <br>
 * treeUrl:用来返回accordion面板下的tree面板的数据。和accordionUrl和treeUrlParam組合使用 <br>
 * treeUrlParam:用来定义treeUrl的请求参数，默认为id。参数的取值，来源于accordionUrl返回数据的各Itme的id值<br>
 * treeEvent:用来定义treePanel上的事件的，所有对树的处理，可以在这里实现。
 * 
 * @author yongtree
 * @class OECP.ui.AccordionTreePanel
 * @extends Ext.Panel
 */
OECP.ui.AccordionTreePanel = Ext.extend(Ext.Panel, {
	collapsible : this.collapsible || true,
	enableDD : false,
	enableDrag : false,
	region : this.region || 'west',
	layout : "accordion",
	split : this.split || true,
	margins : this.margins || '0 0 0 5',
	lines : this.lines || true,
	treeEvent : this.treeEvent,
	fullDataUrl : this.fullDataUrl,
	accordionUrl : this.accordionUrl,
	treeUrl : this.treeUrl,
	treeUrlParam : this.treeUrlParam || "id",
	keepActive : this.keepActive || true,
	initComponent : function() {
		this.addEvents("afterloaded");
		OECP.ui.AccordionTreePanel.superclass.initComponent.call(this);
		var accordionPanel = this;
		Ext.Ajax.request({
			url : this.fullDataUrl ? this.fullDataUrl : this.accordionUrl,
			success : function(response, options) {
				var arr = eval(response.responseText);
				var __activedPanelId = getCookie("activedPanel_" + accordionPanel.id);
				accordionPanel.removeAll();
				for (var i = 0; i < arr.length; i++) {
					var panel = new Ext.tree.TreePanel({
						id : accordionPanel.id + "_"+ arr[i].id,
						title : arr[i].text,
						layout : "fit",
						animate : true,
						border : false,
						autoScroll : true,
						loader : accordionPanel.fullDataUrl
								? null
								: new Ext.tree.TreeLoader({
									url : (accordionPanel.treeUrl
											.lastIndexOf('?') > -1)
											? (accordionPanel.treeUrl
													+ "&"
													+ accordionPanel.treeUrlParam
													+ "=" + arr[i].id)
											: (accordionPanel.treeUrl
													+ "?"
													+ accordionPanel.treeUrlParam
													+ "=" + arr[i].id)
								}),
						root : new Ext.tree.AsyncTreeNode({
									id : accordionPanel.id + '_root' +arr[i].id,
									text : arr[i].text,
									expanded : true,
									leaf : false,
									children : accordionPanel.fullDataUrl
											? arr[i].children
											: null
								}),
						rootVisible : false,
						listeners : accordionPanel.treeEvent
					});
					accordionPanel.add(panel);
					if (this.keepActive) {
						panel.on("expand", function(p) {
									var expires = new Date();
									expires.setDate(expires.getDate() + 30);
									setCookie("activedPanel_" + accordionPanel.id, p.id,
											expires, __ctxPath);
								});
						if (arr[i].id == __activedPanelId) {
							accordionPanel.layout.activeItem = panel;
						}
					}
				}
				accordionPanel.doLayout();
				accordionPanel.fireEvent('afterloaded');
			}
		});
	}
});