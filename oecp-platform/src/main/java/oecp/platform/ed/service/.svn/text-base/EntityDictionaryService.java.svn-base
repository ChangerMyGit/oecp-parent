/*
 * Copyright (c) 2012 OECP All Rights Reserved.                	
 * <a href="http://www.oecp.cn">OECP</a> 
*/                                                                
  

package oecp.platform.ed.service;

import java.util.List;
import java.util.Map;

import oecp.framework.entity.base.BaseEO;
import oecp.framework.service.BaseService;
import oecp.platform.ed.eo.EntityDictionary;

/** 
 *
 * @author wangliang  
 * @date 2012-3-19 下午4:46:02 
 * @version 1.0
 *  
 */
public interface EntityDictionaryService extends BaseService<EntityDictionary> {
	/**
	 * 根据表名 获取实体对象属性
	 * @author wangliang
	 * @date 2012-3-19下午5:18:04
	 * @param tableName
	 * @return
	 */
	public List<EntityDictionary> getEntityByTableName(String tableName);
	
	public Map<String,?> getEntityParams(BaseEO<?> eo) throws ClassNotFoundException, InstantiationException, IllegalAccessException;
}
