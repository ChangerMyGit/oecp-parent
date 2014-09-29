package oecp.platform.maindata.service;

import java.util.List;

import oecp.framework.exception.BizException;
import oecp.framework.service.BaseService;
import oecp.platform.maindata.eo.MDResource;
import oecp.platform.maindata.eo.MDResourceField;

/**
 * 主数据资源类型服务接口
 * 
 * @author liujingtao
 * @date 2011-6-24 上午09:26:06
 * @version 1.0
 */
public interface MDResourceService extends BaseService<MDResource>{
	/**
	 * 查询相应编码的主数据资源
	 * @author liujt
	 * @date 2011-11-1下午4:15:38
	 * @return
	 */
	List<MDResource> queryMDResource(String code,int start, int limit);
	
	/**
	 * 得到所有的主数据资源类型
	 * @author liujingtao
	 * @date 2011 6 24 09:34:14
	 * @return
	 */
	List<MDResource> getAllMDResources();
	
	/**
	 * 得到指定实体类名的主数据资源
	 * @author liujt
	 * @date 2011-10-19下午4:39:23
	 * @param eoClassName
	 * @return
	 */
	MDResource getMdResourceByEOClassName(String eoClassName);
	
	/**
	 * 分页查找主数据资源
	 * @author liujt
	 * @date 2011-10-31上午9:26:09
	 * @param start
	 * @param limit
	 * @return
	 */
	List<MDResource> getMDResources(int start, int limit);
	
	/**
	 * 获得主数据资源总数
	 * @author liujt
	 * @date 2011-10-31上午9:27:15
	 * @return
	 */
	long getTotalCount();
	
	/**
	 * 保存主数据资源
	 * @author liujt
	 * @throws BizException 
	 * @date 2011-11-7上午11:51:37
	 */
	void saveMDResource(MDResource mdResource, List<MDResourceField> fields) throws BizException;
}
