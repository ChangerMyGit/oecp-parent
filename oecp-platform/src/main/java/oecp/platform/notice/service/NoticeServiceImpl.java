package oecp.platform.notice.service;

import java.util.LinkedHashMap;
import java.util.List;

import oecp.framework.dao.QueryCondition;
import oecp.framework.dao.QueryResult;
import oecp.framework.exception.BizException;
import oecp.platform.base.service.PlatformBaseServiceImpl;
import oecp.platform.notice.eo.NoticeEO;
import oecp.platform.org.eo.Organization;

import org.springframework.stereotype.Service;


/**
 * 
 * 公告服务
 * @author lyc
 * 
 */
@Service("noticeService")
public class NoticeServiceImpl extends PlatformBaseServiceImpl<NoticeEO>
		implements NoticeService {

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
	@Override
	public QueryResult<NoticeEO> list(List<QueryCondition> conditions,
			int start, int limit) {
		LinkedHashMap<String, String> map = new LinkedHashMap<String, String>();
		map.put("createTime", "DESC");
		return this.getDao().getScrollData(NoticeEO.class, start, limit,
				conditions, map);
	}
	@Override
	public Organization getChildOrgs(String orgid) {
		Organization rootOrg = getDao().findByWhere(Organization.class, "o.id=?", new Object[]{orgid});
		loadChildOrgs(rootOrg);
		return rootOrg;
	}
	@Override
	public Organization getCreateOrgs(String orgid) {
		Organization rootOrg = getDao().findByWhere(Organization.class, "o.id=?", new Object[]{orgid});
		return rootOrg;
	}
	/**
	 * 加载子组织<B>(递归方法)</B>
	 * @author slx
	 * @date 2011 4 12 10:24:21
	 * @modifyNote
	 * @param org
	 */
	private void loadChildOrgs(Organization org){
		List<Organization> orgs = org.getChildOrgs();
		//如果自组织列表为空,则表示没有下级,跳出递归
		if(orgs == null){
			return;
		}
		// 循环递归取子组织利用Hibernate延迟加载机制加载出数据
		for (Organization o : orgs) {
			loadChildOrgs(o);
		}
	}

	/**
	 * 获取OecpTask
	 * 
	 * @author YangTao
	 * @date 2012-3-8上午10:45:01
	 * @param warnId
	 * @return
	 */
	@Override
	public NoticeEO getNoticeById(String bulletinId) throws BizException {
		NoticeEO noticeEO = this.find(bulletinId);
		noticeEO.loadLazyAttributes();
		return noticeEO;
	}

}
