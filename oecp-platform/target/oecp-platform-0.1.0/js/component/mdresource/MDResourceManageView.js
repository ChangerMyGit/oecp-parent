// 主数据资源管理界面
Ext.ns("OECP.mdresource");
/**
 * 
 * @class OECP.mdresource.MDResourceManageView
 * @extends Ext.Panel
 */
OECP.mdresource.MDResourceManageView = Ext.extend(Ext.Panel, {
	id : 'OECP.mdresource.MDResourceManageView',
	title : '主数据资源管理',
	layout : 'border',
	// 快速查询面板
	quickPanel : null,
	// 主数据资源面板
	mdresourcePanel : null,
	// 查询按钮
	quickButton : null,
	// 添加按钮
	addBtn : null,
	// 编辑按钮
	editBtn : null,
	// 删除按钮
	delBtn : null,

	win : null,
	headDataUrl : __ctxPath + "/mdresource/getMDResources.do",
	bodyDataUrl : __ctxPath + "/mdresource/getMDResourceFields.do",
	delUrl : __ctxPath + "/mdresource/deleteMDResource.do",
	headColumns : [{
				header : '主资源编码',
				dataIndex : 'code'
			}, {
				header : '主资源名称',
				dataIndex : 'name'
			}, {
				header : '对应实体类名',
				dataIndex : 'eoClassName'
			}, {
				header : '对应表名',
				dataIndex : 'tableName'
			}],

	headStoreFields : ['id', 'code', 'name', 'eoClassName', 'tableName'],

	bodyColumns : [{
				header : '字段名',
				dataIndex : 'name'
			}, {
				header : '显示名称',
				dataIndex : 'dispName'
			}, {
				header : '对应的UI类',
				dataIndex : 'uiClass'
			}, {
				header : '是否显示',
				dataIndex : 'isDisplay'
			}, {
				header : '关联主数据资源',
				dataIndex : 'relatedMD.name'
			}],
	bodyStoreFields : ['id', 'name', 'dispName', 'uiClass', 'isDisplay',
			'relatedMD.name'],

	initComponent : function() {
		var master = this;
		master.quickButton = new OECP.ui.button.QueryButton();

		master.quickPanel = new Ext.FormPanel({
					region : 'north',
					frame : true,
					height : 35,
					items : [{
								layout : 'column',
								items : [{
											columnWidth : .2,
											layout : 'form',
											items : [{
														xtype : 'textfield',
														fieldLabel : '主数据资源编码',
														name : 'mdResourceCode'
													}]
										}, master.quickButton]
							}]
				});

		master.mdresourcePanel = new OECP.ui.MasterDetailPanel({
					headStoreParams : {},
					region : 'center',
					headCheckBox : true,
					topItem : master.quickPanel,
					headDataUrl : master.headDataUrl,
					headPageSize : 25,
					headColumns : master.headColumns,
					headStoreFields : master.headStoreFields,
					bodyDataUrl : master.bodyDataUrl,
					bodyColumns : master.bodyColumns,
					bodyStoreFields : master.bodyStoreFields
				});
		master.mdresourcePanel.queryBtn.hidden = true;

		master.items = [master.mdresourcePanel];

		master.mdresourcePanel.headStore.load();

		master.quickButton.on('click', function() {
			var mr = master.quickPanel.getForm().getValues();
			master.mdresourcePanel.headStore.baseParams.code = mr['mdResourceCode'];
			master.mdresourcePanel.headStore.load();
		});

		master.win = new OECP.mdresource.MDResourceEditWindow();

		master.mdresourcePanel.addBtn.on('click', function() {
					master.win.editPanel.dataClear();
					master.win.show();
				});

		master.mdresourcePanel.editBtn.on('click', function() {
					var m = master.mdresourcePanel.headCurrentPK;
					if (m != '' && m != null) {
						master.win.show();
						master.win.editPanel.doQuery({
									params : {
										'id' : m
									}
								});
					}
				});

		master.mdresourcePanel.delBtn.on('click', function() {
					var b = master.mdresourcePanel.headGrid.getSelectionModel()
							.getSelections();
					var e = [];
					for (var i = 0; i < b.length; i++) {
						e.push(b[i].data.id);
					}
					Ext.Ajax.request({
										url : master.delUrl,
										success : function(response) {
											var msg = Ext.util.JSON
												.decode(response.responseText);		
											if (msg.success) {
												Ext.ux.Toast.msg("信息", msg.msg);
												master.mdresourcePanel.headStore.load();
												master.mdresourcePanel.reloadBodyStore();
												master.win.ref.store.load();
											}
										},
										failure : function() {
											Ext.Msg.show({
														title : "错误",
														msg : msg.result.msg,
														buttons : Ext.Msg.OK
													});
										},
										params : {
											ids : e
										}
									});
				});

		master.win.on('dataSaved', function(id) {
					master.mdresourcePanel.headStore.load();
					master.mdresourcePanel.reloadBodyStore();
					master.win.ref.store.load();
				});

		OECP.mdresource.MDResourceManageView.superclass.initComponent
				.call(this);
	}

});