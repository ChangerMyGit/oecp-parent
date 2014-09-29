/**
 * @inclule "../../../ext-oecp/ui/TreeGridPanel.js"
 */
Ext.ns("OECP.bpm.definition");

/**
 * 流程定义页面
 * 
 * @author yongtree
 * @class OECP.bpm.ProcessDefView
 * @extends OECP.ui.TreeGridView
 */
OECP.bpm.ProcessDefView = function() {
	var tg = new OECP.ui.TreeGridView({
				treeDataUrl : __ctxPath + "/bpm/def/functionTree.do",
				queryRecordUrl : __ctxPath + "/bpm/def/list.do",
				title : '流程定义',
				id : 'OECP.bpm.ProcessDefView',
				treePanelTitle : '支持流程的功能',
				gridStoreFields : ['deployId','proDefId','id', 'name','description','version','createdByOrgName','belongFunctionName' ,'createTime','webPictureString'],
				pageSize:100,
				clickOnlyLeaf : true,
				gridItems : [{
							header : "流程定义ID",
							dataIndex : 'id',
							hidden : true, 
							width : 60
						},{
							header : "功能名称",
							dataIndex : 'belongFunctionName',
							width : 60
						},{
							header : "流程名称",
							dataIndex : 'name',
							width : 60
						}, {
							header : '创建组织',
							dataIndex : 'createdByOrgName',
							width : 60
						}, {
							header : '流程描述',
							dataIndex : 'description',
							width : 100
						},{
							header : '创建时间',
							dataIndex : 'createTime',
							width : 100
						}],
				gridBtns : [
//				            {
//							text : '发布流程',
//							handler : function() {
//								if (tg.currentNode == undefined) {
//									Ext.MessageBox.show({
//												title : "警告",
//												msg : '请选择要发布流程的功能节点！',
//												buttons : Ext.MessageBox.OK,
//												icon : Ext.MessageBox.ERROR
//											});
//								} else {
//									new OECP.bpm.ProcessDefWin(tg.currentNode,tg.store);
//								}
//							}
//						},
						{
							text : '流程设计与发布',
							iconCls : 'btn-add',
							handler : function() {
								if (tg.currentNode == undefined) {
									Ext.MessageBox.show({
												title : "警告",
												msg : '请选择要发布流程的功能节点！',
												buttons : Ext.MessageBox.OK,
												icon : Ext.MessageBox.ERROR
											});
								} else {
//									var webWindow = window.open("js/component/bpm/webdesign/webdesign.jsp?functionId="+tg.currentNode.id,"_blank");
									var webWindow = new Ext.Window({
										id:'win_webdesign',
										title:'流程设计与发布',
										width:800,
										height:500,
										modal : true,
										html:'<iframe src='+__fullPath+'/js/component/bpm/webdesign/webdesign.jsp?functionId='+tg.currentNode.id+' width=780 height=460></iframe>'
									});
									webWindow.on("close",function(){
										tg.store.removeAll();
										tg.store.load();
									});
									webWindow.show();
								}
							}
						},{
							text : '流程复制与发布',
							iconCls : 'btn-edit',
							handler : function() {
								var records = tg.selected();
								var record = tg.grid.getSelectionModel().getSelected();
								if (!record) {
									Ext.Msg.alert("提示", "请先选择要复制的流程定义!");
									return;
								}
								if (records.length >1) {
									Ext.MessageBox.alert('警告', '请选择一条流程定义，进行复制!');
									return;
								}
								
								var webPictureString = record.get('webPictureString');
								var webWindow = new Ext.Window({
										id:'win_webdesign',
										title:'流程设计与发布',
										width:800,
										height:500,
										modal : true,
										html:'<iframe src='+__fullPath+'/js/component/bpm/webdesign/webdesign.jsp?functionId='+tg.currentNode.id+' class="webPictureJson" jsonValue=' + webPictureString +' width=780 height=460></iframe>'
								});
								webWindow.on("close",function(){
									tg.store.removeAll();
									tg.store.load();
								});
								webWindow.show();
								
							}
						},
						{
							text : '查看流程图',
							iconCls : 'btn-preview',
							handler : function() {
								var removeIds = "";
								var records = tg.selected();
								var record = tg.grid.getSelectionModel().getSelected();
								if (!record) {
									Ext.Msg.alert("提示", "请先选择要查看的流程定义!");
									return;
								}
								if (records.length >1) {
									Ext.MessageBox.alert('警告', '请选择一条流程定义，进行查看!');
									return;
								}
								var deployId = record.get('deployId');
								
								var panel = new Ext.Panel({
//											height : 590,
//											width : 590,
											frame : true,
											autoScroll : true,
											html:'<image src="'+__fullPath+'/bpm/instance/getDefinitionImage.do?deployId='+deployId+'"></image>'
								});
								
								var win = new Ext.Window({
//									height : 500,
//									width : 600,
									modal : true,
									title : "流程图",
									autoScroll : true,
									items : [panel]
								});
								
								win.show();
							}
						},{
							text : '删除流程',
							iconCls : 'btn-delete',
							handler : function() {
								var removeIds = "";
								var records = tg.selected();
								var record = tg.grid.getSelectionModel().getSelected();
								if (!record) {
									Ext.Msg.alert("提示", "请先选择要删除的行!");
									return;
								}
								if (records.length == 0) {
									Ext.MessageBox.alert('警告', '请选择一条流程定义，进行删除!');
									return;
								}
								
								Ext.Msg.confirm('提示框', '您确定要进行删除操作吗?', function(button) {
									if (button == 'yes') {
										for (var i = 0; i < records.length; i++) {
											var record = records[i];
											var id = record.get('id');
											var deployId = record.get('deployId');
											if(id=='undefined' || id =='' || id == null || id == 'null'){
//												tg.store.remove(records[i]);
											}else{
												removeIds += id;
												removeIds += "#";
												removeIds += deployId;
												removeIds += ",";
											}
										}
										if(!Ext.isEmpty(removeIds)){
											var actionUtil = new ActionUtil();
											actionUtil.deleteProcessDefinition(removeIds,tg.store);
											// 清空删除列表
											removeIds = '';
										}
									}
								}, this);
								
							}
						}]
			});
			tg.tree.expandAll();
	return tg;
}
