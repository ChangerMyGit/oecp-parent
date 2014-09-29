Ext.ns('OECP.ui.BillFlowHistory');

/***
 * 常量
 */
OECP.ui.BillFlowHistory.StaticParam = {
	/**
	 * 当前打开窗口的宽度
	 */
	currentWindowWidth : 800,
	/**
	 * 当前打开窗口的高度
	 */
	currentWindowHeight : 400,
	/**
	 * 查看上游单据时，当前单据在窗口的水平坐标值
	 */	
	upCurrentBillWidth : 880,
	/**
	 * 查看下游单据时，当前单据在窗口的水平坐标值
	 */	
	downCurrentBillWidth : 10,
	/**
	 * 画线的宽度范围
	 * @type Number
	 */
	drawingWidth : 1000,
	/**
	 * 画线的高度范围
	 * @type Number
	 */
	drawingHeight : 10000
};

/**
 * 单据panel
 */
OECP.ui.BillFlowHistory.Panel = Ext.extend(Ext.Panel, {
	/**
	 * 高度
	 */
	height : 100,
	/**
	 * 宽度
	 */
	width : 150,
	/**
	 * 
	 */
	frame : false,
	/**
	 * 滚动条
	 */
	autoScroll : true,
	/**
	 * 传进来josn数据
	 */
	jsonData : null,
	/**
	 * 工具栏
	 */
	bbar  : [],
	/**
	 * 上游或下游
	 */
	up : false,
	/**
	 * 功能编号
	 */
	functionCode :　null,
	/**
	 * 当前单据的ID
	 */
	billId : null,
	/**
	 * 打开单据的url
	 */
	billURL : null,
	/**
	 * 父级窗口
	 */
	pWindow : null,
	/**
	 * 点击上游或下游按钮次数
	 */
	clickCount : 0,
	/**
	 * 前一个对象
	 */
	preBillObject : null,
	/**
	 *  初始化方法
	 */
	initComponent : function() {
		var scope = this;
		/**
		 * 参数
		 */
		this.functionCode = this.jsonData['functionCode'];
		this.billId = this.jsonData['id'];
		this.billURL = this.jsonData['billURL'];
		this.pWindow = this.pWindow;
		this.setHtml(scope);
		/**
		 *  初始化按钮
		 */
		this.btnPre = new OECP.ui.Button({text:'<--上游单据',handler:function(){scope.preBill(scope);}});
		this.btnNext = new OECP.ui.Button({text:'下游单据-->',handler:function(){scope.nextBill(scope);}});
		this.bbar = this.jsonData['functionName']==undefined?[]:(this.up?[this.btnPre]:[this.btnNext]);
		/**
		 * 打开单据的事件
		 */
		this.on("render",function(panel){panel.body.on('dblclick', scope.openBillJsp,scope);},scope);
		OECP.ui.BillFlowHistory.Panel.superclass.initComponent.call(this);
	},
	/**
	 * html内容
	 */
	setHtml :　function(scope){
		var htmlContent = '<p style="font-size:12px;" >单据名称: ';
		htmlContent+=this.jsonData['functionName']==undefined?'当前单据':this.jsonData['functionName'];
		htmlContent+='</p><p style="font-size:12px" >单据号: ';
		htmlContent+=this.jsonData['billsn'];
		htmlContent+='</p><p style="font-size:12px" >状态: ';
		var value = this.jsonData['state'];
		if('EDIT' == value){
			htmlContent+= '编辑';
		}else if('BPMING' == value){
			htmlContent+= '审批中';
		}
		else if('EFFECTIVE' == value){
			htmlContent+= '通过';
		}
		else if('INVALID' == value){
			htmlContent+= '未过';
		}
		else if('TEMPORARY' == value){
			htmlContent+= '临时';
		}
		htmlContent+="</p>";
		scope.html = htmlContent;
	},
	/**
	 * 查看某个单据的上游单据
	 */
	preBill : function(scope){
		var functionCode = this.functionCode;
		var billId = this.billId;
		var seeDirection = "UP";
		
		if(scope.clickCount<1){
			this.addHistoryPanel(scope,billId,functionCode,seeDirection);
		}else{
			Ext.Msg.show({
				title : '提示',
				msg : "该单据的上游单据已经展现",
				buttons : Ext.Msg.OK
			});
		}
	},
	/**
	 * 查看某个单据的下游
	 */
	nextBill : function(scope){
		var functionCode = this.functionCode;
		var billId = this.billId;
		var seeDirection = "DOWN";
		
		if(scope.clickCount<1){
			this.addHistoryPanel(scope,billId,functionCode,seeDirection);
		}else{
			Ext.Msg.show({
				title : '提示',
				msg : "该单据的下游单据已经展现",
				buttons : Ext.Msg.OK
			});
		}
	},
	/**
	 * 动态增加panel
	 */
	addHistoryPanel : function(scope,billId,functionCode,seeDirection){
		/**
		 * 发送请求加载上游或下游数据
		 */
		Ext.Ajax.request({
			url:__ctxPath + "/billflow/getBillFlowHistory.do",
			params:{
				billId : billId,
				functionCode : functionCode,
				seeDirection : seeDirection
			},
			success : function(h) {
				var data =  Ext.util.JSON.decode(h.responseText);
				if(data.success){
					//记录该单据的下游或下游单据是否已打开
					scope.clickCount++;
					//窗口中单据panel的个数
					var count = scope.pWindow.mainPanel.items.length;
					var xvalue = (seeDirection=="UP"?(scope.x-250):(scope.x+250));
					var num = data['result'].length;
					//当前单据对象
					var object = scope;
					for(var i=0;i<num;i++){
						var yvalue = 105*(i+1);
						yvalue = scope.getXYValue(scope,xvalue,yvalue);
						var billPanel = new OECP.ui.BillFlowHistory.Panel({
							pWindow:scope.pWindow,
							up:scope.up,
							jsonData: data['result'][i],
							x:xvalue,
							y:yvalue,
							preBillObject:scope
						});
						scope.pWindow.xyArray.push(xvalue+','+yvalue);
						scope.pWindow.mainPanel.add(billPanel);
						scope.pWindow.mainPanel.doLayout();
						//当前单据和上/下单据画线
						scope.pWindow.drawLine(object,billPanel);
					}
					scope.pWindow.columnsNum++;
				}else{
					Ext.Msg.show({
						title : '错误',
						msg : data.msg,
						buttons : Ext.Msg.OK
					});
				}
				
			}
		});
	},
	/**
	 * 递归取出panel可以存放的xy位置
	 */
	getXYValue : function(scope,xvalue,yvalue){
		if(scope.pWindow.xyArray.indexOf(xvalue+','+yvalue) > -1)
			return scope.getXYValue(scope,xvalue,yvalue+105);
		else
			return yvalue;
	},
	/**
	 * 打开历史单据的具体页面信息
	 */
	openBillJsp : function(){
		var id= this.billId;
		var billURL = this.billURL;
		var functionCode = this.functionCode;
		if(Ext.isEmpty(billURL)){
			Ext.Msg.alert("提示","流程默认页面没有注册，请联系管理员");
			return;
		}
		var fromPanel = new Ext.Panel({
//					title : "当前业务表单",
					height : 400,
					width : 770,
					frame : true,
					autoScroll : true,
					html:'<iframe frameborder="no" border="0" height="350" width="740" src="'+__fullPath+billURL+"&billId="+id+'"></iframe>'
		});
		var billWindow = new OECP.ui.CommonWindow({
			title:'单据信息',
			width:780,
			height:420,
			closeAction : 'hide',
			componetArray : [fromPanel]
		});
		billWindow.show();
	
	}
});

/**
 * 查看单据流历史窗口：查看上游和下游单据
 * author yangtao
 * OECP.ui.BillFlowHistoryWindow
 * extend Ext.Window
 */
OECP.ui.BillFlowHistory.Window = Ext.extend(Ext.Window, {
	/**
	 * 窗口标题
	 */
	title : '', 
	/**
	 * 滚动条
	 */
	autoScroll : true,
	/**
	 * 窗口后面的遮罩
	 */
	modal : true,
	/**
	 * 上游或下游
	 */
	up : false,
	/**
	 * 上游或下游单据的数据
	 */
	jsonData : null,
	/**
	 * 当前单据的数据
	 */
	currentJsonData : null,
	/**
	 * 宽度
	 */
	width:OECP.ui.BillFlowHistory.StaticParam.currentWindowWidth,
	/**
	 * 高度
	 */
	height:OECP.ui.BillFlowHistory.StaticParam.currentWindowHeight,
	/**
	 * 列表数据项
	 */
	filedData : [],
	/**
	 * 列表表头
	 */
	structure : [],
	
	/**
	 * store的参数
	 */
	storeParams : null,
	
	/**
	 * 布局
	 */
	layout : 'fit',
	/**
	 * 列数
	 */
	columnsNum : 2,
	/**
	 * 窗口中已经存在panel的xy值
	 */
	xyArray : [],
	
	/**
	 *  初始化方法
	 */
	initComponent : function() {
		var master = this;
		this.title = '查看 ' + (this.up ? '上游' : '下游') +' 单据';
		var num = this.jsonData.length;
		/**
		 * 刚打开窗口，初始化两个panel
		 */
		var xvalue = this.up ?OECP.ui.BillFlowHistory.StaticParam.upCurrentBillWidth :OECP.ui.BillFlowHistory.StaticParam.downCurrentBillWidth ;
		var xvalue2 = this.up ?(OECP.ui.BillFlowHistory.StaticParam.upCurrentBillWidth-250):(OECP.ui.BillFlowHistory.StaticParam.downCurrentBillWidth+250);
		var yvalue = num>=2?Math.round(num/2)*105:105;
		var yvalue2;
		this.currentBillPanel = new OECP.ui.BillFlowHistory.Panel({
			pWindow:master,
			up:this.up,
			jsonData:this.currentJsonData,
			x:xvalue,
			y:yvalue,
			bodyStyle :{background: '#00FF00'}
		});
		this.xyArray.push(xvalue+','+yvalue);

		/**
		 * 主panel
		 */
		this.mainPanel = new Ext.Panel({
			width: OECP.ui.BillFlowHistory.StaticParam.drawingWidth,
			height: OECP.ui.BillFlowHistory.StaticParam.drawingHeight,
			autoScroll:true,
			renderTo:document.body,
			layout:'absolute',		   
			items:[this.currentBillPanel]
		});
		
		this.historyBillPanel = [];
		for(var i=0;i<num;i++){
			yvalue2 = 105*(i+1)
			this.xyArray.push(xvalue2+','+yvalue2);
			this.historyBillPanel[i] = new OECP.ui.BillFlowHistory.Panel({
				pWindow:master,
				up:this.up,
				jsonData:this.jsonData[i],
				x:xvalue2,
				y:yvalue2,
				preBillObject:this.currentBillPanel
			});
			this.mainPanel.add(this.historyBillPanel[i]);
			this.mainPanel.doLayout();
		}
		
		/**
		 * 加入到窗口中
		 */
		master.items = [this.mainPanel];
		/**
		 * 关闭窗口时，清空窗口中已经存在panel的xy值数组
		 */
		this.on("close",function(){
			master.xyArray.length = 0;
		},this);
		OECP.ui.BillFlowHistory.Window.superclass.initComponent.call(this);
	},
	/**
	 * 加载完窗口之后，在panel直接画线
	 */
	afterWindowdrawLine : function(){
		this.surf=new Ext.Drawing.Surface({El:this.mainPanel.body,width:OECP.ui.BillFlowHistory.StaticParam.drawingWidth,height:OECP.ui.BillFlowHistory.StaticParam.drawingHeight});
		this.r = this.surf.render;
		var svg=this.r.getRoot();
		for(var i = 0;i<this.historyBillPanel.length;i++){
			this.drawLine(this.currentBillPanel,this.historyBillPanel[i]);
		}
	},
	/**
	 * 动态增加panel之后画线
	 */
	drawLine : function(e1,e2){
		if(!this.surf){
			//width:this.mainPanel.body.getWidth()
			this.surf=new Ext.Drawing.Surface({El:this.mainPanel.body,width:OECP.ui.BillFlowHistory.StaticParam.drawingWidth,height:OECP.ui.BillFlowHistory.StaticParam.drawingHeight});
			this.r = this.surf.render;
		}
		
		var svg=this.r.getRoot();
		var s1 = this.getoffset(e1.body.dom),s2 = this.getoffset(e2.body.dom),
			z1 = e1.getSize(),z2=e2.getSize();
		s1[0] = (s1[0] + z1.width-7);
		s1[1] = (s1[1] + z1.height/2);
		s2[0] = (s2[0]);
		s2[1] = (s2[1] +z2.height/2);
		
		
		var line = this.r.createShape({
			type:"line",
			points:{
				x1:s1[0],
				x2:s2[0],
				y1:s1[1],
				y2:s2[1]
			},
			stroke:{
				color:"red"
			}
		});
		svg.appendChild(line.dom);
	},
	/**
	 * 得出某个panel的x\y坐标
	 */
	getoffset : function(e)
	{
		var node = e;/*求坐标的元素*/
	    var s1=[];/*保存XY坐标*/
	    s1[0]=0;
	    s1[1]=0;
	    var i = 1;
	    //累计元素每级offsetParent属性元素的偏移量
	    while ((node = node.offsetParent)) {
	     if(i>2)
	    		break;
	     s1[0]  = s1[0]+node.offsetLeft;
	     s1[1]  = s1[1]+node.offsetTop;
	      i++;
	    }
	    return s1;
	}
});


