
Ext.ns("OECP.person");
/**
 * 兼职岗位窗口
 * @author yangtao
 * @class OtherPostSelectWindow
 * @extends Ext.form.ComboBox
 */
OECP.person.OtherPostSelectWindow = Ext.extend(OECP.ui.CommonWindow, {
		id : "OECP.person.OtherPostSelectWindow",
		title : '兼职岗位',
		width : 600,
		height : 520,
		/**
		 * 排除掉已经选择的岗位
		 * @type 
		 */
		postIds : null,
		/**
		 * 部门树panel
		 * @type 
		 */
		deptTreePanel : undefined,
		/**
		 * 岗位grid
		 * @type 
		 */
		postGridPanel : undefined,
		/**
		 * 窗口中心面板
		 * @type 
		 */
		contentPanel : undefined,
		/**
		 * 初始化方法
		 */
		initComponent : function() {
			var me = this;
			this.postIds = this.postIds;
			this.addEvents("doConfirm");
			this.initWindowBtns(me);
			this.initWestContent(me);
			this.initCenterContent(me);
			OECP.person.OtherPostSelectWindow.superclass.initComponent.call(this);
		},
		/**
		 * 初始化窗口里面的按钮
		 * @param {} me
		 */
		initWindowBtns : function(me){
			var confirmBtn = new OECP.ui.Button({
					text : "确定",
					iconCls : 'btn-save',
					handler : function(){
						me.fireEvent('doConfirm',me.postGridPanel.getSelectionModel().getSelections());
						me.close();
					}
				});
	
			var closeBtn = new OECP.ui.Button({
					text : "关闭",
					iconCls : 'btn-cancel',
					handler : function(){
						me.close();
					}
				});
	
			me.buttonArray = [confirmBtn,closeBtn];
		},
		/**
		 * 初始化west内容
		 * @param {} me
		 */
		initWestContent : function(me){
			if(!Ext.isDefined(me.deptTreePanel)){
				me.deptTreePanel = new Ext.tree.TreePanel({
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
									dataUrl : __ctxPath
											+ "/dept/depttree.do"
								}),
						listeners : {
							"click" : function(node) {
								me.postGridPanel.getStore().removeAll();
								me.postGridPanel.getStore().baseParams.deptid = node.id;
								me.postGridPanel.getStore().baseParams.postIds = me.postIds;
								me.postGridPanel.getStore().load();
							}
						}
				});
			}
		},
		/**
		 * 初始化center内容
		 * @param {} me
		 */
		initCenterContent : function(me){
			// 岗位资料
			var store = new Ext.data.JsonStore({
					url : __ctxPath + "/person/getPostList.do",
					storeId : 'id',
					fields : ['id', 'name', 'code', 'charge',
							'parent.id', 'parent.name', 'dept.id',
							'dept.name']
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
						dataIndex : "dept.id",
						hidden : true
			}]);

			// 详细信息显示
			me.postGridPanel = new Ext.grid.GridPanel({
					region : 'center',
					margins : '0 0 0 3',
					enableDD : false,
					enableDrag : false,
					autoScroll : true,
					rootVisible : false,
					sm : sm,
					cm : postData_cm,
					store : store
			});
			me.contentPanel = new Ext.Panel({
				width : 580,
				height : 420,
				layout : 'border',
				autoScroll	: true,
				items : [me.deptTreePanel,me.postGridPanel]
			});
			me.componetArray = [me.contentPanel];
		}
});