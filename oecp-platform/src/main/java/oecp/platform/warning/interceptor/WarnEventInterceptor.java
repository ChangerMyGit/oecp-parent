package oecp.platform.warning.interceptor;

import javax.annotation.Resource;

import oecp.framework.exception.BizException;
import oecp.platform.org.eo.Organization;
import oecp.platform.warning.service.WarningEngine;

/**
 * 事件预警
 * 在oecp.platform.event.service.EventEngine.fireEvent之后执行的拦截器
 *
 * @author YangTao
 * @date 2012-3-12上午09:27:07
 * @version 1.0
 */
public class WarnEventInterceptor {
	
	@Resource
	private WarningEngine warningEngine;
	
	/**
	 * 做事件预警处理
	 * 
	 * @author YangTao
	 * @date 2012-3-12上午09:28:46
	 */
	public void doEventInterceptor(Object source, String eventName, Organization org,Object... objects) throws BizException{ 
		warningEngine.onEventWarn(source,eventName,org,objects);
	}

	public WarningEngine getWarningEngine() {
		return warningEngine;
	}

	public void setWarningEngine(WarningEngine warningEngine) {
		this.warningEngine = warningEngine;
	}
	
	
}
