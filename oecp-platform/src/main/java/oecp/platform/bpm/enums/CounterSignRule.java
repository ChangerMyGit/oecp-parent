/*
 * Copyright (c) 2011 OECP All Rights Reserved.                	
 * <a href="http://www.oecp.cn">http://www.oecp.cn</a>                                                                 
 */    

package oecp.platform.bpm.enums;

import oecp.framework.util.enums.EnumDescription;

/**
 * 会签规则
 * @author yangtao
 * @date 2011-8-30上午10:08:08
 * @version 1.0
 */
public enum CounterSignRule {
	@EnumDescription("没有会签")
	NO_COUNTERSIGN_RULE,
	@EnumDescription("一票通过")
	ONE_TICKET_PASS,
	@EnumDescription("一票否决(全票通过)")
	ONE_TICKET_NO_PASS,
	@EnumDescription("比例否决")
	PROPORTION
}
