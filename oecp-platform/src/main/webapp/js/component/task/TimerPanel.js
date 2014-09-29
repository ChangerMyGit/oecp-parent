Ext.ns("OECP.Task")
/**
 * 定时器预警配置面板
 * @class OECP.Task.TimerPanel
 * @extends Ext.Panel
 */
OECP.Task.TimerPanel = Ext.extend(OECP.TimerBasePanel, {
	title : '定时信息',
	layout : 'fit',
	/**定时器ID字段**/
	timerIdField : 'task.oecpTaskTimer.id',
	/**定时器类型字段**/
	timerTypeField : 'task.oecpTaskTimer.timerType',
	/**定时器循环类型字段**/
	timerTypeCircleValue : 'TIMER_CIRCLE',
	/**定时器固定时间类型字段**/
	timerTypeSelectedValue : 'TIMER_SELECTED',
	/**定时器手动输入类型字段**/
	timerTypeInputValue : 'TIMER_INPUT',
	/**定时器循环时间字段**/
	timerCircleValueField : 'task.oecpTaskTimer.circleValue',
	/**定时器循环次数字段**/
	timerCircleNumField : 'task.oecpTaskTimer.circleNum',
	/**定时器天日期类型ID**/
	timerDayDateId : 'task.oecpTaskTimer.dates',
	/**定时器天周类型ID**/
	timerDayWeekId : 'task.oecpTaskTimer.weeks',
	/**定时器天类型字段**/
	timerDayTypeField : 'task.oecpTaskTimer.timerDayType',
	/**定时器天日期类型字段**/
	timerDayDateValue : 'TIMER_DAY_DATE',
	/**定时器天周类型字段**/
	timerDayWeekValue : 'TIMER_DAY_WEEK',
	/**定时器月字段**/
	timerMonthField : 'task.oecpTaskTimer.month',
	/**定时器天字段**/
	timerDayField : 'task.oecpTaskTimer.day',
	/**定时器周字段**/
	timerWeekField : 'task.oecpTaskTimer.week',
	/**定时器小时字段**/
	timerHourField : 'task.oecpTaskTimer.hour',
	/**定时器分钟字段**/
	timerMinuteField : 'task.oecpTaskTimer.minute',
	/**定时器秒字段**/
	timerSecondField : 'task.oecpTaskTimer.second',
	/**定时器输入表达式字段**/
	timerInputExpField : 'task.oecpTaskTimer.inputExpression',
	/**定时器开始时间字段**/
	timerStartTimeField : 'task.oecpTaskTimer.startTime',
	/**定时器结束时间字段**/
	timerEndTimeField : 'task.oecpTaskTimer.endTime',
	/**定时器form reader**/
	formReader : new Ext.data.JsonReader({
			root : 'result'
		}, [{
			name : 'task.oecpTaskTimer.id',
			mapping : 'oecpTaskTimer.id'
		}, {
			name : 'task.oecpTaskTimer.timerType',
			mapping : 'oecpTaskTimer.timerType'
		}, {
			name : 'task.oecpTaskTimer.circleValue',
			mapping : 'oecpTaskTimer.circleValue'
		}, {
			name : 'task.oecpTaskTimer.circleNum',
			mapping : 'oecpTaskTimer.circleNum'
		}, {
			name : 'task.oecpTaskTimer.month',
			mapping : 'oecpTaskTimer.month'
		}, {
			name : 'task.oecpTaskTimer.timerDayType',
			mapping : 'oecpTaskTimer.timerDayType'
		}, {
			name : 'task.oecpTaskTimer.day',
			mapping : 'oecpTaskTimer.day'
		}, {
			name : 'task.oecpTaskTimer.week',
			mapping : 'oecpTaskTimer.week'
		}, {
			name : 'task.oecpTaskTimer.hour',
			mapping : 'oecpTaskTimer.hour'
		}, {
			name : 'task.oecpTaskTimer.minute',
			mapping : 'oecpTaskTimer.minute'
		}, {
			name : 'task.oecpTaskTimer.second',
			mapping : 'oecpTaskTimer.second'
		}, {
			name : 'task.oecpTaskTimer.inputExpression',
			mapping : 'oecpTaskTimer.inputExpression'
		}, {
			name : 'task.oecpTaskTimer.startTime',
			mapping : 'oecpTaskTimer.startTime'
		}, {
			name : 'task.oecpTaskTimer.endTime',
			mapping : 'oecpTaskTimer.endTime'
		}]),
	/**
	 * 初始化方法
	 */
	initComponent : function(){
	 	OECP.Task.TimerPanel.superclass.initComponent.call(this);
	}
});