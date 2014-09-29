Ext.ns('OECP.ui.combobox');
/**
 * 枚举辅助编码
 * 
 * @author wangliang
 * @class OECP.ui.combobox.EnumsCombo
 * @extends Ext.form.ComboBox
 */
OECP.ui.combobox.EnumsCombo = Ext.extend(Ext.form.ComboBox, {
			/**
			 * @cfg {String} className 枚举类名
			 */
			/**
			 * @cfg {Array} exclude 排除列表
			 */
			exclude : [],
			/**
			 * @cfg {String} valueField 值字段
			 */
			valueField : 'value',
			/**
			 * 
			 * @cfg {String} displayField 显示字段
			 */
			displayField : 'name',
			/**
			 * @cfg {String} emptyText 提示信息
			 */
			emptyText : '请选择',
			/**
			 * 
			 * @cfg {String} mode 加载方式
			 */
			mode : 'local',
			editable : false,
			triggerAction : 'all',
			initComponent : function() {
				var scope = this;
				if (!this.className) {
					this.store = new Ext.data.SimpleStore();
				}
				if (!this.store) {
					this.store = new Ext.data.JsonStore({
								url : __ctxPath + "/enums/ref.do",
								root : 'result',
								fields : ['value', 'name'],
								autoLoad : true,
								baseParams : {
									className : scope.className
								}
							});
					if (!Ext.isEmpty(this.exclude)) {
						this.store.baseParams['exclude'] = this.exclude;
					}
				}
				OECP.ui.combobox.EnumsCombo.superclass.initComponent.call(this);
			}
		});
Ext.reg('enumscombo', OECP.ui.combobox.EnumsCombo);