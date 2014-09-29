package oecp.platform.warning.enums;

import oecp.framework.util.enums.EnumDescription;

/**
 * 预警通知方式
 *
 * @author YangTao
 * @date 2012-3-6下午02:29:35
 * @version 1.0
 */
public enum WarnNoticeItem {
	@EnumDescription("门户通知")
	PORTAL_NOTICE,
	@EnumDescription("邮件通知")
	EMAIL_NOTICE,
	@EnumDescription("短信通知")
	SHORT_MESSAGE_NOTICE
}
