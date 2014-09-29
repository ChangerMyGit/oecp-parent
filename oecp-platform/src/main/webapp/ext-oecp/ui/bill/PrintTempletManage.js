Ext.ns("OECP.ui");
/**
 *  打印模板工具类 
 *  遍历Ext容器，对容器内的空间和表格输出成打印模板
 *  
 *   例:
 <code>
     var store = new Ext.data.SimpleStore({
        fields: [
           {name: 'company'},
           {name: 'price', type: 'float'},
           {name: 'change', type: 'float'},
           {name: 'pctChange', type: 'float'},
           {name: 'lastChange', type: 'date', dateFormat: 'n/j h:ia'}
        ]
    });
     var grid = new Ext.grid.GridPanel({
        store: store,
        columns: [
            {id:'company',header: "Company", width: 160, sortable: true, dataIndex: 'company'},
            {header: "Price", width: 75, sortable: true, renderer: 'usMoney', dataIndex: 'price'},
            {header: "Change", width: 75, sortable: true, renderer: change, dataIndex: 'change'},
            {header: "% Change", width: 75, sortable: true, renderer: pctChange, dataIndex: 'pctChange'},
            {header: "Last Updated", width: 85, sortable: true, renderer: Ext.util.Format.dateRenderer('m/d/Y'), dataIndex: 'lastChange'}
        ],
        stripeRows: true,
        autoExpandColumn: 'company',
        height:350,
        width:600,
    });
    
    grid.render('grid-example');
    
    var _printTemplate = OECP.ui.PrintTempletManage.getTemplate(grid,['client']);
    //追加调用打印设计函数
	_printTemplate = _printTemplate.concat("LODOP.PRINT_DESIGN();");
	//临时数据
	var _json = {client:[
		{company:'a',price:45.2,change:'hi',pctChange:'test',lastChange:'2012-10-31'},
		{company:'b',price:45.2,change:'xxxxi',pctChange:'ggd',lastChange:'2011-12-01'}
	]};
	//获取打印控件
	LODOP=getLodop(document.getElementById('LODOP_OB'),document.getElementById('LODOP_EM')); 
	var _template =new Ext.XTemplate(_printTemplate);
	var _printTemplate=_template.applyTemplate(_json);
	eval(_printTemplate);
    
 </code>
 *   
 */
OECP.ui.PrintTempletManage = {};
OECP.ui.PrintTempletManage = (function(){
	var com = {}, templet = '', me = this, clientVarNames = [], lastOptions = NaN;
	var ADD_PRINT_TEXT = "LODOP.ADD_PRINT_TEXT",ADD_PRINT_TABLE = "LODOP.ADD_PRINT_TABLE";
	var TABLE_STYLE = "<style>table {border-collapse:collapse;}td,th{border-left:1px solid #000000;border-top:1px solid #000000;border-right:1px solid #000000;border-bottom:1px solid #000000;}</style>";
	var Return = {
		getTemplate : function(component, names,gridHeights){//获取打印模板
			lastOptions = NaN;//清空内部变量数据，防止脏数据
			com = component;	//初始化 复制控件
			clientVarNames = names;//初始化 json数据中的子表数据标识
			clientGridHeights=gridHeights;//行数
			templet='';//模板
			var foreachItems = function(obj){//递归遍历items
				if(Ext.isObject(obj)){
					templet = templet.concat(formatTemplet(obj));
					if(obj.items){
						foreachItems(obj.items);
					}
				}else if(Ext.isArray(obj)){
					for(var i=0; i < obj.length; i++){
						foreachItems(obj[i]);
					}
				}
			}
			foreachItems(com);
			return templet;
		}
	};
	var getItemLabel = function(obj){//获取标题
		var val = '';
		if(obj instanceof Ext.form.Label)
			val = obj.el.dom.innerHTML();
		else if(obj instanceof Ext.form.Field)//XXX form布局里的空间fieldLabel才有效。这里无法上级是不是form布局
			val = obj.fieldLabel;
		return val;
	};
	var formatTemplet = function(obj) {
		var _t = '', _form = Ext.form,lastheight=0;
		if (Ext.isObject(obj) && obj instanceof Ext.BoxComponent) {
			var _dom = obj.el.dom;
			var _xy = obj.getPosition();// 控件坐标
			var _height = _dom.scrollHeight, _width = _dom.clientWidth;//控件高度、宽度
			if(obj instanceof _form.Label && !obj.hidden){//判断是标签控件
				_t = _t.concat(String.format(ADD_PRINT_TEXT+'("{0}","{1}","{2}","{3}","{4}");', _xy[0], _xy[1], _width, _height, obj.text));
			}else if (obj instanceof _form.Field && !obj.hidden) {// 判断是录入字段
				var _title = getItemLabel(obj) || '';
				var _labelWidth = _dom.offsetLeft;//标题宽度
				var _labelTop = _xy[0], _labelLeft = _xy[1] - _labelWidth;//标题坐标
				if(!Ext.isEmpty(_title,false)){
					_t = _t.concat(String.format(ADD_PRINT_TEXT+'("{0}","{1}","{2}","{3}","{4}");', _labelTop, _labelLeft, _labelWidth, _height, _title));//拼装标题
				}
				var _objname = obj.name;
				if(obj instanceof _form.ComboBox && _objname.indexOf(".")!=-1){//判断是参选框，将obj.id替换成横obj.name，规避显示主键的问题
						_objname = _objname.substring(0,_objname.lastIndexOf("."))+".name";
				}
				_t = _t.concat(String.format(ADD_PRINT_TEXT+'("{0}","{1}","{2}","{3}","{{4}}");', _xy[1], _xy[2], _width, _height, _objname));
				lastheight = _xy[0]+_height;
			} else if (obj instanceof Ext.grid.GridPanel && !obj.hidden) {//判断是表格
				var _cm = obj.getColumnModel(),gridHeight=obj.el.dom.clientHeight,gridWidth=obj.el.dom.clientWidth;
				var _tmp1 = '<thead><tr>',_varName=(isNaN(lastOptions) ? clientVarNames[0] : clientVarNames[(lastOptions)]);
				var _tmp2 = '<tpl for="' + _varName + '"><tr>';
				lastOptions = (isNaN(lastOptions) ? 0 : lastOptions) + 1;//记录本次选择的clientVarNames下标
				gridHeight=clientGridHeights[_varName]?(clientGridHeights[_varName]*23+23):gridHeight;
				_t = String.format(ADD_PRINT_TABLE+'("{0}","{1}","{2}","{3}",', _xy[1],_xy[0]+(lastOptions*10),gridWidth+10,gridHeight) + '"'+TABLE_STYLE+'<table width=' + obj.el.dom.clientWidth + '>';
				for (var i = 0; i < _cm.config.length; i++) {
					if (_cm.config[i].header !== '' &&  !_cm.config[i].hidden) {
						_cwidth = _cm.config[i].width || _cm.defaults.width;//列宽
						_tmp1 = _tmp1.concat("<th width=" + _cwidth + ">"+_cm.config[i].header+"</th>");
						_tmp2 = _tmp2.concat("<td width=" + _cwidth + ">{"+_cm.config[i].dataIndex+"}</td>");
					}
				}
				_tmp1 = _tmp1.concat('</tr></thead>'),	_tmp2 = _tmp2.concat('</tr></tpl>');
				_t = _t.concat(_tmp1).concat(_tmp2).concat('</table>");');
			}
		} 
		return _t;
	}
	return Return;
})();