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

import javax.jws.WebParam;
import javax.jws.WebService;


import org.apache.cxf.annotations.WSDLDocumentation;
import org.springframework.stereotype.Service;

/**
 * 事件机制API实现
 * @author slx
 * @date 2011 4 18 15:39:53
 * @version 1.0
 */
@Service("BCEventAPI")
@WebService(targetNamespace="http://www.oecp.cn",serviceName="BCEventAPI")
//@WSDLDocumentationCollection(value={
//		@WSDLDocumentation(placement=Placement.SERVICE,value="业务事件服务"),
//		@WSDLDocumentation(placement=Placement.TOP,value="业务事件服务,提供业务事件的触发接口.版权归<a href='http://www.oecp.cn'>OECP社区</a>所有."),
//	})
public class BCEventAPIImpl implements BCEventAPI{

	@Override
	@WSDLDocumentation("<p>发起业务事件.</p><p>需要3个参数:</br>1 bcCode当前组件的编号</br>2 eventCode事件编号</br>3 dataXML事件源XML格式的数据</p>")
	public void fireBCEvent(
			@WebParam(name="bcCode,")String bcCode,
			@WebParam(name="eventCode")String eventCode,
			@WebParam(name="dataXML")String dataXML) {
		// TODO 触发事件
	}

}
