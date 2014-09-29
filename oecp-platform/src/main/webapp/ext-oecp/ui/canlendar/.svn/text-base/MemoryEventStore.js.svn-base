/*!
 * Extensible 1.0.2
 * Copyright(c) 2010-2012 Extensible, LLC
 * licensing@ext.ensible.com
 * http://ext.ensible.com
 */
Ext.ns('Ext.ensible.sample');

Ext.ensible.sample.msg = function(title, format){
    if(!this.msgCt){
        this.msgCt = Ext.DomHelper.insertFirst(document.body, {id:'msg-div'}, true);
    }
    this.msgCt.alignTo(document, 't-t');
    var s = String.format.apply(String, Array.prototype.slice.call(arguments, 1));
    var m = Ext.DomHelper.append(this.msgCt, {html:'<div class="msg"><h3>' + title + '</h3><p>' + s + '</p></div>'}, true);

    m.slideIn('t').pause(3).ghost('t', {remove:true});
};

/*!
 * Extensible 1.0.2
 * Copyright(c) 2010-2012 Extensible, LLC
 * licensing@ext.ensible.com
 * http://ext.ensible.com
 */
/*
 * This is a simple in-memory store implementation that is ONLY intended for use with
 * calendar samples running locally in the browser with no external data source. Under
 * normal circumstances, stores that use a MemoryProxy are read-only and intended only
 * for displaying data read from memory. In the case of the calendar, it's still quite
 * useful to be able to deal with in-memory data for sample purposes (as many people 
 * may not have PHP set up to run locally), but by default, updates will not work since the
 * calendar fully expects all CRUD operations to be supported by the store (and in fact
 * will break, for example, if phantom records are not removed properly). This simple
 * class gives us a convenient way of loading and updating calendar event data in memory,
 * but should NOT be used outside of the local samples. 
 * 
 * For a real-world store implementation see the remote sample (remote.js).
 */
Ext.ensible.sample.MemoryEventStore = Ext.extend(Ext.data.Store, {
    // private
    constructor: function(config){
        config = Ext.applyIf(config || {}, {
            storeId: 'eventStore',
            root: 'evts',
            proxy: new Ext.data.MemoryProxy(),
            writer: new Ext.data.DataWriter(),
            fields: Ext.ensible.cal.EventRecord.prototype.fields.getRange(),
            idProperty: Ext.ensible.cal.EventMappings.EventId.mapping || 'id'
        });
        this.reader = new Ext.data.JsonReader(config);
        Ext.ensible.sample.MemoryEventStore.superclass.constructor.call(this, config);
        
        // By default this shared example store will monitor its own CRUD events and 
        // automatically show a page-level message for each event. This is simply a shortcut
        // so that each example doesn't have to provide its own messaging code, but this pattern
        // of handling messages at the store level could easily be implemented in an application
        // (see the source of test-app.js for an example of this). The autoMsg config is provided
        // to turn off this automatic messaging in any case where this store is used but the 
        // default messaging is not desired.
        if(config.autoMsg !== false){
            // Note that while the store provides individual add, update and remove events, those only 
            // signify that records were added to the store, NOT that your changes were actually 
            // persisted correctly in the back end (in remote scenarios). While this isn't an issue
            // with the MemoryProxy since everything is local, it's still harder to work with the 
            // individual CRUD events since they have different APIs and quirks (notably the add and 
            // update events both fire during record creation and it's difficult to differentiate a true
            // update from an update caused by saving the PK into a newly-added record). Because of all
            // this, in general the 'write' event is the best optiosn for generically messaging after 
            // CRUD persistance has actually succeeded.
            this.on('write', this.onWrite, this);
        }
        
        this.initRecs();
    },
    
    // If the store started with preloaded inline data, we have to make sure the records are set up
    // properly as valid "saved" records otherwise they may get "added" on initial edit.
    initRecs: function(){
        this.each(function(rec){
            rec.store = this;
            rec.phantom = false;
        }, this);
    },
    
    // private
    onWrite: function(store, action, data, resp, rec){
        if(Ext.ensible.sample.msg){
            if(Ext.isArray(rec)){
                Ext.each(rec, function(r){
                    this.onWrite.call(this, store, action, data, resp, r);
                }, this);
            }
            else {
            	// 封装参数
                var paramData = {};
                this.buildData(paramData,rec);
                switch(action){
                    case 'create': 
                    	if(!rec.data["Title"]){
                    		Ext.ensible.sample.msg("提示","请输入标题");
                    		//Ext.Msg.show({title:'错误',msg:"请输入标题",buttons:Ext.Msg.OK});
                    		return;
                     	} else {
                        	this.doSubmit("save",paramData,rec,true);
                        	this.load();
                            Ext.ensible.sample.msg('Add', 'Added "' + Ext.value(rec.data[Ext.ensible.cal.EventMappings.Title.name], '(No title)') + '"');
                     	}
                    	
                        break;
                    case 'update':
                    	if(!rec.data["Title"]){
                    		Ext.ensible.sample.msg("提示","请输入标题");
                    		return;
                    	} else {
                        	this.doSubmit("save",paramData,rec);
                            Ext.ensible.sample.msg('Update', 'Updated "' + Ext.value(rec.data[Ext.ensible.cal.EventMappings.Title.name], '(No title)') + '"');
                    	}
                        break;
                    case 'destroy':
                    	this.doSubmit("delete",paramData,rec);
                        Ext.ensible.sample.msg('Delete', 'Deleted "' + Ext.value(rec.data[Ext.ensible.cal.EventMappings.Title.name], '(No title)') + '"');
                        break;
                }
            }
        }
    },

    // private
    onCreateRecords : function(success, rs, data) {
        // Since MemoeryProxy has no "create" implementation, added events
        // get stuck as phantoms without an EventId. The calendar does not support
        // batching transactions and expects valid records to be non-phantoms, so for
        // the purpose of local samples we can hack that into place. In real remote
        // scenarios this is handled either automatically by the store or by your own
        // application CRUD code, and so you should NEVER actually do something like this.
        if(Ext.isArray(rs)){
            Ext.each(rs, function(rec){
                this.onCreateRecords.call(this, success, rec, data);
            }, this);
        }
        else {
            rs.phantom = false;
            rs.data[Ext.ensible.cal.EventMappings.EventId.name] = rs.id;
            rs.commit();
        }
    },
    
    // private 
    buildData : function(paramData,rec){
    	paramData["canlendarNote.title"] = rec.data["Title"];
    	paramData["canlendarNote.notes"] = rec.data["Notes"];
    	paramData["canlendarNote.startDate"] = Ext.util.Format.date(rec.data["StartDate"],'Y-m-d H:i:s');
    	paramData["canlendarNote.endDate"] = Ext.util.Format.date(rec.data["EndDate"],'Y-m-d H:i:s');
    	// 回调函数回写
    	paramData["rec"] = rec;
    },
    
    doSubmit : function(action,paramData,rec,createFlag){
    	 if(!createFlag){
    		 // 保存修改才会复制ID
    		 if(rec.json)
    		    paramData["canlendarNote.id"] = rec.json.pk; // 刷新页面后取值
    		 else
    			paramData["canlendarNote.id"] = rec.pk; // 未刷新页面 取值 
    	 }
    	 Ext.Ajax.request({
    		 url : '/oecp-platform/canlendar/'+ action +'Note.do',
    		 params : paramData,
             success : function(response,options){
                 var json = Ext.decode(response.responseText);
                 // 回写主键 第二次更新可获得最新生成的数据
                 options.params.rec.pk = json.id;
             },
            failure : function(response,options) {
                 // var json = Ext.decode(response.responseText);
                 // Ext.Msg.show({title:'错误',msg:json.msg,buttons:Ext.Msg.OK});
            }
    	 });
    }
});