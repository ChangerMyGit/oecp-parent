package oecp.platform.warning.service;

import oecp.framework.dao.DAO;
import oecp.framework.exception.BizException;
import oecp.platform.org.eo.Organization;
import oecp.platform.warning.enums.TimerWarnStartFlag;
import oecp.platform.warning.eo.Warn;
import oecp.platform.warning.eo.WarnItfContent;

/**
 * 预警引擎
 *
 * @author YangTao
 * @date 2012-3-12上午10:11:21
 * @version 1.0
 */
public interface WarningEngine {
	/**
	 * 事件预警
	 * 
	 * @author YangTao
	 * @date 2012-3-12上午10:26:39
	 */
	public void onEventWarn(Object source, String eventName, Organization org,Object... objects)throws BizException;
	/**
	 * 定时器预警
	 * 
	 * @author YangTao
	 * @date 2012-3-15上午09:24:48
	 * @throws BizException
	 */
	public void onTimerWarn(Warn warn,TimerWarnStartFlag timerWarnStartFlag)throws BizException;
}
