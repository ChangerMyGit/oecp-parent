package oecp.framework.xml;

import org.dom4j.Document;

/**
 * 
 * 任何xml创建必须实现该接口
 * @author hailang
 * @date 2011-7-15 11:03:56
 * @version 1.0
 */
public interface ICreateXML {
	/**
	 * 
	 * @author 创建XML文件
	 * @date 2011-7-15 11:04:27
	 */
	public Document createXML();
}
