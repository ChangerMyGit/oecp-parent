package oecp.platform.otask.eo;

import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.Transient;

import oecp.framework.entity.base.StringPKEO;
import oecp.platform.otask.enums.TimerDayType;
import oecp.platform.otask.enums.TimerType;
import oecp.platform.warning.enums.TimerWarnDayType;
/**
 * 
 * @Desc 任务管理 定时任务配置
 * @author yangtao
 * @date 2012-3-29
 *
 */
@Entity
@Table(name="OECP_TASK_TIMER")
public class OecpTaskTimer   extends StringPKEO {

	/**
	 * 
	 */
	private static final long serialVersionUID = 6987723670397393267L;
	
	//类型
	private TimerType timerType;
	//循环值 分钟
	private String circleValue;
	//循环次数
	private String circleNum;
	//月
	private String month;
	//天类型
	private TimerDayType timerDayType;
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
	
	
	public TimerType getTimerType() {
		return timerType;
	}
	public void setTimerType(TimerType timerType) {
		this.timerType = timerType;
	}
	public String getCircleValue() {
		return circleValue;
	}
	public void setCircleValue(String circleValue) {
		this.circleValue = circleValue;
	}
	public String getCircleNum() {
		return circleNum;
	}
	public void setCircleNum(String circleNum) {
		this.circleNum = circleNum;
	}
	public String getMonth() {
		return month;
	}
	public void setMonth(String month) {
		this.month = month;
	}
	public TimerDayType getTimerDayType() {
		return timerDayType;
	}
	public void setTimerDayType(TimerDayType timerDayType) {
		this.timerDayType = timerDayType;
	}
	public String getDay() {
		return day;
	}
	public void setDay(String day) {
		this.day = day;
	}
	public String getWeek() {
		return week;
	}
	public void setWeek(String week) {
		this.week = week;
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
		if(this.timerDayType==TimerDayType.TIMER_DAY_DATE){
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
