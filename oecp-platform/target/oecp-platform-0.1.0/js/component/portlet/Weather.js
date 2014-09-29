Ext.ns('OECP.portlet');

/**
 * 天气预报portlet
 */
OECP.portlet.Weather = Ext.extend(Ext.ux.Portlet,{
    width:350,
    html:'<iframe width="300" scrolling="no" height="300" frameborder="0" allowtransparency="true" src="http://i.tianqi.com/index.php?c=code&id=19&icon=1&temp=1&num=3"></iframe>'
});
Ext.reg('portlet_weather', OECP.portlet.Weather);