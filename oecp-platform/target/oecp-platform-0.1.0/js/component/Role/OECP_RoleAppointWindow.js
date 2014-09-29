Ext.ns('OECP.role');
/**
 * 角色委派窗口
 * 
 * @author liujingtao
 * @class OECP.role.RoleAppointWindow
 * @extends Ext.Window
 */
OECP.role.RoleAppointWindow = Ext.extend(Ext.Window, {
	title : '角色委派',
	height : 400,
	width : 360,
	modal: true,
	/**
	 * @cfg {String} hasRoleUserUrl 有角色的用户列表url
	 */
	hasRoleUserUrl : '',
	/**
	 * @cfg{String} notHasRoleUserUrl 没有角色的用户列表url
	 */
	notHasRoleUserUrl : '',
	/**
	 * @cfg {String} orgRoleId 角色主键
	 */
	orgRoleId : '',
	initComponent : function() {
		var scope = this;
		this.ds1 = new Ext.data.JsonStore({
					url : __ctxPath + scope.notHasRoleUserUrl,
					baseParams : {
						id : scope.orgRoleId
					},
					root : 'result',
					fields : ['id', 'name'],
					autoLoad : true
				});
		this.ds2 = new Ext.data.JsonStore({
					url : __ctxPath + scope.hasRoleUserUrl,
					baseParams : {
						id : scope.orgRoleId
					},
					root : 'result',
					fields : ['id', 'name'],
					autoLoad : true
				});
		this.formPanel = new Ext.form.FormPanel({
					bodyStyle : 'padding:10px;',
					items : [{
						xtype : 'itemselector',
						name : 'itemselector',
						hideLabel : true,
						imagePath : __ctxPath + '/extjs/ux/images/',
						drawUpIcon : false,
						drawDownIcon : false,
						drawTopIcon : false,
						drawBotIcon : false,
						multiselects : [{
									legend : '未分配用户',
									width : 150,
									height : 200,
									store : scope.ds1,
									displayField : 'name',
									valueField : 'id'
								}, {
									legend : '已分配用户',
									width : 150,
									height : 200,
									store : scope.ds2,
									displayField : 'name',
									valueField : 'id',
									tbar : [{
										text : '清空',
										handler : function() {
											scope.formPanel.getForm()
													.findField('itemselector')
													.reset();
										}
									}]
								}]
					}],
					buttons : [{
						text : '保存',
						handler : function() {
							if (scope.formPanel.getForm().isValid()) {
								var _data = scope.formPanel.getForm()
										.getValues(false);
								var _itemselector = [];
								if (_data.itemselector) {
									_itemselector = _data.itemselector
											.split(',');
								}
								Ext.Ajax.request({
									method : 'POST',
									url : __ctxPath
											+ '/role/saveUsersRole.do',
									params : {
										usersJson : _itemselector,
										orgRoleId : scope.orgRoleId
									},
									success : function(request) {
										var json = Ext.util.JSON
												.decode(request.responseText);
										Ext.ux.Toast
												.msg('信息', json.msg);
										scope.close();
									},
									failure : function(request) {
										Ext.ux.Toast.msg('信息',
												'保存失败,请联系管理员！');
										scope.close();
									}
								});
							}
						}
					}]
				});
		this.items = [scope.formPanel];
		OECP.role.RoleAppointWindow.superclass.initComponent.call(this);
	}
});