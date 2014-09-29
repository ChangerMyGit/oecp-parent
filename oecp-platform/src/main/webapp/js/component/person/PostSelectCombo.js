
Ext.ns("OECP.person");
/**
 * 任职岗位combo管理
 * @author yangtao
 * @class PostSelectCombo
 * @extends Ext.form.ComboBox
 */
OECP.person.PostSelectCombo = Ext.extend(Ext.form.ComboBox, {
		/**
		 * 选中的部门标识
		 * @type 
		 */
		deptId : null,
		/**
		 * 初始化方法
		 */
		initComponent : function() {
			this.initBox();
			OECP.person.PostSelectCombo.superclass.initComponent.call(this);
		},
		/**
		 * 初始化box
		 */
		initBox : function() {
			var This = this;
			// 岗位资料
			This.store = new Ext.data.JsonStore({
					url : __ctxPath + "/person/getPostList.do",
					baseParams : {
						deptid : This.deptId
					},
					storeId : 'id',
					fields : ['id', 'name', 'code', 'charge',
							'parent.id', 'parent.name', 'dept.id',
							'dept.name']
			});

			var sm = new Ext.grid.CheckboxSelectionModel();// 选择器
			var cm = new Ext.grid.ColumnModel([sm,
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
			var postDataPanel = new Ext.grid.GridPanel({
						region : 'center',
						margins : '0 0 0 3',
						enableDD : false,
						enableDrag : false,
						autoScroll : true,
						rootVisible : false,
						sm : sm,
						cm : cm,
						store : This.store
			});
			postDataPanel.getSelectionModel().singleSelect = true;// 单选
			
			This.onOkClick = function() {
				if (sm.getCount() == 0) {
					Ext.ux.Toast.msg("信息", "请选择一条记录！");
				} else if (sm.getCount() > 1) {
					Ext.ux.Toast.msg("信息", "只能选择一个任职岗位！");
				} else {
					var b = postDataPanel.getSelectionModel().getSelections();
					This.setValue(b[0][This.valueField]);// 赋值
					This.box.hide();// 隐藏窗体
				}
			}
			
			This.box = new Ext.Window({
				layout : 'border',
				title : '任职岗位',
				height : 400,
				width : 600,
				modal : true,
				closable : true,
				closeAction : 'hide',
				items : [postDataPanel],
				buttons : ['->', '-', {
							text : '确定',
							iconCls : 'btn-save',
							listeners : {
								// 按钮点击事件 获取并处理显示值与实际值
								'click' : function(e) {
									This.onOkClick();
								}
							}
						}, '-', {
							text : '关闭',
							iconCls : 'btn-cancel',
							listeners : {
								'click' : function(e) {
									This.box.hide();
								}
							}
						}, '-']
			});
			This.store.removeAll();
			This.store.reload();		
		},
		onDestroy:function(){
			if(this.box){
				Ext.destroy(this.box);
			}
			OECP.person.PostSelectCombo.superclass.onDestroy.call(this);
		},
		onTriggerClick : function() {
			this.onFocus();
			this.box.show();
		}
});