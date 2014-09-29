Ext.ns("OECP.Person");
/**
 * 新增、编辑窗口
 * @class OECP.Person.PersonWindow
 * @extends OECP.ui.CommonWindow
 */
OECP.Person.PersonWindow = Ext.extend(OECP.ui.CommonWindow,{
	width : 600,
	height : 520,
	/**
	 * 选中的部门ID
	 * @type 
	 */
	deptId : null,
	/**
	 * 编辑时员工ID
	 * @type 
	 */
	personId : null,
	/**
	 * 是编辑或是新增
	 * @type 
	 */
	isEdit : null,
	/**
	 * 编辑时加载数据
	 * @type 
	 */
	loadUrl : __ctxPath + '/person/loadData.do',
	/**
	 * 窗口里面内容
	 * @type 
	 */
	contentPanel : undefined,
	/**
	 * 员工form信息
	 * @type 
	 */
	personFormPanel : undefined,
	/**
	 * 员工兼职信息
	 * @type 
	 */
	postGridPanel : undefined,
	postGridUrl : __ctxPath + '/person/loadOtherPosts.do',
	postGridField : ['id','code','name','charge','parent.id','parent.name', 'dept.id','dept.name'],
	postGridAddBtn : undefined,
	postGridDelBtn : undefined,
	/**
	 * 任职岗位
	 * @type 
	 */
	postCombo : undefined,
	/**
	 * 保存URL
	 * @type String
	 */
	saveUrl : __ctxPath + '/person/save.do',
	/**
	 * 初始化方法
	 */
	initComponent : function(){
		var me = this;
		this.deptId = this.selectedDeptId;
		this.initWindowBtns(me);
		this.initPostCombo(me);
		this.initContent(me);
		if(this.isEdit)
			this.putValues(me);
		/**
		 * 数据保存完成
		 */
		this.addEvents('doSaved');
		OECP.Person.PersonWindow.superclass.initComponent.call(this);
	},
	/**
	 * 初始化窗口里面的按钮
	 * @param {} me
	 */
	initWindowBtns : function(me){
		var saveBtn = new OECP.ui.Button({
				text : "保存",
				iconCls : 'btn-save',
				handler : function(){
					me.doSave(me);
				}
			});

		var closeBtn = new OECP.ui.Button({
				text : "关闭",
				iconCls : 'btn-cancel',
				handler : function(){
					me.close();
				}
			});

		me.buttonArray = [saveBtn,closeBtn];
		
		if(!Ext.isDefined(me.postGridAddBtn)){
			me.postGridAddBtn = new OECP.ui.Button({
				text : "新增",
				iconCls : 'btn-add',
				handler : function(){
					//已经选择的兼职岗位数据
					var postIds = new Array();
					postIds.push(me.postCombo.getValue());
					var num = me.postGridPanel.getStore().getCount();
					for(var i=0;i<num;i++){
						var record = me.postGridPanel.getStore().getAt(i);
						var id = record.get("id");
						postIds.push(id);
					}
					var otherPostWindow = new OECP.person.OtherPostSelectWindow({postIds:postIds});
					otherPostWindow.on("doConfirm",function(records){
						me.postGridPanel.getStore().add(records);
					});
					otherPostWindow.show();
				}
			});
		}
		if(!Ext.isDefined(me.postGridDelBtn)){
			me.postGridDelBtn = new OECP.ui.Button({
				text : "删除",
				iconCls : 'btn-delete',
				handler : function(){
					var records = me.postGridPanel.getSelectionModel().getSelections();
					if(records.length==0){
						Ext.Msg.alert('提示','请至少选择一条记录进行删除',function(){});
						return;
					}
					Ext.each(records,function(record){
						me.postGridPanel.getStore().remove(record);
					});
				}
			});
		}
	},
	/**
	 * 初始化任职岗位combo
	 * @param {} me
	 */
	initPostCombo : function(me){
		if(!Ext.isDefined(me.postCombo)){
			me.postCombo = new OECP.person.PostSelectCombo({
				deptId : me.deptId,
				hiddenName : 'person.post.id',
				displayField : 'name',
				valueField : 'id',
				dataIndex : 'person.post.id',
				fieldLabel : '任职岗位',
				mapping : 'person.post.id',
				anchor:'95%'
			});
		}
	},
	
	/**
	 * 初始化窗口里面的内容
	 * @param {} me
	 */
	initContent : function(me){
		if(!Ext.isDefined(me.personFormPanel)){
			me.personFormPanel = new Ext.FormPanel({
				region	: 'center',
		        labelWidth: 75, 
		        frame:true,
		        bodyStyle:'padding:5px 5px 0',
		        width: 350,
		        reader : new Ext.data.JsonReader({
					root : 'result'
				}, [{
							name : 'person.id',
							mapping : 'id'
						}, {
							name : 'person.no',
							mapping : 'no'
						}, {
							name : 'person.name',
							mapping : 'name'
						}, {
							name : 'person.email',
							mapping : 'email'
						}, {
							name : 'person.mobile',
							mapping : 'mobile'
						}, {
							name : 'person.post.id',
							mapping : 'post.id'
						}]),
		        items: [{
		            layout:'column',
		            items:[{
		                columnWidth:.5,
		                layout: 'form',
		                items: [{
		                	xtype:'hidden',
			                name: 'person.id'
			            },{
		                	xtype:'textfield',
			                fieldLabel: '人员编号',
			                name: 'person.no',
			                allowBlank:false,
			                anchor:'95%'
			            }, {
			            	xtype:'textfield',
			                fieldLabel: '邮箱',
			                name: 'person.email',
			                vtype:'email',
			                vtypeText:"不是有效的邮箱地址",
			                anchor:'95%'
			            }, me.postCombo]
		            },{
		                columnWidth:.5,
		                layout: 'form',
		                items: [{
		                	xtype:'textfield',
			                fieldLabel: '人员姓名',
			                name: 'person.name',
			                allowBlank:false,
			                anchor:'95%'
			            },{
			            	xtype:'textfield',
			                fieldLabel: '手机号码',
			                name: 'person.mobile',
			                anchor:'95%'
			            }]
		            }]
		        }]
		        
		    });
		}
		if(!Ext.isDefined(me.postGridPanel)){
			var sm = new Ext.grid.CheckboxSelectionModel();
			me.postGridPanel = new Ext.grid.GridPanel({
				title : '兼职信息',
				region	: 'south',
				height : 320,
				sm : sm,
				columns : [sm,{dataIndex : "id",hidden:true},
						   {header : "岗位编码",dataIndex : "code",width:90},
						   {header : "岗位名称",dataIndex : "name",width:100},
						   {header : "是否管理岗位",dataIndex : "charge",width:100},
						   {header : "上级岗位名称",dataIndex : "parent.name"},
						   {header : "部门名称",dataIndex : "dept.name"}],
				store	:  new Ext.data.JsonStore({
					url	:	me.postGridUrl,
					fields : me.postGridField
				}),
				tbar : [me.postGridAddBtn,me.postGridDelBtn]
			});	
		}
		
		me.contentPanel = new Ext.Panel({
			title : '员工信息',
			layout : 'border',
			width : 585,
			height : 450,
			autoScroll	: true,
			items : [me.personFormPanel,me.postGridPanel]
		});
		me.componetArray = [me.contentPanel];
	},
	/**
	 * 保存之前校验
	 * @param {} me
	 */
	doCheckBeforeSave : function(me){
		me.personFormPanel.getForm().isValid();
		 var no = me.personFormPanel.form.findField('person.no').getValue();
		 var name = me.personFormPanel.form.findField('person.name').getValue();
		 var email = me.personFormPanel.form.findField('person.email').getValue();
		 var mobile = me.personFormPanel.form.findField('person.mobile').getValue();
		 var post = me.personFormPanel.form.findField('person.post.id').getValue();
		 if(Ext.isEmpty(no)){
		 	Ext.Msg.alert('提示','人员编号不能为空');
		 	return false;
		 }
		 if(Ext.isEmpty(name)){
		 	Ext.Msg.alert('提示','人员姓名不能为空');
		 	return false;
		 }
		 if(!Ext.isEmpty(email)&&!new RegExp("^[0-9a-zA-Z]+@[0-9a-zA-Z]+[\.]{1}[0-9a-zA-Z]+[\.]?[0-9a-zA-Z]+$").test(email)){
		 	Ext.Msg.alert('提示','邮箱格式不正确');
		 	return false;
		 }
		 if(!Ext.isEmpty(mobile)&&!(/^1[3|4|5|8][0-9]\d{8}$/.test(mobile))){
		 	Ext.Msg.alert('提示','手机号码格式不正确');
		 	return false;
		 }
		 if(Ext.isEmpty(post)){
		 	Ext.Msg.alert('提示','任职岗位不能为空');
		 	return false;
		 }
		 var num = me.postGridPanel.getStore().getCount();
		 for(var i=0;i<num;i++){
			var record = me.postGridPanel.getStore().getAt(i);
			var id = record.get("id");
			if(id==post){
				Ext.Msg.alert('提示','任职岗位不能再选择成兼职岗位');
		 		return false;
			}
		 }
		 return true;
	},

	/**
	 * 保存方法
	 */
	doSave : function(me){
		//表单数据
		var formData = me.personFormPanel.getForm().getValues();
		//兼职岗位数据
		var num = me.postGridPanel.getStore().getCount();
		for(var i=0;i<num;i++){
			var record = me.postGridPanel.getStore().getAt(i);
			var id = record.get("id");
			formData["person.otherPosts["+i+"].id"]=id;
		}
		//基本校验
		if(!me.doCheckBeforeSave(me)){
			return;
		}
		Ext.Ajax.request({
			url : me.saveUrl,
			params : formData,
			success : function(request) {
				var json = Ext.decode(request.responseText);
				if (json.success) {
					Ext.ux.Toast.msg('信息',	json.msg);
					me.fireEvent('doSaved');
					me.close();
				} else {
					Ext.Msg.show({title:'错误',msg:json.msg,buttons:Ext.Msg.OK	});
				}
			},
			failure : function(request) {
				var json = Ext.decode(request.responseText);
				Ext.Msg.show({title:'错误',msg:json.msg,buttons:Ext.Msg.OK});
			}
		});
	},
	/**
	 * 编辑时，赋值
	 * @param {} me
	 */
	putValues : function(me){
		Ext.Ajax.request({
			url : me.loadUrl,
			params : {
				"personId":me.personId
			},
			success : function(request) {
				var json = Ext.decode(request.responseText);
				if (json.success) {
					//加载表单信息
					var rs = me.personFormPanel.reader.readRecords(json);
					var _data = rs.records && rs.records[0] ? rs.records[0].data : null;
					me.personFormPanel.getForm().setValues(_data);
					//加载兼职信息
					me.postGridPanel.getStore().removeAll();
					me.postGridPanel.getStore().baseParams = {"personId":me.personId};
					me.postGridPanel.getStore().load();
				} else {
					Ext.Msg.show({title:'错误',msg:json.msg,buttons:Ext.Msg.OK	});
				}
			},
			failure : function(request) {
				var json = Ext.decode(request.responseText);
				Ext.Msg.show({title:'错误',msg:json.msg,buttons:Ext.Msg.OK});
			}
		});
	}
});