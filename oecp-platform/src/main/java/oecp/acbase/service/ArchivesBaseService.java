/**
 * oecp-platform - ArchivesBaseService.java
 * copyright 2009-2012 OECP www.oecp.cn
 * 创建人:Administrator	创建时间:下午2:27:39		版本:v1
 * ============================================
 * 修改人：			修改时间:					版本:
 * 修改原因:
 *
 * ============================================
 */

package oecp.acbase.service;

import java.util.LinkedHashMap;
import java.util.List;

import oecp.acbase.eo.BaseMasArchivesEO;
import oecp.framework.dao.QueryCondition;
import oecp.framework.dao.QueryResult;
import oecp.framework.exception.BizException;
import oecp.framework.service.BaseService;
import oecp.platform.user.eo.User;

/**
 * 业务档案基础服务接口
 * @author luanyoubo
 * @date 2014年2月25日 下午2:27:39
 * @version 1.0
 * @param <T>
 * 
 */
public interface ArchivesBaseService<T extends BaseMasArchivesEO> extends BaseService<T> {
	// 查询档案
	public QueryResult<T> query(String userID, String functionCode,
			String orgId, List<QueryCondition> conditions, int startRow,int rows) throws BizException;
	
	public QueryResult<T> comboQuery(String userID, String functionCode,
			String orgId, T eo, int startRow,int rows,LinkedHashMap<String, String> orderby) throws BizException;
	
	// 新增 保存档案
	public void saveArchives(T archives, User operator, String functionCode) throws BizException;
	// 删除档案
	public void deleteArchives(String[] ids, User operator, String functionCode) throws BizException;

}
