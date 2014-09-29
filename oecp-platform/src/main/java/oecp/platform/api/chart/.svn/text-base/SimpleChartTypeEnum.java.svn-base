/**
 * oecp-platform - ChartTypeEnum.java
 * copyright 2009-2012 OECP www.oecp.cn
 * 创建人:slx	创建时间:2012-2-17上午9:09:09		版本:v1
 * ============================================
 * 修改人：			修改时间:					版本:
 * 修改原因:
 *
 * ============================================
 */
package oecp.platform.api.chart;

import oecp.framework.util.enums.EnumDescription;

/**
 * 简单图表类型
 * @author slx
 * @date 2012-2-17
 */
public enum SimpleChartTypeEnum implements oecp.platform.api.chart.ChartAPI.ChartTypeEnum {
	@EnumDescription("柱状图")
	Bar_Y("simpleBar_YChartFactory"),
	@EnumDescription("条形图")
	Bar_X("simpleBar_XChartFactory"),
	@EnumDescription("饼图")
	Pie("simplePieChartFactory"),
	@EnumDescription("折线图")
	Line("simpleLineChartFactory");

	private String value;
	private SimpleChartTypeEnum(String value) {
		this.value = value;
	}
	@Override
	public String getFactoryName() {
		return this.value;
	}

}
