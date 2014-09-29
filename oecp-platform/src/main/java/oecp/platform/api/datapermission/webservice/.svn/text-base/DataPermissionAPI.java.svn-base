/************************* 版权声明 *********************************
 *                                                                  *
 *                     版权所有：百洋软件                          				 	*
 *          Copyright (c) 2011 by www.oecp.cn                	*
 *                                                                  *
 ************************* 变更记录 *********************************
 *
 * 创建者：宋黎晓   创建日期： 2011 4 19
 * 备注：
 * 
 * 修改者：       修改日期：
 * 备注：
 * 
 */    

package oecp.platform.api.datapermission.webservice;

import javax.jws.WebParam;
import javax.jws.WebResult;
import javax.jws.WebService;

import org.apache.cxf.annotations.WSDLDocumentation;
import org.apache.cxf.annotations.WSDLDocumentationCollection;
import org.apache.cxf.annotations.WSDLDocumentation.Placement;

/**
 * 数据权限服务API
 * @author slx
 * @date 2011 4 19 09:08:07
 * @version 1.0
 */
@WebService(targetNamespace="http://www.oecp.cn",serviceName="dataPermissionAPI")
@WSDLDocumentationCollection(value={
		@WSDLDocumentation(placement=Placement.SERVICE,value="数据权限服务    "),
		@WSDLDocumentation(placement=Placement.TOP,value="数据权限服务,提供获取数据权限服务.版权归<a href='http://www.oecp.cn'>OECP社区</a>所有.   ")
	})
public interface DataPermissionAPI {
	
	/**
	 * 得到某功能下一个字段的数据权限控制SQL子查询条件
	 * @author slx
	 * @date 2011 4 19 09:10:04
	 * @modifyNote
	 * @param userID
	 * 		用户ID
	 * @param funcCode
	 * 		功能编号
	 * @param fieldName
	 * 		字段编号
	 * @return
	 * 		SQL查询条件控制语句
	 */
	@WebResult(name="dataPermissionSQL")
	@WSDLDocumentation("<p>获得用户在某个功能上指定字段的数据权限查询控制语句.</p><p>需要3个参数:</br>1 userID用户主键</br>2 funcCode 功能编号</br>3 fieldName字段编号.</p><p>返回值为SQL查询Where子句中的条件语句.</p>")
	public String getFieldDataPermissionSQL(
			@WebParam(name="userID")String userID, 
			@WebParam(name="orgId")String orgId,
			@WebParam(name="funcCode")String funcCode, 
			@WebParam(name="fieldName")String fieldName,
			@WebParam(name="asName")String asName);
	
	/**
	 * 得到某个功能下所有字段的数据权限控制SQL子查询条件
	 * @author slx
	 * @date 2011 4 19 09:10:04
	 * @modifyNote
	 * @param userID
	 * 		用户ID
	 * @param funcCode
	 * 		功能编号
	 * @return
	 * 		SQL查询条件控制语句
	 */
	@WebResult(name="dataPermissionSQL")
	@WSDLDocumentation("<p>获得用户在某个功能上所有字段的数据权限查询控制语句.</p><p>需要2个参数:1 userID用户主键</br>2 funcCode 功能编号.</p><p>返回值为SQL查询Where子句中的条件语句.</p>")
	String getDataPermissionSQL(@WebParam(name="userID")String userID, 
			@WebParam(name="funcCode")String funcCode);
}
