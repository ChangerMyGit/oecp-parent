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

import javax.annotation.Resource;

import oecp.platform.api.datapermission.DataPermissionSQLService;

import org.springframework.stereotype.Service;

/**
 * 数据权限API实现
 * @author slx
 * @date 2011 4 19 09:14:30
 * @version 1.0
 */
@Service("dataPermissionAPI")
public class DataPermissionAPIImpl implements DataPermissionAPI {
	
	@Resource
	private DataPermissionSQLService dataPermissionSQLService;
	
	@Override
	public String getFieldDataPermissionSQL(String userID,String orgId, String funcCode, String fieldName,String asName) {
		try{
			String sql = dataPermissionSQLService.getDataPermissionSQL(userID, orgId, funcCode, fieldName, asName);
			System.out.println(sql);
			return sql;
		}catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}
	
	@Override
	public String getDataPermissionSQL(String userID, String funcCode) {
		// TODO Auto-generated method stub
		return null;
	}

	public DataPermissionSQLService getDataPermissionSQLService() {
		return dataPermissionSQLService;
	}

	public void setDataPermissionSQLService(
			DataPermissionSQLService dataPermissionSQLService) {
		this.dataPermissionSQLService = dataPermissionSQLService;
	}

}
