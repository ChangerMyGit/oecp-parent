package oecp.platform.warning.responder.factory;

import oecp.framework.util.SpringContextUtil;
import oecp.platform.warning.enums.WarnNoticeItem;
import oecp.platform.warning.responder.WarningResponder;
import oecp.platform.warning.responder.service.WarningPortalResponder;

/**
 * 根据通知方式获取通知响应的bean
 *
 * @author YangTao
 * @date 2012-3-12上午11:11:18
 * @version 1.0
 */
public class WarningResponderFactory {
	public static WarningResponder getWarningResponder(WarnNoticeItem wn){
		WarningResponder warningResponder = null;
		
		if(wn == WarnNoticeItem.PORTAL_NOTICE){
			warningResponder = (WarningResponder)SpringContextUtil.getBean("warningPortalResponder");
		}else if(wn == WarnNoticeItem.EMAIL_NOTICE){
			
		}else if(wn == WarnNoticeItem.SHORT_MESSAGE_NOTICE){
			
		}
		
		return warningResponder;
	}

}
