// 单据引用档案下拉组件
Ext.ns("OECP.ui");
/**
 * @author luanyoubo
 * @class ArchivesSelectCombo
 * @extends Ext.form.ComboBox
 */
OECP.ui.ArchivesSelectCombo = Ext.extend(Ext.form.ComboBox, {
    loadUrl : '',
    dataFields : [],// 数据映射字段
    columnItems : [],
    dataStore : null,// 数据源
    enableKeyEvents : true,
    pageSize : 10,
    listWidth : 400,
    resizable : false,
    triggerAction : 'all',
    queryParam : 'bill.code',
    initComponent : function() {
	/**
	 * @cfg {Ext.util.MixedCollection} historicalData 历史数据集<br>
	 *      用于缓存已经选择过或过滤过的数据集合.分页时使用
	 */
	this.historicalData = new Ext.util.MixedCollection(false, function(field) {
		return field[this.valueField || 'id'];
	});
	OECP.ui.ArchivesSelectCombo.superclass.initComponent.call(this);
	this.initDorpDown();
    },
    initDorpDown : function() {
	this.initStore();
	this.initDorpDownGrid();
	// this.initMenu();
    },
    initDorpDownGrid : function() {
	var me = this;
	var sm = new Ext.grid.RowSelectionModel({
	    singleSelect : this.singleSelect == undefined ? true : this.singleSelect
	});// 选择器
	var cm = new Ext.grid.ColumnModel(OECP.core.util.clone(this.columnItems));
	// 详细信息显示
	this.grid = new Ext.grid.GridPanel({
	    region : 'center',
	    margins : '0 0 0 3',
	    // autoScroll : true,
	    height : 200,
	    wigth : 200,
	    sm : sm,
	    cm : cm,
	    store : me.store
	});
	// this.grid.on("rowclick",onGridClick);
    },
    initStore : function(recreate) {
	var me = this;
	if (!this.store || recreate) { // 下拉自动提示的store(未初始化，或者刻意重建）。
	    this.store = new Ext.data.JsonStore({
		url : __ctxPath + this.loadUrl,
		baseParams : {
		    functionCode : me.functionCode,
		    start : 0,
		    limit : 25
		},
		totalProperty : 'totalCounts',
		root : 'result',
		remoteSort : true,
		fields : this.dataFields
	    });
	    this.store.on({
		scope : this,
		load : this.onLoad,
		exception : this.collapse
	    });
	    // this.store.load();
	}
    },
    // 覆盖父类方法，将下拉列表改成下拉表格
    initList : function() {
	if (!this.list) {
	    var cls = 'x-combo-list', listParent = Ext.getDom(this.getListParent() || Ext.getBody());

	    this.list = new Ext.Layer({
		parentEl : listParent,
		shadow : this.shadow,
		cls : [ cls, this.listClass ].join(' '),
		constrain : false,
		zindex : this.getZIndex(listParent)
	    });

	    var lw = this.listWidth || Math.max(this.wrap.getWidth(), this.minListWidth);
	    this.list.setSize(lw, 0);
	    this.list.swallowEvent('mousewheel');
	    this.assetHeight = 0;
	    if (this.syncFont !== false) {
		this.list.setStyle('font-size', this.el.getStyle('font-size'));
	    }
	    if (this.title) {
		this.header = this.list.createChild({
		    cls : cls + '-hd',
		    html : this.title
		});
		this.assetHeight += this.header.getHeight();
	    }

	    this.innerList = this.list.createChild({
		cls : cls + '-inner'
	    });
	    
	    this.innerList.setWidth(lw - this.list.getFrameWidth('lr'));

	    if (this.pageSize) {
		this.footer = this.list.createChild({
		    cls : cls + '-ft'
		});
		this.pageTb = new Ext.PagingToolbar({
		    store : this.store,
		    pageSize : this.pageSize,
		    renderTo : this.footer
		});
		this.assetHeight += this.footer.getHeight();
	    }

	    this.view = this.grid;
	    this.view.render(this.innerList);

	    this.mon(this.view, {
		containerclick : this.onViewClick,
		click : this.onViewClick,
		scope : this
	    });

	    if (this.resizable) {
		this.resizer = new Ext.Resizable(this.list, {
		    pinned : true,
		    handles : 'se'
		});
		this.mon(this.resizer, 'resize', function(r, w, h) {
		    this.maxHeight = h - this.handleHeight - this.list.getFrameWidth('tb') - this.assetHeight;
		    this.listWidth = w;
		    this.innerList.setWidth(w - this.list.getFrameWidth('lr'));
		    this.restrictHeight();
		}, this);

		this[this.pageSize ? 'footer' : 'innerList'].setStyle('margin-bottom', this.handleHeight + 'px');
	    }
	}
    },
    // 覆盖父类方法，增加了自动计算“下拉选项”的高度
    expand : function() {
	if (this.isExpanded() || !this.hasFocus) {
	    return;
	}

	if (this.title || this.pageSize) {
	    this.assetHeight = 0;
	    if (this.title) {
		this.assetHeight += this.header.getHeight();
	    }
	    if (this.pageSize) {
		this.assetHeight += this.footer.getHeight();
	    }
	}

	if (this.bufferSize) {
	    this.doResize(this.bufferSize);
	    delete this.bufferSize;
	}
	this.list.alignTo.apply(this.list, [ this.el ].concat(this.listAlign));

	// zindex can change, re-check it and set it if necessary
	this.list.setZIndex(this.getZIndex());
	this.list.show();
	if (Ext.isGecko2) {
	    this.innerList.setOverflow('auto'); // necessary for FF 2.0/Mac
	}
	this.mon(Ext.getDoc(), {
	    scope : this,
	    mousewheel : this.collapseIf,
	    mousedown : this.collapseIf
	});
	this.restrictHeight();
	this.fireEvent('expand', this);
    },
    // 覆盖父类：点击表格列头部分不会隐藏掉“下拉选项”
    collapseIf : function(e) {
	if (!this.isDestroyed && !e.within(this.wrap) && !e.within(this.list) && !e.within(this.view.view.innerHd) && !e.within(this.view.view.hmenu.el)
		&& !e.within(this.view.view.colMenu.el)) {
	    this.collapse();
	}
    },
    onViewClick : function(doFocus) {
	if (doFocus && doFocus.within) {
	    var e = doFocus;
	    if (this.isDestroyed || e.within(this.view.view.innerHd) || e.within(this.view.view.hmenu.el) || e.within(this.view.view.colMenu.el)) {
		return;
	    }
	}
	var index = this.view.getSelectionModel().lastActive, s = this.store, r = s.getAt(index);
	if (r) {
	    this.onSelect(r, index);
	}
	if (doFocus !== false) {
	    this.el.focus();
	}
    },
    // 覆盖方法，去掉自动滚动滚动条功能。下拉为表格，不要自动滚动。
    select : function(index, scrollIntoView) {
	this.selectedIndex = index;
	this.view.getSelectionModel().selectRow(index);
    },
    // 赋值并添加到历史数据，用于解决表格界面不显示编码问题
    setValueEx : function(val, displayVal) {
	var text = displayVal;
	Ext.form.ComboBox.superclass.setValue.call(this, text);
	this.lastSelectionText = text;
	if (this.hiddenField) {
	    this.hiddenField.value = val;
	}
	this.value = val;
	if (this.pageSize) {
	    var defaultObj = {};
	    defaultObj[this.valueField] = val;
	    defaultObj[this.displayField] = displayVal;
	    if (!this.historicalData.item(val)) {
		this.historicalData.add(val, new this.store.recordType(defaultObj));
	    }
	}
	return this;
    },
    // 重载方法 对分页时存在的历史数据做过滤
    findRecord : function(prop, value) {
	var record;
	if (this.store.getCount() > 0) {
	    this.store.each(function(r) {
		if (r.data[prop] == value) {
		    record = r;
		    return false;
		}
	    });
	}
	if (!record) {
	    if (this.pageSize) {
		this.historicalData.each(function(r2) {
		    if (r2.data[prop] == value) {
			record = r2;
			return false;
		    }
		});
	    }
	}
	return record;
    },
    onDestroy : function() {
	if (this.grid) {
	    Ext.destroy(this.grid);
	}
	OECP.ui.ArchivesSelectCombo.superclass.onDestroy.call(this);
    }
});