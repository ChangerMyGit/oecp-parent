var store = new Ext.data.JsonStore({
    url: '/org/orgInfo.do',
//    root: 'images',
    fields: [
        'id','code', 'name','lock'
    ]
});
store.load({params:{id:'org01'}});

var tpl = new Ext.XTemplate(
    '<tpl for=".">',
        '<p>1111{id}</p>',
        '<p>1111{code}</p>',
        '<p>33333{name}</p>',
        '<p>44444{lock}</p>',
    '</tpl>'
);

var panel = new Ext.Panel({
    id:'images-view',
    
    frame:true,
    width:535,
    autoHeight:true,
    collapsible:true,
    layout:'fit',
    title:'Simple DataView',

    items: new Ext.DataView({
        store: store,
        tpl: tpl,
        autoHeight:true,
        multiSelect: true,
        overClass:'x-view-over',
        itemSelector:'div.thumb-wrap',
        emptyText: 'No images to display'
    })
});

panel.render(document.body);