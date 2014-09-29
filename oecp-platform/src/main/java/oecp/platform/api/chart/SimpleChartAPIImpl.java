/**
 * oecp-platform - SimpleChartAPIImpl.java
 * copyright 2009-2012 OECP www.oecp.cn
 * 创建人:slx	创建时间:2012-2-10下午2:42:40		版本:v1
 * ============================================
 * 修改人：			修改时间:					版本:
 * 修改原因:
 *
 * ============================================
 */
package oecp.platform.api.chart;

import java.util.List;

import oecp.framework.util.SpringContextUtil;
import oecp.platform.chart.service.ChartFactory;

import org.springframework.stereotype.Service;

/**
 * 简单图表API实现类
 * @author slx
 * @date 2012-2-10
 */
@Service("SimpleChartAPI")
public class SimpleChartAPIImpl implements ChartAPI {

	/* (non-Javadoc)
	 * @see oecp.platform.api.chart.SimpleChartAPI#generateSimpleChartString(oecp.platform.api.chart.SimpleChartAPI.ChartTypeEnum, java.lang.String, java.util.List, oecp.platform.api.chart.DataDescription)
	 */
	@Override
	public String generateSimpleChartString(ChartTypeEnum type, String title, List<?> datas, DataDescription dataDesp) {
		ChartFactory chartFactory = (ChartFactory)SpringContextUtil.getBean(type.getFactoryName());
		return chartFactory.createChart(title, datas, dataDesp);
	}

}
