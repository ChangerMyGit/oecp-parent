Ext.ns('test');
test.TestBillView = Ext.extend(OECP.ui.BasicListBill,{
	billFlowFlag:true,
	functionCode:'uiviewtest',
	headEntityName:'bill',
	bodyEntityName:'details',
	queryListUrl: __ctxPath+'/demo/Uiviewtest/query.do',
	delUrl 		: __ctxPath+'/demo/Uiviewtest/delete.do',
	isAssessUrl : __ctxPath+'/demo/Uiviewtest/isAssess.do',
	queryBillUrl: __ctxPath+'/demo/Uiviewtest/load.do',
	editUrl 	: __ctxPath+'/demo/Uiviewtest/edit.do',
	commitUrl 	: __ctxPath+'/demo/Uiviewtest/commit.do',
	auditUrl 	: __ctxPath+'/demo/Uiviewtest/audit.do',
	submitUrl	: __ctxPath+ '/demo/Uiviewtest/save.do',
	preDatasUrl	: __ctxPath+ '/demo/Uiviewtest/getFromPreDatas.do',
	billIsInProcessUrl: __ctxPath+ '/demo/Uiviewtest/billIsInProcess.do',
	//单据流中查看当前单据的URL,和审批流中查看页是同一个地址
	currentBillUrl: '/page/demo/uiviewtest/uiviewtestApproveView.jsp?functionCode=uiviewtest',
	bill_liststore_cfg:{
		url:__ctxPath+'/demo/Uiviewtest/query.do',
		baseParams:{functionCode:'uiviewtest'},
		totalProperty:'totalCounts',
		root:'result',
		fields:[
				'id','billsn','state','note',
				{name:'orderdate',type:'date'},
				{name:'organization.name'},//汉字
				{name:'organization.id'},//编码
				{name:'creater.id'}//编码
				,'details'//临时数据 业务流使用
				,'creater',
				'bizType','preBillID','nextBillID'//后三个为业务流必须字段
				]
	},
	details_liststore_cfg :{
		fields:['id','needcheck','num','needdate','otherdis']
	},
	details_cardstore_cfg :{
		fields:['id','needcheck','num','needdate','otherdis']
	},
	initComponent:function(){
		this.width = document.body.clientWidth;
		this.height = document.body.clientHeight,
		test.TestBillView.superclass.initComponent.call(this);
	}
});
