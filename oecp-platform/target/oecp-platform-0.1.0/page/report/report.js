Ext.ns('OECP.report');
/**
 * 报表视图
 * @class OECP.report.ReportView
 * @extends Ext.Panel
 */
OECP.report.ReportView=Ext.extend(Ext.Panel,{
	/**
	 * @cfg {String} reportCode 报表编号  
	 */
	height:document.body.clientHeight,
	//	width:document.body.clientWidth,
	layout:'fit',
	initComponent:function(){
		this.queryPanel=new Ext.FormPanel({//查询模板
			region:'north',layout:'border',frame:true,items:[
					{xtype:'panel',region:'center'},
					{xtype:'panel',region:'east',height:25,width:50,items:[
						{xtype:'button',text:'查询',width:40,handler:this.doQuickQuery.createDelegate(this)}]
					}
			]
		});
		this.initBtns();//初始化按钮
		this.loadQueryCfg();//初始化复杂查询框
		this.loadUI();//初始化报表视图
		OECP.report.ReportView.superclass.initComponent.call(this);
	},
	//private 初始化按钮
	initBtns:function(){
		var me=this;
		this.queryBtn=new OECP.ui.button.QueryButton();//查询按钮
		this.queryBtn.setText('高级查询');
		this.printBtn=new OECP.ui.button.PrintButton({
			handler:function(){
				me.doPrint.call(me);
			}
		});
		this.toggleBtn=new Ext.Button({text:'隐藏常用查询',height:15,width:15,icon:'../../images/btn/up.gif'
		,handler:function(){
				if(me.queryPanel.hidden){
					me.queryPanel.show();
					me.toggleBtn.setIcon('../../images/btn/up.gif');
					me.toggleBtn.setText("隐藏常用查询");
					me.doLayout();
				}else{
					me.queryPanel.hide();
					me.toggleBtn.setIcon('../../images/btn/last.gif');
					me.toggleBtn.setText("显示常用查询");
					me.doLayout();
				}
			}
		});//查询按钮
		this.tbar=[this.queryBtn,this.printBtn,this.toggleBtn];
	},
	//private 打印
	doPrint:function(){
		var me=this,json=this.jsonresult;
		if(Ext.isDefined(json) && !Ext.isEmpty(json)){
			var _grids=me.grids,_varnames=[],_vartotal={},_jsondata={};
			for(var i=0;i<_grids.length;i++){
				var __n=_grids[i].dataRoot;
				_varnames.push(__n);//获取表格标识名称，用于获取json数据
				_vartotal[__n]=json.result.grids[__n].totalrecord;//获取表格行数
				_jsondata[__n]=json.result.grids[__n].datas;//拼装json结果集
			}
			var _printTemplate = OECP.ui.PrintTempletManage.getTemplate(_grids,_varnames,_vartotal);//拼装模板字符串
			LODOP=getLodop(document.getElementById('LODOP_OB'),document.getElementById('LODOP_EM'));
			var _template =new Ext.XTemplate(_printTemplate);//初始化模板
			var _printTemplate=_template.applyTemplate(_jsondata);//拼装数据
			eval(_printTemplate);//执行追加数据
			var _heights=[];_charsX=[];
			for(var i=0;i<this.grids.length;i++){//计算表格实际高度
				var obj=this.grids[i];
				_heights.push((obj.store.getCount()+1)*23+5 +obj.getPosition()[1]);//行数 每行23px高度 5px间隔
			}
			function sortNumber(a, b){//降序排序比较
				return b-a;
			}
			_heights.sort(sortNumber);//数值排序
			var lastXY=[],lastX=0,lastY=0;
			for(var i=0;i<me.chars.length;i++){//追加图片
				var obj=me.chars[i],_h=0,_w=0,_xy=obj.getPosition();
				if(Ext.isEmpty(lastXY)){//判断是第一次循环图表,初始化坐标
					lastY=_heights[0]+10;
					lastX=0;
				}else{
					if(_xy[1]===lastXY[1]) lastX=lastX+me.chars[i].width;//判断x坐标与上个图表x坐标相同
					if(_xy[0]===lastXY[0]) lastY=lastY+me.chars[i].height;//判断y坐标与上个图表y坐标相同
				}
				LODOP.ADD_PRINT_IMAGE(lastY,lastX,"100%","100%","data:image/png;base64,"+me.chars[i].getPNGImage());
				lastXY=_xy;
			}
//			LODOP.PRINT_DESIGN(); //打印维护   XXX重复点击后有重复追加数据的问题。原因不详
			LODOP.PREVIEW();//打印预览
		}
	},
	//private 初始化报表视图
	loadUI:function(){
		Ext.Ajax.request({
			scope:this,
		   	url: __ctxPath + "/report/loadui.do",
		   	success: function(res){
		   		var me=this,msg=eval("("+res.responseText+")");
		   		if(msg.success && msg.result.items && Ext.isArray(msg.result.items) &&  msg.result.items.length>0){
		   			this.add(new Ext.Panel({layout:'border',items:[this.queryPanel,{xtype:'panel',region:'center',items:msg.result}]}));
		   			this.setHeight(msg.result.height);
		   			this.doLayout();
		   			this.initPrivateItems();
		   		}
		   	},
		   	failure: function(response, opts){
		   		Ext.Msg.alert({title:"错误",msg:'server-side failure with status code ' + response.status})
		   	},
		   	params:{ 'rptcode':this.reportCode}
		});
	},
	//private 远程调用，获取查询框配置参数
	loadQueryCfg:function(){
		var me=this;
		//加载复杂查询窗口
		Ext.Ajax.request({
			scope:this,
			url: __ctxPath+'/report/getQueryScheme.do',
			params:{rptcode:this.reportCode},
			success: function(response, opts){
				var obj=Ext.decode(response.responseText);
				if(this.initQueryWin(obj.result)){//初始化查询框成功后给查询加按钮加事件
					this.queryBtn.on('click',function(){
						me.queryWindow.show();
					},this);
				}
			},
			failure: function(response, opts){
				Ext.ux.Toast.msg('错误','server-side failure with status code ' + response.status);
			}
		});
		//加载常用查询条件
		Ext.Ajax.request({
			url:__ctxPath+'/report/getQueryCommon.do',
			params:{rptcode:this.reportCode},
			success:function(response, opts){
				var json=Ext.decode(response.responseText);
				if(json.success && !Ext.isEmpty(json.result)){
					var _items=[],conditions=json.result['conditions'],operators=json.result['operator'],num=0;
					_items[0]=[];
					for(var i=0;i<conditions.length;i++){
						var cfg=conditions[i],operator=operators[cfg.operators[0]].operator,_defaultVal=conditions[i].defaultvalue||'';
						if(i>0 && i%4==0) {//每4个控件换一行
							num++,_items[num]=[];
						}
						var _tmpCfg={xtype:'textfield',fieldLabel:cfg.dispname,name:'conditions['+i+'].value',value:_defaultVal};//"查询值"录入框
						if(cfg.required) _tmpCfg['allowBlank']=false;
						if(cfg.editorcfg && !Ext.isEmpty(cfg.editorcfg,false)){//判断其他参数非空后，转换为对象并合并参数。
							_tmpCfg=Ext.apply(_tmpCfg,Ext.decode(cfg.editorcfg));
						}
						_items[num].push(new Ext.Panel({
							width:250,frame:true,layout:'form',baseCls:'',	items:[
								Ext.apply({labelWidth:100,width:120},_tmpCfg),
								{xtype:'textfield',name:'conditions['+i+'].field',value:cfg.field,hidden:true},
								{xtype:'textfield',name:'conditions['+i+'].operator',value:operator,hidden:true}
							]
						}));
					}
					for(var i=0;i<_items.length;i++){
						_items[i]=new Ext.Panel({layout:'column',items:_items[i]});
					}
					if(Ext.isEmpty(_items)){
						me.queryPanel.setVisible(false);//无查询条件就隐藏
					}else{
						me.queryPanel.items.get(0).add(_items);//追加查询条件
						me.queryPanel.setHeight((num+1)*35);//一行31px。
					}
					me.queryPanel.doLayout();	
				}
			}
		});
	},
	//private 初始化查询框
	initQueryWin:function(config){
		if(!config) return false;
		var _fieldData=[],_persOperator={},_conditions=config.conditions,_fieldDefaultValue={},refs={};
		var fieldsAllowBlank={};
		for(var i=0;i<_conditions.length;i++){//初始查询框参数
			var _fieldname=_conditions[i].field,_dispname=_conditions[i].dispname;
			var _fieldType=Ext.isEmpty(_conditions[i].fieldType)?'java.lang.String':_conditions[i].fieldType;
			var _operators=_conditions[i].operators,refCfg={};
			_fieldData.push([_fieldname,_dispname,_fieldType]);//字段名
			if(!Ext.isEmpty(_operators)){//有专有条件符
				_persOperator[_fieldname]=[];
				for(var j=0;j<_operators.length;j++)//通过枚举值获取条件符
					_persOperator[_fieldname].push(config.operator[_operators[j]].operator);
			}
			if(_conditions[i].defaultvalue) _fieldDefaultValue[_fieldname]=_conditions[i].defaultvalue;//拼装默认值
			if(_conditions[i].editorcfg) {
				var _eidtcfg = Ext.decode(_conditions[i].editorcfg);
				_eidtcfg.allowBlank = !_conditions[i].required;
				refCfg=Ext.create(_eidtcfg);
			}else{
				refCfg={xtype:'textfield',allowBlank:!_conditions[i].required};
			}
			refs[_fieldname]=new Ext.grid.GridEditor({field:refCfg});
			fieldsAllowBlank[_fieldname]=!_conditions[i].required;
		}
		this.queryWindow=new OECP.ui.QueryWindow({conditionKey:'conditions',fieldData :_fieldData,persOperator: _persOperator,fieldDefaultValue:_fieldDefaultValue,refs:refs,fieldsAllowBlank:fieldsAllowBlank});
		this.queryWindow.on('beforequery',function(query){
				for (var i=0; i<query.queryStore.getCount(); i++) {
					var _r=query.queryStore.getAt(i),_cs=query.columnsStore;
					var _field=_r.get(_cs[0][0]),_value=_r.get(_cs[2][0]);
					if(!query.fieldsAllowBlank[_field] && Ext.isEmpty(_value,false)){
						query.queryGrid.startEditing(i,3);
						return false;
					}
				}
		});
		this.queryWindow.on('afterquery', function(query){//点击“查询”后事件
			if (typeof query.conditionResult != 'undefined'){
				this.doQuery(query.conditionResult);
			}
		},this);
		return true;
	},
	//private 读取表格和图表
	initPrivateItems:function(){
		var me=this;
		this.grids=[],this.chars=[];
		function eachMyItem(_item){
			if(_item instanceof Ext.util.MixedCollection){
				_item.each(function(obj){
					if(Ext.isDefined(obj.getXType) && Ext.isFunction(obj.getXType) && !Ext.isEmpty(obj.getXType())){
						if(obj instanceof Ext.grid.GridPanel && obj.dataRoot) me.grids.push(obj);
						if(obj.getXType()==="oecpchart") me.chars.push(obj);
					}
					if(obj.items){
						eachMyItem(obj.items);
					}
				},this);
			}
		}
		eachMyItem(this.items);
	},
	//private 快速查询
	doQuickQuery:function(){
		if(this.queryPanel.getForm().isValid ()) this.doQuery(this.queryPanel.getForm().getValues());
	},
	//private 查询
	doQuery:function(params){
		Ext.Msg.wait('正在查询，请稍候......', '提示');
		var me = this;
		Ext.Ajax.request({
			scope:this,
			params:Ext.apply({"rptcode":this.reportCode},params),
			url: __ctxPath+'/report/query.do',
			success: function(response, opts) {
				Ext.Msg.hide(); 
				this.setValues(Ext.decode(response.responseText));
			},
			failure: function(response, opts) {
				Ext.Msg.hide(); 
				console.log('server-side failure with status code ' + response.status);
			}
		})
	},
	//private 界面赋值
	setValues:function(json){
		var me=this;
		if(json.success){
			me.jsonresult=json;
			var _result = json.result,_grids=me.grids,_chars=me.chars;
			var _gresult=_result['grids'], _cresult=_result['charts'];
			if(_gresult){
				for(var _varname in _gresult){
					for(var i=0;i<_grids.length;i++){
						if(_varname===_grids[i].dataRoot){
							_grids[i].store.loadData(_gresult[_varname].datas);
							break;
						}
					}
				}
			}
			if(_cresult){
				for(var _varname in _cresult){
					for(var i=0;i<_chars.length;i++){
						if(_varname===_chars[i].dataRoot){
							_chars[i].setValues(_cresult[_varname]);
						}
					}
				}
			}
		}else{
			me.jsonresult=null;
		}
	}
}); 

