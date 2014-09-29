Ext.ns('OECP.ui');
/**
 * 平台使用的界面按钮类
 * @author slx
 * @class OECP.ui.Button
 * @extends Ext.Button
 */
OECP.ui.Button = Ext.extend(Ext.Button,{
	
	
	
	initComponent : function(){
		OECP.ui.Button.superclass.initComponent.call(this);
	}
});
Ext.reg('oecpbutton', OECP.ui.Button);

/**
 * tbar上设置:plugins:new Ext.ux.ToolbarKeyMap()
 * 按钮或者menuitem上设置keyBinding:{key:'s',ctrl:true},showHotKey:true,showHotKeyOnText:false,handler:function(){}等属性。
**/
Ext.ux.ToolbarKeyMap = Ext.extend(Object, (function() {
    var kb,
        owner,
        mappings;

    function addKeyBinding(c) {
        if (kb = c.keyBinding) {
            delete c.keyBinding;
            if (c.handler) {
                kb.fn = function(k, e) {
                    e.preventDefault();
                    e.stopEvent();
                    if(!c.hidden && !c.disabled){
                    	c.handler.call(c.scope, c, e);
                    }
                };
            }else if (c.onClick) {
                kb.fn = function(k, e) {
                    e.preventDefault();
                    e.stopEvent();
                    if(!c.hidden && !c.disabled){
	                    e.button=0;
	                    e.type='click';
	                    c.onClick.call(c, e);
                	}
                };
            }
            mappings.push(kb);
            var t = [];
            if (kb.ctrl) t.push('Ctrl');
            if (kb.alt) t.push('Alt');
            if (kb.shift) t.push('Shift');
            t.push(kb.key.toUpperCase());
            c.hotKey = t.join('+');
            if (c instanceof Ext.menu.Item) {
                c.onRender = c.onRender.createSequence(addMenuItemHotKey);
            } else if ((c instanceof Ext.Button) && (c.showHotKey)) {
                c.onRender = c.onRender.createSequence(addButtonHotKey);
            }
        }
        if ((c instanceof Ext.Button) && c.menu) {
            c.menu.cascade(addKeyBinding);
        }
    }

    function findKeyNavs() {
        delete this.onRender;
        if (owner = this.ownerCt) {
            mappings = [];
            this.cascade(addKeyBinding);
            if (!owner.menuKeyMap) {
                owner.menuKeyMap = new Ext.KeyMap(owner.el, mappings);
                owner.el.dom.tabIndex = 0;
            } else {
                 //owner.menuKeyMap.addBinding(mappings);
            }
        }
    }

    function addMenuItemHotKey() {
        delete this.onRender;
        this.el.setStyle({
            overflow: 'hidden',
            zoom: 1
        });
        this.el.child('.x-menu-item-text').setStyle({
            'float': 'left'
        });
        this.el.createChild({
            style: {
                padding: '0px 0px 0px 15px',
                float: 'right'
            },
            html: this.hotKey
        });
    }

    function addButtonHotKey() {
        delete this.onRender;
        this.tooltipType = 'title';
        this.setTooltip(this.tooltip?this.tooltip+'\n快捷键('+this.hotKey+')':'快捷键('+this.hotKey+')');
        if(this.showHotKeyOnText){
	        var p = this.btnEl.up('');
	        p.setStyle({
	            overflow: 'hidden',
	            zoom: 1
	        });
	        p.up('td').setStyle('text-align', 'left');
	        this.btnEl.setStyle('.x-menu-item-text').setStyle({
	            'float': 'left'
	        });
	        p = p.createChild({
	                style: {
	                padding: '0px 0px 0px 15px',
	                float: 'right',
	                position: 'relative',
	                bottom: Ext.isWebKit ? '-1px' : '-2px'
	            },
	            html: this.hotKey
	        });
		}
    }

    return {
        init: function(toolbar) {
            toolbar.onRender = toolbar.onRender.createSequence(findKeyNavs);
            toolbar.doLayout = toolbar.doLayout.createSequence(findKeyNavs);
        }
    };
})());