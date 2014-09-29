// 门户界面
var tools = [ {
    id : 'gear',
    // hidden : true,
    handler : function(e, target, panel) {
	var portal = panel.ownerCt.ownerCt;
	if (panel.ownerCt.ownerCt && panel.ownerCt.ownerCt.getPortletConfig) {
	    var data = panel.ownerCt.ownerCt.getPortletConfig(panel);
	    if (!panel.configWin) {
		panel.configWin = new OECP.portal.PortletSettingWindow();
		panel.configWin.on('dataSaved', function(formdata) {
		    portal.setPortletConfig(panel, formdata);
		}, panel);
		panel.on('destroy', function() {
		    panel.configWin.destroy();
		}, panel);
	    }
	    panel.configWin.show();
	    panel.configWin.setFormData(data);
	}
    }
}, {
    id : 'close',
    handler : function(e, target, panel) {
	var portal = panel.ownerCt.ownerCt;
	panel.ownerCt.remove(panel, true);
	portal.updatePortletWinSelectState();
    }
} ];

Ext.ns("OECP.portal");

OECP.portal.PortalView = Ext.extend(Ext.Panel, {
    id : 'OECP.portal.PortalView',
    title : '我的桌面',
    animate : true,
    border : false,
    closable : false,
    autoScroll : true,
    layout : 'fit',
    initComponent : function() {
	OECP.portal.PortalView.superclass.initComponent.call(this);
	this.portal = new OECP.portal.Portal({
	    manager : this,
	    items : []
	});
	this.add(this.portal);
    }
});

/**
 * @author slx
 * @class PortalView
 * @extends Ext.Panel
 */
OECP.portal.Portal = Ext.extend(Ext.ux.Portal, {
    margins : '35 5 5 0',
    initComponent : function() {
	this.tbar = [ {
	    text : '保存',
	    xtype : 'savebtn',
	    scope : this,
	    handler : function() {
		this.doSave();
	    }
	}, {
	    text : '添加列',
	    xtype : 'addbtn',
	    scope : this,
	    handler : function() {
		this.addCol();
	    }
	}, {
	    text : '添加小模块',
	    xtype : 'rowaddbtn',
	    scope : this,
	    handler : function() {
		this.showPortletsWin();
	    }
	}, {
	    text : '取消',
	    xtype : 'closebtn',
	    scope : this,
	    handler : function() {
		this.doCancel();
	    }
	} ];
	OECP.portal.Portal.superclass.initComponent.call(this);
	this.getTopToolbar().hide();
	this.editMenu = new Ext.menu.Menu({
	    items : [ {
		text : '整理',
		scope : this,
		handler : function(item, e) {
		    this.toEditState();
		}
	    } ]
	});
	this.on('drop', function(e) {
	    this.addPortlet(e.panel);
	}, this);
	// 调试用代码， 实际应该为：
	this.loadMyCofing();
    },
    addPortlet : function(portlet){
	this.portletToEdit(portlet);
	this.updatePortletWinSelectState();
    },
    addContextmenu : function() {
	this.getEl().on('contextmenu', this.showContextmenu, this);
    },
    removeContextmenu : function() {
	this.getEl().un('contextmenu', this.showContextmenu, this);
    },
    showContextmenu : function(e, htmldom) {
	this.editMenu.showAt(e.getXY());
    },
    addCol : function() {
	var col = new Ext.ux.PortalColumn({
	    columnWidth : .0
	});

	var len = this.items.length;
	col.columnWidth = 1 / (len + 1); // 要插入的列的宽度，取总列数做平均
	this.add(col);
	this.avgColWidth();
	this.showColSetPanel(col);
    },
    showPortletsWin : function() {
	if (!this.portletsWin) {
	    this.portletsWin = new OECP.portal.PortletSelectWindow();
	}
	// 获取已选择
	this.portletsWin.show();
	this.updatePortletWinSelectState();
    },
    updatePortletWinSelectState:function(){
	if(this.portletsWin){
	    this.portletsWin.setSelected(this.getAllPortletIds());
	}
    },
    colResize : function(r, w, h, e) {
	var portal = this.ownerCt;
	var w = w / portal.getWidth();
	this.columnWidth = w;
	this.ownerCt.avgColWidth();
	r.el.dom.style.height = '';
    },
    // 整理当前的列调整宽度至全屏
    avgColWidth : function() {
	var w = 0; // 目前的总列宽
	var len = this.items.length;
	for (var i = 0; i < len; i++) {
	    w += this.items.items[i].columnWidth;
	}
	var moreW = 1 - w; // 多出来的列宽
	var avgW = moreW / w; // 多出来的列宽平均分布
	for (var i = 0; i < len; i++) {
	    var iw = avgW * this.items.items[i].columnWidth;
	    this.items.items[i].columnWidth = this.items.items[i].columnWidth + iw;
	}
	this.doLayout();
    },
    // 切换到设置状态
    toEditState : function() {
	this.cascade(this.portletToEdit);// 小窗变为可编辑
	this.getTopToolbar().show(); // 显示保存等按钮
	this.removeContextmenu(); // 移除右键菜单
	// 显示列宽设置和删除列按钮
	this.cascade(this.showColSetPanel, this);
    },
    // 切换到查看状态
    toViewState : function() {
	this.cascade(this.portletToView); // 小窗变为查看状态
	this.addContextmenu(); // 启用右键菜单
	this.getTopToolbar().hide(); // 隐藏保存等按钮
	// 隐藏列宽设置和删除列按钮
	this.cascade(this.hideColSetPanel, this);
	if (this.portletsWin) {
	    this.portletsWin.hide();
	}
    },
    showColSetPanel : function(item) {
	if (item instanceof Ext.ux.PortalColumn) {
	    var col = item;
	    var scope = this;
	    if (!col.setpanel) {
		col.setpanel = new Ext.Panel({
		    draggable : false,
		    frame : false,
		    border : false,
		    tools : [ {
			id : 'close',
			scope : scope,
			handler : function(e, target, panel) {
			    scope.removeCol.call(scope, col);
			}
		    } ]
		});
		col.insert(0, col.setpanel);
		col.doLayout();
	    }
	    col.setpanel.show();
	    col.resizer = new Ext.Resizable(col.getId(), {
		handles : 'e',
		pinned : true
	    });
	    col.resizer.on("resize", this.colResize, col);
	}
    },
    hideColSetPanel : function(item) {
	if (item instanceof Ext.ux.PortalColumn) {
	    var col = item;
	    if (col.setpanel) {
		col.setpanel.hide();
		col.resizer.destroy(false);
	    }
	}
    },
    removeCol : function(col) {
	if (col.items.length > 1) {
	    Ext.MessageBox.confirm('请确认', '此列中存在有小窗体，删除列会将他们一起删除！您确定仍要删除吗？', function(btn) {
		if (btn === 'yes') {
		    col.ownerCt.remove(col);
		    this.avgColWidth();
		    this.updatePortletWinSelectState();
		}
	    }, this);
	} else {
	    col.ownerCt.remove(col);
	    this.avgColWidth();
	    this.updatePortletWinSelectState();
	}
    },
    portletToEdit : function(item) {
	if (item instanceof Ext.ux.Portlet) {
	    item.draggable = true;
	    // 启用拖放
	    if (item.dd) {
		item.dd.unlock();
		item.header.setStyle('cursor', 'move');
	    }
	    // 编辑状态显示 header
	    if (item.header)
		item.header.setStyle('display', null);
//		item.header.show();
	    // 显示设置按钮
	    if (item.getTool('gear')) {
		item.getTool('gear').show();
	    }
	}
    },
    portletToView : function(item) {
	if (item instanceof Ext.ux.Portlet) {
	    // 禁止拖放
	    item.draggable = false;
	    if (item.dd) {
		item.dd.lock();
		item.header.setStyle('cursor', '');
	    }
	    // 去除设置按钮
	    if (item.getTool('gear')) {
		item.getTool('gear').hide();
	    }
	    // 查看状态时，根据设置更新 hideBorder等
	    if (item.hideHeader) {
		if (item.header)
		    item.header.setStyle('display', 'none');
//		item.header.hide();
	    }
	    // 根据设置更新边距
	    item.el.dom.style.width = '';
	    item.body.dom.style.width = '';
	    item.body.dom.style.height = item.height;
	    item.el.dom.style.marginTop = item.marginTop;
	    item.el.dom.style.marginBottom = item.marginBottom;
	    item.el.dom.style.marginLeft = item.marginLeft;
	    item.el.dom.style.marginRight = item.marginRight;
	}
    },
    // 保存设置
    doSave : function() {
	// 保存整体的设置信息
	var datas = this.getProtalSettingDatas();
	Ext.Ajax.request({
	    url : __ctxPath + "/portalCfg/saveMySetting.do",
	    params : datas,
	    scope : this,
	    success : function(d) {
		var json = Ext.util.JSON.decode(d.responseText);
		if (json.success) {
		    this.loadMyCofing();
		    Ext.ux.Toast.msg("信息", json.msg);
		} else {
		    Ext.MessageBox.alert('保存出错', json.msg);
		}
	    }
	}, this);
    },

    doCancel : function() {
	this.removeAll();
	this.add(this.bakitems);
	this.doLayout();
	this.toViewState();
    },
    getAllPortletIds : function() {
	var plets = this.getAllChilds(this, Ext.ux.Portlet);
	var ids = [];
	for (var i = 0; i < plets.length; i++) {
	    ids.push(plets[i].portletId);
	}
	return ids;
    },
    getProtalSettingDatas : function() {
	var datas = {};
	var cols = this.getAllChilds(this, Ext.ux.PortalColumn);
	for (var i = 0; i < cols.length; i++) {
	    var key = 'areas' + '[' + i + ']';
	    var coldata = {};
	    datas[key + '.columnWidth'] = cols[i].columnWidth;
	    var plets = this.getAllChilds(cols[i], Ext.ux.Portlet);
	    for (var ii = 0; ii < plets.length; ii++) {
		var pdata = {}, pkey = key + '.portletConfigs[' + ii + ']';
		var pc = this.getPortletConfig(plets[ii]);
		for ( var ik in pc) {
		    datas[pkey + '.' + ik] = pc[ik];
		}
	    }
	}
	return datas;
    },
    getPortletConfig : function(portlet) {
	var pletdata = {};
	pletdata.height = portlet.height;
	pletdata['portlet.id'] = portlet.portletId;
	pletdata.title = portlet.title;
	pletdata.hideHeader = portlet.hideHeader;
	pletdata.hideBorder = portlet.hideBorder;
	pletdata.marginTop = portlet.marginTop;// portlet.el.dom.style.marginTop
	pletdata.marginBottom = portlet.marginBottom;
	pletdata.marginLeft = portlet.marginLeft;
	pletdata.marginRight = portlet.marginRight;
	return pletdata;
    },
    setPortletConfig : function(portlet, config) {
	portlet.body.setHeight(config.height), portlet.height = config.height;
	portlet.setTitle(config.title), portlet.title = config.title;
	portlet.hideHeader = config.hideHeader;
	portlet.hideBorder = config.hideBorder;
	// portlet.el.dom.style.marginTop
	portlet.el.dom.style.marginTop = portlet.marginTop = config.marginTop;
	portlet.el.dom.style.marginBottom = portlet.marginBottom = config.marginBottom;
	portlet.el.dom.style.marginLeft = portlet.marginLeft = config.marginLeft;
	portlet.el.dom.style.width = '';
	portlet.body.dom.style.width = '';
	portlet.el.dom.style.marginRight = portlet.marginRight = config.marginRight;

	if (portlet.hideBorder) {
	    portlet.border = false;
	    portlet.el.addClass(portlet.baseCls + '-noborder');
	    portlet.body.addClass(portlet.bodyCls + '-noborder');
	} else {
	    portlet.el.removeClass(portlet.baseCls + '-noborder');
	    portlet.body.removeClass(portlet.bodyCls + '-noborder');
	    portlet.border = true;
	}
	this.avgColWidth();
	portlet.el.repaint();
    },
    getAllChilds : function(thisobj, childExtClass, childs) {
	if (thisobj.items) {
	    var is = thisobj.items.items;
	    childs = childs ? childs : [];
	    for (var i = 0; i < is.length; i++) {
		if (is[i] instanceof childExtClass) {
		    childs.push(is[i]);
		}
		this.getAllChilds(is[i], childExtClass, childs);
	    }
	    return childs;
	}
	return [];
    },
    // 加载我的门户设置
    loadMyCofing : function() {
	Ext.Ajax.request({
	    url : __ctxPath + "/portalCfg/loadMySetting.do",
	    scope : this,
	    success : function(d) {
		var json = Ext.util.JSON.decode(d.responseText);
		if (json.success) {
		    this.loadMyPortal(json.result.areas);
		} else {
		    Ext.MessageBox.alert('加载出错', json.msg);
		}
	    }
	}, this);
    },
    // 根据我的门户设置加载我的protal界面
    loadMyPortal : function(config) {
	// 根据设置 创建items
	var items = [];
	var jsfiles = [];
	if (config) {
	    for (var i = 0; i < config.length; i++) {
		var col = {};
		col.columnWidth = config[i].columnWidth;
		if (config[i].portletConfigs) {
		    col.items = [];
		    for (var ii = 0; ii < config[i].portletConfigs.length; ii++) {
			var plet_cfg = config[i].portletConfigs[ii];
			var plet = {
			    tools : tools
			};
			var params;
			if (plet_cfg.portlet.initParams) {
			    if (Ext.isString(plet_cfg.portlet.initParams)) {
				params = eval('({' + plet_cfg.portlet.initParams + '})');
			    } else {
				params = plet_cfg.portlet.initParams;
			    }
			    Ext.apply(plet, params);
			}
			plet.title = plet_cfg.title ? plet_cfg.title : plet_cfg.portlet.displayName;
			plet.xtype = plet_cfg.portlet.xtype;
			plet.height = plet_cfg.height;
			plet.portletId = plet_cfg.portlet.id;
			plet.hideHeader = plet_cfg.hideHeader;
			plet.hideBorder = plet_cfg.hideBorder;
			plet.frame = false;
			if (plet_cfg.hideBorder) {
			    plet.border = false;
			}
			plet.marginTop = plet_cfg.marginTop;// portlet.el.dom.style.marginTop
			plet.marginBottom = plet_cfg.marginBottom;
			plet.marginLeft = plet_cfg.marginLeft;
			plet.marginRight = plet_cfg.marginRight;
			col.items.push(plet);
			if (plet_cfg.portlet.jsFiles) {
			    var jsFiles  = eval(plet_cfg.portlet.jsFiles);
			    if (Ext.isArray(jsFiles)) {
				for (var ji = 0; ji < jsFiles.length; ji++) {
				    jsfiles.push(jsFiles[ji]);
				}
			    } else if (Ext.isString(jsFiles)) {
			    	jsfiles.push(jsFiles);
			    }
			}
		    }
		}
		items.push(col);
	    }
	}

	ScriptMgr.load({
	    scripts : jsfiles,
	    scope : this,
	    callback : function() {
		this.bakitems = OECP.core.util.clone(items);
		this.removeAll();
		this.add(items);
		this.doLayout();
		this.protalLoaded();
	    }
	});
    },
    protalLoaded : function() {
	this.toViewState();
	// this.avgColWidth();
    },

    onDestroy : function() {
	if (this.portletsWin) {
	    this.portletsWin.destroy();
	}
    }
});
