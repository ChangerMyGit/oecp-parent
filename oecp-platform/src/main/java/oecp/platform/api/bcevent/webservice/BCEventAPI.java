/************************* 版权声明 *********************************
 *                                                                  *
 *                     版权所有：百洋软件                          				 	*
 *          Copyright (c) 2011 by www.oecp.cn                	*
 *                                                                  *
 ************************* 变更记录 *********************************
 *
 * 创建者：宋黎晓   创建日期： 2011 4 18
 * 备注：
 * 
 * 修改者：       修改日期：
 * 备注：
 * 
 */    

package oecp.platform.api.bcevent.webservice;

/**
 * 业务事件API接口
 * @author slx
 * @date 2011 4 18 15:03:24
 * @version 1.0
 */
public interface BCEventAPI {

	/**
	 * 触发事件的服务
	 * @author slx
	 * @date 2011 4 18 15:24:58
	 * @modifyNote
	 * @param eventCode
	 * 		事件编号
	 * @param dataXML
	 * 		事件源数据的XML字符串
	 */
	public void fireBCEvent(String bcCode,String eventCode ,String dataXML);
}
