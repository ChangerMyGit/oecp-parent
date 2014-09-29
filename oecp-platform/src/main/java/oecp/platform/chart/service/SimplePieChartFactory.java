/**
 * oecp-platform - SimplePieChartFactory.java
 * copyright 2009-2012 OECP www.oecp.cn
 * 创建人:slx	创建时间:2012-2-16上午11:05:42		版本:v1
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
import oecp.platform.chart.vo.PieChart;
import oecp.platform.chart.vo.anychart.ChartNode;

/**
 * 简单饼图创建工厂
 * @author slx
 * @date 2012-2-16
 */
@Component("simplePieChartFactory")
public class SimplePieChartFactory extends AbstractSimpleChartFactory {

	/* (non-Javadoc)
	 * @see oecp.platform.chart.service.AbstractSimpleChartFactory#initChartConfig(java.lang.String, oecp.platform.api.chart.SimpleChartAPI.DataDescription)
	 */
	@Override
	protected AbstractChart initChartConfig(DataDescription dataDesp) {
		PieChart bar = new PieChart();
		ChartNode chart = bar.fristChart();
		
		// 设置图例显示
//		chart.setValue("chart_settings.legend.legend", true);
//		chart.setValue("chart_settings.legend.title.enabled", false);
//		chart.setValue("chart_settings.legend.title.text", "图例");
//		chart.setValue("chart_settings.legend.items.item.source", "Points");
		
		chart.setValue("data_plot_settings.enable_3d_mode", true); // 3D模式
		chart.setValue("data_plot_settings.pie_series.tooltip_settings.enabled", true); // 启用浮动提示
		chart.setValue("data_plot_settings.pie_series.tooltip_settings.format", dataDesp.sField!=null?"{%SeriesName}\n{%Name}\n{%YValue}":"{%Name}\n{%YValue}"); // 浮动提示内容
		chart.setValue("data_plot_settings.pie_series.label_settings.enabled", true); // 柱顶提示文字
		chart.setValue("data_plot_settings.pie_series.label_settings.format", "{%Name}"); // 提示显示内容
		
		chart.setValue("plot_type", "Pie"); // 柱状图
		
		return bar;
	}

	public static void main(String[] args) {
		SimplePieChartFactory f = new SimplePieChartFactory();
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
		String json = f.createChart("测试",datas,new DataDescription("value","能量值","area","覆盖区域","months","月份"));
		System.out.println(json);
	}
}
