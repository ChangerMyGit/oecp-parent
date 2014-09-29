/**
 * oecp-platform - SimpleLineChartFactory.java
 * copyright 2009-2012 OECP www.oecp.cn
 * 创建人:slx	创建时间:2012-2-16下午2:23:05		版本:v1
 * ============================================
 * 修改人：			修改时间:					版本:
 * 修改原因:
 *
 * ============================================
 */
package oecp.platform.chart.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Component;

import oecp.framework.vo.base.DataVO;
import oecp.framework.vo.base.SimpleDataVO;
import oecp.platform.api.chart.ChartAPI.DataDescription;
import oecp.platform.chart.vo.AbstractChart;
import oecp.platform.chart.vo.LineChart;
import oecp.platform.chart.vo.anychart.ChartNode;

/**
 * 简单线形图工厂
 * @author slx
 * @date 2012-2-16
 */
@Component("simpleLineChartFactory")
public class SimpleLineChartFactory extends AbstractSimpleChartFactory {

	/* (non-Javadoc)
	 * @see oecp.platform.chart.service.AbstractSimpleChartFactory#initChartConfig(oecp.platform.api.chart.SimpleChartAPI.DataDescription)
	 */
	@Override
	protected AbstractChart initChartConfig(DataDescription dataDesp) {
		LineChart line = new LineChart();
		ChartNode chart = line.fristChart();
		
		// 设置XY轴标题
		chart.setValue("chart_settings.axes.x_axis.title.enabled", true); 
		chart.setValue("chart_settings.axes.x_axis.title.text", dataDesp.nTitle);
		chart.setValue("chart_settings.axes.y_axis.title.enabled", true); 
		chart.setValue("chart_settings.axes.y_axis.title.text", dataDesp.vTitle);
		
		// 设置图例显示
		chart.setValue("chart_settings.legend.legend", true);
		chart.setValue("chart_settings.legend.icon.type", "Box");
		chart.setValue("chart_settings.legend.title.enabled", false);
		
		chart.setValue("data_plot_settings.line_series.tooltip_settings.enabled", true); // 启用浮动提示
		chart.setValue("data_plot_settings.line_series.tooltip_settings.format", dataDesp.sField!=null?"{%SeriesName}\n{%YValue}":"{%YValue}"); // 浮动提示内容
		chart.setValue("data_plot_settings.line_series.label_settings.enabled", true); // 柱顶提示文字
		chart.setValue("data_plot_settings.line_series.label_settings.format", "{%YValue}"); // 提示显示内容
		
		chart.setValue("plot_type", "CategorizedVertical"); // 纵轴为数值的二维坐标图
		chart.setValue("data_plot_settings.default_series_type", "Spline"); // 线形图
		
		return line;
	}

	public static void main(String[] args) {
		SimpleLineChartFactory f = new SimpleLineChartFactory();
		List<DataVO> datas = new ArrayList<DataVO>();
		DataVO vo11 = new SimpleDataVO();vo11.setValue("area", "山东");vo11.setValue("value", 100);vo11.setValue("months", "一月");
		DataVO vo12 = new SimpleDataVO();vo12.setValue("area", "北京");vo12.setValue("value", 180);vo12.setValue("months", "一月");
		DataVO vo13 = new SimpleDataVO();vo13.setValue("area", "河北");vo13.setValue("value", 130);vo13.setValue("months", "一月");
		DataVO vo21 = new SimpleDataVO();vo21.setValue("area", "山东");vo21.setValue("value", 110);vo21.setValue("months", "二月");
		DataVO vo22 = new SimpleDataVO();vo22.setValue("area", "北京");vo22.setValue("value", 160);vo22.setValue("months", "二月");
		DataVO vo23 = new SimpleDataVO();vo23.setValue("area", "河北");vo23.setValue("value", 150);vo23.setValue("months", "二月");
		DataVO vo31 = new SimpleDataVO();vo31.setValue("area", "山东");vo31.setValue("value", 180);vo31.setValue("months", "三月");
		DataVO vo32 = new SimpleDataVO();vo32.setValue("area", "北京");vo32.setValue("value", 120);vo32.setValue("months", "三月");
		DataVO vo33 = new SimpleDataVO();vo33.setValue("area", "河北");vo33.setValue("value", 140);vo33.setValue("months", "三月");
		datas.add(vo11);
		datas.add(vo12);
		datas.add(vo13);
		datas.add(vo21);
		datas.add(vo22);
		datas.add(vo23);
		datas.add(vo31);
		datas.add(vo32);
		datas.add(vo33);
		String json = f.createChart("测试",datas,new DataDescription("value","能量值","months","月份","area","覆盖区域"));
		System.out.println(json);
	}
}
