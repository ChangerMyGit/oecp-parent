Ext.onReady(function() {
			var initView =function (config){
				if(!config){
					return;
				}
				var _fieldData=[],_persOperator={},_conditions=config.conditions,_fieldDefaultValue={};
				for(var i=0;i<_conditions.length;i++){
					var _fieldname=_conditions[i].field,_dispname=_conditions[i].dispname,_fieldType=Ext.isEmpty(_conditions[i].fieldType)?'java.lang.String':_conditions[i].fieldType;
					var _operators=_conditions[i].operators;
					_fieldData.push([_fieldname,_dispname,_fieldType]);//字段名
					if(!Ext.isEmpty(_operators)){//自定义条件副
						_persOperator[_fieldname]=[]
						for(var j=0;j<_operators.length;j++){
							_persOperator[_fieldname].push(config.operator[_operators[j]].operator);
						}
					}
					if(_conditions[i].defaultvalue) _fieldDefaultValue[_fieldname]=_conditions[i].defaultvalue;
				}

				var queryWindow = new OECP.ui.QueryWindow({
								fieldData :_fieldData,// [['name', '名称','java.lang.String'], ['code', '编码','java.lang.String'],['type','产品类型','java.lang.String']],
								persOperator: _persOperator,//{name:['=','like'],code:['<','>']},
								fieldDefaultValue:_fieldDefaultValue// {name : '哈密瓜',code : 'a'},
//								defaultCondition : {corp:'1001'}
//								refs : {
//									code : new Ext.grid.GridEditor(new Ext.form.ComboBox(
//											{
//												store : new Ext.data.SimpleStore({
//															fields : ['value', 'text'],
//															data : [['a', '早饭'],
//																	['b', '午饭'],
//																	['c', '晚饭']]
//														}),
//												valueField : 'value',
//												displayField : 'text',
//												mode : 'local',
//												editable : false,
//												triggerAction : 'all'
//											}))
//								}
							});
					queryWindow.on('afterquery', function(query) {
								if (typeof query.conditionResult != 'undefined') {
									alert(Ext.encode(query.conditionResult));
									return false;
								}
							});
					var btn = new Ext.Button({
								text : '查询',
								renderTo : Ext.getBody(),
								handler : function() {
									queryWindow.show();
								}
							});
			}
			Ext.Ajax.request({
				url: 'http://localhost:8080/oecp-platform/app/query/getQueryScheme.do',
				params:{code:'slxtest001'},
				success: function(response, opts) {
					var obj = Ext.decode(response.responseText);
					initView(obj.result);
				},
				failure: function(response, opts) {
					console.log('server-side failure with status code ' + response.status);
				}
			});
		
		});