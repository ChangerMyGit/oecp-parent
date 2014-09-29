package oecp.platform.notice.service;

import java.util.List;

import oecp.framework.dao.QueryCondition;
import oecp.framework.dao.QueryResult;
import oecp.framework.exception.BizException;
import oecp.framework.service.BaseService;
import oecp.platform.notice.eo.NoticeEO;
import oecp.platform.org.eo.Organization;

public interface NoticeService extends BaseService<NoticeEO> {
	/**
	 * 列表数据
	 * 
	 * @author YangTao
	 * @date 2012-3-6下午04:28:03
	 * @param conditions
	 * @param start
	 * @param limit
	 * @return
	 */
	public QueryResult<NoticeEO> list(List<QueryCondition> conditions,
			int start, int limit);

	/**
	 * 获取OecpTask
	 * 
	 * @author YangTao
	 * @date 2012-3-8上午10:45:01
	 * @param warnId
	 * @return
	 */
	public NoticeEO getNoticeById(String bulletinId) throws BizException;

	/**
	 * @author liyanchao
	 * @date 2014年4月22日上午10:42:14
	 * @param orgid
	 * @return
	 */
	Organization getChildOrgs(String orgid);

	/**
	 * @author liyanchao
	 * @date 2014年4月28日上午10:10:18
	 * @param orgid
	 * @return
	 */
	Organization getCreateOrgs(String orgid);

	/**
	 * 启用或停用
	 * 
	 * @author YangTao
	 * @date 2012-3-16上午10:00:00
	 * @param warn
	 */
	//public void startOecpTask(String taskId, User user) throws BizException;


}
