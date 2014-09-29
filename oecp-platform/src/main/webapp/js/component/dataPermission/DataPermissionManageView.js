// 数据权限管理界面
Ext.ns("OECP.dataPermission");
/**
 * @author liujingtao
 * @class PostManageView
 * @extends Ext.Panel
 */
OECP.dataPermission.DataPermissionManageView = Ext.extend(Ext.Panel, {
	id : 'OECP.dataPermission.DataPermissionManageView',
	title : '数据权限管理',
	layout : 'border',
	deptTreeUrl : __ctxPath + "/dept/depttree.do",
	postGridUrl : __ctxPath + "/post/getPostList.do",
	mdsourceGridUrl : __ctxPath + "/datapermission/getmdResources.do",
	mdsourceFieldsUrl : __ctxPath + "/datapermission/getmdResourceFields.do",
	dataPermissionUrl : __ctxPath + "/datapermission/getDataPermissions.do",
	savedataPermissionUrl : __ctxPath
			+ '/datapermission/updateDataPermissions.do',
	selectCheckedUrl : __ctxPath + "/datapermission/getCheckedDataIds.do",
	getFormItemsUrl : __ctxPath + "/datapermission/getFormItems.do",

	initComponent : function() {
		var dpmv = this;
		OECP.dataPermission.DataPermissionManageView.superclass.initComponent
				.call(this);

		// 部门树
		var deptTreePanel = new Ext.tree.TreePanel({
					title : '部门列表',
					animate : true,
					region : 'west',
					collapsible : true,// 面板是否可收缩
					margins : '0 0 0 3',
					autoScroll : true,
					rootVisible : false,
					enableDD : false,
					enableDrag : false,
					split : true,
					width : 170,
					lines : true,
					root : new Ext.tree.AsyncTreeNode({
								id : "dept_root",
								text : "所有部门"
							}),
					loader : new Ext.tree.TreeLoader({
						dataUrl : dpmv.deptTreeUrl,
						createNode : function(attr){
							attr.icon = __ctxPath+'/images/menus/personal/personal.png';
					        if(this.baseAttrs){
					            Ext.applyIf(attr, this.baseAttrs);
					        }
					        if(this.applyLoader !== false && !attr.loader){
					            attr.loader = this;
					        }
					        if(Ext.isString(attr.uiProvider)){
					           attr.uiProvider = this.uiProviders[attr.uiProvider] || eval(attr.uiProvider);
					        }
					        if(attr.nodeType){
					            return new Ext.tree.TreePanel.nodeTypes[attr.nodeType](attr);
					        }else{
					            return attr.leaf ?
					                        new Ext.tree.TreeNode(attr) :
					                        new Ext.tree.AsyncTreeNode(attr);
					        }
					    }
					}),
					listeners : {
						"click" : function(node) {
							post_store.baseParams.deptid = node.id;
							post_store.load();
						}
					}
				});
		// 岗位store
		var post_store = new Ext.data.JsonStore({
					url : dpmv.postGridUrl,
					baseParams : {
						deptid : ""
					},
					fields : ['id', 'name', 'code'],
					autoLoad : true
				});
		// 岗位选择器
		var post_sm = new Ext.grid.CheckboxSelectionModel();
		// 岗位cm
		var post_cm = new Ext.grid.ColumnModel([post_sm,
				new Ext.grid.RowNumberer(), {
					header : "主键",
					dataIndex : "id",
					hidden : true
				}, {
					header : "岗位名称",
					dataIndex : "name"
				}, {
					header : "岗位编码",
					dataIndex : "code"
				}]);
		// 岗位列表面板
		var post_grid = new Ext.grid.GridPanel({
					title : '岗位列表',
					enableDD : false,
					flex : 1,
					viewConfig : {
						forceFit : true
					},
					sm : post_sm,
					enableDrag : false,
					autoScroll : true,
					cm : post_cm,
					store : post_store
				});

		// 资源store
		var mdsource_store = new Ext.data.JsonStore({
					url : dpmv.mdsourceGridUrl,
					baseParams : {
						funId : ""
					},
					fields : ['id', 'name', 'code', 'jsClassName'],
					autoLoad : true
				});
		// 资源选择器
		var mdsource_sm = new Ext.grid.CheckboxSelectionModel();
		// 资源cm
		var mdsource_cm = new Ext.grid.ColumnModel([mdsource_sm,
				new Ext.grid.RowNumberer(), {
					header : "主键",
					dataIndex : "id",
					hidden : true
				}, {
					header : "资源名称",
					dataIndex : "name"
				}, {
					header : "资源编码",
					dataIndex : "code"
				}]);
		// 资源列表面板
		var mdsource_grid = new Ext.grid.GridPanel({
					flex : 1,
					title : '资源列表',
					enableDD : false,
					viewConfig : {
						forceFit : true
					},
					enableDrag : false,
					autoScroll : true,
					sm : mdsource_sm,
					cm : mdsource_cm,
					store : mdsource_store
				});

		var funComboBox = new OECP.core.FunComboBox({
					hiddenName : 'post.dept.id',
					rootVisible : true,
					emptyText : '不针对功能分配',
					rootId : 'none',
					rootText : '不针对功能分配',
					nodeClick : function(node) {
						if (node.id != null && node.id != '') {
							if (node.id == 'none' || node.isLeaf()) {
								funComboBox.setValueAndText(node.id, node.text);
								funComboBox.menu.hide();
								mdsource_store.baseParams.funId = node.id;
								mdsource_store.reload();
							} else {
								Ext.Msg.alert("提示", "此节点无效，请重新选择!")
							}
						}
					}
				});
		// 岗位资源列表
		var postMDResourcePanel = new Ext.Panel({
			title : '岗位资源列表',
			region : 'center',
			margins : '0 0 0 3',
			layout : 'hbox',
			layoutConfig : {
				padding : '5',
				align : 'stretch'
			},
			autoScroll : true,
			items : [post_grid, mdsource_grid],
			tbar : [{
				text : '规则分配',
				listeners : {
					'click' : function() {
						if (post_sm.getCount() == 0) {
							Ext.ux.Toast.msg("信息", "请选择一个岗位！");
							return;
						}
						if (post_sm.getCount() > 1) {
							Ext.ux.Toast.msg("信息", "只能选择一个岗位！");
							return;
						}
						if (mdsource_sm.getCount() == 0) {
							Ext.ux.Toast.msg("信息", "请选择一个主资源！");
							return;
						}
						if (mdsource_sm.getCount() > 1) {
							Ext.ux.Toast.msg("信息", "只能选择一个主资源！");
							return;
						}

						var ms = mdsource_grid.getSelectionModel()
								.getSelections();
						var ps = post_grid.getSelectionModel().getSelections();

						dpmv.mdResourceId = ms[0].data['id'];
						dpmv.postId = ps[0].data['id'];

						Ext.Ajax.request({
							url : dpmv.mdsourceFieldsUrl,
							success : function(res) {
								var resjson = Ext.util.JSON
										.decode(res.responseText);
								var fieldDataTmp = [];
								var refTmp = {};
								// code : new Ext.grid.GridEditor(
								for (i = 0; i < resjson.length; i++) {
									var arrTmp = [resjson[i].id,resjson[i].dispName];
									fieldDataTmp.push(arrTmp);
									if(resjson[i].uiClass){
										var c = resjson[i].uiClass;
										refTmp[resjson[i].id] = new Ext.grid.GridEditor({field:Ext.util.JSON
												.decode(c)});
									}
								}
								if (fieldDataTmp.length == 0) {
									fieldDataTmp.push(['没有资源列', '没有资源列']);
								}

								var queryStoreTmp = new Ext.data.JsonStore({
											url : dpmv.dataPermissionUrl,
											baseParams : {
												postId : dpmv.postId,
												mdResourceId : dpmv.mdResourceId,
												funId : mdsource_store.baseParams.funId
											},
											fields : ["id", "operator",
													"value",{name:"field",mapping:"mdField.id"}],
											autoLoad : true
										});

								var queryWindow = new OECP.ui.QueryWindow({
									title : '数据权限分配',
									fieldData : fieldDataTmp,
									queryStore : queryStoreTmp,
									refs : refTmp,
									columnsStore : [['field', '资源列名'],
											['operator', '条件'],
											['value', '属性值'],['fieldType','字段类型']],
									operatorData : [['>', '大于'], ['<', '小于'],
											['>=', '大于或等于'], ['<=', '小于或等于'],
											['=', '等于'], ['!=', '不等于'],
											['like', '相似于']],
									defaultCondition : {
										name : '10010',
										code : '1001'
									},
									buttons : [{
										text : "保存",
										listeners : {
											'click' : function(btn, e) {
												// 获取查询数据
												var qStore = queryWindow.queryGrid
														.getStore();
												var j = 0;
												// 拼装提交对象
												var conditionResult = {}
												for (var i = 0; i < qStore
														.getCount(); i++) {
													var g = queryWindow.queryStore
															.getAt(i);
													conditionResult['dplist['
															+ i + '].id'] = g
															.get('id');
													conditionResult['dplist['
															+ i + '].operator'] = g
															.get('operator');
													conditionResult['dplist['
															+ i + '].value'] = g
															.get('value');
													conditionResult['dplist['
															+ i
															+ '].mdField.id'] = g
															.get('field');
													conditionResult['dplist['
															+ i + '].post.id'] = dpmv.postId;
												}
												conditionResult['postId'] = dpmv.postId;
												conditionResult['mdResourceId'] = dpmv.mdResourceId;
												conditionResult['funId'] = mdsource_store.baseParams.funId;

												Ext.Ajax.request({
													url : dpmv.savedataPermissionUrl,
													success : function(msg) {
														Ext.ux.Toast
																.msg(
																		"信息",
																		Ext.util.JSON
																				.decode(msg.responseText).msg);
														queryWindow.hide();
													},
													failure : function(msg) {
														Ext.Msg.show({
															title : "错误",
															msg : Ext.util.JSON
																	.decode(msg.responseText).msg,
															buttons : Ext.Msg.OK
														});
													},
													params : conditionResult
												});
											}
										}
									}, {
										text : "关闭",
										listeners : {
											'click' : function(btn, e) {
												queryWindow.fireEvent(
														'closebuttonclick',
														queryWindow);
												queryWindow.hide();
											}
										}
									}]
								});

								queryWindow.show();
							},
							failure : function() {
								alert('失败');
							},
							params : {
								mdResourceid : dpmv.mdResourceId
							}
						});

					}
				}
			}, {
				text : '离散分配',
				listeners : {
					'click' : function() {
						if (post_sm.getCount() == 0) {
							Ext.ux.Toast.msg("信息", "请选择一个岗位！");
							return;
						}
						if (post_sm.getCount() > 1) {
							Ext.ux.Toast.msg("信息", "只能选择一个岗位！");
							return;
						}
						if (mdsource_sm.getCount() == 0) {
							Ext.ux.Toast.msg("信息", "请选择一个主资源！");
							return;
						}
						if (mdsource_sm.getCount() > 1) {
							Ext.ux.Toast.msg("信息", "只能选择一个主资源！");
							return;
						}

						var ms = mdsource_grid.getSelectionModel()
								.getSelections();
						var ps = post_grid.getSelectionModel().getSelections();

						dpmv.mdResourceId = ms[0].data['id'];
						dpmv.postId = ps[0].data['id'];

						if (ms[0].data['jsClassName'] == '' || ms[0].data['jsClassName'] == null) {
							Ext.Ajax.request({
								url : dpmv.getFormItemsUrl,
								success : function(res) {
									var fields = Ext.util.JSON
														.decode(res.responseText);
									var fieldsItem = ["id"];					
									var cmItem = [];
									var renderer = function(value, metaData, record, rowIndex, colIndex, store) { 
										var editor = this.editor?this.editor:this.scope.editor;
										if(!editor)
											return value;
										var cmb = editor.field;
										var st = cmb.store;
								        var idx = st.find(cmb.valueField, value);  
								        return (idx != "-1") ? st.getAt(idx).data[cmb.displayField] : ''; 
								    }
									for(var i = 0; i < fields.length; i++){
										fieldsItem.push(fields[i]['name']);
										var col = {header : fields[i]['dispName'], dataIndex : fields[i]['name'],renderer:renderer};
										if(fields[i]['uiClass']){
											var cmb = Ext.util.JSON.decode(fields[i]['uiClass']);
											cmb.autoLoad = true;
											col.editor = new Ext.grid.GridEditor({field:cmb});
										}
										cmItem.push(col);
									}
									var win = new OECP.dataPermission.MDResourceFormWindow({
										pageSize : 10,
										fieldsItem : fieldsItem,
										cmItem : cmItem
									});
									win.show();
									win.addDatas = [];
									win.delDatas = [];
									win.postId = dpmv.postId;
									win.mdResourceId = dpmv.mdResourceId;
									win.funId = mdsource_store.baseParams.funId;
									win.store.baseParams.mdResourceId = dpmv.mdResourceId;
									win.store.load({
												params : {
													start : 0,
													limit : win.pageSize
												}
											});

									// 向后台提交页面所有元素ID，返回已勾选的ID
									win.store.on('load', function() {
										// 拼装提交对象
										var mddataids = {}
										for (var i = 0; i < win.store
												.getCount(); i++) {
											var g = win.store.getAt(i);
											mddataids['idlist[' + i + ']'] = g
													.get('id');
										}
										mddataids['postId'] = dpmv.postId;
										mddataids['mdResourceId'] = dpmv.mdResourceId;
										mddataids['funId'] = mdsource_store.baseParams.funId;

										Ext.Ajax.request({
											url : dpmv.selectCheckedUrl,
											success : function(ids) {
												var idsjson = Ext.util.JSON
														.decode(ids.responseText);
												// 调用勾选方法
												win.idscheck(idsjson);
											},
											failure : function(msg) {

											},
											params : mddataids
										});
									});
								},
								failure : function(msg) {

								},
								params : {
									mdId : ms[0].data['id']
								}
							});
						}
						// var win = eval('(new ' + ms[0].data['jsClassName'] +
						// '())');
					}
				}
			}, funComboBox]
		});
		// 空白部分
		var panStatePanel = new Ext.Panel({
					height : 23,
					layout : 'fit',
					region : 'south',
					title : '&nbsp;',
					baseCls : 'x-plain'
				});

		this.add(deptTreePanel);
		this.add(postMDResourcePanel);
		this.add(panStatePanel);
	}
});