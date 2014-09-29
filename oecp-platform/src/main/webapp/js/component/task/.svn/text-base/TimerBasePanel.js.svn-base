Ext.ns("OECP")
/**
 * 定时器面板
 * @class OECP.TimerBasePanel
 * @extends Ext.Panel
 */
OECP.TimerBasePanel = Ext.extend(Ext.Panel, {
	/**校验cron表达式**/
	validateCronExpUrl : __ctxPath + '/task/manage/validateCronExp.do',
	/**校验cron表达式的结果标志**/
	validateCronExpFlag : null,
	/**开始日期和结束日期大小比较标志**/
	dateValidateFlag : null,
	/**
	 * 初始化方法
	 */
	initComponent : function(){
		var me = this;
		me.validateCronExpFlag = true;
		me.dateValidateFlag = true;
		me.initMonth(me);
		me.initDay(me);
		me.initWeek(me);
		me.initHour(me);
		me.initMinute(me);
		me.initSecond(me);
		me.initTimerFormPanel(me);
	 	me.selectCircleType(true,me);
	 	this.items = [this.TimerFormPanel];   
	 	OECP.TimerBasePanel.superclass.initComponent.call(this);
	},
	/**
	 * 初始化月
	 * @param {} me
	 */
	initMonth : function(me){
		var codes =[['*','每月'], 
					['1','1'], 
					['2','2'], 
					['3','3'], 
					['4','4'], 
					['5','5'], 
					['6','6'], 
					['7','7'], 
					['8','8'], 
					['9','9'], 
					['10','10'], 
					['11','11'], 
					['12','12']];
		var store = new Ext.data.SimpleStore({
		    fields: ['id', 'name'],
		    data : codes
		});
		me.monthCombo = new Ext.form.ComboBox({
			hiddenName:me.timerMonthField,
			name: me.timerMonthField,
			fieldLabel: '月',
		    store: store,
		    width:90,
		    displayField:'name',
		    valueField:'id',
		    typeAhead: true,
		    mode: 'local',
		    triggerAction: 'all',
		    emptyText:'请选择月份',
		    selectOnFocus:true
		});
	},
	/**
	 * 初始化天
	 * @param {} me
	 */
	initDay : function(me){
		var codes =[['*','每天'], 
					['1','1'], ['2','2'], ['3','3'], ['4','4'], ['5','5'], ['6','6'], 
					['7','7'], ['8','8'], ['9','9'], ['10','10'], ['11','11'], ['12','12'],
					['13','13'], ['14','14'], ['15','15'], ['16','16'], ['17','17'], ['18','18'],
					['19','19'], ['20','20'], ['21','21'], ['22','22'], ['23','23'], ['24','24'],
					['25','25'], ['26','26'], ['27','27'], ['28','28'], ['29','29'], ['30','30'],
					['31','31']];
		var store = new Ext.data.SimpleStore({
		    fields: ['id', 'name'],
		    data : codes
		});
		me.dayCombo = new Ext.form.ComboBox({
			columnWidth:.7,
			hiddenName:me.timerDayField,
			name: me.timerDayField,
			fieldLabel: '天',
		    store: store,
		    width:90,
		    displayField:'name',
		    valueField:'id',
		    typeAhead: true,
		    mode: 'local',
		    triggerAction: 'all',
		    emptyText:'请选择天',
		    selectOnFocus:true
		});
	},
	/**
	 * 初始化周
	 * @param {} me
	 */
	initWeek : function(me){
		var codes =[['MON','1'], ['TUE','2'], ['WED','3'], ['THU','4'], ['FRI','5'], ['SAT','6'], 
					['SUN','7']];
		var store = new Ext.data.SimpleStore({
		    fields: ['id', 'name'],
		    data : codes
		});
		me.weekCombo = new Ext.form.ComboBox({
			columnWidth:.7,
			hiddenName:me.timerWeekField,
			name: me.timerWeekField,
			fieldLabel: '周',
		    store: store,
		    width:90,
		    displayField:'name',
		    valueField:'id',
		    typeAhead: true,
		    mode: 'local',
		    triggerAction: 'all',
		    emptyText:'请选择周',
		    selectOnFocus:true
		});
	},
	/**
	 * 初始化小时
	 * @param {} me
	 */
	initHour : function(me){
		var codes =[['*','每小时'], 
					['0','0'],['1','1'], ['2','2'], ['3','3'], ['4','4'], ['5','5'], ['6','6'], 
					['7','7'], ['8','8'], ['9','9'], ['10','10'], ['11','11'], ['12','12'],
					['13','13'],['14','14'], ['15','15'], ['16','16'], ['17','17'], ['18','18'], ['19','19'], 
					['20','20'], ['21','21'], ['22','22'], ['23','23']];
		var store = new Ext.data.SimpleStore({
		    fields: ['id', 'name'],
		    data : codes
		});
		me.hourCombo = new Ext.form.ComboBox({
			hiddenName:me.timerHourField,
			name: me.timerHourField,
			fieldLabel: '小时',
		    store: store,
		    width:90,
		    displayField:'name',
		    valueField:'id',
		    typeAhead: true,
		    mode: 'local',
		    triggerAction: 'all',
		    emptyText:'请选择小时',
		    selectOnFocus:true
		});
	},
	/**
	 * 初始化分钟
	 * @param {} me
	 */
	initMinute : function(me){
		var codes =[['*','每分钟'], 
					['0','0'],['1','1'], ['2','2'], ['3','3'], ['4','4'], ['5','5'], ['6','6'], 
					['7','7'], ['8','8'], ['9','9'], ['10','10'], ['11','11'], ['12','12'],
					['13','13'], ['14','14'], ['15','15'], ['16','16'], ['17','17'], ['18','18'],
					['19','19'], ['20','20'], ['21','21'], ['22','22'], ['23','23'], ['24','24'],
					['25','25'], ['26','26'], ['27','27'], ['28','28'], ['29','29'], ['30','30'],
					['31','31'], ['32','32'], ['33','33'], ['34','34'], ['35','35'], ['36','36'],
					['37','37'], ['38','38'], ['39','39'], ['40','40'], ['41','41'], ['42','42'],
					['43','43'], ['44','44'], ['45','45'], ['46','46'], ['47','47'], ['48','48'],
					['49','49'], ['50','50'], ['51','51'], ['52','52'], ['53','53'], ['54','54'],
					['55','55'], ['56','56'], ['57','57'], ['58','58'], ['59','59']];
		var store = new Ext.data.SimpleStore({
		    fields: ['id', 'name'],
		    data : codes
		});
		me.minuteCombo = new Ext.form.ComboBox({
			hiddenName:me.timerMinuteField,
			name: me.timerMinuteField,
			fieldLabel: '分钟',
		    store: store,
		    width:90,
		    displayField:'name',
		    valueField:'id',
		    typeAhead: true,
		    mode: 'local',
		    triggerAction: 'all',
		    emptyText:'请选择分钟',
		    selectOnFocus:true
		});
	},
	/**
	 * 初始化秒
	 * @param {} me
	 */
	initSecond : function(me){
		var codes =[['*','每秒'], 
					['0','0'],['1','1'], ['2','2'], ['3','3'], ['4','4'], ['5','5'], ['6','6'], 
					['7','7'], ['8','8'], ['9','9'], ['10','10'], ['11','11'], ['12','12'],
					['13','13'], ['14','14'], ['15','15'], ['16','16'], ['17','17'], ['18','18'],
					['19','19'], ['20','20'], ['21','21'], ['22','22'], ['23','23'], ['24','24'],
					['25','25'], ['26','26'], ['27','27'], ['28','28'], ['29','29'], ['30','30'],
					['31','31'], ['32','32'], ['33','33'], ['34','34'], ['35','35'], ['36','36'],
					['37','37'], ['38','38'], ['39','39'], ['40','40'], ['41','41'], ['42','42'],
					['43','43'], ['44','44'], ['45','45'], ['46','46'], ['47','47'], ['48','48'],
					['49','49'], ['50','50'], ['51','51'], ['52','52'], ['53','53'], ['54','54'],
					['55','55'], ['56','56'], ['57','57'], ['58','58'], ['59','59']];
		var store = new Ext.data.SimpleStore({
		    fields: ['id', 'name'],
		    data : codes
		});
		me.secondCombo = new Ext.form.ComboBox({
			hiddenName:me.timerSecondField,
			name: me.timerSecondField,
			fieldLabel: '秒',
		    store: store,
		    width:90,
		    displayField:'name',
		    valueField:'id',
		    typeAhead: true,
		    mode: 'local',
		    triggerAction: 'all',
		    emptyText:'请选择秒',
		    selectOnFocus:true
		});
	},
	/**
	 * 初始化定时器预警面板
	 * @param {} me
	 */
	initTimerFormPanel : function(me){
		var fieldSet = new Ext.form.FieldSet({
            title: '选择定时类型',
            collapsible: false,
            height : 300,
            items :[{
        			xtype:'panel',layout:'fit',items:[
	        			{
	        				xtype:'radio',boxLabel: '循环类型', name: me.timerTypeField, inputValue: me.timerTypeCircleValue, checked: true,
	        				listeners:{
	        					check:function(){
	        						me.selectCircleType(this.getValue(),me);
	        					}
	        				}
	        			}
	        		]
		        	}, new Ext.form.FieldSet({
			            collapsible: false,
			            height : 50,
			            items :[{
					           	layout:'column',
					           	items:[{
					           		columnWidth:.50,
					           		items:[{
					           					xtype:'label',text:'循环时间(秒):'
					           				},{
						           		    	xtype:'textfield',
								                fieldLabel: '循环时间(秒)',
								                name: me.timerCircleValueField,
								                allowBlank:false,
								                width:100
					           		       }
					           		]
			           			},{
					           		columnWidth:.50,
					           		items:[{
					           					xtype:'label',text:'循环次数(-1代表无限次,0代表一次):'
					           				},{
						           		    	xtype:'textfield',
								                fieldLabel: '循环次数(-1代表无限次)',
								                name: me.timerCircleNumField,
								                allowBlank:false,
								                width:60
					           		       }
					           		]
			           			}]
				        	}]
		        	}),{
	           		xtype:'panel',layout:'fit',items:[
	        			{
	        				xtype:'radio',boxLabel: '固定时间类型',name: me.timerTypeField, inputValue: me.timerTypeSelectedValue,
	        				listeners:{
	        					check:function(){
	        						me.selectMixtedType(this.getValue(),me);
	        					}
	        				}
	        			}
	        		]},new Ext.form.FieldSet({
			            collapsible: false,
			            height : 90,
			            items :[
			            	{
					           	layout:'column',
					           	items:[
					           		{
						           		columnWidth:.17,
						           		items:[
						           				{xtype:'label',text:'月:',align:'center'},
						           				me.monthCombo
						           		]
				           			},
					           		{
						           		columnWidth:.32,
						           		items:[
						           				{xtype:'label',text:'日:'},
						           				{
							           				layout:'column',
							           				items:[
									        			{
									        				columnWidth:.3,xtype:'radio',boxLabel: 'dates',id:me.timerDayDateId, name: me.timerDayTypeField, inputValue: me.timerDayDateValue, checked: true,
									        				listeners:{
																check:function(){
																	if(this.getValue()){
																		me.TimerFormPanel.form.findField(me.timerDayField).enable();
																		me.TimerFormPanel.form.findField(me.timerWeekField).disable();
																	}
																}						        			
										        			}
									        			},
									        			me.dayCombo
									        		]
								        		},
								        		{
									        		layout:'column',
									        		items:[
									        			{
									        				columnWidth:.3,xtype:'radio',boxLabel: 'weeks', id:me.timerDayWeekId,name: me.timerDayTypeField, inputValue: me.timerDayWeekValue,
									        				listeners:{
																check:function(){
																	if(this.getValue()){
																		me.TimerFormPanel.form.findField(me.timerWeekField).enable();
																		me.TimerFormPanel.form.findField(me.timerDayField).disable();
																	}
																}						        			
										        			}
									        			},
									        			me.weekCombo
									        		]
								        		}
						           		]
					           		},
					           		{
					           			columnWidth:.17,
					           			items:[
					           				{xtype:'label',text:'小时:'},
					           				 me.hourCombo
					           			]
					           		},
					           		{
					           			columnWidth:.17,
					           			items:[
					           				{xtype:'label',text:'分钟:'},
					           				 me.minuteCombo
					           			]
					           		},
					           		{
					           			columnWidth:.17,
					           			items:[
					           				{xtype:'label',text:'秒:'},
					           				 me.secondCombo
					           			]
					           		}
					           	]
					          }
			            ]
		        	}),{
	        		xtype:'panel',layout:'fit',items:[
      	        			{
      	        				xtype:'radio',boxLabel: '手动输入时间表达式', name: me.timerTypeField, inputValue: me.timerTypeInputValue,
      	        				listeners:{
      	        					check:function(){
      	        						me.selectInputType(this.getValue(),me);
      	        					}
      	        				}
      	        			}
      	        		]
      	        	}, new Ext.form.FieldSet({
			            collapsible: false,
			            height : 50,
			            items :[
			            	{
		    		           	layout:'column',
		    		           	items:[{
		    		           		columnWidth:1,
		    		           		items:[{
		    		           					xtype:'label',text:'时间表达式:'
		    		           				},{
		    			           		    	xtype:'textfield',
		    					                name: me.timerInputExpField,
		    					                allowBlank:false,
		    					                width:150,
								                listeners : {
								                	blur : function(){
								                		if(!Ext.isEmpty(this.getValue()))
								                			me.validateCronExpression(me);
								                	}
								                }
		    		           		       }
		    		           		]
		               			}]
		    	        	}
			            ]
		        	})]
		});
		me.TimerFormPanel = new Ext.FormPanel({
		        labelWidth: 70, 
		        autoScroll:true,
		        frame:true,
		        bodyStyle:'padding:5px 5px 0',
		        width: 350,
		        reader : me.formReader,
		        items: [{
		        		xtype:'hidden',
		        		name: me.timerIdField
		        	},fieldSet,{
			           	layout:'column',
			           	items:[
			           		{
				           		columnWidth:.50,
				           		items:[
				           				{xtype:'label',text:'定时开始时间:',align:'center'},
				           				{
				    						xtype: 'datetimefield',
				    						fieldLabel: '开始时间',
				    						name : me.timerStartTimeField,
				    						width:200,
				    						listeners:{
				    							focus:function(){
				    								this.selectText();
				    							}
				    						}
				    					 }
				           		]
		           			},
		           			{
				           		columnWidth:.50,
				           		items:[
				           				{xtype:'label',text:'定时结束时间:',align:'center'},
				           				{
				    						xtype: 'datetimefield',
				    						fieldLabel: '结束时间',
				    						name : me.timerEndTimeField,
				    						width:200,
				    						listeners:{
					    						focus:function(){
					    							this.selectText();
					    						},
					    						blur:function(){
					    							var endTime = this.getValue();
					    							var startTime = me.TimerFormPanel.form.findField(me.timerStartTimeField).getValue();
					    							if(!Ext.isEmpty(endTime)){
					    								if(startTime>endTime){
						    								me.dateValidateFlag = false;
						    								Ext.Msg.alert('提示','结束日期不能小于开始日期');
						    								this.selectText();
						    							}else{
						    								me.dateValidateFlag = true;
						    							}
					    							}
					    						}
				    						}
				    					 }
				           		]
		           			}]}
		       ]
		 });
	},
	/**
	 * 选择循环类型
	 * @param {} value
	 * @param {} me
	 */
	selectCircleType : function(value,me){
		if(value){
			me.TimerFormPanel.form.findField(me.timerCircleValueField).enable();
			me.TimerFormPanel.form.findField(me.timerCircleNumField).enable();
			me.TimerFormPanel.form.findField(me.timerMonthField).disable();
			me.TimerFormPanel.findById(me.timerDayDateId).disable();
			me.TimerFormPanel.findById(me.timerDayWeekId).disable();
			me.TimerFormPanel.form.findField(me.timerDayField).disable();
			me.TimerFormPanel.form.findField(me.timerWeekField).disable();
			me.TimerFormPanel.form.findField(me.timerHourField).disable();
			me.TimerFormPanel.form.findField(me.timerMinuteField).disable();
			me.TimerFormPanel.form.findField(me.timerSecondField).disable();
			me.TimerFormPanel.form.findField(me.timerInputExpField).disable();
		}
	},
	/**
	 * 选择固定时间类型
	 * @param {} value
	 * @param {} me
	 */
	selectMixtedType : function(value,me){
		if(value){
			me.TimerFormPanel.form.findField(me.timerMonthField).enable();
			me.TimerFormPanel.findById(me.timerDayDateId).enable();
			me.TimerFormPanel.findById(me.timerDayWeekId).enable();
			me.TimerFormPanel.form.findField(me.timerDayField).enable();
			me.TimerFormPanel.form.findField(me.timerWeekField).enable();
			me.TimerFormPanel.form.findField(me.timerHourField).enable();
			me.TimerFormPanel.form.findField(me.timerMinuteField).enable();
			me.TimerFormPanel.form.findField(me.timerSecondField).enable();
			me.TimerFormPanel.form.findField(me.timerCircleValueField).disable();
			me.TimerFormPanel.form.findField(me.timerCircleNumField).disable();
			me.TimerFormPanel.form.findField(me.timerInputExpField).disable();
		}
	},
	/**
	 * 选择手动输入类型
	 * @param {} value
	 * @param {} me
	 */
	selectInputType : function(value,me){
		if(value){
			me.TimerFormPanel.form.findField(me.timerInputExpField).enable();
			me.TimerFormPanel.form.findField(me.timerCircleValueField).disable();
			me.TimerFormPanel.form.findField(me.timerCircleNumField).disable();
			me.TimerFormPanel.form.findField(me.timerMonthField).disable();
			me.TimerFormPanel.findById(me.timerDayDateId).disable();
			me.TimerFormPanel.findById(me.timerDayWeekId).disable();
			me.TimerFormPanel.form.findField(me.timerDayField).disable();
			me.TimerFormPanel.form.findField(me.timerWeekField).disable();
			me.TimerFormPanel.form.findField(me.timerHourField).disable();
			me.TimerFormPanel.form.findField(me.timerMinuteField).disable();
			me.TimerFormPanel.form.findField(me.timerSecondField).disable();
		}
	},
	/**
	 * 校验cron表达式
	 * @param {} me
	 */
	validateCronExpression : function(me){
		 var cronExpression = me.TimerFormPanel.form.findField(me.timerInputExpField).getValue();
		 Ext.Ajax.request({
			url : me.validateCronExpUrl,
			params : {
				"cronExpression":cronExpression
			},
			success : function(request) {
				var json = Ext.decode(request.responseText);
				if (json.success) {
					me.validateCronExpFlag = true;
				} else {
					me.validateCronExpFlag = false;
					Ext.Msg.alert('提示',json.msg);
				}
			},
			failure : function(request) {
				me.validateCronExpFlag = false;
				var json = Ext.decode(request.responseText);
				Ext.Msg.show({title:'错误',msg:json.msg,buttons:Ext.Msg.OK});
			}
		});
	},
	/**
	 * 获取定时器form的数据
	 * @return {}
	 */
	getFormValues : function(){
		var me = this;
		var result = new Array();
		var oecpTaskTimerType = this.TimerFormPanel.getForm().findField(me.timerTypeField).getGroupValue();
		if(oecpTaskTimerType == me.timerTypeCircleValue){
			var circleValue = this.TimerFormPanel.form.findField(me.timerCircleValueField).getValue();
			var circleNum = this.TimerFormPanel.form.findField(me.timerCircleNumField).getValue();
			if(Ext.isEmpty(circleValue)){
				Ext.Msg.alert('提示','循环时间不能为空');
			 	result[0] = false;
				return result;
			}else if(!Ext.isEmpty(circleValue)&&!(/^\d{0,10}$/.test(circleValue))){
			 	Ext.Msg.alert('提示','循环时间为整数');
			 	result[0] = false;
				return result;
			}else{
			 	result[0] = true;
			}
			if(Ext.isEmpty(circleNum)){
				Ext.Msg.alert('提示','通知循环次数不能为空');
			 	result[0] = false;
				return result;
			}else if(!Ext.isEmpty(circleNum)&&!(/^(((-1){0,1})|(\d{0,10}))$/.test(circleNum))){
			 	Ext.Msg.alert('提示','通知循环次数为数字(-1或整数)');
			 	result[0] = false;
			 	return result;
			}else{
			 	result[0] = true;
			}
		}else if(oecpTaskTimerType == me.timerTypeSelectedValue){
			var month = this.TimerFormPanel.form.findField(me.timerMonthField).getValue();
			var hour = this.TimerFormPanel.form.findField(me.timerHourField).getValue();
			var minute = this.TimerFormPanel.form.findField(me.timerMinuteField).getValue();
			var second = this.TimerFormPanel.form.findField(me.timerSecondField).getValue();
			var oecpTaskTimerDayType = this.TimerFormPanel.form.findField(me.timerDayTypeField).getGroupValue();
			if(Ext.isEmpty(month)){
			 	Ext.Msg.alert('提示','月不能为空');
			 	result[0] = false;
				return result;
			}else{
			 	result[0] = true;
			}
			if(oecpTaskTimerDayType == me.timerDayDateValue){
				var day = this.TimerFormPanel.form.findField(me.timerDayField).getValue();
				if(Ext.isEmpty(day)){
				 	Ext.Msg.alert('提示','天不能为空');
				 	result[0] = false;
					return result;
				}else{
				 	result[0] = true;
				}
			}else{
				var week = this.TimerFormPanel.form.findField(me.timerWeekField).getValue();
				if(Ext.isEmpty(week)){
				 	Ext.Msg.alert('提示','周不能为空');
				 	result[0] = false;
					return result;
				}else{
				 	result[0] = true;
				}
			}
			if(Ext.isEmpty(hour)){
			 	Ext.Msg.alert('提示','小时不能为空');
			 	result[0] = false;
				return result;
			}else{
			 	result[0] = true;
			}
			if(Ext.isEmpty(minute)){
			 	Ext.Msg.alert('提示','分钟不能为空');
			 	result[0] = false;
				return result;
			}else{
			 	result[0] = true;
			}
			if(Ext.isEmpty(second)){
			 	Ext.Msg.alert('提示','秒不能为空');
			 	result[0] = false;
				return result;
			}else{
			 	result[0] = true;
			}
		}else{
			//表达式校验
			if(!me.validateCronExpFlag){
				Ext.Msg.alert('提示','时间表达式校验未通过！');
				result[0] = false;
				return result; 
			}
			var inputExpression = this.TimerFormPanel.form.findField(me.timerInputExpField).getValue();
			if(Ext.isEmpty(inputExpression)){
			 	Ext.Msg.alert('提示','时间表达式不能为空');
			 	result[0] = false;
				return result; 
			}else{
			 	result[0] = true;
			}
		}
		if(!me.dateValidateFlag){
			Ext.Msg.alert('提示','结束日期不能小于开始日期');
			result[0] = false;
			return result; 
		}
		result[1] = this.TimerFormPanel.getForm().getValues();
		return result;
	}	
	

})