Ext.ns("OECP.Warn")
/**
 * 定时器预警配置面板
 * @class OECP.Warn.TimerWarnPanel
 * @extends Ext.Panel
 */
OECP.Warn.TimerWarnPanel = Ext.extend(OECP.TimerBasePanel, {
	title : '定时信息',
	layout : 'fit',
	autoScroll : true,
	/**定时器ID字段**/
	timerIdField : 'warn.timerWarn.id',
	/**定时器类型字段**/
	timerTypeField : 'warn.timerWarn.timerWarnType',
	/**定时器循环类型字段**/
	timerTypeCircleValue : 'TIMER_CIRCLE',
	/**定时器固定时间类型字段**/
	timerTypeSelectedValue : 'TIMER_SELECTED',
	/**定时器手动输入类型字段**/
	timerTypeInputValue : 'TIMER_INPUT',
	/**定时器循环时间字段**/
	timerCircleValueField : 'warn.timerWarn.circleValue',
	/**定时器循环次数字段**/
	timerCircleNumField : 'warn.timerWarn.circleNum',
	/**定时器天日期类型ID**/
	timerDayDateId : 'warn.timerWarn.dates',
	/**定时器天周类型ID**/
	timerDayWeekId : 'warn.timerWarn.weeks',
	/**定时器天类型字段**/
	timerDayTypeField : 'warn.timerWarn.timerWarnDayType',
	/**定时器天日期类型字段**/
	timerDayDateValue : 'TIMER_DAY_DATE',
	/**定时器天周类型字段**/
	timerDayWeekValue : 'TIMER_DAY_WEEK',
	/**定时器月字段**/
	timerMonthField : 'warn.timerWarn.month',
	/**定时器天字段**/
	timerDayField : 'warn.timerWarn.day',
	/**定时器周字段**/
	timerWeekField : 'warn.timerWarn.week',
	/**定时器小时字段**/
	timerHourField : 'warn.timerWarn.hour',
	/**定时器分钟字段**/
	timerMinuteField : 'warn.timerWarn.minute',
	/**定时器秒字段**/
	timerSecondField : 'warn.timerWarn.second',
	/**定时器输入表达式字段**/
	timerInputExpField : 'warn.timerWarn.inputExpression',
	/**定时器开始时间字段**/
	timerStartTimeField : 'warn.timerWarn.startTime',
	/**定时器结束时间字段**/
	timerEndTimeField : 'warn.timerWarn.endTime',
	/**定时器form reader**/
	formReader : new Ext.data.JsonReader({
		root : 'result'
	}, [{
		name : 'warn.timerWarn.id',
		mapping : 'timerWarn.id'
	}, {
		name : 'warn.timerWarn.timerWarnType',
		mapping : 'timerWarn.timerWarnType'
	}, {
		name : 'warn.timerWarn.circleValue',
		mapping : 'timerWarn.circleValue'
	}, {
		name : 'warn.timerWarn.circleNum',
		mapping : 'timerWarn.circleNum'
	}, {
		name : 'warn.timerWarn.month',
		mapping : 'timerWarn.month'
	}, {
		name : 'warn.timerWarn.timerWarnDayType',
		mapping : 'timerWarn.timerWarnDayType'
	}, {
		name : 'warn.timerWarn.day',
		mapping : 'timerWarn.day'
	}, {
		name : 'warn.timerWarn.week',
		mapping : 'timerWarn.week'
	}, {
		name : 'warn.timerWarn.hour',
		mapping : 'timerWarn.hour'
	}, {
		name : 'warn.timerWarn.minute',
		mapping : 'timerWarn.minute'
	}, {
		name : 'warn.timerWarn.second',
		mapping : 'timerWarn.second'
	},{
		name : 'warn.timerWarn.inputExpression',
		mapping : 'timerWarn.inputExpression'
	}, {
		name : 'warn.timerWarn.startTime',
		mapping : 'timerWarn.startTime'
	}, {
		name : 'warn.timerWarn.endTime',
		mapping : 'timerWarn.endTime'
	}]),
	/**
	 * 初始化方法
	 */
	initComponent : function(){
		OECP.Warn.TimerWarnPanel.superclass.initComponent.call(this);
	}
});