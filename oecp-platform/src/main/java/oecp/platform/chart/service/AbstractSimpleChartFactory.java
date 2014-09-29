package oecp.platform.chart.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;

import oecp.framework.entity.base.BaseEO;
import oecp.framework.util.FastJsonUtils;
import oecp.framework.util.ReflectionUtils;
import oecp.framework.vo.base.DataVO;
import oecp.platform.api.chart.ChartAPI.DataDescription;
import oecp.platform.chart.vo.AbstractChart;
import oecp.platform.chart.vo.anychart.ChartNode;

/**
 * 简单图表工厂的父类
 * <br/>将简单图表的创建步骤分解为两步：1.初始化图表配置。2.构建数据。
 * @author slx
 * @date 2012-2-15
 */
public abstract class AbstractSimpleChartFactory implements ChartFactory {

	@Override
	public String createChart(String title, List<? extends Object> datas, DataDescription dataDesp) {
		AbstractChart chart = initChartConfig(dataDesp);
		chart.fristChart().setValue("chart_settings.title.text", title); 
		
		ChartNode datanode = buildData(datas, dataDesp);
		chart.fristChart().setValue("data", datanode);
		return FastJsonUtils.toJson(chart);
	}

	/**
	 * 初始化图表配置
	 * @author slx
	 * @date 2012-2-15
	 * @param dataDisp
	 * @return
	 */
	protected abstract AbstractChart initChartConfig(DataDescription dataDesp);
	
	private static final String S_DEFAULT = "default";
	/**
	 * 构建数据节点
	 * @author slx
	 * @date 2012-2-15
	 * @param datas
	 * @param dataDisp
	 * @return
	 */
	protected ChartNode buildData(List<?> datas, DataDescription dataDesp){
		if(datas == null || datas.size()<1 || dataDesp.vField == null)
			return null;
		
		ChartNode dataNode = new ChartNode();
		// 将数据整理成 以series名称为key的list列表形式
		HashMap<String, List<?>> dataMap = new LinkedHashMap<String, List<?>>();
		if(dataDesp.sField != null){
			for (int i = 0; i < datas.size(); i++) {
				Object data = datas.get(i);
				String s = getValue(data,dataDesp.sField).toString();
				List dataList = dataMap.get(s);
				if(dataList == null){
					dataList = new ArrayList<Object>();
					dataMap.put(s, dataList);
				}
				dataList.add(datas.get(i));
			}
		}else{
			dataMap.put(S_DEFAULT, datas);
		}
		
		int sidx = 0;
		for (String key : dataMap.keySet()) {
			String spix = "series[" + sidx + "]";
			if(!key.equals(S_DEFAULT)){
				dataNode.setValue(spix + ".name", key);
			}
			List<?> dataList = dataMap.get(key);
			for (int i = 0; i < dataList.size(); i++) {
				Object row = dataList.get(i);
				Object name = getValue(row, dataDesp.nField);
				Object y = getValue(row, dataDesp.vField);
				
				String ppix = spix + ".point["+ i + "]";
				dataNode.setValue(ppix+".name", name);
				dataNode.setValue(ppix+".y", y);
			}
			sidx ++;
		}
		return dataNode;
	}
	
	protected Object getValue(Object obj,String field){
		if(obj instanceof DataVO){
			return ((DataVO)obj).getValue(field);
		}else if(obj instanceof BaseEO){
			return ((BaseEO<?>)obj).getAttributeValue(field);
		}else{
			return ReflectionUtils.getFieldValue(obj, field);
		}
	}
}
