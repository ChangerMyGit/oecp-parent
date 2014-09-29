
Ext.ns('OECP.ui.comp');

/**
 * 添加取消删除前事件beforerowselect，用于处理一些不允许取消删除的数据。
 * @author slx
 * @class OECP.ui.comp.OECPCheckboxSelectionModel
 * @extends Ext.grid.CheckboxSelectionModel
 */
OECP.ui.comp.OECPCheckboxSelectionModel = Ext.extend(Ext.grid.CheckboxSelectionModel,{

	constructor : function(){
		this.addEvents('beforerowdeselect');
        OECP.ui.comp.OECPCheckboxSelectionModel.superclass.constructor.apply(this, arguments);
    },
    
    deselectRow : function(index, preventViewNotify){
        if(this.isLocked()){
            return;
        }
        if(this.last == index){
            this.last = false;
        }
        if(this.lastActive == index){
            this.lastActive = false;
        }
        var r = this.grid.store.getAt(index);
        if(r && this.fireEvent('beforerowdeselect', this, index, r) !== false){
            this.selections.remove(r);
            if(!preventViewNotify){
                this.grid.getView().onRowDeselect(index);
            }
            this.fireEvent('rowdeselect', this, index, r);
            this.fireEvent('selectionchange', this);
        }
    }
})
