package oecp.framework.util;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;


/**
 * 读取Properties综合类
 * 
 * @author yongtree
 */
public class PropertiesUtil {

	/**
	 * 配置文件对象
	 */
	private Properties props = null;

	/**
	 * 构造函数 <br>
	 * 对classpath下的两个位置进行扫描：<br>
	 * 1、classpath根目录下 <br>
	 * 2、classpath下的config目录下，该目录下的配置会覆盖根目录下的配置
	 * 
	 * @param fileName
	 *            配置文件名称
	 */
	public PropertiesUtil(String fileName) {
		props = new Properties();
		try {
			String root = this.getClass().getResource("/").getPath();
			File root_f = new File(root + fileName);
			if (root_f.exists()) {
				props.load(new FileInputStream(root_f));
			}
			File root_c = new File(root + "config/" + fileName);
			if (root_c.exists()) {
				props.load(new FileInputStream(root_c));
			}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * 根据key值读取配置的值
	 * 
	 * @author yongtree
	 * @param key
	 *            key值
	 * @return key 键对应的值
	 * @throws IOException
	 */
	public String readValue(String key) throws IOException {
		return props.getProperty(key);
	}

	/**
	 * 读取properties的全部信息 Jun 26, 2010 9:21:01 PM
	 * 
	 * @author yongtree
	 * @throws FileNotFoundException
	 *             配置文件没有找到
	 * @throws IOException
	 *             关闭资源文件，或者加载配置文件错误
	 * 
	 */
	public Map<String, String> readAllProperties()
			throws FileNotFoundException, IOException {
		// 保存所有的键值
		Map<String, String> map = new HashMap<String, String>();
		Enumeration en = props.propertyNames();
		while (en.hasMoreElements()) {
			String key = (String) en.nextElement();
			String Property = props.getProperty(key);
			map.put(key, Property);
		}
		return map;
	}
}
