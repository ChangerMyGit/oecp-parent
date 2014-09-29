Ext.ns('OECP.grid');
/**
 * @author wangliang
 *         <p>
 *         参照编辑器 扩展自 Ext.grid.GridEditor<br>
 *         针对编辑器失去焦点后就赋值的逻辑进行修改.造成自定义参选数据总是为空<br>
 *         更新为当编辑器发出选择后事件时再进行数据赋值
 *         </p>
 */
OECP.grid.RefGridEditor = function(field, config) {
	OECP.grid.RefGridEditor.superclass.constructor.call(this, field, config);
	var master = this;
	// 当参照选择结束后 通知回写数据
	field.on('refselect', function(ref) {
				if (master.allowBlur === true && master.editing
						&& master.selectSameEditor !== true) {
					master.completeEdit();
				}
			});
};

Ext.extend(OECP.grid.RefGridEditor, Ext.grid.GridEditor, {
			onBlur : function() {
				// 覆盖原有方法，不执行数据回写操作
			}
		});