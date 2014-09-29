/**
 * 应用的工具类，封装了全局的操作方法
 * 
 * @author yongtree
 */
Ext.ns("App");
Ext.ns("AppUtil");
var jsCache = new Array();
/**
 * xml字符串转化为dom对象
 * 
 * @param {}
 *            xmlStr
 * @return {}
 */
function strToDom(xmlStr) {
	if (window.ActiveXObject) {
		var dom = new ActiveXObject("Microsoft.XMLDOM");
		dom.async = "false";
		dom.loadXML(xmlStr);
		return dom;
	} else {
		if (document.implementation && document.implementation.createDocument) {
			var dp = new DOMParser();
			var dom = dp.parseFromString(xmlStr, "text/xml");
			return dom;
		}
	}
}
/**
 * 将一个view的名称实例化一个对象，类似于java的Class.forName(clzName)
 * 
 * @param {}
 *            viewName 视图名称
 * @param {}
 *            params 构造函数的参数
 * @return {} view对应的对象
 */
function newView(viewName, params) {
	var str = "new " + viewName;
	if (params != null) {
		str += "(params);";
	} else {
		str += "();";
	}
	return eval(str);
}

function $ImportJs(viewName, callback,params) {
		var scope = viewName.replace(/\./g, "_");
	var b = document.getElementById(scope+ '-hiden');
	if (b != null) {
		var view = eval('new ' + viewName + '('+params+')');
		callback.call(this, view);
	} else {
		var jsArr = eval('App.importJs.' + scope);
		if (jsArr == undefined) {
			var view = eval('new ' + viewName + '('+params+')');
			if(arguments.length>2 && !Ext.isEmpty(arguments[2]) && Ext.isObject(arguments[2])){
				for(var obj in arguments[2]){
					view[obj]=arguments[2][obj];
				}
			}
			callback.call(this, view);
			return;
		}
		ScriptMgr.load({
					scripts : jsArr,
					callback : function() {

						Ext.DomHelper
								.append(
										document.body,
										"<div id='"
												+ scope
												+ "-hiden' style='display:none'></div>");
						var view = eval('new ' + viewName + '('+params+')');
						callback.call(this, view);
					}
				});
	}
}


function $parseDate(b) {
	if (typeof b == "string") {
		var a = b.match(/^ *(\d{4})-(\d{1,2})-(\d{1,2}) *$/);
		if (a && a.length > 3) {
			return new Date(parseInt(a[1]), parseInt(a[2]) - 1, parseInt(a[3]));
		}
		a = b
				.match(/^ *(\d{4})-(\d{1,2})-(\d{1,2}) +(\d{1,2}):(\d{1,2}):(\d{1,2}) *$/);
		if (a && a.length > 6) {
			return new Date(parseInt(a[1]), parseInt(a[2]) - 1, parseInt(a[3]),
					parseInt(a[4]), parseInt(a[5]), parseInt(a[6]));
		}
		a = b
				.match(/^ *(\d{4})-(\d{1,2})-(\d{1,2}) +(\d{1,2}):(\d{1,2}):(\d{1,2})\.(\d{1,9}) *$/);
		if (a && a.length > 7) {
			return new Date(parseInt(a[1]), parseInt(a[2]) - 1, parseInt(a[3]),
					parseInt(a[4]), parseInt(a[5]), parseInt(a[6]),
					parseInt(a[7]));
		}
	}
	return null;
}
function $formatDate(b) {
	if (typeof b == "string") {
		b = parseDate(b);
	}
	if (b instanceof Date) {
		var k = b.getFullYear();
		var a = b.getMonth() + 1;
		var j = b.getDate();
		var g = b.getHours();
		var e = b.getMinutes();
		var f = b.getSeconds();
		var c = b.getMilliseconds();
		if (a < 10) {
			a = "0" + a;
		}
		if (j < 10) {
			j = "0" + j;
		}
		if (g < 10) {
			g = "0" + g;
		}
		if (e < 10) {
			e = "0" + e;
		}
		if (f < 10) {
			f = "0" + f;
		}
		if (c > 0) {
			return k + "-" + a + "-" + j + " " + g + ":" + e + ":" + f + "."
					+ c;
		}
		if (g > 0 || e > 0 || f > 0) {
			return k + "-" + a + "-" + j + " " + g + ":" + e + ":" + f;
		}
		return k + "-" + a + "-" + j;
	}
	return "";
}




function $convertTableToMap(n) {
	if (n.rows.length != 2) {
		return [];
	}
	var h = [];
	var l = n.rows[0];
	var k = n.rows[1];
	for (var f = 0; f < l.cells.length; f++) {
		var b = {};
		var m = k.cells[f];
		var e;
		for (var d = 0; d < m.childNodes.length; d++) {
			if (m.childNodes[d].getAttribute
					&& m.childNodes[d].getAttribute("name")) {
				e = m.childNodes[d];
				break;
			}
		}
		var a = e.getAttribute("name");
		var g = l.cells[f].innerHTML;
		var c = e.getAttribute("xtype");
		b.name = a;
		b.header = g;
		b.xtype = c;
		h.push(b);
	}
	return h;
}
App.getContentPanel = function() {
	var a = Ext.getCmp("centerTabPanel");
	return a;
};

function uniqueArray(e) {
	e = e || [];
	var b = {};
	for (var d = 0; d < e.length; d++) {
		var c = e[d];
		if (typeof(b[c]) == "undefined") {
			b[c] = 1;
		}
	}
	e.length = 0;
	for (var d in b) {
		e[e.length] = d;
	}
	return e;
}
/* This function is used to set cookies */
function setCookie(name,value,expires,path,domain,secure) {
  document.cookie = name + "=" + escape (value) +
    ((expires) ? "; expires=" + expires.toGMTString() : "") +
    ((path) ? "; path=" + path : "") +
    ((domain) ? "; domain=" + domain : "") + ((secure) ? "; secure" : "");
}

/*This function is used to get cookies */
function getCookie(name) {
	var prefix = name + "=" 
	var start = document.cookie.indexOf(prefix); 
	
	if (start==-1) {
		return null;
	}
	
	var end = document.cookie.indexOf(";", start+prefix.length);
	if (end==-1) {
		end=document.cookie.length;
	}

	var value=document.cookie.substring(start+prefix.length, end) ;
	return unescape(value);
}

/* This function is used to delete cookies */
function deleteCookie(name,path,domain) {
  if (getCookie(name)) {
    document.cookie = name + "=" +
      ((path) ? "; path=" + path : "") +
      ((domain) ? "; domain=" + domain : "") +
      "; expires=Thu, 01-Jan-70 00:00:01 GMT";
  }
}


String.prototype.trim = function() {
	return (this.replace(/^[\s\xA0]+/, "").replace(/[\s\xA0]+$/, ""));
};

/**
 * 封装请求
 * @param {} config
 */
function $request(config){
		Ext.Ajax.request({
			url:config.url,
			params:config.params,
			method:config.method==null?'POST':config.method,
			success:function(response,options){
				if(config.success!=null){
					config.success.call(this,response,options);
				}
			},
			failure:function(response,options){
				Ext.MessageBox.show({
					title : '操作信息',
					msg : '操作出错，请联系管理员！',
					buttons : Ext.MessageBox.OK,
					icon : Ext.MessageBox.ERROR
				});
				if(config.success!=null){
					config.failure.call(this,response,options);
				}
			}
		});
}

function asynReq() {
	var a = Ext.Ajax.getConnectionObject().conn;
	a.open("GET", url, false);
	a.send(null);
}
AppUtil.addPrintExport = function(a) {
};
AppUtil.removeTab = function(a) {
	var b = App.getContentPanel();
	var c = b.getItem(a);
	if (c != null) {
		b.remove(c, true);
	}
};
AppUtil.activateTab = function(a) {
	var b = App.getContentPanel();
	b.activate(a);
};
function $converDetail(d, k) {
	var c = this.formPanel.getForm().getEl().dom;
	var g = c.getElementsByTagName("table");
	for (var p = 0; p < g.length; p++) {
		var t = g[p].getAttribute("isdetail");
		if (t != null) {
			var j = g[p].parentNode;
			var u = $convertTableToMap(g[p]);
			var n = [];
			var b = [];
			if (this.taskId) {
				var r = document.getElementById("subPkName_" + this.taskId);
				var v = r.value;
				n.push(v);
				b.push({
							dataIndex : v,
							header : v,
							hidden : true
						});
			}
			var o = 0;
			for (var q = 0; q < u.length; q++) {
				var s = null;
				if (k && k[u[q].name]) {
					s = k[u[q].name];
				}
				if (s != 0) {
					n.push(u[q].name);
					if (s == 1) {
						o--;
						b.push({
									dataIndex : u[q].name,
									header : u[q].header
								});
					} else {
						o++;
						b.push({
									dataIndex : u[q].name,
									header : u[q].header,
									editor : new Ext.form.TextField()
								});
					}
				} else {
					o--;
				}
			}
			var m = document.createElement("div");
			j.appendChild(m);
			this.detailPanel = new HT.EditorGridPanel({
				renderTo : m,
				tbar : new Ext.Toolbar({
					hidden : u.length == o ? false : true,
					frame : true,
					items : [{
								text : "添加记录",
								iconCls : "btn-add",
								scope : this,
								handler : function() {
									var f = this.detailPanel.getStore().recordType;
									this.detailPanel.getStore().add(new f());
								}
							}, {
								text : "删除记录",
								iconCls : "btn-del",
								scope : this,
								handler : function() {
									var f = this.detailPanel.getStore();
									var x = this.detailPanel
											.getSelectionModel()
											.getSelections();
									var z = "";
									for (var y = 0; y < x.length; y++) {
										if (z != "") {
											z += ",";
										}
										if (x[y].data.detailId != null) {
											z += x[y].data.detailId;
										}
										f.remove(x[y]);
									}
								}
							}]
				}),
				clicksToEdit : 1,
				width : g[p].offsetWidth,
				showPaging : false,
				autoHeight : true,
				fields : n,
				columns : b
			});
			var h = document.getElementById("subSetVarName_" + this.taskId);
			if (d && h) {
				var w = d[h.value];
				if (w) {
					this.detailPanel.getStore().loadData({
								result : w
							});
				}
			}
			j.removeChild(g[p]);
			break;
		}
	}
	var a = c.elements || (document.forms[c] || Ext.getDom(c)).elements;
	var e = this.formPanel;
	var u = new Ext.util.MixedCollection();
	var l = new Array();
	Ext.each(a, function(i, z) {
		var P, x, J, y, G;
		var N = null;
		if (!i) {
			return;
		}
		P = i.name;
		x = i.type;
		if (k && k[P]) {
			N = k[P];
		}
		if (x == "button" || x == "hidden") {
			return;
		}
		y = i.getAttribute("xtype");
		if (d && d[P]) {
			i.value = d[P];
		}
		var C = i.parentNode;
		var I = i.getAttribute("width");
		if (!I) {
			I = C.offsetWidth;
		}
		if (I < 300 && C.offsetWidth > 300) {
			I = 300;
		}
		if (N && (N == 0 || N == 1)
				&& (y != "officeeditor" || (y == "officeeditor" && N == 0))) {
			i.setAttribute("style", "display:none;");
			var H = document.createElement("p");
			H.setAttribute("style", "width:" + I + "px;");
			H.innerHTML = N == 0 ? '<font color="red">无权限</font>' : i.value
					? i.value
					: "";
			C.appendChild(H);
			return;
		}
		if (y == "datefield") {
			var L = i.getAttribute("dateformat");
			var K = document.createElement("span");
			try {
				C.replaceChild(K, i);
			} catch (M) {
				alert(P + "   error !!");
			}
			var F = document.createElement("div");
			K.appendChild(F);
			var D;
			if (L == "yyyy-MM-dd HH:mm:ss") {
				D = new Cls.form.DateTimeField({
							name : P,
							renderTo : F,
							width : 200,
							format : "Y-m-d H:i:s",
							value : new Date(),
							allowBlank : false
						});
			} else {
				D = new Ext.form.DateField({
							name : P,
							renderTo : F,
							height : 21,
							width : 100,
							format : "Y-m-d",
							value : new Date(),
							allowBlank : false
						});
			}
			if (i.value) {
				D.setValue($parseDate(i.value));
			}
		} else {
			if (y == "diccombo") {
				var E = i.getAttribute("txtitemname");
				var B = i.getAttribute("txtisnotnull");
				var K = document.createElement("span");
				try {
					C.replaceChild(K, i);
				} catch (M) {
					alert(P + "   error !!");
				}
				var F = document.createElement("div");
				K.appendChild(F);
				var D = new DicCombo({
							name : P,
							itemName : E,
							renderTo : F,
							width : I,
							allowBlank : B == 1 ? false : true
						});
				if (i.value) {
					D.setValue($parseDate(i.value));
				}
			} else {
				if (y == "fckeditor") {
					G = C.offsetHeight;
					var K = document.createElement("span");
					try {
						C.replaceChild(K, i);
					} catch (M) {
						alert(P + "   error !!");
					}
					var F = document.createElement("div");
					K.appendChild(F);
					var D = new Ext.ux.form.FCKeditor({
								name : P,
								renderTo : F,
								height : G,
								allowBlank : false
							});
					if (i.value) {
						D.setValue(i.value);
					}
					e.add(D);
				} else {
					if (y == "officeeditor") {
						var K = document.createElement("span");
						G = C.offsetHeight;
						this.hiddenF = new Ext.form.Hidden({
									name : P
								});
						this.hiddenF.render(K);
						try {
							C.replaceChild(K, i);
						} catch (M) {
							alert(P + "   error !!");
						}
						Ext.useShims = true;
						var D = new NtkOfficePanel({
									showToolbar : N == 1 ? false : true,
									width : I,
									height : G,
									doctype : "doc",
									unshowMenuBar : false
								});
						if (N == 1) {
							D.setReadOnly();
						}
						D.panel.render(K);
						if (i.value) {
							this.hiddenF.setValue(i.value);
							D.openDoc(i.value);
							this.fileId = i.value;
						}
						this.officePanel = D;
						e.add(D);
					} else {
						if (y == "userselector") {
							var K = document.createElement("span");
							var f = new Ext.form.Hidden({
										name : P + "ids"
									});
							f.render(K);
							try {
								C.replaceChild(K, i);
							} catch (M) {
								alert(P + "   error !!");
							}
							var A = i.getAttribute("issingle");
							var O = new Ext.form.TextField({
										name : P,
										height : 21,
										width : I
												? (I - 90 > 0 ? I - 90 : I)
												: I
									});
							if (A == 0) {
								O = new Ext.form.TextArea({
											name : P,
											width : I ? (I - 90 > 0
													? I - 90
													: I) : I
										});
							}
							var D = new Ext.form.CompositeField({
										width : I,
										items : [O, {
											xtype : "button",
											width : 78,
											border : false,
											text : "选择人员",
											iconCls : "btn-user",
											handler : function() {
												UserSelector.getView(
														function(Q, R) {
															O.setValue(R);
														},
														A == 1 ? true : false)
														.show();
											}
										}]
									});
							l.push("userselector" + z);
							u.add("userselector" + z, K);
							u.add("userselector" + z + "-cmp", D);
							if (i.value) {
								O.setValue(i.value);
							}
						} else {
							if (y == "depselector") {
								var K = document.createElement("span");
								var f = new Ext.form.Hidden({
											name : P + "ids"
										});
								f.render(K);
								try {
									C.replaceChild(K, i);
								} catch (M) {
									alert(P + "   error !!");
								}
								var A = i.getAttribute("issingle");
								var O = new Ext.form.TextField({
											name : P,
											height : 21,
											width : I ? (I - 90 > 0
													? I - 90
													: I) : I
										});
								if (A == 0) {
									O = new Ext.form.TextArea({
												name : P,
												width : I ? (I - 90 > 0 ? I
														- 90 : I) : I
											});
								}
								var D = new Ext.form.CompositeField({
											width : I,
											items : [O, {
												xtype : "button",
												border : false,
												width : 78,
												text : "选择部门",
												iconCls : "btn-dep",
												handler : function() {
													DepSelector.getView(
															function(Q, R) {
																O.setValue(R);
															},
															A == 1
																	? true
																	: false)
															.show();
												}
											}]
										});
								l.push("depselector" + z);
								u.add("depselector" + z, K);
								u.add("depselector" + z + "-cmp", D);
								if (i.value) {
									O.setValue(i.value);
								}
							} else {
								if (y == "fileattach") {
									var K = document.createElement("span");
									var f = new Ext.form.Hidden({
												name : P
											});
									f.render(K);
									try {
										C.replaceChild(K, i);
									} catch (M) {
										alert(P + "   error !!");
									}
									var O = new Ext.Panel({
												width : I ? (I - 90 > 0 ? I
														- 90 : I) : I,
												height : 60,
												autoScroll : true,
												html : ""
											});
									var D = new Ext.form.CompositeField({
										width : I,
										items : [O, {
											xtype : "button",
											width : 78,
											text : "选择附件",
											iconCls : "menu-attachment",
											handler : function() {
												var Q = App.createUploadDialog(
														{
															file_cat : "flow",
															callback : function(
																	S) {
																for (var R = 0; R < S.length; R++) {
																	if (f
																			.getValue() != "") {
																		f
																				.setValue(f
																						.getValue()
																						+ ",");
																	}
																	f
																			.setValue(f
																					.getValue()
																					+ S[R].fileId
																					+ "|"
																					+ S[R].filename);
																	Ext.DomHelper
																			.append(
																					O.body,
																					'<span><a href="#" onclick="FileAttachDetail.show('
																							+ S[R].fileId
																							+ ')">'
																							+ S[R].filename
																							+ '</a> <img class="img-delete" src="'
																							+ __ctxPath
																							+ '/images/system/delete.gif" onclick="AppUtil.removeFile(this,'
																							+ S[R].fileId
																							+ ')"/>&nbsp;|&nbsp;</span>');
																}
															}
														});
												Q.show(this);
											}
										}]
									});
									l.push("fileattach" + z);
									u.add("fileattach" + z, K);
									u.add("fileattach" + z + "-cmp", D);
									AppUtil.removeFile = function(U, Q, V) {
										var R = f;
										var T = R.getValue();
										if (T.indexOf(",") < 0) {
											R.setValue("");
										} else {
											T = T
													.replace("," + Q + "|" + V,
															"").replace(
															Q + "|" + V + ",",
															"");
											R.setValue(T);
										}
										var S = Ext.get(U.parentNode);
										S.remove();
									};
									D.on("render", function() {
										if (i.value) {
											f.setValue(i.value);
											var S = i.value.split(",");
											for (var U = 0; U < S.length; U++) {
												var T = S[U];
												var R = T.split("|");
												var Q = R[0];
												var V = R[1];
												Ext.DomHelper
														.append(
																O.body,
																'<span><a href="#" onclick="FileAttachDetail.show('
																		+ Q
																		+ ')">'
																		+ V
																		+ '</a> <img class="img-delete" src="'
																		+ __ctxPath
																		+ '/images/system/delete.gif" onclick="AppUtil.removeFile(this,'
																		+ Q
																		+ ",'"
																		+ V
																		+ "')\"/>&nbsp;|&nbsp;</span>");
											}
										}
									}, this);
								}
							}
						}
					}
				}
			}
		}
	}, this);
	if (l.length > 0 && u.length > 0) {
		Ext.each(l, function(f) {
					var x = u.get(f + "-cmp");
					var i = u.get(f);
					x.render(i);
				});
	}
}