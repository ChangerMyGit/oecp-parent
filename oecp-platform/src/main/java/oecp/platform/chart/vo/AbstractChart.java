/**
 * oecp-platform - Chart.java
 * copyright 2009-2012 OECP www.oecp.cn
 * 创建人:slx	创建时间:2012-2-10下午2:54:12		版本:v1
 * ============================================
 * 修改人：			修改时间:					版本:
 * 修改原因:
 *
 * ============================================
 */
package oecp.platform.chart.vo;

import java.util.ArrayList;
import java.util.List;

import oecp.platform.chart.vo.anychart.ChartNode;

/**
 * 图表实体抽象类
 * @author slx
 * @date 2012-2-10
 */
public abstract class AbstractChart {

	private ChartNode settings;
	private ChartNode charts;
	
	public AbstractChart() {
		this.charts = new ChartNode();
		List<ChartNode> cs = new ArrayList<ChartNode>();
		cs.add(new ChartNode());
		this.charts.setValue("chart", cs);
		
		settings = new ChartNode();
		settings.setValue("animation.enabled", true);
	}
	
	public ChartNode fristChart(){
		return ((List<ChartNode>)this.charts.getValue("chart")).get(0);
	}
	
	public ChartNode getSettings() {
		return settings;
	}
	public void setSettings(ChartNode settings) {
		this.settings = settings;
	}
	public ChartNode getCharts() {
		return charts;
	}
	public void setCharts(ChartNode charts) {
		this.charts = charts;
	}
}
