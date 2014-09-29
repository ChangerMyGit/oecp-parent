Ext.ns('OECP.chart')

OECP.chart.AnyChart = Ext.extend(Ext.Panel, {
	chartData : {},
	chart : undefined,
	initComponent : function() {
		OECP.chart.AnyChart.superclass.initComponent.call(this);
		this.chart = new AnyChart(__ctxPath+'/ext-oecp/ui/chart/anychart/swf/AnyChart.swf',__ctxPath+'/ext-oecp/ui/chart/anychart/swf/Preloader.swf');
		this.chart.wMode = "transparent";
		this.chart.width = this.width;
		this.chart.height = this.height;
		this.chart.setJSONData(this.chartData);
		
		this.on('afterrender',function(){
			this.chart.write(this.id);
		});
	},
	setValues: function(data){
		this.chartData = data;
		this.chart.setJSONData(data);
	},
	getPNGImage:function(){
		return this.chart.getPNGImage();
	},
	saveAsImage:function(){
		return this.chart.saveAsImage();
	}
});
OECP.AnyChart = OECP.chart.AnyChart;
Ext.reg('oecpchart', OECP.chart.AnyChart);