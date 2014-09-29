Ext.onReady(function() {
	//延迟加载，规避IE浏览器由于界面内未加载完Lodop控件造成获取报错。
	var task = new Ext.util.DelayedTask(function(){
    	//表格线css
		var TABLE_STYLE = "<style>table {border-collapse:collapse;}td,th{border-left:1px solid #000000;border-top:1px solid #000000;border-right:1px solid #000000;border-bottom:1px solid #000000;}</style>";
		var ADD_PRINT_TABLE = "LODOP.ADD_PRINT_TABLE";
	
		LODOP = getLodop(document.getElementById('LODOP2'), document	.getElementById('LODOP_EM2'));
		LODOP.SET_SHOW_MODE("DESIGN_IN_BROWSE", 1);//嵌入浏览器直接显示
		LODOP.SET_SHOW_MODE("SETUP_ENABLESS","11111111111110");//屏蔽关闭按钮
		LODOP.PRINT_DESIGN();
		
		// 模板列表
		var templateCombo = new Ext.form.ComboBox({
			width : 120,
			store : {
				autoLoad : false,
				url : __ctxPath + '/printTemplate/list.do',
				xtype : 'jsonstore',
				root : 'result',
				fields : ['id', 'name', 'vtemplate']
			},
			valueField : 'id',
			displayField : 'name',
			mode : 'local',
			triggerAction : 'all',
			editable : false,
			listeners:{
				'select':function(combo ,record , index ){
					var _template = record.get('vtemplate');
					LODOP.SET_PRINT_STYLEA('All','Deleted',true);
					//去除初始化方法；否则多次加载会报错
					if(_template.indexOf('LODOP.PRINT_INIT')!=-1){
						_template = _template.substring(_template.indexOf('");')+3);
					}
					eval(_template);
				}
			}
		});
		var functionTree = new OECP.core.FunComboBox({//平台功能节点树
			minListWidth : 200,emptyText:'',
			onTriggerClick : function() {if (this.menu) {this.menu.show(this.el, "tl-bl?");}}//重载,去除展开整棵树
		});
		functionTree.tree.on("click",function(node){
			templateCombo.store.removeAll();
			paramsGrid.store.removeAll();
			LODOP.SET_PRINT_STYLEA('All','Deleted',true);//清空预览界面
			if(node.leaf){
				var _id = node.id.substring(4);
				templateCombo.store.load({params:{id:_id}});
				Ext.Ajax.request({
					url:__ctxPath+'/printTemplate/getEntityParams.do',
					params:{id:_id},
					success: function(response, opts) {
						var obj = Ext.decode(response.responseText);
			      		if(obj.success){
			      			if(tabs.items.length>1){//删除第一个元素（panel）以外的所有标签
				      			for(var i=tabs.items.length;i>1;i--){
				      				tabs.remove(tabs.get(i-1));
				      			}
			      			}
			      			var _children = obj.result['children_bill'];
			      			var _bill =obj.result['main_bill'];
			      			paramsGrid.store.loadData({success:true,result:_bill});
			      			if(_children.length>0){
			      				for(var i=0;i<_children.length;i++){
			      					tabs.add({
										title : '子表'+(i+1),
										closable : false,
										items : [{
											xtype : 'grid',
											width : 180,
											height : 600,
											islist:_children[i].islist,
											objname:_children[i].objname,
											columns : [{dataIndex : 'dispname',header : '显示名',width : 90}, {dataIndex : 'attrname',header : '属性名',width : 90	}],
											store : {
												autoLoad : false,
												xtype : 'jsonstore',
												fields : ['dispname', 'attrname'],
												data:_children[i].params
											},
											listeners:{
												'rowdblclick':function(grid ,  rowIndex ,  e ){
													if(grid.islist){//判断子表是一对多关系
														var _records = grid.store.data,_cwidth = 90;
														var _tmp1 = '<thead><tr>',_tmp2 = '<tpl for="' + grid.objname + '"><tr>';
														var _t =TABLE_STYLE+ '<table width=600>';
														for (var i = 0; i < _records.length; i++) {
															var _record = _records.get(i);
															_tmp1 = _tmp1.concat("<th width=" + _cwidth + ">"+_record.get('dispname')+"</th>");
															var _cname = _record.get('attrname');
															if(_cname.indexOf(".") != -1){
																_cname = "[values."+_cname+"]"
															}
															_tmp2 = _tmp2.concat("<td width=" + _cwidth + ">{"+_cname+"}</td>");
														}
														_tmp1 = _tmp1.concat('</tr></thead>'),	_tmp2 = _tmp2.concat('</tr></tpl>');
														_t = _t.concat(_tmp1).concat(_tmp2).concat('</table>');
														LODOP.ADD_PRINT_TABLE(10,10,600,400,_t);
													}else{//一对一关系
														var dname = grid.store.getAt(rowIndex).get('dispname');
														var aname = grid.store.getAt(rowIndex).get('attrname');
														if(aname.indexOf(".") != -1){
															aname = "[values."+aname+"]";//模板内使用values.xxx.xx时要加中括号 才能以javascript方式调用
														}
														LODOP.ADD_PRINT_TEXT(10,10,100,20,dname);
														LODOP.ADD_PRINT_TEXT(10,120,100,20,"{"+aname+"}");
													}
												}
											}
										}]
									})
			      				}
			      			}
			      		}else{
			      			alert("加载失败！");
			      		}
			  		 },
					failure: function(response, opts) {
						console.log('server-side failure with status code ' + response.status);
					}
				});
			}else{
				templateCombo.setValue('');
				functionTree.setValue('');
	  			if(tabs.items.length>1){
	      			for(var i=tabs.items.length;i>1;i--){
	      				tabs.remove(tabs.get(i-1));
	      			}
	  			}
				Ext.ux.Toast.msg("信息", "请选择末级节点");
			}
		});
		var saveTemplate = function(){
			var argv = arguments.length,params={},saveAs=false,_tc=templateCombo;
			if(argv>0 && arguments[0]){
				params['template.name']=arguments[0],saveAs = true;
			}else{
				if(Ext.isEmpty(_tc.getValue(),false)){
					var _name = prompt("模板名称：","打印模板");
					if(Ext.isEmpty(_name,false)){return;}
					_name =  _name.replace(/(^\s*)|(\s*$)/g, "");
					if(Ext.isEmpty(_name,false)){
						alert("模板名不能为空！");
						return;
					}
					params['template.name'] =_name; 
				}else{
					params['template.name'] = _tc.findRecord(_tc.valueField,_tc.getValue()).get('name');
				}
				params['template.id'] = _tc.getValue();
			}
			var funId = functionTree.getValue();
			funId = !Ext.isEmpty(funId)?funId.substring(4):'';
			if(Ext.isEmpty(funId)){
				alert("请先选择功能节点！");
				return ;
			}
			params['template.function.id']=funId;
			var _template = LODOP.GET_VALUE("ProgramCodes",0);
			params['template.vtemplate'] = LODOP.GET_VALUE("ProgramCodes",0);
			Ext.Ajax.request({
				url:__ctxPath+'/printTemplate/save.do',
				params:params,
				success: function(response, opts) {
			      var json = Ext.decode(response.responseText);
			      if(json.success){
			      	Ext.ux.Toast.msg("信息", "保存成功");
			      	json.result.id='fun_'+json.result.id;//追加前缀，保持数据统一
			      	var r = new _tc.store.recordType(json.result);
			      	if(!saveAs){
			      		_tc.store.removeAt(_tc.selectedIndex);
			      	}
			      	_tc.store.add(r);
			      	_tc.setValue(r.get("id"));
			      }else{
			      	Ext.ux.Toast.msg("错误", "保存失败");
			      }
			   },
			   failure: function(response, opts) {
			   	  Ext.Msg.alert("错误","保存失败，错误内容：\n"+response.responseText);
			   }
			});
		}
		// 系统按钮
		var buttonpanel = new Ext.Toolbar({
			renderTo : 'header',
			height : 30,
			items : [{
					text:'常用操作',
					menu : new Ext.menu.Menu({
						items:[{
							text:'手工修改模板',
							handler:function(){
								var _win = new Ext.Window({
									layout:'fit',
									title:'手工修改模板',
									height:400,
									width:550,
									items:[{
										xtype:'textarea',
										height:380,
										width:530,
										value:LODOP.GET_VALUE("ProgramCodes",0)
									}],
									bbar:new Ext.Toolbar({
								        items: ['->','-',{
								            text: '插入更新',
								            handler:function(){
								            	var _template = _win.items.get(0).getValue();
								            	if(_template.indexOf('LODOP.PRINT_INIT')!=-1){
														_template = _template.substring(_template.indexOf('");')+3);
												}
												LODOP.SET_PRINT_STYLEA('All','Deleted',true);//清空预览界面
								            	eval(_template);
								            	_win[_win.closeAction]();
								            }
								        },'-',{
								            text: '取消',
								            handler:function(){
								            	_win[_win.closeAction]();
								            }
								        }]
								    })
								});
								_win.show();
							}
						},{
							text:'清空模板',
							handler:function(){
								LODOP.SET_PRINT_STYLEA('All','Deleted',true);
							}
						}
						]
					})
				},'-','功能：',functionTree, '-', '打印模板:',	templateCombo,'-',
				{
					xtype : 'splitbutton',
					text : '保存',
					handler : function() {
						if(Ext.isEmpty(functionTree.getValue())){
							alert("请先选择功能节点！");
							return;
						}
						saveTemplate();
					},
					menu : new Ext.menu.Menu({
						items : [{
							text : '另存为',
							handler : function() {
									if(Ext.isEmpty(functionTree.getValue())){
										alert("请先选择功能节点！");
										return;
									}
									var _name = prompt("模板名称：","打印模板");	
									if(Ext.isEmpty(_name,false)){	return;}
									_name =  _name.replace(/(^\s*)|(\s*$)/g, "");
									if(Ext.isEmpty(_name,false)){
										alert("模板名不能为空！");
									}else{
										saveTemplate(_name);
									}
							}
						}]
					})
				}
			]
		});
		
		// 属性列表
		var paramsGrid = new Ext.grid.GridPanel({
					region:'center',
					border : false,
					width : 180,
					height : 600,
					store : {
						url : __ctxPath+'/printTemplate/getEntityParams.do',
						autoLoad : false,
						root:'result',
						xtype : 'jsonstore',
						fields : ['dispname', 'attrname']
					},
					columns : [{header:'显示名', dataIndex:'dispname',width: 90}, {header:'属性名',dataIndex:'attrname',width:90}],
					listeners:{
						'rowdblclick':function(grid, rowIndex, e){
							var record = grid.store.getAt(rowIndex);
							LODOP.ADD_PRINT_TEXT(10,10,100,20,record.get("dispname"));
							var attrname = record.get("attrname");
							if(attrname.indexOf(".") != -1){
								attrname = "[values."+attrname+"]";
							}
							LODOP.ADD_PRINT_TEXT(10,120,100,20,"{"+attrname+"}");
						}
					}
				});
		// 左侧工具栏布局
		var panel = new Ext.Panel({
					title:'主表',
					closable:false, 
					layout : 'fit',
					baseCls : '',
					height : 600,//document.body.clientHeight,
					width : 200,
					border : false,
					items : [paramsGrid]
				})
		var tabs = new Ext.TabPanel({
			renderTo : 'sidebar',
			enableTabScroll:true,
			tabWidth:135,
			width : 200,
			height : 600,//document.body.clientHeight,
			activeTab : 0,
			frame : true,
			defaults : {
				autoHeight : true
			},
			items : [panel]
		});
	});
	task.delay(500);//延迟500毫秒响应
	
});