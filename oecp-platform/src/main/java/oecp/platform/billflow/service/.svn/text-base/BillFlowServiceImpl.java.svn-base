/************************* 版权声明 *********************************
 *                                                                  *
 *                     版权所有：百洋软件                          				 	*
 *          Copyright (c) 2011 by www.oecp.cn                	*
 *                                                                  *
 ************************* 变更记录 *********************************
 *
 * 创建者：宋黎晓   创建日期： 2011-6-20
 * 备注：
 * 
 * 修改者：       修改日期：
 * 备注：
 * 
 */    

package oecp.platform.billflow.service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;

import javax.annotation.Resource;

import oecp.framework.dao.DAO;
import oecp.framework.dao.QLBuilder;
import oecp.framework.dao.QLType;
import oecp.framework.dao.QueryObject;
import oecp.framework.dao.QueryResult;
import oecp.framework.entity.base.BaseEO;
import oecp.framework.exception.BizException;
import oecp.framework.util.SpringContextUtil;
import oecp.framework.vo.base.SimpleDataVO;
import oecp.platform.api.datapermission.DataPermissionSQLService;
import oecp.platform.base.service.PlatformBaseServiceImpl;
import oecp.platform.bcfunction.eo.Function;
import oecp.platform.bcfunction.eo.FunctionUI;
import oecp.platform.bcfunction.service.BcFunctionService;
import oecp.platform.billflow.eo.BillFlowConfig;
import oecp.platform.billrelation.eo.BillRelationEO;
import oecp.platform.biztype.eo.BizType;
import oecp.platform.user.eo.User;

import org.apache.commons.lang.StringUtils;
import org.hibernate.Query;
import org.springframework.stereotype.Service;

/**
 * 单据流服务类。可用来获得单据流配置，获得单据前置查询结果等。
 * @author slx
 * @date 2011-6-20 下午01:59:44
 * @version 1.0
 */
@Service("billFlowService")
public class BillFlowServiceImpl extends PlatformBaseServiceImpl<BillFlowConfig> implements BillFlowService {

	protected static final String where_BizAndFuncode = " o.bizType.id=? AND o.function.code=? ";
	
	protected static final String where_BizAndNextFuncode = " o.bizType.id=? AND o.nextBillFunction.code=? ";
	
	protected static final String where_BizAndPreFuncode = " o.bizType.id=? AND o.preBillFunction.code=?  ";
	// 组件功能接口类
	@Resource
	private BcFunctionService bcFunctionService;
	@Resource
	private DataPermissionSQLService dataPermissionSQLService;
	/**
	 * 根据业务类型和功能编号得到单据配置信息
	 */
	@Override
	public BillFlowConfig getBillFlowConfig(String bizTypeID, String functionCode) {
		BillFlowConfig config = getDao().findByWhere(BillFlowConfig.class, where_BizAndFuncode, new Object[]{bizTypeID,functionCode});
		if(config!=null)
			config.loadLazyAttributes();
		return config;
	}
	
	/**
	 * 获取前置单据
	 */
	@Override
	public QueryResult<HashMap> getPreDatas(String bizTypeID, String functionID, QueryObject qyobj,int start,int limit) throws BizException {
		return getPreDatas(bizTypeID,functionID,qyobj.getWhereQL(),qyobj.getQueryParams(),start, limit);
	}
	/**
	 * 获取前置单据查询dao实例
	 * @author songlixiao
	 * @date 2013-11-29上午11:11:37
	 * @param daoname
	 * @return
	 */
	protected DAO getQueryDao(String daoname){
		return (DAO) SpringContextUtil.getBean(daoname);
	}
	
	/**
	 * 获取前置单据
	 */
	@Override
	public QueryResult<HashMap> getPreDatas(String bizTypeID, String functionCode ,String customerWhere, Object[] prams,int start,int limit) throws BizException {
		List<Object[]> rs = getDao().queryFieldValues(BillFlowConfig.class, new String[]{"preQuerySQL","qlType","daobeanname"}, where_BizAndFuncode, new Object[]{bizTypeID,functionCode});
		QueryResult<HashMap> qr = new QueryResult<HashMap>();
		if(rs != null && rs.size()==1 ){
			String ql = (String)rs.get(0)[0];
			if(!ql.contains("where")&&!ql.contains("WHERE"))
				ql+=" WHERE 1=1 ";
			if(StringUtils.isNotEmpty(customerWhere))
				ql = ql.concat(" AND ").concat(customerWhere);
			//结果总数
			int totalSize = this.getQueryDao(rs.get(0)[2].toString()).queryVOs(ql, (QLType)rs.get(0)[1], prams,start,limit).size();
			//结果列表
			List<SimpleDataVO> list= this.getQueryDao(rs.get(0)[2].toString()).queryVOs(SimpleDataVO.class,ql, (QLType)rs.get(0)[1], prams,start,limit);
			if(list.size()!=0){
				SimpleDataVO sd = list.get(0);
				sd.getFieldNames();
			}
			//组装QueryResult
			List<HashMap> resultList =new ArrayList<HashMap>();
			for(SimpleDataVO sdv : list){
				HashMap<String, Object> values = new HashMap<String, Object>();
				String[] fields = sdv.getFieldNames();
				for(String field : fields){
					values.put(field, sdv.getValue(field));
				}
				resultList.add(values);
			}
			qr.setResultlist(resultList);
			qr.setTotalrecord(new Long(totalSize));
			return qr;
		}else if(rs.size() > 1){
			throw new BizException("单据流配置重复！");
		}
		throw new BizException("无匹配的业务单据流配置！");
	}
	
	
	/**
	 * 根据功能编号，组织活动业务类型列表
	 */
	@Override
	public List<BizType> getBizTypeByFunCode(User user,String functionCode, String orgID) {
		//数据权限
		String hql_where = "";
		try {
			hql_where = dataPermissionSQLService.getDataPermissionSQL(user.getId(), orgID, functionCode,"o.bizType","bt");
		} catch (BizException e) {
			e.printStackTrace();
		}
		if(StringUtils.isNotEmpty(hql_where))
			hql_where = " AND "+hql_where;
		else
			hql_where = "";
		System.out.println(hql_where);
		Query q = getDao().getHibernateSession().createQuery("SELECT bt FROM BillFlowConfig bfc INNER JOIN bfc.bizType bt WHERE bfc.function.code=? AND (bt.org.id = ? OR bt.shared=true)"+hql_where);
		new QLBuilder().setQueryParams(q, new Object[]{functionCode,orgID});
		
		return q.list();
	}

	/**
	 * 根据“功能”获得此功能所使用的dao。
	 * @author songlixiao
	 * @date 2013-12-2上午9:56:29
	 * @param func
	 * @return
	 */
	protected DAO getFunctionDAO(Function func){
		return (DAO)SpringContextUtil.getBean(func.getBc().getDaoName());
	}

	/**
	 * 查询某个单据的单据流中的历史信息
	 * 
	 * @author YangTao
	 * @date 2011-12-13上午11:04:53
	 * @param <T>
	 * @param functionCode
	 * @param billId
	 * @param seeDirection
	 * @return
	 */
	public <T extends BaseEO> QueryResult<HashMap> getCurrentBillHistory(String functionCode,String billId,String seeDirection)throws BizException{
		QueryResult<HashMap> qr = null;
		try {
			//根据功能获取当前实体
			Function function = bcFunctionService.getFunctionByCode(functionCode);
			String mainEntity = function.getMainEntity();
			if(StringUtils.isEmpty(mainEntity)){
				throw new BizException("该功能上没配置主实体信息");
			}
			//根据billId查询出当前的实体信息
			Class cuurentEntityClass = Class.forName(mainEntity);
			DAO dao = getFunctionDAO(function);
			T cuurentEntity = (T)dao.find(cuurentEntityClass, billId);
			//根据当前对象获取，生成当前对象的业务类型
			String  bizType = null;
			if(cuurentEntity.getAttributeValue("bizType")!=null){
				bizType = (String)cuurentEntity.getAttributeValue("bizType");
			}else{
				throw new BizException("该单据没有业务类型，查询不出上游或下游单据");
			}
			qr = this.queryBillHistory(bizType, functionCode, cuurentEntity,seeDirection);
		}  catch (Exception e) {
			e.printStackTrace();
			throw new BizException(e.getMessage());
		}
		return qr;
		
	}
	
	
	/**
	 * 查询历史单据信息
	 * 
	 * @author YangTao
	 * @date 2011-12-14上午11:43:46
	 * @param <T>
	 * @param bizType
	 * @param functionCode
	 * @param cuurentEntity
	 * @param seeDirection
	 * @return
	 * @throws BizException
	 */
	private <T extends BaseEO> QueryResult<HashMap> queryBillHistory(String bizType,String functionCode,T cuurentEntity,String seeDirection)throws BizException{
		QueryResult<HashMap> qr = new QueryResult<HashMap>();
		List<SimpleDataVO> list= new ArrayList<SimpleDataVO>();
		try {
			String sql = "";
			//当前单据保存的历史单据ID
			String historyBillId = null;
			String historyBillEntity = null;
			Function historyFunction = null;
			//单据的配置信息
			BillFlowConfig billFlowConfig = this.getBillFlowConfig(bizType,functionCode);
			List<BillFlowConfig> billFlowConfigs = this.getBillFlowConfigByDirectionFunction(bizType,functionCode,seeDirection);
			//上游单据
			if("UP".equals(seeDirection)){
				//1、 从中间表获取上游单据
				historyFunction = billFlowConfig.getPreBillFunction();
				if(historyFunction==null)
					throw new BizException("当前单据的业务类型没有配置上游单据，请联系管理员");
				historyBillEntity = historyFunction.getMainEntity();
				DAO functionDao = getFunctionDAO(historyFunction);
				List<BillRelationEO> relations = functionDao.queryByWhere(BillRelationEO.class," o.currentBillID = ? ",new Object[]{cuurentEntity.getId()});				
				if(relations==null || relations.size()==0)
					throw new BizException("当前单据的业务类型没有配置上游单据，请联系管理员");
				for (BillRelationEO relationEO : relations) {
					historyBillId = relationEO.getPreBillID();
					sql = "SELECT O.id as id,O.billsn as billsn,O.state as state,'"+historyFunction.getCode()+"' as functionCode,'"+historyFunction.getName()+"' as functionName FROM "+historyBillEntity+" O WHERE O.id='"+historyBillId+"'";
					list.addAll(this.queryVos(functionDao,sql));
				}
				if(billFlowConfigs!=null&&billFlowConfigs.size()!=0){
					//2、上游单据中保存当前单据的ID
					for(BillFlowConfig bfc : billFlowConfigs){
						historyFunction = bfc.getFunction();
						historyBillEntity = historyFunction.getMainEntity();
						DAO dao = getFunctionDAO(historyFunction);
						sql = "SELECT O.id as id,O.billsn as billsn,O.state as state,'"+historyFunction.getCode()+"' as functionCode,'"+historyFunction.getName()+"' as functionName FROM "+historyBillEntity+" O WHERE O.nextBillID='"+cuurentEntity.getId()+"'";
						list.addAll(this.queryVos(dao,sql));
					}
				}
				if(StringUtils.isEmpty(sql.toString()))
					throw new BizException("没有上游单据");
			}else{//下游单据
				//1、当前单据中保存下游单据的ID
				if(cuurentEntity.getAttributeValue("nextBillID")!=null){
					historyBillId = (String)cuurentEntity.getAttributeValue("nextBillID");
					historyFunction = billFlowConfig.getNextBillFunction();
					if(historyFunction==null)
						throw new BizException("当前单据的业务类型没有配置下游单据，请联系管理员");
					historyBillEntity = historyFunction.getMainEntity();
					DAO dao = getFunctionDAO(historyFunction);
					sql = "SELECT O.id as id,O.billsn as billsn,O.state as state,'"+historyFunction.getCode()+"' as functionCode,'"+historyFunction.getName()+"' as functionName FROM "+historyBillEntity+" O WHERE O.id='"+historyBillId+"'";
					list.addAll(this.queryVos(dao,sql));
				}
				if(billFlowConfigs!=null&&billFlowConfigs.size()!=0){
					//2、下游单据中保存当前单据的ID
					for(BillFlowConfig bfc : billFlowConfigs){
						historyFunction = bfc.getFunction();
						historyBillEntity = historyFunction.getMainEntity();
						DAO dao = getFunctionDAO(historyFunction);
						sql = "SELECT O.id as id,O.billsn as billsn,O.state as state,'"+historyFunction.getCode()+"' as functionCode,'"+historyFunction.getName()+"' as functionName FROM "+historyBillEntity+" O WHERE O.preBillID='"+cuurentEntity.getId()+"'";
						list.addAll(this.queryVos(dao,sql));
					}
				}
				if(StringUtils.isEmpty(sql.toString()))
					throw new BizException("没有下游单据");
			}
			
			//结果列表
			if(list.size()==0){
				if("UP".equals(seeDirection)){
					throw new BizException("没有上游单据");
				}else{
					throw new BizException("没有下游单据");
				}
			}
			//去重
			HashSet<SimpleDataVO> set = new HashSet<SimpleDataVO>();
			set.addAll(list);
			list = Arrays.asList(set.toArray(new SimpleDataVO[0]));
			//组装QueryResult
			List<HashMap> resultList =new ArrayList<HashMap>();
			for(SimpleDataVO sdv : list){
				HashMap<String, Object> values = new HashMap<String, Object>();
				String[] fields = sdv.getFieldNames();
				for(String field : fields){
					values.put(field, sdv.getValue(field));
				}
				resultList.add(values);
			}
			qr.setResultlist(resultList);
			qr.setTotalrecord(new Long(list.size()));
		} catch (Exception e) {
			e.printStackTrace();
			throw new BizException(e.getMessage());
		}
		return qr;
	}
	
	/**
	 * 
	 * 根据方向获取BillFlowConfig
	 * @author YangTao
	 * @date 2011-12-21上午10:51:44
	 * @param bizTypeID
	 * @param FunctionCode
	 * @param seeDirection
	 * @return
	 */
	private List<BillFlowConfig> getBillFlowConfigByDirectionFunction(String bizTypeID, String FunctionCode,String seeDirection){
		List<BillFlowConfig> list = new ArrayList();
		if("UP".equals(seeDirection)){
			list = getDao().queryByWhere(BillFlowConfig.class, where_BizAndNextFuncode, new Object[]{bizTypeID,FunctionCode});
		}else{
			list = getDao().queryByWhere(BillFlowConfig.class, where_BizAndPreFuncode, new Object[]{bizTypeID,FunctionCode});
		}
		return list;
	}
	
	/**
	 * 
	 * 根据hql查询，返回SimpleDataVO结果
	 * @author YangTao
	 * @date 2011-12-21上午10:51:06
	 * @param hql
	 * @return
	 */
	private List<SimpleDataVO> queryVos(DAO dao,String hql){
		List<SimpleDataVO> list= dao.queryVOs(SimpleDataVO.class, hql, QLType.HQL, null,-1,-1);
		if(list.size()!=0){
			for(SimpleDataVO sd : list){
				String billURL = "";
				List<FunctionUI> li = this.getDao().queryByWhere(FunctionUI.class, "o.function.code=? and o.isDefaultForProcess=?", new Object[]{sd.getValue("functionCode"),true});
				if(li.size()!=0)
					billURL = li.get(0).getSign();
				sd.getFieldNames();
				sd.setValue("functionCode", sd.getValue("functionCode"));
				sd.setValue("functionName", sd.getValue("functionName"));
				sd.setValue("billURL", billURL);
			}
		}
		return list;
	}

	public BcFunctionService getBcFunctionService() {
		return bcFunctionService;
	}

	public void setBcFunctionService(BcFunctionService bcFunctionService) {
		this.bcFunctionService = bcFunctionService;
	}

	public DataPermissionSQLService getDataPermissionSQLService() {
		return dataPermissionSQLService;
	}

	public void setDataPermissionSQLService(
			DataPermissionSQLService dataPermissionSQLService) {
		this.dataPermissionSQLService = dataPermissionSQLService;
	}
	
	
}
