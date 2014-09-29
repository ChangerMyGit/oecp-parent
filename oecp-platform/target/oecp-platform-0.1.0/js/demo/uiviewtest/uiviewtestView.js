Ext.ns('OECPBC.demo.uiviewtest');
/***
 * url常量
 */
OECPBC.demo.uiviewtest.StaticUrl = {
	headDataUrl: __ctxPath + '/demo/Uiviewtest/query.do',
	bodyDataUrl :  __ctxPath+ '/demo/Uiviewtest/loadDatail.do',
	headDelUrl : __ctxPath+'/demo/Uiviewtest/delete.do',
	isAssessUrl : __ctxPath+'/demo/Uiviewtest/isAssess.do',
	queryUrl : __ctxPath + '/demo/Uiviewtest/load.do',
	editUrl : __ctxPath + '/demo/Uiviewtest/edit.do',
	commitUrl : __ctxPath + '/demo/Uiviewtest/commit.do',
	auditUrl :  __ctxPath + '/demo/Uiviewtest/audit.do'
};
/**
 * 单据视图
 */
OECPBC.demo.uiviewtest.uiviewtestView = Ext.extend(OECPBC.demo.uiviewtest.uiviewtestBaseView,{
		id : 'OECPBC.demo.uiviewtest.uiviewtestView',
		title : 'uiviewtest界面',
		//单据组件
		
		//权限按钮
		btnPermission : this.btnPermission,
		 // 查询界面的主界面数据
		headDataUrl : OECPBC.demo.uiviewtest.StaticUrl.headDataUrl, 
		// 查询界面的从界面数据
		bodyDataUrl : OECPBC.demo.uiviewtest.StaticUrl.bodyDataUrl,
		//刪除主界面数据
		headDelUrl : OECPBC.demo.uiviewtest.StaticUrl.headDelUrl,
		//校验是否有审核
		isAssessUrl : OECPBC.demo.uiviewtest.StaticUrl.isAssessUrl,
		//查看
	    queryUrl : OECPBC.demo.uiviewtest.StaticUrl.queryUrl,
	    //编辑
	    editUrl : OECPBC.demo.uiviewtest.StaticUrl.editUrl,
	    //提交审批
	    commitUrl : OECPBC.demo.uiviewtest.StaticUrl.commitUrl,
	    //审批
	    auditUrl :  OECPBC.demo.uiviewtest.StaticUrl.auditUrl,
		//快速查询Label
		quickLabel : [
					{text : '备注',style:'font-size:11px',xtype : 'label'}
		],
		//快速查询field
		quickFields : [
					{dataIndex : 'note',name:'condition[0].value',hidden:false,readOnly:false,xtype:'textfield'}
			],
		
		//快速查询条件
		hiddenFields : [
					{name:'condition[0].field',value:'note',hidden:true,xtype:'textfield'},
					{name:'condition[0].operator',value:'like',hidden:true,xtype:'textfield'},
					{name:'condition[0].fieldType',value:'java.lang.String',hidden:true,xtype:'textfield'}
			],
		//主界面grid
		headColumns : [
			{header : '主键',dataIndex : 'id',hidden : true}, 
					{header : '备注',dataIndex : 'note',hidden : false,fieldType:'java.lang.String'}
					,{header : '订单日期',dataIndex : 'orderdate',hidden : false,fieldType:'java.util.Date'}
					,{header : '订购公司',dataIndex : 'organization',hidden : false,fieldType:'java.lang.String'}
			,{header : '单据状态',dataIndex : 'state',hidden : false,renderer : function(value){
				if('EDIT' == value){
					return '编辑';
				}else if('BPMING' == value){
					return '审批中';
				}
				else if('EFFECTIVE' == value){
					return '通过';
				}
				else if('INVALID' == value){
					return '未过';
				}
			}}
			],
		headStoreFields : [ 
			{name : 'id',mapping : 'id'},
					{name : 'note',mapping : 'note'}
					,{name : 'orderdate',mapping : 'orderdate'}
					,{name : 'organization',mapping : 'organization.name'}
			,{name : 'state',mapping : 'state'}
			],
		//从界面grid
		bodyColumns : [ 
			{header : '主键',dataIndex : 'id',hidden : true}, 
					{header : '订购组件',dataIndex : 'bc',hidden : false,fieldType:'java.lang.String'}
					,{header : '是否需要验货',dataIndex : 'needcheck',hidden : false,fieldType:'java.lang.Boolean'}
					,{header : '需求数量',dataIndex : 'num',hidden : false,fieldType:'java.lang.Double'}
					,{header : '需求日期',dataIndex : 'needdate',hidden : false,fieldType:'java.util.Date'}
					,{header : '其他要求',dataIndex : 'otherdis',hidden : false,fieldType:'java.lang.String'}
					,{header : '计量单位',dataIndex : 'numunit',hidden : false,fieldType:'java.lang.String'}
		],
		bodyStoreFields : [
			{name : 'id',mapping : 'id'},
					{name : 'bc',mapping : 'bc.name'}
					,{name : 'needcheck',mapping : 'needcheck'}
					,{name : 'num',mapping : 'num'}
					,{name : 'needdate',mapping : 'needdate'}
					,{name : 'otherdis',mapping : 'otherdis'}
					,{name : 'numunit',mapping : 'numunit.name'}
		],
		
		initComponent : function(){
			var scope = this;
			this._topItems = this.hiddenFields;
			for(var i=0;i<this.quickLabel.length;i++){
				this._topItems.push(this.quickLabel[i]);
				this._topItems.push(this.quickFields[i]);
			}
			OECPBC.demo.uiviewtest.uiviewtestView.superclass.initComponent.call(this);
		}
});