/**
 * oecp-platform - ExtUIViewParser.java
 * copyright 2009-2012 OECP www.oecp.cn
 * 创建人:slx	创建时间:2011-11-7上午10:43:24		版本:v1
 * ============================================
 * 修改人：			修改时间:					版本:
 * 修改原因:
 *
 * ============================================
 */
package oecp.platform.uiview.web.parser;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import oecp.platform.uiview.enums.ComponentType;
import oecp.platform.uiview.vo.UIComponentVO;

import org.springframework.stereotype.Component;

import freemarker.template.Configuration;
import freemarker.template.Template;

/**
 * Ext的视图解析器
 * @author slx
 * @date 2011-11-7
 */
@Component("extUIViewParser")
public class ExtUIViewParser implements UIViewParser {
	
	private Configuration cfg = new Configuration();

	@Override
	public String parserToSourceCode(UIComponentVO uivo) {
		if(uivo == null){
			return "{}";
		}
		cfg.setDefaultEncoding("UTF-8");
		try {
			InputStreamReader templateReader = new InputStreamReader(this.getClass().getResourceAsStream("/oecp/platform/uiview/web/parser/extview.flt"));
//			InputStreamReader templateReader = new InputStreamReader(new FileInputStream("E:/code/javaEEworkspace/oecp-parent/oecp-platform/src/main/config/oecp/platform/uiview/web/parser/extview.flt"));
			Template template = new Template("extproesser", templateReader ,cfg );
			StringWriter sw = new StringWriter();
			
			Map<String ,Object> param = new HashMap<String, Object>();
			param.put("vo", uivo);
			template.process(param, sw);
			return sw.toString();
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	public static void main(String[] args) {
		UIViewParser parser = new ExtUIViewParser();
		UIComponentVO vo = new UIComponentVO();
		vo.setType(ComponentType.Panel);
		vo.setTitle("测试");
		vo.setCancommit(true);
		
		UIComponentVO vo1 = new UIComponentVO();
		vo1.setType(ComponentType.Field);
		vo1.setTitle("字段1");
		vo1.setWidth(100);
		vo1.setHeight(50);
		
		UIComponentVO vo2 = new UIComponentVO();
		vo2.setType(ComponentType.Field);
		vo2.setTitle("字段2");
		List<UIComponentVO> vochildren = new ArrayList<UIComponentVO>();
		vochildren.add(vo1);
		vochildren.add(vo2);
		vo.setChildren(vochildren);
		String extcode = parser.parserToSourceCode(vo);
		System.out.println(extcode);
	}
}
