package oecp.platform.warning.eo;

import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.Transient;

import oecp.framework.entity.base.StringPKEO;
import oecp.platform.warning.enums.TimerWarnDayType;
import oecp.platform.warning.enums.TimerWarnType;

/**
 * 定时器预警
 *
 * @author YangTao
 * @date 2012-3-12下午03:01:53
 * @version 1.0
 */
@Entity
@Table(name="OECP_WARN_TIMER")
public class TimerWarn extends StringPKEO {

	
	private static final long serialVersionUID = 1L;
	
	//类型
	private TimerWarnType timerWarnType;
	//循环值 分钟
	private String circleValue;
	//循环次数
	private String circleNum;
	//月
	private String month;
	//天类型
	private TimerWarnDayType timerWarnDayType;
	//天
	private String day;
	//周
	private String week;
	//小时
	private String hour;
	//分钟
	private String minute;
	//秒
	private String second;
	//开始时间
	private String startTime;
	//结束时间
	private String endTime;
	//手动输入表达式
	private String inputExpression;
	
	public String getMonth() {
		return month;
	}
	public void setMonth(String month) {
		this.month = month;
	}
	public String getDay() {
		return day;
	}
	public void setDay(String day) {
		this.day = day;
	}
	public String getHour() {
		return hour;
	}
	public void setHour(String hour) {
		this.hour = hour;
	}
	public String getMinute() {
		return minute;
	}
	public void setMinute(String minute) {
		this.minute = minute;
	}
	public String getSecond() {
		return second;
	}
	public void setSecond(String second) {
		this.second = second;
	}
	public TimerWarnType getTimerWarnType() {
		return timerWarnType;
	}
	public void setTimerWarnType(TimerWarnType timerWarnType) {
		this.timerWarnType = timerWarnType;
	}
	public String getCircleValue() {
		return circleValue;
	}
	public void setCircleValue(String circleValue) {
		this.circleValue = circleValue;
	}
	public TimerWarnDayType getTimerWarnDayType() {
		return timerWarnDayType;
	}
	public void setTimerWarnDayType(TimerWarnDayType timerWarnDayType) {
		this.timerWarnDayType = timerWarnDayType;
	}
	public String getWeek() {
		return week;
	}
	public void setWeek(String week) {
		this.week = week;
	}
	public String getCircleNum() {
		return circleNum;
	}
	public void setCircleNum(String circleNum) {
		this.circleNum = circleNum;
	}
	
	public String getStartTime() {
		return startTime;
	}
	public void setStartTime(String startTime) {
		this.startTime = startTime;
	}
	public String getEndTime() {
		return endTime;
	}
	public void setEndTime(String endTime) {
		this.endTime = endTime;
	}
	public String getInputExpression() {
		return inputExpression;
	}
	public void setInputExpression(String inputExpression) {
		this.inputExpression = inputExpression;
	}
	/**
	 * 根据配置获取quarz表达式
	 * 
	 * @author YangTao
	 * @date 2012-3-14下午04:26:58
	 * @return
	 */
	@Transient
	public String getConExpression(){
		StringBuffer sb = new StringBuffer();
		if(this.timerWarnDayType==TimerWarnDayType.TIMER_DAY_DATE){
			sb.append(this.second+" ");	
			sb.append(this.minute+" ");	
			sb.append(this.hour+" ");
			sb.append(this.day+" ");
			sb.append(this.month+" ");
			sb.append("?");
		}else{
			sb.append(this.second+" ");	
			sb.append(this.minute+" ");	
			sb.append(this.hour+" ");
			sb.append("? ");
			sb.append(this.month+" ");
			sb.append(this.week+" ");
		}
			
		return sb.toString();
	}

}
