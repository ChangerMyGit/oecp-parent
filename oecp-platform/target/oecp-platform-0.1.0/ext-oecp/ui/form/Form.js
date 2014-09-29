Ext.ns('OECP.ui');
/**
 * 重载Form类
 * 
 * @class OECP.ui.FormPanel
 * @extends Ext.FormPanel
 */
OECP.ui.FormPanel = Ext.extend(Ext.FormPanel, {
			createForm : function() {
				var config = Ext.applyIf({
							listeners : {}
						}, this.initialConfig);
				return new OECP.ui.BasicForm(null, config);
			}
		});
Ext.reg('oecpform', OECP.ui.FormPanel);
/**
 * 重载BasicForm类的setValues方法，对field为带分页的空间进行赋值处理，解决调用setValue中可能出现显示编码不显示汉字问题<br> *
 * <code>
 var test = new OECP.ui.ref.GridWinRef({
 name : 'o.dept.id',
 fieldLabel : '部门',
 valueField : 'id',
 displayField : 'code',
 width : 120,
 entityName : 'oecp.platform.org.eo.Department',
 functionCode : 'testbill',
 fieldName : 'o.dept',
 codeField : 'code',
 pageSize : 2
 });
 form = new OECP.ui.FormPanel({
 reader : new Ext.data.JsonReader({}, [{
 name : 'o.dept',
 mapping : 'dept.code'
 }, 'org']),
 width : 500,
 height : 200,
 items : [test, {
 xtype : 'textfield',
 fieldLabel : '公司',
 name : 'o.org',
 dataIndex : 'org'
 }],
 bbar : [{
 xtype : 'button',
 text : '加载',
 handler : function() {
 var json = {};
 json['o.dept.id'] = '227162b7k9260008';
 json['o.dept.code'] = 'code06';
 json['org'] = '公司1';
 form.getForm().setValues(json);
 }
 }]
 });

 Json 数据 :{'o.dept.id':'227162b7k9260008','o.dept.code':'code06','org':'公司1'}
 注意加载的数据格式中一定要包含与控件displayFild属性相同的字段也就是"code"。
 </code>
 * 
 * @class OECP.ui.BasicForm
 * @extends Ext.form.BasicForm
 */
OECP.ui.BasicForm = Ext.extend(Ext.form.BasicForm, {
	setValues : function(values) {
		if (Ext.isArray(values)) {
			for (var i = 0, len = values.length; i < len; i++) {
				var v = values[i];
				var f = this.findField(v.id);
				if (f) {
					if (Ext.isFunction(f.setValueEx) && f.displayField) {
						var varg = v.value.split('.');
						varg[varg.length - 1] = f.displayField;
						var nid = varg.join('.');
						var nval = '';
						for (var j = 0; j < values.length; j++) {
							if (values[j].id === nid) {
								nval = values[j].value;
								break;
							}
						}
						f.setValueEx(v.value, nval);
					} else {
						f.setValue(v.value);
					}
					if (this.trackResetOnLoad) {
						f.originalValue = f.getValue();
					}
				}
			}
		} else {
			var field, id;
			for (id in values) {
				if (!Ext.isFunction(values[id]) && (field = this.findField(id))) {
					if (Ext.isFunction(field.setValueEx) && field.displayField) {
						var varg = id.split('.');
						varg[varg.length - 1] = field.displayField;
						field.setValueEx(values[id], values[varg.join(".")]);
					} else {
						field.setValue(values[id]);
					}
					if (this.trackResetOnLoad) {
						field.originalValue = field.getValue();
					}
				}
			}
		}
		return this;
	}
});
