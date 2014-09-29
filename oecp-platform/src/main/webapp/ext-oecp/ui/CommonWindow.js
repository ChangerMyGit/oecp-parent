/** 普通窗口，里面放入一些基本的组件：grid\panel等等
 * @author yangtao
 * @class OECP.ui.CommonWindow
 * @extends Ext.Window
 */
Ext.ns('OECP.ui');
OECP.ui.CommonWindow = Ext.extend(Ext.Window, {
	//组件ID
//	id : Ext.id(),
	//边框
	border : true,
	//关闭
	closable : true,
	//点击关闭时，是隐藏窗口
//	closeAction : 'hide',
	//滚动条
	autoScroll : true,
	//后面的一切内容进行遮罩
	modal : true,
	//用户可以改变window的大小
	resizable : true,
	//窗口里面按钮的位置
	buttonAlign : "center",
	//窗口里面的按钮
	buttonArray :null,
	//窗口里面的组件
	componetArray :null,
	//初始化方法
	initComponent : function() {
		var scope = this;
		scope.items = scope.componetArray;
		//如果该窗口有按钮的话，默认有一个关闭该窗口的按钮
//		if(scope.buttonArray!=null){
//			var closeButton = new OECP.ui.Button({
//				text:'关闭',
//				handler:function(){
//					scope[scope.closeAction]();
//				}});
//			scope.buttonArray.push(closeButton);
//		}
			
		scope.buttons = scope.buttonArray;
		OECP.ui.CommonWindow.superclass.initComponent.call(this);
	}	
});
Ext.reg('oecpCommonWindow', OECP.ui.CommonWindow);