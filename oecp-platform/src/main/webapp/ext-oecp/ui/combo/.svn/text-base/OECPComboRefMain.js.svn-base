Ext.onReady(function() {
	// var ref = new OECP.ui.ref.ComboRef({
	// width:120,
	// entityName:'oecp.platform.org.eo.Department',
	// functionCode:'testbill',
	// fieldName:'o.dept',
	// codeField:'code',
	// minChars:1
	// });
	// ref.getStore().on('load',function(){
	// alert('hello');
	// });
	// ref.render('ref');
	// var btn = new Ext.Button({text:'cick',handler:function(){
	// var refValue = ref.getValue();
	// alert(refValue);
	// }});
	// btn.render('button');
	// var addEventBtn = new Ext.Button({text:'add
	// event',handler:function(){
	// ref.getStore().on('load',function(){
	// alert('store reload');
	// });
	// }})
	// addEventBtn.render('eventbtn');
	// 下拉框
	var test = new OECP.ui.ref.GridWinRef({
				minListWidth : 238,
				name : 'o.dept.id',
				fieldLabel : '部门',
				valueField : 'id',
				displayField : 'code',
				width : 120,
				entityName : 'oecp.platform.org.eo.Department',
				functionCode : 'testbill',
				fieldName : 'o.dept',
				codeField : 'code',
				pageSize : 2
			});
	var form = {};
	// form = new OECP.ui.FormPanel({
	// reader : new Ext.data.JsonReader({}, [{
	// name : 'o.dept',
	// mapping : 'dept.code'
	// }, 'org']),
	// width : 500,
	// height : 200,
	// items : [test, {
	// xtype : 'textfield',
	// fieldLabel : '公司',
	// name : 'o.org',
	// dataIndex : 'org'
	// }],
	// bbar : [{
	// xtype : 'button',
	// text : '加载',
	// handler : function() {
	// var json = {};
	// json['o.dept.id'] = '227162b7k9260008';
	// json['o.dept.code'] = 'code06';
	// json['org'] = '公司1';
	// form.getForm().setValues(json);
	// }
	// }]
	// });
	// form.render('test');
	//
	// var btn = new Ext.Button({
	// text : 'c',
	// handler : function() {
	// if (test.view) {
	// test.view.show();
	// }
	// }
	// });
	// btn.render('tp');

	/** ************************************************* */
	function formatDate(value) {
		return value ? value.dateFormat('M d, Y') : '';
	}
	var fm = Ext.form;
	var cm = new Ext.grid.ColumnModel({
		defaults : {
			sortable : true
		},
		columns : [{
					id : 'common',
					header : 'Common Name',
					dataIndex : 'common',
					width : 220,
					editor : new fm.TextField({
								allowBlank : false
							})
				}, {
					header : 'Light',
					dataIndex : 'light',
					width : 130,
					editor : new OECP.grid.RefGridEditor(test),
					renderer : function(value, metaData, record, rowIndex,
							colIndex, store) {
						var data = test.findRecord(test.valueField, value);
						if (data) {
							return data.data[test.displayField];
						} else {
							return value;
						}
						return value;
					}
				}, {
					header : 'Price',
					dataIndex : 'price',
					width : 70,
					align : 'right',
					renderer : 'usMoney',
					editor : new fm.NumberField({
								allowBlank : false,
								allowNegative : false,
								maxValue : 100000
							})
				}, {
					header : 'Available',
					dataIndex : 'availDate',
					width : 95,
					renderer : formatDate,
					editor : new fm.DateField({
						format : 'm/d/y',
						minValue : '01/01/06',
						disabledDays : [0, 6],
						disabledDaysText : 'Plants are not available on the weekends'
					})
				}, {
					xtype : 'checkcolumn',
					header : 'Indoor?',
					dataIndex : 'indoor',
					width : 55
				}]
	});
	var store = new Ext.data.Store({
				autoDestroy : true,
				url : 'plants.xml',
				reader : new Ext.data.XmlReader({
							record : 'plant',
							fields : [{
										name : 'common',
										type : 'string'
									}, {
										name : 'botanical',
										type : 'string'
									}, {
										name : 'light'
									}, {
										name : 'price',
										type : 'float'
									}, {
										name : 'availDate',
										mapping : 'availability',
										type : 'date',
										dateFormat : 'm/d/Y'
									}, {
										name : 'indoor',
										type : 'bool'
									}]
						}),

				sortInfo : {
					field : 'common',
					direction : 'ASC'
				}
			});
	var grid = new Ext.grid.EditorGridPanel({
				store : store,
				cm : cm,
				width : 600,
				height : 300,
				title : 'Edit Plants?',
				frame : true,
				clicksToEdit : 1,
				tbar : [{
							text : 'Add Plant',
							handler : function() {
								var Plant = grid.getStore().recordType;
								var p = new Plant({
											common : 'New Plant 1',
											light : 'Mostly Shade',
											price : 0,
											availDate : (new Date())
													.clearTime(),
											indoor : false
										});
								grid.stopEditing();
								store.insert(0, p);
								grid.startEditing(0, 0);
							}
						}]
			});

	store.load({
		callback : function() {
			Ext.Msg.show({
						title : 'Store Load Callback',
						msg : 'store was loaded, data available for processing',
						modal : false,
						icon : Ext.Msg.INFO,
						buttons : Ext.Msg.OK
					});
		}
	});
	grid.render('test');
	/** **************************************************** */
	// var btn = new Ext.ux.form.DateTimeField({
	// name : 'pdate',
	// setValue : function(date) {
	// if (typeof(date) === "string") {
	// if (date != '') {
	// date = new Date(date);
	// } else {
	// date = new Date();
	// }
	// }
	// Ext.form.DateField.superclass.setValue.call(this,
	// this.formatDate(this.parseDate(date)));
	// }
	// });
	// var p = new Ext.FormPanel({
	// items : [btn],
	// bbar : [{
	// text : 'setValue',
	// handler : function() {
	// btn.setValue('2010-11-20 12:30:01');
	// }
	// }, {
	// text : 'getValue',
	// handler : function() {
	// alert(btn.getValue());
	// }
	// }, {
	// text : 'submit',
	// handler : function() {
	// p.getForm().submit({
	// url : 'sss.do'
	// });
	// }
	// }]
	// });
	// p.render('eventbtn');
	var btn1 = new Ext.Button({
				text : 'a',
				id : 'add_btn'
			});
	var btn2 = new Ext.Button({
				text : 'b',
				id : 'add_btn'
			});
	var pp = new Ext.Panel({
				items : [btn1, {
							xtype : 'panel',
							items : [btn2],
							html : 'test'
						}]
			});
	var a = Ext.getCmp('add_btn');
	pp.render('eventbtn');
});