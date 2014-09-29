/**
 * @author wangliang
 *         <p>
 *         ColumnModel 用于EditGridPanel单元格弹出自定义控件
 *         </p>
 */
Ext.ns('OECP.ui.grid');
/**
 * 构造函数
 * 
 * @param {}
 *            grid gridPanel
 * @param {}
 *            store 数据仓库
 * @param {}
 *            column 列
 * @param {}
 *            ref 自定义参照数组 obj 类似下列
 * 
 * <pre><code>
 * 			{ 
 * 				'GENDER':new Ext.grid.GridEditor({
 * 					store : new Ext.data.SimpleStore({
 * 					fields : ['value','text'],
 * 					data : 	[['0100','男'],['0101','女']]
 * 					}),
 * 					emptyText : '请输入',
 * 					mode : 'local',
 * 					triggerAction : 'all',
 * 					valueField : 'value',
 * 					displayField : 'text',
 * 					readOnly : true
 * 			})},
 * 			'AGE',new Ext.grid.GridEditor(new Ext.form.NumberField({selectOnFocus:true, style:'text-align:left;'}))
 * 			}
 * </code></pre>
 */
OECP.ui.grid.QueryColumnModel = function(grid, store, column, ref) {
	this.grid = grid;
	this.store = store;
	this.customEditors = ref;
	OECP.ui.grid.QueryColumnModel.superclass.constructor.call(this, column);
};
Ext.extend(OECP.ui.grid.QueryColumnModel, Ext.grid.ColumnModel, {

			/**
			 * 重载父类方法 返回编辑器中定义的单元格/列.
			 * 
			 * @param {Number}
			 *            colIndex 列索引
			 * @param {Number}
			 *            rowIndex 行索引
			 * @return {Ext.Editor} The {@link Ext.Editor Editor} that was
			 *         created to wrap the {@link Ext.form.Field Field} used to
			 *         edit the cell.
			 */
			getCellEditor : function(colIndex, rowIndex) {
				// 判断是最后一个字段
				if (colIndex == 3) {
					var p = this.store.getAt(rowIndex);
					field_str = p.data.field;
					// 返回自定义编辑器
					if (this.customEditors && this.customEditors[field_str]) {
						return this.customEditors[field_str];
					}
				}
				return this.config[colIndex].getCellEditor(rowIndex);
			}
		});
