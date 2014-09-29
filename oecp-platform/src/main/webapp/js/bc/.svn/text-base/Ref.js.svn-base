Ext.ns('OECPBC.Bill.ref');
/**
 * 主表空间
 */

//主键
OECPBC.Bill.ref.BillIDField = Ext.extend(Ext.form.Field,{
			fieldLabel : '主键',
			dataIndex : 'id',
			name:'id',
			mapping:'id',
			hidden:true,
			readOnly:true
})

OECPBC.Bill.ref.ProvinceCombo = Ext.extend(Ext.form.ComboBox,{
			fieldLabel : '省份',
			emptyText : '请选择',
			width : 120,
			store : new Ext.data.JsonStore({
						root : 'result',
						totalProperty : 'totalCounts',
						fields : ['id', 'name'],
						autoLoad : true,
						url : __ctxPath + "/bill/ref/provinceRef.do"
						
					}),
			valueField : 'id',
			displayField : 'name',
			mode : 'local',
			allowBlank : false,
			readOnly : false,
			editable : false,
			hiddenName : 'province.id',
			triggerAction : 'all'
});


OECPBC.Bill.ref.ChargeoffTypeCombo = Ext.extend(Ext.form.ComboBox, {
			fieldLabel : '核销类别',
			width : 120,
			xtype : 'combo',
			name : 'chargeoff_type',
			dataIndex : 'chargeoff_type',
			mapping : 'chargeoff_type',
			store : new Ext.data.SimpleStore({
						fields : ['value', 'text'],
						data : [['按流向核销', '按流向核销'], ['按回款核销', '按回款核销']]
					}),
			valueField : 'value',
			displayField : 'text',
			mode : 'local',
			allowBlank : false,
			readOnly : false,
			editable : false,
			triggerAction : 'all'
		});

//textfield
OECPBC.Bill.ref.BillsnField = Ext.extend(Ext.form.TextField,{
			fieldLabel : '单据号',
			width : 120,
			name : 'billsn',
			dataIndex : 'billsn',
			readOnly : true
//			editable : false,
//			defaultReadOnly : true,
		});
//TextArea
OECPBC.Bill.ref.InfoField = Ext.extend(Ext.form.TextArea,{
			fieldLabel : '备注',
			width : 120,
			name : 'info',
			dataIndex : 'info',
//			defaultReadOnly : true,
//			editable : false,
			readOnly : false
		});
		
//dateField
OECPBC.Bill.ref.DateField = Ext.extend(Ext.ux.form.DateTimeField,{
			fieldLabel : '制单时间',
			width : 120,
			dataIndex : 'createdate',
			name:'createdate',
			mapping:'createdate',
			setValue : function(date){
			    if(typeof(date) === "string") {  
			    	if(date != ''){
			      		date = new Date(date);  
			    	}else{
						date = new Date();	    	
			    	}
			    }
			      Ext.form.DateField.superclass.setValue.call(this, this.formatDate(this.parseDate(date)));
			  } ,
//			editable : false,
			readOnly:true
		});

// 显示用,不提交数据
OECPBC.Bill.ref.CreaterTextField = Ext.extend(Ext.form.TextField, {
			fieldLabel : '制单人',
			width : 120,
			name : 'creater.name',
			dataIndex : 'creater.name',
			hiddenName : 'creater.name',
			mapping : 'creater.name',
			readOnly : true,
			editable : false
		});

OECPBC.Bill.ref.CreaterIDTextField = Ext.extend(Ext.form.TextField, {
			fieldLabel : '制单人ID',
			width : 120,
			name : 'creater.id',
			dataIndex : 'creater.id',
			mapping : 'creater.id',
			readOnly : true,
//			editable : false,
			hidden : false
		});


OECPBC.Bill.ref.ChangerTextField = Ext.extend(Ext.form.TextField, {
	fieldLabel : '修改人',
	width : 120,
	name : 'changer.name',
	dataIndex : 'changer.name',
	hiddenName : 'changer.name',
	mapping : 'changer.name',
	readOnly : true,
	editable : false
});

OECPBC.Bill.ref.ChangerIDTextField = Ext.extend(Ext.form.TextField, {
	fieldLabel : '修改人ID',
	width : 120,
	name : 'changer.id',
	dataIndex : 'changer.id',
	mapping : 'changer.id',
	readOnly : true,
	editable : false,
	hidden : false
});

OECPBC.Bill.ref.ChangedateField = Ext.extend(Ext.ux.form.DateTimeField, {
	fieldLabel : '修改时间',
	width : 120,
	dataIndex : 'changedate',
	name : 'changedate',
	mapping : 'changedate',
	setValue : function(date) {
		if (typeof(date) === "string") {
			if (date != '') {
				date = new Date(date);
			} else {
				date = new Date();
			}
		}
		Ext.form.DateField.superclass.setValue.call(this, this
						.formatDate(this.parseDate(date)));
	},
	editable : false,
	readOnly : true
});

OECPBC.Bill.ref.StateField = Ext.extend(Ext.form.TextField,{
			fieldLabel : '单据状态',
			width : 120,
			name : 'state',
			dataIndex : 'state',
			mapping : 'state',
			hidden : true,
//			defaultReadOnly : true,
//			editable : false,
			readOnly : true
		});		
		
//enum Combo
OECPBC.Bill.ref.BillStateCombo = Ext.extend(OECP.ui.combobox.EnumsCombo, {
			className : 'com.examples.bill.eo.BillState',
			fieldLabel : '单据状态',
			dataIndex : 'state',
			mapping : 'state',
			hidden : false,
			hiddenName : 'state',
			readOnly : true,
//			defaultReadOnly : true,
//			editable : false,
			name : 'state',
			width : 120
		});

//单据的业务类型id
OECPBC.Bill.ref.BizTypeIDField = Ext.extend(Ext.form.Field,{
			fieldLabel : '单据的业务类型id',
			dataIndex : 'bizType.id',
			name:'bizType.id',
			mapping:'bizType.id',
			hidden:true,
			readOnly:true
});

//单据的上游单据id
OECPBC.Bill.ref.PreBillIDField = Ext.extend(Ext.form.Field,{
			fieldLabel : '单据的上游单据id',
			dataIndex : 'preBillID',
			name:'preBillID',
			mapping:'preBillID',
			hidden:true,
			readOnly:true
});

//单据的下游单据id
OECPBC.Bill.ref.NextBillIDField = Ext.extend(Ext.form.Field,{
			fieldLabel : '单据的下游单据id',
			dataIndex : 'nextBillID',
			name:'nextBillID',
			mapping:'nextBillID',
			hidden:true,
			readOnly:true
});
