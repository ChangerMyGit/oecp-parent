package test.platform.uiview.service;

import java.text.NumberFormat;
import java.text.ParseException;
import java.util.List;
import java.util.Scanner;

import oecp.framework.dao.DAO;
import oecp.framework.dao.QLBuilder;
import oecp.platform.uiview.eo.UIComponent;
import test.platform.utils.TestBeanUtil;

public class UIComponentTest {

	private static DAO dao;
	
	public static void main(String[] args) throws Exception {
		System.out.println(Double.parseDouble("3/4"));
		System.out.println(new Double("3:4"));
		System.out.println(new Double(".8"));
		try {
			dao = (DAO) TestBeanUtil.getBean("platformDao");
			while(true){
				try {
					List<UIComponent> uis = dao.queryByWhere(UIComponent.class, " o.parent is null AND o.id NOT IN (SELECT v.viewcomp.id FROM FunctionView v WHERE v.viewcomp IS NOT NULL)) ", null);
					for (int i = 0; i < uis.size(); i++) {
						dao.delete(UIComponent.class,uis.get(i).getId());
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
				Scanner sc = new Scanner(System.in);
				sc.next();
			}
		} finally{
			TestBeanUtil.destroy();
		}
	}

}
