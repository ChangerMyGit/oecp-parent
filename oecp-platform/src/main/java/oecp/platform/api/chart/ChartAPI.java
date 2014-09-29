/**
 * oecp-platform - SimpleChartAPI.java
 * copyright 2009-2012 OECP www.oecp.cn
 * 创建人:slx	创建时间:2012-2-10下午2:18:06		版本:v1
 * ============================================
 * 修改人：			修改时间:					版本:
 * 修改原因:
 *
 * ============================================
 */
package oecp.platform.api.chart;

import java.util.List;

import org.apache.commons.lang.StringUtils;

/**
 * 简单图表API
 * @author slx
 * @date 2012-2-10
 */
public interface ChartAPI {
	/**
	 * 生成图表信息字符串
	 * @author slx
	 * @date 2012-2-10
	 * @param type
	 * 		图表类型枚举
	 * @param title
	 * 		标题
	 * @param datas
	 * 		数据集
	 * @param dataDesp
	 * 		数据结构描述
	 * @return
	 */
	public String generateSimpleChartString(ChartTypeEnum type ,String title,List<?> datas , DataDescription dataDesp);
	
	public interface ChartTypeEnum{
		public String getFactoryName();
	}
	
	/**
	 * 作为图表数据的结构描述类<br/>
	 * 例如数据结构如下的数据集：<br/><pre>
	 * area = 山东，month = 2月 ，value=9999
	 * area = ...，month = ... ，value=...
	 * 显示出下面的表格：
	 * ----------------------
	 * |地区     | 月份  |  数量    |
	 * |---------------------
	 * |山东     | 2月    | 9999  |
	 * |---------------------
	 * |....  |.... |....   |
	 * ----------------------
	 * “value”为统计值列，“month”为名称列，“area”为区域列，
	 * 他们对相应的title，分别是“数量”，“月份”，“地区”。
	 * </pre>
	 * <b>注意：数据集只能有2-3个列，“值”与“名称”列必须有，“区域”列可根据情况或有或无。
	 * @author slx
	 * @date 2012-2-10
	 */
	public class DataDescription {
		/** 作为图表数值使用的字段名 **/
		public String vField;
		/** 数值字段的标题 **/
		public String vTitle;
		/** 名称字段的字段名 **/
		public String nField;
		/** 名称字段的标题 **/
		public String nTitle;
		/** 区域字段的字段名 **/
		public String sField;
		/** 区域字段的字段名 **/
		public String sTitle;
		
		public DataDescription() {
		}
		public DataDescription(String vfield,String vTitle,String nField,String nTitle) {
			this(vfield, vTitle, nField, nTitle, null, null);
		}
		public DataDescription(String vField,String vTitle,String nField,String nTitle,String sField,String sTitle) {
			this.vField = StringUtils.isEmpty(vField)?null:vField;
			this.vTitle = StringUtils.isEmpty(vTitle)?null:vTitle;
			this.nField = StringUtils.isEmpty(nField)?null:nField;
			this.nTitle = StringUtils.isEmpty(nTitle)?null:nTitle;
			this.sField = StringUtils.isEmpty(sField)?null:sField;
			this.sTitle = StringUtils.isEmpty(sTitle)?null:sTitle;
		}
	}
}
