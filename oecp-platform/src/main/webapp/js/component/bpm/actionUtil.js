ActionUtil = Ext.extend(Ext.util.Observable, {
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
	//启动流程
    startProcessDefinition : function(proDefId,deployId) {
		var url =  __ctxPath + "/bpm/def/start.do";
		this.showMessageBox('启动流程');
		var data = this.ajaxGetDataExt(url,{proDefId:proDefId,deployId:deployId}
			,function(){
				Ext.Msg.alert('提示','启动成功',function(){});
			}
			,function(flag){
				Ext.Msg.alert('提示',flag.responseText,function(){});
			}
		);
		
		return data;
	},
	//删除流程
    deleteProcessDefinition : function(param,store) {
		var url =  __ctxPath + "/bpm/def/delete.do";
		this.showMessageBox('删除流程');
		var data = this.ajaxGetDataExt(url,{param:param,id:id}
			,function(){
				Ext.Msg.alert('提示','删除成功',function(){
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
	//停止流程实例
    stopProcessInstance : function(param,store) {
		var url =  __ctxPath + "/bpm/instance/end.do";
		this.showMessageBox('停止流程实例');
		var data = this.ajaxGetDataExt(url,{param:param}
			,function(){
				Ext.Msg.alert('提示','停止成功',function(){
				
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
	//执行某个任务
    executeTask : function(param,win2,store,win) {
		var url =  __ctxPath + "/bpm/task/completeTask.do";
		this.showMessageBox('执行某个任务');
		var data = this.ajaxGetDataExt(url,param
			,function(){
				Ext.Msg.alert('提示','执行成功',function(){
					win2.close();
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
	//执行某个任务
    executeTask : function(param,win,store) {
		var url =  __ctxPath + "/bpm/task/completeTask.do";
		this.showMessageBox('执行某个任务');
		var data = this.ajaxGetDataExt(url,param
			,function(){
				Ext.Msg.alert('提示','执行成功',function(){
					win.hide();
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
	//重新指派某个任务
    reasignTask : function(taskId,store,win,userName) {
		var url =  __ctxPath + "/bpm/task/reassignPerson.do";
		this.showMessageBox('重新指派某个任务');
		var data = this.ajaxGetDataExt(url,{taskId:taskId,userName:userName}
			,function(){
				Ext.Msg.alert('提示','重新指派成功',function(){
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
	//增加虚拟流程
    addVirProcessDefinition : function(param,win,store) {
		var url =  __ctxPath + "/bpm/def/addVirProcessDefinition.do";
		this.showMessageBox('分配流程');
		var data = this.ajaxGetDataExt(url,param
			,function(){
				Ext.Msg.alert('提示','分配成功',function(){
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
	//删除虚拟流程
    deleteVirtualProcessDefinition : function(removeIds,store) {
		var url =  __ctxPath + "/bpm/def/deleteVirProcessDefinition.do";
		this.showMessageBox('删除虚拟流程');
		var data = this.ajaxGetDataExt(url,{virProcessDefIds:removeIds}
			,function(){
				Ext.Msg.alert('提示','删除成功',function(){
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
	//给节点分配人员
    assignVirProActivity : function(param,win) {
		var url =  __ctxPath + "/bpm/def/assignVirProActivity.do";
		this.showMessageBox('保存');
		var data = this.ajaxGetDataExt(url,param
			,function(){
				Ext.Msg.alert('提示','保存成功',function(){
//					Ext.getCmp("VirProcessDefView").reloadDetail();
					win.close();
				});
			}
			,function(flag){
				Ext.Msg.alert('提示',flag.responseText,function(){});
			}
		);
		
		return data;
	},
	//给节点委派人员
    reassignVirProActivity : function(param,win,store) {
		var url =  __ctxPath + "/bpm/task/reassignVirProActivity.do";
		this.showMessageBox('委派');
		var data = this.ajaxGetDataExt(url,param
			,function(){
				Ext.Msg.alert('提示','委派成功',function(){
					win.close();
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
	//给节点配置条件信息
    assignDecisionCondition : function(param,win) {
		var url =  __ctxPath + "/bpm/def/assignDecisionCondition.do";
		this.showMessageBox('保存');
		var data = this.ajaxGetDataExt(url,param
			,function(){
				Ext.Msg.alert('提示','保存成功',function(){
//					Ext.getCmp("VirProcessDefView").reloadDetail();
					win.close();
				});
			}
			,function(flag){
				Ext.Msg.alert('提示',flag.responseText,function(){});
			}
		);
		
		return data;
	},
	//启动虚拟流程
    startVirProcessDefinition : function(virProDefinitionId) {
		var url =  __ctxPath + "/bpm/def/startVirProcessDefinition.do";
		this.showMessageBox('启动流程');
		var data = this.ajaxGetDataExt(url,{virProDefinitionId:virProDefinitionId}
			,function(){
				Ext.Msg.alert('提示','启动成功',function(){});
			}
			,function(flag){
				Ext.Msg.alert('提示',flag.responseText,function(){});
			}
		);
		
		return data;
	},
	//退回某个任务
    rejectTask : function(param,win2,store,win) {
		var url =  __ctxPath + "/bpm/task/reject.do";
		this.showMessageBox('退回某个任务');
		var data = this.ajaxGetDataExt(url,param
			,function(){
				Ext.Msg.alert('提示','退回成功',function(){
					win2.close();
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
	//退回某个任务
    rejectTask : function(param,win,store) {
		var url =  __ctxPath + "/bpm/task/reject.do";
		this.showMessageBox('退回某个任务');
		var data = this.ajaxGetDataExt(url,param
			,function(){
				Ext.Msg.alert('提示','退回成功',function(){
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
		var url =  __ctxPath + "/bpm/task/withdraw.do";
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
	//启用流程
    useProcessDefinition : function(param,store,msg) {
		var url =  __ctxPath + "/bpm/def/use.do";
		this.showMessageBox(msg);
		var data = this.ajaxGetDataExt(url,param
			,function(){
				Ext.Msg.alert('提示',msg+'成功',function(){
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
	}
});