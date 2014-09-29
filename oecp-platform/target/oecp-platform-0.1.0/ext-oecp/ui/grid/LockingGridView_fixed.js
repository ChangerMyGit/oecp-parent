/**
 * 此js为了修复锁定列与合计行功能共同使用时产生的显示异常。
 */
Ext.ux.grid.LockingGridView.prototype.updateColumnWidth = function(col, width){
        var w = this.getColumnWidth(col),
            llen = this.cm.getLockedCount(),
            ns, rw, c, row;
        this.updateLockedWidth();
        if(col < llen){
            ns = this.getLockedRows();
            rw = this.getLockedWidth();
            c = col;
        }else{
            ns = this.getRows();
            rw = this.getTotalWidth();
            c = col - llen;
        }
        var hd = this.getHeaderCell(col);
        hd.style.width = w;
        for(var i = 0, len = ns.length; i < len; i++){
            row = ns[i];
            row.style.width = rw;
            if(row.firstChild){
                row.firstChild.style.width = rw;
                row.firstChild.rows[0].childNodes[c].style.width = w;
            }
        }
        this.onColumnWidthUpdated(col, w, this.getTotalWidth());
        this.syncHeaderHeight();
        // slx 修正 解决拖动列宽时，锁定部分数据显示区过大，覆盖非锁定区横向滚动条区域。
        var mb = this.scroller.dom;
		this.lockedScroller.dom.style.height = mb.clientHeight;
    };
    
Ext.ux.grid.LockingGridView.prototype.refresh = function(headersToo){
        this.fireEvent('beforerefresh', this);
        this.grid.stopEditing(true);
        var result = this.renderBody();
        this.mainBody.update(result[0]).setWidth(this.getTotalWidth());
        this.lockedBody.update(result[1]).setWidth(this.getLockedWidth());
        if(headersToo === true){
            this.updateHeaders();
            this.updateHeaderSortState();
        }
        this.processRows(0, true);
        this.layout();
        this.applyEmptyText();
        this.fireEvent('refresh', this);
        // slx 修正 ，当活动区域出现滚动条时，锁定区域的行不对应。
        var mb = this.scroller.dom;
		this.lockedScroller.dom.style.height = mb.clientHeight;
		this.syncScroll();
    };
    
Ext.ux.grid.LockingGridView.prototype.getColumnData=function(){
	var cs = [], cm = this.cm, colCount = cm.getColumnCount();
	for(var i = 0; i < colCount; i++){
		var name = cm.getDataIndex(i);
		cs[i] = {
		name : (!Ext.isDefined(name) ? (this.ds.fields.get(i)?this.ds.fields.get(i).name:'') : name),
		renderer : cm.getRenderer(i),
		scope   : cm.getRendererScope(i),
		id : cm.getColumnId(i),
		style : this.getColumnStyle(i),
		locked : cm.isLocked(i)
		};
	}
	return cs;
};