package oecp.framework.service;

import java.io.Serializable;
import java.util.List;

import oecp.framework.dao.QueryObject;
import oecp.framework.exception.BizException;

/**
 * 基础服务接口
 * 
 * @author slx
 * @date 2011 4 7 10:31:09
 * @version 1.0
 * @param <T>
 */
public interface BaseService<T> {
	
	/**
	 * 创建对象
	 * @author slx
	 * @date 2011 4 7 10:47:16
	 * @modifyNote
	 * @param t
	 * @throws BizException
	 */
	public void create(T t) throws BizException;

	/**
	 * 保存实体,新实体则创建,老实体则更新.
	 * @author slx
	 * @date 2011 4 7 10:47:29
	 * @modifyNote
	 * @param t
	 * @throws BizException
	 */
	public void save(T t) throws BizException;

	/**
	 * 按照主键列表批量删除一组实体
	 * @author slx
	 * @date 2011 4 7 10:48:47
	 * @modifyNote
	 * @param ids
	 * @throws BizException
	 */
	public void delete(Serializable[] ids) throws BizException;
	
	/**
	 * 删除指定主键的实体
	 * @author slx
	 * @date 2011 4 7 10:49:09
	 * @modifyNote
	 * @param id
	 * @throws BizException
	 */
	public void delete(Serializable id) throws BizException;
	
	/**
	 * 按照查询对象中的条件进行查询
	 * @author slx
	 * @date 2011 4 7 10:49:21
	 * @modifyNote
	 * @param queryObj
	 * @return
	 */
	public List<T> query(QueryObject queryObj) throws BizException;
	
	/**
	 * 按照id查询单个实体
	 * @author slx
	 * @date 2011 4 7 10:49:39
	 * @modifyNote
	 * @param id
	 * @return
	 */
	public T find(Serializable id) throws BizException;
	
	/**
	 * 按照id查询单个实体,并且一并加载所有懒加载字段.
	 * @author slx
	 * @date 2011 4 7 10:53:16
	 * @modifyNote
	 * @param id
	 * @return
	 * @throws BizException
	 */
	public T find_full(Serializable id)throws BizException;
}
