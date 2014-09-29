package oecp.framework.util;



/**
 * 
* 获取文件路径
* @author hailang
* @date 2011-7-25 下午04:53:52
*
 */
public class Path {
	
	/**
	 * 
	* @author hailang
	* @date 2011-7-25 下午04:54:52
	* @Title: getClassPath
	* @return
	 */
	public static String getProjectPath() {
		String str = Path.class.getResource("/").toString().substring(6);
		return str.substring(0, str.indexOf("WEB-INF"));
		
	}
	
}
