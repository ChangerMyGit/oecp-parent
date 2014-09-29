package test.platform.utils;

import java.util.ArrayList;

import org.springframework.context.support.AbstractApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

/**
 * 测试用的Spring配置文件加载器
 * 
 * @author slx
 * @date 2011 4 7 16:07:56
 * @version 1.0
 */
public class TestBeanUtil {

	private static AbstractApplicationContext ctx = null;
	private static String[] locations = {};
	
	private static ArrayList<String> filepaths = new ArrayList<String>(){{
		add("applicationcontext-mongo.xml");
		add("springcontext-test.xml");
		add("/oecp/platform/org/service/applicationcontext.xml");
		add("/oecp/platform/bcevent/service/applicationcontext.xml");
		add("/oecp/platform/bcfunction/service/applicationcontext.xml");
		}};
	
	private static void load(){
		locations = filepaths.toArray(locations);
    	ctx = new ClassPathXmlApplicationContext(locations);   
	}
	
	public static void addSpringContextFile(String filePath){
		filepaths.add(filePath);
	}
	
	/**
	 * 得到Spring应用上下文
	 * @author slx
	 * @date 2011 4 7 16:08:17
	 * @modifyNote
	 * @return
	 */
	public static AbstractApplicationContext getApplicationContext(){
		if(ctx == null){
			load();
		}
		return ctx;
	}
	
	public static Object getBean(String name){
		return getApplicationContext().getBean(name);
	}
	
	public static <T> T getBean(Class<T> beanType){
		return getApplicationContext().getBean(beanType);
	}
	
	public static <T> T getBean(String name,Class<T> beanType){
		return getApplicationContext().getBean(name,beanType);
	}
	
	public static void destroy(){
		if(ctx!=null)
			ctx.destroy();
	}
}
