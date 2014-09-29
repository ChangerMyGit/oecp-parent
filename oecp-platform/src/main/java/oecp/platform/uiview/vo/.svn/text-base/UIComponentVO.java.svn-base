/**
 * oecp-platform - UIComponentVO.java
 * copyright 2009-2012 OECP www.oecp.cn
 * 创建人:slx	创建时间:2011-11-1下午2:20:27		版本:v1
 * ============================================
 * 修改人：			修改时间:					版本:
 * 修改原因:
 *
 * ============================================
 */
package oecp.platform.uiview.vo;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.persistence.Transient;

import oecp.framework.vo.base.DataVO;
import oecp.platform.uiview.eo.UIComponent;

/**
 * UI控件vo
 * </br> 将竖存储的特殊属性名也加入与普通的属性同样使用。
 * @author slx
 * @date 2011-11-1
 */
public class UIComponentVO extends UIComponent implements DataVO{

	private static final long serialVersionUID = 1L;
	
	private Map<String, Object> specialattrs = new HashMap<String, Object>();
	private Set<String> fieldNames = new HashSet<String>();
	private String[] finalfieldNames = new String[0];
	
	private List<UIComponentVO> children;
	
	public UIComponentVO() {
		List<String> temp_ffname = Arrays.asList(this.getAttributeNames());
		temp_ffname = new ArrayList<String>(temp_ffname);
		temp_ffname.remove("attrs");
		temp_ffname.remove("items");
		temp_ffname.remove("parent");
		temp_ffname.add("children");
		finalfieldNames = (String[])temp_ffname.toArray(finalfieldNames);
	}
	public List<UIComponentVO> getChildren() {
		return children;
	}
	public void setChildren(List<UIComponentVO> children) {
		this.children = children;
	}

	/**
	 * 获得所有字段名
	 */
	@Override
	@Transient
	public String[] getFieldNames() {
		fieldNames.clear();
		addAttrNames(getFinalAttrNames());
		addAttrNames(getSpecialAttrNames());
		
		return (String[])fieldNames.toArray(new String[0]);
	}
	@Transient
	private void addAttrNames(String[] names){
		for (String fieldname : names) {
			fieldNames.add(fieldname);
		}
	}
	/**
	 * 获得固定字段名
	 * @return
	 */
	@Transient
	public String[] getFinalAttrNames(){
		return finalfieldNames;
	}
	/**
	 * 获得特殊字段名
	 * @return
	 */
	@Transient
	public String[] getSpecialAttrNames(){
		return (String[])specialattrs.keySet().toArray(new String[0]);
	}

	protected boolean isFinalAttr(String attrname){
		for (String finalname : getFinalAttrNames()) {
			if(attrname.equals(finalname)){
				return true;
			}
		}
		return false;
	}
	@Override
	@Transient
	public Object getValue(String attrname) {
		if(isFinalAttr(attrname)){
			return super.getAttributeValue(attrname);
		}else{
			return specialattrs.get(attrname);
		}
	}

	@Override
	@Transient
	public void setValue(String attrname, Object value) {
		if(isFinalAttr(attrname)){
			super.setAttributeValue(attrname,value);
		}else{
			if(value == null ){
				if(specialattrs.containsKey(attrname))
					specialattrs.remove(attrname);
			}else{
				specialattrs.put(attrname,value);
			}
		}
	}

	@Override
	@Transient
	public Class getFieldType(String attrname) {
		
		try {
			if("id".equals(attrname))
				return String.class;
			return this.getBeanUtility().getAttributeType(attrname);
		} catch (Exception e) {
			return String.class;
		}
	}
	
	public static void main(String[] args) {
		UIComponentVO vo = new UIComponentVO();
		vo.setValue("test", "tttttt");
		for (int i = 0; i < vo.getFieldNames().length; i++) {
			System.out.println(vo.getFieldNames()[i] + " : " + vo.getFieldType(vo.getFieldNames()[i]));
		}
	}
}
