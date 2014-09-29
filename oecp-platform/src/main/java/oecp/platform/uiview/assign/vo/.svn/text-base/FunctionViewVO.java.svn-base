/**
 * oecp-platform - FunctionViewVO.java
 * copyright 2009-2012 OECP www.oecp.cn
 * 创建人:slx	创建时间:2011-11-2下午3:02:59		版本:v1
 * ============================================
 * 修改人：			修改时间:					版本:
 * 修改原因:
 *
 * ============================================
 */
package oecp.platform.uiview.assign.vo;

import oecp.framework.vo.base.BaseNormalVOUtility;
import oecp.framework.vo.base.DataVO;
import oecp.platform.uiview.assign.eo.FunctionView;
import oecp.platform.uiview.vo.UIComponentVO;

/**
 * 界面视图vo
 * @author slx
 * @date 2011-11-2
 */
public class FunctionViewVO extends FunctionView implements DataVO{

	private static final long serialVersionUID = 1L;
	private BaseNormalVOUtility util;
	private UIComponentVO head;
	private UIComponentVO body;
	private UIComponentVO foot;
	private String viewcompid;
	
	public FunctionViewVO() {
		util = new BaseNormalVOUtility(getClass());
		util.removeField("viewcomp");
	}
	public UIComponentVO getHead() {
		return head;
	}
	public void setHead(UIComponentVO head) {
		this.head = head;
	}
	public UIComponentVO getBody() {
		return body;
	}
	public void setBody(UIComponentVO body) {
		this.body = body;
	}
	public UIComponentVO getFoot() {
		return foot;
	}
	public void setFoot(UIComponentVO foot) {
		this.foot = foot;
	}
	public String getViewcompid() {
		return viewcompid;
	}
	public void setViewcompid(String viewcompid) {
		this.viewcompid = viewcompid;
	}
	public String[] getFieldNames() {
		return util.getFieldNames();
	}

	public Object getValue(String fieldname) {
		return util.getValue(this, fieldname);
	}

	public void setValue(String fieldname, Object value) {
		util.setValue(this, fieldname, value);
	}

	@Override
	public Class<?> getFieldType(String fieldname) {
		if("id".equals(fieldname)){
			return String.class;
		}
		return util.getFieldType(fieldname);
	}
	
	public static void main(String[] args) {
		FunctionViewVO vo = new FunctionViewVO();
		vo.setValue("test", "tttttt");
		for (int i = 0; i < vo.getFieldNames().length; i++) {
			System.out.println(vo.getFieldNames()[i] + " : " + vo.getFieldType(vo.getFieldNames()[i]));
		}
	}
}
