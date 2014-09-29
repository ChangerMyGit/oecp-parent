Ext.ns('OECP.ui.base');

OECP.ui.base.Grid = Ext.extend(Ext.grid.GridPanel, {

			initComponent : function() {
				if (this.showSM && this.sm) {
					if (this.columns) {
						this.columns = ([this.sm].concat(this.columns));
					} else if (this.colModel) {
						this.colModel.config = [this.sm].concat(this.colModel.config);
					}
				}

				OECP.ui.base.Grid.superclass.initComponent.call(this);
				// 一下设置checkcolumn不可编辑
				var cols = this.getColumnModel().config;
				for(var i=0 ; i< cols.length ; i++){
				    if(cols[i].xtype == 'checkcolumn'){
					cols[i].processEvent = function(name, e, grid,rowIndex, colIndex) {
						return Ext.grid.ActionColumn.superclass.processEvent.apply(this, arguments);
					};
				    }
				}
			}
		});

Ext.reg('oecpgrid', OECP.ui.base.Grid);

OECP.ui.base.EditGrid = Ext.extend(Ext.grid.EditorGridPanel, {

			initComponent : function() {
				if (this.showSM && this.sm) {
					if (this.columns) {
						this.columns = ([this.sm].concat(this.columns));
					} else if (this.colModel) {
						this.colModel.config = [this.sm].concat(this.colModel.config);
					}
				}

				OECP.ui.base.EditGrid.superclass.initComponent.call(this);
			}
		});

Ext.reg('oecpeditgrid', OECP.ui.base.EditGrid);
// 将可编辑的表格中 回车的跳转方式改为跟Tab一样。
Ext.grid.RowSelectionModel.prototype.onEditorKey=function(field, e){
	    var k = e.getKey(), 
	    newCell, 
	    g = this.grid, 
	    last = g.lastEdit,
	    ed = g.activeEditor,
	    shift = e.shiftKey,
	    ae, last, r, c;
	    
	if(k == e.TAB){
	    e.stopEvent();
	    ed.completeEdit();
	    if(shift){
	        newCell = g.walkCells(ed.row, ed.col-1, -1, this.acceptsNav, this);
	    }else{
	        newCell = g.walkCells(ed.row, ed.col+1, 1, this.acceptsNav, this);
	    }
	}else if(k == e.ENTER){
	    if(this.moveEditorOnEnter !== false){
	        if(shift){
	        	newCell = g.walkCells(last.row, last.col - 1, -1, this.acceptsNav, this);
            }else{
                newCell = g.walkCells(last.row, last.col + 1, 1, this.acceptsNav, this);
	        }
	    }
	}
	if(newCell){
	    r = newCell[0];
	    c = newCell[1];
	
	    this.onEditorSelect(r, last.row);
	
	    if(g.isEditor && g.editing){ 
	        ae = g.activeEditor;
	        if(ae && ae.field.triggerBlur){
	            
	            ae.field.triggerBlur();
	        }
	    }
	    g.startEditing(r, c);
	}
};