package oecp.bcbase.service;

import java.io.Serializable;
import java.util.List;

import oecp.framework.dao.QueryCondition;
import oecp.framework.dao.QueryResult;
import oecp.framework.entity.base.BaseEO;
import oecp.framework.exception.BizException;
import oecp.framework.service.BaseService;
import oecp.framework.vo.base.SimpleDataVO;
import oecp.platform.org.eo.Organization;
import oecp.platform.user.eo.User;
/**
 * 业务单据基础服务接口
 * 
 * @author slx
 * @date 2011-12-29
 */
public interface BizBaseService<T extends BaseEO<?>> extends BaseService<T> {

	public QueryResult<T> query(String userID, String functionCode, String orgId, List<QueryCondition> conditions, int startRow, int rows) throws BizException ;
	public void saveBill(T bill, User operator, String functionCode) throws BizException;
	public void deleteBills(String[] ids, User operator, String functionCode) throws BizException;
	public void commit(Serializable bizPK, User operator, Organization org, String functionCode) throws BizException;
	public List<T> getFromPreDatas(String bizTypeID,String functionCode,User user,List<SimpleDataVO> simpleDataVOs) throws BizException;
}
