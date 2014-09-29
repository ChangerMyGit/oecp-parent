Ext.ns("OECP.bpm.definition");

OECP.bpm.ProcessDefWin = function(node, store) {
	var form = new Ext.FormPanel({
				labelWidth : 90,
				frame : false,
				bodyStyle : 'padding:5px 5px 0',
				// width : 350,
				url : __ctxPath + "/bpm/def/deploy.do",
				autoHeight : true,
				autoWidth : true,
				fileUpload : true,
				defaults : {
					width : 230
				},
				defaultType : 'textfield',
				items : [{
							xtype : 'hidden',
							name : 'def.belongFunction.id',
							value : node.id
						}, {
							fieldLabel : '功能名称',
							value : node.text,
							disabled : true
						}, {
							fieldLabel : '流程名称',
							name : 'def.name',
							allowBlank : false
						}, {
							fieldLabel : '流程描述',
							xtype : 'textarea',
							name : 'def.description'
						}, {
							fieldLabel : '上传流程文件',
							name : 'uploader.upload',
							xtype : 'fileuploadfield',
							allowBlank : false,
							blankText:'请选择zip格式的流程文件!',
							emptyText: '请选择zip格式的流程文件...'
						}]
			});

	var tree = new Ext.tree.TreePanel({
		title : '选择要分配的组织',
		height : 300,
		width : 400,
		useArrows : true,
		autoScroll : true,
		animate : true,
		enableDD : true,
		containerScroll : true,
		rootVisible : false,
		frame : false,
		root : {
			nodeType : 'async',
			id : "source"
		},
		loader : new Ext.tree.TreeLoader({
					dataUrl : __ctxPath + "/app/org/ownerTree.do?cb=0",
					baseParams : {}
		}),
		listeners : {
			 "load" : function(node) {
			 },
			 'checkchange' : function(node, checked) {
			 }
		}
		});

	//tree.expandAll();

	var win = new Ext.Window({
				title : '添加流程定义',
				width : 400,
				authHeight : true,
				items : [form],
				buttons : [{
					text : '发布',
					handler : function() {
						form.getForm().submit({
//								waitMsg : '正在提交用户信息',
								params : {
									
								},
								success : function(form, o) {
									win.close();
									Ext.ux.Toast.msg("信息", "流程发布成功！");
									store.reload({
												start : 0,
												limit : 100
									});
								},
								failure : function(form, action) {
									var f = Ext.util.JSON
											.decode(action.response.responseText);
									Ext.MessageBox.show({
												title : "警告",
												msg : f.result,
												buttons : Ext.MessageBox.OK,
												icon : Ext.MessageBox.ERROR
											});
								}
							});
						/**var nodes = tree.getChecked();

						if (nodes.length == 0) {
							Ext.MessageBox.show({
										title : "警告",
										msg : '请选择要发布到的组织！',
										buttons : Ext.MessageBox.OK,
										icon : Ext.MessageBox.ERROR
									});
						} else {
							var ids = '';
							Ext.each(nodes, function(node) {
										ids = ids + "," + node.id
									});
							form.getForm().submit({
								waitMsg : '正在提交用户信息',
								params : {
									orgIds : ids
								},
								success : function(form, o) {
									win.close();
									Ext.ux.Toast.msg("信息", "流程发布成功！");
									store.reload({
												start : 0,
												limit : 100
									});
								},
								failure : function(form, action) {
									var f = Ext.util.JSON
											.decode(action.response.responseText);
									Ext.MessageBox.show({
												title : "警告",
												msg : f.result,
												buttons : Ext.MessageBox.OK,
												icon : Ext.MessageBox.ERROR
											});
								}
							});
						}**/
					}
				}]
			});

	win.show();
}
