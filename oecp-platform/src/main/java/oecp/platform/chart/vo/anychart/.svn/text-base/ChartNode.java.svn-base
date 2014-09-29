/**
 * oecp-platform - ChartNode.java
 * copyright 2009-2012 OECP www.oecp.cn
 * 创建人:slx	创建时间:2012-2-14上午10:06:09		版本:v1
 * ============================================
 * 修改人：			修改时间:					版本:
 * 修改原因:
 *
 * ============================================
 */
package oecp.platform.chart.vo.anychart;

import java.util.ArrayList;
import java.util.List;

import oecp.framework.vo.base.SimpleDataVO;

/**
 * 图表配置节点
 * 
 * @author slx
 * @date 2012-2-14
 * @情人节 纪念！为了这特殊的日子里写下的平凡的代码！ O(∩_∩)O哈哈~
 */
public class ChartNode extends SimpleDataVO {

	/**
	 * 可以用类似aaa.bbb[0].ccc的fieldname，来setValue。
	 * 如果访问最后一级属性也就是ccc的过程中，aaa或者bbb为空，则自动创建它们
	 * ，使用ChartNode对象或者ArrayList<ChartNode>对象。
	 */
	@Override
	public void setValue(String fieldname, Object value) {
		AttributeName name = new AttributeName(fieldname);
		if (name.isLastLevel()) { // 如果是最后一级，直接赋值
			super.setValue(fieldname, value);
		} else { // 否则取出内部对象，为内部对象赋值。内部对象只可能是ChartNode或者ArrayList<ChartNode>
			Object childNode = this.getValue(name.getRealCurrentName());
			if (childNode == null) { // 取到的中间对象为空，则创建之。
				if (name.isList()) {
					childNode = new ArrayList<ChartNode>();
				} else {
					childNode = new ChartNode();
				}
				super.setValue(name.getRealCurrentName(), childNode);
			}
			if (childNode instanceof List) { // 如果是list 则根据下标取出内部的ChartNode
				int listsize = ((List<ChartNode>) childNode).size();
				if(listsize <= name.getCurrentIndex()){
					while (((List<ChartNode>) childNode).size() <= name.getCurrentIndex()) {
						((List<ChartNode>) childNode).add(new ChartNode());
					}
				}
				Object element = ((List<ChartNode>) childNode).get(name.getCurrentIndex());
				childNode = element;
			}
			((ChartNode) childNode).setValue(name.innerNames, value);
		}
	}

	/**
	 * 属性名称类，描述字符串属性名称中，当前vo中属性和内部对象的属性。 aaa.bbb[0].ccc currentName =
	 * "aaa";innerNames="bbb[0].ccc"
	 */
	class AttributeName {
		/** 当前对象内的属性 **/
		String currentName = null;
		/** 内部属性 **/
		String innerNames = null;

		public AttributeName(String attrnames) {
			if (attrnames != null) {
				int p = attrnames.indexOf('.');
				if (p != -1) {
					currentName = attrnames.substring(0, p);
					innerNames = attrnames.substring(p+1);
				} else {
					currentName = attrnames;
					innerNames = null;
				}
			}
		}

		/**
		 * 当前对象真实的属性名，针对于待下标的属性，取“[”前面的部分。
		 **/
		String getRealCurrentName() {
			int p = currentName.indexOf("[");
			if (p != -1)
				return currentName.substring(0, currentName.indexOf("["));
			else
				return currentName;
		}

		/**
		 * 获取当前List属性的下标
		 * 
		 * @return
		 */
		int getCurrentIndex() {
			if (isList()) {
				int start = currentName.indexOf("[")+1;
				int end = currentName.indexOf("]");
				String s_idx = currentName.substring(start, end);
				return Integer.parseInt(s_idx);
			}
			return -1;
		}

		/**
		 * 判断当前属性是否是List
		 * 
		 * @return
		 */
		boolean isList() {
			return currentName.indexOf("[") != -1;
		}

		/**
		 * 判断当前属性最后一级
		 * 
		 * @return
		 */
		boolean isLastLevel() {
			return innerNames == null;
		}
	}
}
