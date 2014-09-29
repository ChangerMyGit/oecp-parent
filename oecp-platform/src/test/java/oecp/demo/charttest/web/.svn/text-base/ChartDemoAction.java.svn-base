package oecp.demo.charttest.web;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;

import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.Namespace;
import org.apache.struts2.convention.annotation.ParentPackage;
import org.apache.struts2.convention.annotation.Result;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import oecp.framework.vo.base.DataVO;
import oecp.framework.vo.base.SimpleDataVO;
import oecp.platform.api.chart.ChartAPI;
import oecp.platform.api.chart.ChartAPI.DataDescription;
import oecp.platform.api.chart.SimpleChartTypeEnum;
import oecp.platform.web.BasePlatformAction;
@Controller("chartDemoAction")
@Scope("prototype")
@ParentPackage("default-package")
@Namespace("/demo/chart")
public class ChartDemoAction extends BasePlatformAction {

	private static final long serialVersionUID = 1L;

	@Resource(name="SimpleChartAPI")
	private ChartAPI chartAPI;

	@Action("line")
	public String getLineChart(){
		chartJson = chartAPI.generateSimpleChartString(SimpleChartTypeEnum.Line, "线形图演示", getData(), new DataDescription("value","短信发送数","months","月份","area","覆盖区域"));
		setJsonString(chartJson);
		return SUCCESS;
	}
	
	private String chartJson ;
	@Action(value="demo",results={@Result(name = "success",location = "/js/demo/chart/anychartDemo.jsp")})
	public String demo(){
		chartJson = chartAPI.generateSimpleChartString(SimpleChartTypeEnum.Bar_Y, "柱状图演示", getData(), new DataDescription("value","短信发送数","months","月份","area","覆盖区域"));
		return SUCCESS;
	}
	public List<DataVO> getData(){
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
		
		return datas;
	}
	
	public void setChartAPI(ChartAPI chartAPI) {
		this.chartAPI = chartAPI;
	}
	public String getChartJson() {
		return chartJson;
	}
	public void setChartJson(String chartJson) {
		this.chartJson = chartJson;
	}
}
