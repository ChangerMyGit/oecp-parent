package test.platform.uiview.function.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import oecp.framework.dao.DAO;
import oecp.framework.exception.BizException;
import oecp.platform.bcfunction.eo.Function;
import oecp.platform.uiview.assign.service.FunctionViewService;
import oecp.platform.uiview.assign.vo.FunctionViewVO;
import oecp.platform.uiview.enums.ComponentType;
import oecp.platform.uiview.vo.UIComponentVO;
import oecp.platform.uiview.web.parser.ExtUIViewParser;
import oecp.platform.uiview.web.parser.UIViewParser;

import org.hibernate.Query;
import org.hibernate.Session;

import test.platform.utils.TestBeanUtil;

public class FunctionViewServiceTest {

	public static String funcviewid = null;
	public static FunctionViewVO funviewvo;
	public static FunctionViewService service;

	public static void testCreateFuncView() throws Exception {
//		service = (FunctionViewService) TestBeanUtil.getBean("functionViewServiceImpl");
//		funviewvo = newFunctionVO();
//		funviewvo.setFunc(new Function());
//		funviewvo.getFunc().setId("12");
//		service.saveViewVO(funviewvo);
		
	}

	public static void testFindFuncView() throws BizException {
		Session session = null;
		try {
			DAO dao = (DAO)TestBeanUtil.getBean("platformDao");
			session = dao.getHibernateSession().getSessionFactory().openSession();
			Query q = session.createQuery("SELECT count(o) FROM Function o  JOIN o.children o_children JOIN o.functionFields o_functionFields JOIN o.uis o_uis");
			Object o = q.uniqueResult();
			System.out.println(o);
		} finally {
			if (session != null) {
				session.close();
				session = null;
			}
		}
//		System.out.print("funviewid ：");
//		Scanner sc = new Scanner(System.in);
//		funcviewid = sc.next();
//		service = (FunctionViewService) TestBeanUtil.getBean("functionViewServiceImpl");
//		funviewvo = service.findViewVO(funcviewid);
//		System.out.println("查询结果");
//		System.out.println(funviewvo);
//		System.out.println("Form界面 : "+funviewvo.getViewcomp());
	}

	public static void testUpdateFuncView() throws Throwable {
		funviewvo.setViewname("slx修改测试");
		funviewvo.getBody().setTitle("slx修改body的title");
		funviewvo.setFoot(null);
		funviewvo.getHead().getChildren().get(0).setTitle("slx修改表头字段");
		funviewvo.getHead().getChildren().get(0).setValue("onform",false);
		service.saveViewVO(funviewvo);
	}

	public static void testExtParser() throws Throwable {
		service = (FunctionViewService) TestBeanUtil.getBean("functionViewServiceImpl");

		UIComponentVO uivo = null;
		System.out.println("1.List界面 2.Form界面 ");
		Scanner sc = new Scanner(System.in);
		if(sc.nextInt() == 1){
			uivo = service.getListPanelByViewID(funcviewid);
		}else{
			uivo = service.getFormPanelByViewID(funcviewid);
		}
		UIViewParser parser = new ExtUIViewParser();
		String extjs = parser.parserToSourceCode(uivo);
		System.out.println(extjs);
	}

	public static void main(String[] args) throws Throwable {
		try {
			int key = 1;
			while (key != 0) {
				try {
					System.out.println("当前FunctionViewID ：".concat(funcviewid==null?"无":funcviewid));
					System.out.println("请输入测试编号：1.查询 2.新建 3.更新 4.输出ext 5.删除 6.clone  0.退出测试");
					Scanner sc = new Scanner(System.in);
					key = sc.nextInt();
					runTest(key);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		} finally {
			TestBeanUtil.destroy();
		}
	}

	public static void runTest(int testkey) throws Throwable {
		switch (testkey) {
		case 1:
			testFindFuncView();
			break;
		case 2:
			testCreateFuncView();
			break;
		case 3:
			testUpdateFuncView();
			break;
		case 4:
			testExtParser();
			break;
		case 5:
			service.delete(funcviewid);
			break;
		case 6:
			service.cloneViewByID(funcviewid, "testclone001", "克隆测试", null, true);
			break;

		default:
			testFindFuncView();
			break;
		}
		
	}

	private static FunctionViewVO newFunctionVO() {
		FunctionViewVO viewvo = new FunctionViewVO();
		viewvo.setViewcode("test001");
		viewvo.setViewname("测试功能视图001");
		UIComponentVO head = new UIComponentVO();
		viewvo.setHead(head);
		UIComponentVO body = new UIComponentVO();
		viewvo.setBody(body);
		UIComponentVO foot = new UIComponentVO();
		viewvo.setFoot(foot);

		head.setCols(2);
		head.setHeight(200);
		head.setWidth(800);
		head.setType(ComponentType.Panel);
		head.setIdx(0);
		head.setTitle("表头panel");
		head.setChildren(new ArrayList<UIComponentVO>());
		UIComponentVO headf01 = new UIComponentVO();
		headf01.setTitle("字段1");
		headf01.setType(ComponentType.Field);
		headf01.setCancommit(true);
		headf01.setIdx(0);
		headf01.setValue("onform", true);
		headf01.setValue("onlist", true);
		head.getChildren().add(headf01);
		UIComponentVO headf02 = new UIComponentVO();
		headf02.setTitle("字段2");
		headf02.setType(ComponentType.Field);
		headf02.setCancommit(true);
		headf02.setIdx(1);
		headf02.setColspan(1);
		headf02.setValue("onform", true);
		headf02.setValue("onlist", true);
		head.getChildren().add(headf02);
		UIComponentVO headf03 = new UIComponentVO();
		headf03.setTitle("字段3");
		headf03.setType(ComponentType.Field);
		headf03.setCancommit(true);
		headf03.setIdx(2);
		headf03.setColspan(2);
		headf03.setValue("onform", true);
		headf03.setValue("onlist", true);
		head.getChildren().add(headf03);

		body.setHeight(200);
		body.setWidth(800);
		body.setType(ComponentType.Grid);
		body.setIdx(0);
		body.setTitle("表体grid");
		body.setChildren(new ArrayList<UIComponentVO>());
		UIComponentVO bodyf01 = new UIComponentVO();
		bodyf01.setTitle("字段1");
		bodyf01.setType(ComponentType.Field);
		bodyf01.setCancommit(true);
		bodyf01.setIdx(0);
		bodyf01.setValue("onform", true);
		bodyf01.setValue("onlist", true);
		body.getChildren().add(bodyf01);
		UIComponentVO bodyf02 = new UIComponentVO();
		bodyf02.setTitle("字段2");
		bodyf02.setType(ComponentType.Field);
		bodyf02.setCancommit(true);
		bodyf02.setIdx(1);
		bodyf02.setColspan(1);
		bodyf02.setValue("onform", true);
		bodyf02.setValue("onlist", true);
		body.getChildren().add(bodyf02);
		UIComponentVO bodyf03 = new UIComponentVO();
		bodyf03.setTitle("字段3");
		bodyf03.setType(ComponentType.Field);
		bodyf03.setCancommit(true);
		bodyf03.setIdx(2);
		bodyf03.setColspan(2);
		bodyf03.setValue("onform", true);
		bodyf03.setValue("onlist", true);
		body.getChildren().add(bodyf03);

		foot.setHeight(100);
		foot.setCols(3);
		foot.setWidth(800);
		foot.setType(ComponentType.Panel);
		foot.setIdx(0);
		foot.setTitle("表尾panel");
		foot.setChildren(new ArrayList<UIComponentVO>());
		UIComponentVO footf01 = new UIComponentVO();
		footf01.setTitle("字段1");
		footf01.setType(ComponentType.Field);
		footf01.setCancommit(true);
		footf01.setIdx(0);
		footf01.setValue("onform", true);
		footf01.setValue("onlist", true);
		foot.getChildren().add(footf01);
		UIComponentVO footf02 = new UIComponentVO();
		footf02.setTitle("字段2");
		footf02.setType(ComponentType.Field);
		footf02.setCancommit(true);
		footf02.setIdx(1);
		footf02.setColspan(1);
		footf02.setValue("onform", true);
		footf02.setValue("onlist", true);
		foot.getChildren().add(footf02);
		UIComponentVO footf03 = new UIComponentVO();
		footf03.setTitle("字段3");
		footf03.setType(ComponentType.Field);
		footf03.setCancommit(true);
		footf03.setIdx(2);
		footf03.setColspan(2);
		footf03.setValue("onform", true);
		footf03.setValue("onlist", true);
		foot.getChildren().add(footf03);

		return viewvo;
	}
}
