/*
 * Copyright (c) 2011 OECP All Rights Reserved.                	
 * <a href="http://www.oecp.cn">OECP</a> 
*/                                                                
  

package oecp.platform.datapermission.web;

import javax.annotation.Resource;

import oecp.framework.exception.BizException;
import oecp.platform.api.datapermission.DataPermissionSQLService;
import oecp.platform.web.BasePlatformAction;

import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.Namespace;
import org.apache.struts2.convention.annotation.ParentPackage;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

/** 
 *
 * @author liujt  
 * @date 2011-8-29 下午03:21:10 
 * @version 1.0
 *  
 */
@Controller("TestSQLAction")
@Scope("prototype")
@ParentPackage("default-package")
@Namespace("/datapermission")
public class TestSQLAction extends BasePlatformAction {
	private static final long serialVersionUID = -745705305676653216L;
	
	@Resource
	private DataPermissionSQLService dataPermissionSQLService;
	
	@Action("testSQL")
	public String testSQL() throws BizException{
//		String hql = dataPermissionSQLService.getDataPermissionSQL("admin", "0000", "SYS_0019", "o.sshpin",null);
//		String hql = dataPermissionSQLService.getDataPermissionSQL("admin", "0000", "SYS_0019");
		String hql = dataPermissionSQLService.getDataPermissionSQL("admin", "0000", "bill_code");
		System.out.println(hql);
		return SUCCESS;
	}

	public DataPermissionSQLService getDataPermissionSQLService() {
		return dataPermissionSQLService;
	}

	public void setDataPermissionSQLService(
			DataPermissionSQLService dataPermissionSQLService) {
		this.dataPermissionSQLService = dataPermissionSQLService;
	}
	
	
}
