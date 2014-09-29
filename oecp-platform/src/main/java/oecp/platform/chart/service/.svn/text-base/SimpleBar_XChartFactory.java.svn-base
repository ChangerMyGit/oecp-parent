/**
 * oecp-platform - SimpleBar_XChartFactory.java
 * copyright 2009-2012 OECP www.oecp.cn
 * 创建人:slx	创建时间:2012-2-16上午10:57:27		版本:v1
 * ============================================
 * 修改人：			修改时间:					版本:
 * 修改原因:
 *
 * ============================================
 */
package oecp.platform.chart.service;

import org.springframework.stereotype.Component;

import oecp.platform.api.chart.ChartAPI.DataDescription;
import oecp.platform.chart.vo.AbstractChart;

/**
 * 横向条形图工厂
 * @author slx
 * @date 2012-2-16
 */
@Component("simpleBar_XChartFactory")
public class SimpleBar_XChartFactory extends SimpleBar_YChartFactory {

	@Override
	protected AbstractChart initChartConfig(DataDescription dataDesp) {
		AbstractChart bar = super.initChartConfig(dataDesp);
		bar.fristChart().setValue("plot_type", "CategorizedHorizontal"); // 横轴为数值的二维坐标图
		bar.fristChart().setValue("data_plot_settings.bar_series.label_settings.format", dataDesp.sField!=null?"{%SeriesName} {%YValue}":"{%YValue}"); // 提示显示内容
		return bar;
	}
}
