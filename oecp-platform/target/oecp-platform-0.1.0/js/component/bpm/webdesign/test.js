[{
states:{
		rect1:{
			type:'start',text:{text:'开始'}, attr:{ x:523, y:38, width:50, height:50}, props:{text:{value:'开始'},temp1:{value:''},temp2:{value:''}}
		},
		rect8:{
			type:'end',text:{text:'结束'}, attr:{ x:518, y:414, width:50, height:50}, props:{text:{value:'结束'},temp1:{value:''},temp2:{value:''}}
		},
		rect20:{
			type:'task',text:{text:'任务'}, attr:{ x:493, y:227, width:100, height:50}, props:{text:{value:'任务'},assignee:{value:''},form:{value:''},desc:{value:''}}
		}
},
paths:{
		path21:{
			from:'rect1',to:'rect20', dots:[],text:{text:'TO 任务'},textPos:{x:0,y:-10}, props:{text:{value:''}}
		},
		path22:{
			from:'rect20',to:'rect8', dots:[],text:{text:'TO 结束'},textPos:{x:0,y:-10}, props:{text:{value:''}}
		}
},
props:{
		props:{name:{value:'新建流程'},key:{value:''},desc:{value:''}}
}
}]