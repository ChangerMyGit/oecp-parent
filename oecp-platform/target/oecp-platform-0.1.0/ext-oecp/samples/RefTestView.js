Ext.onReady(function() {
	/**
	 * 测试带分页的下拉 访问当前页面无数据时如何进行工作。
	 */
	var store = new Ext.data.JsonStore({
				autoLoad : true,
				url : '/oecp-platform/bill/foo/query.do',
				root : 'result',
				totalProperty : 'totalCounts',
				id : 'myRefStore',
				fields : ['id', 'billsn'],
				baseParams : {
					limit : 5,
					start : 0
				}
			});

	var ref = new Ext.form.ComboBox({
				minChars : 2,
				mode : 'local',
				pageSize : 5,
				valueField : 'id',
				displayField : 'billsn',
				triggerAction : 'all',
				store : 'myRefStore'
			});
	ref.render('ref1');

	var ref2 = new Ext.form.ComboBox({
				minChars : 2,
				mode : 'local',
				pageSize : 5,
				valueField : 'id',
				displayField : 'billsn',
				triggerAction : 'all',
				store : 'myRefStore'
			});
	ref2.render('ref2');

	var btn1 = new Ext.Button({
				text : 'page1:4ab169gs24ae0027'
			});
	btn1.render('btn1');
	var btn2 = new Ext.Button({
				text : 'page2:833169oelobf000b'
			});
	btn2.render('btn2');
	btn1.on('click', function() {
				ref.setValue("4ab169gs24ae0027");
			});
	btn2.on('click', function() {
				ref.setValue("833169oelobf000b");
			});

	//
	//
	// 表格测试
	Ext.QuickTips.init();
	Ext.state.Manager.setProvider(new Ext.state.CookieProvider());

	var myData = {
		seccess : true,
		result : [{
					'company' : {
						'id' : 'BX001',
						'name' : '3m Co'
					},
					'price' : 71.72,
					'change' : 0.02,
					'pctChange' : 0.03,
					'lastChange' : '9/1 12:00am'
				}, {
					'company' : {
						'id' : 'BX002',
						'name' : 'Electrolux'
					},
					'price' : 29.01,
					'change' : -0.42,
					'pctChange' : 1.47,
					'lastChange' : '9/1 12:00am'
				}],
		totalCount : 2
	};

	function change(val) {
		if (val > 0) {
			return '<span style="color:green;">' + val + '</span>';
		} else if (val < 0) {
			return '<span style="color:red;">' + val + '</span>';
		}
		return val;
	}

	/**
	 * Custom function used for column renderer
	 * 
	 * @param {Object}
	 *            val
	 */
	function pctChange(val) {
		if (val > 0) {
			return '<span style="color:green;">' + val + '%</span>';
		} else if (val < 0) {
			return '<span style="color:red;">' + val + '%</span>';
		}
		return val;
	}

	var store = new Ext.data.JsonStore({
				root : 'result',
				totalProperty : 'totalCount',
				fields : [{
							name : 'company',
							mapping : 'company.name'
						}, {
							name : 'price',
							type : 'float'
						}, {
							name : 'change',
							type : 'float'
						}, {
							name : 'pctChange',
							type : 'float'
						}, {
							name : 'lastChange',
							type : 'date',
							dateFormat : 'n/j h:ia'
						}]
			});

	store.loadData(myData);

	var grid = new Ext.grid.GridPanel({
		store : store,
		columns : [{
					id : 'company',
					header : 'Company',
					width : 160,
					sortable : true,
					dataIndex : 'company'
				}, {
					header : 'Price',
					width : 75,
					sortable : true,
					renderer : 'usMoney',
					dataIndex : 'price'
				}, {
					header : 'Change',
					width : 75,
					sortable : true,
					renderer : change,
					dataIndex : 'change'
				}, {
					header : '% Change',
					width : 75,
					sortable : true,
					renderer : pctChange,
					dataIndex : 'pctChange'
				}, {
					header : 'Last Updated',
					width : 85,
					sortable : true,
					renderer : Ext.util.Format.dateRenderer('m/d/Y'),
					dataIndex : 'lastChange'
				}, {
					xtype : 'actioncolumn',
					width : 50,
					items : [{
						icon : '/oecp-platform/extjs/resources/images/yourtheme/dd/drop-no.gif', // Use
						tooltip : 'Sell stock',
						handler : function(grid, rowIndex, colIndex) {
							var rec = store.getAt(rowIndex);
							alert("Sell " + rec.get('company'));
						}
					}, {
						getClass : function(v, meta, rec) { // Or return a class
							// from a function
							if (rec.get('change') < 0) {
								this.items[1].tooltip = 'Do not buy!';
								return 'alert-col';
							} else {
								this.items[1].tooltip = 'Buy stock';
								return 'buy-col';
							}
						},
						handler : function(grid, rowIndex, colIndex) {
							var rec = store.getAt(rowIndex);
							alert("Buy " + rec.get('company'));
						}
					}]
				}],
		stripeRows : true,
		autoExpandColumn : 'company',
		height : 350,
		width : 600,
		title : 'Array Grid',
		stateful : true,
		stateId : 'grid'
	});

	grid.render('grid-example');

	// 测试 Reader
	var _rc = new Ext.data.JsonReader({
				root : 'result',
				totalProperty : 'totalCount',
				fields : [{
							name : 'company',
							mapping : 'company.name'
						}, {
							name : 'price',
							type : 'float'
						}, {
							name : 'change',
							type : 'float'
						}, {
							name : 'pctChange',
							type : 'float'
						}, {
							name : 'lastChange',
							type : 'date',
							dateFormat : 'n/j h:ia'
						}]
			})
	var _newObj = _rc.readRecords(myData);
	_newObj.records
	
	
	
	var p2 = new Ext.Panel({
		height:400,
		width:800
	});
	p2.render('panel2');
	p2.load({
		text: 'Loading...',
		nocache: false,
		scripts: true,
		url:'/oecp-platform/bill/test.html'
	})
});