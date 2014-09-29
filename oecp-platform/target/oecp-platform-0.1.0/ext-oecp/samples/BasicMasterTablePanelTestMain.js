Ext.onReady(function() {
	

			__ctxPath = "/oecp-platform";
			Ext.ns('test');
//			test.TestBillPanel = Ext.extend(OECP.ui.BasicListBill,{
//						viewVoId:'70a16eo5o3vh0000',
//						height:550,
//						width:720,
//						headEntityName:'headeo',
//						bodyEntityName:'bodyeo1',
//						bodyeo1_store :new Ext.data.JsonStore({
//							fields:['id','a','b','c','d','j']
//						}),
//						initComponent:function(){
//							this.bodyeo_store =new Ext.data.JsonStore(),
//							test.TestBillPanel.superclass.initComponent.call(this);
//						},
//						initSuccessCallback:function(){
//
//						}
//			});
			test.TestBillView = Ext.extend(OECP.ui.BasicCardBill,{
						height : 720,
						width : 900,
						layout:'table',
						headEntityName:'headeo',
						bodyEntityName:'bodyeo1',
						layoutConfig: {columns: 1},
						viewVoId:'70a16eo5o3vh0000',
						headeo_store:new Ext.data.JsonStore({}),
						bodyeo1_store :new Ext.data.JsonStore({
							fields:['id','a','b','c','d','j']
						}),
						initComponent:function(){
							test.TestBillView.superclass.initComponent.call(this);
						}
					});
			var view = new test.TestBillView();
			view.render("panel");
		});