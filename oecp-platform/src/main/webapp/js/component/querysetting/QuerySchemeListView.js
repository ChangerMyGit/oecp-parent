// 界面视图分配
Ext.ns('OECP.query');
OECP.query.QuerySchemeListView = Ext.extend(Ext.Panel, {
	id : 'OECP.query.QuerySchemeListView',
	title : '查询方案设计',
	treePanel : null,
	bcId : null,
	layout : 'border',
	accordionTreePanel : null,
	querySchemePanel : null,
	getQuerySchemesURL : __ctxPath + '/query/setting/list.do',
	initComponent : function() {
		this.initPanels();
		this.initWins();
		this.initRowChange();
		this.items = [this.accordionTreePanel, this.listViewPanel];
		OECP.query.QuerySchemeListView.superclass.initComponent.call(this);
	},
	initPanels : function(){
		this.initTree();
		this.initListPanel();
		this.initConditionGrids();
	},
	initListPanel : function(){
		this.initButtons();
		this.initListViewPanel();
	},
	initWins : function(){
		var master = this;
		master.win = new OECP.query.QuerySchemeWindow();
		master.win.on('billsaved',function(form){
			this.querySchemePanel.store.reload();
			this.flushDatas(master.querySchemePanel.getSelectionModel().getSelections());
		},master);
	},
	initTree : function(){
		var master = this;
		master.accordionTreePanel = new OECP.ui.AccordionTreePanel({
			width : 150,
			title : '组件与功能',
			accordionUrl : __ctxPath + '/function/listAllBCs.do',
			treeUrl : __ctxPath + '/function/fuctionTreeCode.do',
			treeEvent : {
				'click' : function(node) {
					master.funcId = node.id;
					master.qsgrid_store.baseParams.funcId = node.id;
					master.qsgrid_store.load();
					if (node.id != 'root' && node.leaf == true) {
						master.addBtn.setDisabled(false);
					}else{
						master.addBtn.setDisabled(true);
					}
				}
			}
		});
	},
	initButtons : function(){
		var master = this;
		master.addBtn = new OECP.ui.button.AddButton({
			disabled:true,
			handler : function() {
				master.win.setValues({funcId:master.funcId});
				master.win.show();
			}
		});
		master.delBtn = new OECP.ui.button.DelButton({
			disabled:true,
			handler : function() {
				var sdata = master.querySchemePanel.getSelectionModel().getSelections();
				if(sdata.length>0){
					Ext.Msg.confirm("信息确认", "您确认要删除吗？", function(b) {
						if (b == 'yes') {
							Ext.Ajax.request({
								url:__ctxPath + '/query/setting/del.do',
								params:{'qsId':sdata[0].id,'funcId':master.funcId},
								scope:master,
								success: function(response, opts) {
									var obj = Ext.decode(response.responseText);
									if(obj.success){
										Ext.ux.Toast.msg("信息","删除成功！");
										master.querySchemePanel.store.reload();
									}else{
							      		 Ext.Msg.alert("错误","删除失败!\n错误信息:"+obj.msg);
									}
								},   
								failure: function(response, opts) {
									Ext.Msg.alert("错误","删除失败!\n服务器端故障状态代码:"+response.status);
							   }
							});
						}
					});
				}
			}
		});
		master.editBtn = new OECP.ui.button.EditButton({
			disabled:true,
			handler : function() {
				var sdata = master.querySchemePanel.getSelectionModel().getSelections();
				if(sdata.length>0){
					master.win.setValues(master.currentRow);
					master.win.show();
				}
			}
		});
		master.previewBtn = new OECP.ui.button.PreviewButton({
			disabled : true,
			handler : function() {
				Ext.Ajax.request({
					scope:master,
					url: __ctxPath+'/app/query/loadQueryScheme.do',
					params:{code:master.currentRow.code},
					success: function(response, opts){
						var obj=Ext.decode(response.responseText);
						this.showPreviewWin(obj.result);
					},
					failure: function(response, opts){
						Ext.ux.Toast.msg('错误','加载失败!\n服务器端故障状态代码:' + response.status);
					}
				});
			}
		});
	},
	initListGrid : function(){
		var master = this;
		master.qsgrid_store = new Ext.data.JsonStore({
			url : master.getQuerySchemesURL,
			baseParams : {
				funcId : ""
			},
			root:'result',
			fields : ['id', 'code', 'name']
		});

		master.qsgrid_sm = new Ext.grid.CheckboxSelectionModel();
		master.qsgrid_cm = new Ext.grid.ColumnModel([master.qsgrid_sm,
			new Ext.grid.RowNumberer(), {
				header : "主键",
				dataIndex : "id",
				hidden : true
			}, {
				header : "查询方案编号",
				dataIndex : "code"
			}, {
				header : "查询方案名称",
				dataIndex : "name"
			}]);
		master.querySchemePanel = new Ext.grid.GridPanel({
			height: 240,
			region : 'center',
			enableDD : false,
			enableDrag : false,
			autoScroll : true,
			sm : master.qsgrid_sm,
			cm : master.qsgrid_cm,
			store : master.qsgrid_store,
			tbar : [master.addBtn,master.editBtn,master.delBtn,master.previewBtn]
		});
	},
	initConditionGrids : function(){
		if(!this.conditionGrids){
			this.conditionGrids = new OECP.query.QueryConditionSettingPanel({
				region : 'south',
				activeTab : 0,
				autoScroll : true,
				layoutOnTabChange : true,
				defaults : {
					autoScroll : true
				},
				cfgs : [ {title : '固定隐藏条件',prefix : 'fixedconditions',readonly : true}, 
				             {title : '常用条件',prefix : 'commonconditions',readonly : true}, 
				             {title : '其他可用条件',prefix : 'otherconditions',readonly : true} ]
			});
		}
	},
	initListViewPanel : function(){
		var master = this;
		this.initListGrid();
		this.initConditionGrids();
		master.listViewPanel = new Ext.Panel({
			title : '查询方案列表',
			region : 'center',
			enableDD : false,
			enableDrag : false,
			items : [master.querySchemePanel,master.conditionGrids]
		});
	},
	showPreviewWin : function(config){// 初始化预览窗体并显示
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
		var queryWindow=new OECP.ui.QueryWindow({closeAction:'close',conditionKey:'conditions',fieldData :_fieldData,persOperator: _persOperator,fieldDefaultValue:_fieldDefaultValue,refs:refs,fieldsAllowBlank:fieldsAllowBlank});
		queryWindow.show();
	},
	initRowChange : function(){
		var master = this;
		// 改变按钮状态
		master.querySchemePanel.getSelectionModel().on('selectionchange', function(m) {
			var data = m.getSelections();
			master.flushDatas(data);
		},this);
	},
	flushDatas : function(data){
		var master = this;
		if (data.length > 0) {
			master.flushCurrentRow(data[0]);
			master.editBtn.setDisabled(false);
			master.previewBtn.setDisabled(false);
			master.delBtn.setDisabled(false);
		} else {
			master.setCurrentRow({});
			master.editBtn.setDisabled(true);
			master.previewBtn.setDisabled(true);
			master.delBtn.setDisabled(true);
		}
	},
	flushCurrentRow:function(row){
		var master = this;
		Ext.Ajax.request({
			url:__ctxPath + '/query/setting/load.do',
			params:{'qsId':row.id},
			scope:master,
			success: function(response, opts) {
				var obj = Ext.decode(response.responseText);
				if(obj.success){
					obj.result.funcId = master.funcId;
					master.setCurrentRow(obj.result);
				}else{
					master.setCurrentRow({});
					Ext.Msg.alert("错误","删除失败!\n错误信息:"+obj.msg);
				}
			},   
			failure: function(response, opts) {
				master.setCurrentRow({});
				Ext.Msg.alert("错误","删除失败!\n服务器端故障状态代码:"+response.status);
		   }
		});
	},
	setCurrentRow : function(data){
		this.currentRow = data;
		this.conditionGrids.setConditions(this.currentRow);
	}
});
