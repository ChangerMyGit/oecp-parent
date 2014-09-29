/**
 * 重载 jsonreader 方法，修正 store内定义的'xxxx.xxx'类型字段取值时，父级属性为空时抛异常
 * 使用try函数尝试获取数据，如果没有则返回空字符串，防止报错影响js调用。
 */
Ext.data.JsonReader.prototype.createAccessor=function(){
        var re=/[\[\.]/;
        return function(expr) {
            if(Ext.isEmpty(expr)){
                return Ext.emptyFn;
            }
            if(Ext.isFunction(expr)){
                return expr;
            }
            var i=String(expr).search(re);
            if(i >= 0){
                return new Function('obj', 'var err=false; try{obj'+(i>0?'.':'')+expr+';}catch(e){err=true;}  if(!err) return obj' + (i > 0 ? '.' : '') + expr+'; else return "";');
            }
            return function(obj){
                return obj[expr];
            };

        };
    }();
//重载form赋值方法，对下拉空间带分页做处理。
Ext.form.BasicForm.prototype.setValues=function(values){
    if(Ext.isArray(values)){ // array of objects
        for(var i = 0, len = values.length; i < len; i++){
            var v = values[i];
            var f = this.findField(v.id);
            if(f){
            	f.setValue(v.value);
                if(this.trackResetOnLoad){
                    f.originalValue = f.getValue();
                }
            }
        }
    }else{ // object hash
        var field;
        for(id in values){
            if(!Ext.isFunction(values[id]) && (field = this.findField(id))){
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
                if(this.trackResetOnLoad){
                    field.originalValue = field.getValue();
                }
            }
        }
    }
    return this;
};

Ext.ns('OECP.report');
OECP.report.ManagePanel=Ext.extend(Ext.Panel, {
	title:'报表设计器',
	id : 'OECP.report.ManagePanel',
	listUrl:'/report/setting/list.do',
	queryUrl:'/report/setting/load.do',
	saveUrl:'/report/setting/save.do',
	delUrl:'/report/setting/del.do',
	layout:'border',
	height:document.body.clientHeight,
	frame:true,
	queryItems:[{xtype:'textfield',fieldLabel:'报表编号',name:'code',operator:'like'}, 
		{xtype:'textfield',fieldLabel:'报表名称',name:'name',operator:'like'}
	],
	//private  获取已经选择的主键
	getCurrentBillPk:function(){
		var me=this,_ids=[];
		me.grid.getSelectionModel().each(function(i) {_ids.push( i.data.id);});
		return _ids;
	},
	listeners:{
		'afterrender':function(me){
			//延迟1秒后查询数据，规避meetingEnum未加载完，造成枚举值不显示
			var task = new Ext.util.DelayedTask(function(){
				this.doQueryList();
			},me);
			task.delay(1000);
		}
	},
	//private 
	showMsg:function(flash,title,msg,fn){
		if(Ext.isEmpty(title,false)) title='信息';
		if(flash)
			Ext.ux.Toast.msg(title,msg);
		else
			Ext.Msg.alert(title,msg,fn,this);
	},
	initComponent:function() {
		var me=this;
		me.initEnumKeyVal();
		me.initGrid();
		me.initEditWin();
		me.initQueryPanel();
		me.initEvent();
		me.items=[me.queryPanel,me.grid];
		OECP.report.ManagePanel.superclass.initComponent.call(this);
	},
	//private 初始化枚举值
	initEnumKeyVal:function(){
		this.meetingEnumStore=new Ext.data.JsonStore({url:__ctxPath + "/enums/ref.do",root:'result',fields:['value','name'],autoLoad:true,baseParams:{className:'oecp.framework.dao.QLType'}});
	},
	//初始化主体界面
	initGrid:function(){
		var me=this;
		me.store=new Ext.data.JsonStore({
			url:__ctxPath+me.listUrl,
			root:'result',
			totalProperty:'totalCounts',
			autoLoad:false,
			fields:['id','code', 'name', 'daobeanname', 'view.title']
		});
		//增加按钮 、编辑按钮
		me.addBtn=new OECP.ui.button.AddButton(),me.editBtn=new OECP.ui.button.EditButton({disabled:true}),me.delBtn=new OECP.ui.button.DelButton({disabled:true});
		me.previewBtn=new OECP.ui.Button({text:'预览报表',pid : 'preview',iconCls:'x-btn-preview-trigger',disabled:true});
		var _sm=new Ext.grid.CheckboxSelectionModel({singleSelect:true});
		var _bbar=new Ext.PagingToolbar({pageSize:25,store:me.store,displayInfo:true,displayMsg:'当前页记录索引{0}-{1}， 共{2}条记录',emptyMsg:'当前没有记录'});
		me.grid=new Ext.grid.GridPanel({
			store: me.store,
			region: 'center',
			tbar:[me.addBtn,me.editBtn,me.delBtn,me.previewBtn],
			bbar:_bbar,
			columns:[_sm,
				{header:'报表编号',width:200,sortable:true,dataIndex:'code'},
				{header:'报表名称',sortable:true,dataIndex:'name'},
				{header:'数据源DAO',sortable:true,dataIndex:'daobeanname'},
				{header:'视图标题',sortable:true,dataIndex:'view.title'}
			],
			sm:_sm,
			width: 600,
			height: 600,
			frame: true
		});
		me.grid.getSelectionModel().on('selectionChange',function(){
			if(me.grid.getSelectionModel().getSelections().length>0){
				me.editBtn.setDisabled(false);
				me.delBtn.setDisabled(false);
				me.previewBtn.setDisabled(false);
			}else{
				me.editBtn.setDisabled(true);
				me.delBtn.setDisabled(true);
				me.previewBtn.setDisabled(true);
			}
		});
	},
	//private 初始化查询
	initQueryPanel:function(){
		var me=this;
		me.queryBtn=new OECP.ui.button.QueryButton();//查询按钮
		var _cfg=[];
		for(var i=0;i<me.queryItems.length;i++){//循环追加查询参数
			var _itemCfg=me.queryItems[i];
			var _item1=Ext.applyIf({name:'conditions['+i+'].value'},_itemCfg);//"值"录入字段
			if(_itemCfg.hiddenName){
				_item1=Ext.applyIf({hiddenName:_item1.name},_item1);
			}
			var _item2={xtype:'hidden',name:'conditions['+i+'].operator',value:_itemCfg.operator?_itemCfg.operator:'='};//"条件" 隐藏域
			var _item3={xtype:'hidden',name:'conditions['+i+'].field',value:_itemCfg.name};//"字段名" 隐藏域
			var _item4={xtype:'hidden',name:'conditions['+i+'].fieldType',value:_itemCfg.fieldType?_itemCfg.fieldType:'java.lang.String'};//"字段类型" 隐藏域
			_cfg.push({layout:'form',items:_item1}),_cfg.push(_item2),_cfg.push(_item3),_cfg.push(_item4);
		}
		_cfg.push({xtype:'label',html:'&nbsp;&nbsp;&nbsp;&nbsp;'}),_cfg.push(me.queryBtn);
		me.queryPanel=new Ext.FormPanel({height:40,frame:true,region:'north',items:[{layout:'column',items:_cfg}]});
	},
	//private 初始化事件
	initEvent:function(){
		var me=this;
		me.grid.on('rowdblclick',function(g,rowIndex,e){
			var _r = g.store.getAt(rowIndex);
			this.doQuery(_r.get('id'),true); 
		},me);
		me.queryBtn.on('click',function(){
			this.doQueryList();
		},me);
		me.addBtn.on('click',function(){
			this.doQuery('',false); 
			me.editWin.form.getForm().findField('report.qltype').setValue('SQL');
		},me);
		me.previewBtn.on('click',function(){
			var _ids=this.getCurrentBillPk();
			if(!Ext.isDefined(_ids) || Ext.isEmpty(_ids)){
				this.showMsg(false,'','请先选择一条记录！');
				return;
			}
			if(_ids.length>1){
				this.showMsg(false,'','勾选一条记录大于一条！');
				return;
			}
			this.doPreview(_ids[0],false); 
		},me);
		me.editBtn.on('click',function(){
			var _ids=this.getCurrentBillPk();
			if(!Ext.isDefined(_ids) || Ext.isEmpty(_ids)){
				this.showMsg(false,'','请先选择一条记录！');
				return;
			}
			if(_ids.length>1){
				this.showMsg(false,'','勾选一条记录大于一条！');
				return;
			}
			this.doQuery(_ids[0],false);	
		},me);
		me.delBtn.on('click',function(){ 
			var _ids=this.getCurrentBillPk();
			if(!Ext.isDefined(_ids) || Ext.isEmpty(_ids)){
				this.showMsg(false,'','请先选择一条记录！');
				return;
			}
			if(_ids.length>1){
				this.showMsg(false,'','勾选一条记录大于一条！');
				return;
			}
			this.doDel(_ids[0]);	
		},me); 
	},
	//private 初始化编辑窗口
	initEditWin:function(){
		var me=this;
		me.editWin=new OECP.report.SettingWin({queryUrl:me.queryUrl,saveUrl:me.saveUrl});
		me.editWin.on('billsaved',function(win){
			this.grid.store.reload();
		},me);
	},
	/**
	 * 单据查询方法
	 * @param {} pk 单据主键
	 * @param {} readonly 只读 (true:只读;false:非只读)
	 */
	doQuery:function(pk,readonly){
			this.editWin.clearData(true);
			this.editWin.setReadOnly(readonly);
			this.editWin.doQuery(pk);
			this.editWin.show();
			this.editWin.setPageIndex(0);
			this.editWin.form.doLayout();
//			this.editWin.form.fixedGrid.setAutoScroll(true);
//			this.editWin.form.commonGrid.setAutoScroll(true); slx
//			this.editWin.form.otherGrid.setAutoScroll(true);
	},
	
	doPreview:function(pk,readonly){
		var rptcode = this.grid.getStore().getById(pk).data['code'];
		if(!this.previewWin){
			this.previewWin = new Ext.Window({height:600,width:1000,autoScroll:true,closeAction:'hide',modal:true});
			this.previewWin.contentEl = Ext.DomHelper.append(document.body, {
		    tag : 'iframe',
		    style : "border 0px none;scrollbar:true",
		    height : "100%",
		    width : "100%"
	   });
		}
		var url = __ctxPath+ '/page/report/report.jsp?reportCode='+rptcode;
		this.previewWin.show();
		this.previewWin.contentEl.contentDocument.body.innerHTML = "<center>正在加载报表界面,请稍候......</center>";
		this.previewWin.contentEl.src = url;
	},
	/**
	 * 快速查询
	 */
	doQueryList:function(){
		var _baseParams=this.queryPanel.getForm().getValues();
		this.grid.store.baseParams=_baseParams;
		this.grid.store.load();
	},
	doDel:function(pk){
		var me = this;
		Ext.Ajax.request({
			url:__ctxPath+me.delUrl,
			params:{'report.id':pk},
			scope:me,
			success: function(response, opts) {
				var obj = Ext.decode(response.responseText);
				if(obj.success){
		      		Ext.ux.Toast.msg("信息",obj.msg);
					this.grid.store.load(); 	
				}else{
		      		 Ext.Msg.alert("错误","删除失败!\n错误信息:"+obj.msg);
				}
			},   
			failure: function(response, opts) {
				Ext.Msg.alert("错误","删除失败!\n服务器端故障状态代码:"+response.status);
		   }
		});
	},
	//销毁
	destroy:function(){
		if(me.editWin){
			Ext.destroy(me.editWin);
		}
		OECP.report.ManagePanel.superclass.destroy.call(this);
	}
});

OECP.report.SettingWin=Ext.extend(Ext.Window,{
	/**
	 * @cfg {String} queryUrl 查询地址
	 */
	/**
	 * @cfg {String} saveUrl 保存地址
	 */
	/***
	 *@cfg {String} billid 单据主键
	 */
	billid:'',
	modal : true,
	closeAction:'hide',
	layout : 'fit',
	width:800,
	height:550,
	/**
	 * 清空\重置表单
	 * @param {} flag true:清空表单;undefined或false 重置表单
	 */
	clearData:function(flag){
		this.form.clearData(flag);
	},
	initComponent:function(){
		this.addEvents('billsaved');
		this.initForm();
		this.initBtns();
		OECP.report.SettingWin.superclass.initComponent.call(this);
	},
	initForm:function(){
		var me=this;
		me.form=new OECP.report.SettingForm({
			url:__ctxPath+me.saveUrl
		});
		me.items=[me.form];
	},
	initBtns:function(){
		var me=this;
		me.saveBtn=new OECP.ui.button.BizSaveButton({
			handler:function(){
				this.form.getForm().submit({
					scope:this,
	                waitMsg: '提交数据...',
	                success: function(form, action){
	                	Ext.ux.Toast.msg("信息","保存成功！");
	                	this.fireEvent('billsaved',this);
	                    this[this.closeAction]();
	                },
	                failure: function(form, action){
	                    if (action.failureType === Ext.form.Action.CONNECT_FAILURE) {
	                        Ext.Msg.alert('Error', 'Status:'+action.response.status+': '+ action.response.statusText);
	                    }
	                    if (action.failureType === Ext.form.Action.SERVER_INVALID){
	                        Ext.Msg.alert('错误', action.result.msg||action.result.errormsg);
	                    }
	               	 }
				,params:me.form.getOutOfFormDatas()});
			},
			scope:this
		});
		me.closeBtn=new OECP.ui.button.BizCloseButton({
			handler:function(){
				me[me.closeAction]();
			},
			scope:this
		});
		me.btnPre= new OECP.ui.Button({text:'<-上一步',handler:function(){me.showPrePanel(me);}});
		me.btnNext= new OECP.ui.Button({text:'下一步->',handler:function(){me.showNextPanel(me);}});
		me.buttons=[me.btnPre,me.saveBtn,me.closeBtn,me.btnNext];
	},
	doQuery:function(pk){
		scope = this;
		if(Ext.isDefined(pk) && !Ext.isEmpty(pk,false)){
			Ext.Ajax.request({
					   	url:__ctxPath+this.queryUrl,
					   	success: function(res){
					   		var msg = eval("("+Ext.util.Format.trim(res.responseText)+")");
					   		if(msg.success){
//						   		Ext.ux.Toast.msg("信息", msg.msg);
						   		scope.form.getForm().setValues(scope.pressData(msg.result,['fixedconditions','commonconditions','otherconditions','view']));
						   		// 给查询条件表格赋值
						   		if(msg.result.queryscheme){
						   			if(msg.result.queryscheme.fixedconditions)
							   			scope.form.fixedGrid.setValues(msg.result.queryscheme.fixedconditions);
						   			if(msg.result.queryscheme.commonconditions)
										scope.form.commonGrid.setValues(msg.result.queryscheme.commonconditions);
						   			if(msg.result.queryscheme.otherconditions)
										scope.form.otherGrid.setValues(msg.result.queryscheme.otherconditions);
						   		}else{
						   			scope.form.fixedGrid.setValues([]);
									scope.form.commonGrid.setValues([]);
									scope.form.otherGrid.setValues([]);
						   		}
						   		// 给视图配置界面赋值
						   		if(msg.result.view){
									scope.form.viewPanel.doLoad(msg.result.view);
						   		}else{
									scope.form.viewPanel.doLoad();
						   		}
					   		}else{
					   			Ext.Msg.alert({title:"错误",msg:msg.msg});
					   		}
					   	},
					   	failure: function(){Ext.Msg.alert({title:"错误",msg:msg.result.msg});},
					   	params: { 'report.id':pk}
					});
//			this.form.load({url:__ctxPath+this.queryUrl,params:{'report.id':pk}}); 
			this.form.doLayout();
		}else{
			scope.form.viewPanel.doLoad();
		}
	},
	pressData : function(data, except, parentKey) {// 打平json对象
				except = except || [];
				var _newdata = {};
				for (_key in data) { // 遍历所有key
					if (isNotExcept(_key)) { // 如果不是例外的key
						_value = data[_key];
						if (_value && _value instanceof Object) { // 如果是json对象
							var _rs_newdata = this.pressData(_value, except, _key);
							for (_newkey in _rs_newdata) {
								_newdata[(parentKey ? parentKey + "." : "") + _newkey] = _rs_newdata[_newkey];
							}
						} else {
							_newdata[(parentKey ? parentKey + "." : "") + _key] = _value;
						}
					}
				}
				function isNotExcept(_key) {
					for (ei in except) {
						if (_key == except[ei]) {
							return false;
						}
					}
					return true;
				}
				return _newdata;
			},
	setReadOnly:function(flag){
		this.form.setReadOnly(flag);
	},
	pageindex : 0,
	showPrePanel : function(scope){
		this.setPageIndex(this.pageindex-1);
	},
	showNextPanel : function(scope){
		this.setPageIndex(this.pageindex+1);
	},
	setPageIndex : function(i){
		if(i<=0){
			this.pageindex = 0;
			this.btnPre.setDisabled(true);
			this.btnNext.setDisabled(false);
		}else if(i>=3){
			this.pageindex = 3;
			this.btnNext.setDisabled(true);
			this.btnPre.setDisabled(false);
		}else{
			this.pageindex = i;
			this.btnPre.setDisabled(false);
			this.btnNext.setDisabled(false);
		}
		this.form.getLayout().setActiveItem(this.pageindex);
		if(this.pageindex == 3){// 视图设计界面显示后重新布局一下，以免视图预览显示不正常。
			this.form.viewPanel.doLayout();
		}
	}
});

//编辑界面
OECP.report.SettingForm=Ext.extend(Ext.FormPanel,{
	frame:true,
	layout : 'card',
	autoScroll	: true,
	layoutConfig: {
		deferredRender : false
	},
	/**
	 * 清空\重置表单
	 * @param {} flag true:清空表单;undefined或false 重置表单
	 */
	clearData:function(flag){
		// 清除Grid数据
		if(flag){
			if (!Ext.isEmpty(this.getForm()) && this.getForm().getEl() && this.getForm().getEl().dom){ 
				this.getForm().getEl().dom.reset();
				this.fixedGrid.setValues([]);
				this.commonGrid.setValues([]);
				this.otherGrid.setValues([]);
				// 清除视图配置数据
				this.viewPanel.treePanel.root.removeAll();
				this.viewPanel.updateBuilderPanel();
			}
		}else{
			this.getForm().reset();
		}
		this.doLayout();
	},
	getOutOfFormDatas : function(){
		// 拼接参数返回
		var gridDatas = {},_gv={};
		_gv = this.fixedGrid.getValues();
		Ext.apply(gridDatas,_gv);
		_gv = this.commonGrid.getValues();
		Ext.apply(gridDatas,_gv);
		_gv = this.otherGrid.getValues();
		Ext.apply(gridDatas,_gv);
		// 拼接视图设置数据
		_gv = this.viewPanel.getViewDom();
		Ext.apply(gridDatas,_gv);
		return gridDatas;
	},
	initComponent:function(){
		var me = this;
		OECP.report.SettingForm.superclass.initComponent.call(this);
		this.initSQLPanel();
		this.initScriptPanel();
		this.initQueryPanel();
		this.initViewPanel();
	},
	/**
	 * 初始化查询语句设置界面
	 */
	initSQLPanel : function(){
		this.sqlPanel = new Ext.Panel({
							layout : 'border'
						});
		var topPanel = new Ext.Panel({
			region : 'north',
			layout : 'table',
			height : 30,
			layoutConfig : {col: 5},
			items:[
			{xtype:'textfield',dataIndex:'id',fieldLabel : "报表id",name:'report.id',hidden:true},
			{layout:'form',labelWidth:60,items:[{xtype:'textfield',dataIndex:'code',fieldLabel : "报表编号",name:'report.code'}]},
			{layout:'form',labelWidth:60,items:[{width:200,xtype:'textfield',dataIndex:'name',fieldLabel : "报表名称",name:'report.name'}]},
			{layout:'form',width:158,labelWidth:60,items:[{width:90,xtype:'combo',dataIndex:'daobeanname',fieldLabel : "查询DAO",name:'report.daobeanname',
					store:{xtype:'jsonstore',root:'result',url:__ctxPath+'/report/setting/daonames.do',fields:['name'],autoLoad:true},
				triggerAction:'all',mode:'remote',valueField:'name',displayField:'name',hiddenName:'report.daobeanname'}]},
			{layout:'form',width:130,labelWidth:60,items:[{width:60,xtype:'enumscombo',className:'oecp.framework.dao.QLType',value:'SQL',dataIndex:'qltype',fieldLabel : "QL类型",name:'report.qltype'}]}
			]
		});
		var sqltext = new Ext.form.TextArea({
							dataIndex : 'qlstr',
							fieldLabel : '查询语句',
							name : 'report.qlstr'
						});
		var sqltextpanel = new Ext.Panel({title:'报表查询SQL',layout:'fit',region:'center'});
		this.add(this.sqlPanel);
		sqltextpanel.add(sqltext);
		this.sqlPanel.add(topPanel);
		this.sqlPanel.add(sqltextpanel);
	},
	/**
	 * 初始化"加工脚本"设置界面
	 */
	initScriptPanel : function(){
		this.scriptPanel = new Ext.Panel({
							layout : 'column'
						});
		var qls_tip = "可在脚本中对查询语句和查询条件对象继续操作，比如追加查询条件，动态修改查询语句等。脚本使用groovy语言，语法与java代码很类似，具体请自行查找资料学习。</br>"
			+"可用变量:</br>"
			+"&nbsp;&nbsp;ql : 查询语句。</br>"
			+"&nbsp;&nbsp;qo : 查询条件对象。</br>"
			+"&nbsp;&nbsp;funcCode : 当前功能编号。</br>"
			+"&nbsp;&nbsp;user : 当前登录人。</br>"
			+"&nbsp;&nbsp;org : 当前组织。</br>"
			+"&nbsp;&nbsp;dao : 当前报表所用的dao对象。</br>"
			+"</br>"
			+"代码示例：</br>"
			+" // 如果需要使用到某个java类，需要import。</br>"
			+" import java.util.Date;</br>"
			+" // 如果当前公司编号为0000，则追加查询条件o.orgname like '%集团'</br>"
			+" if(org.code=='0000'){</br>"
			+" &nbsp;&nbsp;ql = ql + \" AND o.orgname like '%集团' \";</br>"
			+" }</br>"
			+" // 追加参数式查询条件</br>"
			+" qo.addCondition(\"o.user_pk\", \"=\", user.id);</br>"
			+" // <b>注意：如果脚本中修改了查询语句，此处一定要记得return！！修改qo的操作不需要返回。</b></br>"
			+" return ql;</br>"
			;
		
		var leftPanel = new Ext.Panel({
			columnWidth:.5,
			items:[
			{layout:'form',labelWidth:160,items:[{
				width:200,xtype:'checkbox',inputName:'report.doqlscript',inputValue:true ,defaultValue :false,dataIndex:'doqlscript',fieldLabel : "查询前处理查询条件",name:'report.doqlscript',mapping : "doqlscript"
				}]
				,listeners:{
				afterrender:function(){
					new Ext.ToolTip({
				        target: this.id,
				        html: qls_tip,
				        title: '“查询前处理查询条件”脚本说明',
				        autoHide: false,
				        showDelay : 1000,
				        width : 500,
				        closable: true,
				        draggable:true
				    });
				}
				}},
			{width:'100%',height:'95%',xtype:'textarea',dataIndex:'qlscript',name:'report.qlscript',mapping : "qlscript"}
			]
		});
		var rss_tip = "可在脚本中对查询结果rrs进行加工，比如用脚本对查询语句查询出的结果进行计算，得到一个新的结果，作为图型显示的数据。脚本使用groovy语言，语法与java代码很类似，具体请自行查找资料学习。</br>"
			+"可用变量:</br>"
			+"&nbsp;&nbsp;ql : 查询语句。</br>"
			+"&nbsp;&nbsp;params : 执行查询时用到的参数数组。</br>"
			+"&nbsp;&nbsp;funcCode : 当前功能编号。</br>"
			+"&nbsp;&nbsp;user : 当前登录人。</br>"
			+"&nbsp;&nbsp;org : 当前组织。</br>"
			+"&nbsp;&nbsp;dao : 当前报表所用的dao对象。</br>"
			+"&nbsp;&nbsp;rrs : 报表查询后的到的结果对象。</br>"
			+"</br>"
			+"常用类说明：</br>"
			+"&nbsp;&nbsp;oecp.platform.rpt.ReportResultSet：保存整个查询结果的类，rrs即使这个类的实例。rrs中有两个组要的属性，grids和charts分别保存表格数据和图形数据。" +
					"为实现多表格、多图形的需要，这两个属性的类型为HashMap,以 key- List<SimpleDataVO>的方式保存结果。默认情况下grids和charts中都只有一个结果集List，名称分别为grid1和chart1(<b>注意：此名称对应报表视图中Gird和Chart的结果集名称属性</b>)。"
					 +"如需实现多表格或多图形，可能需要多个结果集(当然也可以将视图上的多个表格或者对象指向同一个结果集)，请使用脚本对数据进行加工或利用dao查询出新的结果集，放入到rrs对应的位置，同时配置视图的结果集名称属性与结果集对应。"
					 +"另外，如只是需要改变数据集的名称 请使用 gridRename或者chartRename方法来更改名称。如：rrs.gridRename('grid1','newgridname');</br>"
			+"&nbsp;&nbsp;oecp.framework.vo.base.SimpleDataVO：查询结果每一行数据都保存在此类的一个实例中。</br>"
			+"</br>"
			+"Groovy精品文章：http://www.ibm.com/developerworks/cn/education/java/j-groovy/index.html</br>"
			;
			
		var rightPanel = new Ext.Panel({
			columnWidth:.5,
			items:[
			{layout:'form',labelWidth:160,items:[{width:200,xtype:'checkbox',inputName:'report.dorsscript',inputValue:true ,defaultValue :false,dataIndex:'dorsscript',fieldLabel : "查询后整理查询结果",name:'report.dorsscript',mapping : "report.dorsscript"}]
			,listeners:{
				afterrender:function(){
					new Ext.ToolTip({
				        target: this.id,
				        html: rss_tip,
				        title: '“查询后整理查询结果”脚本说明',
				        autoHide: false,
				        showDelay : 1000,
				        width : 500,
				        closable: true,
				        draggable:true
				    });
				}
				}},
			{width:'100%',height:'95%',xtype:'textarea',dataIndex:'rsscript',name:'report.rsscript'}
			]
		});
		this.scriptPanel.add(leftPanel);
		this.scriptPanel.add(rightPanel);
		this.add(this.scriptPanel);
//		this.scriptPanel.on('afterrender',function(){
//			new Ext.ToolTip({
//		        target: this.id,
//		        html: 'Click the X to close me',
//		        title: 'My Tip Title',
//		        autoHide: false,
//		        closable: true,
//		        draggable:true
//		    });
//		});
	},
	/**
	 * 初始化可用查询设置界面
	 */
	initQueryPanel : function(){
		this.queryPanel = new Ext.Panel({layouy:'border'});
		this.queryTopPanel = new Ext.Panel({
							title : '查询方案设置',
							layout : 'column',
							region : 'north',
							items : [
								{xtype:'textfield',dataIndex:'queryscheme.id',fieldLabel : "查询方案id",name:'report.queryscheme.id',hidden:true},
								{layout:'form',columnWidth:.5,labelWidth:60,items:[{xtype:'textfield',dataIndex:'queryscheme.code',fieldLabel : "方案编号",name:'report.queryscheme.code'}]},
								{layout:'form',columnWidth:.5,labelWidth:60,items:[{width:200,xtype:'textfield',dataIndex:'queryscheme.name',fieldLabel : "方案名称",name:'report.queryscheme.name'}]}
							]
						});
		this.queryPanel.add(this.queryTopPanel);
		this.gridTabPanel = new Ext.TabPanel({
							activeTab : 0,
//							autoScroll : true,
							region : 'center',
							layoutOnTabChange : true,
							defaults : {
								autoScroll :true
							}
						});
		this.queryPanel.add(this.gridTabPanel);
		this.fixedGrid = this.createConditionGrid('固定隐藏条件','fixedconditions');
		this.commonGrid = this.createConditionGrid('常用条件','commonconditions');
		this.otherGrid = this.createConditionGrid('其他可用条件','otherconditions');
		this.gridTabPanel.add(this.fixedGrid);
		this.gridTabPanel.add(this.commonGrid);
		this.gridTabPanel.add(this.otherGrid);
		
		this.add(this.queryPanel);
	},
	createConditionGrid : function(title,prefix,datas){
		var jstore = new Ext.data.JsonStore({
							storeId : 'id',
							fields : ['id','field', 'dispname', 'operators', 'defaultvalue', 'fieldType', 'required','editorcfg'],
							data : datas || []
						});
		var sm = new Ext.grid.CheckboxSelectionModel();
		var grid = new Ext.grid.EditorGridPanel({
							tbar : [],
							title : title,
							store : jstore,
							height:360,
							prefix : prefix,
							getValues : function(){
								var _v = {};
								var grid_store = this.getStore();
								grid_store.each(function(record,i) {
											var values = record.data;
											for (_gkey in values) {
												// 操作符多选处理
												if(_gkey == "operators"){
													var opr_v = values[_gkey];
													if(!Ext.isEmpty(opr_v)){
														var arr_orp_v = opr_v.split(','),j=0;
														for(;j<arr_orp_v.length;j++){
															_v['report.queryscheme.'+this.prefix+"["+i+"].operators[" +j +"]"] = arr_orp_v[j];
														}
													}
												}else{
													_v['report.queryscheme.'+this.prefix+"["+i+"]." +_gkey] = values[_gkey];
												}
											}
										},this);
								return _v;
							},
							setValues : function(datas){
								datas = datas||[];
								var i=0;
								for(;i<datas.length;i++){
									if(datas[i].operators){// 将json中的operators数组变为lovcombo识别的字符串
										datas[i].operators = datas[i].operators.join(',');
									}
								}
								this.getStore().loadData(datas);
							},
							sm : sm,
							columns : [sm, {
										header : "字段名",
										dataIndex : "field",
										editor : {xtype:'textfield',allowBlank : false}
									}, {
										header : "显示名称",
										dataIndex : "dispname",
										editor : {xtype:'textfield',allowBlank : false}
									}, {
										header : "可用操作符",
										dataIndex : "operators",
										renderer : function(value, metadata, record, rowIndex, colIndex, store) {
											if(value){
												var editor = this.editor;
												var vals = value.split(',');
												for(vi in vals){
													rec = editor.findRecord(editor.valueField, vals[vi]);
													if(rec){
														vals[vi] = rec.get(editor.displayField);
													}else{
														vals[vi] = null;
													}
												}
												return vals.join(',');
											}else{
												return '';
											}
										},
										editor : {
											width : 60,
											xtype : prefix=='otherconditions'?'lovcombo':'combo',// 除其他可选条件外，固定条件和常用条件，都只允许选一个操作符
											valueField : 'value',
											displayField : 'name',
											emptyText : '请选择',
											mode : 'local',
											editable : false,
											triggerAction : 'all',
											store : {
												xtype : 'jsonstore',
												url : __ctxPath + "/enums/ref.do",
												root : 'result',
												fields : ['value', 'name'],
												autoLoad : true,
												baseParams : {
													className : 'oecp.platform.query.setting.enums.Operator'
												}
											}

										}
									}, {
										header : "默认值",
										dataIndex : "defaultvalue",
										editor : {xtype:'textfield'}
									}, {
										header : "字段类型",
										dataIndex : "fieldType",
										editor :
										{xtype:'textfield',allowBlank : false}
									}, {
										header : "是否必填条件",
										dataIndex : "required",
										editable:prefix=='fixedconditions'?false:true,
										processEvent : function(name, e, grid, rowIndex, colIndex) {
											if(prefix=='fixedconditions'){
												return Ext.grid.ActionColumn.superclass.processEvent.apply(this, arguments);
											} else {
												if (name == 'mousedown') {
													var record = grid.store.getAt(rowIndex);
													record.set(this.dataIndex, !record.data[this.dataIndex]);
													return false;
												} else {
													return Ext.grid.ActionColumn.superclass.processEvent.apply(this, arguments);
												}
											}
										},
										xtype : 'checkcolumn'
									},{
										header : "编辑控件设置",
										dataIndex : "editorcfg",
										editor :
										{xtype:'textfield'}
									}]
						});
						
		var btn_addrow = new Ext.Button({text : '新增',iconCls : 'btn-select',xtype:'oecpbutton',disabled:false,
		handler:function(){
			var _record = new grid.store.recordType({required:prefix=='fixedconditions'?true:false,fieldType:'java.lang.String'});
			var row = grid.getStore().getCount();
			grid.stopEditing();
			grid.getStore().insert(row, _record);
			grid.startEditing(0, 0);
		}});						
		var btn_delrow = new Ext.Button({text : '删除',iconCls : 'btn-clear',xtype:'oecpbutton',disabled:true,
		handler:function(){
			var rows = grid.getSelectionModel().getSelections();
			grid.getStore().remove(rows);
		}});						
		grid.getTopToolbar().addButton([btn_addrow,btn_delrow]);
		grid.getSelectionModel().on('selectionChange',function(){
			if(grid.getSelectionModel().getSelections().length>0){
				btn_delrow.setDisabled(false);
			}else{
				btn_delrow.setDisabled(true);
			}
		});
		
		return grid;
	},
	/**
	 * 初始化报表视图设置界面
	 */
	initViewPanel : function(){
		this.viewPanel = new OECP.report.view.ReportViewDesignPanel();
		this.add(this.viewPanel);
	},
	//检测控件是否合法
	checkValid:function(){
		var me=this;
		var _err=true;
		if(me.items){
			me.items.each(function(o){
				if(!o.isValid(true)){
					_err=false;
				};
			});
		}
		return _err;
	},
	//设置界面元素是否可编辑
	setReadOnly:function(readonly){
		var foreachFn=function(_items){//遍历设置控件只读属性
			if(_items instanceof Ext.form.Field){
				if(!Ext.isDefined(_items['defaultReadOnly']))
					_items.setReadOnly(readonly);
				return;
			}
			if(_items instanceof Ext.util.MixedCollection){
				for(var i=0;i<_items.length;i++)
					foreachFn(_items.get(i));
			}
			if(_items.items){
				foreachFn(_items.items);
			}
		};
		foreachFn(this.items);
	}
});
