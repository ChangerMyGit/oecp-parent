/*
 * Copyright (c) 2011 OECP All Rights Reserved.                	
 * <a href="http://www.oecp.cn">OECP</a> 
 */

package oecp.bcbase.service;

import java.io.Serializable;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;

import javax.annotation.Resource;

import oecp.bcbase.enums.BillState;
import oecp.bcbase.eo.BaseBillEO;
import oecp.bcbase.eo.BaseBizBillEO;
import oecp.bcbase.eo.BaseBizDetailBillEO;
import oecp.bcbase.eo.annotations.BillItems;
import oecp.bcbase.utils.BizServiceHelper;
import oecp.framework.dao.QueryCondition;
import oecp.framework.dao.QueryResult;
import oecp.framework.entity.base.BaseEO;
import oecp.framework.exception.BizException;
import oecp.framework.util.GenericsUtils;
import oecp.framework.vo.base.SimpleDataVO;
import oecp.platform.api.billsn.BillSNGetterService;
import oecp.platform.api.bpm.ExecutionAPIService;
import oecp.platform.api.bpm.ExecutionResult;
import oecp.platform.api.datapermission.DataPermissionSQLService;
import oecp.platform.base.service.PlatformBaseServiceImpl;
import oecp.platform.billflow.eo.BillFlowConfig;
import oecp.platform.billflow.itf.BillCreaterToNext;
import oecp.platform.billflow.itf.BillPreWriteBacker;
import oecp.platform.billflow.service.BillFlowService;
import oecp.platform.billrelation.eo.BillRelationEO;
import oecp.platform.billrelation.eo.DetailBillRelationEO;
import oecp.platform.biztype.eo.BizType;
import oecp.platform.bpm.bizservice.BizServiceForBpm;
import oecp.platform.bpm.enums.VirProcessInstanceState;
import oecp.platform.org.eo.Organization;
import oecp.platform.user.eo.User;

import org.apache.commons.lang.StringUtils;

/**
 * 业务服务基础父类
 * 
 * @author slx
 * @date 2011-12-26
 */
public abstract class BizBaseServiceImpl<T extends BaseBillEO> extends PlatformBaseServiceImpl<T> implements BizBaseService<T>,BizServiceForBpm{

	/** 数据权限类 **/
	@Resource(name = "dataPermissionSQLService")
	protected DataPermissionSQLService dataPerService;
	public void setDataPerService(DataPermissionSQLService dataPerService) {
		this.dataPerService = dataPerService;
	}

	/** bpm服务类 **/
	@Resource(name = "executionAPIService")
	protected ExecutionAPIService executionAPIService;
	public void setExecutionAPIService(ExecutionAPIService executionAPIService) {
		this.executionAPIService = executionAPIService;
	}

	/** 单据号生成服务类 **/
	@Resource(name = "billSNGetterService")
	protected BillSNGetterService snService;
	public void setSnService(BillSNGetterService snService) {
		this.snService = snService;
	}
	/** 单据流服务类 **/
	@Resource(name="billFlowService")
	protected BillFlowService billFlowService;
	public void setBillFlowService(BillFlowService billFlowService) {
		this.billFlowService = billFlowService;
	}

	BizServiceHelper helper = new BizServiceHelper();
	
	@Override
	public QueryResult<T> query(String userID, String functionCode, String orgId, List<QueryCondition> conditions, int startRow, int rows) throws BizException {
		// 数据权限
		String data_sql = dataPerService.getDataPermissionSQL(userID, orgId, functionCode);
		String query = buildQueryHQL(conditions, data_sql);
		query = query.concat(StringUtils.isEmpty(data_sql) ? "" : " WHERE " + data_sql);
		// 使用DAO的分页查询查询出结果
		return getDao().getScrollData(query, conditions, startRow, rows);
	}

	/**
	 * 构建并返回查询表单的hql语句
	 * 
	 * @author slx
	 * @date 2011-12-23
	 * @return
	 * @throws BizException
	 */
	protected String buildQueryHQL(List<QueryCondition> conditions, String data_sql) throws BizException {
		// 从实体中得到引用的list( 只需要List添加到join语句中，其他的用[主表.对象名]就可以查询 )
		Class<T> t = GenericsUtils.getSuperClassGenricType(this.getClass());
		BaseEO<?> eo = null;
		try {
			eo = t.newInstance();
			StringBuffer sf_hql = new StringBuffer("SELECT o FROM ").append(t.getName()).append(" o ");
			String[] attrnames = eo.getAttributeNames();
			for (int i = 0; i < attrnames.length; i++) {
				try {
					Class<?> fc = eo.getAttributeType(attrnames[i]);
					if (Collection.class.isAssignableFrom(fc)) {
						// 检查是否需要拼接这个子表到JOIN中，如果查询条件和数据权限的sql中出现过，则拼接。
						String asname = "o_".concat(attrnames[i]); // 子表别名如：
																	// eo.children
																	// 别名为eo_children
						boolean needtable = false;
						needtable = (data_sql != null && data_sql.indexOf(asname) > -1);
						if (!needtable) {
							if (conditions != null && conditions.size() > 0) {
								for (QueryCondition qcon : conditions) {
									if (qcon.getValue() != null && qcon.getField().indexOf(asname) > -1) {
										needtable = true;
										break;
									}
								}
							}
						}
						if (needtable) {
							// 将其拼接成HQL的JOIN查询语句
							sf_hql.append(" LEFT JOIN o.").append(attrnames[i]).append(" ").append(asname);
						}
					}
				} catch (NoSuchFieldException e) {
					throw new BizException(e);
				}
			}
			return sf_hql.toString();
		} catch (Exception e) {
			throw new BizException(e);
		}
	}

	@Override
	public void saveBill(T bill,User operator, String functionCode) throws BizException {
		// 清掉主表空id 和子表空id
		if (bill.getId() == null || StringUtils.isEmpty(bill.getId().toString().trim()) || "null".equalsIgnoreCase(bill.getId())) {
			bill.setId(null);
			bill.setCreater(operator.getId());
			bill.setCreatedate(new Date());
			bill.setChanger(operator.getId());
			bill.setChangedate(bill.getCreatedate());
		}else{
			bill.setChanger(operator.getId());
			bill.setChangedate(new Date());
		}
		filtBill(bill);
		if (bill.getId() == null) {
			// 新单子设置单据号
			bill.setBillsn(snService.getBillCode(bill, functionCode));
		}
		beforeBillSave(bill);
		validateBill(bill);
		
		if(bill instanceof BaseBizBillEO){
			if(((BaseBizBillEO) bill).getBizType()!=null){//有业务类型
				BillFlowConfig config = null;
				config = billFlowService.getBillFlowConfig(((BaseBizBillEO) bill).getBizType(), functionCode);
				//单据保存前特殊校验
				billFlowValidate(bill,config);
				if(bill.getState() == null || BillState.TEMPORARY.equals(bill.getState())){
					bill.setState(BillState.EDIT);
				}
				save(bill);
				// 写入中间表.....
				if(bill instanceof BaseBizBillEO)
				   saveBillRelation(bill);				
				//单据保存后回写上游单据
				billFlowWriteBack(bill, config, operator);
			}else{//无业务类型
				if(bill.getState() == null || BillState.TEMPORARY.equals(bill.getState())){
					bill.setState(BillState.EDIT);
				}
				save(bill);
				// 写入中间表.....
				if(bill instanceof BaseBizBillEO)
				   saveBillRelation(bill);
			}

		}
		afterBillSave(bill);
	}

	
    // 重写查找单据方法 获取中间表
	@Override
	public T find_full(Serializable id) throws BizException {
		T t = super.find_full(id);
		// 获取主单据的对应关系
		if(t instanceof BaseBizBillEO){
			List<String> relations = new ArrayList<String>();
			List<BillRelationEO> relationsEO = getDao().queryByWhere(BillRelationEO.class,
					" o.currentBillID = ?  ", new Object[] { t.getId() });
			for(BillRelationEO relationEO : relationsEO){
				relations.add(relationEO.getPreBillID());
			}
			((BaseBizBillEO)t).setRelations(relations);
			// 获取明细单据的对应关系
			try {
				List details = getBillDetails(t.getClass(), t);
				// 如果存在明细
				if(details!=null){
					for (int i = 0; i < details.size(); i++) {
						BaseBizDetailBillEO detailBillEO = (BaseBizDetailBillEO) details.get(i);
						List<DetailBillRelationEO> detailRelationsEO = getDao().queryByWhere(DetailBillRelationEO.class,
										" o.currentBillID = ?  and  o.currentBillDetailID = ?",
										new Object[] { t.getId(),detailBillEO.getId() });
						List<String[]> detailRelations = new ArrayList<String[]>();
						for (DetailBillRelationEO eo : detailRelationsEO) {
							detailRelations.add(new String[] { eo.getPreBillID(),
									eo.getPreBillDetailID(),
									eo.getPreBillDetailQty().toString(),
									eo.getCurrentBillDetailQty().toString() });
						}
						detailBillEO.setRelations(detailRelations);
					}
				}
			} catch (Exception e) {
				throw new RuntimeException(e);
			}
		}
		return t;
	}
	
	
	/**
	 * 写入中间表 (主单据对照，明细单据对照)
	 * @author luanyoubo
	 * @date 2014年1月22日下午3:06:29
	 * @param bill
	 * @throws BizException 
	 */
	private void saveBillRelation(T bill) {
		// 如果存在上下有单据关系
		List<String> relations = ((BaseBizBillEO)bill).getRelations();
		if (relations != null && relations.size() > 0) {
			// 保存主单据的关系对照 首先删除之前的单据对照关系
			getDao().deleteByWhere(BillRelationEO.class, " o.currentBillID = ? ", new Object[] { bill.getId() });
			for(String preBillID:relations){
				BillRelationEO billRelationEO = new BillRelationEO();
				billRelationEO.setCurrentBillID(bill.getId());
				billRelationEO.setPreBillID(preBillID);
				getDao().create(billRelationEO);
			}
			
			try {
				List details = getBillDetails(bill.getClass(), bill);
				for (int i = 0; i < details.size(); i++) {
					BaseBizDetailBillEO detailBillEO = (BaseBizDetailBillEO) details.get(i);
					List<String[]> detailRelations = detailBillEO.getRelations();
					if (detailRelations != null && detailRelations.size() > 0){
						// 保存明细单据的关系对照 首先删除之前的单据对照关系
						getDao().deleteByWhere(DetailBillRelationEO.class, 
								" o.currentBillID = ? and o.currentBillDetailID = ?", 
								new Object[] { bill.getId() , detailBillEO.getId() });
						for (String[] args : detailRelations) {
							String[] strs = args[0].split(",");
							if(strs.length>1){ // 防止缺少明细对照关系
								DetailBillRelationEO detailBillRelationEO = new DetailBillRelationEO();
								detailBillRelationEO.setPreBillID(strs[0]);
								detailBillRelationEO.setPreBillDetailID(strs[1]);
								detailBillRelationEO.setPreBillDetailQty(Long.valueOf(strs[2]));
								detailBillRelationEO.setCurrentBillDetailQty(Long.valueOf(strs[3]));
								detailBillRelationEO.setCurrentBillDetailID(detailBillEO.getId());
								detailBillRelationEO.setCurrentBillID(bill.getId());
	                            getDao().create(detailBillRelationEO);  
							}
						}
					}
				}
			} catch (Exception e) {
				throw new RuntimeException(e);
			}
		}
	}
	
	// 获取单据明细对象类表
	private List getBillDetails(Class clazz,T bill) throws Exception{
		Method[] methods = clazz.getMethods();
		for (Method method : methods) {
			if (method.getAnnotation(BillItems.class) != null
					&& List.class == (method.getReturnType())) {
				List details = (List) method.invoke(bill,new Object[] {});
				return details;
			}
		}
		return null;
	}
	
	/**
	 * 单据流中的单据保存前特殊校验
	 * @author slx
	 * @date 2011-12-28
	 * @param bill
	 * @param config
	 * @throws BizException
	 */
	protected void billFlowValidate(T bill,BillFlowConfig config) throws BizException{
		if(config != null){
			String fromPreCheckClassName = config.getBillCreaterFromPreCheck();
			if(StringUtils.isNotEmpty(fromPreCheckClassName)){
				try {
					helper.reflectInvokeMethod(fromPreCheckClassName, "beforeSaveCheckBill", new Class[]{BaseBizBillEO.class}, new Object[]{bill});
				} catch (Exception e) {
					e.printStackTrace();
					throw new BizException("调用保存之前校验方法进行校验失败");
				}
			}
		}
	}
	
	/**
	 * 单据流中的单据保存后回写上游单据
	 * @author slx
	 * @date 2011-12-28
	 * @param bill
	 * @param config
	 * @param operator
	 * @throws BizException
	 */
	protected void billFlowWriteBack(T bill,BillFlowConfig config,User operator) throws BizException{
		try{
			if (config != null) {
				BillPreWriteBacker billWriteBacker = config.getClassBillPreWriteBacker();
				if (billWriteBacker != null)
					billWriteBacker.writeBack((BaseBizBillEO)bill, operator.getId());
			}
		}  catch (Exception e) {
			e.printStackTrace();
			throw new BizException("调用上游单据回写器方法失败");
		}
	}
	
	/**
	 * 过滤整理单据内主子表对象之间的关系,剔除对象的“”id,更新单据默认值。
	 * @author slx
	 * @date 2011-12-28
	 * @param bill
	 * @return
	 */
	private T filtBill(T bill){
		String[] items = getItemFieldNames(bill);
		String billfield = null;
		for (int i = 0; i < items.length; i++) {
			Object itemvalue = bill.getAttributeValue(items[i]);
			if (itemvalue != null) {
				if (itemvalue instanceof BaseEO) {
					BaseEO<?> itemeo = (BaseEO<?>) itemvalue;
					billfield = getMainBillFieldName(itemeo);
					// 设置子表的主表对象关联
					if (billfield != null)
						itemeo.setAttributeValue(billfield, bill);
					if (itemeo.getId() == null || StringUtils.isEmpty(itemeo.getId().toString().trim())) {
						itemeo.setId(null);
					}
				} else if (itemvalue instanceof List) {
					List<BaseEO<?>> itemslist = (List<BaseEO<?>>) itemvalue;
					for (BaseEO<?> itemeo : itemslist) {
						billfield = getMainBillFieldName(itemeo);
						if (billfield != null)
							itemeo.setAttributeValue(billfield, bill);
						if (itemeo.getId() == null || StringUtils.isEmpty(itemeo.getId().toString().trim())) {
							itemeo.setId(null);
						}
					}
				}
			}
			billfield = null;
		}
		return bill;
	}

	protected abstract void validateBill(T bill) throws BizException;

	protected abstract void beforeBillSave(T bill) throws BizException;

	protected abstract void afterBillSave(T bill) throws BizException;

	/**
	 * 获得主表对象中的子表对象声明的属性名称
	 * 
	 * @author slx
	 * @date 2011-12-23
	 * @param bill
	 * @return
	 */
	protected String[] getItemFieldNames(T bill) {
		return BizServiceHelper.getItemFieldNames(bill);
	}

	/**
	 * 从子表中获得与主表关联的属性
	 * 
	 * @author slx
	 * @date 2011-12-23
	 * @param itemeo
	 * @return
	 */
	protected String getMainBillFieldName(BaseEO<?> itemeo) {
		return BizServiceHelper.getBillFieldInItem(itemeo);
	}

	@Override
	public void deleteBills(String[] ids, User operator, String functionCode) throws BizException {
		String bizType = null;
		for(String id : ids){
			T bill = find(id);
			//非编辑状态不能删除
			if(BillState.EDIT != bill.getState()){
				throw new BizException("所选单据的状态有为【".concat(bill.getEnumDescription("state")).concat("】，不允许删除！"));
			}
			
			beforeDelete(bill,operator);
			// 删除前调用后置单据回写器
			if(bizType!=null){
				BillFlowConfig config = billFlowService.getBillFlowConfig(bizType, functionCode);
				if (config != null) {
					BillPreWriteBacker billWriteBacker;
					try {
						billWriteBacker = config.getClassBillPreWriteBacker();
						if (billWriteBacker != null)
							billWriteBacker.onBillDelete(bill, operator.getId());
					} catch (Exception e) {
						throw new BizException(e);
					} 
				}
			}
		}
		
		this.delete(ids);
		deleteRelation(ids);
	}
	
	/**
	 * 删除单据上下游关系
	 * @author luanyoubo
	 * @date 2014年1月27日下午2:02:41
	 * @param ids
	 */
	private void deleteRelation(String[] ids) {
		for (String id : ids) {
            getDao().deleteByWhere(BillRelationEO.class, " o.currentBillID = ? ", new Object[] { id });
            getDao().deleteByWhere(DetailBillRelationEO.class, " o.currentBillID = ? ", new Object[] { id });
		}
	}

	protected abstract void beforeDelete(T bill,User operator);
	
	/**
	 * 单据提交流程
	 * 
	 * @author slx
	 * @date 2011-12-23
	 * @param bizPK
	 * @param operator
	 * @param org
	 * @param functionCode
	 * @throws BizException
	 */
	@Override
	public void commit(Serializable bizPK, User operator, Organization org, String functionCode) throws BizException {
		T bill = find(bizPK);
		beforeCommit(bill, operator, org);
		if (!operator.getId().equals(bill.getCreater())) {
			throw new BizException("您不是当前单据的制单人，只有制单人才能提交审批！");
		}
		if (!BillState.EDIT.equals(bill.getState())) {
			throw new BizException("当前单据不是编辑状态，不允许提交审批！");
		}
		ExecutionResult exeresult = null;
		try {
			exeresult = executionAPIService.startProcessInstance(functionCode, bill.getId().toString(), getBillInfoString(bill), org.getId(), operator.getId(), null);
		} catch (Exception e) {
			throw new BizException(e.getMessage());
		}
		afterCommit(bill, operator, org, exeresult);
	}
	
	protected abstract void beforeCommit(T bill,User operator,Organization org) throws BizException;
	protected abstract void afterCommit(T bill,User operator,Organization org,ExecutionResult exeresult) throws BizException;

	/**
	 * 获取单据的简单描述字符串，用于给流程代办任务显示。
	 * 
	 * @author slx
	 * @date 2011-12-23
	 * @param bill
	 * @return
	 */
	protected abstract String getBillInfoString(T bill);

	/**
	 * 单据流程状态变更 
	 * @author slx
	 * @date 2011-12-23
	 * @param bizPK
	 * @param bpmstate
	 */
	@Override
	public void changeBillState(Serializable bizPK, String functionCode, VirProcessInstanceState bpmstate) throws Exception {
		T bill = find(bizPK);
		BillFlowConfig config = null;
		if (bill instanceof BaseBizBillEO) {
			if(((BaseBizBillEO) bill).getBizType()!=null){//有业务类型
				config = billFlowService.getBillFlowConfig(((BaseBizBillEO) bill).getBizType(), functionCode);
			}
		}
		if (bpmstate == VirProcessInstanceState.END) {
			bill.setState(BillState.EFFECTIVE);
			if (config != null) {
				BillCreaterToNext billCreaterToNext = config.getClassBillCreaterToNext();
				if (billCreaterToNext != null)
					billCreaterToNext.createBill((BaseBizBillEO)bill, bill.getChanger(),functionCode);
			}
		} else if (bpmstate == VirProcessInstanceState.PERSON_END) {
			bill.setState(BillState.INVALID);
		} else if (bpmstate == VirProcessInstanceState.RUNNING) {
			bill.setState(BillState.BPMING);
		}
		if (config != null) {
			BillPreWriteBacker billWriteBacker = config.getClassBillPreWriteBacker();
			if (billWriteBacker != null)
				billWriteBacker.writeBack((BaseBizBillEO)bill, bill.getChanger());
		}
	}
	
	/**
	 * 从上游单据制单时获得数据
	 */
	public List<T> getFromPreDatas(String bizTypeID,String functionCode,User user,List<SimpleDataVO> simpleDataVOs) throws BizException{
		BillFlowConfig config = billFlowService.getBillFlowConfig(bizTypeID, functionCode);
		String fromPreClassName = config.getBillCreaterFromPre();
		List<T> list = null;
		try {
			if(StringUtils.isEmpty(fromPreClassName)){
				throw new BizException("后台没配置--当前单据创建器");
			}
			//调用当前单据创建器
	        Object[] ob = new Object[]{simpleDataVOs,user.getId(),config.getBizType()};
	        list = (List<T>)helper.reflectInvokeMethod(fromPreClassName, "createBill", new Class[]{List.class,String.class,BizType.class}, ob);
		} catch (Exception e) {
			e.printStackTrace();
			throw new BizException(e);
		}
		return list;
	}

}
