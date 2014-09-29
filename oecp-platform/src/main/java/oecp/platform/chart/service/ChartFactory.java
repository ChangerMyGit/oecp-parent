/**
 * oecp-platform - ChartFactory.java
 * copyright 2009-2012 OECP www.oecp.cn
 * 创建人:slx	创建时间:2012-2-10下午2:46:03		版本:v1
 * ============================================
 * 修改人：			修改时间:					版本:
 * 修改原因:
 *
 * ============================================
 */
package oecp.platform.chart.service;

import java.util.List;

import oecp.platform.api.chart.ChartAPI.DataDescription;

/**
 * 图表工厂接口
 * @author slx
 * @date 2012-2-10
 */
public interface ChartFactory {
	/**
	 * 根据数据以及描述生成图表字符串。（不限制JSON和XML均使用此接口）
	 * @author slx
	 * @date 2012-2-15
	 * @param title
	 * @param datas
	 * @param dataDisp
	 * @return
	 */
	public String createChart(String title , List<? extends Object> datas, DataDescription dataDesp);
}
