/**
 * oecp-platform - UIViewParser.java
 * copyright 2009-2012 OECP www.oecp.cn
 * 创建人:slx	创建时间:2011-11-7上午10:41:16		版本:v1
 * ============================================
 * 修改人：			修改时间:					版本:
 * 修改原因:
 *
 * ============================================
 */

package oecp.platform.uiview.utils;

import java.util.ArrayList;
import java.util.List;

import oecp.platform.uiview.eo.SpecialAttribute;
import oecp.platform.uiview.eo.UIComponent;
import oecp.platform.uiview.vo.UIComponentVO;

import org.springframework.beans.BeanUtils;

/** 
 * ui组件对象工具类
 * @author slx  
 * @date 2012-4-28 下午1:56:39 
 * @version 1.0
 *  
 */
public class UIComponentObjUtils {
	
	/**
	 * 将UI控件的"VO"对象，转换为"EO"对象
	 * @author slx
	 * @date 2012-4-28下午2:35:43
	 * @param vo
	 * @return
	 */
	public static UIComponent transfer2UIEO(UIComponentVO vo){
		UIComponent eo = new UIComponent();
		// 将vo中的字段拷贝到eo中，保证items和attrs不被覆盖
		BeanUtils.copyProperties(vo, eo, new String[] { "items", "attrs" });
		// 处理扩展属性
		List<SpecialAttribute> spattrs = eo.getAttrs();
		if (spattrs == null) { // eo的扩展属性。如果为空，则初始一个list。
			spattrs = new ArrayList<SpecialAttribute>();
			eo.setAttrs(spattrs);
		}
		String[] vospattrnames = vo.getSpecialAttrNames();
		String spattrname = null;
		boolean deleted = true;
		for (int i = 0; i < spattrs.size(); i++) {
			spattrname = spattrs.get(i).getAttrname();
			deleted = true;
			for (int j = 0; j < vospattrnames.length; j++) {
				// 找到相同的赋值，清掉已赋值的名称，跳出内循环
				if (spattrname.equals(vospattrnames[j])) {
					spattrs.get(i).setAttrvalue(vo.getValue(spattrname) == null ? null : vo.getValue(spattrname).toString());
					vospattrnames[j] = null;
					deleted = false;
					break;
				}
			}
			// 内循环后没找到的清掉自己
			if (deleted) {
				spattrs.remove(i);
				i--;
			}
		}
		// vo的扩展属性名称列表中，不为空的名称字段即是新增的，因为相同的已经在前面的循环中被清空。
		for (int i = 0; i < vospattrnames.length; i++) {
			if (vospattrnames[i] != null && vo.getValue(vospattrnames[i]) != null) {
				SpecialAttribute attr = new SpecialAttribute();
				attr.setComp(eo);
				attr.setAttrname(vospattrnames[i]);
				attr.setAttrvalue(vo.getValue(vospattrnames[i]).toString());
				eo.getAttrs().add(attr);
			}
		}

		// 处理内部子对象
		if (vo.getChildren() != null && vo.getChildren().size() > 0) {
			if (eo.getItems() == null) {
				eo.setItems(new ArrayList<UIComponent>());
			} else {
				eo.getItems().clear();
			}
			for (int i = 0; i < vo.getChildren().size(); i++) {
				// 递归调用。用vo的子对象创建子eo，并加到当前eo的items中。
				UIComponent itemeo = transfer2UIEO(vo.getChildren().get(i));
				itemeo.setParent(eo);
				eo.getItems().add(itemeo);
			}
		}else{
			if(eo.getItems()!=null)
				eo.getItems().clear();
		}

		return eo;
	}
	
	/**
	 * 将UI控件的"EO"对象，转换为"VO"对象
	 * @author slx
	 * @date 2012-4-28下午2:35:00
	 * @param eo
	 * @return
	 */
	public static UIComponentVO transfer2UIVO(UIComponent eo){
		if (eo == null) {
			return null;
		}
		UIComponentVO vo = new UIComponentVO();
		BeanUtils.copyProperties(eo, vo); // 先拷贝公有属性
		// 复制eo中的特殊属性到vo
		List<SpecialAttribute> otherattrs = eo.getAttrs();
		if (otherattrs != null) {
			for (SpecialAttribute attr : otherattrs) {
				vo.setValue(attr.getAttrname(), attr.getAttrvalue());
			}
		}
		// 递归创建子元素
		List<UIComponent> items = eo.getItems();
		if (items != null && items.size() > 0) {
			int len = items.size();
			vo.setChildren(new ArrayList<UIComponentVO>(len));
			for (int i = 0; i < len; i++) {
				vo.getChildren().add(transfer2UIVO(items.get(i)));
			}
		}
		return vo;
	}
}
