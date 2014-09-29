Ext.ns("OECP.core.util");
/**
 * 克隆一个对象
 * @param {} o
 * @return {}
 */
OECP.core.util.clone = function(o) {
    if(!o || 'object' !== typeof o) {
        return o;
    }
    if('function' === typeof o.clone) {
        return o.clone();
    }
    var c = '[object Array]' === Object.prototype.toString.call(o) ? [] : {};
    var p, v;
    for(p in o) {
        if(o.hasOwnProperty(p)) {
            v = o[p];
            if(v && 'object' === typeof v) {
                c[p] = OECP.core.util.clone(v);
            }
            else {
                c[p] = v;
            }
        }
    }
    return c;
}; // eo function clone  
//除法
OECP.core.util.accDiv =function(arg1,arg2){ 
	return OECP.core.util.accMul(arg1,1/arg2);
}
//乘法
OECP.core.util.accMul=function(arg1,arg2){ 
	var m=0,s1=arg1.toString(),s2=arg2.toString(); 
	try{m+=s1.split(".")[1].length}catch(e){} 
	try{m+=s2.split(".")[1].length}catch(e){} 
	return Number(s1.replace(".",""))*Number(s2.replace(".",""))/Math.pow(10,m) 
}
//加法
OECP.core.util.accAdd=function(arg1,arg2){ 
	var r1,r2,m;
	try{r1=arg1.toString().split(".")[1].length}catch(e){r1=0}
	try{r2=arg2.toString().split(".")[1].length}catch(e){r2=0}
	m=Math.pow(10,Math.max(r1,r2));
	return (OECP.core.util.accMul(arg1,m)+OECP.core.util.accMul(arg2,m))/m; 
}
//减法
OECP.core.util.accSub=function(arg1,arg2){
	return OECP.core.util.accAdd(arg1,-arg2);
}
