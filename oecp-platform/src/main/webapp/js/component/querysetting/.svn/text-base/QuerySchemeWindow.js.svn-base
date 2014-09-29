
/**
 * 查询方案设置窗体
 */
Ext.ns('OECP.query');
OECP.query.QuerySchemeWindow = Ext.extend(Ext.Window,{
	title : '查询方案设置',
	modal : true,
	closeAction:'hide',
	layout : 'fit',
	width:800,
	height:550,
	initComponent:function(){
		this.addEvents('billsaved');
		this.initForm();
		this.initBtns();
		OECP.query.QuerySchemeWindow.superclass.initComponent.call(this);
		this.fbar.plugins = new Ext.ux.ToolbarKeyMap();
		this.fbar.plugins.init(this.fbar);
	},
	
	initForm:function(){
		this.form = new Ext.FormPanel({
			frame:true,
			layout : 'border',
			autoScroll	: true,
			url: __ctxPath + '/query/setting/save.do',
			setValues : function(values){
			    if(Ext.isArray(values)){ // array of objects
			        for(var i = 0, len = values.length; i < len; i++){
			            var v = values[i];
			            var f = this.form.findField(v.id);
			            if(f){
			            	f.setValue(v.value);
			                if(this.form.trackResetOnLoad){
			                    f.originalValue = f.getValue();
			                }
			            }
			        }
			    }else{ // object hash
			        var field;
			        for(id in values){
			            if(!Ext.isFunction(values[id]) && (field = this.form.findField(id))){
			            	////////////////add begin//////////////////
			            	if(field instanceof Ext.form.ComboBox && field.pageSize>0 && !Ext.isEmpty(values[id])){
			            		if(field.findRecord(field.valueField, values[id])===true){
			            			var _data ={},_dsf=field.displayField;
				            		_data[field.valueField]=values[id];
				            		if(id.indexOf('.')!=-1){
				            			_dsf=id.substring(0,(id.lastIndexOf(".")+1))+field.displayField;
				            		}
									_data[field.displayField]=values[_dsf]?values[_dsf]:values[id];
				            		var _r =new field.store.recordType(_data);
				            		field.store.add(_r);
			            		}
			            	}
			            	/////////////////add end///////////////////////
			                field.setValue(values[id]);
			                if(this.form.trackResetOnLoad){
			                    field.originalValue = field.getValue();
			                }
			            }
			        }
			    }
			    return this;
			}
		});
		this.items=[this.form];
		
		this.queryTopPanel = new Ext.Panel({
			layout : 'column',
			region : 'north',
			height: 40,
			items : [
				{xtype:'textfield',dataIndex:'id',fieldLabel : "查询方案id",name:'qs.id',hidden:true},
				{xtype:'textfield',dataIndex:'funcId',fieldLabel : "功能id",name:'funcId',hidden:true},
				{layout:'form',columnWidth:.5,labelWidth:60,items:[{xtype:'textfield',dataIndex:'code',fieldLabel : "方案编号",name:'qs.code'}]},
				{layout:'form',columnWidth:.5,labelWidth:60,items:[{width:200,xtype:'textfield',dataIndex:'name',fieldLabel : "方案名称",name:'qs.name'}]}
			]
		});
		this.form.add(this.queryTopPanel);
		if(!this.gridTabPanel){
			this.gridTabPanel = new OECP.query.QueryConditionSettingPanel(
			{
				region : 'center',
				activeTab : 0,
				layoutOnTabChange : true,
				defaults : {
					autoScroll : true
				},
				cfgs : [ {title : '固定隐藏条件',prefix : 'fixedconditions',readonly : false}, 
				             {title : '常用条件',prefix : 'commonconditions',readonly : false}, 
				             {title : '其他可用条件',prefix : 'otherconditions',readonly : false} ]
			});
			this.form.add(this.gridTabPanel);
		}
	},
	
	initBtns:function(){
		var me=this;
		me.saveBtn=new OECP.ui.button.BizSaveButton({
			handler:function(){
				this.form.getForm().submit({
					scope:me,
	                waitMsg: '提交数据...',
	                success: function(form, action){
	                	Ext.ux.Toast.msg("信息","保存成功！");
	                	me.fireEvent('billsaved',form);
	                    me[me.closeAction]();
	                },
	                failure: function(form, action){
	                    if (action.failureType === Ext.form.Action.CONNECT_FAILURE) {
	                        Ext.Msg.alert('Error', 'Status:'+action.response.status+': '+ action.response.statusText);
	                    }
	                    if (action.failureType === Ext.form.Action.SERVER_INVALID){
	                        Ext.Msg.alert('错误', action.result.msg||action.result.errormsg);
	                    }
	               	 }
				,params:me.getFormDatas()});
			},
			scope:this
		});
		me.closeBtn=new OECP.ui.button.BizCloseButton({
			handler:function(){
				me[me.closeAction]();
			},
			scope:this
		});
		me.buttons=[me.saveBtn,me.closeBtn];
	},
	setValues : function(data){
		this.form.setValues({id:null,funcId:null,code:null,name:null});
		if(data){
			this.form.setValues(data);
			this.gridTabPanel.setConditions(data);
   		}else{
   			this.gridTabPanel.setConditions({});
   		}
	},
	getFormDatas:function(){
		return this.gridTabPanel.getConditions();
	}
});