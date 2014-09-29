Ext.ns('OECP.portal');

/**
 * portlet小模块选择窗体
 * 
 * @class OECP.ui.FormWindow
 * @extends Ext.Window
 */
OECP.portal.PortletSelectWindow = Ext.extend(Ext.Window, {
    width : 650,
    height : 400,
    title : "选择小模块，拖放到你的桌面上。",
    autoScroll : true,
    closeAction : 'hide',
    plain : true,
    modal : false,
    // frame : false,
    layout : 'border',
    /**
     * 设置已选择的protlet
     * 
     * @param {Array}
     *                formdata
     */
    setSelected : function(portletIds) {
	this.selectedIds = portletIds;
	this.updateSelectState();
    },
    initComponent : function() {
	OECP.portal.PortletSettingWindow.superclass.initComponent.call(this);
	this.initPanels();
	this.loadProtlets();
    },
    // 初始化窗体中的panel
    initPanels : function() {
	if (!this.portletsPanel) {
	    this.portletsPanel = new Ext.Panel({
		border : false,
		header : false,
		plain : true,
		frame : false,
		autoScroll : true,
		width : 200,
		items : [],
		region : 'west'
	    });
	}
	this.add(this.portletsPanel);

	if (!this.reviewPanel) {
	    this.reviewCol = new Ext.ux.PortalColumn({
		columnWidth : 1,
		items : []
	    });
	    this.reviewPanel = new Ext.ux.Portal({
		border : false,
		header : false,
		frame : false,
		margins : '10',
		width : 400,
		items : [ this.reviewCol ],
		region : 'center'
	    });
	    this.reviewPanel.on('validatedrop', function() {
		return false;
	    });
	}
	this.add(this.reviewPanel);
    },
    // 获取所有可用的portlet，并将它们添加到选择窗体
    loadProtlets : function() {
	if (!this.portlets) {
	    this.refreshPortlets();
	}
	this.updateSelectState();
    },
    // 重新获取并刷新protlets
    refreshPortlets : function() {
	Ext.Ajax.request({
	    url : __ctxPath + "/portalCfg/loadAllPortlets.do",
	    scope : this,
	    success : function(d) {
		var json = Ext.util.JSON.decode(d.responseText);
		if (json.success) {
		    this.portlets = json.result;
		    this.addPortletToWindow();
		    this.updateSelectState();
		} else {
		    Ext.MessageBox.alert('加载小模块出错', json.msg);
		}
	    }
	}, this);
    },
    // 将portlet添加到窗体
    addPortletToWindow : function() {
	// this.portletsPanel
	var pletBtns = [];
	if (this.portlets) {
	    for (var i = 0; i < this.portlets.length; i++) {
		var plet = this.portlets[i];
		var pletBtn = new Ext.Panel({
		    xtype : 'panel',
		    frame : false,
		    header : false,
		    border : true,
		    bodyStyle : 'padding:3px;cursor:pointer',
		    html : plet.displayName,
		    plet_idx : i,
		    win : this,
		    selected : false
		});
		pletBtn.on('afterrender', function() {
		    this.getEl().on('click', function(e, t) {
			if (!this.selected) {
			    this.win.doView(this.plet_idx);
			} else {
			    this.win.doView(this.plet_idx);
			}
		    }, this);
		}, pletBtn);
		pletBtn.setSelected = function(state) {
		    // TODO 更改按钮状态
		    this.selected = state;
		    if(state){// 选中的不能再点击
			this.body.setStyle('background','#ddd');
			this.body.setStyle('cursor',null);
		    }else{
			this.body.setStyle('background',null);
			this.body.setStyle('cursor','pointer');
		    }
		};
		plet.selectBtn = pletBtn;
		pletBtns.push(pletBtn);
	    }
	}
	this.portletsPanel.removeAll();
	this.portletsPanel.add(pletBtns);
	this.portletsPanel.doLayout();
    },
    doView : function(plet_idx) {
	// 预览
	// 初始化portlet
	var plet = this.portlets[plet_idx];
	var item = {
	    xtype : plet.xtype,
	    tools : tools,
	    portletId : plet.id,
	    title : plet.displayName
	};
	if (Ext.isString(plet.initParams)) {
	    params = eval('({' + plet.initParams + '})');
	} else {
	    params = plet.initParams;
	}
	Ext.apply(item, params);
	var jsfiles = [];
	if (plet.jsFiles) {
	    jsfiles  = eval(plet.jsFiles);
	}
	ScriptMgr.load({ // 加载以来的js脚本
	    scripts : jsfiles,
	    scope : this,
	    callback : function() {
		this.reviewCol.removeAll();
		this.reviewCol.add(item);
		this.doLayout();
	    }
	});
    },
    updateSelectState : function() {
	if (this.selectedIds && this.portlets) {
	    for (var i = 0; i < this.portlets.length; i++) {
		var selected = this.selectedIds.include(this.portlets[i].id);
		this.portlets[i].selectBtn.setSelected(selected);
	    }
	}
    },
    onDestroy : function() {
	Ext.Window.superclass.onDestroy.call(this);
    }
});