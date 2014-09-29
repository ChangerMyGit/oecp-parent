/*
 * Copyright (c) 2011 OECP All Rights Reserved.                	
 * <a href="http://www.oecp.cn">OECP</a> 
 */

package oecp.platform.api.datapermission;

import oecp.framework.exception.BizException;

/**
 * 数据权限SQL查询条件服务
 * 
 * @author liujt
 * @date 2011-8-23 下午02:27:16
 * @version 1.0
 */
public interface DataPermissionSQLService {
	/**
	 * 得到某个功能下某一个字段的数据权限控制SQL子查询条件
	 * 
	 * @author slx
	 * @date 2011 6 29 09:47:49
	 * @modifyNote
	 * @param userID
	 *            用户ID
	 * @param funcCode
	 *            功能编号
	 * @return SQL查询条件控制语句
	 * @throws BizException
	 */
	String getDataPermissionSQL(String userID, String orgId, String funcCode,
			String funFieldName, String asName) throws BizException;

	/**
	 * 得到某个功能下所有字段的数据权限控制SQL子查询条件
	 * 
	 * @author liujt
	 * @date 2011-10-10下午02:52:49
	 * @param userID
	 * @param funcCode
	 * @param orgId
	 * @return
	 * @throws BizException
	 */
	String getDataPermissionSQL(String userID, String orgId, String funcCode)
			throws BizException;
}
