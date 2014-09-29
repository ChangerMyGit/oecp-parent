package oecp.platform.warning.enums;

import oecp.framework.util.enums.EnumDescription;
/**
 * 预警定时器类型
 *
 * @author YangTao
 * @date 2012-3-13下午02:25:39
 * @version 1.0
 */
public enum TimerWarnType {
	@EnumDescription("循环类型")
	TIMER_CIRCLE,
	@EnumDescription("固定时间类型")
	TIMER_SELECTED,
	@EnumDescription("手动输入表达式")
	TIMER_INPUT
}
