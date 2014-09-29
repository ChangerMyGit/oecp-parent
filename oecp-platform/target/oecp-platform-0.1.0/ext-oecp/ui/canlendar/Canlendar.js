Ext.ensible.cal.Canlendar = Ext.extend(Ext.Panel,{
	   title : "日历",
	   layout:'fit',
	   animate : true,
	   border : false,
	   closable : false,
	   autoScroll : true,
       initComponent : function(){
       	  Ext.ensible.cal.Canlendar.superclass.initComponent.call(this);
       	  this.loadStore();
       } , 
       loadStore : function (){
		    Ext.Ajax.request({
		        url: "/oecp-platform/extensible/src/locale/extensible-lang-zh_CN.js", // 指定中文的资源 
		        disableCaching: false,
		        success: function(resp, opts){
		        	// 获取中文资源 
		            eval(resp.responseText); 
		            Ext.Ajax.request({
		            	url : "/oecp-platform/canlendar/getNotes.do",
		            	method : "post",
		            	scope : this,
		            	success : function(response) {
		    				var res = response.responseText;
		    				eval('var data='+res);
		                    var eventStore = new Ext.ensible.sample.MemoryEventStore({
		                    	data :data
		                    });

		                    var panel = new Ext.ensible.cal.CalendarPanel({
		                        eventStore: eventStore,
		                        activeItem: 1,
		                        editModal: true
		                    });
		                    this.add(panel);
		                    this.doLayout();
		                    
		            	}
		            });
		        },
		        failure: function(){
		            Ext.Msg.alert('Failure', 'Failed to load locale file.');
		        },
		        scope: this 
		    });
       } 
});