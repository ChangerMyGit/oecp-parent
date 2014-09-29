// 部门管理界面
Ext.ns("OECP.post");
/**
 * @author liujingtao
 * @class ParentPostSelectComboBox
 * @extends Ext.form.ComboBox
 */
OECP.post.ParentPostSelectComboBox = Ext.extend(Ext.form.ComboBox, {
			initComponent : function() {
				this.initBox();
				OECP.post.ParentPostSelectComboBox.superclass.initComponent
						.call(this);
			},

			onTriggerClick : function() {
				this.postData_store.removeAll();
				this.onFocus();
				this.box.show();
			},
			initBox : function() {
				var ppscb = this;

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
										id : "dept_root_cbb",
										text : "所有部门"
									}),

							loader : new Ext.tree.TreeLoader({
										dataUrl : __ctxPath + "/dept/depttree.do",
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
									ppscb.postData_store.removeAll();
									ppscb.postData_store.baseParams.deptid = node.id;
									ppscb.postData_store.baseParams.postid = ppscb.postid;
									ppscb.postData_store.load();
								}
							}
						});

				// 岗位资料
				ppscb.postData_store = new Ext.data.JsonStore({
							url : __ctxPath + "/post/getPostList.do",
							baseParams : {
								deptid : ""
							},
							storeId : 'id',
							fields : ['id', 'name', 'code', 'charge',
									'parent.id', 'parent.name', 'dept.id',
									'dept.name'],
							autoLoad : true
						});

				var sm = new Ext.grid.CheckboxSelectionModel();// 选择器

				var postData_cm = new Ext.grid.ColumnModel([sm,
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
						}, {
							header : "是否管理岗位",
							dataIndex : "charge"
						}, {
							header : "上级岗位主键",
							dataIndex : "parent.id",
							hidden : true
						}, {
							header : "上级岗位名称",
							dataIndex : "parent.name"
						}, {
							header : "所属部门主键",
							dataIndex : "dept.id"
						}]);

				// 详细信息显示
				var postDataPanel = new Ext.grid.GridPanel({
							title : '详细信息',
							region : 'center',
							margins : '0 0 0 3',
							enableDD : false,
							enableDrag : false,
							autoScroll : true,
							rootVisible : false,
							sm : sm,
							cm : postData_cm,
							store : ppscb.postData_store
						});

				ppscb.store = ppscb.postData_store;

				ppscb.onOkClick = function() {
					if (sm.getCount() == 0) {
						Ext.ux.Toast.msg("信息", "请选择一条记录！");
					} else if (sm.getCount() > 1) {
						Ext.ux.Toast.msg("信息", "只能选择一个上级部门！");
					} else {
						var b = postDataPanel.getSelectionModel()
								.getSelections();
						ppscb.setValue(b[0][ppscb.valueField]);// 赋值
						ppscb.box.hide();// 隐藏窗体
					}
				}
				
				ppscb.box = new Ext.Window({
							layout : 'border',
							title : '上级岗位',
							height : 400,
							width : 600,
							modal : true,
							closable : true,
							closeAction : 'hide',
							items : [deptTreePanel, postDataPanel],
							buttons : ['->', '-', {
										text : '确定',
										listeners : {
											// 按钮点击事件 获取并处理显示值与实际值
											'click' : function(e) {
												ppscb.onOkClick();
											}
										}
									}, '-', {
										text : '关闭',
										listeners : {
											'click' : function(e) {
												ppscb.box.hide();
											}
										}
									}, '-']
						});
			},
			onDestroy:function(){
				if(this.box){
					Ext.destroy(this.box);
				}
				OECP.post.ParentPostSelectComboBox.superclass.onDestroy.call(this);
			}
		});