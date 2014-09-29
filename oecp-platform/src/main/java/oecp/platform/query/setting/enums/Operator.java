/**
 * oecp-platform - UIViewParser.java
 * copyright 2009-2012 OECP www.oecp.cn
 * 创建人:slx	创建时间:2011-11-7上午10:41:16		版本:v1
 * ============================================
 * 修改人：			修改时间:					版本:
 * 修改原因:
 *
 * ============================================
 */

package oecp.platform.query.setting.enums;

import oecp.framework.util.enums.EnumDescription;
import oecp.platform.query.setting.annotation.OperatorDescription;

/** 
 * 操作符
 * @author slx  
 * @date 2012-4-24 下午4:56:19 
 * @version 1.0
 */
public enum Operator {
	@EnumDescription("等于")
	@OperatorDescription(operator = "=")
	Equal,
	@OperatorDescription(operator = "<")
	@EnumDescription("小于")
	Less,
	@OperatorDescription(operator = ">")
	@EnumDescription("大于")
	Greater,
	@OperatorDescription(operator = "<=")
	@EnumDescription("小于等于")
	LessOrEqual,
	@OperatorDescription(operator = ">=")
	@EnumDescription("大于等于")
	GreaterOrEqua,
	@OperatorDescription(operator = "<>")
	@EnumDescription("不等于")
	NotEqual,
	@OperatorDescription(operator = "like", left = "%", right = "%")
	@EnumDescription("包含")
	Like,
	@OperatorDescription(operator = "not like", left = "%", right = "%")
	@EnumDescription("不包含")
	NotLike,
	@OperatorDescription(operator = "like ", right = "%")
	@EnumDescription("开头等于")
	LikeLeft,
	@OperatorDescription(operator = " like", left = "%")
	@EnumDescription("结尾等于")
	LikeRight,
	@OperatorDescription(operator = "not like ", right = "%")
	@EnumDescription("开头不等于")
	NotLikeLeft,
	@OperatorDescription(operator = " not like", left = "%")
	@EnumDescription("结尾不等于")
	NotLikeRight
}
