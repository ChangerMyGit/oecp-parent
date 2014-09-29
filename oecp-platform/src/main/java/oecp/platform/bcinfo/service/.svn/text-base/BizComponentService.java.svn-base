package oecp.platform.bcinfo.service;

import java.util.List;

import oecp.acbase.service.ArchivesBaseService;
import oecp.framework.exception.BizException;
import oecp.platform.bcinfo.eo.BizComponent;

/**
 * 
 *
 * @author lintao  
 * @date 2011-8-9 上午10:22:34 
 * @version 1.0
 *
 */
public interface BizComponentService  extends ArchivesBaseService<BizComponent>{
	/**
	 * 
	* 获取所有组件
	* @author lintao
	* @date 2011-8-8下午03:59:01
	* @param 
	* @return list
	*              所有 业务组件
	 */
	List<BizComponent> findAll();
	/**
	 * 
	* 保存业务组件详细信息
	* @author lintao
	* @date 2011-8-10下午02:15:20
	* @param 
	* @return
	 * @throws BizException 
	 */
	
	void saveComponentInfo(String id)throws BizException;
	/**
	 * 
	* 初始化组件
	* @author lintao
	* @date 2011-8-11下午02:07:56
	* @param id
	*              组件ID
	* @return 
	*                
	 */
	void initComponentInfo(String id)throws BizException;
	/**
	 * 
	* 对接业务组件
	* 依次调用服务、事件、依赖、功能注册的服务
	* @author lintao
	* @date 2011-8-12上午10:46:07
	* @param 
	* @return
	 */
	void connection(String id)throws BizException;
	
	/**
	 * 删除组件并级联删除其它相关信息
	* 
	* @author lintao
	* @date 2011-8-17上午11:00:34
	* @param  id
	*           组件ID
	* @return
	 */
	void deleteComponent(String id)throws BizException;
}
