/*
 * Copyright (c) 2011 OECP All Rights Reserved.                	
 * <a href="http://www.oecp.cn">http://www.oecp.cn</a>                                                                 
 */    

package oecp.platform.bpm.service;

import java.io.File;
import java.util.List;

import oecp.framework.dao.QueryCondition;
import oecp.framework.dao.QueryResult;
import oecp.framework.exception.BizException;
import oecp.framework.service.BaseService;
import oecp.platform.bpm.eo.BizConfigEo;
import oecp.platform.bpm.eo.BizProActivity;
import oecp.platform.bpm.eo.BizProDefinition;

/**
 *
 * @author yangtao
 * @date 2011-9-5上午10:33:10
 * @version 1.0
 */
public interface BizProDefinitionService  extends BaseService<BizProDefinition> {
	
	/**
	 * 
	 * 部署业务流程
	 * @author yangtao
	 * @date 2011-9-5上午10:35:36
	 * @param bizProDefinition
	 * @param zip
	 * @throws BizException
	 */
	public void deploy(BizProDefinition bizProDefinition, File zip) throws BizException;

	/**
	 * 
	 * 业务流程列表
	 * @author yangtao
	 * @date 2011-9-5上午11:04:49
	 * @param list
	 * @param start
	 * @param limit
	 * @return
	 */
	public QueryResult<BizProDefinition> list(List<QueryCondition> conditions,int start,int limit);
	
	/**
	 * 
	 * 获取某个业务流程下面的所有的节点
	 * @author yangtao
	 * @date 2011-9-5下午02:14:00
	 * @param bizProDefinitionId
	 * @return
	 */
	public List<BizProActivity> getBizProActivities(String bizProDefinitionId);
	
	/**
	 * 
	 * 获得某个业务流程节点的配置信息
	 * @author yangtao
	 * @date 2011-9-5下午02:22:02
	 * @param bizProActivityId
	 * @return
	 */
	public String[] getBizConfigEoByBizProActivityId(String bizProActivityId);
	
	/**
	 * 
	 * 保存配置信息
	 * @author yangtao
	 * @date 2011-9-5下午03:00:42
	 * @param bizProActivityId
	 * @param webServiceUrl
	 */
	public void saveConfigInfo(String bizProActivityId,String webServiceUrl);
	
	/**
	 * 
	 * 启动流程
	 * @author yangtao
	 * @date 2011-9-5下午03:49:00
	 * @param proDefId
	 */
	public void start(String proDefId);
	
}
