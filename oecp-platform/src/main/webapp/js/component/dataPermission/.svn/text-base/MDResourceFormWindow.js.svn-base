Ext.ns('OECP.dataPermission');
/**
 *  主数据资源权限离散分配窗口
 * @class OECP.dataPermission.MDResourceFormWindow
 * @extends Ext.Window
 */
OECP.dataPermission.MDResourceFormWindow = Ext.extend(Ext.Window, {
			title : '数据资源离散分配',
			width : 400,
			height : 400,
			pageSize : 10,
			plain : true,
			modal : true,
			layout : 'hbox',
			mdResourceId : '',
			postId : '',
			funId : '',
			buttons : [],
			buttonAlign : 'center',
			layoutConfig : {
				padding : '5',
				align : 'stretch'
			},
			autoScroll : true,
			store : null,
			storeUrl : __ctxPath + "/datapermission/getAllmdData.do",
			saveDatasUrl : __ctxPath + "/datapermission/saveDatas.do",
			addDatas : [],
			delDatas : [],
			cmItem : [],
			fieldsItem : [],
			initComponent : function() {
				var pmdrw = this;
				OECP.dataPermission.MDResourceFormWindow.superclass.initComponent
						.call(this);
				pmdrw.store = new Ext.data.JsonStore({
							totalProperty : 'totalCounts',
							root : 'result',
							url : this.storeUrl,
							baseParams : {
								mdResourceId : ""
							},
							fields : pmdrw.fieldsItem
						});
				// 资源选择器
				pmdrw.sm = new Ext.grid.CheckboxSelectionModel();
				var cmDatas = [pmdrw.sm].concat(pmdrw.cmItem);
				pmdrw.cm = new Ext.grid.ColumnModel(cmDatas);

				pmdrw.gridPanel = new Ext.grid.GridPanel({
							flex : 1,
							enableDrag : false,
							autoScroll : true,
							viewConfig : {
								forceFit : true
							},
							sm : this.sm,
							cm : this.cm,
							store : this.store,

							bbar : new Ext.PagingToolbar({
										pageSize : pmdrw.pageSize,
										store : this.store,
										displayInfo : true,
										displayMsg : "当前页记录索引{0}-{1}， 共{2}条记录",
										emptyMsg : "当前没有记录"
									})
						});
				// 得到id数组后回填函数
				pmdrw.idscheck = function(idsjson) {
					pmdrw.sm.un('rowselect',
							pmdrw.saveData);
					pmdrw.sm.un('rowdeselect',
							pmdrw.deleteData);
					var checkrows = [];
					for (var i = 0; i < pmdrw.store.getCount(); i++) {
						var g = pmdrw.store.getAt(i);
						if(idsjson.indexOf(g.get('id')) == -1 && pmdrw.addDatas.indexOf(g.get('id')) != -1){
							checkrows.push(i);
						} else if(idsjson.indexOf(g.get('id')) != -1 && pmdrw.delDatas.indexOf(g.get('id')) == -1){
							checkrows.push(i);
						}
					}
					pmdrw.sm.selectRows(checkrows);
					
					// 保存方法
					pmdrw.saveData = function(sele, rowIndex) {
						var g = pmdrw.store.getAt(rowIndex);
						var dataId = g.get('id');
						var num = pmdrw.delDatas.indexOf(dataId);
						for (var j = 0; j < idsjson.length; j++) {
							if (g.get('id') == idsjson[j]) {
								checkrows.push(i)
							}
						}
						if(num == -1){
							if(pmdrw.addDatas.indexOf(dataId) == -1 && idsjson.indexOf(dataId) == -1){
								pmdrw.addDatas.push(dataId);
							}
						} else {
							pmdrw.delDatas.remove(dataId);
						}
					}
					// 删除方法
					pmdrw.deleteData = function(sele, rowIndex) {
						var g = pmdrw.store.getAt(rowIndex);
						var dataId = g.get('id');
						var num = pmdrw.addDatas.indexOf(dataId);
						if(num == -1){
							if(pmdrw.delDatas.indexOf(dataId) == -1 && idsjson.indexOf(dataId) != -1){
								pmdrw.delDatas.push(dataId);
							}
						} else {
							pmdrw.addDatas.remove(dataId);
						}
					}
					
					// 添加监听，当勾选时，服务器端保存数据
					pmdrw.sm.on('rowselect',
							pmdrw.saveData);

					// 添加监听，当取消勾选时，服务器端删除数据
					pmdrw.sm.on('rowdeselect',
							pmdrw.deleteData);
							
				}

				this.add(pmdrw.gridPanel);
				var btn_save = new Ext.Button({
							text : '保存',
							handler : function() {
								Ext.Ajax.request({
									url : pmdrw.saveDatasUrl,
									success : function(msg) {
										Ext.ux.Toast.msg("信息",Ext.util.JSON.decode(msg.responseText).msg);
										pmdrw.close();
									},
									failure : function() {
										Ext.Msg.show({
												title : "错误",
												msg : msg.result.msg,
												buttons : Ext.Msg.OK
											});
									},
									params : {
										addDatas : pmdrw.addDatas,
										delDatas : pmdrw.delDatas,
										mdResourceId : pmdrw.mdResourceId,
										postId : pmdrw.postId,
										funId : pmdrw.funId
									}
								});
							}
						});
				var btn_close = new Ext.Button({
							text : '关闭',
							handler : function() {
								pmdrw.close();
							}
						});
				pmdrw.addButton(btn_save);
				pmdrw.addButton(btn_close);
			}
		});