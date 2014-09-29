Ext.ns('OECPBC.demo.uiviewtest');

OECPBC.demo.uiviewtest.uiviewtestPanel = Ext.extend(OECP.ui.MasterDetailEditPanel,{
			frame : true,
			bodyStyle : 'padding:3px 3px 0',
			modifiedVarName : 'uiviewtest',
			detailVarName : 'details',
			delVarName : 'details',
			submitUrl : __ctxPath + '/demo/Uiviewtest/save.do',
			visibleFields : [],
			hiddenFields : [],
			tailFields : [],
			reader:new Ext.data.JsonReader({},
				['billsn','createdate','creater.id','changer.id','changedate','state','id'

					,{name:'note', mapping:'note'}
					,{name:'orderdate', mapping:'orderdate'}
					,{name:'organization', mapping:'organization'}
				]
			),
			initComponent : function(){
				var scope = this;
				
				var idField = new OECPBC.Bill.ref.BillIDField();
				var state = new OECPBC.Bill.ref.StateField();
				var billsnField = new OECPBC.Bill.ref.BillsnField();
				scope.hiddenFields.push(idField,state,billsnField);
				
				var items1 = [billsnField];
				var items2 =[];
				var items3 =[];
				var _note = new Ext.form.TextField({
					fieldLabel : '备注',
					name:'note',
					dataIndex : 'note',
					allowBlank:true
					,maxLength:50
				});
				scope.visibleFields.push(_note);
				items2.push(_note);
				var _orderdate = new Ext.form.TextField({
					fieldLabel : '订单日期',
					name:'orderdate',
					dataIndex : 'orderdate',
					allowBlank:false
					
				});
				scope.visibleFields.push(_orderdate);
				items3.push(_orderdate);
				var _organization = new Ext.form.TextField({
					fieldLabel : '订购公司',
					name:'organization',
					dataIndex : 'organization',
					allowBlank:true
					
				});
				scope.visibleFields.push(_organization);
				items1.push(_organization);
					
				this.items = [scope.hiddenFields[0],scope.hiddenFields[1],{
							layout : 'column',
							items : [{
								columnWidth : 0.3,
								layout : 'form',
								items : items1
							}, {
								columnWidth : 0.3,
								layout : 'form',
								items : items2
							}, {
								columnWidth : 0.3,
								layout : 'form',
								items : items3
							}]
						}];
				var createID = new OECPBC.Bill.ref.CreaterIDTextField();
				var createTimeField = new OECPBC.Bill.ref.DateField();
				var changerId = new OECPBC.Bill.ref.ChangerIDTextField();
				var changedateField =new OECPBC.Bill.ref.ChangedateField();
				
				scope.tailFields.push(createID,createTimeField,changerId,changedateField);
				this.tailItem = {
					layout : 'column',
					items : [{
								columnWidth : 0.5,
								layout : 'form',
								items : [scope.tailFields[0],scope.tailFields[2]]
							}, {
								columnWidth : 0.5,
								layout : 'form',
								items : [scope.tailFields[1],scope.tailFields[3]]
							}]
				};
				if(!this.bodyGrid){
					this.bodyStore = new Ext.data.JsonStore({
						fields : [
							
							{name:'bc' ,type:'string'}
							,{name:'needcheck' ,type:'bool'}
							,{name:'num' ,type:'number'}
							,{name:'needdate' ,type:'date'}
							,{name:'otherdis' ,type:'string'}
							,{name:'numunit' ,type:'string'}
						]
					});
					this.bodyColumns = [

						{
							header : '订购组件',
							name : 'bc',
							dataIndex : 'bc',
							editor : new Ext.form.TextField({
								allowBlank:false
								
							})
						}
						,{
							header : '是否需要验货',
							name : 'needcheck',
							dataIndex : 'needcheck',
							editor : new Ext.form.TextField({
								allowBlank:true
								
							})
						}
						,{
							header : '需求数量',
							name : 'num',
							dataIndex : 'num',
							editor : new Ext.form.TextField({
								allowBlank:false
								
							})
						}
						,{
							header : '需求日期',
							name : 'needdate',
							dataIndex : 'needdate',
							editor : new Ext.form.TextField({
								allowBlank:true
								
							})
						}
						,{
							header : '其他要求',
							name : 'otherdis',
							dataIndex : 'otherdis',
							editor : new Ext.form.TextField({
								allowBlank:true
								,maxLength:500
							})
						}
						,{
							header : '计量单位',
							name : 'numunit',
							dataIndex : 'numunit',
							editor : new Ext.form.TextField({
								allowBlank:false
								
							})
						}
					]
				}
				OECPBC.demo.uiviewtest.uiviewtestPanel.superclass.initComponent.call(this);
			},
		setDefaultState : function(value){
			this.setEditable(value)
			if(value){
				if(typeof(this.visibleFields[0].getEl()) != 'undefined'){
					for(var i=0;i<this.visibleFields.length;i++){
						this.visibleFields[i].setReadOnly(false);
					}
				}
			}else{
				for(var i=0;i<this.visibleFields.length;i++){
					this.visibleFields[i].setReadOnly(true);
				}
			}
		}
});