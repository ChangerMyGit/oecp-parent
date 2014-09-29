package oecp.platform.chart.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Component;

import oecp.framework.vo.base.DataVO;
import oecp.framework.vo.base.SimpleDataVO;
import oecp.platform.api.chart.ChartAPI.DataDescription;
import oecp.platform.chart.vo.AbstractChart;
import oecp.platform.chart.vo.BarChart;
import oecp.platform.chart.vo.anychart.ChartNode;

/**
 * 简单柱状图工厂
 * 
 * @author slx
 * @date 2012-2-15
 */
@Component("simpleBar_YChartFactory")
public class SimpleBar_YChartFactory extends AbstractSimpleChartFactory {

	@Override
	protected AbstractChart initChartConfig(DataDescription dataDesp) {
		BarChart bar = new BarChart();
		ChartNode chart = bar.fristChart();
		
		// 设置XY轴标题
		chart.setValue("chart_settings.axes.x_axis.title.enabled", true); 
		chart.setValue("chart_settings.axes.x_axis.title.text", dataDesp.nTitle);
		chart.setValue("chart_settings.axes.y_axis.title.enabled", true); 
		chart.setValue("chart_settings.axes.y_axis.title.text", dataDesp.vTitle);
		
		chart.setValue("data_plot_settings.enable_3d_mode", true); // 3D模式
		chart.setValue("data_plot_settings.bar_series.tooltip_settings.enabled", true); // 启用浮动提示
		chart.setValue("data_plot_settings.bar_series.tooltip_settings.format", dataDesp.sField!=null?"{%SeriesName}\n{%YValue}":"{%YValue}"); // 浮动提示内容
		chart.setValue("data_plot_settings.bar_series.label_settings.enabled", true); // 柱顶提示文字
		chart.setValue("data_plot_settings.bar_series.label_settings.format", dataDesp.sField!=null?"{%SeriesName}\n{%YValue}":"{%YValue}"); // 提示显示内容
		
		chart.setValue("plot_type", "CategorizedVertical"); // 纵轴为数值的二维坐标图
		
		return bar;
	}
	
	public static void main(String[] args) {
		SimpleBar_YChartFactory f = new SimpleBar_XChartFactory();
//		SimpleBar_YChartFactory f = new SimpleBar_YChartFactory();
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
