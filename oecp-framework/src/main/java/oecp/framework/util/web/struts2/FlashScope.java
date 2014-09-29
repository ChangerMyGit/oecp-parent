/************************* 版权声明 *********************************
 * 
 * 版权所有：百洋软件
 * Copyright (c) 2010 by Pearl Ocean.
 * 
 ************************* 变更记录 *********************************
 *
 * 创建者：yongtree   创建日期： 2010-3-30
 * 创建记录：创建类结构。
 * 
 * 修改者：       修改日期：
 * 修改记录：
 ************************* 随   笔 *********************************
 *
 * 这里可以写写感想，感慨，疑问什么的。
 * 
 ******************************************************************
 */

package oecp.framework.util.web.struts2;

import java.io.Serializable;
import java.util.Map;

/**
 * 
 * 这是一个flash作用域的接口，flash对象保存的信息只会保存到下一个请求， <br>
 * 当下一个请求结束后，上一个请求保存在该作用域的信息将自动被清除。 <br>
 * 该对象主要解决重定向后，信息丢失的问题。同时，由于采用自动清除机制， <br>
 * 也防止了因为程序人员管理不善，session不能及时清理的问题。
 * 
 * @author yongtree
 * @date 2010-3-30 上午08:53:25
 * @version 1.0
 */
public interface FlashScope extends Map, Serializable {
	
	public static final String FLASH_SCOPE = "com.posoft.web.servlet.FLASH_SCOPE";
	/**
	 * 设置一个flash作用域经过新的请求到下个状态。
	 */
	void next();

	/**
	 * 返回flash对象现有的状态，如果你不希望在下面的请求中使用包含的变量。
	 * 
	 * @return A map
	 */
	Map getNow();
}
