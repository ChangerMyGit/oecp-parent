Ext.ns('Ext.ux.grid');  
/**
 * 表格合计行插件
 * @author slx
 * @param {} config
 */
Ext.ux.grid.GridSummary = function(config) {  
    Ext.apply(this, config);  
};  
  
Ext.extend(Ext.ux.grid.GridSummary, Ext.util.Observable, {  
    init: function(grid) {  
        this.grid = grid;  
        this.cm = grid.getColumnModel();  
        this.view = grid.getView();  
        var v = this.view;  
  
        // override GridView's onLayout() method
        v.onLayout = this.onLayout;  
  
        v.afterMethod('render', this.refreshSummary, this);  
        v.afterMethod('refresh', this.refreshSummary, this);  
        v.afterMethod('syncScroll', this.syncSummaryScroll, this);  
        v.afterMethod('onColumnWidthUpdated', this.doWidth, this);  
        v.afterMethod('onAllColumnWidthsUpdated', this.doAllWidths, this);  
        v.afterMethod('onColumnHiddenUpdated', this.doHidden, this);
  
        // update summary row on store's add/remove/clear/update events
        grid.store.on({  
            add: this.refreshSummary,  
            remove: this.refreshSummary,  
            clear: this.refreshSummary,  
            update: this.refreshSummary,  
            scope: this  
        });
        	
        if (!this.rowTpl) {  
            this.rowTpl = new Ext.Template(  
                '<div class="x-grid3-summary-row x-grid3-gridsummary-row-offset">',  
                    '<table class="x-grid3-summary-table" border="0" cellspacing="0" cellpadding="0" style="{tstyle}" mce_style="{tstyle}">',  
                        '<tbody><tr>{cells}</tr></tbody>',  
                    '</table>',  
                '</div>'  
            );  
            this.rowTpl.disableFormats = true;  
        }  
        this.rowTpl.compile();  
  
        if (!this.cellTpl) {  
            this.cellTpl = new Ext.Template(  
                '<td class="x-grid3-col x-grid3-cell x-grid3-td-{id} {css}" style="{style}" mce_style="{style}">',  
                    '<div class="x-grid3-cell-inner x-grid3-col-{id}" unselectable="on" {attr}>{value}</div>',  
                "</td>"  
            );  
            this.cellTpl.disableFormats = true;  
        }  
        this.cellTpl.compile();  
        if(Ext.isDefined(this.grid.view.lockText)){ // 如果是可锁定列的表格
	        if (!this.cellTplLock) {  
	            this.cellTplLock = new Ext.Template(  
	                '<td class="x-grid3-col x-grid3-cell x-grid3-td-{id} {css}" style="{style}" mce_style="{style}">',  
	                    '<div class="x-grid3-cell-inner x-grid3-col-{id}" unselectable="on" {attr}>{value}</div>',  
	                "</td>"  
	            );  
	            this.cellTplLock.disableFormats = true;  
	        }  
	        this.cellTplLock.compile();  
        }
    },  
  
    calculate: function(rs, cm) {  
        var data = {}, cfg = cm.config;  
        for (var i = 0, len = cfg.length; i < len; i++) { // loop through all columns in ColumnModel
            var cf = cfg[i], // get column's configuration
                cname = cf.dataIndex; // get column dataIndex
  
            // initialise grid summary row data for
            // the current column being worked on
            data[cname] = 0;  
  
            if (cf.summaryType) {  
                for (var j = 0, jlen = rs.length; j < jlen; j++) {  
                    var r = rs[j]; // get a single Record
                    data[cname] = Ext.ux.grid.GridSummary.Calculations[cf.summaryType](r.get(cname), r, cname, data, j);  
                }  
            }  
        }  
  
        return data;  
    },  
  
    onLayout: function(vw, vh) {  
        if (Ext.type(vh) != 'number') { // handles grid's height:'auto' config
            return;  
        }  
        // note: this method is scoped to the GridView
        if (!this.grid.getGridEl().hasClass('x-grid-hide-gridsummary')) {  
            // readjust gridview's height only if grid summary row is visible
			// this.scroller.setHeight(vh - this.summary.getHeight());
        }
        if(Ext.isDefined(this.grid.view.lockText)){
	        this.grid.view.lockedScroller.setHeight(vh - this.summary.getHeight());
	        this.grid.view.syncScroll();// 解决锁定列时，合计被压在表格边框之下，造成行对不齐问题。
        }
    },  
  
    syncSummaryScroll: function() {  
        var mb = this.view.scroller.dom;  
  
        this.view.summaryWrap.dom.scrollLeft = mb.scrollLeft;  
        this.view.summaryWrap.dom.scrollLeft = mb.scrollLeft; // second time for IE (1/2 time first fails, other browsers gnore)
        
		if(Ext.isIE6){ // IE6下设置marginBottom为负数，内层div将会一直向下撑大外层div。而如果设置为0，由于原来就是0，设置后会认为没有改动，效果不变化。
		   this.view.summaryWrap.dom.style.marginBottom = 1 ;
		   this.view.summaryWrap.dom.style.marginBottom = 0;
		}else{
			this.view.summaryWrap.dom.style.marginBottom = 0 - mb.scrollTop;
		}
        if(Ext.isDefined(this.grid.view.lockText)){ // 可锁定列的表格
	        this.view.summaryWrapLock.dom.scrollLeft = mb.scrollLeft;  
	        this.view.summaryWrapLock.dom.scrollLeft = mb.scrollLeft; 
			if(Ext.isIE6){
			   this.view.summaryWrapLock.dom.style.marginBottom = 1 ;
			   this.view.summaryWrapLock.dom.style.marginBottom = 0;
			}else{
				this.view.summaryWrapLock.dom.style.marginBottom = 0 - mb.scrollTop;
			}
        }
    },  
  
    doWidth: function(col, w, tw) {
        var s = this.view.summary.dom;  
        var llen = Ext.isDefined(this.grid.view.lockText)?this.view.cm.getLockedCount():0;
	    
        if(col < llen){
	        var slock = this.view.summaryLock.dom;
	        tw = this.view.lockedHd.getWidth();
	  		slock.style.width = tw;
	        slock.firstChild.style.width = tw;  
	        slock.firstChild.rows[0].childNodes[col].style.width = w;  
	    }else{
	    	tw = this.view.mainBody.getWidth();
	  		s.style.width = tw;
	        s.firstChild.style.width = tw;  
	        col = col-llen;
	        s.firstChild.rows[0].childNodes[col].style.width = w;  
	    }
    },  
  
    doAllWidths: function(ws, tw) {  
    	var slock = this.view.summaryLock.dom, 
        wlenlock = ws.length;  
  
		slock.style.width = tw;
        slock.firstChild.style.width = tw;
        var cellslock = slock.firstChild.rows[0].childNodes;  
  
        for (var j = 0; j < wlenlock; j++) {  
            cellslock[j].style.width = ws[j];  
        }
    	
        var s = this.view.summary.dom, 
        wlen = ws.length;  
  
		s.style.width = tw;
        s.firstChild.style.width = tw;
        var cells = s.firstChild.rows[0].childNodes;  
  
        for (var j = 0; j < wlen; j++) {  
            cells[j].style.width = ws[j];  
        }
    },  
  
    doHidden: function(col, hidden, tw) {  
  
        var llen = Ext.isDefined(this.grid.view.lockText)?this.view.cm.getLockedCount():0
        ,s,display = hidden ? 'none' : '';
        if(col < llen){
	        s = this.view.summaryLock.dom;  
	    }else{
	        s = this.view.summary.dom;
	        col = col-llen;
	    }
        s.firstChild.style.width = tw;  
        s.firstChild.rows[0].childNodes[col].style.display = display;  
    },  
  
    renderSummary: function(o, cs, cm) {
        cs = cs || this.view.getColumnData();  
        var cfg = cm.config,  
            buf = [],  
            buflck = [],
            last = cs.length - 1;  
  
        for (var i = 0, len = cs.length; i < len; i++) {  
            var c = cs[i], cf = cfg[i], p = {};  
  			
            p.id = c.id;  
            p.style = c.style;  
            p.css = i == 0 ? 'x-grid3-cell-first ' : (i == last ? 'x-grid3-cell-last ' : '');  
  
            if (cf.summaryType || cf.summaryRenderer) {  
                p.value = (cf.summaryRenderer || c.renderer)(o.data[c.name], p, o);  
            } else {  
                p.value = '';  
            }  
            // 此处设置默认不显示时用什么符号标记
            if (p.value == undefined || p.value === "") p.value = "&nbsp";  
            if(c.locked){
	            buflck[buflck.length] = this.cellTplLock.apply(p);  
  			}else{
	            buf[buf.length] = this.cellTpl.apply(p);  
  			}
        }  
  
        var result = [];
        result[0] = this.rowTpl.apply({  
            tstyle: 'width:' + this.view.getTotalWidth() + ';',  
            cells: buf.join('')  
        });
        result[1] = Ext.isDefined(this.grid.view.lockText)?this.rowTpl.apply({  
            tstyle: 'width:' + this.view.getLockedWidth() + ';',  
            cells: buflck.join('')  
        }):{};
        
        return result;  
    },  
  
    refreshSummary: function() {  
        var g = this.grid, ds = g.store,  
            cs = this.view.getColumnData(),  
            cm = this.cm,  
            rs = ds.getRange(),  
            data = this.calculate(rs, cm),  
            buf = this.renderSummary({ data: data }, cs, cm);  
        var uphtml = '<div class="x-grid3-summary-row x-grid3-gridsummary-row-offset" style="background: none;border-left: 0px;border-right: 0px;color: none;">'
       	+ '<table class="x-grid3-summary-table" border="0" cellspacing="0" cellpadding="0" >'
       	+ '<tbody><tr><td class="x-grid3-col x-grid3-cell " >'
       	+ '<div class="x-grid3-cell-inner" unselectable="on">&nbsp;</div></td></tr></tbody></table></div>';
        if (!this.view.summaryWrap) {  
            this.view.summaryWrap = Ext.DomHelper.insertBefore(this.view.scroller.last(), {  
                tag: 'div',  
                style : 'position:absolute;bottom:0px;margin-bottom:0;',
                cls: 'x-grid3-gridsummary-row-inner'
            }, true);  
            this.updiv = Ext.DomHelper.insertBefore(this.view.scroller.last(), {  
                tag: 'div',  
                html : uphtml,
                cls: 'x-grid3-gridsummary-row-inner'
            }, true); 
        }  
       this.view.summary = this.view.summaryWrap.update(buf[0]).first();  
       this.updiv.setHeight(this.view.summary.getHeight());
      
       if(Ext.isDefined(this.grid.view.lockText)){
	       if (!this.view.summaryWrapLock) { 
	           this.view.summaryWrapLock = Ext.DomHelper.insertAfter(this.view.lockedBody, {  
	                tag: 'div',  
	                style : 'position:absolute;bottom:0px;margin-bottom:0;',
	                cls: 'x-grid3-gridsummary-row-inner'
	            }, true);  
	          this.updivlocked = Ext.DomHelper.insertAfter(this.view.lockedBody, {  
	                tag: 'div',  
	                html : uphtml,
	                cls: 'x-grid3-gridsummary-row-inner'
	            }, true);  
	        }
	       this.view.summaryLock = this.view.summaryWrapLock.update(buf[1]).first();
	       this.updivlocked.setHeight(this.view.summaryLock.getHeight());
       }
       
    },  
  
    toggleSummary: function(visible) { // true to display summary row
        var el = this.grid.getGridEl();
  
        if (el) {  
            if (visible === undefined) {  
                visible = el.hasClass('x-grid-hide-summary');  
            }  
            el[visible ? 'removeClass' : 'addClass']('x-grid-hide-summary');  
  
            this.view.layout(); // readjust gridview height
        }  
    },  
  
    getSummaryNode: function() {  
        return this.view.summary  
    }  
});  
Ext.reg('gridsummary', Ext.ux.grid.GridSummary);  
  
/*
 * all Calculation methods are called on each Record in the Store with the
 * following 5 parameters:
 * 
 * v - cell value record - reference to the current Record colName - column name
 * (i.e. the ColumnModel's dataIndex) data - the cumulative data for the current
 * column + summaryType up to the current Record rowIdx - current row index
 */  
Ext.ux.grid.GridSummary.Calculations = {  
    sum: function(v, record, colName, data, rowIdx) {  
        return data[colName] + Ext.num(v, 0);  
    },  
  
    count: function(v, record, colName, data, rowIdx) {  
        return rowIdx + 1;  
    },  
  
    max: function(v, record, colName, data, rowIdx) {  
    	var max = data[colName+'max'] === undefined ? (data[colName+'max'] = v) : data[colName+'max'];
        return v > max ? (data[colName+'max'] = v) : max;
    },  
  
    min: function(v, record, colName, data, rowIdx) {  
    	var min = data[colName] === undefined ? (data[colName] = v) : data[colName];
        return v < min ? (data[colName+'min'] = v) : min;
    },  
  
    average: function(v, record, colName, data, rowIdx) {  
        var t = data[colName] + Ext.num(v, 0), count = record.store.getCount();  
        return rowIdx == count - 1 ? (t / count) : t;  
    }  
}  