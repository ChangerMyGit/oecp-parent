package oecp.platform.maindata.service;

import java.util.List;

import oecp.framework.service.BaseService;
import oecp.platform.maindata.eo.MDResourceField;

/**
 * 主数据资源列服务接口
 *
 * @author liujingtao
 * @date 2011-6-24 下午02:07:56
 * @version 1.0
 */
public interface MDResourceFieldService extends BaseService<MDResourceField>{
	/**
	 * 得到指定数据资源类型的所有主数据资源列
	 * @author liujingtao
	 * @date 2011 6 24 14:12:02
	 * @param mdResourceid
	 * @return
	 * 	 主数据资源列列表
	 */
	public List<MDResourceField> getMDResourceFields(String mdResourceid);
	
	/**
	 * 得到指定数据资源类型，且相关资源非空的 主数据资源列
	 * @author liujingtao
	 * @date 2011 6 24 14:12:02
	 * @param mdResourceid
	 * @return
	 * 	 主数据资源列列表
	 */
	public List<MDResourceField> getRelatedMDFields(String mdResourceid);
	
	/**
	 * 得到指定数据资源类型的是否显示的主数据资源列
	 * @author liujt
	 * @date 2011-9-2上午10:20:15
	 * @param mdResourceid
	 * @return
	 */
	public List<MDResourceField> getDisplayMDFields(String mdResourceid, Boolean isDisplay);
}
