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

import java.util.HashMap;
import java.util.List;

import oecp.framework.dao.QueryObject;
import oecp.framework.dao.QueryResult;
import oecp.framework.entity.base.BaseEO;
import oecp.framework.exception.BizException;
import oecp.platform.billflow.eo.BillFlowConfig;
import oecp.platform.biztype.eo.BizType;
import oecp.platform.user.eo.User;

/**
 * 单据流服务
 * @author slx
 * @date 2011-6-20 下午01:58:19
 * @version 1.0
 */
public interface BillFlowService {
	
	/**
	 * 获得一个功能单据在某业务类型中的单据流配置情况。
	 * @author slx
	 * @date 2011-6-17 下午03:12:13
	 * @modifyNote
	 * @param bizTypeID
	 * @param functionCode
	 * @return
	 */
	public BillFlowConfig getBillFlowConfig(String bizTypeID,String functionCode);

	/**
	 * 根据单据的当前流程获得制单的前置数据
	 * @author slx
	 * @date 2011-6-20 下午02:05:11
	 * @modifyNote
	 * @param bizTypeID
	 * @param functionCode
	 * @param customerWhere
	 * 		使用时追加的where条件
	 * @param prams
	 * 		查询参数
	 * @return
	 */
	public QueryResult<HashMap> getPreDatas(String bizTypeID,String functionCode,String customerWhere , Object[] prams,int start,int limit) throws BizException;
	
	/**
	 * 使用查询对象查询
	 * @author slx
	 * @date 2011-6-28 下午07:22:33
	 * @modifyNote
	 * @param bizTypeID
	 * @param functionCode
	 * @param qyobj
	 * @return
	 * @throws BizException
	 */
	public QueryResult<HashMap> getPreDatas(String bizTypeID,String functionCode,QueryObject qyobj,int start,int limit) throws BizException;
	
	/**
	 * 获得一个单据上的业务类型
	 * @author slx
	 * @date 2011-6-20 下午03:55:29
	 * @modifyNote
	 * @param fucntionCode
	 * @param orgID
	 * @return
	 */
	public List<BizType> getBizTypeByFunCode(User user,String fucntionCode, String orgID);
	
		
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
	public <T extends BaseEO> QueryResult<HashMap> getCurrentBillHistory(String functionCode,String billId,String seeDirection)throws BizException;
}
