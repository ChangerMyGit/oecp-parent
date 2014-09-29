Ext.ns("OECP.ui");
/**
 * 下拉列表选择树 <br>
 * 依赖EXTJS3版本
 * 
 * @class OECP.ui.ComboBoxTree
 * @extends Ext.form.ComboBox
 * @author yongtree
 */
OECP.ui.TreeComboBox = Ext.extend(Ext.form.ComboBox, {
	editable : this.editable || false,
	mode : 'local',
	fieldLabel : this.fieldLabel || "",
	emptyText : this.emptyText || "请选择",
	allowBlank : this.allowBlank || true,
	blankText : this.blankText || "必须输入!",
	triggerAction : 'all',
	width : 160,
	displayField : 'text',
	valueField : 'id',
	selectOnFocus : true,
	/**
	 * 根的名字
	 * 
	 * @type String
	 */
	rootText : this.rootText,
	rootId : this.rootId || "_oecp",
	/**
	 * 树的请求地址
	 * 
	 * @type String
	 */
	treeUrl : this.treeUrl,
	rootVisible : this.rootVisible || true,
	tree : null,
	//树节点点击事件
    nodeClick : null,
	
	onTriggerClick : function() {
				if (this.menu) {
					this.menu.show(this.el, "tl-bl?");
					this.tree.expandAll();
				}
			},
	
	setValueAndText : function(v, t) {
		var RecordType = Ext.data.Record.create([{
					name : this.valueField
				}, {
					name : this.displayField
				}]);
		var data = {};
		data[this.valueField] = v;
		data[this.displayField] = t;
		var record = new RecordType(data);
		this.getStore().insert(0, record);
		this.setValue(v);
	},	
			
	initComponent : function() {
		var c = this;
		this.store = new Ext.data.SimpleStore({
			fields : [this.valueField,this.displayField],
			data : [[]]
		}),
		OECP.ui.TreeComboBox.superclass.initComponent.call(this);
		
		if(!this.tree){
			this.tree = new Ext.tree.TreePanel({
				height : 160,
				width : 160,
				scope : c,
				autoScroll : true,
				split : true,
				root : new Ext.tree.AsyncTreeNode({
							expanded : true,
							id : c.rootId,
							text : c.rootText
						}),

				loader : new Ext.tree.TreeLoader({
							url : c.treeUrl,
							createNode : function(attr){
								if(c.treeIcon){
									attr.icon = c.treeIcon;
								}
								
						        if(this.baseAttrs){
						            Ext.applyIf(attr, this.baseAttrs);
						        }
						        if(this.applyLoader !== false && !attr.loader){
						            attr.loader = this;
						        }
						        if(Ext.isString(attr.uiProvider)){
						           attr.uiProvider = this.uiProviders[attr.uiProvider] || eval(attr.uiProvider);
						        }
						        if(attr.nodeType){
						            return new Ext.tree.TreePanel.nodeTypes[attr.nodeType](attr);
						        }else{
						            return attr.leaf ?
						                        new Ext.tree.TreeNode(attr) :
						                        new Ext.tree.AsyncTreeNode(attr);
						        }
						    }
							
						}),
				rootVisible : c.rootVisible
			});
		}
				
		if (!this.menu) {
					this.menu = new Ext.menu.Menu({
								items : [new Ext.menu.Adapter(c.tree)]
							});
				}
				
		if(!c.nodeClick){
			c.nodeClick = function(node) {
						if (node.id != null && node.id != '') {
							if (node.id != '_oecp') {
								c.setValueAndText(node.id, node.text);
								c.menu.hide();
							} else {
								Ext.Msg.alert("提示", "此节点无效，请重新选择!");
							}
						}
					};
		}
		c.tree.on('click', c.nodeClick);		
	}
});