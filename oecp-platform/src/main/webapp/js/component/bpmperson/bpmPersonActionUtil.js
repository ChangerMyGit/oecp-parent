BpmPersonActionUtil = Ext.extend(Ext.util.Observable, {
	//异步获取数据
	ajaxGetData : function(url) {
		var conn = Ext.lib.Ajax.getConnectionObject().conn;
		conn.open("post", url, false);
		conn.send(null);
		// alert(conn.responseText)
		if (conn.status == "200") {
			var responseText = conn.responseText;
			responseText = clearReturn(responseText);
			var data = eval("(" + responseText + ")");
			return data;
		} else {
			return 'no result';
		}
	},
	//通过ajax
	ajaxGetDataExt : function(url,param,successFn,failureFn) {
		//var conn = Ext.lib.Ajax.getConnectionObject().conn;
		Ext.Ajax.request({
			url:url,
			params:param,
			success:successFn,
			failure:failureFn
		});
	},
	// 显示进度条   
	showMessageBox : function(msg){
        Ext.MessageBox.show({   
            msg : msg+'中，请稍后...',   
            width : 300,   
            wait : true,   
            progress : true,   
            closable : true,   
            waitConfig : {   
                interval : 200  
            },   
            icon : Ext.Msg.INFO   
        });
	},
	//执行某个任务
    executeTask : function(param,win,store) {
		var url =  __ctxPath + "/bpm/person/task/completeTask.do";
		this.showMessageBox('执行某个任务');
		var data = this.ajaxGetDataExt(url,param
			,function(){
				Ext.Msg.alert('提示','执行成功',function(){
					win.close();
					store.removeAll();
					store.load({
						
					});
				});
			}
			,function(flag){
				Ext.Msg.alert('提示',flag.responseText,function(){});
			}
		);
		
		return data;
	},
	//取回某个任务
    backTask : function(param,store) {
		var url =  __ctxPath + "/bpm/person/task/withdraw.do";
		this.showMessageBox('取回某个任务');
		var data = this.ajaxGetDataExt(url,param
			,function(){
				Ext.Msg.alert('提示','取回成功',function(){
					store.removeAll();
					store.load();
				});
			}
			,function(flag){
				Ext.Msg.alert('提示',flag.responseText,function(){});
			}
		);
		
		return data;
	},
	//给节点委派人员
    reassignVirProActivity : function(param,win) {
		var url =  __ctxPath + "/bpm/person/task/reassignVirProActivity.do";
		this.showMessageBox('给节点委派人员');
		var data = this.ajaxGetDataExt(url,param
			,function(){
				Ext.Msg.alert('提示','委派成功',function(){
					win.close();
				});
			}
			,function(flag){
				Ext.Msg.alert('提示',flag.responseText,function(){});
			}
		);
		
		return data;
	}
});