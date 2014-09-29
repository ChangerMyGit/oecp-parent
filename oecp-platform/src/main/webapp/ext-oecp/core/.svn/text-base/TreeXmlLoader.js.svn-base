Ext.ns("OECP.core");
OECP.core.TreeXmlLoader = Ext.extend(Ext.tree.TreeLoader, {
			load : function(node, callback) {
				if (this.clearOnLoad) {
					while (node.firstChild) {
						node.removeChild(node.firstChild);
					}
				}
				if (this.doPreload(node)) {
					if (typeof callback == "function") {
						callback();
					}
				} else {
					this.loadXml(node, callback);
				}
			},
			doPreload : function(node) {
				if (node.attributes.children) {
					if (node.childNodes.length < 1) {
						var children = node.attributes.children;
						node.beginUpdate();
						for (var b = 0, a = children.length; b < a; b++) {
							var e = node.appendChild(this.createNode(children[b]));
							if (this.preloadChildren) {
								this.doPreload(e);
							}
						}
						node.endUpdate();
					}
					return true;
				} else {
					return false;
				}
			},
			loadXml : function(node, callback) {
				var d = node.attributes.xmlNode;
				if (d && ((d.nodeType == 1) || (d.nodeType == 9))) {
					childNodes = d.childNodes, l = d.childNodes.length;
					for (var a = 0; a < l; a++) {
						var e = d.childNodes[a];
						if (e.nodeType == 1) {
							node.appendChild(this.createNode({
										id : e.getAttribute("id"),
										//iconCls : e.getAttribute("iconCls"),
										text : e.getAttribute("text"),
										iframe : e.getAttribute("iframe"),
										iframeUrl:e.getAttribute("iframeUrl"),
										xmlNode : e,
										expanded : true,
										leaf : ((e.childNodes.length) == 0)
									}));
						} else {
							if ((e.nodeType == 3)
									&& (e.data.trim().length != 0)) {
								node.appendChild(this.createNode({
											expanded : true,
											text : e.data,
											leaf : true
										}));
							}
						}
					}
				}
				callback(this, node);
			},
			createNode : function(attr) {
				if (this.baseAttrs) {
					Ext.applyIf(attr, this.baseAttrs);
				}
				if (this.applyLoader !== false) {
					attr.loader = this;
				}
				if (typeof attr.uiProvider == "string") {
					attr.uiProvider = this.uiProviders[attr.uiProvider]
							|| eval(attr.uiProvider);
				}
				return (attr.leaf
						? new Ext.tree.TreeNode(attr)
						: new Ext.tree.AsyncTreeNode(attr));
			}
		});