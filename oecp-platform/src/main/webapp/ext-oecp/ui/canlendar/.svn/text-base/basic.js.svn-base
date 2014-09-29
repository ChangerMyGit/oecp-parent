/*!
 * Extensible 1.0.2
 * Copyright(c) 2010-2012 Extensible, LLC
 * licensing@ext.ensible.com
 * http://ext.ensible.com
 */
Ext.onReady(function(){
    
    Ext.Ajax.request({
        url: "/oecp-platform/extensible/src/locale/extensible-lang-zh_CN.js", // 指定中文的资源 
        disableCaching: false,
        success: function(resp, opts){
        	// 获取中文资源 
            eval(resp.responseText); 
            Ext.Ajax.request({
            	url : "/oecp-platform/canlendar/getNotes.do",
            	method : "post",
            	success : function(response) {
    				var res = response.responseText;
    				eval('var data='+res);
                    var eventStore = new Ext.ensible.sample.MemoryEventStore({
                        data: Ext.ensible.sample.EventData
                    	//data :data
                    });

                    new Ext.ensible.cal.CalendarPanel({
                        eventStore: eventStore,
                        renderTo: 'simple',
                        title: '日历',
                        width: 1000,
                        height: 500
                    });
            	}
            });
        },
        failure: function(){
            Ext.Msg.alert('Failure', 'Failed to load locale file.');
        },
        scope: this 
    });
});