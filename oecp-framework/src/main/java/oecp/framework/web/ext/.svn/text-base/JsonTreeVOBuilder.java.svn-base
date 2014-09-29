/************************* 版权声明 *********************************
 *                                                                  *
 *                     版权所有：百洋软件                          				 	*
 *          Copyright (c) 2011 by www.oecp.cn                	*
 *                                                                  *
 ************************* 变更记录 *********************************
 *
 * 创建者：宋黎晓   创建日期： 2011 5 20
 * 备注：
 * 
 * 修改者：       修改日期：
 * 备注：
 * 
 */

package oecp.framework.web.ext;

import java.util.ArrayList;
import java.util.List;

import oecp.framework.util.ReflectionUtils;
import oecp.framework.util.enums.EnumDescription;

/**
 * JsonTreeVO构建类。 </br> 提供将EO和EO列表转换为JsonTreeVO和JsonTreeVO列表的工具方法。
 * 
 * @author slx
 * @date 2011 5 20 17:15:58
 * @version 1.0
 */
public class JsonTreeVOBuilder {

	/**
	 * 将一个带有上下级结构的eo列表构建成JsonTreeVO列表。
	 * （EO列表中只需要包含几个顶级EO。另外，顶级EO下面的子EO不能为空，否则构建完成后将没有下级）。 是否叶子节点是根据是否还有下级来设置的。
	 * </br> 注意：<b>此方法与JsonTreeVO getTreeVOFromEO()方法相互递归。</b>
	 * 
	 * @author slx
	 * @date 2011 5 20 17:09:30
	 * @modifyNote
	 * @param eos
	 * @param idField
	 * @param textField
	 * @param childField
	 * @param cs
	 *            CheckShow
	 * @return
	 */
	public static List<JsonTreeVO> getTreeVOsFromEOs(List eos, String idField,
			String textField, String childField, CheckShow cs) {
		if (eos == null || eos.size() == 0) {
			return null;
		}
		int size = eos.size();
		List<JsonTreeVO> vos = new ArrayList<JsonTreeVO>(size);
		for (int i = 0; i < size; i++) {
			JsonTreeVO vo = getTreeVOFromEO(eos.get(i), idField, textField,
					childField, cs);
			vos.add(vo);
		}
		return vos;
	}

	/**
	 * 将一个EO转换为一个JsonTreeVO同List<JsonTreeVO> getTreeVOsFromEOs方法。
	 * 
	 * @author slx
	 * @date 2011 5 20 17:12:36
	 * @modifyNote
	 * @param eo
	 * @param idField
	 * @param textField
	 * @param childField
	 * @param cs
	 * @return
	 */
	public static JsonTreeVO getTreeVOFromEO(Object eo, String idField,
			String textField, String childField, CheckShow cs) {
		JsonTreeVO vo = new JsonTreeVO();
		vo.setId(ReflectionUtils.getFieldValue(eo, idField).toString());
		vo.setText(ReflectionUtils.getFieldValue(eo, textField).toString());
		switch (cs) {
		case CHECK:
			vo.setChecked(true);
			break;
		case NOCHECK:
			vo.setChecked(false);
			break;
		default:
			break;
		}
		List childrens = (List) ReflectionUtils.getFieldValue(eo, childField);
		if (childrens != null && childrens.size() > 0) {
			vo.setLeaf(false);
			vo.setChildren(getTreeVOsFromEOs(childrens, idField, textField,
					childField, cs));
		} else {
			vo.setLeaf(true);
		}

		return vo;
	}

	/**
	 * 通过最末级的叶子节点列表反向构建树
	 * 
	 * @author yongtree
	 * @date 2011-6-16 下午01:50:00
	 * @param leafs
	 *            末级叶子对象列表
	 * @param idField
	 *            要转化成tree id字段
	 * @param textField
	 *            要转化成tree text字段
	 * @param parentField
	 *            父级字段
	 * @return
	 */
	public static List<JsonTreeVO> reverseBuildTree(List leafs, String idField,
			String textField, String parentField, CheckShow cs) {
		List<JsonTreeVO> list = new ArrayList<JsonTreeVO>();
		for (int i = 0; i < leafs.size(); i++) {
			JsonTreeVO vo = new JsonTreeVO();
			vo.setId(ReflectionUtils.getFieldValue(leafs.get(i), idField)
					.toString());
			vo.setText(ReflectionUtils.getFieldValue(leafs.get(i), textField)
					.toString());
			vo.setLeaf(true);
			switch (cs) {
			case CHECK:
				vo.setChecked(true);
				break;
			case NOCHECK:
				vo.setChecked(false);
				break;
			default:
				break;
			}
			list.add(vo);
			Object po = ReflectionUtils
					.getFieldValue(leafs.get(i), parentField);
			if (po != null) {
				reverseBuildTree(list, vo, po, idField, textField, parentField,
						cs);
			}
		}
		return list;
	}

	private static void reverseBuildTree(List<JsonTreeVO> list,
			JsonTreeVO childTreeNode, Object o, String idField,
			String textField, String parentField, CheckShow cs) {
		list.remove(childTreeNode);
		// 先从list查找有无该node
		JsonTreeVO vo = null;
		for (JsonTreeVO node : list) {
			if (node.getId().equals(
					ReflectionUtils.getFieldValue(o, idField).toString()))
				vo = node;
		}
		if (vo == null) {
			vo = new JsonTreeVO();
			vo.setId(ReflectionUtils.getFieldValue(o, idField).toString());
			vo.setText(ReflectionUtils.getFieldValue(o, textField).toString());
			vo.setLeaf(false);
			switch (cs) {
			case CHECK:
				vo.setChecked(true);
				break;
			case NOCHECK:
				vo.setChecked(false);
				break;
			default:
				break;
			}
		}
		vo.getChildren().add(childTreeNode);
		list.add(vo);
		Object po = ReflectionUtils.getFieldValue(o, parentField);
		if (po != null) {
			reverseBuildTree(list, vo, po, idField, textField, parentField, cs);
		}
	}

	public enum CheckShow {
		/* 不显示 */
		NONE,
		/* 显示被选中 */
		CHECK,
		/* 显示未被选中 */
		NOCHECK
	}
}
